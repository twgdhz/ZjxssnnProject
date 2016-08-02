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
import com.zjxfood.adapter.GoldAdapter;

public class GoldPopup extends PopupWindow {
//	private Button btn_take_photo, btn_pick_photo, btn_cancel;
	private View mMenuView;
	private ListView mListView;
	private String[] mTypeArrays;
	private GoldAdapter mPopCommListTypeAdapter;
	public GoldPopup(Activity context, AdapterView.OnItemClickListener itemsOnClick,float width) {
		super(context);
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mMenuView = inflater.inflate(R.layout.popupwindow_commodity_list_type, null);
		mListView = (ListView) mMenuView
				.findViewById(R.id.popupwindow_commodity_type_list);
		mTypeArrays = context.getResources().getStringArray(R.array.myGold);
		mPopCommListTypeAdapter = new GoldAdapter(
				context, mTypeArrays);
		mListView.setAdapter(mPopCommListTypeAdapter);
		mListView.setOnItemClickListener(itemsOnClick);
//		mOkText = (TextView) mMenuView.findViewById(R.id.mall_buy_ok);
//		mCancelText = (TextView) mMenuView.findViewById(R.id.mall_buy_cancel);
//		mMessageText = (TextView) mMenuView.findViewById(R.id.mall_detail_buy_content_text);
		//取消按钮

		//设置SelectPicPopupWindow的View
		this.setContentView(mMenuView);
		//设置SelectPicPopupWindow弹出窗体的宽
		this.setWidth((int) width);
		//设置SelectPicPopupWindow弹出窗体的高
		this.setHeight(LayoutParams.WRAP_CONTENT);
		//设置SelectPicPopupWindow弹出窗体可点击
		this.setFocusable(true);
		//设置SelectPicPopupWindow弹出窗体动画效果
		this.setAnimationStyle(R.style.AnimTop_miss);
		//实例化一个ColorDrawable颜色为半透明
		ColorDrawable dw = new ColorDrawable(0x00000000);
		//设置SelectPicPopupWindow弹出窗体的背景
		this.setBackgroundDrawable(dw);
		//mMenuView添加OnTouchListener监听判断获取触屏位置如果在选择框外面则销毁弹出框
		mMenuView.setOnTouchListener(new OnTouchListener() {
			
			public boolean onTouch(View v, MotionEvent event) {
				int height = mMenuView.findViewById(R.id.mall_jh_content_layou).getTop();
				int bottom = mMenuView.findViewById(R.id.mall_jh_content_layou).getBottom();
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

}
