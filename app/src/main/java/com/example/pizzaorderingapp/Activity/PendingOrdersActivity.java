package com.example.pizzaorderingapp.Activity;

import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.app.AlertDialog;

import com.example.pizzaorderingapp.Adapter.OrderItemsAdapter;
import com.example.pizzaorderingapp.Helper.DatabaseHelper;
import com.example.pizzaorderingapp.Model.Order;
import com.example.pizzaorderingapp.Model.OrderItem;
import com.example.pizzaorderingapp.R;
import com.example.pizzaorderingapp.Utils.MailSender;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class PendingOrdersActivity extends AppCompatActivity implements com.example.pizzaorderingapp.Adapters.AdminPendingOrderAdapter.OrderActionListener {

    private static final String TAG = "PendingOrdersActivity";

    private RecyclerView recyclerView;
    private com.example.pizzaorderingapp.Adapters.AdminPendingOrderAdapter orderAdapter;
    private ArrayList<Order> orderList;
    private DatabaseHelper dbHelper;
    private ExecutorService emailExecutor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pending_orders);

        recyclerView = findViewById(R.id.recyclerViewPendingOrders);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        dbHelper = new DatabaseHelper(this);

        // Initialize the ExecutorService for email sending
        emailExecutor = Executors.newSingleThreadExecutor();

        // Load orders with "Pending" status
        loadOrders();

        // Initialize the adapter
        orderAdapter = new com.example.pizzaorderingapp.Adapters.AdminPendingOrderAdapter(this, orderList, this);
        recyclerView.setAdapter(orderAdapter);
    }

    private void loadOrders() {
        orderList = dbHelper.getOrdersByStatus("Pending");
        if (orderAdapter != null) {
            orderAdapter.updateOrders(orderList); // Update adapter with new data
        }
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
                        Toast.makeText(PendingOrdersActivity.this, "Order has been canceled.", Toast.LENGTH_SHORT).show();

                        // Send cancellation email
                        sendCancellationEmail(orderId);
                    }
                })
                .setNegativeButton("No", null)
                .show();
    }

    @Override
    public void onConfirmClick(final int orderId) {
        // Show confirmation dialog before confirming
        new AlertDialog.Builder(this)
                .setTitle("Confirm Delivery")
                .setMessage("Are you sure you want to confirm this order?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // Handle confirm action
                        dbHelper.updateOrderStatus(orderId, "Delivering");
                        loadOrders(); // Refresh the order list
                        Toast.makeText(PendingOrdersActivity.this, "Order has been confirmed for delivery.", Toast.LENGTH_SHORT).show();

                        // Send confirmation email
                        sendConfirmationEmail(orderId);
                    }
                })
                .setNegativeButton("No", null)
                .show();
    }

    @Override
    public void onOrderItemClick(final int orderId) {
        Log.d(TAG, "Order item clicked: " + orderId); // Log to check if the method is triggered
        showOrderItemsDialog(orderId);
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

                // Log success
                Log.d(TAG, "Cancellation email sent to " + userEmail);

            } catch (Exception e) {
                // Log detailed error
                Log.e(TAG, "Failed to send cancellation email for Order ID: " + orderId, e);
            }
        });
    }

    private void sendConfirmationEmail(final int orderId) {
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

                String subject = "Order Confirmation";
                String message = "Your order with Order Code: " + orderId + " has been confirmed for delivery. Thank you for your order!";

                // Log before sending
                Log.d(TAG, "Sending confirmation email to: " + userEmail);
                Log.d(TAG, "Subject: " + subject);
                Log.d(TAG, "Message: " + message);

                // Send email
                MailSender mailSender = new MailSender(userEmail, subject, message);
                mailSender.execute(); // Assume this method sends an email

            } catch (Exception e) {
                // Log detailed error
                Log.e(TAG, "Failed to send confirmation email for Order ID: " + orderId, e);
            }
        });
    }

    private void showOrderItemsDialog(int orderId) {
        List<OrderItem> orderItems = fetchOrderItems(orderId);
        Log.d(TAG, "Fetched order items: " + orderItems); // Log to verify order items are fetched

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_order_items, null);
        builder.setView(dialogView);

        RecyclerView rvOrderItems = dialogView.findViewById(R.id.rvOrderItems);
        rvOrderItems.setLayoutManager(new LinearLayoutManager(this));
        OrderItemsAdapter adapter = new OrderItemsAdapter(orderItems);
        rvOrderItems.setAdapter(adapter);

        builder.setPositiveButton("OK", (dialog, which) -> dialog.dismiss());

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private List<OrderItem> fetchOrderItems(int orderId) {
        List<OrderItem> orderItems = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT oi.quantity, oi.price, mi.name FROM order_items oi " +
                "JOIN menu_items mi ON oi.menu_item_id = mi._id " +
                "WHERE oi.order_id = ?", new String[]{String.valueOf(orderId)});

        if (cursor.moveToFirst()) {
            do {
                int quantity = cursor.getInt(cursor.getColumnIndexOrThrow("quantity"));
                double price = cursor.getDouble(cursor.getColumnIndexOrThrow("price"));
                String name = cursor.getString(cursor.getColumnIndexOrThrow("name"));

                orderItems.add(new OrderItem(name, quantity, price));
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();

        return orderItems;
    }
}
