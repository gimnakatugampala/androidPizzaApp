package com.example.pizzaorderingapp.Helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "pizzaOrderingAppDB";
    private static final int DATABASE_VERSION = 1; // Increment this version if schema changes

    // Table names
    public static final String TABLE_ORDERS = "orders";
    public static final String TABLE_USERS = "users";
    public static final String TABLE_MENU_ITEMS = "menu_items";
    public static final String TABLE_MENU_ITEM_CATEGORY = "menu_item_category"; // New table

    // Column names
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_USER_EMAIL_ORDERS = "user_email";
    public static final String COLUMN_ORDER_DETAILS = "order_details";
    public static final String COLUMN_TOTAL_AMOUNT = "total_amount";
    public static final String COLUMN_DATE = "order_date";

    public static final String COLUMN_FIRST_NAME = "first_name";
    public static final String COLUMN_LAST_NAME = "last_name";
    public static final String COLUMN_USER_EMAIL = "user_email";
    public static final String COLUMN_PASSWORD = "password";
    public static final String COLUMN_ROLE = "role";

    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_DESCRIPTION = "description";
    public static final String COLUMN_PRICE = "price";
    public static final String COLUMN_CATEGORY = "category";
    public static final String COLUMN_TOPPINGS = "toppings";
    public static final String COLUMN_IMAGE_URI = "image_uri";

    public static final String COLUMN_CATEGORY_NAME = "categoryName"; // New column
    public static final String COLUMN_IMAGE_URL = "imageUrl"; // New column

    private static final String TABLE_ORDERS_CREATE =
            "CREATE TABLE " + TABLE_ORDERS + " (" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_USER_EMAIL_ORDERS + " TEXT, " +
                    COLUMN_ORDER_DETAILS + " TEXT, " +
                    COLUMN_TOTAL_AMOUNT + " TEXT, " +
                    COLUMN_DATE + " TEXT" +
                    ");";

    private static final String TABLE_USERS_CREATE =
            "CREATE TABLE " + TABLE_USERS + " (" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_FIRST_NAME + " TEXT, " +
                    COLUMN_LAST_NAME + " TEXT, " +
                    COLUMN_USER_EMAIL + " TEXT, " +
                    COLUMN_PASSWORD + " TEXT, " +
                    COLUMN_ROLE + " TEXT" +
                    ");";

    private static final String TABLE_MENU_ITEMS_CREATE =
            "CREATE TABLE " + TABLE_MENU_ITEMS + " (" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_NAME + " TEXT, " +
                    COLUMN_DESCRIPTION + " TEXT, " +
                    COLUMN_PRICE + " REAL, " +
                    COLUMN_CATEGORY + " TEXT, " +
                    COLUMN_TOPPINGS + " TEXT, " +
                    COLUMN_IMAGE_URI + " TEXT, " +
                    "is_deleted INTEGER DEFAULT 0" + // New column for soft delete
                    ");";

    private static final String TABLE_MENU_ITEM_CATEGORY_CREATE =
            "CREATE TABLE " + TABLE_MENU_ITEM_CATEGORY + " (" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_CATEGORY_NAME + " TEXT, " +
                    COLUMN_IMAGE_URL + " TEXT" +
                    ");";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TABLE_ORDERS_CREATE);
        db.execSQL(TABLE_USERS_CREATE);
        db.execSQL(TABLE_MENU_ITEMS_CREATE);
        db.execSQL(TABLE_MENU_ITEM_CATEGORY_CREATE);

        // Insert default categories
//        insertDefaultCategories(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ORDERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MENU_ITEMS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MENU_ITEM_CATEGORY);
        onCreate(db);
    }

    private void insertDefaultCategories(SQLiteDatabase db) {
        Log.d("DatabaseHelper", "Inserting default categories...");
        insertCategory(db, "Veg", "https://www.indianhealthyrecipes.com/wp-content/uploads/2015/10/pizza-recipe-1.jpg");
        insertCategory(db, "Cheese and Onion", "https://www.fuegowoodfiredovens.com/wp-content/uploads/2022/08/goats-cheese-caramelised-onions-and-fig-pizza.jpg");
        insertCategory(db, "Chicken", "https://breadboozebacon.com/wp-content/uploads/2023/05/BBQ-Chicken-Pizza-SQUARE.jpg");
        insertCategory(db, "Beef", "https://embed.widencdn.net/img/beef/pz4eba64j5/exact/beef-pepper-and-onion-pizza-horizontal.tif?keep=c&u=7fueml");
    }

    private void insertCategory(SQLiteDatabase db, String categoryName, String imageUrl) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_CATEGORY_NAME, categoryName);
        values.put(COLUMN_IMAGE_URL, imageUrl);
        long result = db.insert(TABLE_MENU_ITEM_CATEGORY, null, values);
        Log.d("DatabaseHelper", "Inserted category: " + categoryName + ", Result: " + result);
    }
}
