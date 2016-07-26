package com.example.hanbyeol.sure_park;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by hanbyeol on 2016-07-25.
 */
public class MySQLiteOpenHelperCard extends SQLiteOpenHelper {
    public MySQLiteOpenHelperCard(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "create table card summary" +
                "(_id integer primary key autoincrement," +
                "cardNumber text, cardHolder text, cardExpirationMonth text, cardExpirationYear text, cardValidationCode text);";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        String sql = "drop table if exists card summary;";
        db.execSQL(sql);
        onCreate(db);
    }
}
