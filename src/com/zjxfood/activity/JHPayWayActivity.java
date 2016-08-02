package com.zjxfood.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.umeng.analytics.MobclickAgent;
import com.zjxfood.application.ExitApplication;
import com.zjxfood.common.Constants;
import com.zjxfood.http.XutilsUtils;
import com.zjxfood.popupwindow.CashJhPopupWindow;

import java.util.HashMap;

public class JHPayWayActivity extends AppActivity implements OnClickListener {

	private LinearLayout mWeixinPay;
	private LinearLayout mBalanceLayout;
	private LinearLayout mAlipayLayout;
	private RelativeLayout mHeadLayout;
	private ImageView mBackImage;
	private Bundle mBundle;
	private String price, merchantName, mId, mLoginImage, type;
	private TextView mPayPriceText;
	private String mFlum;
	private HashMap<String, Object> mCurrencyMap;
	private HashMap<String, Object> mCashJhMap;
	private CashJhPopupWindow mCashJhPopupWindow;// 余额激活
	private float mMyCash = 0;// 我的余额
	private TextView mMyCashText;
	private boolean isCashJh = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.pay_way_layout);
		setImmerseLayout(findViewById(R.id.head_layout));
		ExitApplication.getInstance().addActivity(this);
		ExitApplication.getInstance().addJhActivity(this);
		mBundle = getIntent().getExtras();

		init();
		if (mBundle != null) {
			price = mBundle.getString("price");
			merchantName = mBundle.getString("merchantName");
			mId = mBundle.getString("mId");
			mPayPriceText.setText("需要支付金额" + price + "元激活帐号！");
			mLoginImage = mBundle.getString("LoginImage");
			mFlum = mBundle.getString("flum");
			type = mBundle.getString("type");
		}
		getCurrencyHttp();
	}

	private void init() {
		mWeixinPay = (LinearLayout) findViewById(R.id.pay_way_weixin_layout);
		// mWeixinPay.setVisibility(View.GONE);
		mBalanceLayout = (LinearLayout) findViewById(R.id.cash_pay_way_yue_layout);
		mHeadLayout = (RelativeLayout) findViewById(R.id.title_pay_way_id);
		mBackImage = (ImageView) mHeadLayout
				.findViewById(R.id.pay_way_back_image);
		mAlipayLayout = (LinearLayout) findViewById(R.id.pay_way_zhifubao_layout);
		mPayPriceText = (TextView) findViewById(R.id.pay_way_head_text);
		mMyCashText = (TextView) findViewById(R.id.pay_way_yue_money);

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
		// 微信支付
		case R.id.pay_way_weixin_layout:
			intent.setClass(getApplicationContext(), WeixinPayActivity.class);
			bundle.putString("price", price);
			// bundle.putString("price", "0.01");
			bundle.putString("merchantName", merchantName);
			bundle.putString("mId", mId);
			bundle.putString("LoginImage", mLoginImage);
			bundle.putString("flum", mFlum);
			bundle.putString("flag", "2");// 激活标识
			bundle.putString("MallOrMerchant", "merchant");
			intent.putExtras(bundle);
			startActivity(intent);
			break;
		// 余额支付
		case R.id.cash_pay_way_yue_layout:
			// intent.setClass(getApplicationContext(),
			// BalancePayActivity.class);
			// bundle.putString("price", price);
			// bundle.putString("merchantName", "[食尚男女]"+merchantName);
			// bundle.putString("mId", mId);
			// bundle.putString("LoginImage", mLoginImage);
			// intent.putExtras(bundle);
			// startActivity(intent);
			mCashJhPopupWindow = new CashJhPopupWindow(JHPayWayActivity.this,
					itemsOnClick, "激活会员需要支付39.8元，确认使用余额支付？");
			mCashJhPopupWindow.showAtLocation(mWeixinPay, Gravity.CENTER, 0, 0);
			break;
		case R.id.pay_way_back_image:
			finish();
			break;
		// 支付宝支付
		case R.id.pay_way_zhifubao_layout:
			intent.setClass(getApplicationContext(), JHAlipayActivity.class);
			bundle.putString("price", price);
			// bundle.putString("price", "0.01");
			bundle.putString("merchantName", merchantName);
			bundle.putString("mId", mId);
			bundle.putString("LoginImage", mLoginImage);
			bundle.putString("flum", mFlum);
			intent.putExtras(bundle);
			startActivity(intent);
			break;
		}
	}

	OnClickListener itemsOnClick = new OnClickListener() {
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.mall_jh_true:
				if (isCashJh) {
					if (mMyCash > 39.8) {
						getCashJhHttp();
					} else {
						Toast.makeText(getApplicationContext(), "余额不足",
								Toast.LENGTH_SHORT).show();
					}
				} else {
					Toast.makeText(getApplicationContext(), "数据加载失败",
							Toast.LENGTH_SHORT).show();
				}
				if (mCashJhPopupWindow != null
						&& mCashJhPopupWindow.isShowing()) {
					mCashJhPopupWindow.dismiss();
				}
				break;

			case R.id.mall_jh_false:
				break;
			}
		}
	};

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		switch (keyCode) {
		case KeyEvent.KEYCODE_BACK:
			finish();
			break;

		default:
			break;
		}
		return super.onKeyDown(keyCode, event);
	}

	// 获取余额
	private void getCurrencyHttp() {
		StringBuilder sb = new StringBuilder();
		sb.append("uid=" + Constants.mId);
		XutilsUtils.get(Constants.getCurrency, sb,
				new RequestCallBack<String>() {
					@Override
					public void onFailure(HttpException arg0, String arg1) {
					}
					@Override
					public void onSuccess(ResponseInfo<String> res) {
						 mCurrencyMap = Constants.getJsonObject(res.result);
//						mCurrencyMap = Constants
//								.getJsonObjectByData(res.result);
						if (mCurrencyMap != null
								&& mCurrencyMap.get("Code").toString()
										.equals("200")) {
							// Log.i("我的余额",
							// mCurrencyMap.get("Data").toString()+"==========="+mCurrencyMap);
							mMyCash = Float.parseFloat(mCurrencyMap.get("Data")
									.toString());
						}
						Log.i("我的余额", mCurrencyMap + "===========" + mMyCash);
						handler.sendEmptyMessageDelayed(1, 0);
					}
				});
	}

	// 激活
	private void getCashJhHttp() {
		StringBuilder sb = new StringBuilder();
		sb.append("uid=" + Constants.mId + "&money=39.8");
		XutilsUtils.get(Constants.cashJh, sb, new RequestCallBack<String>() {
			@Override
			public void onFailure(HttpException arg0, String arg1) {
			}

			@Override
			public void onSuccess(ResponseInfo<String> res) {
				mCashJhMap = Constants.getJsonObject(res.result);
				Log.i("激活结果", mCashJhMap + "===========");
				handler.sendEmptyMessageDelayed(2, 0);
			}
		});
	}

	Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 1:
				isCashJh = true;
				mMyCashText.setText(mMyCash + "");
				break;

			case 2:
				if (mCashJhMap != null && mCashJhMap.get("Message") != null) {
					Toast.makeText(getApplicationContext(),
							mCashJhMap.get("Message").toString(),
							Toast.LENGTH_SHORT).show();
				}
				if (mCashJhMap.get("Code") != null
						&& mCashJhMap.get("Code").toString().equals("200")) {
					Intent intent = new Intent();
					intent.setClass(getApplicationContext(), MyNewActivity.class);
					startActivity(intent);
					ExitApplication.getInstance().finishJh();
				}

				break;
			}
		};
	};

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
