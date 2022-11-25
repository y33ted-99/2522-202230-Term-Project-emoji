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
 */
public class PlayArea {

    /**
     * Width of the play area.
     */
    public static  final  int WIDTH = 600;
    /**
     * Height of the play area.
     */
    public static final  int HEIGHT = 600;

    /**
     * The play area in which the player and letters exist.
     */
    private static final  Rectangle PLAY_AREA = new Rectangle();

    /**
     * Loads the background image.
     */
    public static ImageView createBackground() {
        ImageView backgroundImageView = null;
        try (InputStream is = Files.newInputStream(Paths.get("resources/bg/blue-pink-background.jpg"))) {
            backgroundImageView = new ImageView(new Image(is));
            backgroundImageView.setFitHeight(EmojiApp.APP_HEIGHT);
            backgroundImageView.setPreserveRatio(true);
            backgroundImageView.setCache(true);
            return backgroundImageView;
        } catch (IOException e) {
            System.out.println("Cannot load image");
        }
        return backgroundImageView;
    }

    /**
     * Creates the main play area where the action takes place.
     */
    public static Rectangle createPlayArea() {
        Rectangle playArea = new Rectangle();
        playArea.setX(getMarginX());
        playArea.setY(getMarginY());
        playArea.setWidth(WIDTH);
        playArea.setHeight(HEIGHT);
        playArea.setStroke(Color.BLACK);
        playArea.setFill(new Color(1, 1, 1, 0.8));
        playArea.setArcHeight(10);
        playArea.setArcWidth(10);
        return playArea;
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
