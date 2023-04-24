package edu.wpi.tacticaltritons.controllers;

import edu.wpi.tacticaltritons.App;
import io.github.palexdev.materialfx.controls.MFXScrollPane;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;

public class CreditsController {

    @FXML private MFXScrollPane creditText;
    @FXML
    public void initialize() {
        creditText.setFitToHeight(true);
        creditText.setFitToWidth(true);
        App.getPrimaryStage().widthProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                creditText.setMinWidth(newValue.doubleValue());
            }
        });
        App.getPrimaryStage().heightProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                creditText.setMinHeight(newValue.doubleValue());
            }
        });
    }
    private void resize(){
    }
}
