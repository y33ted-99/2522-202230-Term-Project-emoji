package ca.bcit.comp2522.termproject.emoji;

import javafx.scene.media.AudioClip;
import javafx.scene.paint.Color;

import java.net.URL;
import java.util.Objects;

/**
 * Represents a game item a.k.a. power-up
 *
 * @author Terence Grigoruk
 * @author Brian Mak
 * @version Fall 2022
 */
public class GameItem extends Entity {
    /**
     * The point value of a GameItem.
     */
    public static final int POINTS_PER_ITEM = 15;
    private static AudioClip itemSound;
    private final EmojiType itemType;
    private boolean isAlive;


    protected GameItem(final int xPosition, final int yPosition, final String image, final EmojiType type) {
        super(xPosition, yPosition, image);
        URL soundFile = EmojiApp.class.getResource("soundfx/item.aiff");
        itemSound = new AudioClip(Objects.requireNonNull(soundFile).toExternalForm());
        itemType = type;
        isAlive = true;
    }

    /**
     * Creates and returns an instance of type GameItem.
     *
     * @param type the game item type as
     * @return an instance of type GameItem
     */
    public static GameItem getInstance(final EmojiType type) {
        String filename = "item/" + type.getFilename();
        int posX = EmojiApp.RNG.nextInt(
                PlayArea.WIDTH - Entity.IMAGE_SIZE)
                + PlayArea.getMarginX();
        return new GameItem(posX, PlayArea.getMarginY(), filename, type);
    }

    /**
     * Returns the color associated with this game item.
     *
     * @return game item's color as Color
     */
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
            itemSound.play();
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
