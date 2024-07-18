package com.example.pizzaorderingapp.Models;

public class Order {
    private String userEmail;
    private String orderDetails;
    private String totalAmount;
    private String orderDate;

    public Order(String userEmail, String orderDetails, String totalAmount, String orderDate) {
        this.userEmail = userEmail;
        this.orderDetails = orderDetails;
        this.totalAmount = totalAmount;
        this.orderDate = orderDate;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public String getOrderDetails() {
        return orderDetails;
    }

    public String getTotalAmount() {
        return totalAmount;
    }

    public String getOrderDate() {
        return orderDate;
    }
}
