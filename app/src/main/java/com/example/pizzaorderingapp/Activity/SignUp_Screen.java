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

        firstName = findViewById(R.id.fristName);
        lastName = findViewById(R.id.lastName);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        confirmPassword = findViewById(R.id.password2);
        signUpButton = findViewById(R.id.button3);

        userRepository = new UserRepository(this);
        sessionManager = new SessionManager(this);

        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String fName = firstName.getText().toString();
                String lName = lastName.getText().toString();
                String emailStr = email.getText().toString();
                String pass = password.getText().toString();
                String confirmPass = confirmPassword.getText().toString();

                if (validateInputs(fName, lName, emailStr, pass, confirmPass)) {
                    String role = "Member"; // or "Admin" based on your logic
                    if (!userRepository.isUserExists(emailStr)) {
                        boolean isRegistered = userRepository.registerUser(fName, lName, emailStr, pass, role);

                        if (isRegistered) {
                            sessionManager.createLoginSession(emailStr, role);
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
            Toast.makeText(SignUp_Screen.this, "Please enter a valid email address", Toast.LENGTH_SHORT).show();
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
