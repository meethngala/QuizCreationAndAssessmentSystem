
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package qcasMode;


import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.StringTokenizer;
import com.opencsv.CSVReader;
import java.io.FileReader;


/**
 *Class to take in an input file and store data in each row in fieldsLines and break each row into elements in wordsarray
 * @author paridhichoudhary
 */
public class InputFileReader {
    
    /**
     *Contains each row of text file
     */
    public ArrayList<String> fieldsLines = new ArrayList();

    /**
     *Breaks each row into elements and stores into array list of string arrays. 
     */
    public List<String[]> wordsArray = new ArrayList();
    
    /**
     *Reads the file line by line and stores data in fieldslines and wordsarray
     * @param filePath
     * @throws IOException
     */
    public void readFile(String filePath) throws  IOException{
        FileReader filereader = new FileReader(filePath);
        //BufferedReader for the file
        BufferedReader reader = new BufferedReader(filereader);
        CSVReader reader1 = new CSVReader(new FileReader(filePath));
        wordsArray = reader1.readAll();

//        int i =0;
//        String line;
//        //Read data from a file
//        while ((line=reader.readNext())!=null) {
//            fieldsLines.add(i,line); // each row is added to the arraylist
//            i++;
//        }
//        
//        for (i=0;i<fieldsLines.size();i++){
//            int j =0;
//            StringTokenizer str = new StringTokenizer(fieldsLines.get(i),",");
//            String[] wordsArray = new String[str.countTokens()]; // each row is broken into elements using a delimiter and each element is stored in wordsArray
//            while (str.hasMoreTokens()){
//                wordsArray[j] = str.nextToken(); 
//                j++;
//            }
//            this.wordsArray.add(wordsArray);   // returns the wordsarray
        }
    }