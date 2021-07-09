package com.taewon.dolphin;

import androidx.annotation.Nullable;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class RequestGetContact extends StringRequest {

    final private static String url = "http://xodnjs2546.cafe24.com/getUsersPhoneNum.php";
    private Map<String, String> mHash;
    public RequestGetContact(String userMajor, Response.Listener<String> listener) {
        super(Method.POST, url, listener, null);
        mHash = new HashMap<>();
        mHash.put("userMajor",userMajor);
        mHash.put("Token", "dolphin");
    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return mHash;
    }
}
