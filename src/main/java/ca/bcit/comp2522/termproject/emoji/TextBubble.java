package ca.bcit.comp2522.termproject.emoji;

import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;

public abstract class TextBubble extends Group {
    private final ImageView textBubbleImage;
    private final ImageView emoji;
    private final Text phrase;

//    protected enum SIDE {LEFT, RIGHT, TOP, BOTTOM}
//

    public TextBubble(final String side, final int position, final String type) {

        textBubbleImage = loadTextBubbleImage(side);
        emoji = createEmoji(type);
        phrase = createPhrase(type);

        getChildren().addAll(textBubbleImage, emoji, phrase);
        positionTextBubble(this, side, position);
    }

    private ImageView loadTextBubbleImage(final String side) {
        String image =  "text-bubble-" + side + ".png";
        ImageView imageView = new ImageView(new Image(image));
        imageView.setFitHeight(58);
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

    protected abstract ImageView createEmoji(final String type);

    protected abstract Text createPhrase(final String type);

    protected abstract void pop();
}
