package com.zjxfood.activity;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * 竞拍列表页面
 * @author zjx
 *
 */
public class AuctionDetailActivity extends AppActivity implements OnClickListener{

	private WebView mWebView;
	private String mDetail;
	private Bundle mBundle;
	private ImageView mBackImage;//返回按钮
	private TextView mTitleText;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.auction_detail_web_layout);
		setImmerseLayout(findViewById(R.id.head_layout));
		init();
		mBundle = getIntent().getExtras();
		if(mBundle!=null){
			mDetail = mBundle.getString("detail");
			mWebView.loadDataWithBaseURL(null, mDetail, "text/html", "utf-8", null);
		}
	}
	
	private void init(){
		mBackImage = (ImageView) findViewById(R.id.title_back_image);
		mTitleText = (TextView) findViewById(R.id.title_text);
		mTitleText.setText("商品介绍");
		mWebView = (WebView) findViewById(R.id.auction_web_view);
		
		mBackImage.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.title_back_image:
			finish();
			break;

		default:
			break;
		}
	}
}
