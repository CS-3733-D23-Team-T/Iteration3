package edu.wpi.tacticaltritons.robot;

import arduino.Arduino;
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
        System.out.println("test");
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
            setLED(true);
            for(int i = 0; i < angle.size();i++){
                drive(angle.get(i),drive.get(i));
                while(!checkComplete);
            }
            setLED(false);
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
}

