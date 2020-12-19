package edu.csc413.tankgame.model;

public class CushionAiTank extends TurretAiTank{

    public CushionAiTank(String id, double x, double y, double angle){
        super(id, x, y, angle);
    }

    private int coolDown = 300; //set a coolDown time for AI, so it must wait 300 frames to shoot.

    public void shootMode(GameState gameState) {
        --coolDown;
        if(coolDown == 0){
            shoot(gameState);
            coolDown = 300;
        }
    }

    @Override
    public void move(GameState gameState){

        findTarget(gameState);

        Entity playerTank = gameState.getEntity(GameState.PLAYER_TANK_ID);
        double dx = playerTank.getX() - getX();
        double dy = playerTank.getY() - getY();

        double distance = Math.sqrt(dx * dx + dy * dy);
        if(distance > 400.0){
            moveForward();
        }else if(distance < 200.0){
            moveBackward() ;
        }

        shootMode(gameState);

    }
}



