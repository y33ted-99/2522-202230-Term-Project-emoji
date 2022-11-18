package ca.bcit.comp2522.termproject.emoji;

public enum PlayerState {
    CRYING("cat_crying.png"),
    HEART_EYES("cat_heart-eyes.png"),
    JOY("cat_joy.png"),
    POUTING("cat_pouting.png"),
    SCREAM("cat_scream.png"),
    SMILEY("cat_smiley.png"),
    SMIRK("cat_smirk.png");

    private String filename;

    PlayerState(String filename) {
        this.filename = filename;
    }

    public String getFilename() {
        return filename;
    }
}