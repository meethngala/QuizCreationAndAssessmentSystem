/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
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
import qcasMode.*;

/**
 * FXML Controller class
 *
 * @author Meeth
 */
public class StudentActionController implements Initializable {

    /**
     * Initializes the controller class.
     */
    @FXML
    private ImageView quizImage;
    
    
    @FXML
    private Button LogoutButton;
    private Student student;
    @FXML
    private ImageView pastPerformance;
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    @FXML
    private void goToTestOptions(MouseEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("StudentTestDashboard.fxml"));
        Parent root = (Parent) loader.load();
        StudentTestDashboardController studentTestDashboardController = loader.<StudentTestDashboardController> getController();
        studentTestDashboardController.setStudent(student);
        Stage stage = (Stage) quizImage.getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
    
    @FXML
    private void logoutStudent(MouseEvent event) throws IOException 
    {
        Parent root = FXMLLoader.load(getClass().getResource("StudentLoginPage.fxml"));
        Stage stage = (Stage) LogoutButton.getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
    
    public void setStudent(Student student) {
        this.student = student;
    }

    @FXML
    private void getPastPerformance(MouseEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("StudentPerformance.fxml"));
        Parent root = (Parent) loader.load();
        StudentPerformanceController studentPerformanceController = loader.<StudentPerformanceController> getController();
        studentPerformanceController.setStudent(student);
        Stage stage = (Stage) pastPerformance.getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
    

    
}
