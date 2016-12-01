/*
 * This class controls the student sign up page
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
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import qcasMode.*;

/**
 * FXML Controller class
 *
 * @author Khushboo Banwari
 */
public class SignupPageController implements Initializable {

    // Buttons
    @FXML
    private Button SubmitButton;
    @FXML
    private Button BackButton;
    @FXML
    private Button ContinueButton;
    // text fields
    @FXML
    private TextField userName;
    @FXML
    private TextField firstName;
    @FXML
    private TextField lastName;
    // passwordfield
    @FXML
    private PasswordField password;
    @FXML
    private PasswordField password2;
    //labels    
    @FXML
    private Label YouAreRegisteredLabel;
    @FXML
    private Label UsernameExistsLabel;
    @FXML
    private Label passwordsDontMatchLabel;
    @FXML
    private Label detailsCheckLabel;

    /**
     * Initializes the controller class
     *
     * @param url Reference of a URL Object
     * @param rb Reference of a ResourceBundle Object
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

    }

    /**
     * This method controls the action after the submit button is pressed     *
     * @param event Reference of a MouseEvent Object
     */
    @FXML
    private void Submit(MouseEvent event) throws IOException {
        try {
            // connect to the database
            databaseConnection dBconn = new databaseConnection("jdbc:mysql://qcasrohan.caswkasqdmel.ap-southeast-2.rds.amazonaws.com:3306/QCASRohan", "rohan", "rohantest");
            Connection con = dBconn.getConnection();
            Statement stmt = con.createStatement();

            String userNameToCheck, firstN, lastN, pw, pw2;
            // get the text entered in each of the text fields and passwordfields
            userNameToCheck = userName.getText();
            firstN = firstName.getText();
            lastN = lastName.getText();
            pw = password.getText();
            pw2 = password2.getText();
            // if any of the fields are left empty- dont insert
            // else insert into the database
            if (!userNameToCheck.equals("") && !firstN.equals("") && !lastN.equals("") && !pw.equals("") && !pw2.equals("")) {
                String checkQuery = "SELECT StudentId FROM QCASRohan.STUDENT WHERE StudentId = '" + userNameToCheck + "'";
                String insertQuery1 = "INSERT INTO STUDENT VALUES(?, ?, ?, ?)";

                ResultSet rs;
                rs = stmt.executeQuery(checkQuery);
                int flag = 0;

                if (!rs.next()) {
                    // new username 
                    // adding to the database 
                    PreparedStatement psInsert1 = con.prepareStatement(insertQuery1);
                    psInsert1.setString(1, userNameToCheck);
                    psInsert1.setString(2, lastN);
                    psInsert1.setString(3, firstN);
                    psInsert1.setString(4, pw);
                    psInsert1.executeUpdate();
                    UsernameExistsLabel.setVisible(false);  
                    YouAreRegisteredLabel.setVisible(true); // once registered, display this label
                    ContinueButton.setVisible(true);  // continue to next page button- visible
                } else {
                    UsernameExistsLabel.setVisible(true); // if the username already exists- display label
                }
            } else {
                detailsCheckLabel.setVisible(true); // if any of the fields are left empty
            }

        } catch (SQLException ex) {
            System.out.println("Error" + ex);
        }
    }

    
    /**
     * This method controls the action of the back button
     */
    @FXML
    private void goBack() throws IOException {
        // go back to the student login page
        Parent root = FXMLLoader.load(getClass().getResource("StudentLoginPage.fxml"));
        Stage stage = (Stage) BackButton.getScene().getWindow(); // set the stage
        Scene scene = new Scene(root);
        stage.setScene(scene);  // set the scene
        stage.show();
    }
    
    /**
     * This method controls the action of the Continue button - once the student registers
     */
    @FXML
    private void continueToSignIn() throws IOException {
        //  go to the student login page afte rthe student registers
        Parent root = FXMLLoader.load(getClass().getResource("StudentLoginPage.fxml"));
        Stage stage = (Stage) ContinueButton.getScene().getWindow(); // set the stage
        Scene scene = new Scene(root);
        stage.setScene(scene); // set the scene
        stage.show();
    }    
    /**
     * This method checks if the reentered password is the same as the password entered first
     */
    @FXML
    private void checkPasswordMatch() throws IOException {
        String pw, pw2;
        pw = password.getText();
        pw2 = password2.getText();
        if (pw.equals(pw2)) { // if they are equal
            passwordsDontMatchLabel.setVisible(false); // dont display the label
        } else {
            passwordsDontMatchLabel.setVisible(true); // display this label till the passwords match 
        }
    }

}
