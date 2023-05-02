package edu.wpi.tacticaltritons.controllers.signagePages;

import edu.wpi.tacticaltritons.App;
import edu.wpi.tacticaltritons.controllers.MapSuperController;
import edu.wpi.tacticaltritons.database.DAOFacade;
import edu.wpi.tacticaltritons.database.LocationName;
import edu.wpi.tacticaltritons.database.Move;
import edu.wpi.tacticaltritons.database.Node;
import edu.wpi.tacticaltritons.pathfinding.AStarAlgorithm;
import edu.wpi.tacticaltritons.pathfinding.AlgorithmSingleton;
import edu.wpi.tacticaltritons.pathfinding.CongestionController;
import io.github.palexdev.materialfx.controls.MFXFilterComboBox;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Polyline;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import net.kurobako.gesturefx.GesturePane;

import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.*;

public class SignageMoveController extends MapSuperController {

    @FXML
    private Label locationNameDisplay, roomDisplay, floorDisplay, dateDisplay, signLocationDisplay, bottomTextDisplay;
    @FXML private VBox vBox;

    Date today = Date.valueOf(java.time.LocalDate.now());

    public SignageMoveController() throws SQLException {
    }

    private void setLabels(Move move, String room) {
        locationNameDisplay.setText(move.getLocation().getLongName());
        roomDisplay.setText(Integer.toString(move.getNode().getNodeID()));
        floorDisplay.setText(move.getNode().getFloor());
        dateDisplay.setText(move.getMoveDate().toString());
        signLocationDisplay.setText(room);
    }


    @FXML
    private void initialize() throws SQLException {
        initializeImages();
        initializeGesturePane();
        initalizeFloorButtons();
        this.floor1Group.setVisible(true);
        this.floor1Image.setVisible(true);

        List<Move> moves = DAOFacade.getAllMoves();
        HashMap<Integer, Move> hash = new HashMap<>();
        Collections.sort(moves, new Comparator<Move>() {
            @Override
            public int compare(Move o1, Move o2) {
                return o1.getMoveDate().compareTo(o2.getMoveDate());
            }
        });

        bottomTextDisplay.setText("MOVE DESTINATION");
        MFXFilterComboBox<String> displaySelect = new MFXFilterComboBox<>();
        displaySelect.setPromptText("Display location");
        displaySelect.setFloatingText("Display location");
        displaySelect.prefWidthProperty().set(300);
        for(LocationName location:DAOFacade.getAllLocationNames()){
            displaySelect.getItems().add(location.getLongName());
        }
        vBox.getChildren().add(displaySelect);
            displaySelect.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    try {
                        pathfinding(moves.get(0).getNode().getNodeID(),DAOFacade.getNode(displaySelect.getSelectedItem(),today).getNodeID() , true);
                        setLabels(moves.get(0),displaySelect.getSelectedItem());
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                    bottomTextDisplay.setText("All Moves:");
                    vBox.getChildren().remove(displaySelect);
                    for (Move move : moves) {
                        hash.put(move.getNode().getNodeID(), move);
                        if(move.getMoveDate().getTime() > (today.getTime() - 2629746e3)){ //one month before today
                            Button button = new Button(move.getLocation().getLongName() + ": " + move.getMoveDate());
                            button.getStyleClass().add("button-submit");
                            button.setPrefWidth(325);
                            button.setPrefHeight(30);
                            button.setFont(new Font(15));
                            vBox.getChildren().add(button);
                            button.setOnAction(new EventHandler<ActionEvent>() {
                                @Override
                                public void handle(ActionEvent event) {
                                    clearAllNodes();
                                    try {
                                        pathfinding(move.getNode().getNodeID(),DAOFacade.getNode(displaySelect.getSelectedItem(),today).getNodeID(),true);
                                    } catch (SQLException e) {
                                        throw new RuntimeException(e);
                                    }
                                }
                            });
                        }
                    }
                }
            });
        gesturePane.setScrollBarPolicy(GesturePane.ScrollBarPolicy.NEVER);

    }

}

