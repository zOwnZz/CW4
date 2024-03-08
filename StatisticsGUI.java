import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class StatisticsGUI extends BaseGUI {
    private Controller controller;

    public StatisticsGUI(Controller controller) {
        this.controller = controller;
    }

    @Override
    public Scene getScene() {
        VBox layout = new VBox(10);
        Label titleLabel = new Label("COVID-19 Statistics");
        layout.getChildren().add(titleLabel);

        // Add more UI elements to display the statistics

        Scene scene = new Scene(getRoot(), WIN_WIDTH, WIN_HEIGHT);
        // Add layout to the scene
        return scene;
    }

    // In StatisticsGUI, add a method to update the statistics display
    public void updateStatisticsDisplay(String fromDate, String toDate) {
        double totalDeaths = controller.calculateTotalDeaths(fromDate, toDate);
        double averageCases = controller.calculateAverageTotalCases(fromDate, toDate);

        // Update the GUI components with these values
    }

}
