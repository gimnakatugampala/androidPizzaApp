package com.example.pizzaorderingapp.Activity;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.pizzaorderingapp.Model.PromoCode;
import com.example.pizzaorderingapp.R;
import com.example.pizzaorderingapp.Repository.PromoCodeRepository;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class AddPromoCodeActivity extends AppCompatActivity {

    private TextInputEditText editTextPromoCode;
    private TextInputEditText editTextDiscountPercentage;
    private TextInputEditText editTextExpiryDate;
    private TextInputLayout textInputLayoutPromoCode;
    private TextInputLayout textInputLayoutDiscountPercentage;
    private TextInputLayout textInputLayoutExpiryDate;
    private Button buttonSavePromoCode;
    private PromoCodeRepository promoCodeRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_promo_code);

        // Initialize views
        initViews();

        // Initialize repository
        promoCodeRepository = new PromoCodeRepository(this);

        // Set up DatePicker for Expiry Date
        setupDatePicker();

        // Set up button click listener
        buttonSavePromoCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                savePromoCode();
            }
        });
    }

    private void initViews() {
        editTextPromoCode = findViewById(R.id.edit_text_promo_code);
        editTextDiscountPercentage = findViewById(R.id.edit_text_discount_percentage);
        editTextExpiryDate = findViewById(R.id.edit_text_expiry_date);
        textInputLayoutPromoCode = findViewById(R.id.text_input_layout_promo_code);
        textInputLayoutDiscountPercentage = findViewById(R.id.text_input_layout_discount_percentage);
        textInputLayoutExpiryDate = findViewById(R.id.text_input_layout_expiry_date);
        buttonSavePromoCode = findViewById(R.id.button_add_promo_code);
    }

    private void setupDatePicker() {
        editTextExpiryDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePicker();
            }
        });
    }

    private void showDatePicker() {
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        // Format the date as YYYY-MM-DD
                        String date = String.format(Locale.US, "%04d-%02d-%02d", year, month + 1, dayOfMonth);
                        editTextExpiryDate.setText(date);
                    }
                }, year, month, day);

        datePickerDialog.show();
    }

    private void savePromoCode() {
        // Reset error messages
        resetErrors();

        // Get input values
        String promoCode = editTextPromoCode.getText().toString().trim();
        String discountPercentageStr = editTextDiscountPercentage.getText().toString().trim();
        String expiryDate = editTextExpiryDate.getText().toString().trim();

        // Validate inputs
        if (!validateInputs(promoCode, discountPercentageStr, expiryDate)) {
            return;
        }

        double discountPercentage = Double.parseDouble(discountPercentageStr);

        // Create PromoCode object
        PromoCode promoCodeObj = new PromoCode();
        promoCodeObj.setPromoCode(promoCode);
        promoCodeObj.setDiscountPercentage(discountPercentage);
        promoCodeObj.setExpiryDate(expiryDate);

        // Save to database
        if (promoCodeRepository.addPromoCode(promoCodeObj)) {
            Toast.makeText(this, "Promo code added successfully", Toast.LENGTH_SHORT).show();
            finish(); // Close activity and return to previous screen
        } else {
            Toast.makeText(this, "Failed to add promo code", Toast.LENGTH_SHORT).show();
        }
    }

    private void resetErrors() {
        textInputLayoutPromoCode.setError(null);
        textInputLayoutDiscountPercentage.setError(null);
        textInputLayoutExpiryDate.setError(null);
    }

    private boolean validateInputs(String promoCode, String discountPercentageStr, String expiryDate) {
        boolean isValid = true;

        if (promoCode.isEmpty()) {
            textInputLayoutPromoCode.setError("Please enter a promo code");
            isValid = false;
        }

        if (discountPercentageStr.isEmpty()) {
            textInputLayoutDiscountPercentage.setError("Please enter a discount percentage");
            isValid = false;
        } else {
            try {
                double discountPercentage = Double.parseDouble(discountPercentageStr);
                if (discountPercentage < 0) {
                    textInputLayoutDiscountPercentage.setError("Discount percentage must be positive");
                    isValid = false;
                }
            } catch (NumberFormatException e) {
                textInputLayoutDiscountPercentage.setError("Invalid discount percentage");
                isValid = false;
            }
        }

        if (expiryDate.isEmpty() || !isValidDate(expiryDate)) {
            textInputLayoutExpiryDate.setError("Invalid expiry date format. Use YYYY-MM-DD.");
            isValid = false;
        }

        return isValid;
    }

    private boolean isValidDate(@NonNull String date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        sdf.setLenient(false);
        try {
            sdf.parse(date);
            return true;
        } catch (ParseException e) {
            return false;
        }
    }
}
