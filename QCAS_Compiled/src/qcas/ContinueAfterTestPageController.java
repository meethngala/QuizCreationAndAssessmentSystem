/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package qcas;

import com.itextpdf.text.DocumentException;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import qcasMode.Quiz;

/**
 * FXML Controller class
 *
 * @author WalterWhite
 */
public class ContinueAfterTestPageController implements Initializable {

    @FXML 
    private Label TimeUpLabel ;
    
    @FXML 
    private Label QuizUpLabel ;
    @FXML
    private Quiz quiz;
    @FXML 
    private Button continueButton ;
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
    }    
    
    @FXML
    public void setQuiz(Quiz quiz) 
    {
        QuizUpLabel.setVisible(true);
        this.quiz = quiz;
        
    }
    @FXML
    public void continueToResults() throws IOException, DocumentException
    {        
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("StudentTestResult.fxml"));
        Parent root = (Parent) loader.load();
        StudentTestResultController studentTestResultController = loader.<StudentTestResultController> getController();
        studentTestResultController.setQuiz(quiz);
        studentTestResultController.getChart();
        Stage stage = (Stage) continueButton.getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
   
   
    
}
