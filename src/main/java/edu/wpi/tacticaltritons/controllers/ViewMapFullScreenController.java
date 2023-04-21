package edu.wpi.tacticaltritons.controllers;

import edu.wpi.tacticaltritons.App;
import edu.wpi.tacticaltritons.database.*;
import edu.wpi.tacticaltritons.navigation.Navigation;
import edu.wpi.tacticaltritons.navigation.Screen;
import io.github.palexdev.materialfx.controls.*;
import javafx.animation.Interpolator;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.effect.Light;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Polyline;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;
import net.kurobako.gesturefx.GesturePane;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.sql.Date;
import java.util.HashMap;
import java.util.List;

public class ViewMapFullScreenController {

    @FXML
    private GesturePane gesturePane;


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
    private MFXFilterComboBox<String> searchOnMap = new MFXFilterComboBox<>();

    @FXML
    private MFXFilterComboBox<String> floorSelect = new MFXFilterComboBox<>();

    @FXML
    private MFXButton filter;

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
    private MFXCheckbox selectAll;

    @FXML
    private Line filterLine;

    @FXML
    private StackPane filterPane;

    @FXML
    private MFXButton viewNodes;

    @FXML
    private MFXButton pathfinding;

    @FXML
    private MFXButton menuBar;

    @FXML
    private StackPane menuPane;

    @FXML
    private Text menuLocationName;

    @FXML
    private Text menuLocationDescription;

    @FXML
    private MFXButton editMap;

    @FXML
    private MFXButton applyFilter;


    Date today = new Date(2023, 4, 13);


    public ViewMapFullScreenController() throws SQLException {
    }

    public void clearAllNodes() {
        floor1Group.getChildren().remove(1, floor1Group.getChildren().size());
        floor2Group.getChildren().remove(1, floor2Group.getChildren().size());
        floor3Group.getChildren().remove(1, floor3Group.getChildren().size());
        L1Group.getChildren().remove(1, L1Group.getChildren().size());
        L2Group.getChildren().remove(1, L2Group.getChildren().size());
    }

    public Circle drawCircle(double x, double y, Color fill, Color stroke) {
        Circle circle = new Circle();
        circle.setVisible(true);
        circle.setFill(fill);
        circle.setStroke(stroke);
        circle.setStrokeWidth(3.0f);
        circle.setCenterX(x);
        circle.setCenterY(y);
        circle.setRadius(10.0);
        return circle;
    }

    public void showFilters(Boolean bool) {
        bathrooms.setVisible(bool);
        restrooms.setVisible(bool);
        exits.setVisible(bool);
        elevators.setVisible(bool);
        stairs.setVisible(bool);
        hallways.setVisible(bool);
        departments.setVisible(bool);
        labs.setVisible(bool);
        services.setVisible(bool);
        infoDesks.setVisible(bool);
        conferenceRooms.setVisible(bool);
        retail.setVisible(bool);
        selectAll.setVisible(bool);
        filterLine.setVisible(bool);
        filterPane.setVisible(bool);
    }

    public void findNodes(String nodeType, List<Double> xCoord, List<Double> yCoord, List<Integer> nodeIDs, HashMap<Integer, Move> hash) {
        try {
            for (Node node : DAOFacade.getAllNodes()) {
                if (node.getFloor().equals(this.floorSelect.getText().substring(0, this.floorSelect.getText().indexOf(" ")))) {
                    if (hash.get(node.getNodeID()) == null) {

                    } else {
                        if (hash.get(node.getNodeID()).getLocation().getNodeType().equals(nodeType)) {
                            System.out.println("Node type: " + hash.get(node.getNodeID()).getLocation().getNodeType());
                            xCoord.add((double) node.getXcoord());
                            yCoord.add((double) node.getYcoord());
                            nodeIDs.add(node.getNodeID());
                        }
                    }
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }



        for (int coordinate = 0; coordinate < xCoord.size() - 1; coordinate++) {
            Circle circle = drawCircle(xCoord.get(coordinate), yCoord.get(coordinate), Color.RED, Color.BLACK);
            Text text = new Text("");
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

            circle.toFront();

            clickCircle(circle, xCoord, yCoord, nodeIDs, hash);


            switch (this.floorSelect.getText()) {
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

        try {
            for (Edge edge : DAOFacade.getAllEdges()) {
                if(edge.getStartNode().getFloor().equals(this.floorSelect.getText().substring(0, this.floorSelect.getText().indexOf(" "))) && edge.getStartNode().getFloor().equals(edge.getEndNode().getFloor())) {
                    Line edgeLine = new Line();
                    edgeLine.setStartX(edge.getStartNode().getXcoord());
                    edgeLine.setStartY(edge.getStartNode().getYcoord());
                    edgeLine.setEndX(edge.getEndNode().getXcoord());
                    edgeLine.setEndY(edge.getEndNode().getYcoord());
                    edgeLine.setStroke(Color.GREEN);
                    edgeLine.setOpacity(0.7);
                    edgeLine.setStrokeWidth(4);

                    switch (this.floorSelect.getText()) {
                        case "L1 - Lower Level 1":
                            this.L1Group.getChildren().add(edgeLine);
                            break;
                        case "L2 - Lower Level 2":
                            this.L2Group.getChildren().add(edgeLine);
                            break;
                        case "1 - 1st Floor":
                            this.floor1Group.getChildren().add(edgeLine);
                            break;
                        case "2 - 2nd Floor":
                            this.floor2Group.getChildren().add(edgeLine);
                            break;
                        case "3 - 3rd Floor":
                            this.floor3Group.getChildren().add(edgeLine);
                            break;
                    }
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        xCoord.clear();
        yCoord.clear();
        nodeIDs.clear();
    }


    public void findAllNodes(List<Double> xCoord, List<Double> yCoord, List<Integer> nodeIDs, HashMap<Integer, Move> hash) {
        try {
            for (Node node : DAOFacade.getAllNodes()) {
                if (node.getFloor().equals(this.floorSelect.getText().substring(0, this.floorSelect.getText().indexOf(" ")))) {
                    if (hash.get(node.getNodeID()) == null) {

                    } else {
                        xCoord.add((double) node.getXcoord());
                        yCoord.add((double) node.getYcoord());
                        nodeIDs.add(node.getNodeID());
                    }
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }


        for (int coordinate = 0; coordinate < xCoord.size() - 1; coordinate++) {
            Circle circle = drawCircle(xCoord.get(coordinate), yCoord.get(coordinate), Color.GRAY, Color.DARKGRAY);
            Text text = new Text("");
            text.setFill(Color.WHITE);
            text.setStroke(Color.GREEN);
            text.setFont(Font.font("Ariel", FontWeight.BOLD, 17));
            text.toFront();
            LocationName location = hash.get(nodeIDs.get(coordinate)).getLocation();
            if (!location.getNodeType().equals("HALL")) {
                text.setText(hash.get(nodeIDs.get(coordinate)).getLocation().getShortName());
            }

            text.setX(xCoord.get(coordinate) - (text.getLayoutBounds().getWidth() / 2));
            text.setY(yCoord.get(coordinate) + (circle.getRadius() * 2));

            clickCircle(circle, xCoord, yCoord, nodeIDs, hash);

            switch (this.floorSelect.getText()) {
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

    public void clickCircle(Circle circle, List<Double> xCoord, List<Double> yCoord, List<Integer> nodeIDs, HashMap<Integer, Move> hash) {
        Text textLocation = new Text("");
        circle.setOnMouseClicked(event1 -> {

            clearAllNodes();
            findAllNodes(xCoord, yCoord, nodeIDs, hash);

            try {
                for (Move move : DAOFacade.getAllMoves()) {
                    if (move.getNode().getXcoord() == circle.getCenterX() && move.getNode().getYcoord() == circle.getCenterY()) {
                        this.searchOnMap.getSelectionModel().selectItem(move.getLocation().getLongName());

                        try {
                            setMenuBarAllText(hash);
                        } catch (SQLException e) {
                            throw new RuntimeException(e);
                        }

                        textLocation.setFill(Color.WHITE);
                        textLocation.setStroke(Color.MEDIUMBLUE);
                        textLocation.setFont(Font.font("Ariel", FontWeight.BOLD, 17));
                        textLocation.toFront();
                        textLocation.setText(move.getLocation().getShortName());
                        textLocation.setX(move.getNode().getXcoord() - (textLocation.getLayoutBounds().getWidth() / 2));
                        textLocation.setY(move.getNode().getYcoord() + (10 * 2));
                        switch (this.floorSelect.getText()) {

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

            Circle newCircle = drawCircle(circle.getCenterX(), circle.getCenterY(), Color.PINK, Color.RED);
            newCircle.setVisible(true);

            switch (this.floorSelect.getText()) {

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
            Point2D centerpoint = new Point2D(newCircle.getCenterX(), newCircle.getCenterY());
            gesturePane.zoomTo(1.5, centerpoint);
        });
    }

    public void selectFilters(Boolean bool) {
        restrooms.setSelected(bool);
        elevators.setSelected(bool);
        stairs.setSelected(bool);
        hallways.setSelected(bool);
        departments.setSelected(bool);
        labs.setSelected(bool);
        infoDesks.setSelected(bool);
        conferenceRooms.setSelected(bool);
        retail.setSelected(bool);
        services.setSelected(bool);
        exits.setSelected(bool);
        bathrooms.setSelected(bool);
    }

    public void selectNone() {

        selectNone(restrooms);
        selectNone(elevators);
        selectNone(stairs);
        selectNone(hallways);
        selectNone(departments);
        selectNone(labs);
        selectNone(infoDesks);
        selectNone(conferenceRooms);
        selectNone(retail);
        selectNone(services);
        selectNone(exits);
        selectNone(bathrooms);

    }

    public void selectNone(MFXCheckbox checkbox) {
        checkbox.setOnMouseClicked(event -> {
            if (!checkbox.isSelected()) {
                selectAll.setSelected(false);
            }
        });
    }

    public void componentShift(int translate) {
        menuBar.setTranslateX(translate);
        searchOnMap.setTranslateX(translate);
        pathfinding.setTranslateX(translate);
        floorSelect.setTranslateX(translate);
        viewNodes.setTranslateX(translate);
    }

    public void setMenuBarAllText(HashMap<Integer, Move> hash) throws SQLException {
        menuLocationName.setText(searchOnMap.getSelectedItem());
        menuLocationDescription.setText("Short Name: " + (hash.get(DAOFacade.getNode(this.searchOnMap.getSelectedItem(), today).getNodeID()).getLocation().getShortName()) + "\n" + "Node Type: " + (hash.get(DAOFacade.getNode(this.searchOnMap.getSelectedItem(), today).getNodeID()).getLocation().getNodeType()) + "\n" + "Node ID: " + DAOFacade.getNode(this.searchOnMap.getSelectedItem(), today).getNodeID() + "\n" + "Coordinates (x,y): (" + (DAOFacade.getNode(this.searchOnMap.getSelectedItem(), today).getXcoord()) + "," + (DAOFacade.getNode(this.searchOnMap.getSelectedItem(), today).getYcoord()) + ")" + "\n" + "Floor: " + (DAOFacade.getNode(this.searchOnMap.getSelectedItem(), today).getFloor()) + "\n" + "Building: " + (DAOFacade.getNode(this.searchOnMap.getSelectedItem(), today).getBuilding()));
    }

    public void initializeImages() {
        lowerLevel1Image.setImage(App.lowerlevel1);
        lowerLevel2Image.setImage(App.lowerlevel2);
        floor1Image.setImage(App.firstfloor);
        floor2Image.setImage(App.secondfloor);
        floor3Image.setImage(App.thirdfloor);
    }

    int filterCounter = 0;
    int menuBarCounter = 0;

    @FXML
    private void initialize() throws SQLException {

        List<Double> xCoord = new ArrayList<Double>();
        List<Double> yCoord = new ArrayList<Double>();
        List<Double> startEnd = new ArrayList<Double>();
        List<Integer> nodeIDs = new ArrayList<Integer>();
        List<Move> allMoves = DAOFacade.getAllMoves();

        HashMap<Integer, Move> hash = new HashMap<>();
        for (Move move : allMoves) {
            hash.put(move.getNode().getNodeID(), move);
        }

        initializeImages();


        floorSelect.toFront();
        floorSelect.setVisible(true);
        searchOnMap.toFront();
        searchOnMap.setVisible(true);

        gesturePane.toBack();

        showFilters(false);

        filter.setVisible(true);
        menuBar.setVisible(true);
        menuPane.setVisible(false);


        selectAll.setSelected(true);
        selectFilters(true);

        selectNone();

        menuLocationDescription.setWrappingWidth(170);
        menuLocationName.setWrappingWidth(170);

        this.pathfinding.setOnAction(event -> {
            Navigation.navigate(Screen.PATHFINDING);
        });

        this.editMap.setOnAction(event -> {
            Navigation.navigate(Screen.EDIT_MAP);
        });


        this.menuBar.setOnAction(event -> {
            if (!menuPane.isVisible()) {
                menuPane.setVisible(true);
                componentShift(210);
                if (searchOnMap.getText() == "") {
                    menuLocationName.setText("Brigham and Women's Hospital");
                    menuLocationDescription.setText("Brigham and Women's Hospital is the second largest teaching hospital of Harvard Medical School and the largest hospital in the Longwood Medical Area in Boston, Massachusetts");

                } else {
                    try {
                        setMenuBarAllText(hash);
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                }
            } else {
                menuPane.setVisible(false);
                componentShift(0);
            }
        });

        this.selectAll.setOnMouseClicked(event -> {
            if (selectAll.isSelected()) {
                selectFilters(true);
            } else {
                selectFilters(false);
            }
        });

        this.viewNodes.setOnAction(event -> {
            findNodes("REST", xCoord, yCoord, nodeIDs, hash);
            findNodes("ELEV", xCoord, yCoord, nodeIDs, hash);
            findNodes("STAI", xCoord, yCoord, nodeIDs, hash);
            findNodes("HALL", xCoord, yCoord, nodeIDs, hash);
            findNodes("DEPT", xCoord, yCoord, nodeIDs, hash);
            findNodes("LABS", xCoord, yCoord, nodeIDs, hash);
            findNodes("INFO", xCoord, yCoord, nodeIDs, hash);
            findNodes("CONF", xCoord, yCoord, nodeIDs, hash);
            findNodes("RETL", xCoord, yCoord, nodeIDs, hash);
            findNodes("SERV", xCoord, yCoord, nodeIDs, hash);
            findNodes("EXIT", xCoord, yCoord, nodeIDs, hash);
            findNodes("BATH", xCoord, yCoord, nodeIDs, hash);

        });


        this.applyFilter.setOnAction(event -> {
            gesturePane.zoomTo(0.5, new Point2D(2500, 1700));
            gesturePane.centreOn(new Point2D(2500, 1000));
            clearAllNodes();

            findAllNodes(xCoord, yCoord, nodeIDs, hash);


            if (restrooms.isSelected() == true) {
                findNodes("REST", xCoord, yCoord, nodeIDs, hash);
            }

            if (elevators.isSelected() == true) {
                findNodes("ELEV", xCoord, yCoord, nodeIDs, hash);
            }

            if (stairs.isSelected() == true) {
                findNodes("STAI", xCoord, yCoord, nodeIDs, hash);
            }

            if (hallways.isSelected() == true) {
                findNodes("HALL", xCoord, yCoord, nodeIDs, hash);
            }

            if (departments.isSelected() == true) {
                findNodes("DEPT", xCoord, yCoord, nodeIDs, hash);
            }

            if (labs.isSelected() == true) {
                findNodes("LABS", xCoord, yCoord, nodeIDs, hash);
            }

            if (infoDesks.isSelected() == true) {
                findNodes("INFO", xCoord, yCoord, nodeIDs, hash);
            }

            if (conferenceRooms.isSelected() == true) {
                findNodes("CONF", xCoord, yCoord, nodeIDs, hash);
            }

            if (retail.isSelected() == true) {
                findNodes("RETL", xCoord, yCoord, nodeIDs, hash);
            }

            if (services.isSelected() == true) {
                findNodes("SERV", xCoord, yCoord, nodeIDs, hash);
            }

            if (exits.isSelected() == true) {
                findNodes("EXIT", xCoord, yCoord, nodeIDs, hash);
            }

            if (bathrooms.isSelected() == true) {
                findNodes("BATH", xCoord, yCoord, nodeIDs, hash);
            }
        });

        this.filter.setOnAction(event -> {
            if (filterCounter % 2 == 0) {
                showFilters(true);
            } else {
                showFilters(false);
            }
            filterCounter++;
        });


        this.searchOnMap.setOnAction(event -> {

            try {
                setMenuBarAllText(hash);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

            clearAllNodes();
            Circle circle = new Circle();

            try {
                circle = drawCircle(DAOFacade.getNode(this.searchOnMap.getSelectedItem(), today).getXcoord(), DAOFacade.getNode(this.searchOnMap.getSelectedItem(), today).getYcoord(), Color.RED, Color.BLACK);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

            String endFloor = null;
            try {
                endFloor = DAOFacade.getNode(this.searchOnMap.getSelectedItem(), today).getFloor();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

            if (endFloor != null) {
                switch (endFloor) {
                    case "L1":
                        this.floorSelect.getSelectionModel().selectItem("L1 - Lower Level 1");
                        L1Group.setVisible(true);
                        lowerLevel1Image.setVisible(true);
                        this.L1Group.getChildren().add(circle);
                        break;
                    case "L2":
                        this.floorSelect.getSelectionModel().selectItem("L2 - Lower Level 2");
                        L2Group.setVisible(true);
                        lowerLevel2Image.setVisible(true);
                        this.L2Group.getChildren().add(circle);
                        break;
                    case "1":
                        this.floorSelect.getSelectionModel().selectItem("1 - 1st Floor");
                        floor1Group.setVisible(true);
                        floor1Image.setVisible(true);
                        this.floor1Group.getChildren().add(circle);
                        break;
                    case "2":
                        this.floorSelect.getSelectionModel().selectItem("2 - 2nd Floor");
                        floor2Group.setVisible(true);
                        floor2Image.setVisible(true);
                        this.floor2Group.getChildren().add(circle);
                        break;
                    case "3":
                        this.floorSelect.getSelectionModel().selectItem("3 - 3rd Floor");
                        floor3Group.setVisible(true);
                        floor3Image.setVisible(true);
                        this.floor3Group.getChildren().add(circle);
                        break;
                }
            }
            Point2D centrePoint = new Point2D(circle.getCenterX(), circle.getCenterY());
            gesturePane.centreOn(centrePoint);
        });


        this.floorSelect.setItems(
                FXCollections.observableArrayList(
                        "L1 - Lower Level 1",
                        "L2 - Lower Level 2",
                        "1 - 1st Floor",
                        "2 - 2nd Floor",
                        "3 - 3rd Floor"));

        this.floorSelect.getSelectionModel().selectItem("1 - 1st Floor");
        this.gesturePane.setVisible(true);
        this.floor1Group.setVisible(true);
        this.floor1Image.setVisible(true);

        for (LocationName name : DAOFacade.getAllLocationNames()) {
            searchOnMap.getItems().add(name.getLongName());
        }

        this.floorSelect
                .getSelectionModel()
                .selectedItemProperty()
                .addListener(
                        new ChangeListener<String>() {
                            @Override
                            public void changed(
                                    ObservableValue<? extends String> selected, String oldFloor, String newFloor) {
                                if (oldFloor != null) {
                                    switch (oldFloor) {
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


        gesturePane.setScrollBarPolicy(GesturePane.ScrollBarPolicy.NEVER);
        gesturePane.reset();


    }
}
