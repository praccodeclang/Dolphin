package com.taewon.dolphin;

import android.app.AlertDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

public class FindIdFragment extends Fragment {

    EditText findIdUserNameText;
    EditText findIdUserPhoneNumText;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup root = (ViewGroup)inflater.inflate(R.layout.fragment_find_id, container, false);
        Button findIdBtn = root.findViewById(R.id.findIdBtn);
        findIdUserNameText = root.findViewById(R.id.findIdUserNameText);
        findIdUserPhoneNumText = root.findViewById(R.id.findIdUserPhoneNumText);

        findIdBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nameText = findIdUserNameText.getText().toString();
                String phoneNumText = findIdUserPhoneNumText.getText().toString();
                if(nameText.isEmpty() || phoneNumText.isEmpty())
                {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                    builder.setIcon(R.drawable.ic_baseline_block_24)
                            .setTitle("뭔가 비었어요.")
                            .setMessage("\t빈 곳없이 입력해주세요.")
                            .setPositiveButton("확인",null)
                            .show();
                    return;
                }
                requestFindId(nameText, phoneNumText);
            }
        });
        return root;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    private void requestFindId(String nameText, String phoneNumText)
    {
        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try
                {
                    JSONObject jsonObject = new JSONObject(response);
                    boolean success = jsonObject.getBoolean("success");
                    if(success)
                    {
                        String userID = jsonObject.getString("userID");
                        TextView textview = new TextView(getContext());

                        String msg = "\t"+nameText+"님의 ID는 "+userID+"입니다.";
                        SpannableStringBuilder span = new SpannableStringBuilder(msg);
                        span.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.Dolphin)),
                                msg.indexOf(" ID는")+5,
                                msg.indexOf(" ID는")+5+userID.length(),
                                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

                        textview.setText(span);
                        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                        builder.setIcon(R.drawable.icon_dolphins)
                                .setTitle("이거 같아요.")
                                .setMessage(textview.getText())
                                .setPositiveButton("확인",null)
                                .show();
                    }
                    else
                    {
                        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                        builder.setIcon(R.drawable.ic_baseline_block_24)
                                .setTitle("계정이 없습니다.")
                                .setMessage("\t회원가입 먼저 진행해주세요.")
                                .setPositiveButton("확인",null)
                                .show();
                    }
                }
                catch (Exception e)
                {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                    builder.setIcon(R.drawable.ic_baseline_block_24)
                            .setTitle("서버가 이상해요!")
                            .setMessage("\t서버가 불안정한것 같아요. \n\t잠시 후에 다시 시도해주세요.")
                            .setPositiveButton("확인",null)
                            .show();
                }
            }
        };
        RequestFindId request = new RequestFindId(nameText, phoneNumText, responseListener);
        RequestQueue queue = Volley.newRequestQueue(getContext());
        queue.add(request);

    }
}
