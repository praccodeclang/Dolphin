package com.taewon.dolphin;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class RequestUpdateInfo extends StringRequest {

    private Map<String, String> mHash;

    public RequestUpdateInfo(String userStdCode,String userID,String newPassword, String url, Response.Listener<String> listener) {
        super(Method.POST, url, listener, null); // 여기가 실질적으로 요청을 보내는 곳입니다.
        mHash = new HashMap<>();
        mHash.put("userStudentCode", userStdCode);
        mHash.put("userID", userID);
        mHash.put("newPassword", newPassword);
    }


    @Override
    protected Map<String, String> getParams () throws AuthFailureError {
        return mHash;
    }
}
