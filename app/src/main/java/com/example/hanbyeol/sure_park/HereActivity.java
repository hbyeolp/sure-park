package com.example.hanbyeol.sure_park;

import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageButton;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class HereActivity extends AppCompatActivity implements View.OnClickListener {
    String gateresult="fail";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_here);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
       ImageButton btnhere=(ImageButton) findViewById (R.id.button_imhere);
        btnhere.setOnClickListener(this);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_imhere:
                AlertDialog.Builder alert_confirm = new AlertDialog.Builder(this);
                alert_confirm.setMessage("Would you like to open the gate?").setCancelable(false).setPositiveButton("No",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        }).setNegativeButton("Yes",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                HttpGetGate delInfo= new HttpGetGate();
                                delInfo.execute();
                                return;
                            }
                        });
                AlertDialog alert = alert_confirm.create();
                alert.show();
                break;
        }
    }
    public class HttpGetGate extends AsyncTask<String, Void, String> {
        @Override
        public String doInBackground(String... params) {
            try {
                URL url = new URL("http://172.16.30.206:8080/surepark-restful/opengate/" + MainActivity.phoneNum);
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
                System.out.println("Herer code "+responseCode);

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
                    System.out.println("Here response "+response);

                    JSONObject responseJSON = new JSONObject(response);
                    gateresult = (String) responseJSON.get("result");
                    System.out.println("Here"+ gateresult);

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

            if(gateresult.equals("success")) {
                AlertDialog.Builder card_alert_confirm = new AlertDialog.Builder(HereActivity.this);
                card_alert_confirm.setMessage("Gate Open Success").setCancelable(false).setPositiveButton("Confirm",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // 'YES'
                                HttpPostLogin postLogin = new HttpPostLogin();
                                postLogin.execute();
                                finish();
                            }
                        });
                AlertDialog alert1 = card_alert_confirm.create();
                alert1.show();
            }
            else if(gateresult.equals("fail")){
                AlertDialog.Builder card_alert_noconfirm = new AlertDialog.Builder(HereActivity.this);
                card_alert_noconfirm.setMessage("Gate Open Fail").setCancelable(false).setPositiveButton("Confirm",
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
    public class HttpPostLogin extends AsyncTask<String, Void, Void> {
        @Override
        public Void doInBackground(String... params) {
            try {
                String address = "http://172.16.30.206:8080/surepark-restful/drivers";
                URL url = new URL(address);
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
}
