package com.example.pizzaorderingapp.Activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.pizzaorderingapp.Adapter.MenuItemAdapter;
import com.example.pizzaorderingapp.Model.MenuItem;
import com.example.pizzaorderingapp.R;
import com.example.pizzaorderingapp.Repository.MenuItemRepository;

import java.util.List;

public class MenuItemListActivity extends AppCompatActivity {

    private ListView listViewMenuItems;
    private TextView textViewNoMenuItems;
    private MenuItemRepository menuItemRepository;
    private MenuItemAdapter menuItemAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_list);

        listViewMenuItems = findViewById(R.id.menu_item_list);
        textViewNoMenuItems = findViewById(R.id.text_view_no_menu_items);
        menuItemRepository = new MenuItemRepository(this);

        refreshMenuItems();
    }

    public void refreshMenuItems() {
        List<MenuItem> menuItemList = menuItemRepository.getAllMenuItems();
        if (menuItemList.isEmpty()) {
            listViewMenuItems.setVisibility(View.GONE);
            textViewNoMenuItems.setVisibility(View.VISIBLE);
        } else {
            listViewMenuItems.setVisibility(View.VISIBLE);
            textViewNoMenuItems.setVisibility(View.GONE);
            menuItemAdapter = new MenuItemAdapter(this, menuItemList);
            listViewMenuItems.setAdapter(menuItemAdapter);
        }
    }
}
