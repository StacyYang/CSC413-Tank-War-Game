package edu.csc413.tankgame.model;


//superclass of tanks and shells
public abstract class Entity {
    protected static final double TANK_TURN_SPEED = Math.toRadians(3.0);

    private final String id;
    private double x;
    private double y;
    private double angle;

    public Entity(String id, double x, double y, double angle) {
        this.id = id;
        this.x = x;
        this.y = y;
        this.angle = angle;
    }

    public String getId() {
        return id;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public void setX(double newX) {
        this.x = newX;
    }

    public void setY(double newY){
        this.y = newY;
    }

    public double getAngle() {
        return angle;
    }

    public abstract double getSpeed(); //Because tank's MOVEMENT_SPEED is different from shell.

    //public abstract void move(GameState gameState ,MainView mainView);
    public abstract void move(GameState gameState);
    public abstract void checkBorder();
    public abstract boolean isAtBorder();




    // TODO: The methods below are provided so you don't have to do the math for movement. However, note that they are
    // protected. You should not be calling these methods directly from outside the Tank class hierarchy. Instead,
    // consider how to design your Tank class(es) so that a Tank can represent both a player-controlled tank and an AI
    // controlled tank.

    protected void moveForward() {
        x += getSpeed() * Math.cos(angle);
        y += getSpeed() * Math.sin(angle);
    }

    protected void moveBackward() {
        x -= getSpeed() * Math.cos(angle);
        y -= getSpeed() * Math.sin(angle);
    }

    protected void turnLeft() {
        angle -= TANK_TURN_SPEED;
    }

    protected void turnRight() {
        angle += TANK_TURN_SPEED;
    }

}
