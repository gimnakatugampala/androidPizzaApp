package com.example.pizzaorderingapp.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.pizzaorderingapp.Models.Order;
import com.example.pizzaorderingapp.Helper.DatabaseHelper;


import java.util.ArrayList;
import java.util.List;

public class OrderRepository {

//    private DatabaseHelper dbHelper;
//
//    public OrderRepository(Context context) {
//        dbHelper = new DatabaseHelper(context);
//    }
//
//    public void addOrder(Order order) {
//        SQLiteDatabase db = dbHelper.getWritableDatabase();
//        ContentValues values = new ContentValues();
//        values.put(DatabaseHelper.COLUMN_USER_EMAIL, order.getUserEmail());
//        values.put(DatabaseHelper.COLUMN_ORDER_DETAILS, order.getOrderDetails());
//        values.put(DatabaseHelper.COLUMN_TOTAL_AMOUNT, order.getTotalAmount());
//        values.put(DatabaseHelper.COLUMN_DATE, order.getOrderDate());
//
//        db.insert(DatabaseHelper.TABLE_ORDERS, null, values);
//        db.close();
//    }
//
//    public List<Order> getOrdersByEmail(String email) {
//        List<Order> orders = new ArrayList<>();
//        SQLiteDatabase db = dbHelper.getReadableDatabase();
//
//        String[] columns = {
//                DatabaseHelper.COLUMN_USER_EMAIL,
//                DatabaseHelper.COLUMN_ORDER_DETAILS,
//                DatabaseHelper.COLUMN_TOTAL_AMOUNT,
//                DatabaseHelper.COLUMN_DATE
//        };
//
//        String selection = DatabaseHelper.COLUMN_USER_EMAIL + " = ?";
//        String[] selectionArgs = {email};
//
//        Cursor cursor = db.query(
//                DatabaseHelper.TABLE_ORDERS,
//                columns,
//                selection,
//                selectionArgs,
//                null,
//                null,
//                DatabaseHelper.COLUMN_DATE + " DESC"
//        );
//
//        if (cursor != null && cursor.moveToFirst()) {
//            do {
//                String userEmail = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_USER_EMAIL));
//                String orderDetails = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_ORDER_DETAILS));
//                String totalAmount = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_TOTAL_AMOUNT));
//                String orderDate = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_DATE));
//
//                orders.add(new Order(userEmail, orderDetails, totalAmount, orderDate));
//            } while (cursor.moveToNext());
//        }
//
//        if (cursor != null) {
//            cursor.close();
//        }
//
//        db.close();
//        return orders;
//    }
}
