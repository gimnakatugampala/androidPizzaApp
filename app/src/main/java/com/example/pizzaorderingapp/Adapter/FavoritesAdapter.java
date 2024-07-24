package com.example.pizzaorderingapp.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pizzaorderingapp.Model.Order;
import com.example.pizzaorderingapp.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class FavoritesAdapter extends RecyclerView.Adapter<FavoritesAdapter.FavoritesViewHolder> {
    private List<Order> favoriteOrders;
    private Context context;

    public FavoritesAdapter(List<Order> favoriteOrders, Context context) {
        this.favoriteOrders = favoriteOrders;
        this.context = context;
    }

    @NonNull
    @Override
    public FavoritesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_favorite_order, parent, false);
        return new FavoritesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FavoritesViewHolder holder, int position) {
        Order order = favoriteOrders.get(position);
        holder.bind(order);
    }

    @Override
    public int getItemCount() {
        return favoriteOrders.size();
    }

    class FavoritesViewHolder extends RecyclerView.ViewHolder {
        private TextView tvOrderId, tvOrderStatus, tvTotalAmount, tvOrderDate;

        public FavoritesViewHolder(@NonNull View itemView) {
            super(itemView);
            tvOrderId = itemView.findViewById(R.id.tvOrderId);
            tvOrderStatus = itemView.findViewById(R.id.tvOrderStatus);
            tvTotalAmount = itemView.findViewById(R.id.tvTotalAmount);
            tvOrderDate = itemView.findViewById(R.id.tvOrderDate);
        }

        public void bind(Order order) {
            // Use the Order model's methods to set text in the TextViews
            tvOrderId.setText("Order Code: " + String.valueOf(order.getId()));
            tvOrderStatus.setText("Status: " + order.getStatus());
            tvTotalAmount.setText("Total: " + order.getTotalAmount()); // Assuming totalAmount is a formatted String
            tvOrderDate.setText(order.getDate());

            try {
                long timestamp = Long.parseLong(order.getDate());
                Date date = new Date(timestamp);
                SimpleDateFormat outputFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
                String dateString = outputFormat.format(date);
                tvOrderDate.setText("Date: " + dateString);
            } catch (NumberFormatException e) {
                tvOrderDate.setText("Date: Invalid Date");
            }

        }
    }
}
