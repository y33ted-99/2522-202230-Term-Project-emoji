package ca.bcit.comp2522.termproject.emoji;

import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
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
    private static final int CELL_GAP = 9;
    private static final int LETTER_MARGIN = 14;
    private static final Group letterBar = new Group();
    private static final Rectangle container = new Rectangle();
    private static List<Text> letters = new ArrayList<>();
    private static List<String> wordList = loadWordList();
//    private static List<String> wordList = new ArrayList<>();
//    static {
//        EnumSet.allOf(EnemyType.class).forEach(emoji -> wordList.add(emoji.getPhrase()));
//    }

    /*
     * Reads words from 'wordlist.txt' and adds to wordList
     */
    private static List<String> loadWordList() {
        URL url = EmojiApp.class.getResource("wordlist.txt");
        List<String> list = null;
        try {
            Path filepath = Paths.get(url.toURI());
            list = Files.readAllLines(filepath, StandardCharsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (URISyntaxException urise) {
            urise.printStackTrace();
        }
        return list;
    }

    /**
     * Returns a Group containing elements of a letter bar.
     */
    public static Group createLetterBar() {
        createContainer();
        drawCells();
        letterBar.setTranslateX(PlayArea.getMarginX());
        letterBar.setTranslateY(PlayArea.getMarginY()
                + PlayArea.HEIGHT
                + ((PlayArea.getMarginY()
                - container.getHeight()) / 2));
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

    /*
     * Draws cells that letters are placed in after capture by player.
     */
    private static void drawCells() {
        for (int i = 0; i < CAPACITY; i++) {
            Rectangle cell = new Rectangle(FONT_SIZE, FONT_SIZE + 1);
            cell.setFill(Color.WHITE);
            cell.setStroke(Color.LIGHTGRAY);
            cell.setTranslateX((i * FONT_SIZE) + CELL_GAP);
            cell.setTranslateY(10);
            letterBar.getChildren().add(cell);
        }
    }

    public static int getSize() {
        return letters.size();
    }

    public static Point2D getNextSlot() {
        return new Point2D(
                letterBar.getTranslateX() + (letters.size() * FONT_SIZE) + LETTER_MARGIN,
                letterBar.getTranslateY() + 36);
    }

    public static void addLetter(final Text letter) {
        letters.add(letter);
        int pointsGained = checkIfContainsWords();
        while (pointsGained > 0) {
            EmojiApp.addToScore(pointsGained);
            pointsGained = checkIfContainsWords();
        }
        if (letters.size() == CAPACITY) {
            EmojiApp.setGameOver(true);
        }
    }

    private static int checkIfContainsWords() {
        StringBuilder stringBuilder = new StringBuilder();
        for (Text letter : letters) {
            stringBuilder.append(letter.getText());
        }
        String letterBarString = stringBuilder.toString();
        int index = -1;
        int wordLength = 0;
        for (String word : wordList) {
            if (word.length() > letters.size()) {
                break;
            }
            if (letterBarString.contains(word)) {
                index = letterBarString.indexOf(word);
                wordLength = word.length();
                System.out.println("removing " + word + " from " + letterBarString);
            }
        }
        if (index >= 0) {
            removeWord(index, wordLength);
            return wordLength;
        }
        return 0;
    }

    private static void removeWord(final int index, final int length) {
        Text letter;
        // remove letters from arraylist
        for (int i = 0; i < length; i++) {
            letter = letters.remove(index);
            EmojiApp.removeFromRootScene(letter);
        }
        // shift letters on right side of removed word to the left
        for (int i = index; i < Math.min(index + length, letters.size()); i++) {
            letters.get(i).setTranslateX(letterBar.getTranslateX() + (i * FONT_SIZE) + LETTER_MARGIN);
        }
    }
}