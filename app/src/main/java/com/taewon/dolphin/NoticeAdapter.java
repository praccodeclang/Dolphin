package com.taewon.dolphin;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

public class NoticeAdapter extends BaseAdapter {
    private Context context;
    private List<NoticeItem> noticeItemList;

    public NoticeAdapter(Context context, List<NoticeItem> noticeItemList) {
        this.context = context;
        this.noticeItemList = noticeItemList;
    }


    //getters
    @Override
    public int getCount() {
        return noticeItemList.size();
    }

    @Override
    public Object getItem(int i) {
        return noticeItemList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup parent) {
        View v = View.inflate(context, R.layout.notice_item, null);
        TextView noticeTitle = (TextView)v.findViewById(R.id.noticeTitle);
        TextView noticeDate = (TextView)v.findViewById(R.id.noticeDate);
        noticeTitle.setText(noticeItemList.get(i).getTitle());
        noticeDate.setText(noticeItemList.get(i).getDate());
        return v;
    }
}
