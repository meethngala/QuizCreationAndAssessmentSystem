
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package qcasMode;

import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * @author paridhichoudhary
 */
public class MultipleAnswer extends Question {

    public MultipleAnswer(String abbreviation, String difficulty, String description, HashMap answerChoices, Integer answer, int questionNumber) {
        super(abbreviation, difficulty, description, answerChoices, answer, questionNumber);
    }

    public boolean checkValidity(ArrayList<String> ans) {
        boolean check = false;
        ArrayList<String> selectedAnswers = new ArrayList();
        for (int i = 0; i < ans.size(); i++) {
            if (!ans.get(i).equals("null")){
                selectedAnswers.add(ans.get(i));
            }
        }
        for (int i = 0; i < selectedAnswers.size(); i++){
            if (answerChoices.get(selectedAnswers.get(i))!=null){
                if (answerChoices.get(selectedAnswers.get(i)).equals("incorrect")){
                    check = false;
                }
                else{
                    check = true;
                }
            }
                
        }
        return check;
    }
    
    
}