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
import com.example.pizzaorderingapp.Adapters.AdminCompletedOrderAdapter;
import com.example.pizzaorderingapp.Helper.DatabaseHelper;
import com.example.pizzaorderingapp.Model.Order;
import com.example.pizzaorderingapp.R;

import java.util.ArrayList;
import java.util.List;

public class CompletedOrdersActivity extends AppCompatActivity implements AdminCompletedOrderAdapter.OrderActionListener {

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

        // Initialize the list and adapter
        orderList = new ArrayList<>();
        orderAdapter = new AdminCompletedOrderAdapter(this, orderList, this);
        recyclerView.setAdapter(orderAdapter);

        // Load orders with "Completed" status
        loadOrders();
    }

    private void loadOrders() {
        orderList.clear(); // Clear the list before adding new data
        orderList.addAll(dbHelper.getOrdersByStatus("Completed"));
        orderAdapter.notifyDataSetChanged(); // Refresh the adapter with the new data
    }

    @Override
    public void onOrderItemClick(int orderId) {
        showOrderItemsDialog(orderId);
    }

    private void showOrderItemsDialog(int orderId) {
        List<com.example.pizzaorderingapp.Model.OrderItem> orderItems = fetchOrderItems(orderId);

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

    private List<com.example.pizzaorderingapp.Model.OrderItem> fetchOrderItems(int orderId) {
        List<com.example.pizzaorderingapp.Model.OrderItem> orderItems = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT oi.quantity, oi.price, mi.name FROM order_items oi " +
                "JOIN menu_items mi ON oi.menu_item_id = mi._id " +
                "WHERE oi.order_id = ?", new String[]{String.valueOf(orderId)});

        if (cursor.moveToFirst()) {
            do {
                int quantity = cursor.getInt(cursor.getColumnIndexOrThrow("quantity"));
                double price = cursor.getDouble(cursor.getColumnIndexOrThrow("price"));
                String name = cursor.getString(cursor.getColumnIndexOrThrow("name"));

                orderItems.add(new com.example.pizzaorderingapp.Model.OrderItem(name, quantity, price));
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();

        return orderItems;
    }
}
