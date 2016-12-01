/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package qcas;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import qcasMode.*;

/**
 * FXML Controller class
 *
 * @author Meeth
 */
public class StudentTestDashboardController implements Initializable {

    @FXML
    private Button startTestButton;
    @FXML
    private Button LogoutButton;
    @FXML
    private Button BackButton;
    String difficultyLevel;
    @FXML
    private ComboBox studentTComboBox;
    int numberOfQuestions;
    @FXML
    private RadioButton RB1;
    @FXML
    private RadioButton RB2;
    @FXML
    private RadioButton RB3;
    @FXML
    private ToggleGroup studentTG;
    TestController t;
    private ObservableList comboData = FXCollections.observableArrayList();
    @FXML
    private RadioButton RB4;
    private Student student;
    @FXML
    private Label unselectedParameterLabel;

    public StudentTestDashboardController() throws IOException, SQLException {

    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        comboData.add(15);
        comboData.add(20);
        comboData.add(25);
        studentTComboBox.setItems(comboData);

        RB1.setToggleGroup(studentTG);
        RB2.setToggleGroup(studentTG);
        RB3.setToggleGroup(studentTG);
    }

    @FXML
    private void goToTest(MouseEvent event) throws IOException, SQLException {

        unselectedParameterLabel.setVisible(false);
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("Test.fxml"));
        if (studentTComboBox.getSelectionModel().getSelectedItem() != null) {
            numberOfQuestions = (int) studentTComboBox.getSelectionModel().getSelectedItem();
            if (RB1.isSelected()) {
                difficultyLevel = "E";
            }
            if (RB2.isSelected()) {
                difficultyLevel = "M";
            }
            if (RB3.isSelected()) {
                difficultyLevel = "H";
            }
            if (RB4.isSelected()) {
                difficultyLevel = "MI";
            }
            Parent root = (Parent) loader.load();
            if (difficultyLevel != null) {
                TestController testController = loader.<TestController>getController().initialize(difficultyLevel, numberOfQuestions, student);
                Stage stage = (Stage) startTestButton.getScene().getWindow();
                Scene scene = new Scene(root);
                stage.setScene(scene);
                stage.getOwner();
                stage.show();
            } 
            else {
                unselectedParameterLabel.setVisible(true);
            }
        }
        else {
                unselectedParameterLabel.setVisible(true);
            }

    }

    @FXML
    private void goBack() throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("StudentAction.fxml"));
        Stage stage = (Stage) BackButton.getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    private void logoutStudent() throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("StudentLoginPage.fxml"));
        Stage stage = (Stage) LogoutButton.getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void setStudent(Student student) {
        this.student = student;
    }

}
