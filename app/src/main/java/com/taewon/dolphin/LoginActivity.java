package com.taewon.dolphin;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {

    EditText userID, userPassword;
    Button loginBtn;
    TextView register, find;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        userID = (EditText) findViewById(R.id.userID);
        userPassword = (EditText) findViewById(R.id.userPassword);
        loginBtn = (Button)findViewById(R.id.loginBtn);
        register = (TextView)findViewById(R.id.register);
        find = (TextView)findViewById(R.id.find);

    }
}
