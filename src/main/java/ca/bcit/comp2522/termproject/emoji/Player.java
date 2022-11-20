package ca.bcit.comp2522.termproject.emoji;

import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

/**
 * Represents the player.
 *
 * @author Terence Grigoruk
 * @author Brian Mak
 * @version Fall 2022
 */
public class Player extends Entity{


    /**
     * Creates instance of type Player.
     * @param xPosition
     * @param yPosition
     */
    public Player(final int xPosition, final int yPosition) {
        super(xPosition, yPosition, "player/" + PlayerState.SMILEY.getFilename());
    }
}


