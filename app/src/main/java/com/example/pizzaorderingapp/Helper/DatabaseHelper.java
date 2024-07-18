package com.example.pizzaorderingapp.Database;



import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "pizzaOrderingApp.db";
    private static final int DATABASE_VERSION = 1;

    public static final String TABLE_ORDERS = "orders";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_USER_EMAIL = "user_email";
    public static final String COLUMN_ORDER_DETAILS = "order_details";
    public static final String COLUMN_TOTAL_AMOUNT = "total_amount";
    public static final String COLUMN_DATE = "order_date";

    private static final String TABLE_CREATE =
            "CREATE TABLE " + TABLE_ORDERS + " (" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_USER_EMAIL + " TEXT, " +
                    COLUMN_ORDER_DETAILS + " TEXT, " +
                    COLUMN_TOTAL_AMOUNT + " TEXT, " +
                    COLUMN_DATE + " TEXT" +
                    ");";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TABLE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ORDERS);
        onCreate(db);
    }
}
