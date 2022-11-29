package ca.bcit.comp2522.termproject.emoji;

import javafx.animation.Interpolator;
import javafx.animation.TranslateTransition;
import javafx.application.Platform;

import javafx.scene.paint.Color;
import javafx.util.Duration;

/**
 * Represents a game item a.k.a. power-up
 *
 * @author Terence Grigoruk
 * @author Brian Mak
 * @version Fall 2022
 */
public class GameItem extends Entity {
    private static final int FALL_TIME = 5000;
    private EmojiType itemType;
    private boolean isAlive;


    protected GameItem(final int xPosition, final int yPosition, final String image, final EmojiType type) {
        super(xPosition, yPosition, image);
        itemType = type;
        isAlive = true;
//        fall();
    }

    /**
     * Creates instance of type GameItem.
     *
     * @param type the game item type as
     * @return
     */
    public static GameItem getInstance(final EmojiType type) {
        String filename = "item/" + type.getFilename();
        int posX = EmojiApp.RNG.nextInt(
                PlayArea.WIDTH - Entity.IMAGE_SIZE)
                + PlayArea.getMarginX();
        return new GameItem(posX, PlayArea.getMarginY(), filename, type);
    }

    public Color getColor() {
        return itemType.getColor();
    }


    /**
     * Returns true if alive.
     *
     * @return true if alive
     */
    public boolean isAlive() {
        return isAlive;
    }

    /**
     * Updates the game item.
     */
    public void update() {
        if (getBoundsInParent().intersects(EmojiApp.getPlayerBounds())) {
            LetterBar.removeLettersByColor(itemType.getColor());
            isAlive = false;
        }
        if (getTranslateY() < PlayArea.getMarginY() + PlayArea.HEIGHT - Entity.IMAGE_SIZE) {
            setTranslateY(getTranslateY() + 2);
        } else {
            isAlive = false;
        }
    }
}
