package com.zjxfood.reserve;

import com.umeng.analytics.MobclickAgent;
import com.zjxfood.activity.R;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;

public class ReserveOrderInfoActivity extends Activity{
	private ListView mListView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.reserve_order_info_layout);
		//
		init();
	}
	
	private void init(){
		mListView = (ListView) findViewById(R.id.reserve_order_info_list);
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
