package ca.bcit.comp2522.termproject.emoji;

import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public abstract class TextBubble extends Group {

    public static final int TEXT_BUBBLE_HEIGHT = 60;
    private ImageView textBubbleImage;
    private Entity emoji;
    private Text phrase;


//    protected enum SIDE {LEFT, RIGHT, TOP, BOTTOM}
//

    public TextBubble(final GameSide side, final int position, final EmojiType type) {

        loadTextBubbleImage(side);
        emoji = createEmoji(type);
        phrase = createPhrase(type);

        getChildren().addAll(emoji, phrase);
        positionTextBubble(this, side, position);
    }

    private void loadTextBubbleImage(final GameSide side) {
        Path textBubbleFilename = Path.of("resources/text-bubble/" + side.getFilename());
        System.out.println(textBubbleFilename);
        try (InputStream is = Files.newInputStream(textBubbleFilename)) {
            textBubbleImage = new ImageView(new Image(is));
        } catch (IOException e) {
            System.out.println("Cannot load image");
        }
        textBubbleImage.setFitHeight(TEXT_BUBBLE_HEIGHT);
        textBubbleImage.setPreserveRatio(true);
        textBubbleImage.setSmooth(true);
        textBubbleImage.setCache(true);
        this.getChildren().add(textBubbleImage);
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

    protected abstract Entity createEmoji(final EmojiType type);

    protected abstract Text createPhrase(final EmojiType type);

    protected abstract void pop();
}
