package com.taewon.dolphin;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.telephony.SmsManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.util.ArrayList;

public class RegisterActivity extends AppCompatActivity {

    Boolean isValidate = false;
    Boolean isCertified = false;
    Boolean isPassSame = false;
    private View decorView;
    private int uiOption;
    private int userRandNum;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        isValidate = false;
        isCertified = false;
        isPassSame = false;
        final Button validateBtn = (Button)findViewById(R.id.validate);
        final Button registerBtn = (Button)findViewById(R.id.registerBtn);
        final Button certifyPhone = (Button)findViewById(R.id.certifyPhone);
        final Button certifyBtn = (Button)findViewById(R.id.certifyBtn);

        final EditText nameText = (EditText)findViewById(R.id.nameText);
        final EditText idText = (EditText)findViewById(R.id.idText);
        final EditText passwordText = (EditText)findViewById(R.id.passwordText);
        final EditText passwordTextChk = (EditText)findViewById(R.id.passwordTextChk);
        final TextView warningText = (TextView)findViewById(R.id.warningText);
        final Spinner userMajor = (Spinner)findViewById(R.id.userMajor);
        final Spinner userDept = (Spinner)findViewById(R.id.userDept);
        final EditText PHONE = (EditText)findViewById(R.id.PHONE);


        final EditText certifyNum = (EditText)findViewById(R.id.certifyNum);
        final LinearLayout ceritifyZone = (LinearLayout)findViewById(R.id.certifyZone);
        AlertDialog alertDialog;


        //1. 중복확인 버튼
        validateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //클릭되면 실행.
                String userID = idText.getText().toString();
                if(userID.equals(""))
                {
                    AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
                    builder.setIcon(R.drawable.icon_dolphins).setTitle("빈칸인데요?!").setMessage("\t아이디를 비우지말아요.\n\t우리는 당신이 궁금하거든요.").setNegativeButton("확인", null);
                    AlertDialog dialog = builder.create();
                    dialog.show();
                    return;
                }
                if(userID.length() < 4)
                {
                    AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
                    builder.setIcon(R.drawable.icon_dolphins).setTitle("아이디가 너무 짧아요.").setMessage("\t아이디는 4자리가 넘게해주세요.").setNegativeButton("확인", null);
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
                            boolean isExistUser = jsonObject.getBoolean("success");
                            if(isExistUser)
                            {
                                AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
                                builder.setIcon(R.drawable.icon_dolphins).setTitle("중복된 아이디").setMessage("\t이미 존재하는 아이디입니다.\n\t다른 아이디를 입력해주세요.").setNegativeButton("확인", null);
                                AlertDialog dialog = builder.create();
                                dialog.show();
                            }
                            else
                            {
                                AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
                                builder.setIcon(R.drawable.icon_dolphins).setMessage("이 아이디로 하시겠습니까?").setNegativeButton("취소", null).setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        idText.setBackgroundColor(getResources().getColor(R.color.LockColor));
                                        idText.setEnabled(false);
                                        validateBtn.setBackgroundColor(getResources().getColor(R.color.LockColor));
                                        validateBtn.setEnabled(false);
                                        isValidate = true;
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

                RequestUserValidate validateRequest = new RequestUserValidate(userID, responseListener);
                RequestQueue queue = Volley.newRequestQueue(RegisterActivity.this);
                queue.add(validateRequest);
            }
        });

        //패스워드 확인
        passwordTextChk.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                warningText.setVisibility(View.VISIBLE);
                if(passwordText.getText().toString().equals(passwordTextChk.getText().toString()))
                {
                    warningText.setTextColor(Color.GREEN);
                    warningText.setText("비밀번호가 일치합니다.");
                    isPassSame = true;
                }
                else
                {
                    warningText.setTextColor(Color.RED);
                    warningText.setText("비밀번호가 다릅니다.");
                    isPassSame = false;
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        //전화번호 인증 버튼
        certifyPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //클릭되면 실행.
                String userPhone = PHONE.getText().toString();

                //만약 번호가 11자리가 아니면, 함수를 실행하지 않습니다.
                if(userPhone.length() != 11)
                {
                    AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
                    builder.setIcon(R.drawable.icon_dolphins).setTitle("휴대폰이 맞나요?").setMessage("제가 아는 휴대폰은 11자리 숫자인걸요?").setNegativeButton("확인",null);
                    AlertDialog dialog = builder.create();
                    dialog.show();
                    return;
                }

                //1000~9999 난수발생해서 저장하고, 문자를 보냅니다.
                try {
                    //문자가 보내지지 않으면, 사용자가 권한을 설정하지 않은것이므로, 앱정보로 이동해 권한을 설정할 수 있도록 합니다.
                    android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(RegisterActivity.this);
                    builder.setIcon(R.drawable.icon_dolphins).setTitle("휴대폰 인증").setMessage("핸드폰으로 문자 한 개만 보낼게요!\n대부분 문자는 공짜입니다!");
                    builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            userRandNum = (int)((Math.random()*8999)+1000);
                            sendSMS(userPhone, "Dolphin 가입을 위한 인증코드: "+Integer.toString(userRandNum));
                            ceritifyZone.setVisibility(View.VISIBLE);
                        }
                    });
                    builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    }).show();

                }
                catch (Exception e)
                {
                    //문자가 보내지지 않으면, 사용자가 권한을 설정하지 않은것이므로, 앱정보로 이동해 권한을 설정할 수 있도록 합니다.
                    android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(RegisterActivity.this);
                    builder.setIcon(R.drawable.icon_dolphins).setTitle("앱 권한").setMessage("우리 앱을 원활하게 사용하려면, 애플리케이션 정보>권한 에서 모든 권한을 허용해주세요.");
                    builder.setPositiveButton("권한설정", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).setData(Uri.parse("package:"+getApplicationContext().getPackageName()));
                            startActivity(intent);
                            dialog.cancel();;
                        }
                    });
                    builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                            finish();
                        }
                    }).show();
                }
            }
        });

        //유저가 받은 인증번호를 입력하고 인증버튼을 누르면, 실행
        certifyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int userInput;
                try {
                    userInput = Integer.parseInt(certifyNum.getText().toString());
                    if(isCompareCertifyNum(userInput, userRandNum))
                    {
                        AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
                        builder.setMessage("\t인증되었습니다.").setNegativeButton("확인",null);
                        AlertDialog dialog = builder.create();
                        dialog.show();
                        isCertified = true;

                        ceritifyZone.setVisibility(View.GONE);
                        certifyPhone.setEnabled(false);
                        certifyPhone.setBackgroundColor(getResources().getColor(R.color.LockColor));
                        PHONE.setEnabled(false);
                        PHONE.setBackgroundColor(getResources().getColor(R.color.LockColor));
                        certifyBtn.setEnabled(false);
                        certifyBtn.setBackgroundColor(getResources().getColor(R.color.LockColor));
                        return;
                    }
                    else
                    {
                        AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
                        builder.setIcon(R.drawable.icon_dolphins).setTitle("그게 아닌것 같아요.").setMessage("\t당신의 휴대폰이 맞나요?").setNegativeButton("확인",null);
                        AlertDialog dialog = builder.create();
                        dialog.show();
                        return;
                    }
                }
                catch (Exception e)
                {
                    AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
                    builder.setIcon(R.drawable.icon_dolphins).setTitle("우리는 4자리 숫자를 보냈어요").setMessage("\t4자리 숫자가 맞는지 확인해주세요!").setNegativeButton("확인",null);
                    AlertDialog dialog = builder.create();
                    dialog.show();
                    return;
                }

            }
        });

        //회원가입 버튼
        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //중복확인 안했을 시.
                if(!isValidate)
                {
                    AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
                    builder.setIcon(R.drawable.icon_dolphins).setMessage("\t중복 확인을 해주세요.").setNegativeButton("확인",null);
                    AlertDialog dialog = builder.create();
                    dialog.show();
                    return;
                }

                //휴대폰 인증 안했을 시.
                if(!isCertified)
                {
                    AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
                    builder.setIcon(R.drawable.icon_dolphins).setMessage("\t휴대폰을 인증해주세요.").setNegativeButton("확인",null);
                    AlertDialog dialog = builder.create();
                    dialog.show();
                    return;
                }
                //비밀번호 다를 시.
                if(!isPassSame)
                {
                    AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
                    builder.setIcon(R.drawable.icon_dolphins).setMessage("\t휴대폰을 인증해주세요.").setNegativeButton("확인",null);
                    AlertDialog dialog = builder.create();
                    dialog.show();
                    return;
                }
                String userID = idText.getText().toString();
                String userPassword = passwordText.getText().toString();
                String userName = nameText.getText().toString();
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
                if(userPassword.length()<6)
                {
                    AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
                    builder.setIcon(R.drawable.icon_dolphins).setTitle("비밀번호가 너무 짧습니다.").setMessage("\t비밀번호는 6자리 이상이었으면 좋겠어요.").setNegativeButton("확인",null);
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
                                builder.setMessage("왜 안될까요..?").setMessage("\t저도 잘 모르겠어요.\n\t개발자에게 알려주세요\n\t010-4640-7993").setNegativeButton("확인", null);
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

                RequestUserRegister validateRequest = new RequestUserRegister(userID, userPassword, userName, Major, department, userPhone, responseListener);
                RequestQueue queue = Volley.newRequestQueue(RegisterActivity.this);
                queue.add(validateRequest);
            }
        });

        //메이저 선택시,
        userMajor.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                ArrayAdapter<String> adapter;
                switch (position)
                {
                    case 0:
                        adapter = new ArrayAdapter<>(RegisterActivity.this, R.layout.support_simple_spinner_dropdown_item, getResources().getStringArray(R.array.it));
                        userDept.setAdapter(adapter);
                        break;
                    case 1:
                        adapter = new ArrayAdapter<>(RegisterActivity.this, R.layout.support_simple_spinner_dropdown_item, getResources().getStringArray(R.array.nursing));
                        userDept.setAdapter(adapter);
                        break;
                    case 2:
                        adapter = new ArrayAdapter<>(RegisterActivity.this, R.layout.support_simple_spinner_dropdown_item, getResources().getStringArray(R.array.early_childhood_education));
                        userDept.setAdapter(adapter);
                        break;
                    case 3:
                        adapter = new ArrayAdapter<>(RegisterActivity.this, R.layout.support_simple_spinner_dropdown_item, getResources().getStringArray(R.array.sports_guidance));
                        userDept.setAdapter(adapter);
                        break;
                    case 4:
                        adapter = new ArrayAdapter<>(RegisterActivity.this, R.layout.support_simple_spinner_dropdown_item, getResources().getStringArray(R.array.architectural_design));
                        userDept.setAdapter(adapter);
                        break;
                    case 5:
                        adapter = new ArrayAdapter<>(RegisterActivity.this, R.layout.support_simple_spinner_dropdown_item, getResources().getStringArray(R.array.physiotherapy));
                        userDept.setAdapter(adapter);
                        break;
                    case 6:
                        adapter = new ArrayAdapter<>(RegisterActivity.this, R.layout.support_simple_spinner_dropdown_item, getResources().getStringArray(R.array.socialwelfare));
                        userDept.setAdapter(adapter);
                        break;
                    case 7:
                        adapter = new ArrayAdapter<>(RegisterActivity.this, R.layout.support_simple_spinner_dropdown_item, getResources().getStringArray(R.array.mechanical_Engineering));
                        userDept.setAdapter(adapter);
                        break;
                    case 8:
                        adapter = new ArrayAdapter<>(RegisterActivity.this, R.layout.support_simple_spinner_dropdown_item, getResources().getStringArray(R.array.dental_hygiene));
                        userDept.setAdapter(adapter);
                        break;
                    case 9:
                        adapter = new ArrayAdapter<>(RegisterActivity.this, R.layout.support_simple_spinner_dropdown_item, getResources().getStringArray(R.array.tax_accounting));
                        userDept.setAdapter(adapter);
                        break;
                    case 10:
                        adapter = new ArrayAdapter<>(RegisterActivity.this, R.layout.support_simple_spinner_dropdown_item, getResources().getStringArray(R.array.electrical_and_electronic_engineering));
                        userDept.setAdapter(adapter);
                        break;
                    case 11:
                        adapter = new ArrayAdapter<>(RegisterActivity.this, R.layout.support_simple_spinner_dropdown_item, getResources().getStringArray(R.array.food_and_nutrition));
                        userDept.setAdapter(adapter);
                        break;
                    case 12:
                        adapter = new ArrayAdapter<>(RegisterActivity.this, R.layout.support_simple_spinner_dropdown_item, getResources().getStringArray(R.array.distribution_and_logistics_management));
                        userDept.setAdapter(adapter);
                        break;
                    case 13:
                        adapter = new ArrayAdapter<>(RegisterActivity.this, R.layout.support_simple_spinner_dropdown_item, getResources().getStringArray(R.array.safety_and_industrial_engineering));
                        userDept.setAdapter(adapter);
                        break;
                    case 14:
                        adapter = new ArrayAdapter<>(RegisterActivity.this, R.layout.support_simple_spinner_dropdown_item, getResources().getStringArray(R.array.hotel_culinary_and_bakery));
                        userDept.setAdapter(adapter);
                        break;
                    case 15:
                        adapter = new ArrayAdapter<>(RegisterActivity.this, R.layout.support_simple_spinner_dropdown_item, getResources().getStringArray(R.array.global_business));
                        userDept.setAdapter(adapter);
                        break;
                    case 16:
                        adapter = new ArrayAdapter<>(RegisterActivity.this, R.layout.support_simple_spinner_dropdown_item, getResources().getStringArray(R.array.digital_content_design));
                        userDept.setAdapter(adapter);
                        break;
                    case 17:
                        adapter = new ArrayAdapter<>(RegisterActivity.this, R.layout.support_simple_spinner_dropdown_item, getResources().getStringArray(R.array.chemical_engineering));
                        userDept.setAdapter(adapter);
                        break;
                    default:
                        break;
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }

        });
    }//onCreate_End


    //두 정수가 맞는지 리턴합니다.
    Boolean isCompareCertifyNum(int num1, int num2){
        return num1 == num2;
    }

    //메시지를 보냅니다.
    private void sendSMS(String phoneNumber, String message)
    {
        String SENT = "SMS_SENT";
        String DELIVERED = "SMS_DELIVERED";

        PendingIntent sentPI = PendingIntent.getBroadcast(this, 0, new Intent(SENT),0);
        PendingIntent deliveredPI = PendingIntent.getBroadcast(this, 0, new Intent(DELIVERED),0);

        registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                switch (getResultCode()){
                    case Activity.RESULT_OK :
                        Toast.makeText(getBaseContext(),"인증 문자가 전송되었습니다.", Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        }, new IntentFilter(SENT));
        SmsManager sms = SmsManager.getDefault();
        sms.sendTextMessage(phoneNumber, null, message, sentPI, deliveredPI);
    }


}
