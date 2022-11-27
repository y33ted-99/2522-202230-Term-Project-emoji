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
     *
     * @return a Group containing elements of a letter bar
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
                letterBar.getTranslateX() + (letters.size() * FONT_SIZE) + LETTER_MARGIN,
                letterBar.getTranslateY() + 36);
    }

    /**
     * Adds a letter to the letter bar.
     *
     * @param letter the letter to add as Text
     */
    public static void addLetter(final Text letter) {
        letters.add(letter);
        int totalPoints = 0;
        int points = checkIfContainsWords();
        while (points > 0) {
            totalPoints += points;
            EmojiApp.addToScore(points);
            points = checkIfContainsWords();
        }
        if (totalPoints > 0) {
            repositionLetters();
        }
        if (letters.size() == CAPACITY) {
            EmojiApp.setGameOver(true);
        }
    }

    /*
     * Checks if letter bar contains a words and returns the number of letters removed form the letter bar.
     */
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

    /*
     * Removes a word from the letter bar.
     */
    private static void removeWord(final int index, final int length) {
        Text letter;
        // remove letters from arraylist
        for (int i = 0; i < length; i++) {
            letter = letters.remove(index);
            EmojiApp.removeFromRootScene(letter);
        }
    }

    // TODO: removeLetter() should remove letters belonging to a particular set i.e. a TextBubble
    /*
     * Removes a specific letter from the letter bar.
     */
    private static void removeLetter(final Text letterToRemove) {
        for (int i = 0; i < letters.size(); i++) {
            if (letters.get(i) == letterToRemove) {
                letters.remove(letterToRemove);
                EmojiApp.removeFromRootScene(letterToRemove);
            }
        }
        repositionLetters();
    }

    /*
     * Repositions letters i.e. if some were removed.
     */
    private static void repositionLetters() {
        for (int i = 0; i < letters.size(); i++) {
            double translate = letterBar.getTranslateX() + (i * FONT_SIZE) + 9;
            System.out.println(translate);
            letters.get(i).setX(translate);
        }
    }
}
