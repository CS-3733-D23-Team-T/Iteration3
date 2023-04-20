package edu.wpi.tacticaltritons.controllers.serviceRequest;

import edu.wpi.tacticaltritons.App;
import edu.wpi.tacticaltritons.entity.MealDeliveryEntity;
import edu.wpi.tacticaltritons.navigation.Navigation;
import edu.wpi.tacticaltritons.navigation.Screen;
import javafx.beans.binding.Bindings;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.*;
import javafx.scene.shape.Rectangle;
import lombok.Getter;

import java.sql.SQLException;


/**
 * Controller for the meal delivery service request item page. Collects info on the page and stores it
 *
 * @author Mark Caleca
 */
public class MealDeliveryRequestItemsController {
    @FXML
    ScrollPane scrollPane, orderPaneScroll;
    @FXML
    BorderPane content;
    @FXML
    GridPane gridMeal;
    @FXML
    FlowPane restaurantPane, orderPane;
    @FXML
    Label restaurantDisplayHeader, restaurantDisplayHeader1, priceDisplay;
    @FXML
    Rectangle headerRectangle, orderPaneRectangle, restaurantRectangle;
    @FXML
    StackPane headerStackPane, orderPaneStack, restaurantPaneStack;
    @FXML
    Button clearButton, cancelButton, checkoutButton;
    @FXML @Getter
    VBox orderListPane;
    //form selection data storage
    static MealDeliveryEntity me;

    @FXML
    public void initialize() throws SQLException {
        me = MealDeliveryRequestRestaurantController.me;

//        me.initCheckoutButton(checkoutButton, orderListPane);
        me.initClearButton(clearButton, orderListPane);
        me.initCancelButton(cancelButton, orderListPane);
        me.initCheckoutButton(checkoutButton, orderListPane);
        me.initCheckoutButton(checkoutButton, orderListPane);

        me.initItems(App.getPrimaryStage(), scrollPane, restaurantPane,orderListPane);

        restaurantRectangle.widthProperty().bind(restaurantPane.widthProperty().subtract(40));
        restaurantRectangle.heightProperty().bind(restaurantPane.heightProperty());

        restaurantDisplayHeader.textProperty().bind(me.restaurant);
        restaurantDisplayHeader1.textProperty().bind(me.restaurant);
        priceDisplay.textProperty().bind(Bindings.format("$%.2f", me.price));

        orderPaneScroll.prefHeightProperty().bind(me.screenY.subtract(90));
        orderPaneScroll.prefWidthProperty().bind(me.screenX.subtract(restaurantPane.widthProperty().add(80)));
        orderPaneRectangle.widthProperty().bind(orderPaneScroll.widthProperty().subtract(40));
        orderPaneRectangle.heightProperty().bind(Bindings.max(80,orderListPane.heightProperty().subtract(2)));

//        orderPaneRectangle.heightProperty().bind(me.screenY.subtract(clearButton.heightProperty().add(20)));
//        orderPaneRectangle.widthProperty().bind(me.screenX.subtract(restaurantPane.widthProperty().add(50)));
        restaurantRectangle.widthProperty().bind(restaurantPane.widthProperty().subtract(40));
//        orderPaneScroll.prefWidthProperty().bind(orderPaneRectangle.widthProperty().add(40));
//        orderPaneScroll.prefHeightProperty().bind(orderPaneRectangle.heightProperty().add(40));

    }
}
