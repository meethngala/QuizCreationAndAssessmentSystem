
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package qcasMode;

import java.io.IOException;
import java.sql.SQLException;

/**
 *
 * @author paridhichoudhary
 */
public class QCAS {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException, SQLException {
        // TODO code application logic here
        Quiz quiz = new Quiz("MI",4, "jdbc:mysql://qcasrohan.caswkasqdmel.ap-southeast-2.rds.amazonaws.com:3306/QCASRohan?zeroDateTimeBehavior=convertToNull", "rohan", "rohantest", 0.25, 0.25,0.25, 0.25); //Provide the details to conduct quiz
//        for (int i=0; i<quiz.questions.size();i++){
//            System.out.println(quiz.questions.get(i).abbreviation);
//        }
        //quiz.conductQuiz(); //Conduct the quiz

    }
    
}