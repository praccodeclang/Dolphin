package com.taewon.dolphin;

import android.content.DialogInterface;
import android.content.Intent;
import android.nfc.NfcAdapter;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class ActivityNfc extends AppCompatActivity {
    NfcAdapter nfcAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nfc_student_card);

    }

    @Override
    protected void onResume() {
        super.onResume();
        NfcAdapter nfcAdapter = NfcAdapter.getDefaultAdapter(this);
        if(nfcAdapter.isEnabled())
        {
            //nfc가 켜져있다면 실행
        }
        else
        {
            AlertDialog.Builder builder = new AlertDialog.Builder(ActivityNfc.this);
            builder.setIcon(R.drawable.icon_dolphins)
                    .setTitle("NFC기능이 꺼져있습니다.")
                    .setMessage("설정에서 NFC를 켜주세요.")
                    .setPositiveButton("설정하기",new DialogInterface.OnClickListener()
                    {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if(Build.VERSION.SDK_INT > Build.VERSION_CODES.JELLY_BEAN)
                            {
                                startActivity(new Intent(Settings.ACTION_NFC_SETTINGS));
                            }
                            else
                            {
                                startActivity(new Intent(Settings.ACTION_WIRELESS_SETTINGS));
                            }
                        }
                    }).show();
        }
    }
}
