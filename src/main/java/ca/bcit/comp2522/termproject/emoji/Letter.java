package ca.bcit.comp2522.termproject.emoji;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.geometry.Bounds;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextBoundsType;
import javafx.util.Duration;

/**
 * Represents a letter that is shot out by an enemy at the player.
 */
public class Letter extends Group implements Runnable {
    private static final int FONT_SIZE = 29;

    private final Text letter = new Text();

    private int speed;
    private double xVelocity;
    private double yVelocity;
    private int bounceCount;
    private boolean hasEnteredPlayArea;
    private boolean isAlive;

    /**
     * Creates an instance of a type Letter.
     *
     * @param character a String
     * @param color     letter color as Color
     * @param path      path the letter follows
     * @param speed     an int
     */
    public Letter(final char character,
                  final Color color,
                  final Line path,
                  final int speed) {
        this.speed = speed;
        letter.setText(String.valueOf(character));
        letter.setFont(Font.font("Arial Black", FontWeight.BOLD, FONT_SIZE));
        letter.setFill(color);
        letter.setStroke(color.darker());
        letter.setBoundsType(TextBoundsType.VISUAL);
        letter.setX(path.getStartX());
        letter.setY(path.getStartY());
        setDirection(path.getStartX(), path.getStartY(), path.getEndX(), path.getEndY());
        EmojiApp.addToScene(letter);
        isAlive = true;
    }

    /**
     * Returns true if letter is still in play.
     *
     * @return true if letter is still in play
     */
    public boolean isAlive() {
        return isAlive;
    }

    /*
     * Sets the letter's initial movement direction toward the player.
     */
    private void setDirection(final double startX, final double startY, final double endX, final double endY) {
        double xDiff = endX - startX;
        double yDiff = endY - startY;
        double hyp = Math.sqrt(Math.pow(xDiff, 2) + Math.pow(yDiff, 2));
        xVelocity = speed * (xDiff / hyp);
        yVelocity = speed * (yDiff / hyp);
    }

    /**
     * Runs the letter's animation (bouncing around the play area).
     */
    public void run() {
        while (isAlive) {
            try {
                Thread.sleep(20); // sleep for 20 milliseconds
            } catch (InterruptedException exception) {
                exception.printStackTrace();
            }
            Platform.runLater(() -> {
                // check if collides with player
                if (detectCollisionWithPlayer()) {
                    moveLetterToLetterBar();
                    return;
                }
                detectCollisionWIthBorder();
                letter.setX(letter.getX() + xVelocity); // determines new x-position
                letter.setY(letter.getY() + yVelocity); // determines new y-position
            });
        }
    }

    private void detectCollisionWIthBorder() {
        checkIfEnteredPlayArea();
        boolean hasBounced = false;
        if (hasEnteredPlayArea) {
            Bounds letterBounds = letter.getBoundsInParent();
            // if bounce off left or right of Panel
            if (letterBounds.getMinX() <= PlayArea.getMarginX()
                    || letterBounds.getMaxX() >= PlayArea.getMarginX() + PlayArea.WIDTH) {
                xVelocity *= -1; // reverses velocity in x direction
                hasBounced = true;
            }
            // if bounce off top or bottom of Panel
            if (letterBounds.getMinY() <= PlayArea.getMarginY()
                    || letterBounds.getMaxY() >= PlayArea.getMarginY() + PlayArea.HEIGHT) {
                yVelocity *= -1; // reverses velocity in y direction
                hasBounced = true;
            }
            if (hasBounced) {
                bounceCount++;
            }
        }
    }

    /*
     * Checks if letter has fully entered play area after being shot out.
     */
    private void checkIfEnteredPlayArea() {
        if (PlayArea.getBounds().contains(letter.getBoundsInParent())) {
            hasEnteredPlayArea = true;
        }
    }

    /*
     * Returns true if the letter has collided with the player.
     */
    private boolean detectCollisionWithPlayer() {
        if (letter.getBoundsInParent().intersects(EmojiApp.getPlayerBounds())) {
            isAlive = false;
            return true;
        }
        return false;
    }

    /*
     * Move letter to player's letter bar..
     */
    private void moveLetterToLetterBar() {
        Timeline timeline = new Timeline();
        KeyValue keyValueX = new KeyValue(letter.translateXProperty(), 0);
        KeyValue keyValueY = new KeyValue(letter.yProperty(), EmojiApp.APP_HEIGHT - 40);
        KeyValue keyValueR = new KeyValue(letter.rotateProperty(), 720);

        Duration duration = Duration.millis(300);
        KeyFrame keyFrame = new KeyFrame(duration, keyValueX, keyValueY, keyValueR);
        timeline.getKeyFrames().add(keyFrame);
        timeline.play();
    }

    public void update() {

    }
}
