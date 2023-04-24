package edu.wpi.tacticaltritons.controllers.signage;

import edu.wpi.tacticaltritons.navigation.Navigation;
import edu.wpi.tacticaltritons.navigation.Screen;
import edu.wpi.tacticaltritons.styling.ThemeColors;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;

import java.awt.*;
import java.lang.reflect.Array;

public class presetButtonController {
    @FXML private GridPane basePane;
    @FXML private Label presetName;
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

    public void setPresetContents(String[] content){
        forwardLocations = content;
        singleDisplay = true;
    }
    private void loadData(){
        System.out.println("clicked");
        signagePageInteractionClass.forwardLocations = forwardLocations;
        signagePageInteractionClass.leftLocations = leftLocations;
        signagePageInteractionClass.rightLocations = rightLocations;
        signagePageInteractionClass.backLocations = backLocations;
        signagePageInteractionClass.signleDisplay = singleDisplay;
        Navigation.navigate(Screen.SIGNAGE);
    }
}
