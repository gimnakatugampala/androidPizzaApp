package com.example.pizzaorderingapp;

public class User {
    private String email;
    private String firstName;
    private String lastName;
    private String deliveryAddress;
    private String phone;
    private String imageUri;

    public User(String email, String firstName, String lastName, String deliveryAddress, String phone, String imageUri) {
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.deliveryAddress = deliveryAddress;
        this.phone = phone;
        this.imageUri = imageUri;
    }

    public String getEmail() {
        return email;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getDeliveryAddress() {
        return deliveryAddress;
    }

    public String getPhone() {
        return phone;
    }

    public String getImageUri() {
        return imageUri;
    }
}
