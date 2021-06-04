package com.taewon.dolphin;

import android.widget.LinearLayout;
import android.widget.TextView;

public class ContactItem {
    private String contactUserName;
    private String contactUserPhoneNum;
    private String contactUserDept;
    public ContactItem(String contactUserName, String contactUserPhoneNum, String contactUserDept){
        this.contactUserName = contactUserName;
        this.contactUserPhoneNum = contactUserPhoneNum;
        this.contactUserDept = contactUserDept;
    }


    //getters
    public String getContactUserName() {
        return contactUserName;
    }
    public String getContactUserPhoneNum() {
        return contactUserPhoneNum;
    }
    public String getContactUserDept() {
        return contactUserDept;
    }
}
