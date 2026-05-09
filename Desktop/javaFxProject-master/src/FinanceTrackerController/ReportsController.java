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

public class ReportsController implements Initializable {

    @FXML
    private Label lblIncome;

    @FXML
    private Label lblExpense;

    @FXML
    private Label lblBalance;

    @FXML
    private TableView<Transaction> tableTransactions;

    //  تعديل جديد:
    // غيرنا Category -> Transaction
    // لأن الجدول يعرض عمليات مالية مش تصنيفات
    @FXML
    private TableColumn<Transaction, Integer> colId;

    @FXML
    private TableColumn<Transaction, String> colName;

    @FXML
    private TableColumn<Transaction, Double> colAmount;

    @FXML
    private TableColumn<Transaction, String> colType;

    
    //  تعديل جديد مهم:
    // خزنا البيانات داخل ObservableList
    // عشان نستخدمها بالجدول + بالحسابات + بالStreams
    private ObservableList<Transaction> transactionList =
            FXCollections.observableArrayList();

    
    @Override
    public void initialize(URL location, ResourceBundle resources) {

        // ربط أعمدة الجدول مع بيانات Transaction
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));

        colName.setCellValueFactory(new PropertyValueFactory<>("category"));

        colAmount.setCellValueFactory(new PropertyValueFactory<>("amount"));

        colType.setCellValueFactory(new PropertyValueFactory<>("type"));

        
        // تحميل البيانات من الملف
        loadTransactions();

        
        // حساب التقرير
        calculateReport();
    }

    
    
    //  تحميل العمليات من الملف
    void loadTransactions() {

        // تنظيف الليست قبل التحميل
        transactionList.clear();

        try {

            BufferedReader reader = new BufferedReader(
                    new FileReader("Document/transactions.txt")
            );

            String line;

            while ((line = reader.readLine()) != null) {

                // تجاهل الأسطر الفاضية
                if (line.trim().isEmpty()) {
                    continue;
                }

                
                // تقسيم السطر باستخدام الفاصلة
                String[] parts = line.split(",");

                
                // إنشاء object من نوع Transaction
                transactionList.add(new Transaction(

                        Integer.parseInt(parts[0].trim()),

                        parts[1].trim(),

                        Double.parseDouble(parts[2].trim()),

                        parts[3].trim(),

                        java.time.LocalDate.parse(parts[4].trim())
                ));
            }

            reader.close();

        } catch (Exception e) {

            e.printStackTrace();
        }

        
        // عرض البيانات داخل الجدول
        tableTransactions.setItems(transactionList);
    }

    
    
    //  حساب التقرير باستخدام Streams
    void calculateReport() {

        
        // حساب مجموع الـ Income
        double totalIncome = transactionList.stream()

                // فلترة العمليات من نوع Income
                .filter(t -> t.getType().equalsIgnoreCase("Income"))

                // تحويل العمليات إلى أرقام amount
                .mapToDouble(Transaction::getAmount)

                // جمعهم
                .sum();

        
        
        // حساب مجموع الـ Expense
        double totalExpense = transactionList.stream()

                .filter(t -> t.getType().equalsIgnoreCase("Expense"))

                .mapToDouble(Transaction::getAmount)

                .sum();

        
        
        // حساب الرصيد النهائي
        double balance = totalIncome - totalExpense;

        
        
        // عرض النتائج داخل الـ Labels
        lblIncome.setText(String.valueOf(totalIncome));

        lblExpense.setText(String.valueOf(totalExpense));

        lblBalance.setText(String.valueOf(balance));
    }
}