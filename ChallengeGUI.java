import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class ChallengeGUI extends BaseGUI {
    private ChallengeControl simulator;
    private Boolean firstChart;
    Button startSimulation = new Button("Play!");
    Button stopSimulation = new Button("Stop!");

    /**
     * Constructor assigning all the neccessary variables
     * @param controller keeps and manages data
     * @param controllerGUI keeps track of creating and changing scenes
     */
    public ChallengeGUI(Controller controller, ControllerGUI controllerGUI){
        super(controller, controllerGUI);
        disableButtons();
    }

    /**
     * Create all the components in the scene and add it to the main root
     * @return the challenge scene
     */
    @Override
    public Scene getScene(){
        BorderPane root = getRoot();

        // While creating a scene, there is no chart visible
        firstChart = true;

        // Receive a data
        HashMap<String, ArrayList<CovidData>> boroughAndData = controller.boroughAndData();

        // Create main components of the scene
        VBox center = new VBox();
        HBox boroughSelectionPane = new HBox();
        HBox simulationButtons = new HBox();
        HBox upperPane = new HBox();

        // Create borough selector and start stop buttons
        ComboBox<String> selection = new ComboBox<>();
        selection.getItems().addAll(boroughAndData.keySet());
        Label selectionText = new Label("Select borough: ");

        // Set event to start and stop buttons
        startSimulation.setOnAction(event -> {
            simulator.playSimulation();
            startSimulation.setDisable(true);
        });
        stopSimulation.setOnAction(event -> {
            simulator.stopSimulation();
            startSimulation.setDisable(false);
        });

        // Create the layout of components on the screen
        boroughSelectionPane.getChildren().addAll(selectionText, selection);
        boroughSelectionPane.setSpacing(20);

        simulationButtons.getChildren().addAll(startSimulation, stopSimulation);
        simulationButtons.setSpacing(40);

        upperPane.getChildren().addAll(boroughSelectionPane, simulationButtons);
        upperPane.setSpacing(50);
        upperPane.setAlignment(Pos.CENTER);

        // Create main GUI components to handle progress bars
        VBox healthBarsPane = new VBox();

        HBox retAndRecPane = new HBox(new Label("Retail and recreation: "));
        HBox grocAndPhaPane = new HBox(new Label("Grocery and pharmacy: "));
        HBox parksPane = new HBox(new Label("Parks: "));
        HBox transitPane = new HBox(new Label("Transit stations: "));
        HBox workplacePane = new HBox(new Label("Workplaces: "));
        HBox residentialPane = new HBox(new Label("Residential area: "));

        ArrayList<HBox> healthBarsPanes = new ArrayList<>(Arrays.asList(retAndRecPane, grocAndPhaPane, parksPane, transitPane, workplacePane, residentialPane));

        // Labels that show number of people at the end of progress bars
        Label retAndRecLabel = new Label("");
        Label grocAndPhaLabel = new Label("");
        Label parksLabel = new Label("");
        Label transitLabel = new Label("");
        Label workplaceLabel = new Label("");
        Label residentialLabel = new Label("");

        ArrayList<Label> labelsPopulation = new ArrayList<>(Arrays.asList(retAndRecLabel, grocAndPhaLabel, parksLabel, transitLabel, workplaceLabel, residentialLabel));

        // Create progress bars for each category in GMR
        ProgressBar retAndRecBar = new ProgressBar(); retAndRecPane.getChildren().addAll(retAndRecBar, retAndRecLabel);
        ProgressBar grocAndPhaBar = new ProgressBar(); grocAndPhaPane.getChildren().addAll(grocAndPhaBar, grocAndPhaLabel);
        ProgressBar parksBar = new ProgressBar(); parksPane.getChildren().addAll(parksBar, parksLabel);
        ProgressBar transitBar = new ProgressBar(); transitPane.getChildren().addAll(transitBar, transitLabel);
        ProgressBar workplaceBar = new ProgressBar(); workplacePane.getChildren().addAll(workplaceBar, workplaceLabel);
        ProgressBar residentialBar = new ProgressBar(); residentialPane.getChildren().addAll(residentialBar, residentialLabel);

        // Set colors of each progress bar
        retAndRecBar.setStyle(cssForBars("#9195F6"));
        grocAndPhaBar.setStyle(cssForBars("#BFEA7C"));
        parksBar.setStyle(cssForBars("#416D19"));
        transitBar.setStyle(cssForBars("#9B4444"));
        workplaceBar.setStyle(cssForBars("#FC6736"));
        residentialBar.setStyle(cssForBars("#DC84F3"));

        ArrayList<ProgressBar> healthBars = new ArrayList<>(Arrays.asList(retAndRecBar, grocAndPhaBar, parksBar, transitBar, workplaceBar, residentialBar));

        // Style boxes with progress bars, labels and numbers
        for(HBox pane : healthBarsPanes){
            pane.setAlignment(Pos.CENTER);
            healthBarsPane.getChildren().addAll(pane);
            pane.setSpacing(10);
        }

        // Style progress bars
        for(ProgressBar bar : healthBars){
            bar.setProgress((double) 1 /6);
            bar.setPrefWidth(600);
        }

        healthBarsPane.setSpacing(10);
        healthBarsPane.setAlignment(Pos.CENTER);

        // When the borough is selected, get ready to stat the simulation
        selection.setOnAction(event -> {
            // Get the selected borough
            String selectedOption = selection.getSelectionModel().getSelectedItem();

            enableButtons();

            // Set each bar to the default value
            for(ProgressBar bar : healthBars){
                bar.setProgress((double) 1/6);
            }

            // Remove the old chart if there is any (if it is not the first simulation in this scene
            if(!firstChart) {
                simulator.disableSimulation();
                center.getChildren().removeLast();
            }

            // Create new simulation, get and set required values
            simulator = new ChallengeControl(controller, healthBars, selectedOption, labelsPopulation);
            LineChart<String, Number> lineChart =  simulator.createChart();
            firstChart = false;

            // Set population label to be the same everywhere
            for(Label label : labelsPopulation)
                label.setText(String.valueOf(simulator.getInitialNumber()));

            center.getChildren().add(lineChart);
            startSimulation.setDisable(false);
        });

        // Fill the box and style it
        center.getChildren().addAll(upperPane, healthBarsPane);
        center.setAlignment(Pos.CENTER);
        center.setSpacing(15);

        root.setCenter(center);

        return new Scene(root, winWidth, winHeight);
    }

    /**
     * Generating css styling text for buttons
     * @param color the requested color of the button
     * @return String being a css styling
     */
    private String cssForBars(String color){
        return "-fx-background-color: #392467; " + // Background color
                "-fx-accent: " + color + "; " + // Progress color
                "-fx-text-box-border: #392467; " + // Border color
                "-fx-control-inner-background: #FFFFEC; " + // Background color of the progress bar itself
                "-fx-border-width: 1px; " + // Border width
                "-fx-border-radius: 0px; " + // Border radius
                "-fx-padding: 1px;"; // Padding
    }

    public void disableButtons(){
        startSimulation.setDisable(true);
        stopSimulation.setDisable(true);
    }
    public void enableButtons(){
        startSimulation.setDisable(false);
        stopSimulation.setDisable(false);
    }
}
