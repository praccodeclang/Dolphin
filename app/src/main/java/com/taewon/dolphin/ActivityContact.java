package com.taewon.dolphin;

import android.os.Bundle;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

public class ActivityContact extends AppCompatActivity {

    ListView contactListView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mypage);
        contactListView = (ListView)findViewById(R.id.contactListView);
    }

    private void getContactList(ListView contactListView)
    {

    }
}
