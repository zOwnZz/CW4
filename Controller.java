import java.util.ArrayList;

public class Controller {
    private ArrayList<CovidData> data;

    public Controller(){
        CovidDataLoader loader = new CovidDataLoader();
        data = loader.load();
    }
    // method returning specific data for scenes
}
