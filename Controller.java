import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import java.util.ArrayList;
import javafx.scene.layout.Pane;
import java.util.Iterator;
import javafx.scene.layout.HBox;
import javafx.scene.control.Button;

import javafx.event.*;

public class ControllerGUI extends Application {
    
    private Scene[] pagess = new Scene[4]; 
    public int currentPageIndex = 0;
    Controller controller = new Controller();
    private Stage primaryStage;
    BaseGUI welcome = new WelcomeGUI(controller, buttons());
        BaseGUI challenge = new ChallengeGUI(controller, buttons());
    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        Controller controller = new Controller();
        HBox buttons = buttons();
        
        BaseGUI welcome = new WelcomeGUI(controller, buttons());
        BaseGUI challenge = new ChallengeGUI(controller, buttons());
        
        pagess[0] = welcome.getScene();
        pagess[1] = challenge.getScene();
        
        // Get all scenes for the project
        Scene scene = welcome.getScene();
        
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
    
     private void updateScene() {
         
    
    }
    
    private HBox buttons() {
        HBox buttons = new HBox();
        // Code for buttons
        Button backButton = new Button(">");
        Button nextButton = new Button("<");
        buttons.setSpacing(945);
        
        backButton.setOnAction(new EventHandler<ActionEvent>() {
            
            public void handle(ActionEvent event) {
                // Define the action to be performed when next button is clicked
                primaryStage.setScene(pagess[0]);
                
            primaryStage.show();
                System.out.println("Back Button clicked");
            }
        });

        nextButton.setOnAction(new EventHandler<ActionEvent>() {
            
            public void handle(ActionEvent event) {
                // Define the action to be performed when back button is clicked
                primaryStage.setScene(pagess[1]);
                System.out.println("Next Button clicked");
            }
        });
        
        buttons.getChildren().addAll(nextButton, backButton);
        return buttons;
        
        
    }
}
