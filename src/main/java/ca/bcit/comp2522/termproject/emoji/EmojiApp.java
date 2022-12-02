package ca.bcit.comp2522.termproject.emoji;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.geometry.Bounds;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
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

    private static Scene scene;
    private static Pane root;
    private static MainMenu mainMenu;
    private static StatusDisplay statusDisplay;
    private static Pane enterName;
    private static Pane gameRound;
    private static Player player;
    private static TextBubbleGroup leftTextBubbleGroup;
    private static TextBubbleGroup rightTextBubbleGroup;
    private static List<GameItem> gameItems;
    private static AnimationTimer timer;
    private static boolean gameOver = false;
    private static long startTime;
    private static int difficulty = 0;

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
        primaryStage.setTitle("La Chat-room");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    /*
     * Creates the main game screen elements (persistent through life of app).
     */
    private Parent createPersistentGameElements() {
        root = new Pane();
        root.setPrefSize(APP_WIDTH, APP_HEIGHT);
        Node background = PlayArea.createBackground();
        Node playArea = PlayArea.createPlayArea();
        Node letterBar = LetterBar.createLetterBar();
        mainMenu = new MainMenu();
        statusDisplay = new StatusDisplay();
        enterName = new EnterName();
        root.getChildren().addAll(
                background,
                playArea,
                letterBar,
                mainMenu,
                statusDisplay);
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
                rightTextBubbleGroup);
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
     * Starts the game.
     */
    public static void startGame() {
        gameOver = false;
        difficulty = 0;
        mainMenu.setVisible(false);
        root.getChildren().add(createGameRound());
        startTime = System.nanoTime();
        timer.start();
    }

    /**
     * Shows the "enter name" screen.
     */
    public static void showEnterName() {
        enterName = new EnterName();
        root.getChildren().add(enterName);
    }

    public static void recordScore(final String name) {
        HighScores.addScore(name, getPlayerScore(), (int) statusDisplay.getElapsedTime());
        System.out.println("high score for "
                + name + ": "
                + getPlayerScore() + " points, in "
                + statusDisplay.getElapsedTime() + " seconds");
        returnToMainMenu();
    }

    /**
     * Returns game to main menu.
     */
    public static void returnToMainMenu() {
        root.getChildren().remove(enterName);
        LetterBar.clear();
        root.getChildren().remove(gameRound);
        mainMenu.setVisible(true);
        System.out.println(HighScores.getScores());
    }

    /**
     * Returns the system time when main game timer started.
     *
     * @return game start time in nanoseconds as long
     */
    public static long getStartTime() {
        return startTime;
    }

    /*
     * Update entities during the main game loop.
     */
    private static void onUpdate(final long now) {

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
        checkGameItems();
        gameItems.forEach(GameItem::update);
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
        Iterator<GameItem> iterator = gameItems.iterator();
        while (iterator.hasNext()) {
            GameItem gameItem = iterator.next();
            if (!gameItem.isAlive()) {
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
}

