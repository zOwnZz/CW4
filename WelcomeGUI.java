import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.geometry.Pos;
public class WelcomeGUI extends BaseGUI {
    private Controller controller;
    public WelcomeGUI(Controller controller, ControllerGUI controllerGUI){
        super(controller, controllerGUI);
        this.controller = controller;

    }
    @Override
    public Scene getScene() {
        BorderPane root = getRoot();

        VBox center = new VBox();

        Label InfoLabel = new Label("This application visualises the impact of Covid-19 in London.");

        // Create a layout to stack the labels on top of each other
        StackPane stackPane = new StackPane();
        // Add the labels to the layout
        stackPane.getChildren().addAll( InfoLabel);

        // Create a scene with the layout
        Scene scene = new Scene(stackPane, 300, 200);
        center.getChildren().addAll(InfoLabel);
        center.setAlignment(Pos.CENTER);
        center.setSpacing(30);

        root.setCenter(center);
        return new Scene(root, WIN_WIDTH, WIN_HEIGHT);
    }
}
