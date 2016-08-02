package com.zjxfood.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.project.util.DensityUtils;
import com.tencent.mm.sdk.modelmsg.SendAuth;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.umeng.analytics.MobclickAgent;
import com.zjxfood.activity.wxapi.WXEntryActivity;
import com.zjxfood.application.ExitApplication;
import com.zjxfood.common.Constants;
import com.zjxfood.http.AsyUtils;
import com.zjxfood.http.ReadJson;
import com.zjxfood.http.XutilsUtils;
import com.zjxfood.popupwindow.LogPopupWindow;
import com.zjxfood.popupwindow.XieyiPopupWindow;

import org.apache.http.util.EncodingUtils;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.regex.Pattern;

/**
 * 登录界面
 * 
 * @author zjx
 * 
 */
public class MyUserLogActivity extends AppActivity implements OnClickListener {
	// 登录、注册
	private TextView mLog, mReg;
	// 标题栏
	private LinearLayout mHeadLayout;
	// 忘记密码
	private TextView mForgetPwdText;
	// 返回
	private ImageView mBackImage;
	// 登录按钮
	private Button mLogBtn;
	// 用户名、密码
	private EditText mUserName, mPassWord;
	// private String json = "";
	private Bundle mBundle;
	private String username;
//	private ArrayList<HashMap<String, Object>> mList;
	// 自动登录
	private CheckBox mAutomaticLog;
	// 记住密码
	private CheckBox mRememberPwd;
	// private String id;
	private boolean isLogClick = true;
	// private String sign = "";
	private PopupWindow mRegAlertPop;
	// private TextView mXieyiText;
	private LinearLayout mAlertLayout;
	// private Button mAgreeBtn,mRefuseBtn;
	private XieyiPopupWindow mXieyiPopupWindow;
	private LogPopupWindow mLogPopupWindow;
	private boolean isTimeout = true;
	// IWXAPI 是第三方app和微信通信的openapi接口
	private IWXAPI api;
	String code1 = null;
	private HashMap<String, Object> mUserMapLists;
	private RelativeLayout mWxLayout;
	private HashMap<String, Object> UserMapLists;
	private HashMap<String, Object> mWxMap;
	private HashMap<String, Object> mLogMaps;
	private ImageView mBannerImage;
	private String mFlag = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.user_log_layout);
		setImmerseLayout(findViewById(R.id.head_layout));
		ExitApplication.getInstance().addMyActivity(this);
		initView();
		// 通过WXAPIFactory工厂，获取IWXAPI的实例
		api = WXAPIFactory.createWXAPI(this, Constants.APP_ID2, false);
		api.registerApp(Constants.APP_ID2);
		// 判断是否记住密码
		isRemember();
		mBundle = getIntent().getExtras();
		if (mBundle != null) {
			if(mBundle.getString("flag")!=null){
				mFlag = mBundle.getString("flag");
			}
			username = mBundle.getString("userName");
			mUserName.setText(username);
		}
		//注册广播
		registerBoradcastReceiver();
	}

	private void initView() {
		mHeadLayout = (LinearLayout) findViewById(R.id.user_log_title);
		mLog = (TextView) findViewById(R.id.log_user_log_text);
		mReg = (TextView) findViewById(R.id.log_user_reg_text);
		mForgetPwdText = (TextView) findViewById(R.id.forget_password_text);
		mBackImage = (ImageView) mHeadLayout
				.findViewById(R.id.user_log_back_image);
		mLogBtn = (Button) findViewById(R.id.user_log_submit);
		mUserName = (EditText) findViewById(R.id.user_name_edit);
		mPassWord = (EditText) findViewById(R.id.user_pwd_edit);
		mAutomaticLog = (CheckBox) findViewById(R.id.checkBox_automatic_btn_login);
		mRememberPwd = (CheckBox) findViewById(R.id.checkBox_remember_btn_login);
		mAlertLayout = (LinearLayout) findViewById(R.id.log_not_log_alert_view);
		mWxLayout = (RelativeLayout) findViewById(R.id.my_log_weixin);
		mBannerImage = (ImageView) findViewById(R.id.user_head_image);
		ViewGroup.LayoutParams params = mBannerImage.getLayoutParams();
		params.height = DensityUtils.dp2px(getApplication(),200);
		mBannerImage.setLayoutParams(params);

		mForgetPwdText.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);// 下划线
		mReg.setOnClickListener(this);
		mForgetPwdText.setOnClickListener(this);
		mBackImage.setOnClickListener(this);
		mLogBtn.setOnClickListener(this);
		mAutomaticLog.setOnCheckedChangeListener(mChangeListener);
		mAlertLayout.setOnClickListener(this);
		mWxLayout.setOnClickListener(this);
	}

	// 获取缓存帐号信息
	private void isRemember() {
		SharedPreferences sp = getApplicationContext().getSharedPreferences(
				"MyAccounts", MODE_PRIVATE);
		if (sp != null) {
			mUserName.setText(sp.getString("userName", ""));
			mPassWord.setText(sp.getString("pwd", ""));
		}
	}
	@Override
	public void onClick(View v) {
		Intent intent = new Intent();
		// LayoutInflater inflater =
		// LayoutInflater.from(getApplicationContext());
		switch (v.getId()) {
		case R.id.log_user_reg_text:
			// 注册
			mXieyiPopupWindow = new XieyiPopupWindow(MyUserLogActivity.this,
					itemClick);
			mXieyiPopupWindow.showAtLocation(mLog, Gravity.CENTER, 0, 0);
			break;
		case R.id.forget_password_text:
			// 忘记密码
			intent.setClass(getApplicationContext(), RetrievePwdActivity.class);
			startActivity(intent);
			break;
		case R.id.user_log_back_image:
			finish();
			break;
		case R.id.user_log_submit:
			// 登录
			if (isLogClick) {
				isLogClick = false;
				if (!(mUserName.getText().toString().equals(""))
						&& !(mPassWord.getText().toString().equals(""))) {
					mLogPopupWindow = new LogPopupWindow(MyUserLogActivity.this);
					mLogPopupWindow.showAtLocation(mLog, Gravity.CENTER, 0, 0);
					log();
				} else {
					isLogClick = true;
					Toast.makeText(
							getApplicationContext(),
							getResources().getString(
									R.string.user_log_password_alert),
							Toast.LENGTH_SHORT).show();
				}
			}
			break;
		case R.id.log_not_log_alert_view:
//
			break;
		//微信登录
			case R.id.my_log_weixin:
				WeixinPayActivity.flag = "0";
				Log.i("tag","微信登录======================");
				final SendAuth.Req req = new SendAuth.Req();
				req.scope = "snsapi_userinfo";
				req.state = "wechat_sdk_demo_test";
				api.sendReq(req);
				// 微信登录
//				intent.setClass(getApplicationContext(), WxLogActivity.class);
//				startActivity(intent);
				break;
		}
	}
	//获取授权后的code
	private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			code1 = intent.getExtras().get("code").toString();
			Constants.code = intent.getExtras().get("code").toString();
			Log.i("code", Constants.code+"====================");
			new Thread(gettoken).start();
		}
	};
	//注册广播
	public void registerBoradcastReceiver() {
		IntentFilter myIntentFilter = new IntentFilter();
		myIntentFilter.addAction(WXEntryActivity.action);
		// 注册广播
		registerReceiver(mBroadcastReceiver, myIntentFilter);
	}
	// 根据code得到access_token、openid
	Runnable gettoken = new Runnable() {
		@Override
		public void run() {
			try {
				String strr = Constants.gettoken + "appid=" + Constants.APP_ID2
						+ "&secret=" + Constants.AppSecret2 + "&code="
						+ Constants.code + "&grant_type=authorization_code";
				String json = ReadJson.readParse(strr);
				// Log.i("mUserMapLists", "================" + json);
				mUserMapLists = Constants.getJson2Object(json);
				Log.i("WX",
						"================"
								+ mUserMapLists.get("access_token").toString());
				Constants.access_token = mUserMapLists.get("access_token")
						.toString();
				Constants.openid = mUserMapLists.get("openid").toString();
				Log.i("WX_CODE", "================" + mUserMapLists);
//				Toast.makeText(getApplicationContext(),mUserMapLists+"===",Toast.LENGTH_SHORT).show();
//				handler.sendEmptyMessageDelayed(7, 0);
			} catch (Exception e) {
				e.printStackTrace();
			}
			handler.sendEmptyMessageDelayed(5, 0);
		}
	};
	// 根据access_token、openid得到用户信息
	Runnable userinfo = new Runnable() {
		@Override
		public void run() {
			try {
				String strr = Constants.userinfo + "access_token="
						+ Constants.access_token + "&openid="
						+ Constants.openid;
				String json = ReadJson.readParse(strr);
				// Log.i("mUserMapLists", "================" + json);
				UserMapLists = Constants.getJson2Object(json);
				Constants.nickname = UserMapLists.get("nickname").toString();
//				Toast.makeText(getApplicationContext(),UserMapLists+"用户信息",Toast.LENGTH_SHORT).show();
				Log.i("userinfo", "================" + UserMapLists);
				handler.sendEmptyMessageDelayed(6, 0);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	};
	private android.view.View.OnClickListener itemClick = new OnClickListener() {
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.popup_reg_xieyi_to_confirm_text:
				Intent intent = new Intent();
				intent.setClass(getApplicationContext(),
						MyUserRegActivity.class);
				startActivity(intent);
				MyUserLogActivity.this.finish();
				mXieyiPopupWindow.dismiss();
				// mAlertLayout.setVisibility(View.GONE);
				break;

			case R.id.popup_reg_xieyi_cancel_cancel_text:
				mXieyiPopupWindow.dismiss();
				// mAlertLayout.setVisibility(View.GONE);
				break;
			}
		}
	};

	// 同意协议并注册
	OnClickListener mAgreeClick = new OnClickListener() {
		@Override
		public void onClick(View arg0) {
			Intent intent = new Intent();
			intent.setClass(getApplicationContext(), MyUserRegActivity.class);
			startActivity(intent);
			MyUserLogActivity.this.finish();
			mRegAlertPop.dismiss();
			mAlertLayout.setVisibility(View.GONE);
		}
	};
	OnClickListener mRefuseClick = new OnClickListener() {
		@Override
		public void onClick(View arg0) {
			mRegAlertPop.dismiss();
			mAlertLayout.setVisibility(View.GONE);
		}
	};

	// 读取协议文件
	public String getFromAssets(String fileName) {
		String result = "";
		try {
			InputStream in = getResources().getAssets().open(fileName);
			// 获取文件的字节数
			int lenght = in.available();
			// 创建byte数组
			byte[] buffer = new byte[lenght];
			// 将文件中的数据读到byte数组中
			in.read(buffer);
			result = EncodingUtils.getString(buffer, "UTF-8");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	private void log() {
		handler.sendEmptyMessageDelayed(3, 8000);//超过8秒超时
		RequestParams params = new RequestParams();
		params.put("username", mUserName.getText().toString());
		params.put("password", mPassWord.getText().toString());

		AsyUtils.get(Constants.log2, params, new JsonHttpResponseHandler() {
			@Override
			public void onSuccess(JSONObject response) {
				try {
					mLogMaps = Constants.getJsonObject(response.toString());
//					mList = getJsonLists(response.toString());
					Log.i("mLogMaps", mLogMaps+"=================");
					if (mLogMaps != null && mLogMaps.size() > 0) {
						if (mLogMaps.get("Id").equals("0")) {
							handler.sendEmptyMessageDelayed(2, 0);
						} else {
							Constants.mUserName = mLogMaps.get("Username")
									.toString();
							Constants.mId = mLogMaps.get("Id").toString();
							Constants.mUserCode = mLogMaps.get("Usercode")
									.toString();
							Constants.mPayPassword = mLogMaps
									.get("Paypassword").toString();
							Constants.mShMoney = mLogMaps.get("Shmoney")
									.toString();
							Constants.mPassWord = mPassWord.getText()
									.toString();
							Constants.mFid = mLogMaps.get("Fid").toString();
							Constants.mIsjh = mLogMaps.get("Isjh")
									.toString();
//							Constants.LevelId = mList.get(0).get("Level_id")
//									.toString();
							Constants.UserLevelMemo = mLogMaps.get("UserLevelMemo")
									.toString();
							Constants.headPath = mLogMaps.get("Avatar")
									.toString();
							if (!(mLogMaps.get("Realname").equals("null"))) {
								Constants.mRealname = URLDecoder.decode(mLogMaps
										.get("Realname").toString(),
										"UTF-8");
							} else {
								Constants.mRealname = mLogMaps
										.get("Username").toString();
							}
							// 自动登录
							if (mAutomaticLog.isChecked()) {
								automaticLog();
							} else {
								SharedPreferences sp = getApplicationContext()
										.getSharedPreferences("MyAccounts",
												MODE_PRIVATE);
								Editor editor = sp.edit();
								editor.clear();
								editor.commit();
							}
							// 记住密码
							if (mRememberPwd.isChecked()
									&& !mAutomaticLog.isChecked()) {
								rememberPwd();
							}
							Constants.onLine = 1;
							handler.sendEmptyMessageDelayed(1, 0);
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				//友盟帐号统计
				MobclickAgent.onProfileSignIn(mUserName.getText().toString());
				Log.i("JSONObject", response + "==================");
				super.onSuccess(response);
			}

			@Override
			public void onFailure(Throwable e, JSONObject errorResponse) {
				handler.sendEmptyMessageDelayed(4, 0);
				Log.i("onFailure", errorResponse
						+ "========JSONObject==========" + errorResponse);
				super.onFailure(e, errorResponse);
			}
		});
	}

	// 自动登录
	private void automaticLog() {
		Pattern p = Pattern.compile("\\s*|\t|\r|\n");
		SharedPreferences sp = getApplicationContext().getSharedPreferences(
				"MyAccounts", MODE_PRIVATE);
		Editor editor = sp.edit();
		editor.putString("userName", p.matcher(mUserName.getText().toString())
				.replaceAll(""));
		editor.putString("pwd", p.matcher(mPassWord.getText().toString())
				.replaceAll(""));
		editor.putString("Id", mLogMaps.get("Id").toString());
		editor.putString("userCode", mLogMaps.get("Usercode").toString());
		editor.putString("Fid", mLogMaps.get("Fid").toString());
		editor.putString("Isjh", mLogMaps.get("Isjh").toString());
		editor.putString("headPath", mLogMaps.get("Avatar").toString());
		try {
			editor.putString(
					"Realname",
					URLDecoder.decode(mLogMaps.get("Realname").toString(),
							"UTF-8").toString());
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		editor.putString("flag", 1 + "");
		editor.commit();
	}
	//微信自动登录
	private void wxAutomaticLog(){
		Pattern p = Pattern.compile("\\s*|\t|\r|\n");
		SharedPreferences sp = getApplicationContext().getSharedPreferences(
				"MyAccounts", MODE_PRIVATE);
		Editor editor = sp.edit();
		editor.putString("userName", p.matcher(mUserName.getText().toString())
				.replaceAll(""));
		editor.putString("pwd", "null");
		editor.putString("unionid", UserMapLists.get("unionid").toString());
		editor.putString("Id", mLogMaps.get("Id").toString());
		editor.putString("userCode", mLogMaps.get("Usercode").toString());
		editor.putString("Fid", mLogMaps.get("Fid").toString());
		editor.putString("Isjh", mLogMaps.get("Isjh").toString());
		editor.putString("headPath", mLogMaps.get("Avatar").toString());
		try {
			editor.putString(
					"Realname",
					URLDecoder.decode(mLogMaps.get("Realname").toString(),
							"UTF-8").toString());
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		editor.putString("flag", 2 + "");
		editor.commit();
	}

	// 记住密码
	private void rememberPwd() {
		Pattern p = Pattern.compile("\\s*|\t|\r|\n");
		SharedPreferences sp = getApplicationContext().getSharedPreferences(
				"MyAccounts", MODE_PRIVATE);
		Editor editor = sp.edit();
		editor.putString("userName", p.matcher(mUserName.getText().toString())
				.replaceAll(""));
		editor.putString("pwd", p.matcher(mPassWord.getText().toString())
				.replaceAll(""));
		editor.putString("flag", 0 + "");
		editor.commit();
	}

	Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			Intent intent = new Intent();
			Bundle bundle;
			switch (msg.what) {
			case 1:
				isTimeout = false;
				if(mLogPopupWindow!=null && mLogPopupWindow.isShowing()) {
					mLogPopupWindow.dismiss();
				}
				// 跳转我的界面
				isLogClick = true;
//				intent.setClass(getApplicationContext(), MyActivity.class);
				if(mFlag.equals("cars")){
					intent.setClass(getApplicationContext(), NewCarsActivity.class);
					startActivity(intent);
				}else{
					intent.setClass(getApplicationContext(), MyNewActivity.class);
					bundle = new Bundle();
					bundle.putString("name", mUserName.getText().toString());
					intent.putExtras(bundle);
					startActivity(intent);
				}

				finish();
				break;
			case 2:
				mLogPopupWindow.dismiss();
				Toast.makeText(getApplicationContext(), "登录失败！用户名或密码错误！",
						Toast.LENGTH_SHORT).show();
				isLogClick = true;
				break;
			case 3:
				if(isTimeout){
//					mLogPopupWindow.dismiss();
//					Toast.makeText(getApplicationContext(), "登录超时！",
//							Toast.LENGTH_SHORT).show();
				}
				break;
			case 4:
				mLogPopupWindow.dismiss();
				Toast.makeText(getApplicationContext(), "登录失败！",
						Toast.LENGTH_SHORT).show();
				isLogClick = true;
				break;
			case 5:
				new Thread(userinfo).start();
			break;
			case 6:
//					Toast.makeText(getApplicationContext(), "获取用户信息成功！",
//							Toast.LENGTH_SHORT).show();
				Log.i("userinfo", "=========unionid=======" + UserMapLists);
				wxLog();
			break;
			case 7:
				try {
				intent.setClass(getApplicationContext(), WxLogActivity.class);
				bundle = new Bundle();
				bundle.putString("unionId", UserMapLists.get("unionid").toString());
				bundle.putString("openid", UserMapLists.get("openid").toString());

				bundle.putString("nickname", UserMapLists.get("nickname").toString());
				bundle.putString("headimgurl", UserMapLists.get("headimgurl").toString());
				intent.putExtras(bundle);
				startActivity(intent);
				}catch (Exception e){

				}
			break;
			}
		};
	};
	// 微信登录
	private void wxLog() {
		StringBuilder sb = new StringBuilder();
		sb.append("unionId=" + UserMapLists.get("unionid"));
		XutilsUtils.get(Constants.wxLog, sb,
				new RequestCallBack<String>() {
					@Override
					public void onFailure(HttpException arg0, String arg1) {
					}
					@Override
					public void onSuccess(ResponseInfo<String> res) {
						Log.i("登录", res.result + "===============");
						mWxMap = Constants.getJsonObject(res.result);
						if(mWxMap!=null && mWxMap.size()>0 && mWxMap.get("Code")!=null){
							if(!mWxMap.get("Code").toString().equals("200")){
								//跳转到绑定页面
								handler.sendEmptyMessageAtTime(7,0);
							}else{
								//登录成功
								mLogMaps = Constants.getJsonObjectByData(res.result);
								try{
								if (mLogMaps != null && mLogMaps.size() > 0) {
									if (mLogMaps.get("Id").equals("0")) {
										handler.sendEmptyMessageDelayed(2, 0);
									} else {
										Constants.mUserName = mLogMaps.get("Username")
												.toString();
										Constants.mId = mLogMaps.get("Id").toString();
										Constants.mUserCode = mLogMaps.get("Usercode")
												.toString();
										Constants.mPayPassword = mLogMaps
												.get("Paypassword").toString();
										Constants.mShMoney = mLogMaps.get("Shmoney")
												.toString();
										Constants.mPassWord = mPassWord.getText()
												.toString();
										Constants.mFid = mLogMaps.get("Fid").toString();
										Constants.mIsjh = mLogMaps.get("Isjh")
												.toString();
										Constants.headPath = mLogMaps.get("Avatar")
												.toString();
//										Constants.LevelId = mLogMaps.get("Level_id")
//												.toString();
										Constants.UserLevelMemo = mLogMaps.get("UserLevelMemo")
												.toString();
										if (!(mLogMaps.get("Realname").equals("null"))) {
											Constants.mRealname = URLDecoder.decode(mLogMaps
															.get("Realname").toString(),
													"UTF-8");
										} else {
											Constants.mRealname = mLogMaps
													.get("Username").toString();
										}
										// 微信自动登录
										if (mAutomaticLog.isChecked()) {
											wxAutomaticLog();
										} else {
											SharedPreferences sp = getApplicationContext()
													.getSharedPreferences("MyAccounts",
															MODE_PRIVATE);
											Editor editor = sp.edit();
											editor.clear();
											editor.commit();
										}
										// 记住密码
//										if (mRememberPwd.isChecked()
//												&& !mAutomaticLog.isChecked()) {
//											rememberPwd();
//										}
										Constants.onLine = 1;
										handler.sendEmptyMessageDelayed(1, 0);
									}
								}
								}catch (Exception e){}
							}
						}
//                        mToatleMap = Constants.getJsonObjectByData(res.result);
//                        mAuctionPeople.setText("出价次数："
//                                + mToatleMap.get("total"));
//
					}
				});
	}
	OnCheckedChangeListener mChangeListener = new OnCheckedChangeListener() {
		@Override
		public void onCheckedChanged(CompoundButton arg0, boolean isChecked) {
			if (isChecked) {
				mRememberPwd.setChecked(true);
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

	@Override
	protected void onDestroy() {
		super.onDestroy();
		unregisterReceiver(mBroadcastReceiver);
	}
}
