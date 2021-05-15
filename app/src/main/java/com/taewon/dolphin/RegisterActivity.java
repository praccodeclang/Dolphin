package com.taewon.dolphin;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;

public class RegisterActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        final Button validateBtn = (Button)findViewById(R.id.validate);
        final Button registerBtnBtn = (Button)findViewById(R.id.registerBtn);

        final EditText nameText = (EditText)findViewById(R.id.nameText);
        final EditText idText = (EditText)findViewById(R.id.idText);
        final EditText passwordText = (EditText)findViewById(R.id.passwordText);
        final Spinner userMajor = (Spinner)findViewById(R.id.userMajor);
        final Spinner userDept = (Spinner)findViewById(R.id.userDept);
        final EditText PHONE = (EditText)findViewById(R.id.PHONE);
        AlertDialog alertDialog;

        final Boolean[] isValidate = {false};
        //1. 중복확인 버튼
        validateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //클릭되면 실행.
                String userID = idText.getText().toString();
                System.out.println(userID);
                Response.Listener<String> responseListener = new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) { // 서버 응답시 실행
                        try
                        {
                            JSONObject jsonObject = new JSONObject(response);
                            boolean isExistUser = jsonObject.getBoolean("success");
                            if(!isExistUser)
                            {
                                AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
                                builder.setTitle("중복된 아이디").setMessage("\t이미 존재하는 아이디입니다.\n\t다른 아이디를 입력해주세요.").setNegativeButton("확인", null);
                                AlertDialog dialog = builder.create();
                                dialog.show();
                            }
                            else
                            {
                                AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
                                builder.setMessage("이 아이디로 하시겠습니까?").setNegativeButton("취소", null).setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        idText.setBackgroundColor(getResources().getColor(R.color.LockColor));
                                        idText.setEnabled(false);
                                        validateBtn.setBackgroundColor(getResources().getColor(R.color.LockColor));
                                        validateBtn.setEnabled(false);
                                        isValidate[0] = true;
                                        dialog.dismiss();
                                    }
                                });
                                AlertDialog dialog = builder.create();
                                dialog.show();
                            }

                        }
                        catch (Exception e)
                        {
                            System.out.println("진짜 큰일났다.");
                            e.printStackTrace();
                        }
                    }
                };

                UserValidate validateRequest = new UserValidate(userID, responseListener);
                RequestQueue queue = Volley.newRequestQueue(RegisterActivity.this);
                queue.add(validateRequest);
            }
        });

        registerBtnBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String userID = idText.getText().toString();
                String userPassword = passwordText.getText().toString();
                String userName = idText.getText().toString();
                String Major = userMajor.getSelectedItem().toString();
                String department = userDept.getSelectedItem().toString();
                String userPhone = PHONE.getText().toString();

                if(userID.isEmpty() || userPassword.isEmpty() || userName.isEmpty() || Major.isEmpty() || department.isEmpty() || userPhone.isEmpty())
                {
                    AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
                    builder.setTitle("빈 공간이 있습니다.").setMessage("\t우리는 철벽수비를 자랑한다구요!").setNegativeButton("확인",null);
                    AlertDialog dialog = builder.create();
                    dialog.show();
                    return;
                }

                Response.Listener<String> responseListener = new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) { // 서버 응답시 실행
                        try
                        {
                            JSONObject jsonObject = new JSONObject(response);
                            boolean success = jsonObject.getBoolean("success");
                            if(success)
                            {
                                AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
                                builder.setTitle("떠날 준비완료.").setMessage("\t친구들을 만나러 가볼까요?").setNegativeButton("확인", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                                        intent.putExtra("userID", userID);
                                        intent.putExtra("userPassword", userPassword);
                                        startActivity(intent);
                                        dialog.dismiss();
                                        finish();
                                    }
                                });
                                AlertDialog dialog = builder.create();
                                dialog.show();
                            }
                            else
                            {
                                AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
                                builder.setMessage("나도 몰라요.").setMessage("\t왜 안될까요..?\n\t개발자에게 알려주세요\n\t010-4640-7993").setNegativeButton("확인", null);
                                AlertDialog dialog = builder.create();
                                dialog.show();
                            }

                        }
                        catch (Exception e)
                        {
                            AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
                            builder.setMessage("큰일났어요!").setMessage("\t서버가 불안정한것 같아요.\n\t조금만 기다렸다가 다시 시도해주세요.").setNegativeButton("확인", null);
                            AlertDialog dialog = builder.create();
                            dialog.show();
                            e.printStackTrace();
                        }
                    }
                };

                UserRegister validateRequest = new UserRegister(userID, userPassword, userName, Major, department, userPhone, responseListener);
                RequestQueue queue = Volley.newRequestQueue(RegisterActivity.this);
                queue.add(validateRequest);
            }
        });
    }
}
