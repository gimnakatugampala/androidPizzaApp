package com.example.pizzaorderingapp.Activity;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pizzaorderingapp.Adapter.OrderItemsAdapter;
import com.example.pizzaorderingapp.Adapters.AdminCanceledOrderAdapter;
import com.example.pizzaorderingapp.Helper.DatabaseHelper;
import com.example.pizzaorderingapp.Model.Order;
import com.example.pizzaorderingapp.Model.OrderItem; // Ensure this import
import com.example.pizzaorderingapp.R;

import java.util.ArrayList;
import java.util.List;

public class CanceledOrdersActivity extends AppCompatActivity implements AdminCanceledOrderAdapter.OrderActionListener {

    private RecyclerView recyclerView;
    private AdminCanceledOrderAdapter orderAdapter;
    private ArrayList<Order> orderList;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_canceled_orders);

        recyclerView = findViewById(R.id.recyclerViewCanceledOrders);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        dbHelper = new DatabaseHelper(this);

        // Initialize the list and adapter
        orderList = new ArrayList<>();
        orderAdapter = new AdminCanceledOrderAdapter(this, orderList, this);
        recyclerView.setAdapter(orderAdapter);

        // Load orders with "Canceled" status
        loadOrders();
    }

    private void loadOrders() {
        orderList.clear(); // Clear the list before adding new data
        orderList.addAll(dbHelper.getOrdersByStatus("Canceled"));
        orderAdapter.notifyDataSetChanged(); // Refresh the adapter with the new data
    }

    @Override
    public void onOrderItemClick(final int orderId) {
        showOrderItemsDialog(orderId);
    }



    @Override
    public void onCancelClick(int orderId) {
        // No action needed for canceled orders
    }

    private void showOrderItemsDialog(int orderId) {
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

    private List<OrderItem> fetchOrderItems(int orderId) {
        List<OrderItem> orderItems = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();

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
}
