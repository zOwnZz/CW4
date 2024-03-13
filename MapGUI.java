import javafx.geometry.Insets;
import javafx.scene.*;
import javafx.scene.image.*;
import javafx.scene.layout.*;
import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.SVGPath;
import javafx.scene.web.WebView;
import java.io.IOException;
import java.util.ArrayList;

public class MapGUI extends BaseGUI {   
    
    private Controller controller;
    private String clickedBorough;
    private ArrayList<Button> boroughButtons = new ArrayList<Button>();

    public MapGUI(Controller controller){
        this.controller = controller;
    }

    public Scene getScene (){
        BorderPane root = getRoot();
        Pane map = new Pane();

        String[] boroughs = {
        "ENFI", "BARN", "HRGY", "WALT", "HRRW", "BREN", "CAMD", "ISLI", "HACK", "REDB", "HAVE",
        "HILL", "EALI", "KENS", "WEST", "TOWH", "NEWH", "BARK", "HOUN", "HAMM", "WAND", "CITY",
        "GWCH", "BEXL", "RICH", "MERT", "LAMB", "STHW", "LEWS", "KING", "SUTT", "CROY", "BROM"
        };

        SVGPath hexagon = new SVGPath();
        hexagon.setContent("M 0.0 -50 L 43 -25 L 43 25 L 6e-15 50 L -43 25 L -43 -25 Z");

        for (int i = 0; i < boroughs.length; i++) {
            Button button = new Button(boroughs[i]);
            button.setOnAction(event -> clickedBorough = button.getText());
            button.setShape(hexagon);
            button.setPickOnBounds(false); // Ensure clicks are only registered on the non-transparent parts of the shape
            button.setMinSize(80, 90);
            //w,h
            boroughButtons.add(button);
        
        }

        int yCord = 0;
        int xCord = 0;

        for (Button b : boroughButtons) {
            int index = boroughButtons.indexOf(b);

            if(index == 0){
                yCord = 60;
                xCord = 506;
            }

            else if(index < 4){
                yCord = 132;

                if(index == 1){
                 xCord = 380;
                }
            }
            
            else if(index < 11){
                yCord = 204;
                    
                if(index == 4){
                  xCord = 254;
                }
            }

            else if(index < 18){
                yCord = 276;
                    
                if(index == 11){
                  xCord = 212;
                }
            }          

            else if(index < 24){
                yCord = 348;
                    
                if(index == 18){
                  xCord = 254;
                }
            }

            else if(index < 29){
                yCord = 420;
                    
                if(index == 24){
                  xCord = 296;
                } 
            } 
                
            else{
                yCord = 492;
                    
                if(index == 29){
                  xCord = 338;
                }  

            }  

            b.setLayoutX(xCord); // Set X coordinate for the button
            b.setLayoutY(yCord); // Set Y coordinate for the button
            map.getChildren().add(b); // Add the button to the pane
            xCord += 84;
        }

        root.setCenter(map);
        return new Scene(root, WIN_WIDTH, WIN_HEIGHT);
    }
}
        

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.geometry.Pos;

public class MapGUI extends BaseGUI{
    private Controller controller;
    public MapGUI(Controller controller, ControllerGUI controllerGUI){
        super(controller, controllerGUI);
        this.controller = controller;

    }
    @Override
    public Scene getScene() {
        BorderPane root = getRoot();

        VBox center = new VBox();

        Label InfoLabel = new Label("This is map GUI.");

        // Create a layout to stack the labels on top of each other
        StackPane stackPane = new StackPane();
        // Add the labels to the layout
        stackPane.getChildren().addAll( InfoLabel);

        // Create a scene with the layout
        Scene scene = new Scene(stackPane, 300, 200);
        center.getChildren().addAll(InfoLabel);
        center.setAlignment(Pos.CENTER);
        center.setSpacing(30);
        //return new Scene(root, WIN_WIDTH, WIN_HEIGHT);
        //return scene;
        root.setCenter(center);
        return new Scene(root, WIN_WIDTH, WIN_HEIGHT);
    }
}

