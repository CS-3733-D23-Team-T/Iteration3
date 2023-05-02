package edu.wpi.tacticaltritons;

import edu.wpi.tacticaltritons.data.FlowerHashMap;
import edu.wpi.tacticaltritons.data.FurnitureHashMap;
import edu.wpi.tacticaltritons.data.MealHashMap;
import edu.wpi.tacticaltritons.data.SupplyHashMap;
import edu.wpi.tacticaltritons.data.QuickNavigationMenuButtons;
import edu.wpi.tacticaltritons.database.Tdb;
import edu.wpi.tacticaltritons.navigation.Screen;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
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
  public static Image robot;
  public static Image importExport;

  public static String invalidMFXTextField;
  public static QuickNavigationMenuButtons quickNavigationMenuButtons;
  public static HashMap<String, Image> flowerHashMap;
  public static HashMap<String, Image> furnitureHashMap;
  public static HashMap<String, Image> mealHashMap;
  public static HashMap<String, Image> iconHashMap;
  public static HashMap<String, Image> supplyHashMap;

  @Override
  public void init() {
    log.info("Starting Up");
  }

  @Override
  public void start(Stage primaryStage) throws IOException {
    //    /* primaryStage is generally only used if one of your components require the stage to
    App.primaryStage = primaryStage;
    quickNavigationMenuButtons = new QuickNavigationMenuButtons();
    flowerHashMap = new FlowerHashMap();
    furnitureHashMap = new FurnitureHashMap();
    mealHashMap = new MealHashMap();
    //iconHashMap = new IconHashMap();
    supplyHashMap = new SupplyHashMap();


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

    robot =
            new Image(Objects.requireNonNull(getClass().getResource("images/map_page/robotIcon.png")).toString());

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

    final Scene scene = new Scene(rootPane);
    primaryStage.setScene(scene);
    primaryStage.setTitle("CS3733 - Tactical Tritons");
    primaryStage.show();
  }

  @Override
  public void stop() throws SQLException, ClassNotFoundException {
    Tdb.getInstance().getConnection().close();
    log.info("Shutting Down");
  }
}
