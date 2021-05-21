package com.taewon.dolphin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        

        final Button btnFreeBoard = (Button) findViewById(R.id.btnFreeBoard);
        final TextView viewUserName = (TextView)findViewById(R.id.viewUserName);
        final TextView viewUserMajor = (TextView)findViewById(R.id.viewUserMajor);
        final TextView viewUserDept = (TextView)findViewById(R.id.viewUserDept);

        viewUserName.setText(UserData.getInstance().getUserName());
        viewUserMajor.setText(UserData.getInstance().getUserMajor());
        viewUserDept.setText(UserData.getInstance().getUserDept());

        btnFreeBoard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, FreeBoardActivity.class);
                startActivity(intent);
            }
        });
    }
}