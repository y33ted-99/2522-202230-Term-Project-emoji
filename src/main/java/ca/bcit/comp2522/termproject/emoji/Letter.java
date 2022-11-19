package ca.bcit.comp2522.termproject.emoji;

import com.almasb.fxgl.app.GameApplication;
import javafx.application.Platform;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

public class Letter extends Group implements Runnable{
    private final Text letter = new Text();
    private final int fontSize = 30;

    private int xVelocity = 4;
    private int yVelocity = 3;
    private int bounceCount;

    private final int[] LRTB = {
            EmojiApplication.MARGIN_X,
            EmojiApplication.MARGIN_X + EmojiApplication.PLAY_AREA_SIZE - fontSize,
            EmojiApplication.MARGIN_Y + fontSize,
            EmojiApplication.MARGIN_Y + EmojiApplication.PLAY_AREA_SIZE,};

    public Letter(final String character, final int xStart, final int yStart) {
        letter.setText(character);
        letter.setFont(Font.font("Arial Black", FontWeight.BOLD, fontSize));
        letter.setFill(Color.BLUE);
        letter.setX(xStart);
        letter.setY(yStart);
//        getChildren().add(letter);
        EmojiApplication.root.getChildren().add(letter);
    }

    public void run() {
        while (true) {
            try {
                Thread.sleep(20); // sleep for 20 milliseconds
            } catch (InterruptedException exception) {
                exception.printStackTrace();
            }
            Platform.runLater(() -> {
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
}
