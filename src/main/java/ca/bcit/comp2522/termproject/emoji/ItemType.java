package ca.bcit.comp2522.termproject.emoji;

/**
 * A Power-up emoji type.
 */
public enum ItemType implements EmojiType{
    PIZZA("pizza.png", "ZAAA");

    private String filename;
    private String phrase;

    /**
     * Create instance of enum PowerUp.
     *
     * @param filename a String
     * @param phrase a String
     */
    ItemType(String filename, String phrase) {
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
