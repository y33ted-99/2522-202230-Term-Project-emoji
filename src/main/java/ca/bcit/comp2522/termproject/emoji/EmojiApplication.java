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
public class EmojiApplication extends Application {

    public final static int APP_WIDTH = 1000;
    public final static int APP_HEIGHT = 800;
    public final static int PLAY_AREA_SIZE = 600;
    public final static int EMOJI_SIZE = 40;
    public final static Random RNG = new Random();

    private final static Group root = new Group();

    private static EnemyTextBubble[] enemyTextBubbles;
    private static int enemyCount;

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

        spawnEnemyTextBubble();

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
        int playAreaLeft = (APP_WIDTH - PLAY_AREA_SIZE) / 2;
        int playAreaTop = (APP_HEIGHT - PLAY_AREA_SIZE) / 2;
        Rectangle playArea = new Rectangle(
                playAreaLeft,
                playAreaTop,
                PLAY_AREA_SIZE,
                PLAY_AREA_SIZE);
        playArea.setStroke(Color.BLACK);
        playArea.setFill(new Color(1,1,1,0.8));
        playArea.setArcHeight(10);
        playArea.setArcWidth(10);
        root.getChildren().add(playArea);
    }

    private void createPlayer() {
        Group player = new Player(APP_WIDTH / 2, (int)(APP_HEIGHT * 0.75));
        root.getChildren().add(player);

    }

    private void setUpTextBubbleArrays() {
        int textBubblesPerSide = PLAY_AREA_SIZE / (TextBubble.TEXT_BUBBLE_HEIGHT + 10);
        enemyTextBubbles = new EnemyTextBubble[textBubblesPerSide * 2];
        enemyCount = 0;
    }

    private void spawnEnemyTextBubble() {
        if (enemyCount >= enemyTextBubbles.length) {
            return;
        }
        int position = RNG.nextInt(enemyTextBubbles.length);
        EnemyTextBubble enemy = new EnemyTextBubble(GameSide.LEFT, 200, EnemyType.ANGRY);
//        enemyTextBubbles
        root.getChildren().add(enemy);
    }
}

