package com.example.pizzaorderingapp.Adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.pizzaorderingapp.Domain.FoodDomain;
import com.example.pizzaorderingapp.Helper.ManagementCart;
import com.example.pizzaorderingapp.R;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class AllFoodItemsAdapter extends RecyclerView.Adapter<AllFoodItemsAdapter.ViewHolder> {

    private static final String TAG = "AllFoodItemsAdapter";
    private List<FoodDomain> foodList;
    private List<FoodDomain> foodListFiltered;
    private ManagementCart managementCart;
    private Context context;

    public AllFoodItemsAdapter(Context context, List<FoodDomain> foodList, ManagementCart managementCart) {
        this.context = context;
        this.foodList = new ArrayList<>(foodList); // Create a copy for filtering
        this.foodListFiltered = new ArrayList<>(foodList);
        this.managementCart = managementCart;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholder_food_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        FoodDomain foodItem = foodListFiltered.get(position);

        // Load image
        String imageUri = foodItem.getPic();
        if (imageUri != null && !imageUri.isEmpty()) {
            Uri uri = Uri.parse(imageUri);
            File imgFile = new File(uri.getPath());
            if (imgFile.exists()) {
                Bitmap bitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                holder.imageView.setImageBitmap(bitmap);
            } else {
                holder.imageView.setImageResource(R.drawable.pizza_default);
            }
        } else {
            holder.imageView.setImageResource(R.drawable.pizza_default);
        }

        holder.titleTextView.setText(foodItem.getTitle());
        holder.feeTextView.setText(String.format("$%.2f", foodItem.getFee()));

        holder.addToCartButton.setOnClickListener(v -> managementCart.insertFood(foodItem));
    }

    @Override
    public int getItemCount() {
        return foodListFiltered.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView titleTextView;
        TextView feeTextView;
        Button addToCartButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.pic);
            titleTextView = itemView.findViewById(R.id.title);
            feeTextView = itemView.findViewById(R.id.fee);
            addToCartButton = itemView.findViewById(R.id.add_to_cart_btn);
        }
    }

    // Method to filter the list
    public void filter(String query) {
        foodListFiltered.clear();
        if (query.isEmpty()) {
            foodListFiltered.addAll(foodList);
        } else {
            for (FoodDomain item : foodList) {
                if (item.getTitle().toLowerCase().contains(query.toLowerCase())) {
                    foodListFiltered.add(item);
                }
            }
        }
        notifyDataSetChanged();
    }
}
