package com.example.pizzaorderingapp.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.pizzaorderingapp.Helper.DatabaseHelper;

public class UserRepository {

    private static final String TAG = "UserRepository";
    private DatabaseHelper dbHelper;

    public UserRepository(Context context) {
        dbHelper = new DatabaseHelper(context);
    }

    // Register a new user
    public boolean registerUser(String firstName, String lastName, String email, String password, String role) {
        SQLiteDatabase db = null;
        try {
            db = dbHelper.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(DatabaseHelper.COLUMN_FIRST_NAME, firstName);
            values.put(DatabaseHelper.COLUMN_LAST_NAME, lastName);
            values.put(DatabaseHelper.COLUMN_USER_EMAIL, email);
            values.put(DatabaseHelper.COLUMN_PASSWORD, password);
            values.put(DatabaseHelper.COLUMN_ROLE, role);

            long result = db.insert(DatabaseHelper.TABLE_USERS, null, values);
            return result != -1;
        } catch (SQLException e) {
            Log.e(TAG, "Error registering user", e);
            return false;
        } finally {
            if (db != null) {
                db.close();
            }
        }
    }

    // Check if a user exists by email
    public boolean isUserExists(String email) {
        SQLiteDatabase db = null;
        Cursor cursor = null;
        try {
            db = dbHelper.getReadableDatabase();
            String[] columns = { DatabaseHelper.COLUMN_ID };
            String selection = DatabaseHelper.COLUMN_USER_EMAIL + " = ?";
            String[] selectionArgs = { email };

            cursor = db.query(DatabaseHelper.TABLE_USERS, columns, selection, selectionArgs, null, null, null);
            return cursor != null && cursor.getCount() > 0;
        } catch (SQLException e) {
            Log.e(TAG, "Error checking if user exists", e);
            return false;
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            if (db != null) {
                db.close();
            }
        }
    }

    // Get user role based on email and password
    public String getUserRole(String email, String password) {
        SQLiteDatabase db = null;
        Cursor cursor = null;
        try {
            db = dbHelper.getReadableDatabase();
            String[] columns = { DatabaseHelper.COLUMN_ROLE };
            String selection = DatabaseHelper.COLUMN_USER_EMAIL + " = ? AND " + DatabaseHelper.COLUMN_PASSWORD + " = ?";
            String[] selectionArgs = { email, password };

            cursor = db.query(DatabaseHelper.TABLE_USERS, columns, selection, selectionArgs, null, null, null);
            if (cursor != null && cursor.moveToFirst()) {
                int roleColumnIndex = cursor.getColumnIndex(DatabaseHelper.COLUMN_ROLE);
                if (roleColumnIndex != -1) {
                    return cursor.getString(roleColumnIndex);
                } else {
                    Log.w(TAG, "Column index for role is -1");
                    return null;
                }
            }
            return null;
        } catch (SQLException e) {
            Log.e(TAG, "Error getting user role", e);
            return null;
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            if (db != null) {
                db.close();
            }
        }
    }

    // Get user first name based on email
    public String getUserFirstName(String email) {
        SQLiteDatabase db = null;
        Cursor cursor = null;
        try {
            db = dbHelper.getReadableDatabase();
            String[] columns = { DatabaseHelper.COLUMN_FIRST_NAME };
            String selection = DatabaseHelper.COLUMN_USER_EMAIL + " = ?";
            String[] selectionArgs = { email };

            cursor = db.query(DatabaseHelper.TABLE_USERS, columns, selection, selectionArgs, null, null, null);
            if (cursor != null && cursor.moveToFirst()) {
                int firstNameColumnIndex = cursor.getColumnIndex(DatabaseHelper.COLUMN_FIRST_NAME);
                if (firstNameColumnIndex != -1) {
                    return cursor.getString(firstNameColumnIndex);
                } else {
                    Log.w(TAG, "Column index for first name is -1");
                    return null;
                }
            }
            return null;
        } catch (SQLException e) {
            Log.e(TAG, "Error getting user first name", e);
            return null;
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            if (db != null) {
                db.close();
            }
        }
    }

    // Get user last name based on email
    public String getUserLastName(String email) {
        SQLiteDatabase db = null;
        Cursor cursor = null;
        try {
            db = dbHelper.getReadableDatabase();
            String[] columns = { DatabaseHelper.COLUMN_LAST_NAME };
            String selection = DatabaseHelper.COLUMN_USER_EMAIL + " = ?";
            String[] selectionArgs = { email };

            cursor = db.query(DatabaseHelper.TABLE_USERS, columns, selection, selectionArgs, null, null, null);
            if (cursor != null && cursor.moveToFirst()) {
                int lastNameColumnIndex = cursor.getColumnIndex(DatabaseHelper.COLUMN_LAST_NAME);
                if (lastNameColumnIndex != -1) {
                    return cursor.getString(lastNameColumnIndex);
                } else {
                    Log.w(TAG, "Column index for last name is -1");
                    return null;
                }
            }
            return null;
        } catch (SQLException e) {
            Log.e(TAG, "Error getting user last name", e);
            return null;
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            if (db != null) {
                db.close();
            }
        }
    }
}
