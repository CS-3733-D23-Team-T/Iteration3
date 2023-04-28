// #include <Arduino.h>
#include <Chassis.h>
// #include <Romi32U4Motors.h>
// #include <KinematicParams.h>

// We'll declare motors as global to make the ISRs happier, but we'll put them in Chassis.cpp
// to keep things organized

LeftMotor leftMotor;
RightMotor rightMotor;

int prevLeft = 0, prevRight = 0;

Chassis::Chassis(void) {}

void Chassis::init(void)
{  
    noInterrupts(); //disable interupts while we mess with the Timer4 registers
  
    //sets up timer 4
    TCCR4A = 0x00; //disable some functionality -- no need to worry about this
    TCCR4B = 0x0B; //sets the prescaler -- look in the handout for values
    TCCR4C = 0x04; //toggles pin 6 at the timer frequency
    TCCR4D = 0x00; //normal mode

    /*
    * EDIT THE LINE BELOW WITH YOUR VALUE FOR TOP
    */

    OCR4C = 249;   //TOP goes in OCR4C 

    TIMSK4 = 0x04; //enable overflow interrupt

    interrupts(); //re-enable interrupts

    // init the motors
    Romi32U4Motor::init();

    //pinMode(6, OUTPUT); //COMMENT THIS OUT TO SHUT UP THE PIEZO!!!
    prevLeft = 0;
    prevRight = 0;

    estimatedPitchAngle = 0;
    prevAngle = 0;
}

void Chassis::loop(void)
{
    if(readyToPID)
    {
        if(readyToPID > 1) Serial.println("Missed update in Chassis::loop()");
        
        update();
        readyToPID = 0;
    }
}

void Chassis::update(void)
{
    leftMotor.update();
    rightMotor.update();
    updatePose();
}

void Chassis::setMotorTargetSpeeds(float leftTicksPerInterval, float rightTicksPerInterval)
{
    leftMotor.setTargetSpeed(leftTicksPerInterval);
    rightMotor.setTargetSpeed(rightTicksPerInterval);
}

/*
 * ISR for timing. On overflow, it takes a 'snapshot' of the encoder counts and raises a flag to let
 * the main program it is time to execute the PID calculations.
 */
ISR(TIMER4_OVF_vect)
{
  //Capture a "snapshot" of the encoder counts for later processing
  leftMotor.calcEncoderDelta();
  rightMotor.calcEncoderDelta();

  chassis.readyToPID++;
}

//Pose Data Members
float xPos, yPos, thetaPos, R;
void Chassis::resetPose(){
    xPos = 0;
    yPos = 0;
    prevLeft = 0;
    prevRight = 0;
    thetaPos = 0;
    R = 0;
    leftMotor.getAndResetCount();
    rightMotor.getAndResetCount();

}
void Chassis::updatePose(){
    //ICC method
    int16_t readLeft = leftMotor.getCount();
    int16_t readRight = rightMotor.getCount();
    int16_t leftMotorCount = readLeft - prevLeft;
    int16_t rightMotorCount = readRight - prevRight;
    prevLeft = readLeft;
    prevRight = readRight;
    float deltaAngle = (rightMotorCount - leftMotorCount) * cmPerEncoderTick / wheel_track;
    float dNought = (rightMotorCount + leftMotorCount) * cmPerEncoderTick / 2.0;
    if(rightMotorCount == leftMotorCount){
        xPos += dNought * cos(thetaPos);
        yPos += dNought * sin(thetaPos);
        //thetaPos stays same
    }
    else{
        R = (wheel_track / 2.0) * ((rightMotorCount + leftMotorCount) / (rightMotorCount - leftMotorCount));// * cmPerEncoderTick;
        xPos += R*(sin(thetaPos + deltaAngle) - sin(thetaPos));
        yPos += R*(cos(thetaPos) - cos(thetaPos + deltaAngle));
        thetaPos += deltaAngle;
    }
    bool debug = false;
    if(debug && millis() % 5 == 0){
        Serial.print(deltaAngle);
        Serial.print("\t");
        Serial.print(thetaPos * 180/3.1415);
        Serial.print("\t");
        Serial.print(xPos);
        Serial.print("\t");
        Serial.print(yPos);
        Serial.print("\t");
        Serial.print(dNought);
        Serial.print("\t");
        Serial.print(rightMotorCount);
        Serial.print("\t");
        Serial.print(leftMotorCount);
        Serial.print("\t");
        Serial.println(R);
    }
    if(robotState == DRIVE_TO_POINT){
        driveToPoint(xPosDestination,yPosDestination);
    }
}
void Chassis::driveToPoint(float x, float y){
    xPosDestination = x;
    yPosDestination = y;
    float errorXPos = x - xPos;
    float errorYPos = y - yPos;
    float targetAngle = atan2(errorYPos,errorXPos);
    float errorDistance = sqrt(pow(errorXPos,2)+pow(errorYPos,2));
    float errorThetaPos = targetAngle - thetaPos;
    float KpDistance = 0.3;
    float KpTheta = 1.2;
    float angSpeedLeft = KpDistance * errorDistance - (KpTheta * errorThetaPos * wheel_track / 2);
    float angSpeedRight = KpDistance * errorDistance + (KpTheta * errorThetaPos * wheel_track / 2);
    chassis.setMotorTargetSpeeds(angSpeedLeft,angSpeedRight);
    bool debug = true;
    if(millis() % 6 == 0 && debug){
        Serial.print(errorDistance);
        Serial.print("\t");
        Serial.print(degrees(thetaPos));
        Serial.print("\t");
        Serial.print(degrees(targetAngle));        
        Serial.print("\t");
        Serial.print(degrees(errorThetaPos));
        Serial.print("\t");
        Serial.print(xPos);
        Serial.print("\t");
        Serial.print(errorXPos);
        Serial.print("\t");
        Serial.print(yPos);
        Serial.print("\t");
        Serial.print(errorYPos);
        Serial.print("\t");
        Serial.print(angSpeedLeft);
        Serial.print("\t");
        Serial.println(angSpeedRight);
    }
}