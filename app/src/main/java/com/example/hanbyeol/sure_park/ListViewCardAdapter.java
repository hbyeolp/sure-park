package com.example.hanbyeol.sure_park;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by hanbyeol on 2016-07-27.
 */
public class ListViewCardAdapter extends BaseAdapter {

    CardDbOpenHelper cardDbOpenHelper;
    boolean del_boolean=false;
    public ArrayList<ListViewCardItem> listViewItemList = new ArrayList<ListViewCardItem>() ;

    public ListViewCardAdapter() {

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
            convertView = inflater.inflate(R.layout.listview_card_item, parent, false);
        }
        TextView cardNumView = (TextView) convertView.findViewById(R.id.textView_card_number);
        TextView cardNameView = (TextView) convertView.findViewById(R.id.textView_card_Holder);
        TextView cardDateView = (TextView) convertView.findViewById(R.id.textView_card_Expiration);
        TextView cardCodeView = (TextView) convertView.findViewById(R.id.textView_card_code);
        ImageButton btn_delete = (ImageButton) convertView.findViewById(R.id.button_delete);
        btn_delete.setTag(position);
        btn_delete.setOnClickListener((View.OnClickListener)context);

        ListViewCardItem listViewItem = listViewItemList.get(position);

        cardNumView.setText(listViewItem.getCardnum());
        cardNameView.setText(listViewItem.getCardname());
        cardDateView.setText(listViewItem.getCarddate());
        cardCodeView.setText(listViewItem.getCardcode());

        return convertView;
    }

    // 吏?뺥븳 ?꾩튂(position)???덈뒗 ?곗씠?곗? 愿怨꾨맂 ?꾩씠??row)??ID瑜?由ы꽩. : ?꾩닔 援ы쁽
    @Override
    public long getItemId(int position) {
        return position ;
    }

    @Override
    public Object getItem(int position) {
        return listViewItemList.get(position) ;
    }

    public void addItem(String cardnum, String desc, String carddate, String cvc) {
        ListViewCardItem item = new ListViewCardItem();

        item.setCardnum(cardnum);
        item.setCardname(desc);
        item.setCarddate(carddate);
        item.setCardcode(cvc);

        listViewItemList.add(item);
    }
    public void removeItem(int num) {

        listViewItemList.remove(num);
    }
}
