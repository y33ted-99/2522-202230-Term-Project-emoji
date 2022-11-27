package ca.bcit.comp2522.termproject.emoji;

import javafx.geometry.Point2D;
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
 *
 * @author Terence Grigoruk
 * @author Brian Mak
 * @version Fall 2022
 */
public class LetterBar extends Group {
    private static final int HEIGHT = 50;
    private static final int CAPACITY = 20;
    private static final int FONT_SIZE = 29;
    private static final Group letterBar = new Group();
    private static final Rectangle container = new Rectangle();
    private static List<Text> letters = new ArrayList<>();
//    private static Text[] lArr = new Text[CAPACITY];

    /**
     * Creates instance of type LetterBar.
     */
    public static Group createLetterBar() {
        createContainer();
        letterBar.setTranslateX(PlayArea.getMarginX());
        letterBar.setTranslateY(PlayArea.getMarginY()
                + PlayArea.HEIGHT
                + ((PlayArea.getMarginY()
                - container.getHeight()) / 2));

        // draw cells that Letters are placed in after capture by player
        for (int i = 0; i < CAPACITY; i++) {
            Text letter = new Text();
            letter.setText(String.valueOf(i % 10));
            letter.setFont(Font.font("Arial Black", FontWeight.BOLD, FONT_SIZE));
            letter.setTranslateX((i * FONT_SIZE) + 14);
            letter.setTranslateY(36);

            Rectangle cell = new Rectangle(FONT_SIZE, FONT_SIZE + 1);
            cell.setFill(Color.WHITE);
            cell.setStroke(Color.LIGHTGRAY);
            cell.setTranslateX((i * FONT_SIZE) + 9);
            cell.setTranslateY(10);

            letterBar.getChildren().add(cell);
//            letterBar.getChildren().add(letter);
//            letters.add(letter);
        }
        return letterBar;
    }

    /*
     * Creates the container panel containing the row of letters.
     */
    private static void createContainer() {
        container.setWidth(PlayArea.WIDTH);
        container.setHeight(HEIGHT);
        container.setStroke(Color.BLACK);
        container.setFill(new Color(1, 1, 1, 0.8));
        container.setArcHeight(10);
        container.setArcWidth(10);
        letterBar.getChildren().add(container);
    }

    public static int getSize() {
        return letters.size();
    }

    public static Point2D getNextSlot() {
        return new Point2D(
                letterBar.getTranslateX() + (letters.size() * FONT_SIZE) + 14,
                letterBar.getTranslateY() + 36);
    }

    public static void addLetter(final Text letter) {
        letters.add(letter);
    }
}