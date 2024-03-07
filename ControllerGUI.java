import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class ControllerGUI extends Application {
    private int WIN_WIDTH = 1000;
    private int WIN_HEIGHT = 600;

    @Override
    public void start(Stage primaryStage) {
        Controller controller = new Controller();
        ChallengeGUI chellange = new ChallengeGUI(WIN_WIDTH, WIN_HEIGHT, controller);
        // Create a root node.
        StackPane root = new StackPane();

        // Create a scene with the root node with dimensions 300x250 (width x height)
        Scene scene = chellange.getScene();

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
