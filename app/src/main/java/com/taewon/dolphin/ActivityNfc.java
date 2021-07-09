package com.taewon.dolphin;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.nfc.NfcAdapter;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.common.BitMatrix;

public class ActivityNfc extends AppCompatActivity {
//    NfcAdapter nfcAdapter;

    private WindowManager.LayoutParams params;
    private float brightness;
    private ImageView userBarcode;
    private TextView barcodeInfo;
    private static final String str = "J"+UserData.getInstance().getUserStudentCode();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nfc_student_card);
        Toast.makeText(ActivityNfc.this,"흔들림 발생",Toast.LENGTH_SHORT).show();

        params = getWindow().getAttributes();
        userBarcode = (ImageView)findViewById(R.id.userBarcode);
        barcodeInfo = (TextView)findViewById(R.id.barcodeInfo);


        userBarcode.setImageBitmap(drawUserBarcode(str));
        userBarcode.invalidate();
        barcodeInfo.setText(str);
    }

    @Override
    protected void onResume() {
        super.onResume();

        /* 밝기를 조절합니다. */
        brightness = params.screenBrightness;
        params.screenBrightness = 1f;
        getWindow().setAttributes(params);

//        NfcAdapter nfcAdapter = NfcAdapter.getDefaultAdapter(this);
//        if(nfcAdapter.isEnabled())
//        {
//            //nfc가 켜져있다면 실행
//        }
//        else
//        {
//            AlertDialog.Builder builder = new AlertDialog.Builder(ActivityNfc.this);
//            builder.setIcon(R.drawable.icon_dolphins)
//                    .setTitle("NFC기능이 꺼져있습니다.")
//                    .setMessage("설정에서 NFC를 켜주세요.")
//                    .setPositiveButton("설정하기",new DialogInterface.OnClickListener()
//                    {
//                        @Override
//                        public void onClick(DialogInterface dialog, int which) {
//                            if(Build.VERSION.SDK_INT > Build.VERSION_CODES.JELLY_BEAN)
//                            {
//                                startActivity(new Intent(Settings.ACTION_NFC_SETTINGS));
//                            }
//                            else
//                            {
//                                startActivity(new Intent(Settings.ACTION_WIRELESS_SETTINGS));
//                            }
//                        }
//                    }).show();
//        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        /* 밝기를 원상복구 합니다.*/
        params.screenBrightness = brightness;
        getWindow().setAttributes(params);
    }

    /* 바코드를 그립니다 */
    private Bitmap drawUserBarcode(String code){
        Bitmap bitmap = null;
        MultiFormatWriter gen = new MultiFormatWriter();
        try {
            final int width = 400;
            final int height = 200;
            BitMatrix bytemap = gen.encode(code, BarcodeFormat.CODE_128, width, height);
            bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
            for(int i = 0; i<width; ++i)
            {
                for(int j=0; j<height; ++j)
                {
                    bitmap.setPixel(i, j, bytemap.get(i ,j) ? Color.BLACK : Color.WHITE);
                }
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return bitmap;
    }
}
