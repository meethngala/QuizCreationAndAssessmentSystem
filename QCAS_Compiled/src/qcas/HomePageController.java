/*
 * This class controls the home page of the application (WELCOME TO QCAS page)
 */
package qcas;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Meeth
 */
public class HomePageController implements Initializable {
    // images on the application
    @FXML
    private ImageView loginImage;  
    @FXML
    private ImageView backgroundImage;

    /**
     * Initializes the controller class.
     * @param url Reference of a URL Object
     * @param rb Reference of a ResourceBundle Object
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

    }
    
    /**
     * This method handles the action when the login button(image) is clicked
     * @param event Reference of a MouseEvent Object
     */
    
    @FXML
    private void handleLoginAction(MouseEvent event) throws IOException {
        // next page is the login page 
        Parent root = FXMLLoader.load(getClass().getResource("Login.fxml"));
        Stage stage = (Stage) loginImage.getScene().getWindow();// set the stage
        Scene scene = new Scene(root);
        stage.setScene(scene); // set the scene
        stage.show();
    }
}
