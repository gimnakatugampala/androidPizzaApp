package com.example.pizzaorderingapp.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.pizzaorderingapp.R;

public class LoginScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

    }

    public void gotoSignUp(View view) {
        Intent intent = new Intent(LoginScreen.this, SignUp_Screen.class);
        startActivity(intent);
    }
}