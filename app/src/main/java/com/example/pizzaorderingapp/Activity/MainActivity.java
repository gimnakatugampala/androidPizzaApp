package com.example.pizzaorderingapp.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
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
import com.example.pizzaorderingapp.Util.SessionManager;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerViewCategoryList, recyclerViewPopularList;
    private DatabaseHelper dbHelper;
    private MenuItemRepository menuItemRepository;
    private ManagementCart managementCart;
    private SessionManager sessionManager;
    private ImageView userProfileImage; // Added for profile image

    private TextView welcomeTextView; // Added for displaying the user's first name

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        // Initialize SessionManager
        sessionManager = new SessionManager(this);

        // Initialize helpers
        dbHelper = new DatabaseHelper(this);
        menuItemRepository = new MenuItemRepository(this);
        managementCart = new ManagementCart(this);

        // Initialize ImageView
        userProfileImage = findViewById(R.id.userProfileDetails);
        welcomeTextView = findViewById(R.id.textView4);

        // Load user profile image
        loadUserProfileImage();

        setWelcomeText();

        // Setup RecyclerViews
        setupRecyclerViews();

        // Setup bottom navigation
        setupBottomNavigation();
    }

    private void loadUserProfileImage() {
        String imageUrl = sessionManager.getProfileImageUrl();
        if (imageUrl == null || imageUrl.isEmpty()) {
            // Use default image if no profile picture is provided
            userProfileImage.setImageResource(R.drawable.profile_picture);
        } else {
            // Load the user image using Picasso
            Picasso.get()
                    .load(imageUrl)
                    .placeholder(R.drawable.profile_picture) // Optional placeholder
                    .error(R.drawable.profile_picture) // Optional error image
                    .into(userProfileImage);
        }
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
            int star = random.nextInt(5) + 1;
            int time = random.nextInt(60) + 10;
            int calories = random.nextInt(500) + 100;

            String toppingsString = menuItem.getToppings();
            List<String> toppings = toppingsString != null && !toppingsString.isEmpty()
                    ? Arrays.asList(toppingsString.split("\\s*,\\s*"))
                    : new ArrayList<>();

            FoodDomain foodDomain = new FoodDomain(
                    menuItem.getId(),
                    menuItem.getName(),
                    menuItem.getImageUri(),
                    menuItem.getDescription(),
                    menuItem.getPrice(),
                    String.valueOf(star),
                    calories,
                    time,
                    toppings,
                    ""
            );
            foodList.add(foodDomain);
        }

        RecommendedAdapter recommendedAdapter = new RecommendedAdapter(foodList, new RecommendedAdapter.OnItemClickListener() {
            @Override
            public void onAddToCartClick(FoodDomain foodDomain) {
                managementCart.insertFood(foodDomain);
                Toast.makeText(MainActivity.this, "Added to cart", Toast.LENGTH_SHORT).show();
            }
        }, MainActivity.this);
        recyclerViewPopularList.setAdapter(recommendedAdapter);
    }

    private void setWelcomeText() {
        String firstName = sessionManager.getFirstName(); // Assuming this method returns the user's first name
        if (firstName != null && !firstName.isEmpty()) {
            welcomeTextView.setText("Hi " + firstName + "!");
        } else {
            welcomeTextView.setText("Hi there!");
        }
    }

    private void setupBottomNavigation() {
        LinearLayout homeBtn = findViewById(R.id.homeBtn);
        LinearLayout cartBtn = findViewById(R.id.cartBtn);
        LinearLayout storeBtn = findViewById(R.id.storeBtn);
        LinearLayout myOrdersBtn = findViewById(R.id.myOrdersBtn);
        LinearLayout guestLogout = findViewById(R.id.logoutPofileContainer);

        homeBtn.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, MainActivity.class)));
        cartBtn.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, CartActivity.class)));
        storeBtn.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, AllStores.class)));

        // Set visibility based on user role
        if (sessionManager.isGuest()) {
            findViewById(R.id.containerProfile).setVisibility(View.GONE);
            myOrdersBtn.setVisibility(View.GONE);
            guestLogout.setVisibility(View.VISIBLE);
        } else {
            guestLogout.setVisibility(View.GONE);
        }

        myOrdersBtn.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, MyOrdersActivity.class)));
    }

    public void onGoToUserDashboard(View view) {
        if (!sessionManager.isGuest()) {
            startActivity(new Intent(MainActivity.this, UserDashboardActivity.class));
        } else {
            Toast.makeText(this, "Guest users cannot access the dashboard. Please log in to continue.", Toast.LENGTH_LONG).show();
        }
    }

    public void onClickProfile(View view) {
        startActivity(new Intent(MainActivity.this, UpdateProfileActivity.class));
    }

    public void onClickMyStore(View view) {
        startActivity(new Intent(MainActivity.this, MyOrdersActivity.class));
    }

    public void onClickSeeMore(View view) {
        startActivity(new Intent(MainActivity.this, AllFoodItemsActivity.class));
    }

    public void onClickGuestLogout(View view) {
        // Clear session data
        sessionManager.logoutUser(); // Implement this method to clear user session

        // Redirect to login screen
        Intent intent = new Intent(this, LoginScreen.class);
        startActivity(intent);

        // Optionally, finish the current activity to prevent going back to it
        finish();
    }
}
