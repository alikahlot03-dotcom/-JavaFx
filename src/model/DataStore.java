package model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;   
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalDate;
import java.util.stream.Stream;

/**
 *تتولى DataStore مسؤولية إدارة البيانات المركزية في الذاكرة.
 */
public class DataStore {
    
// تخزين مركزي في الذاكرة للمعاملات
    public static final ObservableList<Transaction> transactions = FXCollections.observableArrayList();
    
    private static final String TRANSACTIONS_FILE = "Document/transactions.txt";

    /**
* يقوم بتحميل المعاملات من ملف النص إلى قائمة ObservableList المركزية باستخدام Java Streams.
     */
    public static void loadTransactions() {
        if (!Files.exists(Paths.get(TRANSACTIONS_FILE))) return;

        try (Stream<String> lines = Files.lines(Paths.get(TRANSACTIONS_FILE))) {
            transactions.clear();
            lines.filter(line -> !line.trim().isEmpty())
                 .forEach(line -> {
                     Transaction t = parseLine(line);
                     if (t != null) {
                         transactions.add(t);
                     }
                 });
        } catch (IOException e) {
            System.err.println("Error loading transactions: " + e.getMessage());
        }
    }

    /**
* يقوم بتحليل السطر ديناميكيًا، ويدعم كلاً من 5 و 6 حقول.     */
    private static Transaction parseLine(String line) {
        try {
            String[] parts = line.split(",");
            if (parts.length >= 5) {
                int id = Integer.parseInt(parts[0]);
                
                String dateStr = parts[parts.length - 1];
                String type = parts[parts.length - 2];
                String amountStr = parts[parts.length - 3];
                
                StringBuilder categoryBuilder = new StringBuilder();
                for (int i = 1; i < parts.length - 3; i++) {
                    if (i > 1) categoryBuilder.append(",");
                    categoryBuilder.append(parts[i]);
                }
                String category = categoryBuilder.toString();

                return new Transaction(id, category, Double.parseDouble(amountStr), type, LocalDate.parse(dateStr));
            }
        } catch (Exception e) {
            System.err.println("Failed to parse line: " + line);
        }
        return null;
    }

    /**
* يضيف معاملة إلى الذاكرة ويلحقها بالملف.     */
    public static void addTransaction(Transaction t) throws IOException {
// تحديث واجهة المستخدم فوراً       
 transactions.add(t);
        
        // حفظ دائم
        String data = t.toString() + System.lineSeparator();
        Files.write(Paths.get(TRANSACTIONS_FILE), data.getBytes(), StandardOpenOption.CREATE, StandardOpenOption.APPEND);
    }
}