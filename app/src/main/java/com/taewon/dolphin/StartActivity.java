package com.taewon.dolphin;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;


public class StartActivity extends AppCompatActivity {

    ImageView imageView;
    Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        //임시로 그냥 얻어오기만 했어요.
        imageView = (ImageView)findViewById(R.id.logo);

        //Handler를 이용해 5초 딜레이를 주고, 액티비티를 이동합니다.
        handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                //딜레이 후 실행할 부분.
                Intent intent = new Intent(StartActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        }, 2000);
    }
}
