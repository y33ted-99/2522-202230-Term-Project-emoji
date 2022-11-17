package ca.bcit.comp2522.termproject.emoji;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;


public class EnemyTextBubbleFactory extends TextBubbleFactory {

    public EnemyTextBubbleFactory(SIDE side, int position, String type) {
        super(side, position, type);
    }

    protected ImageView createEmoji(final String type) {
        Image image = new Image(type + ".png");
        ImageView imageView = new ImageView(image);
        imageView.setFitWidth(EmojiApplication.EMOJI_SIZE);
        imageView.setPreserveRatio(true);
        imageView.setSmooth(true);
        imageView.setCache(true);
        return imageView;
    }
    protected String createPhrase(final String type) {
        return "HELLO";
    }

    protected void pop() {
        // when bubble pops emoji flies out in a fun animation
    }
}
