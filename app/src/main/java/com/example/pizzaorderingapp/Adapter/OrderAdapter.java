package com.example.pizzaorderingapp.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.pizzaorderingapp.Model.Order;
import com.example.pizzaorderingapp.R;
import com.example.pizzaorderingapp.Helper.DatabaseHelper;
import androidx.appcompat.app.AlertDialog;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Locale;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.OrderViewHolder> {

    private ArrayList<Order> orderList;
    private DatabaseHelper dbHelper;
    private Context context;

    public OrderAdapter(ArrayList<Order> orderList, DatabaseHelper dbHelper, Context context) {
        this.orderList = orderList;
        this.dbHelper = dbHelper;
        this.context = context;
        sortOrdersByDateDescending();
    }

    private void sortOrdersByDateDescending() {
        Collections.sort(orderList, new Comparator<Order>() {
            @Override
            public int compare(Order o1, Order o2) {
                long timestamp1 = Long.parseLong(o1.getDate());
                long timestamp2 = Long.parseLong(o2.getDate());
                return Long.compare(timestamp2, timestamp1); // Descending order
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
        long timestamp = Long.parseLong(order.getDate());
        Date date = new Date(timestamp);
        SimpleDateFormat outputFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
        String dateString = outputFormat.format(date);
        holder.tvOrderDate.setText("Date: " + dateString);

        // Show/Hide buttons based on order status
        if ("Pending".equals(order.getStatus())) {
            holder.buttonCancel.setVisibility(View.VISIBLE);
            holder.buttonCancel.setOnClickListener(v -> {
                // Show confirmation dialog before canceling the order
                new AlertDialog.Builder(context)
                        .setTitle("Cancel Order")
                        .setMessage("Are you sure you want to cancel this order?")
                        .setPositiveButton("Yes", (dialog, which) -> cancelOrder(order))
                        .setNegativeButton("No", null)
                        .show();
            });
        } else {
            holder.buttonCancel.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }

    private void cancelOrder(Order order) {
        // Update the order status in the database
        dbHelper.updateOrderStatus(order.getId(), "Canceled");

        // Remove the order from the list and notify the adapter
        orderList.remove(order);
        notifyDataSetChanged();

        // Notify the user about the successful cancellation
        Toast.makeText(context, "Order canceled successfully", Toast.LENGTH_SHORT).show();
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
