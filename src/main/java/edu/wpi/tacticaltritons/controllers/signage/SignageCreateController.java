package edu.wpi.tacticaltritons.controllers.signage;

import edu.wpi.tacticaltritons.App;
import edu.wpi.tacticaltritons.database.DAOFacade;
import edu.wpi.tacticaltritons.database.Signage;
import edu.wpi.tacticaltritons.navigation.Navigation;
import edu.wpi.tacticaltritons.navigation.Screen;
import edu.wpi.tacticaltritons.styling.EffectGenerator;
import edu.wpi.tacticaltritons.styling.ThemeColors;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXScrollPane;
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;
import javafx.scene.shape.Rectangle;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;

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

    @FXML private BorderPane buttonsPane;
    @FXML private HBox buttonsBar;
    @FXML private MFXButton saveButton;
    @FXML private MFXButton cancelButton;
    private ArrayList<Label> locationLabels;
    private final double arrowIconSize = 400;
    private final double fontSize = 70;
    private final double buttonsTotalSize = 420;
    private final double seperatorRatio = 0.6;
    private final URL signageItemPath = this.getClass().getResource("../../views/signagePages/SignageEditItem.fxml");
    private final URL signageConfirmPath = this.getClass().getResource("../../views/signagePages/SignageCreateConfirmPage.fxml");
    private ArrayList<String> forwardLocations;
    private ArrayList<String> leftLocations;
    private ArrayList<String> rightLocations;
    private ArrayList<String> backLocations;
    
    public void initialize() throws IOException {

        if(SignagePageInteraction.createSingleDisplay){
            formatAsSingleDisplay();
        }
        forwardLocations = new ArrayList<>();
        leftLocations = new ArrayList<>();
        rightLocations = new ArrayList<>();
        backLocations = new ArrayList<>();
        if(SignagePageInteraction.editingSignage){
            loadPresetForEdit();
        }
        horizontalResizing(App.getPrimaryStage().getWidth());
        basePaneContainer.setFitToWidth(true);
        basePaneContainer.setFitToHeight(true);
        setResize();
        setButtonsAction();
        EffectGenerator.generateShadowEffect(basePane);
        EffectGenerator.generateShadowEffect(buttonsPane);
        EffectGenerator.generateSpacing(basePane,20);
    }

    private void setButtonsAction(){
        cancelButton.setOnAction(event -> {
            Navigation.navigate(Screen.EDIT_SIGNAGE);
        });
        saveButton.setOnAction(event -> {
            try {
                confirmSave();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
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
                if(newValue.doubleValue() > 600){
                    basePaneContainer.setPrefWidth(newValue.doubleValue());
                    basePane.setPrefWidth(newValue.doubleValue());
                    horizontalResizing(newValue);
                }
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
    private void loadPresetForEdit() throws IOException {
        addNewSignageItems(signageForwardLocations,SignagePageInteraction.forwardLocations);
        if(!SignagePageInteraction.createSingleDisplay){
            addNewSignageItems(signageLeftLocations,SignagePageInteraction.leftLocations);
            addNewSignageItems(signageRightLocations,SignagePageInteraction.rightLocations);
            addNewSignageItems(signageBackLocations,SignagePageInteraction.backLocations);
        }
    }
    private void addNewSignageItem(VBox target) throws IOException {
        addNewSignageItem(target,"");
    }
    private void addNewSignageItem(VBox target, String content) throws IOException {
        FXMLLoader loader = new FXMLLoader(signageItemPath);
        HBox signageItem = loader.load();
        MFXButton deleteButton = (MFXButton) signageItem.getChildren().get(0);
        MFXTextField contentInput = (MFXTextField) signageItem.getChildren().get(1);
        contentInput.setText(content);
        deleteButton.setOnAction(event -> {
            target.getChildren().remove(signageItem);
        });
        target.getChildren().add(target.getChildren().size() - 1,signageItem);
    }
    private void addNewSignageItems(VBox target, String[] contents) throws IOException {
        for(String content: contents){
            addNewSignageItem(target,content);
        }
    }
    private void generatePreset(VBox input, ArrayList<String> output){
        ObservableList<Node> inputList= input.getChildren();
        int startIndex = 0;
        if(SignagePageInteraction.createSingleDisplay){
            startIndex = 1;
        }
        for(int i = startIndex; i < inputList.size()-1;i++){
            MFXTextField signageContent = (MFXTextField)((HBox)inputList.get(i)).getChildren().get(1);
            if(!signageContent.getText().isEmpty()){
                output.add(signageContent.getText());
            }
        }
    }
    private void displayConfirmPage() throws IOException {
        FXMLLoader loader = new FXMLLoader(signageConfirmPath);
        GridPane confirmPage = loader.load();
        MFXTextField presetName = (MFXTextField)confirmPage.getChildren().get(0);
        HBox buttons = (HBox)confirmPage.getChildren().get(1);
        MFXButton cancelButton = (MFXButton) buttons.getChildren().get(0);
        MFXButton confirmButton = (MFXButton) buttons.getChildren().get(1);
        if(SignagePageInteraction.editingSignage){presetName.setText(SignagePageInteraction.presetName);}
        buttonsPane.setOnKeyPressed(event -> {
            if(event.getCode().equals(KeyCode.ENTER)){
                confirmButton.fire();
            }
        });
        presetName.setOnKeyPressed(event -> {
            if(event.getCode().equals(KeyCode.ENTER)){
                confirmButton.fire();
            }
        });
        buttonsPane.setOnKeyPressed(event -> {
            if(event.getCode().equals(KeyCode.ESCAPE)){;
                cancelButton.fire();
            }
        });
        cancelButton.setOnAction(event -> {
            buttonsPane.setCenter(null);
        });
        confirmButton.setOnAction(event -> {
            if(presetName.getText().isEmpty()){
                EffectGenerator.alertOn(presetName,"please input a name");
            }else{
                try {
                    upload(presetName.getText());
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        });
        buttonsPane.setCenter(confirmPage);
        EffectGenerator.generateShadowEffect(buttonsPane);
    }
    private void confirmSave() throws IOException{
        generatePreset(signageForwardLocations,forwardLocations);
        if(!SignagePageInteraction.createSingleDisplay){
            generatePreset(signageLeftLocations,leftLocations);
            generatePreset(signageRightLocations,rightLocations);
            generatePreset(signageBackLocations,backLocations);
        }
        displayConfirmPage();
    }

    private void upload(String presetName) throws SQLException {
        String[] forwardDir = forwardLocations.toArray(new String[0]);
        String[] leftDir = leftLocations.toArray(new String[0]);
        String[] rightDir = rightLocations.toArray(new String[0]);
        String[] backDir = backLocations.toArray(new String[0]);
        if(SignagePageInteraction.editingSignage){
            DAOFacade.deleteSignage(new Signage(presetName,null,null,null,null,false));
        }
        Signage signage = new Signage(presetName,forwardDir,leftDir,rightDir,backDir,SignagePageInteraction.createSingleDisplay);
        try {
            DAOFacade.insertSignage(signage);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        Navigation.navigate(Screen.EDIT_SIGNAGE);
    }

    private void formatAsSingleDisplay(){
        Label singleDisplayTitle = new Label("Stop Here For");
        singleDisplayTitle.setStyle("-fx-text-fill: " + ThemeColors.YELLOW.getColor() + ";" + "-fx-font-size: " + (fontSize + 10));
        signageForwardLocations.getChildren().add(0,singleDisplayTitle);
        signageForwardBlock.getChildren().remove(0,2); // remove arrow and seperator
        basePane.getChildren().remove(1,4); // remove the rest four blocks
        signageForwardBlock.setAlignment(Pos.CENTER); // make text center
    }




}
