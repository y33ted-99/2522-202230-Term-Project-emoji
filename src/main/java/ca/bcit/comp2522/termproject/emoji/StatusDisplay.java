package ca.bcit.comp2522.termproject.emoji;

import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

import java.util.concurrent.TimeUnit;

/**
 * Represents the game status - points and time.
 *
 * @author Terence Grigoruk
 * @author Brian Mak
 * @version Fall 2022
 */
public class StatusDisplay extends Pane {
    private static final Font FONT = Font.font("Arial Black", FontWeight.BOLD, 32);
    private static final int SECONDS_PER_DIFFICULTY_LEVEL = 15;
    private final Text pointDisplay;
    private final Text timeDisplay;
    private long elapsedTime;

    /**
     * Creates an instance of type StatusDisplay.
     */
    public StatusDisplay() {
        pointDisplay = new Text("POINTS: 0");
        pointDisplay.setFont(FONT);
        pointDisplay.setFill(Color.WHITE);
        pointDisplay.setStroke(new Color(0,0,0,0.75));
        pointDisplay.setStrokeWidth(1.5);
        timeDisplay = new Text("TIME: 0");
        timeDisplay.setFont(FONT);
        timeDisplay.setFill(Color.WHITE);
        timeDisplay.setStroke(Color.BLACK);
        timeDisplay.setStrokeWidth(1.5);
        timeDisplay.setStroke(new Color(0,0,0,0.75));
        timeDisplay.setTranslateX(PlayArea.getMarginX() * 1.55);
        setLayoutX(PlayArea.getMarginX() * 1.3);
        setLayoutY(70);
        getChildren().addAll(pointDisplay, timeDisplay);
    }

    /**
     * Returns the elapsed time since timer started.
     *
     * @return elapsed time as long
     */
    public long getElapsedTime() {
        return elapsedTime;
    }

    /**
     * Updates the status display.
     *
     * @param now the current time in nanoseconds as a long
     */
    public void update(final long now) {
        pointDisplay.setText("POINTS: " + EmojiApp.getPlayerScore());
        elapsedTime = TimeUnit.SECONDS.convert(
                now - EmojiApp.getStartTime(),
                TimeUnit.NANOSECONDS);
        timeDisplay.setText("TIME: " + elapsedTime);
        EmojiApp.setDifficultyLevel((int) (elapsedTime / SECONDS_PER_DIFFICULTY_LEVEL));
    }
}
