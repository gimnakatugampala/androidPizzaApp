package com.example.pizzaorderingapp.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.pizzaorderingapp.R;

public class OrderConfirmationActivity extends AppCompatActivity {

    private Button btnGoToHome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_confirmation);

        btnGoToHome = findViewById(R.id.btnGoToHome);

        btnGoToHome.setOnClickListener(v -> {
            Intent homeIntent = new Intent(OrderConfirmationActivity.this, MainActivity.class);
            startActivity(homeIntent);
            finish();
        });
    }
}
