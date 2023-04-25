package edu.wpi.tacticaltritons.pathfinding;

public class Compass {

    int compass = 0%4;
    public int initialCompass;

    public Compass(int initialCompass){
        initialCompass = initialCompass;
    }

    public boolean facingNorth(){
        if(this.compass == 0){
            return true;
        }
        return false;
    }

    public boolean facingEast(){
        if(this.compass == 1){
            return true;
        }
        return false;
    }
    public boolean facingSouth(){
        if(this.compass == 2){
            return true;
        }
        return false;
    }
    public boolean facingWest(){
        if(this.compass == 3){
            return true;
        }
        return false;
    }


    public int getCompass() {
        return compass;
    }

    public void setCompass(int compass) {
        this.compass = compass % 4;
    }


}

//enum Direction {
//    NORTH(0),
//    EAST(90),
//    SOUTH(180),
//    WEST(270);
//
//    private final double angle;
//
//    Direction(double angle) {
//        this.angle = angle;
//    }
//
//    public double getAngle() {
//        return angle;
//    }
