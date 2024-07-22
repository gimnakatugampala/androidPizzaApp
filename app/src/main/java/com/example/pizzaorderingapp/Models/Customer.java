package com.example.pizzaorderingapp.Model;

public class Customer {
    private int id;
    private String email;
    private String name;
    private String phone;
    private String address;
    private String imageUrl;

    // Constructor with id
    public Customer(int id, String email, String name, String phone, String address, String imageUrl) {
        this.id = id;
        this.email = email;
        this.name = name;
        this.phone = phone;
        this.address = address;
        this.imageUrl = imageUrl;
    }

    // Constructor without id
    public Customer(String email, String name, String phone, String address, String imageUrl) {
        this.email = email;
        this.name = name;
        this.phone = phone;
        this.address = address;
        this.imageUrl = imageUrl;
    }

    public int getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }

    public String getPhone() {
        return phone;
    }

    public String getAddress() {
        return address;
    }

    public String getImageUrl() {
        return imageUrl;
    }
}
