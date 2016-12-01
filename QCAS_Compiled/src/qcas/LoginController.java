/*
 * This class controls the login page- students and instructors 
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
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Meeth
 */
public class LoginController implements Initializable {

    //Images in the application
    @FXML
    private ImageView studentLoginImage;

    @FXML
    private ImageView teacherLoginImage;

    /**
     * Initializes the controller class.
     * @param url Reference of a URL Object
     * @param rb Reference of a ResourceBundle Object
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

    /**
     * Function to handle what the application does when a student logins
     * @param event Reference of a MouseEvent Object
     * 
     */
    @FXML
    private void goToStudentLogin(MouseEvent event) throws IOException {
        // next page is student login page
        Parent root = FXMLLoader.load(getClass().getResource("StudentLoginPage.fxml"));
        Stage stage = (Stage) studentLoginImage.getScene().getWindow(); // set the stage
        Scene scene = new Scene(root);
        stage.setScene(scene); // set the scene
        stage.show();
    }
    /**
     * Function to handle what the application does when a teacher logins
     * @param event Reference of a MouseEvent Object
     * 
     */
    @FXML
    private void goToTeacherLogin(MouseEvent event) throws IOException {
        // next page is teacher login page
        Parent root = FXMLLoader.load(getClass().getResource("TeacherLoginPage.fxml"));
        Stage stage = (Stage) teacherLoginImage.getScene().getWindow(); // set the stage
        Scene scene = new Scene(root);
        stage.setScene(scene); // set the scene
        stage.show();
    }
}
