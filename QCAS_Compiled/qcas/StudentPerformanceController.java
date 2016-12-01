/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package qcas;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Image;
import com.itextpdf.text.pdf.PdfWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
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
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javax.imageio.ImageIO;
import qcasMode.Quiz;
import qcasMode.Student;
import qcasMode.databaseConnection;

/**
 * FXML Controller class
 *
 * @author paridhichoudhary
 */
public class StudentPerformanceController implements Initializable {

    @FXML
    private AnchorPane performanceAnchorPane;
    private String time;
    @FXML
    private Button savePDFButton;
    @FXML
    private Label pdfDownloadedLabel;
    @FXML
    private Button logOutButton;
    @FXML
    private LineChart Line1;
    @FXML
    private CategoryAxis Line;
    @FXML
    private Slider slider;
    @FXML
    private Button backButton;
    private Student student;
    @FXML
    private LineChart<?, ?> Line2;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            analysePerf("Month");
        } catch (IOException ex) {
            Logger.getLogger(StudentPerformanceController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(StudentPerformanceController.class.getName()).log(Level.SEVERE, null, ex);
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
    
    @FXML
    private void backButton(MouseEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("StudentAction.fxml"));
        Stage stage = (Stage) backButton.getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    public void exportToPdf() throws IOException, DocumentException {
        WritableImage image = performanceAnchorPane.snapshot(new SnapshotParameters(), null);
        //File file = new File("StudentPerformance.png");
        ByteArrayOutputStream byteArray = new ByteArrayOutputStream();
        try {
            ImageIO.write(SwingFXUtils.fromFXImage(image, null), "png", byteArray);
        } catch (IOException e) {
        }
        Document document = new Document();
        String output = "StudentPastPerformance.pdf";
        try {
            int indentation = 0;
            FileOutputStream fos = new FileOutputStream(output);
            PdfWriter writer = PdfWriter.getInstance(document, fos);
            writer.open();
            document.open();
            Image newImage = Image.getInstance(byteArray.toByteArray());
            newImage.scaleToFit(500, 450);
            document.add(newImage);
            document.close();
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        pdfDownloadedLabel.setVisible(true);

    }

    @FXML
    private void logOutAction(MouseEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("StudentLoginPage.fxml"));
        Stage stage = (Stage) logOutButton.getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
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
            databaseConnection connection = new databaseConnection("jdbc:mysql://qcasrohan.caswkasqdmel.ap-southeast-2.rds.amazonaws.com:3306/QCASRohan?zeroDateTimeBehavior=convertToNull", "rohan", "rohantest");
            Statement stmt = connection.getConnection().createStatement();
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
                    z = " select monthname(TESTTIME) Month from (SELECT * FROM RESULTS WHERE TESTTIME BETWEEN DATE_SUB(NOW(), INTERVAL 1 year) AND NOW() order by TESTTIME AND STUDENTID = '" + student.getId() + "') db group by monthname(TESTTIME)  order by TESTTIME;";
                    rs = stmt.executeQuery(z);
//                XYChart.Series series1 = new XYChart.Series();
                    series0.setName("LEGEND");
                    while (rs.next()) {
                        series0.getData().add(new XYChart.Data(rs.getString("Month"), 0));
                    }

                    String y1 = " select monthname(TESTTIME) Month,  avg(SCORE) AvgValue from (SELECT * FROM RESULTS WHERE LEVEL = 'E' AND TESTTIME BETWEEN DATE_SUB(NOW(), INTERVAL 1 year) AND NOW() AND STUDENTID = '" + student.getId() + "') db group by monthname(TESTTIME) order by TESTTIME;";
                    rs = stmt.executeQuery(y1);
//                XYChart.Series series1 = new XYChart.Series();
                    series1.setName("Easy");
                    while (rs.next()) {
                        series1.getData().add(new XYChart.Data(rs.getString("Month"), (int) (rs.getDouble("AvgValue") * 100)));
                    }
                    String y2 = "select monthname(TESTTIME) Month,  avg(SCORE) AvgValue from (SELECT * FROM RESULTS WHERE LEVEL = 'M' AND TESTTIME BETWEEN DATE_SUB(NOW(), INTERVAL 1 year) AND NOW() AND STUDENTID = '" + student.getId() + "') db group by monthname(TESTTIME) order by TESTTIME;";
//                XYChart.Series series2 = new XYChart.Series();
                    series2.setName("Medium");
                    rs = stmt.executeQuery(y2);
                    while (rs.next()) {
                        series2.getData().add(new XYChart.Data(rs.getString("Month"), (int) (rs.getDouble("AvgValue") * 100)));
                    }
                    String y3 = "select monthname(TESTTIME) Month,  avg(SCORE) AvgValue from (SELECT * FROM RESULTS WHERE LEVEL = 'H' AND TESTTIME BETWEEN DATE_SUB(NOW(), INTERVAL 1 year) AND NOW() AND STUDENTID = '" + student.getId() + "') db group by monthname(TESTTIME) order by TESTTIME;";
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
                    z = "select week(TESTTIME) Week from (SELECT * FROM RESULTS WHERE TESTTIME BETWEEN DATE_SUB(NOW(), INTERVAL 1 quarter) AND NOW()  order by TESTTIME AND STUDENTID = '" + student.getId() + "') db group by week(TESTTIME) order by TESTTIME;";
                    rs = stmt.executeQuery(z);
//                XYChart.Series series1 = new XYChart.Series();
                    series0.setName("LEGEND");
                    while (rs.next()) {
                        series0.getData().add(new XYChart.Data(rs.getString("Week"), 0));
                    }

                    String q1 = "select week(TESTTIME) Week, avg(SCORE) WeekAvg from (SELECT * FROM RESULTS WHERE LEVEL = 'E' AND TESTTIME BETWEEN DATE_SUB(NOW(), INTERVAL 1 quarter) AND NOW() AND STUDENTID = '" + student.getId() + "') db group by week(TESTTIME) order by TESTTIME;";
                    rs = stmt.executeQuery(q1);
//                XYChart.Series series1 = new XYChart.Series();
                    series1.setName("Easy");
                    while (rs.next()) {
                        series1.getData().add(new XYChart.Data(rs.getString("Week"), (int) (rs.getDouble("WeekAvg") * 100)));
                    }
                    String q2 = "select week(TESTTIME) Week, avg(SCORE) WeekAvg from (SELECT * FROM RESULTS WHERE LEVEL = 'M' AND TESTTIME BETWEEN DATE_SUB(NOW(), INTERVAL 1 quarter) AND NOW() AND STUDENTID = '" + student.getId() + "') db group by week(TESTTIME) order by TESTTIME;";
//                XYChart.Series series2 = new XYChart.Series();
                    series2.setName("Medium");
                    rs = stmt.executeQuery(q2);
                    while (rs.next()) {
                        series2.getData().add(new XYChart.Data(rs.getString("Week"), (int) (rs.getDouble("WeekAvg") * 100)));
                    }
                    String q3 = "select week(TESTTIME) Week, avg(SCORE) WeekAvg from (SELECT * FROM RESULTS WHERE LEVEL = 'H' AND TESTTIME BETWEEN DATE_SUB(NOW(), INTERVAL 1 quarter) AND NOW() AND STUDENTID = '" + student.getId() + "') db group by week(TESTTIME) order by TESTTIME;";
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
                    z = "select dayofmonth(TESTTIME) Day from (SELECT * FROM RESULTS WHERE TESTTIME BETWEEN DATE_SUB(NOW(), INTERVAL 1 month) AND NOW() order by TESTTIME AND STUDENTID = '" + student.getId() + "') db group by date(TESTTIME) order by TESTTIME;";
                    rs = stmt.executeQuery(z);
//                XYChart.Series series1 = new XYChart.Series();
                    series0.setName("LEGEND");
                    while (rs.next()) {
                        series0.getData().add(new XYChart.Data(rs.getString("Day"), 0));
                    }

                    String m1 = "select dayofmonth(TESTTIME) Day, avg(SCORE) DailyAvg, LEVEL from (SELECT * FROM RESULTS WHERE LEVEL = 'E' AND TESTTIME BETWEEN DATE_SUB(NOW(), INTERVAL 1 month) AND NOW() ORDER BY TESTTIME AND STUDENTID = '" + student.getId() + "') db group by date(TESTTIME) order by TESTTIME;";
                    String m2 = "select dayofmonth(TESTTIME) Day, avg(SCORE) DailyAvg, LEVEL from (SELECT * FROM RESULTS WHERE LEVEL = 'M' AND TESTTIME BETWEEN DATE_SUB(NOW(), INTERVAL 1 month) AND NOW() ORDER BY TESTTIME AND STUDENTID = '" + student.getId() + "') db group by date(TESTTIME) order by TESTTIME;";
                    String m3 = "select dayofmonth(TESTTIME) Day, avg(SCORE) DailyAvg, LEVEL from (SELECT * FROM RESULTS WHERE LEVEL = 'H' AND TESTTIME BETWEEN DATE_SUB(NOW(), INTERVAL 1 month) AND NOW() ORDER BY TESTTIME AND STUDENTID = '" + student.getId() + "') db group by date(TESTTIME) order by TESTTIME;";
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
    
    
    public void setStudent(Student student) {
        this.student = student;
    }


}
