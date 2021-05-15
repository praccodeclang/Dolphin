package com.taewon.dolphin;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class UserRegister extends StringRequest {

    private final static String URL = "http://xodnjs2546.cafe24.com/userRegister.php";
    private Map<String, String> mHash;

    public UserRegister(String userID, String userPassword, String userName, String userMajor, String userDept, String PHONE ,Response.Listener<String> listener) {
        super(Method.POST, URL, listener, null); // 여기가 실질적으로 요청을 보내는 곳입니다.
        mHash = new HashMap<>();
        mHash.put("userID", userID);
        mHash.put("userPassword", userPassword);
        mHash.put("userName", userName);
        mHash.put("userMajor", userMajor);
        mHash.put("userDept", userDept);
        mHash.put("userPhone", PHONE);
    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return mHash;
    }
}
