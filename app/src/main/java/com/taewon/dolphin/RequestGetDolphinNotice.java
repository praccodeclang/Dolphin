package com.taewon.dolphin;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class RequestGetDolphinNotice extends StringRequest {

    private final static String URL = "https://xodnjs2546.cafe24.com/getDolphinNotice.php";
    private Map<String, String> mHash;

    public RequestGetDolphinNotice(Response.Listener<String> listener) {
        super(Method.POST, URL, listener, null); // 여기가 실질적으로 요청을 보내는 곳입니다.
        mHash = new HashMap<>();
        mHash.put("Token", "dolphin");
    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return mHash;
    }
}
