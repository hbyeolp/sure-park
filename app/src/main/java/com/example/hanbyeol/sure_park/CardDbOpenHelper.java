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

    void Delete(int id) {
        int result = mDB.delete(_TABLENAME, "_id=?", new String[] {Integer.toString(id)});
    }

    void Update (String cardNumber, String cardFirstname, String cardLastname, String cardExpirationMonth, String cardExpirationYear, String cardValidationCode) {
        ContentValues values = new ContentValues();
        values.put("cardFirstname", cardFirstname);
        values.put("cardLastname", cardLastname);         // 바꿀값
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

    Boolean Compare (String cardNumber, String cardFirstname, String cardLastname, String cardExpirationMonth, String cardExpirationYear, String cardValidationCode) {
        Cursor c = mDB.query(_TABLENAME, null, null, null, null, null, null);
        while(c.moveToNext()) {
            if(cardNumber.equals(c.getString(1)) && cardLastname.equals(c.getString(2)) && cardFirstname.equals(c.getString(3))&& cardExpirationMonth.equals(c.getString(4)) && cardExpirationYear.equals(c.getString(5)) && cardValidationCode.equals(c.getString(6))){
                return false;
            }
        }
        return true;
    }

    void Insert (String cardNumber, String cardFirstname, String cardLastname, String cardExpirationMonth, String cardExpirationYear, String cardValidationCode) {
        ContentValues values = new ContentValues();
        // 키,값의 쌍으로 데이터 입력
        values.put("cardNumber", cardNumber);
        values.put("cardFirstname", cardFirstname);
        values.put("cardLastname", cardLastname);
        values.put("cardExpirationMonth", cardExpirationMonth);
        values.put("cardExpirationYear", cardExpirationYear);
        values.put("cardValidationCode", cardValidationCode);
        if(Compare(cardNumber, cardLastname, cardFirstname, cardExpirationMonth, cardExpirationYear, cardValidationCode)) {
            mDB.insert(_TABLENAME, null, values);
        }
    }
    void UpdateListView(ListViewCardAdapter arrayAdapter, String[] cardNumbers, String[] cardFirstnames, String[] cardLastnames, String[] cardExpirationMonths, String[] cardExpirationYears, String[] cardValidationCodes){
        Cursor c = mDB.query(_TABLENAME, null, null, null, null, null, null);
        System.out.println("+++++++++++++++");
        if(c.getCount()>0) {
            int a=1;
            c.moveToFirst();
            cardNumbers[0]=c.getString(1).substring(0,4)+c.getString(1).substring(4,8)+c.getString(1).substring(8,12)+c.getString(1).substring(12);
            cardFirstnames[0]=c.getString(2);
            cardLastnames[0]=c.getString(3);
            cardExpirationMonths[0]=c.getString(4);
            cardExpirationYears[0]=c.getString(5);
            cardValidationCodes[0]=c.getString(6);
            arrayAdapter.addItem(cardNumbers[0],cardFirstnames[0]+cardLastnames[0],cardExpirationMonths[0]+"/"+cardExpirationYears[0], cardValidationCodes[0]);
            while (c.moveToNext()) {
                cardNumbers[a]=c.getString(1);
                cardFirstnames[a]=c.getString(2);
                cardLastnames[a]=c.getString(3);
                cardExpirationMonths[a]=c.getString(4);
                cardExpirationYears[a]=c.getString(5);
                cardValidationCodes[a]=c.getString(6);
                arrayAdapter.addItem(cardNumbers[a],cardFirstnames[a]+cardLastnames[a],cardExpirationMonths[a]+"/"+cardExpirationYears[a], cardValidationCodes[a]);
                a++;
            }
        }
    }
}
