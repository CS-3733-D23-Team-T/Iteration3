package edu.wpi.tacticaltritons.controllers.login;

import javafx.fxml.FXML;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;

public class LoginRootController {
    @FXML private FlowPane loginCeneterFlowPane;
    @FXML private ImageView loginImageView;

    @FXML
    private void initialize(){
        loginImageView.setFitWidth(loginCeneterFlowPane.getWidth());
        loginImageView.setFitHeight(loginCeneterFlowPane.getHeight());

        loginCeneterFlowPane.heightProperty().addListener(((obs, o, n) -> {
            double scaleY = loginCeneterFlowPane.getHeight() / loginImageView.getImage().getHeight();
            loginImageView.setScaleY(scaleY);
        }));
        loginCeneterFlowPane.widthProperty().addListener(((obs, o, n) -> {
            double scaleX = loginCeneterFlowPane.getWidth() / loginImageView.getImage().getWidth();
            loginImageView.setScaleX(scaleX);
        }));
    }
}
