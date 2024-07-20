package com.example.pizzaorderingapp.Activity;

import android.os.Bundle;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.pizzaorderingapp.Adapter.MenuItemAdapter;
import com.example.pizzaorderingapp.Model.MenuItem;
import com.example.pizzaorderingapp.R;
import com.example.pizzaorderingapp.Repository.MenuItemRepository;

import java.util.Collections;
import java.util.List;

public class MenuItemListActivity extends AppCompatActivity {

    private ListView listViewMenuItems;
    private MenuItemRepository menuItemRepository;
    private MenuItemAdapter menuItemAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_list);

        listViewMenuItems = findViewById(R.id.menu_item_list);
        menuItemRepository = new MenuItemRepository(this);

        // Load menu items from the repository
        List<MenuItem> menuItemList = menuItemRepository.getAllMenuItems();

        // Reverse the list to show the latest items at the top
        Collections.reverse(menuItemList);

        // Set up the adapter
        menuItemAdapter = new MenuItemAdapter(this, menuItemList);
        listViewMenuItems.setAdapter(menuItemAdapter);
    }
}
