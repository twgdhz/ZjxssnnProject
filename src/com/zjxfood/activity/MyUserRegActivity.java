package com.zjxfood.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.ContentObserver;
import android.database.Cursor;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Build;
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
import android.widget.TextView;
import android.widget.Toast;

import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.project.util.DensityUtils;
import com.umeng.analytics.MobclickAgent;
import com.zjxfood.activity.R;
import com.zjxfood.application.ExitApplication;
import com.zjxfood.common.Constants;
import com.zjxfood.http.AsyUtils;
import com.zjxfood.http.XutilsUtils;

/**
 * 注册
 * 
 * @author zjx
 * 
 */
public class MyUserRegActivity extends AppActivity implements OnClickListener {

	private TextView mLog;
	private LinearLayout mHeadLayout;
	private TextView mCodeText;
	private ImageView mBackImage;
	private Button mRegBtn;
	private EditText mUserName;
	private EditText mPassword, mPassword2;
	private EditText mCode;
	private TextView mGetCode;
	private String code;// 验证码
	private boolean isClick = true;
	private TextView getCodeTime;
	private int n = 60;
	private Timer mTimer;
	private Pattern p = Pattern.compile("\\s*|\t|\r|\n");
	private CheckBox mCheckBox;
	private boolean isReg = true;
	private TextView mScannCode;
	private EditText mInvitationCode;
	private IntentFilter filter;
	private TextView mXieyiText;
//	private boolean isPhoneExist; // 电话号码是否已经存在
	private boolean isPhone = false;// 判断用户是否已经注册
//	private String sign = "";
	private final static int SCANNIN_GREQUEST_CODE = 1;
	private boolean isRegClick = true;
	private HashMap<String, Object> mUserMap1;
	private HashMap<String, Object> mUserMap2;
	private HashMap<String, Object> mNeedMap;
	private String isNeedJhm;
	private ImageView mBannerImage;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.user_reg_layout);
		setImmerseLayout(findViewById(R.id.head_layout));
		ExitApplication.getInstance().addActivity(this);
		ExitApplication.getInstance().addMyActivity(this);
		initView();
		getNeedJhm();
		mCodeText.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
		mScannCode.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
		mCheckBox.setOnCheckedChangeListener(mChangeListener);
//		IntentFilter filter = new IntentFilter("android.provider.Telephony.SMS_RECEIVED");
//		registerReceiver(broadcastReceiver, filter);
		Log.e("userReg", "===================");
		SmsContent content = new SmsContent(handler);
		  // 注册短信变化监听
		  this.getContentResolver().registerContentObserver(Uri.parse("content://sms/"), true, content);
	}

	private void initView() {
		mHeadLayout = (LinearLayout) findViewById(R.id.user_reg_title);
		mLog = (TextView) mHeadLayout.findViewById(R.id.reg_user_log_text);
		mCodeText = (TextView) findViewById(R.id.get_code_text);
		mBackImage = (ImageView) mHeadLayout
				.findViewById(R.id.user_reg_back_image);
		mRegBtn = (Button) findViewById(R.id.user_log_submit);
		mUserName = (EditText) findViewById(R.id.user_name_edit);
		mCode = (EditText) findViewById(R.id.user_code_edit);
		mPassword = (EditText) findViewById(R.id.user_password_edit);
		mPassword2 = (EditText) findViewById(R.id.user_confirm_password_edit);
		mGetCode = (TextView) findViewById(R.id.get_code_text);
		getCodeTime = (TextView) findViewById(R.id.get_code_text_time);
		mCheckBox = (CheckBox) findViewById(R.id.reg_agree_check);
		mScannCode = (TextView) findViewById(R.id.get_invitation_code_text);
		mInvitationCode = (EditText) findViewById(R.id.invitation_code_edit);
		mXieyiText = (TextView) findViewById(R.id.reg_agree_xieyi_text);
		mBannerImage = (ImageView) findViewById(R.id.user_head_image);
		ViewGroup.LayoutParams params = mBannerImage.getLayoutParams();
		params.height = DensityUtils.dp2px(getApplication(),200);
		mBannerImage.setLayoutParams(params);
		mLog.setOnClickListener(this);
		mBackImage.setOnClickListener(this);
		mRegBtn.setOnClickListener(this);
		mGetCode.setOnClickListener(this);
		mScannCode.setOnClickListener(this);
		mXieyiText.setOnClickListener(this);

	}
	
	private void isRegPhone() {
		if (checkPhoneNum(mUserName.getText().toString())) {
		StringBuilder sb = new StringBuilder();
			sb.append("username=" + mUserName.getText().toString());
		XutilsUtils.get(Constants.isReg2, sb,
				new RequestCallBack<String>() {
					@Override
					public void onFailure(HttpException arg0, String arg1) {
						isPhone = true;
					}
					@Override
					public void onSuccess(ResponseInfo<String> res) {
						if ((res.result).equals("true")) {
							isPhone = true;
						} else {
							isPhone = false;
						}
						sendCode();
					}
				});
		} else {
			Toast.makeText(getApplicationContext(), "手机号码格式不正确！",
					Toast.LENGTH_SHORT).show();
		}
	}
//是否需要推荐码
	private void getNeedJhm() {
		RequestParams params = new RequestParams();
		AsyUtils.get(Constants.getNeedJhm, params,
				new JsonHttpResponseHandler() {
					@Override
					public void onSuccess(int statusCode, JSONObject response) {
						
						mNeedMap = Constants.getJsonObject(response.toString());
						if(mNeedMap!=null && mNeedMap.get("Data")!=null){
						isNeedJhm = mNeedMap.get("Data").toString();
						}
						Log.i("结果返回", mNeedMap+"=========mNeedMap======");
						super.onSuccess(statusCode, response);
					}
				});
	}
	
	@Override
	public void onClick(View v) {
		Intent intent = new Intent();
		switch (v.getId()) {
		case R.id.reg_user_log_text:
			intent.setClass(getApplicationContext(), MyUserLogActivity.class);
			startActivity(intent);
			this.finish();
			break;

		case R.id.user_reg_back_image:
			code = "";
			finish();
			break;
		case R.id.user_log_submit:
			if (isReg) {
				if (isRegClick) {
					isRegClick = false;
					isReg();
				}
			}
			break;

		// 实现发送手机验证码
		// 发送成功倒计时
		case R.id.get_code_text:
			isRegPhone();
			break;
		case R.id.get_invitation_code_text:
			//屏蔽推荐码扫描
//			intent.setClass(getApplicationContext(), ZxingActivity.class);
//			Bundle bundle = new Bundle();
//			bundle.putString("flag", "2");
//			intent.putExtras(bundle);
//			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//			startActivityForResult(intent, SCANNIN_GREQUEST_CODE);
//			startActivity(intent);
			break;
		case R.id.reg_agree_xieyi_text:
			intent.setClass(getApplicationContext(), XieyiActivity.class);
			startActivity(intent);
			break;
		}
	}

	// 发送验证码
	private void sendCode() {
		if (!isPhone) {
//		if (true) {
			if (isClick) {
				Random random = new Random();
				String result = "";
				for (int i = 0; i < 6; i++) {
					result += random.nextInt(10);
				}
				code = result;
				Log.i("code", code + "===========code============");
				mTimer = new Timer();
				TimerTask task = new TimerTask() {
					public void run() {
						n--;
						if (n <= 0) {
							handler.sendEmptyMessageDelayed(4, 0);
						} else {
							handler.sendEmptyMessageDelayed(3, 0);
						}
					}
				};
				mTimer.schedule(task, 60, 1000);
				StringBuilder sb = new StringBuilder();
				sb.append("mobile="+mUserName.getText().toString()+"&code="+code);
				XutilsUtils.get(Constants.sendCode3, sb,
						new RequestCallBack<String>() {
							@Override
							public void onFailure(HttpException arg0, String arg1) {}
							@Override
							public void onSuccess(ResponseInfo<String> res) {
								if (res.result.equals("true")) {
									handler.sendEmptyMessageDelayed(2, 0);
								} else {
									Log.i("codejson","=========验证码发送失败============");
							}
							}
						});
				
				
			}
		} else {
			Toast.makeText(getApplicationContext(), "手机号码已经被注册！",
					Toast.LENGTH_SHORT).show();
		}
	}

	private void isReg() {
		if (!(mUserName.getText().toString().equals(""))) {
			if (checkPhoneNum(mUserName.getText().toString())) {
				if (!(mCode.getText().toString().equals(""))
						&& mCode.getText().toString().equals(code)) {
					if(isNeedJhm!=null){
						if(isNeedJhm.equals("1")){
							if (!(mInvitationCode.getText().toString().equals(""))) {
								if (isPwd(mPassword.getText().toString())) {
									if (!(mPassword.getText().toString().equals(""))
											&& mPassword.getText().toString().length() >= 6) {
										if (mPassword
												.getText()
												.toString()
												.equals(mPassword2.getText().toString())) {
											if (!(mPassword2.getText().toString()
													.equals(""))) {
												// new Thread(regRun).start();
												regHttp();
											}
										} else {
											isRegClick = true;
											Toast.makeText(getApplicationContext(),
													"两次密码输入不相同！", Toast.LENGTH_SHORT)
													.show();
										}
									} else {
										isRegClick = true;
										Toast.makeText(getApplicationContext(),
												"密码必须为六位字符以上！", Toast.LENGTH_SHORT)
												.show();
									}
								} else {
									isRegClick = true;
									Toast.makeText(getApplicationContext(), "密码格式不正确！",
											Toast.LENGTH_SHORT).show();
								}
							} else {
								isRegClick = true;
								Toast.makeText(getApplicationContext(), "推荐码不能为空！",
										Toast.LENGTH_SHORT).show();
							}
						}else if(isNeedJhm.equals("0")){
							if (isPwd(mPassword.getText().toString())) {
								if (!(mPassword.getText().toString().equals(""))
										&& mPassword.getText().toString().length() >= 6) {
									if (mPassword
											.getText()
											.toString()
											.equals(mPassword2.getText().toString())) {
										if (!(mPassword2.getText().toString()
												.equals(""))) {
											// new Thread(regRun).start();
											regHttp();
										}
									} else {
										isRegClick = true;
										Toast.makeText(getApplicationContext(),
												"两次密码输入不相同！", Toast.LENGTH_SHORT)
												.show();
									}
								} else {
									isRegClick = true;
									Toast.makeText(getApplicationContext(),
											"密码必须为六位字符以上！", Toast.LENGTH_SHORT)
											.show();
								}
							} else {
								isRegClick = true;
								Toast.makeText(getApplicationContext(), "密码格式不正确！",
										Toast.LENGTH_SHORT).show();
							}
						}
					}else{
						Toast.makeText(getApplicationContext(), "注册失败，请检查网络是否良好", Toast.LENGTH_SHORT).show();
					}
					
				} else {
					isRegClick = true;
					Toast.makeText(getApplicationContext(), "验证码错误！",
							Toast.LENGTH_SHORT).show();
				}
			} else {
				isRegClick = true;
				Toast toast = Toast.makeText(getApplicationContext(),
						"请输入正确的手机号码！", Toast.LENGTH_SHORT);
				toast.setGravity(Gravity.CENTER, 0, 0);
				toast.show();
			}
		} else {
			isRegClick = true;
			Toast.makeText(getApplicationContext(), "请输入手机号码！",
					Toast.LENGTH_SHORT).show();
		}
	}

	private void regHttp() {
		RequestParams params = new RequestParams();
//		if(mInvitationCode.getText().toString().equals(""))
		params.put("code", mInvitationCode.getText().toString());
		params.put("username", mUserName.getText().toString());
		params.put("password", p.matcher(mPassword.getText().toString())
				.replaceAll("") + "");
		params.put("vcode", mCode.getText().toString());
		AsyUtils.get(Constants.reg3, params, new JsonHttpResponseHandler() {
			@Override
			public void onSuccess(JSONObject response) {
				if (response.toString().equals("null")) {
					handler.sendEmptyMessageDelayed(5, 0);
				} else {
						mUserMap1 = Constants.getJsonObject(response.toString());
						if (mUserMap1.get("Code").toString().equals("200")
								&& !Constants.isNull(mUserMap1.get("Data"))) {
							mUserMap2 = Constants.getJsonObject(mUserMap1.get(
									"Data").toString());
							handler.sendEmptyMessageDelayed(1, 0);
						} else {
							handler.sendEmptyMessageDelayed(6, 0);
						}
				}
				super.onSuccess(response);
			}
			@Override
			public void onFailure(Throwable e, JSONObject errorResponse) {
				Log.i("onFailure", errorResponse + "===========");
				handler.sendEmptyMessageDelayed(5, 0);
				super.onFailure(e, errorResponse);
			}
		});
	}

	// 注册成功 增加上级用户20食尚币
	Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			Intent intent = new Intent();
			switch (msg.what) {
			case 1:
				if (mUserMap2 != null && mUserMap2.size() > 0) {
					isRegClick = true;
					// 注册成功后跳转到这里 并跳转至登录页面MyUserLogActivity
					intent.setClass(getApplicationContext(),
							MyUserLogActivity.class);
					Bundle bundle = new Bundle();

					bundle.putString("userName", mUserName.getText().toString());

					bundle.putString("Id", mUserMap2.get("Id").toString());
					intent.putExtras(bundle);
					startActivity(intent);
					finish();
					Toast.makeText(getApplicationContext(),
							mUserMap1.get("Message").toString(),
							Toast.LENGTH_SHORT).show();
				} else {
					isRegClick = true;
					Toast.makeText(getApplicationContext(), "注册失败",
							Toast.LENGTH_SHORT).show();
				}

				break;

			case 2:
				Toast.makeText(getApplicationContext(), "验证码发送成功！",
						Toast.LENGTH_SHORT).show();
				mGetCode.setTextColor(getResources().getColor(R.color.gray));
				isClick = false;
				break;
			case 3:
				getCodeTime.setText(n + "");
				getCodeTime.setVisibility(View.VISIBLE);
				mGetCode.setTextColor(getResources().getColor(R.color.gray));
				break;
			case 4:
				if (mTimer != null) {
					mTimer.cancel();
					mTimer.purge();
					mTimer = null;
					mGetCode.setTextColor(getResources().getColor(
							R.color.main_title_color));
					isClick = true;
					n = 60;
					getCodeTime.setVisibility(View.GONE);
				}
				break;
			case 5:
				isRegClick = true;
				Toast.makeText(getApplicationContext(), "注册失败,用户资料填写不正确！",
						Toast.LENGTH_SHORT).show();
				break;
			case 6:
				if (mUserMap1 != null && mUserMap1.size() > 0) {
					Toast.makeText(getApplicationContext(),
							mUserMap1.get("Message").toString(),
							Toast.LENGTH_SHORT).show();
				}
				isRegClick = true;
				break;

			}
		};
	};

	// 解析json数据
	public ArrayList<HashMap<String, Object>> getJsonList(String json)
			throws JSONException {
		ArrayList<HashMap<String, Object>> list = new ArrayList<HashMap<String, Object>>();

		JSONObject jsonObject = new JSONObject(json);
		Iterator<?> it = jsonObject.keys();
		String a;
		while (it.hasNext()) {
			HashMap<String, Object> map = new HashMap<String, Object>();
			a = it.next().toString();
			map.put(a, jsonObject.get(a).toString());
			list.add(map);
		}
		return list;
	}

	/**
	 * 获取注册完成后 返回的JSON用户信息 并解析json数据（用户ID和用户账号即手机号）
	 * 
	 * @param 注册完成后服务器返回的json数据
	 * @return list
	 * @throws JSONException
	 */
	public ArrayList<HashMap<String, Object>> getUserList(String json)
			throws JSONException {
		ArrayList<HashMap<String, Object>> list = new ArrayList<HashMap<String, Object>>();

		HashMap<String, Object> map = new HashMap<String, Object>();
		JSONObject jsonObject = new JSONObject(json);
		map.put("Id", jsonObject.getString("Id"));
		map.put("Username", jsonObject.getString("Username"));
		list.add(map);

		return list;
	}

	OnCheckedChangeListener mChangeListener = new OnCheckedChangeListener() {

		@Override
		public void onCheckedChanged(CompoundButton arg0, boolean check) {
			if (check) {
				mRegBtn.setBackgroundDrawable(getResources().getDrawable(
						R.drawable.bg_message_btn));
				isReg = true;
			} else {
				mRegBtn.setBackgroundDrawable(getResources().getDrawable(
						R.drawable.bg_un_log_btn));
				isReg = false;
			}
		}
	};

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		switch (keyCode) {
		case KeyEvent.KEYCODE_BACK:
			code = "";
			finish();
			break;

		default:
			break;
		}
		return super.onKeyDown(keyCode, event);
	}

	// 验证密码
	public static boolean isPwd(String str) {
		String regex = "[0-9A-Za-z]*";
		return match(regex, str);
	}

	/**
	 * 判段手机号格式是否正确
	 * 
	 * @param phoneNum
	 * @return boolean
	 */
	public boolean checkPhoneNum(String phoneNum) {
		Pattern pattern = Pattern.compile("^1\\d{10}$");
		Matcher matcher = pattern.matcher(phoneNum);

		if (matcher.matches()) {
			return true;
		}
		return false;
	}

	private static boolean match(String regex, String str) {
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(str);
		return matcher.matches();
	}

	protected void onPause() {
		super.onPause();
		MobclickAgent.onPause(this);
	};

	protected void onResume() {
		super.onResume();
		MobclickAgent.onResume(this);
	};

//	BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
//		@Override
//		public void onReceive(Context arg0, Intent intent) {
//			Bundle bundle = intent.getExtras();  
//	        SmsMessage msg = null;  
//	        if (null != bundle) {  
//	            Object[] smsObj = (Object[]) bundle.get("pdus");  
//	            for (Object object : smsObj) {  
//	                msg = SmsMessage.createFromPdu((byte[]) object);  
//	                  Log.e("短信内容", "number:" + msg.getOriginatingAddress()  
//	                + "   body:" + msg.getDisplayMessageBody() + "  time:"  
//	                        + msg.getTimestampMillis()+"============");
//	                  if(msg!=null && msg.getDisplayMessageBody()!=null){
//	                  mCode.setText(msg.getDisplayMessageBody()+"");
//	                  }
//	                //在这里写自己的逻辑  
//	                if (msg.getOriginatingAddress().equals("10086")) {  
//	                    //TODO  
//	                      
//	                }  
//	                  
//	            }  
//	        }  
//		}
//	};

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
		case SCANNIN_GREQUEST_CODE:
			Log.i("usercode", "========reg===2======");
			// 显示扫描到的内容
			if (!(ZxingActivity.userCode.equals(""))) {
				mInvitationCode.setText(ZxingActivity.userCode);
			}
			break;
		}
	};
	
	/**
     * 监听短信数据库
     */
    class SmsContent extends ContentObserver {

        private Cursor cursor = null;

        public SmsContent(Handler handler) {
            super(handler);
        }
        @Override
        public void onChange(boolean selfChange) {
            super.onChange(selfChange);
            try{
            //读取收件箱中指定号码的短信
            cursor = managedQuery(Uri.parse("content://sms/inbox"), new String[]{"_id", "address", "read", "body"},
                    " address=? and read=?", new String[]{"10690278225431", "0"}, "_id desc");//按id排序，如果按date排序的话，修改手机时间后，读取的短信就不准了
//            Log.i("cursor.isBeforeFirst() " + cursor.isBeforeFirst() + " cursor.getCount()  " + cursor.getCount());
            if (cursor != null && cursor.getCount() > 0) {
                ContentValues values = new ContentValues();
                values.put("read", "1");        //修改短信为已读模式
                cursor.moveToNext();
                int smsbodyColumn = cursor.getColumnIndex("body");
                String smsBody = cursor.getString(smsbodyColumn);
//                MyLog.v("smsBody = " + smsBody);
                Log.e("sms", "smsBody = " + smsBody+"===========");
                mCode.setText(getDynamicPassword(smsBody));
            }
            //在用managedQuery的时候，不能主动调用close()方法， 否则在Android 4.0+的系统上， 会发生崩溃
            if(Build.VERSION.SDK_INT < 14) {
                cursor.close();
            }
            }catch(Exception e){
            	e.printStackTrace();
            }
        }
    }
    public static String getDynamicPassword(String str) {
        Pattern  continuousNumberPattern = Pattern.compile("[0-9\\.]+");
        Matcher m = continuousNumberPattern.matcher(str);
        String dynamicPassword = "";
        while(m.find()){
            if(m.group().length() == 6) {
                System.out.print(m.group());
                dynamicPassword = m.group();
            }
        }

        return dynamicPassword;
    }
}
