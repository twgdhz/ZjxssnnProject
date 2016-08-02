package com.zjxfood.activity;

import android.os.Bundle;

import com.umeng.analytics.MobclickAgent;

public class MallContactListActivity extends AppActivity{
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mall_contact_customer_service);
		setImmerseLayout(findViewById(R.id.head_layout));
	}
	
	private void init(){
		
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
