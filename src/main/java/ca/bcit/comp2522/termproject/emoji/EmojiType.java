package ca.bcit.comp2522.termproject.emoji;

import javafx.scene.paint.Color;

/**
 * Interface for emoji type enumerations.
 *
 * @author Terence Grigoruk
 * @author Brian Mak
 * @version Fall 2022
 */
public interface EmojiType {
    /**
     * Returns a filename.
     * @return filename as String
     */
    String getFilename();

    /**
     * Returns a phrase.
     *
     * @return phrase as String
     */
    String getPhrase();

    /**
     * Returns a color.
     *
     * @return color as Color
     */
    Color getColor();
}
