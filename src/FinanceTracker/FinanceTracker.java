package FinanceTracker;


import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;





public class FinanceTracker extends Application{
    
    
    public static void main(String[] args) {
        launch(args);
    }
    
    @Override
    public void start(Stage stage) throws Exception {
        
        Parent root = FXMLLoader.load(getClass().getResource("/view/login.fxml"));
        
        Scene scene = new Scene(root);
            scene.getStylesheets().add("/css/styleProject.css");
         // scene.getStylesheets().add(getClass().getResource("/css/styleProject.css").toExternalForm());
 
        stage.setScene(scene);
        stage.setTitle("Login Screen");
        stage.show(); 
        
  
    
        
        
        
        
        
    }

}

    
    
    




