package com.taewon.dolphin;

import androidx.annotation.Nullable;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class RequestFreeBoardWrite extends StringRequest {

    final private static String url = "https://xodnjs2546.cafe24.com/insertFreeBoard.php";
    private Map<String, String> mHash;
    public RequestFreeBoardWrite(String title, String contents, String userName, String userID, String userPhoneNum,Response.Listener<String> listener) {
        super(Method.POST, url, listener, null);
        mHash = new HashMap<>();
        mHash.put("title", title);
        mHash.put("contents", contents);
        mHash.put("userName", userName);
        mHash.put("userID", userID);
        mHash.put("userPhoneNum", userPhoneNum);
    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return mHash;
    }
}
