package com.zjxfood.activity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;
import android.widget.ImageView;

import com.umeng.analytics.MobclickAgent;
import com.zjxfood.adapter.MerchantImageAdapter;

public class MerchantTitleImageActivity extends AppActivity {

	private ViewPager mViewPager;
	private ImageView[] mImageViews;
	private MerchantImageAdapter mImageAdapter;
	private Bitmap[] mBitmaps;
	private Bundle mBundle;
	private String[] mImagePath;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.merchant_title_image_gallery);
		setImmerseLayout(findViewById(R.id.head_layout));
		init();
		mBundle = getIntent().getExtras();
		if(mBundle!=null){
			mImagePath = mBundle.getStringArray("list");
		}
		
		mImageAdapter = new MerchantImageAdapter(
				getApplicationContext(), mImageViews,mImagePath);
		mViewPager.setAdapter(mImageAdapter);

	}

	private void init() {
		mViewPager = (ViewPager) findViewById(R.id.merchant_image_gallery);
	}

	Runnable runnable = new Runnable() {
		@Override
		public void run() {
			handler.sendEmptyMessageDelayed(1, 0);
		}
	};

	Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1:
				for (int i = 0; i < 5; i++) {
					ImageView imageView = new ImageView(getApplicationContext());
					mImageViews[i] = imageView;
					mImageViews[i].setImageBitmap(mBitmaps[i]);
				}
				handler.sendEmptyMessageDelayed(2, 0);
				break;

			case 2:
				mImageAdapter = new MerchantImageAdapter(
						getApplicationContext(), mImageViews,mImagePath);
				mViewPager.setAdapter(mImageAdapter);
				break;
			}
		};
	};

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		switch (keyCode) {
		case KeyEvent.KEYCODE_BACK:
			System.gc();
			finish();
			break;

		default:
			break;
		}
		return super.onKeyDown(keyCode, event);
	}
	@Override
	protected void onDestroy() {
		System.gc();
		super.onDestroy();
	}
	@Override
	protected void onResume() {
		super.onResume();
		MobclickAgent.onResume(this);
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		MobclickAgent.onPause(this);
	}
}
