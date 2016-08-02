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

public class MerchantPayPopupWindow extends PopupWindow {
//	private Button btn_take_photo, btn_pick_photo, btn_cancel;
	private View mMenuView;
	private TextView mName, mPrice;
	private TextView mPay,mRecharge;
	private TextView mShowText;
	public MerchantPayPopupWindow(Activity context,OnClickListener itemsOnClick,String name,float price,String allPrice,String currency) {
		super(context);
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mMenuView = inflater.inflate(R.layout.merchant_pay_popup, null);
		mName = (TextView) mMenuView.findViewById(R.id.merchant_pay_name);
		mPrice = (TextView) mMenuView.findViewById(R.id.merchant_pay_price);
		mPay = (TextView) mMenuView.findViewById(R.id.merchant_currency_confirm_pay);
		mRecharge = (TextView) mMenuView.findViewById(R.id.merchant_currency_confirm_recharge);
		mShowText = (TextView) mMenuView.findViewById(R.id.merchant_pay_price_show);
		//取消按钮
		mName.setText(name);
		mPrice.setText("￥"+price);
		//设置按钮监听
		mPay.setOnClickListener(itemsOnClick);
		mRecharge.setOnClickListener(itemsOnClick);
		mShowText.setText("总金额：￥"+allPrice+"\t余额抵扣：￥"+currency);
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
