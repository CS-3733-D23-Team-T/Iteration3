package edu.wpi.tacticaltritons.robot;

import arduino.Arduino;

import java.io.Serial;

public class RobotComm {

    private static String com = "COM5"; //TODO change COM if necessary
    private static int baud = 115200;

    private static String in = "";

    public static void readData(){
        Arduino robot = new Arduino(com, baud);
        in = "";
        if(!robot.openConnection()){
            System.out.println("Error connecting to robot");
        } else{
            while(in.equals("")){ //TODO make sure blocking code isn't blocking anything
                in = robot.serialRead();
            }
//            in = in.substring(in.indexOf('\n')+1,in.indexOf('\n',in.indexOf('\n')+1));
            System.out.println("Read [" + in + "] from robot");
            robot.closeConnection();
        }
    }

    private static void sendData(String data){
        Arduino robot = new Arduino(com, baud);
        if(!robot.openConnection()){
            System.out.println("Error connecting to robot");
        } else{
            robot.serialWrite(data + '\n');
            System.out.println("Wrote [" + data + "] to robot");
            robot.closeConnection();
        }
    }

    public static void drive(float angle, float distance){
        sendData("t:" + angle);
        while(!in.contains("Done")){
            readData();
        }
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        System.out.println("test");
        sendData("f:" + distance);
        while(!in.contains("Done")){
            readData();
        }
    }

    public static void setLED(boolean on){
        sendData("l:" + (on == true?"1":"0"));
    }

/*    Runnable robotRunnable = new Runnable() {
        @Override
        public void run() {
            RobotComm.setLED(true);
            RobotComm.drive(20,20);
        }
    };

    Thread robotThread = new Thread(robotRunnable);
    robotThread.start();*/
}
