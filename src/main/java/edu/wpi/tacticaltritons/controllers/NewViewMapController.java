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
import org.controlsfx.tools.Platform;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;
import java.time.ZoneId;
import java.util.*;

public class NewViewMapController extends MapSuperController {

    @FXML
    private MFXButton filter;

    @FXML
    private MFXCheckbox bathrooms;

    @FXML
    private MFXCheckbox restrooms;
    @FXML
    private MFXCheckbox exits;
    @FXML
    private MFXCheckbox elevators;
    @FXML
    private MFXCheckbox stairs;
    @FXML
    private MFXCheckbox hallways;
    @FXML
    private MFXCheckbox departments;
    @FXML
    private MFXCheckbox labs;
    @FXML
    private MFXCheckbox services;
    @FXML
    private MFXCheckbox infoDesks;
    @FXML
    private MFXCheckbox conferenceRooms;
    @FXML
    private MFXCheckbox retail;

    @FXML
    private MFXCheckbox selectAll;

    @FXML
    private MFXButton applyFilter;

    @FXML
    private StackPane menuPane;

    @FXML
    private StackPane filterPane;

    public NewViewMapController() throws SQLException {
    }

    public void showFilters(Boolean bool) {
        filterPane.setVisible(bool);
    }

    public void selectFilters(Boolean bool) {
        restrooms.setSelected(bool);
        elevators.setSelected(bool);
        stairs.setSelected(bool);
        hallways.setSelected(bool);
        departments.setSelected(bool);
        labs.setSelected(bool);
        infoDesks.setSelected(bool);
        conferenceRooms.setSelected(bool);
        retail.setSelected(bool);
        services.setSelected(bool);
        exits.setSelected(bool);
        bathrooms.setSelected(bool);
    }
    public void selectNone() {

        selectNone(restrooms);
        selectNone(elevators);
        selectNone(stairs);
        selectNone(hallways);
        selectNone(departments);
        selectNone(labs);
        selectNone(infoDesks);
        selectNone(conferenceRooms);
        selectNone(retail);
        selectNone(services);
        selectNone(exits);
        selectNone(bathrooms);

    }

    public void selectNone(MFXCheckbox checkbox) {
        checkbox.setOnMouseClicked(event -> {
            if (!checkbox.isSelected()) {
                selectAll.setSelected(false);
            }
        });
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
            } else {
                menuPane.setVisible(false);
                componentShift(0);
            }
        });
        this.editMap.setOnAction(event -> {
            Navigation.navigate(Screen.EDIT_MAP);
        });
    }




    @FXML
    private void initialize() throws SQLException {

        selectedFloor.FLOOR.floor = "1";

        findAllNodes(allNodeTypes, selectedFloor.FLOOR.floor, "ViewMap");

        initializeGesturePane();
        initializeImages();
        initializeSearch("ViewMap");

        this.floor1Group.setVisible(true);
        this.floor1Image.setVisible(true);
        searchOnMap.toFront();
        searchOnMap.setVisible(true);


        showFilters(false);
        filter.setVisible(true);
        menuBar.setVisible(true);
        menuPane.setVisible(false);
        selectAll.setSelected(true);
        filterPane.setVisible(false);
        selectFilters(true);

        floor1.setStyle("-fx-background-color: BLUE");
        initalizeFloorButtons();

        this.pathfinding.setOnMouseClicked(event -> {
            Navigation.navigate(Screen.PATHFINDING);
        });

        this.editMap.setOnAction(event -> {
            Navigation.navigate(Screen.EDIT_MAP);
        });

        this.selectAll.setOnMouseClicked(event -> {
            if (selectAll.isSelected()) {
                selectFilters(true);
            } else {
                selectFilters(false);
            }
        });

        initializeMenuButton("ViewMap");

        this.applyFilter.setOnAction(event -> {
            clearAllCircles();
            List<String> nodeTypeList = new ArrayList<>();

            if (restrooms.isSelected() == true) {
                nodeTypeList.add("REST");
            }

            if (elevators.isSelected() == true) {
                nodeTypeList.add("ELEV");
            }

            if (stairs.isSelected() == true) {
                nodeTypeList.add("STAI");
            }

            if (hallways.isSelected() == true) {
                nodeTypeList.add("HALL");
            }

            if (departments.isSelected() == true) {
                nodeTypeList.add("DEPT");
            }

            if (labs.isSelected() == true) {
                nodeTypeList.add("LABS");
            }

            if (infoDesks.isSelected() == true) {
                nodeTypeList.add("INFO");
            }

            if (conferenceRooms.isSelected() == true) {
                nodeTypeList.add("CONF");
            }

            if (retail.isSelected() == true) {
                nodeTypeList.add("RETL");
            }

            if (services.isSelected() == true) {
                nodeTypeList.add("SERV");
            }

            if (exits.isSelected() == true) {
                nodeTypeList.add("EXIT");
            }

            if (bathrooms.isSelected() == true) {
                nodeTypeList.add("BATH");
            }

            try {
                findAllNodes(nodeTypeList, selectedFloor.FLOOR.floor, "ViewMap");
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });

        this.filter.setOnAction(event -> {
            if (!filterPane.isVisible()) {
                showFilters(true);
            } else {
                showFilters(false);
            }
        });

        this.searchOnMap.setOnAction(event -> {
            clearAllCircles();
            Circle circle = new Circle();
            final double[] circleCoord = new double[2];
            final String[] thisFloor = new String[1];

            try {
                getMoveHashMap().forEach((key, value) -> {
                    if(value.getLocation().getLongName().equals(this.searchOnMap.getSelectedItem()))
                    {
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
                findAllNodes(blank, thisFloor[0], "ViewMap");
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

            circle = drawCircle( circleCoord[0],  circleCoord[1], Color.PINK, Color.RED);

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


    }

}
