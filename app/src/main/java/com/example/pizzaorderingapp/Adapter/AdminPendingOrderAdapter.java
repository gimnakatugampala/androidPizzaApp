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
import com.example.pizzaorderingapp.Utils.MailSender;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AdminPendingOrderAdapter extends RecyclerView.Adapter<AdminPendingOrderAdapter.OrderViewHolder> {

    private Context context;
    private ArrayList<Order> orders;
    private OrderActionListener listener;
    private ExecutorService emailExecutor;

    public AdminPendingOrderAdapter(Context context, ArrayList<Order> orders, OrderActionListener listener) {
        this.context = context;
        this.orders = orders;
        this.listener = listener;
        // Initialize the ExecutorService for email sending
        this.emailExecutor = Executors.newSingleThreadExecutor();
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

    public void updateOrders(ArrayList<Order> newOrders) {
        this.orders = newOrders;
        notifyDataSetChanged();
    }

    public interface OrderActionListener {
        void onCancelClick(int orderId);
        void onConfirmClick(int orderId);
    }

    class OrderViewHolder extends RecyclerView.ViewHolder {

        TextView textOrderId, textOrderStatus, textTotalAmount, textOrderDate;
        Button buttonCancel, buttonConfirm;
        LinearLayout linearLayoutButtons;

        public OrderViewHolder(@NonNull View itemView) {
            super(itemView);

            textOrderId = itemView.findViewById(R.id.text_order_id);
            textOrderStatus = itemView.findViewById(R.id.text_order_status);
            textTotalAmount = itemView.findViewById(R.id.text_total_amount);
            textOrderDate = itemView.findViewById(R.id.text_order_date);
            buttonCancel = itemView.findViewById(R.id.buttonCancel);
            buttonConfirm = itemView.findViewById(R.id.buttonConfirm);
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

            if (order.getStatus().equals("Pending")) {
                buttonCancel.setVisibility(View.VISIBLE);
                buttonConfirm.setVisibility(View.VISIBLE);
            } else if (order.getStatus().equals("Delivering")) {
                buttonCancel.setVisibility(View.VISIBLE);
                buttonConfirm.setVisibility(View.GONE);
            } else if (order.getStatus().equals("Canceled") || order.getStatus().equals("Completed")) {
                buttonCancel.setVisibility(View.GONE);
                buttonConfirm.setVisibility(View.GONE);
            }

            buttonCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onCancelClick(order.getId());
                }
            });

            buttonConfirm.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onConfirmClick(order.getId());
                    sendConfirmationEmail(order.getUserEmail(), order.getId());
                }
            });
        }

        private void sendConfirmationEmail(final String userEmail, final int orderId) {
            emailExecutor.submit(() -> {
                String subject = "Order Confirmation";
                String message = "Your order with Order Code: " + orderId + " has been confirmed for delivery. Thank you for your order!";
                // Use your email sending implementation here
                try {
                    // This is a placeholder for your email sending logic
                    MailSender mailSender = new MailSender(userEmail, subject, message);
                    mailSender.execute(); // Assume this method sends an email
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        }
    }
}
