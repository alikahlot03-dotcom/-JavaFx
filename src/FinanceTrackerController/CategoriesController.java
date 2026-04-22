/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package FinanceTrackerController;
import FinanceTrackerController.Category;
import FinanceTrackerController.Category;
import java.io.*;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.SortEvent;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;


/**
 * FXML Controller class
 *
 * @author msi
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
    private Button btnAdd;
    @FXML
    private Button btnDelete;
    @FXML
    private TableView<Category> tableCategories;
    @FXML
    private TableColumn<Category, Integer> colId;
    @FXML
    private TableColumn<Category, Integer> colName;
    
    private ObservableList<Category> categoryList = FXCollections.observableArrayList();
    private int idCounter = 1;

    /**
     * Initializes the controller class.
     */
    @Override
    
 
    public void initialize(URL url, ResourceBundle rb) {
       colId.setCellValueFactory(new PropertyValueFactory<>("id"));
    colName.setCellValueFactory(new PropertyValueFactory<>("name"));
    tableCategories.setItems(categoryList);
    setNextId();
    
    
    }  
   
    private void showAlert(String title, String message) {
    Alert alert = new Alert(Alert.AlertType.ERROR);
    alert.setTitle(title);
    alert.setContentText(message);
    alert.showAndWait();
}
    private void saveToFile(Category category) {
    try {
        FileWriter writer = new FileWriter("src/database/categories.txt" , true);
        writer.write(category.getId() + "," + category.getName() + "\n");
        writer.close();
    } catch (IOException e) {
        showAlert("Error", "Error saving data!");
    }
}

    @FXML
    
    private void HandelButtonAdd(ActionEvent event) {
        String name = txtCategoryName.getText();

    if (name.isEmpty()) {
        showAlert("Error", "Category name is required!");
        return;
    }

    for (Category c : categoryList) {
        if (c.getName().equalsIgnoreCase(name)) {
            showAlert("Error", "Category already exists!");
            return;
        }
    }

    Category category = new Category(idCounter++, name);
    categoryList.add(category);

    saveToFile(category); 

    txtCategoryName.clear();
    }
       @FXML
    void handleDeleteCategory(ActionEvent event) {
 Category selected = tableCategories.getSelectionModel().getSelectedItem();

    if (selected == null) {
        showAlert("Error", "Please select a category to delete!");
        return;
    }

    categoryList.remove(selected);

    updateFile(); 
    }
    
    private void updateFile() {
    try {
        FileWriter writer = new FileWriter("src/database/categories.txt");

        for (Category c : categoryList) {
            writer.write(c.getId() + "," + c.getName() + "\n");
        }

        writer.close();

    } catch (IOException e) {
        showAlert("Error", "Error updating file!");
    }
}
    private void setNextId() {
    try {
        File file = new File("src/financtracker/categories.txt");

        if (!file.exists()) return;

        BufferedReader reader = new BufferedReader(new FileReader(file));
        String line;
        int lastId = 0;

        while ((line = reader.readLine()) != null) {
             if (line.trim().isEmpty()) continue; 
            String[] parts = line.split(",");
             if (parts.length < 2) continue; 
            lastId = Integer.parseInt(parts[0]);
            
        }

        idCounter = lastId + 1; 

        reader.close();

    } catch (Exception e) {
        e.printStackTrace();
    }
}

    
    
}