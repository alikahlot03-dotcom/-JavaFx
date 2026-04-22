/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package FinanceTrackerController;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;


/**
 * FXML Controller class
 *
 * @author msi
 */
public class ReportsController implements Initializable {

    @FXML
    private Label lblIncome;
    @FXML
    private Label lblExpense;
    @FXML
    private Label lblBalance;
    @FXML
    private TableView<?> tableTransactions;
    @FXML
    private TableColumn<Category, Integer> colId ;
    @FXML
    private TableColumn<Category, String> colName;
    @FXML
    private TableColumn<?, ?> colAmount;
    @FXML
    private TableColumn<?, ?> colType;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //هادي استبدلوها ب  calculateReport() ;
         showDummyData(); 
    }
    //طبعا هادي دالة مبدئية عشان مش عندي كود الترانزاكشن استبدلوها بالدالة التي بالاسفل 
      private void showDummyData() {

        double totalIncome = 500;
        double totalExpense = 150;
        double balance = totalIncome - totalExpense;

        lblIncome.setText("" + totalIncome);
        lblExpense.setText("" + totalExpense);
        lblBalance.setText("" + balance);
    }
      /*
      private void calculateReport() {
    double totalIncome = 0;
    double totalExpense = 0;

    try {
        BufferedReader reader = new BufferedReader(
            new FileReader("حطو هان مسار transaction.txt")
        );

        String line;

        while ((line = reader.readLine()) != null) {

            if (line.trim().isEmpty()) continue;

            String[] parts = line.split(",");

            double amount = Double.parseDouble(parts[2]);
            String type = parts[3];

            if (type.equalsIgnoreCase("Income")) {
                totalIncome += amount;
            } else {
                totalExpense += amount;
            }
        }

        reader.close();

    } catch (Exception e) {
        e.printStackTrace();
    }

    double balance = totalIncome - totalExpense;

    lblIncome.setText("" + totalIncome);
    lblExpense.setText("" + totalExpense);
    lblBalance.setText("" + balance);
}
      */
      
   
    
    
}