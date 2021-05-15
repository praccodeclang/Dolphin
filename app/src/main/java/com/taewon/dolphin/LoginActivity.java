package com.taewon.dolphin;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class LoginActivity extends AppCompatActivity {
    EditText userID, userPassword;
    Button loginBtn;
    TextView register, find;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Intent resisterIntent = getIntent();
        userID = (EditText) findViewById(R.id.userID);
        userPassword = (EditText) findViewById(R.id.userPassword);
        loginBtn = (Button)findViewById(R.id.loginBtn);
        register = (TextView)findViewById(R.id.register);
        find = (TextView)findViewById(R.id.find);

        userID.setText(resisterIntent.getStringExtra("userID"));
        userPassword.setText(resisterIntent.getStringExtra("userPassword"));

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });

    }


}
