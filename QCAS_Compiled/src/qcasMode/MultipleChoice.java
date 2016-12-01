
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
public class MultipleChoice extends Question {
    
    public MultipleChoice(String abbreviation, String difficulty, String description, HashMap answerChoices, Integer answer, int questionNumber) {
        super(abbreviation, difficulty, description, answerChoices, answer, questionNumber);
    }

    @Override
    boolean checkValidity(ArrayList<String> ans) {
        boolean check = false;
        for (int i = 0; i < ans.size(); i++) {
            if (answerChoices.get(ans.get(i))!=null){
                if (answerChoices.get(ans.get(i)).equals("correct")) {
                    check = true;
                }
            }
        }
        return check;
    }
    
    
}