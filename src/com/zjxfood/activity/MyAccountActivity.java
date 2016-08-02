package com.zjxfood.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.umeng.analytics.MobclickAgent;
import com.zjxfood.application.ExitApplication;
import com.zjxfood.common.Constants;

public class MyAccountActivity extends AppActivity implements OnClickListener {

//	private RelativeLayout mModifyUserLayout;
	private RelativeLayout mSetSecurityLayout;
	private RelativeLayout mHeadLayout;
	private RelativeLayout mModifyLayout;//修改登录密码
//	private RelativeLayout mModifyPayLayout;//修改支付密码
	private ImageView mCancelX;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.my_account_layout);
		setImmerseLayout(findViewById(R.id.head_layout));
		ExitApplication.getInstance().addActivity(this);
		ExitApplication.getInstance().addMyAccountActivity(this);
		init();
	}

	private void init() {
//		mModifyUserLayout = (RelativeLayout) findViewById(R.id.my_user_layout);
		mSetSecurityLayout = (RelativeLayout) findViewById(R.id.my_account_set_security_layout);
		mHeadLayout = (RelativeLayout) findViewById(R.id.title_my_account_id);
		mCancelX = (ImageView) mHeadLayout
				.findViewById(R.id.my_account_info_image);
		mModifyLayout = (RelativeLayout) findViewById(R.id.my_account_log_pwd_layout);
//		mModifyPayLayout = (RelativeLayout) findViewById(R.id.my_pay_pwd_layout);

		mSetSecurityLayout.setOnClickListener(this);
		mCancelX.setOnClickListener(this);
		mModifyLayout.setOnClickListener(this);
//		mModifyPayLayout.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		Intent intent = new Intent();
		switch (v.getId()) {
		case R.id.my_account_set_security_layout:
			intent.setClass(getApplicationContext(), SetSecurityActivity.class);
			startActivity(intent);
			break;

		case R.id.my_account_info_image:
			finish();
			break;
		case R.id.my_account_log_pwd_layout:
			if(Constants.onLine==1){
				intent.setClass(getApplicationContext(), ModifyLogPwdActivity.class);
				startActivity(intent);
			}else{
				intent.setClass(getApplicationContext(), MyUserLogActivity.class);
				startActivity(intent);
				finish();
				Toast.makeText(getApplicationContext(), "请先登录帐号！", Toast.LENGTH_SHORT).show();
			}
			
			break;
		case R.id.my_pay_pwd_layout:
//			if(Constants.onLine==1){
//			intent.setClass(getApplicationContext(), ModifyPayPwdActivity.class);
//			startActivity(intent);
//			}else{
//				intent.setClass(getApplicationContext(), MyUserLogActivity.class);
//				startActivity(intent);
//				finish();
//				Toast.makeText(getApplicationContext(), "请先登录帐号！", Toast.LENGTH_SHORT).show();
//			}
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
