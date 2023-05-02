package edu.wpi.tacticaltritons;

import com.google.common.util.concurrent.AtomicDouble;
import edu.wpi.tacticaltritons.data.FlowerHashMap;
import edu.wpi.tacticaltritons.data.FurnitureHashMap;
import edu.wpi.tacticaltritons.data.QuickNavigationMenuButtons;
import edu.wpi.tacticaltritons.database.*;
import edu.wpi.tacticaltritons.database.Node;
import edu.wpi.tacticaltritons.navigation.Screen;
import edu.wpi.tacticaltritons.pathfinding.AlgorithmSingleton;
import io.github.palexdev.materialfx.controls.*;
import javafx.animation.Animation;
import javafx.animation.RotateTransition;
import javafx.animation.SequentialTransition;
import javafx.animation.TranslateTransition;
import javafx.application.Application;
import javafx.beans.property.*;
import javafx.beans.binding.Bindings;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.fxml.FXMLLoader;
import javafx.geometry.NodeOrientation;
import javafx.geometry.Point2D;
import javafx.geometry.Point3D;
import javafx.geometry.Pos;
import javafx.scene.*;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.*;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.transform.Rotate;
import javafx.stage.Stage;
import javafx.util.Duration;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

@Slf4j
public class App extends Application {

@Getter private static Stage primaryStage;
@Getter private static BorderPane rootPane;
@Getter private static BorderPane navBar;
@Getter private static GridPane loginQuickNavigation;
@Getter private static GridPane staffQuickNavigation;
@Getter private static GridPane adminQuickNavigation;
public static Image groundfloor;
public static Image lowerlevel1;
public static Image lowerlevel2;
public static Image firstfloor;
public static Image secondfloor;
public static Image thirdfloor;
public static Image menuBar;
public static Image pathfinding;
public static Image importExport;

public static String invalidMFXTextField;
public static QuickNavigationMenuButtons quickNavigationMenuButtons;
public static HashMap<String, Image> flowerHashMap;
public static HashMap<String, Image> furnitureHashMap;

@Override
public void init() {
log.info("Starting Up");
}

    @Override
    public void start(Stage primaryStage) throws Exception {
        //    /* primaryStage is generally only used if one of your components require the stage to
        App.primaryStage = primaryStage;
        quickNavigationMenuButtons = new QuickNavigationMenuButtons();
        flowerHashMap = new FlowerHashMap();
        furnitureHashMap = new FurnitureHashMap();


        groundfloor =
        new Image(Objects.requireNonNull(getClass().getResource("images/map_page/00_thegroundfloor.png")).toString());
        lowerlevel1 =
        new Image(Objects.requireNonNull(getClass().getResource("images/map_page/00_thelowerlevel1.png")).toString());
        lowerlevel2 =
        new Image(Objects.requireNonNull(getClass().getResource("images/map_page/00_thelowerlevel2.png")).toString());
        firstfloor =
        new Image(Objects.requireNonNull(getClass().getResource("images/map_page/01_thefirstfloor.png")).toString());
        secondfloor =
        new Image(Objects.requireNonNull(getClass().getResource("images/map_page/02_thesecondfloor.png")).toString());
        thirdfloor = new Image(Objects.requireNonNull(
        getClass().getResource("images/map_page/03_thethirdfloor.png")).toString());

        menuBar =
        new Image(Objects.requireNonNull(getClass().getResource("images/map_page/menu_icon.png")).toString());

        pathfinding =
        new Image(Objects.requireNonNull(getClass().getResource("images/map_page/pathfindingButton.png")).toString());

        importExport = new Image(Objects.requireNonNull(getClass().getResource("images/map_page/export.png")).toString());


        invalidMFXTextField =
        Objects.requireNonNull(getClass().getResource("stylesheets/InvalidMFXTextFields.css")).toString();

        FXMLLoader loader = new FXMLLoader(App.class.getResource(Screen.ROOT.getFilename()));
        rootPane = loader.load();

        loader = new FXMLLoader(App.class.getResource("views/NavigationBar.fxml"));
        navBar = loader.load();

        loader = new FXMLLoader(App.class.getResource("views/navigation/LoginQuickNavigation.fxml"));
        loginQuickNavigation = loader.load();

        loader = new FXMLLoader(App.class.getResource("views/navigation/StaffQuickNavigation.fxml"));
        staffQuickNavigation = loader.load();

        loader = new FXMLLoader(App.class.getResource("views/navigation/AdminQuickNavigation.fxml"));
        adminQuickNavigation = loader.load();

        Image image = new Image(Objects.requireNonNull(App.class.getResource("images/clean_map/map_secondfloor_clean.png")).toString());

        WritableImage writer = new WritableImage(image.getPixelReader(), (int) image.getWidth(), (int) image.getHeight());
        PixelWriter pixelWriter = writer.getPixelWriter();
        PixelReader pixelReader = writer.getPixelReader();


        int[][] matrix = new int[(int) writer.getHeight()][(int) writer.getWidth()];
        for (int i = 0; i < writer.getHeight(); i++) {
            for (int j = 0; j < writer.getWidth(); j++) {
                Color c = pixelReader.getColor(j, i);
                double red = c.getRed() * 255;
                double green = c.getRed() * 255;
                double blue = c.getBlue() * 255;

                if (red < 230 && green < 230 && blue < 230) {
                    matrix[i][j] = 1;
                }
                else {
                    pixelWriter.setColor(j, i, Color.WHITE);
                    matrix[i][j] = 0;
                }
            }
        }

        ImageView iv = new ImageView(writer);
        iv.getTransforms().add(new Rotate(90, Rotate.X_AXIS));

        Rotate yRotate = new Rotate(0, Rotate.Y_AXIS);
        Rotate xRotate = new Rotate(90, Rotate.X_AXIS);

        StringProperty floorNumber = new SimpleStringProperty("2");
        List<Node> nodes = new ArrayList<>(DAOFacade.getAllCurrentMoves(Date.valueOf(LocalDate.now())).parallelStream()
//                        .filter(move -> !move.getLocation().getNodeType().equals("HALL"))
                .map(Move::getNode)
                .filter(node -> node.getFloor().equals(floorNumber.get())).toList());


        // Create and position camera
        DoubleProperty cameraX = new SimpleDoubleProperty(computeMidPoint(nodes.parallelStream().map(Node::getXcoord).toList()));
        double std = computeSTD(nodes.parallelStream().map(node -> new Point2D(node.getXcoord(), node.getYcoord())).toList());
        System.out.println(std);
        DoubleProperty cameraY = new SimpleDoubleProperty(3 * std);
        DoubleProperty cameraZ = new SimpleDoubleProperty(computeMidPoint(nodes.parallelStream().map(Node::getYcoord).toList()));
        PerspectiveCamera camera = new PerspectiveCamera(true);
        camera.getTransforms().addAll(
                yRotate,
                xRotate);
        camera.translateXProperty().bindBidirectional(cameraX);
        camera.translateYProperty().bindBidirectional(cameraY);
        camera.translateZProperty().bindBidirectional(cameraZ);
        camera.setNearClip(1);
        camera.setFarClip(12000);


        // Build the Scene Graph
        Group root = new Group();
        VBox sidePane = new VBox();

        root.getChildren().add(camera);

        Map<Node, LocationName> locations = new HashMap<>();
        List<Move> moves = new ArrayList<>(DAOFacade.getAllCurrentMoves(Date.valueOf(LocalDate.now())));
        nodes.forEach(node -> locations.put(node, moves.parallelStream().filter(move -> move.getNode().getNodeID() == node.getNodeID()).map(Move::getLocation).toList().get(0)));
        Map<String, Color> nodeColors = new HashMap<>();
        nodeColors.put("BATH", Color.MEDIUMSLATEBLUE);
        nodeColors.put("CONF", Color.ORANGERED);
        nodeColors.put("DEPT", Color.GREEN);
        nodeColors.put("ELEV", Color.PURPLE);
        nodeColors.put("EXIT", Color.RED);
        nodeColors.put("HALL", Color.BLACK);
        nodeColors.put("INFO", Color.GOLDENROD);
        nodeColors.put("LABS", Color.MAGENTA);
        nodeColors.put("REST", Color.MEDIUMSLATEBLUE);
        nodeColors.put("RETL", Color.TAN);
        nodeColors.put("SERV", Color.SALMON);
        nodeColors.put("STAI", Color.SILVER);
        AtomicReference<List<Node>> walkingPath = new AtomicReference<>(new ArrayList<>());
        List<Node> pathToCompute = new ArrayList<>();
        List<Box> visibleNodes = new ArrayList<>();

        BooleanProperty pathFinding = new SimpleBooleanProperty(false);

        List<AtomicBoolean> permVisibleTexts = new ArrayList<>();

        MFXButton newPathButton = new MFXButton("New Path");
        newPathButton.disableProperty().bind(pathFinding.not());
        newPathButton.setOnAction(event -> {
            root.getChildren().removeIf(i -> i.getId() != null && i.getId().equals("pathBlock"));
            root.getChildren().removeIf(i -> i.getId() != null && i.getId().contains("bNode"));
            root.getChildren().forEach(node -> {
                if(node.getId() != null && node.getId().contains("nodeText")){
                    node.setVisible(false);
                }
            });
            visibleNodes.forEach(box -> {
                int id = Integer.parseInt(box.getId().substring(box.getId().lastIndexOf("e") + 1));
                Node node = nodes.parallelStream().filter(n -> n.getNodeID() == id).toList().get(0);
                box.setMaterial(new PhongMaterial(nodeColors.get(locations.get(node).getNodeType())));
            });
            permVisibleTexts.forEach(bool -> bool.set(false));
            permVisibleTexts.clear();
            walkingPath.set(null);
            root.getChildren().addAll(visibleNodes);
        });
        sidePane.getChildren().add(newPathButton);

        HBox searchBox = new HBox();
        MFXFilterComboBox<String> nodeSearchBox = new MFXFilterComboBox<>();
        nodeSearchBox.setPromptText("Find a Location");
        nodeSearchBox.getItems().addAll(locations.values().parallelStream().map(LocationName::getLongName).toList());
        nodeSearchBox.setPrefWidth(200);
        nodeSearchBox.getSelectionModel().selectedItemProperty().addListener((obs, o, n) -> {
            locations.forEach((k, v) -> {
                if(v.getLongName().equals(n)){
                    cameraZ.set(k.getYcoord() + 500);
                    cameraX.set(k.getXcoord());
                    cameraY.set(500);
                    yRotate.setAngle(0);
                    xRotate.setAngle(135);

                    root.getChildren().parallelStream().forEach(node -> {
                        if(node.getId() != null && node.getId().contains("nodeText" + k.getNodeID())){
                            node.setVisible(true);
                        }
                    });
                }
            });
            nodeSearchBox.getSelectionModel().clearSelection();
        });
        nodeSearchBox.setDisable(true);
        nodeSearchBox.setOnHidden(event -> nodeSearchBox.setDisable(true));

        MFXButton searchButton = new MFXButton("Search");
        searchButton.setOnAction(event -> {
            if(nodeSearchBox.isDisable()){
                nodeSearchBox.setDisable(false);
                nodeSearchBox.requestFocus();
                nodeSearchBox.show();
            }
            else{
                if(nodeSearchBox.getSelectedItem() != null){
                    locations.forEach((k, v) -> {
                        if(v.getLongName().equals(nodeSearchBox.getSelectedItem())){
                            cameraZ.set(k.getYcoord() + 500);
                            cameraX.set(k.getXcoord());
                            cameraY.set(500);
                            yRotate.setAngle(0);
                            xRotate.setAngle(135);

                            root.getChildren().parallelStream().forEach(node -> {
                                if(node.getId() != null && node.getId().contains("nodeText" + k.getNodeID())){
                                    node.setVisible(true);
                                }
                            });
                        }
                    });
                    nodeSearchBox.getSelectionModel().clearSelection();
                }
            }
        });
        searchBox.getChildren().add(nodeSearchBox);
        searchBox.getChildren().add(searchButton);
        sidePane.getChildren().add(0, searchBox);

        //drawing nodes
        nodes.forEach(node -> {
            if(!locations.get(node).getNodeType().equals("HALL")) {
                Text t = new Text(locations.get(node).getShortName());
                Rectangle rect = new Rectangle();
                t.setId("nodeText"+node.getNodeID());
                Box s = new Box(10, 10, 10);
                AtomicBoolean permVisibleText = new AtomicBoolean(false);
                s.setOnMouseClicked(event -> {
                    //pathfinding
                    if (pathFinding.get()) {
                        permVisibleTexts.add(permVisibleText);
                        permVisibleText.set(true);
                        t.setVisible(true);
                        //starting point
                        s.setMaterial(new PhongMaterial(Color.BLACK));
                        if (pathToCompute.size() == 0) {
                            pathToCompute.add(node);
                        }
                        //ending point
                        else {
                            Node start = pathToCompute.get(0);

                            //removing old paths
                            root.getChildren().removeIf(i -> i.getId() != null && i.getId().equals("pathBlock"));
                            root.getChildren().removeIf(i -> i.getId() != null && i.getId().contains("bNode")
                                    && !i.getId().contains(String.valueOf(start.getNodeID()))
                                    && !i.getId().contains(String.valueOf(node.getNodeID())));

                            try {
                                //Pathfinding
                                List<Node> path = AlgorithmSingleton.getInstance().algorithm.findShortestPath(start, node);
                                walkingPath.set(path);

                                for (int i = 0; i < path.size() - 1; i++) {

                                    //formula
                                    double distance = Math.sqrt(Math.pow(path.get(i + 1).getXcoord() - path.get(i).getXcoord(), 2) +
                                            Math.pow(path.get(i + 1).getYcoord() - path.get(i).getYcoord(), 2));

                                    double opp = path.get(i + 1).getXcoord() - path.get(i).getXcoord();
                                    double adj = path.get(i + 1).getYcoord() - path.get(i).getYcoord();
                                    double angle = Math.atan(opp / adj) * (180 / Math.PI);

                                    Box line = new Box(5, 5, distance);
                                    line.setId("pathBlock");
                                    line.setTranslateZ(path.get(i).getYcoord() + adj / 2);
                                    line.setTranslateX(path.get(i).getXcoord() + opp / 2);
                                    line.getTransforms().add(new Rotate(angle, Rotate.Y_AXIS));
                                    line.setMaterial(new PhongMaterial(Color.BLUE));

                                    root.getChildren().add(line);
                                }

                            } catch (SQLException e) {
                                throw new RuntimeException(e);
                            }
                            pathToCompute.clear();
                        }
                    }
                    else {
                        if(!permVisibleText.get()) {
                            permVisibleTexts.add(permVisibleText);
                            permVisibleText.set(true);
                            t.setVisible(true);

                            t.setFont(new Font(20));
                            t.setTranslateX(node.getXcoord() - (t.getLayoutBounds().getWidth() / 2));

                            Text lt = new Text(locations.get(node).getLongName());
                            lt.setId("dNode" + node.getNodeID());
                            lt.setFont(new Font(24));
                            lt.setTranslateZ(node.getYcoord());
                            lt.setTranslateX(node.getXcoord() - (lt.getLayoutBounds().getWidth() / 2));
                            lt.getTransforms().addAll(t.getTransforms());

                            Text nXY = new Text("x: " + node.getXcoord() + ", y: " + node.getYcoord());
                            nXY.setFont(new Font(20));
                            nXY.setId("dNode" + node.getNodeID());
                            nXY.setTranslateZ(node.getYcoord());
                            nXY.setTranslateX(node.getXcoord() - (nXY.getLayoutBounds().getWidth() / 2));
                            nXY.getTransforms().addAll(t.getTransforms());

                            Text lty = new Text("Type: " + locations.get(node).getNodeType());
                            lty.setId("dNode" + node.getNodeID());
                            lty.setFont(new Font(20));
                            lty.setTranslateZ(node.getYcoord());
                            lty.setTranslateX(node.getXcoord() - (lty.getLayoutBounds().getWidth() / 2));
                            lty.getTransforms().addAll(t.getTransforms());

                            double newRectWidth = computeMax(t.getLayoutBounds().getWidth(),
                                    lt.getLayoutBounds().getWidth(),
                                    nXY.getLayoutBounds().getWidth(),
                                    lty.getLayoutBounds().getWidth()) + 15;
                            double newRectHeight = computeSum(t.getLayoutBounds().getHeight(),
                                    lt.getLayoutBounds().getHeight(),
                                    nXY.getLayoutBounds().getHeight(),
                                    lty.getLayoutBounds().getHeight()) + 30;

                            lt.setTranslateY(t.getTranslateY() + newRectHeight - lt.getLayoutBounds().getHeight());
                            t.setTranslateY(lt.getTranslateY() - t.getLayoutBounds().getHeight() - 5);
                            lty.setTranslateY(t.getTranslateY() - lty.getLayoutBounds().getHeight() - 5);
                            nXY.setTranslateY(lty.getTranslateY() - nXY.getLayoutBounds().getHeight() - 5);


                            double newTranslateY = rect.getTranslateY() - rect.getHeight() + newRectHeight;
                            double newTranslateX = node.getXcoord() - (newRectWidth / 2);
                            rect.setWidth(newRectWidth);
                            rect.setHeight(newRectHeight);
                            rect.setTranslateY(newTranslateY);
                            rect.setTranslateX(newTranslateX);
                            root.getChildren().addAll(nXY, lty, lt);
                        }
                        else {
                            root.getChildren().removeIf(i -> i.getId() != null && i.getId().equals("dNode" + node.getNodeID()));
                            permVisibleText.set(false);
                            permVisibleTexts.remove(permVisibleText);
                            t.setVisible(false);

                            t.setFont(new Font(24));
                            t.setTranslateX(node.getXcoord() - (t.getLayoutBounds().getWidth() / 2));

                            rect.setTranslateY(100);
                            rect.setWidth(t.getLayoutBounds().getWidth() + 20);
                            rect.setHeight(t.getLayoutBounds().getHeight());
                            rect.setTranslateX(node.getXcoord() - (rect.getWidth() / 2));

                            t.setTranslateY(80);
                        }
                    }
                });
                s.setOnMouseEntered(event -> {
                    if(!permVisibleText.get()) {
                        t.setVisible(true);
                    }
                });
                s.setOnMouseExited(event -> {
                    if(!permVisibleText.get()) {
                        t.setVisible(false);
                    }
                });
                s.setId("bNode"+node.getNodeID());
                s.setMaterial(new PhongMaterial(nodeColors.get(locations.get(node).getNodeType())));
                s.setTranslateZ(node.getYcoord());
                s.setTranslateX(node.getXcoord());
                visibleNodes.add(s);

                //drawing the floating text
                t.getTransforms().addAll(
                        new Rotate(180, Rotate.Z_AXIS),
                        new Rotate(180, Rotate.Y_AXIS));
                t.setVisible(false);
                t.setFont(new Font(24));
                t.setTranslateZ(node.getYcoord());
                t.setTranslateX(node.getXcoord() - (t.getLayoutBounds().getWidth() / 2));
                t.setTranslateY(75);


                rect.setId("nodeRect" + node.getNodeID());
                rect.setWidth(t.getLayoutBounds().getWidth() + 20);
                rect.setHeight(t.getLayoutBounds().getHeight());
                rect.setTranslateZ(node.getYcoord() - 5);
                rect.setTranslateX(node.getXcoord() - (rect.getWidth() / 2));
                rect.setTranslateY(95);
                rect.setFill(Color.WHITE);
                rect.setStroke(Color.BLACK);
                rect.setStrokeWidth(5);
                rect.visibleProperty().bind(t.visibleProperty());
                rect.getTransforms().addAll(t.getTransforms());

                t.layoutYProperty().bind(xRotate.pivotXProperty());
                t.setRotationAxis(Rotate.Y_AXIS);
                t.rotateProperty().bind(yRotate.angleProperty());
                t.getTransforms().addAll(xRotate, new Rotate(180, Rotate.X_AXIS));

                root.getChildren().addAll(s, t, rect);
            }
        });


        // Use a SubScene
        SubScene subScene = new SubScene(
        root, 1280, 720,
        true,
        SceneAntialiasing.BALANCED);
        subScene.setFill(Color.ALICEBLUE);
        subScene.setCamera(camera);
        Group group = new Group();
        group.getChildren().add(subScene);

        BooleanProperty movement = new SimpleBooleanProperty(false);
        //mouse drag event
        List<Point2D> points = new ArrayList<>();
        subScene.setOnMouseDragged(event -> {
            if(!movement.get()) {
                if (points.size() == 0) {
                    //initial point
                    points.add(new Point2D(event.getSceneX(), event.getSceneY()));
                }
                else {
                    //second point
                    Point2D secondPoint = new Point2D(event.getSceneX(), event.getSceneY());

                    //formula
                    double hyp = points.get(0).distance(secondPoint);
                    double opp = Math.abs(secondPoint.getY() - points.get(0).getY());
                    double adj = Math.abs(secondPoint.getX() - points.get(0).getX());

                    double magnitude = (hyp * 2.5) / Math.sqrt(subScene.getWidth() * subScene.getHeight());

                    double xMultiplier = secondPoint.getY() < points.get(0).getY() ? 1 : -1;
                    double xAngle = magnitude * xMultiplier * Math.atan(opp / adj) * (180 / Math.PI);

                    double yMultiplier = secondPoint.getX() < points.get(0).getX() ? -1 : 1;
                    double yAngle = magnitude * yMultiplier * Math.atan(adj / opp) * (180 / Math.PI);

                    //updating the transform
                    camera.getTransforms().stream().filter(node -> (node instanceof Rotate)).forEach(node -> {
                        if (((Rotate) node).getAxis().equals(Rotate.X_AXIS) && !Double.isNaN(xAngle)) {
                            if (((Rotate) node).getAngle() >= 90 && ((Rotate) node).getAngle() <= 170) {
                                if ((((Rotate) node).getAngle() + xAngle >= 90) && (((Rotate) node).getAngle() + xAngle <= 170)) {
                                    if (Math.abs(xAngle) < 30) {
                                        ((Rotate) node).setAngle((((Rotate) node).getAngle() + xAngle) % 360);
                                    }
                                }
                            }
                        }
                        else if (((Rotate) node).getAxis().equals(Rotate.Y_AXIS) && !Double.isNaN(yAngle)) {
                            if (Math.abs(yAngle) < 30) {
                                ((Rotate) node).setAngle((((Rotate) node).getAngle() + yAngle) % 360);
                            }
                        }
                    });

                    points.clear();
                }
            }
            else{
                if (points.size() == 0) {
                    //initial point
                    points.add(new Point2D(event.getSceneX(), event.getSceneY()));
                }
                else {
                    //second point
                    Point2D secondPoint = new Point2D(event.getSceneX(), event.getSceneY());

                    //formula
                    double opp = secondPoint.getY() - points.get(0).getY();
                    double adj = secondPoint.getX() - points.get(0).getX();

                    double magnitude = (subScene.getWidth() + subScene.getHeight()) / Math.sqrt(subScene.getWidth() * subScene.getHeight());

                    if(Math.abs(opp) < 50 && Math.abs(adj) < 50) {
                        cameraZ.set(cameraZ.get() - opp * magnitude);
                        cameraX.set(cameraX.get() - adj * magnitude);
                    }

                    points.clear();
                }
            }
        });

        root.getChildren().add(iv);

        //pane stuff
        AnchorPane pane = new AnchorPane();
        pane.setPrefSize(1280,720);
        MFXToggleButton pathFindingToggle = new MFXToggleButton("Path Finding");
        pathFindingToggle.setStyle("-fx-text-fill: blue");
        pathFindingToggle.selectedProperty().bindBidirectional(pathFinding);
        sidePane.getChildren().add(1, pathFindingToggle);

        MFXToggleButton wallsToggle = new MFXToggleButton("Walls");
        wallsToggle.setStyle("-fx-text-fill: blue");
        wallsToggle.selectedProperty().addListener((obs, o, n) -> {
            if(n){
//                for (int i = 0; i < matrix.length; i++) {
//                    for (int j = 0; j < matrix[i].length; j++) {
//                        if (matrix[i][j] == 1) {
//                            if (i< matrix.length-1 && j<matrix[i].length-1) {
//                                if (matrix[i + 1][j] == 0 || matrix[i + 1][j + 1] == 0 || matrix[i][j + 1] == 0) {
//                                    Box b = new Box(1, 30, 1);
//                                    b.setTranslateZ(i);
//                                    b.setTranslateX(j);
//                                    b.setMaterial(new PhongMaterial(Color.web("#A17A4B")));
//                                    b.setId("wall");
//                                    root.getChildren().add(b);
//                                }
//                            }
//                            if(i>1 && j>1){
//                                if(matrix[i - 1][j] == 0 || matrix[i - 1][j - 1] == 0 || matrix[i][j - 1] == 0){
//                                    Box b = new Box(1, 30, 1);
//                                    b.setTranslateZ(i);
//                                    b.setTranslateX(j);
//                                    b.setMaterial(new PhongMaterial(Color.web("#A17A4B")));
//                                    b.setId("wall");
//                                    root.getChildren().add(b);
//                                }
//                            }
//                        }
//                    }
//                }
                for (int i = 0; i < matrix.length; i++) {
                    for (int j = 0; j < matrix[i].length; j++) {
                        if (matrix[i][j] == 1) {
                            int wallWidth = 0;
                            for (int k = j; k < matrix[i].length; k++) {
                                if (matrix[i][k] == 1) {
                                    wallWidth++;
                                } else {
                                    break;
                                }
                            }
                            if (wallWidth >= 2) {
                                Box b = new Box(wallWidth, 30, 1);
                                b.setTranslateZ(i);
                                b.setTranslateX(j + wallWidth/2.0);
                                b.setMaterial(new PhongMaterial(Color.web("#A17A4B")));
                                b.setId("wall");
                                root.getChildren().add(b);
                                j += wallWidth;
                            }
                        }
                    }
                }

        }
            else{
                root.getChildren().removeIf(node -> node.getId() != null && node.getId().equals("wall"));
            }
        });
        sidePane.getChildren().add(wallsToggle);

        MFXToggleButton movementToggle = new MFXToggleButton("Toggle Movement");
        movementToggle.setStyle("-fx-text-fill: blue");
        movementToggle.selectedProperty().bindBidirectional(movement);
        sidePane.getChildren().add(1, movementToggle);

        HBox floorToggles = new HBox();
        ToggleGroup floor = new ToggleGroup();
        MFXRadioButton lower2 = new MFXRadioButton("L2");
        floor.getToggles().add(lower2);
        lower2.selectedProperty().addListener((obs, o, n) -> {
            if(n){
                root.getChildren().removeIf(i -> i.getId() != null && i.getId().equals("pathBlock"));
                root.getChildren().removeIf(i -> i.getId() != null && (i.getId().contains("Node") || i.getId().contains("node")));

                Image img = new Image(Objects.requireNonNull(App.class.getResource("images/clean_map/map_lowerlevel_2_clean.png")).toString());
                WritableImage wri = new WritableImage(img.getPixelReader(), (int) img.getWidth(), (int) img.getHeight());
                PixelWriter pixWri = writer.getPixelWriter();
                PixelReader pixRea = writer.getPixelReader();

                for (int i = 0; i < wri.getHeight(); i++) {
                    for (int j = 0; j < wri.getWidth(); j++) {
                        Color c = pixRea.getColor(j, i);
                        double red = c.getRed() * 255;
                        double green = c.getRed() * 255;
                        double blue = c.getBlue() * 255;

                        if (red < 230 && green < 230 && blue < 230) {
                            matrix[i][j] = 1;
                        }
                        else {
                            pixWri.setColor(j, i, Color.WHITE);
                            matrix[i][j] = 0;
                        }
                    }
                }

                iv.setImage(wri);

                floorNumber.set("L2");
                nodes.clear();
                try {
                    nodes.addAll(DAOFacade.getAllCurrentMoves(Date.valueOf(LocalDate.now())).parallelStream()
                            .map(Move::getNode)
                            .filter(node -> node.getFloor().equals(String.valueOf(floorNumber.get()))).toList());
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
                cameraX.set(computeMidPoint(nodes.parallelStream().map(Node::getXcoord).toList()));
                cameraY.set(3 * computeSTD(nodes.parallelStream().map(node -> new Point2D(node.getXcoord(), node.getYcoord())).toList()));
                cameraZ.set(computeMidPoint(nodes.parallelStream().map(Node::getYcoord).toList()));

                locations.clear();
                moves.clear();
                try {
                    moves.addAll(DAOFacade.getAllCurrentMoves(Date.valueOf(LocalDate.now())));
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
                nodes.forEach(node -> locations.put(node, moves.parallelStream().filter(move -> move.getNode().getNodeID() == node.getNodeID()).map(Move::getLocation).toList().get(0)));
                walkingPath.set(null);
                pathToCompute.clear();
                visibleNodes.clear();
                pathFinding.set(false);

                permVisibleTexts.clear();
                nodeSearchBox.getItems().clear();
                nodeSearchBox.getItems().addAll(locations.values().parallelStream().map(LocationName::getLongName).toList());

                nodes.forEach(node -> {
                    if(!locations.get(node).getNodeType().equals("HALL")) {
                        Text t = new Text(locations.get(node).getShortName());
                        Rectangle rect = new Rectangle();
                        t.setId("nodeText"+node.getNodeID());
                        Box s = new Box(10, 10, 10);
                        AtomicBoolean permVisibleText = new AtomicBoolean(false);
                        s.setOnMouseClicked(event -> {
                            //pathfinding
                            if (pathFinding.get()) {
                                permVisibleTexts.add(permVisibleText);
                                permVisibleText.set(true);
                                t.setVisible(true);
                                //starting point
                                s.setMaterial(new PhongMaterial(Color.BLACK));
                                if (pathToCompute.size() == 0) {
                                    pathToCompute.add(node);
                                }
                                //ending point
                                else {
                                    Node start = pathToCompute.get(0);

                                    //removing old paths
                                    root.getChildren().removeIf(i -> i.getId() != null && i.getId().equals("pathBlock"));
                                    root.getChildren().removeIf(i -> i.getId() != null && i.getId().contains("bNode")
                                            && !i.getId().contains(String.valueOf(start.getNodeID()))
                                            && !i.getId().contains(String.valueOf(node.getNodeID())));

                                    try {
                                        //Pathfinding
                                        List<Node> path = AlgorithmSingleton.getInstance().algorithm.findShortestPath(start, node);
                                        walkingPath.set(path);

                                        for (int i = 0; i < path.size() - 1; i++) {

                                            //formula
                                            double distance = Math.sqrt(Math.pow(path.get(i + 1).getXcoord() - path.get(i).getXcoord(), 2) +
                                                    Math.pow(path.get(i + 1).getYcoord() - path.get(i).getYcoord(), 2));

                                            double opp = path.get(i + 1).getXcoord() - path.get(i).getXcoord();
                                            double adj = path.get(i + 1).getYcoord() - path.get(i).getYcoord();
                                            double angle = Math.atan(opp / adj) * (180 / Math.PI);

                                            Box line = new Box(5, 5, distance);
                                            line.setId("pathBlock");
                                            line.setTranslateZ(path.get(i).getYcoord() + adj / 2);
                                            line.setTranslateX(path.get(i).getXcoord() + opp / 2);
                                            line.getTransforms().add(new Rotate(angle, Rotate.Y_AXIS));
                                            line.setMaterial(new PhongMaterial(Color.BLUE));

                                            root.getChildren().add(line);
                                        }

                                    } catch (SQLException e) {
                                        throw new RuntimeException(e);
                                    }
                                    pathToCompute.clear();
                                }
                            }
                            else {
                                if(!permVisibleText.get()) {
                                    permVisibleTexts.add(permVisibleText);
                                    permVisibleText.set(true);
                                    t.setVisible(true);

                                    t.setFont(new Font(20));
                                    t.setTranslateX(node.getXcoord() - (t.getLayoutBounds().getWidth() / 2));

                                    Text lt = new Text(locations.get(node).getLongName());
                                    lt.setId("dNode" + node.getNodeID());
                                    lt.setFont(new Font(24));
                                    lt.setTranslateZ(node.getYcoord());
                                    lt.setTranslateX(node.getXcoord() - (lt.getLayoutBounds().getWidth() / 2));
                                    lt.getTransforms().addAll(t.getTransforms());

                                    Text nXY = new Text("x: " + node.getXcoord() + ", y: " + node.getYcoord());
                                    nXY.setFont(new Font(20));
                                    nXY.setId("dNode" + node.getNodeID());
                                    nXY.setTranslateZ(node.getYcoord());
                                    nXY.setTranslateX(node.getXcoord() - (nXY.getLayoutBounds().getWidth() / 2));
                                    nXY.getTransforms().addAll(t.getTransforms());

                                    Text lty = new Text("Type: " + locations.get(node).getNodeType());
                                    lty.setId("dNode" + node.getNodeID());
                                    lty.setFont(new Font(20));
                                    lty.setTranslateZ(node.getYcoord());
                                    lty.setTranslateX(node.getXcoord() - (lty.getLayoutBounds().getWidth() / 2));
                                    lty.getTransforms().addAll(t.getTransforms());

                                    double newRectWidth = computeMax(t.getLayoutBounds().getWidth(),
                                            lt.getLayoutBounds().getWidth(),
                                            nXY.getLayoutBounds().getWidth(),
                                            lty.getLayoutBounds().getWidth()) + 15;
                                    double newRectHeight = computeSum(t.getLayoutBounds().getHeight(),
                                            lt.getLayoutBounds().getHeight(),
                                            nXY.getLayoutBounds().getHeight(),
                                            lty.getLayoutBounds().getHeight()) + 30;

                                    lt.setTranslateY(t.getTranslateY() + newRectHeight - lt.getLayoutBounds().getHeight());
                                    t.setTranslateY(lt.getTranslateY() - t.getLayoutBounds().getHeight() - 5);
                                    lty.setTranslateY(t.getTranslateY() - lty.getLayoutBounds().getHeight() - 5);
                                    nXY.setTranslateY(lty.getTranslateY() - nXY.getLayoutBounds().getHeight() - 5);


                                    double newTranslateY = rect.getTranslateY() - rect.getHeight() + newRectHeight;
                                    double newTranslateX = node.getXcoord() - (newRectWidth / 2);
                                    rect.setWidth(newRectWidth);
                                    rect.setHeight(newRectHeight);
                                    rect.setTranslateY(newTranslateY);
                                    rect.setTranslateX(newTranslateX);
                                    root.getChildren().addAll(nXY, lty, lt);
                                }
                                else {
                                    root.getChildren().removeIf(i -> i.getId() != null && i.getId().equals("dNode" + node.getNodeID()));
                                    permVisibleText.set(false);
                                    permVisibleTexts.remove(permVisibleText);
                                    t.setVisible(false);

                                    t.setFont(new Font(24));
                                    t.setTranslateX(node.getXcoord() - (t.getLayoutBounds().getWidth() / 2));

                                    rect.setTranslateY(100);
                                    rect.setWidth(t.getLayoutBounds().getWidth() + 20);
                                    rect.setHeight(t.getLayoutBounds().getHeight());
                                    rect.setTranslateX(node.getXcoord() - (rect.getWidth() / 2));

                                    t.setTranslateY(80);
                                }
                            }
                        });
                        s.setOnMouseEntered(event -> {
                            if(!permVisibleText.get()) {
                                t.setVisible(true);
                            }
                        });
                        s.setOnMouseExited(event -> {
                            if(!permVisibleText.get()) {
                                t.setVisible(false);
                            }
                        });
                        s.setId("bNode"+node.getNodeID());
                        s.setMaterial(new PhongMaterial(nodeColors.get(locations.get(node).getNodeType())));
                        s.setTranslateZ(node.getYcoord());
                        s.setTranslateX(node.getXcoord());
                        visibleNodes.add(s);

                        //drawing the floating text
                        t.getTransforms().addAll(
                                new Rotate(180, Rotate.Z_AXIS),
                                new Rotate(180, Rotate.Y_AXIS));
                        t.setVisible(false);
                        t.setFont(new Font(24));
                        t.setTranslateZ(node.getYcoord());
                        t.setTranslateX(node.getXcoord() - (t.getLayoutBounds().getWidth() / 2));
                        t.setTranslateY(75);


                        rect.setId("nodeRect" + node.getNodeID());
                        rect.setWidth(t.getLayoutBounds().getWidth() + 20);
                        rect.setHeight(t.getLayoutBounds().getHeight());
                        rect.setTranslateZ(node.getYcoord() - 5);
                        rect.setTranslateX(node.getXcoord() - (rect.getWidth() / 2));
                        rect.setTranslateY(95);
                        rect.setFill(Color.WHITE);
                        rect.setStroke(Color.BLACK);
                        rect.setStrokeWidth(5);
                        rect.visibleProperty().bind(t.visibleProperty());
                        rect.getTransforms().addAll(t.getTransforms());

                        root.getChildren().addAll(s, t, rect);
                    }
                });
            }
        });
        floorToggles.getChildren().add(lower2);

        MFXRadioButton lower1 = new MFXRadioButton("L1");
        floor.getToggles().add(lower1);
        lower1.selectedProperty().addListener((obs, o, n) -> {

        });
        floorToggles.getChildren().add(lower1);

        MFXRadioButton floor1 = new MFXRadioButton("1");
        floor.getToggles().add(floor1);
        floorToggles.getChildren().add(floor1);

        MFXRadioButton floor2 = new MFXRadioButton("2");
        floor.getToggles().add(floor2);
        floorToggles.getChildren().add(floor2);

        MFXRadioButton floor3 = new MFXRadioButton("3");
        floor.getToggles().add(floor3);
        floorToggles.getChildren().add(floor3);

        floorToggles.setAlignment(Pos.CENTER);
        floorToggles.setSpacing(10);
        sidePane.getChildren().add(floorToggles);

        Text filterHeader = new Text("Filters");
        filterHeader.setFont(new Font(24));
        sidePane.getChildren().add(filterHeader);

        HBox filterLine1 = new HBox();
        filterLine1.setSpacing(25);

        MFXCheckbox restroomFilter = new MFXCheckbox("Restrooms");
        restroomFilter.setTextFill(Color.MEDIUMSLATEBLUE);
        restroomFilter.setSelected(true);
        restroomFilter.selectedProperty().addListener((obs, o, n) -> {
            if (n) {
                root.getChildren().forEach(node -> {
//                    if(node.getId() != null && )
                });
            }
            else{

            }
        });
        filterLine1.getChildren().add(restroomFilter);

        sidePane.setStyle("-fx-background-color: white; -fx-border-color: black; -fx-min-width: 300; -fx-min-height: 720");
        sidePane.setAlignment(Pos.TOP_CENTER);
        BooleanProperty menuShowing = new SimpleBooleanProperty(true);
        MFXButton transportMenu = new MFXButton("hide");
        transportMenu.setOnAction(event -> {
            if(menuShowing.get()){
                menuShowing.set(false);
                transportMenu.setText("hide");
                TranslateTransition transition = new TranslateTransition();
                transition.setNode(sidePane);
                transition.setByX(-sidePane.getWidth());
                transition.setDuration(Duration.millis(500));
                transition.play();
                transition.setOnFinished(e -> {
                    sidePane.getChildren().remove(transportMenu);
                    transportMenu.setLayoutX(0);
                    pane.getChildren().add(transportMenu);
                });
            }
            else {
                menuShowing.set(true);
                transportMenu.setText("show");
                TranslateTransition transition = new TranslateTransition();
                transition.setNode(sidePane);
                transition.setByX(sidePane.getWidth());
                transition.setDuration(Duration.millis(500));
                transition.play();
                pane.getChildren().remove(transportMenu);
                sidePane.getChildren().add(0, transportMenu);
            }
        });
        transportMenu.setAlignment(Pos.CENTER_RIGHT);
        sidePane.getChildren().add(0, transportMenu);

        pane.getChildren().add(sidePane);
        pane.getChildren().add(group);
        group.toBack();

        final Scene scene = new Scene(pane);

        AtomicInteger nodeCounter = new AtomicInteger();
        AtomicReference<SequentialTransition> pathTransition = new AtomicReference<>();
        scene.setOnKeyPressed(event -> {
            double sceneSize = Math.sqrt(scene.getWidth() * scene.getHeight());
            double screenIncrement = sceneSize / 5;

            double angleY = Math.toRadians(yRotate.getAngle());
            double dxForward = screenIncrement * Math.sin(angleY);
            double dzForward = screenIncrement * Math.cos(angleY);
            double dxNormal = screenIncrement * Math.cos(angleY);
            double dzNormal = screenIncrement * -1 * Math.sin(angleY);

            if (event.isShiftDown() && event.getCode() == KeyCode.EQUALS) {
                camera.setTranslateY(camera.getTranslateY() + screenIncrement);
            }
            else if (event.isShiftDown() && event.getCode() == KeyCode.MINUS) {
                camera.setTranslateY(camera.getTranslateY() - screenIncrement);
            }
            else if (event.getCode() == KeyCode.W) {
                double forwardDir = -1;
                double normalDir = 0;

                cameraX.set(cameraX.get() + forwardDir * dxForward + normalDir * dxNormal);
                cameraZ.set(cameraZ.get() + forwardDir * dzForward + normalDir * dzNormal);
            }
            else if (event.getCode() == KeyCode.D) {
                double forwardDir = 0;
                double normalDir = 1;

                cameraX.set(cameraX.get() + forwardDir * dxForward + normalDir * dxNormal);
                cameraZ.set(cameraZ.get() + forwardDir * dzForward + normalDir * dzNormal);
            }
            else if (event.getCode() == KeyCode.S) {
                double forwardDir = 1;
                double normalDir = 0;

                cameraX.set(cameraX.get() + forwardDir * dxForward + normalDir * dxNormal);
                cameraZ.set(cameraZ.get() + forwardDir * dzForward + normalDir * dzNormal);
            }
            else if (event.getCode() == KeyCode.A) {
                double forwardDir = 0;
                double normalDir = -1;

                cameraX.set(cameraX.get() + forwardDir * dxForward + normalDir * dxNormal);
                cameraZ.set(cameraZ.get() + forwardDir * dzForward + normalDir * dzNormal);
            }
            else if (event.getCode() == KeyCode.L && pathTransition.get() == null) {
                //Move camera to node start
                camera.translateXProperty().unbindBidirectional(cameraX);
                camera.translateYProperty().unbindBidirectional(cameraY);
                camera.translateZProperty().unbindBidirectional(cameraZ);

                camera.setTranslateX(walkingPath.get().get(0).getXcoord());
                camera.setTranslateZ(walkingPath.get().get(0).getYcoord());

                camera.getTransforms().clear();
                double opp = walkingPath.get().get(1).getXcoord() - walkingPath.get().get(0).getXcoord();
                double adj = walkingPath.get().get(1).getYcoord() - walkingPath.get().get(0).getYcoord();
                double angle = 180 + Math.toDegrees(Math.atan2(opp, adj));
                xRotate.setAngle(180);
                yRotate.setAngle(angle);
                camera.getTransforms().addAll(xRotate, yRotate);
                camera.setTranslateY(10);
                double oldAngle = angle;
                //Transition from node to node
                pathTransition.set(new SequentialTransition());
                for (int i = nodeCounter.get(); i < walkingPath.get().size() - 1; i++) {
                    //animation from node to node
                    Point3D currentPosition = new Point3D(walkingPath.get().get(i).getXcoord(), 10, walkingPath.get().get(i).getYcoord());
                    Point3D endPosition = new Point3D(walkingPath.get().get(i + 1).getXcoord(), 10, walkingPath.get().get(i + 1).getYcoord());

                    Point3D translation = endPosition.subtract(currentPosition);

                    double distance = translation.magnitude();
                    double speed = 75;
                    double duration = distance / speed;

                    TranslateTransition transition = new TranslateTransition(Duration.seconds(duration), camera);
                    transition.setByX(translation.getX());
                    transition.setByY(translation.getY());
                    transition.setByZ(translation.getZ());

                    //Animation for rotating
                    if(i < walkingPath.get().size() - 2){
                        camera.getTransforms().clear();
                        camera.getTransforms().add(xRotate);
                        opp = walkingPath.get().get(i + 2).getXcoord() - walkingPath.get().get(i+1).getXcoord();
                        adj = walkingPath.get().get(i + 2).getYcoord() - walkingPath.get().get(i+1).getYcoord();
                        angle = 180 + Math.toDegrees(Math.atan2(opp, adj));
                        double diff = angle - oldAngle;
                        if (diff > 180) {
                            angle -= 360;
                        } else if (diff < -180) {
                            angle += 360;
                        }

                        double rotationSpeed = 100;
                        double rotateDuration = Math.abs(oldAngle-angle) / rotationSpeed;

                        RotateTransition rotateTransition = new RotateTransition(Duration.seconds(rotateDuration), camera);
                        rotateTransition.setFromAngle(oldAngle);
                        rotateTransition.setToAngle(angle);
                        rotateTransition.setAxis(Rotate.Y_AXIS);
                        oldAngle = angle;

                        double finalAngle = angle;
                        rotateTransition.setOnFinished(event1 -> {
                            yRotate.setAngle(finalAngle);
                        });
                        pathTransition.get().getChildren().addAll(transition, rotateTransition);
                    }
                    else {
                        pathTransition.get().getChildren().add(transition);
                    }
                }
                pathTransition.get().play();
            }
            else if (event.getCode() == KeyCode.E) { // Turn Camera Right
                camera.getTransforms().stream().filter(node -> node instanceof Rotate).forEach(transform -> {
                    if(((Rotate) transform).getAxis() == Rotate.Y_AXIS){
                        ((Rotate) transform).setAngle(((Rotate) transform).getAngle() - 7.5);
                    }
                });
            }
            else if (event.getCode() == KeyCode.Q) { // Turn Camera Left
                camera.getTransforms().stream().filter(node -> node instanceof Rotate).forEach(transform -> {
                    if(((Rotate) transform).getAxis() == Rotate.Y_AXIS){
                        ((Rotate) transform).setAngle(((Rotate) transform).getAngle() + 7.5);
                    }
                });
            }
            else if(event.getCode() == KeyCode.ESCAPE){
                if(pathFinding.get()) {
                    if (pathTransition.get() != null) {
                        camera.setRotationAxis(Rotate.Y_AXIS);
                        camera.setRotate(0);
                        xRotate.setAngle(90);
                        if (pathTransition.get().getStatus() == Animation.Status.RUNNING) {
                            pathTransition.get().stop();
                        }
                        pathTransition.set(null);
                        camera.getTransforms().clear();
                        camera.getTransforms().addAll(
                            yRotate,
                            xRotate
                        );
                        camera.translateXProperty().bindBidirectional(cameraX);
                        camera.translateYProperty().bindBidirectional(cameraY);
                        camera.translateZProperty().bindBidirectional(cameraZ);
                    }
                    else if(walkingPath.get() != null){
                        newPathButton.fire();
                    }
                }
                else{
                    permVisibleTexts.forEach(bool -> bool.set(false));
                    permVisibleTexts.clear();
                    root.getChildren().removeIf(i -> i.getId() != null && i.getId().equals("dNode"));
                    root.getChildren().forEach(node -> {
                        if(node.getId() != null && node.getId().contains("nodeText")) {
                            String id = node.getId().substring(node.getId().lastIndexOf('t') + 1);
                            Node n = nodes.parallelStream().filter(no -> no.getNodeID() == Integer.parseInt(id)).toList().get(0);
                            node.setVisible(false);

                            assert node instanceof Text;
                            ((Text) node).setFont(new Font(24));
                            node.setTranslateX(n.getXcoord() - (node.getLayoutBounds().getWidth() / 2));
                            node.setTranslateY(80);

                            Rectangle rect = (Rectangle) root.getChildren().parallelStream().filter(no -> no.getId() != null && no.getId().equals("nodeRect" + id)).toList().get(0);
                            rect.setTranslateY(100);
                            rect.setWidth(node.getLayoutBounds().getWidth() + 20);
                            rect.setHeight(node.getLayoutBounds().getHeight());
                            rect.setTranslateX(n.getXcoord() - (rect.getWidth() / 2));
                        }
                    });
                }
            }
            else if(event.getCode() == KeyCode.SPACE){
                if(pathFinding.get()){
                    if(pathTransition.get() != null){
                        if(pathTransition.get().getStatus() == Animation.Status.RUNNING){
                            pathTransition.get().stop();
                        }
                        else if(pathTransition.get().getStatus() == Animation.Status.STOPPED){
                            pathTransition.get().play();
                        }
                    }
                }
            }
        });


        primaryStage.requestFocus();
        primaryStage.setScene(scene);
        primaryStage.setTitle("CS3733 - Tactical Tritons");
        primaryStage.show();
    }
    private double computeMax(double... items){
        double max = Double.MIN_VALUE;
        if(items == null) return max;
        for(double item : items){
            if(item > max) max = item;
        }
        return max;
    }
    private double computeSum(double... items){
        double sum = 0;
        if(items == null) return sum;
        for(double item : items){
            sum += item;
        }
        return sum;
    }
    private double computeSum(List<Integer> items){
        double sum = 0;
        if(items == null) return sum;
        for(int item : items){
            sum += item;
        }
        return sum;
    }
    private double computeMidPoint(List<Integer> items){
        if(items == null) return 0;
        return computeSum(items) / items.size();
    }
    private double computeSTD(List<Point2D> items){
        if(items == null) return 0;

        double sumX = computeSum(items.parallelStream().map(x -> (int) x.getX()).toList());
        double sumY = computeSum(items.parallelStream().map(y -> (int) y.getY()).toList());

        double meanX = sumX / items.size();
        double meanY = sumY / items.size();

        double stdX = 0, stdY = 0;
        for(Point2D point : items){
            stdX += Math.pow(point.getX() - meanX, 2);
            stdY += Math.pow(point.getY() - meanY, 2);
        }

        return Math.sqrt(Math.sqrt(stdX / items.size()) * Math.sqrt(stdY / items.size()));
    }


    @Override
    public void stop() throws SQLException, ClassNotFoundException {
        Tdb.getConnection().close();
        log.info("Shutting Down");
    }
}
