package com.example.pizzaorderingapp.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pizzaorderingapp.Model.PromoCode;
import com.example.pizzaorderingapp.R;

import java.util.List;

public class PromoCodeAdapter extends RecyclerView.Adapter<PromoCodeAdapter.ViewHolder> {

    private List<PromoCode> promoCodeList;

    public PromoCodeAdapter(List<PromoCode> promoCodeList) {
        this.promoCodeList = promoCodeList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_promo_code, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        PromoCode promoCode = promoCodeList.get(position);
        holder.textViewPromoCode.setText(promoCode.getPromoCode());
        holder.textViewDiscountPercentage.setText(String.valueOf(promoCode.getDiscountPercentage()) + "%");
        holder.textViewExpiryDate.setText(promoCode.getExpiryDate());
    }

    @Override
    public int getItemCount() {
        return promoCodeList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textViewPromoCode;
        TextView textViewDiscountPercentage;
        TextView textViewExpiryDate;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewPromoCode = itemView.findViewById(R.id.text_view_promo_code);
            textViewDiscountPercentage = itemView.findViewById(R.id.text_view_discount_percentage);
            textViewExpiryDate = itemView.findViewById(R.id.text_view_expiry_date);
        }
    }
}
