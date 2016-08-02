package com.zjxfood.popupwindow;

import com.project.util.ScreenUtils;
import com.zjxfood.activity.R;
import com.zjxfood.adapter.PopMallTypeAdapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

public class MallTwoPopupWindow extends PopupWindow {
//	private Button btn_take_photo, btn_pick_photo, btn_cancel;
	private View mMenuView;
	private ListView mListView;

	public MallTwoPopupWindow(Activity context,PopMallTypeAdapter adapter) {
		super(context);
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mMenuView = inflater.inflate(R.layout.popup_mall_list_two_type, null);
		mListView = (ListView) mMenuView.findViewById(R.id.popup_mall_two_type_list);
		mListView.setAdapter(adapter);
		//设置按钮监听
		//设置SelectPicPopupWindow的View
		this.setContentView(mMenuView);
		//设置SelectPicPopupWindow弹出窗体的宽
		this.setWidth(ScreenUtils.getScreenWidth(context) / 3);
		//设置SelectPicPopupWindow弹出窗体的高
		this.setHeight(LayoutParams.WRAP_CONTENT);
		//设置SelectPicPopupWindow弹出窗体可点击
		this.setFocusable(true);
		//设置SelectPicPopupWindow弹出窗体动画效果
		this.setAnimationStyle(R.style.AnimTop_miss);
		//实例化一个ColorDrawable颜色为半透明
//		ColorDrawable dw = new ColorDrawable(0xb0000000);
		//设置SelectPicPopupWindow弹出窗体的背景
//		this.setBackgroundDrawable(dw);
		//mMenuView添加OnTouchListener监听判断获取触屏位置如果在选择框外面则销毁弹出框
//		mMenuView.setOnTouchListener(new OnTouchListener() {
//			public boolean onTouch(View v, MotionEvent event) {
//				int height = mMenuView.findViewById(R.id.pop_layout).getTop();
//				int y=(int) event.getY();
//				if(event.getAction()==MotionEvent.ACTION_UP){
//					if(y<height){
//						dismiss();
//					}
//				}				
//				return true;
//			}
//		});

	}

}
