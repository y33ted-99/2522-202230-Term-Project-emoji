package ca.bcit.comp2522.termproject.emoji;

import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.TextField;
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
import javafx.scene.text.TextAlignment;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Objects;

/**
 * Represents the player.
 *
 * @author Terence Grigoruk
 * @author Brian Mak
 * @version Fall 2022
 */
public class MainMenu extends VBox {
    private static final Text GAME_TITLE  = new Text("Welcome to\nLa Chatroom!");
    private static final Text GAME_INSTRUCTIONS  = new Text(
            "How long can you survive in this social media heckscape?\n"
            + "Pop bubbles to get items that remove letters,\n"
            + "Spell words to remove letters and get massive points!"
    );
    private static final Font BUTTON_FONT = Font.font("Arial", FontWeight.BOLD, 25);
    private static final int SCORES_TO_DISPLAY = 5;
    private final Pane scoreBoard;
    private final HighScores highScores;

    static {
        GAME_TITLE.setFont(Font.loadFont(
                Objects.requireNonNull(EmojiApp.class.getResource("Babelgamee.ttf")).toExternalForm(), 60));
        GAME_TITLE.setFill(Color.MEDIUMPURPLE);
        GAME_TITLE.setTextAlignment(TextAlignment.CENTER);
        GAME_INSTRUCTIONS.setFont(Font.font("Arial", 13));
        GAME_INSTRUCTIONS.setTextAlignment(TextAlignment.CENTER);
    }
    /**
     * Creates instance of type MainMenu.
     */
    public MainMenu() {
        setSpacing(20);
        setTranslateY(PlayArea.getMarginY() * 1.25);
        scoreBoard = new ScoreBoard();
        highScores = loadHighScores();
        updateScoreBoard();
        getChildren().addAll(GAME_TITLE, GAME_INSTRUCTIONS,
                new MenuButton("START", EmojiApp::startGameRound),
                scoreBoard,
                new MenuButton("QUIT", Platform::exit));
        setAlignment(Pos.CENTER);
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
            System.out.println("No past high scores to load - new file will be created.");
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
        updateScoreBoard();
        EmojiApp.returnToMainMenu();
    }

    /*
     * Updates the scoreboard with the most recent 5 scores
     */
    private void updateScoreBoard() {
        scoreBoard.getChildren().clear();
        Text title = new Text("HIGH SCORES");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        title.setUnderline(true);
        setMargin(title, new Insets(10));
        scoreBoard.getChildren().add(title);
        ArrayList<Score> scores = highScores.getScores();
        Collections.sort(scores);
        if (scores.size() == 0) {
            Text noScoresText = new Text("(No scores yet!)");
            scoreBoard.getChildren().add(noScoresText);
        } else {
            for (int i = 0; i < Math.min(scores.size(), SCORES_TO_DISPLAY); i++) {
                Score score = scores.get(i);
                Text scoreText = new Text(score.name() + " ➔ "
                        + score.score() + " points in "
                        + score.time() + "s");
                scoreText.setFont(Font.font("Arial", FontWeight.BOLD, 16));
                scoreText.setFill(new Color(0.25, 0.25, 0.25, 0.9));
                setMargin(scoreText, new Insets(5));
                scoreBoard.getChildren().add(scoreText);
            }
        }
        setTranslateX((EmojiApp.APP_WIDTH
                - Math.max(GAME_TITLE.getBoundsInParent().getWidth(), scoreBoard.getBoundsInParent().getWidth()))
                / 2);
    }

    /*
     * A ScoreBoard that displays player scores.
     */
    private static class ScoreBoard extends VBox {
        ScoreBoard() {
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
            text.setFont(BUTTON_FONT);
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
