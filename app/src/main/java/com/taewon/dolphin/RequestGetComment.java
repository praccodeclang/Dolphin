package com.taewon.dolphin;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class RequestGetComment extends StringRequest {
    final private static String URL = "https://xodnjs2546.cafe24.com/getFreeBoardComment.php";
    private Map<String, String> mHash;

    public RequestGetComment(String BoardId, Response.Listener<String> listener) {
        super(Method.POST, URL, listener, null);
        mHash = new HashMap<>();
        mHash.put("BoardID", BoardId);
    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return mHash;
    }
}
