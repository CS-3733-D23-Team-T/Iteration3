package edu.wpi.tacticaltritons.robot;

import arduino.Arduino;

public class RobotComm {

    private static String com = "COM5"; //TODO change COM if necessary

    public static String readData(){
        String in = "";
        Arduino robot = new Arduino(com, 115200);
        if(!robot.openConnection()){
            System.out.println("Error connecting to robot");
        } else{
            while(in.equals("")){ //TODO make sure blocking code isn't blocking anything
                in = robot.serialRead();
            }
            System.out.println("Read [" + in + "] from robot");
            robot.closeConnection();
        }
        return in;
    }

    public static void sendData(String data){
        Arduino robot = new Arduino("COM4", 115200);
        if(!robot.openConnection()){
            System.out.println("Error connecting to robot");
        } else{
            robot.serialWrite(data);
            System.out.println("Wrote [" + data + "] to robot");
            robot.closeConnection();
        }
    }
}
