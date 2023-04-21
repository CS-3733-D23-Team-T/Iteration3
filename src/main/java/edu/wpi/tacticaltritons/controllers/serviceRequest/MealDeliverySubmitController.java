package edu.wpi.tacticaltritons.controllers.serviceRequest;

import edu.wpi.tacticaltritons.App;
import edu.wpi.tacticaltritons.database.DAOFacade;
import edu.wpi.tacticaltritons.database.LocationName;
import edu.wpi.tacticaltritons.database.Login;
import edu.wpi.tacticaltritons.entity.MealDeliveryEntity;
import edu.wpi.tacticaltritons.styling.ThemeColors;
import io.github.palexdev.materialfx.controls.MFXDatePicker;
import io.github.palexdev.materialfx.controls.MFXFilterComboBox;
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.beans.binding.Bindings;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import lombok.Getter;
import net.kurobako.gesturefx.GesturePane;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;


/**
 * Controller for the meal delivery service form page. Collects info on the page and submits to db
 *
 * @author Mark Caleca
 */
public class MealDeliverySubmitController {
    @FXML
    ScrollPane scrollPane, orderPaneScroll;
    @FXML
    BorderPane content;
    @FXML
    FlowPane formPane, orderPane;
    @FXML
    Label restaurantDisplayHeader1, priceDisplay;
    @FXML
    Rectangle orderPaneRectangle, formRectangle;
    @FXML
    StackPane orderPaneStack, formPaneStack;
    @FXML
    Button clearButton, cancelButton, submitButton, clearFormButton, previewButton;
    @FXML
    VBox orderListPane;

    @FXML @Getter
    public MFXTextField firstName, lastName, patientFirstName, patientLastName, time;
    @FXML @Getter
    MFXFilterComboBox<String> staffMemberName, room;
    @FXML
    MFXDatePicker date = new MFXDatePicker();

    @FXML StackPane mapStackPane;

    @FXML GesturePane groundFloor;
    @FXML ImageView groundFloorImage;
    @FXML ImageView lowerLevel1Image;
    @FXML ImageView lowerLevel2Image;
    @FXML ImageView floor1Image;
    @FXML ImageView floor2Image;
    @FXML ImageView floor3Image;
    @FXML Group groundGroup;
    @FXML Group L1Group;
    @FXML Group L2Group;
    @FXML Group floor1Group;
    @FXML Group floor2Group;
    @FXML Group floor3Group;
    //form selection data storage
    MealDeliveryEntity me;

    @FXML public void initialize() throws SQLException {

        //map config
        java.util.Date today = new java.util.Date(2023, 4, 10);
        lowerLevel1Image.setImage(App.lowerlevel1);
        lowerLevel2Image.setImage(App.lowerlevel2);
        groundFloorImage.setImage(App.groundfloor);
        floor1Image.setImage(App.firstfloor);
        floor2Image.setImage(App.secondfloor);
        floor3Image.setImage(App.thirdfloor);

        //form text field config
        List<MFXTextField> nodes = new ArrayList<>();
        nodes.add(firstName);
        nodes.add(lastName);
        nodes.add(patientFirstName);
        nodes.add(patientLastName);
        nodes.add(staffMemberName);
        nodes.add(room);
        nodes.add(date);
        nodes.add(time);
        for (LocationName name : DAOFacade.getAllLocationNames()) { //room list
            room.getItems().add(name.getLongName());
        }
        ArrayList<Login> allPeople = (ArrayList<Login>) DAOFacade.getAllLogins();
        for(Login people: allPeople){ //staff list
            staffMemberName.getItems().add(people.getFirstName() + " " + people.getLastName() + "/" + people.getEmail());
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        time.setText(formatter.format(LocalDateTime.now().plusMinutes(15)));
        formatter = DateTimeFormatter.ofPattern("MMM dd, yyyy");
        date.setValue(LocalDate.from(formatter.parse(formatter.format(LocalDateTime.now()))));

        me = MealDeliveryRequestItemsController.me;

        //init page layout and buttons
        me.initTextFields(firstName, lastName, patientFirstName, patientLastName, time, room, staffMemberName, date);
        me.initSubmitButton(submitButton);
        me.initClearButton(clearButton, orderListPane);
        me.initCancelButton(cancelButton, orderListPane);
        clearFormButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                for(MFXTextField node:nodes){
                    node.clear();
                }
            }
        });
        me.initCheckout(App.getPrimaryStage(),scrollPane,orderListPane,nodes,previewButton);

        //bind panes to reactively resize
        formRectangle.widthProperty().bind(Bindings.max(220,me.imageViewWidth.add(20)));
        formRectangle.heightProperty().bind(Bindings.max(520,me.screenY.subtract(30)));
        restaurantDisplayHeader1.textProperty().bind(me.restaurant);
        priceDisplay.textProperty().bind(Bindings.format("$%.2f", me.price));

        orderPaneScroll.prefHeightProperty().bind(me.screenY.subtract(90));
        orderPaneScroll.prefWidthProperty().bind(Bindings.max(400,me.imageViewWidth.multiply(1.5)));
        orderPaneRectangle.widthProperty().bind(orderPaneScroll.widthProperty().subtract(40));
        orderPaneRectangle.heightProperty().bind(Bindings.max(80,orderListPane.heightProperty().subtract(2)));
        content.setMargin(scrollPane,new Insets(0));

        //map config
        groundFloor.setVisible(true);
        floor1Image.setVisible(true);
        this.room.setOnAction(event -> {

            clearAllNodes();
            Circle circle = new Circle();

            try {
                circle = drawCircle(DAOFacade.getNode((String) this.room.getSelectedItem(), today).getXcoord(), DAOFacade.getNode((String) this.room.getSelectedItem(), today).getYcoord());
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }


            String endFloor = null;
            try {
                endFloor = DAOFacade.getNode((String) this.room.getSelectedItem(), today).getFloor();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

            if (endFloor != null) {
                switch (endFloor) {
                    case "L1":
                        L1Group.setVisible(true);
                        lowerLevel1Image.setVisible(true);
                        this.L1Group.getChildren().add(circle);
                        break;
                    case "L2":
                        L2Group.setVisible(true);
                        lowerLevel2Image.setVisible(true);
                        this.L2Group.getChildren().add(circle);
                        break;
                    case "1":
                        floor1Group.setVisible(true);
                        floor1Image.setVisible(true);
                        this.floor1Group.getChildren().add(circle);
                        break;
                    case "2":
                        floor2Group.setVisible(true);
                        floor2Image.setVisible(true);
                        this.floor2Group.getChildren().add(circle);
                        break;
                    case "3":
                        floor3Group.setVisible(true);
                        floor3Image.setVisible(true);
                        this.floor3Group.getChildren().add(circle);
                        break;
                }
            }
            Point2D centrePoint = new Point2D(circle.getCenterX(), circle.getCenterY());
            System.out.println(centrePoint);
            groundFloor.centreOn(centrePoint);
        });

        groundFloor.setScrollBarPolicy(GesturePane.ScrollBarPolicy.NEVER);
        groundFloor.reset();

    }

    public void clearAllNodes() {
        floor1Group.getChildren().remove(1, floor1Group.getChildren().size());
        floor2Group.getChildren().remove(1, floor2Group.getChildren().size());
        floor3Group.getChildren().remove(1, floor3Group.getChildren().size());
        L1Group.getChildren().remove(1, L1Group.getChildren().size());
        L2Group.getChildren().remove(1, L2Group.getChildren().size());
    }

    public Circle drawCircle(double x, double y) {
        Circle circle = new Circle();
        circle.setVisible(true);
        circle.setFill(Color.web(ThemeColors.PATH_NODE_COLOR.getColor()));
        circle.setStroke(Color.web(ThemeColors.GRAY.getColor()));
        circle.setStrokeWidth(3.0f);
        circle.setCenterX(x);
        circle.setCenterY(y);
        circle.setRadius(10.0);
        return circle;
    }


}