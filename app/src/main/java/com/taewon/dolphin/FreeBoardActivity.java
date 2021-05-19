package com.taewon.dolphin;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class FreeBoardActivity extends AppCompatActivity{ //클릭 리스너 인터페이스 상속
    private FreeBoardAdapter freeBoardAdapter;
    private ListView freeBoardListView;
    private List<FreeBoardItem> freeBoardItemList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_freeboard);


        freeBoardListView = (ListView)findViewById(R.id.freeBoardListView);
        freeBoardItemList = new ArrayList<>();

        freeBoardListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //실행할거 적으세요..
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        freeBoardItemList.clear();
        Response.Listener<String> responseListener = new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("DATA");
                    int count = 0;
                    int boardID;
                    String title, contents, date, userName, PHONE;

                    for(int i=0; i<jsonArray.length(); i++)
                    {
                        JSONObject object = jsonArray.getJSONObject(i);
                        title = object.get("title").toString();
                        contents = object.get("contents").toString();
                        date = object.get("DATE").toString();
                        userName = object.get("userName").toString();
                        PHONE = object.get("PHONE").toString();
                        boardID = object.getInt("no");
                        freeBoardItemList.add(new FreeBoardItem(title, contents, date, userName, PHONE, boardID));
                        count++;
                    }
                    freeBoardAdapter = new FreeBoardAdapter(FreeBoardActivity.this, freeBoardItemList);
                    freeBoardListView.setAdapter(freeBoardAdapter);
                } catch (Exception e) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(FreeBoardActivity.this);
                    builder.setIcon(R.drawable.icon_dolphins)
                            .setTitle("오류")
                            .setMessage("게시판을 불러오는 중 오류가 발생했습니다.")
                            .setPositiveButton("확인",null)
                            .show();
                    e.printStackTrace();
                }
            }
        };
        GetFreeBoardRequest validateRequest = new GetFreeBoardRequest(responseListener);
        RequestQueue queue = Volley.newRequestQueue(FreeBoardActivity.this);
        queue.add(validateRequest);
    }
}