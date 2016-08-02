package com.zjxfood.activity;

import android.os.Bundle;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.widget.ImageView;

import com.umeng.analytics.MobclickAgent;
import com.zjxfood.adapter.FirstPagerAdapter;
import com.zjxfood.view.FirstStartViewPagerView;

public class FirstStartActivity extends AppActivity{

	private FirstStartViewPagerView mViewPager;
	private FirstPagerAdapter mFirstPagerAdapter;
	private ImageView[] mImageViews;
	private int[] mImageIds = {R.drawable.start_log1,R.drawable.start_log2,R.drawable.start_log3};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.first_start_layout);
		setImmerseLayout(findViewById(R.id.head_layout));
		init();
		mImageViews = new ImageView[3];
		for(int i=0;i<3;i++){
			ImageView imageView = new ImageView(getApplicationContext());
			mImageViews[i] = imageView;
			mImageViews[i].setImageResource(mImageIds[i]);
		}
		mFirstPagerAdapter = new FirstPagerAdapter(getApplicationContext(), mImageViews);
		mViewPager.setAdapter(mFirstPagerAdapter);
		mViewPager.setOnPageChangeListener(mPageChangeListener);
	}
	
	private void init(){
		mViewPager = (FirstStartViewPagerView) findViewById(R.id.first_start_pager);
	}
	
	OnPageChangeListener mPageChangeListener = new OnPageChangeListener() {
		
		@Override
		public void onPageSelected(int position) {
		}
		
		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
			Log.i("code", arg0+"=="+arg1+"=====onPageScrolled======"+arg2);
			
		}
		
		@Override
		public void onPageScrollStateChanged(int arg0) {
//			Log.i("code", arg0+"=====onPageScrolled======");
		}
	};
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
