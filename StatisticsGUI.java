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

    public StatisticsGUI(Controller controller, ControllerGUI controllerGUI){
        super(controller, controllerGUI);
        this.controller = controller;
        populateStatistics();
    }

    private void populateStatistics() {

        statistics[0] = "Average Parks GMR: " + controller.calculateAverageParksGMR();
        statistics[1] = "Average Transit GMR: " + controller.calculateAverageTransitGMR();
        statistics[2] = "Total Deaths: " + controller.calculateTotalDeaths();
        statistics[3] = "Average Total Cases: " + controller.calculateAverageTotalCases();
    }

    @Override
    public Scene getScene() {
    BorderPane root = getRoot();

    statLabel = new Text(statistics[currentStatIndex]); 
    statLabel.setWrappingWidth(200); 
    statLabel.setTextAlignment(TextAlignment.CENTER); 

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

    
    HBox backButtonBox = new HBox(backButton);
    backButtonBox.setAlignment(Pos.CENTER_LEFT);

    HBox statLabelBox = new HBox(statLabel);
    statLabelBox.setAlignment(Pos.CENTER);

    HBox forwardButtonBox = new HBox(forwardButton);
    forwardButtonBox.setAlignment(Pos.CENTER_RIGHT);


    HBox navigationBox = new HBox(backButtonBox, statLabelBox, forwardButtonBox);
    navigationBox.setAlignment(Pos.CENTER);
    navigationBox.setSpacing(10); 

    root.setCenter(navigationBox);

    return new Scene(root, WIN_WIDTH, WIN_HEIGHT);
    }

    private void updateStatLabel() {
        statLabel.setText(statistics[currentStatIndex]);
        statLabel.setWrappingWidth(200); 
        statLabel.setTextAlignment(TextAlignment.CENTER); 

    }

}
