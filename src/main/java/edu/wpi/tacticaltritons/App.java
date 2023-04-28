package edu.wpi.tacticaltritons;

import edu.wpi.tacticaltritons.data.FlowerHashMap;
import edu.wpi.tacticaltritons.data.FurnitureHashMap;
import edu.wpi.tacticaltritons.data.QuickNavigationMenuButtons;
import edu.wpi.tacticaltritons.database.*;
import edu.wpi.tacticaltritons.database.Node;
import edu.wpi.tacticaltritons.navigation.Screen;
import edu.wpi.tacticaltritons.pathfinding.AlgorithmSingleton;
import io.github.palexdev.materialfx.controls.MFXButton;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Point2D;
import javafx.scene.*;
import javafx.scene.effect.ColorAdjust;
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
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

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

        Sphere sphere = new Sphere(2.5);
        sphere.setMaterial(new PhongMaterial(Color.FORESTGREEN));

        sphere.setTranslateZ(7);
        sphere.setTranslateX(2);

        Box box = new Box(5, 5, 5);
        box.setMaterial(new PhongMaterial(Color.RED));

        Translate pivot = new Translate();
        Rotate yRotate = new Rotate(-45, Rotate.Y_AXIS);
        Rotate xRotate = new Rotate(-45, Rotate.X_AXIS);
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
        root.getChildren().add(box);
        root.getChildren().add(sphere);

        List<Node> nodes = DAOFacade.getAllCurrentMoves(Date.valueOf(LocalDate.now())).parallelStream()
                .filter(move -> !move.getLocation().getNodeType().equals("HALL"))
                .map(Move::getNode)
                .filter(node -> node.getFloor().equals("2")).toList();

        Map<Node, String> l = new HashMap<>();
        List<Move> moves = DAOFacade.getAllCurrentMoves(Date.valueOf(LocalDate.now()));
        nodes.forEach(node -> l.put(node, moves.parallelStream().filter(move -> move.getNode().getNodeID() == node.getNodeID()).toList().get(0).getLocation().getShortName()));

        List<Node> pathToCompute = new ArrayList<>();
        nodes.forEach(node -> {
            Text t = new Text(l.get(node));
            t.setId("nodeText");
            Sphere s = new Sphere(15);
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
            t.getTransforms().addAll(
                    new Rotate(-45, Rotate.Y_AXIS),
                    new Rotate(-45, Rotate.X_AXIS));
            t.setVisible(false);
            t.setTranslateZ(node.getYcoord() - t.getLayoutBounds().getWidth());
            t.setTranslateX(node.getXcoord() - t.getLayoutBounds().getWidth());
            t.setFont(new Font(32));
            t.setTranslateY(-30);
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
            if (points.size() == 0) {
                points.add(new Point2D(event.getSceneX(), event.getSceneY()));
            } else {
                Point2D secondPoint = new Point2D(event.getSceneX(), event.getSceneY());

                double hyp = points.get(0).distance(secondPoint);
                double opp = Math.abs(secondPoint.getY() - points.get(0).getY());
                double adj = Math.abs(secondPoint.getX() - points.get(0).getX());

                double magnitude = hyp / Math.sqrt(subScene.getWidth() * subScene.getHeight());

                double xMultiplier = secondPoint.getY() < points.get(0).getY() ? 1 : -1;
                double xAngle = magnitude * xMultiplier * Math.atan(opp / adj) * (180 / Math.PI);

                double yMultiplier = secondPoint.getX() < points.get(0).getX() ? -1 : 1;
                double yAngle = magnitude * yMultiplier * Math.atan(adj / opp) * (180 / Math.PI);

                camera.getTransforms().stream().filter(node -> (node instanceof Rotate)).forEach(node -> {
                    if (((Rotate) node).getAxis().equals(Rotate.X_AXIS) && !Double.isNaN(xAngle)) {
                        if (((Rotate) node).getAngle() <= -10 && ((Rotate) node).getAngle() >= -80) {
                            if (!(((Rotate) node).getAngle() + xAngle >= -10 || ((Rotate) node).getAngle() + xAngle <= -80)) {
                                ((Rotate) node).setAngle(((Rotate) node).getAngle() + xAngle);
                            }
                        }
                    } else if (((Rotate) node).getAxis().equals(Rotate.Y_AXIS) && !Double.isNaN(yAngle)) {
                        ((Rotate) node).setAngle(((Rotate) node).getAngle() + yAngle);
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

        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
                if (matrix[i][j] == 1) {
                    if (i< matrix.length-1 && j<matrix[i].length-1) {
                        if (matrix[i + 1][j] == 0 || matrix[i + 1][j + 1] == 0 || matrix[i][j + 1] == 0) {
                            Box b = new Box(1, 30, 1);
                            b.setTranslateZ(i);
                            b.setTranslateX(j);
                            b.setMaterial(new PhongMaterial(Color.web("#A17A4B")));
                            root.getChildren().add(b);
                        }
                    }
                    if(i>1 && j>1){
                        if(matrix[i - 1][j] == 0 || matrix[i - 1][j - 1] == 0 || matrix[i][j - 1] == 0){
                            Box b = new Box(1, 30, 1);
                            b.setTranslateZ(i);
                            b.setTranslateX(j);
                            b.setMaterial(new PhongMaterial(Color.web("#A17A4B")));
                            root.getChildren().add(b);
                        }
                    }
                }
            }
        }

        ImageView iv = new ImageView(writer);
        iv.getTransforms().add(new Rotate(90, Rotate.X_AXIS));
        root.getChildren().add(iv);

        AnchorPane pane = new AnchorPane();
        pane.setPrefSize(1280,720);
        MFXButton button = new MFXButton("CLICK ME");
        pane.getChildren().add(button);
        pane.getChildren().add(group);
        group.toBack();

        final Scene scene = new Scene(pane);
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
                camera.setTranslateZ(camera.getTranslateZ() + screenIncrement);
            } else if (event.getCode() == KeyCode.D) {
                camera.setTranslateX(camera.getTranslateX() + screenIncrement);
            } else if (event.getCode() == KeyCode.S) {
                camera.setTranslateZ(camera.getTranslateZ() - screenIncrement);
            } else if (event.getCode() == KeyCode.A) {
                camera.setTranslateX(camera.getTranslateX() - screenIncrement);
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
