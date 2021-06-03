package com.taewon.dolphin;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.text.ParseException;
import java.util.List;

public class CommentAdapter extends BaseAdapter{

    private Context context;
    private List<CommentItem> commentItemList;

    public CommentAdapter(Context context, List<CommentItem> commentItemList) {
        this.context = context;
        this.commentItemList = commentItemList;
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
        View v = View.inflate(context, R.layout.comment_item, null);
        ImageView commentImg = (ImageView)v.findViewById(R.id.commentImg);
        TextView commentUserName = (TextView)v.findViewById(R.id.commentUserName);
        TextView commentDate = (TextView)v.findViewById(R.id.commentDate);
        TextView commentText = (TextView)v.findViewById(R.id.commentText);
        LinearLayout commentUDBtns = (LinearLayout)v.findViewById(R.id.commentUDBtns);
        TextView commentDelete = (TextView)v.findViewById(R.id.commentDelete);


        try {
            commentDate.setText(ActivityMain.calDate_ShouldReturnString(commentItemList.get(i).getCommentDate()));
        } catch (ParseException e) {
            commentDate.setText(commentItemList.get(i).getCommentDate());
        }
        if(isMyComment(i))
        {
            commentImg.setImageResource(R.drawable.icon_dolphins);
            commentUDBtns.setVisibility(View.VISIBLE);
            commentDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setIcon(R.drawable.ic_baseline_block_24)
                            .setTitle("댓글 삭제")
                            .setMessage("댓글을 삭제하시겠습니까?")
                            .setNegativeButton("취소",null)
                            .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Response.Listener<String> responseListener = new Response.Listener<String>() {
                                        @Override
                                        public void onResponse(String response) {
                                            try {
                                                JSONObject jsonObject = new JSONObject(response);
                                                boolean success = jsonObject.getBoolean("success");
                                                if(success)
                                                {
                                                    Toast.makeText(context, "삭제되었습니다.", Toast.LENGTH_SHORT).show();
                                                    ((ActivityFreeBoardViewer) ActivityFreeBoardViewer.mContext).requestLoadComments();
                                                }
                                            }
                                            catch (Exception e)
                                            {
                                                Toast.makeText(context, "삭제하지 못했습니다.", Toast.LENGTH_SHORT).show();
                                            }
                                        }

                                    };
                                    RequestCommentDelete validateRequest = new RequestCommentDelete(commentItemList.get(i).getBoardID(), commentItemList.get(i).getCommentDate(), responseListener);
                                    RequestQueue requestQueue = Volley.newRequestQueue(context);
                                    requestQueue.add(validateRequest);
                                }
                            })
                            .show();
                }
            });
        }
        else { commentUDBtns.setVisibility(View.GONE); }
        commentText.setText(commentItemList.get(i).getCommentText());
        commentUserName.setText(commentItemList.get(i).getCommentUserName());

        return v;
    }
    private boolean isMyComment(int index)
    {
        if(commentItemList.get(index).getCommentUserID().equals(UserData.getInstance().getUserID()))
        {
            return true;
        }
        return false;
    }

}
