package ca.bcit.comp2522.termproject.emoji;

import javafx.application.Application;
import javafx.geometry.Rectangle2D;
import javafx.scene.Group;
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

    /**
     * Displays an image centered in a window.
     *
     * @param primaryStage a Stage
     */
    @Override
    public void start(final Stage primaryStage) {

        // create the background image
        Image backgroundImage = new Image("blue-pink-geometric.jpg");
        ImageView imageView = new ImageView(backgroundImage);
//        imageView.setViewport(new Rectangle2D(0, 0, APP_WIDTH, APP_HEIGHT));
        imageView.setFitHeight(APP_HEIGHT);
        imageView.setPreserveRatio(true);
        imageView.setSmooth(true);
        imageView.setCache(true);

        // create the play area
        Rectangle playArea = new Rectangle(
                (APP_WIDTH - PLAY_AREA_SIZE) / 2,
                (APP_HEIGHT - PLAY_AREA_SIZE) / 2,
                PLAY_AREA_SIZE,
                PLAY_AREA_SIZE);
        playArea.setStroke(Color.BLACK);
        playArea.setFill(new Color(1,1,1,0.8));
        playArea.setArcHeight(10);
        playArea.setArcWidth(10);

        Group enemy = new EnemyTextBubbleFactory("left",200, "angry");



        Group root = new Group(imageView, playArea, enemy);
//        root.getChildren().add(new Enemy("angry"));

        Scene scene = new Scene(root, APP_WIDTH, APP_HEIGHT);
        primaryStage.setResizable(false);
        primaryStage.setTitle("Untitled Emoji Game");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(final String[] args) {
        launch();
    }
}

