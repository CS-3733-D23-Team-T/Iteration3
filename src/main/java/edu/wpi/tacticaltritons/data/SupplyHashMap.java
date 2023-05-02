package edu.wpi.tacticaltritons.data;

import edu.wpi.tacticaltritons.App;
import javafx.scene.image.Image;

import java.util.HashMap;
import java.util.Objects;

public class SupplyHashMap extends HashMap<String, Image> {
    public SupplyHashMap(){
       //Writting instruments
        this.put("White Out", new Image(Objects.requireNonNull(App.class.getResource("images/Supply_Request/White out.jpg")).toString()));
        this.put("Blue Pens", new Image(Objects.requireNonNull(App.class.getResource("images/Supply_Request/blue pens.jpg")).toString()));
        this.put("Black Pens", new Image(Objects.requireNonNull(App.class.getResource("images/Supply_Request/Black Pens.png")).toString()));
        this.put("Red Pens", new Image(Objects.requireNonNull(App.class.getResource("images/Supply_Request/red pens.jpg")).toString()));
        this.put("MultiColor Pens", new Image(Objects.requireNonNull(App.class.getResource("images/Supply_Request/Multi color pens.jpg")).toString()));
        this.put("Sharpie", new Image(Objects.requireNonNull(App.class.getResource("images/Supply_Request/Sharpies.png")).toString()));
        this.put("Wooden Pencil", new Image(Objects.requireNonNull(App.class.getResource("images/Supply_Request/wooden pencil update.png")).toString()));
        this.put("Mechanical Pencil", new Image(Objects.requireNonNull(App.class.getResource("images/Supply_Request/Mechanical Pencil.png")).toString()));



        //office basics
        this.put("Stapler", new Image(Objects.requireNonNull(App.class.getResource("images/Supply_Request/Stapler.png")).toString()));
        this.put("Staples", new Image(Objects.requireNonNull(App.class.getResource("images/Supply_Request/Staples.png")).toString()));
        this.put("Scissors", new Image(Objects.requireNonNull(App.class.getResource("images/Supply_Request/Scissors.png")).toString()));


        //orginization
        this.put("Binders", new Image(Objects.requireNonNull(App.class.getResource("images/Supply_Request/binders.jpg")).toString()));
        this.put("Calendar", new Image(Objects.requireNonNull(App.class.getResource("images/Supply_Request/calenders.png")).toString()));
        this.put("Desk Organizer", new Image(Objects.requireNonNull(App.class.getResource("images/Supply_Request/Desk Orginizer.png")).toString()));
        this.put("File", new Image(Objects.requireNonNull(App.class.getResource("images/Supply_Request/File.png")).toString()));
        this.put("File Folder", new Image(Objects.requireNonNull(App.class.getResource("images/Supply_Request/file foulder.jpg")).toString()));
        this.put("Labels", new Image(Objects.requireNonNull(App.class.getResource("images/Supply_Request/file labels.jpg")).toString()));
        this.put("Clips", new Image(Objects.requireNonNull(App.class.getResource("images/Supply_Request/Office Clips.png")).toString()));


        //mail room
        this.put("Card Envelopes", new Image(Objects.requireNonNull(App.class.getResource("images/Supply_Request/card envelopes.jpg")).toString()));


        //Documentation
        this.put("Notebook", new Image(Objects.requireNonNull(App.class.getResource("images/Supply_Request/notebook.jpg")).toString()));
        this.put("Sticky Notes", new Image(Objects.requireNonNull(App.class.getResource("images/Supply_Request/Sticky Notes.png")).toString()));


    }
}
