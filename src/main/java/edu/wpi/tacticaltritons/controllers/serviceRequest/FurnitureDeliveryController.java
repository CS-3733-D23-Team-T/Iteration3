package edu.wpi.tacticaltritons.controllers.serviceRequest;

import edu.wpi.tacticaltritons.auth.Validator;
import edu.wpi.tacticaltritons.database.DAOFacade;
import edu.wpi.tacticaltritons.database.FurnitureRequestOptions;
import edu.wpi.tacticaltritons.navigation.Navigation;
import edu.wpi.tacticaltritons.navigation.Screen;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXFilterComboBox;
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

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class FurnitureDeliveryController {
    private String shopName;
    @FXML private Label checkoutLabel;
    @FXML private Label searchbarShopName;

    @FXML private MFXFilterComboBox searchBar;
    @FXML public FlowPane checkoutFlowpane;
    @FXML private Text itemValidator;
    @FXML private MFXButton clearButton;
    @FXML private MFXButton checkoutButton;
    @FXML private BorderPane infoBoardPane;
    private TabPane tabPane = new TabPane();

    static public ObservableMap<String, Integer > checkoutItems = FXCollections.observableHashMap();

    private List<FurnitureRequestOptions> furnitureRequestOptionsList;

    @FXML
    public void initialize() {
        BooleanProperty validItems = new SimpleBooleanProperty(false);
        checkoutFlowpane.getChildren().addListener(Validator.generateFormListener(validItems, checkoutButton, 1,
                itemValidator.getText(), itemValidator));

        this.shopName = "Storage";
        // Sets the label to the name of the shop that was selected
        checkoutLabel.setText(shopName);
        searchbarShopName.setText(shopName);

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
            Tab tab = new Tab(value);
            tab.setId(value);
            ScrollPane scrollPane = createShopItems(shopItems, value);
            tab.setContent(scrollPane);
            tabPane.getTabs().add(tab);
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

    private ScrollPane createShopItems(ArrayList<FurnitureRequestOptions> furnitureRequestOptionsArrayList, String value) {
        int counter = 0;
        ScrollPane scrollPane = new ScrollPane();
        FlowPane mainFlowPane = new FlowPane();
        mainFlowPane.setPrefHeight(150);
        mainFlowPane.setOrientation(Orientation.HORIZONTAL);
        mainFlowPane.setRowValignment(VPos.CENTER);
        mainFlowPane.setColumnHalignment(HPos.CENTER);
        mainFlowPane.setAlignment(Pos.CENTER);


        for (FurnitureRequestOptions options : furnitureRequestOptionsArrayList) {
            if (options.getItemType().equals(value)) {
                counter++;
                // Creates the outer flowpane to hold all the infomation
                FlowPane flowPane = new FlowPane();
                flowPane.setPrefWidth(200);
                flowPane.setPrefHeight(400);
                flowPane.setOrientation(Orientation.VERTICAL);
                flowPane.setRowValignment(VPos.CENTER);
                flowPane.setColumnHalignment(HPos.CENTER);
                flowPane.setAlignment(Pos.CENTER);
                flowPane.setPadding(new Insets(20, 20, 20, 20));
                flowPane.setMargin(flowPane, new Insets(20, 20, 20, 20));
                flowPane.setBackground(Background.fill(Color.WHITE));


                // Creates the image view
                Image image = new Image("/edu/wpi/tacticaltritons/images/flower_request/Boston Blossoms.jpg");
                ImageView imageView = new ImageView(image);
                imageView.setFitHeight(150);
                imageView.setFitWidth(200);

                // creates the Shope name lable
                Label itemTitle = new Label();
                itemTitle.setText(options.getItemName());
                itemTitle.setWrapText(true);
                itemTitle.setFont(new Font(20));


                //creates the discription label
                Label discriptionLabel = new Label();
                discriptionLabel.setPrefWidth(flowPane.getPrefWidth());
                discriptionLabel.setText(options.getItemDescription());
                discriptionLabel.setFont(new Font(15));
                discriptionLabel.setWrapText(true);
                discriptionLabel.setPadding(new Insets(0, 20, 0, 20));


                flowPane.getChildren().add(imageView);
                flowPane.getChildren().add(itemTitle);
                flowPane.getChildren().add(discriptionLabel);

                flowPane.setOnMouseClicked(event ->
                {
                    updatedCheckoutBox(options);
                });


                mainFlowPane.getChildren().add(flowPane);
            }
        }
        mainFlowPane.setPrefWidth(300 * counter);
        scrollPane.setContent(mainFlowPane);
        return scrollPane;
    }

    private void updatedCheckoutBox(FurnitureRequestOptions options) {
    /*
    Todo
    when hit the furniture in the main page it will update based on the checkout
    when I get the q to 0 delete from the checkout box
     */

        if(!checkoutItems.containsKey(options.getItemName()))
        {
            checkoutItems.put(options.getItemName(), 1);
            FlowPane flowPane = createCheckoutNode(options);
            checkoutFlowpane.getChildren().add(flowPane);
        }
        else
        {
            checkoutItems.put(options.getItemName(), checkoutItems.get(options.getItemName()) + 1);
        }
    }


    private FlowPane createCheckoutNode(FurnitureRequestOptions options) {
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
        Image image = new Image("/edu/wpi/tacticaltritons/images/flower_request/Boston Blossoms.jpg");
        ImageView imageView = new ImageView(image);
        imageView.setFitHeight(50);
        imageView.setFitWidth(50);

        //creates the discription label
        Label itemTitle = new Label();
        itemTitle.setPrefWidth(200);
        itemTitle.setPrefHeight(50);
        itemTitle.setText(options.getItemName());
        itemTitle.setFont(new Font(15));
        itemTitle.setWrapText(true);
        itemTitle.setPadding(new Insets(0, 20, 0, 20));

        Label quantity = new Label();
        quantity.setPrefWidth(50);
        quantity.setPrefHeight(50);
        quantity.setAlignment(Pos.CENTER);
        quantity.textProperty().bind(Bindings.valueAt(checkoutItems,options.getItemName()).asString());

        Button sub = new Button();
        sub.setText("-");
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
