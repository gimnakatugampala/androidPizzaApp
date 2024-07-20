package com.example.pizzaorderingapp.Domain;

import java.io.Serializable;

public class FoodDomain implements Serializable {

    private String title;
    private String pic;  // Image URI or URL
    private String description;
    private double fee;
    private int numberInCart;
    private int star;
    private int calories;
    private int time;

    // Constructor
    public FoodDomain(String title, String pic, String description, double fee, int star, int time, int calories) {
        this.title = title;
        this.pic = pic;
        this.description = description;
        this.fee = fee;
        this.numberInCart = 1; // Default number in cart
        this.star = star;
        this.time = time;
        this.calories = calories;
    }

    // Getters and Setters
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getFee() {
        return fee;
    }

    public void setFee(double fee) {
        this.fee = fee;
    }

    public int getNumberInCart() {
        return numberInCart;
    }

    public void setNumberInCart(int numberInCart) {
        this.numberInCart = numberInCart;
    }

    public int getStar() {
        return star;
    }

    public void setStar(int star) {
        this.star = star;
    }

    public int getCalories() {
        return calories;
    }

    public void setCalories(int calories) {
        this.calories = calories;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }
}
