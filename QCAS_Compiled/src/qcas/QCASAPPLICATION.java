/*
 * This class contains the main method for the QCAS Application
 */
package qcas;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * This class contains the main method for the QCAS Application
 * @author Meeth
 */
public class QCASAPPLICATION extends Application {
    
    @Override
    public void start(Stage stage) throws Exception {
        // first page is the home page
        Parent root = FXMLLoader.load(getClass().getResource("HomePage.fxml"));        
        Scene scene = new Scene(root);        
        stage.setScene(scene);
        stage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}