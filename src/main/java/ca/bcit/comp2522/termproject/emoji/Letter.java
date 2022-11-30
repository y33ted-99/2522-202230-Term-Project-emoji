package ca.bcit.comp2522.termproject.emoji;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.geometry.Bounds;
import javafx.scene.Group;
import javafx.scene.media.AudioClip;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextBoundsType;
import javafx.util.Duration;

import java.net.URL;
import java.util.Objects;

/**
 * Represents a letter that is shot out by an enemy at the player.
 *
 * @author Terence Grigoruk
 * @author Brian Mak
 * @version Fall 2022
 */
public class Letter extends Group {
    private static final int FONT_SIZE = 25;
    private static AudioClip hitSound;
    private final Text letter = new Text();
    private final Color color;
    private double speed;
    private double deltaX;
    private double deltaY;
    private int bounceCount;
    private boolean hasEnteredPlayArea;
    private boolean isAlive;
    private boolean isCollided;

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
                  final double speed) {
        this.color = color;
        this.speed = speed;
        URL soundFile = EmojiApp.class.getResource("soundfx/hit.aiff");
        hitSound = new AudioClip(Objects.requireNonNull(soundFile).toExternalForm());
        letter.setText(String.valueOf(character));
        letter.setFont(Font.font("Arial Black", FontWeight.BOLD, FONT_SIZE));
        letter.setFill(color);
        letter.setStroke(color.darker());
        letter.setBoundsType(TextBoundsType.VISUAL);
        getChildren().add(letter);
        this.setTranslateX(path.getStartX());
        this.setTranslateY(path.getStartY());
        setDirection(path.getStartX(), path.getStartY(), path.getEndX(), path.getEndY());
        isAlive = true;
    }

    /**
     * Returns the letter's Text.
     *
     * @return letter as Text
     */
    public Text getText() {
        return letter;
    }

    /**
     * Returns the letters color.
     *
     * @return color as Color
     */
    public Color getColor() {
        return color;
    }

    /**
     * Returns true if letter is still alive.
     *
     * @return true if letter is still alive
     */
    public boolean isAlive() {
        return isAlive;
    }

    /**
     * Sets isAlive.
     *
     * @param alive as boolean
     */
    public void setAlive(boolean alive) {
        isAlive = alive;
    }

    /**
     * Returns true if letter has collided with player.
     *
     * @return true if letter has collided with player
     */
    public boolean isCollided() {
        return isCollided;
    }

    /*
     * Sets the letter's initial movement direction toward the player.
     */
    private void setDirection(final double startX, final double startY, final double endX, final double endY) {
        double xDiff = endX - startX;
        double yDiff = endY - startY;
        double hyp = Math.sqrt(Math.pow(xDiff, 2) + Math.pow(yDiff, 2));
        deltaX = speed * (xDiff / hyp);
        deltaY = speed * (yDiff / hyp);
    }

    private void detectCollisionWIthBorder() {
        checkIfEnteredPlayArea();
        boolean hasBounced = false;
        if (hasEnteredPlayArea) {
            Bounds letterBounds = getBoundsInParent();
            // if bounce off left or right of Panel
            if (letterBounds.getMinX() <= PlayArea.getMarginX()
                    || letterBounds.getMaxX() >= PlayArea.getMarginX() + PlayArea.WIDTH) {
                deltaX *= -1; // reverses velocity in x direction
                hasBounced = true;
            }
            // if bounce off top or bottom of Panel
            if (letterBounds.getMinY() <= PlayArea.getMarginY()
                    || letterBounds.getMaxY() >= PlayArea.getMarginY() + PlayArea.HEIGHT) {
                deltaY *= -1; // reverses velocity in y direction
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
        if (PlayArea.getBounds().contains(getBoundsInParent())) {
            hasEnteredPlayArea = true;
        }
    }

    /*
     * Returns true if the letter has collided with the player.
     */
    private boolean detectCollisionWithPlayer() {
        return getBoundsInParent().intersects(EmojiApp.getPlayerBounds());
    }

    /*
     * Move letter to player's letter bar.
     */
    private void moveLetterToLetterBar() {
        final double letterRotation = 720;
        final Duration duration = Duration.millis(300);
        Timeline timeline = new Timeline();
        KeyValue keyValueX = new KeyValue(translateXProperty(), LetterBar.getNextSlot().getX());
        KeyValue keyValueY = new KeyValue(translateYProperty(), LetterBar.getNextSlot().getY());
        KeyValue keyValueR = new KeyValue(letter.rotateProperty(), letterRotation);
        KeyFrame keyFrame = new KeyFrame(duration, keyValueX, keyValueY, keyValueR);
        timeline.getKeyFrames().add(keyFrame);
        timeline.play();
        LetterBar.addLetter(this);
    }

    /**
     * Updates this Letter (moves or kills it).
     */
    public void update() {
        final double bubbleDeathFadeDecrement = 0.03;
        final double gameOverFadeDecrement = 0.0018;
        if (isCollided) {
            return;
        }
        if (!isAlive) {
            setOpacity(getOpacity() - bubbleDeathFadeDecrement);
//            return;
        }
        if (EmojiApp.isGameOver()) {
            setOpacity(getOpacity() - gameOverFadeDecrement);
        } else if (detectCollisionWithPlayer()) {
            isCollided = true;
            hitSound.play();
            moveLetterToLetterBar();
            return;
        }
        detectCollisionWIthBorder();
        setTranslateX(getTranslateX() + deltaX);
        setTranslateY(getTranslateY() + deltaY);

    }
}
