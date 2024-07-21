package com.example.pizzaorderingapp.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pizzaorderingapp.Adapter.CartListAdapter;
import com.example.pizzaorderingapp.Helper.ManagementCart;
import com.example.pizzaorderingapp.Interface.ChangeNumberItemsListener;
import com.example.pizzaorderingapp.R;

public class CartActivity extends AppCompatActivity {

    private RecyclerView.Adapter adapter;
    private RecyclerView recyclerViewList;
    private ManagementCart managementCart;
    private TextView totalFeeTxt, taxTxt, deliveryTxt, totalTxt, emptyTxt;
    private double tax;
    private ScrollView scrollView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        managementCart = new ManagementCart(this);

        initView();
        initList();
        calculateCart();
    }

    private void initList() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerViewList.setLayoutManager(linearLayoutManager);
        adapter = new CartListAdapter(managementCart.getListCart(), this, new ChangeNumberItemsListener() {
            @Override
            public void changed() {
                calculateCart();
                updateVisibility();
            }
        });

        recyclerViewList.setAdapter(adapter);
        updateVisibility();
    }

    private void updateVisibility() {
        if (managementCart.getListCart().isEmpty()) {
            emptyTxt.setVisibility(View.VISIBLE);
            scrollView.setVisibility(View.GONE);
        } else {
            emptyTxt.setVisibility(View.GONE);
            scrollView.setVisibility(View.VISIBLE);
        }
    }

    private void calculateCart() {
        double percentTax = 0.02;
        double delivery = 10.0;

        // Calculate tax and total
        double itemTotal = managementCart.getTotalFee();
        tax = Math.round((itemTotal * percentTax) * 100.0) / 100.0;
        double total = Math.round((itemTotal + tax + delivery) * 100.0) / 100.0;

        totalFeeTxt.setText(String.format("$%.2f", itemTotal));
        taxTxt.setText(String.format("$%.2f", tax));
        deliveryTxt.setText(String.format("$%.2f", delivery));
        totalTxt.setText(String.format("$%.2f", total));
    }

    private void initView() {
        totalFeeTxt = findViewById(R.id.totalFeeTxt);
        deliveryTxt = findViewById(R.id.deliveryTxt);
        taxTxt = findViewById(R.id.TaxTxt);  // Corrected ID from TaxTxt to taxTxt
        totalTxt = findViewById(R.id.totalTxt);
        recyclerViewList = findViewById(R.id.view);  // Ensure this ID matches your layout
        scrollView = findViewById(R.id.scrollView);
        emptyTxt = findViewById(R.id.emptyTxt);
    }

    public void onCheckout(View view) {
        // Navigate to the PaymentActivity and pass the total amount
        Intent paymentIntent = new Intent(this, PaymentActivity.class);
        paymentIntent.putExtra("TOTAL_AMOUNT", totalTxt.getText().toString());
        startActivity(paymentIntent);
    }
}
