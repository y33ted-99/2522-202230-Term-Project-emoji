package ca.bcit.comp2522.termproject.emoji;

import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * An abstract game entity.
 */
public abstract class Entity extends Group {

    /**
     * The default image size for an Entity's ImageView
     */
    public static final int IMAGE_SIZE = EmojiApp.EMOJI_SIZE;
    protected ImageView imageView;
    protected int size;

    public Entity() {
        this("enemy/no_mouth.png");
    }

    public Entity(final String image) {
        this(0, 0, image);
    }
    public Entity(final int xPosition, final int yPosition, final String image) {
        this(xPosition, yPosition, IMAGE_SIZE, image);
    }

    /**
     * Create an instance of type Entity.
     *
     * @param xPosition as int
     * @param yPosition as int
     * @param size as int
     * @param imageFilename a String representing the filename of the image loaded into the ImageView
     */
    public Entity(final int xPosition, final int yPosition, final int size, final String imageFilename) {
        this.size = size;
        try (InputStream is = Files.newInputStream(Path.of("resources/" + Paths.get(imageFilename)))) {
            imageView = new ImageView(new Image(is));
        } catch (IOException e) {
            System.out.println("Cannot load image: " + imageFilename);
        }
        imageView.setFitWidth(this.size);
        imageView.setPreserveRatio(true);
        imageView.setSmooth(true);
        imageView.setCache(true);
        this.getChildren().add(imageView);

        this.setTranslateX(xPosition);
        this.setTranslateY(yPosition);
    }

    /**
     * Returns the x coordinate of the center
     *
     * @return the x coordinate of the center as int
     */
    public int getCenterX() {
        return (int)(this.getTranslateX() + (size / 2));
    }
    /**
     * Returns the y coordinate of the center
     *
     * @return the y coordinate of the center as int
     */
    public int getCenterY() {
        return (int)(this.getTranslateY() + (size / 2));
    }
}
