package com.zjxfood.cashindiana;

import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout.LayoutParams;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.project.util.BitmapUtilSingle;
import com.zjxfood.activity.AppActivity;
import com.zjxfood.activity.MallDetailActivity;
import com.zjxfood.activity.MallPayWayActivity;
import com.zjxfood.activity.NewAddressManageActivity;
import com.zjxfood.activity.R;
import com.zjxfood.application.ExitApplication;
import com.zjxfood.common.Constants;
import com.zjxfood.http.AsyUtils;
import com.zjxfood.http.XutilsUtils;

import org.json.JSONObject;

import java.util.HashMap;

//创建订单
public class CashMyIdaCreateOrderInfo extends AppActivity implements OnClickListener {

	private ImageView mBackImage;
	private Bundle mBundle;
	private static String mId;
	private HashMap<String, Object> mDetailMap;
	private BitmapUtils mBitmapUtils;
	private ImageView mImageView;
	private TextView mMallName, mMallPrice, mUserName, mUserPhone,
			mUserAddress;
	private TextView mInfoText1, mInfoText2, mInfoText3, mInfoText4,
			mInfoText5, mInfoText6, mInfoText7, mInfoText8, mInfoText9;
	private Button mCreateBtn;
	private String mFlag;
	private HashMap<String, Object> mDefaulAddressList;
	private String mAddressId;
	private String userName, userPhone, userAddress;
	private String mOtherYf;
	private Button mModifyBtn;
	private String mPrice;
	private PopupWindow mPopupWindow;
	private TextView mOkText, mCancelText;
	private HashMap<String, Object> mOrderMap;

	protected void onCreate(android.os.Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ExitApplication.getInstance().addActivity(this);
		setContentView(R.layout.my_indiana_create_order_value);
		setImmerseLayout(findViewById(R.id.head_layout));
		mBitmapUtils = BitmapUtilSingle
				.getBitmapInstance(getApplicationContext());
		init();
		MallDetailActivity.mFlag = "4";
		mBundle = getIntent().getExtras();
		if (mBundle != null) {
			mFlag = mBundle.getString("flag");
			if (mFlag != null) {
				if (mFlag.equals("1")) {
					mId = mBundle.getString("id");
					mPrice = mBundle.getString("price");
					Log.i("运费1", mPrice + "===============id=" + mId);
					if (Constants.onLine == 1 && !(Constants.mId.equals(""))) {
						// 获取默认地址
						getDefaultAddress();
					}
				} else if (mFlag.equals("2")) {
					mAddressId = mBundle.getString("addressId");
					mUserName.setText("收货人姓名：" + mBundle.getString("userName"));
					mUserPhone.setText("收货人电话："
							+ mBundle.getString("userPhone"));
					mUserAddress.setText("收货人地址："
							+ mBundle.getString("userAddress"));
				}
			}
		}
		if (mId != null) {
			getIndianaDetail();
		}
	};

	private void init() {
		mBackImage = (ImageView) findViewById(R.id.auction_order_value_back_info_image);
		mMallName = (TextView) findViewById(R.id.indiana_create_order_head_name);
		mImageView = (ImageView) findViewById(R.id.indiana_create_order_head_image);
		mMallPrice = (TextView) findViewById(R.id.indiana_create_order_head_price_value);
		mUserName = (TextView) findViewById(R.id.indiana_create_order_receiving_name);
		mCreateBtn = (Button) findViewById(R.id.indiana_create_order_value_btn);
		mUserAddress = (TextView) findViewById(R.id.indiana_create_order_receiving_address);
		mUserPhone = (TextView) findViewById(R.id.indiana_create_order_receiving_phone);
		mInfoText1 = (TextView) findViewById(R.id.indiana_create_order_order_info1);
		mInfoText2 = (TextView) findViewById(R.id.indiana_create_order_order_info2);
		mInfoText3 = (TextView) findViewById(R.id.indiana_create_order_order_info3);
		mInfoText4 = (TextView) findViewById(R.id.indiana_create_order_order_info4);
		mInfoText5 = (TextView) findViewById(R.id.indiana_create_order_order_info5);
		mInfoText6 = (TextView) findViewById(R.id.indiana_create_order_order_info6);
		mInfoText7 = (TextView) findViewById(R.id.indiana_create_order_order_info7);
		mInfoText8 = (TextView) findViewById(R.id.indiana_create_order_order_info8);
		mInfoText9 = (TextView) findViewById(R.id.indiana_create_order_order_info9);
		mModifyBtn = (Button) findViewById(R.id.indiana_create_order_modity_btn);

		mModifyBtn.setOnClickListener(this);
		mCreateBtn.setOnClickListener(this);
		mBackImage.setOnClickListener(this);
	}

	// 获取奖品详情
	private void getIndianaDetail() {
		StringBuilder sb = new StringBuilder();
		sb.append("Id=" + mId);
		XutilsUtils.get(Constants.getCashDetail, sb,
				new RequestCallBack<String>() {
					@Override
					public void onFailure(HttpException arg0, String arg1) {
					}

					@Override
					public void onSuccess(ResponseInfo<String> res) {
						mDetailMap = Constants.getJsonObjectByData(res.result);
						if (mDetailMap != null && mDetailMap.size() > 0) {
							handler.sendEmptyMessageDelayed(1, 0);
						}
					}
				});
	}

	// 获取默认地址
	private void getDefaultAddress() {
		RequestParams params = new RequestParams();
		params.put("uid", Constants.mId);
		AsyUtils.get(Constants.getDefaultAddress2, params,
				new JsonHttpResponseHandler() {
					@Override
					public void onSuccess(JSONObject response) {
						try {
							mDefaulAddressList = Constants
									.getJsonObject(response.toString());
						} catch (Exception e) {
							e.printStackTrace();
						}
						if (mDefaulAddressList != null
								&& mDefaulAddressList.size() > 0) {
							handler.sendEmptyMessageDelayed(2, 0);
						}
						super.onSuccess(response);
					}
				});
	}

	Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			Intent intent = new Intent();
			Bundle bundle = new Bundle();
			switch (msg.what) {
			case 1:
				Log.i("handler1", mDetailMap + "================");
				if (mDetailMap.get("ImgUrl") != null) {
					mBitmapUtils.display(mImageView, mDetailMap.get("ImgUrl")
							.toString());
				}
				if (mDetailMap.get("ProductName") != null) {
					mMallName.setText("【" + mId + "期】"
							+ mDetailMap.get("ProductName").toString());
				}
				java.text.DecimalFormat df = new java.text.DecimalFormat("0.00");
				if (mDetailMap.get("ShipmentFee") != null) {
					Log.i("快递费：", mDetailMap.get("ShipmentFee")+"==============");
					Double price = Double.parseDouble(mDetailMap.get("ShipmentFee")
							.toString());
					mPrice = df.format(price);
					mMallPrice.setText("￥" + mPrice + "元");
				}
				if (mDetailMap.get("ProductName") != null) {
					mInfoText1.setText("商品名："
							+ mDetailMap.get("ProductName").toString());
				}
				if (mDetailMap.get("ExtField1") != null && !mDetailMap.get("ExtField1").toString().equals("null")) {
					mInfoText2.setText(mDetailMap.get("ExtField1").toString());
				}else{
					mInfoText2.setVisibility(View.GONE);
				}
				if (mDetailMap.get("ExtField2") != null && !mDetailMap.get("ExtField2").toString().equals("null")) {
					mInfoText3.setText(mDetailMap.get("ExtField2").toString());
				}else{
					mInfoText3.setVisibility(View.GONE);
				}
				if (mDetailMap.get("ExtField3") != null && !mDetailMap.get("ExtField3").toString().equals("null")) {
					mInfoText4.setText(mDetailMap.get("ExtField3").toString());
				}else{
					mInfoText4.setVisibility(View.GONE);
				}
				if (mDetailMap.get("ExtField4") != null && !mDetailMap.get("ExtField4").toString().equals("null")) {
					mInfoText5.setText(mDetailMap.get("ExtField4").toString());
				}else{
					mInfoText5.setVisibility(View.GONE);
				}
				if (mDetailMap.get("ExtField5") != null && !mDetailMap.get("ExtField5").toString().equals("null")) {
					mInfoText6.setText(mDetailMap.get("ExtField5").toString());
				}else{
					mInfoText6.setVisibility(View.GONE);
				}
				if (mDetailMap.get("CreateTime") != null) {
					mInfoText7.setText("时间："
							+ mDetailMap.get("CreateTime").toString());
				}
				if (mDetailMap.get("LuckyNum") != null) {
					mInfoText8.setText("中奖号："
							+ mDetailMap.get("LuckyNum").toString());
				}
				break;

			case 2:
				if (!Constants.isNull(mDefaulAddressList.get("Address"))) {
					mUserAddress.setText("收货人地址："
							+ mDefaulAddressList.get("Address"));
				}
				if (!Constants.isNull(mDefaulAddressList.get("Mobile"))) {
					mUserPhone.setText("收货人电话："
							+ mDefaulAddressList.get("Mobile"));
				}
				if (!Constants.isNull(mDefaulAddressList.get("Realname"))) {
					mUserName.setText("收货人姓名："
							+ mDefaulAddressList.get("Realname"));
				}
				if (!Constants.isNull(mDefaulAddressList.get("Id"))) {
					mAddressId = mDefaulAddressList.get("Id").toString();
				}
//				mPrice = price;
				mMallPrice.setText("￥" + mPrice + "元");
				// if (mAddressId != null) {
				// getOtherYf();
				// }
				break;
			case 3:
				// if (mOtherYf != null && price != null) {
				// mPrice = (Float.parseFloat(price) + Float
				// .parseFloat(mOtherYf)) + "";
				// } else {
				// mPrice = price;
				// }
				// mMallPrice.setText("￥" + mPrice + "元");
				break;
			// 跳转到支付方式页面
			case 5:
				Toast.makeText(getApplicationContext(), "订单创建成功"+mOrderMap.get("Data").toString(),
						Toast.LENGTH_SHORT).show();
				try{
				if(mDetailMap!=null && mOrderMap!=null && mOrderMap.get("Data")!=null){
					intent.setClass(getApplicationContext(),
							MallPayWayActivity.class);
					bundle = new Bundle();
					bundle.putString("price",mPrice);
					bundle.putString("merchantName", mDetailMap.get("ProductName").toString());
					bundle.putString("mId", mId);
					bundle.putString("type", "gift");
					bundle.putString("flum", "0");
					bundle.putString("payType", "cashYf");
					if(mDetailMap.get("ImgUrl")!=null){
					bundle.putString("LoginImage", mDetailMap.get("ImgUrl").toString());
					}
					bundle.putString("mallOrMerchant", "cashYf");
					bundle.putString("address", mAddressId);
					bundle.putString("giftId", mId);
					bundle.putString("orderId", mOrderMap.get("Data").toString());
					bundle.putString("sizeId", "");
					intent.putExtras(bundle);
					startActivity(intent);
					}
				}catch (Exception e) {
					e.printStackTrace();
				}
				break;
			}
		};
	};

	// 获取额外运费
	// private void getOtherYf() {
	// StringBuilder sb = new StringBuilder();
	// sb.append("addressid=" + mAddressId);
	// XutilsUtils.get(Constants.getOtherYf2, sb,
	// new RequestCallBack<String>() {
	// @Override
	// public void onFailure(HttpException arg0, String arg1) {
	// }
	// @Override
	// public void onSuccess(ResponseInfo<String> res) {
	// Log.i("content", res.result
	// + "===========content==========");
	// mOtherYf = res.result;
	// handler.sendEmptyMessageDelayed(3, 0);
	// }
	// });
	// }

	// 创建订单
	private void createOrder() {
		StringBuilder sb = new StringBuilder();
		sb.append("gameId=" + mId + "&userId=" + Constants.mId + "&addressId="
				+ mAddressId);
		XutilsUtils.get(Constants.createCashYfOrder, sb,
				new RequestCallBack<String>() {
					@Override
					public void onFailure(HttpException arg0, String arg1) {
					}

					@Override
					public void onSuccess(ResponseInfo<String> res) {

						mOrderMap = Constants.getJsonObject(res.result);
						Log.i("mOrderMap", mOrderMap
								+ "===========mOrderMap==========");
						if (mOrderMap != null && mOrderMap.size() > 0) {
							if (mOrderMap.get("Code") != null
									&& mOrderMap.get("Code").toString()
											.equals("200")) {
								handler.sendEmptyMessageDelayed(5, 0);
							} else {
								if (mOrderMap.get("Message") != null) {
									Toast.makeText(
											getApplicationContext(),
											mOrderMap.get("Message").toString(),
											Toast.LENGTH_SHORT).show();
								}
							}
						}
					}
				});
	}

	@Override
	public void onClick(View v) {
		Intent intent = new Intent();
		switch (v.getId()) {
		case R.id.auction_order_value_back_info_image:
			finish();
			break;
		// 修改地址
		case R.id.indiana_create_order_modity_btn:
			Log.i("click", "=========================");
			intent.setClass(getApplicationContext(),
					NewAddressManageActivity.class);
			startActivity(intent);
			finish();
			break;
		// 创建订单
		case R.id.indiana_create_order_value_btn:
			LayoutInflater inflater = LayoutInflater
					.from(getApplicationContext());
			View view = inflater.inflate(
					R.layout.my_indiana_create_order_popup, null);
			mOkText = (TextView) view
					.findViewById(R.id.my_indiana_order_pay_ok_true);
			mCancelText = (TextView) view
					.findViewById(R.id.my_indiana_order_pay_cancel_false);
			mPopupWindow = new PopupWindow(view, LayoutParams.MATCH_PARENT,
					LayoutParams.MATCH_PARENT, false);
			mPopupWindow.setBackgroundDrawable(new BitmapDrawable());
			mPopupWindow.setOutsideTouchable(true);
			mPopupWindow.setFocusable(true);
			ColorDrawable dw = new ColorDrawable(0xb0000000);
			// 设置SelectPicPopupWindow弹出窗体的背景
			mPopupWindow.setBackgroundDrawable(dw);
			// 设置SelectPicPopupWindow弹出窗体动画效果
			mPopupWindow.setAnimationStyle(R.style.AnimTop_miss);
			mPopupWindow.showAtLocation(mMallName, Gravity.CENTER, 0, 0);
			mOkText.setOnClickListener(clickListener);
			mCancelText.setOnClickListener(clickListener);
			break;
		}
	}

	OnClickListener clickListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			Intent intent = new Intent();
			Bundle bundle = new Bundle();
			switch (v.getId()) {
			case R.id.my_indiana_order_pay_ok_true:
				if (mId != null && mAddressId != null && Constants.mId != null) {
					createOrder();
//					if(mDetailMap!=null && mOrderMap!=null && mOrderMap.get("Data")!=null){
//						intent.setClass(getApplicationContext(),
//								MallPayWayActivity.class);
//						bundle = new Bundle();
//						bundle.putString("price",mPrice);
//						bundle.putString("merchantName", mDetailMap.get("ProductName").toString());
//						bundle.putString("mId", mId);
//						bundle.putString("type", "gift");
//						bundle.putString("flum", "0");
//						if(mDetailMap.get("ImgUrl")!=null){
//						bundle.putString("LoginImage", mDetailMap.get("ImgUrl").toString());
//						}
//						bundle.putString("mallOrMerchant", "cashYf");
//						bundle.putString("address", mAddressId);
//						bundle.putString("giftId", mId);
//						bundle.putString("orderId", mOrderMap.get("Data").toString());
//						bundle.putString("sizeId", "");
//						intent.putExtras(bundle);
//						startActivity(intent);
//						}
				} else {
					Toast.makeText(getApplicationContext(), "创建订单失败",
							Toast.LENGTH_SHORT).show();
				}
				if (mPopupWindow != null && mPopupWindow.isShowing()) {
					mPopupWindow.dismiss();
				}
				break;

			case R.id.my_indiana_order_pay_cancel_false:
				if (mPopupWindow != null && mPopupWindow.isShowing()) {
					mPopupWindow.dismiss();
				}
				break;
			}
		}
	};
}
