import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

public class ChallengeGUI extends BaseGUI {
    private Controller controller;

    public ChallengeGUI(Controller controller){
        this.controller = controller;
    }
    public Scene getScene(){
        BorderPane root = getRoot();

        //root.setCenter(...); - Code for the components inside the panel

        return new Scene(root, WIN_WIDTH, WIN_HEIGHT);
    }
}
