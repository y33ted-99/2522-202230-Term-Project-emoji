package ca.bcit.comp2522.termproject.emoji;

/**
 * Represents the player.
 *
 * @author Terence Grigoruk
 * @author Brian Mak
 * @version Fall 2022
 */
public class Player extends Entity {
    /**
     * The player's initial speed.
     */
    public static final int INIT_SPEED = 2;
    /**
     * THe player's speed when pouncing.
     */
    public static final int POUNCE_SPEED = 8;

    private double speed;

    /**
     * Creates instance of type Player.
     *
     * @param xPosition an int
     * @param yPosition an int
     */
    public Player(final int xPosition, final int yPosition) {
        super(xPosition, yPosition, "player/" + PlayerState.SMILEY.getFilename());
        speed = INIT_SPEED;
    }

    public double getSpeed() {
        return speed;
    }

    public void setSpeed(final double speed) {
        this.speed = speed;
    }
}
