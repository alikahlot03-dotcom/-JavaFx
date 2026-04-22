package FinanceTrackerController;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.Parent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.Node;
import java.io.IOException;

/**
 * Combined Controller for the Dashboard.
 * Handles navigation and layout management.
 */
public class DashboardController {

    @FXML private BorderPane mainPane;
    @FXML private Label welcomeLabel;
    @FXML private Button dashboardBtn;
    @FXML private Button categoriesBtn;
    @FXML private Button transactionsBtn;
    @FXML private Button reportsBtn;
    @FXML private Button logoutBtn;

    @FXML
    public void initialize() {
        // Default Welcome Message
        if (welcomeLabel != null) {
            welcomeLabel.setText("Welcome Back, User!");
        }

        // Navigation Bindings
        dashboardBtn.setOnAction(event -> {
            handleNavigation("Overview");
            setActive(dashboardBtn);
        });
        
        categoriesBtn.setOnAction(event -> {
            handleNavigation("Categories");
            setActive(categoriesBtn);
        });
        
        transactionsBtn.setOnAction(event -> {
            handleNavigation("Transactions");
            setActive(transactionsBtn);
        });
        
        reportsBtn.setOnAction(event -> {
            handleNavigation("Reports");
            setActive(reportsBtn);
        });

        logoutBtn.setOnAction(event -> {
            try {
                handleLogout(event);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    private void handleNavigation(String targetPage) {
        try {
            System.out.println("Switching to: " + targetPage);
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/fxml/" + targetPage + ".fxml"));
            Parent root = loader.load();
            mainPane.setCenter(root);
        } catch (IOException e) {
            System.err.println("Failed to load: " + targetPage);
            e.printStackTrace();
        }
    }

    private void setActive(Button activeBtn) {
        // Reset all buttons
        dashboardBtn.getStyleClass().remove("active-nav");
        categoriesBtn.getStyleClass().remove("active-nav");
        transactionsBtn.getStyleClass().remove("active-nav");
        reportsBtn.getStyleClass().remove("active-nav");
        
        // Mark active
        activeBtn.getStyleClass().add("active-nav");
    }

    private void handleLogout(javafx.event.ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/view/fxml/Login.fxml"));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        try {
            scene.getStylesheets().add(getClass().getResource("/css/styleProject.css").toExternalForm());
        } catch (Exception e) {
            System.err.println("Could not load CSS during logout");
        }
        stage.setScene(scene);
        stage.titleProperty().set("Finance Tracker - Login");
        stage.show();
    }
}