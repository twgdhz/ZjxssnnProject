package com.zjxfood.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;

public class FirstStartViewPagerView extends ViewPager {
	private boolean isCanScroll = true;
	private float x;
	private float moveX;

	public FirstStartViewPagerView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	public FirstStartViewPagerView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// init();
	}

	public void setScanScroll(boolean isCanScroll) {
		this.isCanScroll = isCanScroll;
	}

	@Override
	public void scrollTo(int x, int y) {
		
			super.scrollTo(x, y);
		
	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		switch (ev.getAction()) {
		case MotionEvent.ACTION_DOWN:
			x = ev.getX();
			break;

		case MotionEvent.ACTION_MOVE:
			moveX = ev.getX();
			if (moveX - x < 0) {
				Log.e("HomeFragment", "向左滑动");
				isCanScroll = true;
				return true;
			}
			break;
		case MotionEvent.ACTION_UP:
			
			break;
		}
		return super.dispatchTouchEvent(ev);
	}
}
