import javafx.scene.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.control.*;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.SVGPath;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Collections;

import org.apache.commons.lang3.SystemProperties;

import javafx.stage.Stage;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;


public class MapGUI extends BaseGUI {   
    
    private Controller controller;
    private ArrayList<Button> boroughButtons = new ArrayList<Button>();
    private ArrayList<Integer> deaths = new ArrayList<Integer>();
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


    public MapGUI(Controller controller, ControllerGUI controllerGUI){
        super(controller, controllerGUI);
        this.controller = controller;
    }

    public Scene getScene (){
        BorderPane root = getRoot();
        Pane map = new Pane();

        map.getStylesheets().add("style.CSS"); // Replace with the actual path to your CSS file
        
        SVGPath hexagon = new SVGPath();
        hexagon.setContent("M 0.0 -50 L 43 -25 L 43 25 L 6e-15 50 L -43 25 L -43 -25 Z");

        for (int i = 0; i < boroughs.length; i++) {

            Button button = new Button(boroughs[i]);
            button.setOnAction(event -> infoWindow(button.getText()));
            button.setShape(hexagon);
            button.setPickOnBounds(false); // Ensure clicks are only registered on the non-transparent parts of the shape
            button.setMinSize(80, 90); //w,h

            if(boroughButtons.size() < 33){
                boroughButtons.add(button);
            }
        
        }

        int yCord = 0;
        int xCord = 0;
        
        for (Button b : boroughButtons) {
            int index = boroughButtons.indexOf(b);

            if(index == 0){
                yCord = 40;
                xCord = 506;
            }

            else if(index < 4){
                yCord = 112;

                if(index == 1){
                 xCord = 380;
                }
            }
            
            else if(index < 11){
                yCord = 184;
                    
                if(index == 4){
                  xCord = 254;
                }
            }

            else if(index < 18){
                yCord = 256;
                    
                if(index == 11){
                  xCord = 212;
                }
            }          

            else if(index < 24){
                yCord = 328;
                    
                if(index == 18){
                  xCord = 254;
                }
            }

            else if(index < 29){
                yCord = 400;
                    
                if(index == 24){
                  xCord = 296;
                } 
            } 
                
            else{
                yCord = 472;
                    
                if(index == 29){
                  xCord = 338;
                }  

            }  


            b.setLayoutX(xCord); // Set X coordinate for the button
            b.setLayoutY(yCord); // Set Y coordinate for the button
            map.getChildren().add(b); // Add the button to the pane
            xCord += 84;



        }

        VBox legend = new VBox();

        // Example: Create and add 5 squares to the pane
        for (int i = 0; i < 5; i++) {
            // Create a square (Rectangle with equal width and height)
            Rectangle square = new Rectangle(50, 50); // x, y, width, height
            square.setFill(Color.TRANSPARENT); // Set the fill color to transparent
            square.setStroke(Color.BLACK); // Set the border color to black

            // Add the square to the pane
            legend.getChildren().add(square);
        }

        root.setLeft(legend);
        root.setCenter(map);
        deathVisuals();
        return new Scene(root, WIN_WIDTH, WIN_HEIGHT);
    }

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


    private void infoWindow(String borough){

        //make a hashmap connecting the borough code on the button to the full borough name in the data
        HashMap<String, String> names = new HashMap<>();
        names = indexedNames();
        String fName = names.get(borough);

        //create a hashmap off all the data within the selected dates
        HashMap <String, ArrayList<CovidData>> info = new HashMap<>();
        info = controller.boroughAndData();




        TableView<CovidData> table = new TableView<>();
        table.setMinSize(1000, 500);

        Stage stage = new Stage();
        VBox root2 = new VBox();
        Label boroughLbl = new Label(fName);
        root2.getChildren().add(boroughLbl);
        root2.getChildren().add(table);
        
        Scene scene = new Scene(root2, 1200, 500);
        stage.setTitle(fName);
        stage.getIcons().add(new Image("Coronavirus._SARS-CoV-2.png"));
        stage.setScene(scene);
        stage.show();

        String[] dataStuff = {
            "date", "retailRecreationGMR", "groceryPharmacyGMR", "parksGMR", "transitGMR", 
            "workplacesGMR","residentialGMR", "newCases", "totalCases", "newDeaths", "totalDeaths",
        };

        String[] columns = {
            "Date", "Retail/Recreation GMR", "Grocery/Pharmacy GMR", "Parks GMR", "Transit GMR", 
            "Workplaces GMR","Residential GMR", "New Cases", "Total Cases", "New Deaths", "Total Deaths",
        };
            
        for (int i = 0; i < columns.length; i++) {
            TableColumn<CovidData, String> column = new TableColumn<>(columns[i]);
            column.setCellValueFactory(new PropertyValueFactory<>(dataStuff[i]));
            if(i == 1 || i == 2|| i == 5|| i == 6){
                column.setMinWidth(150); // Set minimum width
            }
            table.getColumns().add(column);
        
        }

        //create an array list of the data for the borough in question
        ArrayList<CovidData> boroughData = new ArrayList<>();
        for (String key : info.keySet()) {
            if(key.equals(fName)){
                boroughData = info.get(key);
                for (CovidData i : boroughData) {
                    table.getItems().add(i);
                }
            }
        }
        
    }

    private void deathVisuals(){
        
        //make a hashmap connecting the borough code on the button to the full borough name in the data
        HashMap<String, String> names = new HashMap<>();
        names = indexedNames();

        //create a hashmap off all the data within the selected dates
        HashMap <String, ArrayList<CovidData>> info = new HashMap<>();
        info = controller.boroughAndData();

        //create an array list of the data for the borough in question
        ArrayList<CovidData> boroughData = new ArrayList<>();


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

        for(int i = 0; i < deaths.size(); i++){
            deaths.set(i, deaths.get(i) - minValue2);
        }
        for(int i = 0; i < deaths.size(); i++){
            boroughButtons.get(i).setStyle("-fx-background-color: rgb(255, 0, 0," + String.valueOf(Math.max((double) deaths.get(i) / (double) maxValue2, 0.1)) + ")");
        }




        //0 - 1182
    }
    


}


