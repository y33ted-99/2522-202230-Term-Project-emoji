package ca.bcit.comp2522.termproject.emoji;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

import java.util.Objects;

/**
 * Represents the player.
 *
 * @author Terence Grigoruk
 * @author Brian Mak
 * @version Fall 2022
 */
public class Player extends Entity {
    /**
     * The player's speed.
     */
    public static final double SPEED = 4;
    private static final int BUBBLES_TO_SPAWN_ITEM = 5;
    private static final MediaPlayer GAME_OVER_SOUND = new MediaPlayer(new Media(
            Objects.requireNonNull(EmojiApp.class.getResource("soundfx/gameover.mp3")).toExternalForm()));
    private Point2D moveVector;
    private Point2D moveDestination;
    private int score;
    private int poppedBubbles;

    /**
     * Creates instance of type Player in center of play area.
     */
    public Player() {
        super(EmojiApp.APP_WIDTH / 2 - IMAGE_SIZE / 2,
                EmojiApp.APP_HEIGHT / 2 - IMAGE_SIZE / 2,
                "player/" + PlayerState.SMILEY.getFilename());
        moveVector = new Point2D(0, 0);
    }

    /**
     * Moves the player.
     */
    public void move() {
        if (moveDestination == null || getBoundsInParent().contains(moveDestination)) {
            return;
        }
        double xMove = getTranslateX() - moveVector.getX();
        double yMove = getTranslateY() - moveVector.getY();
        if (isValidMoveX(xMove)) {
            setTranslateX(xMove);
        }
        if (isValidMoveY(yMove)) {
            setTranslateY(yMove);
        }
    }

    /*
     * Checks if movement destination is within bounds and does not overlap with cursor.
     */
    private boolean isValidMoveX(final double xDestination) {
        Bounds playAreaBounds = PlayArea.getBounds();
        return xDestination > playAreaBounds.getMinX()
                && xDestination < playAreaBounds.getMaxX() - Player.IMAGE_SIZE;
    }

    /*
     * Checks if movement destination is within bounds and does not overlap with cursor.
     */
    private boolean isValidMoveY(final double yDestination) {
        Bounds playAreaBounds = PlayArea.getBounds();
        return yDestination > playAreaBounds.getMinY()
                && yDestination < playAreaBounds.getMaxY() - Player.IMAGE_SIZE;
    }


    public void moveToMouse(final MouseEvent event) {
        moveVector = new Point2D(
                getBoundsInParent().getCenterX() - event.getSceneX(),
                getBoundsInParent().getCenterY() - event.getSceneY())
                .normalize()
                .multiply(SPEED);

        moveDestination = new Point2D(event.getSceneX(), event.getSceneY());
    }

    /**
     * Animates the player death.
     */
    public void die() {
        // change the image to the scream face
        String filename = "player/" + PlayerState.SCREAM.getFilename();
        image = new Image(Objects.requireNonNull(EmojiApp.class.getResource(filename)).toExternalForm());
        imageView.setImage(image);
        // animate Y value (falls to bottom) and rotation
        Timeline timeline = new Timeline();
        KeyValue keyValueY = new KeyValue(imageView.yProperty(), EmojiApp.APP_HEIGHT + imageView.getFitHeight());
        KeyValue keyValueR = new KeyValue(imageView.rotateProperty(), 180);
        Duration duration = Duration.millis(8000);
        KeyFrame keyFrame = new KeyFrame(duration, keyValueY, keyValueR);
        timeline.getKeyFrames().add(keyFrame);
        timeline.play();
        GAME_OVER_SOUND.play();
    }

    /**
     * Returns the player's score.
     *
     * @return score as int
     */
    public int getScore() {
        return score;
    }

    /**
     * Adds points to the player's score.
     *
     * @param pointsToAdd an int
     */
    public void addToScore(final int pointsToAdd) {
        this.score += pointsToAdd;
    }

    /**
     * Increments the number of bubbles player has popped.
     */
    public void incrementPoppedBubbles() {
        poppedBubbles++;
        if (poppedBubbles % BUBBLES_TO_SPAWN_ITEM == 0) {
            EmojiApp.spawnItem();
        }
    }
}
