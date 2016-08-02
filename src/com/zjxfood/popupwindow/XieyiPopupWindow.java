package com.zjxfood.popupwindow;

import java.io.InputStream;

import org.apache.http.util.EncodingUtils;

import com.project.util.DensityUtils;
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
import android.widget.PopupWindow;
import android.widget.ScrollView;
import android.widget.TextView;

public class XieyiPopupWindow extends PopupWindow {


//	private Button btn_take_photo, btn_pick_photo, btn_cancel;
	private View mMenuView;
	private TextView mAgree, mRefuse;
//	private TextView mContentText;
	private Context mContext;
	private ScrollView mScrollView;
	private WebView mWebView;
	
	public XieyiPopupWindow(Activity context,OnClickListener itemsOnClick) {
		super(context);
		mContext = context;
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mMenuView = inflater.inflate(R.layout.popup_reg_alert_layout, null);
		mAgree = (TextView) mMenuView.findViewById(R.id.popup_reg_xieyi_to_confirm_text);
		mRefuse = (TextView) mMenuView.findViewById(R.id.popup_reg_xieyi_cancel_cancel_text);
//		mContentText = (TextView) mMenuView.findViewById(R.id.change_head_location_text);
		mWebView = (WebView) mMenuView.findViewById(R.id.change_head_location_webview);
//		mContentText.setText(getFromAssets("xieyi.txt"));
		mWebView.loadUrl("http://wx.zjxssnn.com/Common/Protocol");
		
		mScrollView = (ScrollView) mMenuView.findViewById(R.id.popup_jh_alert_scroll_layout);
//		mContentText.setMovementMethod(ScrollingMovementMethod.getInstance());
		LayoutParams params = mScrollView.getLayoutParams();
		params.height = DensityUtils.dp2px(mContext, 300);
		mScrollView.setLayoutParams(params);
		
		//设置按钮监听
		mAgree.setOnClickListener(itemsOnClick);
		mRefuse.setOnClickListener(itemsOnClick);
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
				
				int height = mMenuView.findViewById(R.id.popup_xieyi_content_layout).getTop();
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
	//读取协议文件
		public String getFromAssets(String fileName) {
			String result = "";
			try {
				InputStream in = mContext.getResources().getAssets().open(fileName);
				// 获取文件的字节数
				int lenght = in.available();
				// 创建byte数组
				byte[] buffer = new byte[lenght];
				// 将文件中的数据读到byte数组中
				in.read(buffer);
				result = EncodingUtils.getString(buffer, "UTF-8");
			} catch (Exception e) {
				e.printStackTrace();
			}
			return result;
		}
}
