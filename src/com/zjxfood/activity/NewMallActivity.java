package com.zjxfood.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zjxfood.application.ExitApplication;
import com.zjxfood.toast.MyToast;

/**
 * Created by Administrator on 2016/7/14.
 */
public class NewMallActivity extends AppActivity implements View.OnClickListener{

    private LinearLayout mMainLayout,mBuyLayout,mMallLayout,mMyLayout;
    private FrameLayout mXjLayout,mSsLayout,mJbLayout,mZcLayout;
    private boolean isExit = false;
    private MyToast mToast;
    private TextView mBottomText;
    private View mBottomView;
    private ImageView mBottomImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_mall_layout);
        setImmerseLayout(findViewById(R.id.head_layout));
        mToast = new MyToast(getApplicationContext());
        init();
    }

    private void init(){
        mMainLayout = (LinearLayout) findViewById(R.id.new_main_home_menu_layout);
        mBuyLayout = (LinearLayout) findViewById(R.id.new_main_buy_menu_layout);
        mMallLayout = (LinearLayout) findViewById(R.id.new_my_buy_car);
        mMyLayout = (LinearLayout) findViewById(R.id.new_main_my_menu_layout);
        mXjLayout = (FrameLayout) findViewById(R.id.xj_mall_layout);
        mSsLayout = (FrameLayout) findViewById(R.id.ss_mall_layout);
        mJbLayout = (FrameLayout) findViewById(R.id.jb_mall_layout);
        mZcLayout = (FrameLayout) findViewById(R.id.zc_mall_layout);
        mBottomText = (TextView) findViewById(R.id.bottom_cars_text);
        mBottomView = findViewById(R.id.new_main_main_view3);
        mBottomImage = (ImageView) findViewById(R.id.new_car_menu_image);
        mMallLayout.setBackgroundColor(getResources().getColor(R.color.main_color13));
        mBottomImage.setImageDrawable(getResources().getDrawable(R.drawable.new_mall_hover));
        mBottomView.setVisibility(View.VISIBLE);
        mBottomText.setTextColor(getResources().getColor(R.color.white));

        mMainLayout.setOnClickListener(click);
        mBuyLayout.setOnClickListener(click);
        mMallLayout.setOnClickListener(click);
        mMyLayout.setOnClickListener(click);
        mXjLayout.setOnClickListener(this);
        mSsLayout.setOnClickListener(this);
        mJbLayout.setOnClickListener(this);
        mZcLayout.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        Intent intent = new Intent();

        switch (view.getId()){
            //现金商城
            case R.id.xj_mall_layout:
                intent.setClass(getApplicationContext(), NewCashMallActivity.class);
                startActivity(intent);
                break;
            //食尚币商城
            case R.id.ss_mall_layout:
                intent.setClass(getApplicationContext(), MallIndexActivity.class);
                startActivity(intent);
                break;
            //金币商城
            case R.id.jb_mall_layout:
                intent.setClass(getApplicationContext(), NewJbMallActivity.class);
                startActivity(intent);
                break;
            case R.id.zc_mall_layout:

                break;
        }
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                exit();
                break;

            default:
                break;
        }
        return false;
    }
    // 退出程序
    private void exit() {
        if (!isExit) {
            isExit = true;
            mToast.show("再按一次退出程序", 1);
            handler.sendEmptyMessageDelayed(0, 2000);
        } else {
            ExitApplication.getInstance().exit();
        }
    }
    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 0:
                    isExit = false;
                    break;
            }
        }
    };
}
