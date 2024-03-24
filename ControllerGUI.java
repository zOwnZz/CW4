import javafx.application.Application;
import javafx.stage.Stage;

public class ControllerGUI extends Application {

    // -------------- Attributes --------------

    private Stage primaryStage;
    private int counter;
    private Boolean ifAvailable;
    Controller controller = new Controller();

    // -------------- Methods --------------

    @Override
    public void start(Stage primaryStage) {
        ifAvailable = false;
        counter = 0;
        this.primaryStage = primaryStage;

        // First welcome scene loaded and displayed
        BaseGUI welcome2 = new WelcomeGUI(controller, this, true);
        primaryStage.widthProperty().addListener((observable, oldValue, newValue) -> welcome2.setButtonsSpacing((double) newValue));
        primaryStage.setScene(welcome2.getScene());
        primaryStage.setTitle("Welcome!");

        // Show the stage
        primaryStage.show();
    }

    /**
     * Changes the scene according to the button which was clicked
     * @param ifPositive if the next or previous button was clicked
     */
    public void changeScene(Boolean ifPositive){
        double width = primaryStage.getWidth();
        setScene(nextCounter(ifPositive));
        primaryStage.setWidth(width);
    }

    /**
     * Open the current scene again, so that all new data is fetched.
     * Save the size of the window and set it after reloading.
     */
    public void reloadScene(){
        double width = primaryStage.getWidth();
        double height = primaryStage.getHeight();

        setScene(counter);

        primaryStage.setWidth(width);
        primaryStage.setHeight(height);
    }

    // -------------- Auxiliary methods --------------

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

    /**
     * Depending on the provided parameters show the appropriate scene.
     * @param nextCounterInt the index of the panel to load.
     */
    private void setScene(int nextCounterInt){
        // In each case create a new objects of GUI class, set the listener for changes in width, set scene and title
        switch (nextCounterInt){
            case 0:
                BaseGUI welcome2 = new WelcomeGUI(controller, this);
                primaryStage.widthProperty().addListener((observable, oldValue, newValue) -> welcome2.setButtonsSpacing((double) newValue));
                primaryStage.setScene(welcome2.getScene());
                primaryStage.setTitle("Welcome!");
                break;
            case 1:
                BaseGUI map2 = new MapGUI(controller, this);
                primaryStage.widthProperty().addListener((observable, oldValue, newValue) -> map2.setButtonsSpacing((double) newValue));
                primaryStage.setScene(map2.getScene());
                primaryStage.setTitle("Map!");
                break;
            case 2:
                BaseGUI statistics2 = new StatisticsGUI(controller, this);
                primaryStage.widthProperty().addListener((observable, oldValue, newValue) -> statistics2.setButtonsSpacing((double) newValue));
                primaryStage.setScene(statistics2.getScene());
                primaryStage.setTitle("Statistics!");
                break;
            case 3:
                BaseGUI challenge2 = new ChallengeGUI(controller, this);
                primaryStage.widthProperty().addListener((observable, oldValue, newValue) -> challenge2.setButtonsSpacing((double) newValue));
                primaryStage.setScene(challenge2.getScene());
                primaryStage.setTitle("Simulation!");
                break;
        }
    }

    // -------------- Getters and Setters --------------

    /**
     * Set if the buttons to switch panel are available
     */
    public void setIfAvailableTrue(){
        ifAvailable = true;
    }

    /**
     * @return if buttons to switch panels should be available
     */
    public Boolean getIfAvailable(){
        return ifAvailable;
    }

    /**
     * @return the current width of the window
     */
    public double getStageWidth(){
        return primaryStage.getWidth();
    }

    /**
     * @return the current height of the window
     */
    public double getStageHeight(){
        return primaryStage.getHeight();
    }

    // -------------- Main method --------------

    public static void main(String[] args) {
        // Launch the JavaFX application
        launch(args);
    }
}
