package com.zjxfood.popupwindow;

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

import com.zjxfood.activity.R;

public class ServiceCommitmentPopupWindow extends PopupWindow {


//	private Button btn_take_photo, btn_pick_photo, btn_cancel;
	private View mMenuView;
	private TextView mText;
	private TextView mContent;
	private String source1 = "<font color='#333333'>2.货物签收当天如发现商品有破损立即拒签，联系</font><font color='#0376F5'>4000120012-8059</font><font color='#333333'>食尚男女物流部，如签收超过三天后再通知食尚男女物流部商品有破损概不退换。</font>";

	public ServiceCommitmentPopupWindow(Activity context,String msg) {
		super(context);
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mMenuView = inflater.inflate(R.layout.mall_service_commitment_layout, null);
		mContent = (TextView) mMenuView.findViewById(R.id.mall_service_commoitment_text1);
//		mContent2 = (TextView) mMenuView.findViewById(R.id.mall_service_commoitment_text2);
//		
//		mContent2.setText(Html.fromHtml(source1));
//		mContent2.setOnClickListener(clickListener);
		mText = (TextView) mMenuView.findViewById(R.id.mall_service_commoitment_confirm);
//		SharedPreferences sp = context.getApplicationContext()
//				.getSharedPreferences("商品详情服务承诺", context.MODE_PRIVATE);
//		if(sp!=null && sp.getString("Content", "")!=null && !sp.getString("Content", "").toString().equals("")){
//			mContent.setText(sp.getString("Content", ""));
//		}
		mContent.setText(msg);
		//取消按钮
		mText.setOnClickListener(new OnClickListener() {
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
//		this.setAnimationStyle(R.style.AnimZoom);
		//实例化一个ColorDrawable颜色为半透明
		ColorDrawable dw = new ColorDrawable(0xb0000000);
		//设置SelectPicPopupWindow弹出窗体的背景
		this.setBackgroundDrawable(dw);
		//mMenuView添加OnTouchListener监听判断获取触屏位置如果在选择框外面则销毁弹出框
		mMenuView.setOnTouchListener(new OnTouchListener() {
			
			public boolean onTouch(View v, MotionEvent event) {
				
				int height = mMenuView.findViewById(R.id.mall_service_commitment_content_layout).getTop();
				int bottom = mMenuView.findViewById(R.id.mall_service_commitment_content_layout).getBottom();
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
