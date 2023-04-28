//Week 4 temp testing code 
//main code incorporated into main.cpp

// #include <Arduino.h>

// #include <ir_codes.h>
// #include <IRdecoder.h>

// #include <Chassis.h>
// #include <event_timer.h>

// #include <RangefinderFront.h>
// #include <RangefinderSide.h>

// const uint8_t IR_DETECTOR_PIN = 1;
// IRDecoder decoder(IR_DETECTOR_PIN);
// int16_t keyPress = 1;
// bool run = false;

// float baseSpeed = 20;
// float angleStart = 0;
// bool onRamp = false;
// Chassis chassis;

// #define LED_PIN 13
// void setLED(bool value)
// {
//   // Serial.println("setLED()");
//   digitalWrite(LED_PIN, value);
// }

// void idle(void)
// {
//   // Serial.println("idle()");
//   setLED(LOW);

//   //stop motors 
//   chassis.setMotorEfforts(0, 0);
// }

// void setup(){
//     Serial.begin(115200);
//     chassis.init();
//     idle();
//     leftMotor.setPIDCoeffs(2.0, .7, 0);
//     rightMotor.setPIDCoeffs(2.0, .7, 0);
//     decoder.init();
// }
// void loop(){
//     chassis.loop();
//     int16_t keyPress = decoder.getKeyCode();
//     if(keyPress == PLAY_PAUSE) {
//         run = true;
//         angleStart = chassis.filteredAngle;
//     }
//     if(run){
//         chassis.setMotorTargetSpeeds(baseSpeed,baseSpeed);
//         setLED(true);
//         if(!onRamp && chassis.filteredAngle - angleStart > 11.0){
//             onRamp = true;
//             delay(1000);
//         }
//         else if(onRamp && abs(chassis.filteredAngle - angleStart) < 3.0){
//             onRamp = false;
//             run = false;
//         }
//     }
//     else {
//         idle();
//         setLED(false);
//     }


// }

//Week 4 old code for reference

// #include <Arduino.h>

// #include <ir_codes.h>
// #include <IRdecoder.h>

// #include <Chassis.h>
// #include <event_timer.h>

// #include <RangefinderFront.h>
// #include <RangefinderSide.h>

// #include <Wire.h>
// #include <openmv.h>

// // #include <PubSubClient.h>
// // #include <mqtt.h>
// // #include <button.h>

// RangefinderSide sideRangefinder(17,0);//echo, trigger
// RangefinderFront frontRangefinder(11,12);

// float baseSpeed = 20;//cm / s
// float Lfromcent =  3;
// float Kp = .5;
// float currentError = 0, previousError = 0, derivativeError = 0;
// // float angy = 1;
// float targetDistance = 10;
// bool checkReachTaget = false;
// float angSpeedLeft = 0, angSpeedRight = 0;

// #define WALL_DISTANCE 20 // adjust as needed
// #define STOP_DISTANCE 20 // adjust as needed

// // Declare a chassis object with nominal dimensions
// Chassis chassis;

// EventTimer deadReckoningTimer, frontRangeTimer, driveTimer;

// // Setup the IR receiver/decoder object
// const uint8_t IR_DETECTOR_PIN = 1;
// IRDecoder decoder(IR_DETECTOR_PIN);

// float angleStart = 0;
// bool onRamp = false;

// // Helper function for debugging
// #define LED_PIN 13
// void setLED(bool value)
// {
//   // Serial.println("setLED()");
//   digitalWrite(LED_PIN, value);
// }

// // Define the robot states
// // moved to Chassis.h to make states accessible by Chassis.cpp for 
// // enum ROBOT_STATE {ROBOT_IDLE, ROBOT_WALL_FOLLOWING, ROBOT_TURN,ROBOT_DRIVE_FOR,DRIVE_TO_POINT};
// // ROBOT_STATE robotState = ROBOT_IDLE;

// // A helper function to stop the motors
// void idle(void)
// {
//   // Serial.println("idle()");
//   setLED(LOW);

//   //stop motors 
//   chassis.setMotorEfforts(0, 0);

//   //set state to idle
//   robotState = ROBOT_IDLE;
// }

// // A helper function to turn a set angle using dead reckoning
// void turn(float ang)
// {
//   float time;
//   int dir = ang >= 0 ? 1:-1;
//   ang = abs(ang);
//   Serial.println("turn()");
//   setLED(HIGH);

//   // TODO: Start the robot turning and set a timer
//   // arc distance = radius * angle = (width/2) * (angle / (2 * pi))
//   // t = distance / speed
//   if(!deadReckoningTimer.isRunning()){
//     time = 7 * (ang/(2*M_PI)) / (8 * baseSpeed);
//     deadReckoningTimer.start((long) (time*1000));
//   }
//   if(!deadReckoningTimer.checkExpired()){
//     chassis.setMotorTargetSpeeds(-baseSpeed*dir,baseSpeed*dir);
//   }
//   else{
//     idle();
//     delay(50);
//     robotState = ROBOT_WALL_FOLLOWING;
//   }
// }

// void beginWallFollowing(void)
// {
//   Serial.println("beginWallFollowing()");
//   setLED(HIGH);
//   robotState = ROBOT_WALL_FOLLOWING; 
// }

// // Used to check if the motions above are complete
// void handleMotionComplete(void)
// {
//     if(robotState != ROBOT_IDLE) idle(); //avoid calling it unnecessarily
// }

// // TODO: Add wall following handler
// void handleWallFollowing(float currDistance, float targetDistance = (float) WALL_DISTANCE, float speed = baseSpeed)
// {
//   if(currDistance > 40.0) currDistance = 40.0;
//   currentError = targetDistance - currDistance;
//   derivativeError = currentError - previousError;
//   float effort = Kp * ((targetDistance - currDistance) + (Lfromcent / speed) * derivativeError);
 
//   if(robotState == ROBOT_WALL_FOLLOWING)
//   {
//     float leftSpeed = baseSpeed + effort;
//     float rightSpeed = baseSpeed - effort;

// #ifdef __WALL_DEBUG__
//     Serial.print(currDistance);
//     Serial.print('\t');
//     Serial.print(leftSpeed);
//     Serial.print('\t');
//     Serial.print(rightSpeed);
//       Serial.print('\t');
//     Serial.print(effort);
//     Serial.print('\n');
// #endif

//     chassis.setMotorTargetSpeeds(leftSpeed, rightSpeed);
//   }
//   previousError = currentError;
// }

// // Handles a key press on the IR remote
// void handleKeyPress(int16_t keyPress)
// {
//   Serial.println("Key: " + String(keyPress));

//   //ENTER_SAVE idles, regardless of state -- E-stop
//   if(keyPress == ENTER_SAVE) idle(); 

//   switch(robotState)
//   {
//     case ROBOT_IDLE:
//       if(keyPress == PLAY_PAUSE){
//           beginWallFollowing();
//         }
//       else if(keyPress == NUM_1){
//         chassis.resetPose();
//         driveTimer.start(6000);
//         chassis.setMotorTargetSpeeds(10,10);
//         robotState = ROBOT_DRIVE_FOR;
//       }
//       else if(keyPress == NUM_2){
//         chassis.resetPose();
//         driveTimer.start(12000);
//         //s = r theta
//         float angSpeed = wheel_track/2 * (360 * PI/180) / 12;
//         chassis.setMotorTargetSpeeds(angSpeed,-angSpeed);
//         robotState = ROBOT_DRIVE_FOR;
//       }
//       else if(keyPress == NUM_3){
//         chassis.resetPose();
//         float baseSpeed = 10;
//         driveTimer.start((long)(2*PI*50*90*1000/(baseSpeed * 360)));
//         angSpeedLeft = baseSpeed - (wheel_track * baseSpeed / (2*50));
//         angSpeedRight = baseSpeed + (wheel_track * baseSpeed / (2*50));
//         chassis.setMotorTargetSpeeds(angSpeedLeft,angSpeedRight);
//         robotState = ROBOT_DRIVE_FOR; 
//       }
//       if(keyPress == NUM_4){
//         chassis.driveToPoint(30,30);
//         robotState = DRIVE_TO_POINT;
//         }
//       else if(keyPress == NUM_5){
//         chassis.driveToPoint(60,0);
//         robotState = DRIVE_TO_POINT;
//       }
//       else if(keyPress == NUM_6){
//         chassis.driveToPoint(30,-30);
//         robotState = DRIVE_TO_POINT;
//       }    
//       else if(keyPress == NUM_7){
//         chassis.driveToPoint(0,0);
//         robotState = DRIVE_TO_POINT;
//       }
//       else if(keyPress == STOP_MODE){
//         chassis.resetPose();
//       }
//     else if(keyPress == NUM_8){//ramp IMU
//         angleStart = chassis.filteredAngle;
//         chassis.setMotorTargetSpeeds(baseSpeed,baseSpeed);
//         robotState = ROBOT_CLIMB;
//         delay(1000); //allow for IMU values to settle
//       }
//       break;
      
//     case ROBOT_WALL_FOLLOWING:
//       //TODO: Add ability to increase/decrease speed
//       if(keyPress == VOLplus) baseSpeed ++;
//       else if(keyPress == VOLminus) baseSpeed --;
//       break;
 
//     default:
//       break;
//   }
// }
// /* method not in use
// void driveArc(float radius,float angle){ //TODO fix or delete
//   angle = radians(angle);
//   float Kp = 1.0;
//   //final x = initial x + 
//   float targetX = radius * cos(angle);
//   float targetY = radius * sin(angle);
//   float errorX = targetX - chassis.xPos;
//   float errorY = targetY - chassis.yPos;
//   //approx 10 second time
//   // driveTimer.start(5000);
//   //s = r theta
//   angSpeedLeft = (50 + ((wheel_track/2)/2)) * angle / 10;
//   angSpeedRight = (50 - ((wheel_track/2)/2)) * angle / 10;
//   chassis.setMotorTargetSpeeds(angSpeedLeft,angSpeedRight);
// }*/

// long prevTime = 0, currentTime = 0;
// // TODO: Add obstacle checker
// bool checkApproachEvent(float targetDistance)
// {
//   bool retVal = false;

//   // TODO: Add logic to detect wall approach as an event
//   //trigger start timer
//   if(frontRangefinder.getDistance() <= targetDistance && !frontRangeTimer.isRunning()){
//     frontRangeTimer.start(200);
//     retVal = false;
//   } 
//   //wall still detected after timer ended
//   else if(frontRangefinder.getDistance() <= targetDistance && frontRangeTimer.checkExpired()){
//     retVal = true;
//   }
//   //wall not detected after timer ended
//   else if(frontRangefinder.getDistance() > targetDistance){
//     frontRangeTimer.cancel();
//     retVal = false;
//   }
//   return retVal;
// }

// void handleApproach(void)
// {
//   Serial.println("Wall!");

//   chassis.setMotorEfforts(0, 0);

//   robotState = ROBOT_TURN;
// }

// /*
//  * This is the standard setup function that is called when the board is rebooted
//  * It is used to initialize anything that needs to be done once.
//  */
// void setup() 
// {
//   // This will initialize the Serial at a baud rate of 115200 for prints
//   // Be sure to set your Serial Monitor appropriately
//   Serial.begin(115200);

//   // initialize the chassis (which also initializes the motors)
//   chassis.init();
//   idle();
//   sideRangefinder.init();
//   delay(1000);
//   frontRangefinder.init();

//   // TODO: Set to what you found in the tuning part
//   leftMotor.setPIDCoeffs(2.0, .7, 0);
//   rightMotor.setPIDCoeffs(2.0, .7, 0);

//   // initialize the IR decoder
//   decoder.init();

//   delay(1000);

//   Serial.println("/setup()");
// }

// /*
//  * The main loop for the program. The loop function is repeatedly called
//  * after setup() is complete.
//  */
// int16_t keyPress = 1;

// void loop()
// {
//   // Must be called regularly!!!
//   chassis.loop();

//   // Everything below is in event checker/handler format

//   // Check for a key press on the remote
//   int16_t keyPress = decoder.getKeyCode();
//   if(keyPress >= 0) {
//     handleKeyPress(keyPress);
//     keyPress = -1;
//   }

//   // Check for a new distance reading
//   float wallDistance = sideRangefinder.getDistance();
//   switch(robotState){
//     case ROBOT_IDLE:
//       idle();
//       break;
//     case ROBOT_WALL_FOLLOWING:
//       handleWallFollowing(wallDistance);
//       // Check to see if we've approached a wall. TODO: implement the functions!
//       if(checkApproachEvent(STOP_DISTANCE)) handleApproach();
//       // Serial.println(sideRangefinder.getDistance());
//       break;
//     case ROBOT_TURN:
//       turn(-90);
//       idle();
//       break;
//     case ROBOT_DRIVE_FOR:
//       if(driveTimer.checkExpired()) {
//         robotState = ROBOT_IDLE;
//         Serial.print(chassis.xPos);
//         Serial.print("\t");
//         Serial.print(chassis.yPos);
//         Serial.print("\t");
//         Serial.println(chassis.thetaPos);  
//         Serial.println("stopping 6");      
//       }
//       break;
//     case DRIVE_TO_POINT://check if robot reached within threshold distance of target
//       float threshold = 3.0;
//       if(checkApproachEvent(STOP_DISTANCE)) handleApproach();
//       if(abs(chassis.xPos - chassis.xPosDestination) <= threshold && abs(chassis.yPos - chassis.yPosDestination) <= threshold){
//         robotState = ROBOT_IDLE;
//       }
//       break;
//     case ROBOT_CLIMB:
//         if(!onRamp && chassis.filteredAngle - angleStart > 11.0){
//             onRamp = true;
//         }
//         else if(onRamp && abs(chassis.filteredAngle - angleStart) < 3.0){
//             onRamp = false;
//             robotState = ROBOT_IDLE;
//             setLED(false);
//         }
//   }
// }
