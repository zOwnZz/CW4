import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Controller {
    private ArrayList<CovidData> data;

    public Controller() {
        CovidDataLoader loader = new CovidDataLoader();
        data = loader.load();
    }

    public double calculateTotalDeaths(String fromDate, String toDate) {
        return data.stream()
                .filter(covidData -> covidData.getDate().compareTo(fromDate) >= 0 && covidData.getDate().compareTo(toDate) <= 0)
                .mapToInt(CovidData::getTotalDeaths)
                .sum();
    }

    public double calculateAverageTotalCases(String fromDate, String toDate) {
        List<CovidData> filteredData = data.stream()
                .filter(covidData -> covidData.getDate().compareTo(fromDate) >= 0 && covidData.getDate().compareTo(toDate) <= 0)
                .collect(Collectors.toList());

        return filteredData.stream()
                .mapToInt(CovidData::getTotalCases)
                .average()
                .orElse(0.0);
    }

    // Implement other statistics methods here
}
