package com.taewon.dolphin;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ActivityContact extends AppCompatActivity {

    ListView contactListView;
    List<ContactItem> contactItemList;
    ImageButton contactBackBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);
        contactListView = (ListView)findViewById(R.id.contactListView);
        contactBackBtn = (ImageButton)findViewById(R.id.contactBackBtn);
        contactItemList = new ArrayList<>();

        contactBackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        requestGetAllContact();
    }

    private void requestGetAllContact()
    {
        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("DATA");
                    for(int i=0; i<jsonArray.length(); i++)
                    {
                        JSONObject obj = jsonArray.getJSONObject(i);
                        String userID = obj.getString("userID");
                        String userName = obj.getString("userName");
                        String userMajor = obj.getString("userMajor");
                        String userPhone = obj.getString("userPhone");
                        if(userID.equals(UserData.getInstance().getUserID()))
                        {
                            continue;
                        }
                        contactItemList.add(new ContactItem(userName, userPhone, userMajor));
                    }
                    ContactAdapter adapter = new ContactAdapter(ActivityContact.this, contactItemList);
                    contactListView.setAdapter(adapter);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
        RequestGetContact validateRequest = new RequestGetContact(UserData.getInstance().getUserMajor(), responseListener);
        RequestQueue queue = Volley.newRequestQueue(ActivityContact.this);
        queue.add(validateRequest);
    }
}
