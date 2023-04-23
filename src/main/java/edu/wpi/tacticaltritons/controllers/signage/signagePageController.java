package edu.wpi.tacticaltritons.controllers.signage;

import edu.wpi.tacticaltritons.App;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import edu.wpi.tacticaltritons.styling.*;

import java.util.ArrayList;


public class signagePageController {
    @FXML private GridPane basePane;

    @FXML private FlowPane signageForwardBlock;
    @FXML private VBox signageForwardLocations;
    @FXML private Rectangle signageForwardSeparator;

    @FXML private FlowPane signageLeftBlock;
    @FXML private VBox signageLeftLocations;
    @FXML private Rectangle signageLeftSeparator;

    @FXML private FlowPane signageRightBlock;
    @FXML private VBox signageRightLocations;
    @FXML private Rectangle signageRightSeparator;

    @FXML private FlowPane signageBackBlock;
    @FXML private VBox signageBackLocations;
    @FXML private Rectangle signageBackSeparator;

    double seperatorRatio = 0.6;
    double arrowIconSize = 400;
    int fontSize = 80;

    VBox[] signageLocationBlocks;

    ArrayList<Label> locationLabels;
    public void initialize(){
        locationLabels = new ArrayList<>();
        signageLocationBlocks = new VBox[]{signageForwardLocations,signageLeftLocations,signageRightLocations,signageBackLocations};
        verticallyResizing(App.getPrimaryStage().getWidth());
        loadLocation(signageForwardLocations,signagePageInteractionClass.forwardLocations); // forward direction block is the default display area
        if(signagePageInteractionClass.signleDisplay){
            formatAsSingleDisplay();
        }else{
            loadLocation(signageLeftLocations,signagePageInteractionClass.leftLocations);
            loadLocation(signageRightLocations,signagePageInteractionClass.rightLocations);
            loadLocation(signageBackLocations,signagePageInteractionClass.backLocations);
            removeEmpty();
        }
        EffectGenerator.generateShadowEffect(basePane);
        EffectGenerator.generateSpacing(basePane,20);
        setResize();
        if(basePane.getChildren().isEmpty()){
            Label askForSelection = new Label("no preset selected,\nplease select one preset in \n\"Edit Signage\" page");
            askForSelection.setStyle("-fx-font-size: " + fontSize * 3);
            basePane.getChildren().add(askForSelection);
        }
    }
    // load all locations
    private void loadLocation(VBox target,String[] source){
        for(String location: source){
            Label locationLabel= new Label(location);
            locationLabel.setStyle("-fx-font-size: " + fontSize);
            target.getChildren().add(locationLabel);
            locationLabels.add(locationLabel);
        }
    }
    // if assign as single display, reformat the block as a single display block
    private void formatAsSingleDisplay(){
        Label singleDisplayTitle = new Label("Stop Here For");
        singleDisplayTitle.setStyle("-fx-text-fill: " + ThemeColors.YELLOW.getColor() + ";" + "-fx-font-size: " + (fontSize * 3 + 10));
        for(int i = 0; i < signageForwardLocations.getChildren().size();i++){
            signageForwardLocations.getChildren().get(i).setStyle("-fx-font-size: " + fontSize * 3);
        }
        signageForwardLocations.getChildren().add(0,singleDisplayTitle);
        signageForwardBlock.getChildren().remove(0,2); // remove arrow and seperator
        basePane.getChildren().remove(1,4); // remove the rest four blocks
        signageForwardBlock.setAlignment(Pos.CENTER); // make text center
    }
    //remove unfilled blocks
    private void removeEmpty(){
        for(int i = 0,j = 0; i < basePane.getChildren().size() && j < signageLocationBlocks.length; i++,j++){
            if(signageLocationBlocks[j].getChildren().isEmpty()){
                basePane.getChildren().remove(i);
                i--;
            }
        }
        // have enough space for big font size under 4K resolution
        if(locationLabels.size() < 13){
            for(Label locaionLabel: locationLabels){
                locaionLabel.setStyle("-fx-font-size: " + fontSize*2);
            }
        }
    }
    // set the on actions
    private void setResize(){
        App.getPrimaryStage().widthProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                verticallyResizing(newValue);
            }
        });
        signageForwardBlock.heightProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                signageForwardSeparator.setHeight(newValue.doubleValue() * seperatorRatio);
            }
        });
        signageLeftBlock.heightProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                signageLeftSeparator.setHeight(newValue.doubleValue() * seperatorRatio);
            }
        });
        signageRightBlock.heightProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                signageRightSeparator.setHeight(newValue.doubleValue() * seperatorRatio);
            }
        });
        signageBackBlock.heightProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                signageBackSeparator.setHeight(newValue.doubleValue() * seperatorRatio);
            }
        });
    }
    // do an initial resizing for fit the screen
    private void verticallyResizing(Number newValue){
        basePane.setPrefWidth(newValue.doubleValue());
        for(Node block: basePane.getChildren()){
            FlowPane pane = (FlowPane) block;
            pane.setPrefWidth(newValue.doubleValue());
            for(Node item: pane.getChildren()){
                if(item instanceof VBox box){
                    box.setPrefWidth(newValue.doubleValue() - arrowIconSize);
                }
            }
        }
    }
}
