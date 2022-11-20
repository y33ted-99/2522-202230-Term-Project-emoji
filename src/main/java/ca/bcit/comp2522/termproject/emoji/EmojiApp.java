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
 * @author Terence Grigoruk
 * @author Brian Mak
 * @version Fall 2022
 */
public class EmojiApp extends Application {

    /**
     * Width of the main app screen.
     */
    public final static int APP_WIDTH = 1000;
    /**
     * Height of the main app screen.
     */
    public final static int APP_HEIGHT = 800;
    /**
     * Width of the play area.
     */
    public final static int PLAY_AREA_WIDTH = 600;
    /**
     * Height of the play area.
     */
    public final static int PLAY_AREA_HEIGHT = 600;

    /**
     * The margins on the left and right side of the play area.
     */
    public final static int MARGIN_X = (APP_WIDTH - PLAY_AREA_WIDTH) / 2;
    /**
     * The margins on the top and bottom side of the play area.
     */
    public final static int MARGIN_Y = (APP_HEIGHT - PLAY_AREA_HEIGHT) / 2;
    /**
     * Random number generator.
     */
    public final static Random RNG = new Random();
    /**
     * The root group to be drawn to screen.
     */
    public final static Group root = new Group();
    /**
     * The player obejct.
     */
    public static Player player;

    /*
     * An array of TextBubbles
     */
    private static TextBubble[] textBubbles;

    /*
     * The current number of enemies in play.
     */
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
     * Starts the game.
     *
     * @param primaryStage a Stage
     */
    @Override
    public void start(final Stage primaryStage) {
        boolean gameNotOver = true;

        createBackground();
        createPlayArea();

        setUpTextBubbleArrays();

        for (int tb = 0; tb < 3; tb++) {
            spawnEnemyTextBubble();
        }


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

    /*
     * Loads the background image.
     */
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

    /*
     * Creates the main play area where the action takes place.
     */
    private void createPlayArea() {
        Rectangle playArea = new Rectangle(
                MARGIN_X,
                MARGIN_Y,
                PLAY_AREA_WIDTH,
                PLAY_AREA_HEIGHT);
        playArea.setStroke(Color.BLACK);
        playArea.setFill(new Color(1, 1, 1, 0.8));
        playArea.setArcHeight(10);
        playArea.setArcWidth(10);
        root.getChildren().add(playArea);
    }

    /*
     * Create the player.
     */
    private void createPlayer() {
        player = new Player(APP_WIDTH / 2, (int) (APP_HEIGHT * 0.75));
        root.getChildren().add(player);

    }

    // TODO: expand setUpTextBubbleArrays() to accommodate all sides of game area
    /*
     * Set up an array of text bubbles.
     */
    private void setUpTextBubbleArrays() {
        int textBubblesPerSide = PLAY_AREA_HEIGHT / TextBubble.TEXT_BUBBLE_HEIGHT;
        textBubbles = new TextBubble[textBubblesPerSide];
        enemyCount = 0;
    }

    /*
     * Randomly spawn a TextBubble containing an enemy
     */
    private void spawnEnemyTextBubble() {
        if (enemyCount >= textBubbles.length) {
            return;
        }
        int index;
        do {
            index = RNG.nextInt(textBubbles.length);
        }
        while (textBubbles[index] != null);
        EnemyType type;
        do {
            type = EnemyType.values()[new Random().nextInt(EnemyType.values().length)];
        }while(checkIfEmojiExists(type));

        int position = (TextBubble.TEXT_BUBBLE_HEIGHT * index) + MARGIN_Y;
        TextBubble enemy = new TextBubble(GameSide.LEFT, position, type);
        enemyCount++;
        root.getChildren().add(enemy);
        textBubbles[index] = enemy;
    }

    private boolean checkIfEmojiExists(EmojiType emoji) {
        for (TextBubble tb: textBubbles) {
            if (tb == null){
                continue;
            }
            if (tb.getType() == emoji) {
                return true;
            }
        }
        return false;
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


