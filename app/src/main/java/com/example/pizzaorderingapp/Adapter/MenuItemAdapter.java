package com.example.pizzaorderingapp.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import com.example.pizzaorderingapp.Activity.EditMenuItemActivity;
import com.example.pizzaorderingapp.Activity.MenuItemListActivity;
import com.example.pizzaorderingapp.Model.MenuItem;
import com.example.pizzaorderingapp.R;
import com.example.pizzaorderingapp.Repository.MenuItemRepository;

import java.io.File;
import java.util.List;

public class MenuItemAdapter extends BaseAdapter {

    private Context context;
    private List<MenuItem> menuItems;
    private MenuItemRepository menuItemRepository;

    public MenuItemAdapter(Context context, List<MenuItem> menuItems) {
        this.context = context;
        this.menuItems = menuItems;
        this.menuItemRepository = new MenuItemRepository(context.getApplicationContext());
    }

    @Override
    public int getCount() {
        return menuItems.size();
    }

    @Override
    public Object getItem(int position) {
        return menuItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.menu_item_list_item, parent, false);
        }

        MenuItem menuItem = menuItems.get(position);

        ImageView imageView = convertView.findViewById(R.id.image_view_menu_item);
        TextView textViewName = convertView.findViewById(R.id.text_view_menu_item_name);
        TextView textViewPrice = convertView.findViewById(R.id.text_view_menu_item_price);
        TextView textViewDescription = convertView.findViewById(R.id.text_view_menu_item_description);
        Button buttonEdit = convertView.findViewById(R.id.button_edit_menu_item);
        Button buttonDelete = convertView.findViewById(R.id.button_delete_menu_item);

        // Load image
        String imageUri = menuItem.getImageUri();
        if (imageUri != null && !imageUri.isEmpty()) {
            Uri uri = Uri.parse(imageUri);
            File imgFile = new File(uri.getPath());
            if (imgFile.exists()) {
                Bitmap bitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                imageView.setImageBitmap(bitmap);
            } else {
                imageView.setImageResource(R.drawable.pizza_default);
            }
        } else {
            imageView.setImageResource(R.drawable.pizza_default);
        }

        textViewName.setText(menuItem.getName());
        textViewPrice.setText(String.format("$%.2f", menuItem.getPrice())); // Formatting price
        textViewDescription.setText(menuItem.getDescription());

        buttonEdit.setOnClickListener(v -> {
            Intent intent = new Intent(context, EditMenuItemActivity.class);
            intent.putExtra("MENU_ITEM_ID", menuItem.getId()); // Pass the menu item ID to the edit activity
            if (context instanceof MenuItemListActivity) {
                ((MenuItemListActivity) context).startActivityForResult(intent, 1); // Update request code as needed
            } else {
                Toast.makeText(context, "Context is not MenuItemListActivity", Toast.LENGTH_SHORT).show();
            }
        });

        buttonDelete.setOnClickListener(v -> {
            new AlertDialog.Builder(context)
                    .setTitle("Delete Menu Item")
                    .setMessage("Are you sure you want to delete this menu item?")
                    .setPositiveButton("Yes", (dialog, which) -> {
                        boolean deleted = menuItemRepository.softDeleteMenuItem(menuItem.getId());
                        if (deleted) {
                            menuItems.remove(position);
                            notifyDataSetChanged();
                            Toast.makeText(context, "Menu item deleted", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(context, "Failed to delete menu item", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .setNegativeButton("No", null)
                    .show();
        });

        return convertView;
    }

    // Method to update the list of menu items
    public void updateMenuItems(List<MenuItem> newMenuItems) {
        this.menuItems.clear();
        this.menuItems.addAll(newMenuItems);
        notifyDataSetChanged();
    }
}
