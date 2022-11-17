package ca.bcit.comp2522.termproject.emoji;

import javafx.scene.Group;
import javafx.scene.image.Image;

/**
 * An abstract game entity.
 */
public abstract class Entity extends Group {

    private int xPosition;
    private int yPosition;
    private int width;
    private int height;
    protected Image image;

    public Entity() {
        this.xPosition = EmojiApplication.APP_WIDTH / 2;
        this.yPosition = EmojiApplication.APP_HEIGHT / 2;
        this.width = EmojiApplication.EMOJI_SIZE;
        this.height = EmojiApplication.EMOJI_SIZE;
    }

    public Entity(int xPosition, int yPosition, int width, int height) {
        this.xPosition = xPosition;
        this.yPosition = yPosition;
        this.width = width;
        this.height = height;
    }
}
