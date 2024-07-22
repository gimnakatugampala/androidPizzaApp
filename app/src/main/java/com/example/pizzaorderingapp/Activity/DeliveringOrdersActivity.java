package com.example.pizzaorderingapp.Activity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.app.AlertDialog;

import com.example.pizzaorderingapp.Adapters.AdminDeliveryOrderAdapter;
import com.example.pizzaorderingapp.Helper.DatabaseHelper;
import com.example.pizzaorderingapp.Model.Order;
import com.example.pizzaorderingapp.R;

import java.util.ArrayList;

public class DeliveringOrdersActivity extends AppCompatActivity implements AdminDeliveryOrderAdapter.OrderActionListener {

    private RecyclerView recyclerView;
    private AdminDeliveryOrderAdapter orderAdapter;
    private ArrayList<Order> orderList;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delivering_orders);

        recyclerView = findViewById(R.id.recyclerViewDeliveringOrders);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        dbHelper = new DatabaseHelper(this);

        // Initialize the adapter and load orders
        orderAdapter = new AdminDeliveryOrderAdapter(this, new ArrayList<>(), this);
        recyclerView.setAdapter(orderAdapter);

        loadOrders(); // Load orders initially
    }

    private void loadOrders() {
        orderList = dbHelper.getOrdersByStatus("Delivering");
        orderAdapter.updateOrders(orderList); // Update the adapter with the new list
    }

    @Override
    public void onCompleteClick(final int orderId) {
        // Show confirmation dialog before completing
        new AlertDialog.Builder(this)
                .setTitle("Confirm Completion")
                .setMessage("Are you sure you want to mark this order as completed?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // Handle complete action
                        dbHelper.updateOrderStatus(orderId, "Completed");
                        loadOrders(); // Refresh the order list

                        // Show a success message
                        Toast.makeText(DeliveringOrdersActivity.this, "Order marked as completed", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("No", null)
                .show();
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

                        // Show a success message
                        Toast.makeText(DeliveringOrdersActivity.this, "Order canceled", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("No", null)
                .show();
    }
}
