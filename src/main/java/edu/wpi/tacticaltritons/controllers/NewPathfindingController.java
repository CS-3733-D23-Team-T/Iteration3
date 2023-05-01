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
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXCheckbox;
import io.github.palexdev.materialfx.controls.MFXDatePicker;
import io.github.palexdev.materialfx.controls.MFXFilterComboBox;
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
import org.checkerframework.checker.units.qual.C;

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
    private Text pathfindingComment;

    @FXML
    private ImageView addStop;

    @FXML
    private VBox allStops;

    @FXML
    private VBox allDirections;

    List<MFXFilterComboBox> allLongNames = new ArrayList<>();
    List<Node> shortestPath = new ArrayList<>();

    public NewPathfindingController() throws SQLException {
    }

    public void showDirections(boolean bool) {
        directionsPane.setVisible(bool);
    }

    public void initializeMenuButton(String page) {
        this.menuBar.setOnMouseClicked(event -> {
            if (!menuPane.isVisible()) {
                menuPane.setVisible(true);
                switch (page) {
                    case "ViewMap":
                        componentShift(210);
                        break;
                    case "Pathfinding":
                        componentShift(210);
                        break;
                    case "EditMap":
                        componentShift(340);
                        break;

                }
                if (pathfindingCommentString == null) {
                    pathfindingComment.setText("Nothing here");
                } else {
                    pathfindingComment.setText(pathfindingCommentString);
                }
            } else {
                menuPane.setVisible(false);
                componentShift(0);
            }
        });
        this.editMap.setOnAction(event -> {
            Navigation.navigate(Screen.EDIT_MAP);
        });
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
            } else {
                direction.setImage(App.arrived);
                thisDirection.setStyle("-fx-background-color: LIGHTGREEN;-fx-background-radius: 10;-fx-effect: dropshadow(three-pass-box, #000000, 10, 0.1, 0.1, 0.1)");
            }
            thisDirection.getChildren().add(direction);
            thisDirection.getChildren().add(textDirection);
            allDirections.getChildren().add(thisDirection);
        }
    }

    public void initialize() throws SQLException {


        addStop.setImage(App.addStop);
        addStop.setRotate(45);

        date.setValue(java.time.LocalDate.now());
        selectedFloor.FLOOR.floor = "1";
        findAllNodes(allNodeTypes, selectedFloor.FLOOR.floor, "Pathfinding");

        allLongNames.add(startLocation);
        allLongNames.add(endLocation);

        initializeImages();
        initalizeFloorButtons();
        initializeGesturePane();
        initializeMenuButton("Pathfinding");
        initializeSearch("Pathfinding");

        this.floor1Group.setVisible(true);
        this.floor1Image.setVisible(true);
        searchOnMap.toFront();
        searchOnMap.setVisible(true);

        menuBar.setVisible(true);
        menuPane.setVisible(false);
        floor1.setStyle("-fx-background-color: BLUE");

        this.viewNodes.setOnAction(event -> {
            clearAllNodes();
            try {
                findAllNodes(allNodeTypes, selectedFloor.FLOOR.floor, "Pathfinding");
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });

        this.directions.setVisible(true);
        showDirections(false);
        this.directions.setOnAction(event -> {
            if (!directionsPane.isVisible()) {
                showDirections(true);
            } else {
                showDirections(false);
            }
        });

        this.addStop.setOnMouseClicked(event -> {
            MFXFilterComboBox addedStop = new MFXFilterComboBox<>();
            addedStop.setPrefSize(300, 50);
            try {
                getLocationNameHashMap().forEach(((key, value) -> {
                    if (!value.getNodeType().equals("HALL")) {
                        addedStop.getItems().add(key);
                    }
                }));
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            allStops.getChildren().add(addedStop);
            allLongNames.add(addedStop);
        });

        this.pathfinding.setOnMouseClicked(
                event -> {
                    clearAllNodes();
                    for (int i = 1; i <= allLongNames.size()-1; i++) {
                        today = java.sql.Date.valueOf(date.getValue());
                        final int[] startNodeID = {0};
                        final int[] endNodeID = {0};
                        String start = allLongNames.get(i-1).getSelectedItem().toString();
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
                            pathfinding(startNodeID[0], endNodeID[0]);
                            shortestPath.addAll(shortestPathMap);
                        } catch (SQLException e) {
                            throw new RuntimeException(e);
                        }
                    }
                    try {
                        setTextDirections(shortestPath);
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                    shortestPath.clear();
                    allDirections.getChildren().removeAll();
                });
    }


}
