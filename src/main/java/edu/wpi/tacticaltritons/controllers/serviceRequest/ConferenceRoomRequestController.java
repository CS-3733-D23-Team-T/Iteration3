package edu.wpi.tacticaltritons.controllers.serviceRequest;

import edu.wpi.tacticaltritons.App;
import edu.wpi.tacticaltritons.auth.UserSessionToken;
import edu.wpi.tacticaltritons.database.*;
import edu.wpi.tacticaltritons.navigation.Navigation;
import edu.wpi.tacticaltritons.navigation.Screen;
import io.github.palexdev.materialfx.controls.*;
import io.github.palexdev.materialfx.dialogs.MFXGenericDialog;
import io.github.palexdev.materialfx.dialogs.MFXGenericDialogBuilder;
import io.github.palexdev.materialfx.dialogs.MFXStageDialog;
import io.github.palexdev.materialfx.enums.ScrimPriority;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.HPos;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Group;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.util.Duration;
import net.kurobako.gesturefx.GesturePane;

import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import edu.wpi.tacticaltritons.styling.*;
public class ConferenceRoomRequestController {
    @FXML private MFXButton cancelButton;
    @FXML private MFXButton clearButton;
    @FXML private MFXButton submitButton;
    @FXML private MFXButton addPeopleButton;
    @FXML private MFXButton deletePeopleButton;

    @FXML private MFXFilterComboBox<String> roomSelection;
    @FXML private MFXFilterComboBox<String> peopleSearchBar;
    @FXML private MFXCheckListView<String> selectedAttendanceList;
    @FXML private MFXTextField firstName;
    @FXML private MFXTextField lastName;
    @FXML private MFXDatePicker date;

    ObservableList<String> conferenceRooms;
    ObservableList<String> attendances;
    ObservableList<String> selectedAttendances;

    int uploadExpectedSize;
    String uploadFirstName;
    String uploadLastName;

    String uploadConferenceRoom;
    Date uploadDate;
    String uploadAttendance = "";

    @FXML private GesturePane groundFloor;
    @FXML private ImageView groundFloorImage;
    @FXML private ImageView lowerLevel1Image;
    @FXML private ImageView lowerLevel2Image;
    @FXML private ImageView floor1Image;
    @FXML private ImageView floor2Image;
    @FXML private ImageView floor3Image;
    @FXML private MFXButton preview;
    @FXML private Group groundGroup;
    @FXML private Group L1Group;
    @FXML private Group L2Group;
    @FXML private Group floor1Group;
    @FXML private Group floor2Group;
    @FXML private Group floor3Group;
    @FXML private StackPane selectedFloorPane;
    @FXML private BorderPane basePane;

    public void initialize() throws SQLException{
        firstName.setText(UserSessionToken.getUser().getFirstname());
        lastName.setText(UserSessionToken.getUser().getLastname());

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM dd, yyyy");
        date.setValue(LocalDate.from(formatter.parse(formatter.format(LocalDateTime.now()))));

        lowerLevel1Image.setImage(App.lowerlevel1);
        lowerLevel2Image.setImage(App.lowerlevel2);
        groundFloorImage.setImage(App.groundfloor);
        floor1Image.setImage(App.firstfloor);
        floor2Image.setImage(App.secondfloor);
        floor3Image.setImage(App.thirdfloor);

        groundFloor.setVisible(true);
        floor1Image.setVisible(true);

        this.preview.setOnAction(event -> {
            clearAllNodes();
            new Circle();
            Circle circle;

            try {
                circle = drawCircle(DAOFacade.getNode(this.roomSelection.getText(),
                        Date.valueOf(LocalDate.now())).getXcoord(),
                        DAOFacade.getNode(this.roomSelection.getText(),
                        Date.valueOf(LocalDate.now())).getYcoord());
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

            String endFloor = null;
            try {
                endFloor = DAOFacade.getNode(this.roomSelection.getText(), Date.valueOf(LocalDate.now())).getFloor();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

            if (endFloor != null) {
                switch (endFloor) {
                    case "L1" -> {
                        L2Group.setVisible(false);
                        floor1Group.setVisible(false);
                        floor2Group.setVisible(false);
                        floor3Group.setVisible(false);
                        L1Group.setVisible(true);
                        lowerLevel1Image.setVisible(true);
                        this.L1Group.getChildren().add(circle);
                    }
                    case "L2" -> {
                        L1Group.setVisible(false);
                        floor1Group.setVisible(false);
                        floor2Group.setVisible(false);
                        floor3Group.setVisible(false);
                        L2Group.setVisible(true);
                        lowerLevel2Image.setVisible(true);
                        this.L2Group.getChildren().add(circle);
                    }
                    case "1" -> {
                        L1Group.setVisible(false);
                        L2Group.setVisible(false);
                        floor2Group.setVisible(false);
                        floor3Group.setVisible(false);
                        floor1Group.setVisible(true);
                        floor1Image.setVisible(true);
                        this.floor1Group.getChildren().add(circle);
                    }
                    case "2" -> {
                        L1Group.setVisible(false);
                        L2Group.setVisible(false);
                        floor1Group.setVisible(false);
                        floor3Group.setVisible(false);
                        floor2Group.setVisible(true);
                        floor2Image.setVisible(true);
                        this.floor2Group.getChildren().add(circle);
                    }
                    case "3" -> {
                        L1Group.setVisible(false);
                        L2Group.setVisible(false);
                        floor2Group.setVisible(false);
                        floor1Group.setVisible(false);
                        floor3Group.setVisible(true);
                        floor3Image.setVisible(true);
                        this.floor3Group.getChildren().add(circle);
                    }
                }
            }
            Point2D centerpoint = new Point2D(circle.getCenterX(), circle.getCenterY());
            groundFloor.centreOn(centerpoint);

        });


        cancelButton.setOnAction(event-> {
           try {
               Cancel();
           } catch (SQLException e) {
               throw new RuntimeException(e);
           }
       });
       clearButton.setOnAction(event-> {
           try {
               Clear();
           } catch (SQLException e) {
               throw new RuntimeException(e);
           }
       });
       submitButton.setOnAction(event-> {
           try {
               submit();
           } catch (SQLException e) {
               throw new RuntimeException(e);
           }
       });
       addPeopleButton.setOnAction(event -> addPeople());
       deletePeopleButton.setOnAction(event->deletePeople());
       initializeData();

        groundFloor.reset();
        groundFloor.setScrollBarPolicy(GesturePane.ScrollBarPolicy.NEVER);

    }

    private void initializeData() throws SQLException {
        List<LocationName> allLocations = DAOFacade.getAllLocationNames();
        conferenceRooms = FXCollections.observableArrayList();
        for(LocationName location: allLocations){
            if(location.getNodeType().equals("CONF")){
                conferenceRooms.add(location.getLongName());
            }
        }

        ArrayList<Login> allPeople= (ArrayList<Login>) DAOFacade.getAllLogins();
        attendances = FXCollections.observableArrayList();
        for(Login people: allPeople){
            attendances.add(people.getFirstName() + " " + people.getLastName() + "/" + people.getEmail());
        }

        selectedAttendances = FXCollections.observableArrayList();
        roomSelection.setItems(conferenceRooms);
        peopleSearchBar.setItems(attendances);
        selectedAttendanceList.setItems(selectedAttendances);
    }

    private void Cancel() throws SQLException {
       Clear();
       Navigation.navigate(Screen.HOME);
    }

    private void Clear() throws SQLException {
       this.initializeData();
       roomSelection.setText("");
       peopleSearchBar.setText("");
       firstName.clear();
       lastName.clear();
       date.clear();
    }

    private void addPeople(){
       String peopleSearched = peopleSearchBar.getText();
       if(!peopleSearched.isEmpty()){
           selectedAttendances.add(peopleSearched);
           peopleSearchBar.clear();
           attendances.remove(peopleSearched);
       }
    }

    private void deletePeople(){
        List<String> PeopleToDelete = selectedAttendanceList.getSelectionModel().getSelectedValues();
        for(String deletedPeople: PeopleToDelete){
            selectedAttendances.remove(deletedPeople);
            if(!attendances.contains(deletedPeople)) attendances.add(deletedPeople);
        }
    }

    private void submit() throws SQLException {
        boolean readyToSubmit = true;

        if(firstName.getText().equals("")){
            EffectGenerator.noFirstNameAlertOn(firstName);
            readyToSubmit = false;
        }else{
            EffectGenerator.noFirstNameAlertOff(firstName);
            uploadFirstName = firstName.getText();
        }

        if(lastName.getText().equals("")){
            EffectGenerator.noLastNameAlertOn(lastName);
            readyToSubmit = false;
        }else{
            EffectGenerator.noLastNameAlertOff(lastName);
            uploadLastName = lastName.getText();
        }

        if(roomSelection.getText().equals("")){
            EffectGenerator.noRoomAlertOn(roomSelection);
            readyToSubmit = false;
        }else{
            EffectGenerator.noRoomAlertOff(roomSelection);
            uploadConferenceRoom = roomSelection.getText();
        }

        if(selectedAttendances.isEmpty()){
            EffectGenerator.alertOn(peopleSearchBar,"please invite Attendees");
            readyToSubmit = false;
        }else{
            EffectGenerator.alertOff(peopleSearchBar,"search for Attendees");
            uploadExpectedSize = 1;
            for(String name: selectedAttendances){
                uploadAttendance += name + ", ";
                uploadExpectedSize++;
            }
            uploadAttendance = uploadAttendance.substring(0,uploadAttendance.length() - 1);
        }

        try {
            uploadDate =  Date.valueOf(date.getValue());
            EffectGenerator.noDateAlertOff(date);
        }catch (NullPointerException e){
            date.setFloatingText("Please Select Date");
            EffectGenerator.noDateAlertOn(date);
            readyToSubmit = false;
        }



        if(readyToSubmit){
            Conference conference = new Conference(uploadFirstName,uploadLastName,uploadDate,uploadAttendance,uploadExpectedSize,uploadConferenceRoom,RequestStatus.PROCESSING);
            DAOFacade.addConference(conference);
            int mostRecentNum = 0;
            for(Conference x : DAOFacade.getAllConference()){
                if(x.getOrderNum()>mostRecentNum){
                    mostRecentNum = x.getOrderNum();
                }
            }
            for(String name: selectedAttendances) {
                String[] split = name.split("/");
                String value = split[0];
                String[] split2 = value.split(" ");
                String firstName = split2[0];
                String lastName = split2[1];
                Invitations invite = new Invitations(mostRecentNum, firstName,lastName,false, uploadDate,uploadConferenceRoom);
                DAOFacade.addInvitation(invite);
            }
            MFXGenericDialog content = new MFXGenericDialog();
            new MFXStageDialog();
            MFXStageDialog stageDialog;
            stageDialog = MFXGenericDialogBuilder.build(content)
                    .toStageDialogBuilder()
                    .initOwner(App.getPrimaryStage())
                    .initModality(Modality.APPLICATION_MODAL)
                    .setDraggable(false)
                    .setTitle("Dialogs Preview")
                    .setOwnerNode(basePane)
                    .setScrimPriority(ScrimPriority.WINDOW)
                    .setScrimOwner(true)
                    .get();
            FlowPane flowPane = new FlowPane();
            flowPane.setAlignment(Pos.CENTER);
            flowPane.setRowValignment(VPos.CENTER);
            flowPane.setColumnHalignment(HPos.CENTER);
            Text text = new Text();
            text.setText("Your order has been confirmed");
            text.setFont(new Font(20));
            text.setStyle("-fx-text-fill: black");
            flowPane.getChildren().add(text);
            content.setContent(flowPane);

            content.setShowClose(false);
            content.setShowMinimize(false);
            content.setShowAlwaysOnTop(false);

            stageDialog.setContent(content);

            MFXStageDialog finalStageDialog = stageDialog;
            finalStageDialog.show();
            ColorAdjust shadow = new ColorAdjust();
            shadow.setBrightness(-.6);
            App.getRootPane().getCenter().setEffect(shadow);
            App.getRootPane().getCenter().setStyle("-fx-background-color: rgba(102,102,102,0.6)");
            content.setMaxSize(App.getRootPane().getWidth()/3, App.getRootPane().getHeight()/3);
            Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(2), event1 -> {
                finalStageDialog.close();
                App.getRootPane().getCenter().setEffect(null);
                App.getRootPane().getCenter().setStyle(null);
                Navigation.navigate(Screen.HOME);
            }));
            timeline.play();
        }
    }

    public void clearAllNodes() {
        floor1Group.getChildren().remove(1, floor1Group.getChildren().size());
        floor2Group.getChildren().remove(1, floor2Group.getChildren().size());
        floor3Group.getChildren().remove(1, floor3Group.getChildren().size());
        L1Group.getChildren().remove(1, L1Group.getChildren().size());
        L2Group.getChildren().remove(1, L2Group.getChildren().size());
    }

    public Circle drawCircle(double x, double y) {
        Circle circle = new Circle();
        circle.setVisible(true);
        circle.setFill(Color.web(ThemeColors.PATH_NODE_COLOR.getColor()));
        circle.setStroke(Color.web(ThemeColors.GRAY.getColor()));
        circle.setStrokeWidth(3.0f);
        circle.setCenterX(x);
        circle.setCenterY(y);
        circle.setRadius(10.0);
        return circle;
    }
}
