package edu.csc413.tankgame.view;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

/**
 * RunGameView is the view representing the screen shown when the game is actually playing. As a result, RunGameView is
 * responsible for tracking what is being drawn on the screen in the form of DrawableEntities. The GameDriver class
 * should interact with RunGameView by asking it to draw a specific entity, named by its ID, at a location and angle.
 */
public class RunGameView extends JPanel {
    public static final Dimension SCREEN_DIMENSIONS = new Dimension(1024, 768);

    public static final String PLAYER_TANK_IMAGE_FILE = "player-tank.png";
    public static final double PLAYER_TANK_INITIAL_X = 200.0;
    public static final double PLAYER_TANK_INITIAL_Y = 150.0;
    public static final double PLAYER_TANK_INITIAL_ANGLE = Math.toRadians(0.0);

    public static final String AI_TANK_IMAGE_FILE = "ai-tank.png";
    public static final double AI_TANK_INITIAL_X = 750.0;
    public static final double AI_TANK_INITIAL_Y = 500.0;
    public static final double AI_TANK_INITIAL_ANGLE = Math.toRadians(180.0);

    // TODO: Feel free to add more constants if you would like multiple AI tanks -- just be sure to set them at
    // different initial locations.

    public static final String SHELL_IMAGE_FILE = "shell.png";

    private final BufferedImage worldImage;
    private final Map<String, DrawableEntity> drawableEntitiesById = new HashMap<>();

    public RunGameView() {
        worldImage = new BufferedImage(SCREEN_DIMENSIONS.width, SCREEN_DIMENSIONS.height, BufferedImage.TYPE_INT_RGB);
        setBackground(Color.BLACK);
    }

    /** Clears the DrawableEntities map. This should be invoked if the game is reset. */
    public void reset() {
        drawableEntitiesById.clear();
    }

    /**
     * Adds a new DrawableEntity to the view. The DrawableEntity must be identified by an ID, so that it can be updated
     * later on by that same ID. It must be initialized with an image file name, as well as an initial location and
     * angle.
     */
    public void addDrawableEntity(
            String id, String entityImageFile, double initialX, double initialY, double initialAngle) {
        DrawableEntity drawableEntity = new DrawableEntity(entityImageFile);
        drawableEntity.setLocationAndAngle(initialX, initialY, initialAngle);
        drawableEntitiesById.put(id, drawableEntity);
    }

    /**
     * Check whether key exists in hashmap (aka new bullets)
     */
    public boolean isRegistered(String id) {
        return drawableEntitiesById.containsKey(id);
    }


    /**
     * Removes the DrawableEntity identified by the provided ID from this view. This should be invoked if an entity has
     * been removed from the game and should no longer be drawn.
     */
    public void removeDrawableEntity(String id) {
        drawableEntitiesById.remove(id);
    }

    /** Updates the DrawableEntity with the provided ID to a new location and angle. */
    public void setDrawableEntityLocationAndAngle(String id, double x, double y, double angle) {
        drawableEntitiesById.get(id).setLocationAndAngle(x, y, angle);
    }

    @Override
    public void paintComponent(Graphics g) {
        // The "image" on which we draw the game screen is just a buffer colored entirely black. We can then draw the
        // DrawableEntities on top of the buffer.
        Graphics2D buffer = worldImage.createGraphics();
        buffer.setColor(Color.BLACK);
        buffer.fillRect(0, 0, SCREEN_DIMENSIONS.width, SCREEN_DIMENSIONS.height);

        for (DrawableEntity drawableEntity : drawableEntitiesById.values()) {
            buffer.drawImage(drawableEntity.getEntityImage(), drawableEntity.getAffineTransform(), null);
        }

        g.drawImage(worldImage, 0, 0, null);
    }
}
