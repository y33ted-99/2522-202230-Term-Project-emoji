package ca.bcit.comp2522.termproject.emoji;

import javafx.scene.Group;
import javafx.scene.input.MouseEvent;

import java.util.Random;

/**
 * Represents a group of TextBubbles.
 *
 * @author Terence Grigoruk
 * @author Brian Mak
 * @version Fall 2022
 */
public class TextBubbleGroup extends Group {
    private final Side side;
    private TextBubble[] textBubbles;
    private int enemyCount;

    /**
     * Create instance of type TextBubbleGroup.
     *
     * @param side play area side as Side enum
     */
    public TextBubbleGroup(final Side side) {
        this.side = side;
        initTextBubbleArrays();
        positionGroup();
    }

    /*
     * Set up an array of text bubbles.
     */
    private void initTextBubbleArrays() {
        int textBubblesPerSide = PlayArea.HEIGHT / TextBubble.TEXT_BUBBLE_HEIGHT;
        textBubbles = new TextBubble[textBubblesPerSide];
    }

    /*
     * Positions the text bubble group according to side of play area.
     */
    private void positionGroup() {
        switch (side) {
            case LEFT -> this.setTranslateY(PlayArea.getMarginY());
            case RIGHT -> {
                this.setTranslateY(PlayArea.getMarginY());
                this.setTranslateX(PlayArea.getMarginX() + PlayArea.WIDTH - (Entity.IMAGE_SIZE / 2));
            }
            case TOP -> this.setTranslateX(PlayArea.getMarginX());
            default -> {
                this.setTranslateX(PlayArea.getMarginX());
                this.setTranslateY(PlayArea.getMarginY() + PlayArea.HEIGHT);
            }
        }
    }

    /**
     * Spawns a TextBubble.
     */
    public void spawnTextBubble() {
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
        EmojiType type;
        do {
            type = EnemyType.values()[new Random().nextInt(EnemyType.values().length)];
        } while (checkIfEmojiExists(type));

        int gapBetweenTextBubbles = (PlayArea.HEIGHT - (textBubbles.length * TextBubble.TEXT_BUBBLE_HEIGHT)) / textBubbles.length;
        int position = (TextBubble.TEXT_BUBBLE_HEIGHT + gapBetweenTextBubbles) * index;
        TextBubble textBubble = new TextBubble(side, position, type);
        getChildren().add(textBubble);
        textBubbles[index] = textBubble;
        enemyCount++;
    }

    /*
     * Returns true if an emoji of the given type exists in this text bubble group.
     */
    private boolean checkIfEmojiExists(final EmojiType emoji) {
        for (TextBubble textBubble : textBubbles) {
            if (textBubble != null && textBubble.getType() == emoji) {
                return true;
            }
        }
        return false;
    }

    /**
     * Updates all text bubbles in this group.
     */
    public void update() {
        for (TextBubble textBubble : textBubbles) {
            if (textBubble == null) {
                continue;
            } else if (!textBubble.isAlive()) {
                removeTextBubble(textBubble);
            } else {
                textBubble.update();
            }
        }
    }

    /*
     * Removes a text bubble from the game.
     */
    private void removeTextBubble(final TextBubble textBubble) {
        for (int i = 0; i < textBubbles.length; i++) {
            if (textBubbles[i] == textBubble) {
                textBubbles[i] = null;
                enemyCount--;
            }
        }
        getChildren().remove(textBubble);
    }

    /**
     * Passes a mouse click event to all text bubbles.
     */
    public void mouseClickHandler() {
        for (TextBubble textBubble : textBubbles) {
            if (textBubble != null) {
                textBubble.mouseClickHandler();
            }
        }
    }
}
