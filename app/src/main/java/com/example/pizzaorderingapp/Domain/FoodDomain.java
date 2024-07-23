package com.example.pizzaorderingapp.Domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class FoodDomain implements Serializable {

    private int id; // ID of the food item
    private String title;
    private String pic;
    private String description;
    private double fee; // Base price of the food item
    private int numberInCart;
    private String star;
    private int calories;
    private int time;
    private List<String> toppings; // List to hold available toppings
    private String selectedToppings; // String to hold selected toppings

    private static final double TOPPING_PRICE = 1.0; // Additional price for each topping

    // Constructor with selectedToppings
    public FoodDomain(int id, String title, String pic, String description, double fee, String star, int calories, int time, List<String> toppings, String selectedToppings) {
        this.id = id;
        this.title = title;
        this.pic = pic;
        this.description = description;
        this.fee = fee;
        this.star = star;
        this.calories = calories;
        this.time = time;
        this.toppings = toppings != null ? new ArrayList<>(toppings) : new ArrayList<>();
        this.selectedToppings = selectedToppings;
        this.numberInCart = 1; // Default value
    }

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

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

    public List<String> getToppings() { return new ArrayList<>(toppings); } // Return a copy for immutability
    public void setToppings(List<String> toppings) { this.toppings = toppings != null ? new ArrayList<>(toppings) : new ArrayList<>(); }

    public String getSelectedToppings() { return selectedToppings; }
    public void setSelectedToppings(String selectedToppings) { this.selectedToppings = selectedToppings; }

    // Calculate total price for the item based on quantity and selected toppings
    public double getTotalPrice() {
        double totalPrice = fee;

        // Add price for each selected topping
        if (selectedToppings != null && !selectedToppings.isEmpty()) {
            String[] selectedToppingArray = selectedToppings.split(",");
            totalPrice += TOPPING_PRICE * selectedToppingArray.length;
        }

        return totalPrice * numberInCart;
    }

    // Utility method to add a selected topping
    public void addTopping(String topping) {
        if (toppings.contains(topping)) {
            String[] selectedToppingArray = selectedToppings != null ? selectedToppings.split(",") : new String[]{};
            List<String> selectedToppingList = new ArrayList<>(List.of(selectedToppingArray));
            if (!selectedToppingList.contains(topping)) {
                selectedToppingList.add(topping);
                selectedToppings = String.join(",", selectedToppingList);
            }
        }
    }

    @Override
    public String toString() {
        return "FoodDomain{" +
                "id=" + id +
                ", title='" + title + '\'' +
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FoodDomain that = (FoodDomain) o;
        return id == that.id &&
                Double.compare(that.fee, fee) == 0 &&
                numberInCart == that.numberInCart &&
                calories == that.calories &&
                time == that.time &&
                Objects.equals(title, that.title) &&
                Objects.equals(pic, that.pic) &&
                Objects.equals(description, that.description) &&
                Objects.equals(star, that.star) &&
                Objects.equals(toppings, that.toppings) &&
                Objects.equals(selectedToppings, that.selectedToppings);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, pic, description, fee, numberInCart, star, calories, time, toppings, selectedToppings);
    }
}
