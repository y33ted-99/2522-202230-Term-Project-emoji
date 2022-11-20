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
    String getFilename();
    String getPhrase();
    Color getColor();
}
