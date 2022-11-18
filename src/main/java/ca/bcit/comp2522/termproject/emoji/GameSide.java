package ca.bcit.comp2522.termproject.emoji;

/**
 * A side of the play area.
 */
public enum GameSide {
    LEFT("text-bubble-left.png"),
    RIGHT("text-bubble-right.png"),
    TOP("text-bubble-top.png"),
    BOTTOM("text-bubble-bottom.png");

    private String filename;

    GameSide(String filename) {
        this.filename = filename;
    }

    public String getFilename() {
        return filename;
    }
}
