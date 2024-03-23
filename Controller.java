import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;

public class Controller {
    private ArrayList<CovidData> data;
    private LocalDate startDate, endDate;
    private HashMap<String, ArrayList<CovidData>> boroughAndData;

    public Controller(){
        CovidDataLoader loader = new CovidDataLoader();
        data = loader.load();
        startDate = getMinDate();
        endDate = getMaxDate();
    }

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

    public void updateData(LocalDate[] datesSelected){
        startDate = datesSelected[0];
        endDate = datesSelected[1];
    }

    public LocalDate getMinDate(){
        return data.stream()
                .map(CovidData::getDate)
                .map(LocalDate::parse)
                .min(Comparator.naturalOrder())
                .orElse(null);
    }

    public LocalDate getMaxDate(){
        return data.stream()
                .map(CovidData::getDate)
                .map(LocalDate::parse)
                .max(Comparator.naturalOrder())
                .orElse(null);
    }

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




    public int calculateTotalDeaths(HashMap<String, ArrayList<CovidData>> boroughAndData) {
    int sumTotalDeaths = 0;
    for (ArrayList<CovidData> dataList : boroughAndData.values()) {
        for (CovidData data : dataList) {
            sumTotalDeaths += data.getTotalDeaths();
        }
    }
    return sumTotalDeaths;
    }


 
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


}

    
