package com.zjxfood.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.webkit.WebView;

import com.umeng.analytics.MobclickAgent;
import com.zjxfood.activity.R;


public class MallIntroductionFragment extends Fragment implements OnClickListener {
	private WebView mWebView;
	private String source = "<p><strong>这是</strong>一个<span style=\"color: rgb(255, 0, 0);\">测试商品</span>，<span style=\"text-decoration: underline;\">包含</span>了各<em>种样式</em>，请<span style=\"font-family: 微软雅黑, &#39;Microsoft YaHei&#39;;\">测试</span>显示是否正确。</p><ul class=\" list-paddingleft-2\" style=\"list-style-type: square;\"><li><p>这个一个项目</p></li><li><p>这是第二项<br/></p></li></ul><p><img src=\"http://open.hexnews.com/content/appimages/sp/tw/xhtw/2/TB2saPCaXXXXXX.XpXXXXXXXXXX_!!1599032511.jpg\"/></p><p><img src=\"http://open.hexnews.com/content/appimages/sp/tw/xhtw/2/TB2saPCaXXXXXX.XpXXXXXXXXXX_!!1599032511.jpg\"/></p><p><img src=\"http://open.hexnews.com/content/appimages/sp/tw/xhtw/2/TB2saPCaXXXXXX.XpXXXXXXXXXX_!!1599032511.jpg\"/></p><p><img src=\"http://open.hexnews.com/content/appimages/sp/tw/xhtw/2/TB2saPCaXXXXXX.XpXXXXXXXXXX_!!1599032511.jpg\"/></p>";
	private Bundle mBundle;
	private String mStr = "";
	private Context mContext;
	public MallIntroductionFragment() {
		super();
	}
	public static MallIntroductionFragment newInstance(Context context,String str) {
		MallIntroductionFragment f = new MallIntroductionFragment();

		Bundle bundle = new Bundle();
		bundle.putString("str", str);
		f.setArguments(bundle);
		return f;
	}


	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.mall_detail_introduction_layout, null);
		initView(view);
		if(mStr!=null){
		mWebView.loadDataWithBaseURL(null, mStr, "text/html", "utf-8", null);
		}
		return view;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Bundle args = getArguments();
		if (args != null) {
			mStr = args.getString("str");
		}
	}

	private void initView(View view) {
		mWebView = (WebView) view.findViewById(R.id.mall_introduction_webview);
	}
	
	@Override
	public void onResume() {
		super.onResume();
		MobclickAgent.onPageStart("MainScreen"); //统计页面
	}
	@Override
	public void onPause() {
		super.onPause();
		MobclickAgent.onPageEnd("MainScreen");
	}

	@Override
	public void onClick(View arg0) {
	}
}
