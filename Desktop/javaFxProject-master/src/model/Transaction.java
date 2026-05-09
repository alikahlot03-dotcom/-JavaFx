package model;

import java.time.LocalDate;

/**
 * كلاس Transaction لتمثيل عملية مالية - متطلبات المرحلة الأولى [259].
 */
public class Transaction {
    private int id;
    private String category;
    private double amount;
    private String type; // Income or Expense
    private LocalDate date;

    public Transaction(int id, String category, double amount, String type, LocalDate date) {
        this.id = id;
        this.category = category;
        this.amount = amount;
        this.type = type;
        this.date = date;
    }

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public double getAmount() { return amount; }
    public void setAmount(double amount) { this.amount = amount; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }

    @Override
    public String toString() {
        return id + "," + category + "," + amount + "," + type + "," + date;
    }
}
