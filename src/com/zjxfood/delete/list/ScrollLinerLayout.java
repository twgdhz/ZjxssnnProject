package com.zjxfood.delete.list;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.Scroller;

public class ScrollLinerLayout extends LinearLayout {

	public ScrollLinerLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	private void init(Context context) {
		mScroller = new Scroller(context);
	}

	private Scroller mScroller;
	private boolean pressed = true;

	public void onDown() {
		if (!mScroller.isFinished()) {
			mScroller.abortAnimation();
		}
	}

	@Override
	public void setPressed(boolean pressed) {
		if (this.pressed)
			super.setPressed(pressed);
		else{
			super.setPressed(this.pressed);
		}
	}

	public void setSingleTapUp(boolean pressed) {
		this.pressed = pressed;
	}

	@Override
	public void computeScroll() {
		if (mScroller.computeScrollOffset()) {
			scrollTo(mScroller.getCurrX(), 0);
			postInvalidate();
		}
	}

	public int getToX() {
		return mScroller.getCurrX();
	}

	public void snapToScreen(int whichScreen) {
		mScroller.startScroll(whichScreen, 0, 0, 0, 50);
		invalidate();

	}

}
