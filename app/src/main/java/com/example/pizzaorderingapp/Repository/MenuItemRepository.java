package com.example.pizzaorderingapp.Repository;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.pizzaorderingapp.Helper.DatabaseHelper;
import com.example.pizzaorderingapp.Model.MenuItem;

import java.util.ArrayList;
import java.util.List;

public class MenuItemRepository {

    private static final String TAG = "MenuItemRepository";

    private static final String TABLE_MENU_ITEMS = "menu_items";
    private static final String TABLE_MENU_ITEM_CATEGORY = "menu_item_category";

    private static final String COLUMN_ID = "_id";
    private static final String COLUMN_NAME = "name";
    private static final String COLUMN_DESCRIPTION = "description";
    private static final String COLUMN_PRICE = "price";
    private static final String COLUMN_CATEGORY = "category";
    private static final String COLUMN_TOPPINGS = "toppings";
    private static final String COLUMN_IMAGE_URI = "image_uri";
    private static final String COLUMN_IS_DELETED = "is_deleted";
    private static final String COLUMN_CATEGORY_NAME = "categoryName";

    private DatabaseHelper databaseHelper;

    public MenuItemRepository(Context context) {
        this.databaseHelper = new DatabaseHelper(context);
    }

    // Insert a new menu item
    public boolean addMenuItem(MenuItem menuItem) {
        try (SQLiteDatabase db = databaseHelper.getWritableDatabase()) {
            ContentValues values = new ContentValues();
            values.put(COLUMN_NAME, menuItem.getName());
            values.put(COLUMN_DESCRIPTION, menuItem.getDescription());
            values.put(COLUMN_PRICE, menuItem.getPrice());
            values.put(COLUMN_CATEGORY, menuItem.getCategory());
            values.put(COLUMN_TOPPINGS, menuItem.getToppings());
            values.put(COLUMN_IMAGE_URI, menuItem.getImageUri());
            long result = db.insert(TABLE_MENU_ITEMS, null, values);
            return result != -1;
        }
    }

    // Update an existing menu item
    public boolean updateMenuItem(MenuItem menuItem) {
        try (SQLiteDatabase db = databaseHelper.getWritableDatabase()) {
            ContentValues values = new ContentValues();
            values.put(COLUMN_NAME, menuItem.getName());
            values.put(COLUMN_DESCRIPTION, menuItem.getDescription());
            values.put(COLUMN_PRICE, menuItem.getPrice());
            values.put(COLUMN_CATEGORY, menuItem.getCategory());
            values.put(COLUMN_TOPPINGS, menuItem.getToppings());
            values.put(COLUMN_IMAGE_URI, menuItem.getImageUri());
            String whereClause = COLUMN_ID + " = ?";
            String[] whereArgs = { String.valueOf(menuItem.getId()) };
            int result = db.update(TABLE_MENU_ITEMS, values, whereClause, whereArgs);
            return result > 0;
        }
    }

    // Soft delete a menu item (mark as deleted)
    public boolean softDeleteMenuItem(int id) {
        try (SQLiteDatabase db = databaseHelper.getWritableDatabase()) {
            ContentValues values = new ContentValues();
            values.put(COLUMN_IS_DELETED, 1);
            String whereClause = COLUMN_ID + " = ?";
            String[] whereArgs = { String.valueOf(id) };
            int result = db.update(TABLE_MENU_ITEMS, values, whereClause, whereArgs);
            return result > 0;
        }
    }

    // Retrieve all non-deleted menu items
    public List<MenuItem> getAllMenuItems() {
        List<MenuItem> menuItems = new ArrayList<>();
        String query = "SELECT * FROM " + TABLE_MENU_ITEMS + " WHERE " + COLUMN_IS_DELETED + " = 0";
        try (SQLiteDatabase db = databaseHelper.getReadableDatabase();
             Cursor cursor = db.rawQuery(query, null)) {

            int idIndex = cursor.getColumnIndex(COLUMN_ID);
            int nameIndex = cursor.getColumnIndex(COLUMN_NAME);
            int descriptionIndex = cursor.getColumnIndex(COLUMN_DESCRIPTION);
            int priceIndex = cursor.getColumnIndex(COLUMN_PRICE);
            int categoryIndex = cursor.getColumnIndex(COLUMN_CATEGORY);
            int toppingsIndex = cursor.getColumnIndex(COLUMN_TOPPINGS);
            int imageUriIndex = cursor.getColumnIndex(COLUMN_IMAGE_URI);

            if (idIndex == -1 || nameIndex == -1 || descriptionIndex == -1 ||
                    priceIndex == -1 || categoryIndex == -1 || toppingsIndex == -1 ||
                    imageUriIndex == -1) {
                Log.w(TAG, "One or more column indices are invalid. Check column names.");
                return menuItems;
            }

            if (cursor.moveToFirst()) {
                do {
                    MenuItem menuItem = new MenuItem();
                    menuItem.setId(cursor.getInt(idIndex));
                    menuItem.setName(cursor.getString(nameIndex));
                    menuItem.setDescription(cursor.getString(descriptionIndex));
                    menuItem.setPrice(cursor.getDouble(priceIndex));
                    menuItem.setCategory(cursor.getString(categoryIndex));
                    menuItem.setToppings(cursor.getString(toppingsIndex));
                    menuItem.setImageUri(cursor.getString(imageUriIndex));
                    menuItems.add(menuItem);
                } while (cursor.moveToNext());
            }
        }
        return menuItems;
    }

    // Retrieve menu item by ID
    public MenuItem getMenuItemById(int id) {
        MenuItem menuItem = null;
        String query = "SELECT * FROM " + TABLE_MENU_ITEMS + " WHERE " + COLUMN_ID + " = ? AND " + COLUMN_IS_DELETED + " = 0";
        try (SQLiteDatabase db = databaseHelper.getReadableDatabase();
             Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(id)})) {

            int idIndex = cursor.getColumnIndex(COLUMN_ID);
            int nameIndex = cursor.getColumnIndex(COLUMN_NAME);
            int descriptionIndex = cursor.getColumnIndex(COLUMN_DESCRIPTION);
            int priceIndex = cursor.getColumnIndex(COLUMN_PRICE);
            int categoryIndex = cursor.getColumnIndex(COLUMN_CATEGORY);
            int toppingsIndex = cursor.getColumnIndex(COLUMN_TOPPINGS);
            int imageUriIndex = cursor.getColumnIndex(COLUMN_IMAGE_URI);

            if (idIndex == -1 || nameIndex == -1 || descriptionIndex == -1 ||
                    priceIndex == -1 || categoryIndex == -1 || toppingsIndex == -1 ||
                    imageUriIndex == -1) {
                Log.w(TAG, "One or more column indices are invalid. Check column names.");
                return menuItem;
            }

            if (cursor.moveToFirst()) {
                menuItem = new MenuItem();
                menuItem.setId(cursor.getInt(idIndex));
                menuItem.setName(cursor.getString(nameIndex));
                menuItem.setDescription(cursor.getString(descriptionIndex));
                menuItem.setPrice(cursor.getDouble(priceIndex));
                menuItem.setCategory(cursor.getString(categoryIndex));
                menuItem.setToppings(cursor.getString(toppingsIndex));
                menuItem.setImageUri(cursor.getString(imageUriIndex));
            }
        }
        return menuItem;
    }

    // Retrieve all categories from the menu_item_category table
    public List<String> getCategories() {
        List<String> categories = new ArrayList<>();
        String query = "SELECT " + COLUMN_CATEGORY_NAME + " FROM " + TABLE_MENU_ITEM_CATEGORY;
        try (SQLiteDatabase db = databaseHelper.getReadableDatabase();
             Cursor cursor = db.rawQuery(query, null)) {

            int categoryNameIndex = cursor.getColumnIndex(COLUMN_CATEGORY_NAME);

            if (categoryNameIndex == -1) {
                Log.w(TAG, "Column index for categoryName is invalid. Check column name.");
                return categories;
            }

            if (cursor.moveToFirst()) {
                do {
                    categories.add(cursor.getString(categoryNameIndex));
                } while (cursor.moveToNext());
            }
        }
        return categories;
    }

    public void insertDefaultCategories() {
        try (SQLiteDatabase db = databaseHelper.getWritableDatabase()) {
            insertCategory(db, "Veg", "https://www.indianhealthyrecipes.com/wp-content/uploads/2015/10/pizza-recipe-1.jpg");
            insertCategory(db, "Cheese and Onion", "https://www.fuegowoodfiredovens.com/wp-content/uploads/2022/08/goats-cheese-caramelised-onions-and-fig-pizza.jpg");
            insertCategory(db, "Chicken", "https://breadboozebacon.com/wp-content/uploads/2023/05/BBQ-Chicken-Pizza-SQUARE.jpg");
            insertCategory(db, "Beef", "https://embed.widencdn.net/img/beef/pz4eba64j5/exact/beef-pepper-and-onion-pizza-horizontal.tif?keep=c&u=7fueml");
        }
    }

    private void insertCategory(SQLiteDatabase db, String categoryName, String imageUrl) {
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_CATEGORY_NAME, categoryName);
        values.put(DatabaseHelper.COLUMN_IMAGE_URL, imageUrl);
        db.insert(DatabaseHelper.TABLE_MENU_ITEM_CATEGORY, null, values);
    }
}
