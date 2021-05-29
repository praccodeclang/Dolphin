package com.taewon.dolphin;

public class CommentItem {
    private String commentUserName;
    private String commentDate;
    private String commentText;
    public CommentItem(String userName, String commentDate, String commentText){
        this.commentUserName = userName;
        this.commentDate = commentDate;
        this.commentText = commentText;
    }


    //getters
    public String getCommentUserName() {
        return commentUserName;
    }

    public String getCommentDate() {
        return commentDate;
    }

    public String getCommentText() {
        return commentText;
    }

}
