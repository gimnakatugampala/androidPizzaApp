package com.example.pizzaorderingapp.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import androidx.appcompat.app.AppCompatActivity;
import com.example.pizzaorderingapp.Util.SessionManager;
import com.example.pizzaorderingapp.R;

public class SplashActivity extends AppCompatActivity {

    private static final int SPLASH_TIME_OUT = 3000; // 3 seconds
    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        // Initialize SessionManager
        sessionManager = new SessionManager(getApplicationContext());

        // Simulate loading time
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // Check session status
                if (sessionManager.isFirstTimeLaunch()) {
                    // New user, redirect to IntroActivity
                    sessionManager.setFirstTimeLaunch(false);
                    Intent intent = new Intent(SplashActivity.this, IntroActivity.class);
                    startActivity(intent);
                } else if (sessionManager.isLoggedIn()) {
                    // User is logged in, redirect to MainActivity
                    Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                    startActivity(intent);
                } else {
                    // Regular user but not signed in, redirect to LoginActivity
                    Intent intent = new Intent(SplashActivity.this, LoginScreen.class);
                    startActivity(intent);
                }

                // Close SplashActivity
                finish();
            }
        }, SPLASH_TIME_OUT);
    }
}
