import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class MainGUI extends Application {

    @Override
    public void start(Stage primaryStage) {
        // Create a root node.
        StackPane root = new StackPane();
        
        // Create a scene with the root node with dimensions 300x250 (width x height)
        Scene scene = new Scene(root, 500, 350);
        
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