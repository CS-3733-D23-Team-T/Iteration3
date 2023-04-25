package edu.wpi.tacticaltritons.controllers;

import edu.wpi.tacticaltritons.App;
import edu.wpi.tacticaltritons.database.*;
import edu.wpi.tacticaltritons.navigation.Navigation;
import edu.wpi.tacticaltritons.navigation.Screen;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXComboBox;
import io.github.palexdev.materialfx.controls.MFXFilterComboBox;
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
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
import java.sql.Date;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class NewEditMapController extends MapSuperController {

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
    @FXML
    private StackPane importExportPane;
    @FXML
    private MFXButton makeEdge;
    @FXML
    private MFXTextField edgeNodeID;

    private Node oldNode;
    private LocationName oldLocationName;
    private boolean clicked = false;
    java.sql.Date today = new Date(2023, 4, 19);

    List<Node> clickNode = new ArrayList<>();
    List<Line> lineList = new ArrayList<>();

    int firstFind = 1;


    public NewEditMapController() throws SQLException {
    }

    public void setLongNamePosition(Text longName, double xCoord, double yCoord) {
        longName.setX(xCoord);
        longName.setY(yCoord);
    }

    public void findAllEdges(){

    }


    public Line drawLine(double startX, double startY, double endX, double endY, Color stroke) {
        Line line = new Line();
        line.setVisible(true);
        line.setStroke(stroke);
        line.setStrokeWidth(4.0f);
        line.setStartX(startX);
        line.setStartX(startX);
        line.setEndX(endX);
        line.setStartY(startY);
        line.setEndY(endY);
        return line;
    }


    public void findAllNodesEdit(List<String> nodeTypeList, String floor, String page) throws SQLException {
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

                    setLongNamePosition(longName, value.getXcoord() - (longName.getLayoutBounds().getWidth() / 2), value.getYcoord() + (circle.getRadius() * 2) + 5);


                    if (firstFind == 1) {
                        circleHashMap.put(value, circle);
                    }

                    switch (value.getFloor()) {
                        case "L1":
                            this.L1Group.getChildren().addAll(circleHashMap.get(value), longName);
                            break;
                        case "L2":
                            this.L2Group.getChildren().addAll(circleHashMap.get(value), longName);
                            break;
                        case "1":
                            this.floor1Group.getChildren().addAll(circleHashMap.get(value), longName);
                            break;
                        case "2":
                            this.floor2Group.getChildren().addAll(circleHashMap.get(value), longName);
                            break;
                        case "3":
                            this.floor3Group.getChildren().addAll(circleHashMap.get(value), longName);
                            break;
                    }
                    setClickedButton(selectedFloor.FLOOR.floor);
                    clickEditCircle(circle, value, longName);
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }));
    }

    public void clickEditCircle(Circle circle, Node node, Text text) {

        circle.setOnMousePressed(event -> {
            if (event.isShiftDown()) {
                gesturePane.setGestureEnabled(false);
                double offsetX = event.getSceneX() - circle.getCenterX();
                double offsetY = event.getSceneY() - circle.getCenterY();


                circle.setUserData(new double[]{offsetX, offsetY});
                try {
                    setMenuBarAllText(getMoveHashMap().get(node.getNodeID()).getLocation().getLongName());
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        circle.setOnMouseDragged(event -> {
            if (event.isShiftDown()) {
                double offsetX = ((double[]) circle.getUserData())[0];
                double offsetY = ((double[]) circle.getUserData())[1];
                circle.setCenterX(event.getSceneX() - offsetX);
                circle.setCenterY(event.getSceneY() - offsetY);
                text.setX(circle.getCenterX() - (text.getLayoutBounds().getWidth() / 2));
                text.setY(circle.getCenterY() + (circle.getRadius() * 2));
                xCoordinate.setText(String.valueOf((int) circle.getCenterX()));
                yCoordinate.setText(String.valueOf((int) circle.getCenterY()));
            }
        });
        circle.setOnMouseReleased(event -> {
            if (event.isShiftDown()) {
                gesturePane.setGestureEnabled(true);
                circle.setUserData(null);
            }
        });
        circle.setOnMouseClicked(event -> {

            componentShift(340);
            menuPane.setVisible(true);
            if (event.isControlDown()) {
                clickNode.add(node);
                circle.setFill(Color.WHITE);
            } else {
                try {
                    setMenuBarAllText(getMoveHashMap().get(node.getNodeID()).getLocation().getLongName());
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }


    public void setMenuBarAllText(String longName) throws SQLException {
        menuLocationName.setText(longName);
        shortName.setText(getMoveHashMap().get(DAOFacade.getNode(longName, today).getNodeID()).getLocation().getShortName());
        nodeType.setText(getMoveHashMap().get(DAOFacade.getNode(longName, today).getNodeID()).getLocation().getNodeType());
        nodeID.setText(DAOFacade.getNode(longName, today).getNodeID() + "");
        xCoordinate.setText(DAOFacade.getNode(longName, today).getXcoord() + "");
        yCoordinate.setText(DAOFacade.getNode(longName, today).getYcoord() + "");
        floor.setText(DAOFacade.getNode(longName, today).getFloor());
        building.setText(DAOFacade.getNode(longName, today).getBuilding());
        oldNode = new Node(Integer.parseInt(nodeID.getText()), Integer.parseInt(xCoordinate.getText()), Integer.parseInt(yCoordinate.getText()), floor.getText(), building.getText());
        oldLocationName = new LocationName(menuLocationName.getText(), shortName.getText(), nodeType.getText());
    }


    @FXML
    private void initialize() throws SQLException {

        importExport.setImage(App.importExport);

        MapSuperController.selectedFloor.FLOOR.floor = "1";

        findAllNodesEdit(allNodeTypes, MapSuperController.selectedFloor.FLOOR.floor, "EditMap");

        initializeImages();
        initalizeFloorButtons();
        initializeGesturePane();
        initializeMenuButton("EditMap");
        initializeSearch("ViewMap");

        this.floor1Group.setVisible(true);
        this.floor1Image.setVisible(true);
        searchOnMap.toFront();
        searchOnMap.setVisible(true);

        menuBar.setVisible(true);
        menuPane.setVisible(false);
        floor1.setStyle("-fx-background-color: BLUE");

        importExport.setVisible(true);
        importExportPane.setVisible(false);

        this.pathfinding.setOnMouseClicked(event -> {
            Navigation.navigate(Screen.PATHFINDING);
        });

        gesturePane.setOnMouseClicked(event -> {
            if (event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 2) {

            }
        });

        this.add.setOnAction(event -> {
            System.out.println("this button works");
            Node node;
            LocationName locationName;
            if (!nodeID.getText().equals("")) {
                node = new Node(Integer.parseInt(nodeID.getText()), Integer.parseInt(xCoordinate.getText()), Integer.parseInt(yCoordinate.getText()), floor.getSelectedItem() + "", building.getSelectedItem() + "");
                try {
                    DAOFacade.addNode(node);
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
            if (!menuLocationName.getText().equals("")) {
                locationName = new LocationName(menuLocationName.getText(), shortName.getText(), nodeType.getSelectedItem() + "");
                try {
                    DAOFacade.addLocationName(locationName);
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
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
                    longNameText = getLocationNameHashMap().get(nodeID.getText()).getLongName();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }

            } else {
                longNameText = (menuLocationName.getText());
            }

            if (shortName.getText() == "") {
                try {
                    shortNameText = getLocationNameHashMap().get(nodeID.getText()).getShortName();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }

            } else {
                shortNameText = (shortName.getText());
            }

            if (nodeType.getText() == "") {

                try {
                    nodeTypeText = getLocationNameHashMap().get(nodeID.getText()).getNodeType();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }

            } else {
                nodeTypeText = (nodeType.getText());
            }

            Node node = new Node(Integer.parseInt(nodeID.getText()), Integer.parseInt(xCoordinateText), Integer.parseInt(yCoordinateText), floorText, buildingText);
            LocationName locationName = new LocationName(longNameText, shortNameText, nodeTypeText);
            try {
                if (clicked) {
                    DAOFacade.updateNode(oldNode, node);
                    DAOFacade.updateLocationName(oldLocationName, locationName);
                } else {
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

        this.gesturePane.setOnKeyPressed(ke -> {
            if (ke.getCode().equals(KeyCode.ENTER)) {
                System.out.println("enter");
                Node startNode = clickNode.get(0);
                Node endNode = clickNode.get(clickNode.size() - 1);
                int xInterval = (startNode.getXcoord() - endNode.getXcoord()) / (clickNode.size() - 1);
                int yInterval = (startNode.getYcoord() - endNode.getYcoord()) / (clickNode.size() - 1);

                for (int i = 1; i < clickNode.size() - 1; i++) {
                    System.out.println(circleHashMap.get(clickNode.get(i)).getCenterX());
                    System.out.println(circleHashMap.get(clickNode.get(i)).getCenterY());
                    circleHashMap.get(clickNode.get(i)).setCenterX(startNode.getXcoord() - (i * xInterval));
                    circleHashMap.get(clickNode.get(i)).setCenterY(startNode.getYcoord() - (i * yInterval));
                    circleHashMap.get(clickNode.get(i)).setFill(Color.RED);
                    circleHashMap.get(clickNode.get(i)).setStroke(Color.BLACK);
                }
                circleHashMap.get(clickNode.get(0)).setFill(Color.RED);
                circleHashMap.get(clickNode.get(clickNode.size() - 1)).setFill(Color.RED);
                clearAllNodes();
                firstFind = 2;
                try {
                    findAllNodesEdit(allNodeTypes, selectedFloor.FLOOR.floor, "ViewMap");
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        this.makeEdge.setOnAction(event -> {

            String currentNodeSelectedId = "";
            String newEdgeNodeId = "";

            if (!(nodeID.getText().isEmpty() || edgeNodeID.getText().isEmpty())) {
                currentNodeSelectedId = nodeID.getText();
                newEdgeNodeId = edgeNodeID.getText();
                Node currentNode;
                Node endNode;

                System.out.println(currentNodeSelectedId + " " + newEdgeNodeId);


                try {
                    currentNode = DAOFacade.getNode(Integer.parseInt(currentNodeSelectedId));
                    endNode = DAOFacade.getNode(Integer.parseInt(newEdgeNodeId));
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
                Edge edge = new Edge(currentNode, endNode);
                try {
                    DAOFacade.addEdge(edge);
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            } else {
                System.out.println("One of the text boxes are empty");
            }

        });


        this.importExport.setOnMouseClicked(event -> {
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
            try {
                findAllNodesEdit(allNodeTypes, selectedFloor.FLOOR.floor, "EditMap");
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });

        this.importButton.setOnAction(
                event -> {
                    Stage outStage = new Stage();
                    FileChooser fileChooser = new FileChooser();
                    File file = fileChooser.showOpenDialog(outStage);
                    try {
                        Import.importFile(file, selector.getText());
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
                setMenuBarAllText(searchOnMap.getSelectedItem());
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

            clearAllNodes();
            Circle circle = new Circle();
            final double[] circleCoord = new double[2];
            final String[] thisFloor = new String[1];

            try {
                getMoveHashMap().forEach((key, value) -> {
                    if (value.getLocation().getLongName().equals(this.searchOnMap.getSelectedItem())) {
                        try {
                            circleCoord[0] = getNodeHashMap().get(key).getXcoord();
                            circleCoord[1] = getNodeHashMap().get(key).getYcoord();
                            thisFloor[0] = getNodeHashMap().get(key).getFloor();
                        } catch (SQLException e) {
                            throw new RuntimeException(e);
                        }
                    }
                });
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

            try {
                findAllNodesEdit(blank, thisFloor[0], "ViewMap");
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

            circle = drawCircle(circleCoord[0], circleCoord[1], Color.PINK, Color.RED);

            String endFloor = null;
            endFloor = thisFloor[0];

            Text longName = new Text();


            try {
                longName.setText(getLocationNameHashMap().get(this.searchOnMap.getSelectedItem()).getShortName());
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            longName.setVisible(true);
            longName.setFont(Font.font("Ariel", FontWeight.BOLD, 15));
            longName.toFront();
            longName.setX(circle.getCenterX() - (longName.getLayoutBounds().getWidth() / 2));
            longName.setY(circle.getCenterY() + (circle.getRadius() * 2) + 5);

            selectedFloor.FLOOR.floor = endFloor;
            if (endFloor != null) {
                switch (endFloor) {
                    case "L1":
                        L1Group.setVisible(true);
                        lowerLevel1Image.setVisible(true);
                        this.L1Group.getChildren().addAll(circle, longName);
                        break;
                    case "L2":
                        L2Group.setVisible(true);
                        lowerLevel2Image.setVisible(true);
                        this.L2Group.getChildren().addAll(circle, longName);
                        break;
                    case "1":
                        floor1Group.setVisible(true);
                        floor1Image.setVisible(true);
                        this.floor1Group.getChildren().addAll(circle, longName);
                        break;
                    case "2":
                        floor2Group.setVisible(true);
                        floor2Image.setVisible(true);
                        this.floor2Group.getChildren().addAll(circle, longName);
                        break;
                    case "3":
                        floor3Group.setVisible(true);
                        floor3Image.setVisible(true);
                        this.floor3Group.getChildren().addAll(circle, longName);
                        break;
                }
                setClickedButton(selectedFloor.FLOOR.floor);
            }
            Point2D centrePoint = new Point2D(circle.getCenterX(), circle.getCenterY());
            gesturePane.centreOn(centrePoint);
        });


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

        gesturePane.setScrollBarPolicy(GesturePane.ScrollBarPolicy.NEVER);
        gesturePane.reset();


    }

}
