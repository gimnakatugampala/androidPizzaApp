package com.example.pizzaorderingapp.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;

import com.example.pizzaorderingapp.R;
import com.example.pizzaorderingapp.Util.SessionManager;

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

    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_dashboard);

        sessionManager = new SessionManager(getApplicationContext());

        // Initialize UI elements
        profileImage = findViewById(R.id.profile_image);
        userName = findViewById(R.id.user_name);
        userEmail = findViewById(R.id.user_email);

        // Set user details
        userName.setText(sessionManager.getEmail());
        userEmail.setText(sessionManager.getEmail());

        // Initialize other UI elements if needed
    }

    public void onhandleLogout(View view) {
        // Clear session data
        sessionManager.logoutUser();

        // Redirect to login screen with message
        Intent intent = new Intent(UserDashboardActivity.this, LoginScreen.class);
        intent.putExtra("logout_message", "Successfully logged out");
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }

    public void onAddMenuItem(View view){
        Intent intent = new Intent(UserDashboardActivity.this, AddMenuItemActivity.class);
        startActivity(intent);
    }

    public  void onMenuItemList(View view){
        Intent intent = new Intent(UserDashboardActivity.this, MenuItemListActivity.class);
        startActivity(intent);
    }
}
