package com.example.pizzaorderingapp.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

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
    private EditText editTextPromoCode;
    private Button buttonApplyPromoCode;
    private double tax;
    private double discountPercentage = 0.0;
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

    private void initView() {
        totalFeeTxt = findViewById(R.id.totalFeeTxt);
        deliveryTxt = findViewById(R.id.deliveryTxt);
        taxTxt = findViewById(R.id.TaxTxt);  // Corrected ID from TaxTxt to taxTxt
        totalTxt = findViewById(R.id.totalTxt);
        recyclerViewList = findViewById(R.id.view);  // Ensure this ID matches your layout
        scrollView = findViewById(R.id.scrollView);
        emptyTxt = findViewById(R.id.emptyTxt);
        editTextPromoCode = findViewById(R.id.editTextPromoCode);
        buttonApplyPromoCode = findViewById(R.id.buttonApplyPromoCode);

        buttonApplyPromoCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                applyPromoCode();
            }
        });
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
        double discountedTotal = itemTotal - (itemTotal * discountPercentage / 100);
        double total = Math.round((discountedTotal + tax + delivery) * 100.0) / 100.0;

        totalFeeTxt.setText(String.format("$%.2f", discountedTotal));
        taxTxt.setText(String.format("$%.2f", tax));
        deliveryTxt.setText(String.format("$%.2f", delivery));
        totalTxt.setText(String.format("$%.2f", total));
    }

    private void applyPromoCode() {
        String promoCode = editTextPromoCode.getText().toString().trim();

        // Logic to validate promo code and get discount percentage
        // For example, this could be a call to a repository or API
        if (isValidPromoCode(promoCode)) {
            discountPercentage = getDiscountPercentage(promoCode);
            calculateCart();
            Toast.makeText(this, "Promo code applied!", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Invalid promo code", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean isValidPromoCode(String promoCode) {
        // Implement your promo code validation logic here
        return true;
    }

    private double getDiscountPercentage(String promoCode) {
        // Implement your logic to retrieve the discount percentage for the promo code
        return 10.0; // Example discount percentage
    }

    public void onCheckout(View view) {
        // Navigate to the PaymentActivity and pass the total amount
        Intent paymentIntent = new Intent(this, PaymentActivity.class);
        paymentIntent.putExtra("TOTAL_AMOUNT", totalTxt.getText().toString());
        startActivity(paymentIntent);
    }
}