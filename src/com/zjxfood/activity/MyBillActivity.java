package com.zjxfood.activity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.umeng.analytics.MobclickAgent;
import com.zjxfood.application.ExitApplication;
/**
 * 我的账单
 * @author zjx
 *
 */
public class MyBillActivity extends AppActivity implements OnClickListener{

	private LinearLayout mThisMonthLayout,mMayMonthLayout,mAprilMonthLayout;
	private ImageView mBackImage;
	private TextView mTitleText;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.my_bill_layout);
		setImmerseLayout(findViewById(R.id.head_layout));
		ExitApplication.getInstance().addActivity(this);
		
		init();
		for(int i=0;i<3;i++){
			addView(mThisMonthLayout);
			addView(mMayMonthLayout);
			addView(mAprilMonthLayout);
		}
	}
	
	private void init(){
		mThisMonthLayout = (LinearLayout) findViewById(R.id.my_bill_now_month_list);
		mMayMonthLayout = (LinearLayout) findViewById(R.id.my_bill_now_month_list2);
		mAprilMonthLayout = (LinearLayout) findViewById(R.id.my_bill_now_month_list3);
//		mBackImage = (ImageView) findViewById(R.id.title_back_image);
		mBackImage = (ImageView) findViewById(R.id.title_back_image);
		mTitleText = (TextView) findViewById(R.id.title_text);
		mTitleText.setText("我的账单");
		mBackImage.setOnClickListener(this);
	}
	
	private void addView(LinearLayout layout){
		LayoutInflater inflater = LayoutInflater.from(getApplicationContext());
		View view = inflater.inflate(R.layout.my_bill_balance_list_item, null);
		layout.addView(view);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.title_back_image:
			System.gc();
			
			finish();
			break;

		default:
			break;
		}
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
