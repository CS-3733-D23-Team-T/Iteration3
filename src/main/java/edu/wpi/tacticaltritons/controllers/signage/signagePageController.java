package edu.wpi.tacticaltritons.controllers.signage;

import edu.wpi.tacticaltritons.App;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import edu.wpi.tacticaltritons.styling.*;

import java.awt.*;
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
    int fontSize = 150;

    public void initialize(){
        verticallyResizing(App.getPrimaryStage().getWidth());
        for(int i = 0; i < 5; i++){
            addForward("room room room " + i);
        }
        for(int i=0; i < 2; i++){
            addLeft("room room room " + i);
        }
        removeEmpty();
        EffectGenerator.generateShadowEffect(basePane);
        EffectGenerator.generateSpacing(basePane,15);
        setResize();
    }

    private void removeEmpty(){
        for(int i = 0; i < basePane.getChildren().size(); i++){
            for(Node items: ((FlowPane)basePane.getChildren().get(i)).getChildren()){
                if(items instanceof VBox){
                    VBox box = (VBox) items;
                    if(box.getChildren().size() == 0){
                        basePane.getChildren().remove(i);
                        i--;
                    }
                }
            }
        }
    }

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
    private void verticallyResizing(Number newValue){
        basePane.setPrefWidth(newValue.doubleValue());
        for(Node block: basePane.getChildren()){
            FlowPane pane = (FlowPane) block;
            pane.setPrefWidth(newValue.doubleValue());
            for(Node item: pane.getChildren()){
                if(item instanceof VBox){
                    VBox box = (VBox) item;
                    box.setPrefWidth(newValue.doubleValue() - arrowIconSize);
                }
            }
        }
    }
    private void addSignage(VBox target,String text){
        Label locationText = new Label(text);
        locationText.setStyle("-fx-font-size: " + fontSize);
        target.getChildren().add(locationText);
    }
    private void addForward(String text){addSignage(signageForwardLocations,text);}
    private void addLeft(String text){addSignage(signageLeftLocations,text);}
    private void addRight(String text){addSignage(signageRightLocations,text);}
    private void addBack(String text){addSignage(signageBackLocations,text);}
}
