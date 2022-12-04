package ca.bcit.comp2522.termproject.emoji;

import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.Comparator;

/**
 * Represents a player's score.
 *
 * @param name  player name as String
 * @param score player score as int
 * @param time  player time as int
 *
 * @author Terence Grigoruk
 * @author Brian Mak
 * @version Fall 2022
 */
public record Score(String name, int score, int time) implements Serializable, Comparable<Score> {
    @Override
    public int compareTo(@NotNull final Score object) {
        return object.score() - this.score;
    }
}
