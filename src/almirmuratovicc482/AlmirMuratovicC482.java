/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package almirmuratovicc482;

import java.io.IOException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import Model.InhousePart;
import Model.Inventory;
import Model.OutsourcedPart;
import View_Controller.MainScreenController;
import static Model.Inventory.getPartInventory;

/**
 *
 * @author almirmuratovic
 */
public class AlmirMuratovicC482 extends Application {
    Stage window;
    private AnchorPane MainScreenView;

    public void initMainScreen() throws IOException{

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(AlmirMuratovicC482.class.getResource("/View_Controller/MainScreen.fxml"));
        AnchorPane MainScreenView = (AnchorPane) loader.load();
    
        Scene scene = new Scene(MainScreenView);
        
        window.setScene(scene);
        window.show();
    }
    
    public void showMainScreen() throws IOException{
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(AlmirMuratovicC482.class.getResource("/View_Controller/MainScreen.fxml"));
        AnchorPane MainScreenView = (AnchorPane) loader.load();
        
        MainScreenController controller = loader.getController();
        controller.setMainApp(this);
    }
    
    @Override
    public void start(Stage primaryStage) throws Exception {
        window = primaryStage;
        window.setTitle("Inventory Mangement System"); 
        initMainScreen();
        showMainScreen();
        
    }

    public static void main(String[] args) {
        launch(args);
    }
    
}
