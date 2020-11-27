package edu.csc413.tankgame.model;


public class PlayerTank extends Tank{


    public PlayerTank(String id, double x, double y, double angle){
        super(id, x, y, angle);
    }//constructor

    @Override
    public void move(GameState gameState){
        if(gameState.getIsUpPressed()) {
            moveForward();
        }
        if(gameState.getIsDownPressed()){
            moveBackward();
        }
        if(gameState.getIsLeftPressed()){
            turnLeft();
        }
        if(gameState.getIsRightPressed()){
            turnRight();
        }
        if(gameState.getIsShootPressed()){
            shoot(gameState);
        }
    }
}
