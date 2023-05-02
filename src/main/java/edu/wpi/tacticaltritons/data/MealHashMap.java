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

        //Cafe
        this.put("Brownie", new Image(Objects.requireNonNull(App.class.getResource("images/restaurants/Cafe/Brownie.png")).toString()));
        this.put("Brownie (V)", new Image(Objects.requireNonNull(App.class.getResource("images/restaurants/Cafe/Brownie (V).png")).toString()));
        this.put("Cold Water", new Image(Objects.requireNonNull(App.class.getResource("images/restaurants/Cafe/Cold Water.png")).toString()));
        this.put("Cookie", new Image(Objects.requireNonNull(App.class.getResource("images/restaurants/Cafe/Cookie.png")).toString()));
        this.put("Cookie (V)", new Image(Objects.requireNonNull(App.class.getResource("images/restaurants/Cafe/Cookie (V).png")).toString()));
        this.put("Hot Chocolate", new Image(Objects.requireNonNull(App.class.getResource("images/restaurants/Cafe/Hot Chocolate.png")).toString()));
        this.put("Hot Water", new Image(Objects.requireNonNull(App.class.getResource("images/restaurants/Cafe/Hot Water.png")).toString()));
        this.put("Milk", new Image(Objects.requireNonNull(App.class.getResource("images/restaurants/Cafe/Milk.png")).toString()));
        this.put("Regular Coffee", new Image(Objects.requireNonNull(App.class.getResource("images/restaurants/Cafe/Regular Coffee.png")).toString()));
        this.put("Tap Water", new Image(Objects.requireNonNull(App.class.getResource("images/restaurants/Cafe/Tap Water.png")).toString()));

        //Pizzeria
        this.put("Cheese Pizza", new Image(Objects.requireNonNull(App.class.getResource("images/restaurants/Pizzeria/Cheese Pizza.png")).toString()));
        this.put("Cold Pizza", new Image(Objects.requireNonNull(App.class.getResource("images/restaurants/Pizzeria/Cold Pizza.png")).toString()));
        this.put("Peperoni Pizza", new Image(Objects.requireNonNull(App.class.getResource("images/restaurants/Pizzeria/Peperoni Pizza.png")).toString()));
        this.put("Tap Waters", new Image(Objects.requireNonNull(App.class.getResource("images/restaurants/Pizzeria/Tap Water.png")).toString()));

        //Pretzels
        this.put("Cheese Pretzel", new Image(Objects.requireNonNull(App.class.getResource("images/restaurants/Pretzels/Cheese Pretzel.png")).toString()));
        this.put("Pretzel", new Image(Objects.requireNonNull(App.class.getResource("images/restaurants/Pretzels/Pretzel.png")).toString()));
        this.put("Pretzel (Extra Salt)", new Image(Objects.requireNonNull(App.class.getResource("images/restaurants/Pretzels/Pretzel (Extra Salt).png")).toString()));
        this.put("Pretzel (V)", new Image(Objects.requireNonNull(App.class.getResource("images/restaurants/Pretzels/Pretzel (V).png")).toString()));
        this.put("Stale Pretzel", new Image(Objects.requireNonNull(App.class.getResource("images/restaurants/Pretzels/Stale Pretzel.png")).toString()));
        this.put("Faucet Water", new Image(Objects.requireNonNull(App.class.getResource("images/restaurants/Pretzels/Tap Water.png")).toString()));
    }
}
