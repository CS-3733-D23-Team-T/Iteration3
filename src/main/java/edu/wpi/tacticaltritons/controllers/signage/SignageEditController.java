package edu.wpi.tacticaltritons.controllers.signage;

import edu.wpi.tacticaltritons.App;
import edu.wpi.tacticaltritons.database.DAOFacade;
import edu.wpi.tacticaltritons.database.Signage;
import edu.wpi.tacticaltritons.navigation.Navigation;
import edu.wpi.tacticaltritons.navigation.Screen;
import edu.wpi.tacticaltritons.styling.*;
import io.github.palexdev.materialfx.controls.MFXButton;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.List;

public class SignageEditController {
    @FXML private FlowPane basePane;
    @FXML private BorderPane addPresetButtonPane;
    @FXML private VBox buttonsBar;
    @FXML private MFXButton addPresetButton;
    private final URL presetButtonPath = this.getClass().getResource("../../views/signagePages/SignagePresetButton.fxml");
    private final URL signageCreateSelectionPath = this.getClass().getResource("../../views/signagePages/SignageCreateSelectionPage.fxml");
    public void initialize() throws IOException, SQLException {
        resizeWidth(App.getPrimaryStage().getWidth());
        resizeHeight(App.getPrimaryStage().getHeight());
        List<Signage> signageList = DAOFacade.getAllSignage();
        for(Signage signage: signageList){
            addPreset(signage.getTitle(), signage.isSingleDisplay(),signage.getForwarddir(),signage.getLeftdir(),signage.getRightdir(),signage.getBackdir());
        }
        addPresetButton.setOnAction(event -> {
            try {
                displayCreateSignageSelectionWindow();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        addPresetButtonPane.setOnKeyPressed(event -> {
            if(event.getCode().equals(KeyCode.ESCAPE)){
                addPresetButtonPane.setCenter(null);
            }
        });
        setResize();
        EffectGenerator.generateShadowEffect(basePane);
        EffectGenerator.generateShadowEffect(addPresetButtonPane);
        EffectGenerator.generateSpacing(basePane,20);
    }

    private void setResize(){
        App.getPrimaryStage().widthProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                resizeWidth(newValue);
            }
        });
        App.getPrimaryStage().heightProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                resizeHeight(newValue);
            }
        });
    }
    private void resizeWidth(Number newValue){
         addPresetButtonPane.setPrefWidth(newValue.doubleValue());
    }
    private void resizeHeight(Number newValue){
        addPresetButtonPane.setPrefHeight(newValue.doubleValue() - 105);
        buttonsBar.setPrefHeight(newValue.doubleValue() - 105);
    }
    // add the four direction signage
    private void addPreset(String presetName, String[] forward, String[] left, String[] right, String[] back) throws IOException {
        FXMLLoader loader = new FXMLLoader(presetButtonPath);
        GridPane presetButton = loader.load();
        PresetButtonController controller = loader.getController();
        controller.setPresetName(presetName);
        controller.setPresetContents(forward,left,right,back);
        basePane.getChildren().add(presetButton);
    }
    //add the single signage (NOT equivalent to only fill the fist list of the method above!)
    private void addPreset(String presetName, String[] singleDisplayContent) throws IOException {
        FXMLLoader loader = new FXMLLoader(presetButtonPath);
        GridPane presetButton = loader.load();
        PresetButtonController controller = loader.getController();
        controller.setPresetName(presetName);
        controller.setPresetContents(singleDisplayContent);
        basePane.getChildren().add(presetButton);
    }

    private void addPreset(String presetName, boolean singleDisplay, String[] forward, String[] left, String[] right, String[] back) throws IOException {
        FXMLLoader loader = new FXMLLoader(presetButtonPath);
        GridPane presetButton = loader.load();
        HBox buttonBar = (HBox) presetButton.getChildren().get(0);
        MFXButton deleteButton = (MFXButton) buttonBar.getChildren().get(0);
        MFXButton editButton = (MFXButton) buttonBar.getChildren().get(1);
        PresetButtonController controller = loader.getController();
        deleteButton.setOnAction(event -> {
            controller.deleteSignage();
            basePane.getChildren().remove(presetButton);
            if(SignagePageInteraction.presetName.equals(controller.getPresetName())){
                SignagePageInteraction.clear();
            }
        });
        controller.setPresetName(presetName);
        controller.setPresetContents(singleDisplay,forward,left,right,back);
        basePane.getChildren().add(presetButton);
    }

    private void displayCreateSignageSelectionWindow() throws IOException {
        FXMLLoader loader = new FXMLLoader(signageCreateSelectionPath);
        VBox signageCreateSelectionPage = loader.load();
        HBox selectionButtons = (HBox) signageCreateSelectionPage.getChildren().get(1);
        MFXButton cancelButton = (MFXButton) signageCreateSelectionPage.getChildren().get(0);
        MFXButton selectFourDirectionPage = (MFXButton) selectionButtons.getChildren().get(0);
        MFXButton selectSinglePage = (MFXButton) selectionButtons.getChildren().get(1);
        cancelButton.setOnAction(event -> {
            addPresetButtonPane.setCenter(null);
        });
        selectFourDirectionPage.setOnAction(event -> {
            SignagePageInteraction.createSingleDisplay = false;
            SignagePageInteraction.editingSignage = false;
            Navigation.navigate(Screen.CREATE_SIGNAGE);
        });
        selectSinglePage.setOnAction(event -> {
            SignagePageInteraction.createSingleDisplay = true;
            SignagePageInteraction.editingSignage = false;
            Navigation.navigate(Screen.CREATE_SIGNAGE);
        });
        addPresetButtonPane.setCenter(signageCreateSelectionPage);
        EffectGenerator.generateShadowEffect(addPresetButtonPane);
    }
}