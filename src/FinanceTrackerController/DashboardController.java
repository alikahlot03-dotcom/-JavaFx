
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
import java.util.HashMap;
import java.util.Map;

public class DashboardController {

    @FXML private BorderPane mainPane;
    @FXML private Label welcomeLabel;
    @FXML private Button dashboardBtn;
    @FXML private Button categoriesBtn;
    @FXML private Button transactionsBtn;
    @FXML private Button reportsBtn;
    @FXML private Button logoutBtn;

    // 🔥 Cache للصفحات
    private final Map<String, Parent> views = new HashMap<>();

    @FXML
    public void initialize() {

        // رسالة ترحيب
        if (welcomeLabel != null) {
            welcomeLabel.setText("Welcome Back, User!");
        }

        // ==========================================
        // إعادة هيكلة لوحة التحكم (Phase 2 Refactoring)
        // ==========================================
        // بدلاً من تكرار كود .setOnAction() لكل زر بشكل منفصل،
        // استخدمنا هيكل بيانات Map لربط كل زر باسم الصفحة الخاصة به.
        // ثم استخدمنا تعبيرات لامبدا (Lambda Expressions) عبر دالة .forEach()
        // لإنشاء Event Handlers بشكل ديناميكي ونظيف. هذا يقلل من تكرار الكود
        // ويجعل عملية إضافة أزرار جديدة مستقبلاً أسهل بكثير.
        Map<Button, String> navMap = Map.of(
            dashboardBtn, "Overview",
            categoriesBtn, "Categories",
            transactionsBtn, "Transactions",
            reportsBtn, "Reports"
        );

        navMap.forEach((btn, page) -> btn.setOnAction(e -> {
            loadPage(page);
            setActive(btn);
        }));

        logoutBtn.setOnAction(e -> {
            try {
                handleLogout(e);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });
    }

    //  تحميل الصفحات مع Cache
    private void loadPage(String page) {
        try {

            // إذا الصفحة غير مخزنة
            if (!views.containsKey(page)) {
                FXMLLoader loader = new FXMLLoader(
                        getClass().getResource("/view/" + page + ".fxml")
                );
                Parent root = loader.load();
                views.put(page, root);
            }

            // عرض الصفحة
            mainPane.setCenter(views.get(page));

        } catch (Exception e) {
            System.err.println("❌ Error loading page: " + page);
            e.printStackTrace();
        }
    }

    // 🔥 تفعيل زر القائمة
    private void setActive(Button activeBtn) {

        dashboardBtn.getStyleClass().remove("active-nav");
        categoriesBtn.getStyleClass().remove("active-nav");
        transactionsBtn.getStyleClass().remove("active-nav");
        reportsBtn.getStyleClass().remove("active-nav");

        activeBtn.getStyleClass().add("active-nav");
    }

    // 🔥 Logout
    private void handleLogout(javafx.event.ActionEvent event) throws IOException {

        Parent root = FXMLLoader.load(getClass().getResource("/view/Login.fxml"));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

        Scene scene = new Scene(root);

        try {
            scene.getStylesheets().add(
                    getClass().getResource("/css/styleProject.css").toExternalForm()
            );
        } catch (Exception e) {
            System.out.println("CSS not loaded");
        }

        stage.setScene(scene);
        stage.setTitle("Finance Tracker - Login");
        stage.show();
    }
}