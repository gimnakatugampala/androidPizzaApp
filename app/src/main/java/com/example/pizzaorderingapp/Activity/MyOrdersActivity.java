package com.example.pizzaorderingapp.Activity;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pizzaorderingapp.Adapter.OrderAdapter;
import com.example.pizzaorderingapp.Model.Order;
import com.example.pizzaorderingapp.Helper.DatabaseHelper;
import com.example.pizzaorderingapp.R;
import com.example.pizzaorderingapp.Util.SessionManager;

import java.util.ArrayList;

public class MyOrdersActivity extends AppCompatActivity {

    private RecyclerView rvMyOrders;
    private OrderAdapter orderAdapter;
    private DatabaseHelper dbHelper;
    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_orders);

        rvMyOrders = findViewById(R.id.rvMyOrders);
        rvMyOrders.setLayoutManager(new LinearLayoutManager(this));

        dbHelper = new DatabaseHelper(this);
        sessionManager = new SessionManager(this);

        String userEmail = sessionManager.getEmail();

        if (userEmail != null) {
            ArrayList<Order> orderList = dbHelper.getAllOrdersByUser(userEmail);
            if (!orderList.isEmpty()) {
                orderAdapter = new OrderAdapter(orderList);
                rvMyOrders.setAdapter(orderAdapter);
            } else {
                Toast.makeText(this, "No orders found", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "User not logged in", Toast.LENGTH_SHORT).show();
        }
    }
}
