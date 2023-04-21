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
import java.sql.Date;
import java.util.HashMap;
import java.util.List;

import edu.wpi.tacticaltritons.styling.ThemeColors;

public class MapSuperController {
    @FXML
    protected GesturePane gesturePane;

    @FXML
    protected Group L1Group;
    @FXML
    protected Group L2Group;
    @FXML
    protected Group floor1Group;
    @FXML
    protected Group floor2Group;
    @FXML
    protected Group floor3Group;

    @FXML
    protected ImageView lowerLevel1Image;

    @FXML
    protected ImageView lowerLevel2Image;

    @FXML
    protected ImageView floor1Image;

    @FXML
    protected ImageView floor2Image;

    @FXML
    protected ImageView floor3Image;

    @FXML
    protected MFXFilterComboBox<String> searchOnMap = new MFXFilterComboBox<>();


    @FXML
    protected ImageView pathfinding;

    @FXML
    protected ImageView menuBar;

    @FXML
    protected StackPane menuPane;

    @FXML
    protected MFXButton editMap;

    @FXML
    protected MFXButton lowerLevel1;

    @FXML
    protected MFXButton lowerLevel2;

    @FXML
    protected MFXButton floor1;

    @FXML
    protected MFXButton floor2;

    @FXML
    protected MFXButton floor3;

    public List<Node> allNodes = DAOFacade.getAllNodes();
    public List<Move> allMoves = DAOFacade.getAllMoves();
    public List<LocationName> allLocationNames = DAOFacade.getAllLocationNames();


    Date today = Date.valueOf(java.time.LocalDate.now());

    public MapSuperController() throws SQLException {
    }

    private enum selectedFloor {
        FLOOR("1");
        public String floor;

        selectedFloor(String floor) {
            this.floor = floor;
        }
    }

    public HashMap<Integer, Node> getNodeHashMap() throws SQLException {
        HashMap<Integer, Node> hash = new HashMap<>();
        for (Node node : allNodes) {
            hash.put(node.getNodeID(), node);
        }
        return hash;
    }

    public HashMap<String, LocationName> getLocationNameHashMap() throws SQLException {
        HashMap<String, LocationName> hash = new HashMap<>();
        for (LocationName locationName : allLocationNames) {
            hash.put(locationName.getLongName(), locationName);
        }
        return hash;
    }

    public void initializeSearch() throws SQLException {
        getLocationNameHashMap().forEach(((key, value) -> {
            if(!value.getNodeType().equals("HALL"))
                searchOnMap.getItems().add(key);
        }));
    }

    public void initializeMenuButton() {
        this.menuBar.setOnMouseClicked(event -> {
            if (!menuPane.isVisible()) {
                menuPane.setVisible(true);
                componentShift(210);
            } else {
                menuPane.setVisible(false);
                componentShift(0);
            }
        });
    }

    public void componentShift(int translate) {
        menuBar.setTranslateX(translate);
        searchOnMap.setTranslateX(translate);
        pathfinding.setTranslateX(translate);
        lowerLevel1.setTranslateX(translate);
        lowerLevel2.setTranslateX(translate);
        floor1.setTranslateX(translate);
        floor2.setTranslateX(translate);
        floor3.setTranslateX(translate);
    }

    public void initializeImages() {
        lowerLevel1Image.setImage(App.lowerlevel1);
        lowerLevel2Image.setImage(App.lowerlevel2);
        floor1Image.setImage(App.firstfloor);
        floor2Image.setImage(App.secondfloor);
        floor3Image.setImage(App.thirdfloor);
        pathfinding.setImage(App.pathfinding);
        menuBar.setImage(App.menuBar);
    }

    public void resetButtons() {
        this.lowerLevel1.setStyle("-fx-background-color: WHITE");
        this.lowerLevel2.setStyle("-fx-background-color: WHITE");
        this.floor1.setStyle("-fx-background-color: WHITE");
        this.floor2.setStyle("-fx-background-color: WHITE");
        this.floor3.setStyle("-fx-background-color: WHITE");
    }

    public void resetImages() {
        L1Group.setVisible(false);
        lowerLevel1Image.setVisible(false);

        L2Group.setVisible(false);
        lowerLevel2Image.setVisible(false);

        floor1Group.setVisible(false);
        floor1Image.setVisible(false);

        floor2Group.setVisible(false);
        floor2Image.setVisible(false);

        floor3Group.setVisible(false);
        floor3Image.setVisible(false);
    }

    public void initalizeFloorButtons() {
        this.lowerLevel1.setOnAction(event -> {
            resetButtons();
            this.lowerLevel1.setStyle("-fx-background-color: BLUE");
            resetImages();
            L1Group.setVisible(true);
            lowerLevel1Image.setVisible(true);
            selectedFloor.FLOOR.floor = "L1";
        });
        this.lowerLevel2.setOnAction(event -> {
            resetButtons();
            this.lowerLevel2.setStyle("-fx-background-color: BLUE");
            resetImages();
            L2Group.setVisible(true);
            lowerLevel2Image.setVisible(true);
            selectedFloor.FLOOR.floor = "L2";
        });
        this.floor1.setOnAction(event -> {
            resetButtons();
            this.floor1.setStyle("-fx-background-color: BLUE");
            resetImages();
            floor1Group.setVisible(true);
            floor1Image.setVisible(true);
            selectedFloor.FLOOR.floor = "1";
        });
        this.floor2.setOnAction(event -> {
            resetButtons();
            this.floor2.setStyle("-fx-background-color: BLUE");
            resetImages();
            floor2Group.setVisible(true);
            floor2Image.setVisible(true);
            selectedFloor.FLOOR.floor = "2";
        });
        this.floor3.setOnAction(event -> {
            resetButtons();
            this.floor3.setStyle("-fx-background-color: BLUE");
            resetImages();
            floor3Group.setVisible(true);
            floor3Image.setVisible(true);
            selectedFloor.FLOOR.floor = "3";
        });
    }

    public Circle drawCircle(double x, double y, Color fill, Color stroke) {
        Circle circle = new Circle();
        circle.setVisible(true);
        circle.setFill(fill);
        circle.setStroke(stroke);
        circle.setStrokeWidth(3.0f);
        circle.setCenterX(x);
        circle.setCenterY(y);
        circle.setRadius(10.0);
        return circle;
    }

    public void editNodeColor(String nodetype ,Color fill, Color stroke) {
        Group thisGroup = new Group();
        switch (selectedFloor.FLOOR.floor) {
            case "L1":
                thisGroup = this.L1Group;
                break;
            case "L2":
                thisGroup = this.L2Group;
                break;
            case "1":
                thisGroup = this.floor1Group;
                break;
            case "2":
                thisGroup = this.floor2Group;
                break;
            case "3":
                thisGroup = this.floor3Group;
                break;
        }
        for(javafx.scene.Node node : thisGroup.getChildren()){
            if(node instanceof Circle)
            {
                ((Circle) node).setFill(fill);
                ((Circle) node).setStroke(stroke);
            }
        }

    }

    public void clearAllNodes() {
        floor1Group.getChildren().remove(1, floor1Group.getChildren().size());
        floor2Group.getChildren().remove(1, floor2Group.getChildren().size());
        floor3Group.getChildren().remove(1, floor3Group.getChildren().size());
        L1Group.getChildren().remove(1, L1Group.getChildren().size());
        L2Group.getChildren().remove(1, L2Group.getChildren().size());
    }

    public HashMap<Integer, Move> getMoveHashMap() throws SQLException {
        HashMap<Integer, Move> hash = new HashMap<>();
        for (Move move : allMoves) {
            hash.put(move.getNode().getNodeID(), move);
        }
        return hash;
    }



    public void findAllNodes() throws SQLException {
        getNodeHashMap().forEach(((key, value) -> {
            if (value.getFloor().equals(selectedFloor.FLOOR.floor)) {
                try {
                    if (getMoveHashMap().get(key) == null) {

                    } else {
                        Circle circle = drawCircle(value.getXcoord(), value.getYcoord(), Color.GRAY, Color.DARKGRAY);
                        switch (selectedFloor.FLOOR.floor) {
                            case "L1":
                                this.L1Group.getChildren().add(circle);
                                break;
                            case "L2":
                                this.L2Group.getChildren().add(circle);
                                break;
                            case "1":
                                this.floor1Group.getChildren().add(circle);
                                break;
                            case "2":
                                this.floor2Group.getChildren().add(circle);
                                break;
                            case "3":
                                this.floor3Group.getChildren().add(circle);
                                break;
                        }
                    }
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        }));
    }


}

