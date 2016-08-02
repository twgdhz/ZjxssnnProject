package com.zjxfood.view;

import com.zjxfood.activity.R;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.LinearLayout;

@SuppressLint("NewApi")
public class WaitingView extends LinearLayout{
    private static final int NUM=5;
    private Context context;
    private ImageView mOldDot;
    public WaitingView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.context=context;
        init();
        // TODO Auto-generated constructor stub
    }

    public WaitingView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context=context;
        init();
        // TODO Auto-generated constructor stub
    }

    public WaitingView(Context context) {
        super(context);
        this.context=context;
        init();
        // TODO Auto-generated constructor stub
    }
    
    private void init(){
        this.setOrientation(LinearLayout.HORIZONTAL);
        this.setGravity(Gravity.CENTER);
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(),
                R.drawable.office_waitingbar_indicator);
        LinearLayout.LayoutParams tLayoutParams=new LinearLayout.LayoutParams(bitmap.getWidth(),
                bitmap.getHeight());
        tLayoutParams.leftMargin=10;
        tLayoutParams.rightMargin=10;
        for (int i = 0; i < NUM; i++) {
            ImageView vDot = new ImageView(context);
            vDot.setLayoutParams(tLayoutParams);
            if (i == 0) {
                vDot.setBackgroundResource(R.drawable.office_waitingbar_indicator_sel);
            } else {
                vDot.setBackgroundResource(R.drawable.office_waitingbar_indicator);
            }
            this.addView(vDot);
        }
        mOldDot=(ImageView) this.getChildAt(0);
        new UpdateHandler().sendEmptyMessage(0);
    }
    class UpdateHandler extends Handler{
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            int tPosition=msg.what;
            if(mOldDot!=null)
                mOldDot.setBackgroundResource(R.drawable.office_waitingbar_indicator);
            ImageView currentDot = (ImageView) WaitingView.this
                    .getChildAt(tPosition);
            currentDot
                    .setBackgroundResource(R.drawable.office_waitingbar_indicator_sel);
            mOldDot=currentDot;
            if(++tPosition==NUM)
                tPosition=0;
            this.sendEmptyMessageDelayed(tPosition, 200);
        }
    }
}
