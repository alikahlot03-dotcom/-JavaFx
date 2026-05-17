/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package FinanceTrackerController;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import Utils.psswordHash;

import java.security.NoSuchAlgorithmException;

import javafx.fxml.FXMLLoader;

import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;

import javafx.scene.input.MouseEvent;

import javafx.stage.Stage;

import Utils.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

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

                Parent root =
                        FXMLLoader.load(
                                getClass()
                                        .getResource("/view/Login.fxml"));

                Stage stage =
                        (Stage) ((Node)
                                event.getSource())
                                .getScene()
                                .getWindow();

                stage.setScene(new Scene(root));

                stage.show();

            } catch (IOException e) {

                e.printStackTrace();
            }
        });
    }

    @FXML
    private void SignupHandler(ActionEvent event)
            throws NoSuchAlgorithmException {

        if (textFieldFirstName.getText().isEmpty()
                || textFieldLastName.getText().isEmpty()
                || textFieldEmail.getText().isEmpty()
                || textFieldPassword.getText().isEmpty()
                || textFeildConfirmPassword.getText().isEmpty()) {

            Alert alert =
                    new Alert(Alert.AlertType.ERROR);

            alert.setTitle("Field Error");

            alert.setHeaderText(null);

            alert.setContentText(
                    "Please fill all fields!");

            alert.getDialogPane()
                    .getStylesheets()
                    .add(getClass()
                            .getResource("/css/styleProject.css")
                            .toExternalForm());

            alert.show();

            return;
        }

        // 2. التحقق من تطابق كلمات المرور
        if (!textFieldPassword.getText()
                .equals(textFeildConfirmPassword.getText())) {

            Alert alert =
                    new Alert(Alert.AlertType.ERROR);

            alert.setTitle("Password Error");

            alert.setContentText(
                    "Passwords do not match!");

            alert.getDialogPane()
                    .getStylesheets()
                    .add(getClass()
                            .getResource("/css/styleProject.css")
                            .toExternalForm());

            alert.show();

            return;
        }

        //هين انا بكمل التشفير
        try {

            // استدعاء دالة التشفير من الكلاس الخاص بك
            String hashedPassword =
                    psswordHash.hashPassword(
                            textFieldPassword.getText());

            Connection con = DBConnection.connect();

            // التحقق إذا الإيميل موجود مسبقاً
            String checkQuery =
                    "SELECT * FROM Users WHERE email=?";

            PreparedStatement checkPs =
                    con.prepareStatement(checkQuery);

            checkPs.setString(1,
                    textFieldEmail.getText());

            ResultSet rs =
                    checkPs.executeQuery();

            if (rs.next()) {

                Alert alert =
                        new Alert(Alert.AlertType.ERROR);

                alert.setTitle("Email Error");

                alert.setHeaderText(null);

                alert.setContentText(
                        "Email already exists!");

                alert.show();

                return;
            }

            // حفظ المستخدم داخل قاعدة البيانات
            String query =
                    "INSERT INTO Users "
                    + "(first_name, last_name, email, password_hash) "
                    + "VALUES (?, ?, ?, ?)";

            PreparedStatement ps =
                    con.prepareStatement(query);

            ps.setString(1,
                    textFieldFirstName.getText());

            ps.setString(2,
                    textFieldLastName.getText());

            ps.setString(3,
                    textFieldEmail.getText());

            ps.setString(4,
                    hashedPassword);

            ps.executeUpdate();

            // تنبيه بالنجاح (Success Alert)
            Alert success =
                    new Alert(Alert.AlertType.INFORMATION);

            success.setTitle("Success");

            success.setHeaderText(null);

            success.setContentText(
                    "Account created successfully!");

            success.getDialogPane()
                    .getStyleClass()
                    .add("alert-success");

            success.getDialogPane()
                    .getStylesheets()
                    .add(getClass()
                            .getResource("/css/styleProject.css")
                            .toExternalForm());

            success.showAndWait().ifPresent(response -> {

                if (response == ButtonType.OK) {

                    try {

                        // الانتقال إلى صفحة اللوقن باستخدام Lambda
                        Parent root =
                                FXMLLoader.load(
                                        getClass()
                                                .getResource("/view/Login.fxml"));

                        Stage stage =
                                (Stage) ((Node)
                                        event.getSource())
                                        .getScene()
                                        .getWindow();

                        stage.setScene(
                                new Scene(root));

                        stage.show();

                    } catch (IOException e) {

                        e.printStackTrace();
                    }
                }
            });

        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    @FXML
    private void onLoginClick(MouseEvent event)
            throws IOException {

        Parent root =
                FXMLLoader.load(
                        getClass()
                                .getResource("/view/Login.fxml"));

        Stage stage =
                (Stage) ((Node)
                        event.getSource())
                        .getScene()
                        .getWindow();

        Scene scene = new Scene(root);

        scene.getStylesheets()
                .add(getClass()
                        .getResource("/css/styleProject.css")
                        .toExternalForm());

        stage.setScene(scene);

        stage.show();
    }
}