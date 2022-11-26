package ca.bcit.comp2522.termproject.emoji;

import javafx.application.Platform;
import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
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
    private static final int[] SPEED_RANGE = {3, 11};
    private final Side side;
    private final int position;
    private final EmojiType type;
    private ImageView textBubbleImageView;
    private final Entity emoji;
    private Text phrase;
    private int textBubbleWidth;

    /**
     * Creates an instance of type TextBubble.
     *
     * @param side     side of play area as Side
     * @param position position along side as int
     * @param type     the type of emoji as EmojiType
     */
    public TextBubble(final Side side, final int position, final EmojiType type) {
        this.side = side;
        this.position = position;
        this.type = type;
        textBubbleImageView = createTextBubbleImage();
        phrase = createPhrase();
        emoji = createEmoji();
        getChildren().addAll(textBubbleImageView, emoji, phrase);
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
        Path textBubbleFilename = Path.of("resources/text-bubble/" + side.getFilename());
        Image textBubbleImage;
        try (InputStream is = Files.newInputStream(textBubbleFilename)) {
            textBubbleImage = new Image(is);
            textBubbleWidth = (int) (textBubbleImage.getWidth() * (TEXT_BUBBLE_HEIGHT / textBubbleImage.getHeight()));
            textBubbleImageView = new ImageView(textBubbleImage);
        } catch (IOException e) {
            System.out.println("Cannot load image");
        }
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

    /**
     * Shoots a group of letters at the Player.
     */
    public void shoot() {
        // letter speed is random
        int speed = EmojiApp.RNG.nextInt(SPEED_RANGE[0], SPEED_RANGE[1]);
        char[] charArray = type.getPhrase().toCharArray();
        int xStart;
        if (side == Side.LEFT) {
            xStart = PlayArea.getMarginX() + 5;
        } else {
            xStart = PlayArea.getMarginX() + PlayArea.WIDTH - 25;
        }
        LetterGroup shotLetters = new LetterGroup(
                charArray,
                xStart,
                PlayArea.getMarginY() + position + (TEXT_BUBBLE_HEIGHT / 2) + 10,
                (int) EmojiApp.getPlayerBounds().getCenterX(),
                (int) EmojiApp.getPlayerBounds().getCenterY(),
                speed);
        Thread shotLettersThread = new Thread(shotLetters);
        shotLettersThread.setDaemon(true);
        shotLettersThread.start();
    }

    private class LetterGroup implements Runnable {
        char[] letters;
        int xStart;
        int yStart;
        int xTarget;
        int yTarget;
        int speed;
        List<Letter> letterList;
        boolean isAlive;

        LetterGroup(final char[] letters,
                           final int xStart,
                           final int yStart,
                           final int xTarget,
                           final int yTarget,
                           final int speed) {
            this.letters = letters;
            this.xStart = xStart;
            this.yStart = yStart;
            this.xTarget = xTarget;
            this.yTarget = yTarget;
            this.speed = speed;
            this.letterList = new ArrayList<>();
            this.isAlive = false;
        }

        /*
         * Runs the letter's animation (bouncing around the play area).
         */
        public void run() {
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
                            xStart,
                            yStart,
                            xTarget,
                            yTarget,
                            speed);
                    letterList.add(letter);
                    Thread letterBouncer = new Thread(letter);
                    letterBouncer.setDaemon(true);
                    letterBouncer.start();
                    isAlive = true;
                });
            }
        }

        /**
         * Returns true if at least one shot letter is alive.
         *
         * @return true if at least one shot letter is alive
         */
        public boolean isAlive() {
            for (Letter letter: letterList) {
                if (letter.isAlive()) {
                    return true;
                }
            }
            return false;
        }
    }

    /**
     * Pops the bubble and removes it (and its associated emoji and its letters) from game.
     */
    public void pop() {
        // when bubble pops emoji flies out in a fun animation
    }
}
