package com.example.pizzaorderingapp.Adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.pizzaorderingapp.Model.MenuItem;
import com.example.pizzaorderingapp.R;

import java.util.List;

public class MenuItemAdapter extends ArrayAdapter<MenuItem> {

    public MenuItemAdapter(@NonNull Context context, @NonNull List<MenuItem> objects) {
        super(context, 0, objects);
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
        }

        return convertView;
    }
}
