import javafx.application.Platform;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.chart.XYChart;
import java.util.ArrayList;

import javafx.scene.chart.LineChart;

public class ChallengeControl {

    private final double initialNumber = 100;
    private final ArrayList<ProgressBar> healthBars;
    private final ArrayList<CovidData> data;
    private Boolean runningSimulation = false;
    ArrayList<Label> labelBars;
    private XYChart.Series<String, Number> newInfected;
    private XYChart.Series<String, Number> newDeaths;
    private int lastIndex;
    ArrayList<Double> people;

    /**
     * Constructor of the class taking care of simulation played on the simulation panel
     * @param controller the main source of modified data
     * @param bars progress bars created in the ChallengeGUI
     * @param borough borough chosen by the user
     * @param labelBars labels with number of population
     */
    public ChallengeControl(Controller controller, ArrayList<ProgressBar> bars, String borough, ArrayList<Label> labelBars){
        // Set all attributes to provided ones or default
        people = new ArrayList<>();
        for(int i = 0; i < 6; i++){
            people.add(initialNumber);
        }
        this.healthBars = bars;
        data = sorter(controller.boroughAndData().get(borough));
        this.labelBars = labelBars;
        lastIndex = 0;
    }

    /**
     * Sort the given data by date
     * @param unsortedData Data in an array list we want to sort
     * @return sorted sata
     */
    private ArrayList<CovidData> sorter(ArrayList<CovidData> unsortedData){
        ArrayList<CovidData> sorted = new ArrayList<>(unsortedData);
        sorted.sort((p1, p2) -> Math.toIntExact(p1.getDateFormat().toEpochDay() - p2.getDateFormat().toEpochDay()));
        return sorted;
    }

    /**
     * Create line chart with the number of new infections and deaths
     * @return deaths and infections line chart
     */
    public LineChart<String, Number> createChart(){
        // Create x and y-axis on the chart
        CategoryAxis xAxis = new CategoryAxis();
        NumberAxis yAxis = new NumberAxis();

        // Create and style the line chart
        LineChart<String, Number> lineChart = new LineChart<>(xAxis, yAxis);
        lineChart.setPrefSize(200, 250);

        // Create data series for deaths and infections
        newDeaths = new XYChart.Series<>();
        newDeaths.setName("New deaths");

        newInfected = new XYChart.Series<>();
        newInfected.setName("New infections");

        lineChart.getData().addAll(newDeaths, newInfected);

        lineChart.setTitle("New deaths and infections each day");

        return lineChart;
    }

    /**
     * Stop the simulation
     */
    public void disableSimulation(){
        runningSimulation = false;
    }

    /**
     * Start the simulation
     */
    public void playSimulation(){
        runningSimulation = true;

        // Play the simulation in the background
        new Thread(() -> {
            int i = lastIndex; // Current object in arrayList
            while(runningSimulation && data.size() > i){
                // Stop simulation after 364 days
                if(i >= 364){
                    runningSimulation = false;
                }

                // Calculate the number of people in each area on the given date
                people.set(0, calculatePeople(data.get(i).getGroceryPharmacyGMR()));
                people.set(1, calculatePeople(data.get(i).getRetailRecreationGMR()));
                people.set(2, calculatePeople(data.get(i).getParksGMR()));
                people.set(3, calculatePeople(data.get(i).getTransitGMR()));
                people.set(4, calculatePeople(data.get(i).getWorkplacesGMR()));
                people.set(5, calculatePeople(data.get(i).getResidentialGMR()));

                // Calculate the sum of all people
                double sum = people.stream().reduce((double) 0, Double::sum);

                final int currentIndex = i;
                Platform.runLater(() -> {
                    // Update labels with the nnumber of people in each area
                    for (int x = 0; x < people.size(); x++) {
                        labelBars.get(x).setText(String.valueOf(Math.round(people.get(x))));
                    }

                    // Update progress bars with the number of people in respect to the sum of them
                    for(int x = 0; x < 6; x++){
                        healthBars.get(x).setProgress(people.get(x) / sum);
                    }

                    // Add new infections to the series on the line chart
                    newInfected.getData().add(new XYChart.Data<>(data.get(currentIndex).getDateFormat().getDayOfMonth() +"/"+ data.get(currentIndex).getDateFormat().getMonthValue(), data.get(currentIndex).getNewCases()));
                    newDeaths.getData().add(new XYChart.Data<>( data.get(currentIndex).getDateFormat().getDayOfMonth() +"/"+ data.get(currentIndex).getDateFormat().getMonthValue(), data.get(currentIndex).getNewDeaths()));
                    newInfected.getNode().setStyle("-fx-stroke: red;");
                    newDeaths.getNode().setStyle("-fx-stroke: black;");
                });
                try {
                    // Wait for 600ms before simulating next day
                    Thread.sleep(600);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                i++;
                lastIndex++;
            }

        }).start();
    }

    /**
     * Calculate the number of people with the given percent
     * @param percent GMR data, change in percent of people in given area
     * @return number of people after calculating the percent
     */
    private double calculatePeople(int percent){
        return initialNumber + initialNumber * percent * 0.01;
    }

    /**
     * @return the initial number of people (pre-pandemic situation)
     */
    public double getInitialNumber(){
        return initialNumber;
    }

    /**
     * Stop the simulation
     */
    public void stopSimulation(){
        runningSimulation = false;
    }

}
