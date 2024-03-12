import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.geometry.Pos;

public class MapGUI extends BaseGUI{
    private Controller controller;
    public MapGUI(Controller controller, ControllerGUI controllerGUI){
        super(controller, controllerGUI);
        this.controller = controller;

    }
    @Override
    public Scene getScene() {
        BorderPane root = getRoot();

        VBox center = new VBox();

        Label InfoLabel = new Label("This is map GUI.");

        // Create a layout to stack the labels on top of each other
        StackPane stackPane = new StackPane();
        // Add the labels to the layout
        stackPane.getChildren().addAll( InfoLabel);

        // Create a scene with the layout
        Scene scene = new Scene(stackPane, 300, 200);
        center.getChildren().addAll(InfoLabel);
        center.setAlignment(Pos.CENTER);
        center.setSpacing(30);
        //return new Scene(root, WIN_WIDTH, WIN_HEIGHT);
        //return scene;
        root.setCenter(center);
        return new Scene(root, WIN_WIDTH, WIN_HEIGHT);
    }
}
