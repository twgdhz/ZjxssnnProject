package com.zjxfood.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.umeng.analytics.MobclickAgent;
import com.zjxfood.application.ExitApplication;

public class PayWayActivity extends AppActivity implements OnClickListener{

	private LinearLayout mWeixinPay;
	private LinearLayout mBalanceLayout;
	private LinearLayout mAlipayLayout;
	private RelativeLayout mHeadLayout;
	private ImageView mBackImage;
	private Bundle mBundle;
	private String price,merchantName,mId,mLoginImage,type,money,currency;
	private TextView mPayPriceText;
	private String mFlum;
	private TextView mTitleText;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.pay_way_layout);
		setImmerseLayout(findViewById(R.id.head_layout));
		ExitApplication.getInstance().addActivity(this);
		ExitApplication.getInstance().addMyCommodityActivity(this);
		mBundle = getIntent().getExtras();
		
		init();
		if(mBundle!=null){
			price = mBundle.getString("price");
			merchantName = mBundle.getString("merchantName");
			mId = mBundle.getString("mId");
			
			mLoginImage = mBundle.getString("LoginImage");
			mFlum = mBundle.getString("flum");
			type = mBundle.getString("type");
			money = mBundle.getString("money");
			currency = mBundle.getString("currency");
			mPayPriceText.setText("需要支付金额"+money);
		}
	}
	
	private void init(){
		mWeixinPay = (LinearLayout) findViewById(R.id.pay_way_weixin_layout);
		mBalanceLayout = (LinearLayout) findViewById(R.id.cash_pay_way_yue_layout);
		mHeadLayout = (RelativeLayout) findViewById(R.id.title_pay_way_id);
		mBackImage = (ImageView) mHeadLayout.findViewById(R.id.title_back_image);
		mTitleText = (TextView) findViewById(R.id.title_text);
		mTitleText.setText("支付方式");
		mAlipayLayout = (LinearLayout) findViewById(R.id.pay_way_zhifubao_layout);
		mPayPriceText = (TextView) findViewById(R.id.pay_way_head_text);
		mBalanceLayout.setVisibility(View.GONE);
		mWeixinPay.setOnClickListener(this);
		mBalanceLayout.setOnClickListener(this);
		mBackImage.setOnClickListener(this);
		mAlipayLayout.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		Intent intent = new Intent();
		Bundle bundle = new Bundle();
		switch (v.getId()) {
		case R.id.pay_way_weixin_layout:
			intent.setClass(getApplicationContext(), WeixinPayActivity.class);
			bundle.putString("price", price);
			bundle.putString("merchantName", merchantName);
			bundle.putString("mId", mId);
			bundle.putString("LoginImage", mLoginImage);
			bundle.putString("flum", mFlum);
			bundle.putString("flag", "1");//支付标识
			bundle.putString("MallOrMerchant", "merchant");
			bundle.putString("money", money);
			bundle.putString("currency", currency);
			intent.putExtras(bundle);
			startActivity(intent);
			break;

		case R.id.cash_pay_way_yue_layout:
			intent.setClass(getApplicationContext(), BalancePayActivity.class);
			bundle.putString("price", price);
			bundle.putString("merchantName", "[食尚男女]"+merchantName);
			bundle.putString("mId", mId);
			bundle.putString("LoginImage", mLoginImage);
			intent.putExtras(bundle);
			startActivity(intent);
			break;
		case R.id.title_back_image:
			finish();
			break;
		case R.id.pay_way_zhifubao_layout:
			intent.setClass(getApplicationContext(), AlipayActivity.class);
			bundle.putString("price", price);
			bundle.putString("merchantName", merchantName);
			bundle.putString("mId", mId);
			bundle.putString("LoginImage", mLoginImage);
			bundle.putString("flum", mFlum);
			bundle.putString("MallOrMerchant", "merchant");
			
			bundle.putString("money", money);
			bundle.putString("currency", currency);
			intent.putExtras(bundle);
			startActivity(intent);
			break;
		}
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		switch (keyCode) {
		case KeyEvent.KEYCODE_BACK:
			System.gc();
			finish();
			break;

		default:
			break;
		}
		return super.onKeyDown(keyCode, event);
	}
	@Override
	protected void onDestroy() {
		System.gc();
		super.onDestroy();
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
