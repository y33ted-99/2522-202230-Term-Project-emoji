package ca.bcit.comp2522.termproject.emoji;

import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

/**
 * Represents the player.
 *
 * @author Terence Grigoruk
 * @author Brian Mak
 * @version Fall 2022
 */
public class MainMenu extends VBox {

    /**
     * Creates instance of type MainMenu.
     */
    public MainMenu() {
        setSpacing(20);
        setTranslateX(380);
        setTranslateY(380);
        getChildren().addAll(
                new MenuItem("START", EmojiApp::startGame),
                new MenuItem("HIGHSCORE", () -> {
                }),
                new MenuItem("QUIT", Platform::exit));
    }

    /*
     * A menu item.
     */
    private static class MenuItem extends StackPane {
        MenuItem(final String name, final Runnable action) {
            LinearGradient gradient = new LinearGradient(
                    0.5, 0.5, 1, 0.5, true, CycleMethod.NO_CYCLE,
                    new Stop(0.8, Color.web("Pink", 0.5)),
                    new Stop(1, Color.web("white", 0.5)));
            Rectangle background = new Rectangle(250, 30, gradient);


            Text text = new Text(name);
            text.setFont(Font.font(25));
            text.fillProperty().bind(
                    Bindings.when(hoverProperty()).then(Color.WHITE).otherwise(Color.BLACK)
            );

            setOnMouseClicked(e -> action.run());
            getChildren().addAll(background, text);
        }

    }
}
