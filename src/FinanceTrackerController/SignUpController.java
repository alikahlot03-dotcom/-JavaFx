/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package FinanceTrackerController;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import Utils.psswordHash;
import java.security.NoSuchAlgorithmException;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ButtonType;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author hp
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
        LoginNow.setOnMouseClicked(event -> {
    try {
        Parent root = FXMLLoader.load(getClass().getResource("/view/Login.fxml"));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    } catch (IOException e) {
        e.printStackTrace();
    }
});
    }    

    @FXML
    private void SignupHandler(ActionEvent event) throws NoSuchAlgorithmException {  

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
           success.setContentText("Account created successfully!");
           success.getDialogPane().getStyleClass().add("alert-success");
//            success.getDialogPane().getStylesheets().add(getClass().getResource("/css/styleProject.css").toExternalForm());
//            success.showAndWait();
////            success.show();
//          Parent root = FXMLLoader.load(getClass().getResource("/view/Login.fxml")); 
//    
//    // 2. الحصول على النافذة الحالية (Stage)
//       Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
//    
//    // 3. تعيين المشهد الجديد
//    Scene scene = new Scene(root);
//    stage.setScene(scene);
//    stage.show();
success.showAndWait().ifPresent(response -> {
    if (response == ButtonType.OK) {
        try {
            // الانتقال إلى صفحة اللوقن باستخدام Lambda
            Parent root = FXMLLoader.load(getClass().getResource("/view/Login.fxml"));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
});
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

   
    
    
    
    
    
    

      
      
      
      
    


    
    