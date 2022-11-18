package ca.bcit.comp2522.termproject.emoji;

/**
 * An enemy emoji type.
 */
public enum EnemyType implements EmojiType {
    ANGRY("angry.png", "OMFG");

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
