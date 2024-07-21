package com.example.pizzaorderingapp.Activity;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.pizzaorderingapp.Domain.FoodDomain;
import com.example.pizzaorderingapp.Helper.ManagementCart;
import com.example.pizzaorderingapp.R;

import java.io.File;
import java.util.List;

public class ShowDetailActivity extends AppCompatActivity {

    private TextView addToCartBtn, titleTxt, feeTxt, descriptionTxt, numberOrderTxt, totalPriceTxt, starTxt, caloriesTxt, timeTxt;
    private ImageView plusBtn, minusBtn, picFood;
    private CheckBox[] toppingsCheckBoxes; // Array to hold CheckBox references
    private FoodDomain foodDomain;
    private int quantity = 1;
    private ManagementCart managementCart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_detail);

        managementCart = new ManagementCart(this);
        initView();
        getFoodDetailsFromIntent();
    }

    private void initView() {
        addToCartBtn = findViewById(R.id.addToCartBtn);
        titleTxt = findViewById(R.id.titleTxt);
        feeTxt = findViewById(R.id.priceTxt);
        descriptionTxt = findViewById(R.id.descriptionTxt);
        numberOrderTxt = findViewById(R.id.numberItemTxt);
        plusBtn = findViewById(R.id.plusCardBtn);
        minusBtn = findViewById(R.id.minusCardBtn);
        picFood = findViewById(R.id.foodPic);
        totalPriceTxt = findViewById(R.id.totalPriceTxt);
        starTxt = findViewById(R.id.starTxt);
        caloriesTxt = findViewById(R.id.caloriesTxt);
        timeTxt = findViewById(R.id.TimeTxt);

        // Initialize CheckBox references
        toppingsCheckBoxes = new CheckBox[]{
                findViewById(R.id.topping1),
                findViewById(R.id.topping2),
                findViewById(R.id.topping3)
                // Add more CheckBox IDs as needed
        };
    }

    private void getFoodDetailsFromIntent() {
        foodDomain = (FoodDomain) getIntent().getSerializableExtra("object");

        if (foodDomain == null) {
            Log.e("ShowDetailActivity", "FoodDomain object is null.");
            return;
        }

        loadFoodImage();
        populateFoodDetails();
        setupButtonListeners();
    }

    private void loadFoodImage() {
        String imageUri = foodDomain.getPic();
        if (imageUri == null || imageUri.isEmpty()) {
            picFood.setImageResource(R.drawable.pizza_default);
            return;
        }

        if (imageUri.startsWith("file://")) {
            File imageFile = new File(imageUri.substring(7)); // Remove "file://" from the path
            if (imageFile.exists()) {
                picFood.setImageURI(android.net.Uri.fromFile(imageFile)); // Directly set URI for local files
            } else {
                Log.w("ShowDetailActivity", "Image file does not exist: " + imageUri);
                picFood.setImageResource(R.drawable.pizza_default); // Set default image
            }
        } else {
            Glide.with(this)
                    .load(imageUri)
                    .placeholder(R.drawable.pizza_default) // Placeholder image while loading
                    .error(R.drawable.pizza_default) // Image to show if loading fails
                    .into(picFood);
        }
    }

    private void populateFoodDetails() {
        titleTxt.setText(foodDomain.getTitle());
        feeTxt.setText("$" + foodDomain.getFee());
        descriptionTxt.setText(foodDomain.getDescription());
        numberOrderTxt.setText(String.valueOf(quantity));
        totalPriceTxt.setText("$" + quantity * foodDomain.getFee());
        starTxt.setText(foodDomain.getStar() + " stars");
        caloriesTxt.setText(foodDomain.getCalories() + " calories");
        timeTxt.setText(foodDomain.getTime() + " mins");

        // Set CheckBox states based on available toppings
        List<String> toppings = foodDomain.getToppings();
        for (int i = 0; i < toppingsCheckBoxes.length; i++) {
            if (i < toppings.size()) {
                toppingsCheckBoxes[i].setText(toppings.get(i));
                toppingsCheckBoxes[i].setChecked(false); // Default to unchecked
                toppingsCheckBoxes[i].setVisibility(View.VISIBLE); // Ensure CheckBox is visible
            } else {
                toppingsCheckBoxes[i].setVisibility(View.GONE); // Hide CheckBox if not needed
            }
        }
    }

    private void setupButtonListeners() {
        plusBtn.setOnClickListener(v -> {
            quantity++;
            updateUI();
        });

        minusBtn.setOnClickListener(v -> {
            if (quantity > 1) {
                quantity--;
            }
            updateUI();
        });

        addToCartBtn.setOnClickListener(v -> {
            foodDomain.setNumberInCart(quantity);
            // Get selected toppings
            StringBuilder selectedToppings = new StringBuilder();
            for (CheckBox checkBox : toppingsCheckBoxes) {
                if (checkBox.getVisibility() == View.VISIBLE && checkBox.isChecked()) {
                    if (selectedToppings.length() > 0) {
                        selectedToppings.append(", ");
                    }
                    selectedToppings.append(checkBox.getText().toString());
                }
            }
            foodDomain.setSelectedToppings(selectedToppings.toString());
            managementCart.insertFood(foodDomain);
        });
    }

    private void updateUI() {
        numberOrderTxt.setText(String.valueOf(quantity));
        totalPriceTxt.setText("$" + quantity * foodDomain.getFee());
    }
}
