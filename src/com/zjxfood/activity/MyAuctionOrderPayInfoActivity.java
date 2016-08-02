package com.zjxfood.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.project.util.BitmapUtilSingle;
import com.zjxfood.common.Constants;
import com.zjxfood.http.XutilsUtils;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;

public class MyAuctionOrderPayInfoActivity extends AppActivity implements
		OnClickListener {
	private Bundle mBundle;
	private String mOrderId;
	private HashMap<String, Object> mOrderMap;
	private TextView mNameText, mPriceText, mOrderText, mAddressName,
			mAddressPhone, mAddressInfo, mDateText;
	private TextView mExtText1, mExtText2, mExtText3, mExtText4, mExtText5,
			mExtText6;
	private String mAddressId;
	private ArrayList<HashMap<String, Object>> mAddressList;
	private ImageView mBackImage;
	private TextView mYfText;
	private ImageView mMallImage;
	private BitmapUtils mBitmapUtils;
	private Button mLogisticsBtn;
	private TextView mTitleText;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.auction_order_pay_value);
		setImmerseLayout(findViewById(R.id.head_layout));
		init();
		mBitmapUtils = BitmapUtilSingle
				.getBitmapInstance(getApplicationContext());
		mBundle = getIntent().getExtras();
		if (mBundle != null) {
			mOrderId = mBundle.getString("id");
			getOrderInfo();
		}
	}

	private void init() {
		mNameText = (TextView) findViewById(R.id.auction_value_head_name);
		mPriceText = (TextView) findViewById(R.id.auction_value_head_price_value);
		mOrderText = (TextView) findViewById(R.id.auction_value_head_order);
		mAddressName = (TextView) findViewById(R.id.auction_value_order_address_name);
		mAddressPhone = (TextView) findViewById(R.id.auction_value_order_address_phone);
		mAddressInfo = (TextView) findViewById(R.id.auction_value_order_address_info);
		mDateText = (TextView) findViewById(R.id.auction_value_order_date);
		mExtText1 = (TextView) findViewById(R.id.auction_value_order_info1);
		mExtText2 = (TextView) findViewById(R.id.auction_value_order_info2);
		mExtText3 = (TextView) findViewById(R.id.auction_value_order_info3);
		mExtText4 = (TextView) findViewById(R.id.auction_value_order_info4);
		mExtText5 = (TextView) findViewById(R.id.auction_value_order_info5);
		mExtText6 = (TextView) findViewById(R.id.auction_value_order_info6);
//		mBackImage = (ImageView) findViewById(R.id.title_back_image);
		mBackImage = (ImageView) findViewById(R.id.title_back_image);
		mTitleText = (TextView) findViewById(R.id.title_text);
		mTitleText.setText("拍卖订单");
		mYfText = (TextView) findViewById(R.id.auction_value_head_yf);
		mMallImage = (ImageView) findViewById(R.id.auction_value_image);
		mLogisticsBtn = (Button) findViewById(R.id.look_order_logistics_btn);

		mBackImage.setOnClickListener(this);
		mLogisticsBtn.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.title_back_image:
			finish();
			break;

		case R.id.look_order_logistics_btn:
			Intent intent = new Intent();
			if (mOrderMap != null && mOrderMap.size() > 0) {
				if (mOrderMap.get("ShipmentCompany") != null
						&& mOrderMap.get("ShipmentNum") != null) {
					Bundle bundle = new Bundle();
					bundle.putString("fh", mOrderMap.get("ShipmentCompany")
							.toString()
							+ "："
							+ mOrderMap.get("ShipmentNum").toString());
					intent.putExtras(bundle);
				}
			}
			intent.setClass(getApplicationContext(), LogisticsActivity.class);
			startActivity(intent);
			break;
		}
	}

	// 获取订单详情
	// private void getOrderInfo() {
	// RequestParams params = new RequestParams();
	// params.put("orderNum", mOrderId);
	// AsyUtils.get(Constants.getAuctionOrderDetail, params,
	// new AsyncHttpResponseHandler() {
	// @Override
	// @Deprecated
	// public void onSuccess(int statusCode, String content) {
	// mOrderMap = Constants.getJsonObjectByData(content);
	// if (mOrderMap != null && mOrderMap.size() > 0) {
	// handler.sendEmptyMessageDelayed(1, 0);
	// }
	// super.onSuccess(statusCode, content);
	// }
	// });
	// }
	private void getOrderInfo() {
		StringBuilder sb = new StringBuilder();
		sb.append("orderNum=" + mOrderId);
		XutilsUtils.get(Constants.getAuctionOrderDetail, sb,
				new RequestCallBack<String>() {
					@Override
					public void onFailure(HttpException arg0, String arg1) {
					}
					@Override
					public void onSuccess(ResponseInfo<String> res) {
						mOrderMap = Constants.getJsonObjectByData(res.result);
						if (mOrderMap != null && mOrderMap.size() > 0) {
							handler.sendEmptyMessageDelayed(1, 0);
						}
					}
				});
	}

	Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 1:
				if (!Constants.isNull(mOrderMap.get("ProductName"))) {
					mNameText.setText(mOrderMap.get("ProductName").toString());
				}
				if (!Constants.isNull(mOrderMap.get("EndPrice"))) {
					mPriceText.setText(mOrderMap.get("EndPrice").toString()
							+ "食尚币");
				}
				if (!Constants.isNull(mOrderMap.get("OrderId"))) {
					mOrderText.setText("订单号："
							+ mOrderMap.get("OrderId").toString());
				}
				if (!Constants.isNull(mOrderMap.get("ShipmentCompany"))) {
					if (mOrderMap.get("ShipmentCompany").toString()
							.equals("null")) {
						mExtText1.setText("物流公司：");
					} else {
						mExtText1.setText("物流公司："
								+ mOrderMap.get("ShipmentCompany").toString());
					}
				}
				if (!Constants.isNull(mOrderMap.get("PayTime"))) {
					mExtText2.setText("支付时间："
							+ mOrderMap.get("PayTime").toString());
				}
				if (!Constants.isNull(mOrderMap.get("ShipmentFee"))) {
					mExtText3.setText("需支付运费："
							+ mOrderMap.get("ShipmentFee").toString());
				}
				if (!Constants.isNull(mOrderMap.get("ShipmentNum"))) {
					if (mOrderMap.get("ShipmentNum").toString().equals("null")) {
						mExtText4.setText("物流单号：");
					} else {
						mExtText4.setText("物流单号："
								+ mOrderMap.get("ShipmentNum").toString());
					}
				}
				if (!Constants.isNull(mOrderMap.get("ShipmentTime"))) {
					if (mOrderMap.get("ShipmentTime").toString().equals("null")) {
						mExtText5.setText("发货时间：");
					} else {
						mExtText5.setText("发货时间："
								+ mOrderMap.get("ShipmentTime").toString());
					}
				}
				if (!Constants.isNull(mOrderMap.get("ShipmentNickName"))) {
					if (mOrderMap.get("ShipmentNickName").toString()
							.equals("null")) {
						mExtText6.setText("操作人：");
					} else {
						mExtText6.setText("操作人："
								+ mOrderMap.get("ShipmentNickName").toString());
					}
				}
				if (!Constants.isNull(mOrderMap.get("CreateTime"))) {
					mDateText.setText("订单创建时间："
							+ mOrderMap.get("CreateTime").toString());
				}
				if (!Constants.isNull(mOrderMap.get("AddressId"))) {
					mAddressId = mOrderMap.get("AddressId").toString();
					getAddress();
				}
				if (!Constants.isNull(mOrderMap.get("ShipmentFee"))) {
					mYfText.setText("快递费：￥"
							+ mOrderMap.get("ShipmentFee").toString());
				}
				if (!Constants.isNull(mOrderMap.get("ImgUrl"))) {
					mBitmapUtils.display(mMallImage, mOrderMap.get("ImgUrl")
							.toString());
				}
				break;

			case 2:
				if (!Constants.isNull(mAddressList.get(0).get("address"))) {
					mAddressInfo.setText("收货人地址："
							+ mAddressList.get(0).get("address"));
				}
				if (!Constants.isNull(mAddressList.get(0).get("realname"))) {
					mAddressName.setText("收货人姓名："
							+ mAddressList.get(0).get("realname"));
				}
				if (!Constants.isNull(mAddressList.get(0).get("mobile"))) {
					mAddressPhone.setText("收货人电话："
							+ mAddressList.get(0).get("mobile"));
				}
				break;
			}
		};
	};

	// 获取收货地址
	// private void getAddress() {
	// RequestParams params = new RequestParams();
	// params.put("addressid", mAddressId);
	// AsyUtils.get(Constants.getAddressById, params,
	// new AsyncHttpResponseHandler() {
	// @Override
	// @Deprecated
	// public void onSuccess(int statusCode, String content) {
	// try {
	// mAddressList = Constants
	// .getJsonArrayByData(content);
	// } catch (JSONException e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// }
	// if (mAddressList != null && mAddressList.size() > 0) {
	// handler.sendEmptyMessageDelayed(2, 0);
	// }
	// super.onSuccess(statusCode, content);
	// }
	// });
	// }
	private void getAddress() {
		StringBuilder sb = new StringBuilder();
		sb.append("addressid=" + mAddressId);
		XutilsUtils.get(Constants.getAddressById, sb,
				new RequestCallBack<String>() {
					@Override
					public void onFailure(HttpException arg0, String arg1) {
					}

					@Override
					public void onSuccess(ResponseInfo<String> res) {
						try {
							mAddressList = Constants
									.getJsonArrayByData(res.result);
						} catch (JSONException e) {
							e.printStackTrace();
						}
						if (mAddressList != null && mAddressList.size() > 0) {
							handler.sendEmptyMessageDelayed(2, 0);
						}
					}
				});
	}

}
