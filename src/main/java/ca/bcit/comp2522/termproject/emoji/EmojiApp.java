package ca.bcit.comp2522.termproject.emoji;

import javafx.animation.AnimationTimer;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.geometry.Bounds;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.Random;

/**
 * The main driver class of the game.
 *
 * @author Terence Grigoruk
 * @author Brian Mak
 * @version Fall 2022
 */
public class EmojiApp extends Application {

    /**
     * Width of the main app screen.
     */
    public static final int APP_WIDTH = 1000;
    /**
     * Height of the main app screen.
     */
    public static final int APP_HEIGHT = 800;

    /**
     * Random number generator.
     */
    public static final Random RNG = new Random();
    /**
     * Intro Music.
     */
    public static final MediaPlayer INTRO_MUSIC = new MediaPlayer(new Media(
            Objects.requireNonNull(EmojiApp.class.getResource("soundfx/intro-music.wav")).toExternalForm()));
    /**
     * Game round music.
     */
    public static final MediaPlayer GAME_MUSIC = new MediaPlayer(new Media(
            Objects.requireNonNull(EmojiApp.class.getResource("soundfx/game-music.wav")).toExternalForm()));
    private static Scene scene;
    private static Pane root;
    private static MainMenu mainMenu;
    private static StatusDisplay statusDisplay;
    private static Pane gameRound;
    private static Player player;
    private static TextBubbleGroup leftTextBubbleGroup;
    private static TextBubbleGroup rightTextBubbleGroup;
    private static List<GameItem> gameItems;
    private static AnimationTimer timer;
    private static boolean gameOver = false;
    private static long startTime;
    private static int difficulty = 0;

    static {
        INTRO_MUSIC.setCycleCount(MediaPlayer.INDEFINITE);
        INTRO_MUSIC.setVolume(0);
        GAME_MUSIC.setCycleCount(MediaPlayer.INDEFINITE);
        GAME_MUSIC.setVolume(0);
    }

    /**
     * Entry point to program.
     *
     * @param args not used
     */
    public static void main(final String[] args) {
        launch();
    }

    /**
     * Starts the application.
     *
     * @param primaryStage a Stage
     */
    @Override
    public void start(final Stage primaryStage) {
        // create the core UI elements
        scene = new Scene(createPersistentGameElements());
        primaryStage.setResizable(false);
        primaryStage.setTitle("La Chatroom");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    /*
     * Creates the main game screen elements (persistent through life of app).
     */
    private Parent createPersistentGameElements() {
        fadeMusic(INTRO_MUSIC, 10, 0.5);
        root = new Pane();
        root.setPrefSize(APP_WIDTH, APP_HEIGHT);
        Node background = PlayArea.createBackground();
        Node playArea = PlayArea.createPlayArea();
        Node letterBar = LetterBar.createLetterBar();
        // main menu has start button that starts a game round
        mainMenu = new MainMenu();
        statusDisplay = new StatusDisplay();
        root.getChildren().addAll(
                background,
                playArea,
                letterBar,
                mainMenu);
        return root;
    }

    /*
     * Creates content for a single round of play.
     */
    private static Pane createGameRound() {
        gameRound = new Pane();
        player = new Player();
        leftTextBubbleGroup = new TextBubbleGroup(Side.LEFT);
        rightTextBubbleGroup = new TextBubbleGroup(Side.RIGHT);
        gameItems = new ArrayList<>();
        gameRound.getChildren().addAll(
                player,
                leftTextBubbleGroup,
                rightTextBubbleGroup,
                statusDisplay);
        // Main game loop
        timer = new AnimationTimer() {
            @Override
            public void handle(final long now) {
                onUpdate(now);
            }
        };
        // assign handlers for mouse events
        scene.setOnMouseMoved(event -> player.moveToMouse(event));
        scene.setOnMouseClicked(event -> {
            leftTextBubbleGroup.mouseClickHandler();
            rightTextBubbleGroup.mouseClickHandler();
        });
        return gameRound;
    }

    /**
     * Starts a round of the game.
     */
    public static void startGameRound() {
        gameOver = false;
        difficulty = 0;
        mainMenu.setVisible(false);
        fadeMusic(INTRO_MUSIC, 0.5, 0);
        fadeMusic(GAME_MUSIC, 5, 0.5);
        root.getChildren().add(createGameRound());
        startTime = System.nanoTime();
        timer.start();
    }

    /**
     * Prompts player to enter their name.
     */
    public static void promptPlayerToEnterName() {
        root.getChildren().add(new EnterName());
        gameRound.getChildren().clear();
    }

    /**
     * Records the player's score and saves to file.
     *
     * @param name player's name as String
     */
    public static void recordPlayerScore(final String name) {
        mainMenu.recordScore(name);
    }

    /**
     * Returns game to main menu.
     */
    public static void returnToMainMenu() {
        timer.stop();
        root.getChildren().remove(root.lookup("#enterName"));
        LetterBar.clear();
        root.getChildren().remove(gameRound);
        mainMenu.setVisible(true);
        fadeMusic(INTRO_MUSIC, 8, 0.5);
    }

    /**
     * Returns the system time when main game timer started.
     *
     * @return game start time in nanoseconds as long
     */
    public static long getStartTime() {
        return startTime;
    }

    /**
     * Returns the elapsed time of the most current game round.
     *
     * @return elapsed time in game round as long
     */
    public static long getElapsedTime() {
        return statusDisplay.getElapsedTime();
    }

    /*
     * Update entities during the main game loop.
     */
    private static void onUpdate(final long now) {
        checkGameItems();
        leftTextBubbleGroup.update();
        rightTextBubbleGroup.update();
        if (gameOver) {
            return;
        }
        // searchForLetters(leftTextBubbleGroup);
        if (now % 15000 < 100) {
            if (player.getCenterX() > APP_WIDTH / 2) {
                leftTextBubbleGroup.spawnTextBubble();
            } else {
                rightTextBubbleGroup.spawnTextBubble();
            }
        }
        statusDisplay.update(now);
        player.move();
    }

    /**
     * Returns the player's bounds.
     *
     * @return player bounds as Bounds
     */
    public static Bounds getPlayerBounds() {
        return player.getBoundsInParent();
    }

    /**
     * Adds a node to the game round pane.
     *
     * @param node a node to be added to the game round pane
     */
    public static void addToGameRound(final Node... node) {
        root.getChildren().addAll(node);
    }

    /**
     * Removes a node from the game round pane.
     *
     * @param node a node to be added to the game round pane
     */
    public static void removeFromGameRound(final Node... node) {
        root.getChildren().removeAll(node);
    }

    /**
     * Sets the game over state.
     *
     * @param go game over state as boolean
     */
    public static void setGameOver(final boolean go) {
        gameOver = go;
        if (gameOver) {
            // ensure player is on top layer
            root.getChildren().remove(player);
            root.getChildren().add(player);
            player.die();
        }
    }

    /**
     * Returns the game over state.
     *
     * @return true if game is over
     */
    public static boolean isGameOver() {
        return gameOver;
    }

    /**
     * Adds points to the player's score.
     *
     * @param points points to add to player score as int
     */
    public static void addToScore(final int points) {
        player.addToScore(points);
    }

    /**
     * Returns the player's score.
     *
     * @return player score as int
     */
    public static int getPlayerScore() {
        return player.getScore();
    }

    /**
     * Increments the number of text bubbles a player has popped.
     */
    public static void incrementPlayerPoppedBubbles() {
        player.incrementPoppedBubbles();
    }

    /**
     * Spawns an item.
     */
    public static void spawnItem() {
        ItemType type = ItemType.values()[new Random().nextInt(ItemType.values().length)];
        GameItem gameItem = GameItem.getInstance(type);
        gameItems.add(gameItem);
        addToGameRound(gameItem);
    }

    /**
     * Checks games and if acquired by player removes from game and adds points to player.
     */
    public static void checkGameItems() {
        gameItems.forEach(GameItem::update);
        Iterator<GameItem> iterator = gameItems.iterator();
        while (iterator.hasNext()) {
            GameItem gameItem = iterator.next();
            if (!gameItem.isAlive() || gameOver) {
                removeFromGameRound(gameItem);
                EmojiApp.addToScore(GameItem.POINTS_PER_ITEM);
                iterator.remove();
            }
        }
    }

    /**
     * Increases the difficulty level.
     *
     * @param level the difficulty leve to set to as int
     */
    public static void setDifficultyLevel(final int level) {
        difficulty = level;
    }

    /**
     * Returns the difficulty level.
     *
     * @return difficulty as int
     */
    public static double getDifficulty() {
        return difficulty;
    }

    /**
     * Fade in/out music.
     *
     * @param music as MediaPLayer
     * @param time seconds as double
     * @param endValue the volume to fade to as double
     */
    public static void fadeMusic(final MediaPlayer music, final double time, final double endValue) {
        if (endValue > 0) {
            music.play();
        }
        Timeline timeline = new Timeline(
                new KeyFrame(Duration.seconds(time),
                        new KeyValue(music.volumeProperty(), endValue)));
        timeline.setOnFinished(event -> {
            if (endValue <= 0) {
                music.stop();
            }
        });
        timeline.play();
    }
}

