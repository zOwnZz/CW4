import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;


public class ControllerGUI extends Application {
    
    @Override
    public void start(Stage primaryStage) {
        Controller controller = new Controller();
        StatisticsGUI statisticsGUI = new StatisticsGUI(controller);
        Scene scene = statisticsGUI.getScene();

        primaryStage.setScene(scene);
        primaryStage.setTitle("COVID-19 Statistics Panel");
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}









  
