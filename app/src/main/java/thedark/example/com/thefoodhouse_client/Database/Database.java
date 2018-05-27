package thedark.example.com.thefoodhouse_client.Database;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;

import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

import java.util.ArrayList;
import java.util.List;

import thedark.example.com.thefoodhouse_client.Model.Order;

public class Database extends SQLiteAssetHelper {

    private static final String DB_NAME = "TheHouseFoodDB.db";
    private static final int DB_VER = 1;

    public Database(Context context) {
        super(context, DB_NAME, null, DB_VER);
    }

    public List<Order> getCarts() {
        SQLiteDatabase db = getReadableDatabase();
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();

        String[] sqlSelect = {"ID", "ProductId", "ProductName", "Quantity", "Price", "Discount"};
        String sqlTable = "OrderDetailt";

        qb.setTables(sqlTable);

        Cursor c = qb.query(db, sqlSelect, null, null, null, null, null);

        final List<Order> result = new ArrayList<>();

        if (c.moveToFirst()) {
            do {
                result.add(new Order(
                        c.getString(c.getColumnIndex("ID")),
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
        String query = String.format("INSERT INTO OrderDetailt (ProductId,ProductName,Quantity,Price,Discount)" +
                        "VALUES ('%s','%s','%s','%s','%s')",
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
        String query = String.format("DELETE FROM OrderDetailt");
        db.execSQL(query);
    }

    public int returnID(String productName) {
        SQLiteDatabase db = getReadableDatabase();
        String query = String.format("SELECT ID FROM OrderDetailt WHERE ProductName = '" + productName + "'");
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
        String query = String.format("DELETE FROM OrderDetailt WHERE ID = '" + id + "'");
        db.execSQL(query);
    }


}
