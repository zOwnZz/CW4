import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

public abstract class BaseGUI {
    protected int WIN_WIDTH = 1000;
    protected int WIN_HEIGHT = 600;

    /**
     * Abstract method that returns a scene. Needs to be implemented in each GUI class
     */
    public abstract Scene getScene();

    /**
     * The basic scene containing menu tab and buttons at the bottom
     */
    protected BorderPane getRoot(){
        BorderPane root = new BorderPane();

        root.setBottom(buttons());
        root.setTop(menuTab());

        return root;
    }
    private VBox menuTab(){
        VBox menuTab = new VBox();
        // Code for menu tab
        return menuTab;
    }
    private VBox buttons() {
        VBox buttons = new VBox();
        // Code for buttons
        return buttons;
    }
}
