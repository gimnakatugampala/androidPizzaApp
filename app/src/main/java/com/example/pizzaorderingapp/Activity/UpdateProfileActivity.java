package com.example.pizzaorderingapp.Activity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.example.pizzaorderingapp.Helper.DatabaseHelper;
import com.example.pizzaorderingapp.R;
import com.example.pizzaorderingapp.Util.SessionManager;

import java.io.IOException;

public class UpdateProfileActivity extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;

    private ImageView profileImageView;
    private EditText firstNameEditText, lastNameEditText, emailEditText, deliveryAddressEditText, phoneEditText;
    private Button chooseImageButton, updateButton;

    private DatabaseHelper databaseHelper;
    private SessionManager sessionManager;
    private String userEmail;
    private String imageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_profile);

        profileImageView = findViewById(R.id.profile_image);
        firstNameEditText = findViewById(R.id.edit_text_first_name);
        lastNameEditText = findViewById(R.id.edit_text_last_name);
        emailEditText = findViewById(R.id.edit_text_email);
        deliveryAddressEditText = findViewById(R.id.edit_text_delivery_address);
        phoneEditText = findViewById(R.id.edit_text_phone);
        chooseImageButton = findViewById(R.id.button_choose_image);
        updateButton = findViewById(R.id.button_update);

        databaseHelper = new DatabaseHelper(this);
        sessionManager = new SessionManager(this);
        userEmail = sessionManager.getEmail();

        loadUserProfile();

        chooseImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openImageChooser();
            }
        });

        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateUserProfile();
            }
        });
    }

    private void loadUserProfile() {
        if (userEmail != null) {
            Cursor cursor = databaseHelper.getReadableDatabase().query(
                    DatabaseHelper.TABLE_USERS, null, DatabaseHelper.COLUMN_USER_EMAIL + "=?",
                    new String[]{userEmail}, null, null, null
            );

            if (cursor != null && cursor.moveToFirst()) {
                int firstNameIndex = cursor.getColumnIndex(DatabaseHelper.COLUMN_FIRST_NAME);
                int lastNameIndex = cursor.getColumnIndex(DatabaseHelper.COLUMN_LAST_NAME);
                int emailIndex = cursor.getColumnIndex(DatabaseHelper.COLUMN_USER_EMAIL);
                int deliveryAddressIndex = cursor.getColumnIndex(DatabaseHelper.COLUMN_DELIVERY_ADDRESS);
                int phoneIndex = cursor.getColumnIndex(DatabaseHelper.COLUMN_PHONE);
                int imageUriIndex = cursor.getColumnIndex(DatabaseHelper.COLUMN_IMAGE_URI);

                if (firstNameIndex != -1) {
                    firstNameEditText.setText(cursor.getString(firstNameIndex));
                }
                if (lastNameIndex != -1) {
                    lastNameEditText.setText(cursor.getString(lastNameIndex));
                }
                if (emailIndex != -1) {
                    emailEditText.setText(cursor.getString(emailIndex));
                }
                if (deliveryAddressIndex != -1) {
                    deliveryAddressEditText.setText(cursor.getString(deliveryAddressIndex));
                }
                if (phoneIndex != -1) {
                    phoneEditText.setText(cursor.getString(phoneIndex));
                }
                if (imageUriIndex != -1) {
                    imageUri = cursor.getString(imageUriIndex);
                    if (imageUri != null && !imageUri.isEmpty()) {
                        profileImageView.setImageURI(Uri.parse(imageUri));
                    }
                }
                cursor.close();
            }
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
            Uri uri = data.getData();
            imageUri = uri.toString();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                profileImageView.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void updateUserProfile() {
        String firstName = firstNameEditText.getText().toString().trim();
        String lastName = lastNameEditText.getText().toString().trim();
        String deliveryAddress = deliveryAddressEditText.getText().toString().trim();
        String phone = phoneEditText.getText().toString().trim();

        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_FIRST_NAME, firstName);
        values.put(DatabaseHelper.COLUMN_LAST_NAME, lastName);
        values.put(DatabaseHelper.COLUMN_DELIVERY_ADDRESS, deliveryAddress);
        values.put(DatabaseHelper.COLUMN_PHONE, phone);
        values.put(DatabaseHelper.COLUMN_IMAGE_URI, imageUri);

        int rowsUpdated = databaseHelper.getWritableDatabase().update(
                DatabaseHelper.TABLE_USERS, values, DatabaseHelper.COLUMN_USER_EMAIL + "=?",
                new String[]{userEmail}
        );

        if (rowsUpdated > 0) {
            // User profile updated successfully
            finish(); // Close the activity
        }
    }
}
