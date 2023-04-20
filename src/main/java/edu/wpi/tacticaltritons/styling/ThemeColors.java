package edu.wpi.tacticaltritons.styling;

import javafx.scene.paint.Color;

public enum ThemeColors {
    GRAY("#d2d2d2"),
    NAVY("#002d59"),
    LIGHT_NAVY("#002d59"),
    YELLOW("#f0ab0b"),
    LIGHT_YELLOW("#fd89b"),
    BACKGROUND_GRAY("#efefef"),
    ALERT("#c00b0b"),
    PATH_NODE_COLOR("#007fe3");

    private final String colorValue;
    ThemeColors(String colorHex) {
        this.colorValue = colorHex;
    }
    // i know you may think why i don't directly store color object here,
    // it's because it unable to load the object here when i first try, if you can solve, thank you
    // this is good i can try to do more testing -apollo


    public String getColor(){
        return colorValue;
    }
}
