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

/**
 * 消费币充值方式
 * @author zjx
 *
 */
public class CurrencyPayWayActivity extends AppActivity implements OnClickListener{

	private LinearLayout mWeixinPay;
	private LinearLayout mBalanceLayout;
	private LinearLayout mAlipayLayout;
	private RelativeLayout mHeadLayout;
	private ImageView mBackImage;
	private Bundle mBundle;
	private String price,merchantName;
	private TextView mAlertText;
	private TextView mTitleText;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
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
			mAlertText.setText("你需要支付金额：￥"+price);
		}
	}
	
	private void init(){
		
		mWeixinPay = (LinearLayout) findViewById(R.id.pay_way_weixin_layout);
		mBalanceLayout = (LinearLayout) findViewById(R.id.cash_pay_way_yue_layout);
		mHeadLayout = (RelativeLayout) findViewById(R.id.title_pay_way_id);
		mAlipayLayout = (LinearLayout) findViewById(R.id.pay_way_zhifubao_layout);
		mAlertText = (TextView) findViewById(R.id.pay_way_head_text);
		mBackImage = (ImageView) findViewById(R.id.title_back_image);
		mTitleText = (TextView) findViewById(R.id.title_text);
		mTitleText.setText("支付方式");
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
			bundle.putString("flag", "3");//支付标识
			bundle.putString("MallOrMerchant", "currency");
			intent.putExtras(bundle);
			startActivity(intent);
			break;
		case R.id.pay_way_zhifubao_layout:
			intent.setClass(getApplicationContext(), AlipayActivity.class);
			bundle.putString("price", price);
			bundle.putString("merchantName", merchantName);
			bundle.putString("MallOrMerchant", "currency");
			intent.putExtras(bundle);
			startActivity(intent);
			break;

		case R.id.title_back_image:
			finish();
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
