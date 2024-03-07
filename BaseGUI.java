import javafx.scene.Scene;
import javafx.scene.layout.VBox;

public abstract class BaseGUI {

    public abstract Scene getScene();
    private VBox menuTab(){
        return new VBox();
    }

}
