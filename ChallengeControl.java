import javafx.application.Platform;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.chart.XYChart;
import java.util.ArrayList;

import javafx.scene.chart.LineChart;

public class ChallengeControl {

    private Controller controller;
    private final double initialNumber = 100;
    private ArrayList<ProgressBar> healthBars;
    private String borough;
    private ArrayList<CovidData> data;
    private Boolean runningSimulation = false;
    ArrayList<Label> labelBars;
    private XYChart.Series<String, Number> newInfected;
    private XYChart.Series<String, Number> newDeaths;
    private int lastIndex;
    ArrayList<Double> people;

    public ChallengeControl(Controller controller, ArrayList<ProgressBar> bars, String borough, ArrayList<Label> labelBars){
//        people = new ArrayList<>(Arrays.asList(retAndRecPeople, grocAndPhaPeople, parksPeople, transitPeople, workplacePeople, residentialPeople));
        people = new ArrayList<>();
        for(int i = 0; i < 6; i++){
            people.add(initialNumber);
        }
        this.controller = controller;
        this.healthBars = bars;
        this.borough = borough;
        data = sorter(controller.boroughAndData().get(borough));
//        Collections.fill(people, initialNumber);
        this.labelBars = labelBars;
        lastIndex = 0;
    }

    private ArrayList<CovidData> sorter(ArrayList<CovidData> unsortedData){
        ArrayList<CovidData> sorted = new ArrayList<>(unsortedData);
        sorted.sort((p1, p2) -> Math.toIntExact(p1.getDateFormat().toEpochDay() - p2.getDateFormat().toEpochDay()));
        return sorted;
    }

    public LineChart<String, Number> createChart(){
        CategoryAxis xAxis = new CategoryAxis();
        NumberAxis yAxis = new NumberAxis();

        LineChart<String, Number> lineChart = new LineChart<>(xAxis, yAxis);
        lineChart.setPrefSize(200, 250);

        newDeaths = new XYChart.Series<>();
        newDeaths.setName("New deaths");

        newInfected = new XYChart.Series<>();
        newInfected.setName("New infections");

        lineChart.getData().addAll(newDeaths, newInfected);

        lineChart.setTitle("New deaths and infections each day");

        return lineChart;
    }

    public void disableSimulation(){
        runningSimulation = false;
    }

    public void playSimulation(){
        runningSimulation = true;
        new Thread(() -> {
            int i = lastIndex; // Current object in arrayList
            while(runningSimulation && data.size() > i){
                if(i >= 364){
                    runningSimulation = false;
                }

                people.set(0, calculatePeople(data.get(i).getGroceryPharmacyGMR()));
                people.set(1, calculatePeople(data.get(i).getRetailRecreationGMR()));
                people.set(2, calculatePeople(data.get(i).getParksGMR()));
                people.set(3, calculatePeople(data.get(i).getTransitGMR()));
                people.set(4, calculatePeople(data.get(i).getWorkplacesGMR()));
                people.set(5, calculatePeople(data.get(i).getResidentialGMR()));

                double sum = people.stream().reduce((double) 0, Double::sum);




                final int currentIndex = i;
                Platform.runLater(() -> {
                    for (int x = 0; x < people.size(); x++) {
                        labelBars.get(x).setText(String.valueOf(Math.round(people.get(x))));
                    }
                    for(int x = 0; x < 6; x++){
                        healthBars.get(x).setProgress(people.get(x) / sum);
                    }
                    newInfected.getData().add(new XYChart.Data<>(data.get(currentIndex).getDateFormat().getDayOfMonth() +"/"+ data.get(currentIndex).getDateFormat().getMonthValue(), data.get(currentIndex).getNewCases()));
                    newDeaths.getData().add(new XYChart.Data<>( data.get(currentIndex).getDateFormat().getDayOfMonth() +"/"+ data.get(currentIndex).getDateFormat().getMonthValue(), data.get(currentIndex).getNewDeaths()));
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
    private double calculatePeople(int percent){
        System.out.println(""+(initialNumber + initialNumber * percent * 0.01));
        return initialNumber + initialNumber * percent * 0.01;
    }

    public double getInitialNumber(){
        return initialNumber;
    }
    public void stopSimulation(){
        runningSimulation = false;
    }

}
