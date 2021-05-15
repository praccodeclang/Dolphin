package com.taewon.dolphin;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;


public class StartActivity extends AppCompatActivity {

    private static final int MULTIPLE_PERMISSION = 10235;
    ImageView imageView;
    Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        /* 권한이 있는지 검사합니다. */
        String[] PEMISSIONS = { Manifest.permission.SEND_SMS, Manifest.permission.RECEIVE_SMS, Manifest.permission.READ_PHONE_STATE };
        if(hasPermissions(this, PEMISSIONS)){
            ActivityCompat.requestPermissions(this, PEMISSIONS, MULTIPLE_PERMISSION);
        }
        else
        {
            Intent intent = new Intent(StartActivity.this, LoginActivity.class);
            startActivity(intent);
        }


        //임시로 그냥 얻어오기만 했어요.
        imageView = (ImageView)findViewById(R.id.logo);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case  MULTIPLE_PERMISSION:
                if(grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    //모든 권한이 있으면 실행합니다.
                    Intent intent = new Intent(StartActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                }
                else{
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setTitle("앱 권한").setMessage("우리 앱을 원활하게 사용하려면, 애플리케이션 정보>권한 에서 모든 권한을 허용해주세요.");
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
                        }
                    });
                    builder.show();
                }
                return;
        }
    }
    public static boolean hasPermissions(Context context, String[] permissions)
    {
        if(context != null && permissions != null)
        {
            for(String permission : permissions)
            {
                if(ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_DENIED)
                {
                    return false;
                }
            }
        }
        return true;
    }



}
