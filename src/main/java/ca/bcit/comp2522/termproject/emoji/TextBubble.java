package ca.bcit.comp2522.termproject.emoji;

import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;

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
        emoji = createEmoji();
        phrase = createPhrase();

        getChildren().addAll(textBubbleImageView, emoji, phrase);
        positionTextBubble();
    }

    /**
     * Returns the side of the play area where this TextBubble exists.
     *
     * @return the side of the play area where this TextBubble exists as Side
     */
    public Side getSide() {
        return side;
    }

    /**
     * Returns the position along the side of the play area where this TextBubble exists.
     *
     * @return the position along the side of the play area where this TextBubble exists as int
     */
    public int getPosition() {
        return position;
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
     * Creates the emoji.
     */
    private Entity createEmoji() {
        Enemy enemy = new Enemy(type);
        int margin = (TEXT_BUBBLE_HEIGHT - Entity.IMAGE_SIZE) / 2;
        enemy.setTranslateX(textBubbleWidth - (margin * 2));
        enemy.setTranslateY(margin);

        if (side == Side.LEFT) {
            enemy.setShootFromX(EmojiApp.MARGIN_X + 5);
        } else {
            enemy.setShootFromX(EmojiApp.MARGIN_X + EmojiApp.PLAY_AREA_WIDTH - 25);
        }
        enemy.setShootFromY(EmojiApp.MARGIN_Y + position + (TEXT_BUBBLE_HEIGHT / 2) + 10);
        enemy.setShootFromSide(side);
        enemy.shoot();
        return enemy;
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

    /**
     * Pops the bubble and removes it (and its associated emoji and its letters) from game.
     */
    public void pop() {
        // when bubble pops emoji flies out in a fun animation
    }
}
