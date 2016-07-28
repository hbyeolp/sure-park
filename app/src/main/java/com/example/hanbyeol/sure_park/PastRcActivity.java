package com.example.hanbyeol.sure_park;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class PastRcActivity extends AppCompatActivity {
    ListViewReservationAdapter m_Adapter;
    ListView listview;
    ReservationDbOpenHelper helperReservation;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_past_rc);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        helperReservation = new ReservationDbOpenHelper(this);
        helperReservation.open();

        listview = (ListView)findViewById(R.id.listView_pre_rc);
        m_Adapter = new ListViewReservationAdapter();
        listview.setAdapter(m_Adapter);
        helperReservation.UpdateListView(m_Adapter);
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        helperReservation.close();
    }
}
