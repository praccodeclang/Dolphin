package com.taewon.dolphin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements SensorEventListener{

    private long mShakeTime;
    private static final int SHAKE_SKIP_TIME = 500;
    private static final float SHAKE_THRESHOLD_GRAVITY = 1.5F;
    private SensorManager mSensorManager;
    private Sensor mAccelerometer;

    private TextView moreViewFreeBoard;
    private TextView moreViewNotice;
    private TextView profileUserName;
    private TextView profileUserDept;
    private TextView noticeDeptText;
    private ListView mainNoticeListView;
    private ListView mainFreeBoardListView;
    private ImageView myPageBtn;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        moreViewFreeBoard = (TextView) findViewById(R.id.moreViewFreeBoard);
        moreViewNotice = (TextView) findViewById(R.id.moreViewNotice);
        profileUserName = (TextView)findViewById(R.id.profileUserName);
        profileUserDept = (TextView)findViewById(R.id.profileUserDept);
        noticeDeptText = (TextView)findViewById(R.id.noticeDeptText);
        mainNoticeListView = (ListView)findViewById(R.id.mainNoticeListView);
        mainFreeBoardListView = (ListView)findViewById(R.id.mainFreeBoardListView);
        myPageBtn = (ImageView)findViewById(R.id.myPageBtn);






        //로그인 창에서 넘어오면, 프로필의 이름과 학과를 UserData 클래스에 저장된 이름과 학과로 초기화합니다.
        profileUserName.setText(UserData.getInstance().getUserName());
        profileUserDept.setText(UserData.getInstance().getUserDept());
        //해당 공지사항이 어떤 학부 공지사항인지알려줍니다.
        noticeDeptText.setText("  "+UserData.getInstance().getUserMajor() + " 공지사항");

        //익명함수 리스너들
        moreViewNotice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(UserData.getInstance().getUserMajorNoticeUrl()));
                startActivity(browserIntent);
            }
        });
        mainNoticeListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                NoticeItem instance = (NoticeItem)parent.getAdapter().getItem(position);
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(instance.getUrl()));
                startActivity(browserIntent);
            }
        });

        mainFreeBoardListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(MainActivity.this, FreeBoardViewerActivity.class);

                //누른 게시판의 인스턴스를 생성해 FreeBoardViewerActivity.class 인텐트로 넘겨준다.
                FreeBoardItem instance = (FreeBoardItem)parent.getAdapter().getItem(position);
                intent.putExtra("Name", instance.getUserName());
                intent.putExtra("Date", instance.getDate());
                intent.putExtra("Title", instance.getTitle());
                intent.putExtra("Contents", instance.getContents());
                intent.putExtra("userPhone", instance.getUserPhone());
                intent.putExtra("BoardID", Integer.toString(instance.getBoardId()));
                startActivity(intent);
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
                Intent intent = new Intent(MainActivity.this, MyPageActivity.class);
                startActivity(intent);
            }
        });

    }//onCreate End


    @Override
    protected void onResume() {
        super.onResume();
        //센서 AWAKE
        mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
        //mainNoticeListView에 아이템 추가(3개만 넣어볼게요.)
        JsoupAsyncTask jsoupAsyncTask = new JsoupAsyncTask(this, UserData.getInstance().getUserMajorNoticeUrl(), mainNoticeListView, 3);
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

                    if(jsonArray.length() < 3)
                    {
                        for(int i=0; i<jsonArray.length(); i++)
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
                    }
                    else {
                        for (int i = 0; i < 3; i++) {
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
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
        RequestGetFreeBoard freeBoardRequest = new RequestGetFreeBoard(responseListener);
        RequestQueue queue = Volley.newRequestQueue(MainActivity.this);
        queue.add(freeBoardRequest);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mSensorManager.unregisterListener(this);
    }

    //Custom Methods
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


    @Override
    public void onSensorChanged(SensorEvent event) {
        if(event.sensor.getType() == Sensor.TYPE_ACCELEROMETER)
        {
            float axisX = event.values[0];
            float axisY = event.values[1];
            float axisZ = event.values[2];

            float gravityX = axisX / SensorManager.GRAVITY_EARTH;
            float gravityY = axisY / SensorManager.GRAVITY_EARTH;
            float gravityZ = axisZ / SensorManager.GRAVITY_EARTH;

            Float f = gravityX * gravityX + gravityY * gravityY + gravityZ * gravityZ;
            double square = Math.sqrt(f.doubleValue());
            float gForce = (float) square;
            if(gForce > SHAKE_THRESHOLD_GRAVITY)
            {
                long currentTime = System.currentTimeMillis();
                if(mShakeTime + SHAKE_SKIP_TIME > currentTime)
                {
                    return;
                }
                mShakeTime = currentTime;
                Toast.makeText(MainActivity.this,"흔들림 발생",Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(MainActivity.this, NfcActivity.class);
                startActivity(intent);
            }

        }

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}