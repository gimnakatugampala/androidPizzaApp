package com.example.pizzaorderingapp.Activity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.app.AlertDialog;

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
        if (orderAdapter != null) {
            orderAdapter.updateOrders(orderList); // Update adapter with new data
        }
    }

    @Override
    public void onCancelClick(final int orderId) {
        // Show confirmation dialog before canceling
        new AlertDialog.Builder(this)
                .setTitle("Confirm Cancellation")
                .setMessage("Are you sure you want to cancel this order?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // Handle cancel action
                        dbHelper.updateOrderStatus(orderId, "Canceled");
                        loadOrders(); // Refresh the order list
                        Toast.makeText(PendingOrdersActivity.this, "Order has been canceled.", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("No", null)
                .show();
    }

    @Override
    public void onConfirmClick(final int orderId) {
        // Show confirmation dialog before confirming
        new AlertDialog.Builder(this)
                .setTitle("Confirm Delivery")
                .setMessage("Are you sure you want to confirm this order?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // Handle confirm action
                        dbHelper.updateOrderStatus(orderId, "Delivering");
                        loadOrders(); // Refresh the order list
                        Toast.makeText(PendingOrdersActivity.this, "Order has been confirmed for delivery.", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("No", null)
                .show();
    }
}
