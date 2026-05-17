package FinanceTrackerController;

import Utils.DBConnection;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import model.DataStore;
import model.Transaction;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * [Controller Layer: TransactionsController]
 * المتحكم الرئيسي المسؤول عن الربط بين واجهة المستخدم (View) ومنطق البيانات (Model).
 * يطبق هذا الكلاس أفضل الممارسات البرمجية المطلوبة في المشاريع الجامعية الاحترافية.
 */
public class TransactionsController {

    // حقول الإدخال
    @FXML private TextField amountField;
    @FXML private DatePicker datePicker;
    @FXML private ComboBox<String> categoryComboBox;
    @FXML private ComboBox<String> typeComboBox;

    // أدوات البحث والفلترة
    @FXML private TextField searchField;
    @FXML private DatePicker searchDatePicker;
    @FXML private ComboBox<String> sortComboBox;

    // الجدول والأعمدة
    @FXML private TableView<Transaction> transactionsTable;
    @FXML private TableColumn<Transaction, Integer> idColumn;
    @FXML private TableColumn<Transaction, String> categoryColumn;
    @FXML private TableColumn<Transaction, Double> amountColumn;
    @FXML private TableColumn<Transaction, String> typeColumn;
    @FXML private TableColumn<Transaction, LocalDate> dateColumn;

    // القوائم المغلفة (Wrappers) للبحث والفرز
    private FilteredList<Transaction> filteredData;
    private SortedList<Transaction> sortedData;

    private final String CATEGORIES_FILE = "Document/categories.txt";

    @FXML
    public void initialize() {
        setupTableColumns();
        loadCategories();
        
        // 1. تحميل البيانات من قاعدة البيانات (عبر DataStore)
        DataStore.loadTransactions();

        // 2. تطبيق الـ FilteredList (لدعم البحث اللحظي)
        filteredData = new FilteredList<>(DataStore.transactions, p -> true);

        // 3. ربط البحث اللحظي بمستمع (Listener)
        searchField.textProperty().addListener((observable, oldValue, newValue) -> applyFilters());
        searchDatePicker.valueProperty().addListener((observable, oldValue, newValue) -> applyFilters());

        // 4. تطبيق الـ SortedList (لدعم الفرز اللحظي)
        sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(transactionsTable.comparatorProperty());

        // 5. ربط الجدول بالبيانات المفلترة والمفرزة
        transactionsTable.setItems(sortedData);

        // 6. تهيئة قوائم الاختيار
        typeComboBox.setItems(FXCollections.observableArrayList("Income", "Expense"));
        sortComboBox.setItems(FXCollections.observableArrayList("Amount (Asc)", "Amount (Desc)", "Date (Asc)", "Date (Desc)"));
        sortComboBox.setOnAction(e -> handleSortSelection());
        
        // 7. مستمع لاختيار صف من الجدول (لملء الحقول تلقائياً عند التعديل)
        transactionsTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                populateFields(newSelection);
            }
        });
    }

    private void setupTableColumns() {
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        categoryColumn.setCellValueFactory(new PropertyValueFactory<>("category"));
        amountColumn.setCellValueFactory(new PropertyValueFactory<>("amount"));
        typeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
    }

private void loadCategories() {

    try {

        Connection con = DBConnection.connect();

        String query =
                "SELECT * FROM Categories";

        PreparedStatement ps =
                con.prepareStatement(query);

        ResultSet rs = ps.executeQuery();

        ObservableList<String> categories =
                FXCollections.observableArrayList();

        while (rs.next()) {

            categories.add(
                    rs.getString("name"));
        }

        categoryComboBox.setItems(categories);

    } catch (Exception e) {

        e.printStackTrace();
    }
}

    /**
     * عملية الإضافة (Create)
     */
    @FXML
    private void handleAddTransaction() {
        if (!validateInputs()) return;

        Transaction t = new Transaction(0, categoryComboBox.getValue(), 
                                      Double.parseDouble(amountField.getText()), 
                                      typeComboBox.getValue(), datePicker.getValue());

        if (DataStore.addTransaction(t)) {
            clearFields();
            showAlert(Alert.AlertType.INFORMATION, "Success", "Transaction added successfully to MySQL!");
        }
    }

    /**
     * عملية التعديل (Update)
     */
    @FXML
    private void handleUpdateTransaction() {
        Transaction selected = transactionsTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert(Alert.AlertType.WARNING, "Selection Required", "Please select a transaction to update.");
            return;
        }

        if (!validateInputs()) return;

        selected.setCategory(categoryComboBox.getValue());
        selected.setAmount(Double.parseDouble(amountField.getText()));
        selected.setType(typeComboBox.getValue());
        selected.setDate(datePicker.getValue());

        if (DataStore.updateTransaction(selected)) {
            showAlert(Alert.AlertType.INFORMATION, "Success", "Transaction updated successfully!");
            transactionsTable.refresh();
        }
    }

    /**
     * عملية الحذف (Delete)
     */
    @FXML
    private void handleDeleteTransaction() {
        Transaction selected = transactionsTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert(Alert.AlertType.WARNING, "Selection Required", "Please select a transaction to delete.");
            return;
        }

        // تأكيد الحذف
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to delete this transaction?", ButtonType.YES, ButtonType.NO);
        confirm.showAndWait().ifPresent(response -> {
            if (response == ButtonType.YES) {
                if (DataStore.deleteTransaction(selected.getId())) {
                    showAlert(Alert.AlertType.INFORMATION, "Deleted", "Transaction removed successfully!");
                }
            }
        });
    }

    /**
     * تطبيق الفلاتر (Search Logic)
     */
    @FXML
    private void handleSearch() {
        applyFilters();
    }

    private void applyFilters() {
        String searchText = searchField.getText() == null ? "" : searchField.getText().toLowerCase();
        LocalDate searchDate = searchDatePicker.getValue();

        filteredData.setPredicate(transaction -> {
            boolean matchesSearch = searchText.isEmpty() || transaction.getCategory().toLowerCase().contains(searchText);
            boolean matchesDate = (searchDate == null) || transaction.getDate().equals(searchDate);
            return matchesSearch && matchesDate;
        });
    }

    /**
     * منطق الفرز (Sorting Logic)
     */
    private void handleSortSelection() {
        String sortType = sortComboBox.getValue();
        if (sortType == null) return;

        switch (sortType) {
            case "Amount (Asc)":
                transactionsTable.getSortOrder().clear();
                amountColumn.setSortType(TableColumn.SortType.ASCENDING);
                transactionsTable.getSortOrder().add(amountColumn);
                break;
            case "Amount (Desc)":
                transactionsTable.getSortOrder().clear();
                amountColumn.setSortType(TableColumn.SortType.DESCENDING);
                transactionsTable.getSortOrder().add(amountColumn);
                break;
            case "Date (Asc)":
                transactionsTable.getSortOrder().clear();
                dateColumn.setSortType(TableColumn.SortType.ASCENDING);
                transactionsTable.getSortOrder().add(dateColumn);
                break;
            case "Date (Desc)":
                transactionsTable.getSortOrder().clear();
                dateColumn.setSortType(TableColumn.SortType.DESCENDING);
                transactionsTable.getSortOrder().add(dateColumn);
                break;
        }
    }

    @FXML
    private void handleReset() {
        searchField.clear();
        searchDatePicker.setValue(null);
        sortComboBox.setValue(null);
        filteredData.setPredicate(p -> true);
        transactionsTable.getSortOrder().clear();
    }

    private void populateFields(Transaction t) {
        amountField.setText(String.valueOf(t.getAmount()));
        datePicker.setValue(t.getDate());
        categoryComboBox.setValue(t.getCategory());
        typeComboBox.setValue(t.getType());
    }

    private void clearFields() {
        amountField.clear();
        datePicker.setValue(LocalDate.now());
        categoryComboBox.setValue(null);
        typeComboBox.setValue(null);
    }

    private boolean validateInputs() {
        if (amountField.getText().isEmpty() || categoryComboBox.getValue() == null ||
            typeComboBox.getValue() == null || datePicker.getValue() == null) {
            showAlert(Alert.AlertType.ERROR, "Input Error", "Please fill all fields!");
            return false;
        }
        try {
            Double.parseDouble(amountField.getText());
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Input Error", "Amount must be a number!");
            return false;
        }
        return true;
    }

    private void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);

        DialogPane dialogPane = alert.getDialogPane();
        String css = getClass().getResource("/css/styleProject.css").toExternalForm();
        if (css != null) {
            dialogPane.getStylesheets().add(css);
            dialogPane.getStyleClass().add("alert");

            if (type == Alert.AlertType.INFORMATION) {
                dialogPane.getStyleClass().add("alert-success");
            } else if (type == Alert.AlertType.ERROR || type == Alert.AlertType.WARNING) {
                dialogPane.getStyleClass().add("alert-error");
            }
        }

        alert.showAndWait();
    }
}