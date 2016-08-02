package com.zjxfood.activity;

import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.umeng.analytics.MobclickAgent;
import com.zjxfood.application.ExitApplication;
import com.zjxfood.common.Constants;
import com.zjxfood.http.XutilsUtils;

import java.util.HashMap;

/**
 * 签到
 * 
 * @author zjx
 * 
 */
public class SignNewActivity extends AppActivity implements OnClickListener {

	private TextView mSigns;// 签到次数
	private Button mSignBtn;
	private Button mCheckBtn;
	private String signNum;
	private ImageView mBackImage;
	// private LoadTask mLoadTask;
	private TextView mSignDetail;
//	private int num = 1;
	private boolean flag = false;
//	private String sign = "";
	private boolean isClick = true;
//	private int x = 1;
//	private RunTask mRunTask;
	private String outofDay = "";// 剩余激活天数
	private PopupWindow mSignPopup;
	private boolean isGetJhDate = false, isOutDay = false;
	private String source1 = "<font color='#666666'>还有</font>";
	private String source2 = "<font color='#666666'>天未激活，食尚币就要清零咯！赶紧去激活吧！</font>";
	private TextView mSignJhText;
	private Button mJhBtn;
	private String mJhMoney = "";
	private ImageView mJhXImage, mMallXImage;
	private Button mCheckMoreBtn;
	private HashMap<String,Object> mSignMap;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.sign_new_layout);
		setImmerseLayout(findViewById(R.id.head_layout));
		ExitApplication.getInstance().addActivity(this);
		init();
		JhDateHttp();
		getSignsHttp();
	}

	private void init() {
		mSigns = (TextView) findViewById(R.id.signs_num_show);
		mSignBtn = (Button) findViewById(R.id.sign_new_btn);
		mCheckBtn = (Button) findViewById(R.id.sign_new_check_btn);
		mBackImage = (ImageView) findViewById(R.id.sign_back_image);
		mSignDetail = (TextView) findViewById(R.id.sign_detail_show);

		mBackImage.setOnClickListener(this);
		mSignBtn.setOnClickListener(this);
		mCheckBtn.setOnClickListener(this);
	}

	Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 1:
				try{
				if (signNum != null && !(signNum.equals(""))) {
					mSigns.setText(signNum);
					mSignDetail.setText("累计签到" + signNum + "天，共获取"
							+ Integer.parseInt(signNum) * 10 + "食尚币");
					flag = true;
					getUserSignOutDayHttp();
				}
				}catch (Exception e) {
					e.printStackTrace();
				}
				break;

			case 2:
				try{
				if (flag) {
					if (mSigns.getText().toString().equals("0")) {
						mSigns.setText(01 + "");
						Toast.makeText(getApplicationContext(),
								"签到成功，赠送10食尚币!", Toast.LENGTH_SHORT).show();
						if (Constants.mShMoney != null
								&& !(Constants.mShMoney.equals(""))) {
							Constants.mShMoney = Integer
									.parseInt(Constants.mShMoney) + 10 + "";
						}
						if (!Constants.isNull(mSigns.getText())) {
							mSignDetail.setText("累计签到"
									+ mSigns.getText().toString()
									+ "天，共获取"
									+ Integer.parseInt(mSigns.getText()
											.toString()) * 10 + "食尚币");
						}
					} else {
						mSigns.setText(Integer.parseInt(mSigns.getText()
								.toString()) + 1 + "");
						Toast.makeText(getApplicationContext(),
								"签到成功，赠送10食尚币!", Toast.LENGTH_SHORT).show();
						if (Constants.mShMoney != null
								&& !(Constants.mShMoney.equals(""))) {
							Constants.mShMoney = Integer
									.parseInt(Constants.mShMoney) + 10 + "";
						}
						mSignDetail.setText("累计签到"
								+ mSigns.getText().toString() + "天，共获取"
								+ Integer.parseInt(mSigns.getText().toString())
								* 10 + "食尚币");
					}
//					if (Constants.mIsjh.equals("1")) {
						LayoutInflater inflater = LayoutInflater
								.from(getApplicationContext());
						View view = inflater.inflate(
								R.layout.popup_sign_success_mall_layout, null);
						mCheckMoreBtn = (Button) view
								.findViewById(R.id.popup_sign_alert_mall_btn);
						mMallXImage = (ImageView) view
								.findViewById(R.id.popup_sign_mall_x_image);
						mSignPopup = new PopupWindow(view,
								LayoutParams.MATCH_PARENT,
								LayoutParams.MATCH_PARENT, false);
						mSignPopup.setBackgroundDrawable(new BitmapDrawable());
						mSignPopup.showAtLocation(mSignBtn, Gravity.CENTER, 0,
								0);
						mMallXImage.setOnClickListener(mCancelClick);
						mCheckMoreBtn.setOnClickListener(mToMallClick);
//					} else {
//						LayoutInflater inflater = LayoutInflater
//								.from(getApplicationContext());
//						View view = inflater.inflate(
//								R.layout.popup_sign_jh_layout, null);
//						mSignJhText = (TextView) view
//								.findViewById(R.id.popup_sign_success_day_show);
//						mJhXImage = (ImageView) view
//								.findViewById(R.id.popup_sign_jh_x_image);
//						mJhBtn = (Button) view
//								.findViewById(R.id.popup_sign_alert_jh_btn);
//						mSignPopup = new PopupWindow(view,
//								LayoutParams.MATCH_PARENT,
//								LayoutParams.MATCH_PARENT, false);
//						mSignPopup.setBackgroundDrawable(new BitmapDrawable());
//						mSignPopup.showAtLocation(mSignBtn, Gravity.CENTER, 0,
//								0);
//						String day = "<font color='#E78143'>" + outofDay
//								+ "</font>";
//						mSignJhText.setText(Html.fromHtml(source1).toString()
//								+ Html.fromHtml(day) + Html.fromHtml(source2));
//						mJhBtn.setOnClickListener(mJhClick);
//						mJhXImage.setOnClickListener(mCancelClick);
//					}
				}
				isClick = true;
				}catch (Exception e) {
					e.printStackTrace();
				}
				break;
			case 3:
				isClick = true;
				Toast.makeText(getApplicationContext(), "签到失败,每天只能签到一次！",
						Toast.LENGTH_SHORT).show();
				break;
			case 4:
				isGetJhDate = true;
				break;
			case 5:
				isOutDay = true;
				break;
			case 6:
				Intent intent = new Intent();
				Bundle bundle = new Bundle();
				// 跳转到支付方式选择页面
				bundle.putString("price", mJhMoney);
				bundle.putString("merchantName", "食尚男女");
				bundle.putString("mId", Constants.mId);
				bundle.putString("LoginImage", "");
				bundle.putString("flum", "");
				bundle.putString("type", "");
				intent.putExtras(bundle);
				intent.setClass(getApplicationContext(), JHPayWayActivity.class);
				startActivity(intent);
				break;
			case 7:
				Toast.makeText(getApplicationContext(), "激活失败，请重新登录！",
						Toast.LENGTH_SHORT).show();
				break;
			}
		};
	};

	// 去激活
	OnClickListener mJhClick = new OnClickListener() {
		@Override
		public void onClick(View arg0) {
			if(mSignPopup!=null && mSignPopup.isShowing()){
			mSignPopup.dismiss();
			}
			if (Constants.onLine == 1 && !(Constants.mId.equals(""))) {
				getJhMoneyHttp();
			} else {
				Toast.makeText(getApplicationContext(), "激活失败，请重新登录帐号！",
						Toast.LENGTH_SHORT).show();
			}
		}
	};
	// 取消popup
	OnClickListener mCancelClick = new OnClickListener() {
		@Override
		public void onClick(View arg0) {
			if(mSignPopup!=null && mSignPopup.isShowing()){
			mSignPopup.dismiss();
			}
		}
	};
	// 商城购物
	OnClickListener mToMallClick = new OnClickListener() {
		@Override
		public void onClick(View arg0) {
			if(mSignPopup!=null && mSignPopup.isShowing()){
			mSignPopup.dismiss();
			}
			Intent intent = new Intent();
			intent.setClass(getApplicationContext(), MallIndexActivity.class);
			startActivity(intent);
			finish();
		}
	};

	private void getSignsHttp() {
		StringBuilder sb = new StringBuilder();
		sb.append("userida="+Constants.mId);
		XutilsUtils.get(Constants.getQdCount2, sb,
				new RequestCallBack<String>() {
					@Override
					public void onFailure(HttpException arg0, String arg1) {
					}

					@Override
					public void onSuccess(ResponseInfo<String> res) {
						Log.i("content", res.result + "========签到次数========");
						signNum = res.result;
						handler.sendEmptyMessageDelayed(1, 0);
					}
				});
	}

	private void signHttp() {
		StringBuilder sb = new StringBuilder();
		sb.append("userId="+Constants.mId);
		XutilsUtils.get(Constants.getSign3, sb,
				new RequestCallBack<String>() {
					@Override
					public void onFailure(HttpException arg0, String arg1) {
					}
					@Override
					public void onSuccess(ResponseInfo<String> res) {
						mSignMap = Constants.getJsonObject(res.result);
						Log.i("mSignMap",res.result+"=================");
						if (mSignMap!=null && mSignMap.get("Code")!=null) {
							if(mSignMap.get("Code").toString().equals("200")){
								handler.sendEmptyMessageDelayed(2, 0);
							}else{
								if(mSignMap.get("Message")!=null){
									isClick = true;
									Toast.makeText(getApplicationContext(),mSignMap.get("Message").toString(),Toast.LENGTH_SHORT).show();
								}
							}
						} else {
							handler.sendEmptyMessageDelayed(3, 0);
						}
					}
				});
	}
	

	//获取清除天数
	private void getUserSignOutDayHttp() {
		StringBuilder sb = new StringBuilder();
			sb.append("uid=" + Constants.mId);
		XutilsUtils.get(Constants.getUserSignOutDay2, sb,
				new RequestCallBack<String>() {
					@Override
					public void onFailure(HttpException arg0, String arg1) {
					}
					@Override
					public void onSuccess(ResponseInfo<String> res) {
						outofDay = res.result;
						handler.sendEmptyMessageDelayed(5, 0);
					}
				});
	}
	//获取激活时间
	private void JhDateHttp() {
		StringBuilder sb = new StringBuilder();
			sb.append("uid=" + Constants.mId);
		XutilsUtils.get(Constants.getJhDate2, sb,
				new RequestCallBack<String>() {
					@Override
					public void onFailure(HttpException arg0, String arg1) {
					}
					@Override
					public void onSuccess(ResponseInfo<String> res) {
						if ((res.result).equals("0")) {
							Constants.mIsjh = 0 + "";
						} else {
							Constants.mIsjh = 1 + "";
						}
						handler.sendEmptyMessageDelayed(4, 0);
					}
				});
	}
	//获取激活金额
	private void getJhMoneyHttp() {
		StringBuilder sb = new StringBuilder();
			sb.append("uid=" + Constants.mId);
		XutilsUtils.get(Constants.getJhMoney2, sb,
				new RequestCallBack<String>() {
					@Override
					public void onFailure(HttpException arg0, String arg1) {
						handler.sendEmptyMessageDelayed(7, 0);
					}
					@Override
					public void onSuccess(ResponseInfo<String> res) {
						mJhMoney = res.result;
						if (mJhMoney != null && !(mJhMoney.equals(""))) {
							// 如果获取金额不为空 跳转到激活页面
							handler.sendEmptyMessageDelayed(6, 0);
						} else {
							handler.sendEmptyMessageDelayed(7, 0);
						}
					}
				});
	}
	
	@Override
	public void onClick(View v) {
		Intent intent = new Intent();
		switch (v.getId()) {
		case R.id.sign_back_image:
			finish();
			break;

		case R.id.sign_new_btn:
			if (isClick) {
				if (isGetJhDate) {
					if (isOutDay) {
						isClick = false;
						signHttp();
					} else {
						Toast.makeText(getApplicationContext(), "签到失败！",
								Toast.LENGTH_SHORT).show();
					}
				} else {
					Toast.makeText(getApplicationContext(), "签到失败！",
							Toast.LENGTH_SHORT).show();
				}
			} else {
				Toast.makeText(getApplicationContext(), "签到失败！",
						Toast.LENGTH_SHORT).show();
			}
			break;
		case R.id.sign_new_check_btn:
//			intent.setClass(getApplicationContext(),
//					MyRestaurantBiActivity.class);
			intent.setClass(getApplicationContext(),
					MyShmoneyActivity.class);
			startActivity(intent);
			finish();
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		switch (keyCode) {
		case KeyEvent.KEYCODE_BACK:
			System.gc();
			finish();
			break;

		default:
			break;
		}
		return super.onKeyDown(keyCode, event);
	}

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
