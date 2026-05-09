/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package FinanceTrackerController;

import Utils.psswordHash;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.security.NoSuchAlgorithmException;
import java.util.ResourceBundle;
import java.util.Scanner;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author hp
 */
public class LoginController implements Initializable {

    @FXML
    private Button loginButton;
    @FXML
    private TextField textFieldEmail;
    @FXML 
    private PasswordField textFieldPassword;
    @FXML
    private Label createAccount;
   

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    

    @FXML
    private void EventHandler(ActionEvent event) throws NoSuchAlgorithmException, IOException {
   if (textFieldEmail.getText().isEmpty() || textFieldPassword.getText().isEmpty()) {
     Alert alert = new Alert(Alert.AlertType.ERROR);
         alert.setTitle("Fieled");
         alert.setHeaderText(null);
         alert.setContentText("Please fill all fields!"); 
         alert.getDialogPane().getStylesheets().add(
         getClass().getResource("/css/styleProject.css").toExternalForm());
         alert.show();
    return;
}
   
   String email = textFieldEmail.getText().trim();
   String password = textFieldPassword.getText().trim();
   String hashPassword = psswordHash.hashPassword(textFieldPassword.getText());
   
   boolean found = false;
    try (
            Scanner scanner = new Scanner(new File("Document/User.txt")))
    {
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            String[] data = line.split(","); // تقسيم السطر بالفاصلة
            
           //هين تاكد ان الملف يختوي على خمس معلومات وتحت كاتب انه يقرا ويتحقق منالاندكس الرابع والخامس ويشوف هل موجودين في الملف النصي ام لا وهو نتاج عملية ساين اب  
           //
            if (data.length >= 5) {
                if (data[3].trim().equals(email) && data[4].trim().equals(hashPassword)) {
                    found = true;
                    break;
                }
            }
        }
    } catch (FileNotFoundException e) {
        System.out.println("File not found!");
    }

  
    if (found) {
        Alert success = new Alert(Alert.AlertType.INFORMATION);
        success.setContentText("Login Successful! Welcome.");
        success.getDialogPane().getStyleClass().add("alert-success");
        success.getDialogPane().getStylesheets().add(getClass().getResource("/css/styleProject.css").toExternalForm());
             success.showAndWait();
              
              Parent root = FXMLLoader.load(getClass().getResource("/view/Dashboard.fxml"));
               Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
               stage.setScene(new Scene(root));
              stage.setTitle("Finance Tracker - Dashboard");
              stage.show();
            
    } else {
        Alert errorAlert = new Alert(Alert.AlertType.ERROR);
        errorAlert.setTitle("Login Failed");
        errorAlert.setContentText("Invalid email or password!");
        errorAlert.getDialogPane().getStylesheets().add(getClass().getResource("/css/styleProject.css").toExternalForm());
        errorAlert.show();
            
    }
            }
    

    @FXML
    private void ClickCreateAccount(MouseEvent event) throws IOException {
        
        Parent root = FXMLLoader.load(getClass().getResource("/view/SignUp.fxml"));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        
        Scene scene = new Scene(root);
//         scene.getStylesheets().add(getClass().getResource("/FinanceTracker/styleProject.css").toExternalForm());
          scene.getStylesheets().add(getClass().getResource("/css/styleProject.css").toExternalForm());
            
      
        stage.setScene(scene);
        stage.show();
        
    }
    
    
    
}
            

        
        
        
    
    
    

   

    

    

