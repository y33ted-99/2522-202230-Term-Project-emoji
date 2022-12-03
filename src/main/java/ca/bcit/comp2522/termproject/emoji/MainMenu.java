package ca.bcit.comp2522.termproject.emoji;

import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.TextField;
import javafx.scene.layout.Border;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;

/**
 * Represents the player.
 *
 * @author Terence Grigoruk
 * @author Brian Mak
 * @version Fall 2022
 */
public class MainMenu extends VBox {
    private static final Font FONT = Font.font("Arial", FontWeight.BOLD, 25);
    private final Pane scoreBoard;
    private final HighScores highScores;

    /**
     * Creates instance of type MainMenu.
     */
    public MainMenu() {
        setSpacing(20);
        setTranslateY(PlayArea.getMarginY() * 2.5);
        scoreBoard = new ScoreBoard();
        highScores = loadHighScores();
        updateScoreBoard();
        getChildren().addAll(
                new MenuButton("START", EmojiApp::startGameRound),
                scoreBoard,
                new MenuButton("QUIT", Platform::exit));
    }

    /*
     * Loads high scores from file.
     */
    private static HighScores loadHighScores() {
        HighScores highScoresTemp = new HighScores();
        try {
            FileInputStream fileIn = new FileInputStream("high-scores.data");
            ObjectInputStream in = new ObjectInputStream(fileIn);
            highScoresTemp = (HighScores) in.readObject();
            in.close();
            fileIn.close();
        } catch (IOException i) {
            System.out.println("No past high scores to load - creating new list.");
        } catch (ClassNotFoundException c) {
            System.out.println("HighScores class not found");
            c.printStackTrace();
        }
        return highScoresTemp;
    }

    /**
     * Records the player's score and saves to file.
     *
     * @param name player's name as String
     */
    public void recordScore(final String name) {
        Score score = new Score(name, EmojiApp.getPlayerScore(), (int) EmojiApp.getElapsedTime());
        highScores.addScore(score);
        try (var fos = new FileOutputStream("high-scores.data");
             var oos = new ObjectOutputStream(fos)) {
            oos.writeObject(highScores);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        System.out.println(score);
        updateScoreBoard();
        EmojiApp.returnToMainMenu();
    }

    /*
     * Updates the scoreboard with the most recent 5 scores
     */
    private void updateScoreBoard() {
        final int scoresToDisplay = 5;
        scoreBoard.getChildren().clear();
        Text title = new Text("HIGH SCORES");
        title.setFont(FONT);
        title.setUnderline(true);
        setMargin(title, new Insets(10));
        scoreBoard.getChildren().add(title);
        ArrayList<Score> scores = highScores.getScores();
        if (scores.size() == 0) {
            Text scoreText = new Text("(No scores yet!)");
            scoreBoard.getChildren().add(scoreText);
            return;
        }
        for (int i = 0; i < Math.min(scores.size(), scoresToDisplay); i++) {
            Score score = scores.get(i);
            Text scoreText = new Text(score.name() + ": "
                    + score.score() + "pts in "
                    + score.time() + "s");
            scoreText.setFont(FONT);
            scoreText.setFill(new Color(0.25, 0.25, 0.25, 0.95));
            setMargin(scoreText, new Insets(10));
            scoreBoard.getChildren().add(scoreText);
        }
        // center the menu
        setTranslateX((EmojiApp.APP_WIDTH
                - scoreBoard.getBoundsInParent().getWidth()) / 2);
    }

    /*
     * A ScoreBoard that displays player scores.
     */
    private static class ScoreBoard extends VBox {
        ScoreBoard() {
//            setBorder(Border.stroke(Color.DARKGRAY));
            setAlignment(Pos.CENTER);
            Text title = new Text("HIGH SCORE");
            getChildren().addAll(title);
        }
    }

    /*
     * A MenuButton.
     */
    private static class MenuButton extends StackPane {
        MenuButton(final String name, final Runnable action) {
            LinearGradient gradient = new LinearGradient(
                    0.5, 0.5, 1, 0.5, true, CycleMethod.NO_CYCLE,
                    new Stop(0.7, Color.web("Pink", 0.5)),
                    new Stop(1, Color.web("white", 0.5)));
            Rectangle background = new Rectangle(250, 50, gradient);
            background.setArcHeight(20);
            background.setArcWidth(20);
            Text text = new Text(name);
            text.setFont(FONT);
            text.fillProperty().bind(
                    Bindings.when(hoverProperty()).then(Color.WHITE).otherwise(Color.BLACK)
            );
            setOnMouseClicked(e -> action.run());
            getChildren().addAll(background, text);
        }
    }
}

/**
 * A prompt to get a player's name.
 */
class EnterName extends Pane {
    private final TextField textField;

    EnterName() {
        setId("enterName");
        Text prompt = new Text("Enter your name:");
        prompt.setFont(Font.font("Arial Black", FontWeight.BOLD, 18));
        textField = new TextField();
        prompt.setFont(Font.font("Arial Black", 16));
        textField.setTranslateY(30);
        textField.setOnAction(actionEvent -> {
            EmojiApp.recordPlayerScore(textField.getText());
        });
        getChildren().addAll(prompt, textField);
        setTranslateX(400);
        setTranslateY(350);
    }
}

/**
 * Storage for high scores.
 */
class HighScores implements Serializable {
    private final ArrayList<Score> highScores;

    /**
     * Creates instance of type HighScores.
     */
    HighScores() {
        highScores = new ArrayList<>();
    }

    /**
     * Adds a score to the high score list.
     *
     * @param score player score as Score
     */
    public void addScore(final Score score) {
        highScores.add(score);
    }

    /**
     * Returns a list of high scores.
     *
     * @return high scores as ArrayList of String
     */
    public ArrayList<Score> getScores() {
        return highScores;
    }

    /**
     * Prints all scores.
     */
    public void printScores() {
        for (Score score : highScores) {
            System.out.println("Score for " + score.name() + ": "
                    + score.score() + " points in  "
                    + score.time() + " seconds");
        }
    }
}
