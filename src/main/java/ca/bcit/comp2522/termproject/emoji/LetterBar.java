package ca.bcit.comp2522.termproject.emoji;

import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

import java.util.ArrayList;
import java.util.List;

/**
 * A GUI element representing the letters the player hos collided with.
 */
public class LetterBar extends Group {
    private static final int HEIGHT = 50;
    private static final int CAPACITY = 20;
    private static final Rectangle container = new Rectangle();
    private static List<Text> letterArray = new ArrayList<>();
    private static Text[] lArr = new Text[CAPACITY];

    /**
     * Creates instance of type LetterBar.
     */
    public LetterBar() {
        createContainer();
        this.setTranslateX(PlayArea.getMarginX());
        this.setTranslateY(PlayArea.getMarginY() + PlayArea.HEIGHT + ((PlayArea.getMarginY() - container.getHeight()) / 2));

        // draw cells that Letters are placed in after capture by player
        for (int i = 0; i < CAPACITY; i++) {
            Text letter = new Text();
            letter.setText(String.valueOf(i % 10));
            letter.setFont(Font.font("Arial Black", FontWeight.BOLD, 29));
            letter.setTranslateX((i * 29) + 14);
            letter.setTranslateY(36);

            Rectangle cell = new Rectangle(29, 30);
            cell.setFill(Color.WHITE);
            cell.setStroke(Color.LIGHTGRAY);
            cell.setTranslateX((i * 29) + 9);
            cell.setTranslateY(10);

            this.getChildren().add(cell);
            this.getChildren().add(letter);
            letterArray.add(letter);
        }
    }


    /*
     * Creates the main play area where the action takes place.
     */
    private void createContainer() {
        container.setWidth(PlayArea.WIDTH);
        container.setHeight(HEIGHT);
        container.setStroke(Color.BLACK);
        container.setFill(new Color(1, 1, 1, 0.8));
        container.setArcHeight(10);
        container.setArcWidth(10);
        this.getChildren().add(container);
    }
}
// final int xPos, final int yPos