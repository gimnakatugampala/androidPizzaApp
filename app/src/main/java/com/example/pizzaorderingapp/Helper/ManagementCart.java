package com.example.pizzaorderingapp.Helper;

import android.content.Context;
import android.widget.Toast;

import com.example.pizzaorderingapp.Domain.FoodDomain;
import com.example.pizzaorderingapp.Interface.ChangeNumberItemsListener;

import java.util.ArrayList;

public class ManagementCart {

    private Context context;
    private TinyDB tinyDB;

    public ManagementCart(Context context) {
        this.context = context;
        this.tinyDB = new TinyDB(context);
    }

    // Insert or update the food item in the cart
    public void insertFood(FoodDomain item) {
        ArrayList<FoodDomain> listFood = getListCart();
        boolean existsAlready = false;
        int index = 0;

        // Check if the item already exists in the cart
        for (int i = 0; i < listFood.size(); i++) {
            if (listFood.get(i).getTitle().equals(item.getTitle())) {
                existsAlready = true;
                index = i;
                break;
            }
        }

        // Update quantity if item exists, else add new item
        if (existsAlready) {
            FoodDomain existingItem = listFood.get(index);
            existingItem.setNumberInCart(existingItem.getNumberInCart() + item.getNumberInCart());
        } else {
            listFood.add(item);
        }

        // Save updated list to TinyDB
        tinyDB.putListObject("CartList", listFood);
        Toast.makeText(context, "Added to your cart", Toast.LENGTH_SHORT).show();
    }

    // Get the list of items in the cart
    public ArrayList<FoodDomain> getListCart() {
        return tinyDB.getListObject("CartList");
    }

    // Decrease the quantity of a food item in the cart
    public void minusNumberFood(ArrayList<FoodDomain> listFood, int position, ChangeNumberItemsListener changeNumberItemsListener) {
        if (position >= 0 && position < listFood.size()) {
            FoodDomain item = listFood.get(position);

            if (item.getNumberInCart() > 1) {
                item.setNumberInCart(item.getNumberInCart() - 1);
            } else {
                listFood.remove(position);
            }

            // Save updated list to TinyDB
            tinyDB.putListObject("CartList", listFood);
            changeNumberItemsListener.changed();
        } else {
            Toast.makeText(context, "Invalid item position", Toast.LENGTH_SHORT).show();
        }
    }

    // Increase the quantity of a food item in the cart
    public void plusNumberFood(ArrayList<FoodDomain> listFood, int position, ChangeNumberItemsListener changeNumberItemsListener) {
        if (position >= 0 && position < listFood.size()) {
            FoodDomain item = listFood.get(position);
            item.setNumberInCart(item.getNumberInCart() + 1);

            // Save updated list to TinyDB
            tinyDB.putListObject("CartList", listFood);
            changeNumberItemsListener.changed();
        } else {
            Toast.makeText(context, "Invalid item position", Toast.LENGTH_SHORT).show();
        }
    }

    // Remove a food item from the cart
    public void removeFoodFromCart(ArrayList<FoodDomain> listFood, int position, ChangeNumberItemsListener changeNumberItemsListener) {
        if (position >= 0 && position < listFood.size()) {
            listFood.remove(position);
            tinyDB.putListObject("CartList", listFood);
            Toast.makeText(context, "Item removed from cart", Toast.LENGTH_SHORT).show();
            changeNumberItemsListener.changed();
        } else {
            Toast.makeText(context, "Invalid item position", Toast.LENGTH_SHORT).show();
        }
    }

    // Get the total fee of all items in the cart
    public Double getTotalFee() {
        ArrayList<FoodDomain> listFood = getListCart();
        double fee = 0;

        for (FoodDomain item : listFood) {
            fee += item.getTotalPrice(); // Use the updated getTotalPrice method
        }

        return fee;
    }

    // Clear all items from the cart
    public void clearCart() {
        tinyDB.putListObject("CartList", new ArrayList<FoodDomain>());
        Toast.makeText(context, "Cart cleared", Toast.LENGTH_SHORT).show();
    }
}
