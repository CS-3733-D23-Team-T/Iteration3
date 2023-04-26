package edu.wpi.tacticaltritons.controllers;

import edu.wpi.tacticaltritons.App;
import edu.wpi.tacticaltritons.database.*;
import edu.wpi.tacticaltritons.navigation.Navigation;
import edu.wpi.tacticaltritons.navigation.Screen;
import edu.wpi.tacticaltritons.pathfinding.AStarAlgorithm;
import edu.wpi.tacticaltritons.pathfinding.AlgorithmSingleton;
import edu.wpi.tacticaltritons.pathfinding.CongestionController;
import io.github.palexdev.materialfx.controls.*;
import javafx.beans.binding.Bindings;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
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
import java.time.ZoneId;
import java.util.ArrayList;
import java.sql.Date;
import java.util.HashMap;
import java.util.List;

import edu.wpi.tacticaltritons.styling.ThemeColors;
import org.checkerframework.checker.units.qual.C;
import org.controlsfx.control.PopOver;

public class MapSuperController {
    @FXML
    protected ImageView importExport;

    @FXML
    protected GesturePane gesturePane;

    @FXML
    protected Group L1Group;
    @FXML
    protected Group L2Group;
    @FXML
    protected Group floor1Group;
    @FXML
    protected Group floor2Group;
    @FXML
    protected Group floor3Group;

    @FXML
    protected ImageView lowerLevel1Image;

    @FXML
    protected ImageView lowerLevel2Image;

    @FXML
    protected ImageView floor1Image;

    @FXML
    protected ImageView floor2Image;

    @FXML
    protected ImageView floor3Image;

    @FXML
    protected MFXFilterComboBox<String> searchOnMap = new MFXFilterComboBox<>();


    @FXML
    protected ImageView pathfinding;

    @FXML
    protected ImageView menuBar;

    @FXML
    protected StackPane menuPane;

    @FXML
    protected MFXButton editMap;

    @FXML
    protected MFXButton lowerLevel1;

    @FXML
    protected MFXButton lowerLevel2;

    @FXML
    protected MFXButton floor1;

    @FXML
    protected MFXButton floor2;

    @FXML
    protected MFXButton floor3;

    @FXML
    protected MFXButton viewNodes = new MFXButton();


    @FXML
    protected MFXFilterComboBox<String> startLocation = new MFXFilterComboBox<>();

    @FXML
    protected MFXFilterComboBox<String> endLocation = new MFXFilterComboBox<>();
    @FXML
    protected MFXDatePicker date;

    Date today = Date.valueOf(java.time.LocalDate.now());

    public List<Node> allNodes = DAOFacade.getAllNodes();
    public List<Move> allMoves = DAOFacade.getAllCurrentMoves(today);
    public List<Edge> allEdges = DAOFacade.getAllEdges();
    public List<LocationName> allLocationNames = DAOFacade.getAllLocationNames();
    public List<String> blank = new ArrayList<>();

    public List<Node> pathfindingList = new ArrayList<>();
    public List<Node> editMapList = new ArrayList<>();

    public List<String> allNodeTypes = new ArrayList<>(List.of("REST", "ELEV", "STAI", "HALL", "DEPT", "LABS", "INFO", "CONF", "RETL", "SERV", "EXIT", "BATH"));


    public List<Double> xCoord = new ArrayList<Double>(0);
    public List<Double> yCoord = new ArrayList<Double>(0);
    public List<Double> startEnd = new ArrayList<Double>(0);
    public List<Integer> nodeIDs = new ArrayList<Integer>();

    public HashMap<Integer, Circle> circleHashMap = new HashMap<>();
    public HashMap<Integer, List<Line>> lineHashMap = new HashMap<>();



    public MapSuperController() throws SQLException {
    }

    protected enum selectedFloor {
        FLOOR("1");
        public String floor;

        selectedFloor(String floor) {
            this.floor = floor;
        }
    }

    public HashMap<Integer, Node> getNodeHashMap() throws SQLException {
        HashMap<Integer, Node> hash = new HashMap<>();
        for (Node node : allNodes) {
            hash.put(node.getNodeID(), node);
        }
        return hash;
    }

    public HashMap<Integer, ArrayList<Node>> getNeighbourHashMap() throws SQLException {
        HashMap<Integer, ArrayList<Node>> hash = new HashMap<>();
        hash = DAOFacade.getAllNeighbors();
        return hash;
    }


    public HashMap<String, LocationName> getLocationNameHashMap() throws SQLException {
        HashMap<String, LocationName> hash = new HashMap<>();
        for (LocationName locationName : allLocationNames) {
            hash.put(locationName.getLongName(), locationName);
        }
        return hash;
    }

    public void initializeSearch(String page) throws SQLException {
        switch (page) {
            case "ViewMap":
                getLocationNameHashMap().forEach(((key, value) -> {
                    if (!value.getNodeType().equals("HALL"))
                        searchOnMap.getItems().add(key);
                }));
                break;
            case "Pathfinding":
                getLocationNameHashMap().forEach(((key, value) -> {
                    if (!value.getNodeType().equals("HALL")) {
                        startLocation.getItems().add(key);
                        endLocation.getItems().add(key);
                    }
                }));
                break;
        }

    }



    public void componentShift(int translate) {
        menuBar.setTranslateX(translate);
        searchOnMap.setTranslateX(translate);
        pathfinding.setTranslateX(translate);
        lowerLevel1.setTranslateX(translate);
        lowerLevel2.setTranslateX(translate);
        floor1.setTranslateX(translate);
        floor2.setTranslateX(translate);
        floor3.setTranslateX(translate);
        startLocation.setTranslateX(translate);
        endLocation.setTranslateX(translate);
        viewNodes.setTranslateX(translate);
    }

    public void initializeImages() {
        lowerLevel1Image.setImage(App.lowerlevel1);
        lowerLevel2Image.setImage(App.lowerlevel2);
        floor1Image.setImage(App.firstfloor);
        floor2Image.setImage(App.secondfloor);
        floor3Image.setImage(App.thirdfloor);
        pathfinding.setImage(App.pathfinding);
        menuBar.setImage(App.menuBar);
    }

    public void resetButtons() {
        this.lowerLevel1.setStyle("-fx-background-color: WHITE");
        this.lowerLevel2.setStyle("-fx-background-color: WHITE");
        this.floor1.setStyle("-fx-background-color: WHITE");
        this.floor2.setStyle("-fx-background-color: WHITE");
        this.floor3.setStyle("-fx-background-color: WHITE");
    }

    public void resetImages() {
        L1Group.setVisible(false);
        lowerLevel1Image.setVisible(false);

        L2Group.setVisible(false);
        lowerLevel2Image.setVisible(false);

        floor1Group.setVisible(false);
        floor1Image.setVisible(false);

        floor2Group.setVisible(false);
        floor2Image.setVisible(false);

        floor3Group.setVisible(false);
        floor3Image.setVisible(false);
    }

    public void setClickedButton(String thisFloor) {
        switch (thisFloor) {
            case "L1":
                resetButtons();
                this.lowerLevel1.setStyle("-fx-background-color: BLUE");
                resetImages();
                L1Group.setVisible(true);
                lowerLevel1Image.setVisible(true);
                break;
            case "L2":
                resetButtons();
                this.lowerLevel2.setStyle("-fx-background-color: BLUE");
                resetImages();
                L2Group.setVisible(true);
                lowerLevel2Image.setVisible(true);
                break;
            case "1":
                resetButtons();
                this.floor1.setStyle("-fx-background-color: BLUE");
                resetImages();
                floor1Group.setVisible(true);
                floor1Image.setVisible(true);
                break;
            case "2":
                resetButtons();
                this.floor2.setStyle("-fx-background-color: BLUE");
                resetImages();
                floor2Group.setVisible(true);
                floor2Image.setVisible(true);
                break;
            case "3":
                resetButtons();
                this.floor3.setStyle("-fx-background-color: BLUE");
                resetImages();
                floor3Group.setVisible(true);
                floor3Image.setVisible(true);
                break;
        }
    }

    public void initalizeFloorButtons() {
        this.lowerLevel1.setOnAction(event -> {
            resetButtons();
            this.lowerLevel1.setStyle("-fx-background-color: BLUE");
            resetImages();
            L1Group.setVisible(true);
            lowerLevel1Image.setVisible(true);
            selectedFloor.FLOOR.floor = "L1";
        });
        this.lowerLevel2.setOnAction(event -> {
            resetButtons();
            this.lowerLevel2.setStyle("-fx-background-color: BLUE");
            resetImages();
            L2Group.setVisible(true);
            lowerLevel2Image.setVisible(true);
            selectedFloor.FLOOR.floor = "L2";
        });
        this.floor1.setOnAction(event -> {
            resetButtons();
            this.floor1.setStyle("-fx-background-color: BLUE");
            resetImages();
            floor1Group.setVisible(true);
            floor1Image.setVisible(true);
            selectedFloor.FLOOR.floor = "1";
        });
        this.floor2.setOnAction(event -> {
            resetButtons();
            this.floor2.setStyle("-fx-background-color: BLUE");
            resetImages();
            floor2Group.setVisible(true);
            floor2Image.setVisible(true);
            selectedFloor.FLOOR.floor = "2";
        });
        this.floor3.setOnAction(event -> {
            resetButtons();
            this.floor3.setStyle("-fx-background-color: BLUE");
            resetImages();
            floor3Group.setVisible(true);
            floor3Image.setVisible(true);
            selectedFloor.FLOOR.floor = "3";
        });
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

    public void clearAllNodes() {
        L1Group.getChildren().remove(1, L1Group.getChildren().size());
        L2Group.getChildren().remove(1, L2Group.getChildren().size());
        floor1Group.getChildren().remove(1, floor1Group.getChildren().size());
        floor2Group.getChildren().remove(1, floor2Group.getChildren().size());
        floor3Group.getChildren().remove(1, floor3Group.getChildren().size());
    }

    public void clearAllCircles() {
        List<javafx.scene.Node> nodesList = new ArrayList<>();
        for (javafx.scene.Node nodes : L1Group.getChildren()) {
            if (nodes instanceof Circle) {
                nodesList.add(nodes);
            }
        }
        L1Group.getChildren().removeAll(nodesList);
        nodesList.clear();

        for (javafx.scene.Node nodes : L2Group.getChildren()) {
            if (nodes instanceof Circle) {
                nodesList.add(nodes);
            }
        }
        L2Group.getChildren().removeAll(nodesList);
        nodesList.clear();

        for (javafx.scene.Node nodes : floor1Group.getChildren()) {
            if (nodes instanceof Circle) {
                nodesList.add(nodes);
            }
        }
        floor1Group.getChildren().removeAll(nodesList);
        nodesList.clear();

        for (javafx.scene.Node nodes : floor2Group.getChildren()) {
            if (nodes instanceof Circle) {
                nodesList.add(nodes);
            }
        }
        floor2Group.getChildren().removeAll(nodesList);
        nodesList.clear();

        for (javafx.scene.Node nodes : floor3Group.getChildren()) {
            if (nodes instanceof Circle) {
                nodesList.add(nodes);
            }
        }
        floor3Group.getChildren().removeAll(nodesList);
        nodesList.clear();
    }

    public void clearAllTexts() {
        List<javafx.scene.Node> nodesList = new ArrayList<>();
        for (javafx.scene.Node nodes : L1Group.getChildren()) {
            if (nodes instanceof Text) {
                nodesList.add(nodes);
            }
        }
        L1Group.getChildren().removeAll(nodesList);
        nodesList.clear();

        for (javafx.scene.Node nodes : L2Group.getChildren()) {
            if (nodes instanceof Text) {
                nodesList.add(nodes);
            }
        }
        L2Group.getChildren().removeAll(nodesList);
        nodesList.clear();

        for (javafx.scene.Node nodes : floor1Group.getChildren()) {
            if (nodes instanceof Text) {
                nodesList.add(nodes);
            }
        }
        floor1Group.getChildren().removeAll(nodesList);
        nodesList.clear();

        for (javafx.scene.Node nodes : floor2Group.getChildren()) {
            if (nodes instanceof Text) {
                nodesList.add(nodes);
            }
        }
        floor2Group.getChildren().removeAll(nodesList);
        nodesList.clear();

        for (javafx.scene.Node nodes : floor3Group.getChildren()) {
            if (nodes instanceof Text) {
                nodesList.add(nodes);
            }
        }
        floor3Group.getChildren().removeAll(nodesList);
        nodesList.clear();
    }

    public void clearAllLines() {
        List<javafx.scene.Node> nodesList = new ArrayList<>();
        for (javafx.scene.Node nodes : L1Group.getChildren()) {
            if (nodes instanceof Line) {
                nodesList.add(nodes);
            }
        }
        L1Group.getChildren().removeAll(nodesList);
        nodesList.clear();

        for (javafx.scene.Node nodes : L2Group.getChildren()) {
            if (nodes instanceof Line) {
                nodesList.add(nodes);
            }
        }
        L2Group.getChildren().removeAll(nodesList);
        nodesList.clear();

        for (javafx.scene.Node nodes : floor1Group.getChildren()) {
            if (nodes instanceof Line) {
                nodesList.add(nodes);
            }
        }
        floor1Group.getChildren().removeAll(nodesList);
        nodesList.clear();

        for (javafx.scene.Node nodes : floor2Group.getChildren()) {
            if (nodes instanceof Line) {
                nodesList.add(nodes);
            }
        }
        floor2Group.getChildren().removeAll(nodesList);
        nodesList.clear();

        for (javafx.scene.Node nodes : floor3Group.getChildren()) {
            if (nodes instanceof Line) {
                nodesList.add(nodes);
            }
        }
        floor3Group.getChildren().removeAll(nodesList);
        nodesList.clear();
    }

    public HashMap<Integer, Move> getMoveHashMap() throws SQLException {
        HashMap<Integer, Move> hash = new HashMap<>();
        for (Move move : allMoves) {
            hash.put(move.getNode().getNodeID(), move);
        }
        return hash;
    }

    public HashMap<Integer, Edge> getEdgeHashMap() throws SQLException {
        HashMap<Integer, Edge> hash = new HashMap<>();
        for (Edge edge : allEdges) {
            hash.put(edge.getStartNode().getNodeID(), edge);
        }
        return hash;
    }


    public void initializeGesturePane() {
        javafx.application.Platform.runLater(() -> {
            gesturePane.centreOn(new Point2D(2500, 1000));
        });
        this.gesturePane.setVisible(true);
        gesturePane.toBack();
        gesturePane.reset();
    }

    public void findAllNodes(List<String> nodeTypeList, String floor, String page) throws SQLException {
        selectedFloor.FLOOR.floor = floor;
        getNodeHashMap().forEach(((key, value) -> {
            try {
                if (getMoveHashMap().get(key) == null) {

                } else {
                    Circle circle;
                    Text longName = new Text();

                    if (nodeTypeList.contains(getMoveHashMap().get(value.getNodeID()).getLocation().getNodeType())) {
                        circle = drawCircle(value.getXcoord(), value.getYcoord(), Color.RED, Color.BLACK);
                        longName.setFill(Color.BLACK);
                    } else {
                        circle = drawCircle(value.getXcoord(), value.getYcoord(), Color.GRAY, Color.DARKGRAY);
                        longName.setVisible(false);
                    }


                    if (!getMoveHashMap().get(value.getNodeID()).getLocation().getNodeType().equals("HALL")) {
                        longName.setText(getMoveHashMap().get(value.getNodeID()).getLocation().getShortName());
                    }
                    longName.setFont(Font.font("Ariel", FontWeight.BOLD, 15));
                    longName.toFront();
                    longName.setX(value.getXcoord() - (longName.getLayoutBounds().getWidth() / 2));
                    longName.setY(value.getYcoord() + (circle.getRadius() * 2) + 5);

                    circleHashMap.put(value.getNodeID(), circle);

                    switch (value.getFloor()) {
                        case "L1":
                            this.L1Group.getChildren().addAll(circleHashMap.get(value.getNodeID()), longName);
                            break;
                        case "L2":
                            this.L2Group.getChildren().addAll(circleHashMap.get(value.getNodeID()), longName);
                            break;
                        case "1":
                            this.floor1Group.getChildren().addAll(circleHashMap.get(value.getNodeID()), longName);
                            break;
                        case "2":
                            this.floor2Group.getChildren().addAll(circleHashMap.get(value.getNodeID()), longName);
                            break;
                        case "3":
                            this.floor3Group.getChildren().addAll(circleHashMap.get(value.getNodeID()), longName);
                            break;
                    }
                    setClickedButton(selectedFloor.FLOOR.floor);
                    clickCircle(circle, value, page);
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }));
    }

    public void clickCircle(Circle circle, Node node, String page) {
        switch (page) {
            case "ViewMap":
                circle.setOnMouseClicked(event -> {
                    switch (node.getFloor()) {
                        case "L1":
                            for (javafx.scene.Node nodes : L1Group.getChildren()) {
                                if (nodes instanceof Circle) {
                                    ((Circle) nodes).setFill(Color.GRAY);
                                    ((Circle) nodes).setStroke(Color.DARKGRAY);
                                }
                            }
                            break;
                        case "L2":
                            for (javafx.scene.Node nodes : L2Group.getChildren()) {
                                if (nodes instanceof Circle) {
                                    ((Circle) nodes).setFill(Color.GRAY);
                                    ((Circle) nodes).setStroke(Color.DARKGRAY);
                                }
                            }
                            break;
                        case "1":
                            for (javafx.scene.Node nodes : floor1Group.getChildren()) {
                                if (nodes instanceof Circle) {
                                    ((Circle) nodes).setFill(Color.GRAY);
                                    ((Circle) nodes).setStroke(Color.DARKGRAY);
                                }
                            }
                            break;
                        case "2":
                            for (javafx.scene.Node nodes : floor2Group.getChildren()) {
                                if (nodes instanceof Circle) {
                                    ((Circle) nodes).setFill(Color.GRAY);
                                    ((Circle) nodes).setStroke(Color.DARKGRAY);
                                }
                            }
                            break;
                        case "3":
                            for (javafx.scene.Node nodes : floor3Group.getChildren()) {
                                if (nodes instanceof Circle) {
                                    ((Circle) nodes).setFill(Color.GRAY);
                                    ((Circle) nodes).setStroke(Color.DARKGRAY);
                                }
                            }
                            break;
                    }
                    circle.setFill(Color.PINK);
                    circle.setStroke(Color.RED);
                    PopOver popover = new PopOver();
                    StackPane stackPane = new StackPane();
                    Text text = new Text();
                    try {
                        text.setText("Long Name: " + getMoveHashMap().get(node.getNodeID()).getLocation().getLongName() + "\n" + "Short Name: " + getMoveHashMap().get(node.getNodeID()).getLocation().getShortName() + "\n" + "Node Type: " + getMoveHashMap().get(node.getNodeID()).getLocation().getNodeType() + "\n" + "Node ID: " + node.getNodeID() + "\n" + "Coordinates (x,y): (" + node.getXcoord() + "," + node.getYcoord() + ")" + "\n" + "Floor: " + node.getFloor() + "\n" + "Building: " + node.getBuilding());
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                    popover.setDetachable(false);
                    popover.setCornerRadius(5);
                    popover.setAnimated(true);
                    popover.setCloseButtonEnabled(true);
                    popover.setPrefHeight(150);
                    text.setWrappingWidth(400);

                    popover.prefWidthProperty().bind(Bindings.add(10, text.wrappingWidthProperty()));
                    stackPane.setPrefSize(popover.getPrefWidth(), popover.getPrefHeight());

                    text.setFont(Font.font("Ariel", FontWeight.NORMAL, 15));
                    stackPane.getChildren().add(text);
                    stackPane.setAlignment(Pos.CENTER);
                    popover.setContentNode(stackPane);
                    popover.setArrowLocation(PopOver.ArrowLocation.LEFT_CENTER);
                    popover.show(circle);
                    setLocationSearch(node);
                });
                break;
            case "Pathfinding":
                circle.setOnMouseClicked(event -> {
                    circle.setFill(Color.GREEN);
                    pathfindingList.add(node);
                    System.out.println(pathfindingList.size());
                    if (pathfindingList.size() == 2) {
                        System.out.println("pathfinding");
                        clearAllCircles();
                        try {
                            this.startLocation.getSelectionModel().selectItem(getMoveHashMap().get(pathfindingList.get(0).getNodeID()).getLocation().getLongName());
                            this.endLocation.getSelectionModel().selectItem(getMoveHashMap().get(pathfindingList.get(1).getNodeID()).getLocation().getLongName());
                        } catch (SQLException e) {
                            throw new RuntimeException(e);
                        }
                        pathfinding(pathfindingList.get(0).getNodeID(), pathfindingList.get(1).getNodeID());
                        pathfindingList.clear();
                    }
                });
                break;
        }

    }

    public void setLocationSearch(Node node) {
        try {
            if (getLocationNameHashMap().get(getMoveHashMap().get(node.getNodeID()).getLocation().getLongName()).getNodeType().equals("HALL")) {

            } else {
                this.searchOnMap.setText(getMoveHashMap().get(node.getNodeID()).getLocation().getLongName());
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void pathfinding(int startNodeID, int endNodeID) {
        xCoord.clear();
        yCoord.clear();
        startEnd.clear();
        clearAllCircles();
        int startNodeId;
        int endNodeId;
        Node endNode1 = null;
        Node startNode1 = null;
        startNodeId = startNodeID;
        endNodeId = endNodeID;
        List<Node> shortestPathMap = new ArrayList<>();
        try {
            CongestionController congestionController =new CongestionController();
            AStarAlgorithm mapAlgorithm = new AStarAlgorithm(congestionController);
            startNode1 = DAOFacade.getNode(startNodeId);
            endNode1 = DAOFacade.getNode(endNodeId);
            //shortestPathMap = mapAlgorithm.findShortestPath(startNode1, endNode1);
            shortestPathMap = AlgorithmSingleton.getInstance().algorithm.findShortestPath(startNode1, endNode1);
            System.out.println(shortestPathMap.get(0).getXcoord() + "," + shortestPathMap.get(0).getYcoord());

        } catch (SQLException e) {
            e.printStackTrace();
        }

        List<Double> polyList = new ArrayList<>();
        Node lastNode = shortestPathMap.get(0);
        Node finalNode = shortestPathMap.get(shortestPathMap.size() - 2);
        int change = 0;
        for (Node node : shortestPathMap) {


            System.out.println("new Node: " + node.getFloor());
            System.out.println("old Node: " + lastNode.getFloor());

            if ((!node.getFloor().equals(lastNode.getFloor())) || (node.getNodeID() == shortestPathMap.get(shortestPathMap.size() - 1).getNodeID())) {

                System.out.println("change: " + change);

                change++;

                Circle start = new Circle();
                if (startNode1 == shortestPathMap.get(0)) {
                    start = drawCircle(startNode1.getXcoord(), startNode1.getYcoord(), Color.GREEN, Color.BLACK);
                } else {
                    start = drawCircle(startNode1.getXcoord(), startNode1.getYcoord(), Color.BLUE, Color.BLACK);
                    Node finalStartNode = lastNode;
                    start.setOnMouseClicked(event1 -> {
                        System.out.println("This button works");
                        System.out.println(finalStartNode.getFloor());
                        String finalStartFloor = finalStartNode.getFloor();
                        setClickedButton(finalStartFloor);
                    });
                }

                Circle end = new Circle();
                if (node == shortestPathMap.get(shortestPathMap.size() - 1)) {
                    end = drawCircle(node.getXcoord(), node.getYcoord(), Color.RED, Color.BLACK);
                    finalNode = lastNode;
                } else {
                    end = drawCircle(node.getXcoord(), node.getYcoord(), Color.BLUE, Color.BLACK);

                    Node finalEndNode = node;
                    end.setOnMouseClicked(event1 -> {
                        String endFloor = finalEndNode.getFloor();
                        setClickedButton(endFloor);
                    });
                }
                startNode1 = node;

                Polyline path = new Polyline();
                path.setStroke(Color.RED);
                path.setOpacity(0.8);
                path.setStrokeWidth(6.0f);
                path.getPoints().addAll(polyList);

                String startFloor = lastNode.getFloor();
                if (startFloor != null) {
                    switch (startFloor) {
                        case "L1":
                            this.L1Group.getChildren().add(path);
                            this.L1Group.getChildren().add(start);
                            this.L1Group.getChildren().add(end);
                            break;
                        case "L2":
                            this.L2Group.getChildren().add(path);
                            this.L2Group.getChildren().add(start);
                            this.L2Group.getChildren().add(end);
                            break;
                        case "1":
                            this.floor1Group.getChildren().add(path);
                            this.floor1Group.getChildren().add(start);
                            this.floor1Group.getChildren().add(end);
                            break;
                        case "2":
                            this.floor2Group.getChildren().add(path);
                            this.floor2Group.getChildren().add(start);
                            this.floor2Group.getChildren().add(end);
                            break;
                        case "3":
                            this.floor3Group.getChildren().add(path);
                            this.floor3Group.getChildren().add(start);
                            this.floor3Group.getChildren().add(end);
                            break;
                    }
                }
                polyList.clear();
            }
            polyList.add((double) node.getXcoord());
            polyList.add((double) node.getYcoord());
            lastNode = node;
        }

        polyList.add((double) finalNode.getXcoord());
        polyList.add((double) finalNode.getYcoord());
        Polyline path = new Polyline();
        path.setStroke(Color.RED);
        path.setOpacity(0.8);
        path.setStrokeWidth(6.0f);
        path.getPoints().addAll(polyList);

        String startFloor = lastNode.getFloor();
        if (startFloor != null) {
            switch (startFloor) {
                case "L1":
                    this.L1Group.getChildren().add(path);
                    break;
                case "L2":
                    this.L2Group.getChildren().add(path);
                    break;
                case "1":
                    this.floor1Group.getChildren().add(path);
                    break;
                case "2":
                    this.floor2Group.getChildren().add(path);
                    break;
                case "3":
                    this.floor3Group.getChildren().add(path);
                    break;
            }
        }
        polyList.clear();

        System.out.println(change);

        String startingFloor = shortestPathMap.get(0).getFloor();
        setClickedButton(startingFloor);
        Point2D centerpoint = new Point2D(shortestPathMap.get(0).getXcoord(), shortestPathMap.get(0).getYcoord());
        gesturePane.zoomTo(1, centerpoint);
        gesturePane.centreOn(centerpoint);
    }


}

