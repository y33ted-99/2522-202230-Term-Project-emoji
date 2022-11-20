package ca.bcit.comp2522.termproject.emoji;

/**
 * Represents the player.
 *
 * @author Terence Grigoruk
 * @author Brian Mak
 * @version Fall 2022
 */
public class Player extends Entity{
    public static final int SPEED = 15;

    /**
     * Creates instance of type Player.
     * @param xPosition
     * @param yPosition
     */
    public Player(final int xPosition, final int yPosition) {
        super(xPosition, yPosition, "player/" + PlayerState.SMILEY.getFilename());
    }
}
