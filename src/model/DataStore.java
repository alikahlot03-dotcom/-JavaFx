package model;

import Utils.DBConnection;
import Utils.DBConnection;
import Utils.Session;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.sql.*;
import java.time.LocalDate;

/**
 * [Model Layer: DataStore]
 * يعمل هذا الكلاس كـ Repository أو Data Access Object (DAO).
 * يتحكم في تدفق البيانات بين التطبيق وقاعدة بيانات MySQL.
 * 
 * المبادئ المستخدمة:
 * 1. JDBC (Java Database Connectivity): للاتصال بقاعدة البيانات.
 * 2. PreparedStatement: للحماية من SQL Injection (Security Best Practice).
 * 3. ObservableList: لدعم الـ Observer Pattern وتحديث الواجهة تلقائياً.
 */
public class DataStore {

    // القائمة المركزية التي ترتبط بها الـ TableView في الـ UI
    public static final ObservableList<Transaction> transactions = FXCollections.observableArrayList();

    /**
     * تحميل جميع المعاملات من قاعدة البيانات.
     * تستخدم SELECT لجلب البيانات وتحويلها إلى كائنات Java (Mapping).
     */
    public static void loadTransactions() {

    String query =
            "SELECT * FROM transactions "
            + "WHERE user_id=?";

    transactions.clear();

    try (Connection conn = DBConnection.connect();

         PreparedStatement stmt =
                 conn.prepareStatement(query)) {

        stmt.setInt(1,
                Session.currentUserId);

        ResultSet rs =
                stmt.executeQuery();

        while (rs.next()) {

            Transaction t =
                    new Transaction(

                            rs.getInt("id"),

                            rs.getString("category"),

                            rs.getDouble("amount"),

                            rs.getString("type"),

                            rs.getDate("date")
                                    .toLocalDate()
                    );

            transactions.add(t);
        }

    } catch (SQLException e) {

        System.err.println(
                "Database load error: "
                + e.getMessage());
    }
}

    /**
     * إضافة معاملة جديدة إلى قاعدة البيانات.
     * يستخدم PreparedStatement لضمان الأمان.
     */
    public static boolean addTransaction(Transaction t) {
        String sql = "INSERT INTO Transactions\n" +
                  "(category, amount, type, date, user_id)\n" +
                     "VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DBConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setString(1, t.getCategory());
            pstmt.setDouble(2, t.getAmount());
            pstmt.setString(3, t.getType());
            pstmt.setDate(4, Date.valueOf(t.getDate()));
            pstmt.setInt(5,
        Session.currentUserId);

            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                // جلب الـ ID الذي تم توليده تلقائياً
                try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        t.setId(generatedKeys.getInt(1));
                    }
                }
                transactions.add(t); // تحديث القائمة في الذاكرة
                return true;
            }
        } catch (SQLException e) {
            System.err.println("Database insert error: " + e.getMessage());
        }
        return false;
    }

    /**
     * تحديث معاملة موجودة مسبقاً.
     */
    public static boolean updateTransaction(Transaction t) {
        String sql = "UPDATE transactions SET category = ?, amount = ?, type = ?, date = ? WHERE id = ?";

        try (Connection conn = DBConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, t.getCategory());
            pstmt.setDouble(2, t.getAmount());
            pstmt.setString(3, t.getType());
            pstmt.setDate(4, Date.valueOf(t.getDate()));
            pstmt.setInt(5, t.getId());

            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                // تحديث العنصر في القائمة (ObservableList) ليعكس التغيير في الواجهة
                loadTransactions(); // أبسط طريقة لضمان التزامن الكامل
                return true;
            }
        } catch (SQLException e) {
            System.err.println("Database update error: " + e.getMessage());
        }
        return false;
    }

    /**
     * حذف معاملة باستخدام المعرف الخاص بها.
     */
    public static boolean deleteTransaction(int id) {
        String sql = "DELETE FROM transactions WHERE id = ?";

        try (Connection conn = DBConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);

            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                // إزالة العنصر من القائمة المحلية
                transactions.removeIf(t -> t.getId() == id);
                return true;
            }
        } catch (SQLException e) {
            System.err.println("Database delete error: " + e.getMessage());
        }
        return false;
    }
}