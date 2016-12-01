/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package qcas;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import qcasMode.InputFileReader;
import qcasMode.databaseConnection;

/**
 * FXML Controller class
 *
 * @author Meeth
 */
public class TeacherActionController implements Initializable {

    @FXML
    private Button logoutButton ;
    @FXML
    private ImageView importCSVButton;
    @FXML
    private Label questionUploadedLabel;
    @FXML
    private ImageView getDashboardButton;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
    @FXML
    public void logoutTeacher() throws IOException
    {
        Parent root = FXMLLoader.load(getClass().getResource("TeacherLoginPage.fxml"));
                Stage stage = (Stage) logoutButton.getScene().getWindow();
                Scene scene = new Scene(root);
                stage.setScene(scene);
                stage.show();
    }
    
    @FXML
    private void handleImportCSVAction(MouseEvent event) throws IOException, SQLException {
        try{
        FileChooser fileChooser = new FileChooser();
        Stage stage = (Stage) importCSVButton.getScene().getWindow();
        stage.show();
        File f=fileChooser.showOpenDialog(stage);
        String path = f.getAbsolutePath();
        importQuestions(path);
        questionUploadedLabel.setVisible(true);
        } catch(NullPointerException e){
            System.out.println("You cancelled the import window" + e);
        }
    }
    
    
    public void importQuestions(String filePath) throws IOException, SQLException{
            databaseConnection dBconn = new databaseConnection("jdbc:mysql://qcasrohan.caswkasqdmel.ap-southeast-2.rds.amazonaws.com:3306/QCASRohan", "rohan", "rohantest");
            Connection con = dBconn.getConnection();
            PreparedStatement preparedStmt;
            InputFileReader reader = new InputFileReader(); //reader object to read from the input file
            reader.readFile(filePath); //reading the file containing quiz questions
            List<String[]> questionDetails = reader.wordsArray; // Each string from the csv file is stored in an ArrayList
            ArrayList<String> insertQueries = new ArrayList();
            int mastercounter = 0;
//             Inserting question details from the csv file words array into the insert String to be run to store values in a database
            for (int j = 0; j < questionDetails.size(); j++) {
                String countQuery = "select count(*) as count from QCASRohan.QUESTION";
                preparedStmt = con.prepareStatement(countQuery);
                ResultSet rsc = preparedStmt.executeQuery();
                while (rsc.next()) {
                    mastercounter = rsc.getInt("count");
                }
                String checkQuestionQuery = "select * from QCASRohan.QUESTION WHERE DESCRIPTION = ?";
                preparedStmt = con.prepareStatement(checkQuestionQuery);
                preparedStmt.setString(1, questionDetails.get(j)[2]);
                rsc = preparedStmt.executeQuery();
                if (!rsc.next()){                
                    String insertString = "INSERT INTO QCASRohan.QUESTION VALUES (?,?,?,?,?,?,?,?,?,?,?,?);";
                    preparedStmt = con.prepareStatement(insertString);
                    for (int i = 0; i < questionDetails.get(j).length; i++) {
                        preparedStmt.setString(i + 1, questionDetails.get(j)[i]);
                    }
                    for (int k = questionDetails.get(j).length; k < 11; k++) {
                        preparedStmt.setString(k + 1, "");
                    }
                    mastercounter += 1;
                    preparedStmt.setString(12, "" + (mastercounter));
                    preparedStmt.executeUpdate();
                }
            }
    }

    @FXML
    private void goToInstructorDashboard(MouseEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("InstructorDashboard.fxml"));
        Stage stage = (Stage) getDashboardButton.getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
}
