package ca.bcit.comp2522.termproject.emoji;

import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * A GUI element that manages the players letters.
 *
 * @author Terence Grigoruk
 * @author Brian Mak
 * @version Fall 2022
 */
public class LetterBar extends Group {
    private static final int HEIGHT = 50;
    private static final int CAPACITY = 20;
    private static final int FONT_SIZE = 29;
    private static final int CELL_MARGIN = 9;
    private static final int LETTER_MARGIN_X = 14;
    private static final int LETTER_MARGIN_Y = 35;
    private static final int POINTs_PER_LETTER_IN_SPELLED_WORD = 20;
    private static final Group letterBar = new Group();
    private static final Rectangle container = new Rectangle();
    private static List<Letter> lettersArrayList = new ArrayList<>();
    private static List<String> wordList = loadWordList();

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
     *
     * @return a Group containing elements of a letter bar
     */
    public static Group createLetterBar() {
        createContainer();
//        drawCells();
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
        container.setFill(new Color(1, 1, 1, 0.9));
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
            cell.setTranslateX((i * FONT_SIZE) + CELL_MARGIN);
            cell.setTranslateY(10);
            letterBar.getChildren().add(cell);
        }
    }

    /**
     * Returns the coordinates of the next available slot for a letter in the letter bar.
     *
     * @return the coordinate of the next slot position as Point2D
     */
    public static Point2D getNextSlot() {
        return new Point2D(
                letterBar.getTranslateX() + (lettersArrayList.size() * FONT_SIZE) + LETTER_MARGIN_X,
                letterBar.getTranslateY() + LETTER_MARGIN_Y);
    }

    /**
     * Adds a letter to the letter bar.
     *
     * @param letter the letter to add as Text
     */
    public static void addLetter(final Letter letter) {
        lettersArrayList.add(letter);
        int totalPoints = 0;
        int points = checkIfContainsWord();
        while (points > 0) {
            totalPoints += points;
            EmojiApp.addToScore(points);
            points = checkIfContainsWord();
        }
        if (totalPoints > 0) {
            repositionLetters();
            EmojiApp.addToScore(totalPoints * POINTs_PER_LETTER_IN_SPELLED_WORD);
        }
        if (lettersArrayList.size() == CAPACITY) {
            EmojiApp.setGameOver(true);
        }
    }

    /*
     * Checks if letter bar contains a word and returns the number of letters removed form the letter bar.
     */
    private static int checkIfContainsWord() {
        StringBuilder stringBuilder = new StringBuilder();
        for (Letter letter : lettersArrayList) {
            stringBuilder.append(letter.getText().getText());
        }
        String letterBarString = stringBuilder.toString();
        int index = -1;
        int wordLength = 0;
        for (String word : wordList) {
            if (word.length() > lettersArrayList.size()) {
                break;
            }
            if (letterBarString.contains(word)) {
                index = letterBarString.indexOf(word);
                wordLength = word.length();
            }
        }
        if (index >= 0) {
            removeWord(index, wordLength);
            return wordLength;
        }
        return 0;
    }

    /*
     * Removes a word from the letter bar.
     */
    private static void removeWord(final int index, final int length) {
        Letter letter;
        // remove letters from arraylist
        for (int i = 0; i < length; i++) {
            letter = lettersArrayList.remove(index);
            EmojiApp.removeFromRootScene(letter);
        }
    }

    /*
     * Removes letters of a given color from the letter bar.
     */
    public static void removeLettersByColor(final Color color) {
        Iterator iterator = lettersArrayList.iterator();
        while (iterator.hasNext()) {
            Letter letter = (Letter) iterator.next();
            if (letter.getColor().equals(color)) {
                EmojiApp.removeFromRootScene(letter);
                iterator.remove();
            }
        }
        repositionLetters();
    }

    /*
     * Repositions letters i.e. if some were removed.
     */
    private static void repositionLetters() {
        for (int i = 0; i < lettersArrayList.size(); i++) {
            double translate = letterBar.getTranslateX() + (i * FONT_SIZE) + LETTER_MARGIN_X;
            lettersArrayList.get(i).setTranslateX(translate);
        }
    }
}
