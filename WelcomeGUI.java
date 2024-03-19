import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.geometry.Pos;
public class WelcomeGUI extends BaseGUI {
    private Controller controller;
    public WelcomeGUI(Controller controller, ControllerGUI controllerGUI){
        super(controller, controllerGUI);
        this.controller = controller;
    }
    public WelcomeGUI(Controller controller, ControllerGUI controllerGUI, Boolean ifFirst){
        super(controller, controllerGUI, ifFirst);
        this.controller = controller;
    }

    @Override
    public Scene getScene() {
        BorderPane root = getRoot();

        VBox center = new VBox();

        Label InfoLabel = new Label("This application visualises the impact of Covid-19 in London.");

        center.getChildren().addAll(InfoLabel);
        center.setAlignment(Pos.CENTER);
        center.setSpacing(30);

        root.setCenter(center);
        return new Scene(root, winWidth, winHeight);
    }
}
