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
import com.example.pizzaorderingapp.Model.PromoCode;
import com.example.pizzaorderingapp.Repository.PromoCodeRepository;
import com.example.pizzaorderingapp.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class CartActivity extends AppCompatActivity {

    private View checkoutBtn;  // Declare as Button

    private RecyclerView.Adapter adapter;
    private RecyclerView recyclerViewList;
    private ManagementCart managementCart;
    private TextView totalFeeTxt, taxTxt, deliveryTxt, totalTxt, emptyTxt, appliedPromoCodeTxt;
    private EditText editTextPromoCode;
    private Button buttonApplyPromoCode;
    private double tax;
    private double discountPercentage = 0.0;
    private ScrollView scrollView;
    private PromoCodeRepository promoCodeRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        managementCart = new ManagementCart(this);
        promoCodeRepository = new PromoCodeRepository(this);

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
        appliedPromoCodeTxt = findViewById(R.id.appliedPromoCodeTxt);  // New TextView for applied promo code
        checkoutBtn = findViewById(R.id.checkoutBtn);

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
            checkoutBtn.setVisibility(View.GONE);
        } else {
            emptyTxt.setVisibility(View.GONE);
            scrollView.setVisibility(View.VISIBLE);
            checkoutBtn.setVisibility(View.VISIBLE);
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
        updateTotalAmount(total); // Update the total amount in the UI
    }

    private void applyPromoCode() {
        String promoCodeInput = editTextPromoCode.getText().toString().trim();

        if (isValidPromoCode(promoCodeInput)) {
            discountPercentage = getDiscountPercentage(promoCodeInput);
            appliedPromoCodeTxt.setText(String.format("Applied Promo Code: %s (%.2f%% off)", promoCodeInput, discountPercentage));
            calculateCart();
            Toast.makeText(this, "Promo code applied!", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Invalid promo code", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean isValidPromoCode(String promoCodeInput) {
        PromoCode promoCode = promoCodeRepository.getPromoCodeByCode(promoCodeInput);
        if (promoCode != null) {
            // Check if promo code is expired
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            try {
                Date expiryDate = sdf.parse(promoCode.getExpiryDate());
                return expiryDate != null && !expiryDate.before(new Date());
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    private double getDiscountPercentage(String promoCodeInput) {
        PromoCode promo = promoCodeRepository.getPromoCodeByCode(promoCodeInput);
        return (promo != null) ? promo.getDiscountPercentage() : 0.0;
    }

    private void updateTotalAmount(double amount) {
        totalTxt.setText(String.format("$%.2f", amount));
    }

    private double getCurrentTotalAmount() {
        // Implement your logic to retrieve the current total amount
        return managementCart.getTotalFee() - (managementCart.getTotalFee() * discountPercentage / 100) + tax + 10.0;
    }

    public void onCheckout(View view) {
        // Navigate to the PaymentActivity and pass the total amount
        Intent paymentIntent = new Intent(this, PaymentActivity.class);
        paymentIntent.putExtra("TOTAL_AMOUNT", totalTxt.getText().toString());
        startActivity(paymentIntent);
    }
}
