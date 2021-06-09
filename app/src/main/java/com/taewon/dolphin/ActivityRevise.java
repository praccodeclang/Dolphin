package com.taewon.dolphin;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

public class ActivityRevise extends AppCompatActivity {

    private EditText passwordTextChk;
    private TextView warningText;
    private EditText newPassword;
    private Button password_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_revise);

        newPassword = (EditText) findViewById(R.id.newPassword);
        password_button = (Button) findViewById(R.id.password_button);
        warningText = (TextView) findViewById(R.id.warningText);
        passwordTextChk = (EditText) findViewById(R.id.passwordTextChk);

        passwordTextChk.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(newPassword.getText().toString().equals(passwordTextChk.getText().toString()))
                {
                    warningText.setVisibility(View.GONE);
                    password_button.setVisibility(View.VISIBLE);
                }
                else
                {
                    warningText.setVisibility(View.VISIBLE);
                    password_button.setVisibility(View.GONE);
                }

            }
            @Override
            public void afterTextChanged(Editable s) { }
        });

        password_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            boolean success = jsonObject.getBoolean("success");
                            if (success) {
                                SharedPreferences auto = getSharedPreferences("auto", ActivityLogin.MODE_PRIVATE);
                                SharedPreferences.Editor edit =  auto.edit();
                                edit.putString("userPW",jsonObject.getString("userPassword"));
                                Toast.makeText(ActivityRevise.this, "비밀번호가 변경되었습니다.", Toast.LENGTH_LONG).show();
                                finish();
                            } else {
                                Toast.makeText(ActivityRevise.this, "비밀번호 변경을 실패했습니다.", Toast.LENGTH_LONG).show();
                            }
                        }
                        catch (Exception e)
                        {
                            AlertDialog.Builder builder = new AlertDialog.Builder(ActivityRevise.this);
                            builder.setIcon(R.drawable.ic_baseline_block_24)
                                    .setTitle("서버가 불안정한것 같아요.")
                                    .setMessage("조금만 있다가 다시시도해보세요.")
                                    .setPositiveButton("확인", null)
                                    .show();
                        }
                    }
                };
                String userStudentCode = UserData.getInstance().getUserStudentCode();
                String userID = UserData.getInstance().getUserID();
                String url = "http://xodnjs2546.cafe24.com/updateUserPassword.php";
                RequestUpdatePassword request = new RequestUpdatePassword(userStudentCode, userID, newPassword.getText().toString(), url,responseListener);
                RequestQueue queue = Volley.newRequestQueue(ActivityRevise.this);
                queue.add(request);
            }
        });
    }
}