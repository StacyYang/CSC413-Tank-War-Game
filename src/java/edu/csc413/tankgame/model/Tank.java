package edu.csc413.tankgame.model;

/**
 * Model class representing a tank in the game. A tank has a position and an angle. It has a movement speed and a turn
 * speed, both represented below as constants.
 */
// Notice that Tank has a lot in common with Shell. So we create abstract super class ---Entity to share code
// between the two classes so that the logic for e.g. moveForward, etc. are not duplicated.
public abstract class Tank extends Entity{

    private double MOVEMENT_SPEED = 2.0;
    private int healthPoint = 7;

    public Tank(String id, double x, double y, double angle){
        super(id, x, y, angle);
    }

    public int getHealthPoint(){return healthPoint;}
    public void decreaseHealthPoint() {healthPoint--;}
    public void powerUp(){
        healthPoint = healthPoint + 5;
        MOVEMENT_SPEED = MOVEMENT_SPEED * 2;
    }


    @Override
    public double getXBound(){return getX() + 55.0; }

    @Override
    public double getYBound(){return getY() + 55.0; }

    @Override
    public double getSpeed(){
        return MOVEMENT_SPEED;
    }

    @Override
    public void checkBounds(){
        if(this.getX() < GameState.TANK_X_LOWER_BOUND){
            this.setX(GameState.TANK_X_LOWER_BOUND);
        }

        if(this.getX() > GameState.TANK_X_UPPER_BOUND){
            this.setX(GameState.TANK_X_UPPER_BOUND);
        }

        if(this.getY() < GameState.TANK_Y_LOWER_BOUND){
            this.setY(GameState.TANK_Y_LOWER_BOUND);
        }

        if(this.getY() > GameState.TANK_Y_UPPER_BOUND){
            this.setY(GameState.TANK_Y_UPPER_BOUND);
        }
    }

    @Override
    public boolean isAtBorder(){ return false;}

    protected void shoot(GameState gameState){
        Shell bullet = new Shell(getShellX(), getShellY(), getAngle(), getId());
        gameState.addShell(bullet);
    }


    // The following methods will be useful for determining where a shell should be spawned when it
    // is created by this tank. It needs a slight offset so it appears from the front of the tank,
    // even if the tank is rotated. The shell should have the same angle as the tank.
    private double getShellX() {
        return getX() + 30.0 * (Math.cos(getAngle()) + 0.5);
    }
    private double getShellY() {
        return getY() + 30.0 * (Math.sin(getAngle()) + 0.5);
    }
}
