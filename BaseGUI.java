import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.control.*;
import javafx.event.*;
import java.time.LocalDate;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.control.DatePicker;
import javafx.scene.control.DateCell;
import java.util.ArrayList;

public class BaseGUI {
    private DatePicker startDatePicker, endDatePicker;
    
    protected int WIN_WIDTH = 1000;
    protected int WIN_HEIGHT = 600;
    private LocalDate minDate, maxDate, startDate, endDate;
    public ArrayList<BaseGUI> pages = new ArrayList<>();
    public int currentPageIndex = 0;
    private Scene currentScene;
    private Controller controller;
    private HBox buttons;
    public BaseGUI(Controller controller, HBox buttons){
        this.controller = controller;
        this.buttons = buttons;
    }
    /**
     * Abstract method that returns a scene. Needs to be implemented in each GUI class
     */
    public Scene getScene(){
        return new Scene(new VBox(), WIN_WIDTH, WIN_HEIGHT);
    };
    
    /**
     * The basic scene containing menu tab and buttons at the bottom
     */
    protected BorderPane getRoot(){
        BorderPane root = new BorderPane();

        root.setBottom(buttons);
        root.setTop(menuTab());
        
   
        return root;
    }
    private HBox menuTab(){
        HBox menuTab = new HBox();
        // Code for menu tab
        DatePicker startDatePicker = new DatePicker();
        startDatePicker.setEditable(false);
        DatePicker endDatePicker = new DatePicker();
        endDatePicker.setEditable(false);
        
        
        
        minDate = LocalDate.of(2022, 10, 15);
        maxDate = LocalDate.of(2023, 2, 9);
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
        
        startDatePicker.setValue(minDate);
        endDatePicker.setValue(maxDate);
        menuTab.getChildren().addAll(startDatePicker, endDatePicker);
        
        return menuTab;
    }
    
    
    
     


    
    private boolean validateDates(LocalDate startDate, LocalDate endDate) {
        if (startDate == null || endDate == null) {
            return false;
        }

        if (startDate.isAfter(endDate) || endDate.isAfter(maxDate) || startDate.isBefore(minDate)) {
            return false;
        }

        else {
            return true;
        }
    }

    public void handleDateSelection() {
        // DateSelected=true;
        startDate = startDatePicker.getValue();
        endDate = endDatePicker.getValue();
        

        if (!validateDates(startDate, endDate)) {
            
            System.out.println("wrong dates chosen");

            startDatePicker.setValue(null);
            endDatePicker.setValue(null);
            return;
        }
    
    }  
}
