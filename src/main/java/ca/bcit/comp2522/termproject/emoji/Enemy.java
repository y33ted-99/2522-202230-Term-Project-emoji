package ca.bcit.comp2522.termproject.emoji;

import javafx.application.Platform;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents an Enemy emoji.
 *
 * @author Terence Grigoruk
 * @author Brian Mak
 * @version Fall 2022
 */
public class Enemy extends Entity {
    private static final int SHOOT_RATE = 300;
    private static final int[] SPEED_RANGE = {3, 11};
    private EmojiType emoji;
    private int shootFromX;
    private int shootFromY;
    private GameSide shootFromSide;

    /**
     * Creates an instance of type Enemy.
     *
     * @param emoji the type of emoji as enum EmojiType
     */
    public Enemy(final EmojiType emoji) {
        super("enemy/" + emoji.getFilename());
        this.emoji = emoji;
    }

    /**
     * Sets the x position from which a letter is shot from.
     *
     * @param xPosition as int
     */
    public void setShootFromX(final int xPosition) {
        shootFromX = xPosition;
    }

    /**
     * Sets the y position from which a letter is shot from.
     *
     * @param yPosition as int
     */
    public void setShootFromY(final int yPosition) {
        shootFromY = yPosition;
    }

    /**
     * Sets the side of play area the enemy shoots from.
     *
     * @param shootFromSide side of play area the enemy shoots from as GameSide
     */
    public void setShootFromSide(final GameSide shootFromSide) {
        this.shootFromSide = shootFromSide;
    }
    // TODO: When letters dead end thread, wait a bit, then shoot again.
    /**
     * Shoots Letters at the Player.
     */
    public void shoot() {
        int speed = EmojiApp.RNG.nextInt(SPEED_RANGE[0], SPEED_RANGE[1]);
        char[] charArray = emoji.getPhrase().toCharArray();
        if (shootFromSide == GameSide.LEFT) {
            charArray = reverseCharArray(charArray);
        }
        ShotLetters shotLetters = new ShotLetters(
                charArray,
                (int) EmojiApp.player.getCenterX(),
                (int) EmojiApp.player.getCenterY(),
                speed);
        Thread shotLettersThread = new Thread(shotLetters);
        shotLettersThread.setDaemon(true);
        shotLettersThread.start();
    }

    private char[] reverseCharArray(final char[] carr) {
        char[] newCarr = new char[carr.length];
        for (int i = 0; i < carr.length; i++) {
            newCarr[carr.length - i - 1] = carr[i];
        }
        return newCarr;
    }

    private class ShotLetters implements Runnable {
        char[] letters;
        int xTarget;
        int yTarget;
        int speed;
        List<Letter> shotLetters;
        boolean shotLettersAlive;

        ShotLetters(final char[] letters, final int xTarget, final int yTarget, final int speed) {
            this.letters = letters;
            this.xTarget = xTarget;
            this.yTarget = yTarget;
            this.speed = speed;
            shotLetters = new ArrayList<>();
        }

        /*
         * Runs the letter's animation (bouncing around the play area).
         */
        public void run() {
            for (char chr : letters) {
                try {
                    Thread.sleep(SHOOT_RATE);
                } catch (InterruptedException exception) {
                    exception.printStackTrace();
                }
                Platform.runLater(() -> {
                    Letter letter = new Letter(
                            chr,
                            emoji.getColor(),
                            shootFromX,
                            shootFromY,
                            xTarget,
                            yTarget,
                            speed);
                    shotLetters.add(letter);
                    Thread letterBouncer = new Thread(letter);
                    letterBouncer.setDaemon(true);
                    letterBouncer.start();
                    shotLettersAlive = true;
                });
            }
        }

        /**
         * Returns true if at least one shot letter is alive.
         *
         * @return true if at least one shot letter is alive
         */
        public boolean isAlive() {
            for (Letter letter: shotLetters) {
                if (letter.isAlive()) {
                    return true;
                }
            }
            return false;
        }
    }
}
