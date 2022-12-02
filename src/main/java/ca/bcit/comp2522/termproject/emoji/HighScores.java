package ca.bcit.comp2522.termproject.emoji;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * Stores the game's high scores.
 *
 * @author Terence Grigoruk
 * @author Brian Mak
 * @version Fall 2022
 */
public final class HighScores implements Serializable {
    private static final ArrayList<Score> HIGH_SCORES = new ArrayList<>();

    private HighScores() {
    }

    /**
     * Adds a score to the high score list.
     *
     * @param name  player name as String
     * @param score player score as int
     * @param time  player time as int
     */
    public static void addScore(final String name, final int score, final int time) {
        HIGH_SCORES.add(new Score(name, score, time));
    }

    /**
     * Returns a list of high scores.
     *
     * @return high scores as ArrayList of String
     */
    public static ArrayList<Score> getScores() {
        return HIGH_SCORES;
    }
}

record Score(String name, int score, int time) {
}