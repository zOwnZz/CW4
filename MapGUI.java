import javafx.scene.*;
import javafx.scene.layout.*;
import javafx.scene.control.*;
import javafx.scene.shape.SVGPath;
import java.util.ArrayList;
import java.util.HashMap;

public class MapGUI extends BaseGUI {   
    
    private Controller controller;
    private ArrayList<Button> boroughButtons = new ArrayList<Button>();
    private String[] boroughs = {
        "ENFI", "BARN", "HRGY", "WALT", "HRRW", "BREN", "CAMD", "ISLI", "HACK", "REDB", "HAVE",
        "HILL", "EALI", "KENS", "WEST", "TOWH", "NEWH", "BARK", "HOUN", "HAMM", "WAND", "CITY",
        "GWCH", "BEXL", "RICH", "MERT", "LAMB", "STHW", "LEWS", "KING", "SUTT", "CROY", "BROM"
        };
    
    private String[] boroughsFull = {
        "Enfield", "Barnet", "Haringey", "Waltham Forest", "Harrow", "Brent", "Camden", "Islington", "Hackney", "Redbridge", "Havering",
        "Hillingdon", "Ealing", "Kensing And Chelsea", "Westminster", "Tower Hamlets", "Newham", "Barking", "Hounslow", "Hammersmith And Fulham", "Wandsworth", "City Of London",
        "Greenwich", "Bexley", "Richmond", "Merton", "Lambeth", "Southwark", "Lewisham", "Kingston Upon Thames", "Sutton", "Croydon", "Bromley"
        };



    public MapGUI(Controller controller, ControllerGUI controllerGUI){
        super(controller, controllerGUI);
        this.controller = controller;
    }

    public Scene getScene (){
        BorderPane root = getRoot();
        Pane map = new Pane();
        
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


            HashMap <String, ArrayList<CovidData>> maps = new HashMap<>();
            maps = controller.boroughAndData();
            System.out.println(maps.toString());
        }

        root.setCenter(map);
        return new Scene(root, WIN_WIDTH, WIN_HEIGHT);
    }

    private void infoWindow(String borough){

    }

    private void deathVisuals(){

    }
}


