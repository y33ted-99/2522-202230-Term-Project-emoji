package ca.bcit.comp2522.termproject.emoji;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.geometry.Bounds;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
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

    /*
     * The main game pane to which all entities drawn.
     */
    private static Pane root;

    private static Player player;
    private static TextBubbleGroup leftTextBubbleGroup;
    private static TextBubbleGroup rightTextBubbleGroup;
    private static List<GameItem> gameItems;
    private static boolean gameOver = false;
    private static AnimationTimer timer;
    private static MainMenu mainMenu;
    private static GameTimer gameTimer;
    private static long startTime;


    /*
     * Creates all the content to be drawn to screen.
     */
    private Parent createContent() {
        root = new Pane();
        root.setPrefSize(APP_WIDTH, APP_HEIGHT);

        Node background = PlayArea.createBackground();
        Node playArea = PlayArea.createPlayArea();
        Node letterBar = LetterBar.createLetterBar();
        mainMenu = new MainMenu();
        gameTimer = new GameTimer();

        player = createPlayer();
        leftTextBubbleGroup = new TextBubbleGroup(Side.LEFT);
        rightTextBubbleGroup = new TextBubbleGroup(Side.RIGHT);
        gameItems = new ArrayList<>();
        root.getChildren().addAll(
                background,
                playArea,
                letterBar,
                player,
                leftTextBubbleGroup,
                rightTextBubbleGroup,
                mainMenu,
                gameTimer);

        // Main game loop
        timer = new AnimationTimer() {
            @Override
            public void handle(final long now) {
                onUpdate(now);
            }
        };

        return root;
    }

    public static void startGame() {
        removeFromRootScene(mainMenu);
        startTime = System.nanoTime();
        timer.start();
    }

    public static long getStartTime() {
        return startTime;
    }

    /*
     * Update entities during the main game loop.
     */
    private void onUpdate(final long now) {

        // searchForLetters(leftTextBubbleGroup);
        if (!gameOver && now % 15000 < 100) {
            if (player.getCenterX() > APP_WIDTH / 2) {
                leftTextBubbleGroup.spawnTextBubble();
            } else {
                rightTextBubbleGroup.spawnTextBubble();
            }
        }
        leftTextBubbleGroup.update();
        rightTextBubbleGroup.update();
        checkGameItems();
        gameItems.forEach(GameItem::update);
        gameTimer.update(now);
        if (!gameOver) {
            player.move();
        }
    }

    /**
     * Starts the game.
     *
     * @param primaryStage a Stage
     */
    @Override
    public void start(final Stage primaryStage) {

        Scene scene = new Scene(createContent());

        scene.setOnMouseMoved(event -> player.moveToMouse(event));
        scene.setOnMouseClicked(event -> {
            leftTextBubbleGroup.mouseClickHandler(event);
            rightTextBubbleGroup.mouseClickHandler(event);
        });

        primaryStage.setResizable(false);
        primaryStage.setTitle("La Chat-room");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    /**
     * Entry point to program.
     *
     * @param args not used
     */
    public static void main(final String[] args) {
        launch();
    }

    /*
     * Create the player.
     */
    private Player createPlayer() {
        player = new Player(
                APP_WIDTH / 2 - Entity.IMAGE_SIZE / 2,
                APP_HEIGHT / 2 - Entity.IMAGE_SIZE / 2);
        return player;
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
     * Adds a node to the root scene.
     *
     * @param node a node to be added to the root scene
     */
    public static void addToRootScene(final Node node) {
        root.getChildren().add(node);
    }

    /**
     * Removes a node from the root scene.
     *
     * @param node a node to be added to the root scene
     */
    public static void removeFromRootScene(final Node node) {
        root.getChildren().remove(node);
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
        addToRootScene(gameItem);
    }

    /**
     * Checks games and if acquired by player removes from game and adds points to player.
     */
    public static void checkGameItems() {
        Iterator<GameItem> iterator = gameItems.iterator();
        while (iterator.hasNext()) {
            GameItem gameItem = iterator.next();
            if (!gameItem.isAlive()) {
                removeFromRootScene(gameItem);
                EmojiApp.addToScore(GameItem.POINTS_PER_ITEM);
                iterator.remove();
            }
        }
    }
}

