package edu.wpi.tacticaltritons.controllers;

import edu.wpi.tacticaltritons.App;
import edu.wpi.tacticaltritons.auth.UserSessionToken;
import edu.wpi.tacticaltritons.database.*;
import io.github.palexdev.materialfx.controls.MFXButton;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Background;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;

import java.sql.SQLException;
import java.sql.Date;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class NewHomeController {
    @FXML
    FlowPane requestsPane;
    @FXML
    FlowPane movesPane;
    @FXML
    FlowPane eventsPane;
    @FXML
    GridPane tableGridPane;

    TableView<HomeServiceRequests> tableServiceRequest = new TableView<>();

    @FXML
    public void initialize() throws SQLException {
        initMoveTable();
        initServiceTable();
    }

    private void initMoveTable() throws SQLException {
        Date currentDate = Date.valueOf(java.time.LocalDate.now());
        List<Move> allMoves = DAOFacade.getAllMoves();
        List<Move> allFutureMoves = DAOFacade.getAllFutureMoves(currentDate);
        Collections.reverse(allFutureMoves);
        List<Button> moveButtons = new ArrayList<>();

        for(Move moveTo : allFutureMoves){
            int counter = 0;
            boolean check = false;
            for(Move moveFrom : allMoves){
                String moveFromLocation = moveFrom.getLocation().getLongName();
                String moveToLocation = moveTo.getLocation().getLongName();
                if(moveFromLocation.equals(moveToLocation) && moveFrom.getNode().getNodeID() == moveTo.getNode().getNodeID())
                {
                    System.out.println("TEST?");
                    check = true;
                }
                if(check) {
                    if (moveFromLocation.equals(moveToLocation)) {
                        counter++;
                    }
                }
                if(counter == 2){
                    Button moveButton = new Button(moveFrom.getLocation().getLongName() + ": Node " + moveFrom.getNode().getNodeID() + " -> Node " + moveTo.getNode().getNodeID());
                    moveButton.prefWidthProperty().bind(movesPane.widthProperty());
                    moveButtons.add(moveButton);
                    moveButton.setOnAction(event -> {
                        System.out.println("WORKING");
                    });
                    counter = 0;
                }
            }
        }
        movesPane.getChildren().clear();
        Collections.reverse(moveButtons);
        for(Button button : moveButtons) {
            movesPane.getChildren().add(button);
        }
    }

    private void initServiceTable() {
        TableColumn<HomeServiceRequests, String> serviceType = new TableColumn<>("Service Type");
        serviceType.setCellValueFactory(new PropertyValueFactory<>("requestType"));

        TableColumn<HomeServiceRequests, Integer> orderNum = new TableColumn<>("Order Num");
        orderNum.setCellValueFactory(new PropertyValueFactory<>("orderNum"));

        TableColumn<HomeServiceRequests, Date> deliveryDate = new TableColumn<>("Date");
        deliveryDate.setCellValueFactory(new PropertyValueFactory<>("deliveryDate"));

        TableColumn<HomeServiceRequests, Time> deliveryTime = new TableColumn<>("Time");
        deliveryTime.setCellValueFactory(new PropertyValueFactory<>("deliveryTime"));

        ObservableList<HomeServiceRequests> requestObservableList = null;
        try {
            requestObservableList = FXCollections.observableArrayList(DAOFacade.getSessionServiceRequests(UserSessionToken.getUser().getFirstname(), UserSessionToken.getUser().getLastname()));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        TableColumn<HomeServiceRequests, Void> completed = new TableColumn<>("");
        completed.setPrefWidth(100);
        completed.setCellFactory(event -> new TableCell<>() {
            private final Button button = new Button("Complete");

            {
                button.setOnAction(event -> {
                    HomeServiceRequests request = getTableView().getItems().get(getIndex());
                    try {
                        if (request.getRequestType().equals("Meal")) {
                            Meal meal = DAOFacade.getMeal(request.getOrderNum());
                            meal.setStatus(RequestStatus.DONE);
                            DAOFacade.updateMeal(meal);
                        } else if (request.getRequestType().equals("Flower")) {
                            Flower flower = DAOFacade.getFlower(request.getOrderNum());
                            flower.setStatus(RequestStatus.DONE);
                            DAOFacade.updateFlower(flower);
                        } else if (request.getRequestType().equals("Furniture")) {
                            Furniture furniture = DAOFacade.getFurniture(request.getOrderNum());
                            furniture.setStatus(RequestStatus.DONE);
                            DAOFacade.updateFurniture(furniture);
                        }
                    } catch (SQLException ex) {
                        throw new RuntimeException(ex);
                    }

                    getTableView().getItems().remove(request);
                    getTableView().refresh();
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(button);
                }
            }
        });

        tableServiceRequest.getColumns().addAll(completed, serviceType, orderNum, deliveryDate, deliveryTime);

        tableServiceRequest.getItems().addAll(requestObservableList);
        tableServiceRequest.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        App.getPrimaryStage().widthProperty().addListener(((obs, o, n) -> {
            double scaleX = requestsPane.getWidth() / App.getPrimaryStage().getWidth();
            requestsPane.setMinWidth(scaleX);
        }));
        App.getPrimaryStage().heightProperty().addListener(((obs, o, n) -> {
            double scaleY = requestsPane.getHeight() / App.getPrimaryStage().getHeight();
            requestsPane.setMinHeight(scaleY);
        }));

        requestsPane.widthProperty().addListener((observable, oldValue, newValue) -> {
            requestsPane.getChildren().clear();
            tableServiceRequest.setMinWidth(requestsPane.getMinWidth() - 1);
            tableServiceRequest.setPrefWidth(newValue.doubleValue() - 1);
            requestsPane.getChildren().add(tableServiceRequest);
        });

        requestsPane.heightProperty().addListener((observable, oldValue, newValue) -> {
            requestsPane.getChildren().clear();
            tableServiceRequest.setMinWidth(newValue.doubleValue() - 1);
            tableServiceRequest.setPrefHeight(newValue.doubleValue() - 1);
            requestsPane.getChildren().add(tableServiceRequest);
        });
    }
}
