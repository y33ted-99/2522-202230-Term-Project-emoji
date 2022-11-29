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
    private static final List<GameItem> gameItems = new ArrayList<>();
    private static boolean gameOver = false;
    private static int score = 0;

    private Parent createContent() {
        root = new Pane();
        root.setPrefSize(APP_WIDTH, APP_HEIGHT);

        Node background = PlayArea.createBackground();
        Node playArea = PlayArea.createPlayArea();
        Node letterBar = LetterBar.createLetterBar();

        player = createPlayer();
        leftTextBubbleGroup = new TextBubbleGroup(Side.LEFT);
        rightTextBubbleGroup = new TextBubbleGroup(Side.RIGHT);
        root.getChildren().addAll(
                background,
                playArea,
                letterBar,
                player,
                leftTextBubbleGroup,
                rightTextBubbleGroup);

        // Main game loop
        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(final long now) {
                onUpdate(now);
            }
        };
        timer.start();
        return root;
    }

    /*
     * Update entities during the main game loop.
     */
    private void onUpdate(final long now) {

        // searchForLetters(leftTextBubbleGroup);
        if (!gameOver && now % 15000 < 10) {
            if (player.getCenterX() > APP_WIDTH / 2) {
                leftTextBubbleGroup.spawnTextBubble();
            } else {
                rightTextBubbleGroup.spawnTextBubble();
            }
        }
        leftTextBubbleGroup.update();
        rightTextBubbleGroup.update();
        gameItems.forEach(gameItem -> {
            if (!gameItem.isAlive()) {
                gameItems.remove(gameItem);
            }
        });
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

        scene.setOnMouseMoved(event -> {
            player.moveToMouse(event);
        });
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

//    private void searchForLetters(final Group textBubbleGroup) {
//        for (Node textBubble : textBubbleGroup.getChildren()) {
//            for (Node textBubbleComponent : ((Group) textBubble).getChildren()) {
//                if (textBubbleComponent.getClass() == Enemy.class) {
//                    System.out.println(((Enemy) textBubbleComponent).getChildren());
//                }
//            }
//        }
//    }

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
     * @param points
     */
    public static void addToScore(final int points) {
        player.addToScore(points);
    }

    public static void incrementPlayerPoppedBubbles() {
        player.incrementPoppedBubbles();
    }

    public static void spawnItem() {
        ItemType type = ItemType.values()[new Random().nextInt(ItemType.values().length)];
        gameItems.add(GameItem.getInstance(type));
        System.out.println("spawned item: " + type);
    }
}

