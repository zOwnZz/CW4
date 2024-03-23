import javafx.geometry.Insets;
import javafx.scene.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.control.*;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.SVGPath;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Collections;
import javafx.stage.Stage;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;

/**
 * MapGUI creates the GUI for the map page of the COVID DATA program
 *
 */
public class MapGUI extends BaseGUI {
    private Controller controller;
    private ArrayList<Button> boroughButtons = new ArrayList<>();
    private String[] boroughs = {
            "ENFI", "BARN", "HRGY", "WALT", "HRRW", "BREN", "CAMD", "ISLI", "HACK", "REDB", "HAVE",
            "HILL", "EALI", "KENS", "WEST", "TOWH", "NEWH", "BARK", "HOUN", "HAMM", "WAND", "CITY",
            "GWCH", "BEXL", "RICH", "MERT", "LAMB", "STHW", "LEWS", "KING", "SUTT", "CROY", "BROM"
    };
    private String[] boroughsFull = {
            "Enfield", "Barnet", "Haringey", "Waltham Forest", "Harrow", "Brent", "Camden", "Islington", "Hackney", "Redbridge", "Havering",
            "Hillingdon", "Ealing", "Kensington And Chelsea", "Westminster", "Tower Hamlets", "Newham", "Barking", "Hounslow", "Hammersmith And Fulham", "Wandsworth", "City Of London",
            "Greenwich", "Bexley", "Richmond", "Merton", "Lambeth", "Southwark", "Lewisham", "Kingston Upon Thames", "Sutton", "Croydon", "Bromley"
    };

    /**
     * Constructor for objects of class MapGui
     */
    public MapGUI(Controller controller, ControllerGUI controllerGUI){
        super(controller, controllerGUI);
        this.controller = controller;
    }

    /**
     * Creating the map scene
     *
     * @return  Scene containing the required elements
     */
    public Scene getScene (){
        BorderPane root = getRoot();

        //Map
        Pane map = new Pane();
        //Specific coordinates to place buttons in the right position in the pane
        int yCord = 0;
        int xCord = 0;
        map.getStylesheets().add("style.CSS");//add hover effect over the buttons

        //create the hexagonal shape of the buttons
        SVGPath hexagon = new SVGPath();
        hexagon.setContent("M 0.0 -50 L 43 -25 L 43 25 L 6e-15 50 L -43 25 L -43 -25 Z");
        for (int i = 0; i < boroughs.length; i++) {
            Button button = new Button(boroughs[i]);
            button.setOnAction(event -> infoWindow(button.getText()));
            button.setShape(hexagon);
            button.setPickOnBounds(false); // Ensure clicks are only registered on the non-transparent parts of the shape
            button.setMinSize(80, 90); //w,h
            //ensure no new buttons are added when the page is refreshed
            if(boroughButtons.size() < 33){
                boroughButtons.add(button);
            }
        }

        //places the buttons in their respective places in the pane to reperensent a geographically accurate map
        for (Button b : boroughButtons) {
            int index = boroughButtons.indexOf(b);
            if(index == 0){
                yCord = 40;
                xCord = 506;
            }else if(index < 4){
                yCord = 112;
                if(index == 1){
                    xCord = 380;
                }
            }else if(index < 11){
                yCord = 184;
                if(index == 4){
                    xCord = 254;
                }
            }else if(index < 18){
                yCord = 256;
                if(index == 11){
                    xCord = 212;
                }
            }else if(index < 24){
                yCord = 328;
                if(index == 18){
                    xCord = 254;
                }
            }else if(index < 29){
                yCord = 400;
                if(index == 24){
                    xCord = 296;
                }
            }else{
                yCord = 472;
                if(index == 29){
                    xCord = 338;
                }
            }

            b.setLayoutX(xCord); // Set X coordinate for the button
            b.setLayoutY(yCord); // Set Y coordinate for the button
            map.getChildren().add(b); // Add the button to the pane
            xCord += 84;//increment x coordinate to allow for equal spacing
        }

        //legend to explain the map
        VBox legend = new VBox();
        legend.setPadding(new Insets(100, 0, 0, 10)); // Top, Right, Bottom, Left padding
        Label leastDeathsLbl = new Label("Least Deaths");
        Label mostDeathsLbl = new Label("Most Deaths");
        legend.getChildren().add(leastDeathsLbl);
        //Create and add 5 squares to the pane
        for (int i = 1; i < 11; i++) {
            // Create a square (Rectangle with equal width and height)
            Double j = i/10.0;//decide the opacity of the square
            Rectangle square = new Rectangle(25, 25); // x, y, width, height
            square.setFill(Color.RED);
            square.setStroke(Color.TRANSPARENT); // Set the border color to black
            square.setOpacity(j);
            legend.getChildren().add(square);
        }
        legend.getChildren().add(mostDeathsLbl);

        //adding all the elements to the scene
        root.setLeft(legend);
        root.setCenter(map);
        deathVisuals();
        return new Scene(root, winWidth, winHeight);
    }

    /**
     * Connects the two Arrays containing the short names of the boroughs on the buttons and the full names in the data set
     *
     * @return  Hashmap with the key being the short name and the value being the long name
     */
    private HashMap<String, String> indexedNames(){
        HashMap<String, String> boroughNameLink = new HashMap<>();
        String smallName = new String();
        String bigName = new String();
        for (int i = 0; i < boroughs.length; i++){
            smallName = boroughs[i];
            bigName = boroughsFull[i];
            boroughNameLink.put(smallName,bigName);
        }
        return boroughNameLink;
    }

    /**
     * An example of a method - replace this comment with your own
     *
     * @param  borough of the borough thats been clicked
     */
    private void infoWindow(String borough){
        //make a hashmap connecting the borough code on the button to the full borough name in the data
        HashMap<String, String> names = new HashMap<>();
        names = indexedNames();
        String fName = names.get(borough);

        //create a hashmap of all the data within the selected dates for the borough in question
        HashMap <String, ArrayList<CovidData>> info = new HashMap<>();
        info = controller.boroughAndData();

        //Create gui components to present the data in the new window
        VBox root2 = new VBox();
        HBox hbox = new HBox();
        Stage stage = new Stage();
        Scene scene = new Scene(root2, 1180, 550);
        Label boroughLbl = new Label(fName);
        ComboBox<String> sortComboBox = new ComboBox<>();//Combobox for sorting
        TableView<CovidData> table = new TableView<>();//Table of object type CovidData

        //present the new window
        table.setMinSize(1000, 500);
        sortComboBox.setValue("Date"); // Set default value
        hbox.setPadding(new Insets(0, 0, 0, 10)); // Top, Right, Bottom, Left padding
        hbox.setSpacing(10);
        boroughLbl.setStyle("-fx-font-size: 10pt;");
        hbox.getChildren().add(boroughLbl);
        hbox.getChildren().add(sortComboBox);
        root2.getChildren().add(hbox);
        root2.getChildren().add(table);
        stage.setTitle(fName);
        //stage.getIcons().add(new Image("Coronavirus._SARS-CoV-2.png"));
        stage.setScene(scene);
        stage.show();

        //name of attributes of the object type CovidData
        String[] attributeNames = {
                "date", "retailRecreationGMR", "groceryPharmacyGMR", "parksGMR", "transitGMR",
                "workplacesGMR","residentialGMR", "newCases", "totalCases", "newDeaths", "totalDeaths",
        };
        String[] columns = {
                "Date", "Retail/Recreation GMR", "Grocery/Pharmacy GMR", "Parks GMR", "Transit GMR",
                "Workplaces GMR","Residential GMR", "New Cases", "Total Cases", "New Deaths", "Total Deaths",
        };

        HashMap<String, TableColumn<CovidData, String>> columnsLink = new HashMap<>();
        //fill the table with titles for the columns and what attributes of object type Covid Data to expect in each column
        for (int i = 0; i < columns.length; i++) {
            TableColumn<CovidData, String> column = new TableColumn<>(columns[i]);
            column.setCellValueFactory(new PropertyValueFactory<>(attributeNames[i]));
            if(i == 1 || i == 2|| i == 5|| i == 6){
                column.setMinWidth(150); // Set minimum width
            }
            columnsLink.put(columns[i],column);
            table.getColumns().add(column);
        }

        //add data to the table
        ArrayList<CovidData> boroughData = new ArrayList<>();
        for (String key : info.keySet()) {
            if(key.equals(fName)){
                boroughData = info.get(key);
                for (CovidData i : boroughData) {
                    table.getItems().add(i);
                }
            }
        }

        sortComboBox.getItems().addAll(columns);
        //automattically sorts the data depending on the selected column in the combobox
        sortComboBox.getSelectionModel().selectedItemProperty().addListener((options, oldValue, newValue) -> {
            table.getSortOrder().clear();
            String selectedColumn = sortComboBox.getValue();
            table.getSortOrder().clear();
            table.getSortOrder().add(columnsLink.get(selectedColumn));
            table.sort();
        });
    }

    /**
     * Create the chloropleth map look to represent the number of deaths
     *
     */
    private void deathVisuals(){
        //make a hashmap connecting the borough code on the button to the full borough name in the data
        HashMap<String, String> names = new HashMap<>();
        names = indexedNames();

        //create a hashmap off all the data within the selected dates
        HashMap <String, ArrayList<CovidData>> info = new HashMap<>();
        info = controller.boroughAndData();

        //create an array list of the data for the borough in question
        ArrayList<CovidData> boroughData = new ArrayList<>();
        ArrayList<Integer> deaths = new ArrayList<Integer>();
        String shortName = new String();
        String longName = new String();
        int totalDeaths = 0;
        for(Button b : boroughButtons){
            shortName = b.getText();
            longName = names.get(shortName);
            b.setStyle("-fx-background-color:red;");
            //loops through all records in that timeline to find the ones related to the borough in question
            for (String key : info.keySet()) {
                if(key.equals(longName)){
                    boroughData = info.get(key);
                    totalDeaths = 0;
                    int minDeaths = 100000;
                    int maxDeaths = 0;
                    //goes through each date to find the date with the highest total deaths and the lowest total deaths
                    for (CovidData i : boroughData) {
                        if(i.getTotalDeaths() > maxDeaths){
                            maxDeaths = i.getTotalDeaths();
                        }
                        if(i.getTotalDeaths() < minDeaths){
                            minDeaths = i.getTotalDeaths();
                        }
                        totalDeaths = maxDeaths - minDeaths;
                    }
                }
            }
            deaths.add(totalDeaths);
        }

        int minValue2 = Collections.min(deaths);
        int maxValue2 = Collections.max(deaths);
        maxValue2 = maxValue2 - minValue2;

        //calculates the colour of the borough to show as a chloropleth map
        for(int i = 0; i < deaths.size(); i++){
            deaths.set(i, deaths.get(i) - minValue2);
        }
        for(int i = 0; i < deaths.size(); i++){
            Double opacity = Math.max((double) deaths.get(i) / (double) maxValue2, 0.1);
            if(Double.isNaN(opacity)){
                boroughButtons.get(i).setStyle("-fx-background-color: rgb(255, 255, 255)");
            }
            else{
                boroughButtons.get(i).setStyle("-fx-background-color: rgba(255, 0, 0," + String.valueOf(opacity) + ")");
            }

        }
    }
}
