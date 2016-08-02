package com.zjxfood.popupwindow;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.PopupWindow;

import com.project.util.ScreenUtils;
import com.zjxfood.activity.R;
import com.zjxfood.adapter.CashMallTopAdapter;

import java.util.ArrayList;
import java.util.HashMap;

public class CashMoreMallPopup extends PopupWindow {
	private View mContentView;
	private GridView mGridView;
	private CashMallTopAdapter mAdapter;

	public CashMoreMallPopup(Activity context, ArrayList<HashMap<String,Object>> list, AdapterView.OnItemClickListener itemClickListener) {
		super(context);
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mContentView = inflater.inflate(R.layout.new_cash_more_popup, null);
		mGridView = (GridView) mContentView.findViewById(R.id.new_mall_popup_grid_view);
		mAdapter = new CashMallTopAdapter(context.getApplicationContext(),list);
		mGridView.setAdapter(mAdapter);
		mGridView.setOnItemClickListener(itemClickListener);
		//设置SelectPicPopupWindow的View
		this.setContentView(mContentView);
		//设置SelectPicPopupWindow弹出窗体的宽
		this.setWidth(ScreenUtils.getScreenWidth(context));
		//设置SelectPicPopupWindow弹出窗体的高
		this.setHeight(LayoutParams.MATCH_PARENT);
		//设置SelectPicPopupWindow弹出窗体可点击
		this.setFocusable(true);
		//设置SelectPicPopupWindow弹出窗体动画效果
		this.setAnimationStyle(R.style.AnimTop_miss);
		//实例化一个ColorDrawable颜色为半透明
		ColorDrawable dw = new ColorDrawable(0xb0000000);
		//设置SelectPicPopupWindow弹出窗体的背景
		this.setBackgroundDrawable(dw);
		//mMenuView添加OnTouchListener监听判断获取触屏位置如果在选择框外面则销毁弹出框
		mContentView.setOnTouchListener(new OnTouchListener() {
			
			public boolean onTouch(View v, MotionEvent event) {
				int height = mContentView.findViewById(R.id.new_mall_popup_grid_view).getTop();
				int bottom = mContentView.findViewById(R.id.new_mall_popup_grid_view).getBottom();
				int y=(int) event.getY();
				if(event.getAction()==MotionEvent.ACTION_UP){
					if(y<height || y>bottom){
						dismiss();
					}
				}				
				return true;
			}
		});

	}

}
