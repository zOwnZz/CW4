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
                if (covid.getDateFormat().isAfter(startDate) && covid.getDateFormat().isBefore(endDate)) {
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
     * @param boroughAndData data
     * @return number of average total cases
     */
    public double calculateAverageTotalCases(HashMap<String, ArrayList<CovidData>> boroughAndData) {
        long sumTotalCases = 0;
        int count = 0;
        for (ArrayList<CovidData> dataList : boroughAndData.values()) {
            for (CovidData data : dataList) {
                sumTotalCases += data.getTotalCases();
                count++;
            }
        }
        return count > 0 ? Double.parseDouble(String.format("%.3f", (double) sumTotalCases / count)) : 0;
    }

    /**
     * @param boroughAndData data
     * @return total number of deaths
     */
    public int calculateTotalDeaths(HashMap<String, ArrayList<CovidData>> boroughAndData) {
        int sumTotalDeaths = 0;
        for (ArrayList<CovidData> dataList : boroughAndData.values()) {
            for (CovidData data : dataList) {
                sumTotalDeaths += data.getNewDeaths();
            }
        }
        return sumTotalDeaths;
    }

    /**
     * @param boroughAndData data
     * @return the average of GMR in parks
     */
    public double calculateAverageParksGMR(HashMap<String, ArrayList<CovidData>> boroughAndData) {
        long sumParksGMR = 0;
        int count = 0;
        for (ArrayList<CovidData> dataList : boroughAndData.values()) {
            for (CovidData data : dataList) {
                sumParksGMR += data.getParksGMR();
                count++;
            }
        }
        return count > 0 ? Double.parseDouble(String.format("%.3f", (double) sumParksGMR / count)) : 0;
    }

    /**
     * @param boroughAndData data
     * @return the average GMR in transit
     */
    public double calculateAverageTransitGMR(HashMap<String, ArrayList<CovidData>> boroughAndData) {
        long sumTransitGMR = 0;
        int count = 0;
        for (ArrayList<CovidData> dataList : boroughAndData.values()) {
            for (CovidData data : dataList) {
                sumTransitGMR += data.getTransitGMR();
                count++;
            }
        }
        return count > 0 ? Double.parseDouble(String.format("%.3f", (double) sumTransitGMR / count)) : 0;
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
