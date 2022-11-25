package ca.bcit.comp2522.termproject.emoji;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
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
    public static final  int APP_HEIGHT = 800;

    /**
     * The play area in which the player and letters exist.
     */
    public static final  Rectangle PLAY_AREA = new Rectangle();
    /**
     * Width of the play area.
     */
    public static  final  int PLAY_AREA_WIDTH = 600;
    /**
     * Height of the play area.
     */
    public static final  int PLAY_AREA_HEIGHT = 600;

    /**
     * The margins on the left and right side of the play area.
     */
    public static final  int MARGIN_X = (APP_WIDTH - PLAY_AREA_WIDTH) / 2;
    /**
     * The margins on the top and bottom side of the play area.
     */
    public static final  int MARGIN_Y = (APP_HEIGHT - PLAY_AREA_HEIGHT) / 2;
    /**
     * Random number generator.
     */
    public static final  Random RNG = new Random();

    /**
     * The player object.
     */
    public static Player player;

    /**
     * Movement directions.
     */
    public enum Direction {
        UP, DOWN, LEFT, RIGHT
    }

    /**
     * The main game pane to which all entities drawn.
     */
    public static Pane root;

    /*
     * Groups of TextBubbles.
     */
    private static TextBubbleGroup leftTextBubbleGroup;
    private static TextBubbleGroup rightTextBubbleGroup;
    private static Point2D playerMovementVector = new Point2D(0, 0);
    private static MouseEvent mouseMoveEvent;

    private Parent createContent() {
        root = new Pane();
        root.setPrefSize(APP_WIDTH, APP_HEIGHT);


        createBackground();
        createPlayArea();
        createPlayer();

        leftTextBubbleGroup = new TextBubbleGroup(GameSide.LEFT);
        rightTextBubbleGroup = new TextBubbleGroup(GameSide.RIGHT);


        // Main game loop
        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(final long now) {
                onUpdate(now);
            }
        };
        VBox box = new VBox(
                    20,
                    new MenuItem("START", () -> timer.start()),
                    new MenuItem("HIGHSCORE", () -> {}),
                    new MenuItem("QUIT", () -> Platform.exit())
            );

            box.setTranslateX(380);
            box.setTranslateY(380);


            root.getChildren().addAll(leftTextBubbleGroup, rightTextBubbleGroup, box);
        return root;
    }

    /*
     * Update entities during the main game loop.
     */
    private void onUpdate(final long now) {

        if (now % 20000 < 10) {
            if (RNG.nextInt(2) > 0) {
                leftTextBubbleGroup.spawnEnemyTextBubble();
            } else {
                rightTextBubbleGroup.spawnEnemyTextBubble();
            }
        }
        double xMove = (player.getTranslateX() - playerMovementVector.getX());
        double yMove = (player.getTranslateY() - playerMovementVector.getY());
        if (isValidMouseMove(xMove, yMove)) {
            player.setTranslateX(xMove);
            player.setTranslateY(yMove);
        }
        if (player.getSpeed() > Player.INIT_SPEED) {
            player.setSpeed(player.getSpeed() - 0.02);
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

        scene.setOnMouseMoved(this::mouseMoveHandler);
        scene.setOnMouseClicked(this::mouseClickHandler);



        primaryStage.setResizable(false);
        primaryStage.setTitle("Untitled Emoji Game");
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
        PLAY_AREA.setX(MARGIN_X);
        PLAY_AREA.setY(MARGIN_Y);
        PLAY_AREA.setWidth(PLAY_AREA_WIDTH);
        PLAY_AREA.setHeight(PLAY_AREA_HEIGHT);
        PLAY_AREA.setStroke(Color.BLACK);
        PLAY_AREA.setFill(new Color(1, 1, 1, 0.8));
        PLAY_AREA.setArcHeight(10);
        PLAY_AREA.setArcWidth(10);
        root.getChildren().add(PLAY_AREA);
    }

    /*
     * Create the player.
     */
    private void createPlayer() {
        player = new Player(
                APP_WIDTH / 2 - Entity.IMAGE_SIZE / 2,
                APP_HEIGHT / 2 - Entity.IMAGE_SIZE / 2);
        root.getChildren().add(player);
    }

    /*
     * Handle mouse movement.
     */
    private void mouseMoveHandler(final MouseEvent event) {
        mouseMoveEvent = event;
        playerMovementVector = new Point2D(
                player.getCenterX() - event.getSceneX(),
                player.getCenterY() - event.getSceneY())
                .normalize();
        playerMovementVector = playerMovementVector.multiply(player.getSpeed());
    }

    /*
     * Checks if movement destination is within bounds and does not overlap with cursor.
     */
    private boolean isValidMouseMove(final double xDestination, final double yDestination) {
        if (mouseMoveEvent == null) {
            return false;
        }
        Bounds playerBounds = player.getBoundsInParent();
        Bounds playAreaBounds = PLAY_AREA.getBoundsInParent();
        Point2D mouse = new Point2D(mouseMoveEvent.getSceneX(), mouseMoveEvent.getSceneY());
        return !playerBounds.contains(mouse)
                && xDestination > playAreaBounds.getMinX()
                && xDestination < playAreaBounds.getMaxX() - Player.IMAGE_SIZE
                && yDestination > playAreaBounds.getMinY()
                && yDestination < playAreaBounds.getMaxY() - Player.IMAGE_SIZE;
    }

    /*
     * Makes player "pounce" in direction of cursor and/or pop text bubble.
     */
    private void mouseClickHandler(final MouseEvent event) {
        player.setSpeed(Player.POUNCE_SPEED);
    }

    private static class MenuItem extends StackPane {
        MenuItem(String name, Runnable action) {
            LinearGradient gradient = new LinearGradient(
                    0.5, 0.5, 1, 0.5, true, CycleMethod.NO_CYCLE,
                    new Stop(0.8, Color.web("Pink", 0.5)),
                    new Stop(1, Color.web("white", 0.5)));
            Rectangle bg = new Rectangle(250, 30, gradient);


            Text text = new Text(name);
            text.setFont(Font.font(25));
            text.fillProperty().bind(
                    Bindings.when(hoverProperty()).then(Color.WHITE).otherwise(Color.BLACK)
            );

            setOnMouseClicked(e -> action.run());

            getChildren().addAll(bg, text);
        }
    }
}

