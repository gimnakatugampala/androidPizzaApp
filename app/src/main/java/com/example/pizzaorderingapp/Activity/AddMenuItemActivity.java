package com.example.pizzaorderingapp.Activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.CheckBox;
import android.widget.Spinner;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;
import com.example.pizzaorderingapp.R;

public class AddMenuItemActivity extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;

    private ImageView menuItemImage;
    private Button chooseImageButton;
    private EditText menuItemName;
    private EditText menuItemDescription;
    private EditText menuItemPrice;
    private Spinner categorySpinner;
    private CheckBox topping1;
    private CheckBox topping2;
    private CheckBox topping3;
    private Button submitButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_menu_item);

        menuItemImage = findViewById(R.id.menu_item_image);
        chooseImageButton = findViewById(R.id.button_choose_image);
        menuItemName = findViewById(R.id.edit_text_menu_item_name);
        menuItemDescription = findViewById(R.id.edit_text_menu_item_description);
        menuItemPrice = findViewById(R.id.edit_text_menu_item_price);
        categorySpinner = findViewById(R.id.spinner_category);
        topping1 = findViewById(R.id.check_box_topping1);
        topping2 = findViewById(R.id.check_box_topping2);
        topping3 = findViewById(R.id.check_box_topping3);
        submitButton = findViewById(R.id.button_submit);

        // Initialize category spinner
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.pizza_categories, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categorySpinner.setAdapter(adapter);

        // Set click listener for choosing image
        chooseImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openImagePicker();
            }
        });

        // Set click listener for submitting the form
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addMenuItem();
            }
        });
    }

    private void openImagePicker() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri imageUri = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
                menuItemImage.setImageBitmap(bitmap);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void addMenuItem() {
        // Retrieve input data
        String name = menuItemName.getText().toString();
        String description = menuItemDescription.getText().toString();
        String price = menuItemPrice.getText().toString();
        String category = categorySpinner.getSelectedItem().toString();
        boolean hasTopping1 = topping1.isChecked();
        boolean hasTopping2 = topping2.isChecked();
        boolean hasTopping3 = topping3.isChecked();

        // Handle form submission (e.g., save to database)
        // For demonstration purposes, we’ll just log the data
        // You should replace this with actual database insertion code
        System.out.println("Name: " + name);
        System.out.println("Description: " + description);
        System.out.println("Price: " + price);
        System.out.println("Category: " + category);
        System.out.println("Topping 1: " + hasTopping1);
        System.out.println("Topping 2: " + hasTopping2);
        System.out.println("Topping 3: " + hasTopping3);

        // Implement actual saving logic here
    }
}
