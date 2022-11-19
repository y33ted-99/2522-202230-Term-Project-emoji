package ca.bcit.comp2522.termproject.emoji;

/**
 * A player's state.
 *
 * @author Terence Grigoruk
 * @author Brian Mak
 * @version Fall 2022
 */
public enum PlayerState {
    CRYING("cat_crying.png"),
    HEART_EYES("cat_heart-eyes.png"),
    JOY("cat_joy.png"),
    POUTING("cat_pouting.png"),
    SCREAM("cat_scream.png"),
    SMILEY("cat_smiley.png"),
    SMIRK("cat_smirk.png");

    private String filename;

    /**
     * Create instance of PlayerState enum.
     *
     * @param filename a String
     */
    PlayerState(final String filename) {
        this.filename = filename;
    }

    /**
     * Returns the player state's filename.
     *
     * @return filename as String
     */
    public String getFilename() {
        return filename;
    }
}
