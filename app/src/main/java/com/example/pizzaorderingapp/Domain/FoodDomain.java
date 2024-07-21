package com.example.pizzaorderingapp.Domain;

import java.io.Serializable;
import java.util.List;

public class FoodDomain implements Serializable {
    private String title;
    private String pic;
    private String description;
    private double fee;
    private int numberInCart;
    private String star;
    private int calories;
    private int time;
    private List<String> toppings; // List to hold toppings
    private String selectedToppings; // String to hold selected toppings

    public FoodDomain(String title, String pic, String description, double fee, String star, int calories, int time, List<String> toppings) {
        this.title = title;
        this.pic = pic;
        this.description = description;
        this.fee = fee;
        this.star = star;
        this.calories = calories;
        this.time = time;
        this.toppings = toppings;
        this.numberInCart = 1; // Default value
        this.selectedToppings = ""; // Default empty string
    }

    // Getters and Setters

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getPic() { return pic; }
    public void setPic(String pic) { this.pic = pic; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public double getFee() { return fee; }
    public void setFee(double fee) { this.fee = fee; }

    public int getNumberInCart() { return numberInCart; }
    public void setNumberInCart(int numberInCart) { this.numberInCart = numberInCart; }

    public String getStar() { return star; }
    public void setStar(String star) { this.star = star; }

    public int getCalories() { return calories; }
    public void setCalories(int calories) { this.calories = calories; }

    public int getTime() { return time; }
    public void setTime(int time) { this.time = time; }

    public List<String> getToppings() { return toppings; }
    public void setToppings(List<String> toppings) { this.toppings = toppings; }

    public String getSelectedToppings() { return selectedToppings; }
    public void setSelectedToppings(String selectedToppings) { this.selectedToppings = selectedToppings; }

    @Override
    public String toString() {
        return "FoodDomain{" +
                "title='" + title + '\'' +
                ", pic='" + pic + '\'' +
                ", description='" + description + '\'' +
                ", fee=" + fee +
                ", numberInCart=" + numberInCart +
                ", star='" + star + '\'' +
                ", calories=" + calories +
                ", time=" + time +
                ", toppings=" + toppings +
                ", selectedToppings='" + selectedToppings + '\'' +
                '}';
    }
}
