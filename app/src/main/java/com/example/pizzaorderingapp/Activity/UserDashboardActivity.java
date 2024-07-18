package com.example.pizzaorderingapp.Activity;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;

import com.example.pizzaorderingapp.R;

public class UserDashboardActivity extends AppCompatActivity {

    private ImageView profileImage;
    private TextView userName;
    private TextView userEmail;
    private TextView navItem1;
    private TextView navSubItem1;
    private TextView navSubItem2;
    private TextView navItem2;
    private TextView navItem3;
    private TextView navItem4;
    private TextView navItem5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_dashboard);

//        profileImage = findViewById(R.id.profile_image);
//        userName = findViewById(R.id.user_name);
//        userEmail = findViewById(R.id.user_email);
//        navItem1 = findViewById(R.id.nav_item_1);
//        navSubItem1 = findViewById(R.id.nav_sub_item_1);
//        navSubItem2 = findViewById(R.id.nav_sub_item_2);
//        navItem2 = findViewById(R.id.nav_item_2);
//        navItem3 = findViewById(R.id.nav_item_3);
//        navItem4 = findViewById(R.id.nav_item_4);
//        navItem5 = findViewById(R.id.nav_item_5);

        // Set the data dynamically or leave as static content as per your needs
        // For example, you can load profile image from a URL using libraries like Picasso or Glide
        // userName.setText("New User Name");
        // userEmail.setText("newemail@example.com");

        // Set onClickListeners for navigation items and sub-items if needed
        // navItem1.setOnClickListener(view -> { /* Handle click */ });
        // navSubItem1.setOnClickListener(view -> { /* Handle click */ });
    }
}