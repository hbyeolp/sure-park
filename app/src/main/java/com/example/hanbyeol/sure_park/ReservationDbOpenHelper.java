package com.example.hanbyeol.sure_park;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.ArrayAdapter;
import android.widget.ListView;

/**
 * Created by hanbyeol on 2016-07-26.
 */
public class ReservationDbOpenHelper {
    private static final String DATABASE_NAME = "reservation.db";
    private static final int DATABASE_VERSION = 1;
    private static final String _TABLENAME = "reservation";
    public static SQLiteDatabase mDB;
    private DatabaseHelper mDBHelper;
    private Context mCtx;
    private class DatabaseHelper extends SQLiteOpenHelper {

        public DatabaseHelper(Context context, String name,
                              SQLiteDatabase.CursorFactory factory, int version) {

            super(context, name, factory, version);

        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            System.out.println(DataBases.ReservationCreateDB._CREATE);
            db.execSQL(DataBases.ReservationCreateDB._CREATE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS "+_TABLENAME);
            onCreate(db);
        }
    }

    public ReservationDbOpenHelper(Context context){
        this.mCtx = context;
    }

    public ReservationDbOpenHelper open() throws SQLException {
        mDBHelper = new DatabaseHelper(mCtx, DATABASE_NAME, null, DATABASE_VERSION);
        mDB = mDBHelper.getWritableDatabase();
        return this;
    }

    public void close(){
        mDB.close();
    }

    void Delete(String cardNumber) {
        int result = mDB.delete(_TABLENAME, "cardNumber=?", new String[] {cardNumber});
    }

    void Update (String id, String parkingLotName, String email, int carSize, String reservationTime, String entranceTime, String exitTime) {
        ContentValues values = new ContentValues();
        values.put("parkingLotName", parkingLotName);
        values.put("email", email);
        values.put("carSize", carSize);
        values.put("reservationTime", reservationTime);
        values.put("entranceTime", entranceTime);
        values.put("exitTime", exitTime);
        int result = mDB.update(_TABLENAME,
                values,
                "_id=?",
                new String[]{SelectID(id)});
    }
    String SelectID(String id){
        Cursor c = mDB.query(_TABLENAME, null, null, null, null, null, null);
        int _id=0;
        if(c.moveToLast()) {
            _id = c.getInt(c.getColumnIndex("_id"));
            id = Integer.toString(_id);
        }
        return id;
    }
    void UpdateListView(ListViewReservationAdapter arrayAdapter){
        Cursor c = mDB.query(_TABLENAME, null, null, null, null, null, null);
        System.out.println("Update List");
        if(c.getCount()!=0) {
            String carsize="abc";
            c.moveToLast();
            switch (c.getInt(c.getColumnIndex("carSize"))){
                case 1:
                    carsize="Small";
                    break;
                case 2:
                    carsize="Mid-size";
                    break;
                case 3:
                    carsize="Full-size";
                    break;
            }
            System.out.println(carsize);
            arrayAdapter.addItem(c.getString(1),c.getString(2),carsize,c.getString(4),c.getString(5),c.getString(6));
            arrayAdapter.notifyDataSetChanged();
            System.out.println("array add");
            while (c.moveToPrevious()) {
                arrayAdapter.addItem(c.getString(1),c.getString(2),carsize,c.getString(4),c.getString(5),c.getString(6));
                arrayAdapter.notifyDataSetChanged();
            }
        }
    }
    int Count () {
        Cursor c = mDB.query(_TABLENAME, null, null, null, null, null, null);
        return c.getCount();
    }

    long Insert (String parkingLotName, String email, int carSize, String reservationTime, String entranceTime, String exitTime) {
        ContentValues values = new ContentValues();
        values.put("parkingLotName", parkingLotName);
        values.put("email", email);
        values.put("carSize", carSize);
        values.put("reservationTime", reservationTime);
        values.put("entranceTime", entranceTime);
        values.put("exitTime", exitTime);

        return mDB.insert(_TABLENAME, null, values);
    }
}
