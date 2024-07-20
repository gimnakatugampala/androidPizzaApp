package com.example.pizzaorderingapp.Activity;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.pizzaorderingapp.Domain.FoodDomain;
import com.example.pizzaorderingapp.Helper.ManagementCart;
import com.example.pizzaorderingapp.R;

import java.io.File;

public class ShowDetailActivity extends AppCompatActivity {

    private TextView addToCartBtn;
    private TextView titleTxt, feeTxt, descriptionTxt, numberOrderTxt, totalPriceTxt, starTxt, caloriesTxt, timeTxt;
    private ImageView plusBtn, minusBtn, picFood;
    private FoodDomain object;
    private int numberObject = 1;
    private ManagementCart managementCart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_detail);

        managementCart = new ManagementCart(this);

        initView();
        getBundle();
    }

    private void getBundle() {
        object = (FoodDomain) getIntent().getSerializableExtra("object");

        if (object != null) {
            String imageUri = object.getPic();
            Log.d("ShowDetailActivity", "Loading image from URI: " + imageUri);

            if (imageUri != null && !imageUri.isEmpty()) {
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
                            .load(imageUri) // Assume it's a URL
                            .placeholder(R.drawable.pizza_default) // Placeholder image while loading
                            .error(R.drawable.pizza_default) // Image to show if loading fails
                            .into(picFood);
                }
            } else {
                Log.w("ShowDetailActivity", "Image URI is null or empty, setting default image");
                picFood.setImageResource(R.drawable.pizza_default); // Set default image
            }

            titleTxt.setText(object.getTitle());
            feeTxt.setText("$" + object.getFee());
            descriptionTxt.setText(object.getDescription());
            numberOrderTxt.setText(String.valueOf(numberObject));
            totalPriceTxt.setText("$" + numberObject * object.getFee());

            // Set star, calories, and time
            starTxt.setText(object.getStar() + " stars");
            caloriesTxt.setText(object.getCalories() + " calories");
            timeTxt.setText(object.getTime() + " mins");

            plusBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    numberObject++;
                    updateUI();
                }
            });

            minusBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (numberObject > 1) {
                        numberObject--;
                    }
                    updateUI();
                }
            });

            addToCartBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    object.setNumberInCart(numberObject);
                    managementCart.insertFood(object);
                }
            });
        } else {
            Log.e("ShowDetailActivity", "FoodDomain object is null.");
        }
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
    }

    private void updateUI() {
        numberOrderTxt.setText(String.valueOf(numberObject));
        totalPriceTxt.setText("$" + numberObject * object.getFee());
    }
}
