package ca.bcit.comp2522.termproject.emoji;

public class Enemy extends Entity {
    public Enemy(EmojiType emoji) {
        super("enemy/" + emoji.getFilename());
    }

    /**
     * Shoots Letters at the player.
     */
    public void shoot() {
    }
}
