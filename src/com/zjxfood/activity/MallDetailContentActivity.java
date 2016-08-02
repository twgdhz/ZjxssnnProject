package com.zjxfood.activity;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zjxfood.adapter.FragmentPagAdapter;
import com.zjxfood.fragment.MallIntroductionFragment;
import com.zjxfood.fragment.MallParameterFragment;

import java.util.ArrayList;
import java.util.HashMap;
/**
 * 商城商品详情内容
 * @author zjx
 *
 */
public class MallDetailContentActivity extends FragmentActivity implements
		OnClickListener {

	private ViewPager mViewPager;
	private ArrayList<Fragment> mFragments;
	private FragmentPagAdapter mPagerAdapter;// 类型选择适配器
	private Bundle mBundle;
	private String str = "";
	private View mLeftView, mRightView;
	private ArrayList<HashMap<String, Object>> mList;
	private String mGid = "";
	private LinearLayout mLeftLayout, mRightLayout;
	private ImageView mBackImage;
	private TextView mTitleText;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mall_detail_content_layout);
		setImmerseLayout(findViewById(R.id.head_layout));
		init();
		mBundle = getIntent().getExtras();
		if (mBundle != null) {
			str = mBundle.getString("detail");
			mGid = mBundle.getString("gid");
		}
		// 设置两页菜单
		mFragments = new ArrayList<Fragment>();
		
		if (!(str.equals("")) && !(mGid.equals(""))) {
			mFragments.add(MallIntroductionFragment.newInstance(
					getApplicationContext(), str));
			mFragments.add(MallParameterFragment.newInstance(getApplicationContext(),mGid));
			mPagerAdapter = new FragmentPagAdapter(getSupportFragmentManager(),
					mFragments);
			mViewPager.setAdapter(mPagerAdapter);
			mViewPager.setOnPageChangeListener(mPageChangeListener);
		}
	}

	private void init() {
		mViewPager = (ViewPager) findViewById(R.id.mall_detail_content_viewpager);
		mLeftView = findViewById(R.id.mall_content_left_view);
		mRightView = findViewById(R.id.mall_content_right_view);
		mLeftLayout = (LinearLayout) findViewById(R.id.mall_content_left_layout);
		mRightLayout = (LinearLayout) findViewById(R.id.mall_content_right_layout);
//		mBackImage = (ImageView) findViewById(R.id.title_back_image);
		mBackImage = (ImageView) findViewById(R.id.title_back_image);
		mTitleText = (TextView) findViewById(R.id.title_text);
		mTitleText.setText("产品介绍");

		mLeftLayout.setOnClickListener(this);
		mRightLayout.setOnClickListener(this);
		mBackImage.setOnClickListener(this);
	}

	OnPageChangeListener mPageChangeListener = new OnPageChangeListener() {
		@Override
		public void onPageSelected(int position) {
			switch (position) {
			case 0:
				mLeftView.setVisibility(View.VISIBLE);
				mRightView.setVisibility(View.GONE);
				break;

			case 1:
				mLeftView.setVisibility(View.GONE);
				mRightView.setVisibility(View.VISIBLE);
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
		switch (v.getId()) {
		case R.id.mall_content_left_layout:
			mViewPager.setCurrentItem(0);
			mLeftView.setVisibility(View.VISIBLE);
			mRightView.setVisibility(View.GONE);
			break;

		case R.id.mall_content_right_layout:
			mViewPager.setCurrentItem(1);
			mLeftView.setVisibility(View.GONE);
			mRightView.setVisibility(View.VISIBLE);
			break;
		case R.id.title_back_image:
			finish();
			break;
		}
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
