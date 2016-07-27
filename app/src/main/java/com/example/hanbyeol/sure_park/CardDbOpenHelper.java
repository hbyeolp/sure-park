package com.example.hanbyeol.sure_park;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;

/**
 * Created by hanbyeol on 2016-07-26.
 */
public class CardDbOpenHelper {
    private static final String DATABASE_NAME = "card.db";
    private static final int DATABASE_VERSION = 1;
    private static final String _TABLENAME = "card";
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
            System.out.println(DataBases.CardCreateDB._CREATE);
            db.execSQL(DataBases.CardCreateDB._CREATE);
        }

        // 버전이 업데이트 되었을 경우 DB를 다시 만들어 준다.
        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS "+_TABLENAME);
            onCreate(db);
        }
    }

    public CardDbOpenHelper(Context context){
        this.mCtx = context;
    }

    public CardDbOpenHelper open() throws SQLException {
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

    void Update (String cardNumber, String cardHolder, String cardExpirationMonth, String cardExpirationYear, String cardValidationCode) {
        ContentValues values = new ContentValues();
        values.put("cardHolder", cardHolder);         // 바꿀값
        values.put("cardExpirationMonth", cardExpirationMonth); // 바꿀값
        values.put("cardExpirationYear", cardExpirationYear); // 바꿀값
        values.put("cardValidationCode", cardValidationCode); // 바꿀값

        int result = mDB.update(_TABLENAME,
                values,    // 뭐라고 변경할지 ContentValues 설정
                "cardNumber=?", // 바꿀 항목을 찾을 조건절
                new String[]{cardNumber});// 바꿀 항목으로 찾을 값 String 배열
    }

    int Count () {
        Cursor c = mDB.query(_TABLENAME, null, null, null, null, null, null);

        return c.getCount();
    }

    Boolean Compare (String cardNumber, String cardHolder, String cardExpirationMonth, String cardExpirationYear, String cardValidationCode) {
        Cursor c = mDB.query(_TABLENAME, null, null, null, null, null, null);
        while(c.moveToNext()) {
            if(cardNumber.equals(c.getString(0)) && cardHolder.equals(c.getString(1)) && cardExpirationMonth.equals(c.getString(2)) && cardExpirationYear.equals(c.getString(3)) && cardValidationCode.equals(c.getString(4))){
                return false;
            }
        }
        return true;
    }

    void Insert (String cardNumber, String cardHolder, String cardExpirationMonth, String cardExpirationYear, String cardValidationCode) {
        ContentValues values = new ContentValues();
        // 키,값의 쌍으로 데이터 입력
        values.put("cardNumber", cardNumber);
        values.put("cardHolder", cardHolder);
        values.put("cardExpirationMonth", cardExpirationMonth);
        values.put("cardExpirationYear", cardExpirationYear);
        values.put("cardValidationCode", cardValidationCode);
        if(Compare(cardNumber, cardHolder, cardExpirationMonth, cardExpirationYear, cardValidationCode)) {
            mDB.insert(_TABLENAME, null, values);
        }
    }
    void UpdateListView(ArrayAdapter<String> arrayAdapter){
        Cursor c = mDB.query(_TABLENAME, null, null, null, null, null, null);
        if(c.getCount()!=0) {
            c.moveToLast();
            arrayAdapter.add(" Card Number: " + c.getString(1) + "\n CardHolder: " + c.getString(2) + "\n Car Expiration: " + c.getString(3) + "/"+ c.getString(4) + "\n CVC: " + c.getString(5));
            while (c.moveToPrevious()) {
                arrayAdapter.add(" Card Number: " + c.getString(1) + "\n CardHolder: " + c.getString(2) + "\n Car Expiration: " + c.getString(3) + "/"+ c.getString(4) + "\n CVC: " + c.getString(5));
            }
        }
    }
}
