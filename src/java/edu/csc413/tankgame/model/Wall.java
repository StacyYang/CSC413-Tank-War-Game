package edu.csc413.tankgame.model;

public class Wall extends Entity{

    private static long uniqueWallId = 0L;
    private static final String WALL_ID_PREFIX = "wall-";
    private int healthPoint = 5;

    public Wall(double x, double y, double angle) {
        super(getUniqueWallId(), x, y, 0);
    }//Constructor

    private static String getUniqueWallId() { //because shell generates its own IDs.
        return WALL_ID_PREFIX + uniqueWallId++;
    }

    public int getHealthPoint(){return healthPoint;}
    public void decreaseHealthPoint() {healthPoint--;}

    @Override
    public double getXBound(){return getX() + 32.0; }

    @Override
    public double getYBound(){return getY() + 32.0; }
    
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
