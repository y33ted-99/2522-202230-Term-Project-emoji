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
    private static final int POINTS_PER_WORD = 1000;
    private static final int POINTS_PER_LETTER_IN_SPELLED_WORD = 50;
    private static final Group LETTER_BAR = new Group();
    private static final Rectangle CONTAINER = new Rectangle();
    private static final List<Letter> LETTERS = new ArrayList<>();
    private static final List<String> WORD_LIST = loadWordList();

    /*
     * Reads words from 'wordlist.txt' and adds to wordList
     */
    private static List<String> loadWordList() {
        URL url = EmojiApp.class.getResource("wordlist.txt");
        List<String> list = null;
        try {
            assert url != null;
            Path filepath = Paths.get(url.toURI());
            list = Files.readAllLines(filepath, StandardCharsets.UTF_8);
        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
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
        LETTER_BAR.setTranslateX(PlayArea.getMarginX());
        LETTER_BAR.setTranslateY(PlayArea.getMarginY()
                + PlayArea.HEIGHT
                + ((PlayArea.getMarginY()
                - CONTAINER.getHeight()) / 2));
        return LETTER_BAR;
    }

    /*
     * Creates the container panel containing the row of letters.
     */
    private static void createContainer() {
        CONTAINER.setWidth(PlayArea.WIDTH);
        CONTAINER.setHeight(HEIGHT);
        CONTAINER.setStroke(Color.BLACK);
        CONTAINER.setFill(new Color(1, 1, 1, 0.9));
        CONTAINER.setArcHeight(10);
        CONTAINER.setArcWidth(10);
        LETTER_BAR.getChildren().add(CONTAINER);
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
            LETTER_BAR.getChildren().add(cell);
        }
    }

    /**
     * Returns the coordinates of the next available slot for a letter in the letter bar.
     *
     * @return the coordinate of the next slot position as Point2D
     */
    public static Point2D getNextSlot() {
        return new Point2D(
                LETTER_BAR.getTranslateX() + (LETTERS.size() * FONT_SIZE) + LETTER_MARGIN_X,
                LETTER_BAR.getTranslateY() + LETTER_MARGIN_Y);
    }

    /**
     * Adds a letter to the letter bar.
     *
     * @param letter the letter to add as Text
     */
    public static void addLetter(final Letter letter) {
        LETTERS.add(letter);
        int totalPoints = 0;
        int points = checkIfContainsWord();
        while (points > 0) {
            totalPoints += points;
            EmojiApp.addToScore(POINTS_PER_WORD);
            points = checkIfContainsWord();
        }
        if (totalPoints > 0) {
            repositionLetters();
            EmojiApp.addToScore(totalPoints * POINTS_PER_LETTER_IN_SPELLED_WORD);
        }
        if (LETTERS.size() == CAPACITY) {
            EmojiApp.setGameOver(true);
        }
    }

    /*
     * Checks if letter bar contains a word and returns the number of letters removed form the letter bar.
     */
    private static int checkIfContainsWord() {
        StringBuilder stringBuilder = new StringBuilder();
        for (Letter letter : LETTERS) {
            stringBuilder.append(letter.getText().getText());
        }
        String letterBarString = stringBuilder.toString();
        int index = -1;
        int wordLength = 0;
        for (String word : WORD_LIST) {
            if (word.length() > LETTERS.size()) {
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
            letter = LETTERS.remove(index);
            EmojiApp.removeFromGameRound(letter);
        }
    }

    /**
     * Removes letters of a given color from the letter bar and game.
     *
     * @param color the color of letters to remove as Color
     */
    public static void removeLettersByColor(final Color color) {
        Iterator<Letter> iterator = LETTERS.iterator();
        while (iterator.hasNext()) {
            Letter letter = iterator.next();
            if (letter.getColor().equals(color)) {
                EmojiApp.removeFromGameRound(letter);
                iterator.remove();
            }
        }
        repositionLetters();
    }

    /**
     * Removes all letters from letter bar and game.
     */
    public static void clear() {
        Iterator<Letter> iterator = LETTERS.iterator();
        while (iterator.hasNext()) {
            Letter letter = iterator.next();
            EmojiApp.removeFromGameRound(letter);
            iterator.remove();
        }
    }

    /*
     * Repositions letters i.e. if some were removed.
     */
    private static void repositionLetters() {
        for (int i = 0; i < LETTERS.size(); i++) {
            double translate = LETTER_BAR.getTranslateX() + (i * FONT_SIZE) + LETTER_MARGIN_X;
            LETTERS.get(i).setTranslateX(translate);
        }
    }
}
