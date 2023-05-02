package edu.wpi.tacticaltritons.robot;

/**
 * @author Mark Caleca
 * robot communication controller for program to make the robot pathfind
 */

import arduino.Arduino;
import edu.wpi.tacticaltritons.database.Node;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

import java.util.ArrayList;
import java.util.List;

public class RobotComm {

    private static final String COM = "COM5";
    private static final int BAUD = 115200;
    private static boolean checkConnection = true; //true for able to connect, false if unable to connect
    private static boolean checkComplete = false;
    private static DoubleProperty xRobotCoordinate = new SimpleDoubleProperty(-10), yRobotCoordinate = new SimpleDoubleProperty(-10), angleRobotCoordinate = new SimpleDoubleProperty(0);
    private static String in = "";
    private static boolean open = false;

    /**
     * re-check if the program can communicate with the robot, does not always need to be called
     * @param arduino the robot to check connection status
     */
    private static void checkConnection(Arduino arduino){
        checkConnection = arduino.openConnection();
        arduino.closeConnection();
    }

    /**
     * opens a connection to the Arduino if it is not already connected
     * @param arduino the Arduino to connect to
     */
    private static void openConnection(Arduino arduino){
        if(!open){
            arduino.openConnection();
        }
        open = true;
    }

    /**
     * disconnects the Arduino if it is already connected
     * @param arduino the Arduino to disconnect from
     */
    private static void closeConnection(Arduino arduino){
        if(open){
            arduino.closeConnection();
        }
        open = false;
    }

    /**
     * read data from the Arduino
     * @param arduino the Arduino to read from
     * @return true if the robot reports the task as complete, false otherwise
     */
    private static boolean readData(Arduino arduino){
        return readData(arduino,null);
    }

    /**
     * read data from the Arduino and set position coordinates
     * @param arduino the Arduino to read from
     * @param node the start node of the given movement, used to set the robot's position on the map
     * @return true if the robot reports the task as complete, false otherwise
     */
    private static boolean readData(Arduino arduino, Node node){
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
        return checkComplete();
    }

    /**
     * send data to the Arduino
     * @param data the data to send to the arduino
     * @param arduino the arduino to send data to
     */
    private static void sendData(String data, Arduino arduino){
        arduino.serialWrite(data + '\n');
        System.out.println("Wrote [" + data + "] to robot");
    }

    /**
     * send the command to rotate the arduino then go forward (polar coordinates)
     * @param angle the angle to rotate, in degrees
     * @param distance the distance to move, in cm
     * @param arduino the Arduino to control
     * @param node the start node of the maneuver
     */
    private static void drive(float angle, float distance, Arduino arduino, Node node){
        checkComplete = false;
        sendData("t:" + angle, arduino);
        angleRobotCoordinate.set(angle);
        System.out.println(angle);
        while(!readData(arduino)){
        }

        sendData("f:" + distance, arduino);
        while(!readData(arduino, node)){
        }
        checkComplete = true;
    }

    /**
     * turn the arduino's LED on or off
     * @param on true for on, false for off
     * @param arduino the ardunio to controll
     */
    private static void setLED(boolean on, Arduino arduino){
        sendData("l:" + (on ? "1" : "0"), arduino);
    }

    /**
     * controls the ardunio to follow the given map
     *  uses a new thread to preserve the map's functionality while running
     *  checks first if the arduino is connected
     * @param nodes the list of nodes representing the path to follow
     */
    public static void runRobot(List<Node> nodes){
        Runnable robotRunnable = () -> {
            Arduino robot = new Arduino(COM, BAUD);
            checkConnection(robot);
            if(checkConnection && nodes.size() > 0) {
                Node startNode = nodes.get(0);
                Node previousNode = startNode;
                List<Float> distance = new ArrayList<>(), angle = new ArrayList<>();
                for (Node node : nodes) {
                    if (!node.equals(startNode)) {
                        int xDiff = node.getXcoord() - previousNode.getXcoord();
                        int yDiff = -1 * (node.getYcoord() - previousNode.getYcoord()); //inverted
                        float dist = (float) Math.sqrt(Math.pow(xDiff, 2) + Math.pow(yDiff, 2));
                        float ang = (float) Math.toDegrees(Math.atan2(yDiff, xDiff));
                        distance.add(dist);
                        angle.add(ang);
                        previousNode = node;
                    }
                }
                xRobotCoordinate.set(nodes.get(0).getXcoord());
                yRobotCoordinate.set(nodes.get(0).getYcoord());
                openConnection(robot);
                setLED(true, robot);
                while(!readData(robot));
                for(int i = 0; i < angle.size();i++){
                    drive(angle.get(i),distance.get(i), robot, nodes.get(i));
                    while (!checkComplete);
                }
                setLED(false, robot);
                closeConnection(robot);
                Thread.currentThread().interrupt();
            }
        };
        Thread robotThread = new Thread(robotRunnable);
        robotThread.start();
    }


    /**
     * checks if the current task is reported finished by the robot
     * @return true if finished, false if still running
     */
    private static boolean checkComplete(){
        return in.contains("Done");
    }

    /**
     * draw a circle representing the arduino's location on the map, bound to the arduino's position
     * @return the circle
     */
    public static Circle drawObservableCircle() {
        Circle circle = new Circle();
        circle.setVisible(true);
        circle.setFill(Color.GRAY);
        circle.setStroke(Color.BLACK);
        circle.setStrokeWidth(3.0f);
        circle.centerXProperty().bind(xRobotCoordinate);
        circle.centerYProperty().bind(yRobotCoordinate);
        circle.setRadius(10.0);
        return circle;
    }
}

