package com.example.pizzaorderingapp.Activity;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pizzaorderingapp.Adapter.FavoritesAdapter;
import com.example.pizzaorderingapp.Helper.DatabaseHelper;
import com.example.pizzaorderingapp.Model.Order;
import com.example.pizzaorderingapp.R;
import com.example.pizzaorderingapp.Util.SessionManager;

import java.util.ArrayList;
import java.util.List;

// FavoritesActivity.java
public class FavoritesActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private FavoritesAdapter favoritesAdapter;
    private List<Order> favoriteOrders;
    private DatabaseHelper databaseHelper;
    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorites);

        // Initialize RecyclerView and DatabaseHelper
        recyclerView = findViewById(R.id.recyclerViewFavorites);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        databaseHelper = new DatabaseHelper(this);

        // Initialize SessionManager
        sessionManager = new SessionManager(this);

        // Retrieve user email from SessionManager
        String userEmail = sessionManager.getEmail();

        // Initialize favorite orders list and adapter
        favoriteOrders = new ArrayList<>();
        favoritesAdapter = new FavoritesAdapter(favoriteOrders, this);
        recyclerView.setAdapter(favoritesAdapter);

        // Fetch favorite orders from the database and update the adapter
        loadFavoriteOrders(userEmail);
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Refresh the favorite orders when the activity is resumed
        String userEmail = sessionManager.getEmail();
        loadFavoriteOrders(userEmail);
    }

    // Method to load favorite orders from the database and update the adapter
    private void loadFavoriteOrders(String userEmail) {
        favoriteOrders.clear();
        favoriteOrders.addAll(databaseHelper.getFavoriteOrders(userEmail));
        favoritesAdapter.notifyDataSetChanged();
    }
}
