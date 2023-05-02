package edu.wpi.tacticaltritons.controllers.signagePages;

import edu.wpi.tacticaltritons.controllers.MapSuperController;
import edu.wpi.tacticaltritons.database.DAOFacade;
import edu.wpi.tacticaltritons.database.LocationName;
import edu.wpi.tacticaltritons.database.Move;
import edu.wpi.tacticaltritons.database.Node;
import io.github.palexdev.materialfx.controls.MFXFilterComboBox;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.*;

public class SignageMoveController_BROKEN extends MapSuperController {
    public SignageMoveController_BROKEN() throws SQLException{

    }
    @FXML
    private TextArea textDirections;
    @FXML
    private StackPane directionsPane;

    @FXML
    private Text textForDirections;

    @FXML
    private Label locationNameDisplay, roomDisplay, floorDisplay, dateDisplay, signLocationDisplay, bottomTextDisplay;
    @FXML private VBox vBox;

    LocalDate localDate = LocalDate.of(2023,4,24);
    Date today = Date.valueOf(localDate);

    public void setTextDirections(List<Node> shortestPathMap) throws SQLException {
/*        Directions directions = new Directions(shortestPathMap);

        List<String> position = directions.position();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < position.size(); i++) {
            sb.append(position.get(i));
            sb.append("\n");
        }
        String allPositions = sb.toString();
        textDirections.setText(allPositions);*/
    }

    private void setLabels(Move move, String room) {
        locationNameDisplay.setText(move.getLocation().getLongName());
        roomDisplay.setText(Integer.toString(move.getNode().getNodeID()));
        floorDisplay.setText(move.getNode().getFloor());
        dateDisplay.setText(move.getMoveDate().toString());
        signLocationDisplay.setText(room);
    }

    public void initialize() throws SQLException {
        selectedFloor.FLOOR.floor = "1";
//        findAllNodes(allNodeTypes, selectedFloor.FLOOR.floor, "Pathfinding");

//        initializeImages();
//        initalizeFloorButtons();
        initializeGesturePane();
//        initializeMenuButton("Pathfinding");
//        initializeSearch("Pathfinding");

        this.floor1Group.setVisible(true);
        this.floor1Image.setVisible(true);
        searchOnMap.toFront();
        searchOnMap.setVisible(false);

//        menuPane.setVisible(false);
//        floor1.setStyle("-fx-background-color: BLUE");


        List<Move> moves = DAOFacade.getAllMoves();
        HashMap<Integer, Move> hash = new HashMap<>();
        Collections.sort(moves, new Comparator<Move>() {
            @Override
            public int compare(Move o1, Move o2) {
                return o1.getMoveDate().compareTo(o2.getMoveDate());
            }
        });

        bottomTextDisplay.setText("Choose display location");
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
                        pathfinding(DAOFacade.getNode(displaySelect.getSelectedItem(),today).getNodeID(),moves.get(0).getNode().getNodeID(), false);
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
                                    try {
                                        pathfinding(DAOFacade.getNode(displaySelect.getSelectedItem(),today).getNodeID(),move.getNode().getNodeID(), false);
                                        setLabels(move,displaySelect.getSelectedItem());
                                    } catch (SQLException e) {
                                        throw new RuntimeException(e);
                                    }
                                }
                            });
                        }
                    }
                }
            });
//        }
    }

}

