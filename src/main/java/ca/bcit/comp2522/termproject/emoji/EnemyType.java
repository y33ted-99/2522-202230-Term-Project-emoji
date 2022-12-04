package ca.bcit.comp2522.termproject.emoji;

import javafx.scene.paint.Color;

/**
 * An enemy emoji type.
 *
 * @author Terence Grigoruk
 * @author Brian Mak
 * @version Fall 2022
 */
public enum EnemyType implements EmojiType {
    ANGRY("angry.png", "OMFG", Color.RED),
    DROOL("drooling_face.png", "DURR", Color.BLUE),
    VOMIT("face_vomiting.png", "BLECH", Color.GREEN),
    SWEAR("face_with_symbols_on_mouth.png", "FRAK", Color.ORANGE),
    GRIMACE("grimacing.png", "OOF", Color.RED),
    HALO("innocent.png", "WWJD", Color.BLUE),
    SICK("nauseated_face.png", "EWW", Color.GREEN),
    RAGE("rage.png", "GRRR", Color.RED),
    ROFL("rofl.png", "LOL", Color.ORANGE),
    SOB("sob.png", "WAA", Color.BLUE),
    THINK("thinking_face.png", "HUH", Color.PURPLE),
    WOW("star-struck.png", "WOW", Color.YELLOW),
    DOH("confounded.png", "DOH", Color.PURPLE),
    YUM("yum.png", "YUM", Color.YELLOW),
    WAP("stuck_out_tongue_winking_eye.png", "YUM", Color.PINK),
    KISS("kissing_heart.png", "MWAH", Color.PINK),
    OHNO("dizzy_face.png", "OHNO", Color.RED),
    OOPS("face_with_hand_over_mouth.png", "OOPS", Color.ORANGE),
    LUVU("kissing_heart.png", "LUVU", Color.PINK),
    NERD("nerd_face.png", "LEET", Color.GREEN),
    AMA("hugging_face.png", "AMA", Color.PINK),
    FYI("smirk.png", "FYI", Color.PURPLE),
    WORRIED("worried.png", "DAE", Color.ORANGE),
    ZANY("zany_face.png", "YOLO", Color.YELLOW),
    MONACLE("face_with_monocle.png", "IMO", Color.PURPLE),
    FEARFUL("fearful.png", "EEK", Color.BLUE),
    ;

    private String filename;
    private String phrase;
    private Color color;

    /**
     * Create instance of enum EnemyType.
     *
     * @param filename a String
     * @param phrase   a String
     * @param color    a Color
     */
    EnemyType(final String filename, final String phrase, final Color color) {
        this.filename = filename;
        this.phrase = phrase;
        this.color = color;
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

    /**
     * Returns the emoji text color.
     *
     * @return color as Color
     */
    public Color getColor() {
        return color;
    }
}
