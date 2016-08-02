package com.zjxfood.indiana;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.lidroid.xutils.BitmapUtils;
import com.project.util.BitmapUtilSingle;
import com.zjxfood.activity.AppActivity;
import com.zjxfood.activity.MallPayWayActivity;
import com.zjxfood.activity.R;

//未付款
public class MyIndianaNoPayActivity extends AppActivity implements OnClickListener {

	private ImageView mBackImage;
	private BitmapUtils mBitmapUtils;
	private ImageView mImageView;
	private TextView mMallName, mMallPrice, mUserName, mUserPhone,
	mUserAddress;
	private TextView mInfoText1, mInfoText2, mInfoText3, mInfoText4,
	mInfoText5, mInfoText6, mInfoText7, mInfoText8, mInfoText9;
	private Button mPayBtn;
	private Bundle mBundle;
	private String mProductName,mId,mOrderStr,mImageUrl,mNameStr,mPrice,mAddressId,mShipmentNum,mShipmentTime,mShipmentNickName,mShipmentCompany,mAddress,mShipmentName,mMobile;
	private TextView mOrderText;
	
	protected void onCreate(android.os.Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.my_indiana_no_pay_order_value);
		setImmerseLayout(findViewById(R.id.head_layout));
		mBitmapUtils = BitmapUtilSingle
				.getBitmapInstance(getApplicationContext());
		init();
		mBundle = getIntent().getExtras();
		if(mBundle!=null){
			mId = mBundle.getString("id");
			mProductName = mBundle.getString("ProductName");
			mOrderStr = mBundle.getString("OrderId");
			mImageUrl = mBundle.getString("ImgUrl");
			mNameStr = mBundle.getString("UserName");
			mAddress = mBundle.getString("Address");
			mMobile = mBundle.getString("Mobile");
			mPrice = mBundle.getString("price");
			mAddressId = mBundle.getString("AddressId");
			mShipmentNum = mBundle.getString("ShipmentNum");
			mShipmentTime = mBundle.getString("ShipmentTime");
			mShipmentNickName = mBundle.getString("ShipmentNickName");
			mShipmentCompany = mBundle.getString("ShipmentCompany");
			mShipmentName = mBundle.getString("ShipmentName");
		}
		setResource();
	};

	private void init() {
		mBackImage = (ImageView) findViewById(R.id.auction_order_value_back_info_image);
		mImageView = (ImageView) findViewById(R.id.indiana_no_pay_order_head_image);
		mMallName = (TextView) findViewById(R.id.indiana_no_pay_order_head_name);
		mMallPrice = (TextView) findViewById(R.id.indiana_no_pay_order_head_price_value);
		mUserName = (TextView) findViewById(R.id.indiana_no_pay_order_receiving_name);
		mUserPhone = (TextView) findViewById(R.id.indiana_no_pay_order_receiving_phone);
		mUserAddress = (TextView) findViewById(R.id.indiana_no_pay_order_receiving_address);
		mInfoText1 = (TextView) findViewById(R.id.indiana_no_pay_order_order_info1);
		mInfoText2 = (TextView) findViewById(R.id.indiana_no_pay_order_order_info2);
		mInfoText3 = (TextView) findViewById(R.id.indiana_no_pay_order_order_info3);
		mInfoText4 = (TextView) findViewById(R.id.indiana_no_pay_order_order_info4);
		mInfoText5 = (TextView) findViewById(R.id.indiana_no_pay_order_order_info5);
		mInfoText6 = (TextView) findViewById(R.id.indiana_no_pay_order_order_info6);
		mInfoText7 = (TextView) findViewById(R.id.indiana_no_pay_order_order_info7);
		mInfoText8 = (TextView) findViewById(R.id.indiana_no_pay_order_order_info8);
		mOrderText = (TextView) findViewById(R.id.indiana_create_order_head_order);
		mPayBtn = (Button) findViewById(R.id.indiana_no_pay_value_btn);

		mBackImage.setOnClickListener(this);
		mPayBtn.setOnClickListener(this);
	}
	
	private void setResource(){
		mBitmapUtils.display(mImageView, mImageUrl);
		mMallName.setText("【"+mId+"期】"+mProductName);
		mMallPrice.setText("￥"+mPrice+"元");
		mUserName.setText("收货人姓名："+mShipmentName);
		mUserPhone.setText("收货人电话："+mMobile);
		mUserAddress.setText("收货人地址："+mAddress);
		mOrderText.setText("订单号："+mOrderStr);
		mInfoText1.setText("商品："+mProductName);
		mInfoText2.setText("订单号："+mOrderStr);
		mInfoText3.setText("快递费：￥"+mPrice+"元");
		mInfoText4.setText("操作人："+mShipmentNickName);
		mInfoText5.setText("订单状态：未付款");
	}

	@Override
	public void onClick(View v) {
		Intent intent = new Intent();
		Bundle bundle = new Bundle();
		switch (v.getId()) {
		case R.id.auction_order_value_back_info_image:
			finish();
			break;

		case R.id.indiana_no_pay_value_btn:
			// 跳转到支付方式
			if(mOrderStr!=null && mPrice!=null){
			intent.setClass(getApplicationContext(),
					MallPayWayActivity.class);
			bundle = new Bundle();
			bundle.putString("price",mPrice);
			bundle.putString("merchantName", mProductName);
			bundle.putString("mId", mId);
			bundle.putString("type", "gift");
			bundle.putString("flum", "0");
			bundle.putString("LoginImage", mImageUrl);
			bundle.putString("mallOrMerchant", "indiana");
			bundle.putString("address", mAddressId);
			bundle.putString("giftId", mId);
			bundle.putString("orderId", mOrderStr);
			bundle.putString("payType", "indiana");
			bundle.putString("sizeId", "");
			intent.putExtras(bundle);
			startActivity(intent);
			}
			break;
		}
	}
}
