package ca.bcit.comp2522.termproject.emoji;

/**
 * A side of the play area.
 * @author Terence Grigoruk
 * @author Brian Mak
 * @version Fall 2022
 */
public enum GameSide {
    LEFT("text-bubble-left.png"),
    RIGHT("text-bubble-right.png"),
    TOP("text-bubble-top.png"),
    BOTTOM("text-bubble-bottom.png");

    private String filename;

    /**
     * Create instance of enum GameSide.
     *
     * @param filename a String
     */
    GameSide(final String filename) {
        this.filename = filename;
    }

    /**
     * Returns the text-bubble's filename.
     *
     * @return filename as String
     */
    public String getFilename() {
        return filename;
    }
}
