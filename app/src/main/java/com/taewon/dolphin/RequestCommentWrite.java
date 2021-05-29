package com.taewon.dolphin;

import androidx.annotation.Nullable;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class RequestCommentWrite extends StringRequest {

    final private static String url = "https://xodnjs2546.cafe24.com/insertFreeBoardComment.php";
    private Map<String, String> mHash;
    public RequestCommentWrite(String BoardID, String userName, String userComment, Response.Listener<String> listener) {
        super(Method.POST, url, listener, null);
        mHash = new HashMap<>();
        mHash.put("BoardID", BoardID);
        mHash.put("userName", userName);
        mHash.put("userComment", userComment);
    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return mHash;
    }
}
