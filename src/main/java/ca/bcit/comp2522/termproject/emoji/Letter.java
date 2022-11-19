package ca.bcit.comp2522.termproject.emoji;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.util.Duration;

public class Letter extends Group implements Runnable {
    private final Text letter = new Text();
    private final int fontSize = 30;

    private int xVelocity = 4;
    private int yVelocity = 3;
    private int bounceCount;

    private final int[] LRTB = {
            EmojiApp.MARGIN_X,
            EmojiApp.MARGIN_X + EmojiApp.PLAY_AREA_SIZE - fontSize,
            EmojiApp.MARGIN_Y + fontSize,
            EmojiApp.MARGIN_Y + EmojiApp.PLAY_AREA_SIZE,};

    public Letter(final String character, final int xStart, final int yStart) {
        letter.setText(character);
        letter.setFont(Font.font("Arial Black", FontWeight.BOLD, fontSize));
        letter.setFill(Color.BLUE);
        letter.setX(xStart);
        letter.setY(yStart);
//        getChildren().add(letter);
        EmojiApp.root.getChildren().add(letter);
    }

    public void run() {
        while (true) {
            try {
                Thread.sleep(20); // sleep for 20 milliseconds
            } catch (InterruptedException exception) {
                exception.printStackTrace();
            }
            Platform.runLater(() -> {
                // check if collides with player
                if (isCollidePlayer()) {
                    captureLetter();
                    return;
                }
                // if bounce off left or right of Panel
                if (letter.getX() <= LRTB[0] || letter.getX() >= LRTB[1]) {
                    xVelocity *= -1; // reverses velocity in x direction
                    bounceCount++;
                }// if bounce off top or bottom of Panel
                if (letter.getY() <= LRTB[2] || letter.getY() >= LRTB[3]) {
                    yVelocity *= -1; // reverses velocity in y direction
                    bounceCount++;
                }
                letter.setX(letter.getX() + xVelocity); // determines new x-position
                letter.setY(letter.getY() + yVelocity); // determines new y-position
            });
        }
    }

    private boolean isCollidePlayer() {
        return (Math.abs(letter.getX() - EmojiApp.player.getTranslateX()) < 30
                && Math.abs(letter.getY() - EmojiApp.player.getTranslateY()) < 30);
    }

    /*
     * Do an animation for letter when it collides with player.
     */
    private void captureLetter() {
        Timeline timeline = new Timeline();
        KeyValue keyValueX = new KeyValue(letter.translateXProperty(), 0);
        KeyValue keyValueY = new KeyValue(letter.yProperty(), EmojiApp.APP_HEIGHT);
        Duration duration = Duration.millis(500);
        KeyFrame keyFrame = new KeyFrame(duration, keyValueX, keyValueY);
        timeline.getKeyFrames().add(keyFrame);
        timeline.play();
    }

}
