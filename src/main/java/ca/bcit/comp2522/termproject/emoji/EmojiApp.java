package ca.bcit.comp2522.termproject.emoji;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

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
    private static Point2D playerMovementVector = new Point2D(0, 0);
    private static MouseEvent mouseMoveEvent;

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

        if (now % 20000 < 10) {
            if (RNG.nextInt(2) > 0) {
                leftTextBubbleGroup.spawnTextBubble();
            } else {
                rightTextBubbleGroup.spawnTextBubble();
            }
        }
        leftTextBubbleGroup.update();
        rightTextBubbleGroup.update();
        player.move();
    }

    /**
     * Starts the game.
     *
     * @param primaryStage a Stage
     */
    @Override
    public void start(final Stage primaryStage) {

        Scene scene = new Scene(createContent());

        scene.setOnMouseMoved((event) -> {
            player.moveToMouse(event);
        });
//        scene.setOnMouseClicked(InputController::mouseClickHandler);

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

    public static void addToScene(final Node node) {
        root.getChildren().add(node);
    }
}

