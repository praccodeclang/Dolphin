package com.taewon.dolphin;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

public class FreeBoardViewerActivity extends AppCompatActivity {
    Intent freeBoardIntent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_freeboard_viewer);

        //Name, Date, Title, Contents, userPhone, BoardID 이라는 이름으로 넘겨받습니다.
        freeBoardIntent = getIntent();

        //각 뷰들 초기화
        TextView writer = (TextView)findViewById(R.id.writer);
        TextView writtenDate = (TextView)findViewById(R.id.writtenDate);
        TextView board_title = (TextView)findViewById(R.id.board_title);
        TextView board_contents = (TextView)findViewById(R.id.board_contents);
        LinearLayout profileLayout = (LinearLayout)findViewById(R.id.board_profileLayout);
        
        //update&deleteBtn을 담는 레이아웃
        LinearLayout udBtns = (LinearLayout)findViewById(R.id.udBtns);
        ImageButton FreeBoardViewBackBtn = (ImageButton)findViewById(R.id.FreeBoardViewBackBtn);
        Button deleteBtn = (Button)findViewById(R.id.deleteBtn);
        Button modifyBtn = (Button)findViewById(R.id.modifyBtn);

        //TextView 텍스트 초기화
        writer.setText(freeBoardIntent.getStringExtra("Name"));
        writtenDate.setText(freeBoardIntent.getStringExtra("Date"));
        board_title.setText(freeBoardIntent.getStringExtra("Title"));
        board_contents.setText(freeBoardIntent.getStringExtra("Contents"));

        //돌아가기 버튼입니다.
        FreeBoardViewBackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //ClickListener들
        //profile을 누르면 전화를 걸 수 있도록 합니다.
        profileLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(FreeBoardViewerActivity.this);
                builder.setIcon(R.drawable.icon_dolphins).setTitle("전화걸기").setMessage("정말로 " + freeBoardIntent.getStringExtra("Name")+"님에게 전화를 거시겠습니까?")
                        .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:"+freeBoardIntent.getStringExtra("userPhone")));
                                startActivity(intent);
                            }
                        }).setNegativeButton("취소", null).show();
            }
        });

        //삭제버튼 클릭시,
        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(FreeBoardViewerActivity.this);
                builder.setIcon(R.drawable.icon_dolphins).setTitle("삭제").setMessage("정말로 삭제하시겠습니까?")
                        .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Response.Listener<String> responseListener = new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {
                                        try
                                        {
                                            JSONObject jsonObject = new JSONObject(response);
                                            boolean success = jsonObject.getBoolean("success");
                                            if(success)
                                            {
                                                Toast.makeText(FreeBoardViewerActivity.this, "삭제되었습니다.", Toast.LENGTH_SHORT).show();
                                                finish();
                                            }
                                            else
                                            {
                                                Toast.makeText(FreeBoardViewerActivity.this, "삭제하지 못했습니다.", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                        catch (Exception e)
                                        {
                                            e.printStackTrace();
                                            Toast.makeText(FreeBoardViewerActivity.this, "삭제하지 못했습니다.", Toast.LENGTH_SHORT).show();
                                        }

                                    }
                                };
                                RequestFreeBoardDelete validateRequest = new RequestFreeBoardDelete(freeBoardIntent.getStringExtra("BoardID"), responseListener);
                                RequestQueue queue = Volley.newRequestQueue(FreeBoardViewerActivity.this);
                                queue.add(validateRequest);
                            }
                        })
                        .setNegativeButton("취소",null).show();
            }
        });

        //만약 내가 작성한 글이라면, 수정 삭제를 가능하게 합니다.
        if(writer.getText().equals(UserData.getInstance().getUserName()))
        {
            udBtns.setVisibility(View.VISIBLE);
        }


    }
}
