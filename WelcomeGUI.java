import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.geometry.Pos;
import javafx.geometry.Insets;
import javafx.scene.layout.Region;

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
        Label welcomeLabel = new Label("Welcome!");
        welcomeLabel.setStyle("-fx-font-size: 50px;");
        Label infoLabel = new Label("This application visualises the impact of Covid-19 in London.");
        Label enjoyLabel = new Label("Enjoy!");
        Label rulesLabel = new Label("This application visualises the impact of Covid-19 in London.");
        Label info2Label = new Label("To continue please select the dates which you want to view data from and then click submit.");

        center.setAlignment(Pos.CENTER);

        VBox.setMargin(welcomeLabel, new Insets(-150, 0, 0, 0));
        Region gapRegion = new Region();
        gapRegion.setPrefHeight(100);
        center.setSpacing(10);
        center.getChildren().addAll(welcomeLabel,gapRegion, infoLabel,info2Label, enjoyLabel);
        root.setCenter(center);
        return new Scene(root, winWidth, winHeight);
    }
}
