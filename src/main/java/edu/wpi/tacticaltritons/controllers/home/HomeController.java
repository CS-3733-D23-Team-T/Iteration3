package edu.wpi.tacticaltritons.controllers.home;

import edu.wpi.tacticaltritons.App;
import edu.wpi.tacticaltritons.auth.UserSessionToken;
import edu.wpi.tacticaltritons.database.*;
import edu.wpi.tacticaltritons.navigation.Screen;
import edu.wpi.tacticaltritons.styling.GoogleTranslate;
import edu.wpi.tacticaltritons.styling.ThemeColors;
import edu.wpi.tacticaltritons.styling.Translation;
import io.github.palexdev.materialfx.controls.MFXButton;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.skin.TableColumnHeader;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import net.kurobako.gesturefx.GesturePane;
import org.controlsfx.control.PopOver;

import java.io.IOException;
import java.sql.SQLException;
import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Flow;


public class HomeController {
    @FXML
    FlowPane requestsPane;
    @FXML
    BorderPane borderPaneHome;
    @FXML
    FlowPane movesPane;
    @FXML
    FlowPane eventsPane;
    @FXML
    GridPane tableGridPane;
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
    private GesturePane gesturePane = new GesturePane();
    @FXML
    private StackPane stackPane = new StackPane();
    @FXML
    private FlowPane titleFlowPane;

    @FXML
    private GridPane announcementGridPane;
    @FXML Text eventsText;
    @FXML Text requestText;
    @FXML Text moveText;

    TableView<HomeServiceRequests> tableServiceRequest = new TableView<>();
    TableView<Invitations> tableInvitation = new TableView<>();

    @FXML
    public void initialize() throws SQLException, IOException {
        eventsText.setText(GoogleTranslate.translate(eventsText.getText()));
        requestText.setText(GoogleTranslate.translate(requestText.getText()));
        moveText.setText(GoogleTranslate.translate(moveText.getText()));
        initAnnouncements();
        initEventTable();
        initMoveTable();
        initServiceTable();
    }

    private void initAnnouncements() throws SQLException, IOException {
        List<Announcements> announcementsList = DAOFacade.getAllAnnouncements(Timestamp.valueOf(LocalDateTime.now()));

        if (announcementsList.isEmpty()) {

        } else {
            for (int i = 0; i < announcementsList.size(); i++) {
                if (i == 0) {
                    FXMLLoader loader = new FXMLLoader(App.class.getResource(Screen.ANNOUNCEMENT.getFilename()));
                    GridPane gridPane = loader.load();
                    setContent(gridPane, announcementsList.get(i));
                    gridPane.setMargin(gridPane,new Insets(10,10,10,10));
                    announcementGridPane.add(gridPane, 0,0);
                    ColumnConstraints newCol = new ColumnConstraints();
                    newCol.setHgrow(Priority.ALWAYS);
                    announcementGridPane.getColumnConstraints().add(i,newCol);

                } else {
                    FXMLLoader loader = new FXMLLoader(App.class.getResource(Screen.ANNOUNCEMENT.getFilename()));
                    GridPane gridPane = loader.load();
                    setContent(gridPane, announcementsList.get(i));
                    gridPane.setMargin(gridPane,new Insets(10,10,10,10));
                    announcementGridPane.addColumn(i, gridPane);
                    ColumnConstraints newCol = new ColumnConstraints();
                    newCol.setHgrow(Priority.ALWAYS);
                    announcementGridPane.getColumnConstraints().add(i,newCol);
                }
            }
        }
    }

    private void setContent(GridPane gridPane, Announcements announcements) {
        List<Node> nodes = gridPane.getChildren();
        for (Node node : nodes) {
            if (node.getClass().equals(Label.class)) {
                if (node.getId().equals("discriptionLabel")) {
                    ((Label) node).setText(announcements.getContent());
                }else if (node.getId().equals("titleLabel")) {
                    ((Label) node).setText(announcements.getTitle());
                } else if (node.getId().equals("dateLabel")) {
                    ((Label) node).setText(DateTimeFormatter.ofPattern("MM/dd/yyyy").format(announcements.getEffectiveDate().toLocalDateTime()));
                }
            }
        }
    }

    private void initEventTable() {
        TableColumn<Invitations, String> location = new TableColumn<>("Location");
        location.setCellValueFactory(new PropertyValueFactory<>("location"));
        location.setPrefWidth(150);
        location.setCellFactory(column -> {
            return new TableCell<Invitations, String>() {
                private final Text text = new Text();

                {
                    text.wrappingWidthProperty().bind(widthProperty());
                    setGraphic(text);
                }

                @Override
                protected void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);

                    if (item == null || empty) {
                        text.setText(null);
                    } else {
                        text.setText(item);
                    }
                }
            };
        });


        TableColumn<Invitations, Date> date = new TableColumn<>("Date");
        date.setCellValueFactory(new PropertyValueFactory<>("date"));
        date.setPrefWidth(75);

        Date currentDate = Date.valueOf(java.time.LocalDate.now());
        ObservableList<Invitations> inviteObservableList = null;
        try {
            inviteObservableList = FXCollections.observableArrayList(DAOFacade.getAllSessionInvitations(UserSessionToken.getUser().getFirstname(), UserSessionToken.getUser().getLastname(), currentDate));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        TableColumn<Invitations, Void> accepted = new TableColumn<>("");
        accepted.setPrefWidth(74);
        accepted.setCellFactory(event -> new TableCell<>() {
            private final MFXButton button = new MFXButton();

            {
                button.setOnAction(event -> {
                    Invitations invitation = getTableView().getItems().get(getIndex());
                    if (invitation.isAccepted()) {
                        invitation.setAccepted(false);
                        button.setText("Accept");
                        button.setStyle("-fx-background-color: green");
                    } else {
                        invitation.setAccepted(true);
                        button.setText("Cancel");
                        button.setStyle("-fx-background-color: red");
                    }
                    try {
                        DAOFacade.updateInvitation(invitation);
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    Invitations invitation = getTableView().getItems().get(getIndex());
                    if (invitation.isAccepted()) {
                        button.setText("Cancel");
                        button.setStyle("-fx-background-color: red");
                    } else {
                        button.setText("Accept");
                        button.setStyle("-fx-background-color: green");
                    }
                    setGraphic(button);
                }
            }
        });

        tableInvitation.getColumns().addAll(accepted, location, date);

        tableInvitation.getItems().addAll(inviteObservableList);
        tableInvitation.setPrefWidth(301);
        tableInvitation.prefHeightProperty().bind(eventsPane.heightProperty());

        App.getPrimaryStage().heightProperty().addListener(((obs, o, n) -> {
            tableInvitation.prefHeightProperty().unbind();
            double scaleY = eventsPane.getHeight() / App.getPrimaryStage().getHeight();
            eventsPane.setMinHeight(scaleY);
        }));
        eventsPane.heightProperty().addListener((observable, oldValue, newValue) -> {
            tableInvitation.prefHeightProperty().unbind();
            eventsPane.getChildren().clear();
            tableInvitation.setMinHeight(newValue.doubleValue());
            tableInvitation.setPrefHeight(newValue.doubleValue());
            eventsPane.getChildren().add(tableInvitation);
        });

        eventsPane.getChildren().clear();
        eventsPane.getChildren().add(tableInvitation);
        tableInvitation.setFocusTraversable(false);
    }

    private void initMoveTable() throws SQLException {

        Line line = new Line(0, 0, 250, 0);
        line.setVisible(true);
        line.setStrokeWidth(1);
        line.setStroke(Color.BLACK);
        titleFlowPane.getChildren().add(line);

        lowerLevel1Image = new ImageView(App.lowerlevel1);
        lowerLevel2Image = new ImageView(App.lowerlevel2);
        floor1Image = new ImageView(App.firstfloor);
        floor2Image = new ImageView(App.secondfloor);
        floor3Image = new ImageView(App.thirdfloor);
        L1Group = new Group(lowerLevel1Image);
        L2Group = new Group(lowerLevel2Image);
        floor1Group = new Group(floor1Image);
        floor2Group = new Group(floor2Image);
        floor3Group = new Group(floor3Image);

        PopOver popOver = new PopOver();
        popOver.setPrefSize(300, 300);
        popOver.setArrowLocation(PopOver.ArrowLocation.TOP_CENTER);

        Date currentDate = Date.valueOf(java.time.LocalDate.now());
        List<Move> allMoves = DAOFacade.getAllMoves();
        List<Move> allFutureMoves = DAOFacade.getAllFutureMoves(currentDate);
        Collections.reverse(allFutureMoves);
        List<FlowPane> moveButtons = new ArrayList<>();

        for (Move moveTo : allFutureMoves) {
            int counter = 0;
            boolean check = false;
            for (Move moveFrom : allMoves) {
                String moveFromLocation = moveFrom.getLocation().getLongName();
                String moveToLocation = moveTo.getLocation().getLongName();
                if (moveFromLocation.equals(moveToLocation) && moveFrom.getNode().getNodeID() == moveTo.getNode().getNodeID()) {
                    check = true;
                }
                if (check) {
                    if (moveFromLocation.equals(moveToLocation)) {
                        counter++;
                    }
                }
                if (counter == 2) {
                    FlowPane flowPane = new FlowPane();
                    MFXButton moveButton = new MFXButton(moveFrom.getLocation().getLongName() + "\nNode " + moveFrom.getNode().getNodeID() + " -> Node " + moveTo.getNode().getNodeID() + "\n" + moveTo.getMoveDate());
                    moveButton.setTextAlignment(TextAlignment.CENTER);
                    moveButton.prefWidthProperty().bind(Bindings.subtract(movesPane.widthProperty(), 20));
                    flowPane.getChildren().add(moveButton);
                    moveButton.setStyle("-fx-border-radius: 10; -fx-border-color: black; -fx-background-radius: 10");
                    flowPane.setMargin(flowPane, new Insets(10, 10, 0, 10));
                    moveButtons.add(flowPane);

                    moveButton.setOnAction(event -> {
                        gesturePane.setPrefSize(popOver.getPrefWidth(), popOver.getPrefHeight());
                        stackPane.getChildren().clear();
                        stackPane.getChildren().addAll(L1Group, L2Group, floor1Group, floor2Group, floor3Group);
                        gesturePane.setContent(stackPane);
                        displayNode(moveTo, gesturePane);
                        popOver.setContentNode(gesturePane);
                        popOver.show(moveButton);
                    });

                    moveButton.setOnMouseEntered(event ->
                    {
                        moveButton.setStyle("-fx-border-radius: 10; -fx-border-color: black; -fx-background-color: rgba(160,160,160,0.6); -fx-background-radius: 10");
                    });

                    moveButton.setOnMouseExited(event ->
                    {
                        moveButton.setStyle("-fx-border-radius: 10; -fx-border-color: black; -fx-background-color: white; -fx-background-radius: 10");
                    });
                    gesturePane.setScrollBarPolicy(GesturePane.ScrollBarPolicy.NEVER);
                    counter = 0;
                }
            }
        }
        Collections.reverse(moveButtons);

        for (FlowPane button : moveButtons) {
            button.setAlignment(Pos.CENTER);
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

        TableColumn<HomeServiceRequests, String> deliveryTime = new TableColumn<>("Time");
        deliveryTime.setCellValueFactory(cellData -> {
            String time = cellData.getValue().getDeliveryTime().toString();
            if (time.equals("00:00:00")) {
                return new SimpleStringProperty("");
            } else {
                return new SimpleStringProperty(time);
            }
        });

        TableColumn<HomeServiceRequests, String> fullNameCol = new TableColumn<>("Patient");
        fullNameCol.setCellValueFactory(cellData -> {
            String lastName = cellData.getValue().getPatientLast();
            String firstName = cellData.getValue().getPatientFirst();
            if (lastName.isEmpty() && firstName.isEmpty()) {
                return new SimpleStringProperty("");
            } else {
                return new SimpleStringProperty(lastName + ", " + firstName);
            }
        });

        TableColumn<HomeServiceRequests, String> items = new TableColumn<>("Item(s)");
        items.setCellValueFactory(new PropertyValueFactory<>("items"));

        TableColumn<HomeServiceRequests, String> location = new TableColumn<>("Location");
        location.setCellValueFactory(new PropertyValueFactory<>("location"));

        ObservableList<HomeServiceRequests> requestObservableList = null;
        try {
            requestObservableList = FXCollections.observableArrayList(DAOFacade.getSessionServiceRequests(UserSessionToken.getUser().getFirstname(), UserSessionToken.getUser().getLastname()));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        TableColumn<HomeServiceRequests, Void> completed = new TableColumn<>("");
        completed.setPrefWidth(100);
        completed.setCellFactory(event -> new TableCell<>() {
            private final MFXButton button = new MFXButton("Complete");

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
                        } else if (request.getRequestType().equals("Supply")) {
                            Supply supply = DAOFacade.getSupply(request.getOrderNum());
                            supply.setStatus(RequestStatus.DONE);
                            DAOFacade.updateSupply(supply);
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


        TableColumn<HomeServiceRequests, String> title = new TableColumn<>("Service Request Table");

        tableServiceRequest.getColumns().addAll(completed, serviceType, items, location, fullNameCol, deliveryDate, deliveryTime);


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
            tableServiceRequest.setMinWidth(requestsPane.getMinWidth());
            tableServiceRequest.setPrefWidth(newValue.doubleValue());
            requestsPane.getChildren().add(tableServiceRequest);
        });

        requestsPane.heightProperty().addListener((observable, oldValue, newValue) -> {
            requestsPane.getChildren().clear();
            tableServiceRequest.setMinHeight(newValue.doubleValue());
            tableServiceRequest.setPrefHeight(newValue.doubleValue());
            requestsPane.getChildren().add(tableServiceRequest);
        });
        tableServiceRequest.setFocusTraversable(false);
    }


    public void displayNode(Move moveFrom, GesturePane gesturePane) {
        L1Group.setVisible(false);
        L2Group.setVisible(false);
        floor1Group.setVisible(false);
        floor2Group.setVisible(false);
        floor3Group.setVisible(false);
        Circle circle = new Circle();

        circle = drawCircle(moveFrom.getNode().getXcoord(), moveFrom.getNode().getYcoord());


        String endFloor = null;
        endFloor = moveFrom.getNode().getFloor();

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
        Platform.runLater(() -> {
            gesturePane.centreOn(centrePoint);
            gesturePane.zoomTo(1, centrePoint);
        });

        gesturePane.setScrollBarPolicy(GesturePane.ScrollBarPolicy.NEVER);
    }

    public Circle drawCircle(double x, double y) {
        Circle circle = new Circle();
        circle.setVisible(true);
        circle.setFill(Color.web(ThemeColors.PATH_NODE_COLOR.getColor()));
        circle.setStroke(Color.web(ThemeColors.GRAY.getColor()));
        circle.setStrokeWidth(3.0f);
        circle.setCenterX(x);
        circle.setCenterY(y);
        circle.setRadius(10.0);
        return circle;
    }
}
