package edu.wpi.tacticaltritons.controllers.serviceRequest;

import edu.wpi.tacticaltritons.App;
import edu.wpi.tacticaltritons.auth.UserSessionToken;
import edu.wpi.tacticaltritons.database.*;
import edu.wpi.tacticaltritons.navigation.Navigation;
import edu.wpi.tacticaltritons.navigation.Screen;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXDatePicker;
import io.github.palexdev.materialfx.controls.MFXFilterComboBox;
import io.github.palexdev.materialfx.controls.MFXTextField;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Objects;
import java.util.ArrayList;

import javafx.beans.Observable;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
import net.kurobako.gesturefx.GesturePane;

import edu.wpi.tacticaltritons.styling.*;

public class FlowerCheckoutController {
    @FXML private MFXTextField userFirstField;
    @FXML private MFXTextField userLastField;
    @FXML private MFXTextField patientFirstField;
    @FXML private MFXTextField patientLastField;
    @FXML private MFXDatePicker deliveryDateField;
    @FXML private MFXTextField deliveryTimeField;
    @FXML private MFXButton cancelButton;
    @FXML private MFXButton clearButton;
    @FXML private MFXButton submitButton;
    @FXML private FlowPane checkoutFlowplan;

    //  @FXML private Text location;
    @FXML private MFXFilterComboBox locationComboBox;
    @FXML private Text shopName;
    @FXML private GesturePane groundFloor;
    @FXML private ImageView groundFloorImage;
    @FXML private ImageView lowerLevel1Image;
    @FXML private ImageView lowerLevel2Image;
    @FXML private ImageView floor1Image;
    @FXML private ImageView floor2Image;
    @FXML private ImageView floor3Image;
    @FXML private Group groundGroup;
    @FXML private Group L1Group;
    @FXML private Group L2Group;
    @FXML private Group floor1Group;
    @FXML private Group floor2Group;
    @FXML private Group floor3Group;
    @FXML MFXFilterComboBox assignedComboBox;
    @FXML private StackPane selectedFloorPane;
    @FXML private BorderPane basePane;
    private String userFirst;
    private String userLast;
    private String patientFirst;
    private String patientLast;
    private String staffFirst;
    private String staffLast;
    private Date deliveryDate;
    private Time deliveryTime;
    private String location;
    RequestStatus status = RequestStatus.BLANK;
    private double flowerTotal;
    private ObservableMap<String, Integer> checkoutItems = FXCollections.observableHashMap();

    public void initialize() throws SQLException {

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
    //Free Mont Flowers
    imageHashMap.put("Blushing Beauty Bouquet", new Image(Objects.requireNonNull(App.class.getResource("images/flower_request/FMFBlushingBeautyBouquet.png")).toString()));
    imageHashMap.put("Elegance Bouquet", new Image(Objects.requireNonNull(App.class.getResource("images/flower_request/FMFEleganceBouquet.png")).toString()));
    imageHashMap.put("Charming Bouquet", new Image(Objects.requireNonNull(App.class.getResource("images/flower_request/FMFCharmingBouquet.png")).toString()));
    imageHashMap.put("Cherry Bouquet", new Image(Objects.requireNonNull(App.class.getResource("images/flower_request/FMFCherryBouquet.png")).toString()));
    imageHashMap.put("Zen Bouquet", new Image(Objects.requireNonNull(App.class.getResource("images/flower_request/FMFZenBouquet.png")).toString()));
    imageHashMap.put("What a Delight Bouquet", new Image(Objects.requireNonNull(App.class.getResource("images/flower_request/FMFWhataDelightBouquet.png")).toString()));
    imageHashMap.put("Girl Power Bouquet", new Image(Objects.requireNonNull(App.class.getResource("images/flower_request/FMFGirlPowerBouquet.png")).toString()));
    imageHashMap.put("Birds of Paradise Bouquet", new Image(Objects.requireNonNull(App.class.getResource("images/flower_request/FMFMinimalistBouquet.png")).toString()));
    imageHashMap.put("Minimalist Bouquet", new Image(Objects.requireNonNull(App.class.getResource("images/flower_request/FMFMinimalistBouquet.png")).toString()));
    imageHashMap.put("So Chic Bouquet", new Image(Objects.requireNonNull(App.class.getResource("images/flower_request/FMFSoChicBouquet.png")).toString()));

    // Blossoms Path
    imageHashMap.put("Summer Garden Bouquet", new Image(Objects.requireNonNull(App.class.getResource("images/flower_request/BPSummerGardenBouquet.png")).toString()));
    imageHashMap.put("Easter Lily Arrangement", new Image(Objects.requireNonNull(App.class.getResource("images/flower_request/BPEasterLilyArrangement.png")).toString()));
    imageHashMap.put("Tulip Trio", new Image(Objects.requireNonNull(App.class.getResource("images/flower_request/BPTulipTrio.png")).toString()));
    imageHashMap.put("Springtime Garden Basket", new Image(Objects.requireNonNull(App.class.getResource("images/flower_request/BPSpringtimeGardenBasket.png")).toString()));
    imageHashMap.put("Elegant Orchids", new Image(Objects.requireNonNull(App.class.getResource("images/flower_request/BPElegantOrchids.png")).toString()));
    imageHashMap.put("Cherry Blossom Bouquet", new Image(Objects.requireNonNull(App.class.getResource("images/flower_request/BPCherryBlossomBouquet.png")).toString()));
    imageHashMap.put("Winter Garden Centerpiece", new Image(Objects.requireNonNull(App.class.getResource("images/flower_request/BPWinterGardenCenterpiece.png")).toString()));
    imageHashMap.put("Rose Arrangement", new Image(Objects.requireNonNull(App.class.getResource("images/flower_request/BPRoseArrangement.png")).toString()));
    imageHashMap.put("Springtime Succulent Garden", new Image(Objects.requireNonNull(App.class.getResource("images/flower_request/BPSpringtimeSucculentGarden.png")).toString()));
    imageHashMap.put("Butterfly Garden Bouquet", new Image(Objects.requireNonNull(App.class.getResource("images/flower_request/BPButterflyGardenBouquet.png")).toString()));

    // Garden Grace
    imageHashMap.put("Wonderland Bouquet", new Image(Objects.requireNonNull(App.class.getResource("images/flower_request/GGWonderlandBouquet.png")).toString()));
    imageHashMap.put("Sunny Morning Bouquet", new Image(Objects.requireNonNull(App.class.getResource("images/flower_request/GGSunnyMorningBouquet.png")).toString()));
    imageHashMap.put("Holiday Cheer Bouquet", new Image(Objects.requireNonNull(App.class.getResource("images/flower_request/GGHolidayCheerBouquet.png")).toString()));
    imageHashMap.put("Summer Solstice Bouquet", new Image(Objects.requireNonNull(App.class.getResource("images/flower_request/GGSummerSolsticeBouquet.png")).toString()));
    imageHashMap.put("Snowy Splendor Bouquet", new Image(Objects.requireNonNull(App.class.getResource("images/flower_request/GGSnowySplendorBouquet.png")).toString()));
    imageHashMap.put("Forest Bouquet", new Image(Objects.requireNonNull(App.class.getResource("images/flower_request/GGForestBouquet.png")).toString()));
    imageHashMap.put("Berries Bouquet", new Image(Objects.requireNonNull(App.class.getResource("images/flower_request/GGBerriesBouquet.png")).toString()));
    imageHashMap.put("Cozy Nights Bouquet", new Image(Objects.requireNonNull(App.class.getResource("images/flower_request/GGCozyNightsBouquet.png")).toString()));
    imageHashMap.put("Icy Elegance Bouquet", new Image(Objects.requireNonNull(App.class.getResource("images/flower_request/GGIcyEleganceBouquet.png")).toString()));
    imageHashMap.put("Cheer Up Bouquet", new Image(Objects.requireNonNull(App.class.getResource("images/flower_request/GGCheerUpBouquet.png")).toString()));

    // Petal Boutique
    imageHashMap.put("Roses and Lilies", new Image(Objects.requireNonNull(App.class.getResource("images/flower_request/PBRosesandLilies.png")).toString()));
    imageHashMap.put("Tropical Oasis", new Image(Objects.requireNonNull(App.class.getResource("images/flower_request/PBTropicalOasis.png")).toString()));
    imageHashMap.put("Spring Fling", new Image(Objects.requireNonNull(App.class.getResource("images/flower_request/PBSpringFling.png")).toString()));
    imageHashMap.put("Vintage Romance", new Image(Objects.requireNonNull(App.class.getResource("images/flower_request/PBVintageRomance.png")).toString()));
    imageHashMap.put("Succulent Garden", new Image(Objects.requireNonNull(App.class.getResource("images/flower_request/PBSucculentGarden.png")).toString()));
    imageHashMap.put("Enchanted Forest", new Image(Objects.requireNonNull(App.class.getResource("images/flower_request/PBEnchantedForest.png")).toString()));
    imageHashMap.put("Orchid Delight", new Image(Objects.requireNonNull(App.class.getResource("images/flower_request/PBOrchidDelight.png")).toString()));
    imageHashMap.put("Sunflower Surprise", new Image(Objects.requireNonNull(App.class.getResource("images/flower_request/PBSunflowerSurprise.png")).toString()));
    imageHashMap.put("Bold and Beautiful", new Image(Objects.requireNonNull(App.class.getResource("images/flower_request/PBBoldandBeautiful.png")).toString()));
    imageHashMap.put("Garden Party", new Image(Objects.requireNonNull(App.class.getResource("images/flower_request/PBGardenParty.png")).toString()));

//    EffectGenerator.generateShadowEffect(basePane); //shadow generator
    lowerLevel1Image.setImage(App.lowerlevel1);
    lowerLevel2Image.setImage(App.lowerlevel2);
    groundFloorImage.setImage(App.groundfloor);
    floor1Image.setImage(App.firstfloor);
    floor2Image.setImage(App.secondfloor);
    floor3Image.setImage(App.thirdfloor);

        groundFloor.setVisible(true);
        floor1Image.setVisible(true);

//        this.preview.setOnAction(event -> {
//            clearAllNodes();
//            Circle circle = new Circle();
//
//            try {
//                circle = drawCircle(DAOFacade.getNode(this.locationComboBox.getText(), today).getXcoord(), DAOFacade.getNode(this.locationComboBox.getText(), today).getYcoord());
//            } catch (SQLException e) {
//                throw new RuntimeException(e);
//            }
//
//            String endFloor = null;
//            try {
//                endFloor = DAOFacade.getNode(this.locationComboBox.getText(), today).getFloor();
//            } catch (SQLException e) {
//                throw new RuntimeException(e);
//            }
//
//            if (endFloor != null) {
//                switch (endFloor) {
//                    case "L1":
//                        L2Group.setVisible(false);
//                        floor1Group.setVisible(false);
//                        floor2Group.setVisible(false);
//                        floor3Group.setVisible(false);
//
//                        L1Group.setVisible(true);
//                        lowerLevel1Image.setVisible(true);
//                        this.L1Group.getChildren().add(circle);
//                        break;
//                    case "L2":
//                        L1Group.setVisible(false);
//                        floor1Group.setVisible(false);
//                        floor2Group.setVisible(false);
//                        floor3Group.setVisible(false);
//
//                        L2Group.setVisible(true);
//                        lowerLevel2Image.setVisible(true);
//                        this.L2Group.getChildren().add(circle);
//                        break;
//                    case "1":
//                        L1Group.setVisible(false);
//                        L2Group.setVisible(false);
//                        floor2Group.setVisible(false);
//                        floor3Group.setVisible(false);
//
//                        floor1Group.setVisible(true);
//                        floor1Image.setVisible(true);
//                        this.floor1Group.getChildren().add(circle);
//                        break;
//                    case "2":
//                        L1Group.setVisible(false);
//                        L2Group.setVisible(false);
//                        floor1Group.setVisible(false);
//                        floor3Group.setVisible(false);
//
//                        floor2Group.setVisible(true);
//                        floor2Image.setVisible(true);
//                        this.floor2Group.getChildren().add(circle);
//                        break;
//                    case "3":
//                        L1Group.setVisible(false);
//                        L2Group.setVisible(false);
//                        floor2Group.setVisible(false);
//                        floor1Group.setVisible(false);
//
//                        floor3Group.setVisible(true);
//                        floor3Image.setVisible(true);
//                        this.floor3Group.getChildren().add(circle);
//                        break;
//                }
//            }
//            Point2D centerpoint = new Point2D(circle.getCenterX(), circle.getCenterY());
//            groundFloor.centreOn(centerpoint);
//
//        });

        this.checkoutItems = FlowerDeliveryController.checkoutItems;
        this.flowerTotal = FlowerDeliveryController.flowerTotal;

        checkoutItems.forEach((key, value) ->
        {
            checkoutFlowplan.getChildren().add(createCheckoutNode(key, value, imageHashMap.get(key)));
        });
        checkoutFlowplan.setAlignment(Pos.CENTER);;

        shopName.setText(FlowerChoiceController.name);

        clearButton.setOnMouseClicked(event -> clearForm());

        cancelButton.setOnMouseClicked(
                event -> {
                    Navigation.navigate(Screen.HOME);
                    clearForm();
                });

        userFirstField.setText(UserSessionToken.getUser().getFirstname());
        userLastField.setText(UserSessionToken.getUser().getLastname());

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        deliveryTimeField.setText(formatter.format(LocalDateTime.now()));

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
                        Navigation.navigate(Screen.HOME);
                    } else {
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
            System.out.println(centrePoint);
            groundFloor.centreOn(centrePoint);
        });

        groundFloor.setScrollBarPolicy(GesturePane.ScrollBarPolicy.NEVER);


    }

  private FlowPane createCheckoutNode(String key, int value, Image flowerImage) {
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
    Image image = flowerImage;
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
        int total = (int) flowerTotal;
        if(assignedComboBox.getSelectedItem() != null)
        {
            staffFirst = assignedComboBox.getSelectedItem().toString().substring(0,assignedComboBox.getSelectedItem().toString().indexOf(' '));
            staffLast = assignedComboBox.getSelectedItem().toString().substring(assignedComboBox.getSelectedItem().toString().indexOf(' ') + 1, assignedComboBox.getSelectedItem().toString().length());
            status = RequestStatus.PROCESSING;
        }
        else
        {
            staffFirst = null;
            staffLast = null;
        }

        Flower flower = new Flower(userFirst, userLast, patientFirst, patientLast, staffFirst, staffLast, deliveryDate, deliveryTime, location, items.get(), total, status);
        DAOFacade.addFlower(flower);
        FlowerDeliveryController.checkoutItems.clear();
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

        if(assignedComboBox.isSelectable()) {
            EffectGenerator.noAssigendStaff(assignedComboBox);
            readyToSubmit = false;
        }
        else
        {
            EffectGenerator.noAssigendStaffOff(assignedComboBox);
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

        if (deliveryTimeField.getText().isEmpty()) {
            EffectGenerator.noTimeAlertOn(deliveryTimeField);
            readyToSubmit = false;
        } else {
            EffectGenerator.noTimeAlertOff(deliveryTimeField);
            deliveryTime = Time.valueOf(deliveryTimeField.getText() + ":00");
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
        deliveryTimeField.clear();
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
