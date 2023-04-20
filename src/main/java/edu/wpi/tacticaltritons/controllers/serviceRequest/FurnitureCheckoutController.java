package edu.wpi.tacticaltritons.controllers.serviceRequest;

import edu.wpi.tacticaltritons.App;
import edu.wpi.tacticaltritons.auth.UserSessionToken;
import edu.wpi.tacticaltritons.auth.Validator;
import edu.wpi.tacticaltritons.database.*;
import edu.wpi.tacticaltritons.navigation.Navigation;
import edu.wpi.tacticaltritons.navigation.Screen;
import io.github.palexdev.materialfx.controls.*;
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
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import net.kurobako.gesturefx.GesturePane;

import java.sql.Date;
import java.sql.SQLException;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class FurnitureCheckoutController {
  @FXML private MFXTextField userFirstField;
  @FXML private Text userFirstValidator;
  @FXML private MFXTextField userLastField;
  @FXML private Text userLastValidator;
  @FXML private MFXComboBox assignedStaffComboBox;
  @FXML private Text assignedStaffValidator;
  @FXML private MFXDatePicker deliveryDateField;
  @FXML private Text dateValidator;
  @FXML private MFXButton cancelButton;
  @FXML private MFXButton clearButton;
  @FXML private MFXButton submitButton;
  @FXML private MFXButton previewButton;
  @FXML private FlowPane checkoutFlowplan;

  //  @FXML private Text location;
  @FXML private MFXFilterComboBox<String> locationComboBox;
  @FXML private Text shopName;

  @FXML private GesturePane groundFloor;

  @FXML private ImageView groundFloorImage;

  @FXML private ImageView lowerLevel1Image;

  @FXML private ImageView lowerLevel2Image;

  @FXML private ImageView floor1Image;

  @FXML private ImageView floor2Image;

  @FXML private ImageView floor3Image;

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


  @FXML private StackPane selectedFloorPane;


  private ObservableMap<String, Integer > checkoutItems = FXCollections.observableHashMap();

  public void initialize() throws SQLException {
    for (Login login : DAOFacade.getAllLogins()) {
      assignedStaffComboBox.getItems().add(login.getFirstName() + " " + login.getLastName());
    }
    lowerLevel1Image.setImage(App.lowerlevel1);
    lowerLevel2Image.setImage(App.lowerlevel2);
    floor1Image.setImage(App.firstfloor);
    floor2Image.setImage(App.secondfloor);
    floor3Image.setImage(App.thirdfloor);

    floor1Image.setVisible(true);

    /*this.preview.setOnAction(event -> {
      clearAllNodes();
      Circle circle = new Circle();

      try {
        circle = drawCircle(DAOFacade.getNode(this.locationComboBox.getText(), new Date(System.currentTimeMillis())).getXcoord(), DAOFacade.getNode(this.locationComboBox.getText(), new Date(System.currentTimeMillis())).getYcoord());
      } catch (SQLException e) {
        throw new RuntimeException(e);
      }

      String endFloor = null;
      try {
        endFloor = DAOFacade.getNode(this.locationComboBox.getText(), new Date(System.currentTimeMillis())).getFloor();
      } catch (SQLException e) {
        throw new RuntimeException(e);
      }

      if (endFloor != null) {
        switch (endFloor) {
          case "L1":
            L2Group.setVisible(false);
            floor1Group.setVisible(false);
            floor2Group.setVisible(false);
            floor3Group.setVisible(false);

            L1Group.setVisible(true);
            lowerLevel1Image.setVisible(true);
            this.L1Group.getChildren().add(circle);
            break;
          case "L2":
            L1Group.setVisible(false);
            floor1Group.setVisible(false);
            floor2Group.setVisible(false);
            floor3Group.setVisible(false);

            L2Group.setVisible(true);
            lowerLevel2Image.setVisible(true);
            this.L2Group.getChildren().add(circle);
            break;
          case "1":
            L1Group.setVisible(false);
            L2Group.setVisible(false);
            floor2Group.setVisible(false);
            floor3Group.setVisible(false);

            floor1Group.setVisible(true);
            floor1Image.setVisible(true);
            this.floor1Group.getChildren().add(circle);
            break;
          case "2":
            L1Group.setVisible(false);
            L2Group.setVisible(false);
            floor1Group.setVisible(false);
            floor3Group.setVisible(false);

            floor2Group.setVisible(true);
            floor2Image.setVisible(true);
            this.floor2Group.getChildren().add(circle);
            break;
          case "3":
            L1Group.setVisible(false);
            L2Group.setVisible(false);
            floor2Group.setVisible(false);
            floor1Group.setVisible(false);

            floor3Group.setVisible(true);
            floor3Image.setVisible(true);
            this.floor3Group.getChildren().add(circle);
            break;
        }
      }
      Point2D centerpoint = new Point2D(circle.getCenterX(), circle.getCenterY());
      groundFloor.centreOn(centerpoint);

    });*/

    this.checkoutItems = FurnitureDeliveryController.checkoutItems;

    checkoutItems.forEach((key,value)->
    {
      checkoutFlowplan.getChildren().add(createCheckoutNode(key,value));
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

    DateTimeFormatter[] formatters = new DateTimeFormatter[]{
            DateTimeFormatter.ofPattern("MMM dd, yyyy"),
            DateTimeFormatter.ofPattern("MMM d, yyyy")
    };
    try{
      deliveryDateField.setValue(LocalDate.from(formatters[1].parse(formatters[1].format(LocalDateTime.now()))));
    }
    catch(Exception ignored){
      try {
        deliveryDateField.setValue(LocalDate.from(formatters[0].parse(formatters[0].format(LocalDateTime.now()))));
      }
      catch (Exception ignore){

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
          Navigation.navigate(Screen.HOME);
        });

    validUserFirstName.addListener(Validator.generateFormListener(submitButton,
            validUserFirstName, validUserLastName, validDate, validLocation));

    validUserLastName.addListener(Validator.generateFormListener(submitButton,
            validUserFirstName, validUserLastName, validDate, validLocation));

    validDate.addListener(Validator.generateFormListener(submitButton,
            validUserFirstName, validUserLastName, validDate, validLocation));

    validLocation.addListener(Validator.generateFormListener(submitButton,
            validUserFirstName, validUserLastName, validDate, validLocation));

    for(LocationName name:DAOFacade.getAllLocationNames()){
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
    String staffFirst = "";
    String staffLast = "";
    if(!assignedStaffComboBox.getText().isEmpty())
    {
       staffFirst = assignedStaffComboBox.getText().substring(0, i);
       staffLast = assignedStaffComboBox.getText().substring(i+1);
    }
    String pattern = "yyyy-MM-dd";
    DateTimeFormatter df = DateTimeFormatter.ofPattern(pattern);
    String newDate = df.format(deliveryDateField.getValue());
    Date deliveryDate = Date.valueOf(newDate);
    String location = locationComboBox.getText();
    SimpleStringProperty items = new SimpleStringProperty("");
    checkoutItems.forEach((key,value)->
    {
      items.set(items.get() + key + "(" + value + ")\n");
    });
    RequestStatus status = RequestStatus.BLANK;
    if((!assignedStaffComboBox.getText().isEmpty())){
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
