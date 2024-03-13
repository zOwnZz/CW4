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
    private Controller controller;
    private ChallengeControl simulator;
    private Boolean firstChart;
    private String cssForBars(String color){
        return "-fx-background-color: #392467; " + // Background color
               "-fx-accent: " + color + "; " + // Progress color
               "-fx-text-box-border: #392467; " + // Border color
               "-fx-control-inner-background: #FFFFEC; " + // Background color of the progress bar itself
               "-fx-border-width: 1px; " + // Border width
               "-fx-border-radius: 0px; " + // Border radius
               "-fx-padding: 1px;"; // Padding
    }

    public ChallengeGUI(Controller controller, ControllerGUI controllerGUI){
        super(controller, controllerGUI);
        this.controller = controller;
    }



    @Override
    public Scene getScene(){
        BorderPane root = getRoot();

        firstChart = true;

        HashMap<String, ArrayList<CovidData>> boroughAndData = controller.boroughAndData();

        VBox center = new VBox();
        HBox boroughSelectionPane = new HBox();
        HBox simulationButtons = new HBox();
        HBox upperPane = new HBox();

        ComboBox<String> selection = new ComboBox<>();
        Label selectionText = new Label("Select borough: ");
        Button startSimulation = new Button("Play!");
        Button stopSimulation = new Button("Stop!");

        startSimulation.setOnAction(event -> {
            simulator.playSimulation();
            startSimulation.setDisable(true);
        });
        stopSimulation.setOnAction(event -> {
            simulator.stopSimulation();
            startSimulation.setDisable(false);
        });

        selection.getItems().addAll(boroughAndData.keySet());

        boroughSelectionPane.getChildren().addAll(selectionText, selection);
        simulationButtons.getChildren().addAll(startSimulation, stopSimulation);
        upperPane.getChildren().addAll(boroughSelectionPane, simulationButtons);

        boroughSelectionPane.setSpacing(20);
        simulationButtons.setSpacing(40);
        upperPane.setSpacing(50);
        upperPane.setAlignment(Pos.CENTER);

        VBox healthBarsPane = new VBox();

        HBox retAndRecPane = new HBox(new Label("Retail and recreation: "));
        HBox grocAndPhaPane = new HBox(new Label("Grocery and pharmacy: "));
        HBox parksPane = new HBox(new Label("Parks: "));
        HBox transitPane = new HBox(new Label("Transit stations: "));
        HBox workplacePane = new HBox(new Label("Workplaces: "));
        HBox residentialPane = new HBox(new Label("Residential area: "));

        ArrayList<HBox> healthBarsPanes = new ArrayList<>(Arrays.asList(retAndRecPane, grocAndPhaPane, parksPane, transitPane, workplacePane, residentialPane));

        Label retAndRecLabel = new Label("");
        Label grocAndPhaLabel = new Label("");
        Label parksLabel = new Label("");
        Label transitLabel = new Label("");
        Label workplaceLabel = new Label("");
        Label residentialLabel = new Label("");

        ArrayList<Label> labelsPopulation = new ArrayList<>(Arrays.asList(retAndRecLabel, grocAndPhaLabel, parksLabel, transitLabel, workplaceLabel, residentialLabel));

        ProgressBar retAndRecBar = new ProgressBar(); retAndRecPane.getChildren().addAll(retAndRecBar, retAndRecLabel);
        ProgressBar grocAndPhaBar = new ProgressBar(); grocAndPhaPane.getChildren().addAll(grocAndPhaBar, grocAndPhaLabel);
        ProgressBar parksBar = new ProgressBar(); parksPane.getChildren().addAll(parksBar, parksLabel);
        ProgressBar transitBar = new ProgressBar(); transitPane.getChildren().addAll(transitBar, transitLabel);
        ProgressBar workplaceBar = new ProgressBar(); workplacePane.getChildren().addAll(workplaceBar, workplaceLabel);
        ProgressBar residentialBar = new ProgressBar(); residentialPane.getChildren().addAll(residentialBar, residentialLabel);

        retAndRecBar.setStyle(cssForBars("#9195F6"));
        grocAndPhaBar.setStyle(cssForBars("#BFEA7C"));
        parksBar.setStyle(cssForBars("#416D19"));
        transitBar.setStyle(cssForBars("#9B4444"));
        workplaceBar.setStyle(cssForBars("#FC6736"));
        residentialBar.setStyle(cssForBars("#DC84F3"));

        ArrayList<ProgressBar> healthBars = new ArrayList<>(Arrays.asList(retAndRecBar, grocAndPhaBar, parksBar, transitBar, workplaceBar, residentialBar));

        for(HBox pane : healthBarsPanes){
            pane.setAlignment(Pos.CENTER);
            healthBarsPane.getChildren().addAll(pane);
            pane.setSpacing(10);
        }

        for(ProgressBar bar : healthBars){
            bar.setProgress((double) 1 /6);
            bar.setPrefWidth(600);
        }

        healthBarsPane.setSpacing(10);
        healthBarsPane.setAlignment(Pos.CENTER);

        selection.setOnAction(event -> {
            String selectedOption = selection.getSelectionModel().getSelectedItem();
            for(ProgressBar bar : healthBars){
                bar.setProgress((double) 1/6);
                bar.setPrefWidth(600);
            }
            if(!firstChart) {
                simulator.disableSimulation();
                center.getChildren().remove(center.getChildren().size() - 1);
            }
            simulator = new ChallengeControl(controller, healthBars, selectedOption, labelsPopulation);
            LineChart<Number, Number> lineChart =  simulator.createChart();
            firstChart = false;
            for(Label label : labelsPopulation)
                label.setText(String.valueOf(simulator.getInitialNumber()));
            center.getChildren().add(lineChart);
            startSimulation.setDisable(false);
        });

        center.getChildren().addAll(upperPane, healthBarsPane);
        center.setAlignment(Pos.CENTER);
        center.setSpacing(15);

        root.setCenter(center);

        return new Scene(root, WIN_WIDTH, WIN_HEIGHT);
    }
}
