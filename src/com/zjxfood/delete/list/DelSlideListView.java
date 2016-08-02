package com.zjxfood.delete.list;

import com.zjxfood.activity.R;

import android.content.Context;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ListView;

public class DelSlideListView extends ListView implements
		GestureDetector.OnGestureListener, View.OnTouchListener {

	private GestureDetector mDetector;
	private OnDeleteListioner mOnDeleteListioner;
	private int position;
	private float velocityX, velocityY;

	private ListViewonSingleTapUpListenner thisonSingleTapUpListenner;

	public DelSlideListView(Context context) {
		super(context);
		init(context);
	}

	public DelSlideListView(Context context, AttributeSet att) {
		super(context, att);

		init(context);
	}

	public void setDeleteListioner(OnDeleteListioner mOnDeleteListioner) {
		this.mOnDeleteListioner = mOnDeleteListioner;
	}

	public void setSingleTapUpListenner(
			ListViewonSingleTapUpListenner thisonSingleTapUpListenner) {
		this.thisonSingleTapUpListenner = thisonSingleTapUpListenner;
	}

	private int standard_touch_target_size = 0;
	private float mLastMotionX;
	public boolean deleteView = false;
	private ScrollLinerLayout mScrollLinerLayout;
	private boolean scroll = false;
	private int pointToPosition;
	private boolean listViewMoving;
	private boolean delAll = false;
	public boolean isLongPress = false;

	public boolean isDelAll() {
		return delAll;
	}

	public void setDelAll(boolean delAll) {
		this.delAll = delAll;
	}

	private void init(Context mContext) {
		mDetector = new GestureDetector(mContext, this);
		mDetector.setIsLongpressEnabled(false);
		standard_touch_target_size = (int) getResources().getDimension(
				R.dimen.delete_action_len);
		this.setOnTouchListener(this);
	}

	public boolean onDown(MotionEvent e) {
		if (thisonSingleTapUpListenner != null) {
			thisonSingleTapUpListenner.onSingleTapUp();
		}
		mLastMotionX = e.getX();
		pointToPosition = this.pointToPosition((int) e.getX(), (int) e.getY());
		final int p = pointToPosition - this.getFirstVisiblePosition();
		if (mScrollLinerLayout != null) {
			mScrollLinerLayout.onDown();
			mScrollLinerLayout.setSingleTapUp(true);
		}
		if (deleteView && p != position) {
			deleteView = false;
			if (mScrollLinerLayout != null) {
				mScrollLinerLayout.snapToScreen(0);
				mScrollLinerLayout.setSingleTapUp(false);
			}
			position = p;
			scroll = false;
			return true;
		}
		isLongPress = false;
		position = p;
		scroll = false;
		listViewMoving = false;
		return false;
	}

	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
			float velocityY) {
		this.velocityX = velocityX;
		this.velocityY = velocityY;
		return false;
	}

	public void onLongPress(MotionEvent e) {
	}

	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
			float distanceY) {
		if (listViewMoving && !scroll) {
			if (mScrollLinerLayout != null)
				mScrollLinerLayout.snapToScreen(0);
			return false;
		} else if (scroll) {
			if (mScrollLinerLayout != null) {
				int deltaX = (int) (mLastMotionX - e2.getX());
				if (deleteView) {
					deltaX += standard_touch_target_size;
				}
				if (deltaX >= 0 && deltaX <= standard_touch_target_size) {
					mScrollLinerLayout.scrollBy(
							deltaX - mScrollLinerLayout.getScrollX(), 0);
				}
			}
		} else {
			if (Math.abs(distanceX) > Math.abs(distanceY)) {
				try {
					final int pointToPosition1 = this.pointToPosition(
							(int) e2.getX(), (int) e2.getY());
					final int p1 = pointToPosition1
							- this.getFirstVisiblePosition();
					if (p1 == position && mOnDeleteListioner.isCandelete(p1)) {
						mScrollLinerLayout = (ScrollLinerLayout) this
								.getChildAt(p1);
						if (mScrollLinerLayout != null) {
							int deltaX = (int) (mLastMotionX - e2.getX());
							if (deleteView) {
								deltaX += standard_touch_target_size;
							}
							if (deltaX >= 0
									&& deltaX <= standard_touch_target_size
									&& Math.abs(distanceY) < 5) {
								isLongPress = true;
								scroll = true;
								listViewMoving = false;
								mScrollLinerLayout.setSingleTapUp(false);
								mScrollLinerLayout.scrollBy(
										(int) (e1.getX() - e2.getX()), 0);

							}
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		if (scroll) {
			return true;
		}
		return false;
	}

	public void onShowPress(MotionEvent e) {
	}

	public boolean onSingleTapUp(MotionEvent e) {
		if (deleteView) {
			position = -1;
			deleteView = false;
			mScrollLinerLayout.snapToScreen(0);
			scroll = false;
			return true;
		}
		return false;
	}

	public void setScroll(boolean b) {
		listViewMoving = b;

	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (scroll || deleteView) {
			return true;
		}
		return super.onTouchEvent(event);
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		if (isDelAll()) {
			return false;
		} else {
			if (event.getAction() == MotionEvent.ACTION_UP
					|| event.getAction() == MotionEvent.ACTION_CANCEL) {
				int deltaX2 = (int) (mLastMotionX - event.getX());
				if (scroll) {
					if (!deleteView
							&& deltaX2 >= standard_touch_target_size / 2) {
						mScrollLinerLayout
								.snapToScreen(standard_touch_target_size);
						position = pointToPosition
								- this.getFirstVisiblePosition();
						deleteView = true;
					} else {
						position = -1;
						deleteView = false;
						mScrollLinerLayout.snapToScreen(0);
					}
					scroll = false;
					return true;
				}/*
				 * else if (Math.abs(velocityX) > Math.abs(velocityY) && deltaX2
				 * < -80) { mOnDeleteListioner.onBack(); return false; }
				 */
			}
			return mDetector.onTouchEvent(event);
		}

	}

	public void deleteItem() {
		position = -1;
		deleteView = false;
		scroll = false;
		if (mScrollLinerLayout != null) {
			mScrollLinerLayout.snapToScreen(0);
		}
	}
}
