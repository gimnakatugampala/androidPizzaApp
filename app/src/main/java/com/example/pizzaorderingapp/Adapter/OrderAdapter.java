package com.example.pizzaorderingapp.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.pizzaorderingapp.Models.Order;
import com.example.pizzaorderingapp.R;

import java.util.List;

public class OrderAdapter extends BaseAdapter {

    private Context context;
    private List<Order> orderList;

    public OrderAdapter(Context context, List<Order> orderList) {
        this.context = context;
        this.orderList = orderList;
    }

    @Override
    public int getCount() {
        return orderList.size();
    }

    @Override
    public Object getItem(int position) {
        return orderList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_order, parent, false);
        }

        TextView tvOrderDetails = convertView.findViewById(R.id.tvOrderDetails);
        TextView tvTotalAmount = convertView.findViewById(R.id.tvTotalAmount);
        TextView tvOrderDate = convertView.findViewById(R.id.tvOrderDate);

        Order order = orderList.get(position);
        tvOrderDetails.setText(order.getOrderDetails());
        tvTotalAmount.setText(order.getTotalAmount());
        tvOrderDate.setText(order.getOrderDate());

        return convertView;
    }
}
