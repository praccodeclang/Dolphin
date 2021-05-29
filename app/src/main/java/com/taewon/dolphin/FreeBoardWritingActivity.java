package com.taewon.dolphin;

import android.content.DialogInterface;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

public class FreeBoardWritingActivity extends AppCompatActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_freeboard_writing);

        final Button writeOkBtn = (Button)findViewById(R.id.writeOkBtn);
        final EditText writtenTitle =(EditText)findViewById(R.id.writtenTitle);
        final EditText writtenContents =(EditText)findViewById(R.id.writtenContents);
        final ImageButton FreeBoardWriteBackBtn = (ImageButton)findViewById(R.id.FreeBoardWriteBackBtn);

        //버튼 클릭 리스너
        FreeBoardWriteBackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        writeOkBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(writtenTitle.getText().toString().equals("") || writtenContents.getText().toString().equals(""))
                {
                    AlertDialog.Builder builder = new AlertDialog.Builder(FreeBoardWritingActivity.this);
                    builder.setIcon(R.drawable.icon_dolphins).setTitle("뭐라도 적으세요.").setMessage("제목이나 내용은 비워둘 수 없습니다.")
                            .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                }
                            });
                    AlertDialog dialog = builder.create();
                    dialog.show();
                    return;
                }
                //클릭 시, 다이얼로그를 띄워 한번 더 확인.
                AlertDialog.Builder builder = new AlertDialog.Builder(FreeBoardWritingActivity.this);
                builder.setIcon(R.drawable.icon_dolphins)
                        .setTitle("게시글 작성")
                        .setMessage("글을 작성하시겠어요?")
                        .setNegativeButton("취소",null)
                        .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                            //확인버튼 클릭 시, 서버로 요청을 보내 게시글을 업로드
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Response.Listener<String> responseListener = new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {
                                        try {
                                            JSONObject jsonObject = new JSONObject(response);
                                            boolean isSuccess = jsonObject.getBoolean("success");

                                            if(isSuccess)
                                            {
                                                Toast.makeText(FreeBoardWritingActivity.this,"게시글이 작성되었습니다.", Toast.LENGTH_LONG).show();
                                                finish();
                                            }
                                            else
                                            {
                                                Toast.makeText(FreeBoardWritingActivity.this,"실패했습니다. 잠시 후 다시시도해보세요.", Toast.LENGTH_LONG).show();
                                            }

                                        }
                                        catch (Exception e)
                                        {
                                            Toast.makeText(FreeBoardWritingActivity.this,"실패했습니다. 잠시 후 다시시도해보세요.", Toast.LENGTH_LONG).show();
                                            e.printStackTrace();
                                        }
                                    }
                                };
                                RequestFreeBoardWrite validateRequest = new RequestFreeBoardWrite(writtenTitle.getText().toString(), writtenContents.getText().toString(), UserData.getInstance().getUserName(), UserData.getInstance().getUserPhoneNum(), responseListener);
                                RequestQueue queue = Volley.newRequestQueue(FreeBoardWritingActivity.this);
                                queue.add(validateRequest);
                            }
                        })
                        .show();
            }
        });
    }
}
