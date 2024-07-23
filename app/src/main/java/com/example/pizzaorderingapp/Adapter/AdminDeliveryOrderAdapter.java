package com.example.pizzaorderingapp.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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

public class AdminDeliveryOrderAdapter extends RecyclerView.Adapter<AdminDeliveryOrderAdapter.OrderViewHolder> {

    private Context context;
    private ArrayList<Order> orders;
    private OrderActionListener listener;

    public AdminDeliveryOrderAdapter(Context context, ArrayList<Order> orders, OrderActionListener listener) {
        this.context = context;
        this.orders = orders;
        this.listener = listener;
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

    public interface OrderActionListener {
        void onCompleteClick(int orderId);
        void onCancelClick(int orderId);
    }

    class OrderViewHolder extends RecyclerView.ViewHolder {

        TextView textOrderId, textOrderStatus, textTotalAmount, textOrderDate;
        Button buttonCancel, buttonComplete, buttonConfirm;
        LinearLayout linearLayoutButtons;

        public OrderViewHolder(@NonNull View itemView) {
            super(itemView);

            textOrderId = itemView.findViewById(R.id.text_order_id);
            textOrderStatus = itemView.findViewById(R.id.text_order_status);
            textTotalAmount = itemView.findViewById(R.id.text_total_amount);
            textOrderDate = itemView.findViewById(R.id.text_order_date);
            buttonCancel = itemView.findViewById(R.id.buttonCancel);
            buttonConfirm = itemView.findViewById(R.id.buttonConfirm);
            buttonComplete = itemView.findViewById(R.id.buttonComplete);
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

            // Set button visibility based on order status
            if (order.getStatus().equals("Delivering")) {
                buttonCancel.setVisibility(View.VISIBLE);
                buttonComplete.setVisibility(View.VISIBLE);
            } else {
                buttonCancel.setVisibility(View.GONE);
                buttonConfirm.setVisibility(View.GONE);
            }

            // Set up button click listeners with null check
            buttonCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        listener.onCancelClick(order.getId());
                    }
                }
            });

            buttonComplete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        listener.onCompleteClick(order.getId());
                    }
                }
            });
        }
    }

    // Method to update orders and notify the adapter
    public void updateOrders(ArrayList<Order> newOrders) {
        this.orders = newOrders;
        notifyDataSetChanged(); // Notify the adapter of data changes
    }
}
