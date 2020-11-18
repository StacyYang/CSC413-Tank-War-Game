package edu.csc413.tankgame.view;

import javax.imageio.ImageIO;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

/** Represents an image that can be drawn on screen at a specific location and rotated at an angle. */
class DrawableEntity {
    private final BufferedImage entityImage;
    private final AffineTransform affineTransform;

    /**
     * Initializes the DrawableEntity from the given image file. The translation/rotation transform is set to a default
     * transformation where it does nothing.
     */
    DrawableEntity(String imageFile) {
        URL imageUrl = getClass().getClassLoader().getResource(imageFile);
        if (imageUrl == null) {
            throw new RuntimeException("Unable to find the create an image URL from: " + imageFile);
        }
        try {
            entityImage = ImageIO.read(imageUrl);
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }

        affineTransform = new AffineTransform();
    }

    BufferedImage getEntityImage() {
        return entityImage;
    }

    AffineTransform getAffineTransform() {
        return affineTransform;
    }

    /** Updates this DrawableEntity's translation/rotation transform to match the provided coordinates and angle. */
    void setLocationAndAngle(double x, double y, double angle) {
        affineTransform.setToTranslation(x, y);
        affineTransform.rotate(angle, entityImage.getWidth() / 2.0, entityImage.getHeight() / 2.0);
    }
}
