<<<<<<< HEAD
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package FinanceTrackerController;

import java.io.BufferedWriter;
=======
package FinanceTrackerController;

>>>>>>> e98cf67381ea005d1e0a268478373fdda18961bd
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
<<<<<<< HEAD
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
=======
import javafx.scene.control.Button;
>>>>>>> e98cf67381ea005d1e0a268478373fdda18961bd
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import Utils.psswordHash;
import java.security.NoSuchAlgorithmException;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
<<<<<<< HEAD
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author hp
=======
import javafx.stage.Stage;
import java.io.File;

/**
 * FXML Controller class for Sign Up.
>>>>>>> e98cf67381ea005d1e0a268478373fdda18961bd
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
<<<<<<< HEAD
    @FXML
    private Label labelHintFirstName;
    @FXML
    private Label labelHintLastName;
    @FXML
    private Label labelHintEmail;
    @FXML
    private Label labelHintPassword;
    @FXML
    private Label labelHIntConfirmPassword;
    @FXML
    private Label LoginNow;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
=======

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Initialization logic if needed
>>>>>>> e98cf67381ea005d1e0a268478373fdda18961bd
    }    

    @FXML
    private void SignupHandler(ActionEvent event) throws NoSuchAlgorithmException {  

<<<<<<< HEAD
if (textFieldFirstName.getText().isEmpty() || textFieldLastName.getText().isEmpty() || 
        textFieldEmail.getText().isEmpty() || textFieldPassword.getText().isEmpty() || 
        textFeildConfirmPassword.getText().isEmpty()) {
        
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Field Error");
        alert.setHeaderText(null);
        alert.setContentText("Please fill all fields!");
        alert.getDialogPane().getStylesheets().add(getClass().getResource("/css/styleProject.css").toExternalForm());
        alert.show();
        return;
    }

    // 2. التحقق من تطابق كلمات المرور
    if (!textFieldPassword.getText().equals(textFeildConfirmPassword.getText())) {
        Alert alert = new Alert(Alert.AlertType.ERROR); // إنشاء تنبيه جديد أو استخدام القديم
        alert.setTitle("Password Error");
        alert.setContentText("Passwords do not match!");
        alert.getDialogPane().getStylesheets().add(getClass().getResource("/css/styleProject.css").toExternalForm());
        alert.show();
        return;
    }

    //هين انا بكمل التشفير 
    try {
        // استدعاء دالة التشفير من الكلاس الخاص بك
        String hashedPassword = psswordHash.hashPassword(textFieldPassword.getText());

        //هين انا ممكن استخدم البفر لانها بسرع  وبتنظم البيانات ممكن استخدمها للبيانات الكبيرة يعني هيا زي عربة بتخزن البيانات بالممري ومجرد متضغط انت بتنقله على الملف 
        //BufferedWriter writer = new BufferedWriter(new FileWriter("Document/User.txt", true)
        try (
                FileWriter writer = new FileWriter("Document/User.txt", true)) {
            String id = String.valueOf(System.currentTimeMillis()).substring(7);
            String userRow = id + "," + textFieldFirstName.getText() + "," + 
                             textFieldLastName.getText() + "," + textFieldEmail.getText() + "," + 
                             hashedPassword+"\n";

            writer.write(userRow);
           
           // writer.newLine();

            // تنبيه بالنجاح (Success Alert)
            Alert success = new Alert(Alert.AlertType.INFORMATION);
            success.setContentText("Account created successfully!");
            success.getDialogPane().getStyleClass().add("alert-success");
            success.getDialogPane().getStylesheets().add(getClass().getResource("/css/styleProject.css").toExternalForm());
            success.showAndWait();
//            success.show();
          Parent root = FXMLLoader.load(getClass().getResource("/view/Login.fxml")); 
    
    // 2. الحصول على النافذة الحالية (Stage)
       Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
    
    // 3. تعيين المشهد الجديد
    Scene scene = new Scene(root);
    stage.setScene(scene);
    stage.show();
        }
    } catch (IOException e) {
        System.out.println("Error saving to file: " + e.getMessage());
    }

}

    @FXML
    private void onLoginClick(MouseEvent event) throws IOException {
        
        
         Parent root = FXMLLoader.load(getClass().getResource("/view/Login.fxml"));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        
        Scene scene = new Scene(root);
//         scene.getStylesheets().add(getClass().getResource("/FinanceTracker/styleProject.css").toExternalForm());
          scene.getStylesheets().add(getClass().getResource("/css/styleProject.css").toExternalForm());
            
      
        stage.setScene(scene);
        stage.show();
        
        
    }
      

      
      
      
   
   
}

   
    
    
    
    
    
    

      
      
      
      
    


    
    

=======
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
>>>>>>> e98cf67381ea005d1e0a268478373fdda18961bd
