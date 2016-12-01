/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package qcasMode;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author paridhichoudhary
 */
public class FillInTheBlank extends Question {
    
    public FillInTheBlank(String abbreviation, String difficulty, String description, HashMap answerChoices, Integer answer, int questionNumber) {
        super(abbreviation, difficulty, description, answerChoices, answer, questionNumber);
    }

    @Override
    boolean checkValidity(ArrayList<String> ans) {
        boolean check = false;
        int count =0;
        for (Map.Entry<String, String> entry : answerChoices.entrySet()) {
            entry.getKey();
            entry.getValue();
            count+=1;
            if (ans.get(0).toLowerCase().equals(entry.getKey().toLowerCase()) && count==1) {
                check = true;
            }
        }
        
        
        return check;
    }
    
    
}