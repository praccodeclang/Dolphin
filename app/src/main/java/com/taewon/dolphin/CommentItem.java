package com.taewon.dolphin;

import android.widget.LinearLayout;
import android.widget.TextView;

public class CommentItem {
    private String commentUserName;
    private String commentDate;
    private String commentText;
    private String BoardID;
    private String commentUserID;
    public CommentItem(String BoardID, String userName, String commentUserID, String commentDate, String commentText){
        this.BoardID = BoardID;
        this.commentUserName = userName;
        this.commentDate = commentDate;
        this.commentText = commentText;
        this.commentUserID = commentUserID;
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

    public String getCommentUserID() { return commentUserID; }
}
