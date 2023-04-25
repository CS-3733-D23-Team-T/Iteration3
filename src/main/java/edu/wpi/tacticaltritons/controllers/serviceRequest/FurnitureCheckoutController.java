package edu.wpi.tacticaltritons.controllers.serviceRequest;

import edu.wpi.tacticaltritons.App;
import edu.wpi.tacticaltritons.auth.UserSessionToken;
import edu.wpi.tacticaltritons.auth.Validator;
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
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableMap;
import javafx.fxml.FXML;
import javafx.geometry.*;
import javafx.scene.Group;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
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
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class FurnitureCheckoutController {
    @FXML
    private MFXTextField userFirstField;
    @FXML
    private Text userFirstValidator;
    @FXML
    private MFXTextField userLastField;
    @FXML
    private Text userLastValidator;
    @FXML
    private MFXComboBox assignedStaffComboBox;
    @FXML
    private Text assignedStaffValidator;
    @FXML
    private MFXDatePicker deliveryDateField;
    @FXML
    private Text dateValidator;
    @FXML
    private MFXButton cancelButton;
    @FXML
    private MFXButton clearButton;
    @FXML
    private MFXButton submitButton;
    @FXML
    private MFXButton previewButton;
    @FXML
    private FlowPane checkoutFlowplan;

    @FXML
    private MFXComboBox<Integer> hourComboBox;
    @FXML
    private MFXComboBox<String> minComboBox;
    private int hour;
    private int min;

    //  @FXML private Text location;
    @FXML
    private MFXFilterComboBox<String> locationComboBox;
    @FXML
    private Text shopName;
@FXML
private BorderPane basePane;
    @FXML
    private GesturePane groundFloor;

    @FXML
    private ImageView groundFloorImage;

    @FXML
    private ImageView lowerLevel1Image;

    @FXML
    private ImageView lowerLevel2Image;

    @FXML
    private ImageView floor1Image;

    @FXML
    private ImageView floor2Image;

    @FXML
    private ImageView floor3Image;

    @FXML
    private Group groundGroup;
    @FXML
    private Group L1Group;
    @FXML
    private Group L2Group;
    @FXML
    private Group floor1Group;
    @FXML
    private Group floor2Group;
    @FXML
    private Group floor3Group;


    @FXML
    private StackPane selectedFloorPane;


    private ObservableMap<String, Integer> checkoutItems = FXCollections.observableHashMap();

    public void initialize() throws SQLException {

        hourComboBox.setItems(FXCollections.observableArrayList(0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23));
        minComboBox.setItems(FXCollections.observableArrayList("00", "01", "02", "03", "04", "05", "06", "07", "08", "09", "00", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30", "31", "32", "33", "34", "35", "36", "37", "38", "39", "40", "41", "42", "43", "44", "45", "46", "47", "48", "49", "50", "51", "52", "53", "54", "55", "56", "57", "58", "59"));


        for (Login login : DAOFacade.getAllLogins()) {
            assignedStaffComboBox.getItems().add(login.getFirstName() + " " + login.getLastName());
        }
        lowerLevel1Image.setImage(App.lowerlevel1);
        lowerLevel2Image.setImage(App.lowerlevel2);
        floor1Image.setImage(App.firstfloor);
        floor2Image.setImage(App.secondfloor);
        floor3Image.setImage(App.thirdfloor);

        floor1Image.setVisible(true);

        this.checkoutItems = FurnitureDeliveryController.checkoutItems;

        checkoutItems.forEach((key, value) ->
        {
            checkoutFlowplan.getChildren().add(createCheckoutNode(key, value));
        });
        checkoutFlowplan.setAlignment(Pos.CENTER);


        //shopName.setText("Storage");

        clearButton.setOnMouseClicked(event -> clearForm());

        cancelButton.setOnMouseClicked(
                event -> {
                    Navigation.navigate(Screen.HOME);
                    clearForm();
                });

        userFirstField.setText(UserSessionToken.getUser().getFirstname());
        BooleanProperty validUserFirstName = new SimpleBooleanProperty(true);
        userFirstField.textProperty().addListener(Validator.generateValidatorListener(validUserFirstName, "[A-Za-z]{1,50}",
                userFirstValidator.getText(), userFirstValidator));
        userLastField.setText(UserSessionToken.getUser().getLastname());
        BooleanProperty validUserLastName = new SimpleBooleanProperty(true);
        userLastField.textProperty().addListener(Validator.generateValidatorListener(validUserLastName, "[A-Za-z]{1,50}",
                userLastValidator.getText(), userLastValidator));

        BooleanProperty validAssignedStaff = new SimpleBooleanProperty(true);
        assignedStaffComboBox.selectedItemProperty().addListener((obs, o, n) -> validAssignedStaff.set(n != null));

        BooleanProperty vaildHourCombobox = new SimpleBooleanProperty(true);
        hourComboBox.selectedItemProperty().addListener((obs, o, n) -> vaildHourCombobox.set(n != null));

        BooleanProperty vaildMinCombobox = new SimpleBooleanProperty(true);
        minComboBox.selectedItemProperty().addListener((obs, o, n) -> vaildMinCombobox.set(n != null));

        DateTimeFormatter[] formatters = new DateTimeFormatter[]{
                DateTimeFormatter.ofPattern("MMM dd, yyyy"),
                DateTimeFormatter.ofPattern("MMM d, yyyy")
        };
        try {
            deliveryDateField.setValue(LocalDate.from(formatters[1].parse(formatters[1].format(LocalDateTime.now()))));
        } catch (Exception ignored) {
            try {
                deliveryDateField.setValue(LocalDate.from(formatters[0].parse(formatters[0].format(LocalDateTime.now()))));
            } catch (Exception ignore) {

            }
        }
        BooleanProperty validDate = new SimpleBooleanProperty(true);
        deliveryDateField.textProperty().addListener(Validator.generateValidatorListener(
                validDate, dateValidator.getText(), dateValidator, formatters));
        BooleanProperty validLocation = new SimpleBooleanProperty(false);
        locationComboBox.selectedItemProperty().addListener((obs, o, n) -> validLocation.set(n != null));

        submitButton.setOnMouseClicked(
                event -> {
                    try {
                        sendToDatabase();
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                    clearForm();
                    MFXGenericDialog content = new MFXGenericDialog();
                    MFXStageDialog stageDialog = new MFXStageDialog();
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
                    Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(2), event1 -> {
                        finalStageDialog.close();
                        Navigation.navigate(Screen.HOME);
                    }));
                    timeline.play();
                });

        validUserFirstName.addListener(Validator.generateFormListener(submitButton,
                validUserFirstName, validUserLastName, validDate, validLocation, vaildHourCombobox, vaildMinCombobox));

        validUserLastName.addListener(Validator.generateFormListener(submitButton,
                validUserFirstName, validUserLastName, validDate, validLocation, vaildHourCombobox, vaildMinCombobox));

        validDate.addListener(Validator.generateFormListener(submitButton,
                validUserFirstName, validUserLastName, validDate, validLocation, vaildHourCombobox, vaildMinCombobox));

        validLocation.addListener(Validator.generateFormListener(submitButton,
                validUserFirstName, validUserLastName, validDate, validLocation, vaildHourCombobox, vaildMinCombobox));

        vaildHourCombobox.addListener(Validator.generateFormListener(submitButton,
                validUserFirstName, validUserLastName, validDate, validLocation, vaildHourCombobox, vaildMinCombobox));

        vaildMinCombobox.addListener(Validator.generateFormListener(submitButton,
                validUserFirstName, validUserLastName, validDate, validLocation, vaildHourCombobox, vaildMinCombobox));


        for (LocationName name : DAOFacade.getAllLocationNames()) {
            locationComboBox.getItems().add(name.getLongName());
        }

    }

    private FlowPane createCheckoutNode(String key, int value) {
        FlowPane flowPane = new FlowPane();
        flowPane.setPrefWidth(200);
        flowPane.setPrefHeight(100);
        flowPane.setOrientation(Orientation.VERTICAL);
        flowPane.setRowValignment(VPos.CENTER);
        flowPane.setColumnHalignment(HPos.CENTER);
        flowPane.setAlignment(Pos.CENTER);
        flowPane.setPadding(new Insets(20, 20, 20, 20));
        flowPane.setMargin(flowPane, new Insets(10, 20, 10, 20));
        flowPane.setBackground(Background.fill(Color.WHITE));

        // Creates the image view
        Image image = new Image("/edu/wpi/tacticaltritons/images/flower_request/Boston Blossoms.jpg");
        ImageView imageView = new ImageView(image);
        imageView.setFitHeight(50);
        imageView.setFitWidth(50);

        //creates the discription label
        Label itemTitle = new Label();
        itemTitle.setPrefWidth(200);
        itemTitle.setPrefHeight(50);
        itemTitle.setText(key);
        itemTitle.setFont(new Font(15));
        itemTitle.setWrapText(true);
        itemTitle.setPadding(new Insets(0, 20, 0, 20));

        Label quantity = new Label();
        quantity.setPrefWidth(50);
        quantity.setPrefHeight(50);
        quantity.setAlignment(Pos.CENTER);
        quantity.setText(Integer.toString(value));

        flowPane.getChildren().add(imageView);
        flowPane.getChildren().add(itemTitle);
        flowPane.getChildren().add(quantity);
        return flowPane;
    }

    private void sendToDatabase() throws SQLException {
        String userFirst = userFirstField.getText();
        String userLast = userLastField.getText();
        int i = assignedStaffComboBox.getText().indexOf(' ');
        String staffFirst = null;
        String staffLast = null;
        if (!assignedStaffComboBox.getText().isEmpty()) {
            staffFirst = assignedStaffComboBox.getText().substring(0, i);
            staffLast = assignedStaffComboBox.getText().substring(i + 1);
        }
        String pattern = "yyyy-MM-dd";
        DateTimeFormatter df = DateTimeFormatter.ofPattern(pattern);
        String newDate = df.format(deliveryDateField.getValue());
        Date deliveryDate = Date.valueOf(newDate);
        String location = locationComboBox.getText();
        SimpleStringProperty items = new SimpleStringProperty("");
        checkoutItems.forEach((key, value) ->
        {
            items.set(items.get() + key + "(" + value + ")\n");
        });
        RequestStatus status = RequestStatus.BLANK;
        if ((!assignedStaffComboBox.getText().isEmpty())) {
            status = RequestStatus.PROCESSING;
        }
        Furniture furniture = new Furniture(userFirst, userLast, staffFirst, staffLast, deliveryDate, location, items.get(), status);
        DAOFacade.addFurniture(furniture);
        FurnitureDeliveryController.checkoutItems.clear();
    }

    private void clearForm() {
        userFirstField.clear();
        userLastField.clear();
        assignedStaffComboBox.clear();
        deliveryDateField.clear();
        locationComboBox.clear();
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
        circle.setFill(Color.RED);
        circle.setStroke(Color.BLACK);
        circle.setStrokeWidth(3.0f);
        circle.setCenterX(x);
        circle.setCenterY(y);
        circle.setRadius(10.0);
        return circle;
    }
}
