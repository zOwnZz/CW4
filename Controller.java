import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.time.temporal.ChronoUnit;

public class Controller {
    private final ArrayList<CovidData> data;
    private LocalDate startDate, endDate;
    private final LocalDate minDate, maxDate;
    private final int numberOfBoroughs; 
    private final HashMap<String, ArrayList<CovidData>> boroughAndData;

    /**
     * Constructor initializes the controller, loads data, and sets up the minimum and maximum dates.
     */
    public Controller(){
        CovidDataLoader loader = new CovidDataLoader();
        data = loader.load();
        minDate = getMinDate();
        maxDate = getMaxDate();
        startDate = minDate;
        endDate = maxDate;
        boroughAndData = boroughAndData(); 
        numberOfBoroughs = boroughAndData.size(); // Initialize the count of boroughs
    }

    /**
     * Create a hash map within a given period of time with keys being the borough names and values the covidData object.
     * @return this hashMap
     */
    public HashMap<String, ArrayList<CovidData>> boroughAndData() {
        HashMap<String, ArrayList<CovidData>> boroughAndData = new HashMap<>();
        for(CovidData covid : data) {
            if(startDate != null && endDate != null) {
                if (!covid.getDateFormat().isBefore(startDate) && !covid.getDateFormat().isAfter(endDate)) {

                    if (boroughAndData.containsKey(covid.getBorough()))
                        boroughAndData.get(covid.getBorough()).add(covid);
                    else {
                        boroughAndData.put(covid.getBorough(), new ArrayList<>());
                        boroughAndData.get(covid.getBorough()).add(covid);
                    }
                }
            }
        }
        return boroughAndData;
    }

    /**
     * Update data if user selected a new one
     * @param datesSelected dates selected by user
     */
    public void updateData(LocalDate[] datesSelected) {
        // Check if dates are correct (shouldn't be equal to null)
        if (startDate != null) {
            startDate = datesSelected[0];
            endDate = datesSelected[1];
        } else {
            startDate = minDate;
            endDate = maxDate;
        }
    }

    /**
     * @return The earliest date in the dataset.
     */
    public LocalDate getMinDate(){
        return data.stream().map(CovidData::getDate).map(LocalDate::parse).min(Comparator.naturalOrder()).orElse(null);
    }

    /**
     * @return The latest date in the dataset.
     */
    public LocalDate getMaxDate(){
        return data.stream().map(CovidData::getDate).map(LocalDate::parse).max(Comparator.naturalOrder()).orElse(null);
    }

    /**
     * @return The start date currently selected for filtering data.
     */
    public LocalDate getStartDate(){
        return startDate;
    }

    /**
     * @return The end date currently selected for filtering data.
     */
    public LocalDate getEndDate(){
        return endDate;
    }

    /**
     * Calculates the total number of new deaths across all boroughs within the specified date range.
     * @return The sum of new deaths across all boroughs within the date range.
     */
    public int calculateTotalDeaths() {
        int totalDeaths = 0;

        for (ArrayList<CovidData> boroughData : boroughAndData.values()) {
            for (CovidData data : boroughData) {
                totalDeaths += data.getNewDeaths();
            }
        }

        return totalDeaths;
    }

    /**
     * Calculates the average number of new Covid cases per borough over the specified date range.
     * @return The average number of new Covid cases per borough.
     */
    public double calculateAverageTotalCases() {
        long sumNewCases = 0;

        for (ArrayList<CovidData> boroughData : boroughAndData.values()) {
            for (CovidData data : boroughData) {
                sumNewCases += data.getNewCases();
            }
        }

        // Calculate the average cases per borough 
        double average = (double) sumNewCases / numberOfBoroughs;

        // Format the result to 2 decimal places and return
        return Double.parseDouble(String.format("%.2f", average));
    }

    /**
     * Calculates the average parks GMR value across all boroughs within the specified date range.
     * @return The average parks GMR value per day per borough over the selected date range.
     */
    public double calculateAverageParksGMR() {
        long totalParksGMR = 0;
        int GMRDays = 0; // Tracks days with GMR data

        for (ArrayList<CovidData> boroughData : boroughAndData.values()) {
            for (CovidData data : boroughData) {
                if (data.getParksGMR() != 0) { 
                    totalParksGMR += data.getParksGMR();
                    GMRDays++;
                }
            }
        }

        // Prevent division by zero
        if (GMRDays == 0) return 0.0;

        // Calculate the average Parks GMR per day with valid data across all boroughs
        double averageParksGMR = (double) totalParksGMR / GMRDays / numberOfBoroughs;

        // Format the result to 2 decimal places and return
        return Double.parseDouble(String.format("%.2f", averageParksGMR));
    }

    /**
     * Calculates the average transit GMR value across all boroughs within the specified date range.
     * @return The average transit GMR value per day per borough over the selected date range.
     */
    public double calculateAverageTransitGMR() {
        long totalTransitGMR = 0;
        int GMRDays = 0;

        for (ArrayList<CovidData> boroughData : boroughAndData.values()) {
            for (CovidData data : boroughData) {
                if (data.getTransitGMR() != 0) { 
                    totalTransitGMR += data.getTransitGMR();
                    GMRDays++;
                }
            }
        }

        // Prevent division by zero
        if (GMRDays == 0) return 0.0;

        // Calculate the average Transit GMR per day with valid data across all boroughs
        double averageTransitGMR = (double) totalTransitGMR / GMRDays / numberOfBoroughs;

        // Format the result to 2 decimal places and return
        return Double.parseDouble(String.format("%.2f", averageTransitGMR));
    }

    /**
     * Provides the minimum date in the dataset that has been calculated and stored.
     * @return The minimum date in the dataset.
     */
    public LocalDate getMinDateCalculated(){
        return minDate;
    }

    /**
     * Provides the maximum date in the dataset that has been calculated and stored.
     * @return The maximum date in the dataset.
     */
    public LocalDate getMaxDateCalculated(){
        return maxDate;
    }
}
