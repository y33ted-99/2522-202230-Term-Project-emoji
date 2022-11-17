package ca.bcit.comp2522.termproject.emoji;

import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public abstract class TextBubbleFactory extends Group {
    private final ImageView textBubbleImage;
    private final ImageView emoji;
//    private final String phrase;

    protected enum SIDE {LEFT, RIGHT, TOP, BOTTOM}

    ;

    public TextBubbleFactory(final SIDE side, final int position, final String type) {

        textBubbleImage = loadTextBubbleImage(side);
        emoji = createEmoji(type);
//        phrase = createPhrase(type);

        getChildren().addAll(textBubbleImage);
        positionTextBubble(this, side, position);

    }

    private ImageView loadTextBubbleImage(final SIDE side) {
        String image =  switch (side) {
            case RIGHT -> "text-bubble-right.png";
            case LEFT -> "text-bubble-left.png";
            case TOP -> "text-bubble-top.png";
            case BOTTOM -> "text-bubble-bottom.png";
        };
        ImageView imageView = new ImageView(new Image(image));
        imageView.setFitWidth(100);
        imageView.setPreserveRatio(true);
        imageView.setSmooth(true);
        imageView.setCache(true);
        return imageView;
    }

    private void positionTextBubble(final Group textBubble, final SIDE side, final int position) {
        if (side == SIDE.LEFT) {
            textBubble.setTranslateX(20);
            textBubble.setTranslateY(100);
        }
    }

    protected abstract ImageView createEmoji(final String type);

    protected abstract String createPhrase(final String type);

    protected abstract void pop();
}
