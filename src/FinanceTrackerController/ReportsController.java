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

        
        // تنظيف الليست قبل إعادة التحميل
        transactionList.clear();

        try {

            BufferedReader reader = new BufferedReader(
                    new FileReader("Document/transactions.txt")
            );

            String line;

            
            // قراءة الملف سطر سطر
            while ((line = reader.readLine()) != null) {

                
                // تجاهل الأسطر الفارغة
                if (line.trim().isEmpty()) {
                    continue;
                }

                
                /*
                    تقسيم السطر باستخدام الفاصلة
                    
                    مثال:
                    1,Food,50,Expense,2025-08-01
                */
                String[] parts = line.split(",");

                
                
                // إنشاء object جديد من Transaction
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

    
    
    
    /*
        هذه الدالة مسؤولة عن حساب التقرير
        
        استخدمنا Java Streams
        لحساب مجموع الدخل والمصروف بشكل احترافي
    */
    void calculateReport() {

        
        
        /*
            حساب مجموع الـ Income
            
            الخطوات:
            1- فلترة العمليات من نوع Income
            2- استخراج قيمة amount
            3- جمع القيم
        */
        double totalIncome = transactionList.stream()

                .filter(t -> t.getType().equalsIgnoreCase("Income"))

                .mapToDouble(Transaction::getAmount)

                .sum();

        
        
        
        /*
            حساب مجموع الـ Expense بنفس الطريقة
        */
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