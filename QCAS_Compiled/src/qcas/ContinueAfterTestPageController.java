/*
 * This class controls the application page which appears right after the test
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
 * @author Khushboo Banwari
 */
public class ContinueAfterTestPageController implements Initializable {

    // labels
    @FXML
    private Label TimeUpLabel;
    @FXML
    private Label QuizUpLabel;
    @FXML
    private Quiz quiz;
    // buttons
    @FXML
    private Button continueButton;

    /**
     * Initializes the controller class.
     *@param url Reference of a URL Object
     * @param rb Reference of a ResourceBundle Object
     * 
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

    }

    /**
     * Sets the current quiz with the argument passed (reference of a quiz
     * object)
     *
     * @param quiz A reference of the object of a Quiz class
     */
    @FXML
    public void setQuiz(Quiz quiz) {
        QuizUpLabel.setVisible(true);    //quiz up label is made visible 
        this.quiz = quiz;
    }

    /**
     * This method is called when the continue button on the application is
     * pressed
     *
     * @throws IOException
     * @throws DocumentException
     */
    @FXML
    public void continueToResults() throws IOException, DocumentException {
        FXMLLoader loader = new FXMLLoader();
        // next page is the results page
        loader.setLocation(getClass().getResource("StudentTestResult.fxml"));
        Parent root = (Parent) loader.load();
        StudentTestResultController studentTestResultController = loader.<StudentTestResultController>getController();
        studentTestResultController.setQuiz(quiz);
        studentTestResultController.getChart(); // for displaying the chart
        Stage stage = (Stage) continueButton.getScene().getWindow(); //set the stage 
        Scene scene = new Scene(root);
        stage.setScene(scene);//set the scene
        stage.show();
    }
}
