package ca.bcit.comp2522.termproject.emoji;

import javafx.geometry.Bounds;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Represents components of the game play area.
 *
 * @author Terence Grigoruk
 * @author Brian Mak
 * @version Fall 2022
 */
public class PlayArea {

    /**
     * Width of the play area.
     */
    public static final int WIDTH = 600;
    /**
     * Height of the play area.
     */
    public static final int HEIGHT = 600;

    /**
     * The play area in which the player and letters exist.
     */
    private static final Rectangle PLAY_AREA = new Rectangle();

    /**
     * Loads the background image.
     */
    public static ImageView createBackground() {
        ImageView backgroundImageView = new ImageView(new Image(EmojiApp.class.getResource("bg/blue-pink-background.jpg").toExternalForm()));
        backgroundImageView.setFitHeight(EmojiApp.APP_HEIGHT);
        backgroundImageView.setPreserveRatio(true);
        backgroundImageView.setCache(true);
        return backgroundImageView;
    }

    /**
     * Creates the main play area where the action takes place.
     */
    public static Rectangle createPlayArea() {
        PLAY_AREA.setX(getMarginX());
        PLAY_AREA.setY(getMarginY());
        PLAY_AREA.setWidth(WIDTH);
        PLAY_AREA.setHeight(HEIGHT);
        PLAY_AREA.setStroke(Color.BLACK);
        PLAY_AREA.setFill(new Color(1, 1, 1, 0.8));
        PLAY_AREA.setArcHeight(10);
        PLAY_AREA.setArcWidth(10);
        return PLAY_AREA;
    }

    public static Bounds getBounds() {
        return PLAY_AREA.getBoundsInParent();
    }

    public static int getMarginX() {
        return (EmojiApp.APP_WIDTH - WIDTH) / 2;
    }

    public static int getMarginY() {
        return (EmojiApp.APP_HEIGHT - HEIGHT) / 2;
    }
}
