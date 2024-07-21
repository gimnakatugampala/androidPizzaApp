package com.example.pizzaorderingapp.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.pizzaorderingapp.Database.OrderRepository;
import com.example.pizzaorderingapp.Models.Order;
import com.example.pizzaorderingapp.R;
import com.example.pizzaorderingapp.Adapters.OrderAdapter;

import java.util.List;

public class OrderHistoryActivity extends AppCompatActivity {

    private ListView lvOrderHistory;
    private OrderRepository orderRepository;
    private String userEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_history);

        lvOrderHistory = findViewById(R.id.lvOrderHistory);
//        orderRepository = new OrderRepository(this);

        // Assume userEmail is retrieved from the logged-in user's information
//        Intent intent = getIntent();
//        userEmail = intent.getStringExtra("gimnakatugampala1@gmail.com");
//
//        List<Order> orderList = orderRepository.getOrdersByEmail(userEmail);
//        OrderAdapter adapter = new OrderAdapter(this, orderList);
//        lvOrderHistory.setAdapter(adapter);
    }
}
