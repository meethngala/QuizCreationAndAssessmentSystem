/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package qcas;

import com.itextpdf.text.BadElementException;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.SnapshotParameters;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javax.imageio.ImageIO;
import qcasMode.Quiz;
import qcasMode.databaseConnection;

/**
 * FXML Controller class
 *
 * @author Meeth
 */
public class StudentTestResultController implements Initializable {
    private Button button;
    @FXML
    private Button savePDFButton ;
    @FXML
    private AnchorPane performanceAnchorPane ;
    @FXML
    private Label scoreLabel;
    @FXML
    private Label gradeLabel;
    @FXML
    private Label pdfDownloadedLabel ;
    @FXML
    private Button checkAnswers;
    private final CategoryAxis xAxis = new CategoryAxis();
    private final NumberAxis yAxis = new NumberAxis();
    private XYChart.Series correct = new XYChart.Series<>();
    private XYChart.Series incorrect = new XYChart.Series<>();
    private Quiz quiz;
    @FXML
    private BarChart<Integer, Integer> performanceBarChart;
    @FXML
    private Button logOutButton;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
    }   
    
    public void getChart(){
        performanceBarChart.setVisible(true);
        performanceBarChart.getXAxis().setLabel("Accuracy");
        performanceBarChart.getYAxis().setLabel("Count");
        try{
        databaseConnection dBconn = new databaseConnection("jdbc:mysql://qcasrohan.caswkasqdmel.ap-southeast-2.rds.amazonaws.com:3306/QCASRohan", "rohan", "rohantest");
        Connection con = dBconn.getConnection();
        Statement stmt = con.createStatement();
        
        String validityCheck = "SELECT VALIDITY,count(*) As Count from QCASRohan.QUIZ_QUESTION WHERE QUIZID = " + quiz.getQuizNumber() +" group by VALIDITY;";
        ResultSet rs = stmt.executeQuery(validityCheck);
        ArrayList<Integer> validityChecks = new ArrayList();
        int incorrectCount = 0;
        int correctCount=0;
        while(rs.next()){
            if (rs.getString("VALIDITY").equals("correct")){
                correctCount +=1;
            }
            if (rs.getString("VALIDITY").equals("incorrect")){
                incorrectCount +=1;
            }
            validityChecks.add(rs.getInt("Count"));
        }
        
        correct.setName("Correct");
        if (correctCount==1){
        correct.getData().add(new XYChart.Data<>("correct",validityChecks.get(0)));
        }
        else if (correctCount==0) {
        correct.getData().add(new XYChart.Data<>("correct",0));
        }
        incorrect.setName("Incorrect");
        if (incorrectCount==1 && correctCount==0){
        incorrect.getData().add(new XYChart.Data<>("incorrect",validityChecks.get(0)));
        }
        else if (incorrectCount==1 && correctCount==1){
        incorrect.getData().add(new XYChart.Data<>("incorrect",validityChecks.get(1)));
        }
        else {
        incorrect.getData().add(new XYChart.Data<>("incorrect",0));
        }
        performanceBarChart.getData().addAll(correct, incorrect);
        
        scoreLabel.setText("" + (int)(quiz.getScore()*100) + "/100");
        gradeLabel.setText(quiz.getGrade());
        
        // TODO
        } catch(SQLException e){
            System.out.println("SQL Exception: "+ e);
        }
    }
    
    private void handleButtonAction1(MouseEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("HomePage.fxml"));
        Stage stage = (Stage) button.getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
    
    public void setQuiz(Quiz quiz){
        this.quiz = quiz;
    }
    
    
    @FXML
    public void exportToPdf() throws IOException,  DocumentException
    {
       WritableImage image = performanceAnchorPane.snapshot(new SnapshotParameters(), null);
        //File file = new File("StudentPerformance.png");
        ByteArrayOutputStream byteArray = new ByteArrayOutputStream();
        try {
            ImageIO.write(SwingFXUtils.fromFXImage(image, null), "png", byteArray);
        } catch (IOException e) {
        }
        Document document = new Document();
        String output = "StudentQuizPerformance.pdf";
        try {
            int indentation = 0;
            FileOutputStream fos = new FileOutputStream(output);
            PdfWriter writer = PdfWriter.getInstance(document, fos);
            writer.open();
            document.open();
            Image newImage = Image.getInstance(byteArray.toByteArray());
            newImage.scaleToFit(500, 450);
            document.add(newImage);
            document.close();
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        pdfDownloadedLabel.setVisible(true);
        
    }

    @FXML
    private void handleCheckAnswersAction(MouseEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("StudentQuestionFeedback.fxml"));
        Parent root = (Parent) loader.load();
        StudentQuestionFeedbackController studentQuestionFeedbackController = loader.<StudentQuestionFeedbackController> getController();
        studentQuestionFeedbackController.setQuiz(quiz);
        studentQuestionFeedbackController.showAnswers();
        Stage stage = (Stage) checkAnswers.getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    private void logOutAction(MouseEvent event) throws IOException {
                Parent root = FXMLLoader.load(getClass().getResource("StudentLoginPage.fxml"));
                Stage stage = (Stage) logOutButton.getScene().getWindow();
                Scene scene = new Scene(root);
                stage.setScene(scene);
                stage.show();
    }
}
