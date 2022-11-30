package ca.bcit.comp2522.termproject.emoji;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.util.Duration;

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

    private static final int POINTS_PER_BUBBLE = 5;
    private static final int BUBBLES_TO_SPAWN_ITEM = 5;
    private Point2D moveVector;
    private Point2D moveDestination;
    private int points;
    private int poppedBubbles;

    /**
     * Creates instance of type Player.
     *
     * @param xPosition an int
     * @param yPosition an int
     */
    public Player(final int xPosition, final int yPosition) {
        super(xPosition, yPosition, "player/" + PlayerState.SMILEY.getFilename());
        moveVector = new Point2D(0, 0);
    }

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
        image = new Image(EmojiApp.class.getResource("player/" + PlayerState.SCREAM.getFilename()).toExternalForm());
        imageView.setImage(image);
        // animate Y value (falls to bottom) and rotation
        Timeline timeline = new Timeline();
        KeyValue keyValueY = new KeyValue(imageView.yProperty(), EmojiApp.APP_HEIGHT + imageView.getFitHeight());
        KeyValue keyValueR = new KeyValue(imageView.rotateProperty(), 180);
        Duration duration = Duration.millis(8000);
        KeyFrame keyFrame = new KeyFrame(duration, keyValueY, keyValueR);
        timeline.getKeyFrames().add(keyFrame);
        timeline.play();
    }

    /**
     * Returns the player's score.
     *
     * @return score as int
     */
    public int getPoints() {
        return points;
    }

    /**
     * Adds points to the player's score.
     *
     * @param pointsToAdd an int
     */
    public void addToScore(final int pointsToAdd) {
        this.points += pointsToAdd;
        System.out.println("points: " + points);
    }

    /**
     * Increments the number of bubbles player has popped.
     */
    public void incrementPoppedBubbles() {
        points += POINTS_PER_BUBBLE;
        poppedBubbles++;
        if (poppedBubbles % BUBBLES_TO_SPAWN_ITEM == 0) {
            EmojiApp.spawnItem();
        }
    }
}
