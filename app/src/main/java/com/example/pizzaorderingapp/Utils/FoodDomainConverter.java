package com.example.pizzaorderingapp.Utils;

import com.example.pizzaorderingapp.Domain.FoodDomain;
import com.example.pizzaorderingapp.Model.MenuItem;

import java.util.ArrayList;
import java.util.List;

public class FoodDomainConverter {
    public static List<FoodDomain> convertMenuItemsToFoodDomains(List<MenuItem> menuItems) {
        List<FoodDomain> foodDomains = new ArrayList<>();
        for (MenuItem menuItem : menuItems) {
            FoodDomain foodDomain = new FoodDomain(
                    menuItem.getId(),
                    menuItem.getName(),
                    menuItem.getImageUri(),
                    menuItem.getDescription(),
                    menuItem.getPrice(),
                    "", // Assuming no star rating
                    0,  // Assuming no calories
                    0,  // Assuming no preparation time
                    new ArrayList<>(), // Assuming no toppings
                    "" // Assuming no selected toppings
            );
            foodDomains.add(foodDomain);
        }
        return foodDomains;
    }
}