package edu.csc413.tankgame.model;


/**
 * Model class representing a shell that has been fired by a tank. A shell has a position and an angle, as well as a
 * speed. Shells by default should be unable to turn and only move forward.
 */
// Notice that Shell has a lot in common with Tank. So we create abstract super class ---Entity to share code
// between the two classes so that the logic for e.g. moveForward, etc. are not duplicated.

public class Shell extends Entity{
    private static final String SHELL_ID_PREFIX = "shell-";
    private static final double MOVEMENT_SPEED = 4.0;

    private static long uniqueId = 0L;

    public Shell(double x, double y, double angle) {
        super(getUniqueId(), x, y, angle);
    }//Constructor

    @Override
    public double getSpeed(){
        return MOVEMENT_SPEED;
    }

    @Override
    public void move(GameState gameState){
        moveForward();
    }

    @Override
    public void checkBorder(){}

    @Override
    public boolean isAtBorder(){
        if(this.getX() < GameState.SHELL_X_LOWER_BOUND){
            return true;
        }

        if(this.getX() > GameState.SHELL_X_UPPER_BOUND){
            return true;
        }

        if(this.getY() < GameState.SHELL_Y_LOWER_BOUND){
            return true;
        }

        if(this.getY() > GameState.SHELL_Y_UPPER_BOUND){
            return true;
        }

        return false;
    }


    private static String getUniqueId() { //because shell generates its own IDs.
        return SHELL_ID_PREFIX + uniqueId++;
    }
}
