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
import io.github.palexdev.materialfx.dialogs.MFXGenericDialog;
import io.github.palexdev.materialfx.dialogs.MFXGenericDialogBuilder;
import io.github.palexdev.materialfx.dialogs.MFXStageDialog;
import io.github.palexdev.materialfx.enums.ScrimPriority;
import javafx.animation.FadeTransition;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
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
import javafx.stage.Modality;
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
    //observable properties for display data and resizing
    public DoubleProperty price = new SimpleDoubleProperty(); //total price
    public SimpleStringProperty restaurant = new SimpleStringProperty("Select");
    private SimpleStringProperty items = new SimpleStringProperty("");
    public DoubleProperty imageViewWidth = new SimpleDoubleProperty();
    public DoubleProperty screenX = new SimpleDoubleProperty();
    public DoubleProperty screenY = new SimpleDoubleProperty();
    private ObservableMap<String, Integer> orderList = FXCollections.observableHashMap();

    //store data and user enter data
    private Map<String, Double> priceList = new HashMap<>();
    private List<String> title = new ArrayList<>();//name of restaurant or food
    private List<String> description = new ArrayList<>();//description of restaurant or food
    private List<String> paths = new ArrayList<>();//path to image
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
    @Getter
    @Setter
    public static VBox vBox = new VBox(); //order pane vBox to store between pages
    //UI buttons
    private Button checkoutButton = new Button();
    private Button submitButton = new Button();
    private Button clearButton = new Button();
    private Button cancelButton = new Button();


    //------------------------DATABASE ACCESS------------------------//

    /**
     * read database for all available restaurants
     *
     * @param vBoxOrderPane the VBox to display the order in (needs to be cleared)
     * @throws SQLException handle errors reading the database
     */
    public void readDatabaseRestaurants(VBox vBoxOrderPane) throws SQLException {
        clearLists(vBoxOrderPane);
        for (RequestOptions requestOptions : DAOFacade.getAllOptions()) {
            String name = requestOptions.getRestaurant();
            title.add(name);
            description.add(name + " is a fine eating establishment");
            paths.add("/edu/wpi/tacticaltritons/images/restaurants/" + name + ".png");
        }
    }

    /**
     * read database for all items available from a restaurant
     *
     * @param name          the name of the restaurant selected
     * @param vBoxOrderPane the VBox to display the order in (needs to be cleared)
     * @throws SQLException handle errors reading the database
     */
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
        if (vBoxOrderPane.getChildren().size() > 2) {
            Iterator<Node> iterator = vBoxOrderPane.getChildren().subList(2, vBoxOrderPane.getChildren().size()).iterator();
            while (iterator.hasNext()) {
                Node node = iterator.next();
                iterator.remove();
            }

        }
    }

    /**
     * initialize the text fields for the checkout page
     *
     * @param firstName        text field for first name
     * @param lastName         text field for last name
     * @param patientFirstName text field for patient first name
     * @param patientLastName  text field for patient last name
     * @param time             text field for time
     * @param room             dropdown for room locations
     * @param staffMemberName  dropdown for staff member name
     * @param date             date picker
     */
    public void initTextFields(MFXTextField firstName, MFXTextField lastName, MFXTextField patientFirstName, MFXTextField patientLastName, MFXTextField time, MFXFilterComboBox room, MFXFilterComboBox staffMemberName, MFXDatePicker date) {
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
     * checks if the meal form is complete, and if not disable the submit button
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
        if (complete) {
            submitButton.setDisable(false);
        } else
            submitButton.setDisable(true);
        return complete;
    }

    /**
     * calculate the new total order price and set observable price to that value
     */
    private void updatePrice() {
        items.set("");
        price.set(0);
        for (String item : orderList.keySet()) {
            int quantity = orderList.get(item);
            double p = priceList.get(item);
            if (orderList.get(item) > 0) items.set(items.get() + item + " (" + orderList.get(item) + ")\n");
            price.set(price.get() + p * quantity);
        }
    }

    //------------------------Initialize Panes------------------------//

    /**
     * initialize restaurant options from database with rectangles, images
     *
     * @param stage      the stage of the page (for resizing purposes)
     * @param scrollPane the scroll pane for the page (for resizing purposes)
     * @param flowPane   the flow pane to display restaurant options on
     * @throws SQLException handle errors reading the database
     */
    public void initRestaurant(Stage stage, ScrollPane scrollPane, FlowPane flowPane) throws SQLException {
        init(stage, scrollPane); //resizing
        scrollPane.prefWidthProperty().bind(screenX); //resizing
        addRestaurants(flowPane); //view options on screen
    }

    /**
     * initialize item options on the screen from database with rectangles, images
     *
     * @param stage          the stage of the page (for resizing purposes)
     * @param scrollPane     the scroll pane for the part of the page holding items (for resizing purposes)
     * @param restaurantPane the flow pane to display restaurant options on
     * @param orderPane      the VBox to hold items selected, quantity, and price
     * @throws SQLException handle errors reading the database
     */
    public void initItems(Stage stage, ScrollPane scrollPane, FlowPane restaurantPane, VBox orderPane) throws SQLException {
        init(stage, scrollPane);
        scrollPane.prefWidthProperty().bind(screenX.multiply(1.95 / 3));
        readRestaurantItems(restaurant.get(), orderPane);
        restaurantPane.hgapProperty().bind(screenX.divide(128));
        restaurantPane.vgapProperty().bind(screenY.divide(64));
        restaurantPane.prefWidthProperty().bind(screenX.multiply(1.95 / 3));
        addRectanglePanes(restaurantPane, orderPane, false);
    }

    /**
     * @param stage      the stage of the page (for resizing purposes)
     * @param scrollPane the scroll pane for the part of the page holding the form (for resizing purposes)
     * @param orderPane  the VBox to hold items selected, quantity, and price
     * @param nodes      the list of text fields to hold user input for the form
     * @throws SQLException
     */
    public void initCheckout(Stage stage, ScrollPane scrollPane, VBox orderPane, List<MFXTextField> nodes) throws SQLException {
        firstName.setText(UserSessionToken.getUser().getFirstname());
        lastName.setText(UserSessionToken.getUser().getLastname());
        orderPane.getChildren().addAll(getVBox().getChildren().subList(2, getVBox().getChildren().size()));
        init(stage, scrollPane);
        initFormPane(nodes);
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

    /**
     * add restaurant options and rectangles to the screen
     *
     * @param restaurantPane the flow pane to add the options to
     * @throws SQLException handle database read errors
     */
    private void addRestaurants(FlowPane restaurantPane) throws SQLException {
        readDatabaseRestaurants(new VBox());//since there's no order pane on this page, don't need to clear it
        addRectanglePanes(restaurantPane, true);

        //resizing
        restaurantPane.hgapProperty().bind(screenX.divide(80));
        restaurantPane.vgapProperty().bind(screenY.divide(50));
    }

    /**
     * add a new item to the order pane with image, name, quantity, and +/- buttons
     *
     * @param meal      the meal name to add
     * @param orderPane the VBox to add the item information to
     */
    private void addToOrderPane(String meal, VBox orderPane) {
        //image display
        String path = paths.get(title.indexOf(meal)); //store file path of image
        Image image = new Image(path);
        ImageView imageView = new ImageView(image);
        imageView.setPreserveRatio(true);
        imageView.setFitWidth(50);

        //meal name and quantity display
        Label mealDisplay = new Label(meal);
        mealDisplay.getStyleClass().add("text-general");
        Label qtyDisplay = new Label();
        qtyDisplay.textProperty().bind(Bindings.valueAt(orderList, meal).asString());
        qtyDisplay.getStyleClass().add("text-general");

        //plus and minus button display
        Button minus = new Button("-");
        minus.getStyleClass().add("button-submit");
        Button plus = new Button("+");
        plus.getStyleClass().add("button-submit");

        //create a bounding hbox for each item to hold image, text, and buttons
        HBox hBox = new HBox(imageView, mealDisplay, minus, qtyDisplay, plus);
        hBox.setSpacing(20);
        mealDisplay.setPadding(new Insets(8, 0, 0, 0));
        qtyDisplay.setPadding(new Insets(8, 0, 0, 0));
        orderPane.getChildren().add(hBox);

        //configure plus and minus button functionality
        minus.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {

                if (orderList.get(meal) <= 1) { //remove item if quantity reaches zero
                    orderPane.getChildren().remove(hBox);
                }
                orderList.put(meal, orderList.get(meal) - 1); //update quantity of item displayed and on hashmap of order
                updatePrice(); //update price based on new quantity
                formComplete(); //check if there are still items in the order, otherwise disable the submit button
            }
        });
        plus.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                orderList.put(meal, orderList.get(meal) + 1); //update quantity of item displayed and on hashmap of order
                updatePrice(); //update price based on new quantity
            }
        });
    }

    /**
     * add rectangles with items / restaurants to a page if it doesn't have an order pane
     *
     * @param flowPane the flow pane to add to
     * @param type     if restaurant (true) create horizontal rectangles, otherwise create vertical ones
     * @throws SQLException
     */
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

                //rectangle as background for item
                rectangle = new Rectangle();

                //set title and description
                Text top = new Text(title.get(i));
                Text body = new Text(description.get(i));
                body.getStyleClass().add("text-general");

                StackPane stackPane2; //stack text and rectangle
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
                    orderList.put(title.get(i), 0);
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
                        //if already contains item, add 1
                        if (orderList.get(meal) == 0) {
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

    /**
     * initialize checkout page form text fields
     *
     * @param nodes the list of text fields to store data
     * @throws SQLException handle database read errors
     */
    public void initFormPane(List<MFXTextField> nodes) throws SQLException {
        for (MFXTextField node : nodes) {
            node.prefWidthProperty().bind(Bindings.max(200, imageViewWidth));
            node.textProperty().addListener(((observable, oldValue, newValue) -> {
                formComplete();//check if re-enable submit button
            }));
        }
    }


    //------------------------Initialize Buttons------------------------//

    /**
     * initialize the checkout button on the item page to go to the checkout page
     *
     * @param button    pass in the button from scene builder
     * @param orderPane the order VBox to store data
     */
    public void initCheckoutButton(Button button, VBox orderPane) {
        checkoutButton = button;
        checkoutButton.setDisable(true);
        checkoutButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                setVBox(orderPane);
                Navigation.navigate(Screen.MEAL_SUBMIT);
            }
        });
    }

    /**
     * initialize the submit button on the checkout page to submit data to the database
     *
     * @param button pass in the button from scene builder
     */
    public void initSubmitButton(Button button) {
        submitButton = button;
        submitButton.setVisible(true);
        submitButton.setDisable(true);
        submitButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (formComplete()) {
                    MFXGenericDialog content = new MFXGenericDialog();
                    MFXStageDialog stageDialog = new MFXStageDialog();
                    stageDialog = MFXGenericDialogBuilder.build(content)
                            .toStageDialogBuilder()
                            .initOwner(App.getPrimaryStage())
                            .initModality(Modality.APPLICATION_MODAL)
                            .setDraggable(false)
                            .setTitle("Dialogs Preview")
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
                    //convert data for database
                    //order number is time of submission
                    String pattern = "yyyy-MM-dd";
                    DateTimeFormatter df = DateTimeFormatter.ofPattern(pattern);
                    String newDate = (date.getText().isEmpty()) ? df.format(LocalDate.now()) : df.format(date.getValue());
                    Date sendDate = Date.valueOf(newDate);
                    Time sendTime = Time.valueOf(time.getText() + ":00");

                    RequestStatus status = RequestStatus.BLANK;

                    if ((!staffMemberName.getText().isEmpty())) {
                        status = RequestStatus.PROCESSING;
                    }
                    String firstNameStaff = "", lastNameStaff = "";

                    String[] nameWithEmail = staffMemberName.getText().split("/");
                    if (nameWithEmail.length > 1) {
                        firstNameStaff = nameWithEmail[0].split(" ")[0];
                        lastNameStaff = nameWithEmail[0].split(" ")[1];
                    }

                    Meal meal = new Meal(firstName.getText(), lastName.getText(), patientFirstName.getText(),
                            patientLastName.getText(), firstNameStaff, lastNameStaff, sendDate, sendTime,
                            room.getText(), items.get(), (int) price.get(), status);
                    try {
                        DAOFacade.addMeal(meal);
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                } else {
                    submitButton.setDisable(true);//shouldn't be able to reach this point, but just in case
                }
            }
        });
    }

    /**
     * initialize the clear button to delete order
     *
     * @param button        pass in the button from scene builder
     * @param vBoxOrderPane the order VBox to store data
     */
    public void initClearButton(Button button, VBox vBoxOrderPane) {
        clearButton = button;
        clearButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                resetFormData(vBoxOrderPane);
            }
        });
    }

    /**
     * initialize the cancel button to delete the order and go back to the home screen
     *
     * @param button        pass in the button from scene builder
     * @param vBoxOrderPane the order VBox to store data
     */
    public void initCancelButton(Button button, VBox vBoxOrderPane) {
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