package com.taewon.dolphin;

import androidx.annotation.Nullable;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class RequestGetFreeBoard extends StringRequest {

    final private static String URL = "http://xodnjs2546.cafe24.com/getFreeBoard.php";
    final static String Token = "dolphin";
    private Map<String, String> mHash;

    public RequestGetFreeBoard(Response.Listener<String> listener) {
        super(Method.POST, URL, listener, null);
        System.out.println(Token);
        mHash = new HashMap<>();
        mHash.put("Token", Token);
    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return mHash;
    }
}
