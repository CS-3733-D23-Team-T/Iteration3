package edu.wpi.tacticaltritons.controllers;

import edu.wpi.tacticaltritons.App;
import edu.wpi.tacticaltritons.database.DAOFacade;
import edu.wpi.tacticaltritons.database.Move;
import edu.wpi.tacticaltritons.database.Node;
import edu.wpi.tacticaltritons.navigation.Navigation;
import edu.wpi.tacticaltritons.navigation.Screen;
import edu.wpi.tacticaltritons.pathfinding.AStarAlgorithm;
import edu.wpi.tacticaltritons.pathfinding.AlgorithmSingleton;
import edu.wpi.tacticaltritons.pathfinding.Directions;
import io.github.palexdev.materialfx.controls.*;
import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.scene.control.TextArea;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Polyline;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import net.kurobako.gesturefx.GesturePane;
import org.checkerframework.checker.units.qual.C;
import org.controlsfx.control.PopOver;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class NewPathfindingController extends MapSuperController {

    @FXML
    private MFXButton directions;
    @FXML
    private StackPane directionsPane;

    @FXML
    private Text textForDirections;

    @FXML
    private ImageView addStop;

    @FXML
    private VBox allStops;

    @FXML
    private VBox allDirections;

    @FXML
    private MFXToggleButton accessible;

    @FXML private ImageView wheelChair;

    List<MFXFilterComboBox> allLongNames = new ArrayList<>();
    List<Node> shortestPath = new ArrayList<>();

    public NewPathfindingController() throws SQLException {
    }

    public void showDirections(boolean bool) {
        directionsPane.setVisible(bool);
        textForDirections.setVisible(bool);
    }

    public void findAllPathNodes(List<String> nodeTypeList, String floor, String page) throws SQLException {
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
                    clickPathCircle(circle, value, page);
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }));
    }

    public void clickPathCircle(Circle circle, Node node, String page) {
        switch (page) {
            case "Pathfinding":
                circle.setOnMouseClicked(event -> {
                    circle.setFill(Color.GREEN);
                    pathfindingList.add(node);
                    System.out.println(pathfindingList.size());
                    if (pathfindingList.size() == 2) {
                        System.out.println("pathfinding");
                        clearAllNodes();
                        pathfinding(pathfindingList.get(0).getNodeID(), pathfindingList.get(1).getNodeID(), accessible.isSelected());
                        pathfindingList.clear();
                        try {
                            setTextDirections(shortestPathMap);
                        } catch (SQLException e) {
                            throw new RuntimeException(e);
                        }
                    }
                });
                break;
        }

    }

    public void setTextDirections(List<Node> shortestPathMap) throws SQLException {
        Directions directions = new Directions(shortestPathMap);

        List<String> position = directions.printDirections();
        for (int i = 0; i < position.size(); i++) {
            HBox thisDirection = new HBox();
            thisDirection.setPrefSize(260, 50);
            thisDirection.setMaxSize(260, 50);
            thisDirection.setMinSize(260, 50);
            thisDirection.setAlignment(Pos.CENTER_LEFT);
            thisDirection.setStyle("-fx-background-color: WHITE;-fx-background-radius: 10;-fx-effect: dropshadow(three-pass-box, #000000, 10, 0.1, 0.1, 0.1)");
            thisDirection.setPadding(new Insets(10));
            thisDirection.setSpacing(20);
            Text textDirection = new Text(position.get(i));
            textDirection.setFont(new Font("Ariel", 11));

            ImageView direction = new ImageView();
            direction.setFitWidth(30);
            direction.setFitHeight(30);

            if (position.get(i).contains("Go Straight")) {
                direction.setImage(App.goStraight);
            } else if (position.get(i).contains("Turn Right")) {
                direction.setImage(App.goRight);
            } else if (position.get(i).contains("Turn Left")) {
                direction.setImage(App.goLeft);
            } else if (position.get(i).contains("arrived")) {
                direction.setImage(App.arrived);
                thisDirection.setStyle("-fx-background-color: LIGHTGREEN;-fx-background-radius: 10;-fx-effect: dropshadow(three-pass-box, #000000, 10, 0.1, 0.1, 0.1)");
            } else {
                direction.setImage(null);
                thisDirection.setStyle("-fx-background-color: LIGHTBLUE;-fx-background-radius: 10;-fx-effect: dropshadow(three-pass-box, #000000, 10, 0.1, 0.1, 0.1)");
            }
            thisDirection.getChildren().add(direction);
            thisDirection.getChildren().add(textDirection);
            allDirections.getChildren().add(thisDirection);
        }
    }

    public void initialize() throws SQLException {

        pathfinding.setImage(App.pathfinding);

        date.setVisible(true);

        gesturePane.setScrollBarPolicy(GesturePane.ScrollBarPolicy.NEVER);
        gesturePane.reset();

        wheelChair.setImage(App.disability);

        addStop.setImage(App.addStop);
        addStop.setRotate(45);

        date.setValue(java.time.LocalDate.now());
        selectedFloor.FLOOR.floor = "1";
        findAllPathNodes(allNodeTypes, selectedFloor.FLOOR.floor, "Pathfinding");

        allLongNames.add(startLocation);
        allLongNames.add(endLocation);

        initializeImages();
        initalizeFloorButtons();
        initializeGesturePane();
        initializeSearch("Pathfinding");

        this.floor1Group.setVisible(true);
        this.floor1Image.setVisible(true);
        searchOnMap.toFront();
        searchOnMap.setVisible(true);

        floor1.setStyle("-fx-background-color: BLUE");

        this.viewNodes.setOnAction(event -> {
            clearAllNodes();
            try {
                findAllPathNodes(allNodeTypes, selectedFloor.FLOOR.floor, "Pathfinding");
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });

        this.directions.setVisible(true);
        showDirections(false);
        this.directions.setOnAction(event -> {

            if (!directionsPane.isVisible()) {
                directions.setTranslateX(-155);
                showDirections(true);
            } else {
                directions.setTranslateX(0);
                showDirections(false);
            }

        });

        this.addStop.setOnMouseClicked(event -> {
            MFXFilterComboBox addedStop = new MFXFilterComboBox<>();

            HBox thisStop = new HBox();
            thisStop.setAlignment(Pos.CENTER_LEFT);
            thisStop.setSpacing(10);

            addedStop.setMinSize(300, 50);
            try {
                getLocationNameHashMap().forEach(((key, value) -> {
                    if (!value.getNodeType().equals("HALL")) {
                        addedStop.getItems().add(key);
                    }
                }));
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

            ImageView removeStop = new ImageView();
            removeStop.setImage(App.addStop);
            removeStop.setFitWidth(50);
            removeStop.setFitHeight(50);

            thisStop.getChildren().add(addedStop);
            thisStop.getChildren().add(removeStop);

            allStops.getChildren().add(thisStop);
            allLongNames.add(addedStop);

            removeStop.setOnMouseClicked(event1 -> {
                allStops.getChildren().remove(thisStop);
                allLongNames.remove(addedStop);
            });
        });


        this.pathfinding.setOnMouseClicked(
                event -> {
                    shortestPathMap.clear();
                    clearAllNodes();
                    if (allDirections.getChildren().size() > 1) {
                        List<javafx.scene.Node> allHBox = new ArrayList<>();
                        for (javafx.scene.Node node : allDirections.getChildren()) {
                            if (node instanceof HBox) {
                                allHBox.add(node);
                            }
                        }
                        allDirections.getChildren().removeAll(allHBox);
                        allHBox.clear();
                    }

                    clearAllNodes();
                    for (int i = 1; i <= allLongNames.size() - 1; i++) {
                        today = java.sql.Date.valueOf(date.getValue());
                        final int[] startNodeID = {0};
                        final int[] endNodeID = {0};
                        String start = allLongNames.get(i - 1).getSelectedItem().toString();
                        String end = allLongNames.get(i).getSelectedItem().toString();
                        try {
                            List<Move> allCurrentMoves = DAOFacade.getAllCurrentMoves(today);

                            HashMap<Integer, Move> hash = new HashMap<>();

                            for (Move move : allCurrentMoves) {
                                hash.put(move.getNode().getNodeID(), move);
                            }
                            hash.forEach((key, value) -> {
                                if (value.getLocation().getLongName().equals(start)) {
                                    startNodeID[0] = key;
                                }
                                if (value.getLocation().getLongName().equals(end)) {
                                    endNodeID[0] = key;
                                }
                            });
                            pathfinding(startNodeID[0], endNodeID[0], accessible.isSelected());
//                            shortestPath.addAll(shortestPathMap);
                            try {
                                setTextDirections(shortestPathMap);
                            } catch (SQLException e) {
                                throw new RuntimeException(e);
                            }
                        } catch (SQLException e) {
                            throw new RuntimeException(e);
                        }
                    }
                });
    }


}
