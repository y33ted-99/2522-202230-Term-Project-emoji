package ca.bcit.comp2522.termproject.emoji;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Random;

/**
 * The main driver class of the game.
 *
 * @author Brian Mak
 * @author Terence Grigoruk
 * @version Fall 2022
 */
public class EmojiApp extends Application {

    public final static int APP_WIDTH = 1000;
    public final static int APP_HEIGHT = 800;
    public final static int PLAY_AREA_SIZE = 600;
    public final static int EMOJI_SIZE = 40;
    public final static Random RNG = new Random();

    public final static Group root = new Group();

    public static Player player;
    private static TextBubble[] textBubbles;
    private static int enemyCount;

    public final static int MARGIN_X = (APP_WIDTH - PLAY_AREA_SIZE) / 2;
    public final static int MARGIN_Y = (APP_HEIGHT - PLAY_AREA_SIZE) / 2;
    /**
     * Displays an image centered in a window.
     *
     * @param primaryStage a Stage
     */
    @Override
    public void start(final Stage primaryStage) {
        boolean gameNotOver = true;

        createBackground();
        createPlayArea();
        createPlayer();

        setUpTextBubbleArrays();

        for (int tb = 0; tb < 3; tb++) {
            spawnTextBubble();
        }


        Scene scene = new Scene(root, APP_WIDTH, APP_HEIGHT);
        primaryStage.setResizable(false);
        primaryStage.setTitle("Untitled Emoji Game");
        primaryStage.setScene(scene);
        primaryStage.show();

        // main game loop
//        while (gameNotOver) {
//            gameNotOver = player.isAlive();
//        }
    }

    public static void main(final String[] args) {
        launch();
    }

    private void createBackground() {
        try (InputStream is = Files.newInputStream(Paths.get("resources/bg/blue-pink-background.jpg"))) {
            ImageView backgroundImageView = new ImageView(new Image(is));
            backgroundImageView.setFitHeight(APP_HEIGHT);
            backgroundImageView.setPreserveRatio(true);
            backgroundImageView.setCache(true);
            root.getChildren().add(backgroundImageView);
        } catch (IOException e) {
            System.out.println("Cannot load image");
        }
    }

    private void createPlayArea() {
        Rectangle playArea = new Rectangle(
                MARGIN_X,
                MARGIN_Y,
                PLAY_AREA_SIZE,
                PLAY_AREA_SIZE);
        playArea.setStroke(Color.BLACK);
        playArea.setFill(new Color(1, 1, 1, 0.8));
        playArea.setArcHeight(10);
        playArea.setArcWidth(10);
        root.getChildren().add(playArea);
    }

    private void createPlayer() {
        player = new Player(APP_WIDTH / 2, (int) (APP_HEIGHT * 0.75));
        root.getChildren().add(player);

    }

    // TODO: expand setUpTextBubbleArrays() to accommodate all sides of game area
    /*
     * Set up an array of text bubbles.
     */
    private void setUpTextBubbleArrays() {
        int textBubblesPerSide = PLAY_AREA_SIZE / TextBubble.TEXT_BUBBLE_HEIGHT;
        textBubbles = new TextBubble[textBubblesPerSide];
        enemyCount = 0;
    }

    private void spawnTextBubble() {
        if (enemyCount >= textBubbles.length) {
            return;
        }
        int index;
        do {
            index = RNG.nextInt(textBubbles.length);
        }
        while (textBubbles[index] != null);
        EnemyType type = EnemyType.values()[new Random().nextInt(EnemyType.values().length)];
        int position = (TextBubble.TEXT_BUBBLE_HEIGHT * index) + MARGIN_Y;
        TextBubble enemy = new TextBubble(GameSide.LEFT, position, type);
//        textBubbles.add()
        enemyCount++;
        root.getChildren().add(enemy);
        textBubbles[index] = enemy;
    }
}

