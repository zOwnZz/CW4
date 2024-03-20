import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;

import java.time.LocalDate;
import javafx.scene.layout.HBox;

public class BaseGUI {


    protected int WIN_WIDTH = 1000;
    protected int WIN_HEIGHT = 600;
    private Controller controller;
    private LocalDate minDate, maxDate, startDate, endDate;
    private DatePicker startDatePicker, endDatePicker;
    private ControllerGUI controllerGUI;


    public BaseGUI(Controller controller, ControllerGUI controllerGUI){
        this.controller = controller;
        this.controllerGUI = controllerGUI;
        minDate = controller.getMinDate();
        maxDate = controller.getMaxDate();
    }

    /**
     * Method that returns a scene. Should be overriden in each GUI class
     */
    public Scene getScene(){
        return new Scene(new VBox(), WIN_WIDTH, WIN_HEIGHT);
    };

    /**
     * The basic scene containing menu tab and buttons at the bottom
     */
    protected BorderPane getRoot(){
        BorderPane root = new BorderPane();

        root.setBottom(buttons());
        root.setTop(menuTab());

        return root;
    }

    private HBox buttons() {
        HBox buttons = new HBox();

        Button nextButton = new Button(">");
        Button backButton = new Button("<");
        // Make win width dynamic, to be resizable
        buttons.setSpacing(WIN_WIDTH-50);

        backButton.setOnAction(event -> controllerGUI.changeScene(false));

        nextButton.setOnAction(event -> controllerGUI.changeScene(true));

        if(!controllerGUI.getIfAvailable()){
            backButton.setDisable(true);
            nextButton.setDisable(true);
        } else {
            backButton.setDisable(false);
            nextButton.setDisable(false);
        }

        buttons.getChildren().addAll(backButton, nextButton);
        return buttons;
    }

    private HBox menuTab(){
        HBox menuTab = new HBox();

        startDatePicker = new DatePicker();
        startDatePicker.setEditable(false);
        endDatePicker = new DatePicker();
        endDatePicker.setEditable(false);

        endDatePicker.setDayCellFactory(picker -> new DateCell() {
            @Override
            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                setDisable(date.isBefore(minDate) || date.isAfter(maxDate));
            }
        });

        startDatePicker.setDayCellFactory(picker -> new DateCell() {
            @Override
            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                setDisable(date.isBefore(minDate) || date.isAfter(maxDate));
            }
        });

        Button submitButton = new Button("Submit!");
        submitButton.setOnAction(event -> submitDate());

        startDatePicker.setValue(minDate);
        endDatePicker.setValue(maxDate);
        menuTab.getChildren().addAll(startDatePicker, endDatePicker, submitButton);

        return menuTab;
    }

    public void submitDate(){
        controller.updateData(selectedDates());
        controllerGUI.setIfAvailableTrue();
        controllerGUI.reloadScene();
    }

    public LocalDate[] selectedDates() {
        startDate = startDatePicker.getValue();
        endDate = endDatePicker.getValue();

        if (!validateDates(startDate, endDate)) {
            
            System.out.println("wrong dates chosen");
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Error---Invalid Dates Selected");
            alert.setHeaderText(null);
            alert.setContentText("Invalid Dates Selected\n Try Again");
            alert.showAndWait();
            

            startDatePicker.setValue(null);
            endDatePicker.setValue(null);
            return new LocalDate[] {null, null};
        }
        return new LocalDate[] {startDate, endDate};
    }

    private boolean validateDates(LocalDate startDate, LocalDate endDate) {
        if (startDate == null || endDate == null) {
            return false;
        }

        return startDate.isBefore(endDate) && endDate.isBefore(maxDate) && startDate.isAfter(minDate);
    }

}
