package edu.wpi.tacticaltritons.robot;

import arduino.Arduino;
import edu.wpi.tacticaltritons.database.Node;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.FloatProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import lombok.Getter;

import java.util.ArrayList;
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
    @Getter private static String in = "";
    private static boolean open = false;

    /**
     * re-check if the program can communicate with the robot, does not always need to be called
     * @param arduino the robot to check connection status
     */
    public static void checkConnection(Arduino arduino){
        checkConnection = arduino.openConnection();
        arduino.closeConnection();
    }

    public static void openConnection(Arduino arduino){
        if(!open){
            arduino.openConnection();
        }
        open = true;
    }

    public static void closeConnection(Arduino arduino){
        if(open){
            arduino.closeConnection();
        }
        open = false;
    }

    public static boolean readData(Arduino arduino){
        return readData(arduino,null);
    }

    public static boolean readData(Arduino arduino, Node node){
        in = "";
        while(in.equals("")){
            in = arduino.serialRead();
        }
        System.out.println("Read [" + in.substring(0,in.indexOf('\n')) + "] from robot");
        if(in.charAt(0) == 'd'){
            double linearDistance = Double.parseDouble(in.substring(2,in.indexOf('\n')));
            xRobotCoordinate.set(linearDistance * Math.cos(Math.toRadians(angleRobotCoordinate.get())) + node.getXcoord());
            yRobotCoordinate.set(-1*linearDistance * Math.sin(Math.toRadians(angleRobotCoordinate.get())) + node.getYcoord());
        }
        if(checkComplete()){
/*            if(node != null){
                xRobotCoordinate.set(node.getXcoord());
                yRobotCoordinate.set(node.getYcoord());
            }*/
            return true;
        }
        else return false;
    }

    private static void sendData(String data, Arduino arduino){
        arduino.serialWrite(data + '\n');
        System.out.println("Wrote [" + data + "] to robot");
    }

    private static void drive(float angle, float distance, Arduino arduino, Node node){
        checkComplete = false;
        sendData("t:" + angle, arduino);
        angleRobotCoordinate.set(angle);
        System.out.println(angle);
        while(!readData(arduino)){
            sleep(1);
        }

        sleep(0);

        sendData("f:" + distance, arduino);
        while(!readData(arduino, node)){
            sleep(1);
        }
        checkComplete = true;
    }

    public static void setLED(boolean on, Arduino arduino){
        sendData("l:" + (on == true?"1":"0"), arduino);
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

    public static void runRobot(List<Float> angle, List<Float> drive, List<Node> nodes){
        Runnable robotRunnable = () -> {
            xRobotCoordinate.set(nodes.get(0).getXcoord());
            yRobotCoordinate.set(nodes.get(0).getYcoord());
            Arduino robot = new Arduino(com, baud);
            openConnection(robot);
            setLED(true, robot);
            while(!readData(robot));
            for(int i = 0; i < angle.size();i++){
                drive(angle.get(i),drive.get(i), robot, nodes.get(i)); //TODO nodes.get(i+1)
                while (!checkComplete);
            }
            setLED(false, robot);
            closeConnection(robot);
            Thread.currentThread().interrupt();
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

    private static void sleep(int millis){
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}

