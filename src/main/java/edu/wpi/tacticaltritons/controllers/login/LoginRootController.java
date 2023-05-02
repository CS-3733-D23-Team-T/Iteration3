package edu.wpi.tacticaltritons.controllers.login;

import javafx.fxml.FXML;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.StackPane;

public class LoginRootController {
    @FXML private StackPane loginStackPane;
    @FXML private ImageView loginImageView;

    @FXML
    private void initialize(){
        loginImageView.setFitWidth(loginStackPane.getWidth());
        loginImageView.setFitHeight(loginStackPane.getHeight());

        loginStackPane.heightProperty().addListener(((obs, o, n) -> {
            double scaleY = loginStackPane.getHeight() / loginImageView.getImage().getHeight();
            loginImageView.setScaleY(scaleY);
        }));
        loginStackPane.widthProperty().addListener(((obs, o, n) -> {
            double scaleX = loginStackPane.getWidth() / loginImageView.getImage().getWidth();
            loginImageView.setScaleX(scaleX);
        }));
    }
}
