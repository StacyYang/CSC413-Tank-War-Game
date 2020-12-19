package edu.csc413.tankgame.model;


public class SmartAiTank extends Tank{

    public SmartAiTank(String id, double x, double y, double angle){
        super(id, x, y, angle);
    }

    private int coolDown = 300; //set a coolDown time for AI, so it must wait 300 frames to shoot.

    @Override
    public void move(GameState gameState){
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

        if(angleDifference < -Entity.TANK_TURN_SPEED){  //
            turnRight();
        }else if(angleDifference > Entity.TANK_TURN_SPEED){//
            turnLeft();
        }

        double distance = Math.sqrt(dx * dx + dy * dy);
        if(distance > 400.0){
            moveForward();
        }else if(distance < 200.0){
            moveBackward() ;
        }

        --coolDown;
        if(coolDown == 0){
            shoot(gameState);
            coolDown = 300;
        }
    }
}



