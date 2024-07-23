package com.example.pizzaorderingapp.Helper;
import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.pizzaorderingapp.Model.Order;
import com.example.pizzaorderingapp.Model.Customer;


import com.example.pizzaorderingapp.Domain.CategoryDomain;

import java.util.ArrayList;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "pizzaOrderingAppDB";
    private static final int DATABASE_VERSION = 1; // Updated version

    // Table names
    public static final String TABLE_ORDERS = "orders";
    public static final String TABLE_ORDER_ITEMS = "order_items";
    public static final String TABLE_CUSTOMERS = "customers";
    public static final String TABLE_USERS = "users";
    public static final String TABLE_MENU_ITEMS = "menu_items";
    public static final String TABLE_MENU_ITEM_CATEGORY = "menu_item_category";
    public static final String TABLE_PROMO_CODES = "promo_codes";

    // Column names for orders table
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_USER_EMAIL_ORDERS = "user_email";
    public static final String COLUMN_ORDER_STATUS = "order_status";
    public static final String COLUMN_TOTAL_AMOUNT = "total_amount";
    public static final String COLUMN_DATE = "order_date";
    public static final String COLUMN_COMPLETED = "completed";

    // Column names for order items table
    public static final String COLUMN_ORDER_ID = "order_id";
    public static final String COLUMN_MENU_ITEM_ID = "menu_item_id";
    public static final String COLUMN_QUANTITY = "quantity";

    // Column names for customers table
    public static final String COLUMN_CUSTOMER_EMAIL = "customer_email";
    public static final String COLUMN_CUSTOMER_NAME = "customer_name";
    public static final String COLUMN_CUSTOMER_PHONE = "customer_phone";
    public static final String COLUMN_CUSTOMER_ADDRESS = "customer_address";

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

    public static final String COLUMN_PROMO_ID = "promo_id";
    public static final String COLUMN_PROMO_CODE = "promo_code";
    public static final String COLUMN_PROMO_DISCOUNT_PERCENT = "promo_discount_percent";
    public static final String COLUMN_PROMO_EXPIRY_DATE = "promo_expiry_date";

    //    Order items
    public static final String COLUMN_ORDERITEM_ORDER_ID = "order_id";
    public static final String COLUMN_ORDERITEM_MENU_ITEM_ID = "menu_item_id";
    public static final String COLUMN_ORDERITEM_QUANTITY = "quantity";
    public static final String COLUMN_ORDERITEM_PRICE = "price"; // Add the price column here

    private static final String TABLE_ORDERS_CREATE =
            "CREATE TABLE " + TABLE_ORDERS + " (" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_USER_EMAIL_ORDERS + " TEXT, " +
                    COLUMN_ORDER_STATUS + " TEXT, " +
                    COLUMN_TOTAL_AMOUNT + " REAL, " +
                    COLUMN_DATE + " TEXT, " +
                    COLUMN_COMPLETED + " INTEGER DEFAULT 0" +
                    ");";


    private static final String CREATE_TABLE_ORDER_ITEMS =
            "CREATE TABLE " + TABLE_ORDER_ITEMS + " (" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_ORDERITEM_ORDER_ID + " INTEGER, " +
                    COLUMN_ORDERITEM_MENU_ITEM_ID + " INTEGER, " +
                    COLUMN_ORDERITEM_QUANTITY + " INTEGER, " +
                    COLUMN_ORDERITEM_PRICE + " REAL, " + // Include the price column here
                    "FOREIGN KEY(" + COLUMN_ORDERITEM_ORDER_ID + ") REFERENCES " + TABLE_ORDERS + "(" + COLUMN_ID + "), " +
                    "FOREIGN KEY(" + COLUMN_ORDERITEM_MENU_ITEM_ID + ") REFERENCES " + TABLE_MENU_ITEMS + "(" + COLUMN_ID + ")" +
                    ");";

    private static final String CREATE_TABLE_CUSTOMERS =
            "CREATE TABLE " + TABLE_CUSTOMERS + " (" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_CUSTOMER_EMAIL + " TEXT UNIQUE, " +
                    COLUMN_CUSTOMER_NAME + " TEXT, " +
                    COLUMN_CUSTOMER_PHONE + " TEXT, " +
                    COLUMN_CUSTOMER_ADDRESS + " TEXT" +
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

    private static final String CREATE_TABLE_PROMO_CODES = "CREATE TABLE " + TABLE_PROMO_CODES + " ("
            + COLUMN_PROMO_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + COLUMN_PROMO_CODE + " TEXT, "
            + COLUMN_PROMO_DISCOUNT_PERCENT + " REAL, "
            + COLUMN_PROMO_EXPIRY_DATE + " TEXT)";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TABLE_ORDERS_CREATE);
        db.execSQL(CREATE_TABLE_ORDER_ITEMS);
        db.execSQL(CREATE_TABLE_CUSTOMERS);
        db.execSQL(TABLE_USERS_CREATE);
        db.execSQL(TABLE_MENU_ITEMS_CREATE);
        db.execSQL(TABLE_MENU_ITEM_CATEGORY_CREATE);
        db.execSQL(CREATE_TABLE_PROMO_CODES);

        // Insert default categories
        insertDefaultCategories(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        if (oldVersion < 2) {
            // Upgrade logic for version 2
            Cursor cursor = db.rawQuery("PRAGMA table_info(" + TABLE_USERS + ")", null);

            if (cursor != null) {
                boolean hasDeliveryAddress = false;
                boolean hasPhone = false;
                boolean hasImageUri = false;

                try {
                    int nameIndex = cursor.getColumnIndex("name");
                    if (nameIndex == -1) {
                        // Log or handle the error
                        return;
                    }

                    while (cursor.moveToNext()) {
                        String columnName = cursor.getString(nameIndex);
                        if (COLUMN_DELIVERY_ADDRESS.equals(columnName)) {
                            hasDeliveryAddress = true;
                        } else if (COLUMN_PHONE.equals(columnName)) {
                            hasPhone = true;
                        } else if (COLUMN_IMAGE_URI.equals(columnName)) {
                            hasImageUri = true;
                        }
                    }

                    if (!hasDeliveryAddress) {
                        db.execSQL("ALTER TABLE " + TABLE_USERS + " ADD COLUMN " + COLUMN_DELIVERY_ADDRESS + " TEXT;");
                    }
                    if (!hasPhone) {
                        db.execSQL("ALTER TABLE " + TABLE_USERS + " ADD COLUMN " + COLUMN_PHONE + " TEXT;");
                    }
                    if (!hasImageUri) {
                        db.execSQL("ALTER TABLE " + TABLE_USERS + " ADD COLUMN " + COLUMN_IMAGE_URI + " TEXT;");
                    }
                } finally {
                    cursor.close();
                }
            }
        }

        if (oldVersion < 3) {
            // Drop tables if needed for version 3
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_PROMO_CODES);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_MENU_ITEM_CATEGORY);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_MENU_ITEMS);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_CUSTOMERS);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_ORDER_ITEMS);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_ORDERS);

            // Recreate tables
            onCreate(db);
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

    // New method to get delivery address by user email
    public String getDeliveryAddress(String userEmail) {
        SQLiteDatabase db = this.getReadableDatabase();
        String address = null;

        String query = "SELECT " + COLUMN_DELIVERY_ADDRESS + " FROM " + TABLE_USERS +
                " WHERE " + COLUMN_USER_EMAIL + " = ?";
        Cursor cursor = db.rawQuery(query, new String[]{userEmail});

        if (cursor.moveToFirst()) {
            int addressIndex = cursor.getColumnIndex(COLUMN_DELIVERY_ADDRESS);
            if (addressIndex >= 0) {
                address = cursor.getString(addressIndex);
            }
        }

        cursor.close();
        db.close();
        return address;
    }

    // New method to update delivery address
    public boolean updateDeliveryAddress(String userEmail, String newAddress) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_DELIVERY_ADDRESS, newAddress);

        int rowsUpdated = db.update(TABLE_USERS, values,
                COLUMN_USER_EMAIL + " = ?", new String[]{userEmail});

        db.close();
        return rowsUpdated > 0;
    }

    // Method to get all orders for the logged-in user
    public ArrayList<Order> getAllOrdersByUser(String userEmail) {
        ArrayList<Order> orders = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_ORDERS,
                new String[]{COLUMN_ID, COLUMN_USER_EMAIL_ORDERS, COLUMN_ORDER_STATUS, COLUMN_TOTAL_AMOUNT, COLUMN_DATE, COLUMN_COMPLETED},
                COLUMN_USER_EMAIL_ORDERS + "=?",
                new String[]{userEmail}, null, null, null);

        if (cursor != null) {
            while (cursor.moveToNext()) {
                int orderId = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID));
                String userEmailOrder = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_USER_EMAIL_ORDERS));
                String orderStatus = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ORDER_STATUS));
                String totalAmount = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TOTAL_AMOUNT));
                String orderDate = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DATE));
                boolean completed = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_COMPLETED)) == 1;

                Order order = new Order(orderId, userEmailOrder, orderStatus, totalAmount, orderDate, completed);
                orders.add(order);
            }
            cursor.close();
        }

        return orders;
    }


    public ArrayList<Order> getOrdersByStatus(String status) {
        ArrayList<Order> orders = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_ORDERS,
                new String[]{COLUMN_ID, COLUMN_USER_EMAIL_ORDERS, COLUMN_ORDER_STATUS, COLUMN_TOTAL_AMOUNT, COLUMN_DATE, COLUMN_COMPLETED},
                COLUMN_ORDER_STATUS + "=?",
                new String[]{status}, null, null, null);

        if (cursor != null) {
            while (cursor.moveToNext()) {
                int orderId = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID));
                String userEmailOrder = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_USER_EMAIL_ORDERS));
                String orderStatus = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ORDER_STATUS));
                String totalAmount = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TOTAL_AMOUNT));
                String orderDate = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DATE));
                boolean completed = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_COMPLETED)) == 1;

                Order order = new Order(orderId, userEmailOrder, orderStatus, totalAmount, orderDate, completed);
                orders.add(order);
            }
            cursor.close();
        }

        return orders;
    }


    // Method to update the status of an order
    public boolean updateOrderStatus(int orderId, String newStatus) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_ORDER_STATUS, newStatus);

        int rowsUpdated = db.update(TABLE_ORDERS, values,
                COLUMN_ID + " = ?", new String[]{String.valueOf(orderId)});

        db.close();
        return rowsUpdated > 0;
    }


    public ArrayList<Customer> getAllCustomers() {
        ArrayList<Customer> customers = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT c.*, u.image_uri FROM " + TABLE_CUSTOMERS + " c LEFT JOIN " + TABLE_USERS + " u ON c." + COLUMN_CUSTOMER_EMAIL + " = u." + COLUMN_USER_EMAIL;

        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            do {
                String email = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CUSTOMER_EMAIL));
                String name = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CUSTOMER_NAME));
                String phone = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CUSTOMER_PHONE));
                String address = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CUSTOMER_ADDRESS));
                String imageUrl = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_IMAGE_URI));

                customers.add(new Customer(email, name, phone, address, imageUrl));
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return customers;
    }


    public ArrayList<Order> getOrdersByCustomerEmail(String userEmail) {
        ArrayList<Order> orders = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        String query = "SELECT * FROM " + TABLE_ORDERS + " WHERE " + COLUMN_USER_EMAIL + " = ?";
        Cursor cursor = db.rawQuery(query, new String[]{userEmail});

        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow("_id"));
                String email = cursor.getString(cursor.getColumnIndexOrThrow("user_email"));
                String status = cursor.getString(cursor.getColumnIndexOrThrow("order_status"));
                String totalAmount = cursor.getString(cursor.getColumnIndexOrThrow("total_amount"));
                String date = cursor.getString(cursor.getColumnIndexOrThrow("order_date"));
                boolean completed = cursor.getInt(cursor.getColumnIndexOrThrow("completed")) > 0;

                orders.add(new Order(id, email, status, totalAmount, date, completed));
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return orders;
    }


    public ArrayList<String> getAllCustomerEmails() {
        ArrayList<String> emails = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_CUSTOMERS,
                new String[]{COLUMN_CUSTOMER_EMAIL},
                null, null, null, null, null);

        if (cursor != null) {
            while (cursor.moveToNext()) {
                String email = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CUSTOMER_EMAIL));
                emails.add(email);
            }
            cursor.close();
        }

        db.close();
        return emails;
    }

    public Order getOrderById(int orderId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;
        Order order = null;

        try {
            cursor = db.query("orders", null, "id = ?", new String[]{String.valueOf(orderId)}, null, null, null);

            if (cursor != null && cursor.moveToFirst()) {
                // Ensure column names are valid
                int idIndex = cursor.getColumnIndex("id");
                int userEmailIndex = cursor.getColumnIndex("user_email");
                int statusIndex = cursor.getColumnIndex("order_status");
                int totalAmountIndex = cursor.getColumnIndex("total_amount");
                int dateIndex = cursor.getColumnIndex("order_date");
                int completedIndex = cursor.getColumnIndex("completed");

                // Check for valid column indices
                if (idIndex != -1 && userEmailIndex != -1 && statusIndex != -1 && totalAmountIndex != -1 && dateIndex != -1 && completedIndex != -1) {
                    int id = cursor.getInt(idIndex);
                    String userEmail = cursor.getString(userEmailIndex);
                    String status = cursor.getString(statusIndex);
                    String totalAmount = cursor.getString(totalAmountIndex);
                    String date = cursor.getString(dateIndex);
                    boolean completed = cursor.getInt(completedIndex) > 0;

                    order = new Order(id, userEmail, status, totalAmount, date, completed);
                } else {
                    // Handle case where column names are invalid or missing
                    Log.e("DatabaseHelper", "One or more columns are missing in the cursor.");
                }
            }
        } catch (Exception e) {
            // Handle exceptions (e.g., log them)
            Log.e("DatabaseHelper", "Error fetching order by ID", e);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }

        return order;
    }





}