/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package FinanceTrackerController;
import Utils.Session;
import Utils.psswordHash;

import java.io.IOException;

import java.net.URL;

import java.security.NoSuchAlgorithmException;

import java.util.ResourceBundle;

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

import Utils.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

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

        loginButton.setOnAction(event -> {

            try {

                // استدعاء ميثود المعالجة اللي أنت عاملها أصلاً
                EventHandler(event);

            } catch (Exception e) {

                e.printStackTrace();
            }
        });

        createAccount.setOnMouseClicked(event -> {

            try {

                Parent root =
                        FXMLLoader.load(
                                getClass()
                                        .getResource("/view/SignUp.fxml"));

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
    private void EventHandler(ActionEvent event)
            throws NoSuchAlgorithmException, IOException {

        if (textFieldEmail.getText().isEmpty()
                || textFieldPassword.getText().isEmpty()) {

            Alert alert =
                    new Alert(Alert.AlertType.ERROR);

            alert.setTitle("Fieled");

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

        String email =
                textFieldEmail.getText().trim();

        String password =
                textFieldPassword.getText().trim();

        String hashPassword =
                psswordHash.hashPassword(
                        textFieldPassword.getText());

        boolean found = false;

        try {

            Connection con =
                    DBConnection.connect();

            String query =
                    "SELECT * FROM Users "
                    + "WHERE email=? "
                    + "AND password_hash=?";

            PreparedStatement ps =
                    con.prepareStatement(query);

            ps.setString(1, email);

            ps.setString(2, hashPassword);

            ResultSet rs =
                    ps.executeQuery();

           if (rs.next()) {

              found = true;

              Session.currentUserId =
            rs.getInt("id");
}

        } catch (Exception e) {

            e.printStackTrace();
        }

        if (found) {

            Alert success =
                    new Alert(Alert.AlertType.INFORMATION);

            success.setContentText(
                    "Login Successful! Welcome.");

            success.getDialogPane()
                    .getStyleClass()
                    .add("alert-success");

            success.getDialogPane()
                    .getStylesheets()
                    .add(getClass()
                            .getResource("/css/styleProject.css")
                            .toExternalForm());

            // --- هين بنستخدم اللامبدا ---
            success.showAndWait().ifPresent(response -> {

                if (response
                        == javafx.scene.control.ButtonType.OK) {

                    try {

                        // كود الانتقال للداشبورد باستخدام FXMLLoader
                        Parent root =
                                FXMLLoader.load(
                                        getClass()
                                                .getResource("/view/Dashboard.fxml"));

                        Stage stage =
                                (Stage) ((Node)
                                        event.getSource())
                                        .getScene()
                                        .getWindow();

                        stage.setScene(
                                new Scene(root));

                        stage.setTitle(
                                "Finance Tracker - Dashboard");

                        stage.show();

                    } catch (IOException e) {

                        e.printStackTrace();
                    }
                }
            });
            // ----------------------------------------------
        }

        else {

            Alert errorAlert =
                    new Alert(Alert.AlertType.ERROR);

            errorAlert.setTitle("Login Failed");

            errorAlert.setContentText(
                    "Invalid email or password!");

            errorAlert.getDialogPane()
                    .getStylesheets()
                    .add(getClass()
                            .getResource("/css/styleProject.css")
                            .toExternalForm());

            errorAlert.show();
        }
    }

    @FXML
    private void ClickCreateAccount(MouseEvent event)
            throws IOException {

        Parent root =
                FXMLLoader.load(
                        getClass()
                                .getResource("/view/SignUp.fxml"));

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