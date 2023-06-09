package edu.wpi.tacticaltritons.controllers.signage;

import edu.wpi.tacticaltritons.database.DAOFacade;
import edu.wpi.tacticaltritons.database.Signage;
import edu.wpi.tacticaltritons.navigation.Navigation;
import edu.wpi.tacticaltritons.navigation.Screen;
import edu.wpi.tacticaltritons.styling.ThemeColors;
import io.github.palexdev.materialfx.controls.MFXButton;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;

import java.sql.SQLException;

public class PresetButtonController {
    @FXML private GridPane basePane;
    @FXML private Label presetName;
    @FXML private MFXButton edit;
    String[] forwardLocations;
    String[] leftLocations;
    String[] rightLocations;
    String[] backLocations;

    boolean singleDisplay;
    public void initialize(){
        basePane.setOnMouseClicked(
                event -> {
                    try{
                        loadData();
                    }catch (NullPointerException e){
                        e.printStackTrace();
                    }
                }
        );
        edit.setOnAction(event -> {
            editSignage();
        });
        basePane.setOnMouseEntered(event -> {
            presetName.setStyle("-fx-text-fill: " + ThemeColors.YELLOW.getColor()+ "; -fx-font-size: 25");
        });
        basePane.setOnMouseExited(event -> {
           presetName.setStyle("-fx-text-fill: Black; -fx-font-size: 25");
        });

    }
    public void setPresetName(String text){
        presetName.setText(text);
    }

    public void setPresetContents(String[] forward, String[] left, String[] right, String[] back){
        forwardLocations = forward;
        leftLocations = left;
        rightLocations = right;
        backLocations = back;
        singleDisplay = false;
    }

    public void deleteSignage(){
        try {
            DAOFacade.deleteSignage(new Signage(presetName.getText(),null,null,null,null,false));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void setPresetContents(String[] content){
        forwardLocations = content;
        singleDisplay = true;
    }

    public void setPresetContents(Boolean singleDisplay, String[] forward, String[] left, String[] right, String[] back){
        forwardLocations = forward;
        leftLocations = left;
        rightLocations = right;
        backLocations = back;
        this.singleDisplay = singleDisplay;
    }

    public String getPresetName(){
        return presetName.getText();
    }

    private void loadData(){
        loadForInteraction();
        SignagePageInteraction.firstDisplay = false;
        Navigation.navigate(Screen.SIGNAGE);
    }

    private void loadForInteraction(){
        SignagePageInteraction.presetName = presetName.getText();
        SignagePageInteraction.forwardLocations = forwardLocations;
        SignagePageInteraction.leftLocations = leftLocations;
        SignagePageInteraction.rightLocations = rightLocations;
        SignagePageInteraction.backLocations = backLocations;
        SignagePageInteraction.singleDisplay = singleDisplay;
    }

    private void editSignage(){
        loadForInteraction();
        SignagePageInteraction.presetName = presetName.getText();
        SignagePageInteraction.createSingleDisplay = singleDisplay;
        SignagePageInteraction.editingSignage = true;
        Navigation.navigate(Screen.CREATE_SIGNAGE);
    }


}
