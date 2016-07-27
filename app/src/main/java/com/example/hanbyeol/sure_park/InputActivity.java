package com.example.hanbyeol.sure_park;

import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

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

    String[] revtime;
    int available_card=0;

    private CardDbOpenHelper helperCard;

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
        SetTrueClick();
        Spinner time = (Spinner)findViewById(R.id.input_time);
        Spinner size = (Spinner)findViewById(R.id.input_carsize);
        time.setPrompt("Time");
        size.setPrompt("Car Size");

        helperCard = new CardDbOpenHelper(this);
        helperCard.open();

        time.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                switch (position){
                    case 0:
                        if(curMin<30) {
                            MainActivity.re_time = getReserv_time(0, 0);
                            Return_Reservtime(0,0);
                        }
                        else {
                            MainActivity.re_time = getReserv_time(0, 30);
                            Return_Reservtime(0,30);
                        }
                        break;
                    case 1:
                        if(curMin<30) {
                            MainActivity.re_time = getReserv_time(0, 30);
                            Return_Reservtime(0,30);
                        }
                        else {
                            MainActivity.re_time = getReserv_time(1, 0);
                            Return_Reservtime(1,0);
                        }
                        break;
                    case 2:
                        if(curMin<30) {
                            MainActivity.re_time = getReserv_time(1, 0);
                            Return_Reservtime(1,0);
                        }
                        else {
                            MainActivity.re_time = getReserv_time(1, 30);
                            Return_Reservtime(1,30);
                        }
                        break;
                    case 3:
                        if(curMin<30) {
                            MainActivity.re_time = getReserv_time(1, 30);
                            Return_Reservtime(1,30);
                        }
                        else {
                            MainActivity.re_time = getReserv_time(2, 0);
                            Return_Reservtime(2,0);
                        }
                        break;
                    case 4:
                        if(curMin<30) {
                            MainActivity.re_time = getReserv_time(2, 0);
                            Return_Reservtime(2,0);
                        }
                        else {
                            MainActivity.re_time = getReserv_time(2, 30);
                            Return_Reservtime(2,30);
                        }
                        break;
                    case 5:
                        if(curMin<30) {
                            MainActivity.re_time = getReserv_time(2, 30);
                            Return_Reservtime(2,30);
                        }
                        else {
                            MainActivity.re_time = getReserv_time(3, 0);
                            Return_Reservtime(3,0);
                        }
                        break;
                    case 6:
                        if(curMin<30) {
                            MainActivity.re_time = getReserv_time(3, 0);
                            Return_Reservtime(3,0);
                        }
                        else {
                            MainActivity.re_time = getReserv_time(3, 30);
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
                        MainActivity.car_size=1;
                        break;
                    case 1:
                        MainActivity.car_size=2;
                        break;
                    case 2:
                        MainActivity.car_size=3;
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

        if(MainActivity.cardstate==1){
            edit_card_num1.setText(MainActivity.cardnum.substring(0,4));
            edit_card_num2.setText(MainActivity.cardnum.substring(4,8));
            edit_card_num3.setText(MainActivity.cardnum.substring(8,12));
            edit_card_num4.setText(MainActivity.cardnum.substring(12));
            edit_card_cvc.setText(MainActivity.cardcode);
            edit_card_year.setText(MainActivity.cardyear);
            edit_card_mon.setText(MainActivity.cardmon);
            SetFalseClick();
            MainActivity.cardstate=0;
        }

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
    void SetFalseClick(){
        edit_card_num1.setFocusable(false);
        edit_card_num1.setClickable(false);
        edit_card_num2.setFocusable(false);
        edit_card_num2.setClickable(false);
        edit_card_num3.setFocusable(false);
        edit_card_num3.setClickable(false);
        edit_card_num4.setFocusable(false);
        edit_card_num4.setClickable(false);
        edit_card_cvc.setFocusable(false);
        edit_card_mon.setClickable(false);
        edit_card_year.setFocusable(false);
        edit_card_year.setClickable(false);
        edit_card_firstname.setFocusable(false);
        edit_card_firstname.setClickable(false);
        edit_card_lastname.setFocusable(false);
        edit_card_lastname.setClickable(false);
    }
    void SetTrueClick(){
        edit_card_num1.setFocusable(true);
        edit_card_num1.setClickable(true);
        edit_card_num2.setFocusable(true);
        edit_card_num2.setClickable(true);
        edit_card_num3.setFocusable(true);
        edit_card_num3.setClickable(true);
        edit_card_num4.setFocusable(true);
        edit_card_num4.setClickable(true);
        edit_card_cvc.setFocusable(true);
        edit_card_mon.setClickable(true);
        edit_card_year.setFocusable(true);
        edit_card_year.setClickable(true);
        edit_card_firstname.setFocusable(true);
        edit_card_firstname.setClickable(true);
        edit_card_lastname.setFocusable(true);
        edit_card_lastname.setClickable(true);
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
            MainActivity.cardnum=edit_card_num1.getText().toString()+edit_card_num2.getText().toString()+edit_card_num3.getText().toString()+edit_card_num4.getText().toString();
            MainActivity.cardcode=edit_card_cvc.getText().toString();
            MainActivity.cardmon=edit_card_mon.getText().toString();
            MainActivity.cardyear=edit_card_year.getText().toString();
            MainActivity.cardname=edit_card_firstname.getText().toString()+edit_card_lastname.getText().toString();
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
                json.put("cardNumber", MainActivity.cardnum);
                json.put("cardExpirationMonth", MainActivity.cardmon);
                json.put("cardExpirationYear", MainActivity.cardyear);
                json.put("cardValidationCode", MainActivity.cardcode);
                json.put("cardHolder", MainActivity.cardname);
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
                                SetFalseClick();
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
            MainActivity.email=edit_e_mail.getText().toString();
            MainActivity.cardnum=edit_card_num1.getText().toString()+edit_card_num2.getText().toString()+edit_card_num3.getText().toString()+edit_card_num4.getText().toString();
            MainActivity.cardcode=edit_card_cvc.getText().toString();
            MainActivity.cardmon=edit_card_mon.getText().toString();
            MainActivity.cardyear=edit_card_year.getText().toString();
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
                json.put("email", MainActivity.email);
                json.put("parkingLotID", MainActivity.ioc_id);
                json.put("reservationTime", MainActivity.re_time);
                json.put("carSize", MainActivity.car_size);
                json.put("cardNumber",MainActivity.cardnum);
                json.put("cardExpirationMonth", MainActivity.cardmon);
                json.put("cardExpirationYear", MainActivity.cardyear);
                json.put("cardValidationCode", MainActivity.cardcode);
                json.put("cardHolder", MainActivity.cardname);

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
                                helperCard.Insert(MainActivity.cardnum, edit_card_lastname.getText().toString(), edit_card_firstname.getText().toString(), MainActivity.cardmon, MainActivity.cardyear, MainActivity.cardcode);
                                helperCard.close();
                                available_card=0;
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

    public class HttpPostLogin extends AsyncTask<String, Void, String> {
        @Override
        public String doInBackground(String... params) {
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
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            HttpGetState getState = new HttpGetState();
            getState.execute();
        }
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
