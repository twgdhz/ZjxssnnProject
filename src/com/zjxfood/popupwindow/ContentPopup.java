package com.zjxfood.popupwindow;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.webkit.WebView;
import android.widget.PopupWindow;
import android.widget.ScrollView;
import android.widget.TextView;

import com.project.util.DensityUtils;
import com.zjxfood.activity.R;

public class ContentPopup extends PopupWindow {


//	private Button btn_take_photo, btn_pick_photo, btn_cancel;
	private View mMenuView;
	private Context mContext;
	private WebView mWebView;
	private ScrollView mScrollView;
	private TextView mConfirmText;
	private TextView mTitle;

	public ContentPopup(Activity context, String title) {
		super(context);
		mContext = context;
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mMenuView = inflater.inflate(R.layout.content_popup_layout, null);
		mWebView = (WebView) mMenuView.findViewById(R.id.content_web_view);
		mWebView.loadUrl("http://wx.zjxssnn.com/Common/Protocol");
		mScrollView = (ScrollView) mMenuView.findViewById(R.id.content_popup_scroll);
//		mContentText.setMovementMethod(ScrollingMovementMethod.getInstance());
		LayoutParams params = mScrollView.getLayoutParams();
		params.height = DensityUtils.dp2px(mContext, 300);
		mScrollView.setLayoutParams(params);
		mConfirmText = (TextView) mMenuView.findViewById(R.id.content_popup_confirm);
		mTitle = (TextView) mMenuView.findViewById(R.id.content_popup_title);
		mTitle.setText(title);
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
		ColorDrawable dw = new ColorDrawable(0x20000000);
		//设置SelectPicPopupWindow弹出窗体的背景
		this.setBackgroundDrawable(dw);
		mConfirmText.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				dismiss();
			}
		});
		//mMenuView添加OnTouchListener监听判断获取触屏位置如果在选择框外面则销毁弹出框
		mMenuView.setOnTouchListener(new OnTouchListener() {
			
			public boolean onTouch(View v, MotionEvent event) {
				
				int height = mMenuView.findViewById(R.id.content_popup_scroll).getTop();
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
