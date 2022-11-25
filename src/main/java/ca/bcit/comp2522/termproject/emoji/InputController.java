package ca.bcit.comp2522.termproject.emoji;

import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Rectangle;

/**
 * Handles input events.
 */
public class InputController {

    /**
     * Sets player's move vector based on mouse position.
     *
     * @param event a mouse event
     */
    public static void mouseMoveHandler(final MouseEvent event) {
        Point2D moveVector = new Point2D(
                EmojiApp.getPlayerBounds().getCenterX() - event.getSceneX(),
                EmojiApp.getPlayerBounds().getCenterY() - event.getSceneY())
                .normalize()
                .multiply(EmojiApp.getPlayerSpeed());
        EmojiApp.setPlayerMoveVector(moveVector);
        EmojiApp.setPlayerMousePosition(new Point2D(event.getSceneX(), event.getSceneY()));
    }

    /**
     * Pops bubble or removes word when player near respective item.
     */
    public void mouseClickHandler(final MouseEvent event) {
        // TODO
    }

}
