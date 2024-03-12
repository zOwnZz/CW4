import java.util.ArrayList;
import java.util.HashMap;
import java.util.stream.Collectors;

public class Controller {
    private ArrayList<CovidData> data;

    public Controller(){
        CovidDataLoader loader = new CovidDataLoader();
        data = loader.load();
    }

    public HashMap<String, ArrayList<CovidData>> boroughAndData() {
        HashMap<String, ArrayList<CovidData>> boroughAndData = new HashMap<>();
        for(CovidData covid : data){
            if(boroughAndData.containsKey(covid.getBorough()))
                boroughAndData.get(covid.getBorough()).add(covid);
            else {
                boroughAndData.put(covid.getBorough(), new ArrayList<CovidData>());
                boroughAndData.get(covid.getBorough()).add(covid);
            }
        }
        return boroughAndData;
    }
 
    public ArrayList<String> getBoroughs(){
        ArrayList<String> processedData = new ArrayList<>();
        for(int i = 0; i < 20; i++){
            processedData.add(data.get(i).getBorough());
        }
        return processedData;
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
