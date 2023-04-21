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
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

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



    @FXML
    private void initialize() throws SQLException {
        javafx.application.Platform.runLater(() -> {
            gesturePane.centreOn(new Point2D(2500,1000));
        });

        initializeImages();
        initializeSearch();

        this.gesturePane.setVisible(true);
        this.floor1Group.setVisible(true);
        this.floor1Image.setVisible(true);
        searchOnMap.toFront();
        searchOnMap.setVisible(true);
        gesturePane.toBack();
        gesturePane.reset();

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

        initializeMenuButton();

        this.applyFilter.setOnAction(event -> {
            clearAllNodes();

            try {
                findAllNodes();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
//
//            if (restrooms.isSelected() == true) {
//                findNodes("REST");
//            }
//
//            if (elevators.isSelected() == true) {
//                findNodes("ELEV");
//            }
//
//            if (stairs.isSelected() == true) {
//                findNodes("STAI");
//            }
//
//            if (hallways.isSelected() == true) {
//                findNodes("HALL");
//            }
//
//            if (departments.isSelected() == true) {
//                findNodes("DEPT");
//            }
//
//            if (labs.isSelected() == true) {
//                findNodes("LABS");
//            }
//
//            if (infoDesks.isSelected() == true) {
//                findNodes("INFO");
//            }
//
//            if (conferenceRooms.isSelected() == true) {
//                findNodes("CONF");
//            }
//
//            if (retail.isSelected() == true) {
//                findNodes("RETL");
//            }
//
//            if (services.isSelected() == true) {
//                findNodes("SERV");
//            }
//
//            if (exits.isSelected() == true) {
//                findNodes("EXIT");
//            }
//
//            if (bathrooms.isSelected() == true) {
//                findNodes("BATH");
//            }
        });

        this.filter.setOnAction(event -> {
            if (!filterPane.isVisible()) {
                showFilters(true);
            } else {
                showFilters(false);
            }
        });

        this.searchOnMap.setOnAction(event -> {

            clearAllNodes();
            Circle circle = new Circle();

            try {
                circle = drawCircle(DAOFacade.getNode(this.searchOnMap.getSelectedItem(), today).getXcoord(), DAOFacade.getNode(this.searchOnMap.getSelectedItem(), today).getYcoord(), Color.RED, Color.BLACK);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

            String endFloor = null;
            try {
                endFloor = DAOFacade.getNode(this.searchOnMap.getSelectedItem(), today).getFloor();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

            if (endFloor != null) {
                switch (endFloor) {
                    case "L1":

                        L1Group.setVisible(true);
                        lowerLevel1Image.setVisible(true);
                        this.L1Group.getChildren().add(circle);
                        break;
                    case "L2":

                        L2Group.setVisible(true);
                        lowerLevel2Image.setVisible(true);
                        this.L2Group.getChildren().add(circle);
                        break;
                    case "1":

                        floor1Group.setVisible(true);
                        floor1Image.setVisible(true);
                        this.floor1Group.getChildren().add(circle);
                        break;
                    case "2":

                        floor2Group.setVisible(true);
                        floor2Image.setVisible(true);
                        this.floor2Group.getChildren().add(circle);
                        break;
                    case "3":

                        floor3Group.setVisible(true);
                        floor3Image.setVisible(true);
                        this.floor3Group.getChildren().add(circle);
                        break;
                }
            }
            Point2D centrePoint = new Point2D(circle.getCenterX(), circle.getCenterY());
            gesturePane.centreOn(centrePoint);
        });


    }

}
