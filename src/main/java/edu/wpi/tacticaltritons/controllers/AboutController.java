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
            nameText.setText("Name: Luis");
            nameText.setVisible(true);
            positionText.setText("Position: Document");
            positionText.setVisible(true);
            positionText.setTextAlignment(TextAlignment.LEFT);
            positionText.setWrappingWidth(245);
            positionText.setWrappingWidth(245);
            majorText.setText("Major: major");
            majorText.setVisible(true);
            factText.setText("Fun fact: I'm a lefty");
            factText.setVisible(true);
        });
        youssefPane.setOnMouseClicked(event ->
        {
            nameText.setText("Name: Youssef");
            nameText.setVisible(true);
            positionText.setText("Position: Full time");
            positionText.setVisible(true);
            positionText.setTextAlignment(TextAlignment.LEFT);
            positionText.setWrappingWidth(245);
            majorText.setText("Major: major");
            majorText.setVisible(true);
            factText.setText("Fun fact: fact");
            factText.setVisible(true);
        });
        markPane.setOnMouseClicked(event ->
        {
            nameText.setText("Name: Mark");
            nameText.setVisible(true);
            positionText.setText("Position: Manager");
            positionText.setVisible(true);
            positionText.setTextAlignment(TextAlignment.LEFT);
            positionText.setWrappingWidth(245);
            majorText.setText("Major: major");
            majorText.setVisible(true);
            factText.setText("Fun fact: I'm in Alpha Chi Rho and RhoBeta Epsilon");
            factText.setVisible(true);
        });
        quentinPane.setOnMouseClicked(event ->
        {
            nameText.setText("Name: Quentin");
            nameText.setVisible(true);
            positionText.setText("Position: Assistant Lead");
            positionText.setVisible(true);
            positionText.setTextAlignment(TextAlignment.LEFT);
            positionText.setWrappingWidth(245);
            majorText.setText("Major: major");
            majorText.setVisible(true);
            factText.setText("Fun fact: I've traveled to 54 countries.");
            factText.setVisible(true);
        });
        jaiPane.setOnMouseClicked(event ->
        {
            nameText.setText("Name: Jai");
            nameText.setVisible(true);
            positionText.setText("Position: Scrum");
            positionText.setVisible(true);
            positionText.setTextAlignment(TextAlignment.LEFT);
            positionText.setWrappingWidth(245);
            majorText.setText("Major: major");
            majorText.setVisible(true);
            factText.setText("Fun fact: fact");
            factText.setVisible(true);
        });
        sophiaPane.setOnMouseClicked(event ->
        {
            nameText.setText("Name: Sophia");
            nameText.setVisible(true);
            positionText.setText("Position: Product Owner");
            positionText.setVisible(true);
            positionText.setTextAlignment(TextAlignment.LEFT);
            positionText.setWrappingWidth(245);
            majorText.setText("Major: major");
            majorText.setVisible(true);
            factText.setText("Fun fact: I'm in Alpha Phi and I have an identical twin sister");
            factText.setVisible(true);
        });
        vincentPane.setOnMouseClicked(event ->
        {
            nameText.setText("Name: Vincent");
            nameText.setVisible(true);
            positionText.setText("Position: Full Time");
            positionText.setVisible(true);
            positionText.setTextAlignment(TextAlignment.LEFT);
            positionText.setWrappingWidth(245);
            majorText.setText("Major: major");
            majorText.setVisible(true);
            majorText.setVisible(true);
            factText.setText("Fun fact: fact");
            factText.setVisible(true);
        });
        apolloPane.setOnMouseClicked(event ->
        {
            nameText.setText("Name: Apollo");
            nameText.setVisible(true);
            positionText.setText("Position: Lead");
            positionText.setVisible(true);
            positionText.setTextAlignment(TextAlignment.LEFT);
            positionText.setWrappingWidth(245);
            majorText.setText("Major: major");
            majorText.setVisible(true);
            factText.setText("Fun fact: I'm from California");
            factText.setVisible(true);
        });
        vanessaPane.setOnMouseClicked(event ->
        {
            nameText.setText("Name: Vanessa");
            nameText.setVisible(true);
            positionText.setText("Position: Full Time");
            positionText.setVisible(true);
            positionText.setTextAlignment(TextAlignment.LEFT);
            positionText.setWrappingWidth(245);
            majorText.setText("Major: major");
            majorText.setVisible(true);
            factText.setText("Fun fact: fact");
            factText.setVisible(true);
        });
        jamesPane.setOnMouseClicked(event ->
        {
            nameText.setText("Name: James");
            nameText.setVisible(true);
            positionText.setText("Position: Assistant Lead");
            positionText.setVisible(true);
            positionText.setTextAlignment(TextAlignment.LEFT);
            positionText.setWrappingWidth(245);
            majorText.setText("Major: major");
            majorText.setVisible(true);
            factText.setText("Fun fact: I went to Korean school alongside regular school");
            factText.setVisible(true);
        });
    }



}
