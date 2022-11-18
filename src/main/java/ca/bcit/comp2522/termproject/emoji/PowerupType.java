package ca.bcit.comp2522.termproject.emoji;

public enum PowerupType implements EmojiType{
    PIZZA("pizza.png", "ZAAA");

    private String filename;
    private String phrase;

    PowerupType(String filename, String phrase) {
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
