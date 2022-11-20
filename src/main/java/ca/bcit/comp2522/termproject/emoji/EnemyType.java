package ca.bcit.comp2522.termproject.emoji;

import javafx.scene.paint.Color;

/**
 * An enemy emoji type.
 *
 * @author Terence Grigoruk
 * @author Brian Mak
 * @version Fall 2022
 */
public enum EnemyType implements EmojiType {
    ANGRY("angry.png", "OMFG", Color.INDIANRED),
    DROOL("drooling_face.png", "DURR", Color.LIGHTBLUE),
    VOMIT("face_vomiting.png", "BARF", Color.GREEN),
    SWEAR("face_with_symbols_on_mouth.png", "&$!#%", Color.ORANGE),
    GRIMACE("grimacing.png", "OOF", Color.DARKRED),
    HALO("innocent.png", "WWJD", Color.SKYBLUE),
    SICK("nauseated_face.png", "YUCK", Color.DARKGREEN),
    RAGE("rage.png", "GRRR", Color.RED),
    ROFL("rofl.png", "ROFL", Color.ORANGE),
    SOB("sob.png", "WAAH", Color.BLUE),
    THINK("thinking_face.png", "HMMM", Color.GREY),
    ;

    private String filename;
    private String phrase;
    private Color color;

    /**
     * Create instance of enum EnemyType.
     *
     * @param filename a String
     * @param phrase a String
     * @param color a Color
     */
    EnemyType(final String filename, final String phrase, final Color color) {
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
     * Returns the emoji text color.
     *
     * @return color as Color
     */
    public Color getColor() {
        return color;
    }
}
