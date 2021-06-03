package com.taewon.dolphin;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.BlendMode;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorStateListDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.RequiresApi;

import java.text.ParseException;
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

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public View getView(int i, View convertView, ViewGroup parent) {
        View v = View.inflate(context, R.layout.freeboard_item, null);
        ImageView boardProfileImg = (ImageView)v.findViewById(R.id.boardProfileImg);
        TextView boardTitle = (TextView)v.findViewById(R.id.freeboardTitle);
        TextView boardDate = (TextView)v.findViewById(R.id.freeboardDate);
        TextView boardContent = (TextView)v.findViewById(R.id.freeboardContent);
        TextView userName = (TextView)v.findViewById(R.id.freeboardUserName);

        boardTitle.setText(freeBoardItemList.get(i).getTitle());
        boardContent.setText(freeBoardItemList.get(i).getContents());
        userName.setText(freeBoardItemList.get(i).getUserName());
        try {
            boardDate.setText(ActivityMain.calDate_ShouldReturnString(freeBoardItemList.get(i).getDate()));
        } catch (ParseException e) {
            boardDate.setText(freeBoardItemList.get(i).getDate());
        }
        if(freeBoardItemList.get(i).getUserID().equals(UserData.getInstance().getUserID()))
        {
            boardProfileImg.setImageResource(R.drawable.icon_dolphins);
        }
        return v;
    }
}
