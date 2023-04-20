package edu.wpi.tacticaltritons.entity;

import edu.wpi.tacticaltritons.App;
import edu.wpi.tacticaltritons.auth.UserSessionToken;
import edu.wpi.tacticaltritons.controllers.serviceRequest.MealDeliveryRequestItemsController;
import edu.wpi.tacticaltritons.controllers.serviceRequest.MealDeliverySubmitController;
import edu.wpi.tacticaltritons.database.*;
import edu.wpi.tacticaltritons.navigation.Navigation;
import edu.wpi.tacticaltritons.navigation.Screen;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXDatePicker;
import io.github.palexdev.materialfx.controls.MFXFilterComboBox;
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.animation.FadeTransition;
import javafx.beans.Observable;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.DoubleBinding;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableMap;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.*;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;
import lombok.Getter;
import lombok.Setter;
import net.kurobako.gesturefx.GesturePane;
import org.controlsfx.control.spreadsheet.Grid;

import java.sql.SQLException;
import java.sql.Time;
import java.sql.Date;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.TemporalAmount;
import java.util.*;
import java.util.concurrent.Flow;

/**
 * Entity for meal delivery service request and submission pages.
 *
 * @author Mark Caleca
 */
public class MealDeliveryEntity {
    @Getter
    @Setter
    public DoubleProperty price = new SimpleDoubleProperty(); //total price
    public SimpleStringProperty restaurant = new SimpleStringProperty("Select");
    private SimpleStringProperty items = new SimpleStringProperty("");
    public DoubleProperty imageViewWidth = new SimpleDoubleProperty();
    @Setter
    public DoubleProperty screenX = new SimpleDoubleProperty();
    @Setter
    public DoubleProperty screenY = new SimpleDoubleProperty();

    private ObservableMap<String, Integer> orderList = FXCollections.observableHashMap();

    private Map<String, Double> priceList = new HashMap<>();
    //first name
    private MFXTextField firstName = new MFXTextField();
    //last name
    private MFXTextField lastName = new MFXTextField();
    //patient first name
    private MFXTextField patientFirstName = new MFXTextField();
    //patient last name
    private MFXTextField patientLastName = new MFXTextField();
    //assigned staff member name
    private MFXFilterComboBox<String> staffMemberName = new MFXFilterComboBox<>();
    //room number
    private MFXFilterComboBox<String> room = new MFXFilterComboBox<>();
    //date
    private MFXDatePicker date = new MFXDatePicker();
    //time
    private MFXTextField time = new MFXTextField();
    private Rectangle rectangle;
    private List<MFXTextField> nodes = new ArrayList<>();

    private ImageView groundFloorImage = new ImageView();

    private ImageView lowerLevel1Image = new ImageView();

    private ImageView lowerLevel2Image= new ImageView();

    private ImageView floor1Image= new ImageView();

    private ImageView floor2Image= new ImageView();

    private ImageView floor3Image= new ImageView();

//    private MFXButton preview = new MFXButton("Preview");

    private Group groundGroup = new Group();
    private Group L1Group= new Group();
    private Group L2Group= new Group();
    private Group floor1Group= new Group();
    private Group floor2Group= new Group();
    private Group floor3Group= new Group();

    @Getter
    private List<String> title = new ArrayList<>();//name of restaurant or food
    @Getter
    private List<String> description = new ArrayList<>();//description of restaurant or food

    @Getter
    private List<String> paths = new ArrayList<>();//path to image
    @Getter @Setter public static VBox vBox = new VBox();
    private Button checkoutButton = new Button();
    private Button submitButton = new Button();
    private Button clearButton = new Button();
    private Button cancelButton = new Button();

    //------------------------DATABASE ACCESS------------------------//

    public void readDatabaseRestaurants(VBox vBoxOrderPane) throws SQLException {
        clearLists(vBoxOrderPane);
        for (RequestOptions requestOptions : DAOFacade.getAllOptions()) {
            String name = requestOptions.getRestaurant();
            title.add(name);
            description.add(name + " is a fine eating establishment");
            paths.add("/edu/wpi/tacticaltritons/images/restaurants/" + name + ".png");
        }
    }

    public void readRestaurantItems(String name, VBox vBoxOrderPane) throws SQLException {
        clearLists(vBoxOrderPane);
        for (RequestOptions requestOptions : DAOFacade.getAllOptions(name)) {
            String itemName = requestOptions.getItemName();
            title.add(itemName);
            DecimalFormat df = new DecimalFormat("##.00");
            description.add("Buy some delicious " + itemName + " for only $" + df.format(requestOptions.getPrice()));
            paths.add("/edu/wpi/tacticaltritons/images/restaurants/" + restaurant.get() + "/" + itemName + ".png");
            priceList.put(itemName, requestOptions.getPrice());
        }
    }

    //------------------------LOCAL DATA ACCESS------------------------//

    /**
     * clears restaurant/meal selection and resets form data
     */
    private void clearLists(VBox vBoxOrderPane) {
        title.clear();
        paths.clear();
        description.clear();
        resetFormData(vBoxOrderPane);
        priceList.clear();
    }

    /**
     * clears meal selection and form data but keeps restaurant selection
     */
    private void resetFormData(VBox vBoxOrderPane) {
//        orderList.clear();
        for (String item : orderList.keySet()) {
            orderList.put(item, 0);
        }
        items.set("");
        price.set(0);
        checkoutButton.setDisable(true);
        submitButton.setDisable(true);
        for (MFXTextField node : nodes) {
            node.clear();
        }
        //remove everything except title and price
        if(vBoxOrderPane.getChildren().size() > 2){
/*            for(Node node:vBoxOrderPane.getChildren().subList(2,vBoxOrderPane.getChildren().size())){
                vBoxOrderPane.getChildren().remove(node);
            }*/
            Iterator<Node> iterator = vBoxOrderPane.getChildren().subList(2,vBoxOrderPane.getChildren().size()).iterator();
            while(iterator.hasNext()){
                Node node = iterator.next();
                iterator.remove();
            }

        }
    }

    public void initTextFields(MFXTextField firstName,MFXTextField lastName,MFXTextField patientFirstName,MFXTextField patientLastName,MFXTextField time, MFXFilterComboBox room, MFXFilterComboBox staffMemberName, MFXDatePicker date){
        this.firstName = firstName;
        this.lastName = lastName;
        this.patientFirstName = patientFirstName;
        this.patientLastName = patientLastName;
        this.time = time;
        this.room = room;
        this.staffMemberName = staffMemberName;
        this.date = date;
    }

    /**
     * checks if the meal form is complete
     *
     * @return true if complete, false if any field is blank
     */
    private boolean formComplete() {
        boolean timeComplete = false;//true if time is correctly formatted
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("H:mm");
        try {
            dateTimeFormatter.parse(time.getText());
            timeComplete = true;
        } catch (DateTimeParseException e) {
            timeComplete = false;
        }
        boolean form = !(firstName.getText().isEmpty()
                || lastName.getText().isEmpty()
                || patientFirstName.getText().isEmpty()
                || patientLastName.getText().isEmpty()
                || room.getText().isEmpty()
                || time.getText().isEmpty());
        boolean order = false;
        for (String item : orderList.keySet()) {
            if (orderList.get(item) > 0) { //if any of the items have qty > 0, order has items
                order = true;
            }
        }
        boolean complete = form && timeComplete && order;
        if (complete){
            submitButton.setDisable(false);
        }
        else
            submitButton.setDisable(true);
        return complete;
    }

    private void updatePrice() {
        items.set("");
        price.set(0);
        for (String item : orderList.keySet()) {
            int quantity = orderList.get(item);
            double p = priceList.get(item);
            if (orderList.get(item) > 0) items.set(items.get() + item + " (" + orderList.get(item) + ")\n");
            //TODO on click create new rectangle with item image, description, +/- buttons, and qty
            price.set(price.get() + p * quantity);
        }
    }

    //------------------------Initialize Panes------------------------//

    public void initRestaurant(Stage stage, ScrollPane scrollPane, FlowPane flowPane) throws SQLException {
        init(stage, scrollPane);
        scrollPane.prefWidthProperty().bind(screenX);
        addRestaurants(flowPane);
    }

    public void initItems(Stage stage, ScrollPane scrollPane, FlowPane restaurantPane, VBox orderPane) throws SQLException {
        init(stage, scrollPane);
//        vBoxOrderPane = orderPane;
        scrollPane.prefWidthProperty().bind(screenX.multiply(1.95 / 3));
        readRestaurantItems(restaurant.get(),orderPane);
        restaurantPane.hgapProperty().bind(screenX.divide(128));
        restaurantPane.vgapProperty().bind(screenY.divide(64));
        restaurantPane.prefWidthProperty().bind(screenX.multiply(1.95/3));
//        orderPane.prefWidthProperty().bind(screenX.subtract(restaurantPane.prefWidthProperty().add(40)));
//        orderPane.prefHeightProperty().bind(screenY.subtract(clearButton.heightProperty().add(20)));
        addRectanglePanes(restaurantPane,orderPane,false);
//        orderPane.getChildren().add(vBoxOrderPane);
    }

    public void initCheckout(Stage stage, ScrollPane scrollPane, VBox orderPane, List<MFXTextField> nodes, Button preview) throws SQLException {
        firstName.setText(UserSessionToken.getUser().getFirstname());
        lastName.setText(UserSessionToken.getUser().getLastname());
        orderPane.getChildren().addAll(getVBox().getChildren().subList(2,getVBox().getChildren().size()));
/*
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        time.setText(formatter.format(LocalDateTime.now().plusMinutes(15)));

        formatter = DateTimeFormatter.ofPattern("MMM dd, yyyy");
        date.setValue(LocalDate.from(formatter.parse(formatter.format(LocalDateTime.now()))));*/

        init(stage, scrollPane);
//        scrollPane.prefWidthProperty().bind(screenX.multiply(1.95 / 3));
        initFormPane(nodes,preview);
    }

    /**
     * initialize screen size, panes, and gaps between boxes
     *
     * @param stage      the entire page's Stage
     * @param scrollPane the page's scroll pane for restaurants, items, or form data
     * @throws SQLException catch database retrieval errors
     */
    private void init(Stage stage, ScrollPane scrollPane) throws SQLException {
        //reactive resizing binding to screen size properties
        screenX.bind(stage.widthProperty());
        screenY.bind(stage.heightProperty().subtract(App.getNavBar().heightProperty().add(100)));
        scrollPane.setFitToHeight(true);
        scrollPane.setFitToWidth(true);
        imageViewWidth.bind(screenX.divide(6));
        scrollPane.prefHeightProperty().bind(screenY);
    }

    private void addRestaurants(FlowPane restaurantPane) throws SQLException {
        readDatabaseRestaurants(new VBox());//dummy data
        addRectanglePanes(restaurantPane, true);
        restaurantPane.hgapProperty().bind(screenX.divide(80));
        restaurantPane.vgapProperty().bind(screenY.divide(50));
    }

    private void addToOrderPane(String meal, VBox orderPane) {
        int index = title.indexOf(meal);
        String path = paths.get(index);
        Image image = new Image(path);
        ImageView imageView = new ImageView(image);
        imageView.setPreserveRatio(true);
        imageView.setFitWidth(50);

        Label mealDisplay = new Label(meal);
        mealDisplay.getStyleClass().add("text-general");
//        IntegerProperty num = new SimpleIntegerProperty(orderList.get(meal));
//        ObservableMap<String, Integer> order = FXCollections.observableMap(orderList);//TODO
//        num.bind(Bindings.valueAt(order,meal));
//        int num = (orderList.containsKey(meal)) ? orderList.get(meal) : 1;
        Label qtyDisplay = new Label();
//        qtyDisplay.textProperty().bind(num.asString());
//        qtyDisplay.textProperty().bind(Bindings.valueAt(order,meal).asString());
//        qtyDisplay.textProperty().bind(Bindings.createStringBinding(() -> ));
        qtyDisplay.textProperty().bind(Bindings.valueAt(orderList, meal).asString());
        qtyDisplay.getStyleClass().add("text-general");

        Button minus = new Button("-");
        minus.getStyleClass().add("button-submit");
        Button plus = new Button("+");
        plus.getStyleClass().add("button-submit");

        //        mealDisplay.setPrefWidth(160);
//        mealDisplay.prefWidthProperty().bind(orderPane.prefWidthProperty().divide(2));

        HBox hBox = new HBox(imageView, mealDisplay, minus, qtyDisplay, plus);
        hBox.setSpacing(20);
        mealDisplay.setPadding(new Insets(8,0,0,0));
        qtyDisplay.setPadding(new Insets(8,0,0,0));
//        mealDisplay.setAlignment(Pos.BOTTOM_CENTER);
//        qtyDisplay.setAlignment(Pos.BOTTOM_CENTER);
        orderPane.getChildren().add(hBox);
//        vBoxOrderPane.getChildren().add(hBox);
//        stackPaneHelper.getChildren().addAll(hBox,orderPane);
//        orderPane.getChildren().add(hBox);
        minus.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {

                if (orderList.get(meal) <= 1) {
                    orderPane.getChildren().remove(hBox);
                }
//                else
                orderList.put(meal, orderList.get(meal) - 1);
                updatePrice();
                formComplete();
            }
        });
        plus.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                orderList.put(meal, orderList.get(meal) + 1);
                updatePrice();
                formComplete();
            }
        });
    }

    private void addRectanglePanes(FlowPane flowPane, boolean type) throws SQLException {
        addRectanglePanes(flowPane, null, type);
    }

    /**
     * adds all rectangle selection panes with images and text depending on type
     *
     * @param flowPane the FlowPane to edit
     * @param type     if restaurant (true) create horizontal rectangles, otherwise create vertical ones
     * @throws SQLException database read errors
     */
    private void addRectanglePanes(FlowPane flowPane, VBox orderPane, boolean type) throws SQLException {
        int numBoxes = title.size();

        List<String> restaurants = new ArrayList<>();//accumulator of restaurants already drawn

        //create a rectangle with image and text for each entry
        for (int i = 0; i < numBoxes; i++) {
            boolean skip = false;
            for (String restaurant : restaurants) {
                if (title.get(i).equals(restaurant)) {
                    skip = true;
                }
            }
            if (skip)
                i++;//skip duplicate entries
            else {
                restaurants.add(title.get(i));
                //image
                Image image = new Image(paths.get(i));
                ImageView imageView = new ImageView(image);
                imageView.setPreserveRatio(true);

                //rectangle
                //bounds based on size of image
//                rectangle = new Rectangle(imageView.getFitWidth() * 2, rectangleHeight);
                rectangle = new Rectangle();

                //set title and description
                Text top = new Text(title.get(i));
                Text body = new Text(description.get(i));
                body.getStyleClass().add("text-general");

                StackPane stackPane2;
                VBox vBox = new VBox(top, body);
                vBox.setPadding(new Insets(10));

                if (type) { //if restaurant, rectangle should be horizontal
                    rectangle.getStyleClass().add("rectangle");
                    imageView.fitWidthProperty().bind(Bindings.max(150.0, imageViewWidth));
                    rectangle.widthProperty().bind(Bindings.max(302.0, imageViewWidth.multiply(2)));
                    rectangle.heightProperty().bind(Bindings.max(155.0, imageViewWidth.multiply(1.05)));
                    top.getStyleClass().add("text-header");

                    top.wrappingWidthProperty().bind(rectangle.widthProperty().divide(2.3));
                    body.wrappingWidthProperty().bind(rectangle.widthProperty().divide(2.3));

                    HBox hBox = new HBox(imageView, vBox);
                    hBox.setSpacing(5);
                    hBox.setPadding(new Insets(20, 0, 10, 20));
                    stackPane2 = new StackPane(rectangle, hBox);//combine them
                } else { //meals
                    orderList.put(title.get(i), 0);//TODO new
                    rectangle.getStyleClass().add("rectangle-gray");
                    top.getStyleClass().add("text-general");
                    top.wrappingWidthProperty().bind(rectangle.widthProperty().divide(1.2));
                    body.wrappingWidthProperty().bind(rectangle.widthProperty().divide(1.2));
                    VBox vBox1 = new VBox(imageView, vBox);
                    vBox1.setSpacing(5);
                    vBox1.setPadding(new Insets(10));
                    vBox1.setAlignment(Pos.CENTER);
                    stackPane2 = new StackPane(rectangle, vBox1);
                    imageView.fitHeightProperty().bind(Bindings.max(150.0, imageViewWidth.divide(2)));
                    rectangle.widthProperty().bind(Bindings.max(180, imageViewWidth));
                    rectangle.heightProperty().bind(Bindings.max(180, stackPane2.heightProperty()));

                }

                flowPane.getChildren().add(stackPane2);
                flowPane.setPadding(new Insets(20, 0, 0, 0));


                //on click events
                //for restaurant panes, store name, delete panes, and display food panes
                //for food panes, store order details
                stackPane2.setOnMouseClicked(event -> {
                    if (type) {
                        restaurant.setValue(top.getText());
                        Navigation.navigate(Screen.MEAL_REQUEST);
                    } else {
                        String meal = top.getText();
//                        int qty = 1;
                        //if already contains item, add 1
                        if (orderList.get(meal) == 0) {
//                            qty = orderList.get(meal) + 1;
                            addToOrderPane(meal, orderPane);//only add visual to order pane if it isn't already there
                        }
                        checkoutButton.setDisable(false);
                        orderList.put(meal, orderList.get(meal) + 1);//update based on current quantity

                        //print out new order
                        updatePrice();
                    }
                    //animation on click
                    FadeTransition transition = new FadeTransition(Duration.millis(150), stackPane2);
                    transition.setFromValue(1.0);
                    transition.setToValue(0.3);
                    transition.setCycleCount(2);//allows animation to reverse
                    transition.setAutoReverse(true);
                    transition.play();
                });
            }
            flowPane.setOrientation(Orientation.HORIZONTAL);
            flowPane.setAlignment(Pos.TOP_CENTER);
            flowPane.setColumnHalignment(HPos.LEFT);
            flowPane.setRowValignment(VPos.TOP);
        }
    }

    public void initFormPane(List<MFXTextField> nodes, Button preview) throws SQLException { //TODO resume
/*        rectangle = new Rectangle();
        rectangle.widthProperty().bind(Bindings.max(200, imageViewWidth));
        rectangle.heightProperty().bind(screenY.subtract(30));
        rectangle.getStyleClass().add("rectangle");
        rectangle.setTranslateX(10);
        rectangle.setTranslateY(10);*/

/*        Button clearFormButton = new Button("Clear Form");
        clearFormButton.getStyleClass().add("button-clear");

        preview.getStyleClass().add("button-clear");*/

//        for(LocationName name:db.getAllLocationNames()){
/*        for (LocationName name : DAOFacade.getAllLocationNames()) {
            room.getItems().add(name.getLongName());
        }*/

/*        firstName.setPromptText("Your First Name");
        lastName.setPromptText("Your Last Name");
        patientFirstName.setPromptText("Patient First Name");
        patientLastName.setPromptText("Patient Last Name");
        staffFirstName.setPromptText("Staff Member First Name");
        staffLastName.setPromptText("Staff Member Last Name");
        room.setPromptText("Room Number");
        date.setPromptText("Date");
        time.setPromptText("Time (hh:mm)");*/
        for (MFXTextField node : nodes) {
            node.prefWidthProperty().bind(Bindings.max(200, imageViewWidth));
//            node.setAlignment(Pos.CENTER);
            node.textProperty().addListener(((observable, oldValue, newValue) -> {
                formComplete();//check if re-enable submit button
            }));
//            node.setAlignment(Pos.CENTER);
        }
/*
        VBox vBox = new VBox();
        vBox.getChildren().addAll(nodes);
        HBox hBox = new HBox();
        hBox.getChildren().addAll(clearFormButton,preview);
        hBox.setSpacing(30.0);
        vBox.getChildren().add(hBox);
        vBox.setPadding(new Insets(20));
        vBox.setAlignment(Pos.CENTER);
        vBox.setSpacing(10);
        vBox.setTranslateX(10);
        StackPane stackPane = new StackPane(rectangle, vBox);
        vBox.prefWidthProperty().bind(Bindings.max(200, imageViewWidth));
        stackPane.setAlignment(rectangle, Pos.CENTER_LEFT);
        stackPane.setAlignment(vBox, Pos.CENTER_RIGHT);
        stackPane.prefWidthProperty().bind(Bindings.max(200, imageViewWidth));*/

/*        clearFormButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                for (MFXTextField node : nodes) {
                    node.clear();
                }
            }
        });*/
//
//        //TODO add map pane
//
//        java.util.Date today = new java.util.Date(2023, 4, 10);
//
//        lowerLevel1Image.setImage(App.lowerlevel1);
//        lowerLevel2Image.setImage(App.lowerlevel2);
//        groundFloorImage.setImage(App.groundfloor);
//        floor1Image.setImage(App.firstfloor);
//        floor2Image.setImage(App.secondfloor);
//        floor3Image.setImage(App.thirdfloor);
//
//        Group groundGroup = new Group(groundFloorImage);
//        Group L1Group = new Group(lowerLevel1Image);
//        Group L2Group = new Group(lowerLevel2Image);
//        Group floor1Group = new Group(floor1Image);
//        Group floor2Group = new Group(floor2Image);
//        Group floor3Group = new Group(floor3Image);
//
//        StackPane stack = new StackPane();
//        stack.getChildren().add(groundGroup);
//        stack.getChildren().add(L1Group);
//        stack.getChildren().add(L2Group);
//        stack.getChildren().add(floor1Group);
//        stack.getChildren().add(floor2Group);
//        stack.getChildren().add(floor3Group);
////        stack.prefWidthProperty().bind(MealDeliverySubmitController.gesturePaneWidth);
////        stack.setPrefWidth(500);
////        gesturePane.setPrefWidth(500);
//
////        GesturePane gesturePane = new GesturePane();
////        gesturePane.setPrefHeight(520);
////        gesturePane.setPrefWidth(520);
//
////        gesturePane.prefWidthProperty().bind(formPane.prefWidthProperty());
////        gesturePane.prefWidthProperty().bind(MealDeliverySubmitController.gesturePaneWidth);
////        gesturePane.prefHeightProperty().bind(screenY);
////        gesturePane.setContent(stack);
////        gesturePane.setFitHeight(true);
////        gesturePane.setFitWidth(true);
//        //use min max pref to get border
//        //add margins 20, title at top of
///*
//        FlowPane flowPane = new FlowPane();
//
//        flowPane.setAlignment(Pos.CENTER);
//        flowPane.prefWidthProperty().bind(MealDeliverySubmitController.gesturePaneWidth);
//
//        flowPane.getChildren().add(gesturePane);
//        flowPane.setStyle("-fx-background-color: white; -fx-background-radius: 10");*/
//
////        gesturePane.setVisible(true);
//        floor1Image.setVisible(true);
//        lowerLevel1Image.setVisible(false);
//        lowerLevel2Image.setVisible(false);
//        groundFloorImage.setVisible(false);
//        floor2Image.setVisible(false);
//        floor3Image.setVisible(false);
//
//        preview.setOnAction(event -> {
//            Circle circle;
//
//            try {
//                circle = drawCircle(DAOFacade.getNode(this.room.getText(), today).getXcoord(), DAOFacade.getNode(this.room.getText(), today).getYcoord());
//            } catch (SQLException e) {
//                throw new RuntimeException(e);
//            }
//
//            String endFloor = null;
//            try {
//                endFloor = DAOFacade.getNode(this.room.getText(), today).getFloor();
//            } catch (SQLException e) {
//                throw new RuntimeException(e);
//            }
//            circle.setVisible(true);
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
//            gesturePane.centreOn(centerpoint);
//
//        });

//        formPane.getChildren().addAll(flowPane);
//        formPane.getChildren().add(gesturePane);
//        formPane.prefWidthProperty().bind(MealDeliverySubmitController.gesturePaneWidth);
/*
        formPane.setStyle("-fx-background-color: white;");
        formPane.setOrientation(Orientation.HORIZONTAL);
        formPane.getChildren().addAll(stackPane, flowPane);
        formPane.hgapProperty().bind(screenX.divide(40));
        formPane.vgapProperty().bind(screenY.divide(100));
        formPane.prefWidthProperty().bind(screenX.multiply(1.95 / 3));
        formPane.prefHeightProperty().bind(screenY.subtract(10));*/
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
        circle.toFront();
        circle.setViewOrder(1);
        circle.setFill(Color.RED);
        circle.setStroke(Color.BLACK);
        circle.setStrokeWidth(3.0f);
        circle.setCenterX(x);
        circle.setCenterY(y);
        circle.setRadius(10.0);
        return circle;
    }


    //------------------------Initialize Buttons------------------------//

/*    public void initCheckoutButton(Button button, VBox orderPane) {
        checkoutButton = button;
        checkoutButton.setDisable(true);
        checkoutButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (orderList.size() != 0) {
                    Navigation.navigate(Screen.MEAL_SUBMIT);
                    vBox = orderPane;
                } else {
                    checkoutButton.setDisable(true);//shouldn't be able to reach this point, but just in case
                }
            }
        });
    }*/
    public void initCheckoutButton(Button button, VBox orderPane){
        checkoutButton = button;
        checkoutButton.setDisable(true);
        checkoutButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
//                me.getVBox().getChildren().addAll(orderListPane.getChildren());
//                me.setVBox(getOrderListPane());
                setVBox(orderPane);
                Navigation.navigate(Screen.MEAL_SUBMIT);
            }
        });
    }

    public void initSubmitButton(Button button) {
        submitButton = button;
        submitButton.setVisible(true);
        submitButton.setDisable(true);
        submitButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (formComplete()) {
                    Navigation.navigate(Screen.HOME);
                    //order number is time of submission
                    String pattern = "yyyy-MM-dd";
                    DateTimeFormatter df = DateTimeFormatter.ofPattern(pattern);
                    String newDate = (date.getText().isEmpty()) ? df.format(LocalDate.now()) : df.format(date.getValue());
                    Date sendDate = Date.valueOf(newDate);
                    Time sendTime = Time.valueOf(time.getText() + ":00");

                    RequestStatus status = RequestStatus.BLANK;

                    if ((!staffMemberName.getText().isEmpty())){
                        status = RequestStatus.PROCESSING;
                    }

                    String[] nameWithEmail = staffMemberName.getText().split("/");
                    String firstNameStaff = nameWithEmail[0].split(" ")[0];
                    String lastNameStaff = nameWithEmail[0].split(" ")[1];

                    Meal meal = new Meal(firstName.getText(), lastName.getText(), patientFirstName.getText(),
                            patientLastName.getText(), firstNameStaff, lastNameStaff, sendDate, sendTime,
                            room.getText(), items.get(), (int) price.get(), status);
                    try {
                        DAOFacade.addMeal(meal);
//                        DAOFacade.getAllMealRequests();
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                } else {
                    submitButton.setDisable(true);//shouldn't be able to reach this point, but just in case
                }
            }
        });
    }

    public void initClearButton(Button button, VBox vBoxOrderPane) {
        clearButton = button;
        clearButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                resetFormData(vBoxOrderPane);
            }
        });
    }

    public void initCancelButton(Button button,VBox vBoxOrderPane){
        cancelButton = button;
        cancelButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                clearLists(vBoxOrderPane);
                Navigation.navigate(Screen.HOME);
            }
        });
    }
}