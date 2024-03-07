import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class ControllerGUI extends Application {


    @Override
    public void start(Stage primaryStage) {
        Controller controller = new Controller();
        BaseGUI challenge = new ChallengeGUI(controller);

        // Get different scenes
        Scene scene = challenge.getScene();

        // Set the scene on the stage
        primaryStage.setScene(scene);

        // Set the title of the stage (window)
        primaryStage.setTitle("COVID DATA");

        // Show the stage
        primaryStage.show();
    }

    public static void main(String[] args) {
        // Launch the JavaFX application
        launch(args);
    }
}
