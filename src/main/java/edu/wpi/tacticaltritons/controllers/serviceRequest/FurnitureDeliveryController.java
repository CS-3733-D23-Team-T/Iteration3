package edu.wpi.tacticaltritons.controllers.serviceRequest;

import edu.wpi.tacticaltritons.App;
import edu.wpi.tacticaltritons.auth.Validator;
import edu.wpi.tacticaltritons.database.DAOFacade;
import edu.wpi.tacticaltritons.database.FurnitureRequestOptions;
import edu.wpi.tacticaltritons.navigation.Navigation;
import edu.wpi.tacticaltritons.navigation.Screen;
import edu.wpi.tacticaltritons.styling.GoogleTranslate;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXFilterComboBox;
import io.github.palexdev.materialfx.controls.MFXScrollPane;
import javafx.beans.binding.Bindings;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleBooleanProperty;
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
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class FurnitureDeliveryController {
    private String shopName;
    @FXML
    private Label checkoutLabel;
    @FXML Text furnitureFromText;
    @FXML
    public FlowPane checkoutFlowpane;
    @FXML
    private Text itemValidator;
    @FXML
    private MFXButton clearButton;
    @FXML
    private MFXButton checkoutButton;
    @FXML
    private BorderPane infoBoardPane;
    private TabPane tabPane = new TabPane();

    private final double defaultFlowPanePrefWidth = 200;
    private final double defaultFlowPanePrefHeight = 400;
    private final double defaultImageViewFitWidth = 150;
    private final double defaultImageViewFitHeight = 150;
    private final double defaultTitleFontSize = 20;
    private final double defaultTitleHeight = 50;
    private final double defaultDiscriptionFontSize = 15;

    static public ObservableMap<String, Integer> checkoutItems = FXCollections.observableHashMap();

    private List<FurnitureRequestOptions> furnitureRequestOptionsList;

    @FXML
    public void initialize() throws IOException {
        checkoutItems = FXCollections.observableHashMap();
        furnitureRequestOptionsList = new ArrayList<>();

        furnitureFromText.setText(GoogleTranslate.getString("furnitureFrom"));
        checkoutButton.setText(GoogleTranslate.getString("checkout"));
        clearButton.setText(GoogleTranslate.getString("clear"));
        itemValidator.setText(GoogleTranslate.getString("itemValidator"));


        BooleanProperty validItems = new SimpleBooleanProperty(false);
        checkoutFlowpane.getChildren().addListener(Validator.generateFormListener(validItems, checkoutButton, 1,
                itemValidator.getText(), itemValidator));

        this.shopName = "Storage";
        // Sets the label to the name of the shop that was selected
        checkoutLabel.setText(GoogleTranslate.getString(shopName));

        try {
            furnitureRequestOptionsList = DAOFacade.getAllFurnitureRequestOptions();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        ArrayList<FurnitureRequestOptions> shopItems = getFurnitureItems(furnitureRequestOptionsList);
        HashMap<String, String> numberOfTabs = getNumberOfTables(shopItems);


        // Create the individual tabs based on the number of types of items that the shop has
        numberOfTabs.forEach((key, value) ->
        {
            Tab tab = null;
            try {
                tab = new Tab(GoogleTranslate.translate(value));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            tab.setId(value);
            ScrollPane scrollPane = null;
            try {
                scrollPane = createShopItems(shopItems, value, App.furnitureHashMap);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            tab.setContent(scrollPane);
            tabPane.getTabs().add(tab);
            tabPane.getStyleClass().add("tab-pane");
        });

        // this makes it so that the user can not close any of the tabs then addeds it to the boardpane for resizeablity
        tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);
        infoBoardPane.setCenter(tabPane);


        clearButton.setOnAction(event ->
        {
            checkoutItems.clear();
            checkoutFlowpane.getChildren().clear();
        });

        checkoutButton.setOnAction(event ->
        {
            Navigation.navigate(Screen.FURNITURE_CHECKOUT);

        });
    }

    private ScrollPane createShopItems(ArrayList<FurnitureRequestOptions> furnitureRequestOptionsArrayList, String value, HashMap<String, Image> imageHashMap) throws IOException {
        int counter = 0;
        MFXScrollPane scrollPane = new MFXScrollPane();
        scrollPane.setPrefWidth(600);

        FlowPane mainFlowPane = new FlowPane();
        mainFlowPane.setPrefHeight(150);
        mainFlowPane.setOrientation(Orientation.HORIZONTAL);
        mainFlowPane.setRowValignment(VPos.CENTER);
        mainFlowPane.setColumnHalignment(HPos.CENTER);
        mainFlowPane.setAlignment(Pos.TOP_LEFT);


        for (FurnitureRequestOptions options : furnitureRequestOptionsArrayList) {
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
                flowPane.setMargin(flowPane, new Insets(20, 20, 20, 20));
                flowPane.setBackground(Background.fill(Color.WHITE));


                // Creates the image view
                Image image = imageHashMap.get(options.getItemName());
                ImageView imageView = new ImageView(image);
                imageView.setFitWidth(defaultImageViewFitWidth);
                imageView.setFitHeight(defaultImageViewFitHeight);
                imageView.setPreserveRatio(true);


                // creates the Shope name lable
                Label itemTitle = new Label();
                itemTitle.setText(GoogleTranslate.translate(options.getItemName()));
                itemTitle.setWrapText(true);
                itemTitle.setFont(new Font(defaultTitleFontSize));
                itemTitle.setAlignment(Pos.CENTER);
                itemTitle.setTextAlignment(TextAlignment.CENTER);
                itemTitle.setPrefWidth(defaultFlowPanePrefWidth);


                //creates the discription label
                Label discriptionLabel = new Label();
                discriptionLabel.setPrefWidth(flowPane.getPrefWidth());
                discriptionLabel.setText(GoogleTranslate.translate(options.getItemDescription()));
                discriptionLabel.setFont(new Font(defaultDiscriptionFontSize));
                discriptionLabel.setWrapText(true);
                discriptionLabel.setPadding(new Insets(0, 10, 0, 10));
                discriptionLabel.setPrefWidth(defaultFlowPanePrefWidth);
                discriptionLabel.prefHeightProperty().bind(Bindings.divide(flowPane.heightProperty(), 5));

                flowPane.getChildren().add(itemTitle);
                flowPane.getChildren().add(imageView);
                flowPane.getChildren().add(discriptionLabel);
                flowPane.setStyle("-fx-background-radius: 10; -fx-background-color: white");

                flowPane.setOnMouseClicked(event ->
                {
                    try {
                        updatedCheckoutBox(options, imageHashMap);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                });
                mainFlowPane.getChildren().add(flowPane);

                scrollPane.widthProperty().addListener((observable, oldValue, newValue) ->
                {
                    flowPane.setPrefWidth((defaultFlowPanePrefWidth * newValue.doubleValue()) / 700);
                    itemTitle.setPrefWidth((defaultFlowPanePrefWidth * newValue.doubleValue()) / 700);
                    discriptionLabel.setPrefWidth((defaultFlowPanePrefWidth * newValue.doubleValue()) / 700);
                    imageView.setFitWidth(defaultFlowPanePrefWidth * newValue.doubleValue() / 700);

                });

                scrollPane.heightProperty().addListener((observable, oldValue, newValue) ->
                {
                    flowPane.setPrefHeight((defaultFlowPanePrefHeight * newValue.doubleValue()) / 680);
                    itemTitle.setPrefHeight((defaultTitleHeight * newValue.doubleValue()) / 680);
                    discriptionLabel.prefHeightProperty().bind(Bindings.divide(flowPane.heightProperty(), 5));

                    try {
                        itemTitle.setText(GoogleTranslate.translate(options.getItemName()));
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    itemTitle.setFont(new Font((defaultTitleFontSize * newValue.doubleValue()) / 680));

                    try {
                        discriptionLabel.setText(GoogleTranslate.translate(options.getItemDescription()));
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
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

    private void updatedCheckoutBox(FurnitureRequestOptions options,HashMap<String, Image> imageHashMap ) throws IOException {
        if (!checkoutItems.containsKey(options.getItemName())) {
            checkoutItems.put(options.getItemName(), 1);
            FlowPane flowPane = createCheckoutNode(options, imageHashMap.get(options.getItemName()));
            checkoutFlowpane.getChildren().add(flowPane);
        } else {
            checkoutItems.put(options.getItemName(), checkoutItems.get(options.getItemName()) + 1);
        }
    }


    private FlowPane createCheckoutNode(FurnitureRequestOptions options, Image furnitureImage) throws IOException {
        FlowPane flowPane = new FlowPane();
        flowPane.setPrefWidth(400);
        flowPane.setPrefHeight(100);
        flowPane.setOrientation(Orientation.VERTICAL);
        flowPane.setRowValignment(VPos.CENTER);
        flowPane.setColumnHalignment(HPos.CENTER);
        flowPane.setAlignment(Pos.CENTER);
        flowPane.setPadding(new Insets(20, 20, 20, 20));
        flowPane.setMargin(flowPane, new Insets(20, 20, 20, 20));
        flowPane.setBackground(Background.fill(Color.WHITE));

        // Creates the image view
        ImageView imageView = new ImageView(furnitureImage);
        imageView.setFitHeight(50);
        imageView.setFitWidth(50);

        //creates the discription label
        Label itemTitle = new Label();
        itemTitle.setPrefWidth(200);
        itemTitle.setPrefHeight(50);
        itemTitle.setText(GoogleTranslate.translate(options.getItemName()));
        itemTitle.setFont(new Font(15));
        itemTitle.setWrapText(true);
        itemTitle.setPadding(new Insets(0, 20, 0, 20));

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
            }
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
        });

        flowPane.getChildren().add(imageView);
        flowPane.getChildren().add(itemTitle);
        flowPane.getChildren().add(sub);
        flowPane.getChildren().add(quantity);
        flowPane.getChildren().add(add);
        return flowPane;
    }

    private HashMap<String, String> getNumberOfTables(ArrayList<FurnitureRequestOptions> shopItems) {
        HashMap<String, String> numberOfTabs = new HashMap<>();
        for (FurnitureRequestOptions options : shopItems) {
            numberOfTabs.putIfAbsent(options.getItemType(), options.getItemType());
        }
        return numberOfTabs;
    }

    private ArrayList<FurnitureRequestOptions> getFurnitureItems(List<FurnitureRequestOptions> furnitureRequestOptionsList) {
        ArrayList<FurnitureRequestOptions> shopItems = new ArrayList<>();

        for (FurnitureRequestOptions options : furnitureRequestOptionsList) {
            shopItems.add(options);
        }
        return shopItems;
    }

}
