package ca.bcit.comp2522.termproject.emoji;

/**
 * An enemy emoji type.
 *
 * @author Terence Grigoruk
 * @author Brian Mak
 * @version Fall 2022
 */
public enum EnemyType implements EmojiType {
    ANGRY("angry.png", "OMFG"),
    DROOL("drooling_face.png", "DURR"),
    VOMIT("face_vomiting.png", "BARF"),
    SWEAR("face_with_symbols_on_mouth.png", "&$!#%"),
    GRIMACE("grimacing.png", "OOF"),
    HALO("innocent.png", "WWJD"),
    SICK("nauseated_face.png", "YUCK"),
    RAGE("rage.png", "GRRR"),
    ROFL("rofl.png", "ROFL"),
    SOB("sob.png", "WAAH"),
    THINK("thinking_face.png", "HMMM"),
    ;

    private String filename;
    private String phrase;

    /**
     * Create instance of enum EnemyType.
     *
     * @param filename a String
     * @param phrase a String
     */
    EnemyType(final String filename, final String phrase) {
        this.filename = filename;
        this.phrase = phrase;
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
}
