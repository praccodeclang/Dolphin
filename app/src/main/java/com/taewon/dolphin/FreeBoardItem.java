package com.taewon.dolphin;

public class FreeBoardItem {

    private String title;
    private String contents;
    private String date;
    private String userName;
    private String userID;
    private String userPhone;
    private String commentCount;
    private int boardId;

    public FreeBoardItem(int boardId, String title, String contents, String userName, String userID, String userPhone, String date, String commentCount) {
        this.title = title;
        this.contents = contents;
        this.date = date;
        this.userName = userName;
        this.userPhone = userPhone;
        this.boardId = boardId;
        this.userID = userID;
        this.commentCount = commentCount;
    }




    //getters
    public String getTitle() {
        return title;
    }
    public String getContents() {
        return contents;
    }
    public String getDate() {
        return date;
    }
    public int getBoardId() { return boardId; }
    public String getUserName() { return userName; }

    public String getCommentCount() {
        return commentCount;
    }

    public String getUserID() { return userID; }

    public String getUserPhone() { return userPhone; }
}
