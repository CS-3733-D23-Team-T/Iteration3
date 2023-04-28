package edu.wpi.tacticaltritons.controllers.serviceRequest;

import edu.wpi.tacticaltritons.App;
import edu.wpi.tacticaltritons.database.DAOFacade;
import edu.wpi.tacticaltritons.database.FlowerRequestOptions;
import edu.wpi.tacticaltritons.navigation.Navigation;
import edu.wpi.tacticaltritons.navigation.Screen;
import javafx.fxml.FXML;
import javafx.geometry.*;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.FlowPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;

public class MealChoiceController {

    @FXML
    FlowPane leftFlowPane;
    final private double MAX_FONT_SIZE = 50;
    final private double MAX_FLOW_PANE_WIDTH = 1000;
    final private double MAX_FLOW_PANE_HEIGHT = 600;
    private double MAX_FLOW_PANE_WIDTH_INNER;
    private double MAX_IMAGE_VIEW_WIDTH;
    final private double MAX_FLOW_PANE_HEIGHT_INNER = 555;
    final private double MAX_IMAGE_VIEW_HEIGHT = 250;

    public static String name;
    private double noramlWidth = 1280;
    private double normalHeight = 720;

    private double defaultOuterFlowPanePrefWidth = 400;
    private double defaultOuterFlowPanePrefHeight = 150;
    private double defaultInerFlowPanePrefWidth = 250;
    private double defaultInerFlowPanePrefHeight = 150;
    private double defaultImageViewFitWidth = 100;
    private double defaultImageViewFitHeight = 100;
    private double defaultTitleFontSize = 25;
    private double defaultDiscriptionFontSize = 15;


    public void initialize() throws NullPointerException {
        // This gets all the entrys in the database into a list locally so it is only one database call

        HashMap<String, FlowerRequestOptions> uniqueShops = new HashMap<>();
        List<FlowerRequestOptions> flowerRequestOptionsList;
        try {
            flowerRequestOptionsList = DAOFacade.getAllFlowerRequestOptions();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        uniqueShops = findNumberOfShops(flowerRequestOptionsList);

        uniqueShops.forEach((key, value) -> {
            createFlowerShopButton(value, App.flowerHashMap.get(value.getShop()));
        });
    }

    private void createFlowerShopButton(FlowerRequestOptions value, Image shopImage) {
        // created the outer flow pane
        FlowPane flowPaneOuter = new FlowPane();
        flowPaneOuter.setPrefWidth(defaultOuterFlowPanePrefWidth);
        flowPaneOuter.setPrefHeight(defaultOuterFlowPanePrefHeight);
        flowPaneOuter.setOrientation(Orientation.VERTICAL);
        flowPaneOuter.setRowValignment(VPos.CENTER);
        flowPaneOuter.setColumnHalignment(HPos.CENTER);
        flowPaneOuter.setAlignment(Pos.CENTER);
        flowPaneOuter.setBackground(Background.fill(Color.WHITE));
        flowPaneOuter.setPadding(new Insets(20, 20, 20, 20));
        flowPaneOuter.setMargin(flowPaneOuter, new Insets(20, 20, 20, 20));

        //Creates the inner flowpane
        FlowPane flowPaneInner = new FlowPane();
        flowPaneInner.setPrefWidth(defaultInerFlowPanePrefWidth);
        flowPaneInner.setPrefHeight(defaultInerFlowPanePrefHeight);
        flowPaneInner.setOrientation(Orientation.VERTICAL);
        flowPaneInner.setRowValignment(VPos.CENTER);
        flowPaneInner.setColumnHalignment(HPos.CENTER);
        flowPaneInner.setAlignment(Pos.CENTER);

        // creates the imageview
        Image image = shopImage;
        ImageView imageView = new ImageView(image);
        imageView.setFitWidth(defaultImageViewFitWidth);
        imageView.setFitHeight(defaultImageViewFitHeight);

        // creates the Shope name lable
        Label shopNameLabel = new Label();
        shopNameLabel.setText(value.getShop());
        shopNameLabel.setWrapText(true);
        shopNameLabel.setFont(new Font(defaultTitleFontSize));


        //creates the discription label
        Label discriptionLabel = new Label();
        discriptionLabel.setPrefWidth(flowPaneInner.getPrefWidth());
        discriptionLabel.setText(value.getShopDescription());
        discriptionLabel.setFont(new Font(defaultDiscriptionFontSize));
        discriptionLabel.setWrapText(true);
        discriptionLabel.setPadding(new Insets(0, 20, 0, 20));


        // added thins to the inner flowplane
        flowPaneInner.getChildren().add(shopNameLabel);
        flowPaneInner.getChildren().add(discriptionLabel);

        // added things to the other flowpane
        flowPaneOuter.getChildren().add(imageView);
        flowPaneOuter.getChildren().add(flowPaneInner);

        // addes things to the main flow pane
        flowPaneOuter.setStyle("-fx-background-radius: 10; -fx-background-color: white");
        leftFlowPane.getChildren().add(flowPaneOuter);

        flowPaneOuter.setOnMouseClicked(event ->
        {
            name = value.getShop();
            Navigation.navigate(Screen.FLOWER_REQUEST);
        });

        startUpSize(flowPaneOuter, flowPaneInner, imageView, shopNameLabel, discriptionLabel);
        windowChangingSize(flowPaneOuter, flowPaneInner, imageView, shopNameLabel, discriptionLabel);


    }

    private void startUpSize(FlowPane flowPaneOuter, FlowPane flowPaneInner, ImageView imageView, Label shopNameLabel, Label discriptionLabel) {
        double newOuterFlowPaneWidth = defaultOuterFlowPanePrefWidth * App.getPrimaryStage().getWidth() / noramlWidth;
        double newInnerFlowPaneWidth = defaultInerFlowPanePrefWidth * App.getPrimaryStage().getWidth() / noramlWidth;
        double newImageWidth = defaultImageViewFitWidth * App.getPrimaryStage().getWidth() / noramlWidth;

        if (newOuterFlowPaneWidth <= MAX_FLOW_PANE_WIDTH) {
            flowPaneOuter.setPrefWidth(newOuterFlowPaneWidth);
            flowPaneInner.setPrefWidth(newInnerFlowPaneWidth);
            imageView.setFitWidth(newImageWidth);
        } else {
            flowPaneOuter.setPrefWidth(MAX_FLOW_PANE_WIDTH);
            flowPaneInner.setPrefWidth(MAX_FLOW_PANE_WIDTH_INNER);
            imageView.setFitWidth(MAX_IMAGE_VIEW_WIDTH);
        }

        double newOuterFlowPaneHeight = defaultOuterFlowPanePrefHeight * App.getPrimaryStage().getHeight() / normalHeight;
        double newInnerFlowPaneHeight = defaultInerFlowPanePrefHeight * App.getPrimaryStage().getHeight() / normalHeight;
        double newImageHeight = defaultImageViewFitHeight * App.getPrimaryStage().getHeight() / normalHeight;
        double shopTitleTextSize = (defaultTitleFontSize * App.getPrimaryStage().getHeight()) / noramlWidth;
        double shopDiscriptionTextSize = defaultDiscriptionFontSize * App.getPrimaryStage().getHeight() / normalHeight;

        if (newOuterFlowPaneHeight <= MAX_FLOW_PANE_HEIGHT) {
            flowPaneOuter.setPrefHeight(newOuterFlowPaneHeight);
            flowPaneInner.setPrefHeight(newInnerFlowPaneHeight);
            imageView.setFitHeight(newImageHeight);
        } else {
            flowPaneOuter.setPrefHeight(MAX_FLOW_PANE_HEIGHT);
            flowPaneInner.setPrefHeight(MAX_FLOW_PANE_HEIGHT);
            imageView.setFitHeight(MAX_IMAGE_VIEW_HEIGHT);
        }

        if (shopTitleTextSize <= MAX_FONT_SIZE) {
            shopNameLabel.setFont(new Font(shopDiscriptionTextSize));
            discriptionLabel.setFont(new Font(shopTitleTextSize));
        } else {
            shopNameLabel.setFont(new Font(MAX_FONT_SIZE));
            discriptionLabel.setFont(new Font(MAX_FONT_SIZE - 10));
        }
    }

    private void windowChangingSize(FlowPane flowPaneOuter, FlowPane flowPaneInner, ImageView imageView, Label shopNameLabel, Label discriptionLabel) {
        App.getPrimaryStage().widthProperty().addListener(((observable, oldValue, newValue) ->
        {
            double newOuterFlowPaneWidth = defaultOuterFlowPanePrefWidth * newValue.doubleValue() / noramlWidth;
            double newInnerFlowPaneWidth = defaultInerFlowPanePrefWidth * newValue.doubleValue() / noramlWidth;
            double newImageWidth = defaultImageViewFitWidth * newValue.doubleValue() / noramlWidth;

            if (oldValue != null && oldValue.doubleValue() != 0 && newValue != null) {
                if (newOuterFlowPaneWidth <= MAX_FLOW_PANE_WIDTH) {
                    flowPaneOuter.setPrefWidth(newOuterFlowPaneWidth);
                    flowPaneInner.setPrefWidth(newInnerFlowPaneWidth);
                    imageView.setFitWidth(newImageWidth);
                } else {
                    flowPaneOuter.setPrefWidth(MAX_FLOW_PANE_WIDTH);
                    flowPaneInner.setPrefWidth(MAX_FLOW_PANE_WIDTH_INNER);
                    imageView.setFitWidth(MAX_IMAGE_VIEW_WIDTH);
                }
            } else {
                flowPaneOuter.setPrefWidth(defaultOuterFlowPanePrefWidth);
                flowPaneInner.setPrefWidth(defaultInerFlowPanePrefWidth);
                imageView.setFitWidth(defaultImageViewFitWidth);
            }
        }));

        App.getPrimaryStage().heightProperty().addListener((observable, oldValue, newValue) ->
        {
            double newOuterFlowPaneHeight = defaultOuterFlowPanePrefHeight * newValue.doubleValue() / normalHeight;
            double newInnerFlowPaneHeight = defaultInerFlowPanePrefHeight * newValue.doubleValue() / normalHeight;
            double newImageHeight = defaultImageViewFitHeight * newValue.doubleValue() / normalHeight;
            double shopTitleTextSize = (defaultTitleFontSize * newValue.doubleValue()) / noramlWidth;
            double shopDiscriptionTextSize = defaultDiscriptionFontSize * newValue.doubleValue() / normalHeight;

            if (oldValue != null && oldValue.doubleValue() != 0 && newValue != null) {
                if (newOuterFlowPaneHeight <= MAX_FLOW_PANE_HEIGHT) {
                    flowPaneOuter.setPrefHeight(newOuterFlowPaneHeight);
                    flowPaneInner.setPrefHeight(newInnerFlowPaneHeight);
                    imageView.setFitHeight(newImageHeight);
                } else {
                    flowPaneOuter.setPrefHeight(MAX_FLOW_PANE_HEIGHT);
                    flowPaneInner.setPrefHeight(MAX_FLOW_PANE_HEIGHT);
                    imageView.setFitHeight(MAX_IMAGE_VIEW_HEIGHT);
                }

                if (shopTitleTextSize <= MAX_FONT_SIZE) {
                    shopNameLabel.setFont(new Font(shopDiscriptionTextSize));
                    discriptionLabel.setFont(new Font(shopTitleTextSize));
                } else {
                    shopNameLabel.setFont(new Font(MAX_FONT_SIZE));
                    discriptionLabel.setFont(new Font(MAX_FONT_SIZE - 10));
                }

            } else {
                flowPaneOuter.setPrefHeight(defaultOuterFlowPanePrefWidth);
                flowPaneInner.setPrefHeight(defaultInerFlowPanePrefWidth);
                imageView.setFitHeight(defaultImageViewFitWidth);
                shopNameLabel.setFont(new Font(defaultTitleFontSize));
                discriptionLabel.setFont(new Font(defaultDiscriptionFontSize));

            }
        });
    }

//    private ChangeListener<? super Number> generateWidthScaler(Map<Node, Map<Class<?>, List<Double> nodes)

    private HashMap<String, FlowerRequestOptions> findNumberOfShops(List<FlowerRequestOptions> flowerRequestOptionsList) {
        HashMap<String, FlowerRequestOptions> hash = new HashMap<>();

        for (FlowerRequestOptions options : flowerRequestOptionsList) {
            hash.putIfAbsent(options.getShop(), options);
        }

        return hash;
    }
}
