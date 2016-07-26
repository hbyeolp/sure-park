package com.example.hanbyeol.sure_park;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class InputActivity extends AppCompatActivity implements View.OnClickListener {

    int curYear, curMonth, curDay, curHour, curMin, curSec;
    String reserv_time;
    String cardResult="fail";
    String revResult="fail";
    Calendar c;
    EditText edit_e_mail;
    EditText edit_card_num1, edit_card_num2, edit_card_num3, edit_card_num4, edit_card_cvc, edit_card_mon, edit_card_year, edit_card_firstname, edit_card_lastname;
    EditText edit_phone;
    EditText edit_date;
    String e_mail, re_time, car_size, cardmon, cardnum, cardyear, cardcode, cardname;
    String[] revtime;
    int available_card=0;

    String carddbName = "st_file_card.db", reservationdbName="st_file_reservation.db";
    int dbVersion = 1;
    private MySQLiteOpenHelperCard helperCard;
    private MySQLiteOpenHelperReservation helperReservation;
    private SQLiteDatabase card_db, reservation_db;
    String tag = "SQLite"; // Log의 tag 로 사용
    String tablecardName = "card", tablecreservationName="reservation";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        edit_e_mail=(EditText) findViewById(R.id.input_email);
        edit_card_num1= (EditText) findViewById(R.id.input_card1);
        edit_card_num2= (EditText) findViewById(R.id.input_card2);
        edit_card_num3= (EditText) findViewById(R.id.input_card3);
        edit_card_num4= (EditText) findViewById(R.id.input_card4);
        edit_card_cvc= (EditText) findViewById(R.id.input_cvc);
        edit_card_mon= (EditText) findViewById(R.id.input_month);
        edit_card_year= (EditText) findViewById(R.id.input_year);
        edit_card_firstname = (EditText) findViewById(R.id.input_firstname);
        edit_card_lastname = (EditText)findViewById(R.id.input_lastname);
        edit_phone=(EditText) findViewById(R.id.input_phone);
        edit_date=(EditText) findViewById(R.id.input_date);
        edit_phone.setFocusable(false);
        edit_phone.setClickable(false);
        edit_date.setFocusable(false);
        edit_date.setClickable(false);
        Button btncardcheck=(Button)findViewById(R.id.button_cardchek);
        btncardcheck.setOnClickListener(this);
        Button btnrev=(Button)findViewById(R.id.button_reserve);
        btnrev.setOnClickListener(this);
        Curdate();
        RevTime();
        Spinner time = (Spinner)findViewById(R.id.input_time);
        Spinner size = (Spinner)findViewById(R.id.input_carsize);
        time.setPrompt("Time");
        size.setPrompt("Car Size");
        time.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                switch (position){
                    case 0:
                        if(curMin<30) {
                            re_time = getReserv_time(0, 0);
                            Return_Reservtime(0,0);
                        }
                        else {
                            re_time = getReserv_time(0, 30);
                            Return_Reservtime(0,30);
                        }
                        break;
                    case 1:
                        if(curMin<30) {
                            re_time = getReserv_time(0, 30);
                            Return_Reservtime(0,30);
                        }
                        else {
                            re_time = getReserv_time(1, 0);
                            Return_Reservtime(1,0);
                        }
                        break;
                    case 2:
                        if(curMin<30) {
                            re_time = getReserv_time(1, 0);
                            Return_Reservtime(1,0);
                        }
                        else {
                            re_time = getReserv_time(1, 30);
                            Return_Reservtime(1,30);
                        }
                        break;
                    case 3:
                        if(curMin<30) {
                            re_time = getReserv_time(1, 30);
                            Return_Reservtime(1,30);
                        }
                        else {
                            re_time = getReserv_time(2, 0);
                            Return_Reservtime(2,0);
                        }
                        break;
                    case 4:
                        if(curMin<30) {
                            re_time = getReserv_time(2, 0);
                            Return_Reservtime(2,0);
                        }
                        else {
                            re_time = getReserv_time(2, 30);
                            Return_Reservtime(2,30);
                        }
                        break;
                    case 5:
                        if(curMin<30) {
                            re_time = getReserv_time(2, 30);
                            Return_Reservtime(2,30);
                        }
                        else {
                            re_time = getReserv_time(3, 0);
                            Return_Reservtime(3,0);
                        }
                        break;
                    case 6:
                        if(curMin<30) {
                            re_time = getReserv_time(3, 0);
                            Return_Reservtime(3,0);
                        }
                        else {
                            re_time = getReserv_time(3, 30);
                            Return_Reservtime(3, 30);
                        }
                        break;
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
        size.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                switch (position){
                    case 0:
                        car_size="1";
                        break;
                    case 1:
                        car_size="2";
                        break;
                    case 2:
                        car_size="3";
                        break;
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
        ArrayList<String> items =  new ArrayList<String>(Arrays.asList(revtime[0], revtime[1]));
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, items);
        adapter.add(revtime[2]);
        adapter.add(revtime[3]);
        adapter.add(revtime[4]);
        adapter.add(revtime[5]);
        adapter.add(revtime[6]);
        adapter.notifyDataSetChanged();

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        time.setAdapter(adapter);

        edit_phone.setText(MainActivity.phoneNum);
        edit_date.setText(CurDate());

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_cardchek:
                HttpPostCard postCard= new HttpPostCard();
                postCard.execute();
                break;
            case R.id.button_reserve:
                if(available_card==1) {
                    AlertDialog.Builder alert_confirm = new AlertDialog.Builder(this);
                    alert_confirm.setMessage("Would you like to reserve the parking lot?").setCancelable(false).setPositiveButton("No",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            }).setNegativeButton("Yes",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    HttpPostInput postInput= new HttpPostInput();
                                    postInput.execute();
                                    return;
                                }
                            });
                    AlertDialog alert = alert_confirm.create();
                    alert.show();
                }
                else if(available_card==0){
                    AlertDialog.Builder alert_confirm = new AlertDialog.Builder(this);
                    alert_confirm.setMessage("Please, Check the Card").setCancelable(false).setPositiveButton("Confirm",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            });
                    AlertDialog alert = alert_confirm.create();
                    alert.show();
                }
                break;
        }
    }
    public String getReserv_time(int rvhour, int rvmin){
        SimpleDateFormat fm = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        TimeZone zone = TimeZone.getTimeZone("GMT");
        fm.setTimeZone(zone);
        c.add(Calendar.MINUTE, -curMin);
        c.add(Calendar.SECOND, -curSec);
        c.add(Calendar.HOUR, rvhour);
        c.add(Calendar.MINUTE,rvmin);
        Date d = new Date(c.getTimeInMillis());
        reserv_time=fm.format(d);
        return reserv_time;
    }
    public void Return_Reservtime(int rvhour, int rvmin){
        c.add(Calendar.HOUR, -rvhour);
        c.add(Calendar.MINUTE,-rvmin);
    }
    protected String[] RevTime(){
        revtime=new String[7];

        if(curMin<30) {
            revtime[0] = Integer.toString(curHour) + ":00 ~ " + Integer.toString(curHour)+":30";
            revtime[1] = Integer.toString(curHour) + ":30 ~ " + Integer.toString(curHour+1)+":00";
            revtime[2] = Integer.toString(curHour+1) + ":00 ~ " + Integer.toString(curHour+1)+":30";
            revtime[3] = Integer.toString(curHour+1) + ":30 ~ " + Integer.toString(curHour+2)+":00";
            revtime[4] = Integer.toString(curHour+2) + ":00 ~ " + Integer.toString(curHour+2)+":30";
            revtime[5] = Integer.toString(curHour+2) + ":30 ~ " + Integer.toString(curHour+3)+":00";
            revtime[6] = Integer.toString(curHour+3) + ":00 ~ " + Integer.toString(curHour+3)+":30";

        }else {
            revtime[0] = Integer.toString(curHour) + ":30 ~ " + Integer.toString(curHour+1)+":00";
            revtime[1] = Integer.toString(curHour+1) + ":00 ~ " + Integer.toString(curHour+1)+":30";
            revtime[2] = Integer.toString(curHour+1) + ":30 ~ " + Integer.toString(curHour+2)+":00";
            revtime[3] = Integer.toString(curHour+2) + ":00 ~ " + Integer.toString(curHour+2)+":30";
            revtime[4] = Integer.toString(curHour+2) + ":30 ~ " + Integer.toString(curHour+3)+":00";
            revtime[5] = Integer.toString(curHour + 3) + ":00 ~ " + Integer.toString(curHour + 3) + ":30";
            revtime[6] = Integer.toString(curHour+3) + ":30 ~ " + Integer.toString(curHour+4)+":00";
        }

        return revtime;
    }
    protected String CurDate(){
        return Integer.toString(curMonth)+"/"+Integer.toString(curDay)+"/"+Integer.toString(curYear);
    }
    protected void Curdate(){
        c = Calendar.getInstance();
        curYear = c.get(Calendar.YEAR);
        curMonth = c.get(Calendar.MONTH)+1;
        curDay = c.get(Calendar.DAY_OF_MONTH);
        curHour=c.get(Calendar.HOUR);
        curMin=c.get(Calendar.MINUTE);
        curSec=c.get(Calendar.SECOND);
    }
    public class HttpPostCard extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            cardnum=edit_card_num1.getText().toString()+edit_card_num2.getText().toString()+edit_card_num3.getText().toString()+edit_card_num4.getText().toString();
            cardcode=edit_card_cvc.getText().toString();
            cardmon=edit_card_mon.getText().toString();
            cardyear=edit_card_year.getText().toString();
            cardname=edit_card_firstname.getText().toString()+edit_card_lastname.getText().toString();
        }
        @Override
        public String doInBackground(String... params) {
            try {
                URL url = new URL(MainActivity.address+"cardvalidate");
                HttpURLConnection conn    = null;

                OutputStream os   = null;
                InputStream is   = null;
                ByteArrayOutputStream baos = null;

                conn = (HttpURLConnection)url.openConnection();
                conn.setConnectTimeout(5000);
                conn.setReadTimeout(5000);
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Content-Type", "application/json");
                conn.setRequestProperty("Authorization", MainActivity.token_type+" "+MainActivity.access_token);
                conn.setRequestProperty("Accept", "application/json");
                conn.setDoOutput(true);
                conn.setDoInput(true);

                JSONObject json = new JSONObject();

                json.put("phoneNumber", MainActivity.phoneNum);
                json.put("cardNumber", cardnum);
                json.put("cardExpirationMonth", cardmon);
                json.put("cardExpirationYear", cardyear);
                json.put("cardValidationCode", cardcode);
                json.put("cardHolder", cardname);
                os = conn.getOutputStream();
                os.write(json.toString().getBytes());
                os.flush();

                String response;

                int responseCode = conn.getResponseCode();

                if(responseCode == HttpURLConnection.HTTP_OK) {

                    is = conn.getInputStream();
                    baos = new ByteArrayOutputStream();
                    byte[] byteBuffer = new byte[1024];
                    byte[] byteData = null;
                    int nLength = 0;
                    while((nLength = is.read(byteBuffer, 0, byteBuffer.length)) != -1) {
                        baos.write(byteBuffer, 0, nLength);
                    }
                    byteData = baos.toByteArray();

                    response = new String(byteData);

                    JSONObject responseJSON = new JSONObject(response);
                    cardResult = (String) responseJSON.get("result");
                }

            } catch (Exception e) {
                System.out.println("error");
                e.printStackTrace();
            }

            return null;

        }
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            if(cardResult.equals("success")) {
                AlertDialog.Builder card_alert_confirm = new AlertDialog.Builder(InputActivity.this);
                card_alert_confirm.setMessage("Available Card").setCancelable(false).setPositiveButton("Confirm",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // 'YES'
                                cardResult="fail";
                                available_card=1;
                            }
                        });
                AlertDialog alert1 = card_alert_confirm.create();
                alert1.show();
            }
            else {
                AlertDialog.Builder card_alert_noconfirm = new AlertDialog.Builder(InputActivity.this);
                card_alert_noconfirm.setMessage("Unavailable Card").setCancelable(false).setPositiveButton("Confirm",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // 'YES'
                                available_card=0;
                            }
                        });
                AlertDialog alert2 = card_alert_noconfirm.create();
                alert2.show();
            }
        }
    }

    public class HttpPostInput extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            e_mail=edit_e_mail.getText().toString();
            cardnum=edit_card_num1.getText().toString()+edit_card_num2.getText().toString()+edit_card_num3.getText().toString()+edit_card_num4.getText().toString();
            cardcode=edit_card_cvc.getText().toString();
            cardmon=edit_card_mon.getText().toString();
            cardyear=edit_card_year.getText().toString();
        }
        @Override
        public String doInBackground(String... params) {
            try {
                URL url = new URL(MainActivity.address+"reservations");
                HttpURLConnection conn    = null;

                OutputStream os   = null;
                InputStream is   = null;
                ByteArrayOutputStream baos = null;

                conn = (HttpURLConnection)url.openConnection();
                conn.setConnectTimeout(5000);
                conn.setReadTimeout(5000);
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Content-Type", "application/json");
                conn.setRequestProperty("Authorization", MainActivity.token_type+" "+MainActivity.access_token);
                conn.setRequestProperty("Accept", "application/json");
                conn.setDoOutput(true);
                conn.setDoInput(true);

                JSONObject json = new JSONObject();

                json.put("phoneNumber", MainActivity.phoneNum);
                json.put("email", e_mail);
                json.put("parkingLotID", MainActivity.ioc_id);
                json.put("reservationTime", re_time);
                json.put("carSize", car_size);
                json.put("cardNumber",cardnum);
                json.put("cardExpirationMonth", cardmon);
                json.put("cardExpirationYear", cardyear);
                json.put("cardValidationCode", cardcode);
                json.put("cardHolder", cardname);

                os = conn.getOutputStream();
                os.write(json.toString().getBytes());
                os.flush();

                String response;

                int responseCode = conn.getResponseCode();
                if(responseCode == HttpURLConnection.HTTP_OK) {

                    is = conn.getInputStream();
                    baos = new ByteArrayOutputStream();
                    byte[] byteBuffer = new byte[1024];
                    byte[] byteData = null;
                    int nLength = 0;
                    while((nLength = is.read(byteBuffer, 0, byteBuffer.length)) != -1) {
                        baos.write(byteBuffer, 0, nLength);
                    }
                    byteData = baos.toByteArray();

                    response = new String(byteData);

                    JSONObject responseJSON = new JSONObject(response);
                    revResult = (String) responseJSON.get("result");
                    MainActivity.rev_id = (String) responseJSON.get("reservationID");

                }

            } catch (Exception e) {
                System.out.println("error");
                e.printStackTrace();
            }

            return null;

        }
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if(revResult.equals("success")) {
                AlertDialog.Builder alert_confirm = new AlertDialog.Builder(InputActivity.this);
                alert_confirm.setMessage("Reservation Complete").setCancelable(false).setPositiveButton("Confirm",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // 'YES'
                                revResult="fail";
                                HttpPostLogin postLogin = new HttpPostLogin();
                                postLogin.execute();
                                HttpGetState getState = new HttpGetState();
                                getState.execute();
                                available_card=0;
                                CardInsert(cardnum, cardname, cardmon, cardyear, cardcode);
                                ReservationInsert(MainActivity.rev_id, MainActivity.parkinglotname, MainActivity.ioc_id, re_time, car_size, e_mail, MainActivity.phoneNum);
                                finish();
                            }
                        });
                AlertDialog alert = alert_confirm.create();
                alert.show();
            }
            else if(revResult.equals("fail")){
                AlertDialog.Builder alert_confirm = new AlertDialog.Builder(InputActivity.this);
                alert_confirm.setMessage("Reservation Fail").setCancelable(false).setPositiveButton("Confirm",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // 'YES'
                            }
                        });
                AlertDialog alert = alert_confirm.create();
                alert.show();
            }
        }
    }

    public class HttpPostLogin extends AsyncTask<String, Void, Void> {
        @Override
        public Void doInBackground(String... params) {
            try {
                URL url = new URL(MainActivity.address+"drivers");
                HttpURLConnection   conn    = null;

                OutputStream          os   = null;
                InputStream           is   = null;
                ByteArrayOutputStream baos = null;

                conn = (HttpURLConnection)url.openConnection();
                conn.setConnectTimeout(5000);
                conn.setReadTimeout(5000);
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Content-Type", "application/json");
                conn.setRequestProperty("Accept", "application/json");
                conn.setDoOutput(true);
                conn.setDoInput(true);


                JSONObject json = new JSONObject();
                json.put("phoneNumber", MainActivity.phoneNum);
                json.put("secretKey", MainActivity.secret_k);

                os = conn.getOutputStream();
                os.write(json.toString().getBytes());
                os.flush();
                os.close();

                String response;

                int responseCode = conn.getResponseCode();

                if(responseCode == HttpURLConnection.HTTP_OK) {

                    is = conn.getInputStream();
                    baos = new ByteArrayOutputStream();
                    byte[] byteBuffer = new byte[1024];
                    byte[] byteData = null;
                    int nLength = 0;
                    while((nLength = is.read(byteBuffer, 0, byteBuffer.length)) != -1) {
                        baos.write(byteBuffer, 0, nLength);
                    }
                    byteData = baos.toByteArray();

                    response = new String(byteData);

                    JSONObject responseJSON = new JSONObject(response);
                    String user_status = (String) responseJSON.get("driverRegistration");
                    MainActivity.phoneNum = (String) responseJSON.get("phoneNumber");
                    MainActivity.id = (String) responseJSON.get("identificationNumber");
                    MainActivity.status = (String) responseJSON.get("state");
                }

            } catch (Exception e) {
                System.out.println("error");
                e.printStackTrace();
            }

            return null;

        }
    }

    void CardDelete(String cardNumber) {
        int result = card_db.delete(tablecardName, "cardNumber=?", new String[] {cardNumber});
        CardSelect(); // delete 후에 select 하도록
    }

    void CardUpdate (String cardNumber, String cardHolder, String cardExpirationMonth, String cardExpirationYear, String cardValidationCode) {
        ContentValues values = new ContentValues();
        values.put("cardHolder", cardHolder);         // 바꿀값
        values.put("cardExpirationMonth", cardExpirationMonth); // 바꿀값
        values.put("cardExpirationYear", cardExpirationYear); // 바꿀값
        values.put("cardValidationCode", cardValidationCode); // 바꿀값

        int result = card_db.update(tablecardName,
                values,    // 뭐라고 변경할지 ContentValues 설정
                "cardNumber=?", // 바꿀 항목을 찾을 조건절
                new String[]{cardNumber});// 바꿀 항목으로 찾을 값 String 배열
        CardSelect (); // 업데이트 후에 조회하도록
    }

    void CardSelect () {
        Cursor c = card_db.query(tablecardName, null, null, null, null, null, null);
        while(c.moveToNext()) {
            int _id = c.getInt(0);
            String cardNumber = c.getString(1);
            String cardHolder = c.getString(2);
            String cardExpirationMonth = c.getString(3);
            String cardExpirationYear = c.getString(4);
            String cardValidationCode = c.getString(5);

            // 키보드 내리기
            InputMethodManager imm =
                    (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow
                    (getCurrentFocus().getWindowToken(), 0);
        }
    }

    void CardInsert (String cardNumber, String cardHolder, String cardExpirationMonth, String cardExpirationYear, String cardValidationCode) {
        ContentValues values = new ContentValues();
        // 키,값의 쌍으로 데이터 입력
        values.put("cardNumber", cardNumber);
        values.put("cardHolder", cardHolder);
        values.put("cardExpirationMonth", cardExpirationMonth);
        values.put("cardExpirationYear", cardExpirationYear);
        values.put("cardValidationCode", cardValidationCode);

        long result = card_db.insert(tablecardName, null, values);
        CardSelect(); // insert 후에 select 하도록
    }

    void ReservationDelete(String reservationid) {
        int result = reservation_db.delete(tablecreservationName, "reservationID=?", new String[] {reservationid});
        CardSelect(); // delete 후에 select 하도록
    }

    void ReservationUpdate (String reservationid, String parkinglotname, String parkinglotid, String reservationtime, String carsize, String email, String phonenumber) {
        ContentValues values = new ContentValues();
        values.put("parkinglotname", parkinglotname);         // 바꿀값
        values.put("parkinglotid", parkinglotid); // 바꿀값
        values.put("reservationtime", reservationtime); // 바꿀값
        values.put("carsize", carsize); // 바꿀값
        values.put("email", email); // 바꿀값
        values.put("phonenumber", phonenumber); // 바꿀값

        int result = reservation_db.update(tablecreservationName,
                values,    // 뭐라고 변경할지 ContentValues 설정
                "reservationid=?", // 바꿀 항목을 찾을 조건절
                new String[]{reservationid});// 바꿀 항목으로 찾을 값 String 배열
        CardSelect (); // 업데이트 후에 조회하도록
    }

    void ReservationSelect () {
        Cursor c = reservation_db.query(tablecreservationName, null, null, null, null, null, null);
        while(c.moveToNext()) {
            int _id = c.getInt(0);
            String cardNumber = c.getString(1);
            String cardHolder = c.getString(2);
            String cardExpirationMonth = c.getString(3);
            String cardExpirationYear = c.getString(4);
            String cardValidationCode = c.getString(5);

            // 키보드 내리기
            InputMethodManager imm =
                    (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow
                    (getCurrentFocus().getWindowToken(), 0);
        }
    }

    void ReservationInsert (String reservationid, String parkinglotname, String parkinglotid, String reservationtime, String carsize, String email, String phonenumber) {
        ContentValues values = new ContentValues();
        // 키,값의 쌍으로 데이터 입력
        values.put("reservationid", reservationid);
        values.put("parkinglotname", parkinglotname);
        values.put("parkinglotid", parkinglotid);
        values.put("reservationtime", reservationtime);
        values.put("carsize", carsize);
        values.put("email", email);
        values.put("phonenumber", phonenumber);

        long result = reservation_db.insert(tablecreservationName, null, values);
        CardSelect(); // insert 후에 select 하도록
    }

    public class HttpGetState extends AsyncTask<String, Void, String> {
        @Override
        public String doInBackground(String... params) {
            try {
                URL url = new URL(MainActivity.address+"drivers/"+MainActivity.phoneNum);
                HttpURLConnection   conn    = null;
                OutputStream          os   = null;
                InputStream           is   = null;
                ByteArrayOutputStream baos = null;
                conn = (HttpURLConnection)url.openConnection();
                conn.setConnectTimeout(10000);
                conn.setReadTimeout(10000);
                conn.setRequestMethod("GET");
                conn.setRequestProperty("Authorization", MainActivity.token_type+" "+ MainActivity.access_token);
                conn.setRequestProperty("Content-Type", "application/json");
                conn.setRequestProperty("Accept", "application/json");
                conn.setDoInput(true);
                conn.connect();

                String response;

                int responseCode = conn.getResponseCode();
                if(responseCode == HttpURLConnection.HTTP_OK) {

                    is = conn.getInputStream();
                    baos = new ByteArrayOutputStream();
                    byte[] byteBuffer = new byte[1024];
                    byte[] byteData = null;
                    int nLength = 0;
                    while((nLength = is.read(byteBuffer, 0, byteBuffer.length)) != -1) {
                        baos.write(byteBuffer, 0, nLength);
                    }
                    byteData = baos.toByteArray();

                    response = new String(byteData);

                    JSONObject responseJSON = new JSONObject(response);

                    MainActivity.rev_id = (String) responseJSON.get("reservationID");
                    MainActivity.phoneNum = (String) responseJSON.get("phoneNumber");
                    MainActivity.status = (String) responseJSON.get("state");

                } else if(responseCode == HttpURLConnection.HTTP_FORBIDDEN){
                    System.out.println("FOBIDDEN");
                } else if(responseCode == HttpURLConnection.HTTP_UNAUTHORIZED){
                    System.out.println("UNAUTHORIZED");
                }
                conn.disconnect();
            } catch (Exception e) {
                System.out.println("error");
                e.printStackTrace();
            }

            return null;
        }
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

        }
    }
}
