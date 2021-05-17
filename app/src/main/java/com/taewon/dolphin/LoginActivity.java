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
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity {
    EditText userID, userPassword;
    Button loginBtn;
    TextView register, find;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        /* Views */
        Intent resisterIntent = getIntent();
        userID = (EditText) findViewById(R.id.userID);
        userPassword = (EditText) findViewById(R.id.userPassword);
        loginBtn = (Button)findViewById(R.id.loginBtn);
        register = (TextView)findViewById(R.id.register);
        find = (TextView)findViewById(R.id.find);

        //만약 유저가 회원가입을 했다면, 자동으로 설정합니다.
        userID.setText(resisterIntent.getStringExtra("userID"));
        userPassword.setText(resisterIntent.getStringExtra("userPassword"));

        /* 회원가입 클릭 시, 바로 회원가입 창으로 넘어갑니다.*/
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try
                        {
                            JSONObject jsonObject = new JSONObject(response);
                            boolean success = jsonObject.getBoolean("success");
                            if(success)
                            {
                                Toast.makeText(LoginActivity.this, "로그인 성공", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                intent.putExtra("userID", jsonObject.getString("userID"));
                                intent.putExtra("userName", jsonObject.getString("userName"));
                                intent.putExtra("userMajor", jsonObject.getString("userMajor"));
                                intent.putExtra("userDept", jsonObject.getString("userDept"));
                                intent.putExtra("userPhoneNum", jsonObject.getString("userPhoneNum"));
                                startActivity(intent);
                                finish();
                            }
                            else
                            {
                                AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                                builder.setIcon(R.drawable.icon_dolphins).setTitle("우리한텐 없습니다.").setMessage("\t아이디나 비밀번호가 틀렸습니다.\n\t다시 시도해보세요.").setNegativeButton("확인", null);
                                AlertDialog dialog = builder.create();
                                dialog.show();
                            }
                        }
                        catch (JSONException e)
                        {
                            e.printStackTrace();
                        }

                    }
                };
                UserLogin validateRequest = new UserLogin(userID.getText().toString(), userPassword.getText().toString(), responseListener);
                RequestQueue queue = Volley.newRequestQueue(LoginActivity.this);
                queue.add(validateRequest);


            }
        });

    }


}
