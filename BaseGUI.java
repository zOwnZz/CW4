import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.layout.*;
import javafx.scene.control.*;
import java.time.LocalDate;
import java.util.Objects;

import javafx.scene.control.DatePicker;
import javafx.scene.control.DateCell;



public class BaseGUI {

    // -------------- Attributes --------------

    protected double winWidth = 1000;
    protected double winHeight = 750;
    protected Controller controller;
    private LocalDate minDate, maxDate;
    private DatePicker startDatePicker, endDatePicker;
    private ControllerGUI controllerGUI;
    private final HBox buttons = new HBox();

    // -------------- Constructors and initializer --------------

    /**
     * Basic constructor for each panel, providing controllers to initializer and setting window size to the previous one.
     * Called for each stage, despite first opening of welcome window.
     */
    public BaseGUI(Controller controller, ControllerGUI controllerGUI){
        init(controller, controllerGUI);
        winHeight = controllerGUI.getStageHeight();
        winWidth = controllerGUI.getStageWidth();
    }

    /**
     * Basic constructor for each panel, providing controllers to initializer with the window size set in default.
     * Called during first opening of welcome panel
     */
    public BaseGUI(Controller controller, ControllerGUI controllerGUI, Boolean ifFirst){
        init(controller, controllerGUI);
    }

    /**
     * Initializer, called at each constructor. assigns all the object to its reference and calculates dates.
     * @param controller responsible for the main functionality in receiving data and calculating.
     * @param controllerGUI responsible for creating scenes and switching between them
     */
    private void init(Controller controller, ControllerGUI controllerGUI){
        this.controller = controller;
        this.controllerGUI = controllerGUI;
        minDate = controller.getMinDateCalculated();
        maxDate = controller.getMaxDateCalculated();
        setButtonsSpacing(controllerGUI.getStageWidth());
    }

    // -------------- Getters and setters --------------

    /**
     * Method that returns a scene. Should be overriden in each GUI class
     */
    public Scene getScene(){
        return new Scene(new VBox(), winWidth, winHeight);
    }

    /**
     * The basic border pane containing upper menu tab and buttons at the bottom
     */
    protected BorderPane getRoot(){
        BorderPane root = new BorderPane();

        // Add stylesheet and all the components to the root
        root.getStylesheets().add(Objects.requireNonNull(getClass().getResource("style.css")).toExternalForm());
        root.setBottom(getButtons());
        root.setTop(getMenuTab());

        return root;
    }

    /**
     * Creates the box with buttons that allow switching between different panels
     * @return box with buttons at the bottom
     */
    private HBox getButtons() {
        // Create buttons
        Button nextButton = new Button(">");
        Button backButton = new Button("<");

        // Set event while buttons are clicked
        backButton.setOnAction(event -> controllerGUI.changeScene(false));
        nextButton.setOnAction(event -> controllerGUI.changeScene(true));

        // Disable buttons if dates haven't been chosen yet
        if(!controllerGUI.getIfAvailable()){
            backButton.setDisable(true);
            nextButton.setDisable(true);
        } else {
            backButton.setDisable(false);
            nextButton.setDisable(false);
        }

        buttons.getChildren().addAll(backButton, nextButton);

        // Style the buttons and the box
        backButton.setAlignment(Pos.BOTTOM_LEFT);
        nextButton.setAlignment(Pos.BOTTOM_RIGHT);
        buttons.setAlignment(Pos.CENTER);
        buttons.setPadding(new Insets(10, 10, 10, 10));
        buttons.setId("topBox");
        buttons.setPrefHeight(65);
        BorderPane.setMargin(buttons, new javafx.geometry.Insets(10));

        return buttons;
    }

    /**
     * Create an upper menu for each scene, where user can choose and submit the data
     * @return box with upper menu
     */
    private HBox getMenuTab(){
        // Create and style the box
        HBox menuTab = new HBox();
        menuTab.setId("topBox");
        menuTab.setPrefHeight(65);

        // Create date pickers
        startDatePicker = new DatePicker();
        startDatePicker.setEditable(false);
        endDatePicker = new DatePicker();
        endDatePicker.setEditable(false);

        // Give date pickers a functionality of choosing date
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

        // Always show the previously chosen value
        startDatePicker.setValue(controller.getStartDate());
        endDatePicker.setValue(controller.getEndDate());

        // Create a submit button and add on click event
        Button submitButton = new Button("Submit!");
        submitButton.setOnAction(event -> submitDate());

        // Create a layout of upper menu tab
        Label Title = new Label("Covid-19 in London");
        Label fromLabel = new Label("From :");
        Label toLabel = new Label("To :");
        Region gapRegion = new Region();
        gapRegion.setPrefWidth(250);
        menuTab.setSpacing(10);

        // Add all the components to the box and style it
        menuTab.getChildren().addAll(Title, gapRegion, fromLabel, startDatePicker, toLabel, endDatePicker, submitButton);
        menuTab.setAlignment(Pos.CENTER_RIGHT);
        BorderPane.setMargin(menuTab, new javafx.geometry.Insets(15));
        menuTab.setPadding(new javafx.geometry.Insets(15));

        return menuTab;
    }

    /**
     * Set spacing between buttons, to make it resizable
     * @param spacing, the spacing between buttons
     */
    public void setButtonsSpacing(double spacing){
        double BUTTONS_SPACING = 200;
        buttons.setSpacing(spacing - BUTTONS_SPACING);
    }

    // -------------- Auxiliary methods --------------

    /**
     * Pass the selected date to the controller, enable buttons (if not already) and reload scene to get new data
     * from the selected period.
     */
    public void submitDate(){
        controller.updateData(selectedDates());
        controllerGUI.setIfAvailableTrue();
        controllerGUI.reloadScene();
    }

    /**
     * Retrieve the selected dates from date pickers and validate it if the given dates are in between the correct range.
     * @return array of two local dates, the first one start date and second one end date.
     */
    public LocalDate[] selectedDates() {
        // Get values of date pickers
        LocalDate startDate = startDatePicker.getValue();
        LocalDate endDate = endDatePicker.getValue();

        if (!validateDates(startDate, endDate)) {

            // Show alert if dates ranges are wrong
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error---Invalid Dates Selected");
            alert.setHeaderText(null);
            alert.setContentText("Invalid Dates Selected\n Try Again");
            alert.showAndWait();

            // Reset and return null values
            startDatePicker.setValue(null);
            endDatePicker.setValue(null);
            return new LocalDate[] {null, null};
        }
        return new LocalDate[] {startDate, endDate};
    }

    /**
     * Check if given date range is correct
     * @param startDate start date
     * @param endDate end date
     * @return boolean if given date range is correct
     */
    private boolean validateDates(LocalDate startDate, LocalDate endDate) {
        if (startDate == null || endDate == null) {
            return false;
        }
        return startDate.minusDays(1).isBefore(endDate) && endDate.minusDays(1).isBefore(maxDate) && startDate.plusDays(1).isAfter(minDate);
    }

}
