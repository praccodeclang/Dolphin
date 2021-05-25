package com.taewon.dolphin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity{

    private TextView moreViewFreeBoard;
    private TextView moreViewNotice;
    private TextView profileUserName;
    private TextView profileUserDept;
    private ListView mainNoticeListView;
    private ListView mainFreeBoardListView;
    private ImageView myPageBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        moreViewFreeBoard = (TextView) findViewById(R.id.moreViewFreeBoard);
        moreViewNotice = (TextView) findViewById(R.id.moreViewNotice);
        profileUserName = (TextView)findViewById(R.id.profileUserName);
        profileUserDept = (TextView)findViewById(R.id.profileUserDept);
        mainNoticeListView = findViewById(R.id.mainNoticeListView);
        mainFreeBoardListView = findViewById(R.id.mainFreeBoardListView);
        myPageBtn = findViewById(R.id.myPageBtn);

        //로그인 창에서 넘어오면, 프로필의 이름과 학과를 UserData 클래스에 저장된 이름과 학과로 초기화합니다.
        profileUserName.setText(UserData.getInstance().getUserName());
        profileUserDept.setText(UserData.getInstance().getUserDept());


        //온클릭 리스너들
        moreViewNotice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //인텐트 생성해서 공지사항 페이지로 이동합니다.
            }
        });
        moreViewFreeBoard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, FreeBoardActivity.class);
                startActivity(intent);
            }
        });

        //마이페이지 및 설정 액티비티로 넘어갑니다.
        myPageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, MypageActivity.class);
                startActivity(intent);
            }
        });

    }//onCreate End

    @Override
    protected void onResume() {
        super.onResume();
        //mainNoticeListView에 아이템 추가(3개만 넣어볼게요.)
        JsoupAsyncTask jsoupAsyncTask = new JsoupAsyncTask(this, "https://www.uc.ac.kr/computer/index.php?pCode=noticeall", mainNoticeListView, 3);
        jsoupAsyncTask.execute();
        setListViewHeightBasedOnChildren(mainNoticeListView);

        //mainFreeBoardListView 아이템 추가(3개만 넣어볼게요.)
        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("DATA");
                    List<FreeBoardItem> freeBoardItemList = new ArrayList<>();

                    int boardID;

                    for(int i=0; i<3; i++)
                    {
                        String title, contents, date, userName, PHONE;
                        JSONObject object = jsonArray.getJSONObject(i);
                        title = object.get("title").toString();
                        contents = object.get("contents").toString();
                        date = object.get("DATE").toString();
                        userName = object.get("userName").toString();
                        PHONE = object.get("PHONE").toString();
                        boardID = object.getInt("no");
                        freeBoardItemList.add(new FreeBoardItem(title, contents, date, userName, PHONE, boardID));
                    }
                    FreeBoardAdapter freeBoardAdapter = new FreeBoardAdapter(MainActivity.this, freeBoardItemList);
                    mainFreeBoardListView.setAdapter(freeBoardAdapter);
                    setListViewHeightBasedOnChildren(mainFreeBoardListView);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
        RequestGetFreeBoard freeBoardRequest = new RequestGetFreeBoard(responseListener);
        RequestQueue queue = Volley.newRequestQueue(MainActivity.this);
        queue.add(freeBoardRequest);
    }

    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            // pre-condition
            return;
        }

        int totalHeight = 0;

        int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(), View.MeasureSpec.AT_MOST);
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            //listItem.measure(0, 0);
            listItem.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
            totalHeight += listItem.getMeasuredHeight() * 1.25;
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();

        params.height = totalHeight;
        listView.setLayoutParams(params);

        listView.requestLayout();
    }

}