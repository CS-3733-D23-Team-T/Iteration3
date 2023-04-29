package edu.wpi.tacticaltritons;

import com.google.common.util.concurrent.AtomicDouble;
import edu.wpi.tacticaltritons.data.FlowerHashMap;
import edu.wpi.tacticaltritons.data.FurnitureHashMap;
import edu.wpi.tacticaltritons.data.QuickNavigationMenuButtons;
import edu.wpi.tacticaltritons.database.*;
import edu.wpi.tacticaltritons.database.Node;
import edu.wpi.tacticaltritons.navigation.Screen;
import edu.wpi.tacticaltritons.pathfinding.AlgorithmSingleton;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXToggleButton;
import javafx.animation.Animation;
import javafx.animation.RotateTransition;
import javafx.animation.SequentialTransition;
import javafx.animation.TranslateTransition;
import javafx.application.Application;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Point2D;
import javafx.geometry.Point3D;
import javafx.scene.*;
import javafx.scene.image.*;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
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
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

@Slf4j
public class App extends Application {

    @Getter
    private static Stage primaryStage;
    @Getter
    private static BorderPane rootPane;
    @Getter
    private static BorderPane navBar;
    @Getter
    private static GridPane loginQuickNavigation;
    @Getter
    private static GridPane staffQuickNavigation;
    @Getter
    private static GridPane adminQuickNavigation;
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
                } else {
                    pixelWriter.setColor(j, i, Color.WHITE);
                    matrix[i][j] = 0;
                }
            }
        }

        ImageView iv = new ImageView(writer);
        iv.getTransforms().add(new Rotate(90, Rotate.X_AXIS));

        Rotate yRotate = new Rotate(315, Rotate.Y_AXIS);
        Rotate xRotate = new Rotate(135, Rotate.X_AXIS);

// Create and position camera
        PerspectiveCamera camera = new PerspectiveCamera(true);
        camera.getTransforms().addAll(
                yRotate,
                xRotate);
        camera.setTranslateY(1000);
        camera.setTranslateZ(matrix[0].length / 2.0);
        camera.setTranslateX(matrix.length / 2.0);
        camera.setNearClip(1);
        camera.setFarClip(12000);


// Build the Scene Graph
        Group root = new Group();

        root.getChildren().add(camera);

        AtomicInteger floorNumber = new AtomicInteger(2);
        List<Node> nodes = DAOFacade.getAllCurrentMoves(Date.valueOf(LocalDate.now())).parallelStream()
//                .filter(move -> !move.getLocation().getNodeType().equals("HALL"))
                .map(Move::getNode)
                .filter(node -> node.getFloor().equals(String.valueOf(floorNumber))).toList();

        Map<Node, String> shortNames = new HashMap<>();
        Map<Node, String> locationTypes = new HashMap<>();
        Map<Node, String> longNames = new HashMap<>();
        List<Move> moves = DAOFacade.getAllCurrentMoves(Date.valueOf(LocalDate.now()));
        nodes.forEach(node -> {
            Move m = moves.parallelStream().filter(move -> move.getNode().getNodeID() == node.getNodeID()).toList().get(0);
            shortNames.put(node, m.getLocation().getShortName());
            locationTypes.put(node, m.getLocation().getNodeType());
            longNames.put(node, m.getLocation().getLongName());

        });
        AtomicReference<List<Node>> walkingPath = new AtomicReference<>(new ArrayList<>());
        List<Node> pathToCompute = new ArrayList<>();
        List<Box> visibleNodes = new ArrayList<>();

        BooleanProperty pathFinding = new SimpleBooleanProperty(true);

        MFXButton newPathButton = new MFXButton("New Path");
        newPathButton.setOnAction(event -> {
            root.getChildren().removeIf(i -> i.getId() != null && i.getId().equals("pathBlock"));
            visibleNodes.forEach(box -> box.setMaterial(new PhongMaterial(Color.RED)));
            root.getChildren().addAll(visibleNodes);
        });
        //drawing nodes
        nodes.forEach(node -> {
            if(!locationTypes.get(node).equals("HALL")) {
                Text t = new Text(shortNames.get(node));
                t.setId("nodeText");
                Box s = new Box(10, 10, 10);
                s.setOnMouseClicked(event -> {
                    //pathfinding
                    if (pathFinding.get()) {
                        //starting point
                        s.setMaterial(new PhongMaterial(Color.GREEN));
                        if (pathToCompute.size() == 0) {
                            pathToCompute.add(node);

//                            System.out.println("selected: " + node.getNodeID());
                        }
                        //ending point
                        else {
                            Node start = pathToCompute.get(0);
//                            System.out.println("computing: " + start.getNodeID() + " to " + node.getNodeID());

                            //removing old paths
                            root.getChildren().removeIf(i -> i.getId() != null && i.getId().equals("pathBlock"));
                            root.getChildren().removeIf(i -> i.getId() != null && i.getId().contains("node")
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
                });
                s.setOnMouseEntered(event -> {
                    System.out.println(yRotate.getAngle());
                    t.setVisible(true);
                });
                s.setOnMouseExited(event -> {
                    t.setVisible(false);
                });
                s.setId("node"+node.getNodeID());
                s.setMaterial(new PhongMaterial(Color.RED));
                s.setTranslateZ(node.getYcoord());
                s.setTranslateX(node.getXcoord());
                visibleNodes.add(s);


//            Rectangle rect = new Rectangle(node.getXcoord(), node.getYcoord());
//            rect.setTranslateZ(node.getYcoord() + 10);
//            rect.setTranslateX(node.getXcoord() + 10);
//            t.getTransforms().add(new Rotate(90, Rotate.X_AXIS));
//            t.getTransforms().addAll(
//                    xRotate,
//                    yRotate);


                //drawing the floating text
                t.getTransforms().addAll(
                        new Rotate(180, Rotate.Z_AXIS),
                        new Rotate(180, Rotate.Y_AXIS));
                t.setVisible(false);
                t.setTranslateZ(node.getYcoord());
                t.setTranslateX(node.getXcoord() - t.getLayoutBounds().getWidth());
                t.setFont(new Font(32));
                t.setTranslateY(25);
                root.getChildren().addAll(s, t);
            }
        });


// Use a SubScene
        SubScene subScene = new SubScene(
                root,
                1280, 720,
                true,
                SceneAntialiasing.BALANCED
        );
        subScene.setFill(Color.ALICEBLUE);
        subScene.setCamera(camera);
        Group group = new Group();
        group.getChildren().add(subScene);

        //mouse drag event
        List<Point2D> points = new ArrayList<>();
        subScene.setOnMouseDragged(event -> {
            if(points.size() == 0){
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
                                if(Math.abs(xAngle) < 30) {
                                    ((Rotate) node).setAngle(((Rotate) node).getAngle() + xAngle);
                                }
                            }
                        }
                    } else if (((Rotate) node).getAxis().equals(Rotate.Y_AXIS) && !Double.isNaN(yAngle)) {
                        if(Math.abs(yAngle) < 30) {
                            ((Rotate) node).setAngle(((Rotate) node).getAngle() + yAngle);
                        }
                    }
                });

                points.clear();
            }
        });


        root.getChildren().add(iv);

        //pane stuff
        AnchorPane pane = new AnchorPane();
        pane.setPrefSize(1280,720);
        MFXToggleButton pathFindingToggle = new MFXToggleButton("Path Finding");
        pathFindingToggle.setStyle("-fx-text-fill: blue");
        pathFindingToggle.selectedProperty().bindBidirectional(pathFinding);

        MFXToggleButton wallsToggle = new MFXToggleButton("Walls");
        wallsToggle.setStyle("-fx-text-fill: blue");
        wallsToggle.setLayoutY(25);
        wallsToggle.selectedProperty().addListener((obs, o, n) -> {
            if(n){
                for (int i = 0; i < matrix.length; i++) {
                    for (int j = 0; j < matrix[i].length; j++) {
                        if (matrix[i][j] == 1) {
                            if (i< matrix.length-1 && j<matrix[i].length-1) {
                                if (matrix[i + 1][j] == 0 || matrix[i + 1][j + 1] == 0 || matrix[i][j + 1] == 0) {
                                    Box b = new Box(1, 30, 1);
                                    b.setTranslateZ(i);
                                    b.setTranslateX(j);
                                    b.setMaterial(new PhongMaterial(Color.web("#A17A4B")));
                                    b.setId("wall");
                                    root.getChildren().add(b);
                                }
                            }
                            if(i>1 && j>1){
                                if(matrix[i - 1][j] == 0 || matrix[i - 1][j - 1] == 0 || matrix[i][j - 1] == 0){
                                    Box b = new Box(1, 30, 1);
                                    b.setTranslateZ(i);
                                    b.setTranslateX(j);
                                    b.setMaterial(new PhongMaterial(Color.web("#A17A4B")));
                                    b.setId("wall");
                                    root.getChildren().add(b);
                                }
                            }
                        }
                    }
                }
            }
            else{
                root.getChildren().removeIf(node -> node.getId() != null && node.getId().equals("wall"));
            }
        });

        Rectangle background = new Rectangle(300,720);
        background.setFill(Color.WHITE);
        background.setArcHeight(20);
        background.setArcWidth(20);
        pane.getChildren().add(background);
        pane.getChildren().add(pathFindingToggle);
        pane.getChildren().add(wallsToggle);
        pane.getChildren().add(group);
        group.toBack();

        final Scene scene = new Scene(pane);

        AtomicInteger nodeCounter = new AtomicInteger();
        AtomicReference<SequentialTransition> pathTransition = new AtomicReference<>();
        scene.setOnKeyPressed(event -> {
            double sceneSize = Math.sqrt(scene.getWidth() * scene.getHeight());
            double screenIncrement = sceneSize / 5;
            if (event.isShiftDown() && event.getCode() == KeyCode.EQUALS) {
                camera.setTranslateY(camera.getTranslateY() + screenIncrement);
            }
            else if (event.isShiftDown() && event.getCode() == KeyCode.MINUS) {
                camera.setTranslateY(camera.getTranslateY() - screenIncrement);
            }
            else if (event.getCode() == KeyCode.W) {
                AtomicDouble zDisplacement = new AtomicDouble(0);
                AtomicDouble xDisplacement = new AtomicDouble(0);
                camera.getTransforms().forEach(transform -> {
                    if(transform instanceof Rotate){
                        if(((Rotate) transform).getAxis().equals(Rotate.Y_AXIS)){
                            //calculating the x and z displacement (x-z plane is what we are viewing)
                            xDisplacement.set(-1 * screenIncrement * Math.sin(Math.toRadians(((Rotate) transform).getAngle())));
                            zDisplacement.set(-1 * screenIncrement * Math.cos(Math.toRadians(((Rotate) transform).getAngle())));
                        }
                    }
                });
                //translating the camera
                camera.setTranslateZ(camera.getTranslateZ() + zDisplacement.get());
                camera.setTranslateX(camera.getTranslateX() + xDisplacement.get());
            } else if (event.getCode() == KeyCode.D) {
//                AtomicDouble zDisplacement = new AtomicDouble(0);
//                AtomicDouble xDisplacement = new AtomicDouble(0);
//                camera.getTransforms().forEach(transform -> {
//                    if(transform instanceof Rotate){
//                        if(((Rotate) transform).getAxis().equals(Rotate.Y_AXIS)){
//                            xDisplacement.set(-1 * screenIncrement * Math.sin(Math.toRadians(((Rotate) transform).getAngle())));
//                            zDisplacement.set(screenIncrement * Math.cos(Math.toRadians(((Rotate) transform).getAngle())));
//                        }
//                    }
//                });
//                camera.setTranslateZ(camera.getTranslateZ() + zDisplacement.get());
//                camera.setTranslateX(camera.getTranslateX() + xDisplacement.get());
            }
            else if (event.getCode() == KeyCode.S) {
                //opposite math of the W key press
                AtomicDouble zDisplacement = new AtomicDouble(0);
                AtomicDouble xDisplacement = new AtomicDouble(0);
                camera.getTransforms().forEach(transform -> {
                    if(transform instanceof Rotate){
                        if(((Rotate) transform).getAxis().equals(Rotate.Y_AXIS)){
                            //getting the pivot
                            xDisplacement.set(screenIncrement * Math.sin(Math.toRadians(((Rotate) transform).getAngle())));
                            zDisplacement.set(screenIncrement * Math.cos(Math.toRadians(((Rotate) transform).getAngle())));
                        }
                    }
                });
                //moving the pivot
                camera.setTranslateZ(camera.getTranslateZ() + zDisplacement.get());
                camera.setTranslateX(camera.getTranslateX() + xDisplacement.get());
            }
            else if (event.getCode() == KeyCode.A) {
//                AtomicDouble zDisplacement = new AtomicDouble(0);
//                AtomicDouble xDisplacement = new AtomicDouble(0);
//                camera.getTransforms().forEach(transform -> {
//                    if(transform instanceof Rotate){
//                        if(((Rotate) transform).getAxis().equals(Rotate.Y_AXIS)){
//                            xDisplacement.set(screenIncrement * Math.cos((Math.PI / 2) + Math.toRadians(((Rotate) transform).getAngle())));
//                            zDisplacement.set(screenIncrement * Math.sin((Math.PI / 2) + Math.toRadians(((Rotate) transform).getAngle())));
//                        }
//                    }
//                });
//                camera.setTranslateZ(camera.getTranslateZ() + zDisplacement.get());
//                camera.setTranslateX(camera.getTranslateX() + xDisplacement.get());
            }
            else if (event.getCode() == KeyCode.L) {
                //Move camera to node start
                camera.setTranslateX(walkingPath.get().get(0).getXcoord());
                camera.setTranslateZ(walkingPath.get().get(0).getYcoord());

                camera.getTransforms().clear();
                double opp = walkingPath.get().get(1).getXcoord() - walkingPath.get().get(0).getXcoord();
                double adj = walkingPath.get().get(1).getYcoord() - walkingPath.get().get(0).getYcoord();
                double angle = Math.toDegrees(Math.atan2(opp, adj));
                Rotate rotateY = new Rotate(angle, Rotate.Y_AXIS);
                camera.getTransforms().addAll(rotateY);
                camera.setTranslateY(10);

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
                        opp = walkingPath.get().get(i + 2).getXcoord() - walkingPath.get().get(i+1).getXcoord();
                        adj = walkingPath.get().get(i + 2).getYcoord() - walkingPath.get().get(i+1).getYcoord();
                        angle = Math.toDegrees(Math.atan2(opp, adj));

                        double rotationSpeed = 100;
                        double rotateDuration = Math.abs(angle) / rotationSpeed;

                        RotateTransition rotateTransition = new RotateTransition(Duration.seconds(rotateDuration), camera);
                        rotateTransition.setToAngle(angle);
                        rotateTransition.setAxis(Rotate.Y_AXIS);

                        pathTransition.get().getChildren().addAll(transition, rotateTransition);
                    }
                    else {
                        pathTransition.get().getChildren().add(transition);
                    }
                }
                pathTransition.get().play();
                pathTransition.get().setOnFinished(status -> pathTransition.set(null));
            }
            else if (event.getCode() == KeyCode.E) { // Turn Camera Right
                camera.getTransforms().stream().filter(node -> node instanceof Rotate).forEach(transform -> {
                    if(((Rotate) transform).getAxis() == Rotate.Y_AXIS){
                        ((Rotate) transform).setAngle(((Rotate) transform).getAngle() - 5);
                    }
                });
            }
            else if (event.getCode() == KeyCode.Q) { // Turn Camera Left
                camera.getTransforms().stream().filter(node -> node instanceof Rotate).forEach(transform -> {
                    if(((Rotate) transform).getAxis() == Rotate.Y_AXIS){
                        ((Rotate) transform).setAngle(((Rotate) transform).getAngle() + 5);
                    }
                });
            }
            else if(event.getCode() == KeyCode.ESCAPE){
                if(pathFinding.get()) {
                    if (pathTransition.get() == null) {

                    }
                    if (pathTransition.get().getStatus() == Animation.Status.RUNNING) {
                        pathTransition.get().stop();
                    }
                }
            }
        });


        primaryStage.setScene(scene);
        primaryStage.setTitle("CS3733 - Tactical Tritons");
        primaryStage.show();
    }

    @Override
    public void stop() throws SQLException, ClassNotFoundException {
        Tdb.getConnection().close();
        log.info("Shutting Down");
    }
}
