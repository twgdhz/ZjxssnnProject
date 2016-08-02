/*******************************************************************************
 * Copyright 2011, 2012 Chris Banes.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/
package com.zjxfood.photoview.activity;

import java.util.ArrayList;

import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.ViewUtils;
import com.zjxfood.activity.R;
import com.zjxfood.photoview.PhotoView;
import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MenuItem.OnMenuItemClickListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;

/**
 */

public class ViewPagerActivity extends Activity implements OnClickListener {

	private static final String ISLOCKED_ARG = "isLocked";

	private ViewPager mViewPager;
	private MenuItem menuLockItem;
	private Bundle mBundle;
	private ArrayList<String> mArrayBitmaps;
	private Button mBackBtn;
	private int x = 1;
	private PhotoView mPhotoView;
	private int mPosition = 0;
	

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_view_pager);
//		ViewUtils.inject(this);
		init();
		mBundle = getIntent().getExtras();
		if (mBundle != null) {
			mArrayBitmaps = mBundle.getStringArrayList("list");
			mPosition = mBundle.getInt("position");
			handler.sendEmptyMessageDelayed(1, 0);
			Log.i("mArrayBitmaps", mArrayBitmaps+"=============");
		}
		if (savedInstanceState != null) {
			boolean isLocked = savedInstanceState.getBoolean(ISLOCKED_ARG,
					false);
			((HackyViewPager) mViewPager).setLocked(isLocked);
		}
		getmem_UNUSED(getApplicationContext());
	}

	private void init() {
		mBackBtn = (Button) findViewById(R.id.view_pager_bottom_back);
		mViewPager = (HackyViewPager) findViewById(R.id.view_pager);

		mBackBtn.setOnClickListener(this);
	}



	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.viewpager_menu, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		menuLockItem = menu.findItem(R.id.menu_lock);
		toggleLockBtnTitle();
		menuLockItem.setOnMenuItemClickListener(new OnMenuItemClickListener() {
			@Override
			public boolean onMenuItemClick(MenuItem item) {
				toggleViewPagerScrolling();
				toggleLockBtnTitle();
				return true;
			}
		});

		return super.onPrepareOptionsMenu(menu);
	}

	private void toggleViewPagerScrolling() {
		if (isViewPagerActive()) {
			((HackyViewPager) mViewPager).toggleLock();
		}
	}

	private void toggleLockBtnTitle() {
		boolean isLocked = false;
		if (isViewPagerActive()) {
			isLocked = ((HackyViewPager) mViewPager).isLocked();
		}
		String title = (isLocked) ? getString(R.string.menu_unlock)
				: getString(R.string.menu_lock);
		if (menuLockItem != null) {

			menuLockItem.setTitle(title);
		}
	}

	private boolean isViewPagerActive() {
		return (mViewPager != null && mViewPager instanceof HackyViewPager);
	}

	@Override
	protected void onSaveInstanceState(@NonNull Bundle outState) {
		if (isViewPagerActive()) {
			outState.putBoolean(ISLOCKED_ARG,
					((HackyViewPager) mViewPager).isLocked());
		}
		super.onSaveInstanceState(outState);
	}
	Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 1:
				mViewPager.setAdapter(new SamplePagerAdapter(
						getApplicationContext(), mArrayBitmaps));
				mViewPager.setCurrentItem(mPosition);
				break;

			default:
				break;
			}
		};
	};

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.view_pager_bottom_back:
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
	protected void onStop() {
		System.gc();
		super.onStop();
	}
	
	static class SamplePagerAdapter extends PagerAdapter {
		private Context mContext;
		private ArrayList<String> mPaths;
		private BitmapUtils mBitmapUtils;

		public SamplePagerAdapter(Context context, ArrayList<String> paths) {
			this.mContext = context;
			this.mPaths = paths;
			
			mBitmapUtils = new BitmapUtils(mContext);
		}

		@Override
		public int getCount() {
			return mPaths.size();
		}

		@Override
		public View instantiateItem(ViewGroup container, int position) {
			PhotoView photoView = new PhotoView(container.getContext());

			mBitmapUtils.display(photoView, mPaths.get(position));
			container.addView(photoView, LayoutParams.MATCH_PARENT,
					LayoutParams.MATCH_PARENT);
			
			return photoView;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			
			container.removeView((View) object);
		}

		@Override
		public boolean isViewFromObject(View view, Object object) {
			return view == object;
		}
	}
	 public static long getmem_UNUSED(Context mContext) {
	        long MEM_UNUSED;
		// 得到ActivityManager
	        ActivityManager am = (ActivityManager) mContext.getSystemService(Context.ACTIVITY_SERVICE);
		// 创建ActivityManager.MemoryInfo对象  

	        ActivityManager.MemoryInfo mi = new ActivityManager.MemoryInfo();
	        am.getMemoryInfo(mi);

		// 取得剩余的内存空间 

	        MEM_UNUSED = mi.availMem / 1024;
	        Log.i("MEM_UNUSED", MEM_UNUSED+"==========MEM_UNUSED============");
	        return MEM_UNUSED;
	    }
}
