
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package qcasMode;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.Scanner;
import java.util.Set;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Creates an list of question objects, establishes connections and conducts
 * quiz for the user
 *
 * @author paridhichoudhary
 */
public class Quiz {

    public ArrayList<Question> questions = new ArrayList();
    String grade = "Fail"; //Grade is the percentage of correct and incorrect answers 
    double score = 0.0;
    Result result = new Result();
    int numberOfQuestions;
    String difficultyLevel;
    databaseConnection connection;
    Statement stmt;
    PreparedStatement preparedStmt;
    int quizNumber;

    /**
     *
     * @param filepath : path of the file to read questions from
     * @param url : URL for database connection
     * @param username : username for the database
     * @param password : password for the database
     * @throws IOException
     * @throws SQLException
     */
    public Quiz(String difficultyLevel, int numberOfQuestions, String url, String username, String password, Double percentMAQuestions, Double percentMCQuestions, Double percentFIBQuestions, Double percentTFQuestions) throws IOException, SQLException {
        connection = new databaseConnection(url, username, password);
        stmt = connection.con.createStatement();
        setDifficultyLevel(difficultyLevel);
        setNumberOfQuestions(numberOfQuestions);
        setQuizNumber();
        setQuestions(percentMAQuestions, percentMCQuestions, percentFIBQuestions, percentTFQuestions,url,username,password);
        //conductQuiz();
    }

    /**
     * Returns list of questions for this quiz
     *
     * @return : list of questions for this quiz
     */
    public ArrayList<Question> getQuestions() {
        return questions;
    }

    /**
     * sets questions into the array list of questions using database table
     *
     * @param filepath : text file containing questions
     * @throws IOException
     * @throws SQLException
     */
    public void setQuestions(Double percentMAQuestions, Double percentMCQuestions, Double percentFIBQuestions, Double percentTFQuestions,String url, String username, String password) throws IOException, SQLException {
        try {
//            InputFileReader reader = new InputFileReader(); //reader object to read from the input file
//            reader.readFile(Filepath); //reading the file containing quiz questions
//            List<String[]> questionDetails = reader.wordsArray; // Each string from the csv file is stored in an ArrayList
//            ArrayList<String> insertQueries = new ArrayList();
//            int mastercounter = 0;
            // Dropping the table if already present
//            String createQueryTest = "CREATE TABLE QCASRohan.TEST"
//                + "("
//                + "Abbreviation VARCHAR(30000)" + ")";
//            stmt.execute(createQueryTest);
            // Inserting question details from the csv file words array into the insert String to be run to store values in a database
//            for (int j = 0; j < questionDetails.size(); j++) {
//                String countQuery = "select count(*) as count from QCASRohan.QUESTION";
//                preparedStmt = connection.con.prepareStatement(countQuery);
//                ResultSet rsc = preparedStmt.executeQuery();
//
//                while (rsc.next()) {
//                    mastercounter = rsc.getInt("count");
//                }
//                String insertString = "INSERT INTO QCASRohan.QUESTION VALUES (?,?,?,?,?,?,?,?,?,?,?,?)";
//                preparedStmt = connection.con.prepareStatement(insertString);
//                for (int j = 0; j < questionDetails.get(i).length; j++) {
//                    preparedStmt.setString(j+1, questionDetails.get(i)[j]);
//                }
////                preparedStmt
//                for (int i = 0; i < questionDetails.get(j).length; i++) {
//                    preparedStmt.setString(i + 1, questionDetails.get(j)[i]);
//                }
//                for (int k = questionDetails.get(j).length; k < 11; k++) {
//                    preparedStmt.setString(k + 1, "");
//                }
//                mastercounter += 1;
//                preparedStmt.setString(12, "" + mastercounter);
//                int counting = preparedStmt.executeUpdate();

//                preparedStmt.executeUpdate();
//            }
            int numberMAQuestions = (int) (percentMAQuestions * numberOfQuestions);
            int numberMCQuestions = (int) (percentMCQuestions * numberOfQuestions);
            int numberFIBQuestions = (int) (percentFIBQuestions * numberOfQuestions);
            int numberTFQuestions = numberOfQuestions - numberMAQuestions - numberMCQuestions - numberFIBQuestions;

            int questionCounter = 0;

            //Setting MA Questions
            String query = "SELECT * FROM QCASRohan.QUESTION WHERE DIFFICULTYLEVEL = ?"
                    + " AND QUESTIONTYPE = ?"
                    + " ORDER BY RAND()";
            preparedStmt = connection.con.prepareStatement(query); //Using prepared statement to make connection 
            preparedStmt.setString(1, difficultyLevel);
            preparedStmt.setString(2, "MA");
            String mixedQuery = "(SELECT * FROM QCASRohan.QUESTION "
                    + "WHERE DIFFICULTYLEVEL = ? "
                    + "AND QUESTIONTYPE = ? "
                    + "ORDER BY RAND() "
                    + "LIMIT ? )"
                    + "UNION "
                    + "(SELECT * FROM QCASRohan.QUESTION  "
                    + "WHERE DIFFICULTYLEVEL = ? "
                    + "AND QUESTIONTYPE = ? "
                    + "ORDER BY RAND() "
                    + "LIMIT ? )"
                    + "UNION "
                    + "(SELECT * FROM QCASRohan.QUESTION  "
                    + "WHERE DIFFICULTYLEVEL = ? "
                    + "AND QUESTIONTYPE = ? "
                    + "ORDER BY RAND() "
                    + "LIMIT ? )";
            if (difficultyLevel.equals("MI")) {
                preparedStmt = connection.con.prepareStatement(mixedQuery);
                preparedStmt.setString(1, "E");
                preparedStmt.setString(2, "MA");
                preparedStmt.setInt(3, (numberMAQuestions / 3));
                preparedStmt.setString(4, "M");
                preparedStmt.setString(5, "MA");
                preparedStmt.setInt(6, (numberMAQuestions / 3));
                preparedStmt.setString(7, "H");
                preparedStmt.setString(8, "MA");
                preparedStmt.setInt(9, (int) (numberMAQuestions - (2 / 3) * numberMAQuestions));
            }
            Integer answer = 0;
            ResultSet rs = preparedStmt.executeQuery(); // Executes the select all query and stores the result in a result set
            while (rs.next() && questionCounter < numberMAQuestions) {
                HashMap answerChoices = new HashMap(); //
                for (int i = 0; i < 4; i++) {
                    answerChoices.put(rs.getString("ANSWER" + (i + 1)), rs.getString("VALIDITY" + (i + 1)));
                }
                for (int i = 0; i < 4; i++) {
                    if (rs.getString("VALIDITY" + (i + 1)).equals("correct")) {
                        answer = (i + 1);
                    }
                }
                Question question = new MultipleAnswer(rs.getString("QUESTIONTYPE"), rs.getString("DIFFICULTYLEVEL"), rs.getString("DESCRIPTION"), answerChoices, answer, rs.getInt("pk_column")); //Creating a question object out of the information stored in the database
                this.questions.add(question); // Adding the question to the questions set of this quiz
                questionCounter += 1;
            }
            questionCounter = 0;
            preparedStmt.setString(2, "MC");
            if (difficultyLevel.equals("MI")) {
                preparedStmt = connection.con.prepareStatement(mixedQuery);
                preparedStmt.setString(1, "E");
                preparedStmt.setString(2, "MC");
                preparedStmt.setInt(3, (int) (numberMCQuestions / 3));
                preparedStmt.setString(4, "M");
                preparedStmt.setString(5, "MC");
                preparedStmt.setInt(6, (int) (numberMCQuestions / 3));
                preparedStmt.setString(7, "H");
                preparedStmt.setString(8, "MC");
                preparedStmt.setInt(9, (int) (numberMCQuestions - (2 / 3) * numberMCQuestions));
            }
            rs = preparedStmt.executeQuery(); // Executes the select all query and stores the result in a result set
            while (rs.next() && questionCounter < numberMCQuestions) {
                HashMap<String, String> answerChoices = new HashMap<String, String>(); //
                for (int i = 0; i < 4; i++) {
                    answerChoices.put(rs.getString("ANSWER" + (i + 1)), rs.getString("VALIDITY" + (i + 1)));
                }
                for (int i = 0; i < 4; i++) {
                    if (rs.getString("VALIDITY" + (i + 1)) == "correct") {
                        answer = (i + 1);
                    }
                }
                Question question = new MultipleChoice(rs.getString("QUESTIONTYPE"), rs.getString("DIFFICULTYLEVEL"), rs.getString("DESCRIPTION"), answerChoices, answer, rs.getInt("pk_column")); //Creating a question object out of the information stored in the database
                this.questions.add(question); // Adding the question to the questions set of this quiz
                questionCounter += 1;
            }
            questionCounter = 0;
            preparedStmt.setString(2, "TF");
            if (difficultyLevel.equals("MI")) {
                preparedStmt = connection.con.prepareStatement(mixedQuery);
                preparedStmt.setString(1, "E");
                preparedStmt.setString(2, "TF");
                preparedStmt.setInt(3, (int) (numberTFQuestions / 3));
                preparedStmt.setString(4, "M");
                preparedStmt.setString(5, "TF");
                preparedStmt.setInt(6, (int) (numberTFQuestions / 3));
                preparedStmt.setString(7, "H");
                preparedStmt.setString(8, "TF");
                preparedStmt.setInt(9, (int) (numberTFQuestions - (2 / 3) * numberTFQuestions));
            }
            rs = preparedStmt.executeQuery(); // Executes the select all query and stores the result in a result set
            while (rs.next() && questionCounter < numberTFQuestions) {
                HashMap<String,String> answerChoices = new LinkedHashMap(); //
                for (int i = 0; i < 4; i++) {
                    answerChoices.put(rs.getString("ANSWER" + (i + 1)), rs.getString("VALIDITY" + (i + 1)));
                }
                for (int i = 0; i < 4; i++) {
                    if (rs.getString("VALIDITY" + (i + 1)) == "correct") {
                        answer = (i + 1);
                    }
                }
                Question question = new TrueFalse(rs.getString("QUESTIONTYPE"), rs.getString("DIFFICULTYLEVEL"), rs.getString("DESCRIPTION"), answerChoices, answer, rs.getInt("pk_column")); //Creating a question object out of the information stored in the database
                this.questions.add(question); // Adding the question to the questions set of this quiz
                questionCounter += 1;
            }
            questionCounter = 0;
            preparedStmt.setString(2, "FIB");
            if (difficultyLevel.equals("MI")) {
                preparedStmt = connection.con.prepareStatement(mixedQuery);
                preparedStmt.setString(1, "E");
                preparedStmt.setString(2, "FIB");
                preparedStmt.setInt(3, (int) (numberFIBQuestions / 3));
                preparedStmt.setString(4, "M");
                preparedStmt.setString(5, "FIB");
                preparedStmt.setInt(6, (int) (numberFIBQuestions / 3));
                preparedStmt.setString(7, "H");
                preparedStmt.setString(8, "FIB");
                preparedStmt.setInt(9, (int) (numberFIBQuestions - (2 / 3) * numberFIBQuestions));
            }
            rs = preparedStmt.executeQuery(); // Executes the select all query and stores the result in a result set
            while (rs.next() && questionCounter < numberFIBQuestions) {
                HashMap<String,String> answerChoices = new LinkedHashMap(); //
                for (int i = 0; i < 4; i++) {
                    System.out.println(rs.getString("ANSWER" + (i + 1)));
                    System.out.println(rs.getString("VALIDITY" + (i + 1)));
                    System.out.println("\n\n\n");
                    answerChoices.put(rs.getString("ANSWER" + (i + 1)), rs.getString("VALIDITY" + (i + 1)));
                }
                for (int i = 0; i < 4; i++) {
                    if (rs.getString("VALIDITY" + (i + 1)) == "correct") {
                        answer = (i + 1);
                    }
                }
                Question question = new FillInTheBlank(rs.getString("QUESTIONTYPE"), rs.getString("DIFFICULTYLEVEL"), rs.getString("DESCRIPTION"), answerChoices, answer, rs.getInt("pk_column")); //Creating a question object out of the information stored in the database
                this.questions.add(question); // Adding the question to the questions set of this quiz
                questionCounter += 1;

            }
            Collections.shuffle(questions);
        } catch (SQLException e) {
            System.out.println(e + "Connection Not Established"); // Error Handling: Handling the error in case SQL query does not execute
        }
    }

    /**
     * Method that provides the user quiz questions and and takes their answer;
     * also counts the number of right and wrong answers
     */
    public void conductQuiz(String url, String username, String password, Student student) {

        Scanner input = new Scanner(System.in);
        Random rand = new Random();
        int count = 0;
//        int rightCount = 0;

        while (count < numberOfQuestions) {
            int pickIndex = rand.nextInt(this.questions.size()); // random question picked from the questions set
            Question question = questions.get(pickIndex);
            questions.remove(pickIndex);
            int question_type = 0;

            if (question.abbreviation.equals("MA")) {
                question_type = 1;
            } else if (question.abbreviation.equals("MC")) {
                question_type = 2;
            } else if (question.abbreviation.equals("TF")) {
                question_type = 3;
            } else if (question.abbreviation.equals("FIB")) {
                question_type = 4;
            }
            System.out.println("Question:" + question.description + "?"); //Printing the question to the user
            Object[] answerChoicesKey = question.answerChoices.keySet().toArray();
            ArrayList<String> userAnswer = new ArrayList();

            switch (question_type) {
                case 1:

                    System.out.println("Following are the choices for answers: ");
                    System.out.println("a. " + answerChoicesKey[0] + "\n" + "b." + answerChoicesKey[1] + "\n" + "c." + answerChoicesKey[2] + "\n" + "d." + answerChoicesKey[3]);
                    userAnswer.add(input.nextLine());
                    userAnswer.add(input.nextLine());
                    break;
                case 2:
                    System.out.println("Following are the choices for answers: ");
                    System.out.println("a. " + answerChoicesKey[0] + "\n" + "b." + answerChoicesKey[1] + "\n" + "c." + answerChoicesKey[2] + "\n" + "d." + answerChoicesKey[3]);
                    userAnswer.add(input.nextLine());
                    break;
                case 3:
                    System.out.println("a. True");
                    System.out.println("b. False");
                    userAnswer.add(input.nextLine());
                    break;
                case 4:
                    userAnswer.add(input.nextLine());
                    break;
            }

            result.answers.put(question, userAnswer);
            count += 1;
        }
        List<Question> questionObjects = new ArrayList<>();
        ArrayList<ArrayList<String>> value = new ArrayList();
        for (Map.Entry<Question, ArrayList<String>> entry : result.answers.entrySet()) {

            questionObjects.add(entry.getKey());
//                String key = entry.getKey();
            value.add(entry.getValue());
        }
        insertResults(questionObjects, value,url,username,password,student);

    }

    public void insertResults(List<Question> questionObjects, ArrayList<ArrayList<String>> value, String url, String username, String password, Student student ) {
        try {
            connection = new databaseConnection(url,username,password);
            stmt = connection.con.createStatement();
            String insertQuizQuestionQuery = "";
            int rightCount = 0;

            for (int i = 0; i < questionObjects.size(); i++) {
                if (value.get(i)!=null){
                    if (questionObjects.get(i).checkValidity(value.get(i))) {
                        rightCount += 1;
                        insertQuizQuestionQuery = "INSERT INTO QCASRohan.QUIZ_QUESTION VALUES(" + quizNumber + "," + questionObjects.get(i).questionNumber + "," + "'correct');";
                        stmt.executeUpdate(insertQuizQuestionQuery);
                    } else {
                        insertQuizQuestionQuery = "INSERT INTO QCASRohan.QUIZ_QUESTION VALUES(" + quizNumber + "," + questionObjects.get(i).questionNumber + "," + "'incorrect');";
                        stmt.executeUpdate(insertQuizQuestionQuery);
                    }
                }
                else {
                    insertQuizQuestionQuery = "INSERT INTO QCASRohan.QUIZ_QUESTION VALUES(" + quizNumber + "," + questionObjects.get(i).questionNumber + "," + "'incorrect');";
                    stmt.executeUpdate(insertQuizQuestionQuery);
                }
            }

//          
            score = (double) rightCount / (double) numberOfQuestions;
            if (score > 0.4) {
                grade = "Pass";
            }
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date = new Date();
//            System.out.println(dateFormat.format(date));

            String resultQuery = "INSERT INTO QCASRohan.RESULTS VALUES(" + "'" + dateFormat.format(date) + "'," + quizNumber + ",'" + student.id + "'," + score + ",'" + grade + "','" + difficultyLevel + "')";
            stmt.execute(resultQuery);
        } catch (java.util.InputMismatchException e) {
            System.out.println("Input Mismatch Exception: Please enter integers 1, 2 or 3");
        } catch (SQLException e) {
            System.out.println(e + "Connection Not Established"); // Error Handling: Handling the error in case SQL query does not execute
        }

    }

    public void setDifficultyLevel(String difficultyLevel) {
        this.difficultyLevel = difficultyLevel;
    }

    public void setNumberOfQuestions(int numberOfQuestions) {
        this.numberOfQuestions = numberOfQuestions;
    }

    public Result getResult() {
        return this.result;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }

    public databaseConnection getConnection() {
        return connection;
    }

    public void setConnection(databaseConnection connection) {
        this.connection = connection;
    }

    public Statement getStmt() {
        return stmt;
    }

    public void setStmt(Statement stmt) {
        this.stmt = stmt;
    }

    public PreparedStatement getPreparedStmt() {
        return preparedStmt;
    }

    public void setPreparedStmt(PreparedStatement preparedStmt) {
        this.preparedStmt = preparedStmt;
    }

    public int getQuizNumber() {
        return quizNumber;
    }

    public void setQuizNumber() {
        try {
            String countQuery = "Select Count(*) AS COUNT From (Select distinct QUIZID FROM QCASRohan.QUIZ_QUESTION) db;";
            preparedStmt = connection.con.prepareStatement(countQuery);
            ResultSet rsc = preparedStmt.executeQuery();
            while (rsc.next()) {
                this.quizNumber = rsc.getInt("COUNT")+1;
            }    
        } catch (SQLException e){
            System.out.println("SQL Exception: " + e);
        }
        
    }
    
    public void importQuestions(String filePath) throws IOException, SQLException{
        
            InputFileReader reader = new InputFileReader(); //reader object to read from the input file
            reader.readFile(filePath); //reading the file containing quiz questions
            List<String[]> questionDetails = reader.wordsArray; // Each string from the csv file is stored in an ArrayList
            ArrayList<String> insertQueries = new ArrayList();
            int mastercounter = 0;
//             Inserting question details from the csv file words array into the insert String to be run to store values in a database
            for (int j = 0; j < questionDetails.size(); j++) {
                String countQuery = "select count(*) as count from QCASRohan.QUESTION";
                preparedStmt = connection.con.prepareStatement(countQuery);
                ResultSet rsc = preparedStmt.executeQuery();
                while (rsc.next()) {
                    mastercounter = rsc.getInt("count");
                }
                String insertString = "INSERT INTO QCASRohan.QUESTION VALUES (?,?,?,?,?,?,?,?,?,?,?,?)";
                preparedStmt = connection.con.prepareStatement(insertString);
                for (int i = 0; i < questionDetails.get(j).length; i++) {
                    preparedStmt.setString(i + 1, questionDetails.get(j)[i]);
                }
                for (int k = questionDetails.get(j).length; k < 11; k++) {
                    preparedStmt.setString(k + 1, "");
                }
                mastercounter += 1;
                preparedStmt.setString(12, "" + mastercounter);
                int counting = preparedStmt.executeUpdate();

                preparedStmt.executeUpdate();
        }
    }

}
