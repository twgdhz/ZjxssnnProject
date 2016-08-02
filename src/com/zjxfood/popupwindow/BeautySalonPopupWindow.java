package com.zjxfood.popupwindow;

import java.util.ArrayList;
import java.util.HashMap;

import com.zjxfood.activity.R;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

public class BeautySalonPopupWindow extends PopupWindow {


//	private Button btn_take_photo, btn_pick_photo, btn_cancel;
	private View mMenuView;
	private TextView mTitle,mContent;
	private Button mCheckDetailBtn;
	private ImageView mXImage;

	public BeautySalonPopupWindow(Activity context) {
		super(context);
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mMenuView = inflater.inflate(R.layout.popup_main_upmerchant_layout, null);
		mTitle = (TextView) mMenuView.findViewById(R.id.notice_popup_title_text);
		mContent = (TextView) mMenuView.findViewById(R.id.notice_popup_content_text);
		
//		mTitle.setText(mList.get(0).get("title").toString());
//		mContent.setText(mList.get(0).get("content").toString());
		mXImage = (ImageView) mMenuView.findViewById(R.id.popup_main_beauty_salon_xx);
		//取消按钮
		mXImage.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				//销毁弹出框
				dismiss();
			}
		});
		//设置按钮监听
		//设置SelectPicPopupWindow的View
		this.setContentView(mMenuView);
		//设置SelectPicPopupWindow弹出窗体的宽
		this.setWidth(LayoutParams.FILL_PARENT);
		//设置SelectPicPopupWindow弹出窗体的高
		this.setHeight(LayoutParams.MATCH_PARENT);
		//设置SelectPicPopupWindow弹出窗体可点击
		this.setFocusable(true);
		//设置SelectPicPopupWindow弹出窗体动画效果
//		this.setAnimationStyle(R.style.AnimBottom);
		//实例化一个ColorDrawable颜色为半透明
//		ColorDrawable dw = new ColorDrawable(0xb0000000);
		ColorDrawable dw = new ColorDrawable(R.color.transparent2);
		//设置SelectPicPopupWindow弹出窗体的背景
		this.setBackgroundDrawable(dw);
		//mMenuView添加OnTouchListener监听判断获取触屏位置如果在选择框外面则销毁弹出框
		mMenuView.setOnTouchListener(new OnTouchListener() {
			
			public boolean onTouch(View v, MotionEvent event) {
				int height = mMenuView.findViewById(R.id.beauty_salon_scroll_layout).getTop();
				int y=(int) event.getY();
				if(event.getAction()==MotionEvent.ACTION_UP){
					if(y<height){
						dismiss();
					}
				}				
				return true;
			}
		});

	}

}
