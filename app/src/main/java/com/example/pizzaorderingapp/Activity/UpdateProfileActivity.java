package com.example.pizzaorderingapp.Activity;

import android.Manifest;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.pizzaorderingapp.Helper.DatabaseHelper;
import com.example.pizzaorderingapp.R;
import com.example.pizzaorderingapp.Util.SessionManager;

import java.io.IOException;

public class UpdateProfileActivity extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;
    private static final int CAPTURE_IMAGE_REQUEST = 2;
    private static final int REQUEST_STORAGE_PERMISSION = 3;
    private static final int REQUEST_CAMERA_PERMISSION = 4;

    private ImageView profileImageView;
    private EditText firstNameEditText, lastNameEditText, emailEditText, deliveryAddressEditText, phoneEditText;
    private Button chooseImageButton, updateButton;

    private DatabaseHelper databaseHelper;
    private SessionManager sessionManager;
    private String userEmail;
    private Uri imageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_profile);

        // Initialize UI elements
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

        if (userEmail == null) {
            Toast.makeText(this, "User not logged in", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        loadUserProfile();

        chooseImageButton.setOnClickListener(v -> showImageSourceDialog());
        updateButton.setOnClickListener(v -> updateUserProfile());
    }

    private void showImageSourceDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Choose Image Source")
                .setItems(new CharSequence[]{"Camera", "Gallery"}, (dialog, which) -> {
                    switch (which) {
                        case 0:
                            // Camera option selected
                            if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, REQUEST_CAMERA_PERMISSION);
                            } else {
                                openCamera();
                            }
                            break;
                        case 1:
                            // Gallery option selected
                            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_STORAGE_PERMISSION);
                            } else {
                                openImageChooser();
                            }
                            break;
                    }
                });
        builder.create().show();
    }

    private void openCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(intent, CAPTURE_IMAGE_REQUEST);
        } else {
            Toast.makeText(this, "No camera app found", Toast.LENGTH_SHORT).show();
        }
    }

    private void openImageChooser() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == PICK_IMAGE_REQUEST && data != null && data.getData() != null) {
                imageUri = data.getData();
                profileImageView.setImageURI(imageUri);
            } else if (requestCode == CAPTURE_IMAGE_REQUEST && data != null) {
                Bundle extras = data.getExtras();
                if (extras != null) {
                    Bitmap imageBitmap = (Bitmap) extras.get("data");
                    if (imageBitmap != null) {
                        profileImageView.setImageBitmap(imageBitmap);
                        imageUri = saveImageToGallery(imageBitmap);
                    }
                }
            }
        }
    }

    private Uri saveImageToGallery(Bitmap bitmap) {
        String imageTitle = "CapturedImage_" + System.currentTimeMillis();
        String imageDescription = "Image captured by camera";
        return Uri.parse(MediaStore.Images.Media.insertImage(getContentResolver(), bitmap, imageTitle, imageDescription));
    }

    private void loadUserProfile() {
        Cursor cursor = databaseHelper.getReadableDatabase().query(
                DatabaseHelper.TABLE_USERS, null, DatabaseHelper.COLUMN_USER_EMAIL + "=?",
                new String[]{userEmail}, null, null, null
        );

        if (cursor != null) {
            try {
                if (cursor.moveToFirst()) {
                    int firstNameIndex = cursor.getColumnIndex(DatabaseHelper.COLUMN_FIRST_NAME);
                    int lastNameIndex = cursor.getColumnIndex(DatabaseHelper.COLUMN_LAST_NAME);
                    int emailIndex = cursor.getColumnIndex(DatabaseHelper.COLUMN_USER_EMAIL);
                    int deliveryAddressIndex = cursor.getColumnIndex(DatabaseHelper.COLUMN_DELIVERY_ADDRESS);
                    int phoneIndex = cursor.getColumnIndex(DatabaseHelper.COLUMN_PHONE);
                    int imageUriIndex = cursor.getColumnIndex(DatabaseHelper.COLUMN_IMAGE_URI);

                    if (firstNameIndex >= 0) {
                        firstNameEditText.setText(cursor.getString(firstNameIndex));
                    }
                    if (lastNameIndex >= 0) {
                        lastNameEditText.setText(cursor.getString(lastNameIndex));
                    }
                    if (emailIndex >= 0) {
                        emailEditText.setText(cursor.getString(emailIndex));
                    }
                    if (deliveryAddressIndex >= 0) {
                        deliveryAddressEditText.setText(cursor.getString(deliveryAddressIndex));
                    }
                    if (phoneIndex >= 0) {
                        phoneEditText.setText(cursor.getString(phoneIndex));
                    }
                    if (imageUriIndex >= 0) {
                        String uriString = cursor.getString(imageUriIndex);
                        if (uriString != null && !uriString.isEmpty()) {
                            imageUri = Uri.parse(uriString);
                            profileImageView.setImageURI(imageUri);
                        }
                    }
                }
            } finally {
                cursor.close();
            }
        }
    }

    private void updateUserProfile() {
        String firstName = firstNameEditText.getText().toString().trim();
        String lastName = lastNameEditText.getText().toString().trim();
        String email = emailEditText.getText().toString().trim();
        String deliveryAddress = deliveryAddressEditText.getText().toString().trim();
        String phone = phoneEditText.getText().toString().trim();

        if (TextUtils.isEmpty(firstName) || TextUtils.isEmpty(lastName) || TextUtils.isEmpty(email)) {
            Toast.makeText(this, "First name, last name, and email are required.", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!phone.isEmpty() && (phone.length() != 10 || !phone.matches("\\d+"))) {
            Toast.makeText(this, "Phone number must be 10 digits.", Toast.LENGTH_SHORT).show();
            return;
        }

        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_FIRST_NAME, firstName);
        values.put(DatabaseHelper.COLUMN_LAST_NAME, lastName);
        values.put(DatabaseHelper.COLUMN_USER_EMAIL, email); // Email should be updated if changed
        values.put(DatabaseHelper.COLUMN_DELIVERY_ADDRESS, deliveryAddress);
        values.put(DatabaseHelper.COLUMN_PHONE, phone);
        if (imageUri != null) {
            values.put(DatabaseHelper.COLUMN_IMAGE_URI, imageUri.toString());
        } else {
            values.putNull(DatabaseHelper.COLUMN_IMAGE_URI); // Handle optional field
        }

        int rowsUpdated = databaseHelper.getWritableDatabase().update(
                DatabaseHelper.TABLE_USERS, values, DatabaseHelper.COLUMN_USER_EMAIL + "=?",
                new String[]{userEmail}
        );

        if (rowsUpdated > 0) {
            Toast.makeText(this, "Profile updated successfully", Toast.LENGTH_SHORT).show();
            finish(); // Close the activity
        } else {
            Toast.makeText(this, "Failed to update profile", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_STORAGE_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openImageChooser();
            } else {
                Toast.makeText(this, "Storage permission is required to choose an image.", Toast.LENGTH_SHORT).show();
            }
        } else if (requestCode == REQUEST_CAMERA_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openCamera();
            } else {
                Toast.makeText(this, "Camera permission is required to capture an image.", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
