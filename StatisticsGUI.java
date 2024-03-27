import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.text.TextAlignment;
import javafx.scene.layout.Pane;
import javafx.animation.FadeTransition;
import javafx.util.Duration;
import javafx.scene.layout.StackPane;
import javafx.animation.TranslateTransition;
import javafx.animation.ParallelTransition;
import javafx.animation.PauseTransition;
import java.util.HashMap;
import java.util.ArrayList;



public class StatisticsGUI extends BaseGUI {
    private Controller controller;
    private Text statLabel;
    private int currentStatIndex = 0;
    private String[] statistics = new String[4];
    boolean coolingOff = false;

    public StatisticsGUI(Controller controller, ControllerGUI controllerGUI){
        super(controller, controllerGUI);
        this.controller = controller;
        populateStatistics();
    }

    private void populateStatistics() {
        statistics[0] = "Total Deaths: " + controller.calculateTotalDeaths();
        statistics[1] = "Average Total Cases Per Borough: " + controller.calculateAverageTotalCases();
        statistics[2] = "Average Parks GMR: " + controller.calculateAverageParksGMR();
        statistics[3] = "Average Transit GMR: " + controller.calculateAverageTransitGMR();
    }

    @Override
    public Scene getScene() {
        BorderPane root = getRoot();

        statLabel = new Text(statistics[currentStatIndex]);// Default to the first statistic
        statLabel.setStyle("-fx-font-size: 30px;");
        statLabel.setWrappingWidth(275); // Sets the wrapping width to a fixed value
        statLabel.setTextAlignment(TextAlignment.CENTER); // Ensure the text is centered

        Button backButton = new Button("<");
        backButton.setOnAction(e -> {
            if(!coolingOff) {
                currentStatIndex = (currentStatIndex - 1 + statistics.length) % statistics.length;
                transitionBackward();

                backButton.setDisable(true);
                coolingOff = true;

                PauseTransition pause = new PauseTransition(Duration.seconds(0.5));
                pause.setOnFinished(f -> {
                    backButton.setDisable(false);
                    coolingOff = false;
                });

                pause.play();
            }
        });

        Button forwardButton = new Button(">");
        forwardButton.setOnAction(e -> {
            if(!coolingOff) {
                currentStatIndex = (currentStatIndex + 1) % statistics.length;
                transitionForward();

                forwardButton.setDisable(true);
                coolingOff = true;

                PauseTransition pause = new PauseTransition(Duration.seconds(0.5));
                pause.setOnFinished(f -> {
                    forwardButton.setDisable(false);
                    coolingOff = false;
                });

                pause.play();
            }
        });

        // Combine navigation buttons and stat display into a single HBox
        HBox navigationBox = new HBox(90, backButton, statLabel, forwardButton); // Simplified the setup
        navigationBox.setAlignment(Pos.CENTER);

        root.setCenter(navigationBox);

        return new Scene(root, winWidth, winHeight);
    }

    private void transitionForward() {
        // Execute transition: Move current text to the right and fade out, then bring new text from the left
        executeTransition(50, -50);
    }

    private void transitionBackward() {
        // Execute transition: Move current text to the left and fade out, then bring new text from the right
        executeTransition(-50, 50);
    }

    private void executeTransition(double moveOutByX, double moveInByX) {
        FadeTransition fadeOut = new FadeTransition(Duration.millis(250), statLabel);
        fadeOut.setToValue(0);

        TranslateTransition moveOut = new TranslateTransition(Duration.millis(250), statLabel);
        moveOut.setByX(moveOutByX);

        ParallelTransition fadeAndMoveOut = new ParallelTransition(statLabel, fadeOut, moveOut);
        fadeAndMoveOut.setOnFinished(e -> {
            // Once the fade out and move are complete, update the text and reset position
            statLabel.setText(statistics[currentStatIndex]);
            statLabel.setOpacity(0); // Make the label invisible as it resets to start position
            statLabel.setTranslateX(moveInByX); // Position the label at the starting point of the incoming animation

            FadeTransition fadeIn = new FadeTransition(Duration.millis(250), statLabel);
            fadeIn.setFromValue(0);
            fadeIn.setToValue(1);

            TranslateTransition moveIn = new TranslateTransition(Duration.millis(250), statLabel);
            moveIn.setByX(-moveInByX); // Move the label back to its central position

            ParallelTransition fadeInWithMoveIn = new ParallelTransition(statLabel, fadeIn, moveIn);
            fadeInWithMoveIn.play();
        });

        fadeAndMoveOut.play();
    }
}
