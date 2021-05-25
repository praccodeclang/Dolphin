package com.taewon.dolphin;

public class UserData {
    /* 현재 어플을 사용하고있는 유저의 데이터로, 로그인 성공 시, 초기화 되며 앱 실행 중 하나만 존재해야합니다.*/
    /* 어플 사용 중 하나만 존재해아합니다.*/
    /* UserData 클래스 사용법 */
    /* UserData.getInstance().get ~~~ */
    /* UserData.getInstance().set ~~~ */
    /* 싱글톤 패턴*/
    private static UserData instance = new UserData();
    public static UserData getInstance(){
        return instance;
    }
    private UserData(){ }
    /* 싱글턴 패턴 end */

    private String userID, userName, userMajor, userDept, userPhoneNum;




    //getters, setters.
    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserMajor() {
        return userMajor;
    }

    public void setUserMajor(String userMajor) {
        this.userMajor = userMajor;
    }

    public String getUserDept() {
        return userDept;
    }

    public void setUserDept(String userDept) {
        this.userDept = userDept;
    }

    public String getUserPhoneNum() {
        return userPhoneNum;
    }

    public void setUserPhoneNum(String userPhoneNum) {
        this.userPhoneNum = userPhoneNum;
    }
}
