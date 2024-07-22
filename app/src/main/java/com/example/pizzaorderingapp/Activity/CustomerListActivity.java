package com.example.pizzaorderingapp.Activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pizzaorderingapp.Adapter.CustomerAdapter;
import com.example.pizzaorderingapp.Helper.DatabaseHelper;
import com.example.pizzaorderingapp.Model.Customer;
import com.example.pizzaorderingapp.Model.Order;
import com.example.pizzaorderingapp.R;

import java.util.ArrayList;

public class CustomerListActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private CustomerAdapter customerAdapter;
    private ArrayList<Customer> customerList;
    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_list);

        recyclerView = findViewById(R.id.recyclerView);
        databaseHelper = new DatabaseHelper(this);

        customerList = databaseHelper.getAllCustomers();

        if (customerList.isEmpty()) {
            Toast.makeText(this, "No customers found", Toast.LENGTH_SHORT).show();
        } else {
            customerAdapter = new CustomerAdapter(this, customerList, this::showCustomerOrdersDialog);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            recyclerView.setAdapter(customerAdapter);
        }
    }

    private void showCustomerOrdersDialog(Customer customer) {
        ArrayList<Order> orders = databaseHelper.getOrdersByCustomerEmail(customer.getEmail());

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Orders for " + customer.getName());

        if (orders.isEmpty()) {
            builder.setMessage("No orders found for this customer.");
        } else {
            StringBuilder details = new StringBuilder();
            for (Order order : orders) {
                details.append("Order ID: ").append(order.getId()).append("\n")
                        .append("Status: ").append(order.getStatus()).append("\n")
                        .append("Total Amount: ").append(order.getTotalAmount()).append("\n")
                        .append("Date: ").append(order.getDate()).append("\n")
                        .append("Completed: ").append(order.isCompleted() ? "Yes" : "No").append("\n\n");
            }
            builder.setMessage(details.toString());
        }

        builder.setPositiveButton("OK", (dialog, which) -> dialog.dismiss());

        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
