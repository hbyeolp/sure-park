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
public class ListViewCardAdapter extends BaseAdapter {
    // Adapter에 추가된 데이터를 저장하기 위한 ArrayList
    private ArrayList<ListViewCardItem> listViewItemList = new ArrayList<ListViewCardItem>() ;

    // ListViewAdapter의 생성자
    public ListViewCardAdapter() {

    }

    // Adapter에 사용되는 데이터의 개수를 리턴. : 필수 구현
    @Override
    public int getCount() {
        return listViewItemList.size() ;
    }

    // position에 위치한 데이터를 화면에 출력하는데 사용될 View를 리턴. : 필수 구현
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final int pos = position;
        final Context context = parent.getContext();

        // "listview_item" Layout을 inflate하여 convertView 참조 획득.
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.listview_card_item, parent, false);
        }
        TextView cardNumView = (TextView) convertView.findViewById(R.id.textView_card_number);
        TextView cardNameView = (TextView) convertView.findViewById(R.id.textView_card_Holder);
        TextView cardDateView = (TextView) convertView.findViewById(R.id.textView_card_Expiration);
        TextView cardCodeView = (TextView) convertView.findViewById(R.id.textView_card_code);

        // Data Set(listViewItemList)에서 position에 위치한 데이터 참조 획득
        ListViewCardItem listViewItem = listViewItemList.get(position);

        // 아이템 내 각 위젯에 데이터 반영
        cardNumView.setText(listViewItem.getCardnum());
        cardNameView.setText(listViewItem.getCardname());
        cardDateView.setText(listViewItem.getCarddate());
        cardCodeView.setText(listViewItem.getCardcode());

        return convertView;
    }

    // 지정한 위치(position)에 있는 데이터와 관계된 아이템(row)의 ID를 리턴. : 필수 구현
    @Override
    public long getItemId(int position) {
        return position ;
    }

    // 지정한 위치(position)에 있는 데이터 리턴 : 필수 구현
    @Override
    public Object getItem(int position) {
        return listViewItemList.get(position) ;
    }

    // 아이템 데이터 추가를 위한 함수. 개발자가 원하는대로 작성 가능.
    public void addItem(String cardnum, String desc, String carddate, String cvc) {
        ListViewCardItem item = new ListViewCardItem();

        item.setCardnum(cardnum);
        item.setCardname(desc);
        item.setCarddate(carddate);
        item.setCardcode(cvc);

        listViewItemList.add(item);
    }
}
