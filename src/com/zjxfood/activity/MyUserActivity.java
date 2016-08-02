package com.zjxfood.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.umeng.analytics.MobclickAgent;
import com.zjxfood.application.ExitApplication;

/**
 * 我的账户
 * @author zjx
 *
 */
public class MyUserActivity extends AppActivity implements OnClickListener {

	private RelativeLayout mMyUserLayout, mAccountSecurityLayout;
	private RelativeLayout mHeadLayout;
	private ImageView mCancelImage;
	private RelativeLayout mCodeLayout;
	private RelativeLayout mMySettingLayout;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.my_user_layout);
		setImmerseLayout(findViewById(R.id.head_layout));
		ExitApplication.getInstance().addActivity(this);
		ExitApplication.getInstance().addMyAccountActivity(this);
		init();
	}

	private void init() {
		mAccountSecurityLayout = (RelativeLayout) findViewById(R.id.my_account_security_layout);
		mMyUserLayout = (RelativeLayout) findViewById(R.id.my_user_name_layout);
		mHeadLayout = (RelativeLayout) findViewById(R.id.title_my_user_id);
		mCancelImage = (ImageView) mHeadLayout
				.findViewById(R.id.my_user_info_image);
		mMySettingLayout = (RelativeLayout) findViewById(R.id.my_account_setting_layout);
		
		mAccountSecurityLayout.setOnClickListener(this);
		mMyUserLayout.setOnClickListener(this);
		mCancelImage.setOnClickListener(this);
		mMySettingLayout.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		Intent intent = new Intent();
		switch (v.getId()) {
		case R.id.my_account_security_layout:
			intent.setClass(getApplicationContext(), MyAccountActivity.class);
			startActivity(intent);
			break;

		case R.id.my_user_name_layout:
			intent.setClass(getApplicationContext(), MyUserModifyActivity.class);
			startActivity(intent);
			break;
		case R.id.my_user_info_image:
			finish();
			break;
		case R.id.my_account_setting_layout:
			intent.setClass(getApplicationContext(), MySettingActivity.class);
			startActivity(intent);
			break;
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		switch (keyCode) {
		case KeyEvent.KEYCODE_BACK:
			finish();
			break;

		default:
			break;
		}
		return super.onKeyDown(keyCode, event);
	}
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		MobclickAgent.onResume(this);
	}
	
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		MobclickAgent.onPause(this);
	}
}
