package com.example.pizzaorderingapp.Activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.pizzaorderingapp.Helper.DatabaseHelper;  // Correct import
import com.example.pizzaorderingapp.R;
import com.example.pizzaorderingapp.Utils.MailSender;
import com.example.pizzaorderingapp.Util.SessionManager;  // Import SessionManager

public class PaymentActivity extends AppCompatActivity {

    private TextView tvTotalAmount;
    private EditText etCardNumber, etExpiryDate, etCVV;
    private Button btnConfirmPayment;
    private DatabaseHelper dbHelper;
    private SessionManager sessionManager;
    private String loggedInUserEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        tvTotalAmount = findViewById(R.id.tvTotalAmount);
        etCardNumber = findViewById(R.id.etCardNumber);
        etExpiryDate = findViewById(R.id.etExpiryDate);
        etCVV = findViewById(R.id.etCVV);
        btnConfirmPayment = findViewById(R.id.btnConfirmPayment);
        dbHelper = new DatabaseHelper(this);
        sessionManager = new SessionManager(this);

        // Receive the total amount from the checkout screen
        Intent intent = getIntent();
        String totalAmount = intent.getStringExtra("TOTAL_AMOUNT");
        tvTotalAmount.setText("Total Amount: $" + totalAmount);

        // Retrieve logged-in user's email from SessionManager
        loggedInUserEmail = sessionManager.getEmail();

        if (loggedInUserEmail == null) {
            Toast.makeText(this, "User not logged in", Toast.LENGTH_SHORT).show();
            return;
        }

        btnConfirmPayment.setOnClickListener(v -> {
            String cardNumber = etCardNumber.getText().toString();
            String expiryDate = etExpiryDate.getText().toString();
            String cvv = etCVV.getText().toString();

            if (cardNumber.isEmpty() || expiryDate.isEmpty() || cvv.isEmpty()) {
                Toast.makeText(PaymentActivity.this, "Please fill in all the details", Toast.LENGTH_SHORT).show();
            } else {
                checkAndProcessPayment(totalAmount);
            }
        });
    }

    private void checkAndProcessPayment(String totalAmount) {
        // Ensure totalAmount is not null or empty
        if (totalAmount == null || totalAmount.isEmpty()) {
            Toast.makeText(this, "Total amount is missing", Toast.LENGTH_SHORT).show();
            return;
        }

        String currentAddress = dbHelper.getDeliveryAddress(loggedInUserEmail);

        if (currentAddress == null || currentAddress.isEmpty()) {
            showAddressInputDialog(totalAmount);
        } else {
            processPayment(totalAmount);
        }
    }

    private void showAddressInputDialog(String totalAmount) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Enter Delivery Address");

        final EditText input = new EditText(this);
        input.setHint("Delivery Address");
        builder.setView(input);

        builder.setPositiveButton("Submit", (dialog, which) -> {
            String newAddress = input.getText().toString().trim();
            if (newAddress.isEmpty()) {
                Toast.makeText(PaymentActivity.this, "Address cannot be empty", Toast.LENGTH_SHORT).show();
            } else {
                dbHelper.updateDeliveryAddress(loggedInUserEmail, newAddress);
                processPayment(totalAmount);
            }
        });

        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());

        builder.show();
    }

    private void processPayment(String totalAmount) {
        // Implement payment processing logic here
        // For now, we'll just display a success message
        Toast.makeText(PaymentActivity.this, "Payment Successful", Toast.LENGTH_SHORT).show();

        // Send confirmation email
        sendConfirmationEmail(loggedInUserEmail, totalAmount);  // Use logged-in user's email

        // Navigate to order confirmation screen
        Intent confirmationIntent = new Intent(PaymentActivity.this, OrderConfirmationActivity.class);
        startActivity(confirmationIntent);
        finish();
    }

    private void sendConfirmationEmail(String email, String totalAmount) {
        if (email == null || totalAmount == null) {
            Toast.makeText(this, "Email or total amount is missing", Toast.LENGTH_SHORT).show();
            return;
        }

        String subject = "Payment Confirmation";
        String message = "Your payment of $" + totalAmount + " was successful. Thank you for your order!";
        MailSender mailSender = new MailSender(email, subject, message);
        mailSender.execute();
    }
}
