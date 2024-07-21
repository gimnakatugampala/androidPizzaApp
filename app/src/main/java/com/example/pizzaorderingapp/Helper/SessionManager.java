package com.example.pizzaorderingapp.Util;

import android.content.Context;
import android.content.SharedPreferences;

public class SessionManager {

    private static final String PREF_NAME = "UserSession";
    private static final String KEY_IS_LOGGEDIN = "isLoggedIn";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_ROLE = "role";
    private static final String KEY_FIRST_NAME = "firstName"; // Add this line
    private static final String KEY_LAST_NAME = "lastName";   // Add this line
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

    public void createLoginSession(String email, String role, String firstName, String lastName) {
        editor.putBoolean(KEY_IS_LOGGEDIN, true);
        editor.putString(KEY_EMAIL, email);
        editor.putString(KEY_ROLE, role);
        editor.putString(KEY_FIRST_NAME, firstName); // Store first name
        editor.putString(KEY_LAST_NAME, lastName);   // Store last name
        editor.commit();
    }

    public boolean isLoggedIn() {
        return pref.getBoolean(KEY_IS_LOGGEDIN, false);
    }

    public void logoutUser() {
        editor.clear();
        editor.commit();
    }

    public String getEmail() {
        return pref.getString(KEY_EMAIL, null);
    }

    public String getRole() {
        return pref.getString(KEY_ROLE, null);
    }

    public String getFirstName() {
        return pref.getString(KEY_FIRST_NAME, null); // Retrieve first name
    }

    public String getLastName() {
        return pref.getString(KEY_LAST_NAME, null);  // Retrieve last name
    }

    public boolean isFirstTimeLaunch() {
        return pref.getBoolean(KEY_FIRST_TIME_LAUNCH, true);
    }

    public void setFirstTimeLaunch(boolean isFirstTime) {
        editor.putBoolean(KEY_FIRST_TIME_LAUNCH, isFirstTime);
        editor.commit();
    }
}
