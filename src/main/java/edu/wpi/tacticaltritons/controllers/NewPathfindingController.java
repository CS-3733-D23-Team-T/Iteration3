package edu.wpi.tacticaltritons.controllers;

import edu.wpi.tacticaltritons.database.DAOFacade;
import edu.wpi.tacticaltritons.database.Move;
import edu.wpi.tacticaltritons.database.Node;
import edu.wpi.tacticaltritons.navigation.Navigation;
import edu.wpi.tacticaltritons.navigation.Screen;
import edu.wpi.tacticaltritons.pathfinding.AStarAlgorithm;
import edu.wpi.tacticaltritons.pathfinding.AlgorithmSingleton;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXCheckbox;
import io.github.palexdev.materialfx.controls.MFXDatePicker;
import io.github.palexdev.materialfx.controls.MFXFilterComboBox;
import javafx.fxml.FXML;
import javafx.geometry.Point2D;
import javafx.scene.control.TextArea;
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
    private MFXButton directions;
    @FXML
    private StackPane directionsPane;

    @FXML
    private Text textForDirections;


    public NewPathfindingController() throws SQLException {
    }

    public void showDirections(boolean bool) {
        directionsPane.setVisible(bool);
        textDirections.setVisible(bool);
        textForDirections.setVisible(bool);
    }

    public void initialize() throws SQLException {
        selectedFloor.FLOOR.floor = "1";
        findAllNodes(allNodeTypes, selectedFloor.FLOOR.floor, "Pathfinding");

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
                    final int[] endNodeID = new int[1];
                    try {
                        List<Move> allCurrentMoves = DAOFacade.getAllCurrentMoves(today);

                        HashMap<Integer, Move> hash = new HashMap<>();

                        for (Move move : allCurrentMoves) {
                            hash.put(move.getNode().getNodeID(), move);
                        }

                        hash.forEach((key, value) -> {
                            if (value.getLocation().getLongName() == startLocation.getSelectedItem()) {
                                startNodeID[0] = key;
                            }
                            if (value.getLocation().getLongName() == endLocation.getSelectedItem()) {
                                endNodeID[0] = key;
                            }
                        });
                        pathfinding(startNodeID[0], endNodeID[0]);
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                });
    }


}
