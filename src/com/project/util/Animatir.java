package com.project.util;


import android.animation.AnimatorSet;
import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Point;
import android.view.Display;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.LayoutAnimationController;
import android.view.animation.TranslateAnimation;

@SuppressLint("NewApi")
public class Animatir {

	public final static int ANIMATION_DURATION_SLOW = 550;
	public final static int ANIMATION_DURATION = 200;
	public final static int DETAIL_LEFT_RANGE = -550;
	public final static int LEFT_WIDTH = 280;
	public final static int RIGHT_WIDTH = 675;
	public final static int SHOW_WIDTH = 652;
	public final static int ADD_WIDTH = 647;
	public final static int LEFT_RANGE = 73;
	public final static int SHADOW_WIDTH=23;
	public final static int INITIAL_COORD = 0;
	public final static int SCREENHEIGHT = 1;
	public final static int SCREENWIDTH = 0;
	public final static int TOUCH_RANGE_RIGHT=110;
	public final static int TOUCH_RANGE=10;
	public final static float RANGE_LEFT=92.5f;
	public final static int D_VALUE=25;
	public static boolean mSlided1,mSlided2,mSlided3;
	
	public final static int SCHEDULE_LEFT=1080;
	public static float RANG;
	public static Context CONTEXT;
	private static final int ANIMATION_CONTROLLER=300;
	
	private static float[] getScreenSize(Context context){
		Display display = ((Activity) context).getWindowManager().getDefaultDisplay();
		Point size=new Point();
		display.getSize(size);
		return new float[]{size.x,size.y};
	}
	
	public final static float getWidth() {
		return getScreenSize(CONTEXT)[SCREENWIDTH];
	}
	
	public final static float getHeight() {
		return getScreenSize(CONTEXT)[SCREENHEIGHT];
	}
	
	public final static float getLeftInt() {
		return UnitConvertUtil.dip2px(CONTEXT, LEFT_WIDTH);
	}

	public final static float getLeftOut() {
		return UnitConvertUtil.dip2px(CONTEXT, LEFT_RANGE);
	}

	public final static float getRightOut() {
		return getScreenSize(CONTEXT)[SCREENWIDTH]-UnitConvertUtil.dip2px(CONTEXT, RIGHT_WIDTH);
	}
	
	public final static float getDetailOut() {
		return getScreenSize(CONTEXT)[SCREENWIDTH]-UnitConvertUtil.dip2px(CONTEXT, SHOW_WIDTH);
	}
	
	public final static float getAddOut() {
		return UnitConvertUtil.dip2px(CONTEXT, 5);
	}

	public final static float getRightHalf() {
		return getRightOut()+(getLeftInt()-getLeftOut());
	}

	public final static float getDetailHalf() {
		return getDetailOut()+(getLeftInt()-getLeftOut());
	}

	public final static float getRightSmall() {
		return (getScreenSize(CONTEXT)[SCREENWIDTH]-getLeftInt())+getLeftOut()
				-UnitConvertUtil.dip2px(CONTEXT, SHADOW_WIDTH);
	}

	public final static float getDetailSmall() {
		return (getScreenSize(CONTEXT)[SCREENWIDTH]-getLeftInt())+getLeftOut();
	}

	public final static float getAddSmall() {
		return (getScreenSize(CONTEXT)[SCREENWIDTH]-getLeftInt())+getLeftOut();
	}

	public final static float getRightComplete() {
		return getScreenSize(CONTEXT)[SCREENWIDTH]-D_VALUE;
	}

	public final static float getDetailComplete() {
		return getScreenSize(CONTEXT)[SCREENWIDTH];
	}

	public final static float getAddComplete() {
		return getScreenSize(CONTEXT)[SCREENWIDTH];
	}

	public final static ValueAnimator leftOut(final View v,float first,float second){
		ValueAnimator va2 = slide(v,first,second,ANIMATION_DURATION_SLOW);
		mSlided2=true;
		return va2;
	}

	public final static ValueAnimator leftIn(final View v,float first,float second){
		ValueAnimator va2 = slide(v,first,second,ANIMATION_DURATION_SLOW);
		mSlided2=false;
		return va2;
	}

	public final static ValueAnimator rightOut(final View v,float first,float second){
		ValueAnimator va1 = slide(v,first,second,ANIMATION_DURATION_SLOW);
		mSlided1=true;
		return va1;
	}

	public final static ValueAnimator detailOut(final View v,float first,float second){
		ValueAnimator va1 = slide(v,first,second,ANIMATION_DURATION_SLOW);
		mSlided3=true;
		return va1;
	}

	public final static ValueAnimator rightIn(final View v,float first,float second){
		ValueAnimator va1 = slide(v,first,second,ANIMATION_DURATION_SLOW);
		mSlided1=false;
		return va1;
	}

	public final static ValueAnimator detailIn(final View v,float first,float second){
		ValueAnimator va1 = slide(v,first,second,ANIMATION_DURATION_SLOW);
		mSlided3=false;
		return va1;
	}
	
	public final static void animation(View view,float toX) {  
		slide(view,view.getX(),toX,ANIMATION_DURATION).start();  
	} 

	public final static ValueAnimator rightHalf(final View v,float first,float second){
		ValueAnimator va1 = slide(v,first,second,ANIMATION_DURATION_SLOW);
		return va1;
	}

	public final static ValueAnimator slide(final View v,float first,float second,long time){
		ValueAnimator valueAnimator = ValueAnimator.ofFloat(first,second);
		valueAnimator.setDuration(time);
		valueAnimator.addUpdateListener(new AnimatorUpdateListener() {
			@Override
			public void onAnimationUpdate(ValueAnimator animation) {
				v.setX((Float) animation.getAnimatedValue());
				v.invalidate();
			}
		});
		return valueAnimator;
	}
	
	public final static ValueAnimator slideDowan(final View v,float first,float second){
		ValueAnimator valueAnimator = ValueAnimator.ofFloat(first,second);
		valueAnimator.setDuration(ANIMATION_DURATION_SLOW);
		valueAnimator.addUpdateListener(new AnimatorUpdateListener() {
			@Override
			public void onAnimationUpdate(ValueAnimator animation) {
				v.setY((Float) animation.getAnimatedValue());
				v.invalidate();
			}
		});
		return valueAnimator;
	}

	 public final static AnimatorSet animatir(ValueAnimator va1,ValueAnimator va2){
		AnimatorSet animatorSet=new AnimatorSet();
		animatorSet.play(va1).with(va2);
		animatorSet.start();
		return animatorSet;
	}
	
	public final static AnimatorSet animatir(ValueAnimator va1,ValueAnimator va2,ValueAnimator va3){
		AnimatorSet animatorSet=new AnimatorSet();
		animatorSet.play(va1).with(va2).with(va3);
		animatorSet.start();
		return animatorSet;
	}
	
	public static LayoutAnimationController getListAnim() {
		AnimationSet set = new AnimationSet(true);
		Animation animation = new AlphaAnimation(0.0f, 1.0f);
		animation.setDuration(ANIMATION_CONTROLLER);
		set.addAnimation(animation);

		animation = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f,
				Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,
				-1.0f, Animation.RELATIVE_TO_SELF, 0.0f);
		animation.setDuration(500);
		set.addAnimation(animation);
		LayoutAnimationController controller = new LayoutAnimationController(
				set, 0.5f);
		return controller;
	}
}

