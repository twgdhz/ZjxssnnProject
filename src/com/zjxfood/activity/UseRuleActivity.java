package com.zjxfood.activity;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.umeng.analytics.MobclickAgent;



public class UseRuleActivity extends AppActivity implements OnClickListener{

	private RelativeLayout mHeadLayout;
	private ImageView mCancelX;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.shishangbi_use_rule);
		setImmerseLayout(findViewById(R.id.head_layout));
		init();
	}
	
	private void init(){
		mHeadLayout = (RelativeLayout) findViewById(R.id.title_use_rule_id);
		mCancelX = (ImageView) mHeadLayout.findViewById(R.id.use_rule_back_info_image);
		
		mCancelX.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.use_rule_back_info_image:
			finish();
			break;

		default:
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
