/*
 * ============================================================
 * [Controller Class: TransactionsController]
 * يمثل هذا الملف الـ (Controller) المسؤول عن صفحة المعاملات المالية.
 * يطبق نمط MVC Pattern حيث:
 *   - Model: كلاس Transaction و DataStore
 *   - View: ملف Transactions.fxml
 *   - Controller: هذا الملف
 * ============================================================
 */
package FinanceTrackerController;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import model.DataStore;
import model.Transaction;

// مكتبات Java NIO و Streams للتعامل مع الملفات بكفاءة عالية
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * ==========================================
 * إعادة هيكلة صفحة المعاملات (Phase 2 Refactoring)
 * ==========================================
 * المتحكم الرئيسي لصفحة إدارة المعاملات المالية.
 * التعديلات الجديدة:
 * 1. استبدال المعالجة المباشرة للملفات بالاعتماد على In-memory processing عبر كلاس DataStore.
 * 2. استخدام Java Streams في عمليات الفلترة (Filtering) والفرز (Sorting) للحصول على أداء عالي.
 * 3. تطبيق نمط MVC بفصل مسؤولية تخزين البيانات عن واجهة المستخدم بشكل كامل.
 */
public class TransactionsController {

    // =============================================
    // عناصر الواجهة المرتبطة بـ @FXML (Scene Graph Binding)
    // =============================================
    @FXML
    private TextField amountField;
    @FXML
    private DatePicker datePicker;
    @FXML
    private ComboBox<String> categoryComboBox;
    @FXML
    private ComboBox<String> typeComboBox;
    @FXML
    private Button addButton;

    // Search & Sort UI
    @FXML
    private TextField searchField;
    @FXML
    private DatePicker searchDatePicker;
    @FXML
    private ComboBox<String> sortComboBox;

    // جدول عرض المعاملات وأعمدته
    @FXML
    private TableView<Transaction> transactionsTable;
    @FXML
    private TableColumn<Transaction, Integer> idColumn;
    @FXML
    private TableColumn<Transaction, String> categoryColumn;
    @FXML
    private TableColumn<Transaction, Double> amountColumn;
    @FXML
    private TableColumn<Transaction, String> typeColumn;
    @FXML
    private TableColumn<Transaction, LocalDate> dateColumn;

    private final String CATEGORIES_FILE = "Document/categories.txt";

    /**
     * دالة التهيئة (@FXML initialize):
     */
    @FXML
    public void initialize() {
        // ربط أعمدة الجدول بخصائص كلاس Transaction
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        categoryColumn.setCellValueFactory(new PropertyValueFactory<>("category"));
        amountColumn.setCellValueFactory(new PropertyValueFactory<>("amount"));
        typeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));

        // تحميل البيانات من الملفات إلى الذاكرة
        loadCategories();
        DataStore.loadTransactions();

        // ربط الجدول بالقائمة الموجودة في الذاكرة المركزية
        transactionsTable.setItems(DataStore.transactions);

        // تهيئة قوائم الاختيار
        typeComboBox.setItems(FXCollections.observableArrayList("Income", "Expense"));
        datePicker.setValue(LocalDate.now());

        // تهيئة قائمة الفرز (Sort ComboBox)
        sortComboBox.setItems(
                FXCollections.observableArrayList("Amount (Asc)", "Amount (Desc)", "Date (Asc)", "Date (Desc)"));
        sortComboBox.setOnAction(e -> applySort());
    }

    /**
     * تحميل الفئات باستخدام Java Streams
     */
   private void loadCategories() {

    try (Stream<String> lines =
                 Files.lines(Paths.get(CATEGORIES_FILE))) {

        List<String> categories = lines

                .filter(line -> !line.trim().isEmpty())

                .map(line -> line.split(",")[1].trim())

                .collect(Collectors.toList());

        categoryComboBox.setItems(
                FXCollections.observableArrayList(categories)
        );

    } catch (IOException e) {

        System.err.println(
                "Error loading categories: "
                        + e.getMessage()
        );
    }
}

    /**
     * معالج حدث إضافة معاملة جديدة
     */
    @FXML
    private void handleAddTransaction() {
        if (!validateInputs())
            return;

        try {
            int newId = DataStore.transactions.size() + 1;
            String category = categoryComboBox.getValue();
            double amount = Double.parseDouble(amountField.getText());
            String type = typeComboBox.getValue();
            LocalDate date = datePicker.getValue();

            Transaction newTransaction = new Transaction(newId, category, amount, type, date);

            // إضافة البيانات إلى DataStore (يتحدث الجدول فوراً ويحفظ في الملف)
            DataStore.addTransaction(newTransaction);

            amountField.clear();
            showAlert(Alert.AlertType.INFORMATION, "Message", "Transaction saved successfully!");

            // Reapply current search/sort silently
            applyCurrentFilterAndSort();

        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Fieled", "Could not save transaction: " + e.getMessage());
        }
    }

    /**
     * ==========================================
     * وظيفة البحث والفلترة (Streams Filtering - Phase 2)
     * ==========================================
     * يتم استخدام Java Streams لفلترة البيانات المخزنة في الذاكرة (DataStore) 
     * ديناميكياً بدلاً من إجراء حلقات تكرار (Loops) تقليدية.
     * هذا يضمن تحديث الجدول اللحظي وعرض النتائج فوراً بكفاءة عالية.
     */
    @FXML
    private void handleSearch() {
        String searchText = searchField.getText() == null ? "" : searchField.getText().trim().toLowerCase();
        LocalDate searchDate = searchDatePicker.getValue();

        if (searchText.isEmpty() && searchDate == null) {
            showAlert(Alert.AlertType.WARNING, "Warning", "Please enter a category or select a date to search.");
            transactionsTable.setItems(DataStore.transactions);
            applySort();
            return;
        }

        applyCurrentFilterAndSort();
    }

    /**
     * تحديث الفلترة والفرز بصمت دون إظهار تنبيهات (للاستخدام البرمجي)
     */
    private void applyCurrentFilterAndSort() {
        String searchText = searchField.getText() == null ? "" : searchField.getText().trim().toLowerCase();
        LocalDate searchDate = searchDatePicker.getValue();

        // إذا لم يكن هناك بحث نشط، نعرض كل البيانات
        if (searchText.isEmpty() && searchDate == null) {
            transactionsTable.setItems(DataStore.transactions);
            applySort();
            return;
        }

        List<Transaction> filtered = DataStore.transactions.stream()
                .filter(t -> (searchText.isEmpty() || t.getCategory().toLowerCase().contains(searchText)) &&
                        (searchDate == null || t.getDate().equals(searchDate)))
                .collect(Collectors.toList());

        transactionsTable.setItems(FXCollections.observableArrayList(filtered));
        applySort();
    }

    /**
     * إعادة تعيين الفلترة وإظهار كل البيانات
     */
    @FXML
    private void handleReset() {
        searchField.clear();
        searchDatePicker.setValue(null);
        sortComboBox.setValue(null);
        transactionsTable.setItems(DataStore.transactions);
    }

    /**
     * ==========================================
     * وظيفة الفرز اللحظي (Streams Sorting - Phase 2)
     * ==========================================
     * تستخدم هذه الدالة ميثود .sorted() الخاصة بالـ Streams لترتيب عناصر الجدول الحالية
     * بناءً على نوع الفرز المختار (المبلغ أو التاريخ).
     * يتم تمرير Comparator عبر تعبيرات لامبدا (Lambda expressions) لتبسيط الكود.
     * الميزة الأهم: الفرز يتم فوراً دون الحاجة لإعادة تحميل الصفحة أو إعادة قراءة الملف.
     */
    private void applySort() {
        String sortType = sortComboBox.getValue();
        if (sortType == null)
            return;

        ObservableList<Transaction> currentItems = transactionsTable.getItems();
        List<Transaction> sortedList = currentItems.stream()
                .sorted((t1, t2) -> {
                    switch (sortType) {
                        case "Amount (Asc)":
                            return Double.compare(t1.getAmount(), t2.getAmount());
                        case "Amount (Desc)":
                            return Double.compare(t2.getAmount(), t1.getAmount());
                        case "Date (Asc)":
                            return t1.getDate().compareTo(t2.getDate());
                        case "Date (Desc)":
                            return t2.getDate().compareTo(t1.getDate());
                        default:
                            return 0;
                    }
                })
                .collect(Collectors.toList());

        transactionsTable.setItems(FXCollections.observableArrayList(sortedList));
    }

    /**
     * التحقق من صحة المدخلات
     */
    private boolean validateInputs() {
        if (amountField.getText().isEmpty() || categoryComboBox.getValue() == null ||
                typeComboBox.getValue() == null || datePicker.getValue() == null) {
            showAlert(Alert.AlertType.ERROR, "Fieled", "Please complete all fields!");
            return false;
        }

        try {
            double amount = Double.parseDouble(amountField.getText());
            if (amount <= 0) {
                showAlert(Alert.AlertType.ERROR, "Fieled", "Amount must be a positive number!");
                return false;
            }
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Fieled", "Amount must be a numeric value!");
            return false;
        }

        return true;
    }

    /**
     * إظهار رسالة تنبيه
     */
    private void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(title);
        alert.setContentText(content);

        String css = getClass().getResource("/css/styleProject.css").toExternalForm();
        if (css != null) {
            alert.getDialogPane().getStylesheets().add(css);
        }

        alert.showAndWait();
    }
}