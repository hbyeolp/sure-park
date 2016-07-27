package com.example.hanbyeol.sure_park;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.telephony.TelephonyManager;
import android.util.Base64;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    HttpPostLogin postlogin;
    HttpPostOauth postOauth;
    HttpGetList getList;
    HttpGetState getState;

    SearchView searchView;

    public static String secret_k="surepark";
    public static String address = "http://192.168.1.80:8080/surepark-restful/";
    private static final int MY_PERMISSIONS_REQUEST_READ_PHONE_STATE = 1;

    public static String phoneNum;
    public static String access_token, token_type;
    public static String ioc_id="1", rev_id, status="unreserved", pre_resvid;
    public static String id, parkinglotname, email, entranceTime, exitTime, re_time;
    public static int car_size;
    String searchParkinglot;

    JSONArray sureparks;
    int ct=0;
    String[] loc_ids, loc_names;
    ArrayAdapter<String> m_Adapter;

    public static int cardstate=0;
    public static String[] cardmons, cardnums, cardyears, cardcodes, cardnames;
    public static String cardmon, cardnum, cardyear, cardcode, cardname;
    private CardDbOpenHelper helperCard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        searchView = (SearchView) findViewById(R.id.search_parkinglot);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {

                searchParkinglot = searchView.getQuery().toString();
                System.out.println("Query"+searchParkinglot);
                getList = new HttpGetList();
                getList.execute();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String s) {

                return true;
            }
        });
        searchView.setSubmitButtonEnabled(true);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(status.equals("reserved") || status.equals("parked")) {
                    Intent intent = new Intent(MainActivity.this, HereActivity.class);
                    startActivity(intent);
                }else{
                    AlertDialog.Builder alert_confirm = new AlertDialog.Builder(MainActivity.this);
                    alert_confirm.setMessage("Please, Make a Reservation").setCancelable(false).setPositiveButton("Confirm",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            });
                    AlertDialog alert = alert_confirm.create();
                    alert.show();
                }
            }
        });
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        final ListView listview = (ListView)findViewById(R.id.listView);
        m_Adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1);
        listview.setAdapter(m_Adapter);
        m_Adapter.add("example");
        helperCard = new CardDbOpenHelper(this);
        helperCard.open();

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                                    if(status.equals("unreserved")) {
                                                        if(helperCard.Count()>0) {
                                                            AlertDialog.Builder alert_confirm = new AlertDialog.Builder(MainActivity.this);
                                                            alert_confirm.setMessage("Would you like to use the stored card?").setCancelable(false).setPositiveButton("No",
                                                                    new DialogInterface.OnClickListener() {
                                                                        @Override
                                                                        public void onClick(DialogInterface dialog, int which) {
                                                                            Intent myIntent = new Intent(MainActivity.this, InputActivity.class);
                                                                            startActivity(myIntent);
                                                                        }
                                                                    }).setNegativeButton("Yes",
                                                                    new DialogInterface.OnClickListener() {
                                                                        @Override
                                                                        public void onClick(DialogInterface dialog, int which) {
                                                                            Intent myIntent = new Intent(MainActivity.this, RecordCardActivity.class);
                                                                            startActivity(myIntent);
                                                                            return;
                                                                        }
                                                                    });
                                                            AlertDialog alert = alert_confirm.create();
                                                            alert.show();
                                                        }
                                                        else {
                                                            Intent myIntent = new Intent(MainActivity.this, InputActivity.class);
                                                            startActivity(myIntent);
                                                        }
                                                        if(hasValues(loc_ids) && hasValues(loc_names)) {
                                                            MainActivity.ioc_id = loc_ids[listview.getSelectedItemPosition()];
                                                            MainActivity.parkinglotname = loc_names[listview.getSelectedItemPosition()];
                                                        }
                                                        helperCard.close();
                                                    }
                                                    else {
                                                        AlertDialog.Builder alert_confirm = new AlertDialog.Builder(MainActivity.this);
                                                        alert_confirm.setMessage("You already made a reservation").setCancelable(false).setPositiveButton("Confirm",
                                                                new DialogInterface.OnClickListener() {
                                                                    @Override
                                                                    public void onClick(DialogInterface dialog, int which) {

                                                                    }
                                                                });
                                                        AlertDialog alert = alert_confirm.create();
                                                        alert.show();
                                                    }
                                                }
                                            });
        checkPermissionPhone();
    }

        private void checkPermissionPhone() {
        if (checkSelfPermission(Manifest.permission.READ_PHONE_STATE)
                != PackageManager.PERMISSION_GRANTED) {
            // Should we show an explanation?
            if (shouldShowRequestPermissionRationale(Manifest.permission.READ_PHONE_STATE)) {
                // Explain to the user why we need to write the permission.
            }
            requestPermissions(new String[]{Manifest.permission.READ_PHONE_STATE}, MY_PERMISSIONS_REQUEST_READ_PHONE_STATE);

            // MY_PERMISSION_REQUEST_STORAGE is an
            // app-defined int constant

        } else {
            // 다음 부분은 항상 허용일 경우에 해당이 됩니다.
            postlogin= new HttpPostLogin();
            postlogin.execute();
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        System.out.println(status);
        if (id == R.id.nav_rc) {
            // Handle the camera action
            if(status.equals("reserved") || status.equals("parked") || status.equals("paying")) {
                Intent intent = new Intent(MainActivity.this, RcActivity.class);
                startActivity(intent);
                helperCard.close();
            }
            else{

            }
        }
        if(id == R.id.nav_handover){
            if(status.equals("reserved") || status.equals("parked")) {
                Intent intent = new Intent(MainActivity.this, HandoverActivity.class);
                startActivity(intent);
                helperCard.close();
            }else{

            }
        }
        if(id==R.id.nav_here){
            if(status.equals("reserved") || status.equals("parked")) {
                Intent intent = new Intent(MainActivity.this, HereActivity.class);
                startActivity(intent);
                helperCard.close();
            }else{

            }
        }
        if(id==R.id.nav_past_rc){
            Intent intent = new Intent(MainActivity.this, PastRcActivity.class);
            startActivity(intent);
            helperCard.close();
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public class HttpPostLogin extends AsyncTask<String, Void, String> {
        @Override
        public String doInBackground(String... params) {
            try {
                URL url = new URL(address+"drivers");
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

                TelephonyManager telManager = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
                phoneNum = telManager.getLine1Number();

                JSONObject json = new JSONObject();
                json.put("phoneNumber", phoneNum);
                json.put("secretKey", secret_k);

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
                    phoneNum = (String) responseJSON.get("phoneNumber");
                    id = (String) responseJSON.get("identificationNumber");
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
            postOauth = new HttpPostOauth();
            postOauth.execute();
        }
    }
    public class HttpPostOauth extends AsyncTask<String, Void, String> {
        @Override
        public String doInBackground(String... params) {
            try {
                URL url = new URL(address+"oauth/token");
                HttpURLConnection   conn    = null;
                OutputStream          os   = null;
                InputStream           is   = null;
                ByteArrayOutputStream baos = null;
                conn = (HttpURLConnection)url.openConnection();
                conn.setConnectTimeout(10000);
                conn.setReadTimeout(10000);
                conn.setRequestMethod("POST");
                String basicAuth ="Basic " + Base64.encodeToString(("user_driver:123456").getBytes(), Base64.NO_WRAP);
                conn.setRequestProperty("Authorization", basicAuth);
                conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                conn.setRequestProperty("Accept", "application/json");
                conn.setDoOutput(true);
                conn.setDoInput(true);

                String req_token= "password="+id+"&username="+phoneNum+"&grant_type=password&scope=read%20write&client_secret=123456&client_id=user_driver";

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

                    access_token = (String) responseJSON.get("access_token");
                    token_type = (String) responseJSON.get("token_type");
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
            getState = new HttpGetState();
            getState.execute();
        }
    }

    public class HttpGetList extends AsyncTask<String, Void, String> {
        @Override
        public String doInBackground(String... params) {
            try {
                URL url = new URL(address+"sureparks/list/"+parkinglotname);
                HttpURLConnection   conn    = null;
                OutputStream          os   = null;
                InputStream           is   = null;
                ByteArrayOutputStream baos = null;
                conn = (HttpURLConnection)url.openConnection();
                conn.setConnectTimeout(10000);
                conn.setReadTimeout(10000);
                conn.setRequestMethod("GET");
                conn.setRequestProperty("Authorization", token_type+" "+access_token);
                conn.setRequestProperty("Content-Type", "application/json");
                conn.setRequestProperty("Accept", "application/json");
                conn.setDoInput(true);
                conn.connect();

                String response;

                int responseCode = conn.getResponseCode();
                System.out.println("========================="+responseCode);

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
                    sureparks = responseJSON.getJSONArray("sureparks");
                    String count = (String) responseJSON.get("count");
                    ct = Integer.parseInt(count);
                    System.out.println(response);

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
            loc_names=new String[ct];
            loc_ids = new String[ct];
            for(int i=0;i<ct;i++){
                try {
                    JSONObject json = sureparks.getJSONObject(i);
                    loc_names[i] = (String) json.get("parkingLotName");
                    loc_ids[i] = (String) json.get("parkingLotID");
                    m_Adapter.add(parkinglotname+"    ID: "+ loc_ids[i]);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    public class HttpGetState extends AsyncTask<String, Void, String> {
        @Override
        public String doInBackground(String... params) {
            try {
                URL url = new URL(address+"drivers/"+phoneNum);
                HttpURLConnection   conn    = null;
                OutputStream          os   = null;
                InputStream           is   = null;
                ByteArrayOutputStream baos = null;
                conn = (HttpURLConnection)url.openConnection();
                conn.setConnectTimeout(10000);
                conn.setReadTimeout(10000);
                conn.setRequestMethod("GET");
                conn.setRequestProperty("Authorization", token_type+" "+ access_token);
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

                    rev_id = (String) responseJSON.get("reservationID");
                    phoneNum = (String) responseJSON.get("phoneNumber");
                    status = (String) responseJSON.get("state");
                    System.out.println(status);
                    System.out.println("rev_id"+rev_id);
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

    boolean hasValues(String... strs) {

        boolean hasValues = true;

        for (String str : strs) {

            if (str == null || str.length() == 0) {
                hasValues = false;
                break;
            }
        }
        return hasValues;
    }
}
