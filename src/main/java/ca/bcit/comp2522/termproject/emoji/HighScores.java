package ca.bcit.comp2522.termproject.emoji;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Stores the game's high scores.
 *
 * @author Terence Grigoruk
 * @author Brian Mak
 * @version Fall 2022
 */
public class HighScores implements Serializable {
    private final ArrayList<Score> highScores;

    /**
     * Creates instance of type HighScores.
     */
    public HighScores() {
        highScores = new ArrayList<>();
    }

    /**
     * Adds a score to the high score list.
     *
     * @param name  player name as String
     * @param score player score as int
     * @param time  player time as int
     */
    public void addScore(final String name, final int score, final int time) {
        highScores.add(new Score(name, score, time));
    }

    /**
     * Returns a list of high scores.
     *
     * @return high scores as ArrayList of String
     */
    public ArrayList<Score> getScores() {
        return highScores;
    }

    /**
     * Prints all scores.
     */
    public void printScores() {
        for (Score score : highScores) {
            System.out.println("Score for " + score.name() + ": "
                    + score.score() + " points in  "
                    + score.time() + "seconds");
        }
    }
}
