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
import android.webkit.WebView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

public class NoticePopupWindow extends PopupWindow {
	private View mMenuView;
	private TextView btn_cancel,mTitle,mContent;
	private LinearLayout mLayout;
	private WebView mWebView;

	public NoticePopupWindow(Activity context,ArrayList<HashMap<String, Object>> mList) {
		super(context);
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mMenuView = inflater.inflate(R.layout.notice_popup_layout, null);
		mWebView = (WebView) mMenuView.findViewById(R.id.notice_popup_content_text2);
		
		mLayout = (LinearLayout) mMenuView.findViewById(R.id.notice_popup_layout);
		mTitle = (TextView) mMenuView.findViewById(R.id.notice_popup_title_text);
		mContent = (TextView) mMenuView.findViewById(R.id.notice_popup_content_text);
		if(mList.get(0).get("title")!=null){
		mTitle.setText(mList.get(0).get("title").toString());
		}if(mList.get(0).get("content")!=null){
//		mContent.setText(mList.get(0).get("content").toString());
		mWebView.loadDataWithBaseURL(null, mList.get(0).get("content").toString(), "text/html", "utf-8", null);
		}
		
		btn_cancel = (TextView) mMenuView.findViewById(R.id.notice_popup_cancel_text);
		//取消按钮
		btn_cancel.setOnClickListener(new OnClickListener() {
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
		ColorDrawable dw = new ColorDrawable(0xb0000000);
//		ColorDrawable dw = new ColorDrawable(R.color.transparent2);
		//设置SelectPicPopupWindow弹出窗体的背景
		this.setBackgroundDrawable(dw);
		LayoutParams params = mLayout.getLayoutParams();
		params.height = LayoutParams.WRAP_CONTENT;
		mLayout.setLayoutParams(params);
		//mMenuView添加OnTouchListener监听判断获取触屏位置如果在选择框外面则销毁弹出框
		mMenuView.setOnTouchListener(new OnTouchListener() {
			public boolean onTouch(View v, MotionEvent event) {
				int height = mMenuView.findViewById(R.id.notice_popup_layout).getTop();
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
