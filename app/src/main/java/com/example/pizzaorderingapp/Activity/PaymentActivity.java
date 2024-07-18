package com.example.pizzaorderingapp.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.pizzaorderingapp.R;
import com.example.pizzaorderingapp.Utils.MailSender;

public class PaymentActivity extends AppCompatActivity {

    private TextView tvTotalAmount;
    private EditText etCardNumber, etExpiryDate, etCVV;
    private Button btnConfirmPayment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        tvTotalAmount = findViewById(R.id.tvTotalAmount);
        etCardNumber = findViewById(R.id.etCardNumber);
        etExpiryDate = findViewById(R.id.etExpiryDate);
        etCVV = findViewById(R.id.etCVV);
        btnConfirmPayment = findViewById(R.id.btnConfirmPayment);

        // Receive the total amount from the checkout screen
        Intent intent = getIntent();
        String totalAmount = intent.getStringExtra("TOTAL_AMOUNT");
        tvTotalAmount.setText("Total Amount: $" + totalAmount);

        btnConfirmPayment.setOnClickListener(v -> {
            String cardNumber = etCardNumber.getText().toString();
            String expiryDate = etExpiryDate.getText().toString();
            String cvv = etCVV.getText().toString();

            if (cardNumber.isEmpty() || expiryDate.isEmpty() || cvv.isEmpty()) {
                Toast.makeText(PaymentActivity.this, "Please fill in all the details", Toast.LENGTH_SHORT).show();
            } else {
                // Here, you can add the logic to process the payment
                // For now, we'll just display a success message
                Toast.makeText(PaymentActivity.this, "Payment Successful", Toast.LENGTH_SHORT).show();

                // Send confirmation email
                sendConfirmationEmail("gimnakatugampala1@gmail.com", totalAmount);  // replace with actual user email

                // Navigate to order confirmation screen
                Intent confirmationIntent = new Intent(PaymentActivity.this, OrderConfirmationActivity.class);
                startActivity(confirmationIntent);
                finish();
            }
        });
    }

    private void sendConfirmationEmail(String email, String totalAmount) {
        String subject = "Payment Confirmation";
        String message = "Your payment of $" + totalAmount + " was successful. Thank you for your order!";
        MailSender mailSender = new MailSender(email, subject, message);
        mailSender.execute();
    }
}
