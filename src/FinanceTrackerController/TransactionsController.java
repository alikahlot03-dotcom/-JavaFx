package FinanceTrackerController;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import model.Transaction;

import java.io.*;
import java.nio.file.Files;
<<<<<<< HEAD
import java.nio.file.Path;
=======
>>>>>>> e98cf67381ea005d1e0a268478373fdda18961bd
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalDate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * كود التحكم في صفحة المعاملات - المرحلة الأولى [3، 6].
 */
public class TransactionsController {

    @FXML private TextField amountField;
    @FXML private DatePicker datePicker;
    @FXML private ComboBox<String> categoryComboBox;
    @FXML private ComboBox<String> typeComboBox;
    @FXML private Button addButton;

    @FXML private TableView<Transaction> transactionsTable;
    @FXML private TableColumn<Transaction, Integer> idColumn;
    @FXML private TableColumn<Transaction, String> categoryColumn;
    @FXML private TableColumn<Transaction, Double> amountColumn;
    @FXML private TableColumn<Transaction, String> typeColumn;
    @FXML private TableColumn<Transaction, LocalDate> dateColumn;

    private ObservableList<Transaction> transactionList = FXCollections.observableArrayList();
<<<<<<< HEAD
     final String TRANSACTIONS_FILE = "Document/transactions.txt";
      final String CATEGORIES_FILE = "Document/categories.txt";
=======
    private final String TRANSACTIONS_FILE = "transactions.txt";
    private final String CATEGORIES_FILE = "categories.txt";
>>>>>>> e98cf67381ea005d1e0a268478373fdda18961bd

    @FXML
    public void initialize() {
        // 1. إعداد أعمدة الجدول [10، 62]
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        categoryColumn.setCellValueFactory(new PropertyValueFactory<>("category"));
        amountColumn.setCellValueFactory(new PropertyValueFactory<>("amount"));
        typeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));

        // 2. تحميل البيانات الأولية
        loadCategories();
        loadTransactions();
        transactionsTable.setItems(transactionList);

        // 3. ضبط خيارات النوع
        typeComboBox.setItems(FXCollections.observableArrayList("Income", "Expense"));
        
        // ضبط التاريخ الافتراضي لليوم
        datePicker.setValue(LocalDate.now());
    }

<<<<<<< HEAD

    void loadCategories() {
    try (Stream<String> lines = Files.lines(Paths.get(CATEGORIES_FILE))) {
        ObservableList<String> categoryNames = lines
            .filter(line -> !line.trim().isEmpty() && line.contains(","))
            .map(line -> line.split(",")[1]) 
            .collect(Collectors.toCollection(FXCollections::observableArrayList));
            
        categoryComboBox.setItems(categoryNames);
    } catch (IOException e) {
        System.err.println("Error loading categories: " + e.getMessage());
    }
}
 
   
  
    
     void loadTransactions() {
=======
    private void loadCategories() {
        try (Stream<String> lines = Files.lines(Paths.get(CATEGORIES_FILE))) {
            categoryComboBox.setItems(FXCollections.observableArrayList(lines.collect(Collectors.toList())));
        } catch (IOException e) {
            System.err.println("Error loading categories: " + e.getMessage());
        }
    }

    private void loadTransactions() {
>>>>>>> e98cf67381ea005d1e0a268478373fdda18961bd
        if (!Files.exists(Paths.get(TRANSACTIONS_FILE))) return;

        try (Stream<String> lines = Files.lines(Paths.get(TRANSACTIONS_FILE))) {
            transactionList.clear();
            lines.filter(line -> !line.trim().isEmpty())
                 .forEach(line -> {
                     String[] parts = line.split(",");
                     if (parts.length == 5) {
                         transactionList.add(new Transaction(
                                 Integer.parseInt(parts[0]),
<<<<<<< HEAD
                                   parts[1],
                                
=======
                                 parts[1],
>>>>>>> e98cf67381ea005d1e0a268478373fdda18961bd
                                 Double.parseDouble(parts[2]),
                                 parts[3],
                                 LocalDate.parse(parts[4])
                         ));
                     }
                 });
        } catch (IOException e) {
            System.err.println("Error loading transactions: " + e.getMessage());
        }
    }

    @FXML
<<<<<<< HEAD
     void handleAddTransaction() {
=======
    private void handleAddTransaction() {
>>>>>>> e98cf67381ea005d1e0a268478373fdda18961bd
        if (!validateInputs()) return;

        try {
            int newId = transactionList.size() + 1;
            String category = categoryComboBox.getValue();
            double amount = Double.parseDouble(amountField.getText());
            String type = typeComboBox.getValue();
            LocalDate date = datePicker.getValue();

            Transaction newTransaction = new Transaction(newId, category, amount, type, date);
            
            // حفظ في الملف [3، 13]
            saveTransactionToFile(newTransaction);
            
            // تحديث الجدول فورياً [89، 145]
            transactionList.add(newTransaction);
            
            // مسح الحقول بعد النجاح
            amountField.clear();
            
            // تنبيه نجاح العملية [4، 11]
            showAlert(Alert.AlertType.INFORMATION, "Success", "Transaction saved successfully!");

        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "System Error", "Could not save transaction: " + e.getMessage());
        }
    }

<<<<<<< HEAD
     boolean validateInputs() {
=======
    private boolean validateInputs() {
>>>>>>> e98cf67381ea005d1e0a268478373fdda18961bd
        // التحقق من الحقول الفارغة [4، 11]
        if (amountField.getText().isEmpty() || categoryComboBox.getValue() == null || 
            typeComboBox.getValue() == null || datePicker.getValue() == null) {
            showAlert(Alert.AlertType.ERROR, "Input Error", "Please complete all fields!");
            return false;
        }

        // التحقق من صحة المبلغ [4، 11]
        try {
            double amount = Double.parseDouble(amountField.getText());
            if (amount <= 0) {
                showAlert(Alert.AlertType.ERROR, "Amount Error", "Amount must be a positive number!");
                return false;
            }
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Amount Error", "Amount must be a numeric value!");
            return false;
        }

        return true;
    }

<<<<<<< HEAD
      void saveTransactionToFile(Transaction t) throws IOException {
=======
    private void saveTransactionToFile(Transaction t) throws IOException {
>>>>>>> e98cf67381ea005d1e0a268478373fdda18961bd
        String data = t.toString() + System.lineSeparator();
        Files.write(Paths.get(TRANSACTIONS_FILE), data.getBytes(), StandardOpenOption.CREATE, StandardOpenOption.APPEND);
    }

<<<<<<< HEAD
//    private void showAlert(Alert.AlertType type, String title, String content) {
//        Alert alert = new Alert(type);
//        alert.setTitle(title);
//        alert.setHeaderText(null);
//        alert.setContentText(content);
//        
////        alert.getDialogPane().getStyleClass().add("alert-success");
//        alert.getDialogPane().getStylesheets().add(getClass().getResource("/css/styleProject.css").toExternalForm());
//        alert.showAndWait();
//       
//    }
    
    private void showAlert(Alert.AlertType type, String title, String content, boolean isSuccess) {
    Alert alert = new Alert(type);
    alert.setTitle(title);
    alert.setHeaderText(null);
    alert.setContentText(content);
    
   
    alert.getDialogPane().getStylesheets().add(getClass().getResource("/css/styleProject.css").toExternalForm());
    
   
    if (isSuccess) {
        alert.getDialogPane().getStyleClass().add("alert-success");
    } else {
        
        alert.getDialogPane().getStyleClass().add("alert"); 
    }

    alert.showAndWait();
}


   private void showAlert(Alert.AlertType type, String title, String content) {
    showAlert(type, title, content, false);
}
     
=======
    private void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
>>>>>>> e98cf67381ea005d1e0a268478373fdda18961bd
}
