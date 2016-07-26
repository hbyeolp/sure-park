package com.example.hanbyeol.sure_park;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by hanbyeol on 2016-07-25.
 */
public class MySQLiteOpenHelperReservation extends SQLiteOpenHelper {
    public MySQLiteOpenHelperReservation(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "create table reservation summary" +
                "(_id integer primary key autoincrement," +
                "reservationid text, parkinglotname text, parkinglotid text, reservationtime text, carsize text, email text, phonenumber text);";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        String sql = "drop table if exists reservation summary";
        db.execSQL(sql);
        onCreate(db);
    }
}
