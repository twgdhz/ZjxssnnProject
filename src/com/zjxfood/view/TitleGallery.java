package com.zjxfood.view;

import com.zjxfood.activity.MerchantInfoActivity;
import com.zjxfood.activity.MerchantTitleImageActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.widget.Gallery;

/**
 * 商城推广图片画廊
 * @author lenovo
 *
 */
@SuppressWarnings("deprecation")
public class TitleGallery extends Gallery{
	public TitleGallery(Context context) {
		super(context);
	}
	public TitleGallery(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	private boolean isScrollingLeft(MotionEvent e1, MotionEvent e2) {
		return e2.getX() > e1.getX();
	}

	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
			float velocityY) {
		int keyCode = 0;
		if (e2.getX()-e1.getX()>20) {
			keyCode = KeyEvent.KEYCODE_DPAD_LEFT;
			Log.i("gallery", "===========left===========");
		} else if(e1.getX()-e2.getX()>20){
			keyCode = KeyEvent.KEYCODE_DPAD_RIGHT;
			Log.i("gallery", "===========right===========");
		}else if(e2.getX()-e1.getX()<20){
			keyCode = KeyEvent.KEYCODE_DPAD_DOWN;
			Log.i("gallery", "===========down===========");
		}
		onKeyDown(keyCode, null);
		return true;
	}
	@Override
	public boolean onDown(MotionEvent e) {
		Log.i("gallery", "===========down===========");
		return super.onDown(e);
	}
	
}
