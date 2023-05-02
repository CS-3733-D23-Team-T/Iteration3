package edu.wpi.tacticaltritons.controllers;

import javafx.fxml.FXML;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

public class AboutController {
    @FXML private Text nameText;
    @FXML private Text positionText;
    @FXML private Text majorText;
    @FXML private Text factText;
    @FXML private BorderPane luisPane;
    @FXML private BorderPane youssefPane;
    @FXML private BorderPane markPane;
    @FXML private BorderPane quentinPane;
    @FXML private BorderPane jaiPane;
    @FXML private BorderPane sophiaPane;
    @FXML private BorderPane vincentPane;
    @FXML private BorderPane apolloPane;
    @FXML private BorderPane vanessaPane;
    @FXML private BorderPane jamesPane;

    @FXML
    public void initialize() {
        luisPane.setOnMouseClicked(event ->
        {
            nameText.setText("Name: Luis Aldarondo Jr");
            nameText.setVisible(true);
            positionText.setText("Position: Document Analyst and Part Time Software Engineer");
            positionText.setVisible(true);
            positionText.setTextAlignment(TextAlignment.LEFT);
            positionText.setWrappingWidth(245);
            positionText.setWrappingWidth(245);
            majorText.setText("Major: Robotics Engineering");
            majorText.setVisible(true);
            factText.setText("Fun fact: I'm a lefty");
            factText.setVisible(true);
        });
        youssefPane.setOnMouseClicked(event ->
        {
            nameText.setText("Name: Youssef Benchiki");
            nameText.setVisible(true);
            positionText.setText("Position: Full Time Software Engineer");
            positionText.setVisible(true);
            positionText.setTextAlignment(TextAlignment.LEFT);
            positionText.setWrappingWidth(245);
            majorText.setText("Major: Computer Science");
            majorText.setVisible(true);
            factText.setText("Fun fact: I like to surf");
            factText.setVisible(true);
        });
        markPane.setOnMouseClicked(event ->
        {
            nameText.setText("Name: Mark Caleca");
            nameText.setVisible(true);
            positionText.setText("Position: Project Manager and Part Time Software Engineer");
            positionText.setVisible(true);
            positionText.setTextAlignment(TextAlignment.LEFT);
            positionText.setWrappingWidth(245);
            majorText.setText("Major: Robotics Engineering");
            majorText.setVisible(true);
            factText.setText("Fun fact: I'm in Alpha Chi Rho and RhoBeta Epsilon");
            factText.setVisible(true);
        });
        quentinPane.setOnMouseClicked(event ->
        {
            nameText.setText("Name: Quentin Hall");
            nameText.setVisible(true);
            positionText.setText("Position: Assistant Lead Software Engineer");
            positionText.setVisible(true);
            positionText.setTextAlignment(TextAlignment.LEFT);
            positionText.setWrappingWidth(245);
            majorText.setText("Major: Computer Science");
            majorText.setVisible(true);
            factText.setText("Fun fact: I've traveled to 54 countries.");
            factText.setVisible(true);
        });
        jaiPane.setOnMouseClicked(event ->
        {
            nameText.setText("Name: Jai Jariwala");
            nameText.setVisible(true);
            positionText.setText("Position: Scrum Master and Part Time Software Engineer");
            positionText.setVisible(true);
            positionText.setTextAlignment(TextAlignment.LEFT);
            positionText.setWrappingWidth(245);
            majorText.setText("Major: Robotics Engineering");
            majorText.setVisible(true);
            factText.setText("Fun fact: I like to play the guitar");
            factText.setVisible(true);
        });
        sophiaPane.setOnMouseClicked(event ->
        {
            nameText.setText("Name: Sophia Kalavantis");
            nameText.setVisible(true);
            positionText.setText("Position: Product Owner and Part Time Software Engineer");
            positionText.setVisible(true);
            positionText.setTextAlignment(TextAlignment.LEFT);
            positionText.setWrappingWidth(245);
            majorText.setText("Major: Computer Science");
            majorText.setVisible(true);
            factText.setText("Fun fact: I'm in Alpha Phi and I have an identical twin sister");
            factText.setVisible(true);
        });
        vincentPane.setOnMouseClicked(event ->
        {
            nameText.setText("Name: Yuancen (Vincent) Pu");
            nameText.setVisible(true);
            positionText.setText("Position: Full Time Software Engineer");
            positionText.setVisible(true);
            positionText.setTextAlignment(TextAlignment.LEFT);
            positionText.setWrappingWidth(245);
            majorText.setText("Major: Robotics Engineering");
            majorText.setVisible(true);
            majorText.setVisible(true);
            factText.setText("Fun fact: fact");
            factText.setVisible(true);
        });
        apolloPane.setOnMouseClicked(event ->
        {
            nameText.setText("Name: Apollinaris (Apollo) Rowe");
            nameText.setVisible(true);
            positionText.setText("Position: Lead Software Engineer");
            positionText.setVisible(true);
            positionText.setTextAlignment(TextAlignment.LEFT);
            positionText.setWrappingWidth(245);
            majorText.setText("Major: Computer Science");
            majorText.setVisible(true);
            factText.setText("Fun fact: I'm from California");
            factText.setVisible(true);
        });
        vanessaPane.setOnMouseClicked(event ->
        {
            nameText.setText("Name: Vanessa Sam");
            nameText.setVisible(true);
            positionText.setText("Position: Full Time Software Engineer");
            positionText.setVisible(true);
            positionText.setTextAlignment(TextAlignment.LEFT);
            positionText.setWrappingWidth(245);
            majorText.setText("Major: Data Science");
            majorText.setVisible(true);
            factText.setText("Fun fact: I like to collect tiny cups and spoons from the different countries I visit");
            factText.setVisible(true);
        });
        jamesPane.setOnMouseClicked(event ->
        {
            nameText.setText("Name: James Yi");
            nameText.setVisible(true);
            positionText.setText("Position: Assistant Lead Software Engineer");
            positionText.setVisible(true);
            positionText.setTextAlignment(TextAlignment.LEFT);
            positionText.setWrappingWidth(245);
            majorText.setText("Major: Computer Science");
            majorText.setVisible(true);
            factText.setText("Fun fact: I went to Korean school alongside regular school");
            factText.setVisible(true);
        });
    }



}
