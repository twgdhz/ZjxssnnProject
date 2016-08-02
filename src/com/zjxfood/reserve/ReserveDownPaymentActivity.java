package com.zjxfood.reserve;

import com.umeng.analytics.MobclickAgent;
import com.zjxfood.activity.R;

import android.app.Activity;
import android.os.Bundle;

public class ReserveDownPaymentActivity extends Activity{
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.reserve_info_down_payment_layout);
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
