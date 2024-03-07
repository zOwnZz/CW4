import javafx.geometry.Pos;
import javafx.scene.Scene;
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

    /*
    Create simulator with a change of the borough. As a parameter provide a borough's name. In the hash map provide
    simulator with arrayList. Save this arrayList there. do the same functionality as in game of life to run the
    simulation. Create slider to adjust velocity of the simulation. Reset progress bars when the borough is changed.
    Disable changing borough when simulation is running. Stop simulation with the button.
     */

    public ChallengeGUI(Controller controller){
        this.controller = controller;
        this.simulator = new ChallengeControl(controller);
    }
    public Scene getScene(){
        BorderPane root = getRoot();

        HashMap<String, ArrayList<CovidData>> boroughAndData = controller.boroughAndData();

        VBox center = new VBox();
        HBox boroughSelectionPane = new HBox();
        HBox simulationButtons = new HBox();
        HBox upperPane = new HBox();

        ComboBox<String> selection = new ComboBox<>();
        Label selectionText = new Label("Select borough: ");
        Button startSimulation = new Button("Play!");
        Button stopSimulation = new Button("Stop!");


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

        ProgressBar retAndRecBar = new ProgressBar(); retAndRecPane.getChildren().add(retAndRecBar);
        ProgressBar grocAndPhaBar = new ProgressBar(); grocAndPhaPane.getChildren().add(grocAndPhaBar);
        ProgressBar parksBar = new ProgressBar(); parksPane.getChildren().add(parksBar);
        ProgressBar transitBar = new ProgressBar(); transitPane.getChildren().add(transitBar);
        ProgressBar workplaceBar = new ProgressBar(); workplacePane.getChildren().add(workplaceBar);
        ProgressBar residentialBar = new ProgressBar(); residentialPane.getChildren().add(residentialBar);

        ArrayList<ProgressBar> healthBars = new ArrayList<>(Arrays.asList(retAndRecBar, grocAndPhaBar, parksBar, transitBar, workplaceBar, residentialBar));

        for(HBox pane : healthBarsPanes){
            pane.setAlignment(Pos.CENTER);
            healthBarsPane.getChildren().addAll(pane);
        }

        for(ProgressBar bar : healthBars){
            bar.setProgress(1);
            bar.setPrefWidth(200);
        }

        healthBarsPane.setSpacing(10);
        healthBarsPane.setAlignment(Pos.CENTER);

        /*HashMap<String, ArrayList<CovidData>> boroughAndData = controller.boroughAndData();
        for(CovidData covid : boroughAndData.get("Enfield")){
            box.getChildren().add(new Label(covid.getDate()));
        }*/

        center.getChildren().addAll(upperPane, healthBarsPane);
        center.setAlignment(Pos.CENTER);
        center.setSpacing(30);

        root.setCenter(center);

        return new Scene(root, WIN_WIDTH, WIN_HEIGHT);
    }
}
