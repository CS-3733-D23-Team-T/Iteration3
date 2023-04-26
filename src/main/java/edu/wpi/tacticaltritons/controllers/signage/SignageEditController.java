package edu.wpi.tacticaltritons.controllers.signage;

import edu.wpi.tacticaltritons.App;
import edu.wpi.tacticaltritons.database.DAOFacade;
import edu.wpi.tacticaltritons.database.Signage;
import edu.wpi.tacticaltritons.navigation.Navigation;
import edu.wpi.tacticaltritons.navigation.Screen;
import edu.wpi.tacticaltritons.styling.*;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXScrollPane;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.List;

public class SignageEditController {
    @FXML private FlowPane basePane;
    @FXML private AnchorPane addPresetButtonPane;
    @FXML private MFXButton addPresetButton;
    private final URL presetButtonPath = this.getClass().getResource("../../views/signagePages/SignagePresetButton.fxml");
    public void initialize() throws IOException, SQLException {

        List<Signage> signageList = DAOFacade.getAllSignage();
        for(Signage signage: signageList){
            addPreset(signage.getTitle(), signage.isSingleDisplay(),signage.getForwarddir(),signage.getLeftdir(),signage.getRightdir(),signage.getBackdir());
        }
        setResize();
        addPresetButton.setOnAction(event -> {
            Navigation.navigate(Screen.CREATE_SIGNAGE);
        });
        EffectGenerator.generateShadowEffect(basePane);
        EffectGenerator.generateSpacing(basePane,20);
    }

    private void setResize(){
        App.getPrimaryStage().widthProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                addPresetButtonPane.setPrefWidth(newValue.doubleValue());
                addPresetButton.setLayoutX(addPresetButtonPane.getPrefWidth() - 150);
            }
        });
        App.getPrimaryStage().heightProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                addPresetButtonPane.setPrefHeight(newValue.doubleValue() - 105);
                addPresetButton.setLayoutY(addPresetButtonPane.getPrefHeight() - 150);
            }
        });
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
        PresetButtonController controller = loader.getController();
        controller.setPresetName(presetName);
        controller.setPresetContents(singleDisplay,forward,left,right,back);
        basePane.getChildren().add(presetButton);
    }
}