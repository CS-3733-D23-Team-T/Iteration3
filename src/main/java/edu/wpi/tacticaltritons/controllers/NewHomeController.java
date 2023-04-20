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
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;

import java.sql.SQLException;
import java.sql.Time;
import java.util.Date;

public class NewHomeController {
    @FXML FlowPane requestsPane;
    @FXML FlowPane movesPane;
    @FXML FlowPane eventsPane;
    @FXML GridPane tableGridPane;

    TableView<HomeServiceRequests> tableServiceRequest = new TableView<>();
    @FXML
    public void initialize() {
        requestsPane.getChildren().clear();

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

        tableServiceRequest.getColumns().addAll(completed, serviceType,orderNum,deliveryDate,deliveryTime);

        tableServiceRequest.getItems().addAll(requestObservableList);
tableServiceRequest.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        App.getPrimaryStage().widthProperty().addListener((observable, oldValue, newValue) ->
        {
            requestsPane.getChildren().clear();
            if(App.getPrimaryStage().isMaximized())
            {
                System.out.println("Width Property Is maximized");
                System.out.println("Old value: " + oldValue + " newvalue: " + newValue);
                System.out.println("Width of gridpane: " + tableGridPane.getCellBounds(1,0).getWidth());
                System.out.println();
                tableServiceRequest.setPrefWidth(600);
            }
            else
            {
                System.out.println("Width Property");
                System.out.println("Old value: " + oldValue + " newvalue: " + newValue);
                System.out.println("Width of gridpane: " + tableGridPane.getCellBounds(1,0).getWidth());
                System.out.println();
                tableServiceRequest.setPrefWidth(tableGridPane.getCellBounds(1,0).getWidth() - 40);
            }
            requestsPane.getChildren().add(tableServiceRequest);
        });

        App.getPrimaryStage().heightProperty().addListener((observable, oldValue, newValue) ->
        {
//            System.out.println("Height Property");
//            System.out.println("Old value: " + oldValue + " newvalue: " + newValue);
//            System.out.println("Height of gridpane: " + tableGridPane.getCellBounds(1,0).getHeight());
            requestsPane.getChildren().clear();
            tableServiceRequest.setPrefHeight(tableGridPane.getCellBounds(1,0).getHeight());
            requestsPane.getChildren().add(tableServiceRequest);
        });

    }
}
