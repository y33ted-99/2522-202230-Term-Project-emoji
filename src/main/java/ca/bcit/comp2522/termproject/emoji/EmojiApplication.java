package ca.bcit.comp2522.termproject.emoji;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.util.Duration;

/*import javax.swing.*;*/
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
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

    private static TextBubble[] textBubbles;
    private static int enemyCount;

    public final static int MARGIN_X = (APP_WIDTH - PLAY_AREA_SIZE) / 2;
    public final static int MARGIN_Y = (APP_HEIGHT - PLAY_AREA_SIZE) / 2;

    public enum Direction {
        UP, DOWN, LEFT, RIGHT
    }

    private boolean running = false;

    private boolean move = true;
    private Timeline timeline = new Timeline();

    private Direction direction = Direction.UP;

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

        setUpTextBubbleArrays();

        for (int tb = 0; tb < 50; tb++)
            spawnTextBubble();



        /*Scene scene = new Scene(root, APP_WIDTH, APP_HEIGHT);*/
        Scene scene = new Scene(content());
        scene.setOnKeyPressed(event -> {
            switch (event.getCode()) {
                case W:
                    if (direction != Direction.DOWN)
                        direction = Direction.UP;
                    break;
                case S:
                    if (direction != Direction.UP)
                        direction = Direction.DOWN;
                    break;
                case A:
                    if (direction != Direction.RIGHT)
                        direction = Direction.LEFT;
                    break;
                case D:
                    if (direction != Direction.LEFT)
                        direction = Direction.RIGHT;
                    break;
            }
        });
        primaryStage.setResizable(false);
        primaryStage.setTitle("Untitled Emoji Game");
        primaryStage.setScene(scene);
        primaryStage.show();
        startGame();



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
            backgroundImageView.setFitWidth(APP_WIDTH);
            backgroundImageView.setPreserveRatio(false);
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
        Group player = new Player(APP_WIDTH / 2, (int) (APP_HEIGHT * 0.75));
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
    }

    private Parent content() {

        /*APP_WIDTH / 2, (int) (APP_HEIGHT * 0.75)*/

        Group player = new Player(APP_WIDTH / 2, (int) (APP_HEIGHT * 0.75));
        root.getChildren().add(player);
        if (move) {
            KeyFrame frame = new KeyFrame(Duration.millis(75), event -> {
                if (!running)
                    return;

                switch (direction) {
                    case UP:
                        player.setTranslateX(player.getTranslateX());
                        player.setTranslateY(player.getTranslateY() - EMOJI_SIZE);
                        break;

                    case DOWN:
                        player.setTranslateX(player.getTranslateX());
                        player.setTranslateY(player.getTranslateY() + EMOJI_SIZE);
                        break;

                    case LEFT:
                        player.setTranslateX(player.getTranslateX() - EMOJI_SIZE);
                        player.setTranslateY(player.getTranslateY());
                        break;

                    case RIGHT:
                        player.setTranslateX(player.getTranslateX() + EMOJI_SIZE);
                        player.setTranslateY(player.getTranslateY());
                        break;
                }

                if (player.getTranslateX() < 0 || player.getTranslateX() >= MARGIN_X || player.getTranslateY() < 0 ||
                        player.getTranslateY() >= MARGIN_Y) {
                    /*resetPosition();*/
                }

            });

            timeline.getKeyFrames().add(frame);
            timeline.setCycleCount(Timeline.INDEFINITE);

        }
        return root;
    }

    private void startGame() {
        direction = Direction.UP;
        timeline.play();
        running = true;
    }

    /*private void stopGame() {
        running = false;
        timeline.stop();
        playa.clear();
    }
    private void resetPosition() {
        stopGame();
        startGame();
    }*/
}


