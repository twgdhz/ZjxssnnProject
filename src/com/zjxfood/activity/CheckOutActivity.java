package com.zjxfood.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.umeng.analytics.MobclickAgent;
import com.zjxfood.application.ExitApplication;
import com.zjxfood.common.Constants;
import com.zjxfood.http.XutilsUtils;
import com.zjxfood.popupwindow.MerchantPayPopupWindow;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Random;

/**
 * 付款界面
 * 
 * @author Administrator
 * 
 */
public class CheckOutActivity extends AppActivity implements OnClickListener {

	private Button mPayBtn;// 支付
	private PopupWindow mPopupWindow;
//	private View mAlertView;//
//	private ImageView mCancelImage;// 取消
//	private Button mConfirmPayBtn;// 确认支付
	private RelativeLayout mHeadLayout;
	private ImageView mBackImage;// 返回
	private String mId;
	private Bundle mBundle;
	public static String orderId;
//	private EditText mPriceEdit;// 金额
//	private TextView mConfirmPrice;
	private String merchantName;// 商家名字
	private String mFlnum;// 返利
	private TextView mRebatePercent;
	private TextView mMerchantName;
	private ImageView mMerchantImage;
	private String mLoginImage;// 图片路径
	private String MallOrMerchant;
//	private TextView mPayMerchantName;
	private ImageView mPlImage;
	private String plstart;
	private BitmapUtils mBitmapUtils;
	private String mCurrency = "";// 余额
	private HashMap<String, Object> mCurrencyMap;
//	private RelativeLayout mCurrencyLayout;
//	private TextView mInputCurrenText;
	private boolean isPay = false;
	private MerchantPayPopupWindow mPayPopupWindow;
	private EditText mCurrencyEdit;
	private float payCurrency = 0;// 支付的余额
	private Float mPayPrice;// 需要支付的金额
	private HashMap<String, Object> mOrderMap;
	private TextView mCurrencyText;
	private String flag = "";
	private String mPrice;
	private String mIscurrency;//是否支持余额
	private TextView mPriceShow;
	private RelativeLayout mCurrencyLayout;//余额输入界面
	private String mOrdercount;
	private String mIsStop = "true";
	private TextView mOrdercountText;
	private ArrayList<HashMap<String,Object>> mLists;
	private HashMap<String,Object> mMaps;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.check_out_layout);
		setImmerseLayout(findViewById(R.id.head_layout));
		mBitmapUtils = new BitmapUtils(getApplicationContext());
		ExitApplication.getInstance().addActivity(this);
		ExitApplication.getInstance().addMyCommodityActivity(this);

		init();
		orderId = getOutTradeNo();
		mBundle = getIntent().getExtras();
		if (mBundle != null) {
			mId = mBundle.getString("mId");
			merchantName = mBundle.getString("merchantName");
			mFlnum = mBundle.getString("flnum");
			mOrdercount = mBundle.getString("ordercount");
			if(mFlnum!=null){
			mRebatePercent.setText(mFlnum.split("\\.")[0] + "%");
			}
			mLoginImage = mBundle.getString("Logoimage");
			MallOrMerchant = mBundle.getString("MallOrMerchant");
			plstart = mBundle.getString("plstar");
			flag = mBundle.getString("flag");
			mPrice = mBundle.getString("price");
//			mIsStop = mBundle.getString("istop");
			mIscurrency = mBundle.getString("iscurrency");
//			if (flag.equals("2")) {
//			}
			Log.i("二维码扫描", mFlnum+"==="+mLoginImage+"==="+mOrdercount);
		}
		Log.i("累计消费人数", mOrdercount+"=============");
		if(mOrdercount!=null){
			mOrdercountText.setText("累计消费人数："+mOrdercount+"人");
		}
		Log.i("mIscurrency", mIscurrency+"===========是否支持余额支付===========");
		isPay = true;
//		getCurrency();
		getCurrency2();
		mBitmapUtils.display(mMerchantImage, mLoginImage);

		mMerchantName.setText(merchantName);
		mPriceShow.setText("需要支付金额：￥" + mPrice);
		Log.i("plstart", plstart + "====================");
		if (plstart.equals("1")) {
			mPlImage.setImageResource(R.drawable.evaluation_xingxing_png1);
		} else if (plstart.equals("2")) {
			mPlImage.setImageResource(R.drawable.evaluation_xingxing_png2);
		} else if (plstart.equals("3")) {
			mPlImage.setImageResource(R.drawable.evaluation_xingxing_png3);
		} else if (plstart.equals("4")) {
			mPlImage.setImageResource(R.drawable.evaluation_xingxing_png4);
		} else if (plstart.equals("5")) {
			mPlImage.setImageResource(R.drawable.evaluation_xingxing_png5);
		} else {
			mPlImage.setImageResource(R.drawable.evaluation_xingxing_png0);
		}
		
		// new Thread(mImageRun).start();
	}

	private void init() {
		mPayBtn = (Button) findViewById(R.id.check_out_bottom_pay_btn);
		mHeadLayout = (RelativeLayout) findViewById(R.id.title_check_out_id);
		mBackImage = (ImageView) mHeadLayout
				.findViewById(R.id.my_check_out_back_image);
		mRebatePercent = (TextView) findViewById(R.id.check_out_this_rebate_number);
		mMerchantName = (TextView) findViewById(R.id.check_out_merchant_name);
		mMerchantImage = (ImageView) findViewById(R.id.check_out_merchant_image);
		mPlImage = (ImageView) findViewById(R.id.check_out_merchant_score_image);
		mCurrencyEdit = (EditText) findViewById(R.id.check_out_input_curreny_edit);
		mCurrencyText = (TextView) findViewById(R.id.check_out_input_currency_text);
		mPriceShow = (TextView) findViewById(R.id.check_out_input_money_text);
		mCurrencyLayout = (RelativeLayout) findViewById(R.id.check_out_input_currency_edit_layout);
		mOrdercountText = (TextView) findViewById(R.id.check_out_cumulative_numbers);
		
		mPayBtn.setOnClickListener(this);
		mBackImage.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.check_out_bottom_pay_btn:
			// if (!(mPriceEdit.getText().toString().equals(""))) {
			if (isPay) {
				// 转换输入的金额
				Double oldPrice = Double.parseDouble(mPrice);
				String oldPrices = new java.text.DecimalFormat("#.00")
						.format(oldPrice);
				// if (Float.parseFloat((mPriceEdit.getText().toString())) >= 1)
				// {
				if (!mCurrencyEdit.getText().toString().equals("")) {
					Double moneys = Double.parseDouble(mCurrencyEdit.getText()
							.toString());
					Log.i("mPayPrice", "=======0=========");
					payCurrency = Float.parseFloat(new java.text.DecimalFormat(
							"#.00").format(moneys));
					if (mCurrency != null && !mCurrency.equals("")) {
						if (payCurrency <= Float.parseFloat(mCurrency)) {
							mPayPrice = Float.parseFloat(oldPrices)
									- payCurrency;
							Log.i("mPayPrice", mPayPrice + "=======1=========");
							if (payCurrency <= Float.parseFloat(oldPrices)) {
								if (mPayPrice != null) {
									handler.sendEmptyMessageDelayed(3, 0);
								}
							} else {
								Toast.makeText(getApplicationContext(),
										"余额大于支付金额", Toast.LENGTH_SHORT).show();
							}
						} else {
							payCurrency = 0;
							Toast.makeText(getApplicationContext(), "余额不足！",
									Toast.LENGTH_SHORT).show();
						}
					} else {
						mPayPrice = Float.parseFloat(oldPrices);
					}
				} else {
					payCurrency = 0;
					mPayPrice = Float.parseFloat(oldPrices) - payCurrency;
					if (mPayPrice != null) {
						handler.sendEmptyMessageDelayed(3, 0);
					}
					Log.i("mPayPrice", mPayPrice + "=======2=========");
				}

				// } else {
				// Toast.makeText(getApplicationContext(), "金额必须大于1元以上！",
				// Toast.LENGTH_SHORT).show();
				// }
			} else {
				Toast.makeText(getApplicationContext(), "获取余额失败！",
						Toast.LENGTH_SHORT).show();
			}
			break;

		case R.id.my_check_out_back_image:
			finish();
			break;
		}
	}

	OnClickListener mCancelClick = new OnClickListener() {

		@Override
		public void onClick(View v) {
			mPopupWindow.dismiss();
			handler.sendEmptyMessageDelayed(1, 0);
		}
	};
	// 跳转到支付方式界面
	OnClickListener mConfirmClick = new OnClickListener() {
		@Override
		public void onClick(View v) {
			Intent intent = new Intent();
			switch (v.getId()) {
			case R.id.merchant_currency_confirm_pay:
				if (mPayPrice != 0) {
					mPayPopupWindow.dismiss();
					intent = new Intent();
					Bundle bundle = new Bundle();
					bundle.putString("price", mPrice);
					bundle.putString("merchantName", merchantName);
					bundle.putString("mId", mId);
					bundle.putString("LoginImage", mLoginImage);
					bundle.putString("flum", mFlnum);
					bundle.putString("type", "commodity");
					bundle.putString("money", mPayPrice + "");// 现金支付部分
					bundle.putString("currency", payCurrency + "");// 余额支付部分
					bundle.putString("MallOrMerchant", MallOrMerchant);
					intent.putExtras(bundle);
					intent.setClass(getApplicationContext(),
							PayWayActivity.class);
					startActivity(intent);
				} else {
					if(mIsStop.equals("true")){
					createOrder();
					}else if(mIsStop.equals("false")){
						if(mPayPopupWindow!=null && mPayPopupWindow.isShowing()){
							mPayPopupWindow.dismiss();
						}
						Toast.makeText(getApplicationContext(), "该商家暂时不支持余额支付", Toast.LENGTH_SHORT).show();
					}
				}
				break;
			// 充值余额
			case R.id.merchant_currency_confirm_recharge:
				intent.setClass(getApplicationContext(),
						MyCurrencyActivity.class);
				startActivity(intent);
				ExitApplication.getInstance().finishMyCommdity();
				break;
			}

		}
	};

	Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1:
				// mAlertView.setVisibility(View.GONE);
				break;
			case 2:
				if (mMaps.get("AvailableBalance")!=null) {
					String moneys = mMaps.get("AvailableBalance")
							.toString();
					if (Double.parseDouble(moneys) > 0) {
						mCurrency =moneys;
					} else {
						mCurrency = "0.00";
					}
					Log.i("mCurrency", mCurrency + "=========================");
				}
				mCurrencyText.setText("请输入余额\t当前余额：" + mCurrency);
				isPay = true;
				break;
			case 3:
				mPayPopupWindow = new MerchantPayPopupWindow(
						CheckOutActivity.this, mConfirmClick, merchantName,
						mPayPrice, mPrice, payCurrency + "");
				mPayPopupWindow
						.showAtLocation(mBackImage, Gravity.CENTER, 0, 0);
				break;
			case 4:
				mPayPopupWindow.dismiss();
				if(mOrderMap!=null && mOrderMap.size()>0){
					if(mOrderMap.get("Code")!=null && mOrderMap.get("Code").toString().equals("200")){
						Intent intent = new Intent();
						intent = new Intent();
						intent.setClass(getApplicationContext(), MyOrderActivity.class);
						startActivity(intent);
						ExitApplication.getInstance().finishMyCommdity();
						Toast.makeText(getApplicationContext(),mOrderMap.get("Message").toString(),Toast.LENGTH_SHORT).show();
					}else{
						if(mOrderMap.get("Message")!=null){
							Toast.makeText(getApplicationContext(),mOrderMap.get("Message").toString(),Toast.LENGTH_SHORT).show();
						}
					}
				}else{
					Toast.makeText(getApplicationContext(),"支付失败",Toast.LENGTH_SHORT).show();
				}

				Log.i("支付状态", mOrderMap + "============================");
				break;
			}
		};
	};
	Runnable mImageRun = new Runnable() {
		@Override
		public void run() {
			// mBitmap = Constants.getHttpBitmap4(mLoginImage);
			handler.sendEmptyMessageDelayed(2, 0);
		}
	};

//	private void getCurrency() {
//		StringBuilder sb = new StringBuilder();
//			sb.append("uid=" + Constants.mId);
//		XutilsUtils.get(Constants.getCurrency, sb,
//				new RequestCallBack<String>() {
//					@Override
//					public void onFailure(HttpException arg0, String arg1) {
//						isPay = true;
//					}
//					@Override
//					public void onSuccess(ResponseInfo<String> res) {
//						mCurrencyMap = Constants.getJsonObject(res.result);
//
////						mCurrencyMap = Constants.getJsonObjectByData(res.result);
//						Log.i("mCurrencyMap", mCurrencyMap+"=="+res.result);
//						if (mCurrencyMap != null && mCurrencyMap.size() > 0) {
//							handler.sendEmptyMessageDelayed(2, 0);
//
//						}
//					}
//				});
//	}
	private void getCurrency2() {
		StringBuilder sb = new StringBuilder();
		sb.append("userId=" + Constants.mId + "&hasitem=true");
		XutilsUtils.get(Constants.getMyCurrency, sb,
				new RequestCallBack<String>() {
					@Override
					public void onFailure(HttpException arg0, String arg1) {
						isPay = true;
					}
					@Override
					public void onSuccess(ResponseInfo<String> res) {
						Log.i("可用余额",res.result+"================");
						mMaps = Constants.getJsonObjectByData(res.result);
//						mLists = Constants.getJsonArray(mMaps.get("BalanceItems").toString());
						if(mMaps!=null && mMaps.size()>0){
							handler.sendEmptyMessageDelayed(2,0);
						}
						Log.i("mCurrencyMap",mMaps+"================"+mLists);
					}
				});
	}

	// 当支付现金为0时 直接生成订单成功
//	private void createOrder() {
//		RequestParams params = new RequestParams();
//		params.put("uid", Constants.mId);
//		params.put("mid", mId);
//		params.put("total", mPriceEdit.getText().toString());// 消费总金额
//		params.put("money", mPayPrice + "");// 部分金额
//		params.put("currency", payCurrency + "");// 部分余额
//		AsyUtils.get(Constants.createMerchantOrder2, params,
//				new AsyncHttpResponseHandler() {
//					@Override
//					@Deprecated
//					public void onSuccess(int statusCode, String content) {
//						if (statusCode == 200) {
//							mOrderMap = Constants.getJsonObject(content);
//							handler.sendEmptyMessageDelayed(4, 0);
//
//						}
//						super.onSuccess(statusCode, content);
//					}
//				});
//	}
	private void createOrder() {
		StringBuilder sb = new StringBuilder();
			sb.append("userId=" + Constants.mId+"&merchantUserId="+mId+"&total="+mPrice+"&money="+mPayPrice+"&currency="+payCurrency);
		
		XutilsUtils.get(Constants.createMerchantOrder3, sb,
				new RequestCallBack<String>() {
					@Override
					public void onFailure(HttpException arg0, String arg1) {
						Log.i("onFailure", arg1+"=============");
					}

					@Override
					public void onSuccess(ResponseInfo<String> res) {
							mOrderMap = Constants.getJsonObject(res.result);
							handler.sendEmptyMessageDelayed(4, 0);
						}
				});
	}

	// 商家支付生成订单
	public String getOutTradeNo() {
		SimpleDateFormat format = new SimpleDateFormat("MMddHHmmss",
				Locale.getDefault());
		Date date = new Date();
		String key = format.format(date);

		Random r = new Random();
		key = key + r.nextInt();
		key = key.substring(0, 15);
		return "10021" + key;
	}

	public boolean onKeyDown(int keyCode, KeyEvent event) {
		switch (keyCode) {
		case KeyEvent.KEYCODE_BACK:
			finish();
			break;
		default:
			break;
		}
		return false;
	};

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
