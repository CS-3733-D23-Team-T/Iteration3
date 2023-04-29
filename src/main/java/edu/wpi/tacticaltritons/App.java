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
import javafx.animation.RotateTransition;
import javafx.animation.SequentialTransition;
import javafx.animation.TranslateTransition;
import javafx.application.Application;
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
import javafx.scene.shape.Sphere;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;
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

        Translate pivot = new Translate();
        Rotate yRotate = new Rotate(-45, Rotate.Y_AXIS);
        Rotate xRotate = new Rotate(135, Rotate.X_AXIS);
        Translate zoom = new Translate(0, 0, -1000);

// Create and position camera
        PerspectiveCamera camera = new PerspectiveCamera(true);
        camera.getTransforms().addAll(
                pivot,
                yRotate,
                xRotate,
                zoom
        );
        camera.setNearClip(1);
        camera.setFarClip(12000);


// Build the Scene Graph
        Group root = new Group();

        root.getChildren().add(camera);

        List<Node> nodes = DAOFacade.getAllCurrentMoves(Date.valueOf(LocalDate.now())).parallelStream()
                .filter(move -> !move.getLocation().getNodeType().equals("HALL"))
                .map(Move::getNode)
                .filter(node -> node.getFloor().equals("2")).toList();

        Map<Node, String> l = new HashMap<>();
        List<Move> moves = DAOFacade.getAllCurrentMoves(Date.valueOf(LocalDate.now()));
        nodes.forEach(node -> l.put(node, moves.parallelStream().filter(move -> move.getNode().getNodeID() == node.getNodeID()).toList().get(0).getLocation().getShortName()));
        AtomicReference<List<Node>> walkingPath = new AtomicReference<>(new ArrayList<>());
        List<Node> pathToCompute = new ArrayList<>();
        nodes.forEach(node -> {
            Text t = new Text(l.get(node));
            t.setId("nodeText");
            Box s = new Box(10,10,10);
            s.setOnMouseClicked(event -> {
                if (pathToCompute.size() == 0) {
                    pathToCompute.add(node);
                    System.out.println("selected: " + node.getNodeID());
                } else {
                    Node start = pathToCompute.get(0);
                    System.out.println("computing: " + start.getNodeID() + " to " + node.getNodeID());

                    root.getChildren().removeIf(i -> i.getId() != null && i.getId().equals("pathBlock"));

                    try {
                        List<Node> path = AlgorithmSingleton.getInstance().algorithm.findShortestPath(start, node);
                        walkingPath.set(path);

                        for (int i = 0; i < path.size() - 1; i++) {

                            double distance = Math.sqrt(Math.pow(path.get(i + 1).getXcoord() - path.get(i).getXcoord(), 2) +
                                    Math.pow(path.get(i + 1).getYcoord() - path.get(i).getYcoord(), 2));

                            double opp = path.get(i + 1).getXcoord() - path.get(i).getXcoord();
                            double adj = path.get(i + 1).getYcoord() - path.get(i).getYcoord();
                            double angle = Math.atan(opp / adj) * (180 / Math.PI);


                            System.out.println(opp + ", " + adj + ", " + angle);

                            Box line = new Box(10, 10, distance);
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
            });
            s.setOnMouseEntered(event -> {
//                t.getTransforms().clear();
//                t.getTransforms().addAll(xRotate, yRotate);
                t.setVisible(true);
            });
            s.setOnMouseExited(event -> {
                t.setVisible(false);
            });
            s.setMaterial(new PhongMaterial(Color.RED));
            s.setTranslateZ(node.getYcoord());
            s.setTranslateX(node.getXcoord());


//            Rectangle rect = new Rectangle(node.getXcoord(), node.getYcoord());
//            rect.setTranslateZ(node.getYcoord() + 10);
//            rect.setTranslateX(node.getXcoord() + 10);
//            t.getTransforms().add(new Rotate(90, Rotate.X_AXIS));
//            t.getTransforms().addAll(
//                    xRotate,
//                    yRotate);
            t.getTransforms().addAll(
                    new Rotate(180, Rotate.Z_AXIS),
                    new Rotate(180, Rotate.Y_AXIS));
            t.setVisible(false);
            t.setTranslateZ(node.getYcoord());
            t.setTranslateX(node.getXcoord() - t.getLayoutBounds().getWidth());
            t.setFont(new Font(32));
            t.setTranslateY(25);
            root.getChildren().addAll(s, t);
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

        List<Point2D> points = new ArrayList<>();
        subScene.setOnMouseDragged(event -> {
            if(points.size() == 0){
                points.add(new Point2D(event.getSceneX(), event.getSceneY()));
            }
            else {
                Point2D secondPoint = new Point2D(event.getSceneX(), event.getSceneY());

                double hyp = points.get(0).distance(secondPoint);
                double opp = Math.abs(secondPoint.getY() - points.get(0).getY());
                double adj = Math.abs(secondPoint.getX() - points.get(0).getX());

                double magnitude = (hyp * 2.5) / Math.sqrt(subScene.getWidth() * subScene.getHeight());

                double xMultiplier = secondPoint.getY() < points.get(0).getY() ? 1 : -1;
                double xAngle = magnitude * xMultiplier * Math.atan(opp / adj) * (180 / Math.PI);

                double yMultiplier = secondPoint.getX() < points.get(0).getX() ? -1 : 1;
                double yAngle = magnitude * yMultiplier * Math.atan(adj / opp) * (180 / Math.PI);

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
//                    pixelWriter.setColor(j, i, Color.BLACK);
                    matrix[i][j] = 1;
                } else {
                    pixelWriter.setColor(j, i, Color.WHITE);
                    matrix[i][j] = 0;
                }
            }
        }

//        for (int i = 0; i < matrix.length; i++) {
//            for (int j = 0; j < matrix[i].length; j++) {
//                if (matrix[i][j] == 1) {
//                    if (i< matrix.length-1 && j<matrix[i].length-1) {
//                        if (matrix[i + 1][j] == 0 || matrix[i + 1][j + 1] == 0 || matrix[i][j + 1] == 0) {
//                            Box b = new Box(1, 30, 1);
//                            b.setTranslateZ(i);
//                            b.setTranslateX(j);
//                            b.setMaterial(new PhongMaterial(Color.web("#A17A4B")));
//                            root.getChildren().add(b);
//                        }
//                    }
//                    if(i>1 && j>1){
//                        if(matrix[i - 1][j] == 0 || matrix[i - 1][j - 1] == 0 || matrix[i][j - 1] == 0){
//                            Box b = new Box(1, 30, 1);
//                            b.setTranslateZ(i);
//                            b.setTranslateX(j);
//                            b.setMaterial(new PhongMaterial(Color.web("#A17A4B")));
//                            root.getChildren().add(b);
//                        }
//                    }
//                }
//            }
//        }

        ImageView iv = new ImageView(writer);
        iv.getTransforms().add(new Rotate(90, Rotate.X_AXIS));
        root.getChildren().add(iv);

        AnchorPane pane = new AnchorPane();
        pane.setPrefSize(1280,720);
        MFXToggleButton pathFindingButton = new MFXToggleButton("Path Finding");
        Rectangle background = new Rectangle(300,720);
        background.setFill(Color.WHITE);
        background.setArcHeight(20);
        background.setArcWidth(20);
        pathFindingButton.setStyle("-fx-text-fill: blue");
        pane.getChildren().add(background);
        pane.getChildren().add(pathFindingButton);
        pane.getChildren().add(group);
        group.toBack();

        final Scene scene = new Scene(pane);

        AtomicInteger nodeCounter = new AtomicInteger();
        scene.setOnKeyPressed(event -> {
            double sceneSize = Math.sqrt(scene.getWidth() * scene.getHeight());
            double screenIncrement = sceneSize / 5;
            if (event.isShiftDown() && event.getCode() == KeyCode.EQUALS) {
                camera.getTransforms().stream().filter(node -> (node instanceof Translate)).forEach(node -> {
                    if (((Translate) node).getZ() <= -75) {
                        ((Translate) node).setZ(((Translate) node).getZ() + screenIncrement);
                    }
                });
            } else if (event.isShiftDown() && event.getCode() == KeyCode.MINUS) {
                camera.getTransforms().stream().filter(node -> (node instanceof Translate)).forEach(node -> {
                    if (((Translate) node).getZ() <= -50) {
                        ((Translate) node).setZ(((Translate) node).getZ() - screenIncrement);
                    }
                });
            } else if (event.getCode() == KeyCode.W) {
                AtomicDouble zDisplacement = new AtomicDouble(0);
                AtomicDouble xDisplacement = new AtomicDouble(0);
                camera.getTransforms().forEach(transform -> {
                    if(transform instanceof Rotate){
                        if(((Rotate) transform).getAxis().equals(Rotate.Y_AXIS)){
                            xDisplacement.set(-1 * screenIncrement * Math.sin(Math.toRadians(((Rotate) transform).getAngle())));
                            zDisplacement.set(-1 * screenIncrement * Math.cos(Math.toRadians(((Rotate) transform).getAngle())));
                        }
                    }
                });
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
                AtomicDouble zDisplacement = new AtomicDouble(0);
                AtomicDouble xDisplacement = new AtomicDouble(0);
                camera.getTransforms().forEach(transform -> {
                    if(transform instanceof Rotate){
                        if(((Rotate) transform).getAxis().equals(Rotate.Y_AXIS)){
                            xDisplacement.set(screenIncrement * Math.sin(Math.toRadians(((Rotate) transform).getAngle())));
                            zDisplacement.set(screenIncrement * Math.cos(Math.toRadians(((Rotate) transform).getAngle())));
                        }
                    }
                });
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
                //Change angle of camera to be eye level
                camera.getTransforms().stream().filter(node -> (node instanceof Rotate)).forEach(node -> {
                    ((Rotate) node).setAngle(0);
                });

                //Move camera to node start
                camera.setTranslateX(walkingPath.get().get(0).getXcoord());
                camera.setTranslateZ(walkingPath.get().get(0).getYcoord());

                camera.getTransforms().clear();
                double opp = walkingPath.get().get(1).getXcoord() - walkingPath.get().get(0).getXcoord();
                double adj = walkingPath.get().get(1).getYcoord() - walkingPath.get().get(0).getYcoord();
                double angle = Math.toDegrees(Math.atan2(opp, adj));
                Rotate rotateX = new Rotate(angle, Rotate.Y_AXIS);
                camera.getTransforms().addAll(rotateX);
                camera.setTranslateY(-10);

                //Transition from node to node
                SequentialTransition seqTransition = new SequentialTransition();
                for (int i = nodeCounter.get(); i < walkingPath.get().size() - 1; i++) {
                    //animation from node to node
                    Point3D currentPosition = new Point3D(walkingPath.get().get(i).getXcoord(), -10, walkingPath.get().get(i).getYcoord());
                    Point3D endPosition = new Point3D(walkingPath.get().get(i + 1).getXcoord(), -10, walkingPath.get().get(i + 1).getYcoord());

                    Point3D translation = endPosition.subtract(currentPosition);

                    double distance = translation.magnitude();
                    double speed = 75;
                    double duration = distance / speed;

                    TranslateTransition transition = new TranslateTransition(Duration.seconds(duration), camera);
                    transition.setByX(translation.getX());
                    transition.setByY(translation.getY());
                    transition.setByZ(translation.getZ());

                    //Animation for rotating
                    if(i<walkingPath.get().size()-2){
                        camera.getTransforms().clear();
                        opp = walkingPath.get().get(i + 2).getXcoord() - walkingPath.get().get(i+1).getXcoord();
                        adj = walkingPath.get().get(i + 2).getYcoord() - walkingPath.get().get(i+1).getYcoord();
                        angle = Math.toDegrees(Math.atan2(opp, adj));

                        double rotationSpeed = 100;
                        double rotateDuration = Math.abs(angle) / rotationSpeed;

                        RotateTransition rotateTransition = new RotateTransition(Duration.seconds(rotateDuration), camera);
                        rotateTransition.setToAngle(angle);
                        rotateTransition.setAxis(Rotate.Y_AXIS);

                        seqTransition.getChildren().addAll(transition, rotateTransition);
                    } else {
                        seqTransition.getChildren().add(transition);
                    }
                }
                seqTransition.play();
            }
            else if (event.getCode() == KeyCode.M) { // Turn
                Rotate rotateX = new Rotate(5, Rotate.Y_AXIS);
                camera.getTransforms().addAll(rotateX);
            }
            else if (event.getCode() == KeyCode.N) { // Turn
                Rotate rotateX = new Rotate(-5, Rotate.Y_AXIS);
                camera.getTransforms().addAll(rotateX);
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
