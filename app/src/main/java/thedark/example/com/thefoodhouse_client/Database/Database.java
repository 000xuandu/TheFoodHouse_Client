package thedark.example.com.thefoodhouse_client.Database;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

import java.util.ArrayList;
import java.util.List;

import thedark.example.com.thefoodhouse_client.Model.Order;

public class Database extends SQLiteAssetHelper {

    private static final String DB_NAME = "CartTemp.db";
    private static final int DB_VER = 1;

    public Database(Context context) {
        super(context, DB_NAME, null, DB_VER);
    }

    public List<Order> getCarts(String phoneUser) {
        SQLiteDatabase db = getReadableDatabase();
        String query = "SELECT ID, PhoneUser,ProductId,ProductName,Quantity,Price,Discount " +
                "FROM CartTemp " +
                "WHERE PhoneUser = '" + phoneUser + "'";
        @SuppressLint("Recycle") Cursor c = db.rawQuery(query, null);

        final List<Order> result = new ArrayList<>();

        if (c.moveToFirst()) {
            do {
                result.add(new Order(
                        c.getString(c.getColumnIndex("ID")),
                        c.getString(c.getColumnIndex("PhoneUser")),
                        c.getString(c.getColumnIndex("ProductId")),
                        c.getString(c.getColumnIndex("ProductName")),
                        c.getString(c.getColumnIndex("Quantity")),
                        c.getString(c.getColumnIndex("Price")),
                        c.getString(c.getColumnIndex("Discount"))
                ));
            } while (c.moveToNext());
        }
        return result;
    }

    public void addToCart(Order order) {
        SQLiteDatabase db = getReadableDatabase();
        String query = String.format("INSERT INTO CartTemp (PhoneUser,ProductId,ProductName,Quantity,Price,Discount)" +
                        "VALUES ('%s','%s','%s','%s','%s','%s')",
                order.getUserPhone(),
                order.getProductId(),
                order.getProductName(),
                order.getQuantity(),
                order.getPrice(),
                order.getDiscount()
        );
        db.execSQL(query);
    }

    public void cleanCart() {
        SQLiteDatabase db = getReadableDatabase();
        String query = "DELETE FROM CartTemp";
        db.execSQL(query);
    }

    public int returnID(String productName) {
        SQLiteDatabase db = getReadableDatabase();
        String query = "SELECT ID FROM CartTemp WHERE ProductName = '" + productName + "'";
        @SuppressLint("Recycle") Cursor c = db.rawQuery(query, null);
        int resultId = 0;
        if (c != null) {
            c.moveToFirst();
            resultId = c.getColumnIndex("ID");
        }
        return resultId;
    }

    public void deleteItemCart(int id) {
        SQLiteDatabase db = getReadableDatabase();
        String query = "DELETE FROM CartTemp WHERE ID = '" + id + "'";
        db.execSQL(query);
    }


}
