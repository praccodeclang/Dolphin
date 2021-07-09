package com.taewon.dolphin;

import androidx.annotation.Nullable;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class RequestCallMessage extends StringRequest {

    final private static String url = "http://xodnjs2546.cafe24.com/insertContactLog.php";
    private Map<String, String> mHash;
    public RequestCallMessage(String callerName,String receiverName, String conType, Response.Listener<String> listener) {
        super(Method.POST, url, listener, null);
        mHash = new HashMap<>();
        mHash.put("Token", "dolphin");
        mHash.put("callerName",callerName);
        mHash.put("receiverName",receiverName);
        mHash.put("conType",conType);
    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return mHash;
    }
}
