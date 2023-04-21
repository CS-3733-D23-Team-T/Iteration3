package edu.wpi.tacticaltritons.controllers.serviceRequest;

import edu.wpi.tacticaltritons.App;
import edu.wpi.tacticaltritons.entity.MealDeliveryEntity;
import io.github.palexdev.materialfx.controls.MFXScrollPane;
import javafx.fxml.FXML;
import javafx.scene.layout.FlowPane;

import java.sql.SQLException;


/**
 * Controller for the meal delivery service restaurant select page. Collects info on the page and stores it
 *
 * @author Mark Caleca
 */
public class MealDeliveryRequestRestaurantController {
    @FXML private MFXScrollPane scrollPane;
    @FXML private FlowPane restaurantPane;

    //form selection data storage
    static MealDeliveryEntity me;
    @FXML public void initialize() throws SQLException {
        me = new MealDeliveryEntity();
        //init screen by reading database and displaying available restaurants
        me.initRestaurant(App.getPrimaryStage(),scrollPane,restaurantPane);
    }
}