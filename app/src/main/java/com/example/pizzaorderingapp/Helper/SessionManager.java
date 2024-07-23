package com.example.pizzaorderingapp.Util;

import android.content.Context;
import android.content.SharedPreferences;

public class SessionManager {

    private static final String PREF_NAME = "UserSession";
    private static final String KEY_IS_LOGGEDIN = "isLoggedIn";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_ROLE = "role";
    private static final String KEY_FIRST_NAME = "firstName";
    private static final String KEY_LAST_NAME = "lastName";
    private static final String KEY_FIRST_TIME_LAUNCH = "isFirstTimeLaunch";

    SharedPreferences pref;
    SharedPreferences.Editor editor;
    Context context;

    int PRIVATE_MODE = 0;

    public SessionManager(Context context) {
        this.context = context;
        pref = context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    // Create login session
    public void createLoginSession(String email, String role, String firstName, String lastName) {
        editor.putBoolean(KEY_IS_LOGGEDIN, true);
        editor.putString(KEY_EMAIL, email);
        editor.putString(KEY_ROLE, role);
        editor.putString(KEY_FIRST_NAME, firstName);
        editor.putString(KEY_LAST_NAME, lastName);
        editor.apply(); // Use apply for asynchronous operation
    }

    public void createGuestSession() {
        editor.putBoolean(KEY_IS_LOGGEDIN, true);
        editor.putString(KEY_EMAIL, "guest@example.com");
        editor.putString(KEY_ROLE, "guest");
        editor.putString(KEY_FIRST_NAME, "Guest");
        editor.putString(KEY_LAST_NAME, "User");
        editor.apply();
    }

    // Check if user is logged in
    public boolean isLoggedIn() {
        return pref.getBoolean(KEY_IS_LOGGEDIN, false);
    }

    // Logout user
    public void logoutUser() {
        editor.clear();
        editor.apply(); // Use apply for asynchronous operation
    }

    // Check if user is a guest
    public boolean isGuest() {
        return "guest".equalsIgnoreCase(pref.getString(KEY_ROLE, ""));
    }


    // Check if the user is a member
    public boolean isMember() {
        return "member".equalsIgnoreCase(pref.getString(KEY_ROLE, ""));
    }

    // Get email
    public String getEmail() {
        return pref.getString(KEY_EMAIL, null);
    }

    // Get role
    public String getRole() {
        return pref.getString(KEY_ROLE, null);
    }

    // Get first name
    public String getFirstName() {
        return pref.getString(KEY_FIRST_NAME, null);
    }

    // Get last name
    public String getLastName() {
        return pref.getString(KEY_LAST_NAME, null);
    }

    // Check if it's the first time launch
    public boolean isFirstTimeLaunch() {
        return pref.getBoolean(KEY_FIRST_TIME_LAUNCH, true);
    }

    // Set first time launch status
    public void setFirstTimeLaunch(boolean isFirstTime) {
        editor.putBoolean(KEY_FIRST_TIME_LAUNCH, isFirstTime);
        editor.apply(); // Use apply for asynchronous operation
    }

    // Update session details
    public void updateSession(String email, String role, String firstName, String lastName) {
        editor.putString(KEY_EMAIL, email);
        editor.putString(KEY_ROLE, role);
        editor.putString(KEY_FIRST_NAME, firstName);
        editor.putString(KEY_LAST_NAME, lastName);
        editor.apply(); // Use apply for asynchronous operation
    }

    // Check if the user is an admin
    public boolean isAdmin() {
        return "admin".equalsIgnoreCase(pref.getString(KEY_ROLE, ""));
    }

    // Check if the user is a customer
    public boolean isCustomer() {
        return "customer".equalsIgnoreCase(pref.getString(KEY_ROLE, ""));
    }
}
