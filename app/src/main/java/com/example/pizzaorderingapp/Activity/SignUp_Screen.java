package com.example.pizzaorderingapp.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.pizzaorderingapp.Database.UserRepository;
import com.example.pizzaorderingapp.R;
import com.example.pizzaorderingapp.Util.SessionManager;

public class SignUp_Screen extends AppCompatActivity {

    private EditText firstName, lastName, email, password, confirmPassword;
    private Button signUpButton;
    private UserRepository userRepository;
    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_screen);

        // Ensure IDs match with those defined in the XML
        firstName = findViewById(R.id.firstName); // Fixed typo
        lastName = findViewById(R.id.lastName);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        confirmPassword = findViewById(R.id.password2);
        signUpButton = findViewById(R.id.button3); // Fixed ID if needed

        userRepository = new UserRepository(this);
        sessionManager = new SessionManager(this);

        signUpButton.setOnClickListener(v -> {
            String fName = firstName.getText().toString().trim();
            String lName = lastName.getText().toString().trim();
            String emailStr = email.getText().toString().trim();
            String pass = password.getText().toString().trim();
            String confirmPass = confirmPassword.getText().toString().trim();

            if (validateInputs(fName, lName, emailStr, pass, confirmPass)) {
                String role = "Member"; // or "Admin" based on your logic
                if (!userRepository.isUserExists(emailStr)) {
                    boolean isRegistered = userRepository.registerUser(fName, lName, emailStr, pass, role);

                    if (isRegistered) {
                        // Use a default profile image URL or set to an empty string
                        String profileImageUrl = ""; // Default or provided profile image URL
                        sessionManager.createLoginSession(emailStr, role, fName, lName, profileImageUrl);
                        Toast.makeText(SignUp_Screen.this, "Sign Up Successful", Toast.LENGTH_SHORT).show();
                        // Navigate to MainActivity
                        Intent intent = new Intent(SignUp_Screen.this, MainActivity.class);
                        startActivity(intent);
                        finish(); // Finish current activity
                    } else {
                        Toast.makeText(SignUp_Screen.this, "Sign Up Failed", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(SignUp_Screen.this, "User already exists with this email", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private boolean validateInputs(String fName, String lName, String email, String pass, String confirmPass) {
        if (fName.isEmpty()) {
            firstName.setError("First name is required");
            firstName.requestFocus();
            return false;
        }

        if (lName.isEmpty()) {
            lastName.setError("Last name is required");
            lastName.requestFocus();
            return false;
        }

        if (email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            this.email.setError("Please enter a valid email address");
            this.email.requestFocus();
            return false;
        }

        if (pass.isEmpty() || pass.length() < 6) {
            password.setError("Password must be at least 6 characters");
            password.requestFocus();
            return false;
        }

        if (!pass.equals(confirmPass)) {
            confirmPassword.setError("Passwords do not match");
            confirmPassword.requestFocus();
            return false;
        }

        return true;
    }
}
