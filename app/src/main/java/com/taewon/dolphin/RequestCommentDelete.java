package com.taewon.dolphin;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class RequestCommentDelete extends StringRequest {

    final private static String URL = "http://xodnjs2546.cafe24.com/deleteFreeBoardComment.php";
    private Map<String, String> mHash;

    public RequestCommentDelete(String BoardID, String date, Response.Listener<String> listener) {
        super(Method.POST, URL, listener, null);
        mHash = new HashMap<>();
        mHash.put("BoardID", BoardID);
        mHash.put("date", date);
    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return mHash;
    }
}
