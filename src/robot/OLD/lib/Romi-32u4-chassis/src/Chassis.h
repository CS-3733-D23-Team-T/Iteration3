#pragma once

#include <Arduino.h>
#include <Romi32U4Motors.h>
#include <Wire.h> // I2C library

class Chassis
{
protected:

public:
    uint8_t readyToPID = 0;

    Chassis(void);

    void init(void);
    void loop(void);
    void update(void);

    void setMotorEfforts(int16_t left, int16_t right) 
        {leftMotor.setMotorEffort(left); rightMotor.setMotorEffort(right);}

    void setMotorTargetSpeeds(float leftTicksPerInterval, float rightTicksPerInterval);

    //Pose Data Members
    float xPos, yPos, thetaPos, xPosDestination, yPosDestination, observedAngle, estimatedPitchAngle, filteredAngle, bias = 0, prevAngle;
    void updatePose();
    void updatePitch();
    void resetPose();
    void driveToPoint(float x, float y);

};

extern Chassis chassis;

// Define the robot states
enum ROBOT_STATE {ROBOT_IDLE, ROBOT_WALL_FOLLOWING, ROBOT_TURN,DRIVE_TO_POINT, ROBOT_CLIMB, ROBOT_CLIMB_TURN, ROBOT_ESCAPE_J, ROBOT_ESCAPE_I, ROBOT_ESCAPE};
inline ROBOT_STATE robotState = ROBOT_IDLE;