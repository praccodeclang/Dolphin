package com.taewon.dolphin;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

public class ScatterCanvas extends View {
    public ScatterCanvas(Context context, AttributeSet attrs){
        super(context, attrs);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Resources res = getResources();
        BitmapDrawable bd = null;
        bd = (BitmapDrawable)res.getDrawable(R.drawable.bubble);
        Bitmap bit = bd.getBitmap();
//        int w = bit.getWidth();
//        int h = bit.getHeight();

//        float fx = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,1,res.getDisplayMetrics());

        Paint paint = new Paint();
        paint.setAntiAlias(true);

        canvas.drawBitmap(bit,0,0 ,paint);
    }
}
