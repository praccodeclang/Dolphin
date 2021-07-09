package com.taewon.dolphin;

import com.android.volley.AuthFailureError;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class RequestUpdateDeviceInfo extends StringRequest {

    final private static String URL = "https://xodnjs2546.cafe24.com/updateUserDevice.php";
    private Map<String, String> mHash;

    public RequestUpdateDeviceInfo(String userID, String IMEI, String MODEL) {
        super(Method.POST, URL, null, null); // 여기가 실질적으로 요청을 보내는 곳입니다.
        mHash = new HashMap<>();
        mHash.put("IMEI",IMEI);
        mHash.put("MODEL", MODEL);
        mHash.put("userID", userID);
        mHash.put("Token", "dolphin");
    }

    @Override
    protected Map<String, String> getParams () throws AuthFailureError {
        return mHash;
    }
}
