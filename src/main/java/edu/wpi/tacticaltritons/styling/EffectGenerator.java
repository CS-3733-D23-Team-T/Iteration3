package edu.wpi.tacticaltritons.styling;

import io.github.palexdev.materialfx.controls.MFXFilterComboBox;
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.beans.Observable;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.*;
import org.controlsfx.control.spreadsheet.Grid;

public class EffectGenerator {
    private static final DropShadow shadow = new DropShadow(StylingParameters.shadowRadius, StylingParameters.shadowOffsetX, StylingParameters.shadowOffsetY, StylingParameters.shandowColor);
    public static void generateShadowEffect(Pane targetPane){
       for(Node pane: targetPane.getChildren()){
          Pane thisPane = (Pane) pane;
          thisPane.setEffect(shadow);
       }
    }



    public static void generateSpacing(FlowPane targetPane, int spacing){
        Insets marginInsets = new Insets(spacing / 2,spacing,spacing / 2,spacing);
        ObservableList<Node> childrenItems = targetPane.getChildren();
        for(Node item: childrenItems){
            targetPane.setMargin(item,marginInsets);
        }
    }

    public static void generateSpacing(VBox targetVBox, int spacing){
        Insets marginInsets = new Insets(spacing / 2,spacing,spacing / 2,spacing);
        ObservableList<Node> childrenItems = targetVBox.getChildren();
        for(Node item: childrenItems){
            targetVBox.setMargin(item,marginInsets);
        }
    }

    public static void generateSpacing(HBox targetHBox, int spacing){
        Insets marginInsets = new Insets(spacing / 2,spacing,spacing / 2,spacing);
        ObservableList<Node> childrenItems = targetHBox.getChildren();
        for(Node item: childrenItems){
            targetHBox.setMargin(item,marginInsets);
        }
    }

    public static void generateSpacing(GridPane targetGridPane, int spacing){
        Insets marginInsets = new Insets(spacing / 2,spacing,spacing / 2,spacing);
        ObservableList<Node> childrenItems = targetGridPane.getChildren();
        for(Node item: childrenItems){
            targetGridPane.setMargin(item,marginInsets);
        }
    }

    public static void alertOn(MFXTextField target,String alertText){
        target.setStyle("-fx-border-color: " + ThemeColors.ALERT.getColor());
        target.setFloatingText(alertText);
    }

    public static void alertOff(MFXTextField target,String normalText){
        target.setStyle("-fx-border-color: "+ ThemeColors.NAVY.getColor());
        target.setFloatingText(normalText);
    }

    public static void noFirstNameAlertOn(MFXTextField target){
        alertOn(target, StylingParameters.noFirstName);
    }
    public static void noFirstNameAlertOff(MFXTextField target){
        alertOff(target, StylingParameters.FirstName);
    }
    public static void noLastNameAlertOn(MFXTextField target){
        alertOn(target, StylingParameters.LastName);
    }
    public static void noLastNameAlertOff(MFXTextField target){
        alertOff(target, StylingParameters.LastName);
    }
    public static void noDateAlertOn(MFXTextField target){
        alertOn(target, StylingParameters.noFirstName);
    }
    public static void noDateAlertOff(MFXTextField target){
        alertOff(target, StylingParameters.FirstName);
    }
    public static void noRoomAlertOn(MFXTextField target){
        alertOn(target, StylingParameters.noRoom);
    }
    public static void noRoomAlertOff(MFXTextField target){
        alertOff(target, StylingParameters.Room);
    }
    public static void noTimeAlertOn(MFXTextField target){
        alertOn(target, StylingParameters.noTime);
    }
    public static void noTimeAlertOff(MFXTextField target){
        alertOff(target, StylingParameters.Time);
    }
    public static void noAssigendStaff(MFXFilterComboBox assignedComboBox) {
        alertOn(assignedComboBox, StylingParameters.assignedComboBox);
    }
    public static void noAssigendStaffOff(MFXFilterComboBox assignedComboBox) {
        alertOff(assignedComboBox, StylingParameters.assignedComboBox);
    }


}
