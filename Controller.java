import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;

public class Controller {
    private ArrayList<CovidData> data;
    private LocalDate startDate, endDate;

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

    public double calculateAverageCases() {
        return 0;
    }

    public int calculateTotalDeaths() {
        return 0;
    }

    public double calculateAverageStat(String measure) {
        return 0;
    }

}
