package edu.wpi.tacticaltritons.controllers.database;

import edu.wpi.tacticaltritons.database.*;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXFilterComboBox;
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.beans.binding.Bindings;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;

import java.sql.SQLException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class EditDatabaseController {
    private enum Tables {Edges, Locations, Moves, Nodes, Login, Sessions}

    @FXML private FlowPane setTablePane;
    @FXML private MFXButton updateEntryButton;
    @FXML private FlowPane tableInsert;
    @FXML private FlowPane content;
    @FXML private MFXFilterComboBox<String> tableCombobox;

    @FXML
    private void initialize() throws SQLException {
        tableCombobox.getItems().add(Tables.Nodes.name());
        tableCombobox.getItems().add(Tables.Edges.name());
        tableCombobox.getItems().add(Tables.Locations.name());
        tableCombobox.getItems().add(Tables.Moves.name());
        tableCombobox.getItems().add(Tables.Login.name());
        tableCombobox.getItems().add(Tables.Sessions.name());


        tableCombobox.valueProperty().addListener((obs, o, n) -> {
            switch (n){
                case "Nodes" -> {
                    try {
                        updateContent(Tables.Nodes);
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
                case "Edges" -> {
                    try {
                        updateContent(Tables.Edges);
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
                case "Locations" -> {
                    try {
                        updateContent(Tables.Locations);
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
                case "Moves" -> {
                    try {
                        updateContent(Tables.Moves);
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
                case "Login" -> {
                    try {
                        updateContent(Tables.Login);
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
                case "Sessions" -> {
                    try {
                        updateContent(Tables.Sessions);
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        tableCombobox.setValue(Tables.Edges.name());
    }

    private MFXTextField addFieldEdit(String prompt){
        MFXTextField field = new MFXTextField();
        field.setPromptText(prompt);
        field.setPrefWidth(150);
        FlowPane.setMargin(field, new Insets(10,0,10,0));

        return field;
    }

    private void updateContent(Tables table) throws SQLException {
        tableInsert.getChildren().clear();
        switch (table){
            case Edges -> {
                TableView<Edge> tableView = new TableView<>();
                tableView.setPrefWidth(600);
                TableColumn<Edge, String> startNodeCol = new TableColumn<>("Start Node");
                startNodeCol.setCellValueFactory(data ->
                        new ReadOnlyStringWrapper(String.valueOf(data.getValue().getStartNode().getNodeID())));

                TableColumn<Edge, String> endNodeCol = new TableColumn<>("End Node");
                endNodeCol.setCellValueFactory(data ->
                        new ReadOnlyStringWrapper(String.valueOf(data.getValue().getEndNode().getNodeID())));

                tableView.getColumns().add(startNodeCol);
                tableView.getColumns().add(endNodeCol);

                content.getChildren().clear();
                content.getChildren().add(setTablePane);

                ObservableList<Edge> edges = FXCollections.observableArrayList(DAOFacade.getAllEdges());
                MFXTextField startNodeField = addFieldEdit("Start Node");
                MFXTextField endNodeField = addFieldEdit("End Node");

                final Edge[] observableEdge = new Edge[1];

                tableView.getSelectionModel().selectedItemProperty().addListener((obs, o, n) -> {
                    if(n != null){
                        startNodeField.setText(String.valueOf(n.getStartNode().getNodeID()));
                        startNodeField.textProperty().addListener((obs1, o1, n1) -> {
                            n.getStartNode().setNodeID(Integer.parseInt(n1));
                            n.setStartNode(n.getStartNode());
                            observableEdge[0] = n;
                        });
                        endNodeField.setText(String.valueOf(n.getEndNode().getNodeID()));
                        endNodeField.textProperty().addListener((obs1, o1, n1) -> {
                            n.getEndNode().setNodeID(Integer.parseInt(n1));
                            n.setEndNode(n.getEndNode());
                            observableEdge[0] = n;
                        });
                        observableEdge[0] = n;
                    }
                });

                updateEntryButton.setOnAction(event -> {
                    try {
                        DAOFacade.addEdge(observableEdge[0]);
                    } catch (SQLException e) {
                        e.printStackTrace();
                    } });

                content.getChildren().add(startNodeField);
                content.getChildren().add(endNodeField);
                content.getChildren().add(updateEntryButton);


                tableView.getItems().addAll(edges);
                tableInsert.getChildren().add(tableView);
            }
            case Moves -> {
                TableView<Move> tableView = new TableView<>();
                tableView.setPrefWidth(600);
                TableColumn<Move, String> nodeID = new TableColumn<>("Node ID");
                nodeID.setCellValueFactory(data ->
                        new ReadOnlyStringWrapper(String.valueOf(data.getValue().getNode().getNodeID())));

                TableColumn<Move, String> longName = new TableColumn<>("Long Name");
                longName.setCellValueFactory(data ->
                        new ReadOnlyStringWrapper(data.getValue().getLocation().getLongName()));

                TableColumn<Move, String> date = new TableColumn<>("Date");
                date.setCellValueFactory(data ->
                        new ReadOnlyStringWrapper(data.getValue().getMoveDate().toString()));

                tableView.getColumns().add(nodeID);
                tableView.getColumns().add(longName);
                tableView.getColumns().add(date);

                content.getChildren().clear();
                content.getChildren().add(setTablePane);

                ObservableList<Move> moves = FXCollections.observableArrayList(DAOFacade.getAllMoves());
                MFXTextField nodeIDField = addFieldEdit("Node ID");
                MFXTextField longField = addFieldEdit("Long Name");
                MFXTextField dateField = addFieldEdit("Long Name");


                final Move[] observableMove = new Move[1];

                tableView.getSelectionModel().selectedItemProperty().addListener((obs, o, n) -> {
                    if(n != null){
                        nodeIDField.setText(String.valueOf(n.getNode().getNodeID()));
                        nodeIDField.textProperty().addListener((obs1, o1, n1) -> {
                            n.getNode().setNodeID(Integer.parseInt(n1));
                            n.setNode(n.getNode());
                            observableMove[0] = n;
                        });
                        longField.setText(String.valueOf(n.getLocation().getLongName()));
                        longField.textProperty().addListener((obs1, o1, n1) -> {
                            n.getLocation().setLongName(n1);
                            n.setLocation(n.getLocation());
                            observableMove[0] = n;
                        });
                        dateField.setText(String.valueOf(n.getMoveDate().toString()));
                        dateField.textProperty().addListener((obs1, o1, n1) -> {
                            n.setMoveDate(Date.from(Instant.parse(n1)));
                            observableMove[0] = n;
                        });
                        observableMove[0] = n;
                    }
                });

                updateEntryButton.setOnAction(event -> {
                    try {
                        DAOFacade.addMove(observableMove[0]);
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                });

                content.getChildren().add(nodeIDField);
                content.getChildren().add(longField);
                content.getChildren().add(dateField);
                content.getChildren().add(updateEntryButton);

                tableView.getItems().addAll(moves);
                tableInsert.getChildren().add(tableView);
            }
            case Nodes -> {
                TableView<Node> tableView = new TableView<>();
                tableView.setPrefWidth(600);
                TableColumn<Node, String> nodeID = new TableColumn<>("Node ID");
                nodeID.setCellValueFactory(data ->
                        new ReadOnlyStringWrapper(String.valueOf(data.getValue().getNodeID())));

                TableColumn<Node, String> xCoordinate = new TableColumn<>("X-Coordinate");
                xCoordinate.setCellValueFactory(data ->
                        new ReadOnlyStringWrapper(String.valueOf(data.getValue().getXcoord())));

                TableColumn<Node, String> yCoordinate = new TableColumn<>("Y-Coordinate");
                yCoordinate.setCellValueFactory(data ->
                        new ReadOnlyStringWrapper(String.valueOf(data.getValue().getYcoord())));

                TableColumn<Node, String> floor = new TableColumn<>("Floor");
                floor.setCellValueFactory(data ->
                        new ReadOnlyStringWrapper(data.getValue().getFloor()));

                TableColumn<Node, String> building = new TableColumn<>("Building");
                building.setCellValueFactory(data ->
                        new ReadOnlyStringWrapper(data.getValue().getBuilding()));

                tableView.getColumns().add(nodeID);
                tableView.getColumns().add(xCoordinate);
                tableView.getColumns().add(yCoordinate);
                tableView.getColumns().add(floor);
                tableView.getColumns().add(building);

                content.getChildren().clear();
                content.getChildren().add(setTablePane);

                ObservableList<Node> nodes = FXCollections.observableArrayList(DAOFacade.getAllNodes());
                MFXTextField nodeIDField = addFieldEdit("Node ID");
                MFXTextField xcoordField = addFieldEdit("X Coordinate");
                MFXTextField ycoordField = addFieldEdit("Y Coordinate");
                MFXTextField floorField = addFieldEdit("Floor");
                MFXTextField buildingField = addFieldEdit("Building");


                final Node[] observableNode = new Node[1];

                tableView.getSelectionModel().selectedItemProperty().addListener((obs, o, n) -> {
                    if(n != null){
                        nodeIDField.setText(String.valueOf(n.getNodeID()));
                        nodeIDField.textProperty().addListener((obs1, o1, n1) -> {
                            n.setNodeID(Integer.parseInt(n1));
                            observableNode[0] = n;
                        });
                        xcoordField.setText(String.valueOf(n.getXcoord()));
                        xcoordField.textProperty().addListener((obs1, o1, n1) -> {
                            n.setXcoord(Integer.parseInt(n1));
                            observableNode[0] = n;
                        });
                        ycoordField.setText(String.valueOf(n.getYcoord()));
                        ycoordField.textProperty().addListener((obs1, o1, n1) -> {
                            n.setYcoord(Integer.parseInt(n1));
                            observableNode[0] = n;
                        });
                        floorField.setText(String.valueOf(n.getFloor()));
                        floorField.textProperty().addListener((obs1, o1, n1) -> {
                            n.setFloor(n1);
                            observableNode[0] = n;
                        });
                        buildingField.setText(String.valueOf(n.getBuilding()));
                        buildingField.textProperty().addListener((obs1, o1, n1) -> {
                            n.setBuilding(n1);
                            observableNode[0] = n;
                        });
                        observableNode[0] = n;
                    }
                });

                updateEntryButton.setOnAction(event -> {
                    try {
                        DAOFacade.addNode(observableNode[0]);
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                });

                content.getChildren().add(nodeIDField);
                content.getChildren().add(xcoordField);
                content.getChildren().add(ycoordField);
                content.getChildren().add(floorField);
                content.getChildren().add(buildingField);
                content.getChildren().add(updateEntryButton);

                tableView.getItems().addAll(nodes);
                tableInsert.getChildren().add(tableView);
            }
            case Locations -> {
                TableView<LocationName> tableView = new TableView<>();
                tableView.setPrefWidth(600);
                tableView.setEditable(true);
                tableView.getSelectionModel().cellSelectionEnabledProperty().set(true);
                TableColumn<LocationName, String> longName = new TableColumn<>("Long Name");
                longName.setCellValueFactory(data ->
                        new ReadOnlyStringWrapper(data.getValue().getLongName()));

                TableColumn<LocationName, String> shortName = new TableColumn<>("Short Name");
                shortName.setCellValueFactory(data ->
                        new ReadOnlyStringWrapper(data.getValue().getShortName()));

                TableColumn<LocationName, String> nodeType = new TableColumn<>("Node Type");
                nodeType.setCellValueFactory(data ->
                        new ReadOnlyStringWrapper(data.getValue().getNodeType()));

                tableView.getColumns().add(longName);
                tableView.getColumns().add(shortName);
                tableView.getColumns().add(nodeType);

                content.getChildren().clear();
                content.getChildren().add(setTablePane);

                final ObservableList<LocationName> locations = FXCollections.observableArrayList(DAOFacade.getAllLocationNames());
                MFXTextField longField = addFieldEdit("Long Name");
                MFXTextField shortField = addFieldEdit("Short Name");
                MFXTextField nodeTypeField = addFieldEdit("Node Type");


                final LocationName[] observableLocation = new LocationName[1];

                tableView.getSelectionModel().selectedItemProperty().addListener((obs, o, n) -> {
                    if(n != null){
                        longField.setText(String.valueOf(n.getLongName()));
                        longField.textProperty().addListener((obs1, o1, n1) -> {
                            n.setLongName(n1);
                            observableLocation[0] = n;
                        });
                        shortField.setText(String.valueOf(n.getShortName()));
                        shortField.textProperty().addListener((obs1, o1, n1) -> {
                            n.setShortName(n1);
                            observableLocation[0] = n;
                        });
                        nodeTypeField.setText(String.valueOf(n.getNodeType()));
                        nodeTypeField.textProperty().addListener((obs1, o1, n1) -> {
                            n.setNodeType(n1);
                            observableLocation[0] = n;
                        });
                        observableLocation[0] = n;
                    }
                });
                updateEntryButton.setOnAction(event -> {
                    try {
                        DAOFacade.updateLocationName(observableLocation[0]);
                        for(int i = 0; i < locations.size(); i++){
                            if(locations.get(i).getLongName().equals(observableLocation[0].getLongName())){
                                locations.set(i, new LocationName(longField.getText(), shortField.getText(), nodeTypeField.getText()));
                                break;
                            }
                        }
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                });
                content.getChildren().add(longField);
                content.getChildren().add(shortField);
                content.getChildren().add(nodeTypeField);
                content.getChildren().add(updateEntryButton);

                tableView.getItems().addAll(locations);
                tableInsert.getChildren().add(tableView);
            }
            /*case Login -> {
                TableView<Login> tableView = new TableView<>();
                tableView.setPrefWidth(600);
                tableView.setEditable(true);
                tableView.getSelectionModel().cellSelectionEnabledProperty().set(true);
                TableColumn<Login, String> user = new TableColumn<>("User");
                user.setCellValueFactory(data ->
                        new ReadOnlyStringWrapper(data.getValue().getUsername()));

                TableColumn<Login, String> email = new TableColumn<>("Email");
                email.setCellValueFactory(data ->
                        new ReadOnlyStringWrapper(data.getValue().getEmail()));

                TableColumn<Login, String> first = new TableColumn<>("First Name");
                first.setCellValueFactory(data ->
                        new ReadOnlyStringWrapper(data.getValue().getFirstName()));

                TableColumn<Login, String> last = new TableColumn<>("Last Name");
                last.setCellValueFactory(data ->
                        new ReadOnlyStringWrapper(data.getValue().getLastName()));

                tableView.getColumns().add(user);
                tableView.getColumns().add(email);
                tableView.getColumns().add(first);
                tableView.getColumns().add(last);

                content.getChildren().clear();
                content.getChildren().add(setTablePane);

                ObservableList<Login> logins = FXCollections.observableArrayList(DAOFacade.getAllLogins());
                MFXTextField userField = addFieldEdit("Long Name");
                MFXTextField emailField = addFieldEdit("Short Name");
                MFXTextField firstField = addFieldEdit("First Name");
                MFXTextField lastField = addFieldEdit("Last Name");

                updateEntryButton.setOnAction(event -> {
                    try {
                        Login login = new Login(userPK, )
                        DAOFacade.updateLogin(observableLocation[0]);
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                });

                content.getChildren().add(userField);
                content.getChildren().add(emailField);
                content.getChildren().add(firstField);
                content.getChildren().add(lastField);
                content.getChildren().add(updateEntryButton);

                tableView.getItems().addAll(logins);
                tableInsert.getChildren().add(tableView);

            }*/
        }
    }
}
