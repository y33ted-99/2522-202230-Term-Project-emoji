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

public class TextBubble extends Group {

    public static final int TEXT_BUBBLE_HEIGHT = 110;
    private int textBubbleWidth;
    private ImageView textBubbleImageView;
    private Entity emoji;
    private Text phrase;

    public TextBubble(final GameSide side, final int position, final EmojiType type) {

        textBubbleImageView = createTextBubbleImage(side);
        emoji = createEmoji(type);
        phrase = createPhrase(type);

        getChildren().addAll(textBubbleImageView, emoji, phrase);
        positionTextBubble(this, side, position);
    }

    private ImageView createTextBubbleImage(final GameSide side) {
        Path textBubbleFilename = Path.of("resources/text-bubble/" + side.getFilename());
        Image textBubbleImage;
        try (InputStream is = Files.newInputStream(textBubbleFilename)) {
            textBubbleImage = new Image(is);
            textBubbleWidth = (int)(textBubbleImage.getWidth() * (TEXT_BUBBLE_HEIGHT / textBubbleImage.getHeight() ));
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

    private void positionTextBubble(final Group textBubble, final GameSide side, final int position) {
        switch (side) {
            case LEFT -> {
                textBubble.setTranslateX(0);
                textBubble.setTranslateY(position);
            }
            case RIGHT -> {
                textBubble.setTranslateX(700);
                textBubble.setTranslateY(position);
            }
            case TOP -> {
                textBubble.setTranslateX(100 + position);
                textBubble.setTranslateY(100);
            }
            default -> {
                textBubble.setTranslateX(100 + position);
                textBubble.setTranslateY(600);
            }
        }

    }

    private Entity createEmoji(final EmojiType type) {
        Entity enemy = new Enemy(type);
        int margin = (TEXT_BUBBLE_HEIGHT - Entity.IMAGE_SIZE) / 2;
        System.out.println(textBubbleWidth);
        enemy.setTranslateX(textBubbleWidth - (margin * 2));
        enemy.setTranslateY(margin);
        return enemy;
    }

    private Text createPhrase(final EmojiType type) {
        int fontSize = 28;
        int margin = (TEXT_BUBBLE_HEIGHT / 2);
        Text phrase = new Text(margin - (fontSize / 2), margin + (fontSize / 2), type.getPhrase());
        Font font = new Font("Arial Black", fontSize);
        phrase.setFont(font);
        return phrase;
    }

    public void pop() {
        // when bubble pops emoji flies out in a fun animation
    }
}
