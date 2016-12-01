/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package qcas;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import qcasMode.*;

/**
 * FXML Controller class
 *
 * @author Meeth
 */
public class StudentLoginPageController implements Initializable {

    @FXML
    private Button LoginButton;

    @FXML
    private Button BackButton;

    @FXML
    private TextField userName;

    @FXML
    private TextField password;

    @FXML
    private Label WrongCredLabel;

    @FXML
    private Button SignUpButton;
    private Student student;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

    @FXML
    private void checkLogin(MouseEvent event) throws IOException {
        try {
            // TODO
            databaseConnection dBconn = new databaseConnection("jdbc:mysql://qcasrohan.caswkasqdmel.ap-southeast-2.rds.amazonaws.com:3306/QCASRohan", "rohan", "rohantest");
            Connection con = dBconn.getConnection();
            Statement stmt = con.createStatement();

            String userNameToCheck, pw;

            userNameToCheck = userName.getText();
            pw = password.getText();
            
            
            String checkQuery = "SELECT StudentId FROM QCASRohan.STUDENT WHERE StudentId ='" + userNameToCheck + "' AND password='" + pw + "'";
            ResultSet rs;
            rs = stmt.executeQuery(checkQuery);
            int flag = 0;
            if (!rs.next()) {
                WrongCredLabel.setVisible(true); // wrong credentials...
            } else // if the username exists
            {
                checkQuery = "SELECT FIRSTNAME, LASTNAME FROM QCASRohan.STUDENT WHERE StudentId ='" + userNameToCheck + "' AND password='" + pw + "'";
                rs = stmt.executeQuery(checkQuery);
                while(rs.next()){
                    Student student = new Student(userNameToCheck, pw,rs.getString("FirstName"),rs.getString("LastName"));
                    setStudent(student);
                }
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(getClass().getResource("StudentAction.fxml"));
                Parent root = (Parent) loader.load();
                StudentActionController studentActionController = loader.<StudentActionController> getController();
                studentActionController.setStudent(student);
                Stage stage = (Stage) LoginButton.getScene().getWindow();
                Scene scene = new Scene(root);
                stage.setScene(scene);
                stage.show();
            }

        } catch (SQLException ex) {
            System.out.println("Error" + ex);
        }
    }

    @FXML
    private void goToSignupPage(MouseEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("SignupPage.fxml"));
        Stage stage = (Stage) SignUpButton.getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    private void goBack(MouseEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("Login.fxml"));
        Stage stage = (Stage) BackButton.getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void setStudent(Student student) {
        this.student = student;
    }

}
