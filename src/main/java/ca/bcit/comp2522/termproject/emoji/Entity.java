package ca.bcit.comp2522.termproject.emoji;

import javafx.scene.image.Image;

/**
 * An abstract game entity.
 */
public abstract class Entity {

    private int xPosition;
    private int yPosition;
    private int width;
    private int height;
    private Image image;

    public Entity(int xPosition, int yPosition, int width, int height) {
        this.xPosition = xPosition;
        this.yPosition = yPosition;
        this.width = width;
        this.height = height;
    }
}
