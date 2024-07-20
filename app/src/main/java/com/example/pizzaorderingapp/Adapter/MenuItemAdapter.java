package com.example.pizzaorderingapp.Adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

import com.example.pizzaorderingapp.Activity.MenuItemListActivity;
import com.example.pizzaorderingapp.Model.MenuItem;
import com.example.pizzaorderingapp.R;
import com.example.pizzaorderingapp.Repository.MenuItemRepository;

import java.util.List;

public class MenuItemAdapter extends ArrayAdapter<MenuItem> {

    private MenuItemRepository menuItemRepository;

    public MenuItemAdapter(@NonNull Context context, @NonNull List<MenuItem> objects) {
        super(context, 0, objects);
        menuItemRepository = new MenuItemRepository(context);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.menu_item_list_item, parent, false);
        }

        MenuItem menuItem = getItem(position);

        if (menuItem != null) {
            ImageView imageView = convertView.findViewById(R.id.image_view_menu_item);
            TextView textViewName = convertView.findViewById(R.id.text_view_menu_item_name);
            TextView textViewPrice = convertView.findViewById(R.id.text_view_menu_item_price);
            TextView textViewDescription = convertView.findViewById(R.id.text_view_menu_item_description);
            Button buttonDelete = convertView.findViewById(R.id.button_delete_menu_item);

            // Load the image
            Bitmap bitmap = BitmapFactory.decodeFile(menuItem.getImageUri());
            if (bitmap != null) {
                imageView.setImageBitmap(bitmap);
            } else {
                imageView.setImageResource(R.drawable.pizza_default); // Default image if loading fails
            }

            // Set other text fields
            textViewName.setText(menuItem.getName());
            textViewPrice.setText(String.format("$%.2f", menuItem.getPrice()));

            // Display a portion of the description
            String description = menuItem.getDescription();
            if (description.length() > 20) {
                description = description.substring(0, 20) + "...";
            }
            textViewDescription.setText(description);

            // Set up delete button click listener
            buttonDelete.setOnClickListener(v -> {
                new AlertDialog.Builder(getContext())
                        .setTitle("Confirm Delete")
                        .setMessage("Are you sure you want to delete this menu item?")
                        .setPositiveButton("Yes", (dialog, which) -> {
                            boolean success = menuItemRepository.softDeleteMenuItem(menuItem.getId());
                            if (success) {
                                Toast.makeText(getContext(), "Menu Item Deleted", Toast.LENGTH_SHORT).show();
                                // Refresh the list
                                ((MenuItemListActivity) getContext()).refreshMenuItems();
                            } else {
                                Toast.makeText(getContext(), "Error Deleting Menu Item", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .setNegativeButton("No", (dialog, which) -> dialog.dismiss())
                        .show();
            });
        }

        return convertView;
    }
}
