package com.example.hanbyeol.sure_park;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class RecordCardActivity extends AppCompatActivity {
    ArrayAdapter m_Adapter;
    ListView listview;

    private CardDbOpenHelper helperCard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record_card);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        helperCard = new CardDbOpenHelper(this);
        helperCard.open();

        listview = (ListView)findViewById(R.id.listView_record_card);
        m_Adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1);
        listview.setAdapter(m_Adapter);
        MainActivity.cardnames=new String[helperCard.Count()];
        MainActivity.cardnums=new String[helperCard.Count()];
        MainActivity.cardmons=new String[helperCard.Count()];
        MainActivity.cardyears=new String[helperCard.Count()];
        MainActivity.cardcodes=new String[helperCard.Count()];

        helperCard.UpdateListView(m_Adapter,MainActivity.cardnums, MainActivity.cardnames, MainActivity.cardmons, MainActivity.cardyears, MainActivity.cardcodes);

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                MainActivity.cardname=MainActivity.cardnames[listview.getSelectedItemPosition()];
                MainActivity.cardnum=MainActivity.cardnums[listview.getSelectedItemPosition()];
                MainActivity.cardmon=MainActivity.cardmons[listview.getSelectedItemPosition()];
                MainActivity.cardyear=MainActivity.cardyears[listview.getSelectedItemPosition()];
                MainActivity.cardcode=MainActivity.cardcodes[listview.getSelectedItemPosition()];
                MainActivity.cardstate=1;
                Intent intent =  new Intent(RecordCardActivity.this, InputActivity.class);
                startActivity(intent);
                finish();
            }
        });

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        helperCard.close();
    }
}
