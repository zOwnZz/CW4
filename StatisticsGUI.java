import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.geometry.Pos;
import javafx.scene.text.TextAlignment;
import javafx.animation.FadeTransition;
import javafx.util.Duration;
import javafx.animation.TranslateTransition;
import javafx.animation.ParallelTransition;
import javafx.animation.PauseTransition;

/**
 * A GUI component that displays various statistics.
 * It uses animations to transition between different statistics.
 */
public class StatisticsGUI extends BaseGUI {
    private Controller controller;
    private Text statLabel;
    private int currentStatIndex = 0;
    private String[] statistics = new String[4];
    boolean coolingOff = false;

    /**
     * Constructor for initializing the GUI with a controller.
     *
     * @param controller     The main controller handling the business logic.
     * @param controllerGUI  The GUI controller for managing GUI components.
     */
    public StatisticsGUI(Controller controller, ControllerGUI controllerGUI) {
        super(controller, controllerGUI);
        this.controller = controller;
        populateStatistics();
    }

    /**
     * Populates the statistics array with formatted strings.
     * Each string contains a statistic description and its value, separated by a newline character.
     */
    private void populateStatistics() {
        statistics[0] = "Total Deaths:\n" + controller.calculateTotalDeaths();
        statistics[1] = "Average Total Cases Per Borough:\n" + controller.calculateAverageTotalCases();
        statistics[2] = "Average Parks GMR:\n" + controller.calculateAverageParksGMR();
        statistics[3] = "Average Transit GMR:\n" + controller.calculateAverageTransitGMR();
    }

    /**
     * Creates and returns the main scene for the statistics GUI.
     *
     * @return The constructed scene with all GUI components.
     */
    @Override
    public Scene getScene() {
        BorderPane root = getRoot();

        // Initialize the statistics label with the first statistic.
        statLabel = new Text(statistics[currentStatIndex]);
        statLabel.setStyle("-fx-font-size: 30px;");
        statLabel.setWrappingWidth(275);
        statLabel.setTextAlignment(TextAlignment.CENTER);

        // Back button to navigate to the previous statistic.
        Button backButton = createNavigationButton("<", () -> transitionBackward());

        // Forward button to navigate to the next statistic.
        Button forwardButton = createNavigationButton(">", () -> transitionForward());

        // Navigation box containing back and forward buttons along with the statistics text.
        HBox navigationBox = new HBox(90, backButton, statLabel, forwardButton);
        navigationBox.setAlignment(Pos.CENTER);
        root.setCenter(navigationBox);

        return new Scene(root, winWidth, winHeight);
    }

    /**
     * Creates a navigation button with specified text and action.
     *
     * @param buttonText The text to be displayed on the button.
     * @param action     The action to perform when the button is pressed.
     * @return The constructed Button instance.
     */
    private Button createNavigationButton(String buttonText, Runnable action) {
        Button button = new Button(buttonText);
        button.setOnAction(e -> {
            if (!coolingOff) {
                action.run();
                // Disable button temporarily to prevent rapid clicks
                button.setDisable(true);
                coolingOff = true;

                PauseTransition pause = new PauseTransition(Duration.seconds(0.5));
                pause.setOnFinished(f -> {
                    button.setDisable(false);
                    coolingOff = false;
                });

                pause.play();
            }
        });
        return button;
    }

    /**
     * Transitions the statistic text forward, moving to the next statistic.
     */
    private void transitionForward() {
        currentStatIndex = (currentStatIndex + 1) % statistics.length;
        executeTransition(50, -50);
    }

    /**
     * Transitions the statistic text backward, moving to the previous statistic.
     */
    private void transitionBackward() {
        currentStatIndex = (currentStatIndex - 1 + statistics.length) % statistics.length;
        executeTransition(-50, 50);
    }

    /**
     * Executes the transition animation for the statistics text, involving both a fade out and a slide.
     * This creates a smooth transition effect when navigating between statistics.
     *
     * @param moveOutByX The x offset to move the current text out by.
     * @param moveInByX  The x offset to move the new text in by.
     */
    private void executeTransition(double moveOutByX, double moveInByX) {
        // Fade out animation: gradually decreases the opacity of the text to 0.
        FadeTransition fadeOut = new FadeTransition(Duration.millis(250), statLabel);
        fadeOut.setToValue(0);

        // Slide animation: moves the text horizontally.
        TranslateTransition moveOut = new TranslateTransition(Duration.millis(250), statLabel);
        moveOut.setByX(moveOutByX);

        // Combines fade out and slide animations to play them in parallel
        ParallelTransition fadeAndMoveOut = new ParallelTransition(statLabel, fadeOut, moveOut);
        fadeAndMoveOut.setOnFinished(e -> updateAndAnimateTextIn(moveInByX)); // Callback to run after animations complete.

        fadeAndMoveOut.play(); // Starts the transition animation.
    }

    /**
     * Updates the statistic text and initiates the 'fade in' and slide-in animations for the new text.
     * This method is called after the current text has faded out and moved out of view.
     *
     * @param moveInByX The x offset to move the new text in by, completing the transition.
     */
    private void updateAndAnimateTextIn(double moveInByX) {
        // Update text and reset visibility and position for the incoming animation.
        statLabel.setText(statistics[currentStatIndex]);
        statLabel.setOpacity(0); // Initially invisible for fade in.
        statLabel.setTranslateX(moveInByX); // Starts off-screen or at initial position for slide in.

        // Fade in animation: gradually increases the opacity of the text from 0 to 1.
        FadeTransition fadeIn = new FadeTransition(Duration.millis(250), statLabel);
        fadeIn.setFromValue(0);
        fadeIn.setToValue(1);

        // Slide-in animation: moves the text horizontally back to its original position.
        TranslateTransition moveIn = new TranslateTransition(Duration.millis(250), statLabel);
        moveIn.setByX(-moveInByX);

        // Combines fade in and slide-in animations to play them in parallel, creating a smooth entry for the new text.
        ParallelTransition fadeInWithMoveIn = new ParallelTransition(statLabel, fadeIn, moveIn);
        fadeInWithMoveIn.play(); // Initiates the animations to display the new text.
    }
}
