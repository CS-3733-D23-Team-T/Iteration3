package edu.wpi.tacticaltritons;

import edu.wpi.tacticaltritons.database.Tdb;
import edu.wpi.tacticaltritons.navigation.Navigation;
import edu.wpi.tacticaltritons.navigation.Screen;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Objects;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class App extends Application {

  @Getter private static Stage primaryStage;
  @Getter private static BorderPane rootPane;
  @Getter private static BorderPane navBar;
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

  @Override
  public void init() {
    log.info("Starting Up");
  }

  @Override
  public void start(Stage primaryStage) throws IOException {
    //    /* primaryStage is generally only used if one of your components require the stage to
    App.primaryStage = primaryStage;


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

    final Scene scene = new Scene(rootPane);
    scene.setOnKeyPressed(event -> {
      if(Navigation.screen.get().equals(Screen.CREATE_ACCOUNT) &&
        event.getCode().equals(KeyCode.ESCAPE)){
        Navigation.navigate(Screen.LOGIN);
      }
    });
    primaryStage.setScene(scene);
    primaryStage.show();
  }

  public void load() {}

  @Override
  public void stop() throws SQLException, ClassNotFoundException {
    Tdb.getConnection().close();
    log.info("Shutting Down");
  }
}
