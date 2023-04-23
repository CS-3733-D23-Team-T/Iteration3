package edu.wpi.tacticaltritons.controllers.signage;

import edu.wpi.tacticaltritons.styling.*;
import io.github.palexdev.materialfx.controls.MFXButton;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;

import java.io.IOException;
import java.net.URL;
import java.sql.Array;
import java.sql.SQLException;
import java.util.concurrent.Flow;

public class signageEditController {
    @FXML private FlowPane basePane;
    private final URL presetButtonPath = this.getClass().getResource("../../views/signagePages/signagePresetButton.fxml");
    public void initialize() throws IOException {

        //TODO move these four preset to database, and enable function for load database information to this page

        //preset 1
        addPreset("May 2023 screen 1",
                new String[]{},
                new String[]{"Information", "Shapiro Admitting",  "Shapiro Procedural Check-in"},
                new String[]{"Watkins Clinics A & B (this floor)", "Watkins Clinic C (up to 3rd floor)", "Rehabilitation Services (down to 1st floor)"},
                new String[]{});

        //preset 2
        addPreset("May 2023 screen 2",
                new String[]{"Watkins Clinics A & B (this floor)","Watkins Clinic C (EP & Echo) (up to 3rd floor)","Brigham Circle Medical Associates (up to 3rd floor)"},
                new String[]{},
                new String[]{},
                new String[]{"L2PRU (down to Lower Level \"L2\")"});

        //preset 3 single display
        addPreset("July 2023 screen 1",
                new String[]{"Information", "Shapiro Admitting", "Shapiro Procedural Check-in"});

        //preset 4
        addPreset("November 2023 screen 2",
                new String[]{"Watkins Clinic & EP (this floor)"},
                new String[]{"Echocardiography (this floor)"},
                new String[]{"Brigham Circle Medical Associates (Hale Building)"},
                new String[]{"L2PRU (down to Lower Level \"L2\")"});

        EffectGenerator.generateShadowEffect(basePane);
        EffectGenerator.generateSpacing(basePane,20);
    }

    // add the four direction signage
    private void addPreset(String presetName, String[] forward, String[] left, String[] right, String[] back) throws IOException {
        FXMLLoader loader = new FXMLLoader(presetButtonPath);
        GridPane presetButton = loader.load();
        presetButtonController controller = loader.getController();
        controller.setPresetName(presetName);
        controller.setPresetContents(forward,left,right,back);
        basePane.getChildren().add(presetButton);
    }

    //add the single signage (NOT equivalent to only fill the fist list of the method above!)
    private void addPreset(String presetName, String[] singleDisplayContent) throws IOException {
        FXMLLoader loader = new FXMLLoader(presetButtonPath);
        GridPane presetButton = loader.load();
        presetButtonController controller = loader.getController();
        controller.setPresetName(presetName);
        controller.setPresetContents(singleDisplayContent);
        basePane.getChildren().add(presetButton);
    }
}