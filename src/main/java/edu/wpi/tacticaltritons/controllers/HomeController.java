package edu.wpi.tacticaltritons.controllers;

import edu.wpi.tacticaltritons.App;
import edu.wpi.tacticaltritons.auth.UserSessionToken;
import edu.wpi.tacticaltritons.navigation.Navigation;
import edu.wpi.tacticaltritons.navigation.Screen;
import java.awt.*;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.*;

import static javafx.scene.transform.Transform.scale;

public class HomeController {
  @FXML private StackPane background;
  @FXML private BorderPane homepageBoardPane;
  @FXML private Image hospitalImage;
  @FXML private ImageView backgroundImageView;
  @FXML private FlowPane dataBaseContainer;
  @FXML private FlowPane conferenceRoomContainer;
  @FXML private FlowPane directionsContainer;
  @FXML private FlowPane furnitureRequestContainer;
  @FXML private FlowPane mapContainer;
  @FXML private FlowPane flowerRequestContainer;
  @FXML private FlowPane signageContainer;
  @FXML private FlowPane mealRequestContainer;
  @FXML private FlowPane officeSuppliesContainer;
  @FXML private FlowPane bottomPane;

  private final double minWidth = 1280;
  private final double minHeight = 650;

  @FXML
  public void initialize() {

    // these lines of code automaticly set the width and height of the imageview based on the size of the window
    backgroundImageView.setFitWidth(App.getPrimaryStage().getWidth());
    backgroundImageView.setFitHeight(App.getPrimaryStage().getHeight());

    App.getPrimaryStage().widthProperty().addListener(((observable, oldValue, newValue) -> {
        backgroundImageView.setFitWidth(App.getPrimaryStage().getWidth());
      if(App.getPrimaryStage().getWidth() < minWidth)
      {
        backgroundImageView.setFitWidth(minWidth);
      }
    }));

    App.getPrimaryStage().heightProperty().addListener(((observable, oldValue, newValue) -> {
      backgroundImageView.setFitHeight(App.getPrimaryStage().getHeight());
      if(App.getPrimaryStage().getHeight() < minHeight)
      {
        backgroundImageView.setFitHeight(minHeight);
      }
    }));


    if(UserSessionToken.getUser() != null && !UserSessionToken.getUser().isAdmin()){
      bottomPane.getChildren().remove(dataBaseContainer);
    }
    setContainerMouseEvents(dataBaseContainer);
    dataBaseContainer.setOnMouseClicked(event -> Navigation.navigate(Screen.DATABASE));

    setContainerMouseEvents(conferenceRoomContainer);
    conferenceRoomContainer.setOnMouseClicked(event -> Navigation.navigate(Screen.CONFERENCE_ROOM));

    setContainerMouseEvents(directionsContainer);
    directionsContainer.setOnMouseClicked(
        event -> Navigation.navigate(Screen.VIEW_SERVICE_REQUEST));
    setContainerMouseEvents(furnitureRequestContainer);
    furnitureRequestContainer.setOnMouseClicked(event -> Navigation.navigate(Screen.FURNITURE_DELIVERY));

    setContainerMouseEvents(mapContainer);
    mapContainer.setOnMouseClicked(event -> Navigation.navigate(Screen.VIEW_MAP));

    setContainerMouseEvents(flowerRequestContainer);
    flowerRequestContainer.setOnMouseClicked(event -> Navigation.navigate(Screen.FLOWER_CHOICE));

    setContainerMouseEvents(signageContainer);
    signageContainer.setOnMouseClicked(event -> Navigation.navigate(Screen.EDIT_SIGNAGE));

    setContainerMouseEvents(mealRequestContainer);
    mealRequestContainer.setOnMouseClicked(event -> Navigation.navigate(Screen.MEAL_RESTAURANT));

    setContainerMouseEvents(officeSuppliesContainer);
    officeSuppliesContainer.setOnMouseClicked(event -> {
      //TODO do something
    });
  }

  private void setContainerMouseEvents(Node container) {
    if (container == null) return;
    container.setOnMouseEntered(
            event -> container.setStyle("-fx-background-color: rgba(160,160,160,0.6)"));
    container.setOnMouseExited(
            event -> container.setStyle("-fx-background-color: rgba(61,61,61,0.5)"));
  }

}
