package com.zjxfood.cashindiana;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.widget.ImageView;

import com.zjxfood.activity.AppActivity;
import com.zjxfood.activity.R;

public class CashIndianRuleActivity extends AppActivity implements OnClickListener{

	private WebView mWebView;
	private ImageView mBackImage;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.indiana_detail_rule_layout);
		setImmerseLayout(findViewById(R.id.head_layout));
		init();
		mWebView.loadUrl("http://api.zjxssnn.com/rules/car.html");
	}
	
	private void init(){
		mWebView = (WebView) findViewById(R.id.indian_rule_web_view);
		mBackImage = (ImageView) findViewById(R.id.indiana_detail_rule_back_info_image);
		
		mBackImage.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.indiana_detail_rule_back_info_image:
			finish();
			break;

		default:
			break;
		}
	}
}
