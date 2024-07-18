package com.example.pizzaorderingapp.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.pizzaorderingapp.Domain.FoodDomain;
import com.example.pizzaorderingapp.Helper.ManagementCart;
import com.example.pizzaorderingapp.R;

public class ShowDetailActivity extends AppCompatActivity {

    private TextView addToCartBtn;
    private TextView titleTxt, feeTxt, descriptionTxt, numberOrderTxt, totalPriceTxt, starTxt, caloriesTxt, timeTxt;
    private ImageView plusBtn, minusBtn, picFood;
    private FoodDomain object;
    private  int numberObject=1;
    private ManagementCart managementCart;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_detail);

        managementCart = new ManagementCart(this);

        initView();
        getBundle();

    }

    private void getBundle() {

        object=(FoodDomain)getIntent().getSerializableExtra("object");

        int drawableResourceId=this.getResources().getIdentifier(object.getPic(),"drawable",this.getPackageName());
        Glide.with(this)
                .load(drawableResourceId)
                .into(picFood);

        titleTxt.setText(object.getTitle());
        feeTxt.setText("$"+object.getFee());
        descriptionTxt.setText(object.getDescription());
        numberOrderTxt.setText(String.valueOf(numberObject));
        caloriesTxt.setText(object.getCalories() + "calories");
        starTxt.setText(object.getStar()+"");
        timeTxt.setText(object.getTime()+"mins");
        totalPriceTxt.setText( "$" + numberObject * object.getFee());

        plusBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                numberObject=numberObject + 1;
                numberOrderTxt.setText(String.valueOf(numberObject));
                totalPriceTxt.setText( "$" + numberObject * object.getFee());
            }
        });

        minusBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(numberObject > 1){
                    numberObject=numberObject - 1;
                }
                numberOrderTxt.setText(String.valueOf(numberObject));
                totalPriceTxt.setText((String.valueOf(numberObject * object.getFee())));
            }
        });


        addToCartBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                object.setNumberInCart(numberObject);
                managementCart.insertFood(object);
            }
        });

    }

    private void initView() {
        addToCartBtn=findViewById(R.id.addToCartBtn);
        titleTxt=findViewById(R.id.titleTxt);
        feeTxt=findViewById(R.id.priceTxt);
        descriptionTxt=findViewById(R.id.descriptionTxt);
        numberOrderTxt=findViewById(R.id.numberItemTxt);
        plusBtn=findViewById(R.id.plusCardBtn);
        minusBtn=findViewById(R.id.minusCardBtn);
        picFood=findViewById(R.id.foodPic);

        totalPriceTxt=findViewById(R.id.totalPriceTxt);
        starTxt=findViewById(R.id.starTxt);
        caloriesTxt=findViewById(R.id.caloriesTxt);
        timeTxt=findViewById(R.id.TimeTxt);



    }
}