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

public class MallPayWayActivity extends AppActivity implements OnClickListener {

	private LinearLayout mWeixinPay;
	private LinearLayout mBalanceLayout;
	private LinearLayout mAlipayLayout;
	private RelativeLayout mHeadLayout;
	private ImageView mBackImage;
	private Bundle mBundle;
	private String price, merchantName, mId, mLoginImage, type, addressId,
			giftId, sizeId, orderId,mUseCurrency;
	private TextView mPayPriceText;
	private String mFlum;
	private String mallOrMerchant;
	private HashMap<String, Object> mCurrencyMap;
	private HashMap<String, Object> mOrderMap;
	private String mMyCash = "0";// 我的余额
	private TextView mMyCashText;
	private boolean isCashJh = false;
	private CashJhPopupWindow mCashZfPopupWindow;
	private String mPayType;
	private TextView mTitleText;
	private String mOrderId;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.pay_mall_way_layout);
		setImmerseLayout(findViewById(R.id.head_layout));
		ExitApplication.getInstance().addMallActivity(this);
		ExitApplication.getInstance().addMallDetail(this);
		mBundle = getIntent().getExtras();

		init();
		if (mBundle != null) {
			price = mBundle.getString("price");
			merchantName = mBundle.getString("merchantName");
			mId = mBundle.getString("mId");
			mPayPriceText.setText("需要支付金额" + price);
			mLoginImage = mBundle.getString("LoginImage");
			mFlum = mBundle.getString("flum");
			type = mBundle.getString("type");
			giftId = mBundle.getString("giftId");
			addressId = mBundle.getString("address");
			sizeId = mBundle.getString("sizeId");
			mallOrMerchant = mBundle.getString("mallOrMerchant");
			mPayType = mBundle.getString("payType");
			mUseCurrency = mBundle.getString("useCurrency");
			Log.i("address", addressId + "==============="+mPayType);
			if (mBundle.getString("orderId") != null) {
				orderId = mBundle.getString("orderId");
			}
		}
		getCurrencyHttp();
		
	}

	private void init() {
		mWeixinPay = (LinearLayout) findViewById(R.id.pay_way_weixin_layout);
		mBalanceLayout = (LinearLayout) findViewById(R.id.pay_way_yue_layout);
		mHeadLayout = (RelativeLayout) findViewById(R.id.title_pay_way_id);
//		mBackImage = (ImageView) mHeadLayout.findViewById(R.id.title_back_image);
		mBackImage = (ImageView) findViewById(R.id.title_back_image);
		mTitleText = (TextView) findViewById(R.id.title_text);
		mTitleText.setText("支付方式");
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
		case R.id.pay_way_weixin_layout:
			intent.setClass(getApplicationContext(), WeixinPayActivity.class);

			bundle.putString("price", price);
			bundle.putString("merchantName", merchantName);
			bundle.putString("mId", mId);
			bundle.putString("LoginImage", mLoginImage);
			bundle.putString("flum", mFlum);
			bundle.putString("addressId", addressId);
			bundle.putString("giftId", giftId);
			bundle.putString("sizeId", sizeId);
			bundle.putString("flag", "1");// 支付标识
			bundle.putString("MallOrMerchant", mallOrMerchant);
			bundle.putString("orderId", orderId);
			bundle.putString("useCurrency", mUseCurrency);
			intent.putExtras(bundle);
			startActivity(intent);
			break;

		case R.id.pay_way_yue_layout:
			// intent.setClass(getApplicationContext(),
			// BalancePayActivity.class);
			// bundle.putString("price", price);
			// bundle.putString("merchantName", "[食尚男女]"+merchantName);
			// bundle.putString("mId", mId);
			// bundle.putString("LoginImage", mLoginImage);
			// intent.putExtras(bundle);
			// startActivity(intent);
			mCashZfPopupWindow = new CashJhPopupWindow(MallPayWayActivity.this,
					itemsOnClick, "需要支付金额" + price + "确认是否使用余额支付该商品？");
			mCashZfPopupWindow.showAtLocation(mBackImage, Gravity.CENTER, 0, 0);
			break;
		case R.id.title_back_image:
			finish();
			break;
		case R.id.pay_way_zhifubao_layout:
			intent.setClass(getApplicationContext(), AlipayActivity.class);
			bundle.putString("price", price);
			// bundle.putString("price", "0.01");测试数据
			bundle.putString("merchantName", merchantName);
			bundle.putString("mId", mId);
			bundle.putString("LoginImage", mLoginImage);
			bundle.putString("flum", mFlum);
			bundle.putString("addressId", addressId);
			bundle.putString("sizeId", sizeId);
			bundle.putString("giftId", giftId);
			bundle.putString("MallOrMerchant", mallOrMerchant);
			bundle.putString("orderId", orderId);
			bundle.putString("useCurrency", mUseCurrency);
			// bundle.putString("addressId", address);
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
					if (Float.parseFloat(price) <= Float.parseFloat(mMyCash)) {
						if (mPayType != null) {
							if(mPayType.equals("cashYf")){
								//余额支付现金抽奖运费
								cashPayYf();
							}else if(mPayType.equals("indiana")){
								//余额支付食尚夺宝运费
								indianaPayYf();
							}else if(mPayType.equals("auctionYf")){
								//余额支付竞拍订单运费
								auctionPayYf();
							}
							
						} else {
							cashPay();
						}
					} else {
						Toast.makeText(getApplicationContext(), "余额不足",
								Toast.LENGTH_SHORT).show();
					}
				}
				if (mCashZfPopupWindow != null
						&& mCashZfPopupWindow.isShowing()) {
					mCashZfPopupWindow.dismiss();
				}
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
//						 mCurrencyMap = Constants
//						 .getJsonObjectByData(res.result);
						if (mCurrencyMap != null
								&& mCurrencyMap.get("Code").toString()
										.equals("200")) {
							// Log.i("我的余额",
							// mCurrencyMap.get("Data").toString()+"==========="+mCurrencyMap);
							try {
								mMyCash = mCurrencyMap.get(
										"Data").toString() ;
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
						Log.i("我的余额", mCurrencyMap + "===========" + mMyCash);
						handler.sendEmptyMessageDelayed(1, 0);
					}
				});
	}

	// 使用余额支付
	private void cashPay() {
		StringBuilder sb = new StringBuilder();
		if (!(sizeId == null)) {
			sb.append("userid=" + Constants.mId + "&money=" + price
					+ "&addressid=" + addressId + "&remark=&giftid=" + giftId
					+ "&chimaid=" + sizeId + "&usecurrency=true");
		} else {
			sb.append("userid=" + Constants.mId + "&money=" + price
					+ "&addressid=" + addressId + "&remark=&giftid=" + giftId
					+ "&usecurrency=true");
		}
		XutilsUtils.get(Constants.createMallOrder2, sb,
				new RequestCallBack<String>() {
					@Override
					public void onFailure(HttpException arg0, String arg1) {
					}

					@Override
					public void onSuccess(ResponseInfo<String> res) {
						// mCurrencyMap = Constants.getJsonObject(res.result);
						Log.i("订单信息1", res.result + "===========");
						mOrderMap = Constants.getJsonObject(res.result);
						if (mOrderMap != null
								&& mOrderMap.get("Code").toString()
										.equals("200")) {
						}
						Log.i("订单信息2", mOrderMap + "===========");
						handler.sendEmptyMessageDelayed(2, 0);
					}
				});
	}

	private void cashPayYf() {
		StringBuilder sb = new StringBuilder();
		if (orderId != null) {
			sb.append("orderNum=" + orderId);
		}
		XutilsUtils.get(Constants.payCashYf, sb, new RequestCallBack<String>() {
			@Override
			public void onFailure(HttpException arg0, String arg1) {
			}

			@Override
			public void onSuccess(ResponseInfo<String> res) {
				// mCurrencyMap = Constants.getJsonObject(res.result);
				Log.i("订单信息1", res.result + "===========");
				mOrderMap = Constants.getJsonObject(res.result);
				if (mOrderMap != null
						&& mOrderMap.get("Code").toString().equals("200")) {
					handler.sendEmptyMessageDelayed(3, 0);
				}
				Log.i("订单信息2", mOrderMap + "===========");

			}
		});
	}
	private void indianaPayYf() {
		StringBuilder sb = new StringBuilder();
		if (orderId != null) {
			sb.append("orderNum=" + orderId);
		}
		XutilsUtils.get(Constants.payIndianaYf, sb, new RequestCallBack<String>() {
			@Override
			public void onFailure(HttpException arg0, String arg1) {
			}

			@Override
			public void onSuccess(ResponseInfo<String> res) {
				// mCurrencyMap = Constants.getJsonObject(res.result);
				Log.i("订单信息1", res.result + "===========");
				mOrderMap = Constants.getJsonObject(res.result);
				if (mOrderMap != null
						&& mOrderMap.get("Code").toString().equals("200")) {
					handler.sendEmptyMessageDelayed(3, 0);
				}
				Log.i("订单信息2", mOrderMap + "===========");

			}
		});
	}
	private void auctionPayYf() {
		StringBuilder sb = new StringBuilder();
		if (orderId != null) {
			sb.append("orderNum=" + orderId);
		}
		XutilsUtils.get(Constants.payAuctionYf, sb, new RequestCallBack<String>() {
			@Override
			public void onFailure(HttpException arg0, String arg1) {
			}

			@Override
			public void onSuccess(ResponseInfo<String> res) {
				// mCurrencyMap = Constants.getJsonObject(res.result);
				Log.i("订单信息1", res.result + "===========");
				mOrderMap = Constants.getJsonObject(res.result);
				if (mOrderMap != null
						&& mOrderMap.get("Code").toString().equals("200")) {
					handler.sendEmptyMessageDelayed(3, 0);
				}
				Log.i("订单信息2", mOrderMap + "===========");

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
				if (mOrderMap != null) {
					if (mOrderMap.get("Code") != null
							&& mOrderMap.get("Code").toString().equals("200")) {
						Intent intent = new Intent();
						intent.setClass(getApplicationContext(),
								MallOrderActivity.class);
						startActivity(intent);
						ExitApplication.getInstance().finishMall();
						Toast.makeText(getApplicationContext(),
								mOrderMap.get("Message").toString(),
								Toast.LENGTH_SHORT).show();
					} else if (mOrderMap.get("Code") != null
							&& mOrderMap.get("Code").toString().equals("401")) {
						mCashZfPopupWindow = new CashJhPopupWindow(
								MallPayWayActivity.this, itemsOnClick2, "");
						mCashZfPopupWindow.showAtLocation(mBackImage,
								Gravity.CENTER, 0, 0);
					}
				}
				break;
			case 3:
				Toast.makeText(getApplicationContext(), "支付成功",
						Toast.LENGTH_SHORT).show();
				finish();
				break;
			}
		};
	};

	OnClickListener itemsOnClick2 = new OnClickListener() {
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.mall_jh_true:
				Intent intent = new Intent();
				intent.setClass(getApplicationContext(), MyJhActivity.class);
				startActivity(intent);
				ExitApplication.getInstance().finishMall();
				break;

			default:
				break;
			}
		}
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
