package com.zjxfood.activity;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.util.Xml;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.project.util.DensityUtils;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.umeng.analytics.MobclickAgent;
import com.zjxfood.activity.wxapi.WXPayEntryActivity;
import com.zjxfood.application.ExitApplication;
import com.zjxfood.common.Constants;
import com.zjxfood.http.XutilsUtils;
import com.zjxfood.view.RoundImageView;
import com.zjxfood.view.TiltTextView;
import com.zjxfood.weixinpay.MD5;
import com.zjxfood.weixinpay.Util;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.xmlpull.v1.XmlPullParser;

import java.io.StringReader;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * 微信支付
 * 
 * @author zjx
 * 
 */
public class WeixinPayActivity extends AppActivity implements OnClickListener {

	// 支付
	private Button mPayBtn;
	private RelativeLayout mHeadLayout;
	// 返回
	private ImageView mBackImage;
	private PopupWindow mPopupWindow;
	private LinearLayout mAlertLayout;
	// // 取消
	// private ImageView mCancelX;
	// // 抢红包
	// private Button mGrabRedBtn;
	private static final String TAG = "MicroMsg.SDKSample.PayActivity";
	private PayReq req;
	private final IWXAPI msgApi = WXAPIFactory.createWXAPI(this, null);
	private Map<String, String> resultunifiedorder;
	private StringBuffer sb;
	private Bundle mBundle;
	// 价格、名称、id
	private String price, merchantName, mId, addressId = "", giftId, money,
			currency;
	private TextView mPriceText;
	// 图片路径
	private String mLoginImagePath;
	private RoundImageView mRoundImageView;
	private Bitmap mBitmap;
	// 返利
	private String mFlum = "0";
	private TiltTextView mFlumText;
	private TextView mMerchantName;
	private int data;// 支付结果标识
	public static String flag = "0";// 支付/激活 识别
	public static String MallOrMerchant;
	private String mallOrderId = "";// 商城订单
	private String merchantOrderId = "";// 商家订单
	private String currencyOrderId = "";// 商家订单
	private String auctionOrderId = "";// 竞拍商品订单
	private String indianaOrderId = "";// 抽奖商品订单
	private String cashOrderId;// 现金抽奖订单
	private String cashYfOrderId;// 现金抽奖运费订单
	
//	private String callBackUrl = "http://api.hexnews.com/api/payment/GiftOrderWxCallBack";// 微信商城回调地址
	private String callBackUrl = "";// 微信统一回调地址
//	private String callBackUrl = "http://open.hexnews.com/api/payment/GiftOrderWxCallBack";//测试
	private String callBackUrlJh = "http://api.hexnews.com/api/payment/UserJhWxCallBack";// 微信激活回调地址
//	private String callBackUrlJh = "http://open.hexnews.com/api/payment/UserJhWxCallBack";// 微信激活测试
//	private String callBackUrlMerchant = "http://api.hexnews.com/api/payment/OrderWxCallBack";// 微信商家回调地址
//	private String callBackUrlMerchant = "";// 微信商家回调地址
//	private String callBackUrlMerchant = "http://open.hexnews.com/api/payment/OrderWxCallBack";// 微信商家测试
	private String callBackUrlCurrency = "http://api.hexnews.com/api/Payment/CurrencyRechargeOrderWxCallBack";// 消费币支付回调地址
//	private String callBackUrlAuction = "http://api.hexnews.com/api/Payment/AuctionWxCallBack";// 竞拍商品回调
//	 private String callBackUrlIndiana ="http://api.hexnews.com/api/Payment/GameWxCallBack";// 抽奖商品回调
//	private String callBackUrlIndiana = "http://open.hexnews.com/api/Payment/GameWxCallBack";// 抽奖商品回调
//	 private String callBackUrlCash = "http://api.hexnews.com/api/Payment/CarGameNumWxCallBack";// 现金抽奖回调地址
//	 private String callBackUrlCash = "http://open.hexnews.com/api/Payment/CarGameNumWxCallBack";// 现金抽奖回调地址测试
//	 private String callBackUrlCashYf = "http://api.hexnews.com/api/Payment/CarGameWxCallBack";
//	 private String callBackUrlCashYf = "http://open.hexnews.com/api/Payment/CarGameWxCallBack";// 现金抽奖运费回调地址测试
	private boolean isPayAuiction = false;// 判断竞拍商品是否可以支付
	private boolean isPayMall = false;// 判断商城是否可以支付
	private boolean isPayMerchant = false;// 判断商家是否可以支付
	private boolean isPayCurrency = false;// 判断消费币支付是否可以支付
	private boolean isPayIndiana = false;// 判断抽奖支付是否可以支付
	private boolean isCash = false;// 现金抽奖是否可以支付
	private boolean isCashYf = false;// 现金抽奖运费是否可以支付
	private String sizeId = "";// 尺码Id
	private PopupWindow mSuccessPopup;
	private TextView mSuccessText1, mSuccessText2;
	private Button mCheckOrderBtn;
	private ImageView mSuccessXimage;
	private BitmapUtils mBitmapUtils;
	private PopupWindow mInventoryProblemPop;
	private Button mInventoryBtn;
	private TextView mInventoryText;
	private HashMap<String, Object> mMerchantMap, mMallMap, mCurrencyMap,mOrderIdMap,mCallBackMap;
	private String mTotalNum;
	private HashMap<String, Object> mCashMap;
	private String mCallBackName = "";
	private FrameLayout mFlLayout;
	private String mUseCurrency = "false";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		ExitApplication.getInstance().addActivity(this);
		ExitApplication.getInstance().addJhActivity(this);
		ExitApplication.getInstance().addMallActivity(this);
		ExitApplication.getInstance().addMallDetail(this);
		ExitApplication.getInstance().addMyCommodityActivity(this);
		ExitApplication.getInstance().addCashList(this);
		setContentView(R.layout.weixin_pay_layout);
		setImmerseLayout(findViewById(R.id.head_layout));
		init();
		mBitmapUtils = new BitmapUtils(getApplicationContext());
		mBitmapUtils.configDefaultLoadFailedImage(R.drawable.log);
		mBundle = getIntent().getExtras();
		if (mBundle != null) {
			MallOrMerchant = mBundle.getString("MallOrMerchant");
			if (MallOrMerchant.equals("merchant")) {
				mCallBackName = "MerchantOrder";
				price = mBundle.getString("price");
				if(mBundle.getString("merchantName").toString().length()>15) {
					merchantName = mBundle.getString("merchantName").substring(0, 15) + "...";
				}else{
					merchantName = mBundle.getString("merchantName").toString();
				}
				mId = mBundle.getString("mId");
				mLoginImagePath = mBundle.getString("LoginImage");
				mFlum = mBundle.getString("flum");
				flag = mBundle.getString("flag");
				money = mBundle.getString("money");
				currency = mBundle.getString("currency");
//				if (flag.equals("1")) {
//					mPriceText.setText("￥" + money);
//					if (!(mId.equals(""))) {
//						createMerchantHttp();
//					}
//				} else if (flag.equals("2")) {
//					mPriceText.setText("￥" + price);
//					generpreId();
//				}
			} else if (MallOrMerchant.equals("mall")) {
				isPayMall = false;
				mCallBackName = "StoreOrder";
				mUseCurrency = mBundle.getString("useCurrency");
				price = mBundle.getString("price");
				if(mBundle.getString("merchantName").toString().length()>15) {
					merchantName = mBundle.getString("merchantName").substring(0, 15) + "...";
				}else{
					merchantName = mBundle.getString("merchantName").toString();
				}
				mId = mBundle.getString("mId");
				mLoginImagePath = mBundle.getString("LoginImage");
				mFlum = mBundle.getString("flum");
				flag = mBundle.getString("flag");
				addressId = mBundle.getString("addressId");
				giftId = mBundle.getString("giftId");
				sizeId = mBundle.getString("sizeId");
				mallOrderId = mBundle.getString("orderId");
//				if (!(addressId.equals(""))) {
//					createMallOrder();
//				}

			} else if (MallOrMerchant.equals("currency")) {
				price = mBundle.getString("price");
				if(mBundle.getString("merchantName").toString().length()>15) {
					merchantName = mBundle.getString("merchantName").substring(0, 15) + "...";
				}else{
					merchantName = mBundle.getString("merchantName").toString();
				}
//				if (Constants.mId != null && !Constants.mId.equals("")) {
//					createCurrencyOrder();
//				}
				mPriceText.setText("￥" + price);
			} else if (MallOrMerchant.equals("auction")) {
				mFlLayout.setVisibility(View.GONE);
				mCallBackName = "AuctionOrder";
				price = mBundle.getString("price");
				if(mBundle.getString("merchantName").toString().length()>15) {
					merchantName = mBundle.getString("merchantName").substring(0, 15) + "...";
				}else{
					merchantName = mBundle.getString("merchantName").toString();
				}
				mId = mBundle.getString("mId");
				mLoginImagePath = mBundle.getString("LoginImage");
				mFlum = mBundle.getString("flum");
				giftId = mBundle.getString("giftId");
				addressId = mBundle.getString("addressId");
				sizeId = mBundle.getString("sizeId");
				auctionOrderId = mBundle.getString("orderId");

//				if (auctionOrderId != null && !auctionOrderId.equals("")) {
//					isPayAuiction = true;
//					generpreId();
//				}
				mPriceText.setText("￥" + price);
			} else if (MallOrMerchant.equals("indiana")) {
				mCallBackName = "LuckyGameOrder";
				price = mBundle.getString("price");
				if(mBundle.getString("merchantName").toString().length()>15) {
					merchantName = mBundle.getString("merchantName").substring(0, 15) + "...";
				}else{
					merchantName = mBundle.getString("merchantName").toString();
				}
				mId = mBundle.getString("mId");
				mLoginImagePath = mBundle.getString("LoginImage");
				mFlum = mBundle.getString("flum");
				giftId = mBundle.getString("giftId");
				addressId = mBundle.getString("addressId");
				sizeId = mBundle.getString("sizeId");
				indianaOrderId = mBundle.getString("orderId");Log.i("indianaOrderId", indianaOrderId + "===========");
//				if (indianaOrderId != null) {
//					isPayIndiana = true;
//					generpreId();
//				}
				mPriceText.setText("￥" + price);
			}else if (MallOrMerchant.equals("xjdb")) {
				mCallBackName = "CarGameNumOrder";
				price = mBundle.getString("price");
				if(mBundle.getString("merchantName").toString().length()>15) {
					merchantName = mBundle.getString("merchantName").substring(0, 15) + "...";
				}else{
					merchantName = mBundle.getString("merchantName").toString();
				}
				giftId = mBundle.getString("giftId");
				mTotalNum = mBundle.getString("totalNum");
//				if (Constants.mId != null && !Constants.mId.equals("")) {
//					createCashOrder();
//				}
				mPriceText.setText("￥" + price);
			}
			else if (MallOrMerchant.equals("cashYf")) {
				mCallBackName = "CarGameOrder";
				price = mBundle.getString("price");
				if(mBundle.getString("merchantName").toString().length()>15) {
					merchantName = mBundle.getString("merchantName").substring(0, 15) + "...";
				}else{
					merchantName = mBundle.getString("merchantName").toString();
				}
				giftId = mBundle.getString("giftId");
				addressId = mBundle.getString("addressId");
				sizeId = mBundle.getString("sizeId");
				cashYfOrderId = mBundle.getString("orderId");
//				if (cashYfOrderId != null) {
//					isCashYf = true;
//					generpreId();
//				}
				mPriceText.setText("￥" + price);
			}
		}
		mFlumText.setText("返利" + mFlum + "%");
		mMerchantName.setText(merchantName);
		req = new PayReq();
		sb = new StringBuffer();

		msgApi.registerApp(Constants.APP_ID);
		mBitmapUtils.display(mRoundImageView, mLoginImagePath);
		IntentFilter filter = new IntentFilter(WXPayEntryActivity.action);
		registerReceiver(mBroadcastReceiver, filter);
		callBack();
		Log.i("微信支付标识", flag + "===========");
	}

	private void init() {
		mFlLayout = (FrameLayout) findViewById(R.id.weixin_pay_rebate_text_layout);
		mPayBtn = (Button) findViewById(R.id.weixin_liji_pay_btn);
		mHeadLayout = (RelativeLayout) findViewById(R.id.title_weixin_pay_id);
		mBackImage = (ImageView) mHeadLayout
				.findViewById(R.id.pay_weixin_back_image);
		mAlertLayout = (LinearLayout) findViewById(R.id.weixin_pay_alert_view);
		mPriceText = (TextView) findViewById(R.id.weixin_pay_money_text);
		mRoundImageView = (RoundImageView) findViewById(R.id.round_image_view);
		mFlumText = (TiltTextView) findViewById(R.id.weixin_pay_rebate_text);
		mMerchantName = (TextView) findViewById(R.id.weixin_pay_name_text);

		mPayBtn.setOnClickListener(this);
		mBackImage.setOnClickListener(this);
		mAlertLayout.setOnClickListener(this);
	}
	//获取统一回调地址
	private void callBack() {
		StringBuilder sb = new StringBuilder();
		sb.append("name=" +mCallBackName+ "&paymenttype=wx");
		XutilsUtils.get(Constants.callback, sb,
				new RequestCallBack<String>() {
					@Override
					public void onFailure(HttpException arg0, String arg1) {
					}
					@Override
					public void onSuccess(ResponseInfo<String> res) {
						mCallBackMap = Constants.getJsonObject(res.result);
						if(mCallBackMap!=null && mCallBackMap.size()>0){
							if(mCallBackMap.get("Code")!=null && mCallBackMap.get("Code").toString().equals("200")){
								callBackUrl = mCallBackMap.get("Data").toString();
							}
						}
						handler.sendEmptyMessageDelayed(12,0);
						Log.i("回调地址",res.result+"================回调地址："+callBackUrl);
					}
				});
	}
	// 生成prepay_id
	private void generpreId() {
		GetPrepayIdTask getPrepayId = new GetPrepayIdTask();
		getPrepayId.execute();
	}

	@Override
	public void onClick(View v) {
		// Intent intent = new Intent();
		Log.i("btn", "==========支付1==========");
		switch (v.getId()) {
		case R.id.weixin_liji_pay_btn:
			if (Constants.mId != null && !(Constants.mId.equals(""))) {
				if (MallOrMerchant.equals("mall")) {
					if (isPayMall) {
						try {
							genPayReq();
						} catch (Exception e) {
							e.printStackTrace();
						}
					} else {
						if (mMerchantMap != null
								&& mMerchantMap.get("Message") != null) {
							Toast.makeText(WeixinPayActivity.this,
									mMerchantMap.get("Message").toString(),
									Toast.LENGTH_SHORT).show();
						} else {
							Toast.makeText(WeixinPayActivity.this, "订单生成失败",
									Toast.LENGTH_SHORT).show();
						}
					}
				} else if (MallOrMerchant.equals("merchant")) {
					if (flag.equals("1")) {
						if (isPayMerchant) {
							genPayReq();
						} else {
							if (mMerchantMap != null
									&& mMerchantMap.get("Message") != null) {
								Toast.makeText(WeixinPayActivity.this,
										mMerchantMap.get("Message").toString(),
										Toast.LENGTH_SHORT).show();
							} else {
								Toast.makeText(WeixinPayActivity.this,
										"订单生成失败", Toast.LENGTH_SHORT).show();
							}
						}
					} else if (flag.equals("2")) {
						genPayReq();
					}
				} else if (MallOrMerchant.equals("currency")) {
					if (isPayCurrency) {
						genPayReq();
					} else {
						if (mCurrencyMap != null
								&& mCurrencyMap.get("Message") != null) {
							Toast.makeText(WeixinPayActivity.this,
									mCurrencyMap.get("Message").toString(),
									Toast.LENGTH_SHORT).show();
						} else {
							Toast.makeText(WeixinPayActivity.this, "订单生成失败",
									Toast.LENGTH_SHORT).show();
						}
					}
				} else if (MallOrMerchant.equals("auction")) {
					if (isPayAuiction) {
						Log.i("auction", "==========支付1==========");
						genPayReq();
					}
				} else if (MallOrMerchant.equals("indiana")) {
					if (isPayIndiana) {
						Log.i("indiana", "==========支付2==========");
						genPayReq();
					} else {
						Toast.makeText(getApplicationContext(), "支付失败",
								Toast.LENGTH_SHORT).show();
					}
				}else if (MallOrMerchant.equals("xjdb")) {
					if (isCash) {
						Log.i("indiana", "==========支付2==========");
						genPayReq();
					} else {
						Toast.makeText(getApplicationContext(), "支付失败",
								Toast.LENGTH_SHORT).show();
					}
				}else if (MallOrMerchant.equals("cashYf")) {
					if (isCashYf) {
						Log.i("cashYf", "==========支付3==========");
						genPayReq();
					} else {
						Toast.makeText(getApplicationContext(), "支付失败",
								Toast.LENGTH_SHORT).show();
					}
				}
			} else {
				Toast.makeText(getApplicationContext(), "支付失败,请重新登录！",
						Toast.LENGTH_SHORT).show();
			}
			break;
		case R.id.pay_weixin_back_image:
			if (mInventoryProblemPop != null
					&& mInventoryProblemPop.isShowing()) {
				mInventoryProblemPop.dismiss();
			}
			finish();
			break;
		case R.id.weixin_pay_alert_view:

			break;
		}
	}

	Runnable payRun = new Runnable() {
		@Override
		public void run() {
			handler.sendEmptyMessageDelayed(7, 0);
		}
	};

	BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context arg0, Intent intent) {
			Log.i("weixinpay", intent.getExtras().getString("data")
					+ "=============结果查询============");
			data = Integer.parseInt(intent.getExtras().getString("data"));
			switch (data) {
			case 1:
				mAlertLayout.setVisibility(View.VISIBLE);
				LayoutInflater inflater = LayoutInflater
						.from(getApplicationContext());
				View view = inflater.inflate(R.layout.popup_pay_success_layout,
						null);
				mSuccessText1 = (TextView) view
						.findViewById(R.id.popup_pay_success_text1);
				mSuccessText2 = (TextView) view
						.findViewById(R.id.popup_pay_success_text2);
				mCheckOrderBtn = (Button) view
						.findViewById(R.id.pay_success_order_btn);
				mSuccessXimage = (ImageView) view
						.findViewById(R.id.popup_pay_success_x);
				mSuccessPopup = new PopupWindow(view, DensityUtils.dp2px(
						getApplicationContext(), 250),
						LayoutParams.WRAP_CONTENT, false);
				mSuccessPopup.setBackgroundDrawable(new BitmapDrawable());
				// mSuccessPopup.setOutsideTouchable(true);
				// mSuccessPopup.setFocusable(true);
				mSuccessPopup.showAtLocation(mHeadLayout, Gravity.CENTER, 0, 0);

				mCheckOrderBtn.setOnClickListener(mOrderClick);
				mSuccessText1.setText("恭喜你，成功支付" + money + "元");
				mSuccessText2.setText("并且获得" + (int) Float.parseFloat(money)
						+ "个食尚币");
				mSuccessXimage.setOnClickListener(mOrderListClick);
				break;
			case 2:
				Intent intentJh = new Intent();
				intentJh.setClass(getApplicationContext(),
						ActivationResultActivity.class);
				Bundle bundle = new Bundle();
				bundle.putString("result", "success");
				bundle.putString("zffs", "wx");
				intentJh.putExtras(bundle);
				startActivity(intentJh);
				finish();
				break;
			case 3:
				new Thread(mallRun).start();
				break;
				//2元购车成功回调
			case 4:
				ExitApplication.getInstance().finishCash();
				break;
			}
		}
	};

	// 订单列表页面
	OnClickListener mOrderListClick = new OnClickListener() {
		@Override
		public void onClick(View arg0) {
			mSuccessPopup.dismiss();
			mAlertLayout.setVisibility(View.GONE);
			Intent intent = new Intent();
			intent = new Intent();
			intent.setClass(getApplicationContext(), MyOrderActivity.class);
			startActivity(intent);
			ExitApplication.getInstance().finishMyCommdity();
			System.gc();
		}
	};
	// 进入订单详情页面
	OnClickListener mOrderClick = new OnClickListener() {
		@Override
		public void onClick(View arg0) {
			mSuccessPopup.dismiss();
			mAlertLayout.setVisibility(View.GONE);
			Intent intent = new Intent();
			intent = new Intent();
			Bundle bundle = new Bundle();
			bundle.putString("id", "");
			bundle.putString("payId", merchantOrderId);
			intent.setClass(getApplicationContext(), MyOrderInfoActivity.class);
			intent.putExtras(bundle);
			startActivity(intent);
			ExitApplication.getInstance().finishMyCommdity();
			System.gc();
		}
	};

	// 商城购物
	Runnable mallRun = new Runnable() {
		@Override
		public void run() {
			handler.sendEmptyMessageDelayed(7, 0);
		}
	};

	// 抢红包
	android.view.View.OnClickListener mGrabRedClick = new OnClickListener() {
		@Override
		public void onClick(View v) {
			Intent intent = new Intent();
			intent.setClass(getApplicationContext(), GrabRedActivity.class);
			startActivity(intent);
			mPopupWindow.dismiss();
			handler.sendEmptyMessageDelayed(1, 0);
		}
	};

	OnClickListener mClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			mPopupWindow.dismiss();
			handler.sendEmptyMessageDelayed(1, 0);
		}
	};

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		switch (keyCode) {
		case KeyEvent.KEYCODE_BACK:
			if (mInventoryProblemPop != null
					&& mInventoryProblemPop.isShowing()) {
				mInventoryProblemPop.dismiss();
			}
			finish();
			break;

		default:
			break;
		}
		return super.onKeyDown(keyCode, event);
	}

	Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			LayoutInflater inflater = LayoutInflater
					.from(getApplicationContext());
			View view;
			Intent intent = new Intent();
			switch (msg.what) {
			case 1:
				mAlertLayout.setVisibility(View.GONE);
				break;
			case 2:
				// 支付
				sendPayReq();
				break;
			case 3:
				mRoundImageView.setImageBitmap(mBitmap);
				break;
			case 4:
				intent.setClass(getApplicationContext(),
						MallOrderActivity.class);
				startActivity(intent);
				ExitApplication.getInstance().finishMall();
				Log.i("weixin", "========商城购物成功============");
				break;
			case 5:
//				price = mOrderIdMap.get("PayMoney").toString();
//				if(mOrderIdMap.get("PayMoney")!=null) {

//				}
				mPriceText.setText(price);
				isPayMall = true;
				generpreId();
				Log.i("mallOrderId", mallOrderId + "===================");
				break;
			case 6:
				Log.i("mMerchantMap", mMerchantMap + "=============");
				if (mMerchantMap.get("Code") != null
						&& mMerchantMap.get("Code").toString().equals("200")) {
					mOrderIdMap = Constants.getJsonObject(mMerchantMap.get("Data").toString());
					merchantOrderId = mOrderIdMap.get("PayId").toString();
					generpreId();
					isPayMerchant = true;
				}else if(mMerchantMap.get("Message")!=null){
					Toast.makeText(getApplicationContext(),mMerchantMap.get("Message").toString(),Toast.LENGTH_SHORT).show();
				}
				Log.i("merchantOrderId", merchantOrderId
						+ "===================");
				break;
			case 7:
				Toast.makeText(getApplicationContext(), "购买成功！",
						Toast.LENGTH_SHORT).show();
				intent = new Intent();
				intent.setClass(getApplicationContext(),
						MallOrderActivity.class);
				startActivity(intent);
				ExitApplication.getInstance().finishMall();
				Log.i("mall", "商城购物成功！=========================");
				break;
			case 8:
				try {
					inflater = LayoutInflater.from(getApplicationContext());
					view = inflater.inflate(
							R.layout.popup_inventory_problem_layout, null);
					mInventoryProblemPop = new PopupWindow(view,
							LayoutParams.MATCH_PARENT,
							LayoutParams.MATCH_PARENT, false);
					mInventoryProblemPop
							.setBackgroundDrawable(new BitmapDrawable());
					mInventoryProblemPop.showAtLocation(mPayBtn,
							Gravity.CENTER, 0, 0);
					mInventoryBtn = (Button) view
							.findViewById(R.id.popup_inventory_confirm_btn);
					mInventoryText = (TextView) view
							.findViewById(R.id.popup_inventory_text);
//					mInventoryText.setText("支付失败，该商品库存不足！");
					if(mMallMap!=null && mMallMap.get("Message")!=null){
						mInventoryText.setText(mMallMap.get("Message").toString());
						}
					mInventoryBtn.setOnClickListener(mInventoryClick);
				} catch (Exception e) {
					e.printStackTrace();
				}
				break;
			case 9:
				try {
					inflater = LayoutInflater.from(getApplicationContext());
					view = inflater.inflate(
							R.layout.popup_inventory_problem_layout, null);
					mInventoryProblemPop = new PopupWindow(view,
							LayoutParams.MATCH_PARENT,
							LayoutParams.MATCH_PARENT, false);
					mInventoryProblemPop
							.setBackgroundDrawable(new BitmapDrawable());
					mInventoryProblemPop.showAtLocation(mPayBtn,
							Gravity.CENTER, 0, 0);
					mInventoryText = (TextView) view
							.findViewById(R.id.popup_inventory_text);
					mInventoryBtn = (Button) view
							.findViewById(R.id.popup_inventory_confirm_btn);
					mInventoryBtn.setOnClickListener(mInventoryClick);
					mInventoryText.setText("支付失败，收货地址被删除！");
				} catch (Exception e) {
					e.printStackTrace();
				}
				break;
			case 10:
				if (mCurrencyMap.get("Data") != null) {
					currencyOrderId = mCurrencyMap.get("Data").toString();
					isPayCurrency = true;
					generpreId();
				}
				break;
			case 11:
				isCash = true;
				cashOrderId = mCashMap.get("Data").toString();
				generpreId();
				Log.i("cashOrderId", cashOrderId + "===================");
				break;
			case 12:
					if (MallOrMerchant.equals("merchant")) {
						if (flag.equals("1")) {
							mPriceText.setText("￥" + money);
							if (!(mId.equals(""))) {
								createMerchantHttp();
							}
						} else if (flag.equals("2")) {
							mPriceText.setText("￥" + price);
							generpreId();
						}
					} else if (MallOrMerchant.equals("mall")) {
						mPriceText.setText(price);
						isPayMall = true;
						generpreId();
						Log.i("mallOrderId", mallOrderId + "===================");
						//创建商城订单
//						if (!(addressId.equals(""))) {
//							createMallOrder();
//						}
					} else if (MallOrMerchant.equals("currency")) {
						if (Constants.mId != null && !Constants.mId.equals("")) {
							createCurrencyOrder();
						}
						mPriceText.setText("￥" + price);
					} else if (MallOrMerchant.equals("auction")) {
						if (auctionOrderId != null && !auctionOrderId.equals("")) {
							isPayAuiction = true;
							generpreId();
						}
					} else if (MallOrMerchant.equals("indiana")) {
						if (indianaOrderId != null) {
							isPayIndiana = true;
							generpreId();
						}
					}else if (MallOrMerchant.equals("xjdb")) {
						if (Constants.mId != null && !Constants.mId.equals("")) {
							createCashOrder();
						}
					}
					else if (MallOrMerchant.equals("cashYf")) {
						if (cashYfOrderId != null) {
							isCashYf = true;
							generpreId();
						}
					}
					break;
			}
		};
	};

	OnClickListener mInventoryClick = new OnClickListener() {

		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			mInventoryProblemPop.dismiss();
			ExitApplication.getInstance().finishMallDetail();
		}
	};

	/**
	 * 生成签名
	 */

	private String genPackageSign(List<NameValuePair> params) {
		StringBuilder sb = new StringBuilder();

		for (int i = 0; i < params.size(); i++) {
			sb.append(params.get(i).getName());
			sb.append('=');
			sb.append(params.get(i).getValue());
			sb.append('&');
		}
		sb.append("key=");
		sb.append(Constants.API_KEY);

		String packageSign = MD5.getMessageDigest(sb.toString().getBytes())
				.toUpperCase();
		Log.e("orion", packageSign);
		return packageSign;
	}

	private String genAppSign(List<NameValuePair> params) {
		StringBuilder sb = new StringBuilder();

		for (int i = 0; i < params.size(); i++) {
			sb.append(params.get(i).getName());
			sb.append('=');
			sb.append(params.get(i).getValue());
			sb.append('&');
		}
		sb.append("key=");
		sb.append(Constants.API_KEY);

		this.sb.append("sign str\n" + sb.toString() + "\n\n");
		String appSign = MD5.getMessageDigest(sb.toString().getBytes())
				.toUpperCase();
		Log.e("orion", appSign);
		return appSign;
	}

	private String toXml(List<NameValuePair> params) {
		StringBuilder sb = new StringBuilder();
		sb.append("<xml>");
		for (int i = 0; i < params.size(); i++) {
			sb.append("<" + params.get(i).getName() + ">");

			sb.append(params.get(i).getValue());
			sb.append("</" + params.get(i).getName() + ">");
		}
		sb.append("</xml>");

		Log.e("orion", sb.toString());
		return sb.toString();
	}

	private class GetPrepayIdTask extends
			AsyncTask<Void, Void, Map<String, String>> {
		private ProgressDialog dialog;

		@Override
		protected void onPreExecute() {
			try {
				dialog = ProgressDialog.show(WeixinPayActivity.this,
						getString(R.string.app_tip),
						getString(R.string.getting_prepayid));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		@Override
		protected void onPostExecute(Map<String, String> result) {
			try {
				if (dialog != null && dialog.isShowing()) {
					dialog.dismiss();
				}
				sb.append("prepay_id\n" + result.get("prepay_id") + "\n\n");
				// show.setText(sb.toString());
				resultunifiedorder = result;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		@Override
		protected void onCancelled() {
			super.onCancelled();
		}

		@Override
		protected Map<String, String> doInBackground(Void... params) {
			
			String entity = "";
			String url = String
					.format("https://api.mch.weixin.qq.com/pay/unifiedorder");
			if (MallOrMerchant.equals("merchant")) {
				Log.i("merhcant", "===========1============");
				entity = genProductArgsMerchant();
			} else if (MallOrMerchant.equals("mall")) {
				entity = genProductArgs();
			} else if (MallOrMerchant.equals("currency")) {
				entity = genProductCurrency();
			} else if (MallOrMerchant.equals("auction")) {
				Log.i("auction", "============竞拍夺宝============"+callBackUrl);
				entity = genProductAuction();
			} else if (MallOrMerchant.equals("indiana")) {
				Log.i("indiana", "============支付3============");
				entity = genProductOrder(merchantName, callBackUrl,
						indianaOrderId);
			}else if (MallOrMerchant.equals("xjdb")) {
				Log.i("xjdb", "============支付4============");
				entity = genProductOrder(merchantName, callBackUrl,
						cashOrderId);
			}else if (MallOrMerchant.equals("cashYf")) {
				Log.i("cashYf", "============支付5============");
				entity = genProductOrder(merchantName, callBackUrl,
						cashYfOrderId);
			}
			// Log.e("orion", entity);
			
			byte[] buf = Util.httpPost(url, entity);
			Map<String, String> xml = null;
			String content;
			try{
				content = new String(buf);
				Log.e("orion", content);
				xml = decodeXml(content);
			}catch(Exception e){
				e.printStackTrace();
			}
			return xml;
		}
	}

	public Map<String, String> decodeXml(String content) {

		try {
			Map<String, String> xml = new HashMap<String, String>();
			XmlPullParser parser = Xml.newPullParser();
			parser.setInput(new StringReader(content));
			int event = parser.getEventType();
			while (event != XmlPullParser.END_DOCUMENT) {

				String nodeName = parser.getName();
				switch (event) {
				case XmlPullParser.START_DOCUMENT:

					break;
				case XmlPullParser.START_TAG:

					if ("xml".equals(nodeName) == false) {
						// 实例化student对象
						xml.put(nodeName, parser.nextText());
					}
					break;
				case XmlPullParser.END_TAG:
					break;
				}
				event = parser.next();
			}

			return xml;
		} catch (Exception e) {
			Log.e("orion", e.toString());
		}
		return null;

	}

	private String genNonceStr() {
		Random random = new Random();
		return MD5.getMessageDigest(String.valueOf(random.nextInt(10000))
				.getBytes());
	}

	private long genTimeStamp() {
		return System.currentTimeMillis() / 1000;
	}

	private String genOutTradNo() {
		Random random = new Random();
		return MD5.getMessageDigest(String.valueOf(random.nextInt(10000))
				.getBytes());
	}

	// 商城订单生成
	private String genProductArgs() {
		StringBuffer xml = new StringBuffer();
		try {
			String nonceStr = genNonceStr();

			xml.append("</xml>");
			List<NameValuePair> packageParams = new LinkedList<NameValuePair>();
			packageParams
					.add(new BasicNameValuePair("appid", Constants.APP_ID));
			packageParams.add(new BasicNameValuePair("body", "[食尚男女]"
					+ merchantName));
			packageParams
					.add(new BasicNameValuePair("mch_id", Constants.MCH_ID));
			packageParams.add(new BasicNameValuePair("nonce_str", nonceStr));
			// 回调地址
			packageParams
					.add(new BasicNameValuePair("notify_url", callBackUrl));
			Log.i("weixinOrderid",
					genOutTradNo() + "========genOutTradNo========="
							+ mallOrderId.replaceAll("\"", ""));
			// 商户订单号
			packageParams.add(new BasicNameValuePair("out_trade_no",
					mallOrderId.replaceAll("\"", "")));
			packageParams.add(new BasicNameValuePair("spbill_create_ip",
					"127.0.0.1"));

			String s = (Float.parseFloat(price)) * 100 + "";
			s.indexOf(".");
			Log.i("price", s.indexOf(".") + "======s.indexOf()=========");
			s = s.substring(0, s.indexOf("."));
			Log.i("price", s + "======s=========");
			packageParams.add(new BasicNameValuePair("total_fee", s));
			packageParams.add(new BasicNameValuePair("trade_type", "APP"));

			String sign = genPackageSign(packageParams);
			packageParams.add(new BasicNameValuePair("sign", sign));

			String xmlstring = toXml(packageParams);

			return new String(xmlstring.toString().getBytes(), "ISO8859-1");

		} catch (Exception e) {
//			Log.e(TAG, "==========================" + e.getMessage());
			return null;
		}
	}

	// 竞拍订单生成
	private String genProductAuction() {
		StringBuffer xml = new StringBuffer();
		try {
			String nonceStr = genNonceStr();

			xml.append("</xml>");
			List<NameValuePair> packageParams = new LinkedList<NameValuePair>();
			packageParams
					.add(new BasicNameValuePair("appid", Constants.APP_ID));
			packageParams.add(new BasicNameValuePair("body", "[食尚男女]"
					+ merchantName));
			packageParams
					.add(new BasicNameValuePair("mch_id", Constants.MCH_ID));
			packageParams.add(new BasicNameValuePair("nonce_str", nonceStr));
			// 回调地址
			Log.i("回调地址",callBackUrl+"======================");
			packageParams.add(new BasicNameValuePair("notify_url",
					callBackUrl));
			// 商户订单号
			packageParams.add(new BasicNameValuePair("out_trade_no",
					auctionOrderId));
			packageParams.add(new BasicNameValuePair("spbill_create_ip",
					"127.0.0.1"));

			String s = (Float.parseFloat(price)) * 100 + "";
			s.indexOf(".");
			Log.i("price", s.indexOf(".") + "======s.indexOf()=========");
			s = s.substring(0, s.indexOf("."));
			Log.i("price", s + "======s=========");
			packageParams.add(new BasicNameValuePair("total_fee", s));
			packageParams.add(new BasicNameValuePair("trade_type", "APP"));

			String sign = genPackageSign(packageParams);
			packageParams.add(new BasicNameValuePair("sign", sign));

			String xmlstring = toXml(packageParams);

			return new String(xmlstring.toString().getBytes(), "ISO8859-1");

		} catch (Exception e) {
//			Log.e(TAG, "==========================" + e.getMessage());
			return null;
		}
	}

	// 抽奖订单生成
	private String genProductOrder(String name, String callBack, String orderId) {
		StringBuffer xml = new StringBuffer();
		try {
			String nonceStr = genNonceStr();

			xml.append("</xml>");
			List<NameValuePair> packageParams = new LinkedList<NameValuePair>();
			packageParams
					.add(new BasicNameValuePair("appid", Constants.APP_ID));
			packageParams.add(new BasicNameValuePair("body", "[食尚男女]" + name));
			packageParams
					.add(new BasicNameValuePair("mch_id", Constants.MCH_ID));
			packageParams.add(new BasicNameValuePair("nonce_str", nonceStr));
			// 回调地址
			packageParams.add(new BasicNameValuePair("notify_url", callBack));
			// 商户订单号
			packageParams.add(new BasicNameValuePair("out_trade_no", orderId));
			packageParams.add(new BasicNameValuePair("spbill_create_ip",
					"127.0.0.1"));

			String s = (Float.parseFloat(price)) * 100 + "";
			s.indexOf(".");
			Log.i("price", s.indexOf(".") + "======s.indexOf()=========");
			s = s.substring(0, s.indexOf("."));
			Log.i("price", s + "======s=========");
			packageParams.add(new BasicNameValuePair("total_fee", s));
			packageParams.add(new BasicNameValuePair("trade_type", "APP"));

			String sign = genPackageSign(packageParams);
			packageParams.add(new BasicNameValuePair("sign", sign));

			String xmlstring = toXml(packageParams);

			return new String(xmlstring.toString().getBytes(), "ISO8859-1");

		} catch (Exception e) {
//			Log.e(TAG, "==========================" + e.getMessage());
			return null;
		}
	}
//	private String genIndianaOrder() {
//		StringBuffer xml = new StringBuffer();
//		try {
//			String nonceStr = genNonceStr();
//
//			xml.append("</xml>");
//			List<NameValuePair> packageParams = new LinkedList<NameValuePair>();
//			packageParams
//					.add(new BasicNameValuePair("appid", Constants.APP_ID));
//			packageParams.add(new BasicNameValuePair("body", "[食尚男女]"
//					+ merchantName));
//			packageParams
//					.add(new BasicNameValuePair("mch_id", Constants.MCH_ID));
//			packageParams.add(new BasicNameValuePair("nonce_str", nonceStr));
//			// 回调地址
//			packageParams
//					.add(new BasicNameValuePair("notify_url", callBackUrlIndiana));
//			// 商户订单号
//			packageParams.add(new BasicNameValuePair("out_trade_no",
//					indianaOrderId));
//			packageParams.add(new BasicNameValuePair("spbill_create_ip",
//					"127.0.0.1"));
//
//			String s = (Float.parseFloat(price)) * 100 + "";
////			s.indexOf(".");
//			Log.i("price", s.indexOf(".") + "======s.indexOf()=========");
//			s = s.substring(0, s.indexOf("."));
//			Log.i("price", s + "======s=========");
//			packageParams.add(new BasicNameValuePair("total_fee", s));
//			packageParams.add(new BasicNameValuePair("trade_type", "APP"));
//			String sign = genPackageSign(packageParams);
//			packageParams.add(new BasicNameValuePair("sign", sign));
//			String xmlstring = toXml(packageParams);
//			return new String(xmlstring.toString().getBytes(), "ISO8859-1");
//		} catch (Exception e) {
//			Log.e(TAG, "==========================" + e.getMessage());
//			return null;
//		}
//	}
	// 商家生成
	private String genProductArgsMerchant() {
		StringBuffer xml = new StringBuffer();
		try {
			String nonceStr = genNonceStr();

			xml.append("</xml>");
			List<NameValuePair> packageParams = new LinkedList<NameValuePair>();
			packageParams
					.add(new BasicNameValuePair("appid", Constants.APP_ID));
			packageParams.add(new BasicNameValuePair("body", "[食尚男女]"
					+ merchantName));
			packageParams
					.add(new BasicNameValuePair("mch_id", Constants.MCH_ID));
			packageParams.add(new BasicNameValuePair("nonce_str", nonceStr));
			String s = "";
			if (flag.equals("1")) {// 商家
				packageParams.add(new BasicNameValuePair("notify_url",
						callBackUrl));
				// 商户订单号
				packageParams.add(new BasicNameValuePair("out_trade_no",
						merchantOrderId.replaceAll("\"", "")));
				s = (Float.parseFloat(money)) * 100 + "";
				Log.i("merhcant", "===========merchant1============");
			} else if (flag.equals("2")) {// 激活

				packageParams.add(new BasicNameValuePair("notify_url",
						callBackUrlJh));
				String orderId = getOrderId();
				// 商户订单号 18625586699
				Log.i("merhcant", orderId + "===========微信激活========");
				packageParams.add(new BasicNameValuePair("out_trade_no",
						orderId));
				s = (Float.parseFloat(price)) * 100 + "";
			}

			packageParams.add(new BasicNameValuePair("spbill_create_ip",
					"127.0.0.1"));

			s.indexOf(".");
			Log.i("price", s.indexOf(".") + "======s.indexOf()=========");
			s = s.substring(0, s.indexOf("."));
			Log.i("price", s + "======s=========");
			packageParams.add(new BasicNameValuePair("total_fee", s));
			packageParams.add(new BasicNameValuePair("trade_type", "APP"));

			String sign = genPackageSign(packageParams);
			packageParams.add(new BasicNameValuePair("sign", sign));

			String xmlstring = toXml(packageParams);

			return new String(xmlstring.toString().getBytes(), "ISO8859-1");

		} catch (Exception e) {
//			Log.e(TAG, "==========================" + e.getMessage());
			return null;
		}
	}

	// 消费币订单生成
	private String genProductCurrency() {
		StringBuffer xml = new StringBuffer();
		try {
			String nonceStr = genNonceStr();

			xml.append("</xml>");
			List<NameValuePair> packageParams = new LinkedList<NameValuePair>();
			packageParams
					.add(new BasicNameValuePair("appid", Constants.APP_ID));
			packageParams.add(new BasicNameValuePair("body", "[食尚男女]"
					+ merchantName));
			packageParams
					.add(new BasicNameValuePair("mch_id", Constants.MCH_ID));
			packageParams.add(new BasicNameValuePair("nonce_str", nonceStr));
			// 回调地址
			packageParams.add(new BasicNameValuePair("notify_url",
					callBackUrlCurrency));
			Log.i("weixinOrderid",
					genOutTradNo() + "========genOutTradNo========="
							+ currencyOrderId.replaceAll("\"", ""));
			// 商户订单号
			packageParams.add(new BasicNameValuePair("out_trade_no",
					currencyOrderId.replaceAll("\"", "")));
			packageParams.add(new BasicNameValuePair("spbill_create_ip",
					"127.0.0.1"));

			String s = (Float.parseFloat(price)) * 100 + "";
			s.indexOf(".");
			Log.i("price", s.indexOf(".") + "======s.indexOf()=========");
			s = s.substring(0, s.indexOf("."));
			Log.i("price", s + "======s=========");
			packageParams.add(new BasicNameValuePair("total_fee", s));
			packageParams.add(new BasicNameValuePair("trade_type", "APP"));

			String sign = genPackageSign(packageParams);
			packageParams.add(new BasicNameValuePair("sign", sign));

			String xmlstring = toXml(packageParams);

			return new String(xmlstring.toString().getBytes(), "ISO8859-1");

		} catch (Exception e) {
//			Log.e(TAG, "==========================" + e.getMessage());
			return null;
		}
	}

	private void genPayReq() {

		req.appId = Constants.APP_ID;
		req.partnerId = Constants.MCH_ID;
		req.prepayId = resultunifiedorder.get("prepay_id");
		req.packageValue = "Sign=WXPay";
		req.nonceStr = genNonceStr();
		req.timeStamp = String.valueOf(genTimeStamp());
		List<NameValuePair> signParams = new LinkedList<NameValuePair>();
		signParams.add(new BasicNameValuePair("appid", req.appId));
		signParams.add(new BasicNameValuePair("noncestr", req.nonceStr));
		signParams.add(new BasicNameValuePair("package", req.packageValue));
		signParams.add(new BasicNameValuePair("partnerid", req.partnerId));
		signParams.add(new BasicNameValuePair("prepayid", req.prepayId));
		signParams.add(new BasicNameValuePair("timestamp", req.timeStamp));

		req.sign = genAppSign(signParams);

		sb.append("sign\n" + req.sign + "\n\n");

		// show.setText(sb.toString());

		Log.e("orion", signParams.toString());
		handler.sendEmptyMessageDelayed(2, 0);
	}

	private void sendPayReq() {
		msgApi.registerApp(Constants.APP_ID);
		msgApi.sendReq(req);
	}

	public String getOrderId() {
		String str = System.currentTimeMillis() + "";

		String key = str.substring(3, 12);
		return key + "-" + Constants.mId;
	}

	private void createMallOrder() {
		StringBuilder sb = new StringBuilder();
		if (!(sizeId.equals(""))) {
			sb.append("userid=" + Constants.mId + "&money=" + price
					+ "&addressid=" + addressId + "&remark=&giftid=" + giftId
					+ "&chimaid=" + sizeId+ "&useCurrency="+mUseCurrency);
		} else {
			sb.append("userid=" + Constants.mId + "&money=" + price + "&addressid="
					+ addressId + "&remark=&giftid=" + giftId+ "&useCurrency="+mUseCurrency);
		}
		XutilsUtils.get(Constants.createMallOrder3, sb,
				new RequestCallBack<String>() {
					@Override
					public void onFailure(HttpException arg0, String arg1) {
					}

					@Override
					public void onSuccess(ResponseInfo<String> res) {
						Log.i("createMallOrder2", res.result
								+ "========-1==============");
						mMallMap = Constants.getJsonObject(res.result);
						if (mMallMap != null && mMallMap.size() > 0) {
							if (mMallMap.get("Code").toString().equals("200")
									&& mMallMap.get("Data") != null) {
								mOrderIdMap = Constants.getJsonObject(mMallMap.get("Data").toString());
								mallOrderId = mOrderIdMap.get("PayId").toString();
//								mallOrderId = mMallMap.get("Data").toString();
								handler.sendEmptyMessageDelayed(5, 0);
							} else {
								handler.sendEmptyMessageDelayed(8, 0);
							}
						}
					}

				});
	}

	// 消费币充值订单
	private void createCurrencyOrder() {
		StringBuilder sb = new StringBuilder();
		sb.append("uid=" + Constants.mId + "&money=" + price);
		XutilsUtils.get(Constants.createrechargeorder, sb,
				new RequestCallBack<String>() {
					@Override
					public void onFailure(HttpException arg0, String arg1) {
					}

					@Override
					public void onSuccess(ResponseInfo<String> res) {
						Log.i("createrechargeorder", res.result
								+ "==================");
						mCurrencyMap = Constants.getJsonObject(res.result);
						if (mCurrencyMap != null
								&& mCurrencyMap.get("Code") != null
								&& mCurrencyMap.get("Code").toString()
										.equals("200")) {
							handler.sendEmptyMessageDelayed(10, 0);
						}
					}
				});
	}

	private void createMerchantHttp() {
		StringBuilder sb = new StringBuilder();
		sb.append("userId=" + Constants.mId + "&merchantUserId=" + mId + "&total=" + price
				+ "&money=" + money + "&currency=" + currency);
		XutilsUtils.get(Constants.createMerchantOrder3, sb,
				new RequestCallBack<String>() {
					@Override
					public void onFailure(HttpException arg0, String arg1) {
					}

					@Override
					public void onSuccess(ResponseInfo<String> res) {
						mMerchantMap = Constants.getJsonObject(res.result);
						if (mMerchantMap != null && mMerchantMap.size() > 0) {
							handler.sendEmptyMessageDelayed(6, 0);
						}
					}
				});
	}
	// 创建现金抽奖订单
		private void createCashOrder() {
			StringBuilder sb = new StringBuilder();
			sb.append("userId=" + Constants.mId + "&gameId=" + giftId
					+ "&totalNum=" + mTotalNum);
			XutilsUtils.get(Constants.createCashOrder, sb,
					new RequestCallBack<String>() {
						@Override
						public void onFailure(HttpException arg0, String arg1) {
						}

						@Override
						public void onSuccess(ResponseInfo<String> res) {
//							Log.i("mCashMap", res.result + "==================");
							mCashMap = Constants.getJsonObject(res.result);
							Log.i("mCashMap", mCashMap + "==================");
							if (mCashMap != null
									&& mCashMap.get("Code") != null
									&& mCashMap.get("Code").toString()
											.equals("200")) {
								handler.sendEmptyMessageDelayed(11, 0);
							}else{
								Toast.makeText(getApplicationContext(), mCashMap.get("Message").toString(), Toast.LENGTH_SHORT).show();
							}
						}
					});
		}
	@Override
	protected void onDestroy() {
		System.gc();
		unregisterReceiver(mBroadcastReceiver);
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
