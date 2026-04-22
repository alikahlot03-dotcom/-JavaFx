import java.net.URL;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Main application entry point.
 * Starts with the Login page.
 */
public class MainApp extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        // Start with Login page
        URL fxmlLocation = getClass().getResource("/view/fxml/Login.fxml");
        
        if (fxmlLocation != null) {
            Parent root = FXMLLoader.load(fxmlLocation);
            Scene scene = new Scene(root, 900, 700);
            
            // Link the premium stylesheet
            URL cssLocation = getClass().getResource("/css/styleProject.css");
            if (cssLocation != null) {
                scene.getStylesheets().add(cssLocation.toExternalForm());
            } else {
                System.err.println("Warning: styleProject.css not found!");
            }
            
            primaryStage.setTitle("Finance Tracker - Login");
            primaryStage.setResizable(false);
            primaryStage.setScene(scene);
            primaryStage.show();
        } else {
            // Fallback screen if FXML is missing
            javafx.scene.layout.VBox root = new javafx.scene.layout.VBox();
            root.setAlignment(javafx.geometry.Pos.CENTER);
            root.getChildren().add(new javafx.scene.control.Label("Critical Error: Login.fxml not found at /view/fxml/Login.fxml"));
            primaryStage.setTitle("Finance Tracker - Error");
            primaryStage.setScene(new Scene(root, 400, 300));
            primaryStage.show();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
