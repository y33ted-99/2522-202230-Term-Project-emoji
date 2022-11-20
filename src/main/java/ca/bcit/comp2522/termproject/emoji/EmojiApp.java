package ca.bcit.comp2522.termproject.emoji;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
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

    public final static Rectangle playArea = new Rectangle();
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

    // TODO: restructure to make root and player private
    /**
     * The player object.
     */
    public static Entity player;
    /**
     * The main game pane to which all entities drawn.
     */
    public static Pane root;

    /*
     * Groups of TextBubbles.
     */
    private static TextBubbleGroup LeftTextBubbleGroup;
    private static TextBubbleGroup RightTextBubbleGroup;

    private Parent createContent() {
        root = new Pane();
        root.setPrefSize(APP_WIDTH, APP_HEIGHT);

        createBackground();
        createPlayArea();
        createPlayer();
        LeftTextBubbleGroup = new TextBubbleGroup(GameSide.LEFT);
        RightTextBubbleGroup = new TextBubbleGroup(GameSide.RIGHT);
        root.getChildren().addAll(LeftTextBubbleGroup, RightTextBubbleGroup);

        // Main game loop
        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long l) {
                onUpdate();
            }
        };
        timer.start();
        return root;
    }

    /*
     * Update entities during main game loop.
     */
    private void onUpdate() {

        if (RNG.nextFloat() < 0.005) {
            if (RNG.nextInt(2) > 0) {
                LeftTextBubbleGroup.spawnEnemyTextBubble();
            }else {
                RightTextBubbleGroup.spawnEnemyTextBubble();
            }
        }
    }

    /**
     * Starts the game.
     *
     * @param primaryStage a Stage
     */
    @Override
    public void start(final Stage primaryStage) {
        // set up the main game window
        primaryStage.setResizable(false);
        primaryStage.setTitle("Untitled Emoji Game");
        primaryStage.setScene(new Scene(createContent()));
        primaryStage.show();
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
            backgroundImageView.setPreserveRatio(true);
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
        playArea.setX(MARGIN_X);
        playArea.setY(MARGIN_Y);
        playArea.setWidth(PLAY_AREA_WIDTH);
        playArea.setHeight(PLAY_AREA_HEIGHT);
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
}

