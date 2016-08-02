package com.zjxfood.activity;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.widget.ImageView;

import com.umeng.analytics.MobclickAgent;

public class MallIntroductionActivity extends AppActivity implements OnClickListener{
	
	private ImageView mBackImage;
	private WebView mWebView;
	private String source = "<p><strong>这是</strong>一个<span style=\"color: rgb(255, 0, 0);\">测试商品</span>，<span style=\"text-decoration: underline;\">包含</span>了各<em>种样式</em>，请<span style=\"font-family: 微软雅黑, &#39;Microsoft YaHei&#39;;\">测试</span>显示是否正确。</p><ul class=\" list-paddingleft-2\" style=\"list-style-type: square;\"><li><p>这个一个项目</p></li><li><p>这是第二项<br/></p></li></ul><p><img src=\"http://open.hexnews.com/content/appimages/sp/tw/xhtw/2/TB2saPCaXXXXXX.XpXXXXXXXXXX_!!1599032511.jpg\"/></p><p><img src=\"http://open.hexnews.com/content/appimages/sp/tw/xhtw/2/TB2saPCaXXXXXX.XpXXXXXXXXXX_!!1599032511.jpg\"/></p><p><img src=\"http://open.hexnews.com/content/appimages/sp/tw/xhtw/2/TB2saPCaXXXXXX.XpXXXXXXXXXX_!!1599032511.jpg\"/></p><p><img src=\"http://open.hexnews.com/content/appimages/sp/tw/xhtw/2/TB2saPCaXXXXXX.XpXXXXXXXXXX_!!1599032511.jpg\"/></p>";
	private Bundle mBundle;
	private String str = "";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mall_detail_content_layout);
		setImmerseLayout(findViewById(R.id.head_layout));
		init();
		mBundle = getIntent().getExtras();
		if(mBundle!=null){
			str = mBundle.getString("detail");
		}
		mWebView.loadDataWithBaseURL(null, str, "text/html", "utf-8", null);
	}
	
	private void init(){
		mWebView = (WebView) findViewById(R.id.mall_introduction_webview);
		mBackImage = (ImageView) findViewById(R.id.mall_introduction_back_info_image);
		mBackImage.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.mall_introduction_back_info_image:
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
