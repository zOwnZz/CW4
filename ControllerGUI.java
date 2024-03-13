import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class ControllerGUI extends Application {

    @Override
    public void start(Stage primaryStage) {
        Controller controller = new Controller();
        BaseGUI challenge = new ChallengeGUI(controller);
        BaseGUI map = new MapGUI(controller);


        // Get all scenes for the project
        Scene scene = map.getScene();
        scene.getStylesheets().add("mapStyle.css");

        // Set the scene on the stage
        primaryStage.setScene(scene);

        // Set the title of the stage (window)
        primaryStage.setTitle("COVID DATA");
        primaryStage.getIcons().add(new Image("Coronavirus._SARS-CoV-2.png"));

        // Show the stage
        primaryStage.show();
    }

    public static void main(String[] args) {
        // Launch the JavaFX application
        launch(args);
    }
}
