package com.example.hanbyeol.sure_park;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by hanbyeol on 2016-07-27.
 */
public class ListViewReservationAdapter extends BaseAdapter {
    private ArrayList<ListViewReservationItem> listViewItemList = new ArrayList<ListViewReservationItem>() ;

    public ListViewReservationAdapter() {

    }

    @Override
    public int getCount() {
        return listViewItemList.size() ;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final int pos = position;
        final Context context = parent.getContext();

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.listview_reservation_item, parent, false);
        }

        TextView parkinglotidTextView = (TextView) convertView.findViewById(R.id.textView_parkinglot_id) ;
        TextView emailTextView = (TextView) convertView.findViewById(R.id.textView_e_mail) ;
        TextView carsizeTextView = (TextView) convertView.findViewById(R.id.textView_car_size) ;
        TextView reservationtimeTextView = (TextView) convertView.findViewById(R.id.textView_reservation_time) ;
        TextView entrancetimeTextView = (TextView) convertView.findViewById(R.id.textView_entrance_time) ;
        TextView exittimeTextView = (TextView) convertView.findViewById(R.id.textView_exit_time) ;


        ListViewReservationItem listViewItem = listViewItemList.get(position);

        parkinglotidTextView.setText(listViewItem.getParkinglotID());
        emailTextView.setText(listViewItem.getEmail());
        carsizeTextView.setText(listViewItem.getCarSize());
        reservationtimeTextView.setText(listViewItem.getReservationTime());
        entrancetimeTextView.setText(listViewItem.getEntranceTime());
        exittimeTextView.setText(listViewItem.getExitTime());

        return convertView;
    }

    @Override
    public long getItemId(int position) {
        return position ;
    }

    @Override
    public Object getItem(int position) {
        return listViewItemList.get(position) ;
    }

    public void addItem(String parkinglotname, String email, String carSize, String reservationTime, String entranceTime, String exitTime) {
        ListViewReservationItem item = new ListViewReservationItem();
        item.setParkinglotID(parkinglotname);
        item.setEmail(email);
        item.setCarSize(carSize);
        item.setReservationTime(reservationTime);
        item.setEntranceTime(entranceTime);
        item.setExitTime(exitTime);

        listViewItemList.add(item);
    }
}
