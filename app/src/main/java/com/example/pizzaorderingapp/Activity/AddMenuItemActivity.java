package com.example.pizzaorderingapp.Activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
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
import com.example.pizzaorderingapp.Utils.ImageUtils;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.List;

public class AddMenuItemActivity extends AppCompatActivity {

    private ImageView imageViewMenuItem;
    private Button buttonChooseImage, buttonSubmit;
    private EditText editTextName, editTextDescription, editTextPrice;
    private Spinner spinnerCategory;
    private CheckBox checkBoxTopping1, checkBoxTopping2, checkBoxTopping3;
    private Uri imageUri;

    private MenuItemRepository menuItemRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_menu_item);

        // Initialize views
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

        // Set up the category spinner
        loadCategories();

        buttonChooseImage.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intent, 1);
        });

        buttonSubmit.setOnClickListener(v -> addMenuItem());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK && data != null) {
            imageUri = data.getData();
            imageViewMenuItem.setImageURI(imageUri);
        }
    }

    private void loadCategories() {
        List<String> categories = menuItemRepository.getCategories();

        if (categories.isEmpty()) {
            Toast.makeText(this, "No categories available", Toast.LENGTH_SHORT).show();
            return;
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, categories);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCategory.setAdapter(adapter);
    }

    private void addMenuItem() {
        String name = editTextName.getText().toString().trim();
        String description = editTextDescription.getText().toString().trim();
        String priceText = editTextPrice.getText().toString().trim();
        String category = spinnerCategory.getSelectedItem().toString();
        boolean topping1 = checkBoxTopping1.isChecked();
        boolean topping2 = checkBoxTopping2.isChecked();
        boolean topping3 = checkBoxTopping3.isChecked();

        if (name.isEmpty() || description.isEmpty() || priceText.isEmpty() || imageUri == null) {
            Toast.makeText(this, "Please fill all fields and choose an image", Toast.LENGTH_SHORT).show();
            return;
        }

        double price;
        try {
            price = Double.parseDouble(priceText);
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Please enter a valid price", Toast.LENGTH_SHORT).show();
            return;
        }

        String toppings = "";
        if (topping1) toppings += "Topping 1,";
        if (topping2) toppings += "Topping 2,";
        if (topping3) toppings += "Topping 3,";
        if (!toppings.isEmpty()) toppings = toppings.substring(0, toppings.length() - 1); // Remove trailing comma

        // Convert URI to Bitmap
        Bitmap bitmap;
        try {
            InputStream imageStream = getContentResolver().openInputStream(imageUri);
            bitmap = BitmapFactory.decodeStream(imageStream);
        } catch (FileNotFoundException e) {
            Toast.makeText(this, "Error loading image", Toast.LENGTH_SHORT).show();
            return;
        }

        // Save the image to internal storage
        String savedImagePath = ImageUtils.saveImageToInternalStorage(this, bitmap, name);

        // Create a MenuItem object
        MenuItem menuItem = new MenuItem(
                0, // Assuming ID is auto-generated
                name,
                description,
                price,
                category,
                toppings,
                savedImagePath
        );

        // Use the repository to add the menu item
        boolean isInserted = menuItemRepository.addMenuItem(menuItem);

        if (isInserted) {
            Toast.makeText(this, "Menu Item Added Successfully", Toast.LENGTH_SHORT).show();
            finish(); // Close activity
        } else {
            Toast.makeText(this, "Error Adding Menu Item", Toast.LENGTH_SHORT).show();
        }
    }
}
