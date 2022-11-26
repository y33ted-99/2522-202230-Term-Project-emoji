package ca.bcit.comp2522.termproject.emoji;

import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.scene.input.MouseEvent;

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
    private Point2D moveVector;
    private Point2D moveDestination;

    /**
     * Creates instance of type Player.
     *
     * @param xPosition an int
     * @param yPosition an int
     */
    public Player(final int xPosition, final int yPosition) {
        super(xPosition, yPosition, "player/" + PlayerState.SMILEY.getFilename());
        speed = INIT_SPEED;
        moveVector = new Point2D(0, 0);
    }

    public double getSpeed() {
        return speed;
    }

    public void setSpeed(final double speed) {
        this.speed = speed;
    }

    public void setMoveVector(Point2D moveVector) {
        this.moveVector = moveVector;
    }

    public void move() {
        double xMove = getTranslateX() - moveVector.getX();
        double yMove = getTranslateY() - moveVector.getY();
//        System.out.println(xMove);
        if (isValidMove(xMove, yMove)) {
            setTranslateX(xMove);
            setTranslateY(yMove);
        }
        if (speed > INIT_SPEED) {
            speed -= 0.02;
        }
    }

    /*
     * Checks if movement destination is within bounds and does not overlap with cursor.
     */
    private boolean isValidMove(final double xDestination, final double yDestination) {
        if (moveDestination == null) {
            return false;
        }
        Bounds playAreaBounds = PlayArea.getBounds();
        return !getBoundsInParent().contains(moveDestination)
                && xDestination > playAreaBounds.getMinX()
                && xDestination < playAreaBounds.getMaxX() - Player.IMAGE_SIZE
                && yDestination > playAreaBounds.getMinY()
                && yDestination < playAreaBounds.getMaxY() - Player.IMAGE_SIZE;
    }

    public void moveToMouse(final MouseEvent event) {
        moveVector = new Point2D(
                getBoundsInParent().getCenterX() - event.getSceneX(),
                getBoundsInParent().getCenterY() - event.getSceneY())
                .normalize()
                .multiply(speed);

        moveDestination = new Point2D(event.getSceneX(), event.getSceneY());
    }

    // TODO: mouseclick pops bubble / removes word if player in vicinity
}
