package edu.wpi.tacticaltritons.data;

import edu.wpi.tacticaltritons.App;
import javafx.scene.image.Image;

import java.util.HashMap;
import java.util.Objects;

public class MealHashMap extends HashMap<String, Image> {
    public MealHashMap(){
        //logo
        this.put("Au Bon Pain", new Image(Objects.requireNonNull(App.class.getResource("images/restaurants/Au Bon Pain.png")).toString()));
        this.put("Cafe", new Image(Objects.requireNonNull(App.class.getResource("images/restaurants/Cafe.png")).toString()));
        this.put("Pizzeria", new Image(Objects.requireNonNull(App.class.getResource("images/restaurants/Pizzeria.png")).toString()));
        this.put("Pretzels", new Image(Objects.requireNonNull(App.class.getResource("images/restaurants/Pretzels.png")).toString()));


    }
}
