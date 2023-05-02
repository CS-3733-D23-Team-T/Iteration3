package edu.wpi.tacticaltritons.controllers;

import arduino.Arduino;
import edu.wpi.tacticaltritons.App;
import edu.wpi.tacticaltritons.database.DAOFacade;
import edu.wpi.tacticaltritons.database.Move;
import edu.wpi.tacticaltritons.database.Node;
import edu.wpi.tacticaltritons.navigation.Navigation;
import edu.wpi.tacticaltritons.navigation.Screen;
import edu.wpi.tacticaltritons.pathfinding.AStarAlgorithm;
import edu.wpi.tacticaltritons.pathfinding.AlgorithmSingleton;
import edu.wpi.tacticaltritons.pathfinding.Directions;
import edu.wpi.tacticaltritons.robot.RobotComm;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXCheckbox;
import io.github.palexdev.materialfx.controls.MFXDatePicker;
import io.github.palexdev.materialfx.controls.MFXFilterComboBox;
import javafx.fxml.FXML;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.control.TextArea;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.StackPane;
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
    private TextArea textDirections;

    @FXML
    private ImageView robotIcon;

    @FXML
    private MFXButton directions;
    @FXML
    private StackPane directionsPane;

    @FXML
    private Text textForDirections;

    @FXML
    private Text pathfindingComment;

    public NewPathfindingController() throws SQLException {
    }

    public void showDirections(boolean bool) {
        directionsPane.setVisible(bool);
        textDirections.setVisible(bool);
        textForDirections.setVisible(bool);
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
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < position.size(); i++) {
            sb.append(position.get(i));
            sb.append("\n");
        }
        String allPositions = sb.toString();
        System.out.println(allPositions);
        textDirections.setText(allPositions);
    }

    public void initialize() throws SQLException {

        date.setValue(java.time.LocalDate.now());
        selectedFloor.FLOOR.floor = "1";
        findAllNodes(allNodeTypes, selectedFloor.FLOOR.floor, "Pathfinding");

        initializeImages();
        robotIcon.setImage(App.robot);
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

        this.pathfinding.setOnMouseClicked(
                event -> {
                    today = java.sql.Date.valueOf(date.getValue());
                    final int[] startNodeID = {0};
                    final int[] endNodeID = {0};
                    try {
                        List<Move> allCurrentMoves = DAOFacade.getAllCurrentMoves(today);

                        HashMap<Integer, Move> hash = new HashMap<>();

                        for (Move move : allCurrentMoves) {
                            hash.put(move.getNode().getNodeID(), move);
                        }
                        hash.forEach((key, value) -> {
                            if (value.getLocation().getLongName().equals(startLocation.getSelectedItem())) {
                                System.out.println("it works");
                                startNodeID[0] = key;
                            }
                            if (value.getLocation().getLongName().equals(endLocation.getSelectedItem())) {
                                System.out.println("it works");
                                endNodeID[0] = key;
                            }
                        });
                        pathfinding(startNodeID[0], endNodeID[0]);
                        setTextDirections(shortestPathMap);
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                });

        //robot path following with live map updating
        //checks if connection is active, then converts cartesian coordinates to polar coordinates
        //robot code uses a new thread so the map is still usable while communicating with robot
        this.robotIcon.setOnMouseClicked(event -> {
            robotIcon.setDisable(true);
            List<Group> groups = new ArrayList<>();
            groups.add(L1Group);
            groups.add(L2Group);
            groups.add(floor1Group);
            groups.add(floor2Group);
            groups.add(floor3Group);
            for (Group group : groups) {
                group.getChildren().add(RobotComm.drawObservableCircle());
            }
            RobotComm.runRobot(shortestPathMap);
            robotIcon.setDisable(false);

            RobotComm.xRobotCoordinate.addListener(((observable, oldValue, newValue) -> {
                Point2D centerpoint = new Point2D(RobotComm.xRobotCoordinate.get(),RobotComm.yRobotCoordinate.get());
                gesturePane.zoomTo(1, centerpoint);
                gesturePane.centreOn(centerpoint);
            }));
            RobotComm.yRobotCoordinate.addListener(((observable, oldValue, newValue) -> {
                Point2D centerpoint = new Point2D(RobotComm.xRobotCoordinate.get(),RobotComm.yRobotCoordinate.get());
                gesturePane.zoomTo(1, centerpoint);
                gesturePane.centreOn(centerpoint);
            }));
        });
    }

}
