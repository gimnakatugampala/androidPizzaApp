package com.example.pizzaorderingapp.Helper;
import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.text.TextUtils;
import android.util.Log;

import com.example.pizzaorderingapp.Model.Order;
import com.example.pizzaorderingapp.Model.Customer;
import com.example.pizzaorderingapp.Model.MenuItem;


import com.example.pizzaorderingapp.Domain.CategoryDomain;
import com.example.pizzaorderingapp.Model.OrderItem;

import java.util.ArrayList;
import java.util.List;
import com.example.pizzaorderingapp.Model.Store;
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
    private static final String COLUMN_IS_DELETED = "is_deleted";

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

//    Favorites
private static final String TABLE_FAVORITES = "favorites";
    private static final String KEY_USER_EMAIL = "user_email";
    private static final String KEY_ORDER_ID = "order_id";


    // Stores Table Schema
    private static final String TABLE_STORES = "stores";
    private static final String KEY_STORE_NAME = "name";
    private static final String KEY_LATITUDE = "latitude";
    private static final String KEY_LONGITUDE = "longitude";

    private static final String CREATE_TABLE_STORES = "CREATE TABLE " + TABLE_STORES + " ("
            + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
            + KEY_STORE_NAME + " TEXT NOT NULL,"
            + KEY_LATITUDE + " REAL NOT NULL,"
            + KEY_LONGITUDE + " REAL NOT NULL"
            + ");";


    private static final String CREATE_TABLE_FAVORITES = "CREATE TABLE " + TABLE_FAVORITES + " ("
            + KEY_USER_EMAIL + " TEXT,"
            + KEY_ORDER_ID + " TEXT,"
            + "PRIMARY KEY (" + KEY_USER_EMAIL + ", " + KEY_ORDER_ID + "));";


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
        db.execSQL(CREATE_TABLE_FAVORITES);
        db.execSQL(CREATE_TABLE_STORES);


        // Insert default categories
//        insertDefaultCategories(db);
//        Add Admin
//        insertDefaultAdmin(db);

        // Insert default stores
//        insertStores(db);
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
            db.execSQL("DROP TABLE IF EXISTS stores"); // Replace with the actual table name


            // Recreate tables
            onCreate(db);
        }
    }

    private void insertDefaultAdmin(SQLiteDatabase db) {
        // Define default admin data
        String adminEmail = "admin@gmail.com";
        String adminPassword = "12345678"; // Ensure to hash the password in real applications
        String adminRole = "Admin"; // or use whatever role designation is suitable
        String adminFirstName = "Admin";
        String adminLastName = "User";
        String adminProfileImageUrl = ""; // Use a default profile image URL if available

        // Insert admin data
        insertAdmin(db, adminEmail, adminPassword, adminRole, adminFirstName, adminLastName, adminProfileImageUrl);
    }

    private void insertAdmin(SQLiteDatabase db, String email, String password, String role, String firstName, String lastName, String profileImageUrl) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_USER_EMAIL, email);
        values.put(COLUMN_PASSWORD, password); // Consider hashing the password before storing
        values.put(COLUMN_ROLE, role);
        values.put(COLUMN_FIRST_NAME, firstName);
        values.put(COLUMN_LAST_NAME, lastName);
        values.put(COLUMN_IMAGE_URI, profileImageUrl);
        db.insert(TABLE_USERS, null, values); // Replace TABLE_USER with your actual table name
    }

    private void insertStores(SQLiteDatabase db) {
        insertStore(db, "Store 1", 37.3382, -121.8863); // San Jose
        insertStore(db, "Store 2", 37.3541, -121.9552); // Santa Clara
        insertStore(db, "Store 3", 37.3220, -121.8832); // South San Jose
        insertStore(db, "Store 4", 37.3688, -122.0363); // Sunnyvale
        insertStore(db, "Store 5", 37.7749, -122.4194); // San Francisco
        insertStore(db, "Store 6", 37.4419, -122.1430); // Palo Alto
    }

    private void insertStore(SQLiteDatabase db, String storeName, double latitude, double longitude) {
        ContentValues values = new ContentValues();
        values.put("name", storeName);
        values.put("latitude", latitude);
        values.put("longitude", longitude);
        db.insert("stores", null, values);
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
        Cursor cursor = null;

        try {
            cursor = db.query(
                    TABLE_ORDERS,
                    new String[]{COLUMN_ID, COLUMN_USER_EMAIL_ORDERS, COLUMN_ORDER_STATUS, COLUMN_TOTAL_AMOUNT, COLUMN_DATE, COLUMN_COMPLETED},
                    COLUMN_USER_EMAIL_ORDERS + "=?",
                    new String[]{userEmail},
                    null,
                    null,
                    null
            );

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
            }
        } catch (Exception e) {
            e.printStackTrace(); // You might want to log this error or handle it differently
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }

        return orders;
    }

    public List<Integer> getOrderItemIds(int orderId) {
        List<Integer> itemIds = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT menu_item_id FROM Order_items WHERE order_id = ?", new String[]{String.valueOf(orderId)});
        if (cursor.moveToFirst()) {
            do {
                itemIds.add(cursor.getInt(cursor.getColumnIndex("menu_item_id")));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return itemIds;
    }

    // DatabaseHelper.java
    public List<MenuItem> getMenuItemsByIds(List<Integer> itemIds) {
        List<MenuItem> menuItems = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String ids = TextUtils.join(",", itemIds);
        Cursor cursor = db.rawQuery("SELECT * FROM MenuItem WHERE id IN (" + ids + ")", null);
        if (cursor.moveToFirst()) {
            do {
                MenuItem menuItem = new MenuItem();
                menuItem.setId(cursor.getInt(cursor.getColumnIndex("id")));
                menuItem.setName(cursor.getString(cursor.getColumnIndex("name")));
                menuItem.setDescription(cursor.getString(cursor.getColumnIndex("description")));
                menuItem.setPrice(cursor.getDouble(cursor.getColumnIndex("price")));
                menuItem.setCategory(cursor.getString(cursor.getColumnIndex("category")));
                menuItem.setToppings(cursor.getString(cursor.getColumnIndex("toppings")));
                menuItem.setImageUri(cursor.getString(cursor.getColumnIndex("imageUri")));
                menuItems.add(menuItem);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return menuItems;
    }

    private List<com.example.pizzaorderingapp.Model.OrderItem> fetchOrderItems(int orderId) {
        List<OrderItem> orderItems = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT oi.quantity, oi.price, mi.name FROM order_items oi " +
                "JOIN menu_items mi ON oi.menu_item_id = mi._id " +
                "WHERE oi.order_id = ?", new String[]{String.valueOf(orderId)});

        if (cursor.moveToFirst()) {
            do {
                int quantity = cursor.getInt(cursor.getColumnIndexOrThrow("quantity"));
                double price = cursor.getDouble(cursor.getColumnIndexOrThrow("price"));
                String name = cursor.getString(cursor.getColumnIndexOrThrow("name"));

                orderItems.add(new OrderItem(name, quantity, price));
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();

        return orderItems;
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
            cursor = db.query(TABLE_ORDERS, null, COLUMN_ID + " = ?", new String[]{String.valueOf(orderId)}, null, null, null);

            if (cursor != null && cursor.moveToFirst()) {
                int idIndex = cursor.getColumnIndex(COLUMN_ID);
                int userEmailIndex = cursor.getColumnIndex(COLUMN_USER_EMAIL_ORDERS);
                int statusIndex = cursor.getColumnIndex(COLUMN_ORDER_STATUS);
                int totalAmountIndex = cursor.getColumnIndex(COLUMN_TOTAL_AMOUNT);
                int dateIndex = cursor.getColumnIndex(COLUMN_DATE);
                int completedIndex = cursor.getColumnIndex(COLUMN_COMPLETED);

                if (idIndex != -1 && userEmailIndex != -1 && statusIndex != -1 && totalAmountIndex != -1 && dateIndex != -1 && completedIndex != -1) {
                    int id = cursor.getInt(idIndex);
                    String userEmail = cursor.getString(userEmailIndex);
                    String status = cursor.getString(statusIndex);
                    String totalAmount = cursor.getString(totalAmountIndex);
                    String date = cursor.getString(dateIndex);
                    boolean completed = cursor.getInt(completedIndex) > 0;

                    order = new Order(id, userEmail, status, totalAmount, date, completed);
                } else {
                    Log.e("DatabaseHelper", "One or more columns are missing in the cursor.");
                }
            }
        } catch (Exception e) {
            Log.e("DatabaseHelper", "Error fetching order by ID", e);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }

        return order;
    }


    public List<MenuItem> getAllMenuItems() {
        List<MenuItem> menuItems = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(
                TABLE_MENU_ITEMS,
                null,
                COLUMN_IS_DELETED + " = ?", // Filter condition
                new String[]{"0"}, // Value for filter condition
                null,
                null,
                null
        );

        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndex(COLUMN_ID));
                String name = cursor.getString(cursor.getColumnIndex(COLUMN_NAME));
                String description = cursor.getString(cursor.getColumnIndex(COLUMN_DESCRIPTION));
                double price = cursor.getDouble(cursor.getColumnIndex(COLUMN_PRICE));
                String category = cursor.getString(cursor.getColumnIndex(COLUMN_CATEGORY));
                String toppings = cursor.getString(cursor.getColumnIndex(COLUMN_TOPPINGS));
                String imageUri = cursor.getString(cursor.getColumnIndex(COLUMN_IMAGE_URI));
                menuItems.add(new MenuItem(id, name, description, price, category, toppings, imageUri));
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return menuItems;
    }


// Inside DatabaseHelper class

    public void addFavoriteOrder(Order order, String userEmail) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_USER_EMAIL, userEmail); // Use user's email
        values.put(KEY_ORDER_ID, order.getId());

        db.insert(TABLE_FAVORITES, null, values);
        db.close();
    }

    public List<Order> getFavoriteOrders(String userEmail) {
        List<Order> favoriteOrders = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;

        try {
            // Correct the query to use the exact column names from your schema
            String query = "SELECT o." + COLUMN_ID + ", o." + COLUMN_USER_EMAIL_ORDERS + ", o." + COLUMN_ORDER_STATUS + ", o." + COLUMN_TOTAL_AMOUNT + ", o." + COLUMN_DATE + ", o." + COLUMN_COMPLETED +
                    " FROM " + TABLE_ORDERS + " o " +
                    "INNER JOIN " + TABLE_FAVORITES + " f ON o." + COLUMN_ID + " = f." + COLUMN_ORDER_ID +
                    " WHERE f." + COLUMN_USER_EMAIL_ORDERS + " = ?";
            cursor = db.rawQuery(query, new String[]{userEmail});

            if (cursor.moveToFirst()) {
                do {
                    // Retrieve indices
                    int orderIdIndex = cursor.getColumnIndexOrThrow(COLUMN_ID);
                    int userEmailIndex = cursor.getColumnIndexOrThrow(COLUMN_USER_EMAIL_ORDERS);
                    int statusIndex = cursor.getColumnIndexOrThrow(COLUMN_ORDER_STATUS);
                    int totalAmountIndex = cursor.getColumnIndexOrThrow(COLUMN_TOTAL_AMOUNT);
                    int dateIndex = cursor.getColumnIndexOrThrow(COLUMN_DATE);
                    int completedIndex = cursor.getColumnIndexOrThrow(COLUMN_COMPLETED);

                    // Retrieve values
                    int orderId = cursor.getInt(orderIdIndex);
                    String userEmailOrder = cursor.getString(userEmailIndex);
                    String orderStatus = cursor.getString(statusIndex);
                    String totalAmount = cursor.getString(totalAmountIndex);
                    String orderDate = cursor.getString(dateIndex);
                    boolean completed = cursor.getInt(completedIndex) == 1;

                    // Create a new Order object
                    Order order = new Order(orderId, userEmailOrder, orderStatus, totalAmount, orderDate, completed);

                    // Add the Order object to the list
                    favoriteOrders.add(order);
                } while (cursor.moveToNext());
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }

        return favoriteOrders;
    }







    // New method to fetch order details by ID
    public Order getOrderForFavoritesById(String orderId) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_ORDERS + " WHERE " + KEY_ORDER_ID + " = ?";
        Cursor cursor = db.rawQuery(query, new String[]{orderId});
        Order order = null;

        if (cursor.moveToFirst()) {
            // Create an Order object and populate its fields
            int id = cursor.getInt(cursor.getColumnIndex(KEY_ORDER_ID));
            String userEmail = cursor.getString(cursor.getColumnIndex(KEY_USER_EMAIL));
            String status = cursor.getString(cursor.getColumnIndex(COLUMN_ORDER_STATUS));
            String totalAmount = cursor.getString(cursor.getColumnIndex(COLUMN_TOTAL_AMOUNT));
            String date = cursor.getString(cursor.getColumnIndex(COLUMN_DATE));
            boolean completed = cursor.getInt(cursor.getColumnIndex(COLUMN_COMPLETED)) > 0;

            order = new Order(id, userEmail, status, totalAmount, date, completed);
        }

        cursor.close();
        db.close();
        return order;
    }

    public List<Store> getAllStores() {
        List<Store> stores = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM stores", null); // Adjust query as needed

        if (cursor.moveToFirst()) {
            do {
                String name = cursor.getString(cursor.getColumnIndex("name"));
                double latitude = cursor.getDouble(cursor.getColumnIndex("latitude"));
                double longitude = cursor.getDouble(cursor.getColumnIndex("longitude"));
                stores.add(new Store(name, latitude, longitude));
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return stores;
    }

}