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

        // 생성자
        public DatabaseHelper(Context context, String name,
                              SQLiteDatabase.CursorFactory factory, int version) {

            super(context, name, factory, version);

        }

        // 최초 DB를 만들때 한번만 호출된다.
        @Override
        public void onCreate(SQLiteDatabase db) {
            System.out.println(DataBases.ReservationCreateDB._CREATE);
            db.execSQL(DataBases.ReservationCreateDB._CREATE);
        }

        // 버전이 업데이트 되었을 경우 DB를 다시 만들어 준다.
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

    void Update (String id, String email, String parkingLotID, String reservationTime, String carSize, String entranceTime, String exitTime) {
        ContentValues values = new ContentValues();
        values.put("email", email);
        values.put("parkingLotID", parkingLotID);
        values.put("reservationTime", reservationTime);
        values.put("carSize", carSize);
        values.put("entranceTime", entranceTime);
        values.put("exitTime", exitTime);
        int result = mDB.update(_TABLENAME,
                values,    // 뭐라고 변경할지 ContentValues 설정
                "_id=?", // 바꿀 항목을 찾을 조건절
                new String[]{SelectID(id)});// 바꿀 항목으로 찾을 값 String 배열
    }
    String SelectID(String id){
        Cursor c = mDB.query(_TABLENAME, null, null, null, null, null, null);
        int _id=0;
        if(c.moveToLast()) {
            _id = c.getInt(0);
        }
        id = Integer.toString(_id);
        return id;
    }
    void UpdateListView(ListViewReservationAdapter arrayAdapter){
        Cursor c = mDB.query(_TABLENAME, null, null, null, null, null, null);
        if(c.getCount()!=0) {
            c.moveToLast();
            arrayAdapter.addItem(c.getString(1),c.getString(2),c.getString(4),c.getString(3),c.getString(5),c.getString(6));
            System.out.println("array add");
            while (c.moveToPrevious()) {
                arrayAdapter.addItem(c.getString(1),c.getString(2),c.getString(4),c.getString(3),c.getString(5),c.getString(6));
            }
        }
    }
    int Count () {
        Cursor c = mDB.query(_TABLENAME, null, null, null, null, null, null);
        return c.getCount();
    }

    long Insert (String email, String parkingLotID, String reservationTime, String carSize, String entranceTime, String exitTime) {
        ContentValues values = new ContentValues();
        // 키,값의 쌍으로 데이터 입력
        values.put("email", email);
        values.put("parkingLotID", parkingLotID);
        values.put("carSize", carSize);
        values.put("reservationTime", reservationTime);
        values.put("entranceTime", entranceTime);
        values.put("exitTime", exitTime);

        return mDB.insert(_TABLENAME, null, values);
    }
}
