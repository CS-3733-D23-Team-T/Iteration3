package edu.wpi.tacticaltritons.controllers.signage;

public class SignagePageInteraction {
    public static String[] forwardLocations = {};
    public static String[] leftLocations = {};
    public static String[] rightLocations = {};
    public static String[] backLocations = {};
    public static String presetName = "";
    public static boolean singleDisplay = false;
    public static boolean createSingleDisplay = false;
    public  static boolean editingSignage = false;
    public static boolean firstDisplay = true;

    public static void clear(){
        forwardLocations = new String[0];
        leftLocations = new String[0];
        rightLocations = new String[0];
        backLocations = new String[0];
        presetName = "";
        singleDisplay = false;
        createSingleDisplay = false;
        editingSignage = false;
        firstDisplay = true;
    }
    private SignagePageInteraction(){}
}
