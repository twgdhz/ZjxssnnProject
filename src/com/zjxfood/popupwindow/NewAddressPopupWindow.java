package com.zjxfood.popupwindow;

import com.project.util.DensityUtils;
import com.zjxfood.activity.R;
import com.zjxfood.adapter.NewAddressListAdapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

public class NewAddressPopupWindow extends PopupWindow {


	private ListView mListView;
	private View mView;
	
	public NewAddressPopupWindow(Activity context,OnItemClickListener mItemClickListener,NewAddressListAdapter adapter) {
		super(context);
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mView = inflater.inflate(R.layout.new_address_popup_list, null);
		mListView = (ListView) mView.findViewById(R.id.new_address_popup_list);
		//设置按钮监听
		mListView.setAdapter(adapter);
		mListView.setOnItemClickListener(mItemClickListener);
//		LayoutParams params = mListView.getLayoutParams();
//		params.width = DensityUtils.dp2px(context, 150);
//		mListView.setLayoutParams(params);
		//设置SelectPicPopupWindow的View
		this.setContentView(mView);
		//设置SelectPicPopupWindow弹出窗体的宽
		this.setWidth(DensityUtils.dp2px(context, 150));
		//设置SelectPicPopupWindow弹出窗体的高
		this.setHeight(LayoutParams.MATCH_PARENT);
		//设置SelectPicPopupWindow弹出窗体可点击
		this.setFocusable(true);
		//设置SelectPicPopupWindow弹出窗体动画效果
		this.setAnimationStyle(R.style.AnimTop);
		//实例化一个ColorDrawable颜色为半透明
//		ColorDrawable dw = new ColorDrawable(0xb0000000);
//		ColorDrawable dw = new ColorDrawable(R.color.touming);
		//设置SelectPicPopupWindow弹出窗体的背景
//		this.setBackgroundDrawable(dw);
		this.setBackgroundDrawable(new BitmapDrawable());
		//mMenuView添加OnTouchListener监听判断获取触屏位置如果在选择框外面则销毁弹出框
		mView.setOnTouchListener(new OnTouchListener() {
			public boolean onTouch(View v, MotionEvent event) {
				
				int height = mView.findViewById(R.id.new_address_popup_list).getTop();
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
