package com.example.pizzaorderingapp.Activity;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pizzaorderingapp.Adapter.OrderAdapter;
import com.example.pizzaorderingapp.Helper.DatabaseHelper;
import com.example.pizzaorderingapp.Model.Order;
import com.example.pizzaorderingapp.R;

import java.util.ArrayList;
import java.util.List;

// FavoritesActivity.java
public class FavoritesActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private OrderAdapter orderAdapter;
    private List<Order> favoriteOrders;
    private DatabaseHelper databaseHelper;
    private String userEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorites);

        // Initialize RecyclerView and DatabaseHelper
        recyclerView = findViewById(R.id.recyclerViewFavorites);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        databaseHelper = new DatabaseHelper(this);

        // Retrieve user email from SessionManager or pass it as an extra
        // For this example, assuming you get it from SessionManager
        userEmail = getUserEmail(); // Implement this method to retrieve user's email

        // Fetch favorite orders from the database
        favoriteOrders = databaseHelper.getFavoriteOrders(userEmail);

        // Create and set the adapter
        orderAdapter = new OrderAdapter(new ArrayList<>(favoriteOrders), databaseHelper, this);
        recyclerView.setAdapter(orderAdapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Refresh the favorite orders when the activity is resumed
        favoriteOrders.clear();
        favoriteOrders.addAll(databaseHelper.getFavoriteOrders(userEmail));
        orderAdapter.notifyDataSetChanged();
    }

    // Example method to retrieve user's email (you'll need to implement this based on your setup)
    private String getUserEmail() {
        // Retrieve email from SessionManager or Intent extras
        // This is just a placeholder; replace with actual implementation
        return "user@example.com";
    }
}
