package com.zjxfood.activity;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.umeng.analytics.MobclickAgent;
import com.zjxfood.adapter.FragmentPagAdapter;
import com.zjxfood.application.ExitApplication;
import com.zjxfood.fragment.MallDetailFragment;
import com.zjxfood.fragment.MallIntroductionFragment;
import com.zjxfood.fragment.MallParameterFragment;

import java.util.ArrayList;

public class MallNewDetailActivity extends FragmentActivity implements OnClickListener{
	
	private ImageView mBackImage;
	private ViewPager mViewPager;
	private ArrayList<Fragment> mFragments;
	private FragmentPagAdapter mPagerAdapter;// 类型选择适配器
	private String mGid = "";
	private String str = "";
	private TextView mTitleText;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mall_new_detail_layout);
		setImmerseLayout(findViewById(R.id.head_layout));
		ExitApplication.getInstance().addActivity(this);
		// ExitApplication.getInstance().addAddressActivity(this);
		ExitApplication.getInstance().addMallActivity(this);
		init();
		MallDetailActivity.mFlag = "1";
		
		// 设置两页菜单
		if (!(str.equals("")) && !(mGid.equals(""))) {
		mFragments = new ArrayList<Fragment>();
		mFragments.add(new MallDetailFragment());
		mFragments.add(MallIntroductionFragment.newInstance(
				getApplicationContext(), str));
		mFragments.add(MallParameterFragment.newInstance(getApplicationContext(),mGid));
		mPagerAdapter = new FragmentPagAdapter(getSupportFragmentManager(),
				mFragments);
		mViewPager.setAdapter(mPagerAdapter);
//		mViewPager.setOnPageChangeListener(mPageChangeListener);
		}
	}
	
	private void init(){
//		mBackImage = (ImageView) findViewById(R.id.title_back_image);
		mViewPager = (ViewPager) findViewById(R.id.mall_detail_content_viewpager);
		mBackImage = (ImageView) findViewById(R.id.title_back_image);
		mTitleText = (TextView) findViewById(R.id.title_text);
		mTitleText.setText("产品介绍");
		mBackImage.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.title_back_image:
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
	protected void setImmerseLayout(View view) {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
			//透明状态栏
			getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//			//透明导航栏
			getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);

			int statusBarHeight = getStatusBarHeight(this.getBaseContext());
			view.setPadding(0, statusBarHeight, 0, 0);
		}
	}
	/**
	 * 用于获取状态栏的高度。 使用Resource对象获取（推荐这种方式）
	 *
	 * @return 返回状态栏高度的像素值。
	 */
	public static int getStatusBarHeight(Context context) {
		int result = 0;
		int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen",
				"android");
		if (resourceId > 0) {
			result = context.getResources().getDimensionPixelSize(resourceId);
		}
		return result;
	}
}
