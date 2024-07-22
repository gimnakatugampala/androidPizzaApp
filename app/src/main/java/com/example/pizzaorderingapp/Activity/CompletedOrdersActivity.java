package com.example.pizzaorderingapp.Activity;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pizzaorderingapp.Adapters.AdminCompletedOrderAdapter;
import com.example.pizzaorderingapp.Helper.DatabaseHelper;
import com.example.pizzaorderingapp.Model.Order;
import com.example.pizzaorderingapp.R;

import java.util.ArrayList;

public class CompletedOrdersActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private AdminCompletedOrderAdapter orderAdapter;
    private ArrayList<Order> orderList;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_completed_orders);

        recyclerView = findViewById(R.id.recyclerViewCompletedOrders);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        dbHelper = new DatabaseHelper(this);

        // Load orders with "Completed" status
        loadOrders();

        // Initialize the adapter
        orderAdapter = new AdminCompletedOrderAdapter(this, orderList);
        recyclerView.setAdapter(orderAdapter);
    }

    private void loadOrders() {
        orderList = dbHelper.getOrdersByStatus("Completed");
        if (orderAdapter != null) {
            orderAdapter.notifyDataSetChanged(); // Refresh the adapter with the new data
        }
    }
}
