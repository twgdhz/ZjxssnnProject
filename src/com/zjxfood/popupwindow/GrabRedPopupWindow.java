package com.zjxfood.popupwindow;

import com.lidroid.xutils.BitmapUtils;
import com.project.util.BitmapUtilSingle;
import com.project.util.ScreenUtils;
import com.zjxfood.activity.R;
import com.zjxfood.common.Constants;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class GrabRedPopupWindow extends PopupWindow {
//	private Button btn_take_photo, btn_pick_photo, btn_cancel;
	private View mMenuView;
	private TextView mJhText;
	private RelativeLayout mContentLayout;
	private ImageView mReabImage;
	private ImageView mXImage;
	private ImageView mUserImage;
	private TextView mUserName;
	private BitmapUtils mBitmapUtils;
	
	public GrabRedPopupWindow(Context mContext,OnClickListener clickListener,String path,String name) {
		super(mContext);
		LayoutInflater inflater = (LayoutInflater) mContext
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mBitmapUtils = BitmapUtilSingle
				.getBitmapInstance(mContext);
		mBitmapUtils.configDefaultLoadFailedImage(mContext.getResources().getDrawable(R.drawable.hongbao_faile));
		mMenuView = inflater.inflate(R.layout.grab_red_view_layout, null);
		mUserImage = (ImageView) mMenuView.findViewById(R.id.grab_user_head_touxiang_image);
		mUserName = (TextView) mMenuView.findViewById(R.id.grab_red_name);
		mXImage = (ImageView) mMenuView.findViewById(R.id.grab_red_content_right_x);
		mReabImage = (ImageView) mMenuView.findViewById(R.id.grab_red_image);
		LayoutParams params = mReabImage.getLayoutParams();
		params.height = (int) (ScreenUtils.getScreenWidth(mContext)*0.23);
		params.width = (int) (ScreenUtils.getScreenWidth(mContext)*0.23);
		mReabImage.setLayoutParams(params);
		mContentLayout = (RelativeLayout) mMenuView.findViewById(R.id.grab_red_content_layout);
		params = mContentLayout.getLayoutParams();
		params.height = (int) (ScreenUtils.getScreenHeight(mContext)*0.7);
		params.width = (int) (ScreenUtils.getScreenWidth(mContext)*0.85);
		mContentLayout.setLayoutParams(params);
		startAnim(mContentLayout);
		mBitmapUtils.display(mUserImage, path);
		mUserName.setText(name);
		mReabImage.setOnClickListener(clickListener);
		mXImage.setOnClickListener(clickListener);
		//设置SelectPicPopupWindow的View
		this.setContentView(mMenuView);
		//设置SelectPicPopupWindow弹出窗体的宽
		this.setWidth(LayoutParams.FILL_PARENT);
		//设置SelectPicPopupWindow弹出窗体的高
		this.setHeight(LayoutParams.FILL_PARENT);
		//设置SelectPicPopupWindow弹出窗体可点击
		this.setFocusable(true);
		//设置SelectPicPopupWindow弹出窗体动画效果
//		this.setAnimationStyle(R.style.AnimTop_miss);
		//实例化一个ColorDrawable颜色为半透明
		ColorDrawable dw = new ColorDrawable(0xb0000000);
		//设置SelectPicPopupWindow弹出窗体的背景
		this.setBackgroundDrawable(dw);
		//mMenuView添加OnTouchListener监听判断获取触屏位置如果在选择框外面则销毁弹出框
		mMenuView.setOnTouchListener(new OnTouchListener() {
			
			public boolean onTouch(View v, MotionEvent event) {
				int height = mMenuView.findViewById(R.id.grab_red_content_layout).getTop();
				int bottom = mMenuView.findViewById(R.id.grab_red_content_layout).getBottom();
				int y=(int) event.getY();
				if(event.getAction()==MotionEvent.ACTION_UP){
					if(y<height || y>bottom){
//						dismiss();
					}
				}				
				return true;
			}
		});

	}
	private void startAnim(final View view){
		ObjectAnimator anim = ObjectAnimator//  
	            .ofFloat(view, "zjx", 0.0F,  1.0F)//  
	            .setDuration(400);//  
	    anim.start();  
	    anim.addUpdateListener(new AnimatorUpdateListener()  
	    {  
	        @Override  
	        public void onAnimationUpdate(ValueAnimator animation)  
	        {  
	            float cVal = (Float) animation.getAnimatedValue();  
	            view.setAlpha(cVal);  
	            view.setScaleX(cVal);  
	            view.setScaleY(cVal);  
	        }  
	    });  
	}
}
