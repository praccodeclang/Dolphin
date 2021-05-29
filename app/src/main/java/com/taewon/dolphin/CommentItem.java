package com.taewon.dolphin;

import android.widget.LinearLayout;
import android.widget.TextView;

public class CommentItem {
    private String commentUserName;
    private String commentDate;
    private String commentText;
    private String BoardID;
    public CommentItem(String BoardID, String userName, String commentDate, String commentText){
        this.BoardID = BoardID;
        this.commentUserName = userName;
        this.commentDate = commentDate;
        this.commentText = commentText;
    }


    //getters
    public String getCommentUserName() {
        return commentUserName;
    }

    public String getBoardID() { return BoardID; }

    public String getCommentDate() { return commentDate; }

    public String getCommentText() {
        return commentText;
    }

}
