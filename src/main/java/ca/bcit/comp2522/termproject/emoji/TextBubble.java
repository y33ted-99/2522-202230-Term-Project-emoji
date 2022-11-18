package ca.bcit.comp2522.termproject.emoji;

import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;

public abstract class TextBubble extends Group {

    public static final int TEXT_BUBBLE_HEIGHT = 60;
    private final ImageView textBubbleImage;
    private final Entity emoji;
    private final Text phrase;


//    protected enum SIDE {LEFT, RIGHT, TOP, BOTTOM}
//

    public TextBubble(final String side, final int position, final EmojiType type) {

        textBubbleImage = loadTextBubbleImage(side);
        emoji = createEmoji(type);
        phrase = createPhrase(type);

        getChildren().addAll(textBubbleImage, emoji, phrase);
        positionTextBubble(this, side, position);
    }

    private ImageView loadTextBubbleImage(final String side) {
        String image =  "text-bubble-" + side + ".png";
        ImageView imageView = new ImageView(new Image(image));
        imageView.setFitHeight(TEXT_BUBBLE_HEIGHT);
        imageView.setPreserveRatio(true);
        imageView.setSmooth(true);
        imageView.setCache(true);
        return imageView;
    }

    private void positionTextBubble(final Group textBubble, final String side, final int position) {
        switch (side) {
            case "left" -> {
                textBubble.setTranslateX(5);
                textBubble.setTranslateY(position);
            }
            case "right" -> {
                textBubble.setTranslateX(700);
                textBubble.setTranslateY(position);
            }
            case "top" -> {
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
