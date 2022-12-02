package ca.bcit.comp2522.termproject.emoji;

import javafx.geometry.Bounds;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

/**
 * Represents components of the game play area.
 *
 * @author Terence Grigoruk
 * @author Brian Mak
 * @version Fall 2022
 */
public final class PlayArea {

    /**
     * Width of the play area.
     */
    public static final int WIDTH = 600;
    /**
     * Height of the play area.
     */
    public static final int HEIGHT = 600;

    private PlayArea() {
    }

    /**
     * The play area in which the player and letters exist.
     */
    private static final Rectangle PLAY_AREA = new Rectangle();

    /**
     * Loads the background image.
     *
     * @return background image as ImageView
     */
    public static ImageView createBackground() {
        Image backgroundImage = new Image(EmojiApp.class.getResource("bg/blue-pink-background.jpg").toExternalForm());
        ImageView backgroundImageView = new ImageView(backgroundImage);
        backgroundImageView.setFitHeight(EmojiApp.APP_HEIGHT);
        backgroundImageView.setPreserveRatio(true);
        backgroundImageView.setCache(true);
        return backgroundImageView;
    }

    /**
     * Creates the main play area where the action takes place.
     *
     * @return the play area as Rectangle
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

    /**
     * Returns the bounds of the play area.
     *
     * @return bounds of play area as Bounds
     */
    public static Bounds getBounds() {
        return PLAY_AREA.getBoundsInParent();
    }

    /**
     * Returns horizontal margin between play area and game window.
     *
     * @return horizontal margin as int
     */
    public static int getMarginX() {
        return (EmojiApp.APP_WIDTH - WIDTH) / 2;
    }

    /**
     * Returns vertical margin between play area and game window.
     *
     * @return vertical margin as int
     */
    public static int getMarginY() {
        return (EmojiApp.APP_HEIGHT - HEIGHT) / 2;
    }
}
