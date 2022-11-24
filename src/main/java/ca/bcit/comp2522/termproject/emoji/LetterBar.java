package ca.bcit.comp2522.termproject.emoji;

import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

public class LetterBar extends Group {
    private static final Rectangle container = new Rectangle();
    private static final Text[] letterArray = new Text[20];

    public LetterBar() {
        createContainer();
        this.setTranslateX(EmojiApp.MARGIN_X);
        this.setTranslateY(EmojiApp.MARGIN_Y + EmojiApp.PLAY_AREA_HEIGHT + ((EmojiApp.MARGIN_Y - container.getHeight()) / 2));


        for (int i = 0; i < letterArray.length; i++) {
            Text letter = new Text();
            letter.setText(String.valueOf(i % 10));
            letter.setFont(Font.font("Arial Black", FontWeight.BOLD, 30));
            letter.setTranslateX((i * 29) + 13);
            letter.setTranslateY(37);
            this.getChildren().add(letter);
            letterArray[i] = letter;
        }
    }


    /*
     * Creates the main play area where the action takes place.
     */
    private void createContainer() {
//        container.setX(EmojiApp.MARGIN_X);
        container.setWidth(EmojiApp.PLAY_AREA_WIDTH);
        container.setHeight(50);
//        container.setY(EmojiApp.MARGIN_Y + EmojiApp.PLAY_AREA_HEIGHT + ((EmojiApp.MARGIN_Y - container.getHeight()) / 2));
        container.setStroke(Color.BLACK);
        container.setFill(new Color(1, 1, 1, 0.8));
        container.setArcHeight(10);
        container.setArcWidth(10);
        this.getChildren().add(container);
    }
}
// final int xPos, final int yPos