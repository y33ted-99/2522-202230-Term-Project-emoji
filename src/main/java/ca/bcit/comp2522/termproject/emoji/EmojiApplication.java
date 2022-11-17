package ca.bcit.comp2522.termproject.emoji;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

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
        Image backgroundImage = new Image("blue-pink-geometric.jpg");
        ImageView backgroundImageView = new ImageView(backgroundImage);
        backgroundImageView.setFitHeight(APP_HEIGHT);
        backgroundImageView.setPreserveRatio(true);
        backgroundImageView.setSmooth(true);
        backgroundImageView.setCache(true);
        root.getChildren().add(backgroundImageView);
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

    public void createPlayer() {
        Group player = new Player(APP_WIDTH / 2, APP_HEIGHT / 2);
        root.getChildren().add(player);

    }
    public void spawnEnemyTextBubble() {
        EnemyTextBubble enemy = new EnemyTextBubble("left",200, "angry");
        root.getChildren().add(enemy);
    }
}

