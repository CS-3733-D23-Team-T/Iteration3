package edu.wpi.tacticaltritons.controllers.database;

import edu.wpi.tacticaltritons.database.*;
import edu.wpi.tacticaltritons.database.DAOFacade;
import edu.wpi.tacticaltritons.navigation.Navigation;
import edu.wpi.tacticaltritons.navigation.Screen;
import io.github.palexdev.materialfx.controls.*;

import java.io.*;
import java.sql.SQLException;
import java.text.ParseException;

import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import edu.wpi.tacticaltritons.styling.*;

public class DatabaseController {

  // add 4/4/2023
  @FXML private BorderPane basePane;
  @FXML MFXComboBox<String> selectTable;
  @FXML MFXButton editTableButton;
  @FXML MFXButton importButton;
  @FXML MFXButton exportButton;
  @FXML FlowPane tableInsert;

  @FXML
  public void initialize() throws SQLException, ClassNotFoundException {
//     EffectGenerator.generateShadowEffect(basePane);
     this.selectTable.setItems(
        FXCollections.observableArrayList("Node", "Edge", "Location Name", "Move", "Login", "MealRequests", "FlowerRequests", "FurnitureRequests", "ConferenceRequests", "OfficeSuppliesRequest", "MealOptions", "FlowerOptions", "FurnitureOptions", "OfficeSuppliesOptions"));

     this.selectTable.setOnAction(
            event -> {
              if (selectTable.getValue().equals("Node")) {
                tableInsert.getChildren().clear();
                TableView<Node> table = new TableView<>();
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

                table.getColumns().add(nodeID);
                table.getColumns().add(xCoordinate);
                table.getColumns().add(yCoordinate);
                table.getColumns().add(floor);
                table.getColumns().add(building);

                ObservableList<Node> nodes = null;
                try {
                  nodes = FXCollections.observableArrayList(DAOFacade.getAllNodes());
                } catch (SQLException e) {
                  throw new RuntimeException(e);
                }
                table.getItems().addAll(nodes);

                  table.setPrefWidth(tableInsert.getWidth());
                  table.setPrefHeight(tableInsert.getHeight());
                tableInsert.getChildren().add(table);
              }
              else if (selectTable.getValue().equals("Edge")) {

                tableInsert.getChildren().clear();
                TableView<Edge> table = new TableView<>();
                TableColumn<Edge, String> startNodeCol = new TableColumn<>("Start Node");
                startNodeCol.setCellValueFactory(data ->
                        new ReadOnlyStringWrapper(String.valueOf(data.getValue().getStartNode().getNodeID())));

                TableColumn<Edge, String> endNodeCol = new TableColumn<>("End Node");
                endNodeCol.setCellValueFactory(data ->
                        new ReadOnlyStringWrapper(String.valueOf(data.getValue().getEndNode().getNodeID())));

                ObservableList<Edge> edges = null;
                try {
                  edges = FXCollections.observableArrayList(DAOFacade.getAllEdges());
                } catch (SQLException e) {
                  throw new RuntimeException(e);
                }
                table.getItems().addAll(edges);
                table.getColumns().add(startNodeCol);
                table.getColumns().add(endNodeCol);

                  table.setPrefWidth(tableInsert.getWidth());
                  table.setPrefHeight(tableInsert.getHeight());
                tableInsert.getChildren().add(table);
              }
              else if (selectTable.getValue().equals("Location Name")) {
                tableInsert.getChildren().clear();
                TableView<LocationName> table = new TableView<>();
                TableColumn<LocationName, String> longName = new TableColumn<>("Long Name");
                longName.setCellValueFactory(data ->
                        new ReadOnlyStringWrapper(data.getValue().getLongName()));

                TableColumn<LocationName, String> shortName = new TableColumn<>("Short Name");
                shortName.setCellValueFactory(data ->
                        new ReadOnlyStringWrapper(data.getValue().getShortName()));

                TableColumn<LocationName, String> nodeType = new TableColumn<>("Node Type");
                nodeType.setCellValueFactory(data ->
                        new ReadOnlyStringWrapper(data.getValue().getNodeType()));

                table.getColumns().add(longName);
                table.getColumns().add(shortName);
                table.getColumns().add(nodeType);

                ObservableList<LocationName> locationNames = null;
                try {
                  locationNames = FXCollections.observableArrayList(DAOFacade.getAllLocationNames());
                } catch (SQLException e) {
                  throw new RuntimeException(e);
                }
                table.getItems().addAll(locationNames);

                  table.setPrefWidth(tableInsert.getWidth());
                  table.setPrefHeight(tableInsert.getHeight());
                tableInsert.getChildren().add(table);

              }
              else if (selectTable.getValue().equals("Move")) {
                tableInsert.getChildren().clear();
                TableView<Move> table = new TableView<>();
                TableColumn<Move, String> nodeID = new TableColumn<>("Node ID");
                nodeID.setCellValueFactory(data ->
                        new ReadOnlyStringWrapper(String.valueOf(data.getValue().getNode().getNodeID())));
                TableColumn<Move, String> longName = new TableColumn<>("Long Name");
                longName.setCellValueFactory(data ->
                        new ReadOnlyStringWrapper(data.getValue().getLocation().getLongName()));
                TableColumn<Move, String> date = new TableColumn<>("Date");
                date.setCellValueFactory(data ->
                        new ReadOnlyStringWrapper(data.getValue().getMoveDate().toString()));

                table.getColumns().add(nodeID);
                table.getColumns().add(longName);
                table.getColumns().add(date);

                ObservableList<Move> moves = null;
                try {
                  moves = FXCollections.observableArrayList(DAOFacade.getAllMoves());
                } catch (SQLException e) {
                  throw new RuntimeException(e);
                }
                table.getItems().addAll(moves);

                table.setPrefWidth(tableInsert.getWidth());
                table.setPrefHeight(tableInsert.getHeight());
                tableInsert.getChildren().add(table);
              }
            });

    this.importButton.setOnAction(
            event -> {
                String tableName = null;
                if (selectTable.getValue().equals("Node")) {
                    tableName = "node";
                } else if (selectTable.getValue().equals("Edge")) {
                    tableName = "edge";
                } else if (selectTable.getValue().equals("Location Name")) {
                    tableName = "locationname";
                } else if (selectTable.getValue().equals("Move")) {
                    tableName = "move";
                } else if (selectTable.getValue().equals("Login")) {
                    tableName = "login";
                } else if (selectTable.getValue().equals("MealRequests")) {
                    tableName = "meal";
                } else if (selectTable.getValue().equals("FlowerRequests")) {
                    tableName = "flower";
                } else if (selectTable.getValue().equals("FurnitureRequests")) {
                    tableName = "furnitureforms";
                } else if (selectTable.getValue().equals("ConferenceRequests")) {
                    tableName = "conference";
                } else if (selectTable.getValue().equals("OfficeSuppliesRequest")) {
                    tableName = "officesuppliesform";
                } else if (selectTable.getValue().equals("MealOptions")) {
                    tableName = "requestoptions";
                } else if (selectTable.getValue().equals("FlowerOptions")) {
                    tableName = "flowererequestoptions";
                } else if (selectTable.getValue().equals("FurnitureOptions")) {
                    tableName = "furniturerequestoptions";
                } else if (selectTable.getValue().equals("OfficeSuppliesOptions")) {
                    tableName = "officesuppliesrequestoptions";
                }
              Stage outStage = new Stage();
              FileChooser fileChooser = new FileChooser();
              File file = fileChooser.showOpenDialog(outStage);
              try {
                Import.importFile(file, tableName);
              } catch (IOException | SQLException | ParseException e) {
                throw new RuntimeException(e);
              }
            });

    this.exportButton.setOnAction(
            event -> {
              String tableName = null;
                if (selectTable.getValue().equals("Node")) {
                    tableName = "node";
                } else if (selectTable.getValue().equals("Edge")) {
                    tableName = "edge";
                } else if (selectTable.getValue().equals("Location Name")) {
                    tableName = "locationname";
                } else if (selectTable.getValue().equals("Move")) {
                    tableName = "move";
                } else if (selectTable.getValue().equals("Login")) {
                    tableName = "login";
                } else if (selectTable.getValue().equals("MealRequests")) {
                    tableName = "meal";
                } else if (selectTable.getValue().equals("FlowerRequests")) {
                    tableName = "flower";
                } else if (selectTable.getValue().equals("FurnitureRequests")) {
                    tableName = "furnitureforms";
                } else if (selectTable.getValue().equals("ConferenceRequests")) {
                    tableName = "conference";
                } else if (selectTable.getValue().equals("OfficeSuppliesRequest")) {
                    tableName = "officesuppliesform";
                } else if (selectTable.getValue().equals("MealOptions")) {
                    tableName = "requestoptions";
                } else if (selectTable.getValue().equals("FlowerOptions")) {
                    tableName = "flowererequestoptions";
                } else if (selectTable.getValue().equals("FurnitureOptions")) {
                    tableName = "furniturerequestoptions";
                } else if (selectTable.getValue().equals("OfficeSuppliesOptions")) {
                    tableName = "officesuppliesrequestoptions";
                }
              Stage exportStage = new Stage();
              FileChooser fileChooser = new FileChooser();
              fileChooser.setTitle("Export");
              fileChooser.setInitialFileName(tableName);
              fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV", "*.csv"));

              File file = fileChooser.showSaveDialog(exportStage);
              DAOFacade data = new DAOFacade();
              try {
                Export.export(file, tableName);
              } catch (Exception e) {
                throw new RuntimeException(e);
              }
            });
  }
}
