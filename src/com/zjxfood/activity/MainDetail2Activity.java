package com.zjxfood.activity;

import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

public class MainDetail2Activity extends AppActivity implements OnClickListener{

	private TextView mTextView1,mTextView2;
	private ImageView mBackImage;
	private String str1 = "<font color='#323232'>11月12日起，推荐一个游客注册食尚男女，推荐人即可获得</font><font color='red'>10</font><font color='#323232'>食尚币。</color>";
	private String str2 = "<font color='#323232'>11月15日起，食尚男女游客（未激活）即可在食尚商城购买</font><font color='red'>200元</font><font color='#323232'>元以下商品！只需选择商品，选择购买的商品型号，填写收货地址，通过微信或者支付宝支付实际邮费即可下单成功。</color>";
		
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_help_layout2);
		setImmerseLayout(findViewById(R.id.head_layout));
		init();
		mTextView1.setText(Html.fromHtml(str1));
		mTextView2.setText(Html.fromHtml(str2));
	}
	
	private void init(){
		mTextView1 = (TextView) findViewById(R.id.main_help2_content_text);
		mTextView2 = (TextView) findViewById(R.id.main_help2_content_text2);
		mBackImage = (ImageView) findViewById(R.id.main_help_info_image);
		
		mBackImage.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.main_help_info_image:
			finish();
			break;

		default:
			break;
		}
	}
}
