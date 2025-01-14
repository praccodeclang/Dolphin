package com.taewon.dolphin;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;


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
    private ImageView dolphinNoticeBtn;
    private ScrollView mainScrollView;
    private ImageView contactBtn;



    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();
        initListeners();
        initBehavior();
        getFreeBoardsFromServer();
        getNoticesFromHomepage();
        chkNoticeUpdate();
        updateDeviceInfo();
    }//onCreate End

    @Override
    protected void onResume()
    {
        super.onResume();
        /*사용자가 다시 돌아오면 실행합니다.*/
        //센서 AWAKE
        mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);

        getFreeBoardsFromServer();
        getNoticesFromHomepage();

    }

    @Override
    protected void onPause()
    {
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
        dolphinNoticeBtn = (ImageView)findViewById(R.id.dolphinNoticeBtn);
    }

    private void initListeners()
    {
        //아이콘 버튼 리스너
        View.OnClickListener iconBtnListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent;
                switch (v.getId())
                {
                    case R.id.iconBtn1:
                        intent = new Intent(getApplicationContext(), ActivitySchoolNotice.class);
                        break;
                    case R.id.iconBtn2:
                        intent = new Intent(getApplicationContext(), ActivityDiet.class);
                        break;
                    case R.id.iconBtn3:
                        intent = new Intent(getApplicationContext(), ActivityWeather.class);
                        break;
                    case R.id.iconBtn4:
                        intent = new Intent(getApplicationContext(), ActivityCalendar.class);
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

                //누른 게시판의 인스턴스를 생성해 FreeBoardViewerActivity.class에 인텐트로 넘겨줍니다.
                FreeBoardItem instance = (FreeBoardItem)parent.getAdapter().getItem(position);
                intent.putExtra("Name", instance.getUserName());
                try {
                    intent.putExtra("Date", ActivityMain.calDate_ShouldReturnString(instance.getDate()));
                } catch (ParseException e) {
                    intent.putExtra("Date", instance.getDate());
                }
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
                Intent intent = new Intent(ActivityMain.this, ActivityContact.class);
                startActivity(intent);
            }
        });
        dolphinNoticeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showNoticeDialog();
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

        //2. 로그인 창에서 넘어오면, 프로필의 이름과 학과를 UserData 클래스에 저장된 이름과 학과로 초기화합니다.
        profileUserName.setText(UserData.getInstance().getUserName());
        profileUserDept.setText(UserData.getInstance().getUserDept());

        //3. 공지사항 텍스트를 유저의 학부에 맞게 변경합니다.
        noticeDeptText.setText("  "+UserData.getInstance().getUserMajor() + " 공지사항");
    }


    //커스텀 함수
    //서브쓰레드에서 비동기방식으로 공지사항 로드(3개만 가져옴.)를 수행합니다.
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

                    int itemLength = (jsonArray.length() > 0) ? jsonArray.length() : 0;
                    itemLength = itemLength < 3 ? itemLength : 3;
                    if(itemLength == 0)
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
                        lp.setMargins(15, 50, 15, 20);
                        imageView.setLayoutParams(lp);
                        textView.setLayoutParams(lp);
                        container.addView(imageView);
                        container.addView(textView);
                        return;
                    }
                    else
                    {
                        for(int i=0; i<itemLength; i++)
                        {
                            String title, contents, date, userName, userID, PHONE, commentCount;
                            JSONObject object = jsonArray.getJSONObject(i);
                            title = object.get("title").toString();
                            contents = object.get("contents").toString();
                            date = object.get("DATE").toString();
                            userName = object.get("userName").toString();
                            userID = object.get("userID").toString();
                            PHONE = object.get("PHONE").toString();
                            boardID = object.getInt("no");
                            commentCount = object.get("commentCount").toString();
                            freeBoardItemList.add(new FreeBoardItem(boardID, title, contents, userName, userID, PHONE, date, commentCount));
                        }
                        FreeBoardAdapter freeBoardAdapter = new FreeBoardAdapter(ActivityMain.this, freeBoardItemList);
                        mainFreeBoardListView.setAdapter(freeBoardAdapter);
                        mainScrollView.scrollTo(0,0);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        RequestGetFreeBoard freeBoardRequest = new RequestGetFreeBoard(responseListener);
        RequestQueue queue = Volley.newRequestQueue(ActivityMain.this);
        queue.add(freeBoardRequest);
    }


    private void chkNoticeUpdate(){
        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String isChecked = jsonObject.get("chk").toString();
                    if(isChecked.equals("false")){ showNoticeDialog(); }
                    else{ return; }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
        RequestGetDolphinNoticeChk requestDolphinNoticeChk = new RequestGetDolphinNoticeChk(UserData.getInstance().getUserID(), responseListener);
        RequestQueue queue = Volley.newRequestQueue(ActivityMain.this);
        queue.add(requestDolphinNoticeChk);
    }

    private void showNoticeDialog(){
        Dialog dialog;
        dialog = new Dialog(ActivityMain.this);
        dialog.setContentView(R.layout.dolphin_notice_custom_dialog);
        TextView tt = (TextView)dialog.findViewById(R.id.dialog_title);
        TextView tc = (TextView)dialog.findViewById(R.id.dialog_contents);
        CheckBox chkBox = (CheckBox)dialog.findViewById(R.id.dialog_showAgain);

        dialog.findViewById(R.id.dialog_okBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(chkBox.isChecked())
                {
                    updateChk();
                }
                dialog.dismiss();
            }
        });

        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if(jsonObject.getBoolean("success"))
                    {
                        tt.setText(jsonObject.get("title").toString());
                        tc.setText(jsonObject.get("contents").toString());
                        dialog.show();
                    }
                    else
                    {
                        return;
                    }
                }
                catch (Exception e) {
                    return;
                }
            }
        };
        RequestGetDolphinNotice requestDolphinNotice = new RequestGetDolphinNotice(responseListener);
        RequestQueue queue = Volley.newRequestQueue(ActivityMain.this);
        queue.add(requestDolphinNotice);
    }

    private void updateChk(){
        RequestUpdateChk requestDolphinNoticeChk = new RequestUpdateChk(UserData.getInstance().getUserID());
        RequestQueue queue = Volley.newRequestQueue(ActivityMain.this);
        queue.add(requestDolphinNoticeChk);
    }

    private boolean chkDeviceInfo()
    {
        return false;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void updateDeviceInfo(){
        TelephonyManager tm = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
        RequestUpdateDeviceInfo requestDolphinNoticeChk =
                new RequestUpdateDeviceInfo(UserData.getInstance().getUserID(), tm.getImei(), tm.getDeviceId());
        RequestQueue queue = Volley.newRequestQueue(ActivityMain.this);
        queue.add(requestDolphinNoticeChk);
    }

    public static String calDate_ShouldReturnString(String dateString) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.KOREA);
        TimeZone tz = TimeZone.getTimeZone("Asia/Seoul");
        sdf.setTimeZone(tz);
        Date writtenDate = sdf.parse(dateString);
        Date currDate = sdf.parse(sdf.format(new Date()));

        long lWrittenDate = writtenDate.getTime();
        long lCurrDate = currDate.getTime();
        long sec = (lCurrDate - lWrittenDate) / 1000;

        int day = (int)(sec/86400) % 86400;
        int hour = (int)((sec/3600) % 3600) % 24;
        int min = (int)(sec/60) % 60;

        if(day > 0) { return day+"일 전 "; }
        else if(hour > 0){ return hour+"시간 전 "; }
        else if(min > 3){ return min+"분 전 "; }
        else{ return "방금 전 "; }
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
                Intent intent = new Intent(ActivityMain.this, ActivityNfc.class);
                startActivity(intent);
            }

        }

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}