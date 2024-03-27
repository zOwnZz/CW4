import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.geometry.Pos;
import javafx.geometry.Insets;
import javafx.scene.layout.Region;

public class WelcomeGUI extends BaseGUI {
    private Controller controller;
    private Boolean ifFirst = true;
    /**
     * Constructor assigning all the neccessary variables
     * @param controller keeps and manages data
     * @param controllerGUI keeps track of creating and changing scenes
     */
    public WelcomeGUI(Controller controller, ControllerGUI controllerGUI){
        super(controller, controllerGUI);
        this.controller = controller;
        ifFirst = false;
    }


    public WelcomeGUI(Controller controller, ControllerGUI controllerGUI, Boolean ifFirst){
        super(controller, controllerGUI, ifFirst);
        this.controller = controller;

    }

    @Override
    public Scene getScene() {
        BorderPane root = getRoot();



        // Create main components of the scene
        VBox center = new VBox();
        Label welcomeLabel = new Label("Welcome!");
        welcomeLabel.setStyle("-fx-font-size: 50px;");
        Label infoLabel = new Label("This application visualises the impact of Covid-19 in London.");
        Label enjoyLabel = new Label("Enjoy!");
        Label dateLabel = new Label("The current date you have selected is from " + controller.getStartDate() + " to " + controller.getEndDate());
        Label noDateLabel = new Label("You have not selected a date yet");
        Label info2Label = new Label("To continue please select the dates which you want to view data from and then click submit. ");
        Label info3Label = new Label("You can then use the buttons below to navigate through the different panles which include a visual representation of COVID death rates,\n"   + " ".repeat(55)+ "some statistics during the time frame you have selected and a graph simulation ");

        center.setAlignment(Pos.CENTER);

        // Setting margin to adjust welcomeLabel
        VBox.setMargin(welcomeLabel, new Insets(-150, 0, 0, 0));

        // Creating a gap region for spacing
        Region gapRegion = new Region();
        gapRegion.setPrefHeight(100);

        center.setSpacing(10);
        center.getChildren().addAll(welcomeLabel, gapRegion, infoLabel, info2Label,info3Label, enjoyLabel, noDateLabel);

        //User is told what date range they have selected after clicking submit
        if (ifFirst == false) {
            center.getChildren().remove(noDateLabel);
            center.getChildren().add(dateLabel);
        }

        if(controller.getStartDate() == null){
            center.getChildren().add(noDateLabel);
            center.getChildren().remove(dateLabel);

        }

        root.setCenter(center);
        return new Scene(root, winWidth, winHeight);
    }
}
