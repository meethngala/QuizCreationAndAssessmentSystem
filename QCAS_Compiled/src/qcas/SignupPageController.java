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
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
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
 * @author WalterWhite
 */
public class SignupPageController implements Initializable {

    @FXML
    private Button SubmitButton;

    @FXML
    private TextField userName;

    @FXML
    private TextField firstName;

    @FXML
    private TextField lastName;

    @FXML
    private PasswordField password;

    @FXML
    private Button BackButton;

    @FXML
    private Button ContinueButton;

    @FXML
    private Label YouAreRegisteredLabel;

    @FXML
    private Label UsernameExistsLabel;

    @FXML
    private Label passwordsDontMatchLabel;

    @FXML
    private PasswordField password2;
    @FXML
    private Label detailsCheckLabel;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

    }

    @FXML
    private void Submit(MouseEvent event) throws IOException {
        try {
            // TODO
            databaseConnection dBconn = new databaseConnection("jdbc:mysql://qcasrohan.caswkasqdmel.ap-southeast-2.rds.amazonaws.com:3306/QCASRohan", "rohan", "rohantest");
            Connection con = dBconn.getConnection();
            Statement stmt = con.createStatement();

            String userNameToCheck, firstN, lastN, pw, pw2;

            userNameToCheck = userName.getText();
            firstN = firstName.getText();
            lastN = lastName.getText();
            pw = password.getText();
            pw2 = password2.getText();

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
                    YouAreRegisteredLabel.setVisible(true);
                    ContinueButton.setVisible(true);
                } else {
                    UsernameExistsLabel.setVisible(true);
                }
            } else {
                detailsCheckLabel.setVisible(true);
            }

        } catch (SQLException ex) {
            System.out.println("Error" + ex);
        }
    }

    @FXML
    private void goBack() throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("StudentLoginPage.fxml"));
        Stage stage = (Stage) BackButton.getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    private void continueToSignIn() throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("StudentLoginPage.fxml"));
        Stage stage = (Stage) ContinueButton.getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    private void checkPasswordMatch() throws IOException {
        String pw, pw2;
        pw = password.getText();
        pw2 = password2.getText();
        if (pw.equals(pw2)) {
            passwordsDontMatchLabel.setVisible(false);
        } else {
            passwordsDontMatchLabel.setVisible(true);
        }
    }

}
