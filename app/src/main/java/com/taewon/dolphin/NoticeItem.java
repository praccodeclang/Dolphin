package com.taewon.dolphin;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public class NoticeItem {
    private String title;
    private String date;
    private String url;

    public NoticeItem(String url, String title, String date) {
        this.url = url;
        this.title = title;
        this.date = date;
    }

    //getters
    public String getTitle() { return title; }

    public String getDate() { return date; }

    public String getUrl() { return url; }
}
