#include <Arduino.h>
#include <Romi32U4.h>
#include <Chassis.h>

Chassis chassis;
Romi32U4ButtonB buttonB;

int CPR = 270*2;//540
int prevPos = 0, pos = 0;
int timeDelay = 1000; //ms
float timeDelayS = 0.5;//s
bool servoStop = false;
int tolerance = 5;
int prevRead = analogRead(A0), read, delta;
unsigned long now;
//proportional controller to vary speed to each wheel
int kP = 10;
int kPmag = 1;
int kPtotal = 5;
//configure certain functions
const int rangeFinder = -1;
const int crossing = 1;
const int CW = 1;
const int CCW = -1;
//speed of each wheel
int effort = 95;
int baseSpeed = 20;

void setLED(bool value)
{
  Serial.print("setLED()");
  digitalWrite(13, value);
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
  Serial.print("Received_[" + rxString + "]_from app");
  now = millis();
  switch (rxString.charAt(0))
  {
    case 'f': { //forward
      float distance = rxString.substring(2).toFloat();
      chassis.driveFor(distance,baseSpeed);
      while(!chassis.checkMotionComplete()){
        if(millis() - now > 500){
          Serial.print("Driving"); //TODO get distance
          now = millis();
        }
      }
      Serial.print("Done");
      break;
    }

    case 't':{
      float angle = rxString.substring(2).toFloat();
      chassis.turnFor(angle, baseSpeed);   
      while(!chassis.checkMotionComplete()){
        if(millis() - now > 500){
          Serial.print("Rotating" + (String)angle); //TODO get rotation
          now = millis();
        }
      }
      Serial.print("Done");
      break;
    }

    case 'l':{ //LED
      setLED(rxString.charAt(2) == '1' ? true:false);
      break;
    }

    default:
      break;
  }

  rxString = ""; //reset read string for new message once processed 
}

void setup()
{
  //initialize blue motor and servo
  Serial.begin(1152000);
  chassis.init();
}


void loop()
{
  if(checkMessage()) handleMessage();
}