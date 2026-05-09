/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package FinanceTrackerController;
import java.io.*;
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
import javafx.scene.control.SortEvent;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import model.Category;


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
    @FXML
    private TextField txtSearch;
    
    private ObservableList<Category> categoryList = FXCollections.observableArrayList();
    private int idCounter = 1;

    /**
     * Initializes the controller class.
     */
    @Override
    
 
    public void initialize(URL url, ResourceBundle rb) {
       colId.setCellValueFactory(new PropertyValueFactory<>("id"));
       colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        loadCategories();
       tableCategories.setItems(categoryList);
       txtSearch.textProperty().addListener((obs, oldVal, newVal) -> {
      searchCategory();
    });
    setNextId();
    tableCategories.setItems(categoryList);
    sortAndReindex();
    
    
    }  
    private void loadCategories() {
    categoryList.clear();

    try (BufferedReader reader = new BufferedReader(new FileReader("Document/categories.txt"))) {
        String line;

        while ((line = reader.readLine()) != null) {
            if (line.trim().isEmpty()) continue;

            String[] parts = line.split(",");
            categoryList.add(new Category(
                Integer.parseInt(parts[0]),
                parts[1]
            ));
        }
        categoryList.sort(
    Comparator.comparing(Category::getName)
   );

    } catch (Exception e) {
        e.printStackTrace();
    }
}
    
   private void searchCategory() {
    String searchText = txtSearch.getText().toLowerCase();

    if (searchText.isEmpty()) {
        tableCategories.setItems(categoryList);
        return;
    }

    List<Category> filtered = categoryList.stream()
        .filter(c -> c.getName().toLowerCase().contains(searchText))
        .toList();

    tableCategories.setItems(FXCollections.observableArrayList(filtered));
}
   
    private void showAlert(String title, String message) {
    Alert alert = new Alert(Alert.AlertType.ERROR);
    alert.setTitle(title);
    alert.setContentText(message);
    alert.getDialogPane().getStylesheets().add(
         getClass().getResource("/css/styleProject.css").toExternalForm());
    alert.showAndWait();
}
     void saveToFile(Category category) {
    try {
        FileWriter writer = new FileWriter("Document/categories.txt", true);
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
        showAlert("Error","Category name is required!");
        return;
    }

    for (Category c : categoryList) {
        if (c.getName().equalsIgnoreCase(name)) {
            showAlert("Error", "Category already exists!");
            return;
        }
    }

    Category category = new Category(idCounter++, name) {};
  
    categoryList.add(category);
    sortAndReindex();
   
     tableCategories.refresh();
     
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
     sortAndReindex();
     updateFile(); 
    }
    
    
     void updateFile() {
    try {
        FileWriter writer = new FileWriter("Document/categories.txt");

        for (Category c : categoryList) {
            writer.write(c.getId() + "," + c.getName() + "\n");
        }
     writer.close();
     } catch (IOException e) {
        showAlert("Error", "Error updating file!");
    }
}
     
     
     
     void setNextId() {
    try {
        File file = new File("Document/categories.txt");

        if (!file.exists())
            return;
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
private void sortAndReindex() {

    categoryList.sort(
        Comparator.comparing(Category::getName)
    );

    for (int i = 0; i < categoryList.size(); i++) {
        categoryList.get(i).setId(i + 1);
    }
    idCounter = categoryList.size() + 1;
    tableCategories.refresh();
    updateFile();
}
    
    
}