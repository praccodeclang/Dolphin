package com.taewon.dolphin;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        /* Views */
        Intent resisterIntent = getIntent();
        final EditText userID = (EditText) findViewById(R.id.userID);
        final EditText userPassword = (EditText) findViewById(R.id.userPassword);
        final Button loginBtn = (Button)findViewById(R.id.loginBtn);
        final TextView register = (TextView)findViewById(R.id.register);
        final TextView findIDPW = (TextView)findViewById(R.id.find);

        try{
            //만약 유저가 회원가입을 했다면, 자동으로 설정합니다.
            userID.setText(resisterIntent.getStringExtra("userID"));
            userPassword.setText(resisterIntent.getStringExtra("userPassword"));
        }
        catch (Exception e)
        {
            //null 값이 넘어올 수도 있습니다!
            userID.setText(resisterIntent.getStringExtra(""));
            userPassword.setText(resisterIntent.getStringExtra(""));
        }


        /* 회원가입 클릭 시, 바로 회원가입 창으로 넘어갑니다.*/
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });

        //로그인 버튼 클릭 시, 서버에 데이터를 요청하고, 만약 성공적으로 이루어졌다면 받아온 데이터를 UserData.class에저장합니다.
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //서버에서 응답을 받으면 실행.
                        try
                        {
                            JSONObject jsonObject = new JSONObject(response);
                            boolean success = jsonObject.getBoolean("success");
                            if(success)
                            {
                                //로그인에 성공했다면,
                                //UserData.class 에 데이터를 저장합니다.
                                //UserData.Instance().get ~~ 혹은, UserData.Instance().set~~ 으로 사용하면됩니다.
                                Toast.makeText(LoginActivity.this, "로그인 성공", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(LoginActivity.this, MainActivity.class);

                                //1. UserData.class 에 데이터를 저장.
                                UserData.getInstance().setUserID(jsonObject.getString("userID"));
                                UserData.getInstance().setUserName(jsonObject.getString("userName"));
                                UserData.getInstance().setUserMajor(jsonObject.getString("userMajor"));
                                UserData.getInstance().setUserDept(jsonObject.getString("userDept"));
                                UserData.getInstance().setUserPhoneNum(jsonObject.getString("userPhoneNum"));

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
                RequestUserLogin validateRequest = new RequestUserLogin(userID.getText().toString(), userPassword.getText().toString(), responseListener);
                RequestQueue queue = Volley.newRequestQueue(LoginActivity.this);
                queue.add(validateRequest);


            }
        });

    }


}
