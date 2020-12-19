package edu.csc413.tankgame.model;

public class PowerUp extends Entity{

    public PowerUp(String id, double x, double y, double angle) {
        super(id, x, y, 0);
    }//Constructor

    @Override
    public double getXBound(){return getX() + 24.0; }

    @Override
    public double getYBound(){return getY() + 24.0; }

    //leave the methods below empty.
    @Override
    public double getSpeed(){return 0; }

    @Override
    public void move(GameState gameState){ }

    @Override
    public void checkBounds(){}

    @Override
    public boolean isAtBorder(){ return false;}
}
