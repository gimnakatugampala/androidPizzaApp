package com.example.pizzaorderingapp.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.pizzaorderingapp.Database.UserRepository;
import com.example.pizzaorderingapp.R;
import com.example.pizzaorderingapp.Util.SessionManager;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LoginScreen extends AppCompatActivity {

    private EditText emailEditText, passwordEditText;
    private UserRepository userRepository;
    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        emailEditText = findViewById(R.id.editTextTextEmailAddress);
        passwordEditText = findViewById(R.id.editTextTextPassword);
        userRepository = new UserRepository(this);
        sessionManager = new SessionManager(this);

        // Display logout message if available
        if (getIntent().hasExtra("logout_message")) {
            String message = getIntent().getStringExtra("logout_message");
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        }
    }

    public void onLoginSubmit(View view){
        String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();

        if(validateEmail(email) && validatePassword(password)) {
            String role = userRepository.getUserRole(email, password);
            String firstName = userRepository.getUserFirstName(email); // Retrieve first name
            String lastName = userRepository.getUserLastName(email);   // Retrieve last name

            if (role != null) {
                sessionManager.createLoginSession(email, role, firstName, lastName);
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                finish();
            } else {
                Toast.makeText(this, "User does not exist or incorrect credentials.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void gotoSignUp(View view) {
        Intent intent = new Intent(LoginScreen.this, SignUp_Screen.class);
        startActivity(intent);
    }

    private boolean validateEmail(String email) {
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        Matcher matcher = Pattern.compile(emailPattern).matcher(email);
        if(!matcher.matches()) {
            emailEditText.setError("Invalid email address");
            return false;
        }
        return true;
    }

    private boolean validatePassword(String password) {
        if(password.isEmpty() || password.length() < 6) {
            passwordEditText.setError("Password must be at least 6 characters long");
            return false;
        }
        return true;
    }
}
