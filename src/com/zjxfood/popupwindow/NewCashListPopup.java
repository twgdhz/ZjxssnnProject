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
import android.widget.ListView;
import android.widget.PopupWindow;

import com.zjxfood.activity.R;
import com.zjxfood.adapter.NewCashListPopAdapter;

public class NewCashListPopup extends PopupWindow {
//	private Button btn_take_photo, btn_pick_photo, btn_cancel;
	private View mMenuView;
	private ListView mListView;
	private NewCashListPopAdapter mAdapter;
//	private int mPosition = 0;

	public NewCashListPopup(Activity context, int position, String[] list, AdapterView.OnItemClickListener listener) {
		super(context);
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mMenuView = inflater.inflate(R.layout.new_cash_list_popup, null);
		mListView = (ListView) mMenuView.findViewById(R.id.cash_popup_list);
		mAdapter = new NewCashListPopAdapter(context.getApplicationContext(),list,position);
		mListView.setAdapter(mAdapter);
		mListView.setOnItemClickListener(listener);


		//设置SelectPicPopupWindow的View
		this.setContentView(mMenuView);
		//设置SelectPicPopupWindow弹出窗体的宽
		this.setWidth(LayoutParams.FILL_PARENT);
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
		mMenuView.setOnTouchListener(new OnTouchListener() {
			
			public boolean onTouch(View v, MotionEvent event) {
				int height = mMenuView.findViewById(R.id.cash_popup_list).getTop();
				int bottom = mMenuView.findViewById(R.id.cash_popup_list).getBottom();
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
