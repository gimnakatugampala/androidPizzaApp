package com.example.pizzaorderingapp.Repository;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.pizzaorderingapp.Helper.DatabaseHelper;
import com.example.pizzaorderingapp.Model.PromoCode;

import java.util.ArrayList;
import java.util.List;

public class PromoCodeRepository {

    private static final String TAG = "PromoCodeRepository";
    private DatabaseHelper dbHelper;

    public PromoCodeRepository(Context context) {
        dbHelper = new DatabaseHelper(context);
    }

    public boolean addPromoCode(PromoCode promoCode) {
        SQLiteDatabase db = null;
        try {
            db = dbHelper.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(DatabaseHelper.COLUMN_PROMO_CODE, promoCode.getPromoCode());
            values.put(DatabaseHelper.COLUMN_PROMO_DISCOUNT_PERCENT, promoCode.getDiscountPercentage());
            values.put(DatabaseHelper.COLUMN_PROMO_EXPIRY_DATE, promoCode.getExpiryDate());

            long result = db.insert(DatabaseHelper.TABLE_PROMO_CODES, null, values);
            return result != -1;
        } catch (SQLException e) {
            Log.e(TAG, "Error adding promo code", e);
            return false;
        } finally {
            if (db != null && db.isOpen()) {
                db.close();
            }
        }
    }

    public List<PromoCode> getAllPromoCodes() {
        List<PromoCode> promoCodes = new ArrayList<>();
        SQLiteDatabase db = null;
        Cursor cursor = null;

        try {
            db = dbHelper.getReadableDatabase();
            cursor = db.query(DatabaseHelper.TABLE_PROMO_CODES, null, null, null, null, null, null);

            if (cursor.moveToFirst()) {
                int promoCodeIdIndex = cursor.getColumnIndex(DatabaseHelper.COLUMN_PROMO_ID);
                int promoCodeIndex = cursor.getColumnIndex(DatabaseHelper.COLUMN_PROMO_CODE);
                int discountPercentIndex = cursor.getColumnIndex(DatabaseHelper.COLUMN_PROMO_DISCOUNT_PERCENT);
                int expiryDateIndex = cursor.getColumnIndex(DatabaseHelper.COLUMN_PROMO_EXPIRY_DATE);

                // Check if indices are valid
                if (promoCodeIdIndex != -1 && promoCodeIndex != -1 && discountPercentIndex != -1 && expiryDateIndex != -1) {
                    do {
                        PromoCode promoCode = new PromoCode();
                        promoCode.setId(cursor.getInt(promoCodeIdIndex));
                        promoCode.setPromoCode(cursor.getString(promoCodeIndex));
                        promoCode.setDiscountPercentage(cursor.getDouble(discountPercentIndex));
                        promoCode.setExpiryDate(cursor.getString(expiryDateIndex));

                        promoCodes.add(promoCode);
                    } while (cursor.moveToNext());
                } else {
                    Log.e(TAG, "One or more columns not found in cursor.");
                }
            }
        } catch (SQLException e) {
            Log.e(TAG, "Error retrieving promo codes", e);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            if (db != null && db.isOpen()) {
                db.close();
            }
        }
        return promoCodes;
    }

    public boolean deletePromoCode(int id) {
        SQLiteDatabase db = null;
        try {
            db = dbHelper.getWritableDatabase();
            int result = db.delete(DatabaseHelper.TABLE_PROMO_CODES, DatabaseHelper.COLUMN_PROMO_ID + "=?", new String[]{String.valueOf(id)});
            return result > 0;
        } catch (SQLException e) {
            Log.e(TAG, "Error deleting promo code", e);
            return false;
        } finally {
            if (db != null && db.isOpen()) {
                db.close();
            }
        }
    }
}
