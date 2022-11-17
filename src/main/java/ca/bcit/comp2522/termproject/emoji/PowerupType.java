package ca.bcit.comp2522.termproject.emoji;

public enum PowerupType implements EmojiType{
    ANGRY("heart.png");

    private String filename;

    PowerupType(String filename) {
        this.filename = filename;
    }

    public String getFilename() {
        return filename;
    }
}
