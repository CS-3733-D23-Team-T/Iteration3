package edu.wpi.tacticaltritons.controllers;

import edu.wpi.tacticaltritons.App;
import edu.wpi.tacticaltritons.database.*;
import edu.wpi.tacticaltritons.navigation.Navigation;
import edu.wpi.tacticaltritons.navigation.Screen;
import io.github.palexdev.materialfx.controls.*;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
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
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class EditMapFullScreenController {

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
    private MFXButton viewNodes;
    @FXML
    private MFXButton pathfinding;
    @FXML
    private MFXButton menuBar;
    @FXML
    private StackPane menuPane;
    @FXML
    private MFXTextField menuLocationName;
    @FXML
    private MFXTextField shortName;
    @FXML
    private MFXFilterComboBox nodeType;
    @FXML
    private MFXTextField nodeID;
    @FXML
    private MFXTextField xCoordinate;
    @FXML
    private MFXTextField yCoordinate;
    @FXML
    private MFXFilterComboBox floor;
    @FXML
    private MFXFilterComboBox building;
    @FXML
    private MFXButton add;
    @FXML
    private MFXButton save;
    @FXML
    private MFXButton delete;
    @FXML
    private MFXButton importButton;
    @FXML
    private MFXButton exportButton;
    @FXML
    private MFXComboBox selector;
    @FXML private MFXButton importExportBar;
    @FXML private StackPane importExportPane;
    @FXML private MFXButton makeEdge;
    @FXML private MFXTextField edgeNodeID;
    private Node oldNode;
    private LocationName oldLocationName;
    private boolean clicked = false;
    Date today = new Date(2023, 4, 19);


    public EditMapFullScreenController() throws SQLException {
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
        circle.setFill(Color.RED);
        circle.setStroke(Color.BLACK);
        circle.setStrokeWidth(3.0f);
        circle.setCenterX(x);
        circle.setCenterY(y);
        circle.setRadius(10.0);
        return circle;
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
            Circle circle = drawCircle(xCoord.get(coordinate), yCoord.get(coordinate));
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

            clickCircle(circle, xCoord, yCoord, hash);

//            int finalCoordinate = coordinate;
//            circle.setOnMousePressed(event1 -> {
//                gesturePane.setGestureEnabled(false);
//                text.setVisible(false);
//
//                double offsetX = event1.getSceneX() - circle.getCenterX();
//                double offsetY = event1.getSceneY() - circle.getCenterY();
//                circle.setUserData(new double[]{offsetX, offsetY});
//                circle.setFill(Color.MEDIUMBLUE);
//
//                nodeID.setText(Integer.toString(nodeIDs.get(finalCoordinate)));
//                xCoordinate.setText(Integer.toString(hash.get(nodeIDs.get(finalCoordinate)).getNode().getXcoord()));
//                yCoordinate.setText(Integer.toString(hash.get(nodeIDs.get(finalCoordinate)).getNode().getYcoord()));
//                floor.setText(hash.get(nodeIDs.get(finalCoordinate)).getNode().getFloor());
//                building.setText(hash.get(nodeIDs.get(finalCoordinate)).getNode().getBuilding());
//
//                menuLocationName.setText(hash.get(nodeIDs.get(finalCoordinate)).getLocation().getLongName());
//                shortName.setText(hash.get(nodeIDs.get(finalCoordinate)).getLocation().getShortName());
//                nodeType.setText(hash.get(nodeIDs.get(finalCoordinate)).getLocation().getNodeType());
//            });
//            circle.setOnMouseDragged(event1 -> {
//                double offsetX = ((double[])circle.getUserData())[0];
//                double offsetY = ((double[])circle.getUserData())[1];
//                circle.setCenterX(event1.getSceneX() - offsetX);
//                circle.setCenterY(event1.getSceneY() - offsetY);
//                text.setX(circle.getCenterX() - (text.getLayoutBounds().getWidth() / 2));
//                text.setY(circle.getCenterY() + (circle.getRadius() * 2));
//                xCoordinate.setText(String.valueOf((int)circle.getCenterX()));
//                yCoordinate.setText(String.valueOf((int)circle.getCenterY()));
//            });
//            circle.setOnMouseReleased(event1 -> {
//                gesturePane.setGestureEnabled(true);
//                circle.setFill(Color.RED);
//
//
//                text.setVisible(true);
//                circle.setUserData(null);
//            });


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

    public void clickCircle(Circle circle, List<Double> xCoord, List<Double> yCoord, HashMap<Integer, Move> hash) {
        Text textLocation = new Text("");
        circle.setOnMouseClicked(event1 -> {


            clearAllNodes();

            try {
                for (Move move : DAOFacade.getAllMoves()) {
                    if (move.getNode().getXcoord() == circle.getCenterX() && move.getNode().getYcoord() == circle.getCenterY()) {
                        this.searchOnMap.getSelectionModel().selectItem(move.getLocation().getLongName());

                        try {
                            setMenuBarAllText(hash);
                            clicked = true;
                        } catch (SQLException e) {
                            throw new RuntimeException(e);
                        }

                        System.out.println(move.getLocation().getLongName());
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

            Circle newCircle = drawCircle(circle.getCenterX(), circle.getCenterY());
            newCircle.setFill(Color.PINK);
            newCircle.setStroke(Color.RED);
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
            gesturePane.zoomTo(1.5,centerpoint);
        });
    }

    public void componentShift(int translate) {
        importExportBar.setTranslateX(translate);
        menuBar.setTranslateX(translate);
        searchOnMap.setTranslateX(translate);
        pathfinding.setTranslateX(translate);
        floorSelect.setTranslateX(translate);
        viewNodes.setTranslateX(translate);
    }

    public void setMenuBarAllText(HashMap<Integer, Move> hash) throws SQLException {
        menuLocationName.setText(searchOnMap.getSelectedItem());
        shortName.setText(hash.get(DAOFacade.getNode(this.searchOnMap.getSelectedItem(), today).getNodeID()).getLocation().getShortName());
        nodeType.setText(hash.get(DAOFacade.getNode(this.searchOnMap.getSelectedItem(), today).getNodeID()).getLocation().getNodeType());
        nodeID.setText(DAOFacade.getNode(this.searchOnMap.getSelectedItem(), today).getNodeID() + "");
        xCoordinate.setText(DAOFacade.getNode(this.searchOnMap.getSelectedItem(), today).getXcoord() + "");
        yCoordinate.setText(DAOFacade.getNode(this.searchOnMap.getSelectedItem(), today).getYcoord() + "");
        floor.setText(DAOFacade.getNode(this.searchOnMap.getSelectedItem(), today).getFloor());
        building.setText(DAOFacade.getNode(this.searchOnMap.getSelectedItem(), today).getBuilding());
        oldNode = new Node(Integer.parseInt(nodeID.getText()),Integer.parseInt(xCoordinate.getText()),Integer.parseInt(yCoordinate.getText()),floor.getText(),building.getText());
        oldLocationName = new LocationName(menuLocationName.getText(),shortName.getText(),nodeType.getText());
    }

    public void initializeImages() {
        lowerLevel1Image.setImage(App.lowerlevel1);
        lowerLevel2Image.setImage(App.lowerlevel2);
        floor1Image.setImage(App.firstfloor);
        floor2Image.setImage(App.secondfloor);
        floor3Image.setImage(App.thirdfloor);
    }

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

        menuBar.setVisible(true);
        importExportBar.setVisible(true);
        menuPane.setVisible(false);
        importExportPane.setVisible(false);

        this.pathfinding.setOnAction(event -> {
            Navigation.navigate(Screen.PATHFINDING);
        });

        this.add.setOnAction(event -> {
            System.out.println("this button works");
            Node node = new Node(0,0,0,"","");
            LocationName locationName = new LocationName("","","");
            if(!nodeID.getText().equals(""))
            {
                node = new Node(Integer.parseInt(nodeID.getText()), Integer.parseInt(xCoordinate.getText()), Integer.parseInt(yCoordinate.getText()), floor.getSelectedItem() + "", building.getSelectedItem() + "");
                try {
                    DAOFacade.addNode(node);
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
            if(!menuLocationName.getText().equals(""))
            {
                locationName = new LocationName(menuLocationName.getText(), shortName.getText(), nodeType.getSelectedItem() + "");
                try {
                    DAOFacade.addLocationName(locationName);
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
            if(!nodeID.getText().equals("") && !menuLocationName.getText().equals(""))
            {
                hash.put(Integer.parseInt(nodeID.getText()), new Move(node,locationName,today));
            }
        });

        this.save.setOnAction(event -> {
            String xCoordinateText;
            String yCoordinateText;
            String floorText;
            String buildingText;
            String longNameText;
            String shortNameText;
            String nodeTypeText;


            if (xCoordinate.getText() == "") {
                try {
                    xCoordinateText = Integer.toString(DAOFacade.getNode(Integer.parseInt(nodeID.getText())).getXcoord());
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            } else {
                xCoordinateText = (xCoordinate.getText());
            }

            if (yCoordinate.getText() == "") {
                try {
                    yCoordinateText = Integer.toString(DAOFacade.getNode(Integer.parseInt(nodeID.getText())).getYcoord());
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            } else {
                yCoordinateText = (yCoordinate.getText());
            }

            if (floor.getText() == "") {
                try {
                    floorText = (DAOFacade.getNode(Integer.parseInt(nodeID.getText())).getFloor());
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            } else {
                floorText = (floor.getText());
                System.out.println(floorText);
            }

            if (building.getText() == "") {
                try {
                    buildingText = (DAOFacade.getNode(Integer.parseInt(nodeID.getText())).getBuilding());
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            } else {
                buildingText = (building.getText());
            }

            if (menuLocationName.getText() == "") {
                try {
                    longNameText = hash.get(DAOFacade.getNode(Integer.parseInt(nodeID.getText()))).getLocation().getLongName();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            } else {
                longNameText = (menuLocationName.getText());
            }

            if (shortName.getText() == "") {
                try {
                    shortNameText = hash.get(DAOFacade.getNode(Integer.parseInt(nodeID.getText()))).getLocation().getShortName();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            } else {
                shortNameText = (shortName.getText());
            }

            if (nodeType.getText() == "") {
                try {
                    nodeTypeText = hash.get(DAOFacade.getNode(Integer.parseInt(nodeID.getText()))).getLocation().getNodeType();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            } else {
                nodeTypeText = (nodeType.getText());
            }

            Node node = new Node(Integer.parseInt(nodeID.getText()), Integer.parseInt(xCoordinateText), Integer.parseInt(yCoordinateText), floorText, buildingText);
            LocationName locationName = new LocationName(longNameText, shortNameText, nodeTypeText);
            try {
                if(clicked) {
                    DAOFacade.updateNode(oldNode, node);
                    DAOFacade.updateLocationName(oldLocationName, locationName);
                }
                else {
                    DAOFacade.updateNode(node);
                    DAOFacade.updateLocationName(locationName);
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });

        this.delete.setOnAction(event -> {
            System.out.println("this button works");
            Node node = new Node(Integer.parseInt(nodeID.getText()), Integer.parseInt(xCoordinate.getText()), Integer.parseInt(yCoordinate.getText()), floor.getSelectedItem() + "", building.getSelectedItem() + "");
            LocationName locationName = new LocationName(menuLocationName.getText(), shortName.getText(), nodeType.getSelectedItem() + "");
            try {
                DAOFacade.deleteNode(node);
                DAOFacade.deleteLocationName(locationName);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });

        this.makeEdge.setOnAction(event -> {

            String currentNodeSelectedId = "";
            String newEdgeNodeId = "";

            if(!(nodeID.getText().isEmpty() || edgeNodeID.getText().isEmpty()))
            {
                currentNodeSelectedId = nodeID.getText();
                newEdgeNodeId = edgeNodeID.getText();
                Node currentNode;
                Node endNode;

                System.out.println(currentNodeSelectedId + " " + newEdgeNodeId);


                try {
                    currentNode= DAOFacade.getNode(Integer.parseInt(currentNodeSelectedId));
                    endNode = DAOFacade.getNode(Integer.parseInt(newEdgeNodeId));
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
                Edge edge = new Edge(currentNode,endNode);
                try {
                    DAOFacade.addEdge(edge);
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
            else
            {
                System.out.println("One of the text boxes are empty");
            }

        });

        this.menuBar.setOnAction(event -> {
            if (!menuPane.isVisible()) {
                importExportPane.setVisible(false);
                menuPane.setVisible(true);
                componentShift(360);
                if (searchOnMap.getText() == "") {
                    menuLocationName.setText("Brigham and Women's Hospital");
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

        this.importExportBar.setOnAction(event -> {
            if (!importExportPane.isVisible()) {
                importExportPane.setVisible(true);
                menuPane.setVisible(false);
                componentShift(210);
            } else {
                importExportPane.setVisible(false);
                componentShift(0);
            }
                    });

        this.viewNodes.setOnAction(event -> {
            clicked = false;
            if (viewNodes.getText().equals("VIEW NODES")) {
                clearAllNodes();
                findNodes(xCoord, yCoord, nodeIDs, hash);
                viewNodes.setText("REMOVE NODES");
            } else {
                clearAllNodes();
                viewNodes.setText("VIEW NODES");
            }
        });

        this.selector.setItems(FXCollections.observableArrayList("Node", "Edge", "Location Name", "Move"));


        this.importButton.setOnAction(
                event -> {
                    String tableName = null;
                    if (selector.getValue().equals("Node")) {
                        tableName = "node";
                    } else if (selector.getValue().equals("Edge")) {
                        tableName = "edge";
                    } else if (selector.getValue().equals("Location Name")) {
                        tableName = "locationname";
                    } else if (selector.getValue().equals("Move")) {
                        tableName = "move";
                    }
                    Stage outStage = new Stage();
                    FileChooser fileChooser = new FileChooser();
                    File file = fileChooser.showOpenDialog(outStage);
                    try {
                        Import.importFile(file, tableName);
                    } catch (IOException | SQLException | ParseException e) {
                        throw new RuntimeException(e);
                    }
                });

        this.exportButton.setOnAction(event -> {
            String tableName = null;
            if (selector.getValue().equals("Node")) {
                tableName = "node";
            } else if (selector.getValue().equals("Edge")) {
                tableName = "edge";
            } else if (selector.getValue().equals("Location Name")) {
                tableName = "locationname";
            } else if (selector.getValue().equals("Move")) {
                tableName = "move";
            }
            Stage exportStage = new Stage();
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Export");
            fileChooser.setInitialFileName(tableName);
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV", "*.csv"));

            File file = fileChooser.showSaveDialog(exportStage);
            DAOFacade data = new DAOFacade();
            try {
                Export.export(file, tableName);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
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
                circle = drawCircle(DAOFacade.getNode(this.searchOnMap.getSelectedItem(), today).getXcoord(), DAOFacade.getNode(this.searchOnMap.getSelectedItem(), today).getYcoord());
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
            System.out.println(centrePoint);
            gesturePane.centreOn(centrePoint);
        });


        this.floorSelect.setItems(
                FXCollections.observableArrayList(
                        "L1 - Lower Level 1",
                        "L2 - Lower Level 2",
                        "1 - 1st Floor",
                        "2 - 2nd Floor",
                        "3 - 3rd Floor"));

        this.floor.setItems(
                FXCollections.observableArrayList(
                        "L1",
                        "L2",
                        "1",
                        "2",
                        "3"));

        this.nodeType.setItems(
                FXCollections.observableArrayList(
                        "REST", "ELEV", "STAI", "HALL", "DEPT", "LABS", "INFO", "CONF", "RETL", "SERV", "EXIT", "BATH"));

        this.building.setItems(
                FXCollections.observableArrayList(
                        "15 Francis",
                        "45 Francis",
                        "Tower",
                        "BTM",
                        "Shapiro"));

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
