package ca.bcit.comp2522.termproject.emoji;

import javafx.scene.Group;

import java.util.Random;

/**
 * Represents a group of TextBubbles
 *
 * @author Terence Grigoruk
 * @author Brian Mak
 * @version Fall 2022
 */
public class TextBubbleGroup extends Group {
    private GameSide side;
    private TextBubble[] textBubbles;
    private int enemyCount;

    public TextBubbleGroup(GameSide side) {
        this.side = side;
        initTextBubbleArrays();
        positionGroup();
    }

    // TODO: expand setUpTextBubbleArrays() to accommodate all sides of game area
    /*
     * Set up an array of text bubbles.
     */
    private void initTextBubbleArrays() {
        int textBubblesPerSide = EmojiApp.PLAY_AREA_HEIGHT / TextBubble.TEXT_BUBBLE_HEIGHT;
        textBubbles = new TextBubble[textBubblesPerSide];
    }

    private void positionGroup(){
        switch (side){
            case LEFT -> this.setTranslateY(EmojiApp.MARGIN_Y);
            case RIGHT -> {
                this.setTranslateY(EmojiApp.MARGIN_Y);
                this.setTranslateX(EmojiApp.MARGIN_X + EmojiApp.PLAY_AREA_WIDTH - 20);
            }
            case TOP -> this.setTranslateX(EmojiApp.MARGIN_X);
            default -> {
                this.setTranslateX(EmojiApp.MARGIN_X);
                this.setTranslateY(EmojiApp.MARGIN_Y + EmojiApp.PLAY_AREA_HEIGHT);
            }
        }
    }
    /*
     * Randomly spawn a TextBubble containing an enemy
     */
    public void spawnEnemyTextBubble() {
        if (enemyCount >= textBubbles.length) {
            return;
        }
        // get a random index where a text bubble will be created
        int index;
        do {
            index = EmojiApp.RNG.nextInt(textBubbles.length);
        }
        while (textBubbles[index] != null);
        // get a random enemy type to spawn in text bubble
        EnemyType type;
        do {
            type = EnemyType.values()[new Random().nextInt(EnemyType.values().length)];
        } while (checkIfEmojiExists(type));

        int position = (TextBubble.TEXT_BUBBLE_HEIGHT * index);
        TextBubble textBubble = new TextBubble(side, position, type);
        enemyCount++;
        this.getChildren().add(textBubble);
        textBubbles[index] = textBubble;
    }

    private boolean checkIfEmojiExists(EmojiType emoji) {
        for (TextBubble tb : textBubbles) {
            if (tb == null) {
                continue;
            }
            if (tb.getType() == emoji) {
                return true;
            }
        }
        return false;
    }
}
