package ca.bcit.comp2522.termproject.emoji;

import javafx.animation.Interpolator;
import javafx.animation.TranslateTransition;
import javafx.application.Platform;

import javafx.util.Duration;

/**
 * Represents a game item a.k.a. power-up
 */
public class GameItem extends Entity {
    private static final int FALL_TIME = 5000;
    private ItemType itemType;
    private boolean isAlive;


    protected GameItem(final int xPosition, final int yPosition, final String image) {
        super(xPosition, yPosition, image);
        isAlive = true;
        EmojiApp.addToRootScene(this);
        fall();
    }

    /**
     * Creates instance of type GameItem.
     *
     * @param type the game item type as
     * @return
     */
    public static GameItem getInstance(final EmojiType type) {
        String filename = "item/" + type.getFilename();
        int posX = EmojiApp.RNG.nextInt(
                PlayArea.WIDTH - Entity.IMAGE_SIZE)
                + PlayArea.getMarginX();
        return new GameItem(posX, PlayArea.getMarginY(), filename);
    }

    /**
     * Returns true if alive.
     *
     * @return true if alive
     */
    public boolean isAlive() {
        return isAlive;
    }

    public void fall() {
        TranslateTransition translateTransition = new TranslateTransition();
        translateTransition.setDuration(Duration.millis(FALL_TIME));
        translateTransition.setToY(PlayArea.getMarginY() + PlayArea.HEIGHT);
        translateTransition.setNode(this);
        translateTransition.setInterpolator(Interpolator.EASE_IN);
        translateTransition.setOnFinished(actionEvent -> {
            isAlive = false;
        });
        translateTransition.play();
    }

    public void update() {

    }
}
