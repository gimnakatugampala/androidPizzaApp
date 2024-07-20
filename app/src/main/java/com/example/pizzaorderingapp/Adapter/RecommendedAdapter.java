package com.example.pizzaorderingapp.Adapter;

import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pizzaorderingapp.Domain.FoodDomain;
import com.example.pizzaorderingapp.R;

import java.io.File;
import java.util.ArrayList;

public class RecommendedAdapter extends RecyclerView.Adapter<RecommendedAdapter.ViewHolder> {

    private static final String TAG = "RecommendedAdapter"; // Tag for logging
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

        String imageUri = foodDomain.getPic();
        Log.d(TAG, "Loading image from URI: " + imageUri);

        if (imageUri != null && !imageUri.isEmpty()) {
            Uri uri;
            if (imageUri.startsWith("file://")) {
                uri = Uri.parse(imageUri);
            } else {
                uri = Uri.fromFile(new File(imageUri));
            }

            // Check if the URI is a local file
            if ("file".equals(uri.getScheme())) {
                File imageFile = new File(uri.getPath());
                if (imageFile.exists()) {
                    holder.pic.setImageURI(uri); // Set image directly for local files
                } else {
                    Log.w(TAG, "Image file does not exist: " + uri.getPath());
                    holder.pic.setImageResource(R.drawable.pizza_default); // Set error image
                }
            } else {
                Log.e(TAG, "Unsupported URI scheme: " + uri.getScheme());
                holder.pic.setImageResource(R.drawable.pizza_default); // Set error image
            }
        } else {
            Log.w(TAG, "Image URI is null or empty, setting default image");
            holder.pic.setImageResource(R.drawable.burger); // Set default image
        }

        holder.addBtn.setOnClickListener(v -> {
            if (onItemClickListener != null) {
                onItemClickListener.onAddToCartClick(foodDomain);
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
