package ca.bcit.comp2522.termproject.emoji;

/**
 * Represents an Enemy emoji.
 *
 * @author Terence Grigoruk
 * @author Brian Mak
 * @version Fall 2022
 */
public class Enemy extends Entity {
    private final EmojiType emoji;
    /**
     * Creates an instance of type Enemy.
     *
     * @param emoji the type of emoji as enum EmojiType
     */
    public Enemy(final EmojiType emoji) {
        super("enemy/" + emoji.getFilename());
        this.emoji = emoji;
    }
}
