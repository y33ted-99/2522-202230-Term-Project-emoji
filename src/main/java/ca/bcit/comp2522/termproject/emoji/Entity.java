package ca.bcit.comp2522.termproject.emoji;

import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 * An abstract game entity.
 */
public abstract class Entity extends Group {

    private static final int IMAGE_SIZE = EmojiApplication.EMOJI_SIZE;
    protected ImageView imageView;

    public Entity() {
        this("no_mouth");
    }

    public Entity(final String image) {
        this(0, 0, IMAGE_SIZE, image);
    }

    public Entity(int xPosition, int yPosition, int size, final String image) {

        imageView = new ImageView(new Image(image + ".png"));
        imageView.setFitWidth(size);
        imageView.setPreserveRatio(true);
        imageView.setSmooth(true);
        imageView.setCache(true);

        this.setTranslateX(xPosition);
        this.setTranslateY(yPosition);
    }
}
