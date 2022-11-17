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

/**
 * The main driver class of the game.
 *
 * @author Brian Mak
 * @author Terence Grigoruk
 * @version Fall 2022
 */
public class EmojiApplication extends Application {

    public final int appWidth = 1000;
    public final int appHeight = 800;
    public final int playAreaSize = 600;

    /**
     * Displays an image centered in a window.
     *
     * @param primaryStage a Stage
     */
    @Override
    public void start(final Stage primaryStage) {

        // create the background image
        Image backgroundImage = new Image("geometric.jpg");
        ImageView imageView = new ImageView(backgroundImage);
        imageView.setViewport(new Rectangle2D(0, 0, appWidth, appHeight));

        // create the play area
        Rectangle playArea = new Rectangle(
                (appWidth - playAreaSize) / 2,
                (appHeight - playAreaSize) / 2,
                playAreaSize,
                playAreaSize);
        playArea.setStroke(Color.BLACK);
        playArea.setFill(new Color(1,1,1,0.8));
        playArea.setArcHeight(10);
        playArea.setArcWidth(10);


        Group root = new Group(imageView, playArea);

        Scene scene = new Scene(root, appWidth, appHeight);
        primaryStage.setResizable(false);
        primaryStage.setTitle("Untitled Emoji Game");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(final String[] args) {
        launch();
    }
}

