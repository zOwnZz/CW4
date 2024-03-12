import javafx.application.Application;
import javafx.stage.Stage;

public class ControllerGUI extends Application {

    // Array with all GUI objects
    private BaseGUI[] panels = new BaseGUI[4];
    private Stage primaryStage;
    // Counter which scene is currently displayed
    private int counter;
    private Boolean ifAvailable;

    @Override
    public void start(Stage primaryStage) {
        ifAvailable = false;
        counter = 0;
        this.primaryStage = primaryStage;
        Controller controller = new Controller();

        BaseGUI welcome = new WelcomeGUI(controller, this);
        BaseGUI challenge = new ChallengeGUI(controller, this);
        BaseGUI statistics = new StatisticsGUI(controller, this);
        BaseGUI map = new MapGUI(controller, this);

        panels[0] = welcome;
        panels[1] = map;
        panels[2] = statistics;
        panels[3] = challenge;

        // Set the scene on the stage
        primaryStage.setScene(welcome.getScene());

        // Set the title of the stage (window)
        primaryStage.setTitle("COVID DATA");

        // Show the stage
        primaryStage.show();
    }

    /**
     * Changes the scene according to the button which was clicked
     * @param ifPositive if the next panel or previous panel button was clicked
     */
    public void changeScene(Boolean ifPositive){
        primaryStage.setScene(panels[nextCounter(ifPositive)].getScene());
    }

    public void setIfAvailableTrue(){
        ifAvailable = true;
    }
    public Boolean getIfAvailable(){
        return ifAvailable;
    }

    /**
     * Open the current scene again, so that all new data is fetched
     */
    public void reloadScene(){
        primaryStage.setScene(panels[counter].getScene());
    }

    /**
     * Calculate the next index of the displayed panel
     * @param ifPositive Should the index move forward or backwards
     * @return counter, index of the scene we want to display
     */
    private int nextCounter(Boolean ifPositive){
        if(ifPositive){
            counter = (counter + 1) % 4;
        } else {
            if(--counter == -1){
                counter = 3;
            }
        }
        return counter;
    }

    public static void main(String[] args) {
        // Launch the JavaFX application
        launch(args);
    }
}
