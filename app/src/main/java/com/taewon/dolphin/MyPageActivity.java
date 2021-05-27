package com.taewon.dolphin;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;


public class MyPageActivity extends AppCompatActivity {

    private CardView card_Logout;
    private CardView card_Secession;
    private CardView card_Notice;
    private CardView card_Freeboard;
    private TextView myPageUserDept;
    private TextView myPageUserName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mypage);
        card_Logout = (CardView)findViewById(R.id.card_Logout);
        card_Secession = (CardView)findViewById(R.id.card_Secession);
        card_Notice = (CardView)findViewById(R.id.card_notice);
        myPageUserDept =(TextView)findViewById(R.id.myPageUserDept);
        myPageUserName =(TextView)findViewById(R.id.myPageUserName);
        card_Freeboard =(CardView)findViewById(R.id.card_Freeboard);
        myPageUserName.setText(UserData.getInstance().getUserName());
        myPageUserDept.setText(UserData.getInstance().getUserDept());
        card_Logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences pref =getSharedPreferences("auto", MyPageActivity.MODE_PRIVATE);
                SharedPreferences.Editor editor =pref.edit();
                editor.clear();
                editor.commit();
                Intent intent = new Intent(MyPageActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });

        card_Notice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.uc.ac.kr/www/index.php?pCode=ucnotice"));
                startActivity(intent);
            }
        });
        card_Freeboard.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MyPageActivity.this, FreeBoardActivity.class);
                startActivity(intent);
                finish();
            }
        }));
    }
}
