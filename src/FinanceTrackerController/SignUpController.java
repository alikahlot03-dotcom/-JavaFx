package FinanceTrackerController;

import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import Utils.psswordHash;
import java.security.NoSuchAlgorithmException;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.File;

/**
 * FXML Controller class for Sign Up.
 */
public class SignUpController implements Initializable {

    @FXML
    private Button btnSignUp;
    @FXML
    private TextField textFieldFirstName;
    @FXML
    private TextField textFieldLastName;
    @FXML
    private TextField textFieldEmail;
    @FXML
    private PasswordField textFieldPassword;
    @FXML
    private PasswordField textFeildConfirmPassword;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Initialization logic if needed
    }    

    @FXML
    private void SignupHandler(ActionEvent event) throws NoSuchAlgorithmException {  

        if (textFieldFirstName.getText().isEmpty() || textFieldLastName.getText().isEmpty() || 
            textFieldEmail.getText().isEmpty() || textFieldPassword.getText().isEmpty() || 
            textFeildConfirmPassword.getText().isEmpty()) {
            
            showAlert(Alert.AlertType.ERROR, "Field Error", "Please fill all fields!");
            return;
        }

        if (!textFieldPassword.getText().equals(textFeildConfirmPassword.getText())) {
            showAlert(Alert.AlertType.ERROR, "Password Error", "Passwords do not match!");
            return;
        }

        try {
            String hashedPassword = psswordHash.hashPassword(textFieldPassword.getText());

            // Ensure Document directory exists
            File docDir = new File("Document");
            if (!docDir.exists()) {
                docDir.mkdirs();
            }

            try (FileWriter writer = new FileWriter("Document/User.txt", true)) {
                String id = String.valueOf(System.currentTimeMillis()).substring(7);
                String userRow = id + "," + textFieldFirstName.getText() + "," + 
                                 textFieldLastName.getText() + "," + textFieldEmail.getText() + "," + 
                                 hashedPassword + "\n";

                writer.write(userRow);

                showAlert(Alert.AlertType.INFORMATION, "Success", "Account created successfully!");
                
                // Return to Login page
                loadPage(event, "/view/fxml/Login.fxml");
            }
        } catch (IOException e) {
            System.err.println("Error saving to file: " + e.getMessage());
        }
    }

    private void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        try {
            alert.getDialogPane().getStylesheets().add(getClass().getResource("/css/styleProject.css").toExternalForm());
        } catch (Exception e) {
            System.err.println("Could not load CSS for alert");
        }
        alert.showAndWait();
    }

    private void loadPage(ActionEvent event, String fxmlPath) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource(fxmlPath));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        try {
            scene.getStylesheets().add(getClass().getResource("/css/styleProject.css").toExternalForm());
        } catch (Exception e) {
            System.err.println("Could not load CSS for scene: " + fxmlPath);
        }
        stage.setScene(scene);
        stage.show();
    }
}
