package edu.wpi.tacticaltritons.controllers;

import edu.wpi.tacticaltritons.App;
import edu.wpi.tacticaltritons.auth.UserSessionToken;
import edu.wpi.tacticaltritons.database.*;
import edu.wpi.tacticaltritons.navigation.Navigation;
import edu.wpi.tacticaltritons.navigation.Screen;
import edu.wpi.tacticaltritons.styling.ThemeColors;
import io.github.palexdev.materialfx.controls.MFXDatePicker;
import io.github.palexdev.materialfx.controls.MFXFilterComboBox;
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.beans.binding.Bindings;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import net.kurobako.gesturefx.GesturePane;

import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MoveController extends MapSuperController {
    @FXML
    MFXTextField firstName, lastName;
    @FXML
    MFXFilterComboBox<String> staffMemberName, originalRoom, newRoom;
    @FXML
    MFXDatePicker date;
    @FXML
    Button submitButton, cancelButton, clearButton;

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
    @FXML
    StackPane stackPane;
    List<MFXTextField> fields = new ArrayList<>();

    public MoveController() throws SQLException {
    }

    @FXML
    public void initialize() throws SQLException {

        groundFloor.toBack();
        floor1Group.setVisible(true);
        floor1Image.setVisible(true);

        Date today = new Date(2023, 4, 10);
        lowerLevel1Image.setImage(App.lowerlevel1);
        lowerLevel2Image.setImage(App.lowerlevel2);
        groundFloorImage.setImage(App.groundfloor);
        floor1Image.setImage(App.firstfloor);
        floor2Image.setImage(App.secondfloor);
        floor3Image.setImage(App.thirdfloor);

        List<Node> nodes = DAOFacade.getAllNodes();
        List<Move> moves = DAOFacade.getAllCurrentMoves(Date.valueOf(java.time.LocalDate.now()));
        HashMap<String,Node> nodeHashMap = new HashMap<>();
        for(Move move: moves){
            if(!move.getLocation().getNodeType().equals("HALL")){
                newRoom.getItems().add(Integer.toString(move.getNode().getNodeID()) + " - " + move.getLocation().getShortName());
                nodeHashMap.put(Integer.toString(move.getNode().getNodeID()),move.getNode());
            }
        }

        List<LocationName> names = DAOFacade.getAllLocationNames();

        HashMap<String,LocationName> locationNameHashMap = new HashMap<>();

        for (Move move : moves) {
            if(!move.getLocation().getNodeType().equals("HALL")) {
                originalRoom.getItems().add(move.getLocation().getLongName());
                locationNameHashMap.put(move.getLocation().getLongName(), move.getLocation());
            }
        }

        ArrayList<Login> allPeople = (ArrayList<Login>) DAOFacade.getAllLogins();
        for(Login people: allPeople){ //generate staff list dropdown
            staffMemberName.getItems().add(people.getFirstName() + " " + people.getLastName() + "/" + people.getEmail());
        }

        fields.add(firstName);
        fields.add(lastName);
        fields.add(staffMemberName);
        fields.add(originalRoom);
        fields.add(newRoom);
        fields.add(date);

        firstName.setText(UserSessionToken.getUser().getFirstname());
        lastName.setText(UserSessionToken.getUser().getLastname());

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM dd, yyyy");
        date.setValue(LocalDate.from(formatter.parse(formatter.format(LocalDateTime.now()))));

        for (MFXTextField field : fields) {
            field.setPrefHeight(45);
//            field.prefWidthProperty().bind(Bindings.max(200, App.getPrimaryStage().widthProperty().divide(3)).subtract(40));
            field.textProperty().addListener(((observable, oldValue, newValue) -> {
                formComplete();//check if re-enable submit button
            }));
        }

        submitButton.setDisable(true);
        submitButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                String pattern = "yyyy-MM-dd";
                DateTimeFormatter df = DateTimeFormatter.ofPattern(pattern);
                String newDate = (date.getText().isEmpty()) ? df.format(LocalDate.now()) : df.format(date.getValue());
                Date sendDate = Date.valueOf(newDate);
                try {
                    DAOFacade.addMove(new edu.wpi.tacticaltritons.database.Move(nodeHashMap.get(newRoom.getText().split(" -")[0]),locationNameHashMap.get(originalRoom.getText()),sendDate));
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
                Navigation.navigate(Screen.HOME);
            }
        });
        cancelButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                clearForm();
                Navigation.navigate(Screen.HOME);
            }
        });
        clearButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                clearForm();
                formComplete();
            }
        });

        stackPane.setVisible(true);
        groundFloor.setVisible(true);
        floor1Group.setVisible(true);

        this.newRoom.setOnAction(event -> {

            clearAllNodes();
            Circle circle = new Circle();

            try {
                circle = drawCircle(DAOFacade.getNode(Integer.parseInt(this.newRoom.getSelectedItem().split(" -")[0])).getXcoord(), DAOFacade.getNode(Integer.parseInt(this.newRoom.getSelectedItem().split(" -")[0])).getYcoord(), Color.BLUE, Color.BLACK);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

            String endFloor = null;
            try {
                endFloor = DAOFacade.getNode(Integer.parseInt(this.newRoom.getSelectedItem().split(" -")[0])).getFloor();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

            resetImages();


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

        this.originalRoom.setOnAction(event -> {

            clearAllNodes();
            Circle circle = new Circle();

            try {
                circle = drawCircle(DAOFacade.getNode((String) this.originalRoom.getSelectedItem(), today).getXcoord(), DAOFacade.getNode((String) this.originalRoom.getSelectedItem(), today).getYcoord(), Color.RED, Color.BLACK );
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

            String endFloor = null;
            try {
                endFloor = DAOFacade.getNode((String) this.originalRoom.getSelectedItem(), today).getFloor();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

            resetImages();

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

    private boolean formComplete(){
        boolean complete = !(firstName.getText().isEmpty()
            || lastName.getText().isEmpty()
            || originalRoom.getText().isEmpty()
            || newRoom.getText().isEmpty()
            || date.getText().isEmpty());
        if(complete){
            submitButton.setDisable(false);
        }
        else{
            submitButton.setDisable(true);
        }
        return complete;
    }
    private void clearForm(){
        for(MFXTextField field:fields){
            field.clear();
        }
    }
}
