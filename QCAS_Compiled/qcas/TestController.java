/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package qcas;

import java.awt.CheckboxGroup;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBase;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Control;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javax.swing.AbstractButton;
import javax.swing.ButtonGroup;
import javax.swing.JRadioButton;
import qcasMode.*;
/**
 * FXML Controller class
 *
 * @author Meeth
 */
public class TestController implements Initializable {
    
    @FXML
    private Hyperlink testNext;
    @FXML
    private Hyperlink testPrev;
    private Quiz quiz;
    @FXML
    private RadioButton testRB1;
    @FXML
    private RadioButton testRB2;
    @FXML
    private RadioButton testRB3;
    @FXML
    private RadioButton testRB4;
    @FXML
    private CheckBox testCB1;
    @FXML
    private CheckBox testCB2;
    @FXML
    private CheckBox testCB3;
    @FXML
    private CheckBox testCB4;
    @FXML
    private RadioButton testRBTF1;
    @FXML
    private RadioButton testRBTF2;
   
    @FXML
    private TextArea testTextArea;
    @FXML
    private ToggleGroup testTGRB;
    @FXML
    private ToggleGroup testTGRBTF;
    private String difficultyLevel;
    private int numberOfQuestions;
    private int currentQuestionCount=0;
    private ArrayList<Control> checkBoxGroup = new ArrayList();
    private ArrayList<Control> radioButtonGroup = new ArrayList();
    private ArrayList<Control> trueFalseGroup = new ArrayList();
    private ArrayList<Control> fillInTheBlankGroup = new ArrayList();
    @FXML
    private Label MAanswerLabel ;
    @FXML
    private Label MCanswerLabel ;
    @FXML
    private Label TFanswerLabel ;
    @FXML
    private Label FIBanswerLabel ;
    public static int secs;
    public static int maxTime;
    @FXML
    private TextArea TimerTextArea ;
    private Label continueButton ; // Incorporate into FXML
    @FXML
    private TextField fibAnswerBlank;
    private Student student;
    @FXML
    private Button submitButton;
    @FXML
    private Label timeUpLabel;
    @FXML
    private AnchorPane anchorPane;
    @FXML
    private Label questionNumber;
   

    public TestController() throws IOException, SQLException {
        
    }
    /**
     * Initializes the controller class.
     */
    public TestController initialize(String difficultyLevelString,int numberOfQuestion,  Student student) throws IOException, SQLException {
        testRB1.setToggleGroup(testTGRB);
        testRB2.setToggleGroup(testTGRB);
        testRB3.setToggleGroup(testTGRB);
        testRB4.setToggleGroup(testTGRB); 
        radioButtonGroup.add(testRB1);
        radioButtonGroup.add(testRB2);
        radioButtonGroup.add(testRB3);
        radioButtonGroup.add(testRB4);
        testRBTF1.setToggleGroup(testTGRBTF);
        testRBTF2.setToggleGroup(testTGRBTF);
        trueFalseGroup.add(testRBTF1);
        trueFalseGroup.add(testRBTF2);
        checkBoxGroup.add(testCB1);
        checkBoxGroup.add(testCB2);
        checkBoxGroup.add(testCB3);
        checkBoxGroup.add(testCB4);
        fillInTheBlankGroup.add(fibAnswerBlank);
        testTextArea.setWrapText(true) ;
        setDifficultyLevel(difficultyLevelString);
        setNumberOfQuestions(numberOfQuestion);
        setStudent(student);
        this.quiz = new Quiz(difficultyLevel,numberOfQuestions, "jdbc:mysql://qcasrohan.caswkasqdmel.ap-southeast-2.rds.amazonaws.com:3306/QCASRohan?zeroDateTimeBehavior=convertToNull", "rohan", "rohantest", 0.1, 0.1,0.4, 0.4);
        currentQuestionCount += 1;
        testTextArea.setText(quiz.questions.get(currentQuestionCount-1).description);
        setAnswerOptions(getActiveGroup(quiz.questions.get(currentQuestionCount-1).abbreviation),quiz.questions.get(0).answerChoices);
        getOptionsPane(quiz.questions.get(currentQuestionCount-1));
        setPrevNextButton(currentQuestionCount);
        
        
        //Timer 
        secs=(numberOfQuestion*5)+1;
        final ScheduledExecutorService service = Executors.newSingleThreadScheduledExecutor();
        service.scheduleWithFixedDelay(new Runnable()
        {
           @Override
           public void run()
          {
            secs--;
            TimerTextArea.setText((Integer.toString(secs/60))+ " : "
            +Integer.toString(secs%60));

          if(secs==0){
              service.shutdown() ;
              anchorPane.setVisible(false) ;
              timeUpLabel.setVisible(true);
              
              
          }
        }
      }, 0, 1, TimeUnit.SECONDS);
        return this;
    }    
    
    public void setDifficultyLevel(String difficultyLevelString){
        this.difficultyLevel = difficultyLevelString;
        
    }
    
    public void setButtonsVisible(ArrayList<Control> B){
        for (int i=0;i<B.size();i++){
            B.get(i).setVisible(true);
        }
    }
    
    public void setButtonsInvisible(ArrayList<Control> B){
        for (int i=0;i<B.size();i++){
            B.get(i).setVisible(false);
        }
    }
    
    public void unselectCheckBoxes(ArrayList<Control> B){
        for (int i=0;i<B.size();i++){
            CheckBox c1 = (CheckBox) B.get(i);
            c1.setSelected(false);
        }
    }
    
    
    public ArrayList<String> getSelectedCheckBoxes(ArrayList<Control> B){
        ArrayList<String> answers = new ArrayList();
        for (int i=0;i<B.size();i++){
            CheckBox c1 = (CheckBox) B.get(i);
            if (c1.isSelected()){
                answers.add(c1.getText());
            }
        }
        return answers;
    }
    
    public void setNumberOfQuestions(int numberOfQuestions){
        this.numberOfQuestions = numberOfQuestions;
        
    }
    
    public void setAnswerOptions(ArrayList<Control> B, HashMap answerChoices){
        for (int i=0;i<B.size();i++){
            if (getActiveGroup(quiz.questions.get(currentQuestionCount-1).abbreviation)!=trueFalseGroup){
                if (B.get(i).getClass()==RadioButton.class){
                    ((RadioButton) B.get(i)).setText(answerChoices.keySet().toArray()[i].toString());
                }

                if (B.get(i).getClass()==CheckBox.class){
                    ((CheckBox) B.get(i)).setText(answerChoices.keySet().toArray()[i].toString());
                }
            }
        }
        
    }
    
    public void setPrevNextButton(int currentQuestionCount){
        if (currentQuestionCount==1){
            testNext.setVisible(true);
            testPrev.setVisible(false);
        }
        else if (currentQuestionCount==numberOfQuestions){
            testNext.setVisible(false);
            testPrev.setVisible(true);
            }
        else{
            testNext.setVisible(true);
            testPrev.setVisible(true);
        }
    }
    
    public ArrayList<Control> getActiveGroup(String questionType){
        if(questionType.equals("MC")){
            
            return radioButtonGroup;
        }
        else if(questionType.equals("MA")){
            return checkBoxGroup;
        }
        else if(questionType.equals("TF")){
            return trueFalseGroup;
        }
        else if(questionType.equals("FIB")){
            return fillInTheBlankGroup;
        }
        return trueFalseGroup;
    }
    
    public void getOptionsPane(Question q){
        if(q.abbreviation.equals("MC")){
            MCanswerLabel.setVisible(true) ;
            MAanswerLabel.setVisible(false) ;
            TFanswerLabel.setVisible(false) ;
            FIBanswerLabel.setVisible(false) ;
            questionNumber.setText("Question: " + currentQuestionCount);
            testTGRB.getToggles().stream().map((toggle) -> (ToggleButton)toggle).forEach((button) -> {button.setSelected(false);});
            testTGRBTF.getToggles().stream().map((toggle) -> (ToggleButton)toggle).forEach((button) -> {button.setSelected(false);});
            unselectCheckBoxes(checkBoxGroup);
            setButtonsInvisible(checkBoxGroup);
            testTGRB.getToggles().stream().map((toggle) -> (ToggleButton)toggle).forEach((button) -> {button.setVisible(true);});
            testTGRBTF.getToggles().stream().map((toggle) -> (ToggleButton)toggle).forEach((button) -> {button.setVisible(false);});
            fibAnswerBlank.setVisible(false);
            HashMap<Question,ArrayList<String>> answers = this.getAnswers(q, radioButtonGroup);          
            if (answers.get(q)!=null){
                testTGRB.getToggles().stream().map((toggle) -> (ToggleButton)toggle).forEach((button) -> {if (answers.get(q).contains(button.getText())){button.setSelected(true);};});
            }
        }
        else if(q.abbreviation.equals("MA")){
            MAanswerLabel.setVisible(true) ;
            MCanswerLabel.setVisible(false) ;
            TFanswerLabel.setVisible(false) ;
            FIBanswerLabel.setVisible(false) ;
            questionNumber.setText("Question: " + currentQuestionCount);
            testTGRB.getToggles().stream().map((toggle) -> (ToggleButton)toggle).forEach((button) -> {button.setSelected(false);});
            testTGRBTF.getToggles().stream().map((toggle) -> (ToggleButton)toggle).forEach((button) -> {button.setSelected(false);});
            unselectCheckBoxes(checkBoxGroup);
            setButtonsVisible(checkBoxGroup);
            testTGRB.getToggles().stream().map((toggle) -> (ToggleButton)toggle).forEach((button) -> {button.setVisible(false);});
            testTGRBTF.getToggles().stream().map((toggle) -> (ToggleButton)toggle).forEach((button) -> {button.setVisible(false);});
            fibAnswerBlank.setVisible(false);
            HashMap<Question,ArrayList<String>> answers = this.getAnswers(q, checkBoxGroup);
            if (answers.get(q)!=null){
            for (int i=0;i<checkBoxGroup.size();i++){
                CheckBox c1 = (CheckBox) checkBoxGroup.get(i);
                if (answers.get(q).contains(c1.getText())){
                    c1.setSelected(true);
                    }
                }
            }
        }
        else if(q.abbreviation.equals("TF")){
            TFanswerLabel.setVisible(true);
            MAanswerLabel.setVisible(false) ;
            MCanswerLabel.setVisible(false) ;
            FIBanswerLabel.setVisible(false) ;
            questionNumber.setText("Question: " + currentQuestionCount);
            testTGRB.getToggles().stream().map((toggle) -> (ToggleButton)toggle).forEach((button) -> {button.setSelected(false);});
            testTGRBTF.getToggles().stream().map((toggle) -> (ToggleButton)toggle).forEach((button) -> {button.setSelected(false);});
            unselectCheckBoxes(checkBoxGroup);
            setButtonsInvisible(checkBoxGroup);
            testTGRB.getToggles().stream().map((toggle) -> (ToggleButton)toggle).forEach((button) -> {button.setVisible(false);});
            testTGRBTF.getToggles().stream().map((toggle) -> (ToggleButton)toggle).forEach((button) -> {button.setVisible(true);});
            fibAnswerBlank.setVisible(false);
            HashMap<Question,ArrayList<String>> answers = this.getAnswers(q, trueFalseGroup);
            if (answers.get(q)!=null){
                testTGRBTF.getToggles().stream().map((toggle) -> (ToggleButton)toggle).forEach((button) -> {if (answers.get(q).contains(button.getText())){button.setSelected(true);};});
            }

        }
        else if(q.abbreviation.equals("FIB")){
            FIBanswerLabel.setVisible(true);
            MAanswerLabel.setVisible(false) ;
            TFanswerLabel.setVisible(false) ;
            MCanswerLabel.setVisible(false) ;
            questionNumber.setText("Question: " + currentQuestionCount);
            testTGRB.getToggles().stream().map((toggle) -> (ToggleButton)toggle).forEach((button) -> {button.setSelected(false);});
            testTGRBTF.getToggles().stream().map((toggle) -> (ToggleButton)toggle).forEach((button) -> {button.setVisible(false);});
            unselectCheckBoxes(checkBoxGroup);
            setButtonsInvisible(checkBoxGroup);
            testTGRB.getToggles().stream().map((toggle) -> (ToggleButton)toggle).forEach((button) -> {button.setVisible(false);});
            testTGRBTF.getToggles().stream().map((toggle) -> (ToggleButton)toggle).forEach((button) -> {button.setVisible(false);});
            fibAnswerBlank.clear();
            fibAnswerBlank.setVisible(true);
            HashMap<Question,ArrayList<String>> answers = this.getAnswers(q, fillInTheBlankGroup);
            if (answers.get(q)!=null){
                for (int i=0;i<fillInTheBlankGroup.size();i++){
                    TextField fillInTheBlank = (TextField) fillInTheBlankGroup.get(i);
                    if (answers.get(q).get(0)!="null"){
                        fillInTheBlank.setText(answers.get(q).get(0));
                    }
                }
            }
        }
    }
    
    
    @FXML
    public void handleNextAction(MouseEvent event){
        setAnswers(quiz.questions.get(currentQuestionCount-1),getActiveGroup(quiz.questions.get(currentQuestionCount-1).abbreviation));
        currentQuestionCount+=1;
        setPrevNextButton(currentQuestionCount);
        if (currentQuestionCount<=numberOfQuestions){
            testTextArea.setText(quiz.questions.get(currentQuestionCount-1).description);            
            setAnswerOptions(getActiveGroup(quiz.questions.get(currentQuestionCount-1).abbreviation),quiz.questions.get(currentQuestionCount-1).answerChoices);            
            getOptionsPane(quiz.questions.get(currentQuestionCount-1));
        }
    }
    
    @FXML
    public void handlePrevAction(MouseEvent event){
        setAnswers(quiz.questions.get(currentQuestionCount-1),getActiveGroup(quiz.questions.get(currentQuestionCount-1).abbreviation));
        currentQuestionCount-=1;
        setPrevNextButton(currentQuestionCount);
        if (currentQuestionCount<=numberOfQuestions){
            testTextArea.setText(quiz.questions.get(currentQuestionCount-1).description);            
            setAnswerOptions(getActiveGroup(quiz.questions.get(currentQuestionCount-1).abbreviation),quiz.questions.get(currentQuestionCount-1).answerChoices);            
            getOptionsPane(quiz.questions.get(currentQuestionCount-1));
        }
    }
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        
    }
    
    public void setAnswers(Question q, ArrayList<Control> B){
        this.quiz.getResult().getAnswers().put(q,getSelectedAnswerChoices(B) );
        
    }
    
    public HashMap<Question,ArrayList<String>> getAnswers(Question q, ArrayList<Control> B){
        return this.quiz.getResult().getAnswers();
    }

    public ArrayList<String> getSelectedAnswerChoices(ArrayList<Control> B){
        ArrayList<String> answers = new ArrayList();
        for (int i=0;i<B.size();i++){
          if (B.get(i).getClass()==RadioButton.class){
                RadioButton r= (RadioButton) B.get(i);
                if(r.isSelected()){
                    answers.add(r.getText());
                }
                else{
                    answers.add("null");}
                }
            if (B.get(i).getClass()==CheckBox.class){
                CheckBox c= (CheckBox)B.get(i);
                if(c.isSelected()){
                    answers.add(c.getText());
                }
                else
                    answers.add("null");
                }
            if (B.get(i).getClass()==TextField.class){
                TextField c= (TextField)B.get(i);
                if(!c.getText().equals("")){
                    answers.add(c.getText());
                }
                else
                    answers.add("null");
                }
        }
        
        return answers;
    }

  @FXML
    public void handleSubmitAction(MouseEvent event) throws IOException{
        setAnswers(quiz.questions.get(currentQuestionCount-1),getActiveGroup(quiz.questions.get(currentQuestionCount-1).abbreviation));
        List<Question> questionObjects = new ArrayList<>();
        ArrayList<ArrayList<String>> value = new ArrayList();
        for (Map.Entry<Question, ArrayList<String>> entry : quiz.getResult().getAnswers().entrySet()) {
                questionObjects.add(entry.getKey());
                value.add(entry.getValue());
        }
        
        for (int i =0; i<quiz.questions.size();i++){
            if (!questionObjects.contains(quiz.questions.get(i))){
                questionObjects.add(quiz.questions.get(i));
                value.add(null);
                quiz.getResult().getAnswers().put(quiz.questions.get(i),null);
            }
        }
        
        quiz.insertResults(questionObjects, value,"jdbc:mysql://qcasrohan.caswkasqdmel.ap-southeast-2.rds.amazonaws.com:3306/QCASRohan?zeroDateTimeBehavior=convertToNull", "rohan", "rohantest",student);
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("ContinueAfterTestPage.fxml"));
        Parent root = (Parent) loader.load();
        ContinueAfterTestPageController continueAfterTestPageController = loader.<ContinueAfterTestPageController> getController();
        continueAfterTestPageController.setQuiz(quiz);        
        Stage stage = (Stage) submitButton.getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    
    }

    private void setStudent(Student student) {
        this.student = student;
    }
}