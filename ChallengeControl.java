import javafx.application.Platform;
import javafx.scene.chart.NumberAxis;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.chart.XYChart;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import javafx.scene.chart.LineChart;

public class ChallengeControl {

    private Controller controller;
    private final int initialNumber = 1000;
    private int retAndRecPeople;
    private int grocAndPhaPeople;
    private int parksPeople;
    private int transitPeople;
    private int workplacePeople;
    private int residentialPeople;
    private ArrayList<Integer> people;
    private ArrayList<ProgressBar> healthBars;
    private String borough;
    private ArrayList<CovidData> data;
    private Boolean runningSimulation = false;
    ArrayList<Label> labelBars;
    private XYChart.Series<Number, Number> newInfected;
    private XYChart.Series<Number, Number> newDeaths;
    private int lastIndex;

    public ChallengeControl(Controller controller, ArrayList<ProgressBar> bars, String borough, ArrayList<Label> labelBars){
        people = new ArrayList<>(Arrays.asList(retAndRecPeople, grocAndPhaPeople, parksPeople, transitPeople, workplacePeople, residentialPeople));
        this.controller = controller;
        this.healthBars = bars;
        this.borough = borough;
        data = sorter(controller.boroughAndData().get(borough));
        Collections.fill(people, initialNumber);
        this.labelBars = labelBars;
        lastIndex = 0;
    }

    private ArrayList<CovidData> sorter(ArrayList<CovidData> unsortedData){
        ArrayList<CovidData> sorted = new ArrayList<>(unsortedData);
        sorted.sort((p1, p2) -> Math.toIntExact(p1.getDateFormat().toEpochDay() - p2.getDateFormat().toEpochDay()));
        return sorted;
    }

    public LineChart<Number, Number> createChart(){
        NumberAxis xAxis = new NumberAxis();
        NumberAxis yAxis = new NumberAxis();

        LineChart<Number, Number> lineChart = new LineChart<>(xAxis, yAxis);
        lineChart.setPrefSize(200, 250);

        newDeaths = new XYChart.Series<>();
        newDeaths.setName("New deaths");

        newInfected = new XYChart.Series<>();
        newInfected.setName("New infections");

        lineChart.getData().addAll(newDeaths, newInfected);

        lineChart.setTitle("New deaths and infections each day");

        return lineChart;
    }

    private int getNumberPercent(int percent, int number) {
        return number + (number * percent / 1000);
    }

    private int getTotalNumberOfPeople(){
        int total = 0;
        for(int population : people){
            total += population;
        }
        return total;
    }

    public void disableSimulation(){
        runningSimulation = false;
    }

    public void playSimulation(){
        runningSimulation = true;
        new Thread(() -> {
            int i = lastIndex; // Current object in arrayList
            while(runningSimulation && data.size() > i){
                people.set(0, getNumberPercent(data.get(i).getGroceryPharmacyGMR(), people.get(0)));
                people.set(1, getNumberPercent(data.get(i).getRetailRecreationGMR(), people.get(1)));
                people.set(2, getNumberPercent(data.get(i).getParksGMR(), people.get(2)));
                people.set(3, getNumberPercent(data.get(i).getTransitGMR(), people.get(3)));
                people.set(4, getNumberPercent(data.get(i).getWorkplacesGMR(), people.get(4)));
                people.set(5, getNumberPercent(data.get(i).getResidentialGMR(), people.get(5)));


                int currentTotal = 0;
                for(int population : people){
                    currentTotal += population;
                }
                for(int y = 0; y < people.size(); y++){
                    people.set(y, people.get(y) * initialNumber * people.size() / currentTotal);
                    healthBars.get(y).setProgress(Math.min(((double) people.get(y) / (double) getTotalNumberOfPeople()), 1));
                }

                final int currentIndex = i;
                Platform.runLater(() -> {
                    for (int x = 0; x < people.size(); x++) {
                        labelBars.get(x).setText(String.valueOf(people.get(x)));
                    }
                    newInfected.getData().add(new XYChart.Data<>( currentIndex, data.get(currentIndex).getNewCases()));
                    newDeaths.getData().add(new XYChart.Data<>( currentIndex, data.get(currentIndex).getNewDeaths()));
                    newInfected.getNode().setStyle("-fx-stroke: red;");
                    newDeaths.getNode().setStyle("-fx-stroke: black;");
                });
                try {
                    Thread.sleep(600);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                i++;
                lastIndex++;
            }

        }).start();
    }

    public int getInitialNumber(){
        return initialNumber;
    }
    public void stopSimulation(){
        runningSimulation = false;
    }

}
