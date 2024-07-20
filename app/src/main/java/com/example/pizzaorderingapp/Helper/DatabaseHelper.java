package com.example.pizzaorderingapp.Helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.pizzaorderingapp.Domain.CategoryDomain;

import java.util.ArrayList;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "pizzaOrderingAppDB";
    private static final int DATABASE_VERSION = 1;

    // Table names
    public static final String TABLE_ORDERS = "orders";
    public static final String TABLE_USERS = "users";
    public static final String TABLE_MENU_ITEMS = "menu_items";
    public static final String TABLE_MENU_ITEM_CATEGORY = "menu_item_category";

    // Column names for orders table
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_USER_EMAIL_ORDERS = "user_email";
    public static final String COLUMN_ORDER_DETAILS = "order_details";
    public static final String COLUMN_TOTAL_AMOUNT = "total_amount";
    public static final String COLUMN_DATE = "order_date";

    // Column names for users table
    public static final String COLUMN_FIRST_NAME = "first_name";
    public static final String COLUMN_LAST_NAME = "last_name";
    public static final String COLUMN_USER_EMAIL = "user_email";
    public static final String COLUMN_PASSWORD = "password";
    public static final String COLUMN_ROLE = "role";
    public static final String COLUMN_DELIVERY_ADDRESS = "delivery_address";
    public static final String COLUMN_PHONE = "phone";
    public static final String COLUMN_IMAGE_URI = "image_uri";

    // Column names for menu items table
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_DESCRIPTION = "description";
    public static final String COLUMN_PRICE = "price";
    public static final String COLUMN_CATEGORY = "category";
    public static final String COLUMN_TOPPINGS = "toppings";

    // Column names for menu item category table
    public static final String COLUMN_CATEGORY_NAME = "categoryName";
    public static final String COLUMN_IMAGE_URL = "imageUrl";

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
                    COLUMN_USER_EMAIL + " TEXT UNIQUE, " +
                    COLUMN_PASSWORD + " TEXT, " +
                    COLUMN_ROLE + " TEXT, " +
                    COLUMN_DELIVERY_ADDRESS + " TEXT, " +
                    COLUMN_PHONE + " TEXT, " +
                    COLUMN_IMAGE_URI + " TEXT" +
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
                    "is_deleted INTEGER DEFAULT 0" +
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
        insertDefaultCategories(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < 2) {
            db.execSQL("ALTER TABLE " + TABLE_USERS + " ADD COLUMN " + COLUMN_DELIVERY_ADDRESS + " TEXT;");
            db.execSQL("ALTER TABLE " + TABLE_USERS + " ADD COLUMN " + COLUMN_PHONE + " TEXT;");
            db.execSQL("ALTER TABLE " + TABLE_USERS + " ADD COLUMN " + COLUMN_IMAGE_URI + " TEXT;");
        }
    }

    private void insertDefaultCategories(SQLiteDatabase db) {
        insertCategory(db, "Margherita", "https://foodbyjonister.com/wp-content/uploads/2020/01/MargheritaPizza.jpg");
        insertCategory(db, "Pepperoni", "https://againstthegraingourmet.com/cdn/shop/products/Pepperoni_Pizza_Beauty_1200x1200.jpg?v=1658703726");
        insertCategory(db, "Veggie", "https://www.thecandidcooks.com/wp-content/uploads/2022/07/california-veggie-pizza-feature.jpg");
        insertCategory(db, "Meat", "https://halo-pg.com/wp-content/uploads/2021/10/Ultimate-Stuffed-Meatlovers-Pizza-1.jpg");
        insertCategory(db, "Hawaiian", "https://www.jessicagavin.com/wp-content/uploads/2020/07/hawaiian-pizza-16-1200.jpg");
        insertCategory(db, "BBQ", "https://www.thecandidcooks.com/wp-content/uploads/2023/04/bbq-chicken-pizza-feature.jpg");
        insertCategory(db, "Cheese", "https://www.insidetherustickitchen.com/wp-content/uploads/2020/07/Quattro-formaggi-pizza-square-Inside-the-rustic-kitchen.jpg");
        insertCategory(db, "Seafood", "https://images.eatsmarter.com/sites/default/files/styles/facebook/public/seafood-pizza-with-garlic-and-tomatoes-551418.jpg");
        insertCategory(db, "Pesto", "https://www.jessicagavin.com/wp-content/uploads/2022/02/chicken-pesto-pizza-16-1200.jpg");
        insertCategory(db, "White Pizza", "https://tastesbetterfromscratch.com/wp-content/uploads/2020/10/White-Pizza-6-480x270.jpg");
    }

    private void insertCategory(SQLiteDatabase db, String categoryName, String imageUrl) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_CATEGORY_NAME, categoryName);
        values.put(COLUMN_IMAGE_URL, imageUrl);
        db.insert(TABLE_MENU_ITEM_CATEGORY, null, values);
    }

    public ArrayList<CategoryDomain> getAllCategories() {
        ArrayList<CategoryDomain> categoryList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_MENU_ITEM_CATEGORY, null);

        if (cursor.moveToFirst()) {
            do {
                int categoryNameIndex = cursor.getColumnIndex(COLUMN_CATEGORY_NAME);
                int imageUrlIndex = cursor.getColumnIndex(COLUMN_IMAGE_URL);

                if (categoryNameIndex >= 0 && imageUrlIndex >= 0) {
                    String categoryName = cursor.getString(categoryNameIndex);
                    String imageUrl = cursor.getString(imageUrlIndex);
                    categoryList.add(new CategoryDomain(categoryName, imageUrl));
                }
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return categoryList;
    }
}
