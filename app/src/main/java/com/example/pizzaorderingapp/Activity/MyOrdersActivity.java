package com.example.pizzaorderingapp.Activity;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import com.example.pizzaorderingapp.Adapter.OrderItemsAdapter;
import com.example.pizzaorderingapp.Model.OrderItem;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pizzaorderingapp.Adapter.OrderAdapter;
import com.example.pizzaorderingapp.Model.Order;
import com.example.pizzaorderingapp.Helper.DatabaseHelper;
import com.example.pizzaorderingapp.R;
import com.example.pizzaorderingapp.Util.SessionManager;

import java.util.ArrayList;
import java.util.List;

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
                // Pass dbHelper and this (context) to the adapter
                orderAdapter = new OrderAdapter(orderList, dbHelper, this);
                rvMyOrders.setAdapter(orderAdapter);
            } else {
                Toast.makeText(this, "No orders found", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "User not logged in", Toast.LENGTH_SHORT).show();
        }
    }

    // Method to fetch order items from the database
    private List<OrderItem> fetchOrderItems(int orderId) {
        List<OrderItem> orderItems = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase(); // Corrected reference to dbHelper

        Cursor cursor = db.rawQuery("SELECT oi.quantity, oi.price, mi.name FROM order_items oi " +
                "JOIN menu_items mi ON oi.menu_item_id = mi._id " +
                "WHERE oi.order_id = ?", new String[]{String.valueOf(orderId)});

        if (cursor.moveToFirst()) {
            do {
                int quantity = cursor.getInt(cursor.getColumnIndexOrThrow("quantity"));
                double price = cursor.getDouble(cursor.getColumnIndexOrThrow("price"));
                String name = cursor.getString(cursor.getColumnIndexOrThrow("name"));

                orderItems.add(new OrderItem(name, quantity, price));
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();

        return orderItems;
    }

    // Method to show the alert dialog with the order items
    public void showOrderItemsDialog(int orderId) { // Made public for accessibility
        List<OrderItem> orderItems = fetchOrderItems(orderId);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_order_items, null);
        builder.setView(dialogView);

        RecyclerView rvOrderItems = dialogView.findViewById(R.id.rvOrderItems);
        rvOrderItems.setLayoutManager(new LinearLayoutManager(this));
        OrderItemsAdapter adapter = new OrderItemsAdapter(orderItems);
        rvOrderItems.setAdapter(adapter);

        builder.setPositiveButton("OK", (dialog, which) -> dialog.dismiss());

        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
