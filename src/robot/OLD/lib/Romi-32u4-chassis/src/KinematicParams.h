#pragma once

//parameters
inline float wheel_track = 14.65; //cm
inline float wheel_diam = 7.07; //cm
inline float ticks_per_rotation = 1440; // from the datasheet
inline float robotRadius = wheel_track / 2.0;
inline float cmPerEncoderTick = 3.1416 * wheel_diam / ticks_per_rotation; //3.1415*7 / 1440