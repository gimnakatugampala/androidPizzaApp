package com.example.pizzaorderingapp.Model;

public class OrderItem {
    private String menuItemName;
    private int quantity;
    private double price;

    public OrderItem(String menuItemName, int quantity, double price) {
        this.menuItemName = menuItemName;
        this.quantity = quantity;
        this.price = price;
    }

    public String getMenuItemName() {
        return menuItemName;
    }

    public int getQuantity() {
        return quantity;
    }

    public double getPrice() {
        return price;
    }
}
