package com.example.hanbyeol.sure_park;

import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.telephony.TelephonyManager;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;

public class RcActivity extends AppCompatActivity implements View.OnClickListener {
    String cancel_result="fail";
    HttpGetInfo httpgetinfo;
    TextView text_revid, text_phonenum, text_email, text_locid, text_carsize, text_revtime;
    Date d;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rc);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        text_revid=(TextView) findViewById(R.id.textview_reservationID);
        text_phonenum= (TextView) findViewById(R.id.textview_phone);
        text_email= (TextView) findViewById(R.id.textview_email);
        text_locid= (TextView) findViewById(R.id.textview_parkinglot);
        text_carsize= (TextView) findViewById(R.id.textview_carsize);
        text_revtime= (TextView) findViewById(R.id.textview_datetime);

        Button btncardcheck=(Button)findViewById(R.id.button_cancel);
        btncardcheck.setOnClickListener(this);

        httpgetinfo = new HttpGetInfo();
        httpgetinfo.execute();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_cancel:
                AlertDialog.Builder alert_confirm = new AlertDialog.Builder(this);
                alert_confirm.setMessage("Would you like to cancel the reservation?").setCancelable(false).setPositiveButton("No",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        }).setNegativeButton("Yes",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                HttpDelInfo delInfo= new HttpDelInfo();
                                delInfo.execute();
                                return;
                            }
                        });
                AlertDialog alert = alert_confirm.create();
                alert.show();
                break;
        }
    }

    public class HttpGetInfo extends AsyncTask<String, Void, String> {
        @Override
        public String doInBackground(String... params) {
            try {
                URL url = new URL(MainActivity.address+"reservations/"+MainActivity.phoneNum+"/"+MainActivity.rev_id);
                HttpURLConnection   conn    = null;
                OutputStream          os   = null;
                InputStream           is   = null;
                ByteArrayOutputStream baos = null;
                conn = (HttpURLConnection)url.openConnection();
                conn.setConnectTimeout(5000);
                conn.setReadTimeout(5000);
                conn.setRequestMethod("GET");
                conn.setRequestProperty("Authorization", MainActivity.token_type+" "+MainActivity.access_token);
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
                    System.out.println("Code woejfwe"+response);

                    MainActivity.rev_id = (String) responseJSON.get("reservationID");
                    MainActivity.phoneNum = (String) responseJSON.get("phoneNumber");
                    MainActivity.email = (String) responseJSON.get("email");
                    MainActivity.ioc_id = (String) responseJSON.get("parkingLotID");
                    MainActivity.car_size = (int) responseJSON.get("carSize");
                    MainActivity.entranceTime = (String) responseJSON.get("entranceTime");
                    MainActivity.exitTime = (String) responseJSON.get("exitTime");
                    MainActivity.re_time = (String) responseJSON.get("reservationTime");
                    System.out.println("get info: " +response);
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
            SimpleDateFormat fm = new SimpleDateFormat("Z");
            d = new Date();
            String aaa = "-04";
            int a = Integer.parseInt(aaa);
            System.out.println("dfsfsdfsdfsdf"    + aaa);

            text_email.setText(MainActivity.email);
            text_phonenum.setText(MainActivity.phoneNum);
            text_revtime.setText(MainActivity.re_time);
            text_revid.setText(MainActivity.rev_id);
            switch (MainActivity.car_size){
                case 1:
                    text_carsize.setText("Small");
                    break;
                case 2:
                    text_carsize.setText("Mid-size");
                    break;
                case 3:
                    text_carsize.setText("Full-size");
                    break;
            }
            text_locid.setText(MainActivity.ioc_id);
        }
    }

    public class HttpDelInfo extends AsyncTask<String, Void, String> {
        @Override
        public String doInBackground(String... params) {
            try {
                URL url = new URL(MainActivity.address+"reservations/"+MainActivity.rev_id);
                HttpURLConnection   conn    = null;
                OutputStream          os   = null;
                InputStream           is   = null;
                ByteArrayOutputStream baos = null;
                conn = (HttpURLConnection)url.openConnection();
                conn.setConnectTimeout(5000);
                conn.setReadTimeout(5000);
                conn.setRequestMethod("DELETE");
                conn.setRequestProperty("Authorization", MainActivity.token_type+" "+MainActivity.access_token);
                conn.setRequestProperty("Content-Type", "application/json");
                conn.setRequestProperty("Accept", "application/json");
                conn.setDoInput(true);
                conn.setDoOutput(true);

                JSONObject json = new JSONObject();
                json.put("phoneNumber", MainActivity.phoneNum);

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

                    cancel_result = (String) responseJSON.get("result");

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
            if(cancel_result.equals("success")) {
                AlertDialog.Builder alert_confirm1 = new AlertDialog.Builder(RcActivity.this);
                alert_confirm1.setMessage("Cancel Complete").setCancelable(false).setPositiveButton("Confirm",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // 'YES'
                                HttpPostLogin postLogin = new HttpPostLogin();
                                postLogin.execute();
                                HttpPostOauth postOauth = new HttpPostOauth();
                                postOauth.execute();
                                HttpGetState getState = new HttpGetState();
                                getState.execute();
                                cancel_result="fail";
                                finish();
                            }
                        });
                AlertDialog alert = alert_confirm1.create();
                alert.show();
            }
            else if(cancel_result.equals("fail")){
                AlertDialog.Builder alert_confirm2 = new AlertDialog.Builder(RcActivity.this);
                alert_confirm2.setMessage("Cancel Fail").setCancelable(false).setPositiveButton("Confirm",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // 'YES'
                            }
                        });
                AlertDialog alert = alert_confirm2.create();
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

    public class HttpPostOauth extends AsyncTask<String, Void, Void> {
        @Override
        public Void doInBackground(String... params) {
            try {
                URL url = new URL(MainActivity.address+"oauth/token");
                HttpURLConnection   conn    = null;
                OutputStream          os   = null;
                InputStream           is   = null;
                ByteArrayOutputStream baos = null;
                conn = (HttpURLConnection)url.openConnection();
                conn.setConnectTimeout(5000);
                conn.setReadTimeout(5000);
                conn.setRequestMethod("POST");
                String basicAuth ="Basic " + Base64.encodeToString(("user_driver:123456").getBytes(), Base64.NO_WRAP);
                conn.setRequestProperty("Authorization", basicAuth);
                conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                conn.setRequestProperty("Accept", "application/json");
                conn.setDoOutput(true);
                conn.setDoInput(true);

                String req_token= "password="+MainActivity.id+"&username="+MainActivity.phoneNum+"&grant_type=password&scope=read%20write&client_secret=123456&client_id=user_driver";

                os = conn.getOutputStream();
                os.write(req_token.getBytes());
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

                    MainActivity.access_token = (String) responseJSON.get("access_token");
                    MainActivity.token_type = (String) responseJSON.get("token_type");
                    String refresh_token = (String) responseJSON.get("refresh_token");
                    int expires_in = (int) responseJSON.get("expires_in");
                    String scope = (String) responseJSON.get("scope");

                } else if(responseCode == HttpURLConnection.HTTP_FORBIDDEN){
                    System.out.println("FOBIDDEN");
                } else if(responseCode == HttpURLConnection.HTTP_UNAUTHORIZED){
                    System.out.println("UNAUTHORIZED");
                }

            } catch (Exception e) {
                System.out.println("error");
                e.printStackTrace();
            }

            return null;

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
                conn.setConnectTimeout(5000);
                conn.setReadTimeout(5000);
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
