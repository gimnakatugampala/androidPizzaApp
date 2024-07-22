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

    public void  onClickProfile(View view){
        Intent intent = new Intent(UserDashboardActivity.this, UpdateProfileActivity.class);
        startActivity(intent);
    }

    public  void onClickAddPromo(View view){
        Intent intent = new Intent(UserDashboardActivity.this, AddPromoCodeActivity.class);
        startActivity(intent);
    }

    public  void  onClickAllPromos(View view){
        Intent intent = new Intent(UserDashboardActivity.this, PromoCodeListActivity.class);
        startActivity(intent);
    }

    public void onClickPendingOrders(View view){
        Intent intent = new Intent(UserDashboardActivity.this, PendingOrdersActivity.class);
        startActivity(intent);
    }

    public void onClickDeliveryOrders(View view){
        Intent intent = new Intent(UserDashboardActivity.this, DeliveringOrdersActivity.class);
        startActivity(intent);
    }

    public void onClickCancelOrders(View view){
        Intent intent = new Intent(UserDashboardActivity.this, CanceledOrdersActivity.class);
        startActivity(intent);
    }

    public void onClickCompletedOrders(View view){
        Intent intent = new Intent(UserDashboardActivity.this, CompletedOrdersActivity.class);
        startActivity(intent);
    }

    public void onClickMyOrders(View view){
        Intent intent = new Intent(UserDashboardActivity.this, MyOrdersActivity.class);
        startActivity(intent);
    }

    public void onClickAllStores(View view){
        Intent intent = new Intent(UserDashboardActivity.this, AllStores.class);
        startActivity(intent);
    }


    public void onClickManageCustomers(View view){
        Intent intent = new Intent(UserDashboardActivity.this, CustomerListActivity.class);
        startActivity(intent);
    }

}
