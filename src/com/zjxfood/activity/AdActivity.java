package com.zjxfood.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class AdActivity extends AppActivity implements OnClickListener {

	private WebView mWebView;
	private Bundle mBundle;
	private String url, mContent,mId;
	private ImageView mBackImage;
	private Button mButton;
	private String mTitle;
	private TextView mTitleText;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ad_web_layout);
		setImmerseLayout(findViewById(R.id.head_layout));
		init();
		mBundle = getIntent().getExtras();
		if (mBundle != null) {
			url = mBundle.getString("url");
			mContent = mBundle.getString("content");
			mId = mBundle.getString("id");
			Log.i("url", url+"========="+mId);
			if(mId.equals("2") || mId.equals("3")){
				mButton.setVisibility(View.VISIBLE);
			}else{
				mButton.setVisibility(View.GONE);
			}
			mTitle = mBundle.getString("Title");
			mTitleText.setText(mTitle);
		}
		if (url != null) {
			mWebView.loadUrl(url);
			mWebView.setWebViewClient(new WebViewClient() {
				@Override
				public boolean shouldOverrideUrlLoading(WebView view, String url) {
					// TODO Auto-generated method stub
					// 返回值是true的时候控制去WebView打开，为false调用系统浏览器或第三方浏览器
					view.loadUrl(url);
					return true;
				}
			});
		} else if (mContent != null) {
			mWebView.loadDataWithBaseURL(null, mContent, "text/html", "utf-8",
					null);
		}
	}

	private void init() {
		mWebView = (WebView) findViewById(R.id.ad_web_view);
		mBackImage = (ImageView) findViewById(R.id.ad_web_back_info_image);
		mButton = (Button) findViewById(R.id.ad_web_ruzhu_btn);
		mTitleText = (TextView) findViewById(R.id.ad_web_bank_info_text);

		mButton.setOnClickListener(this);
		mBackImage.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		Intent intent = new Intent();
		switch (v.getId()) {
		case R.id.ad_web_back_info_image:
			finish();
			break;

		case R.id.ad_web_ruzhu_btn:
			intent.setClass(getApplicationContext(),
					MerchantSettledActivity.class);
			startActivity(intent);
			break;
		}
	}
}
