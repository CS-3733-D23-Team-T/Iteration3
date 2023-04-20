package edu.wpi.tacticaltritons.controllers;

import edu.wpi.tacticaltritons.App;
import edu.wpi.tacticaltritons.database.*;
import edu.wpi.tacticaltritons.database.Move;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXComboBox;
import io.github.palexdev.materialfx.controls.MFXFilterComboBox;
import io.github.palexdev.materialfx.controls.MFXTextField;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Polyline;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.FileChooser;
import javafx.stage.Popup;
import javafx.stage.PopupWindow;
import javafx.stage.Stage;
import net.kurobako.gesturefx.*;
import javafx.geometry.*;

import javax.xml.stream.Location;


public class EditMapController {

    @FXML
    private MFXFilterComboBox<String> startLocation = new MFXFilterComboBox<>();

    @FXML private StackPane selectedEditPane;

    @FXML
    private MFXFilterComboBox<String> endLocation = new MFXFilterComboBox<>();

    @FXML
    private MFXButton applyAndSave;

    @FXML
    private MFXComboBox<String> comboBox;

    @FXML
    private MFXComboBox<String> rooms;

    @FXML
    private MFXButton preview;

    @FXML
    private MFXTextField searchOnMap;

    @FXML
    private GesturePane groundFloor;

    @FXML
    private ImageView groundFloorImage;

    @FXML
    private ImageView lowerLevel1Image;

    @FXML
    private ImageView lowerLevel2Image;

    @FXML
    private ImageView floor1Image;

    @FXML
    private ImageView floor2Image;

    @FXML
    private ImageView floor3Image;

    @FXML
    private Polyline path;

    @FXML
    private MFXButton viewNodes;

    @FXML
    private StackPane selectedFloorPane;

    @FXML
    private GesturePane circlePane;
    @FXML
    private Group groundGroup;
    @FXML
    private Group L1Group;
    @FXML
    private Group L2Group;
    @FXML
    private Group floor1Group;
    @FXML
    private Group floor2Group;
    @FXML
    private Group floor3Group;

    @FXML
    private MFXButton removeNodes;

    @FXML private Group editGroup1;
    @FXML private Group editGroup2;
    @FXML private Group editGroup3;

    MFXFilterComboBox<String> NodeIDComboBox = new MFXFilterComboBox<>();
    MFXFilterComboBox<String> xCoordComboBox = new MFXFilterComboBox<>();
    MFXFilterComboBox<String> yCoordComboBox = new MFXFilterComboBox<>();
    MFXFilterComboBox<String> floorComboBox = new MFXFilterComboBox<>();
    MFXFilterComboBox<String> buildingComboBox = new MFXFilterComboBox<>();
    MFXFilterComboBox<String> longNamesComboBox = new MFXFilterComboBox<>();
    MFXFilterComboBox<String> shortNameComboBox = new MFXFilterComboBox<>();
    MFXFilterComboBox<String> nodeTypeComboBox = new MFXFilterComboBox<>();


    @FXML
    private void initialize() throws SQLException {
        lowerLevel1Image.setImage(App.lowerlevel1);
        lowerLevel2Image.setImage(App.lowerlevel2);
        groundFloorImage.setImage(App.groundfloor);
        floor1Image.setImage(App.firstfloor);
        floor2Image.setImage(App.secondfloor);
        floor3Image.setImage(App.thirdfloor);


        Date today = new Date(2023, 4, 10);

        DAOFacade datas = new DAOFacade();

        List<Integer> xCoord = new ArrayList<>();
        List<Integer> yCoord = new ArrayList<>();
        List<Double> startEnd = new ArrayList<Double>();
        List<Integer> nodeIDs = new ArrayList<Integer>();
        List<Move> allMoves = DAOFacade.getAllMoves();

        HashMap<Integer, Move> hash = new HashMap<>();
        for (Move move : allMoves) {
            hash.put(move.getNode().getNodeID(), move);
        }

        this.removeNodes.setOnAction(event -> {
            clearAllNodes();
        });


        this.viewNodes.setOnAction(event -> {
            clearAllNodes();
            try {
                for (Node node : DAOFacade.getAllNodes()) {
                    if (node.getFloor().equals(this.comboBox.getText().substring(0, this.comboBox.getText().indexOf(" ")))) {
                        xCoord.add(node.getXcoord());
                        yCoord.add(node.getYcoord());
                        nodeIDs.add(node.getNodeID());
                    }
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }


            for (int coordinate = 0; coordinate < xCoord.size() - 1; coordinate++) {
                Circle circle = drawCircle(xCoord.get(coordinate), yCoord.get(coordinate));
                Text text = new Text("");
                text.setMouseTransparent(true);
                text.setFill(Color.WHITE);
                text.setStroke(Color.MEDIUMBLUE);
                text.setFont(Font.font("Ariel", FontWeight.BOLD, 17));
                text.toFront();
                LocationName location = hash.get(nodeIDs.get(coordinate)).getLocation();
                if (!location.getNodeType().equals("HALL")) {
                    text.setText(hash.get(nodeIDs.get(coordinate)).getLocation().getShortName());
                }
                text.setX(xCoord.get(coordinate) - (text.getLayoutBounds().getWidth() / 2));
                text.setY(yCoord.get(coordinate) + (circle.getRadius() * 2));

                /*circle.setOnMouseClicked(event1 -> {
                    clearAllNodes();
                    Circle newCircle = drawCircle(circle.getCenterX(), circle.getCenterY());
                    newCircle.setFill(Color.PINK);
                    newCircle.setStroke(Color.RED);
                    newCircle.setVisible(true);

                    switch (this.comboBox.getText()) {
                        case "Ground Floor":
                            this.groundGroup.getChildren().add(newCircle);
                            break;
                        case "L1 - Lower Level 1":
                            this.L1Group.getChildren().add(newCircle);
                            break;
                        case "L2 - Lower Level 2":
                            this.L2Group.getChildren().add(newCircle);
                            break;
                        case "1 - 1st Floor":
                            this.floor1Group.getChildren().add(newCircle);
                            break;
                        case "2 - 2nd Floor":
                            this.floor2Group.getChildren().add(newCircle);
                            break;
                        case "3 - 3rd Floor":
                            this.floor3Group.getChildren().add(newCircle);
                            break;
                    }

                    try {
                        for (Move move : DAOFacade.getAllMoves()) {
                            if (move.getNode().getXcoord() == circle.getCenterX() && move.getNode().getYcoord() == circle.getCenterY()) {
                                this.startLocation.setText(move.getLocation().getLongName());
                                textLocation.setFill(Color.WHITE);
                                textLocation.setStroke(Color.MEDIUMBLUE);
                                textLocation.setFont(Font.font("Ariel", FontWeight.BOLD, 17));
                                textLocation.toFront();
                                textLocation.setText(move.getLocation().getShortName());
                                textLocation.setX(move.getNode().getXcoord() - (textLocation.getLayoutBounds().getWidth() / 2));
                                textLocation.setY(move.getNode().getYcoord() + (10 * 2));
                                switch (this.comboBox.getText()) {
                                    case "Ground Floor":
                                        this.groundGroup.getChildren().add(textLocation);
                                        break;
                                    case "L1 - Lower Level 1":
                                        this.L1Group.getChildren().add(textLocation);
                                        break;
                                    case "L2 - Lower Level 2":
                                        this.L2Group.getChildren().add(textLocation);
                                        break;
                                    case "1 - 1st Floor":
                                        this.floor1Group.getChildren().add(textLocation);
                                        break;
                                    case "2 - 2nd Floor":
                                        this.floor2Group.getChildren().add(textLocation);
                                        break;
                                    case "3 - 3rd Floor":
                                        this.floor3Group.getChildren().add(textLocation);
                                        break;
                                }
                            }
                        }
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }



                });*/

                switch (this.comboBox.getText()) {
                    case "Ground Floor":
                        this.groundGroup.getChildren().addAll(circle, text);
                        break;
                    case "L1 - Lower Level 1":
                        this.L1Group.getChildren().addAll(circle, text);
                        break;
                    case "L2 - Lower Level 2":
                        this.L2Group.getChildren().addAll(circle, text);
                        break;
                    case "1 - 1st Floor":
                        this.floor1Group.getChildren().addAll(circle, text);
                        break;
                    case "2 - 2nd Floor":
                        this.floor2Group.getChildren().addAll(circle, text);
                        break;
                    case "3 - 3rd Floor":
                        this.floor3Group.getChildren().addAll(circle, text);
                        break;
                }

                int finalCoordinate = coordinate;
                circle.setOnMousePressed(event1 -> {
                    groundFloor.setGestureEnabled(false);
                    text.setVisible(false);

                    double offsetX = event1.getSceneX() - circle.getCenterX();
                    double offsetY = event1.getSceneY() - circle.getCenterY();
                    circle.setUserData(new double[]{offsetX, offsetY});

                    NodeIDComboBox.setText(Integer.toString(nodeIDs.get(finalCoordinate)));
                    xCoordComboBox.setText(Integer.toString(hash.get(nodeIDs.get(finalCoordinate)).getNode().getXcoord()));
                    yCoordComboBox.setText(Integer.toString(hash.get(nodeIDs.get(finalCoordinate)).getNode().getYcoord()));
                    floorComboBox.setText(hash.get(nodeIDs.get(finalCoordinate)).getNode().getFloor());
                    buildingComboBox.setText(hash.get(nodeIDs.get(finalCoordinate)).getNode().getBuilding());

                    longNamesComboBox.setText(hash.get(nodeIDs.get(finalCoordinate)).getLocation().getLongName());
                    shortNameComboBox.setText(hash.get(nodeIDs.get(finalCoordinate)).getLocation().getShortName());
                    nodeTypeComboBox.setText(hash.get(nodeIDs.get(finalCoordinate)).getLocation().getNodeType());

                });
                circle.setOnMouseDragged(event1 -> {
                    double offsetX = ((double[])circle.getUserData())[0];
                    double offsetY = ((double[])circle.getUserData())[1];
                    circle.setCenterX(event1.getSceneX() - offsetX);
                    circle.setCenterY(event1.getSceneY() - offsetY);
                    text.setX(circle.getCenterX() - (text.getLayoutBounds().getWidth() / 2));
                    text.setY(circle.getCenterY() + (circle.getRadius() * 2));
                    xCoordComboBox.setText(String.valueOf((int)circle.getCenterX()));
                    yCoordComboBox.setText(String.valueOf((int)circle.getCenterY()));
                });
                circle.setOnMouseReleased(event1 -> {
                    groundFloor.setGestureEnabled(true);

                    text.setVisible(true);
                    circle.setUserData(null);
                });
                /*groundFloor.addEventFilter(MouseEvent.MOUSE_DRAGGED, event2 -> {
                    if (event2.getTarget() == circle && event2.getButton() == MouseButton.PRIMARY) {
                        event2.consume();
                    }
                });*/
            }

            xCoord.clear();
            yCoord.clear();

        });


        this.comboBox.setItems(
                FXCollections.observableArrayList(
                        "Ground Floor",
                        "L1 - Lower Level 1",
                        "L2 - Lower Level 2",
                        "1 - 1st Floor",
                        "2 - 2nd Floor",
                        "3 - 3rd Floor"));

        this.comboBox.getSelectionModel().selectItem("1 - 1st Floor");

        this.rooms.setItems(FXCollections.observableArrayList("Node", "Location Name","Import/Export"));
        this.rooms.getSelectionModel().selectFirst();
        editGroup1.setVisible(true);

        this.rooms
                .getSelectionModel()
                .selectedItemProperty()
                .addListener(
                        new ChangeListener<String>() {
                            @Override
                            public void changed(
                                    ObservableValue<? extends String> selected, String oldSelection, String newSelection) {
                                if (oldSelection != null) {
                                    switch (oldSelection) {
                                        case "Node":
                                            editGroup1.setVisible(false);
                                            break;
                                        case "Location Name":
                                            editGroup2.setVisible(false);
                                            break;
                                        case "Import/Export":
                                            editGroup3.setVisible(false);
                                            break;
                                    }
                                }
                                if (newSelection != null) {
                                    switch (newSelection) {
                                        case "Node":
                                            editGroup1.setVisible(true);
                                            break;
                                        case "Location Name":
                                            editGroup2.setVisible(true);
                                            break;
                                        case "Import/Export":
                                            editGroup3.setVisible(true);
                                            break;
                                    }
                                }
                            }
                        });

        this.rooms.valueProperty().addListener((obs, o, n) -> {
            switch (n){
                case "Node":
                    try {
                        showNodeEdit();
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                    break;
                case "Location Name":
                    try {
                        showLocationNameEdit();
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                    break;
                case "Import/Export":
                    try {
                        showImportExportEdit();
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                    break;
            }
        });

        for (LocationName name : DAOFacade.getAllLocationNames()) {
            startLocation.getItems().add(name.getLongName());
        }

        for (LocationName name : DAOFacade.getAllLocationNames()) {
            endLocation.getItems().add(name.getLongName());
        }

        this.groundFloor.setVisible(true);
        this.floor1Group.setVisible(true);
        this.floor1Image.setVisible(true);

        this.preview.setOnAction(event -> {
            clearAllNodes();
            Circle circle = new Circle();

            try {
                circle = drawCircle(DAOFacade.getNode(this.startLocation.getText(), today).getXcoord(), DAOFacade.getNode(this.startLocation.getText(), today).getYcoord());
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

            String endFloor = null;
            try {
                endFloor = DAOFacade.getNode(this.startLocation.getText(), today).getFloor();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

            if (endFloor != null) {
                switch (endFloor) {
                    case "L1":
                        this.comboBox.getSelectionModel().selectItem("L1 - Lower Level 1");
                        L1Group.setVisible(true);
                        lowerLevel1Image.setVisible(true);
                        this.L1Group.getChildren().add(circle);
                        break;
                    case "L2":
                        this.comboBox.getSelectionModel().selectItem("L2 - Lower Level 2");
                        L2Group.setVisible(true);
                        lowerLevel2Image.setVisible(true);
                        this.L2Group.getChildren().add(circle);
                        break;
                    case "1":
                        this.comboBox.getSelectionModel().selectItem("1 - 1st Floor");
                        floor1Group.setVisible(true);
                        floor1Image.setVisible(true);
                        this.floor1Group.getChildren().add(circle);
                        break;
                    case "2":
                        this.comboBox.getSelectionModel().selectItem("2 - 2nd Floor");
                        floor2Group.setVisible(true);
                        floor2Image.setVisible(true);
                        this.floor2Group.getChildren().add(circle);
                        break;
                    case "3":
                        this.comboBox.getSelectionModel().selectItem("3 - 3rd Floor");
                        floor3Group.setVisible(true);
                        floor3Image.setVisible(true);
                        this.floor3Group.getChildren().add(circle);
                        break;
                }
            }
            Point2D centerpoint = new Point2D(circle.getCenterX(), circle.getCenterY());
            groundFloor.centreOn(centerpoint);

        });

        this.comboBox
                .getSelectionModel()
                .selectedItemProperty()
                .addListener(
                        new ChangeListener<String>() {
                            @Override
                            public void changed(
                                    ObservableValue<? extends String> selected, String oldFloor, String newFloor) {
                                if (oldFloor != null) {
                                    switch (oldFloor) {
                                        case "Ground Floor":
                                            groundGroup.setVisible(false);
                                            groundFloorImage.setVisible(false);
                                            break;
                                        case "L1 - Lower Level 1":
                                            L1Group.setVisible(false);
                                            lowerLevel1Image.setVisible(false);
                                            break;
                                        case "L2 - Lower Level 2":
                                            L2Group.setVisible(false);
                                            lowerLevel2Image.setVisible(false);
                                            break;
                                        case "1 - 1st Floor":
                                            floor1Group.setVisible(false);
                                            floor1Image.setVisible(false);
                                            break;
                                        case "2 - 2nd Floor":
                                            floor2Group.setVisible(false);
                                            floor2Image.setVisible(false);
                                            break;
                                        case "3 - 3rd Floor":
                                            floor3Group.setVisible(false);
                                            floor3Image.setVisible(false);
                                            break;
                                    }
                                }
                                if (newFloor != null) {

                                    switch (newFloor) {
                                        case "Ground Floor":
                                            groundGroup.setVisible(true);
                                            groundFloorImage.setVisible(true);
                                            break;
                                        case "L1 - Lower Level 1":
                                            L1Group.setVisible(true);
                                            lowerLevel1Image.setVisible(true);
                                            break;
                                        case "L2 - Lower Level 2":
                                            L2Group.setVisible(true);
                                            lowerLevel2Image.setVisible(true);
                                            break;
                                        case "1 - 1st Floor":
                                            floor1Group.setVisible(true);
                                            floor1Image.setVisible(true);
                                            break;
                                        case "2 - 2nd Floor":
                                            floor2Group.setVisible(true);
                                            floor2Image.setVisible(true);
                                            break;
                                        case "3 - 3rd Floor":
                                            floor3Group.setVisible(true);
                                            floor3Image.setVisible(true);
                                            break;
                                    }
                                }
                            }
                        });
    }

    public Circle drawCircle(double x, double y) {
        Circle circle = new Circle();
        circle.setVisible(true);
        circle.setFill(Color.RED);
        circle.setStroke(Color.BLACK);
        circle.setStrokeWidth(3.0f);
        circle.setCenterX(x);
        circle.setCenterY(y);
        circle.setRadius(10.0);
        return circle;
    }

    public void pressFilter(ActionEvent actionEvent) {
    }

    public void pressBackToHome(ActionEvent actionEvent) {
    }

    public void getComboBoxInfo(ActionEvent actionEvent) {
    }

    public void printSearch(ActionEvent actionEvent) {
    }

    public void clearAllNodes() {
        floor1Group.getChildren().remove(1, floor1Group.getChildren().size());
        floor2Group.getChildren().remove(1, floor2Group.getChildren().size());
        floor3Group.getChildren().remove(1, floor3Group.getChildren().size());
        L1Group.getChildren().remove(1, L1Group.getChildren().size());
        L2Group.getChildren().remove(1, L2Group.getChildren().size());
    }

    public void showNodeEdit() throws SQLException {


        for (Node node : DAOFacade.getAllNodes()) {
            NodeIDComboBox.getItems().add(Integer.toString(node.getNodeID()));
            xCoordComboBox.getItems().add(Integer.toString(node.getXcoord()));
            yCoordComboBox.getItems().add(Integer.toString(node.getYcoord()));
        }

        floorComboBox.getItems().addAll("L1","L2", "1","2","3");
        buildingComboBox.getItems().addAll("15 Francis","45 Francis", "BTM", "Shapiro", "Tower");

        Text NodeIDtext = new Text();
        NodeIDtext.setText("Node ID:");
        HBox NodeIDBox = new HBox(NodeIDtext,NodeIDComboBox);
        NodeIDBox.setAlignment(Pos.CENTER);
        NodeIDBox.setSpacing(30.0);
        NodeIDComboBox.setFloatingText("Node ID");
        NodeIDComboBox.setAllowEdit(true);

        Text xCoordText = new Text();
        xCoordText.setText("X-Coordinate:");
        HBox xCoordBox = new HBox(xCoordText,xCoordComboBox);
        xCoordBox.setAlignment(Pos.CENTER);
        xCoordBox.setSpacing(30.0);
        xCoordComboBox.setFloatingText("X Coordinate");
        xCoordComboBox.setAllowEdit(true);

        Text yCoordText = new Text();
        yCoordText.setText("Y-Coordinate:");
        HBox yCoordBox = new HBox(yCoordText,yCoordComboBox);
        yCoordBox.setAlignment(Pos.CENTER);
        yCoordBox.setSpacing(30.0);
        yCoordComboBox.setFloatingText("Y Coordinate");
        yCoordComboBox.setAllowEdit(true);

        Text floorText = new Text();
        floorText.setText("Floor:");
        HBox floorBox = new HBox(floorText,floorComboBox);
        floorBox.setAlignment(Pos.CENTER);
        floorBox.setSpacing(30.0);
        floorComboBox.setFloatingText("Floor");
        floorComboBox.setAllowEdit(true);

        Text buildingText = new Text();
        buildingText.setText("Building:");
        HBox buildingBox = new HBox(buildingText,buildingComboBox);
        buildingBox.setAlignment(Pos.CENTER);
        buildingBox.setSpacing(30.0);
        buildingComboBox.setFloatingText("Building");
        buildingComboBox.setAllowEdit(true);

        MFXButton add = new MFXButton();
        add.setText("ADD");
        add.setStyle("-fx-background-color: #002d59; -fx-padding: 7; -fx-background-radius: 15;");
        add.setFont(Font.font("Arial Rounded MT Bold",12.0));
        add.setTextFill(Color.WHITE);
        add.setPrefHeight(33.0);
        add.setPrefWidth(133.0);

        add.setOnAction(event -> {
            Node node = new Node(Integer.parseInt(NodeIDComboBox.getText()),Integer.parseInt(xCoordComboBox.getText()) , Integer.parseInt(yCoordComboBox.getText()),floorComboBox.getText(),buildingComboBox.getText());
            try {
                DAOFacade.addNode(node);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });

        MFXButton modify = new MFXButton();
        modify.setText("MODIFY");
        modify.setStyle("-fx-background-color: #002d59; -fx-padding: 7; -fx-background-radius: 15;");
        modify.setFont(Font.font("Arial Rounded MT Bold",12.0));
        modify.setTextFill(Color.WHITE);
        modify.setPrefHeight(33.0);
        modify.setPrefWidth(133.0);

        modify.setOnAction(event -> {
            String xCoordinate;
            String yCoordinate;
            String floor;
            String building;

            if(xCoordComboBox.getText() == "")
            {
                try {
                    xCoordinate = Integer.toString(DAOFacade.getNode(Integer.parseInt(NodeIDComboBox.getText())).getXcoord());
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
            else {
                xCoordinate = (xCoordComboBox.getText());
            }

            if(yCoordComboBox.getText() == "")
            {
                try {
                    yCoordinate = Integer.toString(DAOFacade.getNode(Integer.parseInt(NodeIDComboBox.getText())).getYcoord());
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
            else {
                yCoordinate = (yCoordComboBox.getText());
            }

            if(floorComboBox.getText() == "")
            {
                try {
                    floor = (DAOFacade.getNode(Integer.parseInt(NodeIDComboBox.getText())).getFloor());
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
            else {
                floor = (floorComboBox.getText());
            }

            if(buildingComboBox.getText() == "")
            {
                try {
                    building = (DAOFacade.getNode(Integer.parseInt(NodeIDComboBox.getText())).getBuilding());
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
            else {
                building = (buildingComboBox.getText());
            }

            Node node = new Node(Integer.parseInt(NodeIDComboBox.getText()),Integer.parseInt(xCoordinate) , Integer.parseInt(yCoordinate), floor, building);
            try {
                DAOFacade.updateNode(node);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });

        MFXButton remove = new MFXButton();
        remove.setText("REMOVE");
        remove.setStyle("-fx-background-color: #002d59; -fx-padding: 7; -fx-background-radius: 15;");
        remove.setFont(Font.font("Arial Rounded MT Bold",12.0));
        remove.setTextFill(Color.WHITE);
        remove.setPrefHeight(33.0);
        remove.setPrefWidth(133.0);

        remove.setOnAction(event -> {
            Node node = new Node(Integer.parseInt(NodeIDComboBox.getText()),Integer.parseInt(xCoordComboBox.getText()) , Integer.parseInt(yCoordComboBox.getText()),floorComboBox.getText(),buildingComboBox.getText());
            try {
                DAOFacade.deleteNode(node);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });

        HBox buttons = new HBox(add,modify,remove);
        buttons.setSpacing(20.0);

        VBox nodeEdit = new VBox(NodeIDBox,xCoordBox,yCoordBox,floorBox,buildingBox, buttons);
        nodeEdit.setAlignment(Pos.TOP_CENTER);
        nodeEdit.setSpacing(10.0);

        editGroup1.getChildren().add(nodeEdit);
    }

    public void showLocationNameEdit() throws SQLException {

        for (LocationName name : DAOFacade.getAllLocationNames()) {
            longNamesComboBox.getItems().add(name.getLongName());
            shortNameComboBox.getItems().add(name.getShortName());
        }

        nodeTypeComboBox.getItems().addAll("BATH","CONF", "DEPT","ELEV","EXIT","HALL","INFO","LABS","REST","RETL","SERV","STAI");

        Text longNameText = new Text();
        longNameText.setText("Long Name:");
        HBox longNameBox = new HBox(longNameText,longNamesComboBox);
        longNameBox.setAlignment(Pos.CENTER);
        longNameBox.setSpacing(30.0);
        longNamesComboBox.setFloatingText("Long Name");
        longNamesComboBox.setAllowEdit(true);

        Text shortNameText = new Text();
        shortNameText.setText("Short Name:");
        HBox shortNameBox = new HBox(shortNameText,shortNameComboBox);
        shortNameBox.setAlignment(Pos.CENTER);
        shortNameBox.setSpacing(30.0);
        shortNameComboBox.setFloatingText("Short Name");
        shortNameComboBox.setAllowEdit(true);

        Text nodeTypeText = new Text();
        nodeTypeText.setText("Node Type:");
        HBox nodeTypeBox = new HBox(nodeTypeText,nodeTypeComboBox);
        nodeTypeBox.setAlignment(Pos.CENTER);
        nodeTypeBox.setSpacing(30.0);
        nodeTypeComboBox.setFloatingText("Node Type");
        nodeTypeComboBox.setAllowEdit(true);


        MFXButton add = new MFXButton();
        add.setText("ADD");
        add.setStyle("-fx-background-color: #002d59; -fx-padding: 7; -fx-background-radius: 15;");
        add.setFont(Font.font("Arial Rounded MT Bold",12.0));
        add.setTextFill(Color.WHITE);
        add.setPrefHeight(33.0);
        add.setPrefWidth(133.0);

        add.setOnAction(event -> {
            LocationName name = new LocationName(longNamesComboBox.getText(),shortNameComboBox.getText(),nodeTypeComboBox.getText());
            try {
                DAOFacade.addLocationName(name);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });

        MFXButton modify = new MFXButton();
        modify.setText("MODIFY");
        modify.setStyle("-fx-background-color: #002d59; -fx-padding: 7; -fx-background-radius: 15;");
        modify.setFont(Font.font("Arial Rounded MT Bold",12.0));
        modify.setTextFill(Color.WHITE);
        modify.setPrefHeight(33.0);
        modify.setPrefWidth(133.0);

        modify.setOnAction(event -> {
            String shortName;
            String nodeType;

            if(shortNameComboBox.getText() == "")
            {
                try {
                    shortName = DAOFacade.getLocationName(longNamesComboBox.getText()).getShortName();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
            else {
                shortName = (shortNameComboBox.getText());
            }

            if(nodeTypeComboBox.getText() == "")
            {
                try {
                    nodeType = DAOFacade.getLocationName(nodeTypeComboBox.getText()).getNodeType();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
            else {
                nodeType = (nodeTypeComboBox.getText());
            }

            LocationName name = new LocationName(longNamesComboBox.getText(),shortName,nodeType);
            try {
                DAOFacade.updateLocationName(name);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });

        MFXButton remove = new MFXButton();
        remove.setText("REMOVE");
        remove.setStyle("-fx-background-color: #002d59; -fx-padding: 7; -fx-background-radius: 15;");
        remove.setFont(Font.font("Arial Rounded MT Bold",12.0));
        remove.setTextFill(Color.WHITE);
        remove.setPrefHeight(33.0);
        remove.setPrefWidth(133.0);

        remove.setOnAction(event -> {
            LocationName name = new LocationName(longNamesComboBox.getText(),shortNameComboBox.getText(),nodeTypeComboBox.getText());
            try {
                DAOFacade.deleteLocationName(name);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });

        HBox buttons = new HBox(add,modify,remove);
        buttons.setSpacing(20.0);

        VBox nodeEdit = new VBox(longNameBox,shortNameBox,nodeTypeBox, buttons);
        nodeEdit.setAlignment(Pos.TOP_CENTER);
        nodeEdit.setSpacing(10.0);

        editGroup2.getChildren().add(nodeEdit);
    }

    public void showImportExportEdit() throws SQLException {

        MFXButton importButton = new MFXButton();
        importButton.setText("IMPORT");
        importButton.setStyle("-fx-background-color: #002d59; -fx-padding: 7; -fx-background-radius: 15;");
        importButton.setFont(Font.font("Arial Rounded MT Bold",12.0));
        importButton.setTextFill(Color.WHITE);
        importButton.setPrefHeight(33.0);
        importButton.setPrefWidth(133.0);

        importButton.setOnAction(
                event -> {
                    Stage outStage = new Stage();
                    FileChooser fileChooser = new FileChooser();
                    File file = fileChooser.showOpenDialog(outStage);
                    try {
                        Import.importFile(file);
                    } catch (IOException | SQLException | ParseException e) {
                        throw new RuntimeException(e);
                    }
                });

        MFXComboBox<String> selector = new MFXComboBox<>();

        selector.setItems(FXCollections.observableArrayList("Node", "Edge", "Location Name", "Move"));

        selector.setPrefWidth(100.0);

        selector.getSelectionModel().selectFirst();

        MFXButton exportButton = new MFXButton();
        exportButton.setText("EXPORT");
        exportButton.setStyle("-fx-background-color: #002d59; -fx-padding: 7; -fx-background-radius: 15;");
        exportButton.setFont(Font.font("Arial Rounded MT Bold",12.0));
        exportButton.setTextFill(Color.WHITE);
        exportButton.setPrefHeight(33.0);
        exportButton.setPrefWidth(133.0);

        exportButton.setOnAction(event -> {
            String tableName = null;
            if (selector.getValue().equals("Node")) {
                tableName = "node";
            } else if (selector.getValue().equals("Edge")) {
                tableName = "edge";
            } else if (selector.getValue().equals("Location Name")) {
                tableName = "locationname";
            } else if (selector.getValue().equals("Move")) {
                tableName = "move";
            }
            Stage exportStage = new Stage();
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Export");
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV", "*.csv"));

            File file = fileChooser.showSaveDialog(exportStage);
            DAOFacade data = new DAOFacade();
            try {
                Export.export(file, tableName);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });

        HBox importButtonBox = new HBox(importButton);

        HBox selectorBox = new HBox(selector);

        HBox exportButtonBox = new HBox(exportButton);


        VBox nodeEdit = new VBox(importButtonBox, selectorBox ,exportButtonBox);
        nodeEdit.setAlignment(Pos.TOP_CENTER);
        nodeEdit.setSpacing(10.0);

        editGroup3.getChildren().add(nodeEdit);
    }
}
