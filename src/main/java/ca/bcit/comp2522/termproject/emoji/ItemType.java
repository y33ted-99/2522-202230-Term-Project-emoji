package ca.bcit.comp2522.termproject.emoji;

import javafx.scene.paint.Color;

/**
 * A Power-up emoji type.
 *
 * @author Terence Grigoruk
 * @author Brian Mak
 * @version Fall 2022
 */
public enum ItemType implements EmojiType {
    PIZZA("pizza.png", "ZAAA", Color.RED);

    private String filename;
    private String phrase;
    private Color color;

    /**
     * Create instance of enum PowerUp.
     *
     * @param filename a String
     * @param phrase   a String
     * @param color    a Color
     */
    ItemType(final String filename, final String phrase, final Color color) {
        this.filename = filename;
        this.phrase = phrase;
        this.color = color;
    }

    /**
     * Returns the emoji's filename.
     *
     * @return filename as String
     */
    public String getFilename() {
        return filename;
    }

    /**
     * Returns the emoji's phrase.
     *
     * @return phrase as String
     */
    public String getPhrase() {
        return phrase;
    }

    /**
     * Returns the item text color.
     *
     * @return color as Color
     */
    public Color getColor() {
        return color;
    }
}
