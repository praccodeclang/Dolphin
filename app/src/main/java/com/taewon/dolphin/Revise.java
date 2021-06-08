package com.taewon.dolphin;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

public class Revise extends AppCompatActivity {


    private EditText newPassword;
    private Button password_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_revise);

        newPassword = (EditText) findViewById(R.id.newPassword);
        password_button = (Button) findViewById(R.id.password_button);

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
                                Toast.makeText(Revise.this, "비밀번호가 변경되었습니다.", Toast.LENGTH_LONG).show();
                            } else {
                                Toast.makeText(Revise.this, "비밀번호 변경을 실패 했습니다.", Toast.LENGTH_LONG).show();
                            }
                        }
                        catch (Exception e)
                        {

                        }
                    };
                };
                String userID = UserData.getInstance().getUserID();
                String userName = UserData.getInstance().getUserName();
                String url = "주소적어주세요.";
                RequestUpdateInfo request = new RequestUpdateInfo(userID, userName, newPassword.getText().toString(), url,responseListener);
                RequestQueue queue = Volley.newRequestQueue(Revise.this);
                queue.add(request);
            }
        });
    }
}