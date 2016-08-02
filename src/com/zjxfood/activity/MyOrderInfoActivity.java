package com.zjxfood.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.lidroid.xutils.BitmapUtils;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.umeng.analytics.MobclickAgent;
import com.zjxfood.application.ExitApplication;
import com.zjxfood.common.Constants;
import com.zjxfood.http.AsyUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class MyOrderInfoActivity extends AppActivity implements OnClickListener {
	private ImageView mLoginImage;// 商家图片
	private TextView mMerchantName;// 商家名称
	private TextView mMerchantDescription;// 商家介绍
	private TextView mMerchantPrice;// 付款金额
	private TextView mMerchantAddress;// 商家地址
	private TextView mOrderId;// 订单号
	private TextView mPhoneText;// 手机号
	private TextView mPriceDate;// 付款日期
	private TextView mFlnumText;// 返利
	private TextView mMerchantName2;
	private Bundle mBundle;
	private String mMerchantNameStr, mDescripttionStr, mPriceStr, mAddressStr,
			mOrderidStr, mPhoneStr, mPathStr, mFlnumStr, mDate, orderId;
	private ImageView mBackImage;
	private Button mEvaluationBtn;
	private TextView mFlnumBi;
	// private ArrayList<HashMap<String, Object>> mList;
	// private RunTask mRunTask;
	private String sign = "";
	private Button mGrabBtn;
	private boolean isHB = false, isEval = false;// 判断是否可以抢红包、评价
			// private int x = 1;
	private ArrayList<HashMap<String, Object>> mMerchantList;
	private ImageView mPhoneImage;
	private String payId = "";
	private BitmapUtils mBitmapUtils;
	private ArrayList<HashMap<String, Object>> mMerchantMap;
	private TextView mCurrencyText;
	private TextView mCurrencyValue;
	private TextView mCurrencyPay;
	private TextView mPayMoneyText;
	private TextView mTitleText;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.my_order_info_layout);
		setImmerseLayout(findViewById(R.id.head_layout));
		init();
		mBitmapUtils = new BitmapUtils(getApplicationContext());
		mBitmapUtils.configDiskCacheEnabled(true);
		mBitmapUtils.configMemoryCacheEnabled(false);
		mBundle = getIntent().getExtras();
		if (mBundle != null) {
			payId = mBundle.getString("payId");
//			payId = payId.substring(1, payId.length() - 1);
			Log.i("payId", payId + "=================");
			getUserOrderInfo();
			// }
		}
	}

	private void init() {
		mLoginImage = (ImageView) findViewById(R.id.order_info_image);
		mMerchantName = (TextView) findViewById(R.id.order_info_name);
		mMerchantDescription = (TextView) findViewById(R.id.order_info_description);
		mMerchantPrice = (TextView) findViewById(R.id.order_info_price);
		mMerchantAddress = (TextView) findViewById(R.id.order_merchant_info_left_address);
		mOrderId = (TextView) findViewById(R.id.order_info_id_text);
		mPhoneText = (TextView) findViewById(R.id.order_info_phone_id_text);
		mPriceDate = (TextView) findViewById(R.id.order_info_date_text);
//		mBackImage = (ImageView) findViewById(R.id.title_back_image);
		mBackImage = (ImageView) findViewById(R.id.title_back_image);
		mTitleText = (TextView) findViewById(R.id.title_text);
		mTitleText.setText("订单详情");
		mEvaluationBtn = (Button) findViewById(R.id.order_evaluation_btn);
		mMerchantName2 = (TextView) findViewById(R.id.order_merchant_info_left_name);
		mFlnumText = (TextView) findViewById(R.id.order_info_price_text);
		mFlnumBi = (TextView) findViewById(R.id.order_info_price_leftshow);
		mGrabBtn = (Button) findViewById(R.id.order_grab_red_btn);
		mPhoneImage = (ImageView) findViewById(R.id.order_merchant_phone_image);
		mCurrencyText = (TextView) findViewById(R.id.order_info_currency_right_bi);
		mCurrencyValue = (TextView) findViewById(R.id.order_info_currency_text);
		mCurrencyPay = (TextView) findViewById(R.id.order_info_price_right_bi);
		mPayMoneyText = (TextView) findViewById(R.id.order_info_currency_text2);

		mBackImage.setOnClickListener(this);
		mEvaluationBtn.setOnClickListener(this);
		mGrabBtn.setOnClickListener(this);
		mLoginImage.setOnClickListener(this);
		mPhoneImage.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		Intent intent = new Intent();
		switch (v.getId()) {
		// 电话
		case R.id.order_merchant_phone_image:
			if (mMerchantMap.get(0).get("MerchantPhone") != null) {
				intent.setAction("android.intent.action.CALL");
				intent.setData(Uri.parse("tel:"
						+ mMerchantMap.get(0).get("MerchantPhone").toString()));
				startActivity(intent);
			} else {
				Toast.makeText(getApplicationContext(), "该商户暂时没有联系电话！",
						Toast.LENGTH_SHORT).show();
			}
			break;
		// 进入商户页面
		case R.id.order_info_image:
			if (mMerchantMap.get(0).get("MMid") != null) {
				// x = 2;
				// mRunTask = new RunTask();
				// mRunTask.execute("");
				getMerchantInfo();
			}
			break;
		case R.id.title_back_image:
			finish();
			break;

		case R.id.order_evaluation_btn:
			if (isEval) {
				intent.setClass(getApplicationContext(),
						MyOrderEvaluationActivity.class);
				Bundle bundle = new Bundle();
				if (!Constants.isNull(mMerchantMap.get(0).get("Logoimage"))) {
					bundle.putString("ImagePath",
							mMerchantMap.get(0).get("Logoimage").toString());
				}
				if (!Constants.isNull(mMerchantMap.get(0).get("Merchantname"))) {
					bundle.putString("MerchantName",
							mMerchantMap.get(0).get("Merchantname").toString());
				}
				if (!Constants.isNull(mMerchantMap.get(0).get("Money"))) {
					bundle.putString("MerchantPrice",
							mMerchantMap.get(0).get("Money").toString());
				}
				if (!Constants.isNull(mMerchantMap.get(0).get("Mid"))) {
					bundle.putString("Mid", mMerchantMap.get(0).get("Mid")
							.toString());
				}
				if (!Constants.isNull(mMerchantMap.get(0).get("MMid"))) {
					bundle.putString("MMid", mMerchantMap.get(0).get("MMid")
							.toString());
				}
				if (!Constants.isNull(mMerchantMap.get(0).get("Images1"))) {
					bundle.putString("Images1",
							mMerchantMap.get(0).get("Images1").toString());
				}
				if (!Constants.isNull(mMerchantMap.get(0).get("Images2"))) {
					bundle.putString("Images2",
							mMerchantMap.get(0).get("Images2").toString());
				}
				if (!Constants.isNull(mMerchantMap.get(0).get("Images3"))) {
					bundle.putString("Images3",
							mMerchantMap.get(0).get("Images3").toString());
				}
				if (!Constants.isNull(mMerchantMap.get(0).get("Id"))) {
					bundle.putString("orderId",
							mMerchantMap.get(0).get("Id").toString());
				}
				// Log.i("images1", mMerchantMap.get("Images1").toString()
				// + "=====images1======"
				// + mList.get(0).get("Images2").toString() + "==="
				// + mList.get(0).get("Images3").toString());
				intent.putExtras(bundle);
				startActivity(intent);
			} else {
				Toast.makeText(getApplicationContext(), "已经评价过了！",
						Toast.LENGTH_SHORT).show();
			}
			break;
		case R.id.order_grab_red_btn:

			if (mMerchantMap != null
					&& mMerchantMap.size()>0 && mMerchantMap.get(0).get("Pltext") != null) {
				if (mMerchantMap.get(0).get("Pltext").toString().length() > 0
						&& Integer.parseInt(mMerchantMap.get(0).get("HB")
								.toString()) == -1) {
					intent.setClass(getApplicationContext(),
							GrabRedActivity.class);
					Bundle bundle2 = new Bundle();
					bundle2.putString("Id", orderId);
					intent.putExtras(bundle2);
					startActivity(intent);
					finish();
				} else if (mMerchantMap.get(0).get("Pltext").toString()
						.length() > 0
						&& Integer.parseInt(mMerchantMap.get(0).get("HB")
								.toString()) > -1) {
					Toast.makeText(getApplicationContext(), "已经抢过红包了，请再接再厉！",
							Toast.LENGTH_SHORT).show();
				} else if (mMerchantMap.get(0).get("Pltext").toString()
						.equals("")
						&& mMerchantMap.get(0).get("Pltext").toString()
								.equals("")
						&& Integer.parseInt(mMerchantMap.get(0).get("HB")
								.toString()) == -1) {
					Toast.makeText(getApplicationContext(), "评价成功后才能抢红包！",
							Toast.LENGTH_SHORT).show();
				}
			}
		}
	}

	// 根据商户id获取商户信息
	private void getMerchantInfo() {
		RequestParams params = new RequestParams();
		params.put("merchantid", mMerchantMap.get(0).get("MMid").toString());
		AsyUtils.get(Constants.getMerchantById2, params,
				new JsonHttpResponseHandler() {
					@Override
					public void onSuccess(JSONArray response) {
						mMerchantList = Constants.getJsonArray(response
								.toString());
						handler.sendEmptyMessageDelayed(2, 0);
						super.onSuccess(response);
					}
				});
	}

	// 获取用户订单详情页面
	private void getUserOrderInfo() {
		RequestParams params = new RequestParams();
		params.put("payid", payId);
		AsyUtils.get(Constants.getOrderInfoByPayId2, params,
				new JsonHttpResponseHandler() {
					@Override
					public void onSuccess(JSONObject response) {
						try {
							mMerchantMap = Constants
									.getJsonArrayByData(response.toString());
						} catch (JSONException e) {
							e.printStackTrace();
						}
						Log.i("mMerchantMap", mMerchantMap+"=============");
						handler.sendEmptyMessageDelayed(1, 0);
						super.onSuccess(response);
					}
				});
	}

	

	Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 1:
				if (mMerchantMap != null && mMerchantMap.size() > 0) {
					mPayMoneyText.setText("支付金额：￥"+mMerchantMap.get(0).get("Money").toString());
					
					if (!Constants.isNull(mMerchantMap.get(0).get(
							"currency"))) {
						mCurrencyText.setText("￥"+mMerchantMap.get(0)
								.get("currency").toString());
					}
					if (!Constants.isNull(mMerchantMap.get(0).get("Logoimage"))) {
						mBitmapUtils.display(mLoginImage, mMerchantMap.get(0)
								.get("Logoimage").toString());
					}
					if (!Constants.isNull(mMerchantMap.get(0).get(
							"Merchantname"))) {
						mMerchantName.setText(mMerchantMap.get(0)
								.get("Merchantname").toString());
					}
					if (!Constants.isNull(mMerchantMap.get(0).get("Money"))) {
						mMerchantPrice.setText("￥"
								+ (Float.parseFloat(mMerchantMap.get(0).get("Money").toString())+Float.parseFloat(mMerchantMap.get(0).get("currency").toString())));
					}
					if (!Constants.isNull(mMerchantMap.get(0).get(
							"Merchantname"))) {
						mMerchantName2.setText(mMerchantMap.get(0)
								.get("Merchantname").toString());
					}
					if (!Constants.isNull(mMerchantMap.get(0).get("Address"))) {
						mMerchantAddress.setText(mMerchantMap.get(0)
								.get("Address").toString());
					}
					if (!Constants.isNull(mMerchantMap.get(0).get("Payid"))) {
						mOrderId.setText("订单号："
								+ mMerchantMap.get(0).get("Payid").toString());
					}
					if (!Constants.isNull(mMerchantMap.get(0).get("Username"))) {
						mPhoneText.setText("手机号码："
								+ mMerchantMap.get(0).get("Username")
										.toString());
					}
					if (!Constants
							.isNull(mMerchantMap.get(0).get("Createtime"))) {
						mPriceDate.setText("付款日期："
								+ mMerchantMap.get(0).get("Createtime")
										.toString());
					}
					if (!Constants.isNull(mMerchantMap.get(0).get(
							"currencyback"))) {
						mCurrencyValue.setText("总金额："+(Float.parseFloat(mMerchantMap.get(0).get("Money").toString())+Float.parseFloat(mMerchantMap.get(0).get("currency").toString())));
					}
					if (!Constants.isNull(mMerchantMap.get(0).get("Money"))) {
//						float rebate = Float.parseFloat(mMerchantMap.get(0)
//								.get("Shmoney").toString());
//						int flnums = (int) rebate;
						mFlnumText.setText("支付余额：￥"+mMerchantMap.get(0)
								.get("currency").toString());
						mFlnumBi.setText("￥" + mMerchantMap.get(0).get("Money").toString());
					}
					if (!Constants.isNull(mMerchantMap.get(0).get(
							"Introduction"))) {
						mMerchantDescription.setText(mMerchantMap.get(0)
								.get("Introduction").toString());
					}
					if (mMerchantMap.get(0).get("Pltext") == null) {
						mEvaluationBtn
								.setBackgroundResource(R.drawable.bg_order_evaluation_btn_style);
						isEval = true;
					} else {
						mEvaluationBtn
								.setBackgroundResource(R.drawable.bg_order_evaluation_not_btn_style);
						mEvaluationBtn.setTextColor(getResources().getColor(
								R.color.order_evaluation_not));
						isEval = false;
					}
					if (mMerchantMap.get(0).get("HB") != null
							&& Integer.parseInt(mMerchantMap.get(0).get("HB")
									.toString()) > -1) {
						mGrabBtn.setBackgroundResource(R.drawable.bg_order_grab_not_red_style);
						isHB = false;
					} else if (mMerchantMap.get(0).get("HB").toString()
							.equals("-1")) {
						Log.i("", mMerchantMap.get(0).get("HB").toString()+"====="+mMerchantMap.get(0).get("Pltext"));
						if (mMerchantMap.get(0).get("Pltext")==null) {
							Log.i("mMerchantMap", "========true========");
							mGrabBtn.setBackgroundResource(R.drawable.bg_order_grab_red_style);
							isHB = true;
						} else {
							mGrabBtn.setBackgroundResource(R.drawable.bg_order_grab_not_red_style);
							isHB = false;
							Log.i("mMerchantMap", "========false========");
						}
					}
				}
				break;

			case 2:
				if (mMerchantList != null && mMerchantList.size() > 0) {
					Log.i("mMerchantList", mMerchantList + "===============");
					Intent intent = new Intent();
					Bundle bundle = new Bundle();
					if (!Constants.isNull(mMerchantList.get(0).get(
							"Merchantname"))) {
						bundle.putString("merchantName", mMerchantList.get(0)
								.get("Merchantname").toString());
					}
					if (!Constants
							.isNull(mMerchantList.get(0).get("Logoimage"))) {
						bundle.putString("Logoimage",
								mMerchantList.get(0).get("Logoimage")
										.toString());
					}
					if (!Constants.isNull(mMerchantList.get(0).get("Address"))) {
						bundle.putString("merchantAddress", mMerchantList
								.get(0).get("Address").toString());
					}
					if (!Constants.isNull(mMerchantList.get(0).get("Phone"))) {
						bundle.putString("Phone",
								mMerchantList.get(0).get("Phone").toString());
					}
					if (!Constants.isNull(mMerchantList.get(0).get(
							"Introduction"))) {
						bundle.putString("Introduction", mMerchantList.get(0)
								.get("Introduction").toString());
					}
					if (!Constants.isNull(mMerchantList.get(0).get("Userid"))) {
						bundle.putString("Userid",
								mMerchantList.get(0).get("Userid").toString());
					}
					if (!Constants.isNull(mMerchantList.get(0).get("Id"))) {
						bundle.putString("Id", mMerchantList.get(0).get("Id")
								.toString());
					}
					if (!Constants.isNull(mMerchantList.get(0).get("Flnum"))) {
						bundle.putString("Flnum",
								mMerchantList.get(0).get("Flnum").toString());
					}

					if (mMerchantList.get(0).get("plstar") != null) {
						bundle.putString("plstar",
								mMerchantList.get(0).get("plstar").toString());
					} else {
						bundle.putString("plstar", "0");
					}
					if (!Constants.isNull(mMerchantList.get(0).get(
							"verifyState"))) {
						bundle.putString("verifyState", mMerchantList.get(0)
								.get("verifyState").toString());
					}
					if (!Constants.isNull(mMerchantList.get(0).get("Images1"))) {
						bundle.putString("Images1",
								mMerchantList.get(0).get("Images1").toString());
					}
					if (!Constants.isNull(mMerchantList.get(0).get("Images2"))) {
						bundle.putString("Images2",
								mMerchantList.get(0).get("Images2").toString());
					}
					if (!Constants.isNull(mMerchantList.get(0).get("Images3"))) {
						bundle.putString("Images3",
								mMerchantList.get(0).get("Images3").toString());
					}
					if (!Constants.isNull(mMerchantList.get(0).get("Images4"))) {
						bundle.putString("Images4",
								mMerchantList.get(0).get("Images4").toString());
					}
					if (!Constants.isNull(mMerchantList.get(0).get("Images5"))) {
						bundle.putString("Images5",
								mMerchantList.get(0).get("Images5").toString());
					}
					intent.putExtras(bundle);
					// 跳转到商家详情界面
					intent.setClass(getApplicationContext(),
							MerchantInfoActivity.class);
					startActivity(intent);
					ExitApplication.getInstance().finishMyOrder();
				}
				break;
			}
		};
	};
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
