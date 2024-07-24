package com.example.pizzaorderingapp.Model;

public class Store {
    private String name;
    private double latitude;
    private double longitude;

    public Store(String name, double latitude, double longitude) {
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getName() {
        return name;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }
}
