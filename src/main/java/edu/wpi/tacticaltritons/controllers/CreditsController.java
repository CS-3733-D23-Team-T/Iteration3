package edu.wpi.tacticaltritons.controllers;

import edu.wpi.tacticaltritons.App;
import io.github.palexdev.materialfx.controls.MFXScrollPane;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.fxml.FXML;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;

import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

public class CreditsController {

    @FXML private MFXScrollPane creditText;
    @FXML private Text furnitureClickable1;

    private Map<String, String> furnitureMap = new HashMap<>();
    @FXML
    public void initialize() {
        creditText.setFitToHeight(true);
        creditText.setFitToWidth(true);
        App.getPrimaryStage().widthProperty().addListener((observable, oldValue, newValue) -> creditText.setMinWidth(newValue.doubleValue()));
        App.getPrimaryStage().heightProperty().addListener((observable, oldValue, newValue) -> creditText.setMinHeight(newValue.doubleValue()));

        furnitureMap.put("Office Depot", "link");
        furnitureClickable1.addEventHandler(EventType.ROOT, addEventHandler(furnitureMap.get("Office Depot")));
    }
    private void resize(){
    }
    private EventHandler<? super Event> addEventHandler(String link){
        return event -> {
            if(event.getEventType() == MouseEvent.MOUSE_ENTERED){
                furnitureClickable1.setUnderline(true);
            }
            else if(event.getEventType() == MouseEvent.MOUSE_EXITED){
                furnitureClickable1.setUnderline(false);
            }
            else if(event.getEventType().equals(MouseEvent.MOUSE_CLICKED)){
                Desktop desktop = Desktop.getDesktop();
                try {
                    desktop.browse(new URI(link));
                } catch (IOException | URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            }

        };
    }
}
