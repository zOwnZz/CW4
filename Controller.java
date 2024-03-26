import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;

public class Controller {
    private final ArrayList<CovidData> data;
    private LocalDate startDate, endDate;
    private final LocalDate minDate, maxDate;

    /**
     * Constructor creating all necessary objects and setting minimum and maximum date
     */
    public Controller(){
        CovidDataLoader loader = new CovidDataLoader();
        data = loader.load();
        minDate = getMinDate();
        maxDate = getMaxDate();
        startDate = minDate;
        endDate = maxDate;
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
     * @return the minimum date in the whole data
     */
    public LocalDate getMinDate(){
        return data.stream()
        .map(CovidData::getDate)
        .map(LocalDate::parse)
        .min(Comparator.naturalOrder())
        .orElse(null);
    }

    /**
     * @return the maximum date in the whole data
     */
    public LocalDate getMaxDate(){
        return data.stream()
        .map(CovidData::getDate)
        .map(LocalDate::parse)
        .max(Comparator.naturalOrder())
        .orElse(null);
    }

    /**
     * @return the start date chosen by user
     */
    public LocalDate getStartDate(){
        return startDate;
    }

    /**
     * @return end date chosen by user
     */
    public LocalDate getEndDate(){
        return endDate;
    }

    /**
     * Calculates the total difference in deaths across all boroughs between the earliest and latest dates within a specified date range.
     * This method iterates over the CovidData entries, tracking the minimum and maximum death counts for each borough within the date range.
     * The total deaths difference is the sum of differences between maximum and minimum deaths for each borough.
     * 
     * @return The total deaths difference across all boroughs within the specified date range.
     */
    public int calculateTotalDeaths() {
        // Initialization of variables to track the earliest and latest dates and death counts per borough
        int totalDeathsDifference = 0;
        HashMap<String, LocalDate> minDates = new HashMap<>();
        HashMap<String, LocalDate> maxDates = new HashMap<>();
        HashMap<String, Integer> minDeaths = new HashMap<>();
        HashMap<String, Integer> maxDeaths = new HashMap<>();

        // Loop through each CovidData entry to update minimum and maximum dates and deaths per borough
        for (CovidData covid : data) {
            LocalDate date = covid.getDateFormat();
            if ((startDate != null && endDate != null) && (date.isAfter(startDate) && date.isBefore(endDate))) {
                String borough = covid.getBorough();

                // Update earliest date and death count per borough
                if (!minDates.containsKey(borough) || date.isBefore(minDates.get(borough))) {
                    minDates.put(borough, date);
                    minDeaths.put(borough, covid.getTotalDeaths());
                }

                // Update latest date and death count per borough
                if (!maxDates.containsKey(borough) || date.isAfter(maxDates.get(borough))) {
                    maxDates.put(borough, date);
                    maxDeaths.put(borough, covid.getTotalDeaths());
                }
            }
        }

        // Calculate and return the total deaths difference
        for (String borough : minDeaths.keySet()) {
            totalDeathsDifference += maxDeaths.get(borough) - minDeaths.get(borough);
        }

        return totalDeathsDifference;
    }

    /**
     * Calculates the average number of new Covid cases across all boroughs within a specified date range.
     * The method sums up new cases within the date range and divides by the total number of boroughs to find the average.
     * 
     * @return The average number of new Covid cases across all boroughs.
     */
    public double calculateAverageTotalCases() {
        int totalNewCases = 0;

        // Loop through each CovidData entry to sum up new cases within the date range
        for (CovidData covid : data) {
            if (covid.getDateFormat().isAfter(startDate) && covid.getDateFormat().isBefore(endDate)) {
                totalNewCases += covid.getNewCases();
            }
        }

        // Calculate and return the average new cases per borough
        return totalNewCases / 32.0;
    }

    /**
     * Calculates the average GMR value for parks across all boroughs within a specified date range.
     * It aggregates GMR values for parks per borough and divides by the number of days to find the average, then averages these across all boroughs.
     * 
     * @return The average GMR value for parks across all boroughs.
     */
    public double calculateAverageParksGMR() {
        double sumOfAverages = 0.0;
        HashMap<String, Integer> boroughSum = new HashMap<>();
        HashMap<String, Integer> boroughDays = new HashMap<>();

        // Loop through each CovidData object in the data collection.
        for (CovidData covid : data) {
            // Check if the date of the current CovidData object is within the specified date range.
            if (covid.getDateFormat().isAfter(startDate) && covid.getDateFormat().isBefore(endDate)) {
                // Retrieve the borough name from the current CovidData object.
                String borough = covid.getBorough();
                // Retrieve the GMR value for parks from the current CovidData object.
                int currentParksGMR = covid.getParksGMR();
                // Update the total GMR value for the current borough by adding the current GMR value.
                // If the borough does not exist in the map, it initializes the sum with 0 and then adds the current GMR value.
                boroughSum.put(borough, boroughSum.getOrDefault(borough, 0) + currentParksGMR);
                // Increment the count of days with data for the current borough by 1.
                // If the borough does not exist in the map, it initializes the count with 0 and then increments by 1.
                boroughDays.put(borough, boroughDays.getOrDefault(borough, 0) + 1);
            }
        }

        // Loop through each borough present in the boroughSum map.
        for (String borough : boroughSum.keySet()) {
            // Calculate the average GMR for parks for the current borough by dividing the total GMR value by the number of days with data.
            double average = boroughSum.get(borough) / (double) boroughDays.get(borough);
            // Add the calculated average GMR for the current borough to the sum of averages.
            sumOfAverages += average;
        }
        
        return sumOfAverages / 32.0;
    }

    /**
     * Calculates the average GMR value for transit stations across all boroughs within a specified date range.
     * It aggregates GMR values for transit stations per borough and divides by the number of days to find the average, then averages these across all boroughs.
     * 
     * @return The average GMR value for transit stations across all boroughs.
     */
    public double calculateAverageTransitGMR() {
        double sumOfAverages = 0.0;
        HashMap<String, Integer> boroughSum = new HashMap<>();
        HashMap<String, Integer> boroughDays = new HashMap<>();

        for (CovidData covid : data) {
            if (covid.getDateFormat().isAfter(startDate) && covid.getDateFormat().isBefore(endDate)) {
                String borough = covid.getBorough();
                int currentTransitGMR = covid.getTransitGMR();
                boroughSum.put(borough, boroughSum.getOrDefault(borough, 0) + currentTransitGMR);
                boroughDays.put(borough, boroughDays.getOrDefault(borough, 0) + 1);
            }
        }

        for (String borough : boroughSum.keySet()) {
            double average = boroughSum.get(borough) / (double) boroughDays.get(borough);
            sumOfAverages += average;
        }

        return sumOfAverages / 32.0;
    }

    /**
     * So our program does not calculate min date multiple times, it does it once, saves and returns here
     * @return minimum date in dataset
     */
    public LocalDate getMinDateCalculated(){
        return minDate;
    }

    /**
     * So our program does not calculate min date multiple times, it does it once, saves and returns here
     * @return minimum date in dataset
     */
    public LocalDate getMaxDateCalculated(){
        return maxDate;
    }

}
