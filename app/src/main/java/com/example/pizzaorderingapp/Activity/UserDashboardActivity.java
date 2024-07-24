package com.example.pizzaorderingapp.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.pizzaorderingapp.R;
import com.example.pizzaorderingapp.Util.SessionManager;

public class UserDashboardActivity extends AppCompatActivity {

    private ImageView profileImage;
    private TextView userName;
    private TextView userEmail;

    // Declare variables for admin-specific UI elements using correct IDs from XML
    private TextView menuManagementTextView;
    private TextView manageOrdersTextView;
    private TextView managePromotionsTextView;
    private TextView manageCustomersTextView;

    // Declare variables for admin-specific containers
    private LinearLayout menuItemsContainer;
    private LinearLayout orderContainer;
    private LinearLayout promosContainer;

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

        // Initialize admin-specific UI elements using IDs from XML
        menuManagementTextView = findViewById(R.id.nav_item_1);
        manageOrdersTextView = findViewById(R.id.nav_item_4);
        managePromotionsTextView = findViewById(R.id.nav_item_9);
        manageCustomersTextView = findViewById(R.id.nav_item_8);

        // Initialize admin-specific containers using IDs from XML
        menuItemsContainer = findViewById(R.id.MenuItemsContainer);
        orderContainer = findViewById(R.id.OrderContainer);
        promosContainer = findViewById(R.id.PromosContainer);

        // Set user details
        String firstName = sessionManager.getFirstName();
        String lastName = sessionManager.getLastName();
        String email = sessionManager.getEmail();
        String profileImageUrl = sessionManager.getProfileImageUrl();

        if (firstName != null && lastName != null) {
            String fullName = firstName + " " + lastName;
            userName.setText(fullName);
        }
        userEmail.setText(email);

        // Load profile image
        if (profileImageUrl != null && !profileImageUrl.isEmpty()) {
            Glide.with(this)
                    .load(profileImageUrl)
                    .placeholder(R.drawable.profile_picture) // Default image
                    .error(R.drawable.profile_picture) // Image to display on error
                    .into(profileImage);
        } else {
            profileImage.setImageResource(R.drawable.profile_picture);
        }

        // Show admin-specific sections if the user is an admin
        if ("Admin".equals(sessionManager.getRole())) {
            menuManagementTextView.setVisibility(View.VISIBLE);
            manageOrdersTextView.setVisibility(View.VISIBLE);
            managePromotionsTextView.setVisibility(View.VISIBLE);
            manageCustomersTextView.setVisibility(View.VISIBLE);
            menuItemsContainer.setVisibility(View.VISIBLE);
            orderContainer.setVisibility(View.VISIBLE);
            promosContainer.setVisibility(View.VISIBLE);
        } else {
            menuManagementTextView.setVisibility(View.GONE);
            manageOrdersTextView.setVisibility(View.GONE);
            managePromotionsTextView.setVisibility(View.GONE);
            manageCustomersTextView.setVisibility(View.GONE);
            menuItemsContainer.setVisibility(View.GONE);
            orderContainer.setVisibility(View.GONE);
            promosContainer.setVisibility(View.GONE);
        }
    }

    public void onhandleLogout(View view) {
        sessionManager.logoutUser();
        Intent intent = new Intent(UserDashboardActivity.this, LoginScreen.class);
        intent.putExtra("logout_message", "Successfully logged out");
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }

    public void onAddMenuItem(View view) {
        Intent intent = new Intent(UserDashboardActivity.this, AddMenuItemActivity.class);
        startActivity(intent);
    }

    public void onMenuItemList(View view) {
        Intent intent = new Intent(UserDashboardActivity.this, MenuItemListActivity.class);
        startActivity(intent);
    }

    public void onClickProfile(View view) {
        Intent intent = new Intent(UserDashboardActivity.this, UpdateProfileActivity.class);
        startActivity(intent);
    }

    public void onClickAddPromo(View view) {
        Intent intent = new Intent(UserDashboardActivity.this, AddPromoCodeActivity.class);
        startActivity(intent);
    }

    public void onClickAllPromos(View view) {
        Intent intent = new Intent(UserDashboardActivity.this, PromoCodeListActivity.class);
        startActivity(intent);
    }

    public void onClickPendingOrders(View view) {
        Intent intent = new Intent(UserDashboardActivity.this, PendingOrdersActivity.class);
        startActivity(intent);
    }

    public void onClickDeliveryOrders(View view) {
        Intent intent = new Intent(UserDashboardActivity.this, DeliveringOrdersActivity.class);
        startActivity(intent);
    }

    public void onClickCancelOrders(View view) {
        Intent intent = new Intent(UserDashboardActivity.this, CanceledOrdersActivity.class);
        startActivity(intent);
    }

    public void onClickCompletedOrders(View view) {
        Intent intent = new Intent(UserDashboardActivity.this, CompletedOrdersActivity.class);
        startActivity(intent);
    }

    public void onClickMyOrders(View view) {
        Intent intent = new Intent(UserDashboardActivity.this, MyOrdersActivity.class);
        startActivity(intent);
    }

    public void onClickAllStores(View view) {
        Intent intent = new Intent(UserDashboardActivity.this, AllStores.class);
        startActivity(intent);
    }

    public void onClickManageCustomers(View view) {
        Intent intent = new Intent(UserDashboardActivity.this, CustomerListActivity.class);
        startActivity(intent);
    }


    public void onClickGetFavs(View view) {
        Intent intent = new Intent(UserDashboardActivity.this, FavoritesActivity.class);
        startActivity(intent);
    }
}
