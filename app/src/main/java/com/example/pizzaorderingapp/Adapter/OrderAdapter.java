package com.example.pizzaorderingapp.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;
import com.example.pizzaorderingapp.Model.Order;
import com.example.pizzaorderingapp.R;
import com.example.pizzaorderingapp.Helper.DatabaseHelper;
import com.example.pizzaorderingapp.Utils.MailSender;
import com.example.pizzaorderingapp.Activity.MyOrdersActivity;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Locale;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.OrderViewHolder> {

    private ArrayList<Order> orderList;
    private final DatabaseHelper dbHelper;
    private final Context context;

    public OrderAdapter(ArrayList<Order> orderList, DatabaseHelper dbHelper, Context context) {
        this.orderList = new ArrayList<>(orderList); // Create a new list to avoid mutating the original one
        this.dbHelper = dbHelper;
        this.context = context;
        sortOrdersByDateDescending();
    }

    private void sortOrdersByDateDescending() {
        Collections.sort(orderList, (o1, o2) -> {
            try {
                long timestamp1 = Long.parseLong(o1.getDate());
                long timestamp2 = Long.parseLong(o2.getDate());
                return Long.compare(timestamp2, timestamp1); // Descending order
            } catch (NumberFormatException e) {
                e.printStackTrace(); // Log the error if date parsing fails
                return 0;
            }
        });
    }

    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_order, parent, false);
        return new OrderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {
        Order order = orderList.get(position);
        holder.tvOrderId.setText("Order Code: " + order.getId());
        holder.tvOrderStatus.setText("Status: " + order.getStatus());
        holder.tvTotalAmount.setText("Total: " + order.getTotalAmount());

        // Convert the timestamp to a Date object and format it
        try {
            long timestamp = Long.parseLong(order.getDate());
            Date date = new Date(timestamp);
            SimpleDateFormat outputFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
            String dateString = outputFormat.format(date);
            holder.tvOrderDate.setText("Date: " + dateString);
        } catch (NumberFormatException e) {
            holder.tvOrderDate.setText("Date: Invalid Date");
        }

        // Show/Hide buttons based on order status
        if ("Pending".equals(order.getStatus())) {
            holder.buttonCancel.setVisibility(View.VISIBLE);
            holder.buttonCancel.setOnClickListener(v -> showCancelConfirmationDialog(order));
        } else {
            holder.buttonCancel.setVisibility(View.GONE);
        }

        // Handle item clicks
        holder.itemView.setOnClickListener(v -> {
            if (context instanceof MyOrdersActivity) {
                ((MyOrdersActivity) context).showOrderItemsDialog(order.getId());
            }
        });
    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }

    private void showCancelConfirmationDialog(Order order) {
        new AlertDialog.Builder(context)
                .setTitle("Cancel Order")
                .setMessage("Are you sure you want to cancel this order?")
                .setPositiveButton("Yes", (dialog, which) -> cancelOrder(order))
                .setNegativeButton("No", null)
                .show();
    }

    private void cancelOrder(Order order) {
        // Update the order status in the database
        boolean isUpdated = dbHelper.updateOrderStatus(order.getId(), "Canceled");

        if (isUpdated) {
            // Find the position of the canceled order in the list
            int position = orderList.indexOf(order);
            if (position != -1) {
                // Update the order in the list
                Order updatedOrder = new Order(
                        order.getId(),
                        order.getUserEmail(),
                        "Canceled", // Update status
                        order.getTotalAmount(),
                        order.getDate(),
                        false // Mark as not completed
                );
                orderList.set(position, updatedOrder); // Replace the order
                notifyItemChanged(position); // Notify the adapter that the item has changed
            }

            // Send email notification
            String subject = "Order Cancellation";
            String message = "Dear Customer,\n\nYour order with Code " + order.getId() + " has been successfully canceled.\n\nThank you for using our service.";
            new MailSender(order.getUserEmail(), subject, message).execute();

            // Notify the user about the successful cancellation
            Toast.makeText(context, "Order canceled successfully", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "Failed to cancel order. Please try again.", Toast.LENGTH_SHORT).show();
        }
    }

    static class OrderViewHolder extends RecyclerView.ViewHolder {
        TextView tvOrderId, tvOrderStatus, tvTotalAmount, tvOrderDate;
        Button buttonCancel;

        public OrderViewHolder(@NonNull View itemView) {
            super(itemView);
            tvOrderId = itemView.findViewById(R.id.tvOrderId);
            tvOrderStatus = itemView.findViewById(R.id.tvOrderStatus);
            tvTotalAmount = itemView.findViewById(R.id.tvTotalAmount);
            tvOrderDate = itemView.findViewById(R.id.tvOrderDate);
            buttonCancel = itemView.findViewById(R.id.buttonCancel);
        }
    }
}
