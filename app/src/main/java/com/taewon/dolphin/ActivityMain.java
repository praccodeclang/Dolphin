package com.taewon.dolphin;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
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

import java.util.ArrayList;
import java.util.List;


public class ActivityMain extends AppCompatActivity implements SensorEventListener{

    /* Sensors */
    private long mShakeTime;
    private static final int SHAKE_SKIP_TIME = 500;
    private static final float SHAKE_THRESHOLD_GRAVITY = 1.5F;
    private SensorManager mSensorManager;
    private Sensor mAccelerometer;

    /* Views */
    private LinearLayout iconBtn1;
    private LinearLayout iconBtn2;
    private LinearLayout iconBtn3;
    private LinearLayout iconBtn4;
    private TextView moreViewFreeBoard;
    private TextView moreViewNotice;
    private TextView profileUserName;
    private TextView profileUserDept;
    private TextView noticeDeptText;
    private ListView mainNoticeListView;
    private ListView mainFreeBoardListView;
    private ImageView deptProfile;
    private ImageView myPageBtn;
    private ScrollView mainScrollView;
    private ImageView contactBtn;



    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();
        initBehavior();
        getFreeBoardsFromServer();
        getNoticesFromHomepage();
        initListeners();
    }//onCreate End

    @Override
    protected void onResume() {
        super.onResume();
        /*사용자가 다시 돌아오면 실행합니다.*/
        //센서 AWAKE
        mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);

        getFreeBoardsFromServer();
        getNoticesFromHomepage();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mSensorManager.unregisterListener(this);
    }


    /* Custom Method */
    private void initViews()
    {
        /* Sensor */
        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        /* Views */
        moreViewFreeBoard = (TextView) findViewById(R.id.moreViewFreeBoard);
        moreViewNotice = (TextView) findViewById(R.id.moreViewNotice);
        profileUserName = (TextView)findViewById(R.id.profileUserName);
        profileUserDept = (TextView)findViewById(R.id.profileUserDept);
        noticeDeptText = (TextView)findViewById(R.id.noticeDeptText);
        mainNoticeListView = (ListView)findViewById(R.id.mainNoticeListView);
        mainFreeBoardListView = (ListView)findViewById(R.id.mainFreeBoardListView);
        myPageBtn = (ImageView)findViewById(R.id.myPageBtn);
        deptProfile = (ImageView)findViewById(R.id.deptProfile);
        mainScrollView = (ScrollView)findViewById(R.id.mainScrollView);
        contactBtn = (ImageView)findViewById(R.id.contactBtn);
        iconBtn1 = (LinearLayout)findViewById(R.id.iconBtn1);
        iconBtn2 = (LinearLayout)findViewById(R.id.iconBtn2);
        iconBtn3 = (LinearLayout)findViewById(R.id.iconBtn3);
        iconBtn4 = (LinearLayout)findViewById(R.id.iconBtn4);

        //아이콘 버튼 리스너
        View.OnClickListener iconBtnListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent;
                switch (v.getId())
                {
                    case R.id.iconBtn1:
                        intent = new Intent(getApplicationContext(),SchoolNotice.class);
                        break;
                    case R.id.iconBtn2:
                        intent = new Intent(getApplicationContext(),Diet.class);
                        break;
                    case R.id.iconBtn3:
                        intent = new Intent(getApplicationContext(),Weather.class);
                        break;
                    case R.id.iconBtn4:
                        intent = new Intent(getApplicationContext(),Undefined.class);
                        break;
                    default:
                        throw new IllegalStateException("Unexpected value: " + v.getId());
                }
                startActivity(intent);
            }
        };
        iconBtn1.setOnClickListener(iconBtnListener);
        iconBtn2.setOnClickListener(iconBtnListener);
        iconBtn3.setOnClickListener(iconBtnListener);
        iconBtn4.setOnClickListener(iconBtnListener);

    }

    private void initListeners()
    {
        mainNoticeListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                NoticeItem instance = (NoticeItem)parent.getAdapter().getItem(position);
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(instance.getUrl()));
                startActivity(browserIntent);
            }
        });

        moreViewNotice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(UserData.getInstance().getUserMajorNoticeUrl()));
                startActivity(browserIntent);
            }
        });

        mainFreeBoardListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(ActivityMain.this, ActivityFreeBoardViewer.class);

                //누른 게시판의 인스턴스를 생성해 FreeBoardViewerActivity.class에 인텐트로 넘겨준다.
                FreeBoardItem instance = (FreeBoardItem)parent.getAdapter().getItem(position);
                intent.putExtra("Name", instance.getUserName());
                intent.putExtra("Date", instance.getDate());
                intent.putExtra("Title", instance.getTitle());
                intent.putExtra("Contents", instance.getContents());
                intent.putExtra("userID", instance.getUserID());
                intent.putExtra("userPhone", instance.getUserPhone());
                intent.putExtra("BoardID", Integer.toString(instance.getBoardId()));
                startActivity(intent);
            }
        });

        moreViewFreeBoard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ActivityMain.this, ActivityFreeBoard.class);
                startActivity(intent);
            }
        });

        //마이페이지 및 설정 액티비티로 넘어갑니다.
        myPageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ActivityMain.this, ActivityMyPage.class);
                startActivity(intent);
            }
        });

        // 연락처 버튼 클릭 시, 학부생들의 연락처 리스트로 이동합니다.
        contactBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ActivityMain.this, ActivityFreeBoard.class);
                startActivity(intent);
            }
        });
    }



    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void initBehavior()
    {
        // 1. 유저의 학과에 따라 프로필 사진을 다르게 설정합니다.
        deptProfile.setImageResource(UserData.getInstance().getUserProfile());
        deptProfile.setBackgroundResource(R.drawable.border_layout_profile);
        deptProfile.setClipToOutline(true);
        mainScrollView.fullScroll(ScrollView.FOCUS_UP);

        //2. 로그인 창에서 넘어오면, 프로필의 이름과 학과를 UserData 클래스에 저장된 이름과 학과로 초기화합니다.
        profileUserName.setText(UserData.getInstance().getUserName());
        profileUserDept.setText(UserData.getInstance().getUserDept());

        //3. 공지사항 텍스트를 유저의 학부에 맞게 변경합니다.
        noticeDeptText.setText("  "+UserData.getInstance().getUserMajor() + " 공지사항");
    }


    //서브쓰레드에서 공지사항 로드(3개만 가져옴.)를 수행합니다.
    private void getNoticesFromHomepage()
    {
        JsoupAsyncTask jsoupAsyncTask = new JsoupAsyncTask(this, UserData.getInstance().getUserMajorNoticeUrl(), mainNoticeListView, 3);
        jsoupAsyncTask.execute();
    }

    //서버에서 게시판 정보를 가져와 로드합니다.
    private void getFreeBoardsFromServer()
    {
        //mainFreeBoardListView 아이템 추가(3개만 넣어볼게요.)
        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("DATA");
                    List<FreeBoardItem> freeBoardItemList = new ArrayList<>();
                    LinearLayout container = (LinearLayout)findViewById(R.id.mainFreeBoardContainer);
                    container.setVisibility(View.GONE);
                    mainFreeBoardListView.setVisibility(View.VISIBLE);
                    int boardID;

                    if(jsonArray.length() < 1)
                    {
                        container.setVisibility(View.VISIBLE);
                        mainFreeBoardListView.setVisibility(View.GONE);
                        container.removeAllViews();

                        TextView textView = new TextView(ActivityMain.this);
                        ImageView imageView = new ImageView(ActivityMain.this);

                        textView.setText("게시판에 아무 글도 없네요! \n얼른 첫번째 게시글을 작성해보세요.");
                        textView.setGravity(Gravity.CENTER_HORIZONTAL);
                        imageView.setImageResource(R.drawable.dolphins_empty_board);
                        imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);

                        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 300);
                        lp.gravity = Gravity.CENTER;
                        lp.setMargins(15, 20, 15, 20);
                        imageView.setLayoutParams(lp);
                        textView.setLayoutParams(lp);
                        container.addView(imageView);
                        container.addView(textView);
                        return;
                    }
                    else if(jsonArray.length()>0 && jsonArray.length() < 3)
                    {
                        for(int i=0; i<jsonArray.length(); i++)
                        {
                            String title, contents, date, userName, userID, PHONE;
                            JSONObject object = jsonArray.getJSONObject(i);
                            title = object.get("title").toString();
                            contents = object.get("contents").toString();
                            date = object.get("DATE").toString();
                            userName = object.get("userName").toString();
                            userID = object.get("userID").toString();
                            PHONE = object.get("PHONE").toString();
                            boardID = object.getInt("no");
                            freeBoardItemList.add(new FreeBoardItem(boardID, title, contents, userName, userID, PHONE, date));
                        }
                        FreeBoardAdapter freeBoardAdapter = new FreeBoardAdapter(ActivityMain.this, freeBoardItemList);
                        mainFreeBoardListView.setAdapter(freeBoardAdapter);
                    }
                    else {
                        for (int i = 0; i < 3; i++) {
                            String title, contents, date, userName, userID, PHONE;
                            JSONObject object = jsonArray.getJSONObject(i);
                            title = object.get("title").toString();
                            contents = object.get("contents").toString();
                            date = object.get("DATE").toString();
                            userName = object.get("userName").toString();
                            userID = object.get("userID").toString();
                            PHONE = object.get("PHONE").toString();
                            boardID = object.getInt("no");
                            freeBoardItemList.add(new FreeBoardItem(boardID, title, contents, userName, userID, PHONE, date));
                        }
                        FreeBoardAdapter freeBoardAdapter = new FreeBoardAdapter(ActivityMain.this, freeBoardItemList);
                        mainFreeBoardListView.setAdapter(freeBoardAdapter);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
        RequestGetFreeBoard freeBoardRequest = new RequestGetFreeBoard(responseListener);
        RequestQueue queue = Volley.newRequestQueue(ActivityMain.this);
        queue.add(freeBoardRequest);
    }


    /* Sensors */
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
                Toast.makeText(ActivityMain.this,"흔들림 발생",Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(ActivityMain.this, ActivityNfc.class);
                startActivity(intent);
            }

        }

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}