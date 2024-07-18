package com.example.pizzaorderingapp.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pizzaorderingapp.Adapter.CartListAdapter;
import com.example.pizzaorderingapp.Helper.ManagementCart;
import com.example.pizzaorderingapp.Interface.ChangeNumberItemsListener;
import com.example.pizzaorderingapp.R;

public class CartActivity extends AppCompatActivity {

    private RecyclerView.Adapter adapter;
    private RecyclerView recyclerViewList;
    private ManagementCart managementCart;
    private TextView totalFeeTxt,TaxTxt,deliveryTxt,totalTxt,emptyTxt;
    private double tax;
    private ScrollView scrollView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        managementCart=new ManagementCart(this);

        initView();
        initList();
        calculateCard();

    }

    private void initList() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        recyclerViewList.setLayoutManager(linearLayoutManager);
        adapter=new CartListAdapter(managementCart.getListCart(), this, new ChangeNumberItemsListener() {
            @Override
            public void changed() {
                calculateCard();
            }
        });

        recyclerViewList.setAdapter(adapter);

        if(managementCart.getListCart().isEmpty()){
            emptyTxt.setVisibility(View.VISIBLE);
            scrollView.setVisibility(View.GONE);
        }else{
            emptyTxt.setVisibility(View.GONE);
            scrollView.setVisibility(View.VISIBLE);
        }

    }

    private void calculateCard() {
        double percentTax=0.02;
        double delivery=10;

        tax=Math.round((managementCart.getTotalFee() * percentTax) * 100.0) / 100.0;
        double total=Math.round((managementCart.getTotalFee() + tax + delivery) * 100.0) / 100.0;
        double itemTotal=Math.round(managementCart.getTotalFee() + 100.0) / 100.0;

        totalFeeTxt.setText("$" + itemTotal);
        TaxTxt.setText("$"+tax);
        deliveryTxt.setText("$" + delivery);
        totalTxt.setText("$" + total);

    }

    private void initView() {

        totalFeeTxt=findViewById(R.id.totalFeeTxt);
        deliveryTxt=findViewById(R.id.deliveryTxt);
        TaxTxt=findViewById(R.id.TaxTxt);
        totalTxt=findViewById(R.id.totalTxt);
        recyclerViewList=findViewById(R.id.view);
        scrollView=findViewById(R.id.scrollView);
        emptyTxt=findViewById(R.id.emptyTxt);


    }

    public void onCheckout(View view){

        // Navigate to the PaymentActivity and pass the total amount
        Intent paymentIntent = new Intent(this, PaymentActivity.class);
        paymentIntent.putExtra("TOTAL_AMOUNT", totalTxt.getText());
        startActivity(paymentIntent);
    }

}
