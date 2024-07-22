package com.example.pizzaorderingapp.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pizzaorderingapp.Model.Order;
import com.example.pizzaorderingapp.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Locale;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.OrderViewHolder> {

    private ArrayList<Order> orderList;

    public OrderAdapter(ArrayList<Order> orderList) {
        this.orderList = orderList;
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
    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }

    static class OrderViewHolder extends RecyclerView.ViewHolder {
        TextView tvOrderId, tvOrderStatus, tvTotalAmount, tvOrderDate;

        public OrderViewHolder(@NonNull View itemView) {
            super(itemView);
            tvOrderId = itemView.findViewById(R.id.tvOrderId);
            tvOrderStatus = itemView.findViewById(R.id.tvOrderStatus);
            tvTotalAmount = itemView.findViewById(R.id.tvTotalAmount);
            tvOrderDate = itemView.findViewById(R.id.tvOrderDate);
        }
    }
}
