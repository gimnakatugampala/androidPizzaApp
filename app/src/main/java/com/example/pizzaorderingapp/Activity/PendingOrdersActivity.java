package com.example.pizzaorderingapp.Activity;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pizzaorderingapp.Adapters.AdminOrderAdapter;
import com.example.pizzaorderingapp.Helper.DatabaseHelper;
import com.example.pizzaorderingapp.Model.Order;
import com.example.pizzaorderingapp.R;

import java.util.ArrayList;

public class PendingOrdersActivity extends AppCompatActivity implements AdminOrderAdapter.OrderActionListener {

    private RecyclerView recyclerView;
    private AdminOrderAdapter orderAdapter;
    private ArrayList<Order> orderList;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pending_orders);

        recyclerView = findViewById(R.id.recyclerViewPendingOrders);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        dbHelper = new DatabaseHelper(this);

        // Load orders with "Pending" status
        loadOrders();

        // Initialize the adapter
        orderAdapter = new AdminOrderAdapter(this, orderList, this);
        recyclerView.setAdapter(orderAdapter);
    }

    private void loadOrders() {
        orderList = dbHelper.getOrdersByStatus("Pending");
    }

    @Override
    public void onCancelClick(int orderId) {
        // Handle cancel action
        dbHelper.updateOrderStatus(orderId, "Canceled");
        loadOrders(); // Refresh the order list
        orderAdapter.notifyDataSetChanged();
    }

    @Override
    public void onConfirmClick(int orderId) {
        // Handle confirm action
        dbHelper.updateOrderStatus(orderId, "Delivering");
        loadOrders(); // Refresh the order list
        orderAdapter.notifyDataSetChanged();
    }

    @Override
    public void onCompleteClick(int orderId) {
        // Handle complete action
        dbHelper.updateOrderStatus(orderId, "Completed");
        loadOrders(); // Refresh the order list
        orderAdapter.notifyDataSetChanged();
    }
}
