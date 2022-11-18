package ca.bcit.comp2522.termproject.emoji;

/**
 * A side of the play area.
 */
public enum GameSide {
    LEFT("text-bubble-left.png"),
    RIGHT("text-bubble-left.png"),
    TOP("text-bubble-left.png"),
    BOTTOM("text-bubble-left.png");

    private String filename;

    GameSide(String filename) {
        this.filename = filename;
    }

    public String getFilename() {
        return filename;
    }
}
