package com.example.pizzaorderingapp.Repository;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.pizzaorderingapp.Model.MenuItem;
import com.example.pizzaorderingapp.Helper.DatabaseHelper;

import java.util.ArrayList;
import java.util.List;

public class MenuItemRepository {

    private SQLiteOpenHelper dbHelper;

    public MenuItemRepository(Context context) {
        dbHelper = new DatabaseHelper(context);
    }

    public boolean addMenuItem(MenuItem menuItem) {
        SQLiteDatabase db = null;
        boolean result = false;
        try {
            db = dbHelper.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(DatabaseHelper.COLUMN_NAME, menuItem.getName());
            values.put(DatabaseHelper.COLUMN_DESCRIPTION, menuItem.getDescription());
            values.put(DatabaseHelper.COLUMN_PRICE, menuItem.getPrice());
            values.put(DatabaseHelper.COLUMN_CATEGORY, menuItem.getCategory());
            values.put(DatabaseHelper.COLUMN_TOPPINGS, menuItem.getToppings());
            values.put(DatabaseHelper.COLUMN_IMAGE_URI, menuItem.getImageUri());

            long insertResult = db.insert(DatabaseHelper.TABLE_MENU_ITEMS, null, values);
            result = insertResult != -1;
        } finally {
            if (db != null) {
                db.close();
            }
        }
        return result;
    }

    public List<MenuItem> getAllMenuItems() {
        List<MenuItem> menuItems = new ArrayList<>();
        SQLiteDatabase db = null;
        Cursor cursor = null;
        try {
            db = dbHelper.getReadableDatabase();
            String[] columns = {
                    DatabaseHelper.COLUMN_ID,
                    DatabaseHelper.COLUMN_NAME,
                    DatabaseHelper.COLUMN_DESCRIPTION,
                    DatabaseHelper.COLUMN_PRICE,
                    DatabaseHelper.COLUMN_CATEGORY,
                    DatabaseHelper.COLUMN_TOPPINGS,
                    DatabaseHelper.COLUMN_IMAGE_URI
            };

            cursor = db.query(DatabaseHelper.TABLE_MENU_ITEMS, columns, null, null, null, null, null);
            if (cursor.moveToFirst()) {
                do {
                    int idIndex = cursor.getColumnIndex(DatabaseHelper.COLUMN_ID);
                    int nameIndex = cursor.getColumnIndex(DatabaseHelper.COLUMN_NAME);
                    int descriptionIndex = cursor.getColumnIndex(DatabaseHelper.COLUMN_DESCRIPTION);
                    int priceIndex = cursor.getColumnIndex(DatabaseHelper.COLUMN_PRICE);
                    int categoryIndex = cursor.getColumnIndex(DatabaseHelper.COLUMN_CATEGORY);
                    int toppingsIndex = cursor.getColumnIndex(DatabaseHelper.COLUMN_TOPPINGS);
                    int imageUriIndex = cursor.getColumnIndex(DatabaseHelper.COLUMN_IMAGE_URI);

                    // Check if column indexes are valid
                    if (idIndex != -1 && nameIndex != -1 && descriptionIndex != -1 &&
                            priceIndex != -1 && categoryIndex != -1 && toppingsIndex != -1 &&
                            imageUriIndex != -1) {

                        MenuItem menuItem = new MenuItem(
                                cursor.getInt(idIndex),
                                cursor.getString(nameIndex),
                                cursor.getString(descriptionIndex),
                                cursor.getDouble(priceIndex),
                                cursor.getString(categoryIndex),
                                cursor.getString(toppingsIndex),
                                cursor.getString(imageUriIndex)
                        );
                        menuItems.add(menuItem);
                    }
                } while (cursor.moveToNext());
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            if (db != null) {
                db.close();
            }
        }
        return menuItems;
    }
}
