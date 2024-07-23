package com.example.pizzaorderingapp.Activity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.pizzaorderingapp.Adapters.AdminDeliveryOrderAdapter;
import com.example.pizzaorderingapp.Helper.DatabaseHelper;
import com.example.pizzaorderingapp.Model.Order;
import com.example.pizzaorderingapp.R;
import com.example.pizzaorderingapp.Utils.MailSender;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class DeliveringOrdersActivity extends AppCompatActivity implements AdminDeliveryOrderAdapter.OrderActionListener {

    private static final String TAG = "DeliveringOrdersActivity";
    private RecyclerView recyclerView;
    private AdminDeliveryOrderAdapter orderAdapter;
    private ArrayList<Order> orderList;
    private DatabaseHelper dbHelper;
    private ExecutorService emailExecutor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delivering_orders);

        recyclerView = findViewById(R.id.recyclerViewDeliveringOrders);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        dbHelper = new DatabaseHelper(this);

        // Initialize the ExecutorService for email sending
        emailExecutor = Executors.newSingleThreadExecutor();

        // Initialize the adapter and load orders
        orderAdapter = new AdminDeliveryOrderAdapter(this, new ArrayList<>(), this);
        recyclerView.setAdapter(orderAdapter);

        loadOrders(); // Load orders initially
    }

    private void loadOrders() {
        orderList = dbHelper.getOrdersByStatus("Delivering");
        orderAdapter.updateOrders(orderList); // Update the adapter with the new list
    }

    @Override
    public void onCompleteClick(final int orderId) {
        // Show confirmation dialog before completing
        new AlertDialog.Builder(this)
                .setTitle("Confirm Completion")
                .setMessage("Are you sure you want to mark this order as completed?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // Handle complete action
                        dbHelper.updateOrderStatus(orderId, "Completed");
                        loadOrders(); // Refresh the order list
                        Toast.makeText(DeliveringOrdersActivity.this, "Order marked as completed", Toast.LENGTH_SHORT).show();

                        // Send completion email
                        sendCompletionEmail(orderId);
                    }
                })
                .setNegativeButton("No", null)
                .show();
    }

    @Override
    public void onCancelClick(final int orderId) {
        // Show confirmation dialog before canceling
        new AlertDialog.Builder(this)
                .setTitle("Confirm Cancellation")
                .setMessage("Are you sure you want to cancel this order?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // Handle cancel action
                        dbHelper.updateOrderStatus(orderId, "Canceled");
                        loadOrders(); // Refresh the order list
                        Toast.makeText(DeliveringOrdersActivity.this, "Order canceled", Toast.LENGTH_SHORT).show();

                        // Send cancellation email
                        sendCancellationEmail(orderId);
                    }
                })
                .setNegativeButton("No", null)
                .show();
    }

    private void sendCompletionEmail(final int orderId) {
        emailExecutor.submit(() -> {
            try {
                // Fetch order details and email address
                Order order = dbHelper.getOrderById(orderId);
                if (order == null) {
                    throw new Exception("Order not found");
                }
                String userEmail = order.getUserEmail();
                if (userEmail == null || userEmail.isEmpty()) {
                    throw new Exception("User email is not available");
                }

                String subject = "Order Completion";
                String message = "Your order with Order Code: " + orderId + " has been marked as completed. Thank you for your order!";

                // Log before sending
                Log.d(TAG, "Sending completion email to: " + userEmail);
                Log.d(TAG, "Subject: " + subject);
                Log.d(TAG, "Message: " + message);

                // Send email
                MailSender mailSender = new MailSender(userEmail, subject, message);
                mailSender.execute(); // Assume this method sends an email

            } catch (Exception e) {
                // Log detailed error
                Log.e(TAG, "Failed to send completion email for Order ID: " + orderId, e);
            }
        });
    }

    private void sendCancellationEmail(final int orderId) {
        emailExecutor.submit(() -> {
            try {
                // Fetch order details and email address
                Order order = dbHelper.getOrderById(orderId);
                if (order == null) {
                    throw new Exception("Order not found");
                }
                String userEmail = order.getUserEmail();
                if (userEmail == null || userEmail.isEmpty()) {
                    throw new Exception("User email is not available");
                }

                String subject = "Order Cancellation";
                String message = "We regret to inform you that your order with Order Code: " + orderId + " has been canceled. If you have any questions, please contact us.";

                // Log before sending
                Log.d(TAG, "Sending cancellation email to: " + userEmail);
                Log.d(TAG, "Subject: " + subject);
                Log.d(TAG, "Message: " + message);

                // Send email
                MailSender mailSender = new MailSender(userEmail, subject, message);
                mailSender.execute(); // Assume this method sends an email

            } catch (Exception e) {
                // Log detailed error
                Log.e(TAG, "Failed to send cancellation email for Order ID: " + orderId, e);
            }
        });
    }
}
