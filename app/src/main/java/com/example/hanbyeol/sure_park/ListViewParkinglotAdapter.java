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
public class ListViewParkinglotAdapter extends BaseAdapter {
    // Adapter??異붽????곗씠?곕? ??ν븯湲??꾪븳 ArrayList
    private ArrayList<ListViewParkinglotItem> listViewItemList = new ArrayList<ListViewParkinglotItem>() ;

    // ListViewAdapter???앹꽦??
    public ListViewParkinglotAdapter() {

    }

    // Adapter???ъ슜?섎뒗 ?곗씠?곗쓽 媛쒖닔瑜?由ы꽩. : ?꾩닔 援ы쁽
    @Override
    public int getCount() {
        return listViewItemList.size() ;
    }

    // position???꾩튂???곗씠?곕? ?붾㈃??異쒕젰?섎뒗???ъ슜??View瑜?由ы꽩. : ?꾩닔 援ы쁽
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final int pos = position;
        final Context context = parent.getContext();

        // "listview_item" Layout??inflate?섏뿬 convertView 李몄“ ?띾뱷.
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.listview_parkinglot_item, parent, false);
        }

        // ?붾㈃???쒖떆??View(Layout??inflate???쇰줈遺???꾩젽?????李몄“ ?띾뱷
        TextView titleTextView = (TextView) convertView.findViewById(R.id.textview_parkinglotname) ;

        // Data Set(listViewItemList)?먯꽌 position???꾩튂???곗씠??李몄“ ?띾뱷
        ListViewParkinglotItem listViewItem = listViewItemList.get(position);

        // ?꾩씠????媛??꾩젽???곗씠??諛섏쁺
        titleTextView.setText(listViewItem.getTitle());

        return convertView;
    }

    // 吏?뺥븳 ?꾩튂(position)???덈뒗 ?곗씠?곗? 愿怨꾨맂 ?꾩씠??row)??ID瑜?由ы꽩. : ?꾩닔 援ы쁽
    @Override
    public long getItemId(int position) {
        return position ;
    }

    // 吏?뺥븳 ?꾩튂(position)???덈뒗 ?곗씠??由ы꽩 : ?꾩닔 援ы쁽
    @Override
    public Object getItem(int position) {
        return listViewItemList.get(position) ;
    }

    // ?꾩씠???곗씠??異붽?瑜??꾪븳 ?⑥닔. 媛쒕컻?먭? ?먰븯?붾?濡??묒꽦 媛??
    public void addItem(String title) {
        ListViewParkinglotItem item = new ListViewParkinglotItem();

        item.setTitle(title);

        listViewItemList.add(item);
    }
    public void removeItem(int num) {
        //listViewItemList.remove()
        if (num != 0) {
            for (int i = 0; i < num; i++)
                listViewItemList.remove(i);
        }
    }
}
