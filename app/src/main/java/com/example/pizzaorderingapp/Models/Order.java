package com.example.pizzaorderingapp.Model;

public class Order {
    private int id;
    private String userEmail;
    private String status;
    private String totalAmount;  // Changed from double to String
    private String date;
    private boolean completed;

    // Constructor
    public Order(int id, String userEmail, String status, String totalAmount, String date, boolean completed) {
        this.id = id;
        this.userEmail = userEmail;
        this.status = status;
        this.totalAmount = totalAmount;
        this.date = date;
        this.completed = completed;
    }

    // Getters and setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getUserEmail() { return userEmail; }
    public void setUserEmail(String userEmail) { this.userEmail = userEmail; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getTotalAmount() { return totalAmount; }
    public void setTotalAmount(String totalAmount) { this.totalAmount = totalAmount; }

    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }

    public boolean isCompleted() { return completed; }
    public void setCompleted(boolean completed) { this.completed = completed; }
}
