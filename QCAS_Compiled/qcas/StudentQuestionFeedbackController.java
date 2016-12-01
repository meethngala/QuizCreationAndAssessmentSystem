/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package qcas;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import qcasMode.Quiz;
import qcasMode.databaseConnection;

/**
 * FXML Controller class
 *
 * @author Meeth
 */
public class StudentQuestionFeedbackController implements Initializable {

    private Quiz quiz;

    @FXML
    private TextArea questionReview;
    @FXML
    private AnchorPane AnchorPane;
    @FXML
    private Button logOutButton;
    @FXML
    private Button backButton;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

    }

    public void showAnswers() {

        String feedbackString = "";
        for (int i = 0; i < quiz.questions.size(); i++) {
            feedbackString += "Question " + (i + 1) + ": " + quiz.questions.get(i).getDescription() + "\n";
            if (quiz.questions.get(i).abbreviation.equals("MA")||quiz.questions.get(i).abbreviation.equals("MC")) {
                feedbackString += "Correct Answer: ";
                for (int j = 0; j < quiz.questions.get(i).answerChoices.size(); j++) {
                    if (quiz.questions.get(i).answerChoices.get(quiz.questions.get(i).answerChoices.keySet().toArray()[j].toString()).equals("correct")) {
                        feedbackString += quiz.questions.get(i).answerChoices.keySet().toArray()[j].toString() + ",";
                    }
                }
                feedbackString = feedbackString.substring(0, feedbackString.length()-1);
                feedbackString += "\n";
            }
            else {
                feedbackString += "Correct Answer: " + quiz.questions.get(i).answerChoices.keySet().toArray(new String[0])[0];
                feedbackString += "\n";
                }
            feedbackString += "Your Answer: ";
            String yourAnswerString = "No Answer";
            if(quiz.getResult().getAnswers().get(quiz.questions.get(i))!=null ){
                for (int j = 0; j < quiz.getResult().getAnswers().get(quiz.questions.get(i)).size(); j++) {
                    if(!quiz.getResult().getAnswers().get(quiz.questions.get(i)).equals("")){  
                        if (!quiz.getResult().getAnswers().get(quiz.questions.get(i)).get(j).equals("null")) {
                            yourAnswerString = quiz.getResult().getAnswers().get(quiz.questions.get(i)).get(j) + ",";
                            }
                        }
                }
            }
            feedbackString += yourAnswerString + "\n";
            feedbackString += "\n\n";
            }
        questionReview.setText(feedbackString);
            // TODO
        }
    
    
    public void setQuiz(Quiz quiz) {
        this.quiz = quiz;
    }

    @FXML
    private void logOutButton(MouseEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("StudentLoginPage.fxml"));
                Stage stage = (Stage) logOutButton.getScene().getWindow();
                Scene scene = new Scene(root);
                stage.setScene(scene);
                stage.show();
    }

    @FXML
    private void handleBackAction(MouseEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("StudentTestResult.fxml"));
        Parent root = (Parent) loader.load();
        StudentTestResultController studentTestResultController = loader.<StudentTestResultController> getController();
        studentTestResultController.setQuiz(quiz);
        studentTestResultController.getChart();
        Stage stage = (Stage) backButton.getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
}
