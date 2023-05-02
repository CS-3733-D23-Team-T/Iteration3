package edu.wpi.tacticaltritons.controllers.serviceRequest;

import edu.wpi.tacticaltritons.App;
import edu.wpi.tacticaltritons.database.DAOFacade;
import edu.wpi.tacticaltritons.database.RequestOptions;
import edu.wpi.tacticaltritons.navigation.Navigation;
import edu.wpi.tacticaltritons.navigation.Screen;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXScrollPane;
import javafx.beans.binding.Bindings;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableMap;
import javafx.fxml.FXML;
import javafx.geometry.*;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MealDeliveryController {

    private String shopName;
    @FXML
    private Label checkoutLabel;
    @FXML
    private Label priceLable;
    @FXML
    public FlowPane checkoutFlowpane;
    @FXML
    private MFXButton clearButton;
    @FXML
    private MFXButton checkoutButton;
    @FXML
    private BorderPane infoBoardPane;
    @FXML
    private BorderPane basePane;
    private final TabPane tabPane = new TabPane();

    static public ObservableMap<String, Integer> checkoutItems = FXCollections.observableHashMap();

    static public double total;
    public ObservableMap<String, Double> priceOfItems = FXCollections.observableHashMap();
    private List<RequestOptions> mealRequestOptionsList;

    private final double noramlWidth = 1280;
    private final double normalHeight = 720;
    private final double defaultFlowPanePrefWidth = 200;
    private final double defaultFlowPanePrefHeight = 400;
    private final double defaultImageViewFitWidth = 150;
    private final double defaultImageViewFitHeight = 150;
    private final double defaultTitleFontSize = 20;
    private final double defaultTitleHeight = 50;
    private final double defaultDiscriptionFontSize = 15;
    private final double defaultDiscriptionHeight = 50;

    @FXML
    public void initialize() {

        this.shopName = MealChoiceController.name;
        // Sets the label to the name of the shop that was selected
        checkoutLabel.setText(shopName);

        try {
            mealRequestOptionsList = DAOFacade.getAllOptions();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        ArrayList<RequestOptions> shopItems = getItems(mealRequestOptionsList);
        HashMap<String, String> numberOfTabs = getNumberOfTables(shopItems);


        // Create the individual tabs based on the number of types of items that the shop has
        numberOfTabs.forEach((key, value) ->
        {
            System.out.println(value);
            Tab tab = new Tab(value);
            tab.setId(value);
            ScrollPane scrollPane = createShopIteams(shopItems, value, App.mealHashMap);
            tab.setContent(scrollPane);
            tabPane.getTabs().add(tab);
            tabPane.getStyleClass().add("tab-pane");
        });

        // this makes it so that the user can not close any of the tabs then addeds it to the boardpane for resizeablity
        tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);
        infoBoardPane.setCenter(tabPane);

        //clears
        clearButton.setOnAction(event ->
        {
            priceOfItems.clear();
            checkoutItems.clear();
            updatePriceRequest();
            checkoutFlowpane.getChildren().clear();
        });

        checkoutButton.setOnAction(event ->
        {
            if (checkoutFlowpane.getChildren().size() > 0) {
                total = Double.parseDouble(priceLable.getText());
                Navigation.navigate(Screen.MEAL_SUBMIT);
                priceOfItems.clear();
            }
        });
    }

    private MFXScrollPane createShopIteams(ArrayList<RequestOptions> MealRequestOptionsArrayList, String value, HashMap<String, Image> imageHashMap) {
        int counter = 0;
        MFXScrollPane scrollPane = new MFXScrollPane();
        scrollPane.setPrefWidth(600);

        FlowPane mainFlowPane = new FlowPane();
        mainFlowPane.setPrefHeight(150);
        mainFlowPane.setOrientation(Orientation.HORIZONTAL);
        mainFlowPane.setRowValignment(VPos.CENTER);
        mainFlowPane.setColumnHalignment(HPos.CENTER);
        mainFlowPane.setAlignment(Pos.TOP_LEFT);


        for (RequestOptions options : MealRequestOptionsArrayList) {
            if (options.getItemType().equals(value)) {
                counter++;
                // Creates the outer flowpane to hold all the infomation

                FlowPane flowPane = new FlowPane();
                flowPane.setPrefWidth(defaultFlowPanePrefWidth);
                flowPane.setPrefHeight(defaultFlowPanePrefHeight);
                flowPane.setOrientation(Orientation.VERTICAL);
                flowPane.setRowValignment(VPos.CENTER);
                flowPane.setColumnHalignment(HPos.CENTER);
                flowPane.setAlignment(Pos.TOP_CENTER);
                flowPane.setBackground(Background.fill(Color.WHITE));
                flowPane.setMargin(flowPane, new Insets(20, 20, 20, 20));

                // Creates the image view
                Image image = imageHashMap.get(options.getItemName());
                ImageView imageView = new ImageView(image);
                imageView.setFitWidth(defaultImageViewFitWidth);
                imageView.setFitHeight(defaultImageViewFitHeight);
                imageView.setPreserveRatio(true);

                // creates the Shope name lable
                Label itemTitle = new Label();
                itemTitle.setText(options.getItemName());
                itemTitle.setWrapText(true);
                itemTitle.setFont(new Font(defaultTitleFontSize));
                itemTitle.setAlignment(Pos.CENTER);
                itemTitle.setTextAlignment(TextAlignment.CENTER);
                itemTitle.setPrefWidth(defaultFlowPanePrefWidth);

                Label price = new Label();
                price.setPrefWidth(flowPane.getPrefWidth());
                price.setText(Double.toString(options.getPrice()));
                price.setFont(new Font(defaultDiscriptionFontSize));
                price.setWrapText(true);
                price.setPadding(new Insets(10, 10, 10, 10));
                price.setPrefWidth(defaultFlowPanePrefWidth);
                price.setAlignment(Pos.CENTER);

                //creates the discription label
                Label discriptionLabel = new Label();
                discriptionLabel.setPrefWidth(flowPane.getPrefWidth());
                discriptionLabel.setText(options.getItemDescription());
                discriptionLabel.setFont(new Font(defaultDiscriptionFontSize));
                discriptionLabel.setWrapText(true);
                discriptionLabel.setPadding(new Insets(0, 10, 0, 10));
                discriptionLabel.setPrefWidth(defaultFlowPanePrefWidth);
                discriptionLabel.prefHeightProperty().bind(Bindings.divide(flowPane.heightProperty(), 5));


                flowPane.getChildren().add(itemTitle);
                flowPane.getChildren().add(price);
                flowPane.getChildren().add(imageView);
                flowPane.getChildren().add(discriptionLabel);
                flowPane.setStyle("-fx-background-radius: 10; -fx-background-color: white");

                flowPane.setOnMouseClicked(event ->
                {
                    updatedCheckoutBox(options, imageHashMap);
                });
                mainFlowPane.getChildren().add(flowPane);

                scrollPane.widthProperty().addListener((observable, oldValue, newValue) ->
                {
                    flowPane.setPrefWidth((defaultFlowPanePrefWidth * newValue.doubleValue()) / 700);
                    price.setPrefWidth((defaultFlowPanePrefWidth * newValue.doubleValue()) / 700);
                    itemTitle.setPrefWidth((defaultFlowPanePrefWidth * newValue.doubleValue()) / 700);
                    discriptionLabel.setPrefWidth((defaultFlowPanePrefWidth * newValue.doubleValue()) / 700);
                    imageView.setFitWidth(defaultFlowPanePrefWidth * newValue.doubleValue() / 700);

                });

                scrollPane.heightProperty().addListener((observable, oldValue, newValue) ->
                {
                    flowPane.setPrefHeight((defaultFlowPanePrefHeight * newValue.doubleValue()) / 680);
                    price.setPrefHeight((defaultTitleHeight * newValue.doubleValue()) / 680);
                    itemTitle.setPrefHeight((defaultTitleHeight * newValue.doubleValue()) / 680);
                    discriptionLabel.prefHeightProperty().bind(Bindings.divide(flowPane.heightProperty(), 5));

                    itemTitle.setText(options.getItemName());
                    itemTitle.setFont(new Font((defaultTitleFontSize * newValue.doubleValue()) / 680));

                    price.setText(Double.toString(options.getPrice()));
                    price.setFont(new Font((defaultDiscriptionFontSize * newValue.doubleValue()) / 680));

                    discriptionLabel.setText(options.getItemDescription());
                    discriptionLabel.setFont(new Font((defaultDiscriptionFontSize * newValue.doubleValue()) / 680));
                });
            }
        }
        mainFlowPane.setPrefWidth((defaultFlowPanePrefWidth + 40) * counter);
        scrollPane.widthProperty().addListener((observable, oldValue, newValue) ->
        {
            mainFlowPane.setPrefWidth(newValue.doubleValue());
        });
        scrollPane.setContent(mainFlowPane);
        return scrollPane;
    }

    private void updatedCheckoutBox(RequestOptions options, HashMap<String, Image> imageHashMap) {
        if (!checkoutItems.containsKey(options.getItemName())) {
            checkoutItems.put(options.getItemName(), 1);
            priceOfItems.put(options.getItemName(), options.getPrice());
            FlowPane flowPane = createCheckoutNode(options, imageHashMap.get(options.getItemName()));
            checkoutFlowpane.getChildren().add(flowPane);
        } else {
            checkoutItems.put(options.getItemName(), checkoutItems.get(options.getItemName()) + 1);
        }
        updatePriceRequest();
    }

    private void updatePriceRequest() {
        DoubleProperty totalPrice = new SimpleDoubleProperty();

        checkoutItems.forEach((key, value) ->
        {
            totalPrice.set(totalPrice.get() + value * priceOfItems.get(key));
        });
        priceLable.setText(String.format("%.2f", totalPrice.get()));

    }

    private FlowPane createCheckoutNode(RequestOptions options, Image flowerImage) {
        FlowPane flowPane = new FlowPane();
        flowPane.setPrefWidth(400);
        flowPane.setPrefHeight(100);
        flowPane.setOrientation(Orientation.VERTICAL);
        flowPane.setRowValignment(VPos.CENTER);
        flowPane.setColumnHalignment(HPos.CENTER);
        flowPane.setAlignment(Pos.CENTER);
        flowPane.setPadding(new Insets(20, 20, 20, 20));
        FlowPane.setMargin(flowPane, new Insets(20, 20, 20, 20));
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
        itemTitle.setText(options.getItemName());
        itemTitle.setFont(new Font(16));
        itemTitle.setWrapText(true);
        itemTitle.setPadding(new Insets(0, 10, 0, 10));

        Label quantity = new Label();
        quantity.setPrefWidth(50);
        quantity.setPrefHeight(50);
        quantity.setFont(new Font(15));
        quantity.setAlignment(Pos.CENTER);
        quantity.textProperty().bind(Bindings.valueAt(checkoutItems, options.getItemName()).asString());

        Button sub = new Button();
        sub.setText("-");
        sub.setFont(new Font(15));
        sub.setTextFill(Color.WHITE);
        sub.setPrefWidth(30);
        sub.setPrefHeight(30);
        sub.setStyle("-fx-background-radius: 10");
        sub.setStyle("-fx-background-color: #002d59");
        sub.setOnMouseClicked(event ->
        {
            checkoutItems.put(options.getItemName(), checkoutItems.get(options.getItemName()) - 1);
            if (Integer.parseInt(quantity.getText()) < 1) {
                checkoutFlowpane.getChildren().remove(flowPane);
                checkoutItems.remove(options.getItemName());
                priceOfItems.remove(options.getItemName());
            }
            updatePriceRequest();
        });

        Button add = new Button();
        add.setText("+");
        add.setFont(new Font(15));
        add.setTextFill(Color.WHITE);
        add.setPrefWidth(30);
        add.setPrefHeight(30);
        add.setStyle("-fx-background-radius: 10");
        add.setStyle("-fx-background-color: #002d59");
        add.setOnMouseClicked(event ->
        {
            checkoutItems.put(options.getItemName(), checkoutItems.get(options.getItemName()) + 1);
            updatePriceRequest();
        });

        flowPane.getChildren().add(imageView);
        flowPane.getChildren().add(itemTitle);
        flowPane.getChildren().add(sub);
        flowPane.getChildren().add(quantity);
        flowPane.getChildren().add(add);
        return flowPane;
    }

    private HashMap<String, String> getNumberOfTables(ArrayList<RequestOptions> shopItems) {
        HashMap<String, String> numberOfTabs = new HashMap<>();
        for (RequestOptions options : shopItems) {
            if (options.getRestaurant().equals(shopName)) {
                numberOfTabs.putIfAbsent(options.getItemType(), options.getItemType());
            }
        }
        return numberOfTabs;
    }

    private ArrayList<RequestOptions> getItems(List<RequestOptions> mealRequestOptionsList) {
        ArrayList<RequestOptions> shopIteams = new ArrayList<>();

        for (RequestOptions options : mealRequestOptionsList) {
            if (options.getRestaurant().equals(shopName)) {
                shopIteams.add(options);
            }
        }
        return shopIteams;
    }

}
