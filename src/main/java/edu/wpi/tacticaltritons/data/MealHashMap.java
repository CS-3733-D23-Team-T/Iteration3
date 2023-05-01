package edu.wpi.tacticaltritons.data;

import edu.wpi.tacticaltritons.App;
import javafx.scene.image.Image;

import java.util.HashMap;
import java.util.Objects;

public class MealHashMap extends HashMap<String, Image> {
    public MealHashMap(){
        //Seating
        this.put("Black Backless Stool", new Image(Objects.requireNonNull(App.class.getResource("images/furniture_request/BlackBacklessStool.png")).toString()));
        this.put("Black Stool with back", new Image(Objects.requireNonNull(App.class.getResource("images/furniture_request/BlackStoolwithback.png")).toString()));
        this.put("Black Saddle Bar Stool", new Image(Objects.requireNonNull(App.class.getResource("images/furniture_request/BlackSaddleBarStool.png")).toString()));
        this.put("Black Swivel Chair", new Image(Objects.requireNonNull(App.class.getResource("images/furniture_request/BlackSwivelChair.png")).toString()));
        this.put("White Swivel Chair", new Image(Objects.requireNonNull(App.class.getResource("images/furniture_request/WhiteSwivelChair.png")).toString()));
        this.put("Side Chair", new Image(Objects.requireNonNull(App.class.getResource("images/furniture_request/SideChair.png")).toString()));
        this.put("Mesh Back Side Chair", new Image(Objects.requireNonNull(App.class.getResource("images/furniture_request/MeshBackSideChair.png")).toString()));
        this.put("Accent Chair", new Image(Objects.requireNonNull(App.class.getResource("images/furniture_request/AccentChair.png")).toString()));
        this.put("Blue Stacking Chair", new Image(Objects.requireNonNull(App.class.getResource("images/furniture_request/BlueStackingChair.png")).toString()));
        this.put("Black Stacking Chair", new Image(Objects.requireNonNull(App.class.getResource("images/furniture_request/BlackStackingChair.png")).toString()));


        //Table
        this.put("Multishelf Desk", new Image(Objects.requireNonNull(App.class.getResource("images/furniture_request/MultishelfDesk.png")).toString()));
        this.put("Small White Desk", new Image(Objects.requireNonNull(App.class.getResource("images/furniture_request/SmallWhiteDesk.png")).toString()));
        this.put("Black Standing Desk", new Image(Objects.requireNonNull(App.class.getResource("images/furniture_request/BlackStandingDesk.png")).toString()));
        this.put("White Standing Desk", new Image(Objects.requireNonNull(App.class.getResource("images/furniture_request/WhiteStandingDesk.png")).toString()));
        this.put("Sit-to-Stand Converter", new Image(Objects.requireNonNull(App.class.getResource("images/furniture_request/Sit-to-StandConverter.png")).toString()));
        this.put("Wood Top Desk", new Image(Objects.requireNonNull(App.class.getResource("images/furniture_request/WoodTopDesk.png")).toString()));

    }
}
