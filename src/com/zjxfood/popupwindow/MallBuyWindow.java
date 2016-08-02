package com.zjxfood.popupwindow;

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
import android.widget.PopupWindow;
import android.widget.TextView;

public class MallBuyWindow extends PopupWindow {
//	private Button btn_take_photo, btn_pick_photo, btn_cancel;
	private View mMenuView;
	private TextView mOkText, mCancelText;
	private TextView mMessageText;
	public MallBuyWindow(Activity context,OnClickListener itemsOnClick,String message) {
		super(context);
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mMenuView = inflater.inflate(R.layout.mall_detail_buy_popup, null);
		mOkText = (TextView) mMenuView.findViewById(R.id.mall_buy_ok);
		mCancelText = (TextView) mMenuView.findViewById(R.id.mall_buy_cancel);
		mMessageText = (TextView) mMenuView.findViewById(R.id.mall_detail_buy_content_text);
		//取消按钮
		mCancelText.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				//销毁弹出框
				dismiss();
			}
		});
		if(!message.equals("")){
			mMessageText.setText("购买该商品需要支付快递费：￥"+message);
		}
		
		//设置按钮监听
		mOkText.setOnClickListener(itemsOnClick);
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
