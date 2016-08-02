package com.zjxfood.qq;

import android.app.Activity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tencent.tauth.Tencent;
import com.zjxfood.activity.MallDetailActivity;
import com.zjxfood.activity.R;

public class BaseActivity extends Activity{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.setContentView(R.layout.base_activity_with_titlebar_layout);
		setTitle(null);
		setLeftButton("返回", new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}

    protected void checkTencentInstance() {
    	 MallDetailActivity.mAppid = "222222";
    	 MallDetailActivity.mTencent = Tencent.createInstance(MallDetailActivity.mAppid, BaseActivity.this);

    }

	@Override
    protected void onDestroy() {
        super.onDestroy();
        Util.release();
    }

    @Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode == KeyEvent.KEYCODE_BACK) {
			Util.dismissDialog();
		}
		return super.onKeyDown(keyCode, event);
	}
	public void setLeftButtonEnable(){
		((Button)findViewById(R.id.left_button)).setVisibility(View.VISIBLE);
	}
	@Override
	public void setContentView(int layoutResID) {
		LayoutInflater inflater = LayoutInflater.from(this);
		((LinearLayout)findViewById(R.id.layout_content)).addView(inflater.inflate(layoutResID, null));
	}

	public void setLeftButton(String title, OnClickListener listener){
		Button leftButton = (Button)findViewById(R.id.left_button);
		leftButton.setText(title);
		leftButton.setOnClickListener(listener);
	}

	public void setLeftButton(int strId, OnClickListener listener){
		Button leftButton = (Button)findViewById(R.id.left_button);
		leftButton.setText(getResources().getString(strId));
		leftButton.setOnClickListener(listener);
	}

	public void setBarTitle(String title){
		((TextView)findViewById(R.id.base_title)).setText(title);
	}
}
