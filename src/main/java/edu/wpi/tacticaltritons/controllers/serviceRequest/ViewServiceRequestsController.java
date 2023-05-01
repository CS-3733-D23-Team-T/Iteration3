package edu.wpi.tacticaltritons.controllers.serviceRequest;

import edu.wpi.tacticaltritons.App;
import edu.wpi.tacticaltritons.auth.UserSessionToken;
import edu.wpi.tacticaltritons.database.*;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXComboBox;

import java.sql.SQLException;
import java.sql.Time;
import java.util.Date;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import edu.wpi.tacticaltritons.styling.*;

public class ViewServiceRequestsController {

    @FXML
    private BorderPane basePane;
    @FXML
    private FlowPane conferenceRoomRequest;
    @FXML
    private FlowPane flowerRequests;
    @FXML
    private FlowPane furnitureRequest;
    @FXML
    private FlowPane officeSupplyRequest;
    @FXML
    private FlowPane mealRequests;

    TableView<Meal> tableMeal = new TableView<>();
    TableView<Flower> tableFlower = new TableView<>();
    TableView<Conference> tableConference = new TableView<>();
    TableView<Furniture> tableFurniture = new TableView<>();
    TableView<Supply> tableSupply = new TableView<>();
    private final double minWidth = 1280;
    private final double minHeight = 650;

    @FXML
    public void initialize() throws SQLException, ClassNotFoundException {
        initConferenceRoomRequestTable();
        initFlowerRequestsTable();
        initFurnitureRequestTable();
        intiOfficeSupplyRequestTable();
        initMealRequestTable();
        initStyles();
    }

    public void initStyles(){
        basePane.setStyle("-fx-padding: 25px;");
        conferenceRoomRequest.setStyle("-fx-alignment: center;");
        flowerRequests.setStyle("-fx-alignment: center;");
        furnitureRequest.setStyle("-fx-alignment: center;");
        officeSupplyRequest.setStyle("-fx-alignment: center;");
        mealRequests.setStyle("-fx-alignment: center;");
    }

    private void initMealRequestTable() {
        mealRequests.getChildren().clear();
        tableMeal = new TableView<>();
        TableColumn<Meal, Integer> orderNumber = new TableColumn<>("Order Number");
        orderNumber.setCellValueFactory(new PropertyValueFactory<>("orderNum"));

        TableColumn<Meal, String> firstName = new TableColumn<>("First Name");
        firstName.setCellValueFactory(new PropertyValueFactory<>("requesterFirst"));

        TableColumn<Meal, String> lastName = new TableColumn<>("Last Name");
        lastName.setCellValueFactory(new PropertyValueFactory<>("requesterLast"));

        TableColumn<Meal, String> patientFirst = new TableColumn<>("Patient First Name");
        patientFirst.setCellValueFactory(new PropertyValueFactory<>("patientFirst"));

        TableColumn<Meal, String> patientLast = new TableColumn<>("Last Name");
        patientLast.setCellValueFactory(new PropertyValueFactory<>("patientLast"));

        TableColumn<Meal, String> assigendFirst = new TableColumn<>("Assigned First Name");
        assigendFirst.setCellValueFactory(new PropertyValueFactory<>("assignedStaffFirst"));

        TableColumn<Meal, String> assigendLast = new TableColumn<>("Assigned Last Name");
        assigendLast.setCellValueFactory(new PropertyValueFactory<>("assignedStaffLast"));

        TableColumn<Meal, Date> date = new TableColumn<>("Date");
        date.setCellValueFactory(new PropertyValueFactory<>("deliveryDate"));

        TableColumn<Meal, Time> time = new TableColumn<>("Time");
        time.setCellValueFactory(new PropertyValueFactory<>("deliveryTime"));

        TableColumn<Meal, String> location = new TableColumn<>("Location");
        location.setCellValueFactory(new PropertyValueFactory<>("location"));

        TableColumn<Meal, String> items = new TableColumn<>("Items");
        items.setCellValueFactory(new PropertyValueFactory<>("items"));

        TableColumn<Meal, String> total = new TableColumn<>("Total");
        total.setCellValueFactory(new PropertyValueFactory<>("total"));

        TableColumn<Meal, RequestStatus> status = new TableColumn<>("Status");
        status.setCellValueFactory(new PropertyValueFactory<>("status"));

        ObservableList<Meal> mealObservableList = null;
        try {
            mealObservableList = FXCollections.observableArrayList(DAOFacade.getAllMeal());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        tableMeal.getColumns().addAll(orderNumber, status, firstName, lastName, patientFirst, patientLast, assigendFirst, assigendLast, date, time, location, items, total);

        status.setCellFactory(column -> {
            return new TableCell<Meal, RequestStatus>() {
                private final MFXButton button = new MFXButton("Accept");
                private final MFXButton button2 = new MFXButton("Cancel");

                protected void updateItem(RequestStatus item, boolean empty) {
                    super.updateItem(item, empty);
                    if (item == null) {
                        setText(null);
                        setGraphic(null);
                    } else if (item.toString().equals("")) {
                        button.setStyle("-fx-background-color: green; -fx-font-fill: white;");
                        setText(null);
                        setGraphic(button);
                        button.setOnAction(event -> {
                            Meal meal = getTableView().getItems().get(getIndex());
                            meal.setAssignedStaffLast(UserSessionToken.getUser().getLastname());
                            meal.setAssignedStaffFirst(UserSessionToken.getUser().getFirstname());
                            meal.setStatus(RequestStatus.PROCESSING);
                            getTableView().refresh();
                            try {
                                DAOFacade.updateMeal(meal);
                            } catch (SQLException e) {
                                throw new RuntimeException(e);
                            }
                        });
                    } else if (item.toString().equals("Processing")) {
                        Meal meal = getTableView().getItems().get(getIndex());

                        if ((meal.getAssignedStaffFirst().equals(UserSessionToken.getUser().getFirstname())) && (meal.getAssignedStaffLast().equals(UserSessionToken.getUser().getLastname()))) {
                            button2.setStyle("-fx-background-color: #c00b0b; -fx-font-fill: white;");
                            setText(null);
                            setGraphic(button2);
                            button2.setOnAction(event -> {
                                meal.setAssignedStaffLast(null);
                                meal.setAssignedStaffFirst(null);
                                meal.setStatus(RequestStatus.BLANK);
                                getTableView().refresh();
                                try {
                                    DAOFacade.updateMeal(meal);
                                } catch (SQLException e) {
                                    throw new RuntimeException(e);
                                }
                            });
                        } else {
                            setText(item.toString());
                            setGraphic(null);
                        }
                    } else {
                        setText(item.toString());
                        setGraphic(null);
                    }
                }
            };
        });

        tableMeal.getItems().addAll(mealObservableList);

        tableMeal.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        App.getPrimaryStage().widthProperty().addListener(((obs, o, n) -> {
            double scaleX = mealRequests.getWidth() / App.getPrimaryStage().getWidth();
            mealRequests.setMinWidth(scaleX);
        }));
        App.getPrimaryStage().heightProperty().addListener(((obs, o, n) -> {
            double scaleY = mealRequests.getHeight() / App.getPrimaryStage().getHeight();
            mealRequests.setMinHeight(scaleY);
        }));

        mealRequests.widthProperty().addListener((observable, oldValue, newValue) -> {
            mealRequests.getChildren().clear();
            tableMeal.setMinWidth(mealRequests.getMinWidth());
            tableMeal.setPrefWidth(newValue.doubleValue());
            mealRequests.getChildren().add(tableMeal);
        });

        mealRequests.heightProperty().addListener((observable, oldValue, newValue) -> {
            mealRequests.getChildren().clear();
            tableMeal.setMinHeight(newValue.doubleValue());
            tableMeal.setPrefHeight(newValue.doubleValue());
            mealRequests.getChildren().add(tableMeal);
        });
        tableMeal.setPlaceholder(new Label("No Pending Meal Requests"));
        tableMeal.getStyleClass().add("table-view");
    }

    private void intiOfficeSupplyRequestTable() {
        officeSupplyRequest.getChildren().clear();
        tableSupply = new TableView<>();
        TableColumn<Supply, Integer> orderNumber = new TableColumn<>("Order Number");
        orderNumber.setCellValueFactory(new PropertyValueFactory<>("orderNum"));

        TableColumn<Supply, String> firstName = new TableColumn<>("First Name");
        firstName.setCellValueFactory(new PropertyValueFactory<>("requesterFirst"));

        TableColumn<Supply, String> lastName = new TableColumn<>("Last Name");
        lastName.setCellValueFactory(new PropertyValueFactory<>("requesterLast"));

        TableColumn<Supply, String> assigendFirst = new TableColumn<>("Assigned First Name");
        assigendFirst.setCellValueFactory(new PropertyValueFactory<>("assignedStaffFirst"));

        TableColumn<Supply, String> assigendLast = new TableColumn<>("Assigned Last Name");
        assigendLast.setCellValueFactory(new PropertyValueFactory<>("assignedStaffLast"));

        TableColumn<Supply, Date> date = new TableColumn<>("Date");
        date.setCellValueFactory(new PropertyValueFactory<>("deliveryDate"));

        TableColumn<Supply, Time> time = new TableColumn<>("Time");
        time.setCellValueFactory(new PropertyValueFactory<>("deliveryTime"));

        TableColumn<Supply, String> location = new TableColumn<>("Location");
        location.setCellValueFactory(new PropertyValueFactory<>("location"));

        TableColumn<Supply, String> items = new TableColumn<>("Items");
        items.setCellValueFactory(new PropertyValueFactory<>("items"));

        TableColumn<Supply, String> total = new TableColumn<>("Total");
        total.setCellValueFactory(new PropertyValueFactory<>("total"));

        TableColumn<Supply, RequestStatus> status = new TableColumn<>("Status");
        status.setCellValueFactory(new PropertyValueFactory<>("status"));

        ObservableList<Supply> supplyObservableList = null;
        try {
            supplyObservableList = FXCollections.observableArrayList(DAOFacade.getAllSupply());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        tableSupply.getColumns().addAll(orderNumber, status, firstName, lastName, assigendFirst, assigendLast, date, time, location, items, total);

        status.setCellFactory(column -> {
            return new TableCell<Supply, RequestStatus>() {
                private final MFXButton button = new MFXButton("Accept");
                private final MFXButton button2 = new MFXButton("Cancel");

                protected void updateItem(RequestStatus item, boolean empty) {
                    super.updateItem(item, empty);
                    if (item == null) {
                        setText(null);
                        setGraphic(null);
                    } else if (item.toString().equals("")) {
                        button.setStyle("-fx-background-color: green;");
                        setText(null);
                        setGraphic(button);
                        button.setOnAction(event -> {
                            Supply supply = getTableView().getItems().get(getIndex());
                            supply.setAssignedStaffLast(UserSessionToken.getUser().getLastname());
                            supply.setAssignedStaffFirst(UserSessionToken.getUser().getFirstname());
                            supply.setStatus(RequestStatus.PROCESSING);
                            getTableView().refresh();
                            try {
                                DAOFacade.updateSupply(supply);
                            } catch (SQLException e) {
                                throw new RuntimeException(e);
                            }
                        });
                    } else if (item.toString().equals("Processing")) {
                        Supply supply = getTableView().getItems().get(getIndex());

                        if ((supply.getAssignedStaffFirst().equals(UserSessionToken.getUser().getFirstname())) && (supply.getAssignedStaffLast().equals(UserSessionToken.getUser().getLastname()))) {
                            button2.setStyle("-fx-background-color: red;");
                            setText(null);
                            setGraphic(button2);
                            button2.setOnAction(event -> {
                                supply.setAssignedStaffLast(null);
                                supply.setAssignedStaffFirst(null);
                                supply.setStatus(RequestStatus.BLANK);
                                getTableView().refresh();
                                try {
                                    DAOFacade.updateSupply(supply);
                                } catch (SQLException e) {
                                    throw new RuntimeException(e);
                                }
                            });
                        } else {
                            setText(item.toString());
                            setGraphic(null);
                        }
                    } else {
                        setText(item.toString());
                        setGraphic(null);
                    }
                }
            };
        });

        tableSupply.getItems().addAll(supplyObservableList);

        tableSupply.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        App.getPrimaryStage().widthProperty().addListener(((obs, o, n) -> {
            double scaleX = officeSupplyRequest.getWidth() / App.getPrimaryStage().getWidth();
            officeSupplyRequest.setMinWidth(scaleX);
        }));
        App.getPrimaryStage().heightProperty().addListener(((obs, o, n) -> {
            double scaleY = officeSupplyRequest.getHeight() / App.getPrimaryStage().getHeight();
            officeSupplyRequest.setMinHeight(scaleY);
        }));

        officeSupplyRequest.widthProperty().addListener((observable, oldValue, newValue) -> {
            officeSupplyRequest.getChildren().clear();
            tableSupply.setMinWidth(officeSupplyRequest.getMinWidth());
            tableSupply.setPrefWidth(newValue.doubleValue());
            officeSupplyRequest.getChildren().add(tableSupply);
        });

        officeSupplyRequest.heightProperty().addListener((observable, oldValue, newValue) -> {
            officeSupplyRequest.getChildren().clear();
            tableSupply.setMinHeight(newValue.doubleValue());
            tableSupply.setPrefHeight(newValue.doubleValue());
            officeSupplyRequest.getChildren().add(tableSupply);
        });
        tableSupply.setPlaceholder(new Label("No Pending Supply Requests"));
        tableSupply.getStyleClass().add("table-view");
    }

    private void initFurnitureRequestTable() {
        furnitureRequest.getChildren().clear();
        tableFurniture = new TableView<>();
        TableColumn<Furniture, Integer> orderNumber = new TableColumn<>("Order Number");
        orderNumber.setCellValueFactory(new PropertyValueFactory<>("orderNum"));

        TableColumn<Furniture, String> firstName = new TableColumn<>("First Name");
        firstName.setCellValueFactory(new PropertyValueFactory<>("firstName"));

        TableColumn<Furniture, String> lastName = new TableColumn<>("Last Name");
        lastName.setCellValueFactory(new PropertyValueFactory<>("lastName"));

        TableColumn<Furniture, String> assignedStaffFirst = new TableColumn<>("Assigned Staff First");
        assignedStaffFirst.setCellValueFactory(new PropertyValueFactory<>("assignedStaffFirst"));

        TableColumn<Furniture, String> assignedStaffLast = new TableColumn<>("Assigned Staff Last");
        assignedStaffLast.setCellValueFactory(new PropertyValueFactory<>("assignedStaffLast"));

        TableColumn<Furniture, Date> date = new TableColumn<>("Date");
        date.setCellValueFactory(new PropertyValueFactory<>("deliveryDate"));

        TableColumn<Furniture, String> location = new TableColumn<>("Location");
        location.setCellValueFactory(new PropertyValueFactory<>("location"));

        TableColumn<Furniture, String> items = new TableColumn<>("Items");
        items.setCellValueFactory(new PropertyValueFactory<>("items"));

        TableColumn<Furniture, RequestStatus> status = new TableColumn<>("Status");
        status.setCellValueFactory(new PropertyValueFactory<>("status"));


        tableFurniture.getColumns().addAll(orderNumber, status, firstName, lastName, assignedStaffFirst, assignedStaffLast, date, location, items);


        ObservableList<Furniture> furnitureObservableList = null;
        try {
            furnitureObservableList = FXCollections.observableArrayList(DAOFacade.getAllFurniture());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        status.setCellFactory(column -> {
            return new TableCell<Furniture, RequestStatus>() {
                private final MFXButton button = new MFXButton("Accept");
                private final MFXButton button2 = new MFXButton("Cancel");

                protected void updateItem(RequestStatus item, boolean empty) {
                    super.updateItem(item, empty);
                    if (item == null) {
                        setText(null);
                        setGraphic(null);
                    } else if (item.toString().equals("")) {
                        button.setStyle("-fx-background-color: green; -fx-font-fill: white;");
                        setText(null);
                        setGraphic(button);
                        button.setOnAction(event -> {
                            Furniture furniture = getTableView().getItems().get(getIndex());
                            furniture.setAssignedStaffLast(UserSessionToken.getUser().getLastname());
                            furniture.setAssignedStaffFirst(UserSessionToken.getUser().getFirstname());
                            furniture.setStatus(RequestStatus.PROCESSING);
                            getTableView().refresh();
                            try {
                                DAOFacade.updateFurniture(furniture);
                            } catch (SQLException e) {
                                throw new RuntimeException(e);
                            }
                        });
                    } else if (item.toString().equals("Processing")) {
                        Furniture furniture = getTableView().getItems().get(getIndex());

                        if ((furniture.getAssignedStaffFirst().equals(UserSessionToken.getUser().getFirstname())) && (furniture.getAssignedStaffLast().equals(UserSessionToken.getUser().getLastname()))) {
                            button2.setStyle("-fx-background-color: #c00b0b; -fx-font-fill: white;");
                            setText(null);
                            setGraphic(button2);
                            button2.setOnAction(event -> {
                                furniture.setAssignedStaffLast(null);
                                furniture.setAssignedStaffFirst(null);
                                furniture.setStatus(RequestStatus.BLANK);
                                getTableView().refresh();
                                try {
                                    DAOFacade.updateFurniture(furniture);
                                } catch (SQLException e) {
                                    throw new RuntimeException(e);
                                }
                            });
                        } else {
                            setText(item.toString());
                            setGraphic(null);
                        }
                    } else {
                        setText(item.toString());
                        setGraphic(null);
                    }
                }
            };
        });

        tableFurniture.getItems().addAll(furnitureObservableList);

        tableFurniture.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        App.getPrimaryStage().widthProperty().addListener(((obs, o, n) -> {
            double scaleX = furnitureRequest.getWidth() / App.getPrimaryStage().getWidth();
            furnitureRequest.setMinWidth(scaleX);
        }));
        App.getPrimaryStage().heightProperty().addListener(((obs, o, n) -> {
            double scaleY = furnitureRequest.getHeight() / App.getPrimaryStage().getHeight();
            furnitureRequest.setMinHeight(scaleY);
        }));

        furnitureRequest.widthProperty().addListener((observable, oldValue, newValue) -> {
            furnitureRequest.getChildren().clear();
            tableFurniture.setMinWidth(furnitureRequest.getMinWidth());
            tableFurniture.setPrefWidth(newValue.doubleValue());
            furnitureRequest.getChildren().add(tableFurniture);
        });

        furnitureRequest.heightProperty().addListener((observable, oldValue, newValue) -> {
            furnitureRequest.getChildren().clear();
            tableFurniture.setMinHeight(newValue.doubleValue());
            tableFurniture.setPrefHeight(newValue.doubleValue());
            furnitureRequest.getChildren().add(tableFurniture);
        });
        tableFurniture.setPlaceholder(new Label("No Pending Furniture Requests"));
        tableFurniture.getStyleClass().add("table-view");
    }

    private void initFlowerRequestsTable() {
        flowerRequests.getChildren().clear();
        tableFlower = new TableView<>();
        TableColumn<Flower, Integer> orderNumber = new TableColumn<>("Order Number");
        orderNumber.setCellValueFactory(new PropertyValueFactory<>("orderNum"));

        TableColumn<Flower, String> firstName = new TableColumn<>("First Name");
        firstName.setCellValueFactory(new PropertyValueFactory<>("requesterFirst"));

        TableColumn<Flower, String> lastName = new TableColumn<>("Last Name");
        lastName.setCellValueFactory(new PropertyValueFactory<>("requesterLast"));

        TableColumn<Flower, String> patientFirst = new TableColumn<>("Patient First Name");
        patientFirst.setCellValueFactory(new PropertyValueFactory<>("patientFirst"));

        TableColumn<Flower, String> patientLast = new TableColumn<>("Last Name");
        patientLast.setCellValueFactory(new PropertyValueFactory<>("patientLast"));

        TableColumn<Flower, String> assigendFirst = new TableColumn<>("Assigned First Name");
        assigendFirst.setCellValueFactory(new PropertyValueFactory<>("assignedStaffFirst"));

        TableColumn<Flower, String> assigendLast = new TableColumn<>("Assigned Last Name");
        assigendLast.setCellValueFactory(new PropertyValueFactory<>("assignedStaffLast"));

        TableColumn<Flower, Date> date = new TableColumn<>("Date");
        date.setCellValueFactory(new PropertyValueFactory<>("deliveryDate"));

        TableColumn<Flower, Time> time = new TableColumn<>("Time");
        time.setCellValueFactory(new PropertyValueFactory<>("deliveryTime"));

        TableColumn<Flower, String> location = new TableColumn<>("Location");
        location.setCellValueFactory(new PropertyValueFactory<>("location"));

        TableColumn<Flower, String> items = new TableColumn<>("Items");
        items.setCellValueFactory(new PropertyValueFactory<>("items"));

        TableColumn<Flower, String> total = new TableColumn<>("Total");
        total.setCellValueFactory(new PropertyValueFactory<>("total"));

        TableColumn<Flower, RequestStatus> status = new TableColumn<>("Status");
        status.setCellValueFactory(new PropertyValueFactory<>("status"));


        tableFlower.getColumns().addAll(orderNumber, status, firstName, lastName, patientFirst, patientLast, assigendFirst, assigendLast, date, time, location, items, total);


        ObservableList<Flower> flowerObservableList = null;
        try {
            flowerObservableList = FXCollections.observableArrayList(DAOFacade.getAllFlower());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        status.setCellFactory(column -> {
            return new TableCell<Flower, RequestStatus>() {
                private final MFXButton button = new MFXButton("Accept");
                private final MFXButton button2 = new MFXButton("Cancel");

                protected void updateItem(RequestStatus item, boolean empty) {
                    super.updateItem(item, empty);
                    if (item == null) {
                        setText(null);
                        setGraphic(null);
                    } else if (item.toString().equals("")) {
                        button.setStyle("-fx-background-color: green; -fx-font-fill: white;");
                        setText(null);
                        setGraphic(button);
                        button.setOnAction(event -> {
                            Flower flower = getTableView().getItems().get(getIndex());
                            flower.setAssignedStaffLast(UserSessionToken.getUser().getLastname());
                            flower.setAssignedStaffFirst(UserSessionToken.getUser().getFirstname());
                            flower.setStatus(RequestStatus.PROCESSING);
                            getTableView().refresh();
                            try {
                                DAOFacade.updateFlower(flower);
                            } catch (SQLException e) {
                                throw new RuntimeException(e);
                            }
                        });
                    } else if (item.toString().equals("Processing")) {
                        Flower flower = getTableView().getItems().get(getIndex());

                        if ((flower.getAssignedStaffFirst().equals(UserSessionToken.getUser().getFirstname())) && (flower.getAssignedStaffLast().equals(UserSessionToken.getUser().getLastname()))) {
                            button2.setStyle("-fx-background-color: #c00b0b; -fx-font-fill: white;");
                            setText(null);
                            setGraphic(button2);
                            button2.setOnAction(event -> {
                                flower.setAssignedStaffLast(null);
                                flower.setAssignedStaffFirst(null);
                                flower.setStatus(RequestStatus.BLANK);
                                getTableView().refresh();
                                try {
                                    DAOFacade.updateFlower(flower);
                                } catch (SQLException e) {
                                    throw new RuntimeException(e);
                                }
                            });
                        } else {
                            setText(item.toString());
                            setGraphic(null);
                        }
                    } else {
                        setText(item.toString());
                        setGraphic(null);
                    }
                }
            };
        });

        tableFlower.getItems().addAll(flowerObservableList);

        tableFlower.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        App.getPrimaryStage().widthProperty().addListener(((obs, o, n) -> {
            double scaleX = flowerRequests.getWidth() / App.getPrimaryStage().getWidth();
            flowerRequests.setMinWidth(scaleX);
        }));
        App.getPrimaryStage().heightProperty().addListener(((obs, o, n) -> {
            double scaleY = flowerRequests.getHeight() / App.getPrimaryStage().getHeight();
            flowerRequests.setMinHeight(scaleY);
        }));

        flowerRequests.widthProperty().addListener((observable, oldValue, newValue) -> {
            flowerRequests.getChildren().clear();
            tableFlower.setMinWidth(flowerRequests.getMinWidth());
            tableFlower.setPrefWidth(newValue.doubleValue());
            flowerRequests.getChildren().add(tableFlower);
        });

        flowerRequests.heightProperty().addListener((observable, oldValue, newValue) -> {
            flowerRequests.getChildren().clear();
            tableFlower.setMinHeight(newValue.doubleValue());
            tableFlower.setPrefHeight(newValue.doubleValue());
            flowerRequests.getChildren().add(tableFlower);
        });
        tableFlower.setPlaceholder(new Label("No Pending Flower Requests"));
        tableFlower.getStyleClass().add("table-view");
    }

    private void initConferenceRoomRequestTable() {
        conferenceRoomRequest.getChildren().clear();
        tableConference = new TableView<>();
        TableColumn<Conference, Integer> orderNumber = new TableColumn<>("Order Number");
        orderNumber.setCellValueFactory(new PropertyValueFactory<>("orderNum"));

        TableColumn<Conference, String> firstName = new TableColumn<>("First Name");
        firstName.setCellValueFactory(new PropertyValueFactory<>("firstName"));

        TableColumn<Conference, String> lastName = new TableColumn<>("Last Name");
        lastName.setCellValueFactory(new PropertyValueFactory<>("lastName"));

        TableColumn<Conference, Date> date = new TableColumn<>("Date");
        date.setCellValueFactory(new PropertyValueFactory<>("date"));

        TableColumn<Conference, String> attendance = new TableColumn<>("Attendance");
        attendance.setCellValueFactory(new PropertyValueFactory<>("attendance"));

        TableColumn<Conference, Integer> expectedSize = new TableColumn<>("Expected Size");
        expectedSize.setCellValueFactory(new PropertyValueFactory<>("expectedSize"));

        TableColumn<Conference, String> location = new TableColumn<>("Location");
        location.setCellValueFactory(new PropertyValueFactory<>("location"));

        TableColumn<Conference, RequestStatus> status = new TableColumn<>("Status");
        status.setCellValueFactory(new PropertyValueFactory<>("status"));


        tableConference.getColumns().addAll(orderNumber, status, firstName, lastName, date, attendance, expectedSize, location);

        ObservableList<Conference> conferenceObservableList = null;
        try {
            conferenceObservableList = FXCollections.observableArrayList(DAOFacade.getAllConference());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        status.setCellFactory(column -> {
            return new TableCell<Conference, RequestStatus>() {
                private final MFXButton button = new MFXButton("Confirm");

                protected void updateItem(RequestStatus item, boolean empty) {
                    super.updateItem(item, empty);
                    if (item == null) {
                        setText(null);
                        setGraphic(null);
                    } else if (item.toString().equals("Processing") && UserSessionToken.getUser().isAdmin()) {
                        button.setStyle("-fx-background-color: green; -fx-text-fill: white;");
                        setText(null);
                        setGraphic(button);
                        button.setOnAction(event -> {
                            Conference conference = getTableView().getItems().get(getIndex());
                            conference.setStatus(RequestStatus.DONE);
                            getTableView().refresh();
                            try {
                                DAOFacade.updateConference(conference);
                            } catch (SQLException e) {
                                throw new RuntimeException(e);
                            }
                        });
                    } else {
                        setText(item.toString());
                        setGraphic(null);
                    }
                }
            };
        });

        tableConference.getItems().addAll(conferenceObservableList);

        tableConference.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        App.getPrimaryStage().widthProperty().addListener(((obs, o, n) -> {
            double scaleX = conferenceRoomRequest.getWidth() / App.getPrimaryStage().getWidth();
            conferenceRoomRequest.setMinWidth(scaleX);
        }));
        App.getPrimaryStage().heightProperty().addListener(((obs, o, n) -> {
            double scaleY = conferenceRoomRequest.getHeight() / App.getPrimaryStage().getHeight();
            conferenceRoomRequest.setMinHeight(scaleY);
        }));

        conferenceRoomRequest.widthProperty().addListener((observable, oldValue, newValue) -> {
            conferenceRoomRequest.getChildren().clear();
            tableConference.setMinWidth(conferenceRoomRequest.getMinWidth());
            tableConference.setPrefWidth(newValue.doubleValue());
            conferenceRoomRequest.getChildren().add(tableConference);
        });

        conferenceRoomRequest.heightProperty().addListener((observable, oldValue, newValue) -> {
            conferenceRoomRequest.getChildren().clear();
            tableConference.setMinHeight(newValue.doubleValue());
            tableConference.setPrefHeight(newValue.doubleValue());
            conferenceRoomRequest.getChildren().add(tableConference);
        });
        tableConference.setPlaceholder(new Label("No Pending Conference Requests"));
        tableConference.getStyleClass().add("table-view");
    }
}
