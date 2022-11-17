package ca.bcit.comp2522.termproject.emoji;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Font;
import javafx.scene.text.Text;


public class EnemyTextBubble extends TextBubble {

    public EnemyTextBubble(final String side, final int position, final String type) {
        super(side, position, type);
    }

    protected ImageView createEmoji(final String type) {
        Image image = new Image(type + ".png");
        ImageView imageView = new ImageView(image);
        imageView.setFitWidth(EmojiApplication.EMOJI_SIZE);
        imageView.setPreserveRatio(true);
        imageView.setSmooth(true);
        imageView.setCache(true);
        imageView.setTranslateX(130);
        imageView.setTranslateY(10);
        return imageView;
    }
    protected Text createPhrase(final String type) {
        Text phrase = new Text(50,40, "WTF");
        Font font = new Font("Arial Black",  28);
        phrase.setFont(font);
        return phrase;
    }

    protected void pop() {
        // when bubble pops emoji flies out in a fun animation
    }
}
