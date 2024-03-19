import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.layout.*;
import javafx.scene.control.*;
import java.time.LocalDate;

import javafx.scene.control.DatePicker;
import javafx.scene.control.DateCell;



public class BaseGUI {

    // -------------- Attributes --------------

    protected double winWidth = 1000;
    protected double winHeight = 600;
    private Controller controller;
    private LocalDate minDate, maxDate, startDate, endDate;
    private DatePicker startDatePicker, endDatePicker;
    private ControllerGUI controllerGUI;
    private HBox buttons = new HBox();
    private final double BUTTONS_SPACING = 200;

    // -------------- Constructors and initializer --------------

    public BaseGUI(Controller controller, ControllerGUI controllerGUI){
        init(controller, controllerGUI);
        winHeight = controllerGUI.getStageHeight();
        winWidth = controllerGUI.getStageWidth();
    }
    public BaseGUI(Controller controller, ControllerGUI controllerGUI, Boolean ifFirst){
        init(controller, controllerGUI);
        setButtonsSpacing(controllerGUI.getStageWidth());
    }

    private void init(Controller controller, ControllerGUI controllerGUI){
        this.controller = controller;
        this.controllerGUI = controllerGUI;
        minDate = controller.getMinDate();
        maxDate = controller.getMaxDate();
    }

    // -------------- Getters and setters --------------

    /**
     * Method that returns a scene. Should be overriden in each GUI class
     */
    public Scene getScene(){
        return new Scene(new VBox(), winWidth, winHeight);
    };

    /**
     * The basic scene containing menu tab and buttons at the bottom
     */
    protected BorderPane getRoot(){
        BorderPane root = new BorderPane();

        root.setBottom(getButtons());
        root.setTop(getMenuTab());

        return root;
    }

    private HBox getButtons() {
        Button nextButton = new Button(">");
        Button backButton = new Button("<");

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

        backButton.setAlignment(Pos.BOTTOM_LEFT);
        nextButton.setAlignment(Pos.BOTTOM_RIGHT);
        buttons.setAlignment(Pos.CENTER);
        buttons.setPadding(new Insets(10, 10, 10, 10));
        buttons.setStyle("-fx-background-color: #0f0fff;");

        BorderPane.setMargin(buttons, new javafx.geometry.Insets(10));

        return buttons;
    }

    private HBox getMenuTab(){
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

        menuTab.setAlignment(Pos.CENTER_RIGHT);
        BorderPane.setMargin(menuTab, new javafx.geometry.Insets(10, 40, 10, 10));
        menuTab.setSpacing(15);
        menuTab.setStyle("-fx-background-color: #f0f0ff;");
        menuTab.setPadding(new javafx.geometry.Insets(15));

        return menuTab;
    }

    public void setButtonsSpacing(double spacing){
        buttons.setSpacing(spacing - BUTTONS_SPACING);
    }

    // -------------- Auxiliary methods --------------

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
