/*
 * 
 */ 

//import libraries
#include <Arduino.h>
#include <ir_codes.h>
#include <IRdecoder.h>
#include <Chassis.h>
#include <Wire.h>
#include <stdio.h>

// Declare a chassis object with nominal dimensions
Chassis chassis;

void setLED(bool value);

float baseSpeed = 15;//cm / s //previously 20 cm/s

//robotStates enum initialized in Chassis.h to make states accessible by Chassis.cpp for moving to positions when required
ROBOT_STATE nextRobotState; //remember next robot state to go to once current state finishes (useful for drive to position completion)


//=================ALL ROBOTS=================

// Helper function for debugging by turning LED on/off
#define LED_PIN 13
void setLED(bool value)
{
  Serial.println("setLED()");
  digitalWrite(LED_PIN, value);
}

// A helper function to stop the motors
void idle(void)
{
  // Serial.println("idle()");
  // setLED(LOW);

  //stop motors 
  chassis.setMotorEfforts(0, 0);

  //set state to idle
  robotState = ROBOT_IDLE;
}

//stop the robot when called by changing state to idle and stopping motors
void handleMotionComplete(void)
{
  if(robotState != ROBOT_IDLE) idle(); //avoid calling it unnecessarily
}

bool checkWithinThreshold(float threshold){
  return abs(chassis.xPos - chassis.xPosDestination) <= threshold && abs(chassis.yPos - chassis.yPosDestination) <= threshold;
}


String rxString;//store partially complete messages sent from app to Romi
//read next character of message from app using UART, return true at end of message(new line char)
bool checkMessage(){
  if(Serial.available()){
    char c = Serial.read();//read character
    if(c == '\n'){//message complete at new line character
      return true;
    }
    else rxString += c;//keep adding to message
  }
  return false;//if message not finished sending, return false
}

//check for messages and process complete ones for relevant info
void handleMessage(){ 
  Serial.println("Received [" + rxString + "] from app");
  switch (rxString.charAt(0))
  {
    case 'c': {//coordinate
      float x = rxString.substring(2,rxString.indexOf("_")).toFloat();
      float y = rxString.substring(rxString.indexOf("_")).toFloat();
      robotState = DRIVE_TO_POINT;
      nextRobotState = ROBOT_IDLE;
      chassis.driveToPoint(x,y);
      break;
    }

    case 'l':{
      setLED(rxString.charAt(2) == '1' ? true:false);
      break;
    }

    default:
      break;
  }

  rxString = ""; //reset read string for new message once processed 
}



//initialize robot
void setup() 
{
  //initialize serial (printing to computer) and serial1 (printing to esp32)
  Serial.begin(115200);
  Wire.begin();
  Wire.setClock(100000ul);

  // initialize the chassis (which also initializes the motors)
  chassis.init();

  //configure PID controller coefficients for chassis motors
  leftMotor.setPIDCoeffs(2.0, .7, 0);
  rightMotor.setPIDCoeffs(2.0, .7, 0);
  
  //idle robot on start
  handleMotionComplete();
  robotState = ROBOT_IDLE;

  Serial.print("Test 123");
  setLED(LOW);

}


void loop()
{
  //update chassis motors and position
  chassis.loop();

  //read next char from ESP32/MQTT server and if a full message is received, process the message
  if(checkMessage()) handleMessage();

  switch(robotState){//state machine
    //=================ALL ROBOTS=================
    case ROBOT_IDLE:{
      idle();//stop robot motion
      Serial.print("Test 456");
      robotState = ROBOT_CLIMB;
      break;
    }
    case DRIVE_TO_POINT://check if robot reached within threshold distance of target specified, if so move on to next state
    {
      float threshold = 3.5;//threshold in cm for accuracy of moving to position 
      // if(checkApproachEvent(STOP_DISTANCE)) handleApproach();//check if there is an object in front of the robot
      //check if reached position
      if(checkWithinThreshold(threshold)) robotState = nextRobotState; //after finish driving to position, start or resume robot's main task        
      break;
    }
    case ROBOT_CLIMB:{
    }


    default:
    break;
  }
}