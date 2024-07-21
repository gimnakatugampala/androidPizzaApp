package com.example.pizzaorderingapp.Adapter;

import android.content.Context;
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
import com.example.pizzaorderingapp.Helper.ManagementCart;
import com.example.pizzaorderingapp.Interface.ChangeNumberItemsListener;
import com.example.pizzaorderingapp.R;

import java.io.File;
import java.util.ArrayList;

public class CartListAdapter extends RecyclerView.Adapter<CartListAdapter.ViewHolder> {

    private static final String TAG = "CartListAdapter";
    private ArrayList<FoodDomain> listFoodSelected;
    private ManagementCart managementCart;
    private ChangeNumberItemsListener changeNumberItemsListener;
    private Context context;

    public CartListAdapter(ArrayList<FoodDomain> listFoodSelected, Context context, ChangeNumberItemsListener changeNumberItemsListener) {
        this.listFoodSelected = listFoodSelected;
        this.managementCart = new ManagementCart(context);
        this.changeNumberItemsListener = changeNumberItemsListener;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholder_cart, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        FoodDomain food = listFoodSelected.get(position);

        holder.titleTxt.setText(food.getTitle());
        holder.feeEachItem.setText(String.format("$%.2f", food.getFee()));
        holder.totalEachItem.setText(String.format("$%.2f", food.getNumberInCart() * food.getFee()));
        holder.numberItemTxt.setText(String.valueOf(food.getNumberInCart()));

        // Load image
        String imageUri = food.getPic();
        if (imageUri != null && !imageUri.isEmpty()) {
            Uri uri = Uri.parse(imageUri);
            if ("file".equals(uri.getScheme())) {
                File imageFile = new File(uri.getPath());
                if (imageFile.exists()) {
                    holder.pic.setImageURI(uri);
                } else {
                    Log.w(TAG, "Image file does not exist: " + uri.getPath());
                    holder.pic.setImageResource(R.drawable.pizza_default);
                }
            } else {
                Log.e(TAG, "Unsupported URI scheme: " + uri.getScheme());
                holder.pic.setImageResource(R.drawable.pizza_default);
            }
        } else {
            Log.w(TAG, "Image URI is null or empty, setting default image");
            holder.pic.setImageResource(R.drawable.pizza_default);
        }

        // Set button listeners
        holder.plusCardBtn.setOnClickListener(v -> {
            managementCart.plusNumberFood(listFoodSelected, position, () -> {
                notifyDataSetChanged();
                changeNumberItemsListener.changed();
            });
        });

        holder.minusCardBtn.setOnClickListener(v -> {
            managementCart.minusNumberFood(listFoodSelected, position, () -> {
                notifyDataSetChanged();
                changeNumberItemsListener.changed();
            });
        });

        holder.deleteCardBtn.setOnClickListener(v -> {
            managementCart.removeFoodFromCart(listFoodSelected, position, () -> {
                notifyDataSetChanged();
                changeNumberItemsListener.changed();
            });
        });
    }

    @Override
    public int getItemCount() {
        return listFoodSelected.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView titleTxt, feeEachItem, totalEachItem, numberItemTxt;
        ImageView pic, plusCardBtn, minusCardBtn, deleteCardBtn;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            titleTxt = itemView.findViewById(R.id.titleTxt);
            pic = itemView.findViewById(R.id.pic);
            feeEachItem = itemView.findViewById(R.id.feeEachItem);
            totalEachItem = itemView.findViewById(R.id.totalEachItem);
            plusCardBtn = itemView.findViewById(R.id.plusCardBtn);
            minusCardBtn = itemView.findViewById(R.id.minusCardBtn);
            numberItemTxt = itemView.findViewById(R.id.numberItemTxt);
            deleteCardBtn = itemView.findViewById(R.id.deleteCardBtn);
        }
    }
}
