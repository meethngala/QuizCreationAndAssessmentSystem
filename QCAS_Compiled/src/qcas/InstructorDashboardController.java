/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package qcas;

import com.itextpdf.text.BadElementException;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Image;
import com.itextpdf.text.pdf.PdfWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.binding.Bindings;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.SnapshotParameters;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.StackedBarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javax.imageio.ImageIO;
import qcasMode.Result;
import qcasMode.databaseConnection;

/**
 * FXML Controller class
 *
 * @author Meeth
 */
public class InstructorDashboardController implements Initializable {

    @FXML
    private Slider slider;
//    @FXML
//    private BarChart Bar1;
    @FXML
    private LineChart Line1;
    @FXML
    private LineChart Line2;
    @FXML
    private PieChart Pie1;
    private Button button;
    databaseConnection connection;
    Statement stmt;
    private CategoryAxis xAxis = new CategoryAxis();
    private NumberAxis yAxis = new NumberAxis();
    private ObservableList comboData = FXCollections.observableArrayList();
    @FXML
    private StackedBarChart<String, Number> Bar1;
    @FXML
    private Button dashboardLogout;
    @FXML
    private Button dashboardBack;
    @FXML
    private CategoryAxis Line;
    @FXML
    private Label studentNumber;
    @FXML
    private Label quizNumber;
    @FXML
    private Label numberOfQuestions;
    @FXML
    private AnchorPane lineChartAnchorPaneTR;
    @FXML
    private AnchorPane pieChartAnchorPane;
    @FXML
    private AnchorPane lineChartAnchorPaneBR;
    @FXML
    private AnchorPane stackedBarAnchorPane;
    @FXML
    private Button exportPDFButton;
    @FXML
    private Label pdfDownloadedLabel;
    private String time = "Month";

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            // TODO
//        comboData.add("Year");
//       comboData.add("Month");
//       comboData.add("Quarter");
//       timeComboBox.setItems(comboData);

            analysePerf("Month");
            numberOfQuizzes("Month");
            studentPie("Month");
            makeBar();
            displayStats();
            time = "Month";
        } catch (IOException ex) {
            Logger.getLogger(InstructorDashboardController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(InstructorDashboardController.class.getName()).log(Level.SEVERE, null, ex);
        }

            slider.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable,
                    Number oldValue, Number newValue) {
                if (newValue.intValue() >= 0 && newValue.intValue() < 35) {
                    time = "Month";
                    try {

                        try {
                            analysePerf("Month");
                            numberOfQuizzes("Month");
                            studentPie("Month");
                            makeBar();
                        } catch (SQLException ex) {
                            Logger.getLogger(InstructorDashboardController.class.getName()).log(Level.SEVERE, null, ex);
                        }

                    } catch (IOException ex) {
                        Logger.getLogger(InstructorDashboardController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }

                if (newValue.intValue() >= 35 && newValue.intValue() <= 60) {
                    time = "Quarter";
                    try {
                        try {
                            analysePerf("Quarter");
                            numberOfQuizzes("Quarter");
                            studentPie("Quarter");
                            makeBar();
                        } catch (SQLException ex) {
                            Logger.getLogger(InstructorDashboardController.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    } catch (IOException ex) {
                        Logger.getLogger(InstructorDashboardController.class.getName()).log(Level.SEVERE, null, ex);
                    }

                }

                if (newValue.intValue() > 60) {
                    time = "Year";
                    try {
                        try {
                            //makeBar();
                            analysePerf("Year");
                            studentPie("Year");
                            makeBar();
                            //studentsPassFail();
                        } catch (SQLException ex) {
                            Logger.getLogger(InstructorDashboardController.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    } catch (IOException ex) {
                        Logger.getLogger(InstructorDashboardController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        });

    }

    private void handleButtonAction1(MouseEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("HomePage.fxml"));
        Stage stage = (Stage) button.getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    private void handleLogoutButton(MouseEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("TeacherLoginPage.fxml"));
        Stage stage = (Stage) dashboardLogout.getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    private void handleBackButton(MouseEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("TeacherAction.fxml"));
        Stage stage = (Stage) dashboardBack.getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
    
    public Image pdfExport(WritableImage paneImage, Document document) throws BadElementException, IOException{
        
        ByteArrayOutputStream byteArray = new ByteArrayOutputStream();
        
        try {
            ImageIO.write(SwingFXUtils.fromFXImage(paneImage, null), "png", byteArray);
        } catch (IOException e) {
        }
        Image newImage = Image.getInstance(byteArray.toByteArray());
        newImage.scaleToFit(500, 450);
        return newImage;
    }
   
    @FXML
    public void exportToPdf() throws IOException, DocumentException {
        Document document = new Document();
        String output = "Current" + time + ".pdf";
        FileOutputStream fos = new FileOutputStream(output);
        PdfWriter writer = PdfWriter.getInstance(document, fos);
        writer.open();
        document.open();
        WritableImage image = lineChartAnchorPaneBR.snapshot(new SnapshotParameters(), null);
        Image returnImage = pdfExport(image,document);
        document.add(returnImage);
        image = pieChartAnchorPane.snapshot(new SnapshotParameters(), null);
        returnImage = pdfExport(image,document);
        document.newPage();
        document.add(returnImage);
        image = stackedBarAnchorPane.snapshot(new SnapshotParameters(), null);
        returnImage = pdfExport(image,document);
        document.newPage();
        document.add(returnImage);
        image = lineChartAnchorPaneBR.snapshot(new SnapshotParameters(), null);
        returnImage = pdfExport(image,document);
        document.newPage();
        document.add(returnImage);
        document.close();
        writer.close();
        pdfDownloadedLabel.setVisible(true);

    }

    private void makePie(/*MouseEvent event*/) throws IOException {
        Pie1.setVisible(true);
        Bar1.setVisible(false);
        Line1.setVisible(false);

        ObservableList<PieChart.Data> pieChartData
                = FXCollections.observableArrayList(
                        new PieChart.Data("Grapefruit", 13),
                        new PieChart.Data("Oranges", 25),
                        new PieChart.Data("Plums", 10),
                        new PieChart.Data("Pears", 22),
                        new PieChart.Data("Apples", 30));
        final PieChart chart = new PieChart(pieChartData);
        Pie1.setTitle("Student Performance");
        Pie1.setData(pieChartData);
    }

    public void displayStats() throws SQLException {
        ResultSet rs1;
        ResultSet rs2;
        ResultSet rs3;
        connection = new databaseConnection("jdbc:mysql://qcasrohan.caswkasqdmel.ap-southeast-2.rds.amazonaws.com:3306/QCASRohan?zeroDateTimeBehavior=convertToNull", "rohan", "rohantest");
        stmt = connection.getConnection().createStatement();
        String l1 = "SELECT MAX(QCASRohan.QUESTION.pk_column) as MAX FROM QUESTION;";

        rs1 = stmt.executeQuery(l1);
        while (rs1.next()) {
            numberOfQuestions.setText(rs1.getString("MAX"));
        }
        String l2 = " Select count(*) as COUNT from (Select distinct STUDENTID FROM QCASRohan.RESULTS) da;";
        rs2 = stmt.executeQuery(l2);
        while (rs2.next()) {
            studentNumber.setText(rs2.getString("COUNT"));
        }
        String l3 = " SELECT MAX(QUIZID) as MAX from QCASRohan.RESULTS;";
        rs3 = stmt.executeQuery(l3);
        while (rs3.next()) {
            quizNumber.setText(rs3.getString("MAX"));
        }

    }

    public void makeBar(/*MouseEvent event*/) throws IOException, SQLException {
        //Pie1.setVisible(false);
        Bar1.setVisible(true);
        //Line1.setVisible(false);
        ResultSet rs;
        Bar1.getData().clear();
        final CategoryAxis xAxis = new CategoryAxis();
        final NumberAxis yAxis = new NumberAxis();

        Bar1.getXAxis().setLabel("Question ID");
        Bar1.getYAxis().setLabel("Frequency of Question");
        //Bar1.setScaleY(50.0);

        Bar1.getYAxis().setLayoutY(100);
        XYChart.Series series1 = new XYChart.Series();
        XYChart.Series series2 = new XYChart.Series();

        String i1 = "SELECT QCASRohan.QUIZ_QUESTION.QUESTIONID, VALIDITY, (dt.Count - count(*)) as CorrectCount FROM (Select QUESTIONID, Count(*) as COUNT from QCASRohan.QUIZ_QUESTION GROUP BY QUESTIONID ORDER BY COUNT DESC LIMIT 10) dt LEFT JOIN QCASRohan.QUIZ_QUESTION ON dt.QUESTIONID = QCASRohan.QUIZ_QUESTION.QUESTIONID WHERE QCASRohan.QUIZ_QUESTION.VALIDITY = 'incorrect' GROUP BY dt.QUESTIONID, QCASRohan.QUIZ_QUESTION.VALIDITY ORDER BY dt.QUESTIONID;";

        String i2 = "SELECT QCASRohan.QUIZ_QUESTION.QUESTIONID, VALIDITY, count(*) as IncorrectCount FROM (Select QUESTIONID, Count(*) as COUNT from QCASRohan.QUIZ_QUESTION GROUP BY QUESTIONID ORDER BY COUNT DESC LIMIT 10) dt LEFT JOIN QCASRohan.QUIZ_QUESTION ON dt.QUESTIONID = QCASRohan.QUIZ_QUESTION.QUESTIONID WHERE QCASRohan.QUIZ_QUESTION.VALIDITY = 'incorrect' GROUP BY dt.QUESTIONID, QCASRohan.QUIZ_QUESTION.VALIDITY ORDER BY dt.QUESTIONID;";

        rs = stmt.executeQuery(i1);
        while (rs.next()) {
            System.out.println(String.valueOf(rs.getInt("QUESTIONID")));
            System.out.println(rs.getInt("CorrectCount"));
            series1.getData().add(new XYChart.Data<String, Number>(String.valueOf(rs.getInt("QUESTIONID")), rs.getInt("CorrectCount")));
        }

        rs = stmt.executeQuery(i2);
        while (rs.next()) {
            System.out.println(String.valueOf(rs.getInt("QUESTIONID")));
            System.out.println(rs.getInt("IncorrectCount"));
            series2.getData().add(new XYChart.Data<String, Number>(String.valueOf(rs.getInt("QUESTIONID")), rs.getInt("IncorrectCount")));
        }

        Bar1.getData().addAll(series1, series2);

        Bar1.setTitle("Student Performance");
    }

// Avg Student Performance Report for Instructor
    private void analysePerf(String s) throws IOException, SQLException {
        //Pie1.setVisible(false);
        //Bar1.setVisible(false);
        Line1.setVisible(true);
        int i = 1;
        String case1 = s;

        try {

            //case1 = (String) (timeComboBox.getSelectionModel().getSelectedItem());
            if (case1.equals("Year")) {
                i = 1;
            } else if (case1.equals("Quarter")) {
                i = 2;
            } else if (case1.equals("Month")) {
                i = 3;
            }

            ResultSet rs;
            connection = new databaseConnection("jdbc:mysql://qcasrohan.caswkasqdmel.ap-southeast-2.rds.amazonaws.com:3306/QCASRohan?zeroDateTimeBehavior=convertToNull", "rohan", "rohantest");
            stmt = connection.getConnection().createStatement();
            //final CategoryAxis xAxis = new CategoryAxis();
            //final NumberAxis yAxis = new NumberAxis();

            Line1.setTitle("Average Student Performance");

            XYChart.Series series0 = new XYChart.Series();
            XYChart.Series series1 = new XYChart.Series();
            XYChart.Series series2 = new XYChart.Series();
            XYChart.Series series3 = new XYChart.Series();
            Line1.getYAxis().setLabel("Average Scores");
            String z;
            switch (i) {

                case 1:
                    Line1.getData().clear();
                    Line1.getXAxis().setLabel("Month");
                    z = " select monthname(TESTTIME) Month from (SELECT * FROM RESULTS WHERE TESTTIME BETWEEN DATE_SUB(NOW(), INTERVAL 1 year) AND NOW() order by TESTTIME) db group by monthname(TESTTIME)  order by TESTTIME;";
                    rs = stmt.executeQuery(z);
//                XYChart.Series series1 = new XYChart.Series();
                    series0.setName("LEGEND");
                    while (rs.next()) {
                        series0.getData().add(new XYChart.Data(rs.getString("Month"), 0));
                    }

                    String y1 = " select monthname(TESTTIME) Month,  avg(SCORE) AvgValue from (SELECT * FROM RESULTS WHERE LEVEL = 'E' AND TESTTIME BETWEEN DATE_SUB(NOW(), INTERVAL 1 year) AND NOW()) db group by monthname(TESTTIME) order by TESTTIME;";
                    rs = stmt.executeQuery(y1);
//                XYChart.Series series1 = new XYChart.Series();
                    series1.setName("Easy");
                    while (rs.next()) {
                        series1.getData().add(new XYChart.Data(rs.getString("Month"), (int) (rs.getDouble("AvgValue") * 100)));
                    }
                    String y2 = "select monthname(TESTTIME) Month,  avg(SCORE) AvgValue from (SELECT * FROM RESULTS WHERE LEVEL = 'M' AND TESTTIME BETWEEN DATE_SUB(NOW(), INTERVAL 1 year) AND NOW()) db group by monthname(TESTTIME) order by TESTTIME;";
//                XYChart.Series series2 = new XYChart.Series();
                    series2.setName("Medium");
                    rs = stmt.executeQuery(y2);
                    while (rs.next()) {
                        series2.getData().add(new XYChart.Data(rs.getString("Month"), (int) (rs.getDouble("AvgValue") * 100)));
                    }
                    String y3 = "select monthname(TESTTIME) Month,  avg(SCORE) AvgValue from (SELECT * FROM RESULTS WHERE LEVEL = 'H' AND TESTTIME BETWEEN DATE_SUB(NOW(), INTERVAL 1 year) AND NOW()) db group by monthname(TESTTIME) order by TESTTIME;";
//                XYChart.Series series3 = new XYChart.Series();
                    series3.setName("Hard");
                    rs = stmt.executeQuery(y3);
                    while (rs.next()) {
                        series3.getData().add(new XYChart.Data(rs.getString("Month"), (int) (rs.getDouble("AvgValue") * 100)));
                    }
                    break;
                case 2:
                    Line1.getData().clear();
                    Line1.getXAxis().setLabel("Week Number of the year");
                    z = "select week(TESTTIME) Week from (SELECT * FROM RESULTS WHERE TESTTIME BETWEEN DATE_SUB(NOW(), INTERVAL 1 quarter) AND NOW()  order by TESTTIME) db group by week(TESTTIME) order by TESTTIME;";
                    rs = stmt.executeQuery(z);
//                XYChart.Series series1 = new XYChart.Series();
                    series0.setName("LEGEND");
                    while (rs.next()) {
                        series0.getData().add(new XYChart.Data(rs.getString("Week"), 0));
                    }

                    String q1 = "select week(TESTTIME) Week, avg(SCORE) WeekAvg from (SELECT * FROM RESULTS WHERE LEVEL = 'E' AND TESTTIME BETWEEN DATE_SUB(NOW(), INTERVAL 1 quarter) AND NOW()) db group by week(TESTTIME) order by TESTTIME;";
                    rs = stmt.executeQuery(q1);
//                XYChart.Series series1 = new XYChart.Series();
                    series1.setName("Easy");
                    while (rs.next()) {
                        series1.getData().add(new XYChart.Data(rs.getString("Week"), (int) (rs.getDouble("WeekAvg") * 100)));
                    }
                    String q2 = "select week(TESTTIME) Week, avg(SCORE) WeekAvg from (SELECT * FROM RESULTS WHERE LEVEL = 'M' AND TESTTIME BETWEEN DATE_SUB(NOW(), INTERVAL 1 quarter) AND NOW()) db group by week(TESTTIME) order by TESTTIME;";
//                XYChart.Series series2 = new XYChart.Series();
                    series2.setName("Medium");
                    rs = stmt.executeQuery(q2);
                    while (rs.next()) {
                        series2.getData().add(new XYChart.Data(rs.getString("Week"), (int) (rs.getDouble("WeekAvg") * 100)));
                    }
                    String q3 = "select week(TESTTIME) Week, avg(SCORE) WeekAvg from (SELECT * FROM RESULTS WHERE LEVEL = 'H' AND TESTTIME BETWEEN DATE_SUB(NOW(), INTERVAL 1 quarter) AND NOW()) db group by week(TESTTIME) order by TESTTIME;";
//                XYChart.Series series3 = new XYChart.Series();
                    series3.setName("Hard");
                    rs = stmt.executeQuery(q3);
                    while (rs.next()) {
                        series3.getData().add(new XYChart.Data(rs.getString("Week"), (int) (rs.getDouble("WeekAvg") * 100)));
                    }

                    break;
                case 3:
                    Line1.getData().clear();
                    Line1.getXAxis().setLabel("Date of the Month");
                    z = "select dayofmonth(TESTTIME) Day from (SELECT * FROM RESULTS WHERE TESTTIME BETWEEN DATE_SUB(NOW(), INTERVAL 1 month) AND NOW() order by TESTTIME) db group by date(TESTTIME) order by TESTTIME;";
                    rs = stmt.executeQuery(z);
//                XYChart.Series series1 = new XYChart.Series();
                    series0.setName("LEGEND");
                    while (rs.next()) {
                        series0.getData().add(new XYChart.Data(rs.getString("Day"), 0));
                    }

                    String m1 = "select dayofmonth(TESTTIME) Day, avg(SCORE) DailyAvg, LEVEL from (SELECT * FROM RESULTS WHERE LEVEL = 'E' AND TESTTIME BETWEEN DATE_SUB(NOW(), INTERVAL 1 month) AND NOW() ORDER BY TESTTIME) db group by date(TESTTIME) order by TESTTIME;";
                    String m2 = "select dayofmonth(TESTTIME) Day, avg(SCORE) DailyAvg, LEVEL from (SELECT * FROM RESULTS WHERE LEVEL = 'M' AND TESTTIME BETWEEN DATE_SUB(NOW(), INTERVAL 1 month) AND NOW() ORDER BY TESTTIME) db group by date(TESTTIME) order by TESTTIME;";
                    String m3 = "select dayofmonth(TESTTIME) Day, avg(SCORE) DailyAvg, LEVEL from (SELECT * FROM RESULTS WHERE LEVEL = 'H' AND TESTTIME BETWEEN DATE_SUB(NOW(), INTERVAL 1 month) AND NOW() ORDER BY TESTTIME) db group by date(TESTTIME) order by TESTTIME;";
                    rs = stmt.executeQuery(m1);
//                XYChart.Series series1 = new XYChart.Series();
                    series1.setName("Easy");
                    while (rs.next()) {
                        series1.getData().add(new XYChart.Data(rs.getString("Day"), (int) (rs.getDouble("DailyAvg") * 100)));
                    }
//                XYChart.Series series2 = new XYChart.Series();
                    series2.setName("Medium");
                    rs = stmt.executeQuery(m2);
                    while (rs.next()) {
                        series2.getData().add(new XYChart.Data(rs.getString("Day"), (int) (rs.getDouble("DailyAvg") * 100)));
                    }
//                XYChart.Series series3 = new XYChart.Series();
                    series3.setName("Hard");
                    rs = stmt.executeQuery(m3);
                    while (rs.next()) {
                        series3.getData().add(new XYChart.Data(rs.getString("Day"), (int) (rs.getDouble("DailyAvg") * 100)));
                    }
                    break;

            }

            //series.nodeProperty().get().setStyle("-fx-stroke-width: 1px;");
            //Scene scene = new Scene(lineChart, 800, 600);
            Line1.getData().addAll(series0, series1, series2, series3);

            Node line = series0.getNode().lookup(".chart-series-line");

            Color color = Color.TRANSPARENT; // or any other color

            String rgb = String.format("%d, %d, %d",
                    (int) (color.getRed() * 0),
                    (int) (color.getGreen() * 0),
                    (int) (color.getBlue() * 0));
            line.setStyle("-fx-stroke: rgba(" + rgb + ", 0.0);");

//            Stage stage = new Stage();
//            stage.setScene(scene);
//            stage.show();
        } catch (Exception e) {

            //FilePath.setText("Please select a time period");
            System.out.println(e);
        }
    }

    private void topQuestionsBarGraph(String s) throws IOException, SQLException {
        //Pie1.setVisible(false);
        Bar1.setVisible(true);
        //Line1.setVisible(true);
        int i = 1;
        String case1 = s;

    }

// Number of Quizzes over time
    private void numberOfQuizzes(String s) throws IOException, SQLException {
        int i = 1;
        //Pie1.setVisible(false);
        //Bar1.setVisible(false);
        Line2.setVisible(true);
        String case1 = s;
        int quiz_num = 0;
        try {

            //case1 = (String) (timeComboBox.getSelectionModel().getSelectedItem());
            if (case1.equals("Year")) {
                i = 1;
            } else if (case1.equals("Quarter")) {
                i = 2;
            } else if (case1.equals("Month")) {
                i = 3;
            }

//
            ResultSet rs;
            String z;
//            final CategoryAxis xAxis = new CategoryAxis();
//            final NumberAxis yAxis = new NumberAxis();
//             LineChart<String, Number> lineChart = new LineChart<String, Number>(xAxis, yAxis);
            Line2.setTitle("Number of Quizes over Time");
            Line2.getYAxis().setLabel("Average Scores");
            XYChart.Series series1 = new XYChart.Series();
            switch (i) {

                case 1:
                    Line2.getData().clear();
                    Line2.getXAxis().setLabel("Month");
                    z = "select monthname(TESTTIME) Month, count(Distinct QUIZID) count from (select * from RESULTS WHERE TESTTIME BETWEEN DATE_SUB(NOW(), INTERVAL 1 year) AND NOW() order by TESTTIME) db group by monthname(TESTTIME) order by TESTTIME;";
                    rs = stmt.executeQuery(z);
                    series1.setName("Number of Quizes");
                    while (rs.next()) {
                        series1.getData().add(new XYChart.Data(rs.getString("Month"), (int) (rs.getInt("count"))));
                    }
                    break;
                case 2:
                    Line2.getData().clear();
                    Line2.getXAxis().setLabel("Week Number");
                    z = "select week(TESTTIME) Week, count(Distinct QUIZID) count from (select * from RESULTS WHERE TESTTIME BETWEEN DATE_SUB(NOW(), INTERVAL 1 quarter) AND NOW() order by TESTTIME) db group by week(TESTTIME) order by TESTTIME;";
                    rs = stmt.executeQuery(z);
                    series1.setName("Number of Quizes");
                    while (rs.next()) {
                        series1.getData().add(new XYChart.Data(rs.getString("Week"), (int) (rs.getInt("count"))));
                    }
                    break;
                case 3:
                    Line2.getData().clear();
                    Line2.getXAxis().setLabel("Day of the Month");
                    z = "select dayofmonth(TESTTIME) Day, count(Distinct QUIZID) count from (select * from RESULTS WHERE TESTTIME BETWEEN DATE_SUB(NOW(), INTERVAL 1 month) AND NOW() order by TESTTIME) db group by dayofmonth(TESTTIME) order by TESTTIME;";
                    rs = stmt.executeQuery(z);
                    series1.setName("Number of Quizes");
                    while (rs.next()) {
                        series1.getData().add(new XYChart.Data(rs.getString("Day"), (int) (rs.getInt("count"))));
                    }
                    break;
            }

//            Scene scene = new Scene(lineChart, 800, 600);
            Line2.getData().addAll(series1);
//            Stage stage = new Stage();
//            stage.setScene(scene);
//            stage.show();
        } catch (Exception e) {

            //FilePath.setText("Please select a time period");
            System.out.println(e);
        }
    }

    /*Number of Students Passing and Failing over Time*/
    private void studentsPassFail(String s) throws IOException, SQLException {
        //Pie1.setVisible(false);
        //Bar1.setVisible(false);
        Line1.setVisible(true);
        int i = 1;
        String case1 = "";
        try {

            //case1 = (String) (timeComboBox.getSelectionModel().getSelectedItem());
            if (case1.equals("Year")) {
                i = 1;
            } else if (case1.equals("Quarter")) {
                i = 2;
            } else if (case1.equals("Month")) {
                i = 3;
            }

            ResultSet rs;
//            final CategoryAxis xAxis = new CategoryAxis();
//            final NumberAxis yAxis = new NumberAxis();
//             LineChart<String, Number> lineChart = new LineChart<String, Number>(xAxis, yAxis);
            Line1.setTitle("Number of Students Passing and Failing over Time");

            XYChart.Series series0 = new XYChart.Series();
            XYChart.Series series1 = new XYChart.Series();
            XYChart.Series series2 = new XYChart.Series();
            String z;
            switch (i) {

                case 1:
                    Line1.getData().clear();
                    Line1.getXAxis().setLabel("Months");
                    z = "select monthname(TESTTIME) Month, count(GRADE) count from (select * from RESULTS WHERE  TESTTIME BETWEEN DATE_SUB(NOW(), INTERVAL 1 year) AND NOW() order by TESTTIME) db group by monthname(TESTTIME) order by TESTTIME;";
                    rs = stmt.executeQuery(z);
                    series0.setName("LEGEND");
                    while (rs.next()) {
                        series0.getData().add(new XYChart.Data(rs.getString("Month"), 0));
                    }

                    String y1 = "select monthname(TESTTIME) Month, count(GRADE) count from (select * from RESULTS WHERE  GRADE = \"Pass\" AND TESTTIME BETWEEN DATE_SUB(NOW(), INTERVAL 1 year) AND NOW() order by TESTTIME) db group by monthname(TESTTIME) order by TESTTIME;";
                    rs = stmt.executeQuery(y1);
                    series1.setName("Pass");
                    while (rs.next()) {
                        series1.getData().add(new XYChart.Data(rs.getString("Month"), (int) (rs.getInt("count"))));
                    }
                    String y2 = "select monthname(TESTTIME) Month, count(GRADE) count from (select * from RESULTS WHERE  GRADE = \"Fail\" AND TESTTIME BETWEEN DATE_SUB(NOW(), INTERVAL 1 year) AND NOW() order by TESTTIME) db group by monthname(TESTTIME) order by TESTTIME;";
                    series2.setName("Fail");
                    rs = stmt.executeQuery(y2);
                    while (rs.next()) {
                        series2.getData().add(new XYChart.Data(rs.getString("Month"), (int) (rs.getInt("count"))));
                    }

                    break;
                case 2:
                    Line1.getData().clear();
                    Line1.getXAxis().setLabel("Week Number of the year");
                    z = "select week(TESTTIME) Week, count(GRADE) count from (select * from RESULTS WHERE  TESTTIME BETWEEN DATE_SUB(NOW(), INTERVAL 1 quarter) AND NOW() order by TESTTIME) db group by week(TESTTIME) order by TESTTIME;";
                    rs = stmt.executeQuery(z);
                    series0.setName("LEGEND");
                    while (rs.next()) {
                        series0.getData().add(new XYChart.Data(rs.getString("Week"), 0));
                    }

                    String q1 = "select week(TESTTIME) Week, count(GRADE) count from (select * from RESULTS WHERE  GRADE = \"Pass\" AND TESTTIME BETWEEN DATE_SUB(NOW(), INTERVAL 1 quarter) AND NOW() order by TESTTIME) db group by week(TESTTIME) order by TESTTIME;";
                    rs = stmt.executeQuery(q1);
                    series1.setName("Pass");
                    while (rs.next()) {
                        series1.getData().add(new XYChart.Data(rs.getString("Week"), (int) (rs.getInt("count"))));
                    }
                    String q2 = "select week(TESTTIME) Week, count(GRADE) count from (select * from RESULTS WHERE  GRADE = \"Fail\" AND TESTTIME BETWEEN DATE_SUB(NOW(), INTERVAL 1 quarter) AND NOW() order by TESTTIME) db group by week(TESTTIME) order by TESTTIME;";
                    series2.setName("Fail");
                    rs = stmt.executeQuery(q2);
                    while (rs.next()) {
                        series2.getData().add(new XYChart.Data(rs.getString("Week"), (int) (rs.getInt("count"))));
                    }

                    break;
                case 3:
                    Line1.getData().clear();
                    Line1.getXAxis().setLabel("Date of the Month");
                    z = "select dayofmonth(TESTTIME) Day, count(GRADE) count from (select * from RESULTS WHERE TESTTIME BETWEEN DATE_SUB(NOW(), INTERVAL 1 month) AND NOW() order by TESTTIME) db group by dayofmonth(TESTTIME) order by TESTTIME;";
                    rs = stmt.executeQuery(z);
                    series0.setName("LEGEND");
                    while (rs.next()) {
                        series0.getData().add(new XYChart.Data(rs.getString("Day"), 0));
                    }

                    String m1 = "select dayofmonth(TESTTIME) Day, count(GRADE) count from (select * from RESULTS WHERE  GRADE = \"Pass\" AND TESTTIME BETWEEN DATE_SUB(NOW(), INTERVAL 1 month) AND NOW() order by TESTTIME) db group by dayofmonth(TESTTIME) order by TESTTIME;";
                    String m2 = "select dayofmonth(TESTTIME) Day, count(GRADE) count from (select * from RESULTS WHERE  GRADE = \"Fail\" AND TESTTIME BETWEEN DATE_SUB(NOW(), INTERVAL 1 month) AND NOW() order by TESTTIME) db group by dayofmonth(TESTTIME) order by TESTTIME;";
                    rs = stmt.executeQuery(m1);
                    series1.setName("Pass");
                    while (rs.next()) {
                        series1.getData().add(new XYChart.Data(rs.getString("Day"), (int) (rs.getInt("count"))));
                    }
                    series2.setName("Fail");
                    rs = stmt.executeQuery(m2);
                    while (rs.next()) {
                        series2.getData().add(new XYChart.Data(rs.getString("Day"), (int) (rs.getInt("count"))));
                    }

                    break;

            }

            //series.nodeProperty().get().setStyle("-fx-stroke-width: 1px;");
            //Scene scene = new Scene(lineChart, 800, 600);
            Line1.getData().addAll(series0, series1, series2);

            Node line = series0.getNode().lookup(".chart-series-line");

            Color color = Color.TRANSPARENT; // or any other color

            String rgb = String.format("%d, %d, %d",
                    (int) (color.getRed() * 0),
                    (int) (color.getGreen() * 0),
                    (int) (color.getBlue() * 0));
            line.setStyle("-fx-stroke: rgba(" + rgb + ", 0.0);");

//            Stage stage = new Stage();
//            stage.setScene(scene);
//            stage.show();
        } catch (Exception e) {

            //FilePath.setText("Please select a time period");
            System.out.println(e);
        }
    }

    public void studentPie(String s) throws IOException, SQLException {
        Pie1.setVisible(true);

        int i = 1;

        String z = "";
        double pass = 0.0;
        double fail = 0.0;

        try {

            if (s.equals("Year")) {
                //case1 = (String) (timeComboBox.getSelectionModel().getSelectedItem());
                i = 1;
            } else if (s.equals("Quarter")) {
                i = 2;
            } else if (s.equals("Month")) {
                i = 3;
            }

            ResultSet rs;
            switch (i) {

                case 1:
                    String y1 = "select count(*) as passed from (select * from RESULTS WHERE  TESTTIME BETWEEN DATE_SUB(NOW(), INTERVAL 1 year) AND NOW()) db WHERE GRADE = 'Pass';";
                    rs = stmt.executeQuery(y1);
                    while (rs.next()) {
                        pass = rs.getInt("passed");
                    }
                    String y2 = "select count(*) as passed from (select * from RESULTS WHERE  TESTTIME BETWEEN DATE_SUB(NOW(), INTERVAL 1 year) AND NOW()) db WHERE GRADE = 'Fail';";
                    rs = stmt.executeQuery(y2);
                    while (rs.next()) {
                        fail = rs.getInt("passed");
                    }
                    Pie1.setTitle("Student Grades In Last Year");

                    break;
                case 2:
                    String q1 = "select count(*) as passed from (select * from RESULTS WHERE  TESTTIME BETWEEN DATE_SUB(NOW(), INTERVAL 1 quarter) AND NOW()) db WHERE GRADE = 'Pass';";
                    rs = stmt.executeQuery(q1);
                    while (rs.next()) {
                        pass = rs.getInt("passed");
                    }
                    String q2 = "select count(*) as passed from (select * from RESULTS WHERE  TESTTIME BETWEEN DATE_SUB(NOW(), INTERVAL 1 quarter) AND NOW()) db WHERE GRADE = 'Fail';";
                    rs = stmt.executeQuery(q2);
                    while (rs.next()) {
                        fail = rs.getInt("passed");
                    }
                    Pie1.setTitle("Student Grades In Last Quarter");
                    break;
                case 3:
                    String m1 = "select count(*) as passed from (select * from RESULTS WHERE  TESTTIME BETWEEN DATE_SUB(NOW(), INTERVAL 1 month) AND NOW()) db WHERE GRADE = 'Pass';";
                    rs = stmt.executeQuery(m1);
                    while (rs.next()) {
                        pass = rs.getInt("passed");
                    }
                    String m2 = "select count(*) as passed from (select * from RESULTS WHERE  TESTTIME BETWEEN DATE_SUB(NOW(), INTERVAL 1 month) AND NOW()) db WHERE GRADE = 'Fail';";
                    rs = stmt.executeQuery(m2);
                    while (rs.next()) {
                        fail = rs.getInt("passed");
                    }
                    Pie1.setTitle("Student Grades In Last Month");
                    break;
            }

            double total = pass + fail;
            double correctPerc = Math.round((pass / total) * 100 * 100.0) / 100.0;
            double incorrectPerc = Math.round((fail / total) * 100 * 100.0) / 100.0;;

            ObservableList<PieChart.Data> pieChartData
                    = FXCollections.observableArrayList(
                            new PieChart.Data("Pass", correctPerc),
                            new PieChart.Data("Fail", incorrectPerc));

            final PieChart chart = new PieChart(pieChartData);

            pieChartData.forEach(data
                    -> data.nameProperty().bind(
                            Bindings.concat(
                                    data.getName(), " ", data.pieValueProperty(), "%"
                            )
                    )
            );

            Pie1.setData(pieChartData);
        } catch (Exception e) {

            //FilePath.setText("Please select a time period");
            System.out.println(e);
        }
    }

}
