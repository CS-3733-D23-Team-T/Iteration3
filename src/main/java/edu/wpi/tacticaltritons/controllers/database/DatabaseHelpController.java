package edu.wpi.tacticaltritons.controllers.database;
import io.github.palexdev.materialfx.controls.MFXScrollPane;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;

public class DatabaseHelpController {
@FXML private MFXScrollPane helptext;
  @FXML
  public void initialize() {

    helptext.setFitToHeight(true);
    helptext.setFitToWidth(true);
    helptext.heightProperty().addListener(new ChangeListener<Number>() {
      @Override
      public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
        helptext.setFitToHeight(true);
        System.out.println(oldValue + " " + newValue);
      }
    });

    helptext.widthProperty().addListener(new ChangeListener<Number>() {
        @Override
        public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
            helptext.setFitToWidth(true);
        }
    });
  }
  private void resize(){
  }
}
