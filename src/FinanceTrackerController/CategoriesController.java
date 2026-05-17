package FinanceTrackerController;
import Utils.Session;
import java.sql.Connection;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import java.net.URL;

import java.util.Comparator;
import java.util.List;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import javafx.event.ActionEvent;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

import javafx.scene.control.cell.PropertyValueFactory;

import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;

import model.Category;


import Utils.DBConnection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import static model.DataStore.loadTransactions;

/**
 * Controller responsible for managing categories.
 * Handles:
 * - Adding categories
 * - Deleting categories
 * - Searching categories
 * - Sorting categories alphabetically
 * - Saving and reading categories from file
 */
public class CategoriesController implements Initializable {

    @FXML
    private FlowPane flowpane;

    @FXML
    private VBox vbox1;

    @FXML
    private Label labeltitle;

    @FXML
    private Label labelcategoryname;

    @FXML
    private TextField txtCategoryName;

    @FXML
    private TextField txtSearch;

    @FXML
    private Button btnAdd;

    @FXML
    private Button btnDelete;

    @FXML
    private TableView<Category> tableCategories;

    @FXML
    private TableColumn<Category, Integer> colId;

    @FXML
    private TableColumn<Category, String> colName;

    
    // ObservableList is used to store categories dynamically
    // Any update on this list appears directly in the TableView
    private ObservableList<Category> categoryList =
            FXCollections.observableArrayList();

    
    // Counter used to generate IDs
   // private int idCounter = 1;

    
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {

        // Linking table columns with Category class properties
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));

        colName.setCellValueFactory(new PropertyValueFactory<>("name"));

        
        // Load categories from file when page opens
        loadCategories();

        
        // Display data inside the table
        tableCategories.setItems(categoryList);

        
        // Add listener for live searching
        txtSearch.textProperty().addListener((obs, oldVal, newVal) -> {
            searchCategory();
        });

        
        // Set next available ID
       // setNextId();

        
        // Sort categories alphabetically
        sortAndReindex();
    }

    
    
    /**
     * Reads categories from categories.txt file
     * and stores them inside ObservableList
     */
  private void loadCategories() {

    categoryList.clear();

    try {

        Connection con = DBConnection.connect();

                 String query =
               "SELECT * FROM Categories "
               + "WHERE user_id=? "
               + "ORDER BY name ASC";
          
        PreparedStatement ps =
                con.prepareStatement(query);
        
        ps.setInt(1,
        Session.currentUserId);

        ResultSet rs = ps.executeQuery();

        while (rs.next()) {

            categoryList.add(

                    new Category(

                            rs.getInt("id"),

                            rs.getString("name")
                    )
            );
        }

    } catch (Exception e) {

        e.printStackTrace();
    }
}

    
    
    /**
     * Handles searching categories using Java Streams
     */
   private void searchCategory() {

    String searchText =
            txtSearch.getText().toLowerCase();

    if (searchText.isEmpty()) {

        tableCategories.setItems(categoryList);

        return;
    }

    List<Category> filteredList =

            categoryList.stream()

                    .filter(c -> c.getName()
                            .toLowerCase()
                            .contains(searchText))

                    .toList();

    tableCategories.setItems(

            FXCollections.observableArrayList(filteredList)
    );
}

    
    
    /**
     * Displays alert messages
     */
    private void showAlert(String title, String message) {

        Alert alert = new Alert(Alert.AlertType.ERROR);

        alert.setTitle(title);

        alert.setContentText(message);

        alert.getDialogPane().getStylesheets().add(
                getClass()
                        .getResource("/css/styleProject.css")
                        .toExternalForm()
        );

        alert.showAndWait();
    }

    
    
    /**
     * Saves newly added category into file
     */
    /*
    private void saveToFile(Category category) {

        try {

            FileWriter writer = new FileWriter(
                    "Document/categories.txt",
                    true
            );

            writer.write(
                    category.getId()
                            + ","
                            + category.getName()
                            + "\n"
            );

            writer.close();

        } catch (IOException e) {

            showAlert("Error", "Error saving data!");
        }
    }

    */
    
    /**
     * Handles adding new category
     */
   @FXML
    void HandelButtonAdd(ActionEvent event) {

    String name = txtCategoryName.getText().trim();

    if (name.isEmpty()) {

        showAlert("Error",
                "Category name cannot be empty!");

        return;
    }

    try {

        Connection con = DBConnection.connect();

        String checkQuery =
                "SELECT * FROM Categories WHERE name=?";

        PreparedStatement checkPs =
                con.prepareStatement(checkQuery);

        checkPs.setString(1, name);

        ResultSet rs = checkPs.executeQuery();

        if (rs.next()) {

            showAlert("Error",
                    "Category already exists!");

            return;
        }

        String insertQuery =
                "INSERT INTO Categories(name, user_id)\n" +
                "VALUES(?, ?)";

        PreparedStatement insertPs =
                con.prepareStatement(insertQuery);

        insertPs.setString(1, name);
           
        insertPs.setInt(2,
        Session.currentUserId);
        
        insertPs.executeUpdate();

        txtCategoryName.clear();

        loadCategories();

        showAlert("Success",
                "Category added successfully!");

    } catch (Exception e) {

        e.printStackTrace();
    }
}

    
    
    /**
     * Handles deleting selected category
     */
  @FXML
void handleDeleteCategory(ActionEvent event) {

    Category selected =
            tableCategories
                    .getSelectionModel()
                    .getSelectedItem();

    if (selected == null) {

        showAlert("Error",
                "Please select a category!");

        return;
    }

    try {

        Connection con = DBConnection.connect();

        // حذف العمليات المرتبطة بالتصنيف أولاً
        String deleteTransactionsQuery =
                "DELETE FROM Transactions "
                + "WHERE category=?";

        PreparedStatement ps2 =
                con.prepareStatement(
                        deleteTransactionsQuery);

        ps2.setString(1,
                selected.getName());

        ps2.executeUpdate();

         loadTransactions();
        // حذف التصنيف نفسه
        String query =
                "DELETE FROM Categories WHERE id=?";

        PreparedStatement ps =
                con.prepareStatement(query);

        ps.setInt(1, selected.getId());

        ps.executeUpdate();

        loadCategories();

        showAlert("Success",
                "Category deleted successfully!");

    } catch (Exception e) {

        e.printStackTrace();
    }
}

    
    
    /**
     * Rewrites categories file after deletion or sorting
     */
  
  /*
    private void updateFile() {

        try {

            FileWriter writer =
                    new FileWriter("Document/categories.txt");

            
            // Rewrite all categories into file
            for (Category c : categoryList) {

                writer.write(
                        c.getId()
                                + ","
                                + c.getName()
                                + "\n"
                );
            }
            writer.close();

        } catch (IOException e) {

            showAlert("Error", "Error updating file!");
        }
    }
    */
    
    
    /**
     * Reads last ID from file
     * to continue counting correctly
     */
  /*
    private void setNextId() {

        try {

            File file = new File("Document/categories.txt");

            
            // If file does not exist stop method
            if (!file.exists()) {
                return;
            }

            BufferedReader reader =
                    new BufferedReader(new FileReader(file));

            String line;

            int lastId = 0;

            while ((line = reader.readLine()) != null) {

                if (line.trim().isEmpty()) {
                    continue;
                }

                String[] parts = line.split(",");

                if (parts.length < 2) {
                    continue;
                }

                lastId = Integer.parseInt(parts[0]);
            }

            
            // Next ID becomes last ID + 1
            idCounter = lastId + 1;

            reader.close();

        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    */
    
    /**
     * Sort categories alphabetically
     * then re-arrange IDs sequentially
     */
    private void sortAndReindex() {

        
        // Sort alphabetically by category name
        
    FXCollections.sort(

            categoryList,

            Comparator.comparing(
                    Category::getName
            )
    );

    tableCategories.refresh();
    }
}