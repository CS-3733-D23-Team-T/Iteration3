package edu.wpi.tacticaltritons.robot;

import arduino.Arduino;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.FloatProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import lombok.Getter;

import java.util.List;

public class RobotComm {

    @Getter
    private static String com = "COM5"; //TODO change COM if necessary
    @Getter
    private static int baud = 115200;
    @Getter
    private static boolean checkConnection = true; //true for able to connect, false if unable to connect
    private static boolean checkComplete = false;
    public static DoubleProperty xRobotCoordinate = new SimpleDoubleProperty(-10), yRobotCoordinate = new SimpleDoubleProperty(-10), angleRobotCoordinate = new SimpleDoubleProperty(0);

    /**
     * re-check if the program can communicate with the robot, does not always need to be called
     * @param arduino the robot to check connection status
     */
    public static void checkConnection(Arduino arduino){
        checkConnection = arduino.openConnection();
        arduino.closeConnection();
    }

    private static String in = "";

    public static void readData(){
        Arduino robot = new Arduino(com, baud);
        in = "";
        if(checkConnection && !robot.openConnection()){
            System.out.println("Error connecting to robot");
            checkConnection = false;
        } else{
            while(in.equals("")){
                in = robot.serialRead();
            }
            System.out.println("Read [" + in + "] from robot");
            switch(in.charAt(0)){
                case 't':{
                    angleRobotCoordinate.set(Double.parseDouble(in.substring(2)));
                    break;
                }
                case 'd':{
                    double linearDistance = Double.parseDouble(in.substring(2));
                    xRobotCoordinate.set(linearDistance * Math.sin(angleRobotCoordinate.get()));
                    yRobotCoordinate.set(linearDistance * Math.cos(angleRobotCoordinate.get()));
                    break;
                }
                default:
                    break;
            }
            robot.closeConnection();
        }
    }

    private static void sendData(String data){
        Arduino robot = new Arduino(com, baud);
        if(checkConnection && !robot.openConnection()){
            System.out.println("Error connecting to robot");
            checkConnection = false;
        } else{
            robot.serialWrite(data + '\n');
            System.out.println("Wrote [" + data + "] to robot");
            robot.closeConnection();
        }
    }

    private static void drive(float angle, float distance){
        checkComplete = false;
        sendData("t:" + angle);
        while(!checkComplete()){
            readData();
        }
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        sendData("f:" + distance);
        while(!checkComplete()){
            readData();
        }
        checkComplete = true;
    }

    public static void setLED(boolean on){
        sendData("l:" + (on == true?"1":"0"));
    }

/*    public static void runRobot(float angle, float drive){
        Runnable robotRunnable = () -> {
            setLED(true);
            drive(angle,drive);
            setLED(false);
        };
        Thread robotThread = new Thread(robotRunnable);
        robotThread.start();
    }*/

    public static void runRobot(List<Float> angle, List<Float> drive){
        Runnable robotRunnable = () -> {
            Circle circle = drawObservableCircle();
            setLED(true);
            for(int i = 0; i < angle.size();i++){
                drive(angle.get(i),drive.get(i));
            }
            setLED(false);
            circle.setVisible(false);
        };
        Thread robotThread = new Thread(robotRunnable);
        robotThread.start();
    }


    /**
     * checks if the current task is reported finished by the robot
     * @return true if finished, false if still running
     */
    public static boolean checkComplete(){
        if(in.contains("Done")){
            return true;
        }
        else return false;
    }

    public static Circle drawObservableCircle() {
        Circle circle = new Circle();
        circle.setVisible(true);
        circle.setFill(Color.BLUE);
        circle.setStroke(Color.BLACK);
        circle.setStrokeWidth(3.0f);
        circle.centerXProperty().bind(xRobotCoordinate);
        circle.centerYProperty().bind(yRobotCoordinate);
        circle.setRadius(10.0);
        return circle;
    }
}

