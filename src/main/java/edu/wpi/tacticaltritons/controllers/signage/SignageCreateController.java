package edu.wpi.tacticaltritons.controllers.signage;

import edu.wpi.tacticaltritons.App;
import edu.wpi.tacticaltritons.styling.EffectGenerator;
import edu.wpi.tacticaltritons.styling.ThemeColors;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXScrollPane;
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.beans.Observable;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import org.controlsfx.control.tableview2.filter.filtereditor.SouthFilter;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

public class SignageCreateController {
    @FXML private MFXScrollPane basePaneContainer;
    @FXML private GridPane basePane;

    @FXML private FlowPane signageForwardBlock;
    @FXML private VBox signageForwardLocations;
    @FXML private Rectangle signageForwardSeparator;
    @FXML private MFXButton addForwardButton;

    @FXML private FlowPane signageLeftBlock;
    @FXML private VBox signageLeftLocations;
    @FXML private Rectangle signageLeftSeparator;
    @FXML private MFXButton addLeftButton;

    @FXML private FlowPane signageRightBlock;
    @FXML private VBox signageRightLocations;
    @FXML private Rectangle signageRightSeparator;
    @FXML private MFXButton addRightButton;

    @FXML private FlowPane signageBackBlock;
    @FXML private VBox signageBackLocations;
    @FXML private Rectangle signageBackSeparator;
    @FXML private MFXButton addBackButton;
    @FXML private HBox buttonsBar;
    private ArrayList<Label> locationLabels;
    private final double arrowIconSize = 400;
    private final double buttonsTotalSize = 420;
    private final double seperatorRatio = 0.6;
    private final URL signageItemPath = this.getClass().getResource("../../views/signagePages/SignageEditItem.fxml");

    public void initialize(){
        horizontalResizing(App.getPrimaryStage().getWidth());
        basePaneContainer.setFitToWidth(true);
        basePaneContainer.setFitToHeight(true);
        setResize();
        setButtonsAction();
        EffectGenerator.generateShadowEffect(basePane);
        EffectGenerator.generateSpacing(basePane,20);
    }

    private void setButtonsAction(){
        addForwardButton.setOnAction(event -> {
            try {
                addNewSignageItem(signageForwardLocations);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        addLeftButton.setOnAction(event -> {
            try {
                addNewSignageItem(signageLeftLocations);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        addRightButton.setOnAction(event -> {
            try {
                addNewSignageItem(signageRightLocations);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        addBackButton.setOnAction(event -> {
            try {
                addNewSignageItem(signageBackLocations);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    private void setResize(){
        App.getPrimaryStage().widthProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                basePaneContainer.setPrefWidth(newValue.doubleValue());
                basePane.setPrefWidth(newValue.doubleValue());
                horizontalResizing(newValue);
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

    private void horizontalResizing(Number newValue){
        basePane.setPrefWidth(newValue.doubleValue());
        buttonsBar.setSpacing(newValue.doubleValue() - 420);
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

    private void addNewSignageItem(VBox target) throws IOException {
        FXMLLoader loader = new FXMLLoader(signageItemPath);
        HBox signageItem = loader.load();
        MFXButton deleteButton = (MFXButton) signageItem.getChildren().get(0);
        deleteButton.setOnAction(event -> {
            target.getChildren().remove(signageItem);
        });
        target.getChildren().add(0,signageItem);
    }

    private void generatePreset(VBox input, ArrayList<String> output){
        ObservableList<Node> inputList= input.getChildren();
        for(int i = 0; i < inputList.size()-1;i++){
            MFXTextField signageContent = (MFXTextField)((HBox)inputList.get(i)).getChildren().get(1);
            output.add(signageContent.getText());
        }
    }




}
