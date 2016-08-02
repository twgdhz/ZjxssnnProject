package com.zjxfood.activity;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.widget.ImageView;

import com.umeng.analytics.MobclickAgent;


/**
 * 协议说明
 */

public class XieyiActivity extends AppActivity implements OnClickListener {

	private WebView mWebView;
	private ImageView mBackImage;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.xieyi_shuoming_layout);
		setImmerseLayout(findViewById(R.id.head_layout));
		init();
		mWebView.loadUrl("http://wx.zjxssnn.com/Common/Protocol");
	}

	private void init() {
		mWebView = (WebView) findViewById(R.id.xieyi_web_view);
		mBackImage = (ImageView) findViewById(R.id.xieyi_shuoming_info_image);

		mBackImage.setOnClickListener(this);
	}


	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.xieyi_shuoming_info_image:
			finish();
			break;

		default:
			break;
		}
	}
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		MobclickAgent.onResume(this);
	}
	
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		MobclickAgent.onPause(this);
	}
}
