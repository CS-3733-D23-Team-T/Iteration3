package edu.wpi.tacticaltritons.controllers.serviceRequest;

import edu.wpi.tacticaltritons.App;
import edu.wpi.tacticaltritons.entity.MealDeliveryEntity;
import io.github.palexdev.materialfx.controls.MFXScrollPane;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.shape.Rectangle;

import java.sql.SQLException;


/**
 * Controller for the meal delivery service request item page. Collects info on the page and stores it
 *
 * @author Mark Caleca
 */
public class MealDeliveryRequestRestaurantController {
    @FXML private MFXScrollPane scrollPane;
    @FXML private BorderPane content;
//    GridPane grid;
    @FXML private GridPane gridMeal;
    @FXML private FlowPane restaurantPane;

    //form selection data storage
    static MealDeliveryEntity me;
    @FXML public void initialize() throws SQLException {
        me = new MealDeliveryEntity();

        gridMeal.getStylesheets().add("/edu/wpi/tacticaltritons/stylesheets/ServiceRequest.css");

        me.initRestaurant(App.getPrimaryStage(),scrollPane,restaurantPane);

    }
}
