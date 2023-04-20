package edu.wpi.tacticaltritons.controllers;

import edu.wpi.tacticaltritons.App;
import edu.wpi.tacticaltritons.database.DAOFacade;
import edu.wpi.tacticaltritons.database.LocationName;
import edu.wpi.tacticaltritons.database.Move;
import edu.wpi.tacticaltritons.database.Node;
import edu.wpi.tacticaltritons.navigation.Navigation;
import edu.wpi.tacticaltritons.navigation.Screen;
import edu.wpi.tacticaltritons.pathfinding.AStarAlgorithm;
import edu.wpi.tacticaltritons.pathfinding.AlgorithmSingleton;
import edu.wpi.tacticaltritons.pathfinding.Directions;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXCheckbox;
import io.github.palexdev.materialfx.controls.MFXComboBox;
import io.github.palexdev.materialfx.controls.MFXFilterComboBox;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.control.TextArea;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Polyline;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import net.kurobako.gesturefx.GesturePane;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class PathfindingFullScreenController {

    @FXML
    private MFXButton viewNodes;

    @FXML
    private MFXFilterComboBox<String> startLocation = new MFXFilterComboBox<>();

    @FXML
    private MFXFilterComboBox<String> endLocation = new MFXFilterComboBox<>();

    @FXML
    private MFXButton pathfinding;

    @FXML
    private MFXComboBox<String> floorSelect;

    @FXML
    private TextArea textDirections;

    @FXML
    private MFXButton directions;

    @FXML
    private GesturePane gesturePane;

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
    private StackPane directionsPane;

    @FXML
    private Text textForDirections;
    @FXML
    private MFXButton menuBar;
    @FXML
    private StackPane menuPane;
    @FXML
    private Text menuLocationName;
    @FXML
    private Text menuLocationDescription;

    Date today = new Date(2023, 04, 05);


    int directionsCounter = 0;
    int menuBarCounter = 0;


    public void componentShift(int translate) {
        menuBar.setTranslateX(translate);
        startLocation.setTranslateX(translate);
        endLocation.setTranslateX(translate);
        pathfinding.setTranslateX(translate);
        floorSelect.setTranslateX(translate);
        viewNodes.setTranslateX(translate);
    }

    public void setMenuBarAllText(HashMap<Integer, Move> hash) throws SQLException {
        menuLocationName.setText(startLocation.getSelectedItem());
        menuLocationDescription.setText("Short Name: " + (hash.get(DAOFacade.getNode(this.startLocation.getSelectedItem(), today).getNodeID()).getLocation().getShortName()) + "\n" + "Node Type: " + (hash.get(DAOFacade.getNode(this.startLocation.getSelectedItem(), today).getNodeID()).getLocation().getNodeType()) + "\n" + "Node ID: " + DAOFacade.getNode(this.startLocation.getSelectedItem(), today).getNodeID() + "\n" + "Coordinates (x,y): (" + (DAOFacade.getNode(this.startLocation.getSelectedItem(), today).getXcoord()) + "," + (DAOFacade.getNode(this.startLocation.getSelectedItem(), today).getYcoord()) + ")" + "\n" + "Floor: " + (DAOFacade.getNode(this.startLocation.getSelectedItem(), today).getFloor()) + "\n" + "Building: " + (DAOFacade.getNode(this.startLocation.getSelectedItem(), today).getBuilding()));
    }

    public void showDirections(boolean bool) {
        directionsPane.setVisible(bool);
        textDirections.setVisible(bool);
        textForDirections.setVisible(bool);
    }

    public void findNodes(List<Double> xCoord, List<Double> yCoord, List<Integer> nodeIDs, HashMap<Integer, Move> hash) {

        System.out.println("this button works");
        System.out.println(this.floorSelect.getText().substring(0, 2));
        try {
            for (Node node : DAOFacade.getAllNodes()) {
                if (node.getFloor().equals(this.floorSelect.getText().substring(0, this.floorSelect.getText().indexOf(" ")))) {
                    xCoord.add((double) node.getXcoord());
                    yCoord.add((double) node.getYcoord());
                    nodeIDs.add(node.getNodeID());
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        System.out.println(xCoord.size());

        System.out.println(xCoord);
        System.out.println(yCoord);


        for (int coordinate = 0; coordinate < xCoord.size() - 1; coordinate++) {
            Circle circle = drawCircle(xCoord.get(coordinate), yCoord.get(coordinate), Color.RED);
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

    public void clearAllNodes() {
        floor1Group.getChildren().remove(1, floor1Group.getChildren().size());
        floor2Group.getChildren().remove(1, floor2Group.getChildren().size());
        floor3Group.getChildren().remove(1, floor3Group.getChildren().size());
        L1Group.getChildren().remove(1, L1Group.getChildren().size());
        L2Group.getChildren().remove(1, L2Group.getChildren().size());
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

    public void setTextDirections(List<Node> shortestPathMap) throws SQLException {
        Directions directions = new Directions(shortestPathMap);

        List<String> position = directions.position();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < position.size(); i++) {
            sb.append(position.get(i));
            sb.append("\n");
        }
        String allPositions = sb.toString();
        System.out.println(allPositions);
        textDirections.setText(allPositions);
    }

    @FXML
    private void initialize() throws SQLException {

        lowerLevel1Image.setImage(App.lowerlevel1);
        lowerLevel2Image.setImage(App.lowerlevel2);
        floor1Image.setImage(App.firstfloor);
        floor2Image.setImage(App.secondfloor);
        floor3Image.setImage(App.thirdfloor);

        List<Double> xCoord = new ArrayList<Double>(0);
        List<Double> yCoord = new ArrayList<Double>(0);
        List<Double> startEnd = new ArrayList<Double>(0);
        List<Integer> nodeIDs = new ArrayList<Integer>();
        List<Move> allMoves = DAOFacade.getAllMoves();

        HashMap<Integer, Move> hash = new HashMap<>();
        for (Move move : allMoves) {
            hash.put(move.getNode().getNodeID(), move);
        }

        showDirections(false);
        menuBar.setVisible(true);
        menuPane.setVisible(false);
        gesturePane.toBack();
        menuLocationDescription.setWrappingWidth(170);
        menuLocationName.setWrappingWidth(170);

        this.menuBar.setOnAction(event -> {
            if (!menuPane.isVisible()) {
                menuPane.setVisible(true);
                componentShift(210);
                if (startLocation.getText() == "") {
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


        for (LocationName name : DAOFacade.getAllLocationNames()) {
            startLocation.getItems().add(name.getLongName());
        }

        for (LocationName name : DAOFacade.getAllLocationNames()) {
            endLocation.getItems().add(name.getLongName());
        }

        this.directions.setVisible(true);
        this.directions.setOnAction(event -> {
            if (directionsCounter % 2 == 0) {
                showDirections(true);
            } else {
                showDirections(false);
            }
            directionsCounter++;
        });

        this.viewNodes.setOnAction(event -> {
            if (viewNodes.getText().equals("VIEW NODES")) {
                gesturePane.zoomTo(0.5, new Point2D(2500, 1700));
                gesturePane.centreOn(new Point2D(2500, 1000));
                clearAllNodes();
                findNodes(xCoord, yCoord, nodeIDs, hash);
                viewNodes.setText("REMOVE NODES");
            } else {
                clearAllNodes();
                viewNodes.setText("VIEW NODES");
            }
        });

        this.pathfinding.setOnAction(
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
                        startNodeId = DAOFacade.getNode(startLocation.getSelectedItem(), today).getNodeID();
                        endNodeId = DAOFacade.getNode(endLocation.getSelectedItem(), today).getNodeID();
                        setMenuBarAllText(hash);
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                    List<Node> shortestPathMap = new ArrayList<>();
                    try {
                        AStarAlgorithm mapAlgorithm = new AStarAlgorithm();
                        startNode1 = DAOFacade.getNode(startNodeId);
                        endNode1 = DAOFacade.getNode(endNodeId);
                        //shortestPathMap = mapAlgorithm.findShortestPath(startNode1, endNode1);
                        shortestPathMap = AlgorithmSingleton.getInstance().algorithm.findShortestPath(startNode1, endNode1);
                        setTextDirections(shortestPathMap);
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
                                start = drawCircle(startNode1.getXcoord(), startNode1.getYcoord(), Color.GREEN);
                            } else {
                                start = drawCircle(startNode1.getXcoord(), startNode1.getYcoord(), Color.BLUE);
                                Node finalStartNode = lastNode;
                                start.setOnMouseClicked(event1 -> {
                                    System.out.println("This button works");
                                    System.out.println(finalStartNode.getFloor());
                                    String finalStartFloor = finalStartNode.getFloor();
                                    if (finalStartFloor != null) {
                                        switch (finalStartFloor) {
                                            case "L1":
                                                this.floorSelect.getSelectionModel().selectItem("L1 - Lower Level 1");
                                                break;
                                            case "L2":
                                                this.floorSelect.getSelectionModel().selectItem("L2 - Lower Level 2");
                                                break;
                                            case "1":
                                                this.floorSelect.getSelectionModel().selectItem("1 - 1st Floor");
                                                break;
                                            case "2":
                                                this.floorSelect.getSelectionModel().selectItem("2 - 2nd Floor");
                                                break;
                                            case "3":
                                                this.floorSelect.getSelectionModel().selectItem("3 - 3rd Floor");
                                                break;
                                        }
                                    }

                                });
                            }

                            Circle end = new Circle();
                            if (node == shortestPathMap.get(shortestPathMap.size() - 1)) {
                                end = drawCircle(node.getXcoord(), node.getYcoord(), Color.RED);
                                finalNode = lastNode;
                            } else {
                                end = drawCircle(node.getXcoord(), node.getYcoord(), Color.BLUE);

                                Node finalEndNode = node;
                                end.setOnMouseClicked(event1 -> {
                                    String endFloor = finalEndNode.getFloor();
                                    if (endFloor != null) {
                                        switch (endFloor) {
                                            case "L1":
                                                this.floorSelect.getSelectionModel().selectItem("L1 - Lower Level 1");
                                                break;
                                            case "L2":
                                                this.floorSelect.getSelectionModel().selectItem("L2 - Lower Level 2");
                                                break;
                                            case "1":
                                                this.floorSelect.getSelectionModel().selectItem("1 - 1st Floor");
                                                break;
                                            case "2":
                                                this.floorSelect.getSelectionModel().selectItem("2 - 2nd Floor");
                                                break;
                                            case "3":
                                                this.floorSelect.getSelectionModel().selectItem("3 - 3rd Floor");
                                                break;
                                        }
                                    }

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
                    if (startingFloor != null) {

                        switch (startingFloor) {
                            case "L1":
                                this.floorSelect.getSelectionModel().selectItem("L1 - Lower Level 1");
                                L1Group.setVisible(true);
                                lowerLevel1Image.setVisible(true);
                                break;
                            case "L2":
                                this.floorSelect.getSelectionModel().selectItem("L2 - Lower Level 2");
                                L2Group.setVisible(true);
                                lowerLevel2Image.setVisible(true);
                                break;
                            case "1":
                                this.floorSelect.getSelectionModel().selectItem("1 - 1st Floor");
                                floor1Group.setVisible(true);
                                floor1Image.setVisible(true);
                                break;
                            case "2":
                                this.floorSelect.getSelectionModel().selectItem("2 - 2nd Floor");
                                floor2Group.setVisible(true);
                                floor2Image.setVisible(true);
                                break;
                            case "3":
                                this.floorSelect.getSelectionModel().selectItem("3 - 3rd Floor");
                                floor3Group.setVisible(true);
                                floor3Image.setVisible(true);
                                break;
                        }
                    }
                    Point2D centerpoint = new Point2D(shortestPathMap.get(0).getXcoord(), shortestPathMap.get(0).getYcoord());
                    gesturePane.zoomTo(1, centerpoint);
                    gesturePane.centreOn(centerpoint);
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
        this.floor1Image.setVisible(true);


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
