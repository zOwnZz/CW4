import javafx.scene.Scene;
import javafx.scene.layout.VBox;

public class ChallengeGUI extends BaseGUI {
    private int WIN_WIDTH, WIN_HEIGHT;
    private Controller controller;

    public ChallengeGUI(int width, int height, Controller controller){
        WIN_WIDTH = width;
        WIN_HEIGHT = height;
        this.controller = controller;
    }
    public Scene getScene(){
        VBox root = new VBox();
        return new Scene(root, WIN_WIDTH, WIN_HEIGHT);
    }
}
