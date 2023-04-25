package edu.wpi.tacticaltritons.controllers;

import edu.wpi.tacticaltritons.database.DAOFacade;
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
import java.util.List;

public class NewPathfindingController extends MapSuperController {

    @FXML private MFXDatePicker date;

    public NewPathfindingController() throws SQLException {
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
                findAllNodes(allNodeTypes,selectedFloor.FLOOR.floor, "Pathfinding");
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });

        this.pathfinding.setOnMouseClicked(
                event -> {
                    java.sql.Date sqlDate = java.sql.Date.valueOf(date.getValue());
                    try {
                        pathfinding(DAOFacade.getNode(startLocation.getSelectedItem(), sqlDate).getNodeID(), DAOFacade.getNode(endLocation.getSelectedItem(), sqlDate).getNodeID());
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                });
    }
}
