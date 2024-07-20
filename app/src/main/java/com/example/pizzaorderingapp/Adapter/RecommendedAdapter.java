package com.example.pizzaorderingapp.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pizzaorderingapp.Domain.FoodDomain;
import com.example.pizzaorderingapp.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class RecommendedAdapter extends RecyclerView.Adapter<RecommendedAdapter.ViewHolder> {

    private ArrayList<FoodDomain> foodList;
    private OnItemClickListener onItemClickListener;

    public RecommendedAdapter(ArrayList<FoodDomain> foodList, OnItemClickListener onItemClickListener) {
        this.foodList = foodList;
        this.onItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholder_recommended, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        FoodDomain foodDomain = foodList.get(position);

        holder.title.setText(foodDomain.getTitle());
        holder.fee.setText(String.format("$%.2f", foodDomain.getFee()));

        // Load image using Picasso
        String imageUri = foodDomain.getPic();
        if (imageUri != null && !imageUri.isEmpty()) {
            Picasso.get()
                    .load(imageUri)
                    .placeholder(R.drawable.burger) // Placeholder while loading
                    .error(R.drawable.pizza_default) // Error image if loading fails
                    .into(holder.pic);
        } else {
            holder.pic.setImageResource(R.drawable.burger); // Default image if URI is empty or null
        }

        // Set onClickListener for the add button
        holder.addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onItemClickListener != null) {
                    onItemClickListener.onAddToCartClick(foodDomain);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return foodList.size();
    }

    // Interface for handling click events
    public interface OnItemClickListener {
        void onAddToCartClick(FoodDomain foodDomain);
    }

    // ViewHolder class
    public static class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView pic;
        private TextView title, fee;
        private ImageView addBtn;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            pic = itemView.findViewById(R.id.pic);
            title = itemView.findViewById(R.id.title);
            fee = itemView.findViewById(R.id.fee);
            addBtn = itemView.findViewById(R.id.addBtn);
        }
    }
}
