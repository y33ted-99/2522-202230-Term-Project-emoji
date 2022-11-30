package ca.bcit.comp2522.termproject.emoji;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.geometry.Bounds;
import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.util.Duration;
import javafx.scene.media.AudioClip;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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
    private static final int SHOOT_RATE = 400;
    private static final double[] SPEED_RANGE = {0.5, 3};
    private static final int POINTS_PER_BUBBLE = 5;
    private static AudioClip popSound;
    private final Side side;
    private final int position;
    private final EmojiType type;
    private ImageView textBubbleImageView;
    private final Entity emoji;
    private Text phrase;
    private final Rectangle overlay;
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
        URL soundFile = EmojiApp.class.getResource("soundfx/pop.aiff");
        popSound = new AudioClip(Objects.requireNonNull(soundFile).toExternalForm());
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
        URL textBubbleFilename = EmojiApp.class.getResource("text-bubble/" + side.getFilename());
        Image textBubbleImage = new Image(Objects.requireNonNull(textBubbleFilename).toExternalForm());
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
        phrase.setStroke(type.getColor().darker());
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
        final double margin = 20;
        Bounds tbb = textBubbleImageView.getBoundsInParent();
        Rectangle newOverlay = new Rectangle();
        newOverlay.setWidth(tbb.getWidth() - 2 * margin);
        newOverlay.setHeight(tbb.getHeight() - 2 * margin);
        newOverlay.setX(tbb.getMinX() + margin);
        newOverlay.setY(tbb.getMinY() + margin);
        newOverlay.setArcWidth(margin);
        newOverlay.setArcHeight(margin);
        newOverlay.setFill(new Color(1, 0, 0, 0.2));
        newOverlay.setOpacity(0);
        return newOverlay;
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
            startX = PlayArea.getMarginX() + PlayArea.WIDTH - 2 * margin - 10;
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

    /**
     * Handles a mouse click event (pops a text bubble if it can be popped).
     */
    public void mouseClickHandler() {
        if (isPoppable) {
            popSound.play();
            pop();
            letterGroup.die();
            EmojiApp.incrementPlayerPoppedBubbles();
            EmojiApp.addToScore(POINTS_PER_BUBBLE);
        }
    }

    /**
     * Pops the bubble and removes it (and its associated emoji and its letters) from game.
     */
    public void pop() {
        final Duration fadeOutDuration = Duration.millis(200);
        final Duration emojiDropDuration = Duration.millis(800);
        // fade out the text bubble and phrase
        Timeline timeline = new Timeline();
        KeyValue keyValueTextBubbleImageViewOpacity = new KeyValue(textBubbleImageView.opacityProperty(), 0);
        KeyValue keyValuePhraseOpacity = new KeyValue(phrase.opacityProperty(), 0);
        getChildren().remove(overlay);
        KeyFrame keyFrame = new KeyFrame(fadeOutDuration,
                keyValueTextBubbleImageViewOpacity,
                keyValuePhraseOpacity);
        timeline.getKeyFrames().add(keyFrame);
        timeline.play();
        // drop emoji down out of screen
        TranslateTransition emojiDrop = new TranslateTransition(emojiDropDuration, emoji);
        emojiDrop.setToY(EmojiApp.APP_HEIGHT);
        emojiDrop.setByX(EmojiApp.RNG.nextInt(400) - 200);
        emojiDrop.setOnFinished(finish -> isAlive = false);
        emojiDrop.play();

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
        isPoppable = isPlayerAdjacent() && !EmojiApp.isGameOver();
        showOverlay(isPoppable);
        letterGroup.update();
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
                    // create a delay between letters being shot out
                    Thread.sleep((long) (SHOOT_RATE / speed));
                } catch (InterruptedException exception) {
                    exception.printStackTrace();
                }
                Platform.runLater(() -> {
                    Letter letter = new Letter(chr, type.getColor(), path, speed);
                    letterList.add(letter);
                    EmojiApp.addToRootScene(letter);
                });
            }
        }

        /**
         * Updates each letter.
         */
        public void update() {
            letterList.forEach(Letter::update);
            if (isAlive && letterList.size() > 0 && letterList.stream().allMatch(Letter::isCollided)) {
                //TODO: resolve bug when shooting after bubble popped
//                shoot();
            }
        }

        /**
         * Set each letter isAlive to false.
         */
        public void die() {
            letterList.forEach(letter -> letter.setAlive(false));
        }
    }
}
