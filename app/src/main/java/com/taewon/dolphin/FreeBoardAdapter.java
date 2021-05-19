package com.taewon.dolphin;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

public class FreeBoardAdapter extends BaseAdapter {
    private Context context;
    private List<FreeBoardItem> freeBoardItemList;

    public FreeBoardAdapter(Context context, List<FreeBoardItem> freeBoardItemList) {
        this.context = context;
        this.freeBoardItemList = freeBoardItemList;
    }


    //getters
    @Override
    public int getCount() {
        return freeBoardItemList.size();
    }

    @Override
    public Object getItem(int i) {
        return freeBoardItemList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup parent) {
        View v = View.inflate(context, R.layout.freeboard_item_list, null);
        TextView boardTitle = (TextView)v.findViewById(R.id.freeboard_title);
        TextView boardDate = (TextView)v.findViewById(R.id.freeboard_date);

        boardTitle.setText(freeBoardItemList.get(i).getTitle());
        boardDate.setText(freeBoardItemList.get(i).getDate());
        return v;
    }
}
