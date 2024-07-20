package com.example.pizzaorderingapp.Helper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "pizzaOrderingAppDB";
    private static final int DATABASE_VERSION = 1; // Incremented the database version

    public static final String TABLE_ORDERS = "orders";
    public static final String TABLE_USERS = "users";
    public static final String TABLE_MENU_ITEMS = "menu_items";

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
                    COLUMN_IMAGE_URI + " TEXT" +
                    ");";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TABLE_ORDERS_CREATE);
        db.execSQL(TABLE_USERS_CREATE);
        db.execSQL(TABLE_MENU_ITEMS_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ORDERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MENU_ITEMS);
        onCreate(db);
    }
}
