package ca.bcit.comp2522.termproject.emoji;

/**
 * Represents an Enemy emoji.
 *
 * @author Terence Grigoruk
 * @author Brian Mak
 * @version Fall 2022
 */
public class Enemy extends Entity {
    private EmojiType emoji;
    private int shootFromX;
    private int shootFromY;

    /**
     * Creates an instance of type Enemy.
     *
     * @param emoji the type of emoji as enum EmojiType
     */
    public Enemy(final EmojiType emoji) {
        super("enemy/" + emoji.getFilename());
        this.emoji = emoji;
    }

    /**
     * Sets the x position from which a letter is shot from.
     *
     * @param xPosition as int
     */
    public void setShootFromX(final int xPosition) {
        shootFromX = xPosition;
    }

    /**
     * Sets the y position from which a letter is shot from.
     *
     * @param yPosition as int
     */
    public void setShootFromY(final int yPosition) {
        shootFromY = yPosition;
    }

    /**
     * Shoots Letters at the Player.
     */
    public void shoot() {
        int speed = EmojiApp.RNG.nextInt(3, 9);
        Letter letter = new Letter(
                emoji.getPhrase().substring(0, 1),
                shootFromX,
                shootFromY,
                speed);
        Thread bouncer = new Thread(letter);
        bouncer.setDaemon(true);
        bouncer.start();
    }
}
