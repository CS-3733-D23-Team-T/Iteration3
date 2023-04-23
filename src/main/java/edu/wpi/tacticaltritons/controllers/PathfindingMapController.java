package edu.wpi.tacticaltritons.controllers;

import edu.wpi.tacticaltritons.App;
import edu.wpi.tacticaltritons.database.DAOFacade;
import edu.wpi.tacticaltritons.database.LocationName;
import edu.wpi.tacticaltritons.database.Node;
import edu.wpi.tacticaltritons.navigation.Navigation;
import edu.wpi.tacticaltritons.navigation.Screen;
import edu.wpi.tacticaltritons.pathfinding.AStarAlgorithm;
import edu.wpi.tacticaltritons.pathfinding.Directions;
import io.github.palexdev.materialfx.controls.*;
import io.github.palexdev.materialfx.controls.MFXFilterComboBox;

import java.sql.SQLException;
import java.util.*;
import java.util.List;
import java.sql.Date;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.control.TextArea;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import net.kurobako.gesturefx.GesturePane;

public class PathfindingMapController {

    @FXML
    private MFXFilterComboBox<String> startLocation = new MFXFilterComboBox<>();

    @FXML
    private MFXFilterComboBox<String> endLocation = new MFXFilterComboBox<>();

    @FXML
    private MFXButton pathfinder;

    @FXML
    private MFXComboBox<String> comboBox;

    @FXML
    private TextArea textDirections;

    @FXML
    private MFXButton filterMap;

    @FXML
    private MFXFilterComboBox<String> rooms = new MFXFilterComboBox<>();

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
    private MFXCheckbox bathrooms;
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
    private MFXButton editMap;

    @FXML
    private void initialize() throws SQLException {

        Date today = new Date(2023, 04, 05);


        lowerLevel1Image.setImage(App.lowerlevel1);
        lowerLevel2Image.setImage(App.lowerlevel2);
        groundFloorImage.setImage(App.groundfloor);
        floor1Image.setImage(App.firstfloor);
        floor2Image.setImage(App.secondfloor);
        floor3Image.setImage(App.thirdfloor);

        List<Double> xCoord = new ArrayList<Double>(0);
        List<Double> yCoord = new ArrayList<Double>(0);
        List<Double> startEnd = new ArrayList<Double>(0);

        for (LocationName name : DAOFacade.getAllLocationNames()) {
            rooms.getItems().add(name.getLongName());
        }

        for (LocationName name : DAOFacade.getAllLocationNames()) {
            startLocation.getItems().add(name.getLongName());
        }

        for (LocationName name : DAOFacade.getAllLocationNames()) {
            endLocation.getItems().add(name.getLongName());
        }

//    TextFields.bindAutoCompletion(searchOnMap, possibleWords);

        this.pathfinder.setOnAction(
                event -> {
                  xCoord.clear();
                  yCoord.clear();
                  startEnd.clear();
                    clearAllNodes();
                    int startNodeId;
                    int endNodeId;
                    Node endNode1 = null;
                    Node startNode1 = null;
                    try {
                        AStarAlgorithm mapAlgorithm = new AStarAlgorithm();

                        try {
                            startNodeId = DAOFacade.getNode(startLocation.getText(), today).getNodeID();
                            endNodeId = DAOFacade.getNode(endLocation.getText(), today).getNodeID();
                        } catch (SQLException e) {
                            throw new RuntimeException(e);
                        }

                        startNode1 = DAOFacade.getNode(startNodeId);
                        endNode1 = DAOFacade.getNode(endNodeId);

                        List<Node> shortestPathMap = mapAlgorithm.findShortestPath(startNode1, endNode1);

                        Directions directions = new Directions(shortestPathMap);

                        List<String> position = directions.position();
                        StringBuilder sb = new StringBuilder();
                        for (int i = 0; i < position.size(); i++) {
                            sb.append(position.get(i));
                            sb.append("\n");
                        }
                        String allPositions = sb.toString();
                        textDirections.setText(allPositions);


                        for (Node node : shortestPathMap) {
                            xCoord.add((double) node.getXcoord());
                            yCoord.add((double) node.getYcoord());
                        }

                    } catch (SQLException e) {
                        e.printStackTrace();
                    }

                    int a;
                    for (a = 0; a < xCoord.size(); a++) {
                        startEnd.add(xCoord.get(a));
                        startEnd.add(yCoord.get(a));
                    }

                    for (a = 0; a < startEnd.size(); a++) {
                        //TODO do something
                    }

                    String endFloor = endNode1.getFloor();

                    Circle start = drawCircle(startNode1.getXcoord(), startNode1.getYcoord(), Color.GREEN);
                    Circle end = drawCircle(endNode1.getXcoord(), endNode1.getYcoord(), Color.BLUE);


                    Polyline path = new Polyline();
                    path.setStroke(Color.RED);
                    path.setOpacity(0.8);
                    path.setStrokeWidth(6.0f);
                    path.getPoints().addAll(startEnd);

                    if (endFloor != null) {
                        switch (endFloor) {
                            case "L1":
                                this.comboBox.getSelectionModel().selectItem("L1 - Lower Level 1");
                                L1Group.setVisible(true);
                                lowerLevel1Image.setVisible(true);
                                this.L1Group.getChildren().add(path);
                                this.L1Group.getChildren().add(start);
                                this.L1Group.getChildren().add(end);
                                break;
                            case "L2":
                                this.comboBox.getSelectionModel().selectItem("L2 - Lower Level 2");
                                L2Group.setVisible(true);
                                lowerLevel2Image.setVisible(true);
                                this.L2Group.getChildren().add(path);
                                this.L2Group.getChildren().add(start);
                                this.L2Group.getChildren().add(end);
                                break;
                            case "1":
                                this.comboBox.getSelectionModel().selectItem("1 - 1st Floor");
                                floor1Group.setVisible(true);
                                floor1Image.setVisible(true);
                                this.floor1Group.getChildren().add(path);
                                this.floor1Group.getChildren().add(start);
                                this.floor1Group.getChildren().add(end);
                                break;
                            case "2":
                                this.comboBox.getSelectionModel().selectItem("2 - 2nd Floor");
                                floor2Group.setVisible(true);
                                floor2Image.setVisible(true);
                                this.floor2Group.getChildren().add(path);
                                this.floor2Group.getChildren().add(start);
                                this.floor2Group.getChildren().add(end);
                                break;
                            case "3":
                                this.comboBox.getSelectionModel().selectItem("3 - 3rd Floor");
                                floor3Group.setVisible(true);
                                floor3Image.setVisible(true);
                                this.floor3Group.getChildren().add(path);
                                this.floor3Group.getChildren().add(start);
                                this.floor3Group.getChildren().add(end);
                                break;
                        }
                    }
                    Point2D centerpoint = new Point2D(start.getCenterX(), start.getCenterY());
                    groundFloor.centreOn(centerpoint);
                });

        this.editMap.setOnAction(
                event -> {
                    Navigation.navigate(Screen.EDIT_MAP);
                });

        this.filterMap.setOnAction(event -> {
            clearAllNodes();
            Circle circle = new Circle();

            try {
                circle = drawCircle(DAOFacade.getNode(this.startLocation.getText(), today).getXcoord(), DAOFacade.getNode(this.startLocation.getText(), today).getYcoord(),Color.RED);
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

        this.comboBox.setItems(
                FXCollections.observableArrayList(
                        "Ground Floor",
                        "L1 - Lower Level 1",
                        "L2 - Lower Level 2",
                        "1 - 1st Floor",
                        "2 - 2nd Floor",
                        "3 - 3rd Floor"));

        this.comboBox.getSelectionModel().selectFirst();

        this.groundFloor.setVisible(true);
        this.groundFloorImage.setVisible(true);


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

    public Circle drawCircle(double x, double y, Color color) {
        Circle circle = new Circle();
        circle.setVisible(true);
        circle.setFill(color);
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
