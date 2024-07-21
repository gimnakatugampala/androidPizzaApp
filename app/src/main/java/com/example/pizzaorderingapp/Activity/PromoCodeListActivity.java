package com.example.pizzaorderingapp.Activity;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pizzaorderingapp.Adapter.PromoCodeAdapter;
import com.example.pizzaorderingapp.Model.PromoCode;
import com.example.pizzaorderingapp.R;
import com.example.pizzaorderingapp.Repository.PromoCodeRepository;

import java.util.List;

public class PromoCodeListActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private PromoCodeAdapter promoCodeAdapter;
    private PromoCodeRepository promoCodeRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_promo_code_list);

        recyclerView = findViewById(R.id.recycler_view_promo_codes);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        promoCodeRepository = new PromoCodeRepository(this);
        List<PromoCode> promoCodeList = promoCodeRepository.getAllPromoCodes(); // Assuming this method is added

        promoCodeAdapter = new PromoCodeAdapter(promoCodeList);
        recyclerView.setAdapter(promoCodeAdapter);
    }
}
