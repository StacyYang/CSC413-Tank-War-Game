package edu.csc413.tankgame.model;

/**
 * Model class representing a shell that has been fired by a tank. A shell has a position and an angle, as well as a
 * speed. Shells by default should be unable to turn and only move forward.
 */
// TODO: Notice that Shell has a lot in common with Tank. For full credit, you will need to find a way to share code
// between the two classes so that the logic for e.g. moveForward, etc. are not duplicated.
public class Shell {
    private static final String SHELL_ID_PREFIX = "shell-";
    private static final double MOVEMENT_SPEED = 4.0;

    private static long uniqueId = 0L;

    private final String id;
    private double x;
    private double y;
    private double angle;

    public Shell(double x, double y, double angle) {
        this.id = getUniqueId();
        this.x = x;
        this.y = y;
        this.angle = angle;
    }

    private String getUniqueId() {
        return SHELL_ID_PREFIX + uniqueId++;
    }
}
