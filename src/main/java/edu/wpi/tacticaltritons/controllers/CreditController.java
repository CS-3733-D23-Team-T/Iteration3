package edu.wpi.tacticaltritons.controllers;

import javafx.event.ActionEvent;
import javafx.event.EventType;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;

import java.util.HashMap;
import java.util.Map;

public class CreditController {
    @FXML private GridPane sourceGrid;
    private Map<String, Hyperlink> mealMap = new HashMap<>();
    @FXML private void handleHyperlink(ActionEvent event) {
        Hyperlink source = (Hyperlink) event.getSource();
        System.out.println("You clicked on " + source.getText());
    }
    @FXML private void initialize(){
        mealMap.put("Pizzeria", new Hyperlink("https://www.flaticon.com/free-icon/pizza_169885"));
        mealMap.put("Au Bon Pain", new Hyperlink("https://commons.wikimedia.org/wiki/File:Au_Bon_Pain_2018_logo.svg"));
        mealMap.put("Cafe", new Hyperlink("https://www.vectorstock.com/royalty-free-vector/starbucks-corporation-logo-vector-43663260"));
        mealMap.put("Pretzel Restaurant", new Hyperlink("https://dy5f5j6i37p1a.cloudfront.net/company/logos/158094/original/06b07b83d61811ec83bb37f7e3e9fe81.png"));

        Label nameLabel = new Label("Pizzeria");
        nameLabel.setAlignment(Pos.CENTER_LEFT);
        Label link = new Label(mealMap.get("Pizzeria").getText());
        link.setAlignment(Pos.CENTER);
        sourceGrid.addRow(1, nameLabel, link);
    }
}
