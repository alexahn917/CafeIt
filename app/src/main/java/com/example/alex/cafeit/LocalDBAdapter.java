package com.example.alex.cafeit;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;

public class LocalDBAdapter {
    private SQLiteDatabase db;
    private LocalDBhelper dbHelper;

    private static final String DB_NAME = "lesson.db";
    private static int dbVersion = 1;

    private static final String ORDER_TABLE = "orders";
    private static final String ORDER_ID = "order_id";   // column 0
    private static final String ORDER_ITEM_NAME = "order_item_name";
    private static final String ORDER_SIZE = "order_size";
    private static final String ORDER_QUANTITY = "order_quantity";
    private static final String ORDER_DATE = "order_date";
    private static final String ORDER_TIME = "order_time";
    private static final String ORDER_CAFE = "order_cafe";
    private static final String ORDER_PRICE = "order_price";
    private static final String ORDER_NOTE = "order_note";
    private static final String ORDER_CUSTOMER_NAME = "order_customer_name";
    private static final String ORDER_IMAGE_URL = "order_image_url";
    private static final String ORDER_IS_FAVORITE = "order_is_favorite";
    private static final String[] ORDER_COLS = {ORDER_ID, ORDER_ITEM_NAME, ORDER_SIZE, ORDER_QUANTITY,
            ORDER_DATE, ORDER_TIME, ORDER_CAFE, ORDER_PRICE, ORDER_NOTE, ORDER_CUSTOMER_NAME,
            ORDER_IMAGE_URL, ORDER_IS_FAVORITE};

    public LocalDBAdapter(Context ctx) {
        dbHelper = new LocalDBhelper(ctx, DB_NAME, null, dbVersion);
    }

    public void open() throws SQLiteException {
        try {
            db = dbHelper.getWritableDatabase();
        } catch (SQLiteException ex) {
            db = dbHelper.getReadableDatabase();
        }
    }

    public void close() {
        db.close();
    }

    public void clear() {
        dbHelper.onUpgrade(db, dbVersion, dbVersion+1);  // change version to dump old data
        dbVersion++;
    }

    // database update methods

    public long insertItem(Order order) {
        // create a new row of values to insert
        ContentValues cvalues = new ContentValues();
        // assign values for each col
        SimpleDateFormat to = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        SimpleDateFormat from = new SimpleDateFormat("MM/dd/yyyy", Locale.US);
        try {
            cvalues.put(ORDER_DATE, to.format(from.parse(order.orderTime)));
        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }
        cvalues.put(ORDER_ITEM_NAME, order.itemName);
        cvalues.put(ORDER_SIZE, order.size);
        cvalues.put(ORDER_QUANTITY, order.quantity);
        cvalues.put(ORDER_TIME, order.orderTime);
        cvalues.put(ORDER_CAFE, order.cafeName);
        cvalues.put(ORDER_PRICE, order.price);
        cvalues.put(ORDER_NOTE, order.note);
        cvalues.put(ORDER_CUSTOMER_NAME, order.customerName);
        if (order.image_url != null) {
            cvalues.put(ORDER_IMAGE_URL, order.image_url.toString());
        }
        cvalues.put(ORDER_IS_FAVORITE, order.is_favorite);
        // add to course table in database
        return db.insert(ORDER_TABLE, null, cvalues);
    }

    public boolean removeItem(long lid) {
        return db.delete(ORDER_TABLE, "ORDER_ID="+lid, null) > 0;
    }

    public boolean updateStringField(long lid, int field, String wh) {
        ContentValues cvalue = new ContentValues();
        if (field == 4) {
            SimpleDateFormat to = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
            SimpleDateFormat from = new SimpleDateFormat("MM/dd/yyyy", Locale.US);
            try {
                wh = to.format(from.parse(wh));
            } catch (java.text.ParseException e) {
                e.printStackTrace();
            }
        }
        cvalue.put(ORDER_COLS[field], wh);
        return db.update(ORDER_TABLE, cvalue, ORDER_ID +"="+lid, null) > 0;
    }

    public boolean updatePrice(long lid, float p) {
        ContentValues cvalue = new ContentValues();
        cvalue.put(ORDER_PRICE, p);
        return db.update(ORDER_TABLE, cvalue, ORDER_ID +"="+lid, null) > 0;
    }

    public boolean updateIntField(long lid, int field, int val) {
        ContentValues cvalue = new ContentValues();
        cvalue.put(ORDER_COLS[field], val);
        return db.update(ORDER_TABLE, cvalue, ORDER_ID +"="+lid, null) > 0;
    }

    public boolean updateIsFavorite(long lid, boolean fav) {
        ContentValues cvalue = new ContentValues();
        cvalue.put(ORDER_IS_FAVORITE, fav);
        return db.update(ORDER_TABLE, cvalue, ORDER_ID +"="+lid, null) > 0;
    }

    // database query methods
    public Cursor getAllItems() {
        return db.query(ORDER_TABLE, ORDER_COLS, null, null, null, null, "date(" + ORDER_DATE + ") ASC", null);
    }

    public Cursor getFavorites() {
        return db.query(ORDER_TABLE, ORDER_COLS, ORDER_IS_FAVORITE + " = ?",
                new String[]{"1"}, null, null, "date(" + ORDER_DATE + ") ASC", null);
    }

    public Cursor getItemCursor(long lid) throws SQLException {
        Cursor result = db.query(true, ORDER_TABLE, ORDER_COLS, ORDER_ID +"="+lid, null, null, null, null, null);
        if ((result.getCount() == 0) || !result.moveToFirst()) {
            throw new SQLException("No local items found for row: " + lid);
        }
        return result;
    }

    public Order getOrder(long lid) throws SQLException {
        Cursor cursor = db.query(true, ORDER_TABLE, ORDER_COLS, ORDER_ID +"="+lid, null, null, null, null, null);
        if ((cursor.getCount() == 0) || !cursor.moveToFirst()) {
            throw new SQLException("No local items found for row: " + lid);
        }

        SimpleDateFormat from = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        SimpleDateFormat to = new SimpleDateFormat("MM/dd/yyyy", Locale.US);

        // must use column indices to get column values
        String date = "01/01/2017";
        try {
            date = to.format(from.parse(cursor.getString(4)));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        URL im_url;
        try {
            im_url = new URL(cursor.getString(10));
        } catch (MalformedURLException e) {
            im_url = null;
        }

        Order o =  new Order(cursor.getString(1), cursor.getString(2), cursor.getInt(3),
                date, cursor.getInt(5), cursor.getString(6), cursor.getFloat(7),
                cursor.getString(8), cursor.getString(9), im_url, cursor.getInt(11) != 0);
        cursor.close();
        return o;
    }


    private static class LocalDBhelper extends SQLiteOpenHelper {

        // SQL statement to create a new database
        private static final String DB_CREATE = "CREATE TABLE " + ORDER_TABLE
                + " (" + ORDER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + ORDER_ITEM_NAME + " TEXT, "
                + ORDER_SIZE + " TEXT, " + ORDER_QUANTITY + " INTEGER, " + ORDER_DATE + " TEXT, " +
                ORDER_TIME + " INTEGER, " + ORDER_CAFE + " TEXT, " + ORDER_PRICE + " REAL, " +
                ORDER_NOTE + " TEXT, " + ORDER_CUSTOMER_NAME + " TEXT, " + ORDER_IMAGE_URL + " TEXT, "
                + ORDER_IS_FAVORITE + " INTEGER);";

        public LocalDBhelper(Context context, String name, SQLiteDatabase.CursorFactory fct,
                               int version) {
            super(context, name, fct, version);
        }

        @Override
        public void onCreate(SQLiteDatabase adb) {
            // TODO Auto-generated method stub
            adb.execSQL(DB_CREATE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase adb, int oldVersion, int newVersion) {
            // TODO Auto-generated method stub
            Log.w("LocalDB", "upgrading from version " + oldVersion + " to "
                    + newVersion + ", destroying old data");
            // drop old table if it exists, create new one
            // better to migrate existing data into new table
            adb.execSQL("DROP TABLE IF EXISTS " + ORDER_TABLE);
            onCreate(adb);
        }
    }
}
