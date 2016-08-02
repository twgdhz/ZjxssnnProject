package com.zjxfood.activity;

import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
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

import com.alibaba.fastjson.JSONObject;
import com.alipay.sdk.app.PayTask;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.project.util.DensityUtils;
import com.umeng.analytics.MobclickAgent;
import com.zjxfood.alipay.PayResult;
import com.zjxfood.alipay.SignUtils;
import com.zjxfood.application.ExitApplication;
import com.zjxfood.common.Constants;
import com.zjxfood.http.XutilsUtils;
import com.zjxfood.model.Products;
import com.zjxfood.view.RoundImageView;
import com.zjxfood.view.TiltTextView;

import org.apache.http.entity.StringEntity;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Random;

/**
 * 支付宝
 * 
 * @author zjx
 * 
 */
public class AlipayActivity extends AppActivity implements OnClickListener {

	private Button mPayBtn;
	private PopupWindow mPopupWindow;// 弹窗
	private LinearLayout mAlertLayout;
	// private ImageView mCancelX;
	// private Button mGrabRedBtn;
	private RelativeLayout mHeadLayout;
//	private ImageView mBackImage;// 返回
	private int successFlag = 0;// 成功标识符
	// private String orderid;// 订单id
	private Bundle mBundle;
	private String price, merchantName, mId, giftId, addressId = "", money,
			currency;// 价格、商家名字、用户id
	private TextView mPriceText;
	private String mLoginImagePath;// 图片路径
	private RoundImageView mRoundImageView;
	private String mFlum = "0";// 返利
	private TiltTextView mFlumText;
	private TextView mMerchantName;
	private String MallOrMerchant;
	private boolean isPayMall = false;// 判断商城是否可以支付
	private boolean isPayMerchant = false;// 判断商家是否可以支付
	private boolean isPayCurrency = false;// 判断消费币充值是否可以支付
	private boolean isPayAuiction = false;// 判断竞拍商品是否可以支付
	private boolean isPayIndiana = false;// 判断竞拍商品是否可以支付
	private boolean isCash = false;// 现金抽奖是否可以支付
	private boolean isCashYf = false;// 现金抽奖是否可以支付
	private String mallOrderId = "";// 商城订单
	private String merchantOrderId = "";// 商家订单
	private String currencyOrderId = "";// 消费币订单
	private String auctionOrderId;// 竞拍商品订单
	private String indianaOrderId;// 抽奖商品订单
	private String cashOrderId;// 现金抽奖订单
	private String cashYfOrderId;// 现金抽奖订单
	private String sizeId = "0";// 尺码Id
	private String callBackUrl = "";// 支付宝统一回调地址
//	private String mCallBackUrlMall = "http://api.zjxssnn.com/api/Payment/GiftOrderAlipayCallBack";// 商城回调地址
//	private String mCallBackUrlMerchant = "http://api.zjxssnn.com/api/Payment/OrderAlipayCallBack";// 商家回调地址
	private String mCallBackUrlCurrency = "http://api.zjxssnn.com/api/Payment/CurrencyRechargeOrderAlipayCallBack";// 消费币回调
//	private String mCallBackUrlAuction = "http://api.zjxssnn.com/api/Payment/AuctionAlipayCallBack";// 竞拍回调地址
//	private String mCallBackUrlIndiana = "http://api.zjxssnn.com/api/Payment/GameAlipayCallBack";// 抽奖回调地址
	// private String mCallBackUrlIndiana =
	// "http://open.hexnews.com/api/Payment/GameAlipayCallBack";// 抽奖回调地址
	// private String mCallBackUrlAuction =
	// "http://open.hexnews.com/api/Payment/AuctionAlipayCallBack";// 竞拍回调地址
//	 private String mCallBackUrlCash ="http://api.zjxssnn.com/api/Payment/CarGameNumAlipayCallBack";// 现金抽奖回调地址
//	private String mCallBackUrlCash = "http://open.hexnews.com/api/Payment/CarGameNumAlipayCallBack";// 现金抽奖回调地址测试
	 
//	private String mCallBackUrlCashYf = "http://api.zjxssnn.com/api/Payment/CarGameAlipayCallBack";// 现金抽奖运费回调地址
//	private String mCallBackUrlCashYf = "http://open.hexnews.com/api/Payment/CarGameAlipayCallBack";// 现金抽奖运费回调地址测试
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
	private ImageView mBackImage;//返回按钮
	private TextView mTitleText;
	private String mUseCurrency = "false";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.alipay_pay_layout);
		setImmerseLayout(findViewById(R.id.head_layout));
		mBitmapUtils = new BitmapUtils(getApplicationContext());
		mBitmapUtils.configDefaultLoadFailedImage(R.drawable.log);
		ExitApplication.getInstance().addActivity(this);
		ExitApplication.getInstance().addMyCommodityActivity(this);
		ExitApplication.getInstance().addMallActivity(this);
		ExitApplication.getInstance().addMallDetail(this);
		ExitApplication.getInstance().addCashList(this);
		init();
		mBundle = getIntent().getExtras();
		if (mBundle != null) {
			MallOrMerchant = mBundle.getString("MallOrMerchant");
			if (MallOrMerchant.equals("merchant")) {
				mCallBackName = "MerchantOrder";
				price = mBundle.getString("price");
				merchantName = mBundle.getString("merchantName");
				mId = mBundle.getString("mId");
				mLoginImagePath = mBundle.getString("LoginImage");
				mFlum = mBundle.getString("flum");
				money = mBundle.getString("money");
				currency = mBundle.getString("currency");
				if (!(mId.equals(""))) {
					createMerchantHttp();
				}
				mPriceText.setText("￥" + money);
			} else if (MallOrMerchant.equals("mall")) {
				mCallBackName = "StoreOrder";
				mUseCurrency = mBundle.getString("useCurrency");
				price = mBundle.getString("price");
				merchantName = mBundle.getString("merchantName");
				mId = mBundle.getString("mId");
				mLoginImagePath = mBundle.getString("LoginImage");
				mFlum = mBundle.getString("flum");
				giftId = mBundle.getString("giftId");
				addressId = mBundle.getString("addressId");
				if(mBundle.getString("sizeId")!=null) {
					sizeId = mBundle.getString("sizeId");
				}
//				callBackUrl = "http://171.221.208.3:9001/Alipay/StoreOrder";
				mallOrderId = mBundle.getString("orderId");
				if (mallOrderId != null && !mallOrderId.equals("")) {
//					createNewMallOrder();
					mHandler.sendEmptyMessageDelayed(7, 0);
				}
			} else if (MallOrMerchant.equals("currency")) {
				price = mBundle.getString("price");
				merchantName = mBundle.getString("merchantName");
				if (Constants.mId != null && !Constants.mId.equals("")) {
					createCurrencyOrder();
				}
				mPriceText.setText("￥" + price);
			} else if (MallOrMerchant.equals("auction")) {
				mFlLayout.setVisibility(View.GONE);
				mCallBackName = "AuctionOrder";
				price = mBundle.getString("price");
				merchantName = mBundle.getString("merchantName");
				mId = mBundle.getString("mId");
				mLoginImagePath = mBundle.getString("LoginImage");
				mFlum = mBundle.getString("flum");
				giftId = mBundle.getString("giftId");
				addressId = mBundle.getString("addressId");
				sizeId = mBundle.getString("sizeId");
				auctionOrderId = mBundle.getString("orderId");
				if (auctionOrderId != null && !auctionOrderId.equals("")) {
				}
				mPriceText.setText("￥" + price);
			} else if (MallOrMerchant.equals("indiana")) {
				mCallBackName = "LuckyGameOrder";
				price = mBundle.getString("price");
				merchantName = mBundle.getString("merchantName");
				mId = mBundle.getString("mId");
				mLoginImagePath = mBundle.getString("LoginImage");
				mFlum = mBundle.getString("flum");
				giftId = mBundle.getString("giftId");
				addressId = mBundle.getString("addressId");
				sizeId = mBundle.getString("sizeId");
				indianaOrderId = mBundle.getString("orderId");
				if (indianaOrderId != null && !indianaOrderId.equals("")) {
					mHandler.sendEmptyMessageDelayed(13, 0);
				}
				mPriceText.setText("￥" + price);
			} else if (MallOrMerchant.equals("xjdb")) {
				mCallBackName = "CarGameNumOrder";
				price = mBundle.getString("price");
				merchantName = mBundle.getString("merchantName");
				giftId = mBundle.getString("giftId");
				mTotalNum = mBundle.getString("totalNum");
				if (Constants.mId != null && !Constants.mId.equals("")) {
					createCashOrder();
				}
				mPriceText.setText("￥" + price);
			} else if (MallOrMerchant.equals("cashYf")) {
				mCallBackName = "CarGameOrder";
				price = mBundle.getString("price");
				merchantName = mBundle.getString("merchantName");
				giftId = mBundle.getString("giftId");
				addressId = mBundle.getString("addressId");
				cashYfOrderId = mBundle.getString("orderId");
				Log.i("支付运费订单：", cashYfOrderId+"==================");
				if (cashYfOrderId != null && !cashYfOrderId.equals("")) {
					mHandler.sendEmptyMessageDelayed(15, 0);
				}
				mPriceText.setText("￥" + price);
			}
		}

		Log.i("address", addressId + "==============="+cashYfOrderId);

		mFlumText.setText("返利" + mFlum + "%");
		mMerchantName.setText(merchantName);
		mBitmapUtils.display(mRoundImageView, mLoginImagePath);
		callBack();
	}

	private void init() {
		mFlLayout = (FrameLayout) findViewById(R.id.alipay_reabte_text_layout);
		mPayBtn = (Button) findViewById(R.id.alipay_pay_btn);
		mAlertLayout = (LinearLayout) findViewById(R.id.alipay_pay_alert_view);
		mHeadLayout = (RelativeLayout) findViewById(R.id.title_alipay_pay_id);
//		mBackImage = (ImageView) mHeadLayout
//				.findViewById(R.id.alipay_back_info_image);
		mPriceText = (TextView) findViewById(R.id.alipay_pay_money_text);
		mRoundImageView = (RoundImageView) findViewById(R.id.alipay_pay_round_image);
		mFlumText = (TiltTextView) findViewById(R.id.alipay_reabte_text);
		mMerchantName = (TextView) findViewById(R.id.alipay_pay_name_text);
		mBackImage = (ImageView) findViewById(R.id.title_back_image);
		mTitleText = (TextView) findViewById(R.id.title_text);
		mTitleText.setText("支付宝支付");

		mPayBtn.setOnClickListener(this);
		mBackImage.setOnClickListener(this);
		mAlertLayout.setOnClickListener(this);
	}
	//获取统一回调地址
	private void callBack() {
		StringBuilder sb = new StringBuilder();
		sb.append("name=" +mCallBackName+ "&paymenttype=zfb");
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
						Log.i("回调地址",res.result+"================回调地址："+callBackUrl);
					}
				});
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.alipay_pay_btn:
			pay();
			break;

		case R.id.title_back_image:
			if (mInventoryProblemPop != null
					&& mInventoryProblemPop.isShowing()) {
				mInventoryProblemPop.dismiss();
			}
			finish();
			// pay();
			break;
		case R.id.alipay_pay_alert_view:

			break;
		}
	}

	// 抢红包
	android.view.View.OnClickListener mGrabRedClick = new OnClickListener() {
		@Override
		public void onClick(View arg0) {
			Intent intent = new Intent();
			intent.setClass(getApplicationContext(), GrabRedActivity.class);
			startActivity(intent);
			mPopupWindow.dismiss();
			mHandler.sendEmptyMessageDelayed(3, 0);
		}
	};

	OnClickListener mClickListener = new OnClickListener() {

		@Override
		public void onClick(View arg0) {
			mPopupWindow.dismiss();
			mHandler.sendEmptyMessageDelayed(3, 0);
		}
	};

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
		return false;
	};

	private Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			Intent intent = new Intent();
			switch (msg.what) {
			case Constants.SDK_PAY_FLAG: {
				Log.i("zfbpay", "==========支付提示========");
				PayResult payResult = new PayResult((String) msg.obj);

				// 支付宝返回此次支付结果及加签，建议对支付宝签名信息拿签约时支付宝提供的公钥做验签
				String resultInfo = payResult.getResult();

				String resultStatus = payResult.getResultStatus();

				// 判断resultStatus 为“9000”则代表支付成功，具体状态码代表含义可参考接口文档
				if (TextUtils.equals(resultStatus, "9000")) {
					// Toast.makeText(AlipayActivity.this, "支付成功",
					// Toast.LENGTH_SHORT).show();
					successFlag = 1;
					mHandler.sendEmptyMessageDelayed(4, 0);
				} else {
					// 判断resultStatus 为非“9000”则代表可能支付失败
					// “8000”代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，最终交易是否成功以服务端异步通知为准（小概率状态）
					if (TextUtils.equals(resultStatus, "8000")) {
						successFlag = 0;
						Toast.makeText(AlipayActivity.this, "支付结果确认中",
								Toast.LENGTH_SHORT).show();

					} else {
						successFlag = 0;
						// 其他值就可以判断为支付失败，包括用户主动取消支付，或者系统返回的错误
						if (mMerchantMap != null
								&& mMerchantMap.get("Message") != null) {
							Toast.makeText(AlipayActivity.this,
									mMerchantMap.get("Message").toString(),
									Toast.LENGTH_SHORT).show();
						} else if (mMallMap != null
								&& mMallMap.get("Message") != null) {
							Toast.makeText(AlipayActivity.this,
									mMallMap.get("Message").toString(),
									Toast.LENGTH_SHORT).show();
						} else {
							Log.i("8000", "============支付失败============");
							Toast.makeText(AlipayActivity.this, "支付失败",
									Toast.LENGTH_SHORT).show();
						}

					}
				}
				break;
			}
			case Constants.SDK_CHECK_FLAG: {
				Toast.makeText(AlipayActivity.this, "检查结果为：" + msg.obj,
						Toast.LENGTH_SHORT).show();
				break;
			}
			case 3:
				mAlertLayout.setVisibility(View.GONE);
				break;
			case 4:
				if (MallOrMerchant.equals("merchant")) {
					mAlertLayout.setVisibility(View.VISIBLE);
					LayoutInflater inflater = LayoutInflater
							.from(getApplicationContext());
					View view = inflater.inflate(
							R.layout.popup_pay_success_layout, null);
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
					mSuccessPopup.showAtLocation(mHeadLayout, Gravity.CENTER,
							0, 0);

					mCheckOrderBtn.setOnClickListener(mOrderClick);
					mSuccessText1.setText("恭喜你，成功支付" + money + "元");
					mSuccessText2.setText("并且获得"
							+ (int) Float.parseFloat(price) + "个食尚币");
					mSuccessXimage.setOnClickListener(mOrderListClick);
					// Toast.makeText(getApplicationContext(),
					// "支付成功，评价后可获得抢红包机会！", Toast.LENGTH_SHORT).show();
					//
				} else if (MallOrMerchant.equals("mall")) {
					new Thread(mallRun).start();
				} else if (MallOrMerchant.equals("currency")) {
					Toast.makeText(getApplicationContext(), "消费币充值成功！",
							Toast.LENGTH_SHORT).show();
				}else if (MallOrMerchant.equals("xjdb")) {
					Toast.makeText(getApplicationContext(), "现金抽奖支付成功！",
							Toast.LENGTH_SHORT).show();
					ExitApplication.getInstance().finishCash();
				}else if (MallOrMerchant.equals("cashYf")) {
					Toast.makeText(getApplicationContext(), "运费支付成功！",
							Toast.LENGTH_SHORT).show();
					ExitApplication.getInstance().finishCash();
				}
				break;
			case 5:
				// 支付成功跳转到订单详情界面
				Toast.makeText(getApplicationContext(), "支付成功！",
						Toast.LENGTH_SHORT).show();

				break;
			case 6:
				Toast.makeText(getApplicationContext(), "购买成功！",
						Toast.LENGTH_SHORT).show();
				intent = new Intent();
				intent.setClass(getApplicationContext(),
						MallOrderActivity.class);
				startActivity(intent);
				ExitApplication.getInstance().finishMall();
				Log.i("mall", "商城购物成功！=========================");
				break;
			case 7:
				isPayMall = true;
					mPriceText.setText(price);
				break;
			case 8:
				Toast.makeText(getApplicationContext(), "订单生成失败！",
						Toast.LENGTH_SHORT).show();
				break;
			case 9:
				Log.i("mMerchantMap", mMerchantMap
						+ "===========mMerchantMap==============");
				if (mMerchantMap.get("Code") != null
						&& mMerchantMap.get("Code").toString().equals("200")) {
					mOrderIdMap = Constants.getJsonObject(mMerchantMap.get("Data").toString());
					merchantOrderId = mOrderIdMap.get("PayId").toString();
					isPayMerchant = true;
				}else if(mMerchantMap.get("Message")!=null){
					Toast.makeText(getApplicationContext(),mMerchantMap.get("Message").toString(),Toast.LENGTH_SHORT).show();
				}

				Log.i("merchantOrderId", merchantOrderId
						+ "===========merchantOrderId==============");
				break;
			case 10:
				try {

					LayoutInflater inflater = LayoutInflater
							.from(getApplicationContext());
					View view = inflater.inflate(
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
					// Toast.makeText(getApplicationContext(), "库存不足，无法兑换",
					// Toast.LENGTH_SHORT).show();
					mInventoryBtn.setOnClickListener(mInventoryClick);
					if (mMallMap != null && mMallMap.get("Message") != null) {
						mInventoryText.setText(mMallMap.get("Message")
								.toString());
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				break;
			case 11:
				if (mCurrencyMap.get("Data") != null) {
					currencyOrderId = mCurrencyMap.get("Data").toString();
					isPayCurrency = true;
				}
				break;
			case 12:
				isPayAuiction = true;
				break;
			case 13:
				isPayIndiana = true;
				break;
			case 14:
				if (mCashMap != null && mCashMap.get("Data") != null) {
					isCash = true;
					cashOrderId = mCashMap.get("Data").toString();
					Log.i("订单生成成功", cashOrderId+"==========");
					Toast.makeText(getApplicationContext(), "订单生成成功",
							Toast.LENGTH_SHORT).show();
				}
				break;
			case 15:
				isCashYf = true;
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
	// 进入订单列表页面
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

	// 商城购物
	Runnable mallRun = new Runnable() {
		@Override
		public void run() {
			mHandler.sendEmptyMessageDelayed(6, 0);
		}
	};

	//
	/**
	 * call alipay sdk pay. 调用SDK支付
	 * 
	 */
	public void pay() {
		String orderInfo = "";
		if (MallOrMerchant.equals("merchant")) {
			if (isPayMerchant) {
				// 订单
				orderInfo = getOrderInfo("[食尚男女]" + merchantName, merchantName,
						money, callBackUrl,
						merchantOrderId.replaceAll("\"", ""));
				// 对订单做RSA 签名
				String sign = sign(orderInfo);
				try {
					// 仅需对sign 做URL编码
					sign = URLEncoder.encode(sign, "UTF-8");
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
				// 完整的符合支付宝参数规范的订单信息
				final String payInfo = orderInfo + "&sign=\"" + sign + "\"&"
						+ getSignType();
				Runnable payRunnable = new Runnable() {
					@Override
					public void run() {
						// 构造PayTask 对象
						PayTask alipay = new PayTask(AlipayActivity.this);
						// 调用支付接口，获取支付结果
						String result = alipay.pay(payInfo);

						Message msg = new Message();
						msg.what = Constants.SDK_PAY_FLAG;
						msg.obj = result;
						mHandler.sendMessage(msg);
					}
				};
				// 必须异步调用
				Thread payThread = new Thread(payRunnable);
				payThread.start();
			} else {
				if (mMerchantMap != null && mMerchantMap.get("Message") != null) {
					Toast.makeText(AlipayActivity.this,
							mMerchantMap.get("Message").toString(),
							Toast.LENGTH_SHORT).show();
				} else {
					Toast.makeText(AlipayActivity.this, "商家订单生成失败",
							Toast.LENGTH_SHORT).show();
				}
			}
		} else if (MallOrMerchant.equals("mall")) {
			if (isPayMall) {
				orderInfo = getOrderInfo("[食尚男女]" + merchantName, merchantName,
						price, callBackUrl,
						mallOrderId.replaceAll("\"", ""));
				// 对订单做RSA 签名
				String sign = sign(orderInfo);
				try {
					// 仅需对sign 做URL编码
					sign = URLEncoder.encode(sign, "UTF-8");
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}

				// 完整的符合支付宝参数规范的订单信息
				final String payInfo = orderInfo + "&sign=\"" + sign + "\"&"
						+ getSignType();

				Runnable payRunnable = new Runnable() {
					@Override
					public void run() {
						// 构造PayTask 对象
						PayTask alipay = new PayTask(AlipayActivity.this);
						// 调用支付接口，获取支付结果
						String result = alipay.pay(payInfo);

						Message msg = new Message();
						msg.what = Constants.SDK_PAY_FLAG;
						msg.obj = result;
						mHandler.sendMessage(msg);
					}
				};

				// 必须异步调用
				Thread payThread = new Thread(payRunnable);
				payThread.start();
			} else {
				if (mMallMap != null && mMallMap.get("Message") != null) {
					Toast.makeText(AlipayActivity.this,
							mMallMap.get("Message").toString(),
							Toast.LENGTH_SHORT).show();
				} else {
					Toast.makeText(AlipayActivity.this, "商城订单生成失败",
							Toast.LENGTH_SHORT).show();
				}
			}
		} else if (MallOrMerchant.equals("auction")) {
			if (isPayAuiction) {
				orderInfo = getOrderInfo("[食尚男女]" + merchantName, merchantName,
						price, callBackUrl, auctionOrderId);
				// 对订单做RSA 签名
				String sign = sign(orderInfo);
				try {
					// 仅需对sign 做URL编码
					sign = URLEncoder.encode(sign, "UTF-8");
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}

				// 完整的符合支付宝参数规范的订单信息
				final String payInfo = orderInfo + "&sign=\"" + sign + "\"&"
						+ getSignType();

				Runnable payRunnable = new Runnable() {
					@Override
					public void run() {
						// 构造PayTask 对象
						PayTask alipay = new PayTask(AlipayActivity.this);
						// 调用支付接口，获取支付结果
						String result = alipay.pay(payInfo);

						Message msg = new Message();
						msg.what = Constants.SDK_PAY_FLAG;
						msg.obj = result;
						mHandler.sendMessage(msg);
					}
				};

				// 必须异步调用
				Thread payThread = new Thread(payRunnable);
				payThread.start();
			} else {
				Log.i("auction", "============支付失败============");
				Toast.makeText(AlipayActivity.this, "支付失败", Toast.LENGTH_SHORT)
						.show();
			}
		} else if (MallOrMerchant.equals("indiana")) {// 抽奖
			if (isPayIndiana) {
				orderInfo = getOrderInfo("[食尚男女]" + merchantName, merchantName,
						price, callBackUrl, indianaOrderId);
				// 对订单做RSA 签名
				String sign = sign(orderInfo);
				try {
					// 仅需对sign 做URL编码
					sign = URLEncoder.encode(sign, "UTF-8");
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}

				// 完整的符合支付宝参数规范的订单信息
				final String payInfo = orderInfo + "&sign=\"" + sign + "\"&"
						+ getSignType();

				Runnable payRunnable = new Runnable() {
					@Override
					public void run() {
						// 构造PayTask 对象
						PayTask alipay = new PayTask(AlipayActivity.this);
						// 调用支付接口，获取支付结果
						String result = alipay.pay(payInfo);

						Message msg = new Message();
						msg.what = Constants.SDK_PAY_FLAG;
						msg.obj = result;
						mHandler.sendMessage(msg);
					}
				};

				// 必须异步调用
				Thread payThread = new Thread(payRunnable);
				payThread.start();
			} else {
				Log.i("auction", "============支付失败============");
				Toast.makeText(AlipayActivity.this, "支付失败", Toast.LENGTH_SHORT)
						.show();
			}
		}else if(MallOrMerchant.equals("xjdb")){//现金抽奖
			Log.i("现金抽奖", "============现金抽奖现金抽奖=============="+cashOrderId);
			if(isCash){
				orderInfo = getOrderInfo("[食尚男女]" + merchantName, merchantName,
						price, callBackUrl,
						cashOrderId.replaceAll("\"", ""));
				// 对订单做RSA 签名
				String sign = sign(orderInfo);
				try {
					// 仅需对sign 做URL编码
					sign = URLEncoder.encode(sign, "UTF-8");
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}

				// 完整的符合支付宝参数规范的订单信息
				final String payInfo = orderInfo + "&sign=\"" + sign + "\"&"
						+ getSignType();

				Runnable payRunnable = new Runnable() {
					@Override
					public void run() {
						// 构造PayTask 对象
						PayTask alipay = new PayTask(AlipayActivity.this);
						// 调用支付接口，获取支付结果
						String result = alipay.pay(payInfo);
						Message msg = new Message();
						msg.what = Constants.SDK_PAY_FLAG;
						msg.obj = result;
						mHandler.sendMessage(msg);
					}
				};
				// 必须异步调用
				Thread payThread = new Thread(payRunnable);
				payThread.start();
			
			}
		}else if(MallOrMerchant.equals("cashYf")){
			Log.i("现金抽奖运费支付", "============现金抽奖运费支付=============="+cashYfOrderId);
			if(isCashYf){

				orderInfo = getOrderInfo("[食尚男女]" + merchantName, merchantName,
						price, callBackUrl,
						cashYfOrderId);
				// 对订单做RSA 签名
				String sign = sign(orderInfo);
				try {
					// 仅需对sign 做URL编码
					sign = URLEncoder.encode(sign, "UTF-8");
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}

				// 完整的符合支付宝参数规范的订单信息
				final String payInfo = orderInfo + "&sign=\"" + sign + "\"&"
						+ getSignType();

				Runnable payRunnable = new Runnable() {
					@Override
					public void run() {
						// 构造PayTask 对象
						PayTask alipay = new PayTask(AlipayActivity.this);
						// 调用支付接口，获取支付结果
						String result = alipay.pay(payInfo);
						Message msg = new Message();
						msg.what = Constants.SDK_PAY_FLAG;
						msg.obj = result;
						mHandler.sendMessage(msg);
					}
				};
				// 必须异步调用
				Thread payThread = new Thread(payRunnable);
				payThread.start();
			
			
			}
		}else if (MallOrMerchant.equals("currency")) {
			if (isPayCurrency) {
				orderInfo = getOrderInfo("[食尚男女]" + merchantName, merchantName,
						price, mCallBackUrlCurrency,
						currencyOrderId.replaceAll("\"", ""));
				// 对订单做RSA 签名
				String sign = sign(orderInfo);
				try {
					// 仅需对sign 做URL编码
					sign = URLEncoder.encode(sign, "UTF-8");
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}

				// 完整的符合支付宝参数规范的订单信息
				final String payInfo = orderInfo + "&sign=\"" + sign + "\"&"
						+ getSignType();

				Runnable payRunnable = new Runnable() {
					@Override
					public void run() {
						// 构造PayTask 对象
						PayTask alipay = new PayTask(AlipayActivity.this);
						// 调用支付接口，获取支付结果
						String result = alipay.pay(payInfo);
						Message msg = new Message();
						msg.what = Constants.SDK_PAY_FLAG;
						msg.obj = result;
						mHandler.sendMessage(msg);
					}
				};
				// 必须异步调用
				Thread payThread = new Thread(payRunnable);
				payThread.start();
			} else {
				if (mCurrencyMap != null && mCurrencyMap.get("Message") != null) {
					Toast.makeText(AlipayActivity.this,
							mCurrencyMap.get("Message").toString(),
							Toast.LENGTH_SHORT).show();
				} else {
					Toast.makeText(AlipayActivity.this, "订单生成失败",
							Toast.LENGTH_SHORT).show();
				}
			}
		} else {
			Toast.makeText(getApplicationContext(), "订单生成中...",
					Toast.LENGTH_SHORT).show();
		}
	}

	/**
	 * check whether the device has authentication alipay account.
	 * 查询终端设备是否存在支付宝认证账户
	 * 
	 */
	public void check(View v) {
		Runnable checkRunnable = new Runnable() {
			@Override
			public void run() {
				// 构造PayTask 对象
				PayTask payTask = new PayTask(AlipayActivity.this);
				// 调用查询接口，获取查询结果
				boolean isExist = payTask.checkAccountIfExist();

				Message msg = new Message();
				msg.what = Constants.SDK_CHECK_FLAG;
				msg.obj = isExist;
				mHandler.sendMessage(msg);
			}
		};
		Thread checkThread = new Thread(checkRunnable);
		checkThread.start();
	}

	/**
	 * get the sdk version. 获取SDK版本号
	 * 
	 */
	public void getSDKVersion() {
		PayTask payTask = new PayTask(this);
		String version = payTask.getVersion();
		Toast.makeText(this, version, Toast.LENGTH_SHORT).show();
	}

	/**
	 * create the order info. 创建订单信息
	 * 
	 */
	public String getOrderInfo(String subject, String body, String price,
			String callBackUrl, String orderId) {
		// 签约合作者身份ID
		String orderInfo = "partner=" + "\"" + Constants.PARTNER + "\"";

		// 签约卖家支付宝账号
		orderInfo += "&seller_id=" + "\"" + Constants.SELLER + "\"";
		// orderid = getOutTradeNo();
		// Log.i("orderid", mallOrderId.replaceAll("\"", "")
		// + "==========orderid========");
		// 商户网站唯一订单号
		orderInfo += "&out_trade_no=" + "\"" + orderId + "\"";

		// 商品名称
		orderInfo += "&subject=" + "\"" + subject + "\"";

		// 商品详情
		orderInfo += "&body=" + "\"" + body + "\"";

		// 商品金额
		orderInfo += "&total_fee=" + "\"" + price + "\"";

		// 服务器异步通知页面路径
		orderInfo += "&notify_url=" + "\"" + callBackUrl + "\"";

		// 服务接口名称， 固定值
		orderInfo += "&service=\"mobile.securitypay.pay\"";

		// 支付类型， 固定值
		orderInfo += "&payment_type=\"1\"";

		// 参数编码， 固定值
		orderInfo += "&_input_charset=\"utf-8\"";

		// 设置未付款交易的超时时间
		// 默认30分钟，一旦超时，该笔交易就会自动被关闭。
		// 取值范围：1m～15d。
		// m-分钟，h-小时，d-天，1c-当天（无论交易何时创建，都在0点关闭）。
		// 该参数数值不接受小数点，如1.5h，可转换为90m。
		orderInfo += "&it_b_pay=\"30m\"";

		// extern_token为经过快登授权获取到的alipay_open_id,带上此参数用户将使用授权的账户进行支付
		// orderInfo += "&extern_token=" + "\"" + extern_token + "\"";

		// 支付宝处理完请求后，当前页面跳转到商户指定页面的路径，可空
		orderInfo += "&return_url=\"m.alipay.com\"";

		// 调用银行卡支付，需配置此参数，参与签名， 固定值 （需要签约《无线银行卡快捷支付》才能使用）
		// orderInfo += "&paymethod=\"expressGateway\"";

		return orderInfo;
	}

	/**
	 * get the out_trade_no for an order. 生成商户订单号，该值在商户端应保持唯一（可自定义格式规范）
	 * 
	 */
	public String getOutTradeNo() {
		SimpleDateFormat format = new SimpleDateFormat("MMddHHmmss",
				Locale.getDefault());
		Date date = new Date();
		String key = format.format(date);

		Random r = new Random();
		key = key + r.nextInt();
		key = key.substring(0, 15);
		return key;
	}

	/**
	 * sign the order info. 对订单信息进行签名
	 * 
	 * @param content
	 *            待签名订单信息
	 */
	public String sign(String content) {
		return SignUtils.sign(content, Constants.RSA_PRIVATE);
	}

	/**
	 * get the sign type we use. 获取签名方式
	 * 
	 */
	public String getSignType() {
		return "sign_type=\"RSA\"";
	}

	// 生成订单
	public String getOrderId() {
		SimpleDateFormat format = new SimpleDateFormat("MMddHHmmss",
				Locale.getDefault());
		Date date = new Date();
		String key = format.format(date);
		Random r = new Random();
		key = key + r.nextInt();
		key = key.substring(0, 15);
		return key + "_" + Constants.mId;
	}

	// 创建商城订单
	// private void createMallOrder(){
	// RequestParams params = new RequestParams();
	// params.put("userid", Constants.mId);
	// params.put("money", price);
	// params.put("addressid", addressId);
	// params.put("remark", "");
	// params.put("giftid", giftId);
	// if(!(sizeId.equals(""))){
	// params.put("chimaid", sizeId);
	// }
	// AsyUtils.get(Constants.createMallOrder2, params, new
	// AsyncHttpResponseHandler(){
	// @Override
	// @Deprecated
	// public void onSuccess(int statusCode, String content) {
	// Log.i("createMallOrder2", content + "========-1==============");
	// mMallMap = Constants.getJsonObject(content);
	// if (mMallMap != null && mMallMap.size() > 0) {
	// if(mMallMap.get("Code").toString().equals("200") &&
	// mMallMap.get("Data")!=null){
	// mallOrderId = mMallMap.get("Data").toString();
	// mHandler.sendEmptyMessageDelayed(7, 0);
	// Log.i("mallOrderId", mallOrderId
	// + "========mallOrderId==============");
	// }else{
	// mHandler.sendEmptyMessageDelayed(10, 0);
	// }
	// }
	// super.onSuccess(statusCode, content);
	// }
	// @Override
	// @Deprecated
	// public void onFailure(Throwable error, String content) {
	// Log.i("onFailure", content
	// + "========content==============");
	// mMallMap = Constants.getJsonObject(content);
	// mHandler.sendEmptyMessageDelayed(7, 0);
	// super.onFailure(error, content);
	// }
	// });
	// }
	private void createMallOrder() {
		StringBuilder sb = new StringBuilder();
		if (!(sizeId.equals(""))) {
			sb.append("userId=" + Constants.mId + "&money=" + price
					+ "&addressId=" + addressId + "&remark=&giftId=" + giftId
					+ "&chimaId=" + sizeId + "&useCurrency="+mUseCurrency);
		} else {
			sb.append("userid=" + Constants.mId + "&money=" + price
					+ "&addressid=" + addressId + "&remark=&giftid=" + giftId + "&useCurrency="+mUseCurrency);
		}
		XutilsUtils.get(Constants.createMallOrder3, sb,
				new RequestCallBack<String>() {
					@Override
					public void onFailure(HttpException arg0, String arg1) {
						mMallMap = Constants.getJsonObject(arg1);
						mHandler.sendEmptyMessageDelayed(7, 0);
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
								mHandler.sendEmptyMessageDelayed(7, 0);
								Log.i("mallOrderId", mallOrderId
										+ "========mallOrderId==============");
							} else {
								mHandler.sendEmptyMessageDelayed(10, 0);
							}
						}
					}
				});
	}
	private void createNewMallOrder() {
		StringBuilder sb = new StringBuilder();
		ArrayList<HashMap<String,Object>> list = new ArrayList<>();
		HashMap<String,Object> map = new HashMap<>();
		map.put("productId",giftId);
		map.put("productName","测试商品3");
		map.put("attrId",sizeId);
		map.put("quantity",1);
		list.add(map);
		sb.append("&userId=" + Constants.mId + "&addressId=" + addressId
				+ "&memo=" + "&useCurrency=" + mUseCurrency
				+ "&products=" + list.toString() );

		Products products1 = new Products();
		products1.setAddressId(addressId);
		products1.setMemo("");
		products1.setUseCurrency(mUseCurrency);
		products1.setUserId(Constants.mId);
		products1.setProducts(list);

		String a=JSONObject.toJSON(products1).toString();
		Log.i("表单数据",a+"============================");
		RequestParams params = new RequestParams("UTF-8");
		try {
			params.setBodyEntity(new StringEntity(a,"UTF-8"));
			params.setContentType("application/json");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		XutilsUtils.post(Constants.createNewMallOrder,sb,params,
				new RequestCallBack<String>() {
					@Override
					public void onFailure(HttpException arg0, String arg1) {
					}
					@Override
					public void onSuccess(ResponseInfo<String> res) {
						Log.i("新商城订单", res.result
								+ "========onSuccess==============");
						mMallMap = Constants.getJsonObject(res.result);
						if (mMallMap != null && mMallMap.size() > 0) {
							if (mMallMap.get("Code").toString().equals("200")
									&& mMallMap.get("Data") != null) {
								mOrderIdMap = Constants.getJsonObject(mMallMap.get("Data").toString());
								mallOrderId = mOrderIdMap.get("PayId").toString();
								mHandler.sendEmptyMessageDelayed(7, 0);
								Log.i("mallOrderId", mallOrderId
										+ "========mallOrderId==============");
							} else {
								mHandler.sendEmptyMessageDelayed(10, 0);
							}
						}
					}
				});
	}
	// 创建消费币充值订单
	// private void createCurrencyOrder(){
	// RequestParams params = new RequestParams();
	// params.put("uid", Constants.mId);
	// params.put("money", price);
	// AsyUtils.get(Constants.createrechargeorder, params, new
	// AsyncHttpResponseHandler(){
	// @Override
	// @Deprecated
	// public void onSuccess(int statusCode, String content) {
	// Log.i("createrechargeorder", content+"==================");
	// mCurrencyMap = Constants.getJsonObject(content);
	// if(mCurrencyMap!=null &&
	// mCurrencyMap.get("Code").toString().equals("200")){
	// mHandler.sendEmptyMessageDelayed(11, 0);
	// }
	// super.onSuccess(statusCode, content);
	// }
	// });
	// }
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
							mHandler.sendEmptyMessageDelayed(11, 0);
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
//						Log.i("mCashMap", res.result + "==================");
						mCashMap = Constants.getJsonObject(res.result);
						Log.i("mCashMap", mCashMap + "==================");
						if (mCashMap != null
								&& mCashMap.get("Code") != null
								&& mCashMap.get("Code").toString()
										.equals("200")) {
							mHandler.sendEmptyMessageDelayed(14, 0);
						}else if(mCashMap != null
								&& mCashMap.get("Code") != null
								&& !mCashMap.get("Code").toString()
										.equals("200")){
							Toast.makeText(getApplicationContext(), mCashMap.get("Message").toString(), Toast.LENGTH_SHORT).show();
						}
					}
				});
	}

	// private void createMerchantHttp() {
	// RequestParams params = new RequestParams();
	// params.put("uid", Constants.mId);
	// params.put("mid", mId);
	// params.put("total", price);// 消费总金额
	// params.put("money", money);// 部分金额
	// params.put("currency", currency);// 部分消费币
	// AsyUtils.get(Constants.createMerchantOrder2, params,
	// new AsyncHttpResponseHandler() {
	// @Override
	// @Deprecated
	// public void onSuccess(int statusCode, String content) {
	// if (statusCode == 200) {
	// // merchantOrderId = content;
	// mMerchantMap = Constants.getJsonObject(content);
	// if (mMerchantMap != null && mMerchantMap.size() > 0) {
	// mHandler.sendEmptyMessageDelayed(9, 0);
	// }
	// }
	// super.onSuccess(statusCode, content);
	// }
	// });
	// }
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
						// merchantOrderId = content;
						mMerchantMap = Constants.getJsonObject(res.result);
						if (mMerchantMap != null && mMerchantMap.size() > 0) {
							mHandler.sendEmptyMessageDelayed(9, 0);
						}
					}
				});
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		System.gc();
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
