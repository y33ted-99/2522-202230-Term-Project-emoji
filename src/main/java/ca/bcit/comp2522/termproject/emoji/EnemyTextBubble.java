package ca.bcit.comp2522.termproject.emoji;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Font;
import javafx.scene.text.Text;


public class EnemyTextBubble extends TextBubble {

    public EnemyTextBubble(final GameSide side, final int position, final EmojiType type) {
        super(side, position, type);
    }

    protected Entity createEmoji(final EmojiType type) {
        Entity enemy = new Enemy(type);
        enemy.setTranslateX(140);
        enemy.setTranslateY(10);
        return enemy;

    }
    protected Text createPhrase(final EmojiType type) {
        Text phrase = new Text(40,40, type.getPhrase());
        Font font = new Font("Arial Black",  28);
        phrase.setFont(font);
        return phrase;
    }

    protected void pop() {
        // when bubble pops emoji flies out in a fun animation
    }
}
