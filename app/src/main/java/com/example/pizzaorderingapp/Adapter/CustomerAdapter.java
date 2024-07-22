package com.example.pizzaorderingapp.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pizzaorderingapp.Helper.DatabaseHelper;
import com.example.pizzaorderingapp.Model.Customer;
import com.example.pizzaorderingapp.Model.Order;
import com.example.pizzaorderingapp.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class CustomerAdapter extends RecyclerView.Adapter<CustomerAdapter.CustomerViewHolder> {

    private Context context;
    private ArrayList<Customer> customerList;
    private DatabaseHelper databaseHelper;
    private OnItemClickListener onItemClickListener;

    public interface OnItemClickListener {
        void onItemClick(Customer customer);
    }

    public CustomerAdapter(Context context, ArrayList<Customer> customerList, OnItemClickListener onItemClickListener) {
        this.context = context;
        this.customerList = customerList;
        this.databaseHelper = new DatabaseHelper(context);
        this.onItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public CustomerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_customer, parent, false);
        return new CustomerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CustomerViewHolder holder, int position) {
        Customer customer = customerList.get(position);
        holder.tvName.setText(customer.getName());
        holder.tvEmail.setText(customer.getEmail());

        // Load the customer image
        Picasso.get().load(customer.getImageUrl()).into(holder.ivCustomerImage);

        holder.itemView.setOnClickListener(v -> onItemClickListener.onItemClick(customer));
    }

    @Override
    public int getItemCount() {
        return customerList.size();
    }

    public static class CustomerViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvEmail;
        ImageView ivCustomerImage;

        public CustomerViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvName);
            tvEmail = itemView.findViewById(R.id.tvEmail);
            ivCustomerImage = itemView.findViewById(R.id.ivCustomerImage);
        }
    }
}
