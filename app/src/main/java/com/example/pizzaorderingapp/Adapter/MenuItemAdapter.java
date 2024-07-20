package com.example.pizzaorderingapp.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.pizzaorderingapp.Activity.EditMenuItemActivity;
import com.example.pizzaorderingapp.Activity.MenuItemListActivity;
import com.example.pizzaorderingapp.Model.MenuItem;
import com.example.pizzaorderingapp.R;
import com.example.pizzaorderingapp.Repository.MenuItemRepository;

import java.util.List;

public class MenuItemAdapter extends BaseAdapter {

    private Context context;
    private List<MenuItem> menuItems;
    private MenuItemRepository menuItemRepository;

    public MenuItemAdapter(Context context, List<MenuItem> menuItems) {
        this.context = context;
        this.menuItems = menuItems;
        this.menuItemRepository = new MenuItemRepository(context);
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

        Bitmap bitmap = BitmapFactory.decodeFile(menuItem.getImageUri());
        if (bitmap != null) {
            imageView.setImageBitmap(bitmap);
        } else {
            imageView.setImageResource(R.drawable.pizza_default);
        }

        textViewName.setText(menuItem.getName());
        textViewPrice.setText(String.valueOf(menuItem.getPrice()));
        textViewDescription.setText(menuItem.getDescription());

        buttonEdit.setOnClickListener(v -> {
            Intent intent = new Intent(context, EditMenuItemActivity.class);
            intent.putExtra("menuItem", menuItem); // This should work now
            context.startActivity(intent);
        });


        buttonDelete.setOnClickListener(v -> {
            menuItemRepository.softDeleteMenuItem(menuItem.getId());
            menuItems.remove(position);
            notifyDataSetChanged();
        });

        return convertView;
    }
}
