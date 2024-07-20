package com.example.pizzaorderingapp.Model;

public class MenuItem {

    private int id;
    private String name;
    private String description;
    private double price;
    private String category;
    private String toppings;
    private String imageUri;

    // No-argument constructor
    public MenuItem() {
    }

    public MenuItem(int id, String name, String description, double price, String category, String toppings, String imageUri) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.category = category;
        this.toppings = toppings;
        this.imageUri = imageUri;
    }

    // Getters and setters

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getToppings() {
        return toppings;
    }

    public void setToppings(String toppings) {
        this.toppings = toppings;
    }

    public String getImageUri() {
        return imageUri;
    }

    public void setImageUri(String imageUri) {
        this.imageUri = imageUri;
    }

    @Override
    public String toString() {
        return "MenuItem{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", price=" + price +
                ", category='" + category + '\'' +
                ", toppings='" + toppings + '\'' +
                ", imageUri='" + imageUri + '\'' +
                '}';
    }
}
