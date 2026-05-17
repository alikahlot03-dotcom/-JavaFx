package FinanceTrackerController;

import java.io.BufferedReader;
import java.io.FileReader;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import javafx.scene.control.cell.PropertyValueFactory;

import model.Transaction;

import Utils.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

/*
    هذا الكلاس مسؤول عن صفحة التقارير Reports
    
    وظيفته:
    - قراءة العمليات المالية من الملف
    - عرضها داخل الجدول
    - حساب:
        مجموع الدخل Income
        مجموع المصروف Expense
        الرصيد النهائي Balance
*/

public class ReportsController implements Initializable {

    
    // Labels لعرض القيم النهائية
    @FXML
    private Label lblIncome;

    @FXML
    private Label lblExpense;

    @FXML
    private Label lblBalance;

    
    
    // TableView لعرض العمليات المالية
    @FXML
    private TableView<Transaction> tableTransactions;

    
    
    // أعمدة الجدول
    @FXML
    private TableColumn<Transaction, Integer> colId;

    @FXML
    private TableColumn<Transaction, String> colName;

    @FXML
    private TableColumn<Transaction, Double> colAmount;

    @FXML
    private TableColumn<Transaction, String> colType;

    
    
    /*
        ObservableList
        
        استخدمناها لتخزين البيانات بشكل ديناميكي
        بحيث أي تغيير على البيانات يظهر مباشرة داخل الجدول
    */
    private ObservableList<Transaction> transactionList =
            FXCollections.observableArrayList();

    
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {

        /*
            ربط أعمدة الجدول مع بيانات كلاس Transaction
            
            مثلًا:
            عمود ID يأخذ القيمة من getId()
        */
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));

        colName.setCellValueFactory(new PropertyValueFactory<>("category"));

        colAmount.setCellValueFactory(new PropertyValueFactory<>("amount"));

        colType.setCellValueFactory(new PropertyValueFactory<>("type"));

        
        
        // تحميل العمليات من الملف
        loadTransactions();

        
        // حساب التقرير المالي
        calculateReport();
    }

    
    
    
    /*
        هذه الدالة تقرأ البيانات من ملف transactions.txt
        ثم تحول كل سطر إلى object من نوع Transaction
    */
   void loadTransactions() {

    ObservableList<Transaction> list =
            FXCollections.observableArrayList();

    try {

        Connection con = DBConnection.connect();

        String query =
                "SELECT * FROM Transactions";

        PreparedStatement ps =
                con.prepareStatement(query);

        ResultSet rs = ps.executeQuery();

        while (rs.next()) {

            list.add(

                    new Transaction(

                            rs.getInt("id"),

                            rs.getString("category"),

                            rs.getDouble("amount"),

                            rs.getString("type"),

                            rs.getDate("date")
                                    .toLocalDate()
                    )
            );
        }

        tableTransactions.setItems(list);

    } catch (Exception e) {

        e.printStackTrace();
    }
}

    
    
    
    /*
        هذه الدالة مسؤولة عن حساب التقرير
        
        استخدمنا Java Streams
        لحساب مجموع الدخل والمصروف بشكل احترافي
    */
    private void calculateReport() {

    double totalIncome = 0;

    double totalExpense = 0;

    try {

        Connection con = DBConnection.connect();

        String query =
                "SELECT * FROM Transactions";

        PreparedStatement ps =
                con.prepareStatement(query);

        ResultSet rs = ps.executeQuery();

        while (rs.next()) {

            double amount =
                    rs.getDouble("amount");

            String type =
                    rs.getString("type");

            if (type.equalsIgnoreCase("Income")) {

                totalIncome += amount;

            } else {

                totalExpense += amount;
            }
        }

    } catch (Exception e) {

        e.printStackTrace();
    }

    double balance =
            totalIncome - totalExpense;

    lblIncome.setText("" + totalIncome);

    lblExpense.setText("" + totalExpense);

    lblBalance.setText("" + balance);
}
}