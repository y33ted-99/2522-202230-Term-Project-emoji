package ca.bcit.comp2522.termproject.emoji;

import javafx.animation.FadeTransition;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.geometry.Bounds;
import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a TextBubble that contains an emoji and it's associated phrase.
 *
 * @author Terence Grigoruk
 * @author Brian Mak
 * @version Fall 2022
 */
public class TextBubble extends Group {

    /**
     * Height of text bubble.
     */
    public static final int TEXT_BUBBLE_HEIGHT = 110;
    /**
     * Size of font used in text bubble phrase.
     */
    public static final int FONT_SIZE = 28;
    private static final int SHOOT_RATE = 300;
    private static final double[] SPEED_RANGE = {0.5, 2};
    private final Side side;
    private final int position;
    private final EmojiType type;
    private ImageView textBubbleImageView;
    private final Entity emoji;
    private Text phrase;
    private Rectangle overlay;
    private int textBubbleWidth;
    private LetterGroup letterGroup;
    private Thread shotLettersThread;
    private boolean isPoppable;
    private boolean isAlive;

    /**
     * Creates an instance of type TextBubble.
     *
     * @param side     side of play area as Side
     * @param position position along a side as int
     * @param type     the type of emoji as EmojiType
     */
    public TextBubble(final Side side, final int position, final EmojiType type) {
        this.side = side;
        this.position = position;
        this.type = type;
        this.isAlive = true;
        textBubbleImageView = createTextBubbleImage();
        phrase = createPhrase();
        emoji = createEmoji();
        overlay = createOverlay();
        getChildren().addAll(textBubbleImageView, emoji, phrase, overlay);
        positionTextBubble();
        shoot();
    }

    /**
     * Returns the type of emoji.
     *
     * @return the type of emoji as EmojiType
     */
    public EmojiType getType() {
        return type;
    }

    /*
     * Creates the TextBubble image.
     */
    private ImageView createTextBubbleImage() {
        String textBubbleFilename = "text-bubble/" + side.getFilename();
        Image textBubbleImage = new Image(EmojiApp.class.getResource(textBubbleFilename).toExternalForm());
        textBubbleWidth = (int) (textBubbleImage.getWidth() * (TEXT_BUBBLE_HEIGHT / textBubbleImage.getHeight()));
        textBubbleImageView = new ImageView(textBubbleImage);
        textBubbleImageView.setFitHeight(TEXT_BUBBLE_HEIGHT);
        textBubbleImageView.setPreserveRatio(true);
        textBubbleImageView.setSmooth(true);
        textBubbleImageView.setCache(true);
        return textBubbleImageView;
    }

    /*
     * Positions the TextBubble according to side and position.
     */
    private void positionTextBubble() {
        switch (side) {
            case LEFT, RIGHT -> this.setTranslateY(position);
            default -> this.setTranslateX(position);
        }
    }

    /*
     * Creates the TextBubble's text based on the phrase associated with the emoji.
     */
    private Text createPhrase() {
        int margin = (TEXT_BUBBLE_HEIGHT / 2);
        phrase = new Text(margin - (FONT_SIZE / 2), margin + (FONT_SIZE / 2), type.getPhrase());
        Font font = new Font("Arial Black", FONT_SIZE);
        phrase.setFill(type.getColor());
        phrase.setFont(font);
        return phrase;
    }

    /*
     * Creates the emoji.
     */
    private Entity createEmoji() {
        Enemy enemy = new Enemy(type);
        int margin = (TEXT_BUBBLE_HEIGHT - Entity.IMAGE_SIZE) / 2;
        enemy.setTranslateX(textBubbleWidth - (margin * 2));
        enemy.setTranslateY(margin);
        return enemy;
    }

    /*
     * Creates the overlay used to indicate a text bubble is poppable.
     */
    private Rectangle createOverlay() {
        final double MARGIN = 20;
        Bounds tbb = textBubbleImageView.getBoundsInParent();
        Rectangle overlay = new Rectangle();
        overlay.setWidth(tbb.getWidth() - 2 * MARGIN);
        overlay.setHeight(tbb.getHeight() - 2 * MARGIN);
        overlay.setX(tbb.getMinX() + MARGIN);
        overlay.setY(tbb.getMinY() + MARGIN);
        overlay.setArcWidth(MARGIN);
        overlay.setArcHeight(MARGIN);
        overlay.setFill(new Color(1, 0, 0, 0.2));
        overlay.setOpacity(0);
        return overlay;
    }

    /**
     * Shoots a group of letters at the Player.
     */
    public void shoot() {
        final int margin = 10;
        // letter speed is random
        double speed = EmojiApp.RNG.nextDouble(SPEED_RANGE[0], SPEED_RANGE[1]);
        char[] charArray = type.getPhrase().toCharArray();

        int startX;
        if (side == Side.LEFT) {
            startX = PlayArea.getMarginX() + margin;
        } else {
            startX = PlayArea.getMarginX() + PlayArea.WIDTH - 2 * margin - 5;
        }
        Line path = new Line(startX,
                PlayArea.getMarginY() + position + (double) (TEXT_BUBBLE_HEIGHT / 2) + margin,
                EmojiApp.getPlayerBounds().getCenterX(),
                EmojiApp.getPlayerBounds().getCenterY());
        letterGroup = new LetterGroup(charArray, path, speed);
        shotLettersThread = new Thread(letterGroup);
        shotLettersThread.setDaemon(true);
        shotLettersThread.start();
    }

    private void showOverlay(final boolean show) {
        if (show) {
            overlay.setOpacity(1);
        } else {
            overlay.setOpacity(0);
        }
    }

    private boolean isPlayerAdjacent() {
        final double proximityRange = 15;
        Bounds player = EmojiApp.getPlayerBounds();
        double nearestY = getTranslateY()
                + PlayArea.getMarginY()
                + textBubbleImageView.getFitHeight() / 2;
        double nearestX = PlayArea.getMarginX() + (double) Player.IMAGE_SIZE / 2;
        if (side == Side.RIGHT) {
            nearestX = EmojiApp.APP_WIDTH - nearestX;
        }
        return Math.abs(player.getCenterX() - nearestX) < proximityRange
                && Math.abs(player.getCenterY() - nearestY) < textBubbleImageView.getFitHeight() / 2;
    }


    public void mouseClickHandler(final MouseEvent event) {
        if (isPoppable) {
            pop();
            letterGroup.die();
        }
    }

    /**
     * Pops the bubble and removes it (and its associated emoji and its letters) from game.
     */
    public void pop() {
        final Duration fadeOutDuration = Duration.millis(200);
        final Duration emojiDropDuration = Duration.millis(800);
        Timeline timeline = new Timeline();
        KeyValue keyValueTextBubbleImageViewOpacity = new KeyValue(textBubbleImageView.opacityProperty(), 0);
        KeyValue keyValuePhraseOpacity = new KeyValue(phrase.opacityProperty(), 0);
        getChildren().remove(overlay);
        KeyFrame keyFrame = new KeyFrame(fadeOutDuration,
                keyValueTextBubbleImageViewOpacity,
                keyValuePhraseOpacity);
        timeline.getKeyFrames().add(keyFrame);
        TranslateTransition emojiDrop = new TranslateTransition(emojiDropDuration, emoji);
        emojiDrop.setToY(EmojiApp.APP_HEIGHT);
        emojiDrop.setByX(-100);
        emojiDrop.setOnFinished(finish -> {
            isAlive = false;
        });
        emojiDrop.play();
        timeline.play();
    }

    /**
     * Returns true if text bubble has not been popped.
     *
     * @return true if is alive
     */
    public boolean isAlive() {
        return isAlive;
    }

    /**
     * Updates the group of letters.
     */
    public void update() {
        isPoppable = isPlayerAdjacent();
        showOverlay(isPoppable);
        letterGroup.update();
        if (!letterGroup.isAlive()) {
//            shoot();
        }
        if (!isAlive) {
            //
        }
    }

    /*
     * Represents a group of letters, i.e. a phrase shot out by a text bubble
     */
    private class LetterGroup implements Runnable {
        static final int INITIAL_PAUSE = 300;
        char[] letters;
        Line path;
        double speed;
        List<Letter> letterList;
        boolean isAlive;

        LetterGroup(final char[] letters, final Line path, final double speed) {
            this.letters = letters;
            this.path = path;
            this.speed = speed;
            this.letterList = new ArrayList<>();
        }

        /*
         * Runs the letter's animation (bouncing around the play area).
         */
        public void run() {
            try {
                Thread.sleep(INITIAL_PAUSE);
            } catch (InterruptedException exception) {
                exception.printStackTrace();
            }
            for (char chr : letters) {
                try {
                    Thread.sleep(SHOOT_RATE);
                } catch (InterruptedException exception) {
                    exception.printStackTrace();
                }
                Platform.runLater(() -> {
                    Letter letter = new Letter(
                            chr,
                            type.getColor(),
                            path,
                            speed);
                    letterList.add(letter);
                    getChildren().add(letter);
//                    Thread letterBouncer = new Thread(letter);
//                    letterBouncer.setDaemon(true);
//                    letterBouncer.start();
                });
            }
        }

        /**
         * Returns true if at least one shot letter is alive.
         *
         * @return true if at least one shot letter is alive
         */
        public boolean isAlive() {
            for (Letter letter : letterList) {
                if (letter.isAlive()) {
                    return true;
                }
            }
//            shotLettersThread.stop(); // needed??
            return false;
        }

        /**
         * Updates each letter.
         */
        public void update() {
            for (Letter letter : letterList) {
                letter.update();
            }
        }

        /**
         * Set each letter isAlive to false.
         */
        public void die() {
            for (Letter letter : letterList) {
                letter.setAlive(false);
            }
        }
    }
}
