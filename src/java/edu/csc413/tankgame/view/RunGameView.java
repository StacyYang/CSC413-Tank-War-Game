package edu.csc413.tankgame.view;

import edu.csc413.tankgame.GameDriver;
import edu.csc413.tankgame.model.Tank;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.*;
import java.util.List;

public class RunGameView extends JPanel {
    public static final Dimension SCREEN_DIMENSIONS = new Dimension(1024, 768);

    public static final String PLAYER_TANK_IMAGE_FILE = "player-tank.png";
    public static final double PLAYER_TANK_INITIAL_X = 250.0;
    public static final double PLAYER_TANK_INITIAL_Y = 200.0;
    public static final double PLAYER_TANK_INITIAL_ANGLE = Math.toRadians(0.0);

    public static final String AI_TANK_IMAGE_FILE = "ai-tank.png";
    public static final double AI_TANK_INITIAL_X = 700.0;
    public static final double AI_TANK_INITIAL_Y = 500.0;
    public static final double AI_TANK_INITIAL_ANGLE = Math.toRadians(180.0);

    public static final double AI_TANK_2_INITIAL_X = 700.0;
    public static final double AI_TANK_2_INITIAL_Y = 200.0;
    public static final double AI_TANK_2_INITIAL_ANGLE = Math.toRadians(180.0);

    public static final String POWER_UP_IMAGE_FILE = "powerup.png";
    public static final double POWER_UP_INITIAL_X = 50.0;
    public static final double POWER_UP_INITIAL_Y = 50.0;
    public static final double POWER_UP_INITIAL_ANGLE = 0;

    public static final String SHELL_IMAGE_FILE = "shell.png";

    private static final String SHELL_EXPLOSION_FILE_PREFIX = "shell-explosion-";
    private static final String SHELL_EXPLOSION_FILE_SUFFIX = ".png";

    public static final AnimationResource SHELL_EXPLOSION_ANIMATION =
            new AnimationResource(SHELL_EXPLOSION_FILE_PREFIX, SHELL_EXPLOSION_FILE_SUFFIX, 6);
    public static final int SHELL_EXPLOSION_FRAME_DELAY = 3;
    public static final double SHELL_EXPLOSION_WIDTH = 32.0;
    public static final double SHELL_EXPLOSION_HEIGHT = 32.0;

    private static final String BIG_EXPLOSION_FILE_PREFIX = "big-explosion-";
    private static final String BIG_EXPLOSION_FILE_SUFFIX = ".png";

    public static final AnimationResource BIG_EXPLOSION_ANIMATION =
            new AnimationResource(BIG_EXPLOSION_FILE_PREFIX, BIG_EXPLOSION_FILE_SUFFIX, 7);
    public static final int BIG_EXPLOSION_FRAME_DELAY = 4;
    public static final double BIG_EXPLOSION_WIDTH = 64.0;
    public static final double BIG_EXPLOSION_HEIGHT = 64.0;

    private final BufferedImage worldImage;
    private final Map<String, DrawableEntity> drawableEntitiesById = new HashMap<>();
    private final List<Animation> animations = new LinkedList<>();

    private int playerHP;
    private double playerSP;
    private int aiTank1HP;
    private int aiTank2HP;


    public RunGameView() {
        worldImage = new BufferedImage(SCREEN_DIMENSIONS.width, SCREEN_DIMENSIONS.height, BufferedImage.TYPE_INT_RGB);
        setBackground(Color.BLACK);
    }

    public void reset() {
        drawableEntitiesById.clear();
    }

    public void addDrawableEntity(
            String id, String entityImageFile, double initialX, double initialY, double initialAngle) {
        DrawableEntity drawableEntity = new DrawableEntity(entityImageFile);
        drawableEntity.setLocationAndAngle(initialX, initialY, initialAngle);
        drawableEntitiesById.put(id, drawableEntity);
    }

    /**
     * Removes the DrawableEntity identified by the provided ID from this view. This should be invoked if an entity has
     * been removed from the game and should no longer be drawn.
     */
    public void removeDrawableEntity(String id) {
        drawableEntitiesById.remove(id);
    }

    public void setDrawableEntityLocationAndAngle(String id, double x, double y, double angle) {
        drawableEntitiesById.get(id).setLocationAndAngle(x, y, angle);
    }

    public void addAnimation(AnimationResource animationResource, int frameDelay, double x, double y) {
        animations.add(new Animation(animationResource, frameDelay, x, y));
    }

    public void showHP(Tank playerTank, Tank aiTank1, Tank aiTank2){
        playerHP = playerTank.getHealthPoint();
        playerSP = playerTank.getSpeed();
        aiTank1HP = aiTank1.getHealthPoint();
        aiTank2HP = aiTank2.getHealthPoint();
    }

    @Override
    public void paintComponent(Graphics g) {
        Graphics2D buffer = worldImage.createGraphics();
        buffer.setColor(Color.BLACK);
        buffer.fillRect(0, 0, SCREEN_DIMENSIONS.width, SCREEN_DIMENSIONS.height);

        /////////////////////////////////////////////////////////////////////////
        Map<String, DrawableEntity> newDrawableEntitiesById = new HashMap<>();
        Boolean needTry = true;
        while (needTry) {
            // copy the entities to avoid concurrent modification exception
            try {
                newDrawableEntitiesById = new HashMap<>();
                newDrawableEntitiesById.putAll(drawableEntitiesById);
            } catch (ConcurrentModificationException e) {
                continue;
            }
            needTry = false;
        }
        /////////////////////////////////////////////////////////////////////////

        for (DrawableEntity drawableEntity: newDrawableEntitiesById.values()) {
            buffer.drawImage(drawableEntity.getEntityImage(), drawableEntity.getAffineTransform(), null);
        }

        ListIterator<Animation> animationIterator = animations.listIterator();
        while (animationIterator.hasNext()) {
            Animation animation = animationIterator.next();
            Optional<BufferedImage> nextImage = animation.getImage();
            if (nextImage.isPresent()) {
                buffer.drawImage(nextImage.get(), (int) animation.getX(), (int) animation.getY(), null);
            } else {
                animationIterator.remove();
            }
        }

        g.drawImage(worldImage, 0, 0, null);

//        g.setFont(new Font("TimesRoman", Font.BOLD, 15));
        g.setColor(Color.GREEN);
        g.drawString("PLAYER TANK HP: " + playerHP, 50, 700 );
        g.drawString("PLAYER TANK SPEED: " + playerSP, 50, 730 );

        g.setColor(Color.GREEN);
        g.drawString("AI TANK_1 HP: " + aiTank1HP, 800, 700 );

        g.setColor(Color.GREEN);
        g.drawString("AI TANK_2 HP: " + aiTank2HP, 800, 730 );
    }
}