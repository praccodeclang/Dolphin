package com.taewon.dolphin;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

public class CommentAdapter extends BaseAdapter {

    private Context context;
    private List<CommentItem> commentItemList;


    public CommentAdapter(Context context, List<CommentItem> freeBoardItemList) {
        this.context = context;
        this.commentItemList = freeBoardItemList;
    }


    //getters
    @Override
    public int getCount() {
        return commentItemList.size();
    }

    @Override
    public Object getItem(int i) {
        return commentItemList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup parent) {
        View v = View.inflate(context, R.layout.freeboard_item, null);
        TextView commentUserName = (TextView)v.findViewById(R.id.commentUserName);
        TextView commentDate = (TextView)v.findViewById(R.id.commentDate);
        TextView commentText = (TextView)v.findViewById(R.id.commentText);
        commentUserName.setText(commentItemList.get(i).getCommentUserName());
        commentDate.setText(commentItemList.get(i).getCommentDate());
        commentText.setText(commentItemList.get(i).getCommentText());
        return v;
    }
}
