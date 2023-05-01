package edu.wpi.tacticaltritons.data;

import edu.wpi.tacticaltritons.App;
import javafx.scene.image.Image;

import java.util.HashMap;
import java.util.Objects;

public class MealHashMap extends HashMap<String, Image> {
    public MealHashMap() {
        //logo
        this.put("Au Bon Pain", new Image(Objects.requireNonNull(App.class.getResource("images/restaurants/Au Bon Pain.png")).toString()));
        this.put("Cafe", new Image(Objects.requireNonNull(App.class.getResource("images/restaurants/Cafe.png")).toString()));
        this.put("Pizzeria", new Image(Objects.requireNonNull(App.class.getResource("images/restaurants/Pizzeria.png")).toString()));
        this.put("Pretzels", new Image(Objects.requireNonNull(App.class.getResource("images/restaurants/Pretzels.png")).toString()));

        //Au Bon Pain
        this.put("BLT", new Image(Objects.requireNonNull(App.class.getResource("images/restaurants/Au Bon Pain/BLT.png")).toString()));
        this.put("Chicken Sub", new Image(Objects.requireNonNull(App.class.getResource("images/restaurants/Au Bon Pain/Chicken Sub.png")).toString()));
        this.put("Grilled Cheese (V)", new Image(Objects.requireNonNull(App.class.getResource("images/restaurants/Au Bon Pain/Grilled Cheese (V).png")).toString()));
        this.put("Italian Sub", new Image(Objects.requireNonNull(App.class.getResource("images/restaurants/Au Bon Pain/Italian Sub.png")).toString()));
        this.put("Roast Beef Sandwich", new Image(Objects.requireNonNull(App.class.getResource("images/restaurants/Au Bon Pain/Roast Beef Sandwich.png")).toString()));
        this.put("Tuna Sandwich", new Image(Objects.requireNonNull(App.class.getResource("images/restaurants/Au Bon Pain/Tuna Sandwich.png")).toString()));
        this.put("Turkey Sub", new Image(Objects.requireNonNull(App.class.getResource("images/restaurants/Au Bon Pain/Turkey Sub.png")).toString()));
        this.put("water", new Image(Objects.requireNonNull(App.class.getResource("images/restaurants/Au Bon Pain/water.png")).toString()));


    }
}
