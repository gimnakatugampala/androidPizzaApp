package com.example.pizzaorderingapp.Activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.pizzaorderingapp.Model.MenuItem;
import com.example.pizzaorderingapp.R;
import com.example.pizzaorderingapp.Repository.MenuItemRepository;

import java.util.List;

public class EditMenuItemActivity extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;

    private ImageView imageViewMenuItem;
    private Button buttonChooseImage;
    private EditText editTextName;
    private EditText editTextDescription;
    private EditText editTextPrice;
    private Spinner spinnerCategory;
    private CheckBox checkBoxTopping1;
    private CheckBox checkBoxTopping2;
    private CheckBox checkBoxTopping3;
    private Button buttonSubmit;

    private MenuItemRepository menuItemRepository;
    private ArrayAdapter<String> categoryAdapter;
    private MenuItem menuItem;
    private Uri imageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_menu_item);

        // Initialize UI elements
        imageViewMenuItem = findViewById(R.id.menu_item_image);
        buttonChooseImage = findViewById(R.id.button_choose_image);
        editTextName = findViewById(R.id.edit_text_menu_item_name);
        editTextDescription = findViewById(R.id.edit_text_menu_item_description);
        editTextPrice = findViewById(R.id.edit_text_menu_item_price);
        spinnerCategory = findViewById(R.id.spinner_category);
        checkBoxTopping1 = findViewById(R.id.check_box_topping1);
        checkBoxTopping2 = findViewById(R.id.check_box_topping2);
        checkBoxTopping3 = findViewById(R.id.check_box_topping3);
        buttonSubmit = findViewById(R.id.button_submit);

        menuItemRepository = new MenuItemRepository(this);

        // Populate category spinner
        List<String> categories = menuItemRepository.getCategories();
        categoryAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, categories);
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCategory.setAdapter(categoryAdapter);

        // Get menu item ID from intent
        Intent intent = getIntent();
        int menuItemId = intent.getIntExtra("MENU_ITEM_ID", -1);
        if (menuItemId == -1) {
            Toast.makeText(this, "Invalid menu item ID", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Load menu item details
        menuItem = menuItemRepository.getMenuItemById(menuItemId);
        if (menuItem == null) {
            Toast.makeText(this, "Menu item not found", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Populate fields with menu item details
        populateFields(menuItem);

        // Set up button click listeners
        buttonChooseImage.setOnClickListener(v -> openImageChooser());
        buttonSubmit.setOnClickListener(v -> updateMenuItem());
    }

    private void populateFields(MenuItem menuItem) {
        editTextName.setText(menuItem.getName());
        editTextDescription.setText(menuItem.getDescription());
        editTextPrice.setText(String.valueOf(menuItem.getPrice()));
        spinnerCategory.setSelection(categoryAdapter.getPosition(menuItem.getCategory()));

        // Set toppings
        checkBoxTopping1.setChecked(menuItem.getToppings().contains("Topping 1"));
        checkBoxTopping2.setChecked(menuItem.getToppings().contains("Topping 2"));
        checkBoxTopping3.setChecked(menuItem.getToppings().contains("Topping 3"));

        // Set image URI
        if (menuItem.getImageUri() != null) {
            imageUri = Uri.parse(menuItem.getImageUri());
            imageViewMenuItem.setImageURI(imageUri);
        }
    }

    private void openImageChooser() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();
            imageViewMenuItem.setImageURI(imageUri);
        }
    }

    private void updateMenuItem() {
        String name = editTextName.getText().toString().trim();
        String description = editTextDescription.getText().toString().trim();
        String priceStr = editTextPrice.getText().toString().trim();

        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(description) || TextUtils.isEmpty(priceStr)) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        double price;
        try {
            price = Double.parseDouble(priceStr);
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Invalid price format", Toast.LENGTH_SHORT).show();
            return;
        }

        menuItem.setName(name);
        menuItem.setDescription(description);
        menuItem.setPrice(price);
        menuItem.setCategory(spinnerCategory.getSelectedItem().toString());

        // Set toppings
        StringBuilder toppings = new StringBuilder();
        if (checkBoxTopping1.isChecked()) {
            toppings.append("Topping 1,");
        }
        if (checkBoxTopping2.isChecked()) {
            toppings.append("Topping 2,");
        }
        if (checkBoxTopping3.isChecked()) {
            toppings.append("Topping 3,");
        }
        if (toppings.length() > 0) {
            toppings.setLength(toppings.length() - 1); // Remove the trailing comma
        }
        menuItem.setToppings(toppings.toString());

        // Set image URI
        if (imageUri != null) {
            menuItem.setImageUri(imageUri.toString());
        }

        boolean success = menuItemRepository.updateMenuItem(menuItem);
        if (success) {
            Toast.makeText(this, "Menu item updated successfully", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, "Failed to update menu item", Toast.LENGTH_SHORT).show();
        }
    }
}
