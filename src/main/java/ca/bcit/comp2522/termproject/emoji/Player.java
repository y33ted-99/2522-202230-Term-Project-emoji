package ca.bcit.comp2522.termproject.emoji;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.ParallelTransition;
import javafx.animation.PathTransition;
import javafx.animation.RotateTransition;
import javafx.animation.Timeline;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.util.Duration;

import java.nio.file.Path;

/**
 * Represents the player.
 *
 * @author Terence Grigoruk
 * @author Brian Mak
 * @version Fall 2022
 */
public class Player extends Entity {
    /**
     * The player's initial speed.
     */
    public static final double INIT_SPEED = 4;
    /**
     * The player's speed when pouncing.
     */
    public static final int POUNCE_SPEED = 8;

    private double speed;
    private Point2D moveVector;
    private Point2D moveDestination;
    private int score;

    /**
     * Creates instance of type Player.
     *
     * @param xPosition an int
     * @param yPosition an int
     */
    public Player(final int xPosition, final int yPosition) {
        super(xPosition, yPosition, "player/" + PlayerState.SMILEY.getFilename());
        speed = INIT_SPEED;
        moveVector = new Point2D(0, 0);
    }

    public double getSpeed() {
        return speed;
    }

    public void setSpeed(final double speed) {
        this.speed = speed;
    }

    public void setMoveVector(Point2D moveVector) {
        this.moveVector = moveVector;
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
        if (speed > INIT_SPEED) {
            speed -= 0.02;
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
                .multiply(speed);

        moveDestination = new Point2D(event.getSceneX(), event.getSceneY());
    }

    public void die() {

        image = new Image(EmojiApp.class.getResource("player/" + PlayerState.SCREAM.getFilename()).toExternalForm());
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
    public int getScore() {
        return score;
    }

    /**
     * Adds points to the player's score.
     *
     * @param points an int
     */
    public void addToScore(final int points) {
        score += points;
    }
}
