package com.example.pizzaorderingapp.Activity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pizzaorderingapp.Adapter.AllFoodItemsAdapter;
import com.example.pizzaorderingapp.Domain.FoodDomain;
import com.example.pizzaorderingapp.Model.MenuItem;
import com.example.pizzaorderingapp.Helper.DatabaseHelper;
import com.example.pizzaorderingapp.Utils.FoodDomainConverter;
import com.example.pizzaorderingapp.Helper.ManagementCart;
import com.example.pizzaorderingapp.R;

import java.util.ArrayList;
import java.util.List;

public class AllFoodItemsActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private AllFoodItemsAdapter adapter;
    private EditText searchEditText;
    private ManagementCart managementCart;
    private List<FoodDomain> foodList = new ArrayList<>(); // Initialize to avoid null issues

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_food_items);

        recyclerView = findViewById(R.id.recyclerViewAllFood);
        searchEditText = findViewById(R.id.searchEditText);
        managementCart = new ManagementCart(this);

        // Retrieve food list from the database
        fetchFoodList();

        setupRecyclerView();
        setupSearch();
    }

    private void fetchFoodList() {
        DatabaseHelper dbHelper = new DatabaseHelper(this);
        List<MenuItem> menuItems = dbHelper.getAllMenuItems();
        foodList = FoodDomainConverter.convertMenuItemsToFoodDomains(menuItems);

        // Initialize foodList if it is null
        if (foodList == null) {
            foodList = new ArrayList<>();
        }
    }

    private void setupRecyclerView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new AllFoodItemsAdapter(this, foodList, managementCart);
        recyclerView.setAdapter(adapter);
    }

    private void setupSearch() {
        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                adapter.filter(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }
}
