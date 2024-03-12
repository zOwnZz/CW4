import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.text.TextAlignment;

public class StatisticsGUI extends BaseGUI {
    private Controller controller;
    private Text statLabel;
    private int currentStatIndex = 0;
    private String[] statistics = new String[4]; 

    public StatisticsGUI(Controller controller) {
        this.controller = controller;
        populateStatistics();
    }

    private void populateStatistics() {
        
        statistics[0] = ": " + controller.calculateAverageStat("");
        statistics[1] = ": " + controller.calculateAverageStat("");
        statistics[2] = "Total Deaths: " + controller.calculateTotalDeaths();
        statistics[3] = "Average Total Cases: " + controller.calculateAverageCases();
    }

    @Override
    public Scene getScene() {
        BorderPane root = getRoot();

        statLabel = new Text(statistics[currentStatIndex]); // Default to the first statistic

        Button backButton = new Button("<");
        backButton.setOnAction(e -> {
            currentStatIndex = (currentStatIndex - 1 + statistics.length) % statistics.length;
            updateStatLabel();
        });

        Button forwardButton = new Button(">");
        forwardButton.setOnAction(e -> {
            currentStatIndex = (currentStatIndex + 1) % statistics.length;
            updateStatLabel();
        });

        HBox navigationBox = new HBox(10, backButton, statLabel, forwardButton);
        navigationBox.setAlignment(Pos.CENTER);

        root.setCenter(navigationBox);

        return new Scene(root, WIN_WIDTH, WIN_HEIGHT);
    }

    private void updateStatLabel() {
    statLabel.setText(statistics[currentStatIndex]);
    statLabel.setWrappingWidth(200); // Sets the wrapping width to a fixed value
    statLabel.setTextAlignment(TextAlignment.CENTER); // Ensure the text is centered
    
    }

}
