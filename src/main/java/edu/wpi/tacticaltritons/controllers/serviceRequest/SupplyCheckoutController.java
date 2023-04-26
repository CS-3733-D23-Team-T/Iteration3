package edu.wpi.tacticaltritons.controllers.serviceRequest;

import edu.wpi.tacticaltritons.App;
import edu.wpi.tacticaltritons.auth.UserSessionToken;
import edu.wpi.tacticaltritons.database.*;
import edu.wpi.tacticaltritons.navigation.Navigation;
import edu.wpi.tacticaltritons.navigation.Screen;
import edu.wpi.tacticaltritons.styling.EffectGenerator;
import edu.wpi.tacticaltritons.styling.ThemeColors;
import io.github.palexdev.materialfx.controls.*;
import io.github.palexdev.materialfx.dialogs.MFXGenericDialog;
import io.github.palexdev.materialfx.dialogs.MFXGenericDialogBuilder;
import io.github.palexdev.materialfx.dialogs.MFXStageDialog;
import io.github.palexdev.materialfx.enums.ScrimPriority;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableMap;
import javafx.fxml.FXML;
import javafx.geometry.*;
import javafx.scene.Group;
import javafx.scene.control.Label;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
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
import java.util.HashMap;
import java.util.Objects;

public class SupplyCheckoutController {
    @FXML
    private BorderPane basePane;
    @FXML
    private MFXTextField userFirstField;
    @FXML
    private MFXTextField userLastField;
    @FXML
    private MFXTextField patientFirstField;
    @FXML
    private MFXTextField patientLastField;
    @FXML
    private MFXDatePicker deliveryDateField;
    @FXML
    private MFXComboBox<Integer> hourComboBox;
    @FXML
    private MFXComboBox<String> minComboBox;

    @FXML
    private MFXButton cancelButton;
    @FXML
    private MFXButton clearButton;
    @FXML
    private MFXButton submitButton;
    @FXML
    private FlowPane checkoutFlowplan;

    //  @FXML private Text location;
    @FXML
    private MFXFilterComboBox locationComboBox;
    @FXML
    private Text shopName;
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
    MFXFilterComboBox assignedComboBox;
    @FXML
    private Text priceText;
    private String userFirst;
    private String userLast;
    private String patientFirst;
    private String patientLast;
    private String staffFirst;
    private String staffLast;
    private Date deliveryDate;
    private Time deliveryTime;
    private int hour;
    private int min;
    private String location;
    RequestStatus status = RequestStatus.BLANK;
    private double supplyTotal;
    private ObservableMap<String, Integer> checkoutItems = FXCollections.observableHashMap();


    public void initialize() throws SQLException {

        hourComboBox.setItems(FXCollections.observableArrayList(0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23));
        minComboBox.setItems(FXCollections.observableArrayList("00", "01", "02", "03", "04", "05", "06", "07", "08", "09", "00", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30", "31", "32", "33", "34", "35", "36", "37", "38", "39", "40", "41", "42", "43", "44", "45", "46", "47", "48", "49", "50", "51", "52", "53", "54", "55", "56", "57", "58", "59"));

        for (Login login : DAOFacade.getAllLogins()) {
            assignedComboBox.getItems().add(login.getFirstName() + " " + login.getLastName());
        }

        Date today = new Date(2023, 4, 10);
        lowerLevel1Image.setImage(App.lowerlevel1);
        lowerLevel2Image.setImage(App.lowerlevel2);
        groundFloorImage.setImage(App.groundfloor);
        floor1Image.setImage(App.firstfloor);
        floor2Image.setImage(App.secondfloor);
        floor3Image.setImage(App.thirdfloor);
        HashMap<String, Image> imageHashMap = new HashMap<>();


//    EffectGenerator.generateShadowEffect(basePane); //shadow generator
        lowerLevel1Image.setImage(App.lowerlevel1);
        lowerLevel2Image.setImage(App.lowerlevel2);
        groundFloorImage.setImage(App.groundfloor);
        floor1Image.setImage(App.firstfloor);
        floor2Image.setImage(App.secondfloor);
        floor3Image.setImage(App.thirdfloor);

        groundFloor.setVisible(true);
        floor1Image.setVisible(true);

        this.checkoutItems = SupplyDeliveryController.checkoutItems;
        this.supplyTotal = SupplyDeliveryController.supplyTotal;
        priceText.setText(Double.toString(supplyTotal));


        checkoutItems.forEach((key, value) ->
        {
            checkoutFlowplan.getChildren().add(createCheckoutNode(key, value, imageHashMap.get(key)));
        });
        checkoutFlowplan.setAlignment(Pos.CENTER);
        ;

        shopName.setText(SupplyChoiceController.name);

        clearButton.setOnMouseClicked(event -> clearForm());

        cancelButton.setOnMouseClicked(
                event -> {
                    Navigation.navigate(Screen.HOME);
                    clearForm();
                });

        userFirstField.setText(UserSessionToken.getUser().getFirstname());
        userLastField.setText(UserSessionToken.getUser().getLastname());

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");

        formatter = DateTimeFormatter.ofPattern("MMM dd, yyyy");
        deliveryDateField.setValue(LocalDate.from(formatter.parse(formatter.format(LocalDateTime.now()))));

        submitButton.setOnMouseClicked(
                event -> {
                    if (formComplete()) {
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
                        ColorAdjust shadow = new ColorAdjust();
                        shadow.setBrightness(-.6);
                        App.getRootPane().getCenter().setEffect(shadow);
                        App.getRootPane().getCenter().setStyle("-fx-background-color: rgba(102,102,102,0.6)");
                        content.setMaxSize(App.getRootPane().getWidth()/3, App.getRootPane().getHeight()/3);
                        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(2), event1 -> {
                            finalStageDialog.close();
                            clearForm();
                            App.getRootPane().getCenter().setEffect(null);
                            App.getRootPane().getCenter().setStyle(null);
                            Navigation.navigate(Screen.HOME);
                        }));
                        timeline.play();
                    } else {
                        System.out.println("Form cannot submit");
                        //TODO do something when not filled
                    }
                });

        for (LocationName name : DAOFacade.getAllLocationNames()) {
            locationComboBox.getItems().add(name.getLongName());
        }

        this.locationComboBox.setOnAction(event -> {

            clearAllNodes();
            Circle circle = new Circle();

            try {
                circle = drawCircle(DAOFacade.getNode((String) this.locationComboBox.getSelectedItem(), today).getXcoord(), DAOFacade.getNode((String) this.locationComboBox.getSelectedItem(), today).getYcoord());
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }


            String endFloor = null;
            try {
                endFloor = DAOFacade.getNode((String) this.locationComboBox.getSelectedItem(), today).getFloor();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

            if (endFloor != null) {
                switch (endFloor) {
                    case "L1":
                        L1Group.setVisible(true);
                        lowerLevel1Image.setVisible(true);
                        this.L1Group.getChildren().add(circle);
                        break;
                    case "L2":
                        L2Group.setVisible(true);
                        lowerLevel2Image.setVisible(true);
                        this.L2Group.getChildren().add(circle);
                        break;
                    case "1":
                        floor1Group.setVisible(true);
                        floor1Image.setVisible(true);
                        this.floor1Group.getChildren().add(circle);
                        break;
                    case "2":
                        floor2Group.setVisible(true);
                        floor2Image.setVisible(true);
                        this.floor2Group.getChildren().add(circle);
                        break;
                    case "3":
                        floor3Group.setVisible(true);
                        floor3Image.setVisible(true);
                        this.floor3Group.getChildren().add(circle);
                        break;
                }
            }
            Point2D centrePoint = new Point2D(circle.getCenterX(), circle.getCenterY());
            groundFloor.centreOn(centrePoint);
        });

        groundFloor.setScrollBarPolicy(GesturePane.ScrollBarPolicy.NEVER);


    }

    private FlowPane createCheckoutNode(String key, int value, Image supplyImage) {
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
        Image image = supplyImage;
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
        SimpleStringProperty items = new SimpleStringProperty("");
        checkoutItems.forEach((key, value) ->
        {
            items.set(items.get() + key + "(" + value + ")\n");
        });
        int total = (int) supplyTotal;
        if (assignedComboBox.getSelectedItem() != null) {
            staffFirst = assignedComboBox.getSelectedItem().toString().substring(0, assignedComboBox.getSelectedItem().toString().indexOf(' '));
            staffLast = assignedComboBox.getSelectedItem().toString().substring(assignedComboBox.getSelectedItem().toString().indexOf(' ') + 1, assignedComboBox.getSelectedItem().toString().length());
            status = RequestStatus.PROCESSING;
        }
        else
        {
            staffFirst = null;
            staffLast = null;
        }


        deliveryTime = Time.valueOf(Integer.toString(hour) + ":" + min + ":00");

        Supply supply = new Supply(userFirst, userLast, staffFirst, staffLast, deliveryDate, deliveryTime, location, items.get(), total, status);
        DAOFacade.addSupply(supply);
        SupplyDeliveryController.checkoutItems.clear();
    }

    private boolean formComplete() {
        boolean readyToSubmit = true;
        if (userFirstField.getText().isEmpty()) {
            EffectGenerator.noFirstNameAlertOn(userFirstField);
            readyToSubmit = false;
        } else {
            EffectGenerator.noFirstNameAlertOff(userFirstField);
            userFirst = userFirstField.getText();
        }

        if (userLastField.getText().isEmpty()) {
            EffectGenerator.noLastNameAlertOn(userLastField);
            readyToSubmit = false;
        } else {
            EffectGenerator.noFirstNameAlertOff(userLastField);
            userLast = userLastField.getText();
        }

        if (deliveryDateField.getText().isEmpty()) {
            EffectGenerator.noDateAlertOn(deliveryDateField);
            readyToSubmit = false;
        } else {
            EffectGenerator.noDateAlertOff(deliveryDateField);
            String pattern = "yyyy-MM-dd";
            DateTimeFormatter df = DateTimeFormatter.ofPattern(pattern);
            String newDate = df.format(deliveryDateField.getValue());
            deliveryDate = Date.valueOf(newDate);
        }

        if (patientFirstField.getText().isEmpty()) {
            EffectGenerator.alertOn(patientFirstField, "Please enter patient first name");
            readyToSubmit = false;
        } else {
            EffectGenerator.alertOff(patientFirstField, "Patient first name");
            patientFirst = patientFirstField.getText();
        }

        if (patientLastField.getText().isEmpty()) {
            EffectGenerator.alertOn(patientLastField, "Please enter patient last name");
            readyToSubmit = false;
        } else {
            EffectGenerator.alertOff(patientLastField, "Patient last name");
            patientLast = patientLastField.getText();
        }


        // this is a problem
        if (hourComboBox.getSelectedItem() == null) {
            EffectGenerator.noTimeAlertOn(hourComboBox);
            readyToSubmit = false;
        } else {
            EffectGenerator.noTimeAlertOff(hourComboBox);
            hour = hourComboBox.getSelectedItem();
        }

        if (minComboBox.getSelectedItem() == null) {
            EffectGenerator.noTimeAlertOn(minComboBox);
            readyToSubmit = false;
        } else {
            EffectGenerator.noTimeAlertOff(minComboBox);
            min = Integer.parseInt(minComboBox.getSelectedItem());
        }

        if (locationComboBox.getText().isEmpty()) {
            EffectGenerator.noRoomAlertOn(locationComboBox);
            readyToSubmit = false;
        } else {
            EffectGenerator.noRoomAlertOff(locationComboBox);
            location = locationComboBox.getText();
        }
        return readyToSubmit;
    }

    private void clearForm() {
        userFirstField.clear();
        userLastField.clear();
        patientFirstField.clear();
        patientLastField.clear();
        assignedComboBox.clear();
        deliveryDateField.clear();
        hourComboBox.clear();
        minComboBox.clear();
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
        circle.setFill(Color.web(ThemeColors.PATH_NODE_COLOR.getColor()));
        circle.setStroke(Color.web(ThemeColors.GRAY.getColor()));
        circle.setStrokeWidth(3.0f);
        circle.setCenterX(x);
        circle.setCenterY(y);
        circle.setRadius(10.0);
        return circle;
    }
}
