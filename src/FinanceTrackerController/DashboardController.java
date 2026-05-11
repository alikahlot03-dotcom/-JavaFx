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
import javafx.scene.layout.VBox;
import javafx.geometry.Insets;


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

        dashboardBtn.setOnAction(event -> {
            try {
                // To go back to dashboard overview, we just reload the dashboard center
                // Since Dashboard.fxml is the parent, we can just set the center to the original welcome content
                // Or for simplicity, we can load a separate "Overview.fxml" if it existed.
                // Given the current structure, let's load Dashboard.fxml's initial state or just clear the center.
                // Actually, let's assume "Dashboard" refers to the initial view.
                mainPane.setCenter(createWelcomeView());
                setActive(dashboardBtn);
            } catch (Exception e) {
                e.printStackTrace();
            }
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

    private javafx.scene.Node createWelcomeView() {
        try {
            // Load the initial welcome state
            VBox welcomeBox = new VBox(20);
            welcomeBox.setAlignment(javafx.geometry.Pos.CENTER);
            welcomeBox.setPadding(new Insets(40));
            
            VBox card = new VBox(30);
            card.getStyleClass().add("card");
            card.setMaxWidth(600);
            card.setAlignment(javafx.geometry.Pos.CENTER);
            
            Label welcome = new Label("Welcome to your Dashboard!");
            welcome.getStyleClass().add("header-label");
            
            Label subtext = new Label("Track your income, manage your expenses, and achieve your financial goals with ease.");
            subtext.getStyleClass().add("secondary-label");
            subtext.setWrapText(true);
            subtext.setAlignment(javafx.geometry.Pos.CENTER);
            
            card.getChildren().addAll(welcome, subtext);
            welcomeBox.getChildren().add(card);
            return welcomeBox;
        } catch (Exception e) {
            return new Label("Welcome!");
        }
    }

    private void handleNavigation(String targetPage) {
        try {
            System.out.println("Switching to: " + targetPage);
            // Corrected path: removed /fxml/ as files are in /view/
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/" + targetPage + ".fxml"));
            Parent root = loader.load();
            mainPane.setCenter(root);
        } catch (IOException e) {
            System.err.println("Failed to load: " + targetPage + " at /view/" + targetPage + ".fxml");
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
        Parent root = FXMLLoader.load(getClass().getResource("/view/Login.fxml"));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        try {
            scene.getStylesheets().add(getClass().getResource("/css/styleProject.css").toExternalForm());
        } catch (Exception e) {
            System.err.println("Could not load CSS during logout");
        }
        stage.setScene(scene);
        stage.setTitle("Finance Tracker - Login");
        stage.show();
    }
}