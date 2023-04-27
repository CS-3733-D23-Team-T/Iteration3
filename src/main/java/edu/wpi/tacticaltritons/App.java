package edu.wpi.tacticaltritons;

import com.google.common.util.concurrent.AtomicDouble;
import edu.wpi.tacticaltritons.data.FlowerHashMap;
import edu.wpi.tacticaltritons.data.FurnitureHashMap;
import edu.wpi.tacticaltritons.data.QuickNavigationMenuButtons;
import edu.wpi.tacticaltritons.database.Tdb;
import edu.wpi.tacticaltritons.navigation.Screen;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Point2D;
import javafx.scene.*;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
import javafx.scene.shape.DrawMode;
import javafx.scene.shape.Sphere;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;
import javafx.stage.Stage;
import javafx.util.Duration;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.checkerframework.checker.units.qual.A;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
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


    final Scene scene = new Scene(createContent());
    primaryStage.setScene(scene);
    primaryStage.setTitle("CS3733 - Tactical Tritons");
    primaryStage.show();
  }

  @Override
  public void stop() throws SQLException, ClassNotFoundException {
    Tdb.getConnection().close();
    log.info("Shutting Down");
  }

  private Parent createContent() throws Exception {
    Sphere sphere = new Sphere(2.5);
    sphere.setMaterial(new PhongMaterial(Color.FORESTGREEN));

    sphere.setTranslateZ(7);
    sphere.setTranslateX(2);

    Box box = new Box(5, 5, 5);
    box.setMaterial(new PhongMaterial(Color.RED));

    Translate pivot = new Translate();
    Rotate yRotate = new Rotate(0, Rotate.Y_AXIS);
    Rotate xRotate = new Rotate(0, Rotate.X_AXIS);

    // Create and position camera
    PerspectiveCamera camera = new PerspectiveCamera(true);
    camera.getTransforms().addAll (
            pivot,
            yRotate,
            xRotate,
            new Translate(0, 0, -50)
    );

    // Build the Scene Graph
    Group root = new Group();
    root.getChildren().add(camera);
    root.getChildren().add(box);
    root.getChildren().add(sphere);

    // set the pivot for the camera position animation base upon mouse clicks on objects


    // Use a SubScene
    SubScene subScene = new SubScene(
            root,
            300,300,
            true,
            SceneAntialiasing.BALANCED
    );
    subScene.setFill(Color.ALICEBLUE);
    subScene.setCamera(camera);
    Group group = new Group();
    group.getChildren().add(subScene);

    List<Point2D> points = new ArrayList<>();
    subScene.setOnMouseDragged(event -> {
      System.out.println("(" + event.getSceneX() + ", " + event.getSceneY() + ")");
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
            ((Rotate) node).setAngle(((Rotate) node).getAngle() + xAngle);
          }
          else if(((Rotate) node).getAxis().equals(Rotate.Y_AXIS) && !Double.isNaN(yAngle)){
            ((Rotate) node).setAngle(((Rotate) node).getAngle() + yAngle);
          }
        });


        System.out.println("X Translate Angle = " + xAngle + ", Y Translate Angle = " + yAngle);

        points.clear();
      }
    });

    return group;
  }

}
