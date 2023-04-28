package edu.wpi.tacticaltritons;

import edu.wpi.tacticaltritons.data.FlowerHashMap;
import edu.wpi.tacticaltritons.data.FurnitureHashMap;
import edu.wpi.tacticaltritons.data.QuickNavigationMenuButtons;
import edu.wpi.tacticaltritons.database.Tdb;
import edu.wpi.tacticaltritons.navigation.Screen;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Point2D;
import javafx.scene.*;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.*;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
import javafx.scene.shape.Sphere;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;
import javafx.stage.Stage;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

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

        Sphere sphere = new Sphere(2.5);
        sphere.setMaterial(new PhongMaterial(Color.FORESTGREEN));

        sphere.setTranslateZ(7);
        sphere.setTranslateX(2);

        Box box = new Box(5, 5, 5);
        box.setMaterial(new PhongMaterial(Color.RED));

        Translate pivot = new Translate();
        Rotate yRotate = new Rotate(0, Rotate.Y_AXIS);
        Rotate xRotate = new Rotate(-45, Rotate.X_AXIS);
        Translate zoom = new Translate(0,0,-50);

// Create and position camera
        PerspectiveCamera camera = new PerspectiveCamera(true);
        camera.getTransforms().addAll (
                pivot,
                yRotate,
                xRotate,
                zoom
        );
        camera.setNearClip(1);
        camera.setFarClip(2000);

// Build the Scene Graph
        Group root = new Group();

//        DAOFacade.getAllNodes().forEach(node -> {
//            Sphere s = new Sphere(2.5);
//            s.setTranslateZ(node.getYcoord());
//            s.setTranslateX(node.getXcoord());
//            root.getChildren().add(s);
//        });
        root.getChildren().add(camera);
        root.getChildren().add(box);
        root.getChildren().add(sphere);



// Use a SubScene
        SubScene subScene = new SubScene(
                root,
                1280,720,
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
            else{
                Point2D secondPoint = new Point2D(event.getSceneX(), event.getSceneY());

                double hyp = points.get(0).distance(secondPoint);
                double opp = Math.abs(secondPoint.getY() - points.get(0).getY());
                double adj = Math.abs(secondPoint.getX() - points.get(0).getX());

                double magnitude = hyp / Math.sqrt(subScene.getWidth() * subScene.getHeight());

                double xMultiplier = secondPoint.getY() < points.get(0).getY() ? 1 : -1;
                double xAngle = magnitude * xMultiplier * Math.atan(opp/adj) * (180 / Math.PI);

                double yMultiplier = secondPoint.getX() < points.get(0).getX() ? -1 : 1;
                double yAngle = magnitude * yMultiplier * Math.atan(adj/opp) * (180 / Math.PI);

                camera.getTransforms().stream().filter(node -> (node instanceof Rotate)).forEach(node -> {
                    if(((Rotate) node).getAxis().equals(Rotate.X_AXIS) && !Double.isNaN(xAngle)){
                        if(((Rotate) node).getAngle() <= -10 && ((Rotate) node).getAngle() >= -80) {
                            if(!(((Rotate) node).getAngle() + xAngle >= -10 || ((Rotate) node).getAngle() + xAngle <= -80)){
                                ((Rotate) node).setAngle(((Rotate) node).getAngle() + xAngle);
                            }
                        }
                    }
                    else if(((Rotate) node).getAxis().equals(Rotate.Y_AXIS) && !Double.isNaN(yAngle)){
                        ((Rotate) node).setAngle(((Rotate) node).getAngle() + yAngle);
                    }
                });

                points.clear();
            }
        });

        Image image = new Image(Objects.requireNonNull(App.class.getResource("images/clean_map/map_firstfloor_clean.png")).toString());

        WritableImage writer = new WritableImage(image.getPixelReader(), (int) image.getWidth(), (int) image.getHeight());
        PixelWriter pixelWriter = writer.getPixelWriter();
        PixelReader pixelReader = writer.getPixelReader();

        for(int i = 0; i < writer.getHeight(); i++){
            for(int j = 0; j < writer.getWidth(); j++){
                Color c = pixelReader.getColor(j, i);
                double red = c.getRed() * 255;
                double green = c.getRed() * 255;
                double blue = c.getBlue() * 255;

                if(red < 230 && green < 230 && blue < 230){
                    pixelWriter.setColor(j, i, Color.BLACK);
                }
                else {
                    pixelWriter.setColor(j, i, Color.WHITE);
                }
            }
        }

        ImageView iv = new ImageView(writer);
        iv.setFitWidth(1280);
        iv.setFitHeight(720);
        iv.getTransforms().add(new Rotate(90,Rotate.X_AXIS));
        root.getChildren().add(iv);



        final Scene scene = new Scene(group);
        scene.setOnKeyPressed(event -> {
            double sceneSize = Math.sqrt(scene.getWidth() * scene.getHeight());
            double screenIncrement = sceneSize / 5;
            if(event.isShiftDown() && event.getCode() == KeyCode.EQUALS){
                camera.getTransforms().stream().filter(node -> (node instanceof Translate)).forEach(node -> {
                    if(((Translate) node).getZ() <= -75 && ((Translate) node).getZ() >= -sceneSize){
                        ((Translate) node).setZ(((Translate) node).getZ() + screenIncrement);
                    }
                });
            }
            else if(event.isShiftDown() && event.getCode() == KeyCode.MINUS){
                camera.getTransforms().stream().filter(node -> (node instanceof Translate)).forEach(node -> {
                    if(((Translate) node).getZ() <= -50 && ((Translate) node).getZ() >= -sceneSize){
                        ((Translate) node).setZ(((Translate) node).getZ() - screenIncrement);
                    }
                });
            }
            else if(event.getCode() == KeyCode.W){
                camera.setTranslateY(camera.getTranslateZ() + screenIncrement);
            }
            else if(event.getCode() == KeyCode.D){
                camera.setTranslateX(camera.getTranslateY() + screenIncrement);
            }
            else if(event.getCode() == KeyCode.S){
                camera.setTranslateY(camera.getTranslateZ() - screenIncrement);
            }
            else if(event.getCode() == KeyCode.A){
                camera.setTranslateX(camera.getTranslateY() - screenIncrement);
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
