package com.example.pizzaorderingapp.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pizzaorderingapp.Adapter.CategoryAdapter;
import com.example.pizzaorderingapp.Adapter.RecommendedAdapter;
import com.example.pizzaorderingapp.Domain.CategoryDomain;
import com.example.pizzaorderingapp.Domain.FoodDomain;
import com.example.pizzaorderingapp.Helper.DatabaseHelper;
import com.example.pizzaorderingapp.Helper.ManagementCart;
import com.example.pizzaorderingapp.Model.MenuItem;
import com.example.pizzaorderingapp.Repository.MenuItemRepository;
import com.example.pizzaorderingapp.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerViewCategoryList, recyclerViewPopularList;
    private DatabaseHelper dbHelper;
    private MenuItemRepository menuItemRepository;
    private ManagementCart managementCart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        // Initialize helpers
        dbHelper = new DatabaseHelper(this);
        menuItemRepository = new MenuItemRepository(this);
        managementCart = new ManagementCart(this);

        // Setup RecyclerViews
        setupRecyclerViews();

        // Setup bottom navigation
        setupBottomNavigation();
    }

    private void setupRecyclerViews() {
        // Setup category RecyclerView
        recyclerViewCategoryList = findViewById(R.id.view1);
        recyclerViewCategoryList.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        ArrayList<CategoryDomain> categoryList = dbHelper.getAllCategories();
        CategoryAdapter categoryAdapter = new CategoryAdapter(categoryList);
        recyclerViewCategoryList.setAdapter(categoryAdapter);

        // Setup popular items RecyclerView
        recyclerViewPopularList = findViewById(R.id.view2);
        recyclerViewPopularList.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        List<MenuItem> menuItems = menuItemRepository.getAllMenuItems();
        ArrayList<FoodDomain> foodList = new ArrayList<>();
        Random random = new Random();

        for (MenuItem menuItem : menuItems) {
            // Generate random values for star, time, and calories
            int star = random.nextInt(5) + 1;          // Random star rating between 1 and 5
            int time = random.nextInt(60) + 10;        // Random preparation time between 10 and 70 minutes
            int calories = random.nextInt(500) + 100;  // Random calories between 100 and 600

            // Convert toppings from String to List<String>
            String toppingsString = menuItem.getToppings(); // Assuming this returns a comma-separated string
            List<String> toppings = toppingsString != null && !toppingsString.isEmpty()
                    ? Arrays.asList(toppingsString.split("\\s*,\\s*"))
                    : new ArrayList<>();

            FoodDomain foodDomain = new FoodDomain(
                    menuItem.getId(),
                    menuItem.getName(),
                    menuItem.getImageUri(),
                    menuItem.getDescription(),
                    menuItem.getPrice(),
                    String.valueOf(star), // Convert star to String
                    calories,
                    time,
                    toppings, // Pass toppings list to FoodDomain
                    "" // Default value for selectedToppings
            );
            foodList.add(foodDomain);
        }

        RecommendedAdapter recommendedAdapter = new RecommendedAdapter(foodList, new RecommendedAdapter.OnItemClickListener() {
            @Override
            public void onAddToCartClick(FoodDomain foodDomain) {
                managementCart.insertFood(foodDomain);
                Toast.makeText(MainActivity.this, "Added to cart", Toast.LENGTH_SHORT).show();
            }
        }, MainActivity.this); // Pass the context here
        recyclerViewPopularList.setAdapter(recommendedAdapter);
    }

    private void setupBottomNavigation() {
        LinearLayout homeBtn = findViewById(R.id.homeBtn);
        LinearLayout cartBtn = findViewById(R.id.cartBtn);
        LinearLayout storeBtn = findViewById(R.id.storeBtn);
        LinearLayout myOrdersBtn = findViewById(R.id.myOrdersBtn);

        homeBtn.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, MainActivity.class)));

        cartBtn.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, CartActivity.class)));

        storeBtn.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, AllStores.class)));

//        myOrdersBtn.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, OrderHistoryActivity.class)));
    }

    public void onGoToUserDashboard(View view) {
        startActivity(new Intent(MainActivity.this, UserDashboardActivity.class));
    }

    public void onClickProfile(View view) {
        startActivity(new Intent(MainActivity.this, UpdateProfileActivity.class));
    }

    public  void onClickMyStore(View view){
        startActivity(new Intent(MainActivity.this, MyOrdersActivity.class));
    }

    public  void onClickSeeMore(View view){
        startActivity(new Intent(MainActivity.this, AllFoodItemsActivity.class));
    }
}
