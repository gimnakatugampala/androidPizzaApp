package com.example.pizzaorderingapp.Model;

public class Order {
    private int id;
    private String userEmail;
    private String status;
    private String totalAmount;
    private String date;
    private boolean completed;

    public Order(int id, String userEmail, String status, String totalAmount, String date, boolean completed) {
        this.id = id;
        this.userEmail = userEmail;
        this.status = status;
        this.totalAmount = totalAmount;
        this.date = date;
        this.completed = completed;
    }

    public int getId() {
        return id;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public String getStatus() {
        return status;
    }

    public String getTotalAmount() {
        return totalAmount;
    }

    public String getDate() {
        return date;
    }

    public boolean isCompleted() {
        return completed;
    }
}
