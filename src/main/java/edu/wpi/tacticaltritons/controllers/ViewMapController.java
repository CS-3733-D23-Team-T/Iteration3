package edu.wpi.tacticaltritons.controllers;

import edu.wpi.tacticaltritons.App;
import edu.wpi.tacticaltritons.database.*;
import edu.wpi.tacticaltritons.database.Move;
import edu.wpi.tacticaltritons.navigation.Navigation;
import edu.wpi.tacticaltritons.navigation.Screen;
import io.github.palexdev.materialfx.controls.*;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Polyline;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import net.kurobako.gesturefx.GesturePane;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;


public class ViewMapController {

    @FXML
    private MFXFilterComboBox<String> startLocation = new MFXFilterComboBox<>();

    @FXML private StackPane selectedEditPane;

    @FXML
    private MFXFilterComboBox<String> endLocation = new MFXFilterComboBox<>();

    @FXML
    private MFXButton applyAndSave;

    @FXML
    private MFXComboBox<String> comboBox;

    @FXML
    private MFXComboBox<String> rooms;

    @FXML
    private MFXButton preview;

    @FXML
    private MFXTextField searchOnMap;

    @FXML
    private GesturePane groundFloor;

    @FXML
    private ImageView groundFloorImage;

    @FXML
    private ImageView lowerLevel1Image;

    @FXML
    private ImageView lowerLevel2Image;

    @FXML
    private ImageView floor1Image;

    @FXML
    private ImageView floor2Image;

    @FXML
    private ImageView floor3Image;

    @FXML
    private Polyline path;
    @FXML
    private MFXCheckbox bathrooms;

    @FXML
    private MFXCheckbox restrooms;
    @FXML
    private MFXCheckbox exits;
    @FXML
    private MFXCheckbox elevators;
    @FXML
    private MFXCheckbox stairs;
    @FXML
    private MFXCheckbox hallways;
    @FXML
    private MFXCheckbox departments;
    @FXML
    private MFXCheckbox labs;
    @FXML
    private MFXCheckbox services;
    @FXML
    private MFXCheckbox infoDesks;
    @FXML
    private MFXCheckbox conferenceRooms;
    @FXML
    private MFXCheckbox retail;
    @FXML
    private MFXButton viewNodes;

    @FXML
    private StackPane selectedFloorPane;

    @FXML
    private GesturePane circlePane;
    @FXML
    private Group groundGroup;
    @FXML
    private Group L1Group;
    @FXML
    private Group L2Group;
    @FXML
    private Group floor1Group;
    @FXML
    private Group floor2Group;
    @FXML
    private Group floor3Group;

    @FXML
    private MFXButton removeNodes;

    @FXML private Group editGroup1;
    @FXML private Group editGroup2;
    @FXML private Group editGroup3;

    @FXML
    private MFXButton editMap;

    @FXML private MFXButton pathfindingPage;


    @FXML
    private void initialize() throws SQLException {
        lowerLevel1Image.setImage(App.lowerlevel1);
        lowerLevel2Image.setImage(App.lowerlevel2);
        groundFloorImage.setImage(App.groundfloor);
        floor1Image.setImage(App.firstfloor);
        floor2Image.setImage(App.secondfloor);
        floor3Image.setImage(App.thirdfloor);


        Date today = new Date(2023, 4, 10);

        DAOFacade datas = new DAOFacade();

        List<Double> xCoord = new ArrayList<Double>();
        List<Double> yCoord = new ArrayList<Double>();
        List<Double> startEnd = new ArrayList<Double>();
        List<Integer> nodeIDs = new ArrayList<Integer>();
        List<Move> allMoves = DAOFacade.getAllMoves();

        HashMap<Integer, Move> hash = new HashMap<>();
        for (Move move : allMoves) {
            hash.put(move.getNode().getNodeID(), move);
        }

        this.removeNodes.setOnAction(event -> {
            clearAllNodes();
        });

        this.editMap.setOnAction(
                event -> {
                    Navigation.navigate(Screen.EDIT_MAP);
                });

        this.pathfindingPage.setOnAction(
                event -> {
                    Navigation.navigate(Screen.PATHFINDING);
                });

        this.viewNodes.setOnAction(event -> {
            clearAllNodes();
            if(restrooms.isSelected()==true){
                try {
                    for (Node node : DAOFacade.getAllNodes()) {
                        if (node.getFloor().equals(this.comboBox.getText().substring(0, this.comboBox.getText().indexOf(" ")))) {
                            if(hash.get(node.getNodeID()).getLocation().getNodeType().equals("REST")){
                                xCoord.add((double) node.getXcoord());
                            yCoord.add((double) node.getYcoord());
                            nodeIDs.add(node.getNodeID());}
                        }
                    }
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }


                for (int coordinate = 0; coordinate < xCoord.size() - 1; coordinate++) {
                    Circle circle = drawCircle(xCoord.get(coordinate), yCoord.get(coordinate));
                    Text text = new Text("");
                    Text textLocation = new Text("");
                    text.setFill(Color.WHITE);
                    text.setStroke(Color.MEDIUMBLUE);
                    text.setFont(Font.font("Ariel", FontWeight.BOLD, 17));
                    text.toFront();
                    LocationName location = hash.get(nodeIDs.get(coordinate)).getLocation();
                    if (!location.getNodeType().equals("HALL")) {
                        text.setText(hash.get(nodeIDs.get(coordinate)).getLocation().getShortName());
                    }
                    text.setX(xCoord.get(coordinate) - (text.getLayoutBounds().getWidth() / 2));
                    text.setY(yCoord.get(coordinate) + (circle.getRadius() * 2));

                    circle.setOnMouseClicked(event1 -> {
                        clearAllNodes();
                        Circle newCircle = drawCircle(circle.getCenterX(), circle.getCenterY());
                        newCircle.setFill(Color.PINK);
                        newCircle.setStroke(Color.RED);
                        newCircle.setVisible(true);

                        switch (this.comboBox.getText()) {
                            case "Ground Floor":
                                this.groundGroup.getChildren().add(newCircle);
                                break;
                            case "L1 - Lower Level 1":
                                this.L1Group.getChildren().add(newCircle);
                                break;
                            case "L2 - Lower Level 2":
                                this.L2Group.getChildren().add(newCircle);
                                break;
                            case "1 - 1st Floor":
                                this.floor1Group.getChildren().add(newCircle);
                                break;
                            case "2 - 2nd Floor":
                                this.floor2Group.getChildren().add(newCircle);
                                break;
                            case "3 - 3rd Floor":
                                this.floor3Group.getChildren().add(newCircle);
                                break;
                        }

                        try {
                            for (Move move : DAOFacade.getAllMoves()) {
                                if (move.getNode().getXcoord() == circle.getCenterX() && move.getNode().getYcoord() == circle.getCenterY()) {
                                    this.startLocation.setText(move.getLocation().getLongName());
                                    textLocation.setFill(Color.WHITE);
                                    textLocation.setStroke(Color.MEDIUMBLUE);
                                    textLocation.setFont(Font.font("Ariel", FontWeight.BOLD, 17));
                                    textLocation.toFront();
                                    textLocation.setText(move.getLocation().getShortName());
                                    textLocation.setX(move.getNode().getXcoord() - (textLocation.getLayoutBounds().getWidth() / 2));
                                    textLocation.setY(move.getNode().getYcoord() + (10 * 2));
                                    switch (this.comboBox.getText()) {
                                        case "Ground Floor":
                                            this.groundGroup.getChildren().add(textLocation);
                                            break;
                                        case "L1 - Lower Level 1":
                                            this.L1Group.getChildren().add(textLocation);
                                            break;
                                        case "L2 - Lower Level 2":
                                            this.L2Group.getChildren().add(textLocation);
                                            break;
                                        case "1 - 1st Floor":
                                            this.floor1Group.getChildren().add(textLocation);
                                            break;
                                        case "2 - 2nd Floor":
                                            this.floor2Group.getChildren().add(textLocation);
                                            break;
                                        case "3 - 3rd Floor":
                                            this.floor3Group.getChildren().add(textLocation);
                                            break;
                                    }
                                }
                            }
                        } catch (SQLException e) {
                            throw new RuntimeException(e);
                        }



                    });
                    switch (this.comboBox.getText()) {
                        case "Ground Floor":
                            this.groundGroup.getChildren().addAll(circle, text);
                            break;
                        case "L1 - Lower Level 1":
                            this.L1Group.getChildren().addAll(circle, text);
                            break;
                        case "L2 - Lower Level 2":
                            this.L2Group.getChildren().addAll(circle, text);
                            break;
                        case "1 - 1st Floor":
                            this.floor1Group.getChildren().addAll(circle, text);
                            break;
                        case "2 - 2nd Floor":
                            this.floor2Group.getChildren().addAll(circle, text);
                            break;
                        case "3 - 3rd Floor":
                            this.floor3Group.getChildren().addAll(circle, text);
                            break;
                    }
                }

                xCoord.clear();
                yCoord.clear();
                nodeIDs.clear();
            }

            if(elevators.isSelected()==true){
                try {
                    for (Node node : DAOFacade.getAllNodes()) {
                        if (node.getFloor().equals(this.comboBox.getText().substring(0, this.comboBox.getText().indexOf(" ")))) {
                            if(hash.get(node.getNodeID()).getLocation().getNodeType().equals("ELEV")){
                                xCoord.add((double) node.getXcoord());
                            yCoord.add((double) node.getYcoord());
                            nodeIDs.add(node.getNodeID());}
                        }
                    }
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }


                for (int coordinate = 0; coordinate < xCoord.size() - 1; coordinate++) {
                    Circle circle = drawCircle(xCoord.get(coordinate), yCoord.get(coordinate));
                    Text text = new Text("");
                    Text textLocation = new Text("");
                    text.setFill(Color.WHITE);
                    text.setStroke(Color.MEDIUMBLUE);
                    text.setFont(Font.font("Ariel", FontWeight.BOLD, 17));
                    text.toFront();
                    LocationName location = hash.get(nodeIDs.get(coordinate)).getLocation();
                    if (!location.getNodeType().equals("HALL")) {
                        text.setText(hash.get(nodeIDs.get(coordinate)).getLocation().getShortName());
                    }
                    text.setX(xCoord.get(coordinate) - (text.getLayoutBounds().getWidth() / 2));
                    text.setY(yCoord.get(coordinate) + (circle.getRadius() * 2));

                    circle.setOnMouseClicked(event1 -> {
                        clearAllNodes();
                        Circle newCircle = drawCircle(circle.getCenterX(), circle.getCenterY());
                        newCircle.setFill(Color.PINK);
                        newCircle.setStroke(Color.RED);
                        newCircle.setVisible(true);

                        switch (this.comboBox.getText()) {
                            case "Ground Floor":
                                this.groundGroup.getChildren().add(newCircle);
                                break;
                            case "L1 - Lower Level 1":
                                this.L1Group.getChildren().add(newCircle);
                                break;
                            case "L2 - Lower Level 2":
                                this.L2Group.getChildren().add(newCircle);
                                break;
                            case "1 - 1st Floor":
                                this.floor1Group.getChildren().add(newCircle);
                                break;
                            case "2 - 2nd Floor":
                                this.floor2Group.getChildren().add(newCircle);
                                break;
                            case "3 - 3rd Floor":
                                this.floor3Group.getChildren().add(newCircle);
                                break;
                        }

                        try {
                            for (Move move : DAOFacade.getAllMoves()) {
                                if (move.getNode().getXcoord() == circle.getCenterX() && move.getNode().getYcoord() == circle.getCenterY()) {
                                    this.startLocation.setText(move.getLocation().getLongName());
                                    textLocation.setFill(Color.WHITE);
                                    textLocation.setStroke(Color.MEDIUMBLUE);
                                    textLocation.setFont(Font.font("Ariel", FontWeight.BOLD, 17));
                                    textLocation.toFront();
                                    textLocation.setText(move.getLocation().getShortName());
                                    textLocation.setX(move.getNode().getXcoord() - (textLocation.getLayoutBounds().getWidth() / 2));
                                    textLocation.setY(move.getNode().getYcoord() + (10 * 2));
                                    switch (this.comboBox.getText()) {
                                        case "Ground Floor":
                                            this.groundGroup.getChildren().add(textLocation);
                                            break;
                                        case "L1 - Lower Level 1":
                                            this.L1Group.getChildren().add(textLocation);
                                            break;
                                        case "L2 - Lower Level 2":
                                            this.L2Group.getChildren().add(textLocation);
                                            break;
                                        case "1 - 1st Floor":
                                            this.floor1Group.getChildren().add(textLocation);
                                            break;
                                        case "2 - 2nd Floor":
                                            this.floor2Group.getChildren().add(textLocation);
                                            break;
                                        case "3 - 3rd Floor":
                                            this.floor3Group.getChildren().add(textLocation);
                                            break;
                                    }
                                }
                            }
                        } catch (SQLException e) {
                            throw new RuntimeException(e);
                        }



                    });
                    switch (this.comboBox.getText()) {
                        case "Ground Floor":
                            this.groundGroup.getChildren().addAll(circle, text);
                            break;
                        case "L1 - Lower Level 1":
                            this.L1Group.getChildren().addAll(circle, text);
                            break;
                        case "L2 - Lower Level 2":
                            this.L2Group.getChildren().addAll(circle, text);
                            break;
                        case "1 - 1st Floor":
                            this.floor1Group.getChildren().addAll(circle, text);
                            break;
                        case "2 - 2nd Floor":
                            this.floor2Group.getChildren().addAll(circle, text);
                            break;
                        case "3 - 3rd Floor":
                            this.floor3Group.getChildren().addAll(circle, text);
                            break;
                    }
                }

                xCoord.clear();
                yCoord.clear();
                nodeIDs.clear();
            }

            if(stairs.isSelected()==true){
                try {
                    for (Node node : DAOFacade.getAllNodes()) {
                        if (node.getFloor().equals(this.comboBox.getText().substring(0, this.comboBox.getText().indexOf(" ")))) {
                            if(hash.get(node.getNodeID()).getLocation().getNodeType().equals("STAI")){
                                xCoord.add((double) node.getXcoord());
                            yCoord.add((double) node.getYcoord());
                            nodeIDs.add(node.getNodeID());}
                        }
                    }
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }


                for (int coordinate = 0; coordinate < xCoord.size() - 1; coordinate++) {
                    Circle circle = drawCircle(xCoord.get(coordinate), yCoord.get(coordinate));
                    Text text = new Text("");
                    Text textLocation = new Text("");
                    text.setFill(Color.WHITE);
                    text.setStroke(Color.MEDIUMBLUE);
                    text.setFont(Font.font("Ariel", FontWeight.BOLD, 17));
                    text.toFront();
                    LocationName location = hash.get(nodeIDs.get(coordinate)).getLocation();
                    if (!location.getNodeType().equals("HALL")) {
                        text.setText(hash.get(nodeIDs.get(coordinate)).getLocation().getShortName());
                    }
                    text.setX(xCoord.get(coordinate) - (text.getLayoutBounds().getWidth() / 2));
                    text.setY(yCoord.get(coordinate) + (circle.getRadius() * 2));

                    circle.setOnMouseClicked(event1 -> {
                        clearAllNodes();
                        Circle newCircle = drawCircle(circle.getCenterX(), circle.getCenterY());
                        newCircle.setFill(Color.PINK);
                        newCircle.setStroke(Color.RED);
                        newCircle.setVisible(true);

                        switch (this.comboBox.getText()) {
                            case "Ground Floor":
                                this.groundGroup.getChildren().add(newCircle);
                                break;
                            case "L1 - Lower Level 1":
                                this.L1Group.getChildren().add(newCircle);
                                break;
                            case "L2 - Lower Level 2":
                                this.L2Group.getChildren().add(newCircle);
                                break;
                            case "1 - 1st Floor":
                                this.floor1Group.getChildren().add(newCircle);
                                break;
                            case "2 - 2nd Floor":
                                this.floor2Group.getChildren().add(newCircle);
                                break;
                            case "3 - 3rd Floor":
                                this.floor3Group.getChildren().add(newCircle);
                                break;
                        }

                        try {
                            for (Move move : DAOFacade.getAllMoves()) {
                                if (move.getNode().getXcoord() == circle.getCenterX() && move.getNode().getYcoord() == circle.getCenterY()) {
                                    this.startLocation.setText(move.getLocation().getLongName());
                                    textLocation.setFill(Color.WHITE);
                                    textLocation.setStroke(Color.MEDIUMBLUE);
                                    textLocation.setFont(Font.font("Ariel", FontWeight.BOLD, 17));
                                    textLocation.toFront();
                                    textLocation.setText(move.getLocation().getShortName());
                                    textLocation.setX(move.getNode().getXcoord() - (textLocation.getLayoutBounds().getWidth() / 2));
                                    textLocation.setY(move.getNode().getYcoord() + (10 * 2));
                                    switch (this.comboBox.getText()) {
                                        case "Ground Floor":
                                            this.groundGroup.getChildren().add(textLocation);
                                            break;
                                        case "L1 - Lower Level 1":
                                            this.L1Group.getChildren().add(textLocation);
                                            break;
                                        case "L2 - Lower Level 2":
                                            this.L2Group.getChildren().add(textLocation);
                                            break;
                                        case "1 - 1st Floor":
                                            this.floor1Group.getChildren().add(textLocation);
                                            break;
                                        case "2 - 2nd Floor":
                                            this.floor2Group.getChildren().add(textLocation);
                                            break;
                                        case "3 - 3rd Floor":
                                            this.floor3Group.getChildren().add(textLocation);
                                            break;
                                    }
                                }
                            }
                        } catch (SQLException e) {
                            throw new RuntimeException(e);
                        }



                    });
                    switch (this.comboBox.getText()) {
                        case "Ground Floor":
                            this.groundGroup.getChildren().addAll(circle, text);
                            break;
                        case "L1 - Lower Level 1":
                            this.L1Group.getChildren().addAll(circle, text);
                            break;
                        case "L2 - Lower Level 2":
                            this.L2Group.getChildren().addAll(circle, text);
                            break;
                        case "1 - 1st Floor":
                            this.floor1Group.getChildren().addAll(circle, text);
                            break;
                        case "2 - 2nd Floor":
                            this.floor2Group.getChildren().addAll(circle, text);
                            break;
                        case "3 - 3rd Floor":
                            this.floor3Group.getChildren().addAll(circle, text);
                            break;
                    }
                }

                xCoord.clear();
                yCoord.clear();
                nodeIDs.clear();
            }

            if(hallways.isSelected()==true){
                try {
                    for (Node node : DAOFacade.getAllNodes()) {
                        if (node.getFloor().equals(this.comboBox.getText().substring(0, this.comboBox.getText().indexOf(" ")))) {
                            if(hash.get(node.getNodeID()).getLocation().getNodeType().equals("HALL")){
                                xCoord.add((double) node.getXcoord());
                            yCoord.add((double) node.getYcoord());
                            nodeIDs.add(node.getNodeID());}
                        }
                    }
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }


                for (int coordinate = 0; coordinate < xCoord.size() - 1; coordinate++) {
                    Circle circle = drawCircle(xCoord.get(coordinate), yCoord.get(coordinate));
                    Text text = new Text("");
                    Text textLocation = new Text("");
                    text.setFill(Color.WHITE);
                    text.setStroke(Color.MEDIUMBLUE);
                    text.setFont(Font.font("Ariel", FontWeight.BOLD, 17));
                    text.toFront();
                    LocationName location = hash.get(nodeIDs.get(coordinate)).getLocation();
                    if (!location.getNodeType().equals("HALL")) {
                        text.setText(hash.get(nodeIDs.get(coordinate)).getLocation().getShortName());
                    }
                    text.setX(xCoord.get(coordinate) - (text.getLayoutBounds().getWidth() / 2));
                    text.setY(yCoord.get(coordinate) + (circle.getRadius() * 2));

                    circle.setOnMouseClicked(event1 -> {
                        clearAllNodes();
                        Circle newCircle = drawCircle(circle.getCenterX(), circle.getCenterY());
                        newCircle.setFill(Color.PINK);
                        newCircle.setStroke(Color.RED);
                        newCircle.setVisible(true);

                        switch (this.comboBox.getText()) {
                            case "Ground Floor":
                                this.groundGroup.getChildren().add(newCircle);
                                break;
                            case "L1 - Lower Level 1":
                                this.L1Group.getChildren().add(newCircle);
                                break;
                            case "L2 - Lower Level 2":
                                this.L2Group.getChildren().add(newCircle);
                                break;
                            case "1 - 1st Floor":
                                this.floor1Group.getChildren().add(newCircle);
                                break;
                            case "2 - 2nd Floor":
                                this.floor2Group.getChildren().add(newCircle);
                                break;
                            case "3 - 3rd Floor":
                                this.floor3Group.getChildren().add(newCircle);
                                break;
                        }

                        try {
                            for (Move move : DAOFacade.getAllMoves()) {
                                if (move.getNode().getXcoord() == circle.getCenterX() && move.getNode().getYcoord() == circle.getCenterY()) {
                                    this.startLocation.setText(move.getLocation().getLongName());
                                    textLocation.setFill(Color.WHITE);
                                    textLocation.setStroke(Color.MEDIUMBLUE);
                                    textLocation.setFont(Font.font("Ariel", FontWeight.BOLD, 17));
                                    textLocation.toFront();
                                    textLocation.setText(move.getLocation().getShortName());
                                    textLocation.setX(move.getNode().getXcoord() - (textLocation.getLayoutBounds().getWidth() / 2));
                                    textLocation.setY(move.getNode().getYcoord() + (10 * 2));
                                    switch (this.comboBox.getText()) {
                                        case "Ground Floor":
                                            this.groundGroup.getChildren().add(textLocation);
                                            break;
                                        case "L1 - Lower Level 1":
                                            this.L1Group.getChildren().add(textLocation);
                                            break;
                                        case "L2 - Lower Level 2":
                                            this.L2Group.getChildren().add(textLocation);
                                            break;
                                        case "1 - 1st Floor":
                                            this.floor1Group.getChildren().add(textLocation);
                                            break;
                                        case "2 - 2nd Floor":
                                            this.floor2Group.getChildren().add(textLocation);
                                            break;
                                        case "3 - 3rd Floor":
                                            this.floor3Group.getChildren().add(textLocation);
                                            break;
                                    }
                                }
                            }
                        } catch (SQLException e) {
                            throw new RuntimeException(e);
                        }



                    });
                    switch (this.comboBox.getText()) {
                        case "Ground Floor":
                            this.groundGroup.getChildren().addAll(circle, text);
                            break;
                        case "L1 - Lower Level 1":
                            this.L1Group.getChildren().addAll(circle, text);
                            break;
                        case "L2 - Lower Level 2":
                            this.L2Group.getChildren().addAll(circle, text);
                            break;
                        case "1 - 1st Floor":
                            this.floor1Group.getChildren().addAll(circle, text);
                            break;
                        case "2 - 2nd Floor":
                            this.floor2Group.getChildren().addAll(circle, text);
                            break;
                        case "3 - 3rd Floor":
                            this.floor3Group.getChildren().addAll(circle, text);
                            break;
                    }
                }

                xCoord.clear();
                yCoord.clear();
                nodeIDs.clear();
            }

            if(departments.isSelected()==true){
                try {
                    for (Node node : DAOFacade.getAllNodes()) {
                        if (node.getFloor().equals(this.comboBox.getText().substring(0, this.comboBox.getText().indexOf(" ")))) {
                            if(hash.get(node.getNodeID()).getLocation().getNodeType().equals("DEPT")){
                                xCoord.add((double) node.getXcoord());
                            yCoord.add((double) node.getYcoord());
                            nodeIDs.add(node.getNodeID());}
                        }
                    }
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }


                for (int coordinate = 0; coordinate < xCoord.size() - 1; coordinate++) {
                    Circle circle = drawCircle(xCoord.get(coordinate), yCoord.get(coordinate));
                    Text text = new Text("");
                    Text textLocation = new Text("");
                    text.setFill(Color.WHITE);
                    text.setStroke(Color.MEDIUMBLUE);
                    text.setFont(Font.font("Ariel", FontWeight.BOLD, 17));
                    text.toFront();
                    LocationName location = hash.get(nodeIDs.get(coordinate)).getLocation();
                    if (!location.getNodeType().equals("HALL")) {
                        text.setText(hash.get(nodeIDs.get(coordinate)).getLocation().getShortName());
                    }
                    text.setX(xCoord.get(coordinate) - (text.getLayoutBounds().getWidth() / 2));
                    text.setY(yCoord.get(coordinate) + (circle.getRadius() * 2));

                    circle.setOnMouseClicked(event1 -> {
                        clearAllNodes();
                        Circle newCircle = drawCircle(circle.getCenterX(), circle.getCenterY());
                        newCircle.setFill(Color.PINK);
                        newCircle.setStroke(Color.RED);
                        newCircle.setVisible(true);

                        switch (this.comboBox.getText()) {
                            case "Ground Floor":
                                this.groundGroup.getChildren().add(newCircle);
                                break;
                            case "L1 - Lower Level 1":
                                this.L1Group.getChildren().add(newCircle);
                                break;
                            case "L2 - Lower Level 2":
                                this.L2Group.getChildren().add(newCircle);
                                break;
                            case "1 - 1st Floor":
                                this.floor1Group.getChildren().add(newCircle);
                                break;
                            case "2 - 2nd Floor":
                                this.floor2Group.getChildren().add(newCircle);
                                break;
                            case "3 - 3rd Floor":
                                this.floor3Group.getChildren().add(newCircle);
                                break;
                        }

                        try {
                            for (Move move : DAOFacade.getAllMoves()) {
                                if (move.getNode().getXcoord() == circle.getCenterX() && move.getNode().getYcoord() == circle.getCenterY()) {
                                    this.startLocation.setText(move.getLocation().getLongName());
                                    textLocation.setFill(Color.WHITE);
                                    textLocation.setStroke(Color.MEDIUMBLUE);
                                    textLocation.setFont(Font.font("Ariel", FontWeight.BOLD, 17));
                                    textLocation.toFront();
                                    textLocation.setText(move.getLocation().getShortName());
                                    textLocation.setX(move.getNode().getXcoord() - (textLocation.getLayoutBounds().getWidth() / 2));
                                    textLocation.setY(move.getNode().getYcoord() + (10 * 2));
                                    switch (this.comboBox.getText()) {
                                        case "Ground Floor":
                                            this.groundGroup.getChildren().add(textLocation);
                                            break;
                                        case "L1 - Lower Level 1":
                                            this.L1Group.getChildren().add(textLocation);
                                            break;
                                        case "L2 - Lower Level 2":
                                            this.L2Group.getChildren().add(textLocation);
                                            break;
                                        case "1 - 1st Floor":
                                            this.floor1Group.getChildren().add(textLocation);
                                            break;
                                        case "2 - 2nd Floor":
                                            this.floor2Group.getChildren().add(textLocation);
                                            break;
                                        case "3 - 3rd Floor":
                                            this.floor3Group.getChildren().add(textLocation);
                                            break;
                                    }
                                }
                            }
                        } catch (SQLException e) {
                            throw new RuntimeException(e);
                        }



                    });
                    switch (this.comboBox.getText()) {
                        case "Ground Floor":
                            this.groundGroup.getChildren().addAll(circle, text);
                            break;
                        case "L1 - Lower Level 1":
                            this.L1Group.getChildren().addAll(circle, text);
                            break;
                        case "L2 - Lower Level 2":
                            this.L2Group.getChildren().addAll(circle, text);
                            break;
                        case "1 - 1st Floor":
                            this.floor1Group.getChildren().addAll(circle, text);
                            break;
                        case "2 - 2nd Floor":
                            this.floor2Group.getChildren().addAll(circle, text);
                            break;
                        case "3 - 3rd Floor":
                            this.floor3Group.getChildren().addAll(circle, text);
                            break;
                    }
                }

                xCoord.clear();
                yCoord.clear();
                nodeIDs.clear();
            }

            if(labs.isSelected()==true){
                try {
                    for (Node node : DAOFacade.getAllNodes()) {
                        if (node.getFloor().equals(this.comboBox.getText().substring(0, this.comboBox.getText().indexOf(" ")))) {
                            if(hash.get(node.getNodeID()).getLocation().getNodeType().equals("LABS")){
                                xCoord.add((double) node.getXcoord());
                            yCoord.add((double) node.getYcoord());
                            nodeIDs.add(node.getNodeID());}
                        }
                    }
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }


                for (int coordinate = 0; coordinate < xCoord.size() - 1; coordinate++) {
                    Circle circle = drawCircle(xCoord.get(coordinate), yCoord.get(coordinate));
                    Text text = new Text("");
                    Text textLocation = new Text("");
                    text.setFill(Color.WHITE);
                    text.setStroke(Color.MEDIUMBLUE);
                    text.setFont(Font.font("Ariel", FontWeight.BOLD, 17));
                    text.toFront();
                    LocationName location = hash.get(nodeIDs.get(coordinate)).getLocation();
                    if (!location.getNodeType().equals("HALL")) {
                        text.setText(hash.get(nodeIDs.get(coordinate)).getLocation().getShortName());
                    }
                    text.setX(xCoord.get(coordinate) - (text.getLayoutBounds().getWidth() / 2));
                    text.setY(yCoord.get(coordinate) + (circle.getRadius() * 2));

                    circle.setOnMouseClicked(event1 -> {
                        clearAllNodes();
                        Circle newCircle = drawCircle(circle.getCenterX(), circle.getCenterY());
                        newCircle.setFill(Color.PINK);
                        newCircle.setStroke(Color.RED);
                        newCircle.setVisible(true);

                        switch (this.comboBox.getText()) {
                            case "Ground Floor":
                                this.groundGroup.getChildren().add(newCircle);
                                break;
                            case "L1 - Lower Level 1":
                                this.L1Group.getChildren().add(newCircle);
                                break;
                            case "L2 - Lower Level 2":
                                this.L2Group.getChildren().add(newCircle);
                                break;
                            case "1 - 1st Floor":
                                this.floor1Group.getChildren().add(newCircle);
                                break;
                            case "2 - 2nd Floor":
                                this.floor2Group.getChildren().add(newCircle);
                                break;
                            case "3 - 3rd Floor":
                                this.floor3Group.getChildren().add(newCircle);
                                break;
                        }

                        try {
                            for (Move move : DAOFacade.getAllMoves()) {
                                if (move.getNode().getXcoord() == circle.getCenterX() && move.getNode().getYcoord() == circle.getCenterY()) {
                                    this.startLocation.setText(move.getLocation().getLongName());
                                    textLocation.setFill(Color.WHITE);
                                    textLocation.setStroke(Color.MEDIUMBLUE);
                                    textLocation.setFont(Font.font("Ariel", FontWeight.BOLD, 17));
                                    textLocation.toFront();
                                    textLocation.setText(move.getLocation().getShortName());
                                    textLocation.setX(move.getNode().getXcoord() - (textLocation.getLayoutBounds().getWidth() / 2));
                                    textLocation.setY(move.getNode().getYcoord() + (10 * 2));
                                    switch (this.comboBox.getText()) {
                                        case "Ground Floor":
                                            this.groundGroup.getChildren().add(textLocation);
                                            break;
                                        case "L1 - Lower Level 1":
                                            this.L1Group.getChildren().add(textLocation);
                                            break;
                                        case "L2 - Lower Level 2":
                                            this.L2Group.getChildren().add(textLocation);
                                            break;
                                        case "1 - 1st Floor":
                                            this.floor1Group.getChildren().add(textLocation);
                                            break;
                                        case "2 - 2nd Floor":
                                            this.floor2Group.getChildren().add(textLocation);
                                            break;
                                        case "3 - 3rd Floor":
                                            this.floor3Group.getChildren().add(textLocation);
                                            break;
                                    }
                                }
                            }
                        } catch (SQLException e) {
                            throw new RuntimeException(e);
                        }



                    });
                    switch (this.comboBox.getText()) {
                        case "Ground Floor":
                            this.groundGroup.getChildren().addAll(circle, text);
                            break;
                        case "L1 - Lower Level 1":
                            this.L1Group.getChildren().addAll(circle, text);
                            break;
                        case "L2 - Lower Level 2":
                            this.L2Group.getChildren().addAll(circle, text);
                            break;
                        case "1 - 1st Floor":
                            this.floor1Group.getChildren().addAll(circle, text);
                            break;
                        case "2 - 2nd Floor":
                            this.floor2Group.getChildren().addAll(circle, text);
                            break;
                        case "3 - 3rd Floor":
                            this.floor3Group.getChildren().addAll(circle, text);
                            break;
                    }
                }

                xCoord.clear();
                yCoord.clear();
                nodeIDs.clear();
            }

            if(infoDesks.isSelected()==true){
                try {
                    for (Node node : DAOFacade.getAllNodes()) {
                        if (node.getFloor().equals(this.comboBox.getText().substring(0, this.comboBox.getText().indexOf(" ")))) {
                            if(hash.get(node.getNodeID()).getLocation().getNodeType().equals("INFO")){
                                xCoord.add((double) node.getXcoord());
                            yCoord.add((double) node.getYcoord());
                            nodeIDs.add(node.getNodeID());}
                        }
                    }
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }


                for (int coordinate = 0; coordinate < xCoord.size() - 1; coordinate++) {
                    Circle circle = drawCircle(xCoord.get(coordinate), yCoord.get(coordinate));
                    Text text = new Text("");
                    Text textLocation = new Text("");
                    text.setFill(Color.WHITE);
                    text.setStroke(Color.MEDIUMBLUE);
                    text.setFont(Font.font("Ariel", FontWeight.BOLD, 17));
                    text.toFront();
                    LocationName location = hash.get(nodeIDs.get(coordinate)).getLocation();
                    if (!location.getNodeType().equals("HALL")) {
                        text.setText(hash.get(nodeIDs.get(coordinate)).getLocation().getShortName());
                    }
                    text.setX(xCoord.get(coordinate) - (text.getLayoutBounds().getWidth() / 2));
                    text.setY(yCoord.get(coordinate) + (circle.getRadius() * 2));

                    circle.setOnMouseClicked(event1 -> {
                        clearAllNodes();
                        Circle newCircle = drawCircle(circle.getCenterX(), circle.getCenterY());
                        newCircle.setFill(Color.PINK);
                        newCircle.setStroke(Color.RED);
                        newCircle.setVisible(true);

                        switch (this.comboBox.getText()) {
                            case "Ground Floor":
                                this.groundGroup.getChildren().add(newCircle);
                                break;
                            case "L1 - Lower Level 1":
                                this.L1Group.getChildren().add(newCircle);
                                break;
                            case "L2 - Lower Level 2":
                                this.L2Group.getChildren().add(newCircle);
                                break;
                            case "1 - 1st Floor":
                                this.floor1Group.getChildren().add(newCircle);
                                break;
                            case "2 - 2nd Floor":
                                this.floor2Group.getChildren().add(newCircle);
                                break;
                            case "3 - 3rd Floor":
                                this.floor3Group.getChildren().add(newCircle);
                                break;
                        }

                        try {
                            for (Move move : DAOFacade.getAllMoves()) {
                                if (move.getNode().getXcoord() == circle.getCenterX() && move.getNode().getYcoord() == circle.getCenterY()) {
                                    this.startLocation.setText(move.getLocation().getLongName());
                                    textLocation.setFill(Color.WHITE);
                                    textLocation.setStroke(Color.MEDIUMBLUE);
                                    textLocation.setFont(Font.font("Ariel", FontWeight.BOLD, 17));
                                    textLocation.toFront();
                                    textLocation.setText(move.getLocation().getShortName());
                                    textLocation.setX(move.getNode().getXcoord() - (textLocation.getLayoutBounds().getWidth() / 2));
                                    textLocation.setY(move.getNode().getYcoord() + (10 * 2));
                                    switch (this.comboBox.getText()) {
                                        case "Ground Floor":
                                            this.groundGroup.getChildren().add(textLocation);
                                            break;
                                        case "L1 - Lower Level 1":
                                            this.L1Group.getChildren().add(textLocation);
                                            break;
                                        case "L2 - Lower Level 2":
                                            this.L2Group.getChildren().add(textLocation);
                                            break;
                                        case "1 - 1st Floor":
                                            this.floor1Group.getChildren().add(textLocation);
                                            break;
                                        case "2 - 2nd Floor":
                                            this.floor2Group.getChildren().add(textLocation);
                                            break;
                                        case "3 - 3rd Floor":
                                            this.floor3Group.getChildren().add(textLocation);
                                            break;
                                    }
                                }
                            }
                        } catch (SQLException e) {
                            throw new RuntimeException(e);
                        }



                    });
                    switch (this.comboBox.getText()) {
                        case "Ground Floor":
                            this.groundGroup.getChildren().addAll(circle, text);
                            break;
                        case "L1 - Lower Level 1":
                            this.L1Group.getChildren().addAll(circle, text);
                            break;
                        case "L2 - Lower Level 2":
                            this.L2Group.getChildren().addAll(circle, text);
                            break;
                        case "1 - 1st Floor":
                            this.floor1Group.getChildren().addAll(circle, text);
                            break;
                        case "2 - 2nd Floor":
                            this.floor2Group.getChildren().addAll(circle, text);
                            break;
                        case "3 - 3rd Floor":
                            this.floor3Group.getChildren().addAll(circle, text);
                            break;
                    }
                }

                xCoord.clear();
                yCoord.clear();
                nodeIDs.clear();
            }

            if(conferenceRooms.isSelected()==true){
                try {
                    for (Node node : DAOFacade.getAllNodes()) {
                        if (node.getFloor().equals(this.comboBox.getText().substring(0, this.comboBox.getText().indexOf(" ")))) {
                            if(hash.get(node.getNodeID()).getLocation().getNodeType().equals("CONF")){
                                xCoord.add((double) node.getXcoord());
                            yCoord.add((double) node.getYcoord());
                            nodeIDs.add(node.getNodeID());}
                        }
                    }
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }


                for (int coordinate = 0; coordinate < xCoord.size() - 1; coordinate++) {
                    Circle circle = drawCircle(xCoord.get(coordinate), yCoord.get(coordinate));
                    Text text = new Text("");
                    Text textLocation = new Text("");
                    text.setFill(Color.WHITE);
                    text.setStroke(Color.MEDIUMBLUE);
                    text.setFont(Font.font("Ariel", FontWeight.BOLD, 17));
                    text.toFront();
                    LocationName location = hash.get(nodeIDs.get(coordinate)).getLocation();
                    if (!location.getNodeType().equals("HALL")) {
                        text.setText(hash.get(nodeIDs.get(coordinate)).getLocation().getShortName());
                    }
                    text.setX(xCoord.get(coordinate) - (text.getLayoutBounds().getWidth() / 2));
                    text.setY(yCoord.get(coordinate) + (circle.getRadius() * 2));

                    circle.setOnMouseClicked(event1 -> {
                        clearAllNodes();
                        Circle newCircle = drawCircle(circle.getCenterX(), circle.getCenterY());
                        newCircle.setFill(Color.PINK);
                        newCircle.setStroke(Color.RED);
                        newCircle.setVisible(true);

                        switch (this.comboBox.getText()) {
                            case "Ground Floor":
                                this.groundGroup.getChildren().add(newCircle);
                                break;
                            case "L1 - Lower Level 1":
                                this.L1Group.getChildren().add(newCircle);
                                break;
                            case "L2 - Lower Level 2":
                                this.L2Group.getChildren().add(newCircle);
                                break;
                            case "1 - 1st Floor":
                                this.floor1Group.getChildren().add(newCircle);
                                break;
                            case "2 - 2nd Floor":
                                this.floor2Group.getChildren().add(newCircle);
                                break;
                            case "3 - 3rd Floor":
                                this.floor3Group.getChildren().add(newCircle);
                                break;
                        }

                        try {
                            for (Move move : DAOFacade.getAllMoves()) {
                                if (move.getNode().getXcoord() == circle.getCenterX() && move.getNode().getYcoord() == circle.getCenterY()) {
                                    this.startLocation.setText(move.getLocation().getLongName());
                                    textLocation.setFill(Color.WHITE);
                                    textLocation.setStroke(Color.MEDIUMBLUE);
                                    textLocation.setFont(Font.font("Ariel", FontWeight.BOLD, 17));
                                    textLocation.toFront();
                                    textLocation.setText(move.getLocation().getShortName());
                                    textLocation.setX(move.getNode().getXcoord() - (textLocation.getLayoutBounds().getWidth() / 2));
                                    textLocation.setY(move.getNode().getYcoord() + (10 * 2));
                                    switch (this.comboBox.getText()) {
                                        case "Ground Floor":
                                            this.groundGroup.getChildren().add(textLocation);
                                            break;
                                        case "L1 - Lower Level 1":
                                            this.L1Group.getChildren().add(textLocation);
                                            break;
                                        case "L2 - Lower Level 2":
                                            this.L2Group.getChildren().add(textLocation);
                                            break;
                                        case "1 - 1st Floor":
                                            this.floor1Group.getChildren().add(textLocation);
                                            break;
                                        case "2 - 2nd Floor":
                                            this.floor2Group.getChildren().add(textLocation);
                                            break;
                                        case "3 - 3rd Floor":
                                            this.floor3Group.getChildren().add(textLocation);
                                            break;
                                    }
                                }
                            }
                        } catch (SQLException e) {
                            throw new RuntimeException(e);
                        }



                    });
                    switch (this.comboBox.getText()) {
                        case "Ground Floor":
                            this.groundGroup.getChildren().addAll(circle, text);
                            break;
                        case "L1 - Lower Level 1":
                            this.L1Group.getChildren().addAll(circle, text);
                            break;
                        case "L2 - Lower Level 2":
                            this.L2Group.getChildren().addAll(circle, text);
                            break;
                        case "1 - 1st Floor":
                            this.floor1Group.getChildren().addAll(circle, text);
                            break;
                        case "2 - 2nd Floor":
                            this.floor2Group.getChildren().addAll(circle, text);
                            break;
                        case "3 - 3rd Floor":
                            this.floor3Group.getChildren().addAll(circle, text);
                            break;
                    }
                }

                xCoord.clear();
                yCoord.clear();
                nodeIDs.clear();
            }

            if(retail.isSelected()==true){
                try {
                    for (Node node : DAOFacade.getAllNodes()) {
                        if (node.getFloor().equals(this.comboBox.getText().substring(0, this.comboBox.getText().indexOf(" ")))) {
                            if(hash.get(node.getNodeID()).getLocation().getNodeType().equals("RETL")){
                                xCoord.add((double) node.getXcoord());
                            yCoord.add((double) node.getYcoord());
                            nodeIDs.add(node.getNodeID());}
                        }
                    }
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }

                for (int coordinate = 0; coordinate < xCoord.size() - 1; coordinate++) {
                    Circle circle = drawCircle(xCoord.get(coordinate), yCoord.get(coordinate));
                    Text text = new Text("");
                    Text textLocation = new Text("");
                    text.setFill(Color.WHITE);
                    text.setStroke(Color.MEDIUMBLUE);
                    text.setFont(Font.font("Ariel", FontWeight.BOLD, 17));
                    text.toFront();
                    LocationName location = hash.get(nodeIDs.get(coordinate)).getLocation();
                    if (!location.getNodeType().equals("HALL")) {
                        text.setText(hash.get(nodeIDs.get(coordinate)).getLocation().getShortName());
                    }
                    text.setX(xCoord.get(coordinate) - (text.getLayoutBounds().getWidth() / 2));
                    text.setY(yCoord.get(coordinate) + (circle.getRadius() * 2));

                    circle.setOnMouseClicked(event1 -> {
                        clearAllNodes();
                        Circle newCircle = drawCircle(circle.getCenterX(), circle.getCenterY());
                        newCircle.setFill(Color.PINK);
                        newCircle.setStroke(Color.RED);
                        newCircle.setVisible(true);

                        switch (this.comboBox.getText()) {
                            case "Ground Floor":
                                this.groundGroup.getChildren().add(newCircle);
                                break;
                            case "L1 - Lower Level 1":
                                this.L1Group.getChildren().add(newCircle);
                                break;
                            case "L2 - Lower Level 2":
                                this.L2Group.getChildren().add(newCircle);
                                break;
                            case "1 - 1st Floor":
                                this.floor1Group.getChildren().add(newCircle);
                                break;
                            case "2 - 2nd Floor":
                                this.floor2Group.getChildren().add(newCircle);
                                break;
                            case "3 - 3rd Floor":
                                this.floor3Group.getChildren().add(newCircle);
                                break;
                        }

                        try {
                            for (Move move : DAOFacade.getAllMoves()) {
                                if (move.getNode().getXcoord() == circle.getCenterX() && move.getNode().getYcoord() == circle.getCenterY()) {
                                    this.startLocation.setText(move.getLocation().getLongName());
                                    textLocation.setFill(Color.WHITE);
                                    textLocation.setStroke(Color.MEDIUMBLUE);
                                    textLocation.setFont(Font.font("Ariel", FontWeight.BOLD, 17));
                                    textLocation.toFront();
                                    textLocation.setText(move.getLocation().getShortName());
                                    textLocation.setX(move.getNode().getXcoord() - (textLocation.getLayoutBounds().getWidth() / 2));
                                    textLocation.setY(move.getNode().getYcoord() + (10 * 2));
                                    switch (this.comboBox.getText()) {
                                        case "Ground Floor":
                                            this.groundGroup.getChildren().add(textLocation);
                                            break;
                                        case "L1 - Lower Level 1":
                                            this.L1Group.getChildren().add(textLocation);
                                            break;
                                        case "L2 - Lower Level 2":
                                            this.L2Group.getChildren().add(textLocation);
                                            break;
                                        case "1 - 1st Floor":
                                            this.floor1Group.getChildren().add(textLocation);
                                            break;
                                        case "2 - 2nd Floor":
                                            this.floor2Group.getChildren().add(textLocation);
                                            break;
                                        case "3 - 3rd Floor":
                                            this.floor3Group.getChildren().add(textLocation);
                                            break;
                                    }
                                }
                            }
                        } catch (SQLException e) {
                            throw new RuntimeException(e);
                        }



                    });
                    switch (this.comboBox.getText()) {
                        case "Ground Floor":
                            this.groundGroup.getChildren().addAll(circle, text);
                            break;
                        case "L1 - Lower Level 1":
                            this.L1Group.getChildren().addAll(circle, text);
                            break;
                        case "L2 - Lower Level 2":
                            this.L2Group.getChildren().addAll(circle, text);
                            break;
                        case "1 - 1st Floor":
                            this.floor1Group.getChildren().addAll(circle, text);
                            break;
                        case "2 - 2nd Floor":
                            this.floor2Group.getChildren().addAll(circle, text);
                            break;
                        case "3 - 3rd Floor":
                            this.floor3Group.getChildren().addAll(circle, text);
                            break;
                    }
                }

                xCoord.clear();
                yCoord.clear();
                nodeIDs.clear();
            }

            if(services.isSelected()==true){
                try {
                    for (Node node : DAOFacade.getAllNodes()) {
                        if (node.getFloor().equals(this.comboBox.getText().substring(0, this.comboBox.getText().indexOf(" ")))) {
                            if(hash.get(node.getNodeID()).getLocation().getNodeType().equals("SERV")){
                                xCoord.add((double) node.getXcoord());
                            yCoord.add((double) node.getYcoord());
                            nodeIDs.add(node.getNodeID());}
                        }
                    }
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }


                for (int coordinate = 0; coordinate < xCoord.size() - 1; coordinate++) {
                    Circle circle = drawCircle(xCoord.get(coordinate), yCoord.get(coordinate));
                    Text text = new Text("");
                    Text textLocation = new Text("");
                    text.setFill(Color.WHITE);
                    text.setStroke(Color.MEDIUMBLUE);
                    text.setFont(Font.font("Ariel", FontWeight.BOLD, 17));
                    text.toFront();
                    LocationName location = hash.get(nodeIDs.get(coordinate)).getLocation();
                    if (!location.getNodeType().equals("HALL")) {
                        text.setText(hash.get(nodeIDs.get(coordinate)).getLocation().getShortName());
                    }
                    text.setX(xCoord.get(coordinate) - (text.getLayoutBounds().getWidth() / 2));
                    text.setY(yCoord.get(coordinate) + (circle.getRadius() * 2));

                    circle.setOnMouseClicked(event1 -> {
                        clearAllNodes();
                        Circle newCircle = drawCircle(circle.getCenterX(), circle.getCenterY());
                        newCircle.setFill(Color.PINK);
                        newCircle.setStroke(Color.RED);
                        newCircle.setVisible(true);

                        switch (this.comboBox.getText()) {
                            case "Ground Floor":
                                this.groundGroup.getChildren().add(newCircle);
                                break;
                            case "L1 - Lower Level 1":
                                this.L1Group.getChildren().add(newCircle);
                                break;
                            case "L2 - Lower Level 2":
                                this.L2Group.getChildren().add(newCircle);
                                break;
                            case "1 - 1st Floor":
                                this.floor1Group.getChildren().add(newCircle);
                                break;
                            case "2 - 2nd Floor":
                                this.floor2Group.getChildren().add(newCircle);
                                break;
                            case "3 - 3rd Floor":
                                this.floor3Group.getChildren().add(newCircle);
                                break;
                        }

                        try {
                            for (Move move : DAOFacade.getAllMoves()) {
                                if (move.getNode().getXcoord() == circle.getCenterX() && move.getNode().getYcoord() == circle.getCenterY()) {
                                    this.startLocation.setText(move.getLocation().getLongName());
                                    textLocation.setFill(Color.WHITE);
                                    textLocation.setStroke(Color.MEDIUMBLUE);
                                    textLocation.setFont(Font.font("Ariel", FontWeight.BOLD, 17));
                                    textLocation.toFront();
                                    textLocation.setText(move.getLocation().getShortName());
                                    textLocation.setX(move.getNode().getXcoord() - (textLocation.getLayoutBounds().getWidth() / 2));
                                    textLocation.setY(move.getNode().getYcoord() + (10 * 2));
                                    switch (this.comboBox.getText()) {
                                        case "Ground Floor":
                                            this.groundGroup.getChildren().add(textLocation);
                                            break;
                                        case "L1 - Lower Level 1":
                                            this.L1Group.getChildren().add(textLocation);
                                            break;
                                        case "L2 - Lower Level 2":
                                            this.L2Group.getChildren().add(textLocation);
                                            break;
                                        case "1 - 1st Floor":
                                            this.floor1Group.getChildren().add(textLocation);
                                            break;
                                        case "2 - 2nd Floor":
                                            this.floor2Group.getChildren().add(textLocation);
                                            break;
                                        case "3 - 3rd Floor":
                                            this.floor3Group.getChildren().add(textLocation);
                                            break;
                                    }
                                }
                            }
                        } catch (SQLException e) {
                            throw new RuntimeException(e);
                        }



                    });
                    switch (this.comboBox.getText()) {
                        case "Ground Floor":
                            this.groundGroup.getChildren().addAll(circle, text);
                            break;
                        case "L1 - Lower Level 1":
                            this.L1Group.getChildren().addAll(circle, text);
                            break;
                        case "L2 - Lower Level 2":
                            this.L2Group.getChildren().addAll(circle, text);
                            break;
                        case "1 - 1st Floor":
                            this.floor1Group.getChildren().addAll(circle, text);
                            break;
                        case "2 - 2nd Floor":
                            this.floor2Group.getChildren().addAll(circle, text);
                            break;
                        case "3 - 3rd Floor":
                            this.floor3Group.getChildren().addAll(circle, text);
                            break;
                    }
                }

                xCoord.clear();
                yCoord.clear();
                nodeIDs.clear();
            }

            if(exits.isSelected()==true){
                try {
                    for (Node node : DAOFacade.getAllNodes()) {
                        if (node.getFloor().equals(this.comboBox.getText().substring(0, this.comboBox.getText().indexOf(" ")))) {
                            if(hash.get(node.getNodeID()).getLocation().getNodeType().equals("EXIT")){
                                xCoord.add((double) node.getXcoord());
                            yCoord.add((double) node.getYcoord());
                            nodeIDs.add(node.getNodeID());}
                        }
                    }
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }


                for (int coordinate = 0; coordinate < xCoord.size() - 1; coordinate++) {
                    Circle circle = drawCircle(xCoord.get(coordinate), yCoord.get(coordinate));
                    Text text = new Text("");
                    Text textLocation = new Text("");
                    text.setFill(Color.WHITE);
                    text.setStroke(Color.MEDIUMBLUE);
                    text.setFont(Font.font("Ariel", FontWeight.BOLD, 17));
                    text.toFront();
                    LocationName location = hash.get(nodeIDs.get(coordinate)).getLocation();
                    if (!location.getNodeType().equals("HALL")) {
                        text.setText(hash.get(nodeIDs.get(coordinate)).getLocation().getShortName());
                    }
                    text.setX(xCoord.get(coordinate) - (text.getLayoutBounds().getWidth() / 2));
                    text.setY(yCoord.get(coordinate) + (circle.getRadius() * 2));

                    circle.setOnMouseClicked(event1 -> {
                        clearAllNodes();
                        Circle newCircle = drawCircle(circle.getCenterX(), circle.getCenterY());
                        newCircle.setFill(Color.PINK);
                        newCircle.setStroke(Color.RED);
                        newCircle.setVisible(true);

                        switch (this.comboBox.getText()) {
                            case "Ground Floor":
                                this.groundGroup.getChildren().add(newCircle);
                                break;
                            case "L1 - Lower Level 1":
                                this.L1Group.getChildren().add(newCircle);
                                break;
                            case "L2 - Lower Level 2":
                                this.L2Group.getChildren().add(newCircle);
                                break;
                            case "1 - 1st Floor":
                                this.floor1Group.getChildren().add(newCircle);
                                break;
                            case "2 - 2nd Floor":
                                this.floor2Group.getChildren().add(newCircle);
                                break;
                            case "3 - 3rd Floor":
                                this.floor3Group.getChildren().add(newCircle);
                                break;
                        }

                        try {
                            for (Move move : DAOFacade.getAllMoves()) {
                                if (move.getNode().getXcoord() == circle.getCenterX() && move.getNode().getYcoord() == circle.getCenterY()) {
                                    this.startLocation.setText(move.getLocation().getLongName());
                                    textLocation.setFill(Color.WHITE);
                                    textLocation.setStroke(Color.MEDIUMBLUE);
                                    textLocation.setFont(Font.font("Ariel", FontWeight.BOLD, 17));
                                    textLocation.toFront();
                                    textLocation.setText(move.getLocation().getShortName());
                                    textLocation.setX(move.getNode().getXcoord() - (textLocation.getLayoutBounds().getWidth() / 2));
                                    textLocation.setY(move.getNode().getYcoord() + (10 * 2));
                                    switch (this.comboBox.getText()) {
                                        case "Ground Floor":
                                            this.groundGroup.getChildren().add(textLocation);
                                            break;
                                        case "L1 - Lower Level 1":
                                            this.L1Group.getChildren().add(textLocation);
                                            break;
                                        case "L2 - Lower Level 2":
                                            this.L2Group.getChildren().add(textLocation);
                                            break;
                                        case "1 - 1st Floor":
                                            this.floor1Group.getChildren().add(textLocation);
                                            break;
                                        case "2 - 2nd Floor":
                                            this.floor2Group.getChildren().add(textLocation);
                                            break;
                                        case "3 - 3rd Floor":
                                            this.floor3Group.getChildren().add(textLocation);
                                            break;
                                    }
                                }
                            }
                        } catch (SQLException e) {
                            throw new RuntimeException(e);
                        }



                    });
                    switch (this.comboBox.getText()) {
                        case "Ground Floor":
                            this.groundGroup.getChildren().addAll(circle, text);
                            break;
                        case "L1 - Lower Level 1":
                            this.L1Group.getChildren().addAll(circle, text);
                            break;
                        case "L2 - Lower Level 2":
                            this.L2Group.getChildren().addAll(circle, text);
                            break;
                        case "1 - 1st Floor":
                            this.floor1Group.getChildren().addAll(circle, text);
                            break;
                        case "2 - 2nd Floor":
                            this.floor2Group.getChildren().addAll(circle, text);
                            break;
                        case "3 - 3rd Floor":
                            this.floor3Group.getChildren().addAll(circle, text);
                            break;
                    }
                }

                xCoord.clear();
                yCoord.clear();
                nodeIDs.clear();
            }

            if(bathrooms.isSelected()==true){
                try {
                    for (Node node : DAOFacade.getAllNodes()) {
                        if (node.getFloor().equals(this.comboBox.getText().substring(0, this.comboBox.getText().indexOf(" ")))) {
                            if(hash.get(node.getNodeID()).getLocation().getNodeType().equals("BATH")){
                                xCoord.add((double) node.getXcoord());
                            yCoord.add((double) node.getYcoord());
                            nodeIDs.add(node.getNodeID());}
                        }
                    }
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }


                for (int coordinate = 0; coordinate < xCoord.size() - 1; coordinate++) {
                    Circle circle = drawCircle(xCoord.get(coordinate), yCoord.get(coordinate));
                    Text text = new Text("");
                    Text textLocation = new Text("");
                    text.setFill(Color.WHITE);
                    text.setStroke(Color.MEDIUMBLUE);
                    text.setFont(Font.font("Ariel", FontWeight.BOLD, 17));
                    text.toFront();
                    LocationName location = hash.get(nodeIDs.get(coordinate)).getLocation();
                    if (!location.getNodeType().equals("HALL")) {
                        text.setText(hash.get(nodeIDs.get(coordinate)).getLocation().getShortName());
                    }
                    text.setX(xCoord.get(coordinate) - (text.getLayoutBounds().getWidth() / 2));
                    text.setY(yCoord.get(coordinate) + (circle.getRadius() * 2));

                    circle.setOnMouseClicked(event1 -> {
                        clearAllNodes();
                        Circle newCircle = drawCircle(circle.getCenterX(), circle.getCenterY());
                        newCircle.setFill(Color.PINK);
                        newCircle.setStroke(Color.RED);
                        newCircle.setVisible(true);

                        switch (this.comboBox.getText()) {
                            case "Ground Floor":
                                this.groundGroup.getChildren().add(newCircle);
                                break;
                            case "L1 - Lower Level 1":
                                this.L1Group.getChildren().add(newCircle);
                                break;
                            case "L2 - Lower Level 2":
                                this.L2Group.getChildren().add(newCircle);
                                break;
                            case "1 - 1st Floor":
                                this.floor1Group.getChildren().add(newCircle);
                                break;
                            case "2 - 2nd Floor":
                                this.floor2Group.getChildren().add(newCircle);
                                break;
                            case "3 - 3rd Floor":
                                this.floor3Group.getChildren().add(newCircle);
                                break;
                        }

                        try {
                            for (Move move : DAOFacade.getAllMoves()) {
                                if (move.getNode().getXcoord() == circle.getCenterX() && move.getNode().getYcoord() == circle.getCenterY()) {
                                    this.startLocation.setText(move.getLocation().getLongName());
                                    textLocation.setFill(Color.WHITE);
                                    textLocation.setStroke(Color.MEDIUMBLUE);
                                    textLocation.setFont(Font.font("Ariel", FontWeight.BOLD, 17));
                                    textLocation.toFront();
                                    textLocation.setText(move.getLocation().getShortName());
                                    textLocation.setX(move.getNode().getXcoord() - (textLocation.getLayoutBounds().getWidth() / 2));
                                    textLocation.setY(move.getNode().getYcoord() + (10 * 2));
                                    switch (this.comboBox.getText()) {
                                        case "Ground Floor":
                                            this.groundGroup.getChildren().add(textLocation);
                                            break;
                                        case "L1 - Lower Level 1":
                                            this.L1Group.getChildren().add(textLocation);
                                            break;
                                        case "L2 - Lower Level 2":
                                            this.L2Group.getChildren().add(textLocation);
                                            break;
                                        case "1 - 1st Floor":
                                            this.floor1Group.getChildren().add(textLocation);
                                            break;
                                        case "2 - 2nd Floor":
                                            this.floor2Group.getChildren().add(textLocation);
                                            break;
                                        case "3 - 3rd Floor":
                                            this.floor3Group.getChildren().add(textLocation);
                                            break;
                                    }
                                }
                            }
                        } catch (SQLException e) {
                            throw new RuntimeException(e);
                        }



                    });
                    switch (this.comboBox.getText()) {
                        case "Ground Floor":
                            this.groundGroup.getChildren().addAll(circle, text);
                            break;
                        case "L1 - Lower Level 1":
                            this.L1Group.getChildren().addAll(circle, text);
                            break;
                        case "L2 - Lower Level 2":
                            this.L2Group.getChildren().addAll(circle, text);
                            break;
                        case "1 - 1st Floor":
                            this.floor1Group.getChildren().addAll(circle, text);
                            break;
                        case "2 - 2nd Floor":
                            this.floor2Group.getChildren().addAll(circle, text);
                            break;
                        case "3 - 3rd Floor":
                            this.floor3Group.getChildren().addAll(circle, text);
                            break;
                    }
                }

                xCoord.clear();
                yCoord.clear();
                nodeIDs.clear();

            }


        });


        this.comboBox.setItems(
                FXCollections.observableArrayList(
                        "Ground Floor",
                        "L1 - Lower Level 1",
                        "L2 - Lower Level 2",
                        "1 - 1st Floor",
                        "2 - 2nd Floor",
                        "3 - 3rd Floor"));

        this.comboBox.getSelectionModel().selectItem("1 - 1st Floor");

        for (LocationName name : DAOFacade.getAllLocationNames()) {
            startLocation.getItems().add(name.getLongName());
        }

        this.groundFloor.setVisible(true);
        this.floor1Group.setVisible(true);
        this.floor1Image.setVisible(true);

        this.preview.setOnAction(event -> {
            clearAllNodes();
            Circle circle = new Circle();

            try {
                circle = drawCircle(DAOFacade.getNode(this.startLocation.getText(), today).getXcoord(), DAOFacade.getNode(this.startLocation.getText(), today).getYcoord());
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

            String endFloor = null;
            try {
                endFloor = DAOFacade.getNode(this.startLocation.getText(), today).getFloor();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

            if (endFloor != null) {
                switch (endFloor) {
                    case "L1":
                        this.comboBox.getSelectionModel().selectItem("L1 - Lower Level 1");
                        L1Group.setVisible(true);
                        lowerLevel1Image.setVisible(true);
                        this.L1Group.getChildren().add(circle);
                        break;
                    case "L2":
                        this.comboBox.getSelectionModel().selectItem("L2 - Lower Level 2");
                        L2Group.setVisible(true);
                        lowerLevel2Image.setVisible(true);
                        this.L2Group.getChildren().add(circle);
                        break;
                    case "1":
                        this.comboBox.getSelectionModel().selectItem("1 - 1st Floor");
                        floor1Group.setVisible(true);
                        floor1Image.setVisible(true);
                        this.floor1Group.getChildren().add(circle);
                        break;
                    case "2":
                        this.comboBox.getSelectionModel().selectItem("2 - 2nd Floor");
                        floor2Group.setVisible(true);
                        floor2Image.setVisible(true);
                        this.floor2Group.getChildren().add(circle);
                        break;
                    case "3":
                        this.comboBox.getSelectionModel().selectItem("3 - 3rd Floor");
                        floor3Group.setVisible(true);
                        floor3Image.setVisible(true);
                        this.floor3Group.getChildren().add(circle);
                        break;
                }
            }
            Point2D centerpoint = new Point2D(circle.getCenterX(), circle.getCenterY());
            groundFloor.centreOn(centerpoint);

        });

        this.comboBox
                .getSelectionModel()
                .selectedItemProperty()
                .addListener(
                        new ChangeListener<String>() {
                            @Override
                            public void changed(
                                    ObservableValue<? extends String> selected, String oldFloor, String newFloor) {
                                if (oldFloor != null) {
                                    switch (oldFloor) {
                                        case "Ground Floor":
                                            groundGroup.setVisible(false);
                                            groundFloorImage.setVisible(false);
                                            break;
                                        case "L1 - Lower Level 1":
                                            L1Group.setVisible(false);
                                            lowerLevel1Image.setVisible(false);
                                            break;
                                        case "L2 - Lower Level 2":
                                            L2Group.setVisible(false);
                                            lowerLevel2Image.setVisible(false);
                                            break;
                                        case "1 - 1st Floor":
                                            floor1Group.setVisible(false);
                                            floor1Image.setVisible(false);
                                            break;
                                        case "2 - 2nd Floor":
                                            floor2Group.setVisible(false);
                                            floor2Image.setVisible(false);
                                            break;
                                        case "3 - 3rd Floor":
                                            floor3Group.setVisible(false);
                                            floor3Image.setVisible(false);
                                            break;
                                    }
                                }
                                if (newFloor != null) {

                                    switch (newFloor) {
                                        case "Ground Floor":
                                            groundGroup.setVisible(true);
                                            groundFloorImage.setVisible(true);
                                            break;
                                        case "L1 - Lower Level 1":
                                            L1Group.setVisible(true);
                                            lowerLevel1Image.setVisible(true);
                                            break;
                                        case "L2 - Lower Level 2":
                                            L2Group.setVisible(true);
                                            lowerLevel2Image.setVisible(true);
                                            break;
                                        case "1 - 1st Floor":
                                            floor1Group.setVisible(true);
                                            floor1Image.setVisible(true);
                                            break;
                                        case "2 - 2nd Floor":
                                            floor2Group.setVisible(true);
                                            floor2Image.setVisible(true);
                                            break;
                                        case "3 - 3rd Floor":
                                            floor3Group.setVisible(true);
                                            floor3Image.setVisible(true);
                                            break;
                                    }
                                }
                            }
                        });
    }

    public Circle drawCircle(double x, double y) {
        Circle circle = new Circle();
        circle.setVisible(true);
        circle.setFill(Color.RED);
        circle.setStroke(Color.BLACK);
        circle.setStrokeWidth(3.0f);
        circle.setCenterX(x);
        circle.setCenterY(y);
        circle.setRadius(10.0);
        return circle;
    }

    public void pressFilter(ActionEvent actionEvent) {
    }

    public void pressBackToHome(ActionEvent actionEvent) {
    }

    public void getComboBoxInfo(ActionEvent actionEvent) {
    }

    public void printSearch(ActionEvent actionEvent) {
    }

    public void clearAllNodes() {
        floor1Group.getChildren().remove(1, floor1Group.getChildren().size());
        floor2Group.getChildren().remove(1, floor2Group.getChildren().size());
        floor3Group.getChildren().remove(1, floor3Group.getChildren().size());
        L1Group.getChildren().remove(1, L1Group.getChildren().size());
        L2Group.getChildren().remove(1, L2Group.getChildren().size());
    }
}
