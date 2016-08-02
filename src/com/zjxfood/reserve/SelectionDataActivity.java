package com.zjxfood.reserve;

import java.util.ArrayList;

import com.umeng.analytics.MobclickAgent;
import com.zjxfood.activity.R;
import com.zjxfood.adapter.FragmentPagAdapter;
import com.zjxfood.application.ExitApplication;
import com.zjxfood.fragment.SelectionDataFragment1;
import com.zjxfood.fragment.SelectionDataFragment2;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;

public class SelectionDataActivity extends FragmentActivity implements
		OnClickListener {

	private ViewPager mViewPager;
	private ArrayList<Fragment> mFragments;
	private FragmentPagAdapter mPagerAdapter;// 类型选择适配器
	private Button mChoseDateBtn;
	private ImageView mBackImage;
	public static String selectDate = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.select_date_layout);
		ExitApplication.getInstance().addActivity(this);
		ExitApplication.getInstance().addMyCommodityActivity(this);
		init();

		// 设置两页菜单
		mFragments = new ArrayList<Fragment>();
		mFragments.add(new SelectionDataFragment1());
		mFragments.add(new SelectionDataFragment2());
		mPagerAdapter = new FragmentPagAdapter(getSupportFragmentManager(),
				mFragments);
		mViewPager.setAdapter(mPagerAdapter);
		mViewPager.setOnPageChangeListener(mPageChangeListener);
	}

	private void init() {
		mViewPager = (ViewPager) findViewById(R.id.select_date_viewpager);
		mChoseDateBtn = (Button) findViewById(R.id.select_date_btn);
		mBackImage = (ImageView) findViewById(R.id.select_date_back_info_image);

		mChoseDateBtn.setOnClickListener(this);
		mBackImage.setOnClickListener(this);
	}

	OnPageChangeListener mPageChangeListener = new OnPageChangeListener() {
		@Override
		public void onPageSelected(int position) {
			switch (position) {
			case 0:
				Log.i("position", position + "======0=====");
				((SelectionDataFragment2) mFragments.get(1)).nofityAdapter();
				selectDate = "";
				break;

			case 1:
				((SelectionDataFragment1) mFragments.get(0)).nofityAdapter();
				Log.i("position", position + "=====1======");
				selectDate = "";
				break;
			}
		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
		}

		@Override
		public void onPageScrollStateChanged(int arg0) {
		}
	};

	@Override
	public void onClick(View v) {
		Intent intent = new Intent();
		switch (v.getId()) {
		case R.id.select_date_btn:
			intent.setClass(getApplicationContext(), ReserveUserActivity.class);
			startActivity(intent);
			break;
		// 返回
		case R.id.select_date_back_info_image:
			finish();
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
