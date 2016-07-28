package com.example.hanbyeol.sure_park;

import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class HandoverActivity extends AppCompatActivity implements View.OnClickListener {
    String second_phonenum="", handoverResult="fail";
    EditText edit_second_phone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_handover);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ImageButton btnhere=(ImageButton) findViewById (R.id.button_handover);
        btnhere.setOnClickListener(this);
        edit_second_phone=(EditText) findViewById(R.id.Input_handover_phonnum);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_handover:
                second_phonenum = edit_second_phone.getText().toString();
                if(!second_phonenum.equals("")) {
                    AlertDialog.Builder alert_confirm = new AlertDialog.Builder(this);
                    alert_confirm.setMessage("Would you like to hand over the reservation? Phone Number :" + second_phonenum).setCancelable(false).setPositiveButton("No",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            }).setNegativeButton("Yes",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    HttpPutHandOver putHandOver = new HttpPutHandOver();
                                    putHandOver.execute();
                                    return;
                                }
                            });
                    AlertDialog alert = alert_confirm.create();
                    alert.show();
                }else{
                    AlertDialog.Builder alert_confirm = new AlertDialog.Builder(this);
                    alert_confirm.setMessage("Please, Check the Phone Number").setCancelable(false).setPositiveButton("Confirm",
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
    public class HttpPutHandOver extends AsyncTask<String, Void, String> {
        @Override
        public String doInBackground(String... params) {
            try {
                URL url = new URL(MainActivity.address+"drivers/handover/" + MainActivity.phoneNum);
                HttpURLConnection conn    = null;
                OutputStream os   = null;
                InputStream is   = null;
                ByteArrayOutputStream baos = null;
                conn = (HttpURLConnection)url.openConnection();
                conn.setConnectTimeout(5000);
                conn.setReadTimeout(5000);
                conn.setRequestMethod("PUT");
                conn.setRequestProperty("Authorization", MainActivity.token_type+" "+ MainActivity.access_token);
                conn.setRequestProperty("Content-Type", "application/json");
                conn.setRequestProperty("Accept", "application/json");
                conn.setDoInput(true);
                conn.setDoOutput(true);

                JSONObject json = new JSONObject();

                json.put("secondaryPhoneNumber", second_phonenum);

                os = conn.getOutputStream();
                os.write(json.toString().getBytes());
                os.flush();

                String response;

                int responseCode = conn.getResponseCode();
                System.out.println("hand code "+responseCode);

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
                    System.out.println("hand response "+response);

                    JSONObject responseJSON = new JSONObject(response);
                    handoverResult = (String) responseJSON.get("result");
                    System.out.println(handoverResult);
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

            if(handoverResult.equals("success")) {
                AlertDialog.Builder card_alert_confirm = new AlertDialog.Builder(HandoverActivity.this);
                card_alert_confirm.setMessage("Handover Success").setCancelable(false).setPositiveButton("Confirm",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // 'YES'
                                handoverResult="fail";
                                HttpPostLogin postLogin = new HttpPostLogin();
                                postLogin.execute();
                                System.out.println(MainActivity.status);

                                finish();
                            }
                        });
                AlertDialog alert1 = card_alert_confirm.create();
                alert1.show();
            }
            else if(handoverResult.equals("fail")){
                AlertDialog.Builder card_alert_noconfirm = new AlertDialog.Builder(HandoverActivity.this);
                card_alert_noconfirm.setMessage("Handover Fail").setCancelable(false).setPositiveButton("Confirm",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // 'YES'
                            }
                        });
                AlertDialog alert2 = card_alert_noconfirm.create();
                alert2.show();
            }
        }
    }

    public class HttpPostOauth extends AsyncTask<String, Void, String> {
        @Override
        public String doInBackground(String... params) {
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
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            HttpGetState getState = new HttpGetState();
            getState.execute();
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
                    System.out.println(response);
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
            HttpPostOauth postOauth = new HttpPostOauth();
            postOauth.execute();
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
