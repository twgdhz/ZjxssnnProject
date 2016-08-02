package com.zjxfood.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.project.util.BitmapUtilSingle;
import com.zjxfood.application.ExitApplication;
import com.zjxfood.common.Constants;
import com.zjxfood.fragment.MallParameterFragment;
import com.zjxfood.http.XutilsUtils;
import com.zjxfood.popupwindow.MallBuyWindow;
import com.zjxfood.view.ScrollViewContainer;

import java.util.HashMap;

/**
 * 商城收货地址选择
 * @author zjx
 *
 */
public class MallChoseAddressActivity extends AppActivity implements OnClickListener{

	private TextView mUserName,mUserPhone,mUserAddress;
	private Button mChoseBtn;
	private HashMap<String, Object> mDefaulAddressMap;
	private String mAddressId;
	private String mOtherYf;
	private int data = 0;
	private Bundle mBundle;
	private String userName,userPhone,userAddress;
	private String flag;
	private static String mYf, title,LoginImage = "", giftId, mMoney,mSizeStr,mSizeId,mTypeName;//
	private Button mComfirBtn;
	private MallBuyWindow mBuyWindow;
	private String mPrice;
	private ImageView mBackImage;
	private TextView mMallName,mGuigeText,mPriceText,mSsbText;
	private ImageView mLoginImage;
	private BitmapUtils mBitmapUtils;
	private TextView mShowText;
	private TextView mTitleText;
	private CheckBox mYeBox;
	private String mUseCurrency = "false";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mall_address_chose_layout);
		setImmerseLayout(findViewById(R.id.head_layout));
		ExitApplication.getInstance().addActivity(this);
		ExitApplication.getInstance().addAddressActivity(this);
		ExitApplication.getInstance().addMallActivity(this);
		init();
		mBitmapUtils = BitmapUtilSingle.getBitmapInstance(getApplicationContext());
		MallDetailActivity.mFlag = "1";
		mBundle = getIntent().getExtras();
		if(mBundle!=null){
			flag = mBundle.getString("flag");
			if(flag!=null){
			if(flag.equals("1")){
				mYf = mBundle.getString("yf");
				LoginImage = mBundle.getString("LoginImage");
				title = mBundle.getString("merchantName");
				giftId = mBundle.getString("giftId");
				mSizeId = mBundle.getString("sizeId");
				mSizeStr = mBundle.getString("sizeStr");
				mMoney = mBundle.getString("money");
				if (Constants.onLine == 1 && !(Constants.mId.equals(""))) {
					getDefaultAddress();
				}
				mTypeName = mBundle.getString("typeName");
			}else if(flag.equals("2")){
				userName = mBundle.getString("userName");
				userPhone = mBundle.getString("userPhone");
				userAddress = mBundle.getString("userAddress");
				mAddressId = mBundle.getString("addressId");
				if (userName != null) {
					mUserName.setText("收货人姓名：" + userName);
				} else {
					mUserName.setText("收货人姓名：" + "");
				}
				if (userPhone != null) {
					mUserPhone.setText("收货人电话：" + userPhone);
				} else {
					mUserPhone.setText("收货人电话：" + "");
				}
				if (userAddress != null) {
					mUserAddress.setText("收货人地址：" + userAddress);
				} else {
					mUserAddress.setText("收货人地址：" + "");
				}
				handler.sendEmptyMessageDelayed(2, 0);
//				getOtherYf();
				//
			}
			}
		}
		if(title!=null){
		mMallName.setText(title);
		}
		mBitmapUtils.display(mLoginImage, LoginImage);
		if(mSizeStr!=null){
		mGuigeText.setText("规格："+mSizeStr);
		}
		if(mMoney!=null){
			if(mTypeName.equals("xj")){
				mSsbText.setText("￥"+mMoney);
			}else if(mTypeName.equals("ssb")){
				mSsbText.setText(mMoney+"食尚币");
			}else if(mTypeName.equals("ssjb")){
				mSsbText.setText("￥"+mMoney);
			}

		}
		IntentFilter filter = new IntentFilter(NewAddressActivity.action);
		filter.addAction(NewAddressActivity.action);
		filter.addAction(MallParameterFragment.action);
		filter.addAction(ScrollViewContainer.action);
		registerReceiver(mBroadcastReceiver, filter);
		SharedPreferences sp = getApplicationContext()
				.getSharedPreferences("商城确认订单界面提示信息", MODE_PRIVATE);
		if(sp!=null && sp.getString("Content", "")!=null && !sp.getString("Content", "").toString().equals("")){
			mShowText.setText(sp.getString("Content", ""));
		}
	}
	
	private void init(){
		mUserName = (TextView) findViewById(R.id.mall_detail_receiving_name);
		mUserPhone = (TextView) findViewById(R.id.mall_detail_receiving_phone);
		mUserAddress = (TextView) findViewById(R.id.mall_detail_receiving_address);
		mChoseBtn = (Button) findViewById(R.id.mall_detail_receiving_modity_btn);
		mComfirBtn = (Button) findViewById(R.id.mall_address_chose_btn);
//		mBackImage = (ImageView) findViewById(R.id.title_back_image);
		mBackImage = (ImageView) findViewById(R.id.title_back_image);
		mTitleText = (TextView) findViewById(R.id.title_text);
		mTitleText.setText("确认订单");
		mMallName = (TextView) findViewById(R.id.mall_order_detail_name);
		mGuigeText = (TextView) findViewById(R.id.mall_order_detail_guige);
		mPriceText = (TextView) findViewById(R.id.mall_order_detail_price_value);
		mLoginImage = (ImageView) findViewById(R.id.mall_order_detail_image);
		mSsbText = (TextView) findViewById(R.id.mall_order_detail_ssb_value);
		mShowText = (TextView) findViewById(R.id.mall_order_show);
		mYeBox = (CheckBox) findViewById(R.id.checkBox_yue_btn_login);
		
		mBackImage.setOnClickListener(this);
		mChoseBtn.setOnClickListener(this);
		mComfirBtn.setOnClickListener(this);
	}
		private void getDefaultAddress() {
			StringBuilder sb = new StringBuilder();
				sb.append("uid=" + Constants.mId);
			XutilsUtils.get(Constants.getDefaultAddress2, sb,
					new RequestCallBack<String>() {
						@Override
						public void onFailure(HttpException arg0, String arg1) {
						}
						@Override
						public void onSuccess(ResponseInfo<String> res) {
							try {
								mDefaulAddressMap = Constants.getJsonObject(res.result);
							} catch (Exception e) {
								e.printStackTrace();
							}
							if (mDefaulAddressMap != null
									&& mDefaulAddressMap.size() > 0) {
								handler.sendEmptyMessageDelayed(1, 0);
							}
						}
					});
		}

		// 获取额外运费
//		private void getOtherYf() {
//			StringBuilder sb = new StringBuilder();
//				sb.append("addressid=" + mAddressId);
//			XutilsUtils.get(Constants.getOtherYf2, sb,
//					new RequestCallBack<String>() {
//						@Override
//						public void onFailure(HttpException arg0, String arg1) {
//						}
//						@Override
//						public void onSuccess(ResponseInfo<String> res) {
//							Log.i("content", res.result
//									+ "===========content==========");
//							mOtherYf = res.result;
//							handler.sendEmptyMessageDelayed(2, 0);
//						}
//					});
//		}
		Handler handler = new Handler(){
			public void handleMessage(android.os.Message msg) {
				switch (msg.what) {
				case 1:
					if (mDefaulAddressMap != null && mDefaulAddressMap.size() > 0) {
						if (mDefaulAddressMap.get("Id").toString()
								.equals("null")) {
							return;
						}
						mUserName.setText("收货人姓名："
								+ mDefaulAddressMap.get("Realname")
										.toString());
						mUserPhone.setText("收货人电话："
								+ mDefaulAddressMap.get("Mobile")
										.toString());
						mUserAddress.setText("收货人地址："
								+ mDefaulAddressMap.get("Address")
										.toString());
						mChoseBtn.setText("修改");
						mAddressId = mDefaulAddressMap.get("Id").toString();
						mPrice = Float.parseFloat(mYf)+"";
						mPriceText.setText("￥"+mPrice);
					} else {
						Log.i("mModifyAddressBtn", "=======3==========");
						mChoseBtn.setText("去填写");
					}
					break;

				case 2:
//					if(mOtherYf==null){
					if(mYf!=null){
						mPrice = Float.parseFloat(mYf)+"";
					}
//						}else{
//							mPrice = Float.parseFloat(price) + Float.parseFloat(mOtherYf)+"";
//						}
					mPriceText.setText("￥"+mPrice);
					break;
				}
			};
		};

		@Override
		public void onClick(View v) {
			Intent intent = new Intent();
			switch (v.getId()) {
			//返回
			case R.id.title_back_image:
				ExitApplication.getInstance().finishAddress();
				break;
			case R.id.mall_detail_receiving_modity_btn:
				if (Constants.onLine == 1) {
					if (mChoseBtn.getText().equals("去填写")) {
						intent.setClass(getApplicationContext(),
								NewAddressActivity.class);
						Bundle bundle = new Bundle();
						bundle.putString("flag", "1");
						intent.putExtras(bundle);
						startActivity(intent);
					} else if (mChoseBtn.getText().equals("修改")) {
						intent.setClass(getApplicationContext(),
								NewAddressManageActivity.class);
						Bundle bundle = new Bundle();
						bundle.putString("flag", "1");
						intent.putExtras(bundle);
						startActivity(intent);
					}
				} else {
					Toast.makeText(getApplicationContext(), "请先登录帐号！",
							Toast.LENGTH_SHORT).show();
				}
				break;
				//确认
			case R.id.mall_address_chose_btn:
				if(mAddressId!=null){
					
					mBuyWindow = new MallBuyWindow(
							MallChoseAddressActivity.this,
							mBuyClick,mPrice);
					mBuyWindow.showAtLocation(mUserName, Gravity.CENTER,
							0, 0);
				}else{
					Toast.makeText(getApplicationContext(), "请选择收货地址", Toast.LENGTH_SHORT).show();
				}
				break;
			}
		}
		BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
			@Override
			public void onReceive(Context arg0, Intent intent) {
				if(intent!=null && intent.getExtras().getString("data")!=null){
				data = Integer.parseInt(intent.getExtras().getString("data"));
				switch (data) {
				case 1:
					mUserName.setText("收货人姓名："
							+ intent.getExtras().getString("userName"));

					mUserPhone.setText("收货人电话："
							+ intent.getExtras().getString("userPhone"));

					mUserAddress.setText("收货人地址："
							+ intent.getExtras().getString("userAddress"));
					mAddressId = intent.getExtras().getString("addressId");
					handler.sendEmptyMessageDelayed(2, 0);
//					getOtherYf();
					break;
				}
				}
			}
		};
		
		private OnClickListener mBuyClick = new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				switch (v.getId()) {
				case R.id.mall_buy_ok:
					if(mAddressId!=null){
					intent.setClass(getApplicationContext(),
							MallPayWayActivity.class);
							Bundle bundle = new Bundle();
							bundle.putString("price",mPrice+ "");
//							bundle.putString("price","0.01");
							bundle.putString("merchantName", title);
							//商家ID 商城页面随便传一个来充数
							bundle.putString("mId", "52913");
							bundle.putString("type", "gift");
							bundle.putString("flum", "0");
							bundle.putString("LoginImage", LoginImage);
							bundle.putString("mallOrMerchant", "mall");
							bundle.putString("address", mAddressId);
							bundle.putString("giftId", giftId);
							bundle.putString("sizeId", mSizeId);
						if(mYeBox.isChecked()) {
							bundle.putString("useCurrency", "true");
						}else{
							bundle.putString("useCurrency", "false");
						}
							intent.putExtras(bundle);
							startActivity(intent);
							if(mBuyWindow!=null && mBuyWindow.isShowing()){
								mBuyWindow.dismiss();
							}
					}else{
						Toast.makeText(getApplicationContext(), "收货地址不正确", Toast.LENGTH_SHORT).show();
					}
					break;

				default:
					break;
				}
			}
		};
		public boolean onKeyDown(int keyCode, KeyEvent event) {
			switch (keyCode) {
			case KeyEvent.KEYCODE_BACK:
				ExitApplication.getInstance().finishAddress();
				break;
			default:
				break;
			}
			return false;
		};

	@Override
	protected void onDestroy() {
		unregisterReceiver(mBroadcastReceiver);
		super.onDestroy();
	}

}
