package com.taewon.dolphin;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class LoginActivity extends AppCompatActivity{

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
        final CheckBox autoLogin =(CheckBox)findViewById(R.id.autoLogin);


        //읽어오는 부분
        SharedPreferences auto = getSharedPreferences("auto", LoginActivity.MODE_PRIVATE);
        HashMap<String, String> instanceHash = (HashMap<String, String>)auto.getAll();
        if(instanceHash.size()>0)
        {
            //auto의 키값만 가져오려고 만들었어요~
            Iterator<String> it = instanceHash.keySet().iterator();

            HashMap<String, String> userMap = new HashMap<String, String>();
            while(it.hasNext())
            {
                String key = it.next();
                userMap.put(key, auto.getString(key, null));
            }
            if(!userMap.containsValue(null))
            {
                //UserData.class 에 데이터를 저장.
                UserData.getInstance().setUserStudentCode(userMap.get("userStudentCode"));
                UserData.getInstance().setUserID(userMap.get("userID"));
                UserData.getInstance().setUserName(userMap.get("userName"));
                UserData.getInstance().setUserMajor(userMap.get("userMajor"));
                UserData.getInstance().setUserDept(userMap.get("userDept"));
                UserData.getInstance().setUserPhoneNum(userMap.get("userPhoneNum"));
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        }


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
                        try
                        {
                            JSONObject jsonObject = new JSONObject(response);
                            boolean success = jsonObject.getBoolean("success");
                            if(success)
                            {
                                //로그인에 성공했다면,
                                //UserData.class 에 데이터를 저장합니다.
                                //UserData.Instance().get ~~ 혹은, UserData.Instance().set~~ 으로 사용하면됩니다.
                                Toast.makeText(LoginActivity.this, "", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(LoginActivity.this, MainActivity.class);

                                //UserData.class 에 데이터를 저장.
                                UserData.getInstance().setUserStudentCode(jsonObject.getString("userStudentCode"));
                                UserData.getInstance().setUserID(jsonObject.getString("userID"));
                                UserData.getInstance().setUserName(jsonObject.getString("userName"));
                                UserData.getInstance().setUserMajor(jsonObject.getString("userMajor"));
                                UserData.getInstance().setUserDept(jsonObject.getString("userDept"));
                                UserData.getInstance().setUserPhoneNum(jsonObject.getString("userPhoneNum"));

                                /* 만약 로그인에 성공했으면 메인 엑티비티로 넘어감 근데 자동로그인을 곁들인*/
                                if(autoLogin.isChecked()){
                                    SharedPreferences auto = getSharedPreferences("auto",LoginActivity.MODE_PRIVATE);
                                    SharedPreferences.Editor autoLogin = auto.edit();
                                    autoLogin.putString("userStudentCode", UserData.getInstance().getUserStudentCode());
                                    autoLogin.putString("userID", UserData.getInstance().getUserID());
                                    autoLogin.putString("userPW", userPassword.getText().toString());
                                    autoLogin.putString("userName", UserData.getInstance().getUserName());
                                    autoLogin.putString("userMajor", UserData.getInstance().getUserMajor());
                                    autoLogin.putString("userDept", UserData.getInstance().getUserDept());
                                    autoLogin.putString("userPHoneNum", UserData.getInstance().getUserPhoneNum());
                                    autoLogin.commit();
                                }
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
                            AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                            builder.setIcon(R.drawable.ic_baseline_arrow_back_24)
                                    .setTitle("서버가 불안정합니다.")
                                    .setMessage("\t잠시 후에 다시 시도해보세요.")
                                    .setNegativeButton("확인", null);
                            AlertDialog dialog = builder.create();
                            dialog.show();
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
