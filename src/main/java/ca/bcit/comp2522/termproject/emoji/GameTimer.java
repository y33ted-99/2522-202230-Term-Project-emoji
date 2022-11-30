package ca.bcit.comp2522.termproject.emoji;

import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import java.util.concurrent.TimeUnit;

/**
 * Represents the player.
 *
 * @author Terence Grigoruk
 * @author Brian Mak
 * @version Fall 2022
 */
public class GameTimer extends StackPane {

    private final Text timeDisplay;
    /**
     * Creates an instance of type GameTimer.
     */
    public GameTimer() {
        timeDisplay = new Text("TIME: ");
        timeDisplay.setStroke(Color.BLACK);
        timeDisplay.setFont(Font.font(25));
        getChildren().add(timeDisplay);
        setLayoutX(600);
        setLayoutY(70);
    }

    /**
     * Updates the game timer with current time.
     *
     * @param now the current time in nanoseconds as a long
     */
    public void update(final long now) {
        long elapsedTime = TimeUnit.SECONDS.convert(
                now - EmojiApp.getStartTime(),
                TimeUnit.NANOSECONDS);
        timeDisplay.setText("TIME: " + elapsedTime);
    }
}
