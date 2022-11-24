package ca.bcit.comp2522.termproject.emoji;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.geometry.Bounds;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextBoundsType;
import javafx.util.Duration;

/**
 * Represents a letter that is shot out by an enemy at the player.
 */
public class Letter extends Group implements Runnable {
    private static final int FONT_SIZE = 30;

    private final Text letter = new Text();

    private int speed;
    private int xVelocity;
    private int yVelocity;
    private float opacity = 1;
    private int bounceCount;
    private final int maxBounces = 10;
    private boolean hasEnteredPlayArea = false;
    private boolean isAlive = true;

    // Play area borders (left, right, top, bottom)
    private final int[] LRTB = {
            EmojiApp.MARGIN_X,
            EmojiApp.MARGIN_X + EmojiApp.PLAY_AREA_WIDTH,
            EmojiApp.MARGIN_Y,
            EmojiApp.MARGIN_Y + EmojiApp.PLAY_AREA_HEIGHT,};

    /**
     * Creates an instance of a type Letter.
     *
     * @param character a String
     * @param color     letter color as Color
     * @param xStart    initial x position as int
     * @param yStart    initial y position as int
     * @param xTarget   target x position as int
     * @param yTarget   target y position as int
     * @param speed     an int
     */
    public Letter(final char character, final Color color,
                  final int xStart, final int yStart,
                  final int xTarget, final int yTarget,
                  final int speed) {
        this.speed = speed;
        letter.setText(String.valueOf(character));
        letter.setFont(Font.font("Arial Black", FontWeight.BOLD, FONT_SIZE));
        letter.setFill(color);
        letter.setBoundsType(TextBoundsType.VISUAL);
        letter.setX(xStart);
        letter.setY(yStart);
        setDirection(xStart, yStart, xTarget, yTarget);
        EmojiApp.root.getChildren().add(letter);
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
    private void setDirection(final int xStart, final int yStart, final int xTarget, final int yTarget) {
        double xDiff = xTarget - xStart;
        double yDiff = yTarget - yStart;
        double hyp = Math.sqrt(Math.pow(xDiff, 2) + Math.pow(yDiff, 2));
        xVelocity = (int) (speed * (xDiff / hyp));
        yVelocity = (int) (speed * (yDiff / hyp));
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
                if (detectCollisionWithPlayer() || bounceCount > maxBounces) {
                    removeLetter();
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
            if (letterBounds.getMinX() <= LRTB[0] || letterBounds.getMaxX() >= LRTB[1]) {
                xVelocity *= -1; // reverses velocity in x direction
                hasBounced = true;
            }
            // if bounce off top or bottom of Panel
            if (letterBounds.getMinY() <= LRTB[2] || letterBounds.getMaxY() >= LRTB[3]) {
                yVelocity *= -1; // reverses velocity in y direction
                hasBounced = true;
            }
            if (hasBounced) {
                bounceCount++;
                opacity -= (1 / (float) (maxBounces - 1));
                letter.setOpacity(opacity);
            }
        }
    }

    private void checkIfEnteredPlayArea() {
        if (EmojiApp.PLAY_AREA.getBoundsInParent().contains(letter.getBoundsInParent())) {
            hasEnteredPlayArea = true;
        }
    }

    /*
     * Returns true if the letter has collided with the player.
     */
    private boolean detectCollisionWithPlayer() {
        if (letter.getBoundsInParent().intersects(EmojiApp.player.getBoundsInParent())) {
            isAlive = false;
            return true;
        }
        return false;
    }

    /*
     * Remove letter from play area.
     */
    private void removeLetter() {
        Timeline timeline = new Timeline();
        KeyValue keyValueX = new KeyValue(letter.translateXProperty(), 0);
        KeyValue keyValueY = new KeyValue(letter.yProperty(), EmojiApp.APP_HEIGHT);
        KeyValue keyValueR = new KeyValue(letter.rotateProperty(), 720);

        Duration duration = Duration.millis(300);
        KeyFrame keyFrame = new KeyFrame(duration, keyValueX, keyValueY, keyValueR);
        timeline.getKeyFrames().add(keyFrame);
        timeline.play();
    }

}
