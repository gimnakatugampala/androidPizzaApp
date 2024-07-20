package com.example.pizzaorderingapp.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

import com.example.pizzaorderingapp.Model.MenuItem;
import com.example.pizzaorderingapp.R;
import com.example.pizzaorderingapp.Repository.MenuItemRepository;

import java.util.ArrayList;
import java.util.List;

public class EditMenuItemActivity extends AppCompatActivity {

    private EditText editTextName;
    private EditText editTextDescription;
    private EditText editTextPrice;
    private Spinner spinnerCategory;
    private CheckBox checkBoxTopping1;
    private CheckBox checkBoxTopping2;
    private CheckBox checkBoxTopping3;
    private Button buttonSave;

    private MenuItem menuItem;
    private MenuItemRepository menuItemRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_menu_item);

        // Initialize views
        editTextName = findViewById(R.id.edit_text_menu_item_name);
        editTextDescription = findViewById(R.id.edit_text_menu_item_description);
        editTextPrice = findViewById(R.id.edit_text_menu_item_price);
        spinnerCategory = findViewById(R.id.spinner_category);
        checkBoxTopping1 = findViewById(R.id.check_box_topping1);
        checkBoxTopping2 = findViewById(R.id.check_box_topping2);
        checkBoxTopping3 = findViewById(R.id.check_box_topping3);
        buttonSave = findViewById(R.id.button_submit);

        // Initialize repository
        menuItemRepository = new MenuItemRepository(this);

        // Get the MenuItem object from Intent
        Intent intent = getIntent();
        menuItem = (MenuItem) intent.getSerializableExtra("menuItem");

        if (menuItem != null) {
            // Populate fields with MenuItem data
            editTextName.setText(menuItem.getName());
            editTextDescription.setText(menuItem.getDescription());
            editTextPrice.setText(String.valueOf(menuItem.getPrice()));
            // Set spinner value based on category
            setCategorySpinner(menuItem.getCategory());
            // Set CheckBoxes based on toppings
            setToppingsCheckBoxes(menuItem.getToppings());
        }

        buttonSave.setOnClickListener(v -> {
            // Update MenuItem details and save
            String name = editTextName.getText().toString();
            String description = editTextDescription.getText().toString();
            double price = Double.parseDouble(editTextPrice.getText().toString());
            String category = spinnerCategory.getSelectedItem().toString();
            String toppings = getSelectedToppings();

            MenuItem updatedMenuItem = new MenuItem(menuItem.getId(), name, description, price, category, toppings, menuItem.getImageUri());
            menuItemRepository.updateMenuItem(updatedMenuItem);

            // Return to previous activity
            finish();
        });
    }

    private void setCategorySpinner(String selectedCategory) {
        // Assuming you have a list of categories
        List<String> categories = new ArrayList<>();
        categories.add("Pizza");
        categories.add("Pasta");
        categories.add("Salad");
        // Add more categories as needed

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, categories);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCategory.setAdapter(adapter);

        int spinnerPosition = adapter.getPosition(selectedCategory);
        spinnerCategory.setSelection(spinnerPosition);
    }

    private void setToppingsCheckBoxes(String toppings) {
        // Assuming toppings are comma-separated
        String[] toppingArray = toppings.split(",");
        checkBoxTopping1.setChecked(false);
        checkBoxTopping2.setChecked(false);
        checkBoxTopping3.setChecked(false);

        for (String topping : toppingArray) {
            if (topping.equals("Topping 1")) {
                checkBoxTopping1.setChecked(true);
            } else if (topping.equals("Topping 2")) {
                checkBoxTopping2.setChecked(true);
            } else if (topping.equals("Topping 3")) {
                checkBoxTopping3.setChecked(true);
            }
        }
    }

    private String getSelectedToppings() {
        List<String> selectedToppings = new ArrayList<>();
        if (checkBoxTopping1.isChecked()) {
            selectedToppings.add("Topping 1");
        }
        if (checkBoxTopping2.isChecked()) {
            selectedToppings.add("Topping 2");
        }
        if (checkBoxTopping3.isChecked()) {
            selectedToppings.add("Topping 3");
        }
        return String.join(",", selectedToppings);
    }
}
