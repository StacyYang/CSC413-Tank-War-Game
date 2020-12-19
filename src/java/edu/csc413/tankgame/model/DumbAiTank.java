package edu.csc413.tankgame.model;
import java.util.Random;

public class DumbAiTank extends Tank{

    public DumbAiTank(String id, double x, double y, double angle){
        super(id, x, y, angle);
    }


    @Override
    public void move(GameState gameState ){
        moveForward();
        turnLeft();

        //DumbAitank is set to shoot randomly
        Random rand = new Random();
        if (rand.nextFloat() < 0.01) {
            shoot(gameState);
        }
    }
}
