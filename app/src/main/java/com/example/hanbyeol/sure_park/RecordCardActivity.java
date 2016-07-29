package com.example.hanbyeol.sure_park;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;

public class RecordCardActivity extends AppCompatActivity implements AdapterView.OnItemClickListener, View.OnClickListener {
    ListViewCardAdapter m_Adapter;
    ListView listview;
    boolean del_boolean=false;
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
        m_Adapter = new ListViewCardAdapter();
        listview.setAdapter(m_Adapter);
        MainActivity.cardfirstnames=new String[helperCard.Count()];
        MainActivity.cardlastnames=new String[helperCard.Count()];
        MainActivity.cardnums=new String[helperCard.Count()];
        MainActivity.cardmons=new String[helperCard.Count()];
        MainActivity.cardyears=new String[helperCard.Count()];
        MainActivity.cardcodes=new String[helperCard.Count()];
        helperCard.UpdateListView(m_Adapter,MainActivity.cardnums, MainActivity.cardfirstnames, MainActivity.cardlastnames, MainActivity.cardmons, MainActivity.cardyears, MainActivity.cardcodes);

        listview.setOnItemClickListener(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        helperCard.close();
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Intent intent =  new Intent(RecordCardActivity.this, InputActivity.class);
        startActivity(intent);
        System.out.println(listview.getSelectedItemPosition());
        MainActivity.item=i;
        MainActivity.cardfirstname=MainActivity.cardfirstnames[i];
        MainActivity.cardlastname = MainActivity.cardlastnames[i];
        MainActivity.cardnum=MainActivity.cardnums[i];
        MainActivity.cardmon=MainActivity.cardmons[i];
        MainActivity.cardyear=MainActivity.cardyears[i];
        MainActivity.cardcode=MainActivity.cardcodes[i];
        MainActivity.cardstate=1;
        System.out.println("getselectposition"+listview.getSelectedItemPosition());
        System.out.println("cardstate " + MainActivity.cardstate);
        helperCard.close();
        finish();
    }
    @Override
    public void onClick(final View v) {
        System.out.println("delete button click");
        AlertDialog.Builder alert_confirm = new AlertDialog.Builder(RecordCardActivity.this);
        alert_confirm.setMessage("Would you like to delete the stored card information?").setCancelable(false).setPositiveButton("No",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).setNegativeButton("Yes",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        System.out.println("item " + (int)v.getTag());
                        helperCard.Delete(MainActivity.cardnums[(int)v.getTag()]);
                        m_Adapter.removeItem((int)v.getTag());
                        m_Adapter.notifyDataSetChanged();
                        del_boolean=true;
                    }
                });
        AlertDialog alert = alert_confirm.create();
        alert.show();
        if(del_boolean){
            AlertDialog.Builder alert_confirm1 = new AlertDialog.Builder(RecordCardActivity.this);
            alert_confirm.setMessage("Delete Complete").setCancelable(false).setPositiveButton("Confirm",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            del_boolean=false;
                        }
                    });
            AlertDialog alert1 = alert_confirm1.create();
            alert1.show();
        }
    }
}
