package ca.bcit.comp2522.termproject.emoji;

/**
 * An enemy emoji type.
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

    EnemyType(String filename, String phrase) {
        this.filename = filename;
        this.phrase = phrase;
    }

    public String getFilename() {
        return filename;
    }
    public String getPhrase() {
        return phrase;
    }
}
