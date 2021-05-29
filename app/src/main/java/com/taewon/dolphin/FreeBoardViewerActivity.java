package com.taewon.dolphin;

import android.content.DialogInterface;
import android.content.Intent;
import android.media.Image;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class FreeBoardViewerActivity extends AppCompatActivity {
    Intent freeBoardIntent;

    private ListView freeBoardCommentListView;
    private EditText writeCommentEditText;
    private ImageButton writeCommentBtn;
    private String BoardID;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_freeboard_viewer);


        /* Views */
        /* 댓글 구현 시 필요한 뷰 */
        freeBoardCommentListView = (ListView) findViewById(R.id.freeBoardCommentListView);
        writeCommentEditText = (EditText) findViewById(R.id.writeCommentEditText);
        writeCommentBtn = (ImageButton) findViewById(R.id.writeCommentBtn);

        TextView writer = (TextView) findViewById(R.id.writer);
        TextView writtenDate = (TextView) findViewById(R.id.writtenDate);
        TextView board_title = (TextView) findViewById(R.id.board_title);
        TextView board_contents = (TextView) findViewById(R.id.board_contents);
        LinearLayout profileLayout = (LinearLayout) findViewById(R.id.board_profileLayout);

        //Name, Date, Title, Contents, userPhone, BoardID 이라는 이름으로 넘겨받습니다.
        freeBoardIntent = getIntent();



        //update&deleteBtn을 담는 레이아웃
        LinearLayout udBtns = (LinearLayout) findViewById(R.id.udBtns);
        ImageButton FreeBoardViewBackBtn = (ImageButton) findViewById(R.id.FreeBoardViewBackBtn);
        TextView deleteBtn = (TextView) findViewById(R.id.deleteBtn);
        TextView modifyBtn = (TextView) findViewById(R.id.modifyBtn);


        BoardID = freeBoardIntent.getStringExtra("BoardID");
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





        //만약 내가 작성한 글이라면, 수정 삭제를 가능하게 합니다.
        if (writer.getText().equals(UserData.getInstance().getUserName())) {
            udBtns.setVisibility(View.VISIBLE);
        }

        //onClickListener들
        //profile을 누르면 전화를 걸 수 있도록 합니다.
        profileLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(FreeBoardViewerActivity.this);
                builder.setIcon(R.drawable.icon_dolphins).setTitle("전화걸기").setMessage("정말로 " + freeBoardIntent.getStringExtra("Name") + "님에게 전화를 거시겠습니까?")
                        .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + freeBoardIntent.getStringExtra("userPhone")));
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
                                        try {
                                            JSONObject jsonObject = new JSONObject(response);
                                            boolean success = jsonObject.getBoolean("success");
                                            if (success) {
                                                Toast.makeText(FreeBoardViewerActivity.this, "삭제되었습니다.", Toast.LENGTH_SHORT).show();
                                                finish();
                                            } else {
                                                Toast.makeText(FreeBoardViewerActivity.this, "삭제하지 못했습니다.", Toast.LENGTH_SHORT).show();
                                            }
                                        } catch (Exception e) {
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
                        .setNegativeButton("취소", null).show();
            }
        });


        //댓글 작성버튼 클릭시,
        writeCommentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String comment = writeCommentEditText.getText().toString();
                Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            boolean success = jsonObject.getBoolean("success");
                            if(success)
                            {
                                onResume();
                            }
                            else
                            {
                                AlertDialog.Builder builder = new AlertDialog.Builder(FreeBoardViewerActivity.this);
                                builder.setIcon(R.drawable.icon_dolphins)
                                        .setTitle("오류")
                                        .setMessage("댓글을 작성하지 못했습니다. 잠시 후 다시 시도해 보세요!")
                                        .setPositiveButton("확인",null)
                                        .show();
                            }
                        }
                        catch (Exception e)
                        {
                            AlertDialog.Builder builder = new AlertDialog.Builder(FreeBoardViewerActivity.this);
                            builder.setIcon(R.drawable.icon_dolphins)
                                    .setTitle("오류")
                                    .setMessage("댓글을 작성하지 못했습니다. 잠시 후 다시 시도해 보세요!")
                                    .setPositiveButton("확인",null)
                                    .show();
                            e.printStackTrace();
                        }
                    }
                };
                RequestCommentWrite validateRequest = new RequestCommentWrite(BoardID, UserData.getInstance().getUserName(), comment, responseListener);
                RequestQueue queue = Volley.newRequestQueue(FreeBoardViewerActivity.this);
                queue.add(validateRequest);
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        //댓글을 로드합니다.
        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("DATA");
                    List<CommentItem> commentItemList = new ArrayList<>();
                    for(int i = 0; i < jsonArray.length(); i++)
                    {
                        String userName, date, userComment;
                        JSONObject obj = jsonArray.getJSONObject(i);

                        userName = obj.get("userName").toString();
                        userComment = obj.get("userComment").toString();
                        date = obj.get("date").toString();

                        commentItemList.add(new CommentItem(userName, date, userComment));
                    }
                    CommentAdapter adapter = new CommentAdapter(FreeBoardViewerActivity.this, commentItemList);
                    freeBoardCommentListView.setAdapter(adapter);
                    MainActivity.setListViewHeightBasedOnChildren(freeBoardCommentListView);
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }

            }
        };
        RequestGetComment validateRequest = new RequestGetComment(freeBoardIntent.getStringExtra("BoardID"), responseListener);
        RequestQueue queue = Volley.newRequestQueue(FreeBoardViewerActivity.this);
        queue.add(validateRequest);

    }
}
