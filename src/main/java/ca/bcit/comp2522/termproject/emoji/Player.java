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
        Bounds playerBounds = getBoundsInParent();
        Bounds playAreaBounds = PlayArea.getBounds();
        Point2D mouse = new Point2D(moveDestination.getX(), moveDestination.getY());
        return !playerBounds.contains(mouse)
                && xDestination > playAreaBounds.getMinX()
                && xDestination < playAreaBounds.getMaxX() - Player.IMAGE_SIZE
                && yDestination > playAreaBounds.getMinY()
                && yDestination < playAreaBounds.getMaxY() - Player.IMAGE_SIZE;
    }

//    public void moveToMouse(final MouseEvent event) {
//        Point2D moveVector = new Point2D(
//                getBoundsInParent().getCenterX() - event.getSceneX(),
//                EmojiApp.getPlayerBounds().getCenterY() - event.getSceneY())
//                .normalize()
//                .multiply(EmojiApp.getPlayerSpeed());
//        EmojiApp.setPlayerMoveVector(moveVector);
//        EmojiApp.setPlayerMousePosition(new Point2D(event.getSceneX(), event.getSceneY()));
//    }
}
