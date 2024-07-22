package com.example.pizzaorderingapp.Activity;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pizzaorderingapp.Adapters.AdminCanceledOrderAdapter;
import com.example.pizzaorderingapp.Helper.DatabaseHelper;
import com.example.pizzaorderingapp.Model.Order;
import com.example.pizzaorderingapp.R;

import java.util.ArrayList;

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
    public void onCompleteClick(int orderId) {
        // Handle complete action for canceled orders (if applicable)
        // In this case, nothing needs to be done as orders are already canceled
    }

    @Override
    public void onCancelClick(int orderId) {
        // Handle cancel action (if applicable)
        // In this case, nothing needs to be done as orders are already canceled
    }
}
