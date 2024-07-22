package com.example.pizzaorderingapp.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pizzaorderingapp.Model.Order;
import com.example.pizzaorderingapp.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class AdminCompletedOrderAdapter extends RecyclerView.Adapter<AdminCompletedOrderAdapter.OrderViewHolder> {

    private Context context;
    private ArrayList<Order> orders;

    public AdminCompletedOrderAdapter(Context context, ArrayList<Order> orders) {
        this.context = context;
        this.orders = orders;
    }

    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.admin_item_order, parent, false);
        return new OrderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {
        Order order = orders.get(position);
        holder.bind(order);
    }

    @Override
    public int getItemCount() {
        return orders.size();
    }

    class OrderViewHolder extends RecyclerView.ViewHolder {

        TextView textOrderId, textOrderStatus, textTotalAmount, textOrderDate;
        LinearLayout linearLayoutButtons;

        public OrderViewHolder(@NonNull View itemView) {
            super(itemView);

            textOrderId = itemView.findViewById(R.id.text_order_id);
            textOrderStatus = itemView.findViewById(R.id.text_order_status);
            textTotalAmount = itemView.findViewById(R.id.text_total_amount);
            textOrderDate = itemView.findViewById(R.id.text_order_date);
            linearLayoutButtons = itemView.findViewById(R.id.buttonContainer);
        }

        public void bind(final Order order) {
            textOrderId.setText("Order Code: " + order.getId());
            textOrderStatus.setText("Status: " + order.getStatus());
            textTotalAmount.setText("Total Amount: $" + order.getTotalAmount());

            long timestamp = Long.parseLong(order.getDate());
            Date date = new Date(timestamp);
            SimpleDateFormat outputFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
            String dateString = outputFormat.format(date);

            textOrderDate.setText("Order Date: " + dateString);

            // Hide action buttons for completed orders
            linearLayoutButtons.setVisibility(View.GONE);
        }
    }
}
