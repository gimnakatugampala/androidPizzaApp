package com.example.pizzaorderingapp.Activity;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pizzaorderingapp.Adapter.OrderAdapter;
import com.example.pizzaorderingapp.Helper.DatabaseHelper; // Updated import
import com.example.pizzaorderingapp.Model.Order;
import com.example.pizzaorderingapp.R;

import java.util.ArrayList;

public class DeliveringOrdersActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private OrderAdapter orderAdapter;
    private ArrayList<Order> orderList;
    private DatabaseHelper dbHelper; // Updated variable type

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delivering_orders);

        recyclerView = findViewById(R.id.recyclerViewDeliveringOrders);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        dbHelper = new DatabaseHelper(this); // Updated initialization
        orderList = dbHelper.getOrdersByStatus("Delivering");

        orderAdapter = new OrderAdapter(orderList);
        recyclerView.setAdapter(orderAdapter);

        orderList = dbHelper.getOrdersByStatus("Delivering");
        orderAdapter = new OrderAdapter(orderList);
        recyclerView.setAdapter(orderAdapter);
    }
}
