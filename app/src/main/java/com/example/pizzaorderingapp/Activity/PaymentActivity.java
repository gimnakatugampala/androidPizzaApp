package com.example.pizzaorderingapp.Activity;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.pizzaorderingapp.Domain.FoodDomain;
import com.example.pizzaorderingapp.Helper.DatabaseHelper;
import com.example.pizzaorderingapp.Helper.ManagementCart;
import com.example.pizzaorderingapp.Interface.ChangeNumberItemsListener;
import com.example.pizzaorderingapp.R;
import com.example.pizzaorderingapp.Utils.MailSender;
import com.example.pizzaorderingapp.Util.SessionManager;

import java.util.ArrayList;

public class PaymentActivity extends AppCompatActivity {

    private TextView tvTotalAmount;
    private EditText etCardNumber, etExpiryDate, etCVV;
    private Button btnConfirmPayment;
    private DatabaseHelper dbHelper;
    private SessionManager sessionManager;
    private String loggedInUserEmail;
    private String customerFirstName;
    private String customerLastName;

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

        // Retrieve logged-in user's email and names from SessionManager
        loggedInUserEmail = sessionManager.getEmail();
        customerFirstName = sessionManager.getFirstName(); // Get first name from session
        customerLastName = sessionManager.getLastName();   // Get last name from session

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
        // Generate order details and store in database
        int orderId = createOrder(totalAmount);
        storeOrderItems(orderId);
        storeCustomerDetails();

        // Implement payment processing logic here
        // For now, we'll just display a success message
        Toast.makeText(PaymentActivity.this, "Payment Successful", Toast.LENGTH_SHORT).show();

        // Send confirmation email
        sendConfirmationEmail(loggedInUserEmail, totalAmount);

        // Navigate to order confirmation screen
        Intent confirmationIntent = new Intent(PaymentActivity.this, OrderConfirmationActivity.class);
        startActivity(confirmationIntent);
        finish();
    }

    private int createOrder(String totalAmount) {
        // Create an order and store it in the database
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_USER_EMAIL_ORDERS, loggedInUserEmail);
        values.put(DatabaseHelper.COLUMN_ORDER_STATUS, "Pending");
        values.put(DatabaseHelper.COLUMN_TOTAL_AMOUNT, totalAmount);
        values.put(DatabaseHelper.COLUMN_DATE, System.currentTimeMillis());

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        long orderId = db.insert(DatabaseHelper.TABLE_ORDERS, null, values);
        db.close();
        return (int) orderId;
    }

    private void storeOrderItems(int orderId) {
        // Retrieve cart items from ManagementCart
        ManagementCart managementCart = new ManagementCart(this);
        ArrayList<FoodDomain> cartItems = managementCart.getListCart();

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        for (FoodDomain item : cartItems) {
            ContentValues values = new ContentValues();
            values.put(DatabaseHelper.COLUMN_ORDER_ID, orderId);
            values.put(DatabaseHelper.COLUMN_MENU_ITEM_ID, item.getTitle()); // Use appropriate MenuItemID if needed
            values.put(DatabaseHelper.COLUMN_QUANTITY, item.getNumberInCart());
            values.put(DatabaseHelper.COLUMN_PRICE, item.getFee()); // Base price

            db.insert(DatabaseHelper.TABLE_ORDER_ITEMS, null, values);
        }
        db.close();

        // Clear the cart after processing
        managementCart.clearCart(new ChangeNumberItemsListener() {
            @Override
            public void changed() {
                // Cart has been cleared
                // You can add any additional logic here if needed
            }
        });
    }

    private void storeCustomerDetails() {
        // Create or update customer details in the database
        String customerName = customerFirstName + " " + customerLastName; // Use names from session
        String customerPhone = ""; // Retrieve from user input or session
        String customerAddress = dbHelper.getDeliveryAddress(loggedInUserEmail);

        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_CUSTOMER_EMAIL, loggedInUserEmail);
        values.put(DatabaseHelper.COLUMN_CUSTOMER_NAME, customerName);
        values.put(DatabaseHelper.COLUMN_CUSTOMER_PHONE, customerPhone);
        values.put(DatabaseHelper.COLUMN_CUSTOMER_ADDRESS, customerAddress);

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        long result = db.insertWithOnConflict(DatabaseHelper.TABLE_CUSTOMERS, null, values, SQLiteDatabase.CONFLICT_IGNORE);
        if (result == -1) {
            // Customer already exists, update details if needed
            db.update(DatabaseHelper.TABLE_CUSTOMERS, values,
                    DatabaseHelper.COLUMN_CUSTOMER_EMAIL + " = ?", new String[]{loggedInUserEmail});
        }
        db.close();
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
