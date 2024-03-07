import java.util.ArrayList;

public class Controller {
    private ArrayList<CovidData> data;

    public Controller(){
        CovidDataLoader loader = new CovidDataLoader();
        data = loader.load();
    }
    // method returning specific data for scenes
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
}
