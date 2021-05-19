package com.taewon.dolphin;

public class FreeBoardItem {

    private String title;
    private String contents;
    private String date;
    private String userName;
    private String userPhone;
    private int boardId;

    public FreeBoardItem(String title, String contents, String date, String userName, String userPhone, int boardId) {
        this.title = title;
        this.contents = contents;
        this.date = date;
        this.userName = userName;
        this.userPhone = userPhone;
        this.boardId = boardId;
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
}
