package edu.csc413.tankgame.model;

import java.util.Random;

public class TurretAiTank extends Tank{

    public TurretAiTank(String id, double x, double y, double angle){
        super(id, x, y, angle);
    }

    public void shootMode(GameState gameState) {
        //TurretAiTank will shoot randomly
        Random rand = new Random();
        if (rand.nextFloat() < 0.001) {
            shoot(gameState);
        }
    }

    public void findTarget(GameState gameState) {
        Entity playerTank = gameState.getEntity(GameState.PLAYER_TANK_ID);
        // To figure out what angle the AI tank needs to face, we'll use the change
        // in the x and y axes between the AI and player tanks.
        double dx = playerTank.getX() - getX();
        double dy = playerTank.getY() - getY();
        // atan2 applies arctangent to the ratio of the two provided values.
        double angleToPlayer = Math.atan2(dy, dx);
        double angleDifference = getAngle() - angleToPlayer;
        // We want to keep the angle difference between -180 degrees and 180 degrees
        // for the next step. This ensures that anything outside of that range
        // is adjusted by 360 degrees at a time until it is, so that the angle is
        // still equivalent.
        angleDifference -= Math.floor(angleDifference / Math.toRadians(360.0) + 0.5) * Math.toRadians(360.0);

        if(angleDifference < -Entity.TANK_TURN_SPEED){
            turnRight();
        }else if(angleDifference > Entity.TANK_TURN_SPEED){
            turnLeft();
        }
    }

    @Override
    public void move(GameState gameState){
        findTarget(gameState);
        shootMode(gameState);
    }
}



