package com.zjxfood.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
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
import com.umeng.analytics.MobclickAgent;
import com.zjxfood.common.Constants;
import com.zjxfood.http.XutilsUtils;

import java.util.HashMap;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Administrator 忘记密码 通过手机验证找回密码
 */
public class RetrievePwdActivity extends AppActivity implements OnClickListener {

//	private FrameLayout mFrameLayout1, mFrameLayout2, mFrameLayout3;
	private PopupWindow mPopupWindow;
//	private ListView mListView;
	private String[] mArrays;
//	private RetrieveSpinnerAdapter mSpinnerAdapter;
	private TextView mProblemText1, mProblemText2, mProblemText3;
//	private ScrollView mScrollView;
	private Button mNextBtn,mResetBtn;
//	private TextView mSetUpPwdText, mSecurityText;
//	private View mSetUpView, mSecurityView;
	private TextView mGetCode;
	private RelativeLayout mHeadLayout;
	private ImageView mBackImage;
	private EditText mPhoneEdit, mCodeEdit;
	private String code;
	private int n = 60;
	private Timer mTimer;
	private LinearLayout mCheckPhone, mSetNewPsw,mSafeCheck;
	private boolean isClick = true;
	private TextView mCodeText;
	private EditText mNewPwd, mNewPwd2;
//	private Button mNewBtn;// 找回密码完成按钮
	private Pattern p = Pattern.compile("\\s*|\t|\r|\n");
	private boolean isReg = true;
	private HashMap<String, Object> mIsRegMap;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.retrieve_password_layout);
		setImmerseLayout(findViewById(R.id.head_layout));
		init();
		mArrays = getResources().getStringArray(R.array.problem);
	}

	private void init() {
		mNextBtn = (Button) findViewById(R.id.retrieve_next_btn);
//		mSetUpView = findViewById(R.id.security_verification_setting_view);
//		mSecurityText = (TextView) findViewById(R.id.security_verification_text);
		mGetCode = (TextView) findViewById(R.id.retrieve_pwd_get_code_text);
		mHeadLayout = (RelativeLayout) findViewById(R.id.title_retrieve_pwd_id);
		mBackImage = (ImageView) mHeadLayout.findViewById(R.id.retrieve_password_image);
		mPhoneEdit = (EditText) findViewById(R.id.retrieve_pwd_input_phone_text);
		mCodeEdit = (EditText) findViewById(R.id.retrieve_pwd_input_phone_code_text);
		mNewPwd = (EditText) findViewById(R.id.new_pwd_edit);
		mNewPwd2 = (EditText) findViewById(R.id.new_pwd_two_edit);
		mCodeText = (TextView) findViewById(R.id.retrieve_pwd_text);
		mCheckPhone = (LinearLayout) findViewById(R.id.retrieve_pwd_content_layout);
		mSetNewPsw = (LinearLayout) findViewById(R.id.set_newpwd_layout);
		mResetBtn=(Button) findViewById(R.id.reset_psw_btn);
		mSafeCheck = (LinearLayout) findViewById(R.id.retrieve_pwd_security_verification_layout);
		mGetCode.setOnClickListener(this);
		mSetNewPsw.setVisibility(View.GONE);
		mNextBtn.setOnClickListener(this);
		mResetBtn.setOnClickListener(this);
		mBackImage.setOnClickListener(this);
		mGetCode.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
	}

	@SuppressLint("InflateParams")
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.retrieve_next_btn:
			Log.i("gallery", code + "=======================");
			if (mCodeEdit.getText().toString().equals(code)) {
				mCheckPhone.setVisibility(View.GONE);
				mSafeCheck.setVisibility(View.GONE);
				mSetNewPsw.setVisibility(View.VISIBLE);
				
			} else {
				Toast.makeText(getApplicationContext(), "验证码输入错误！",
						Toast.LENGTH_SHORT).show();
			}
			break;
		// 完成设置新密码的按钮
		case R.id.reset_psw_btn:
			if (!mNewPwd.getText().toString().equals(" ")
					&& !mNewPwd2.getText().toString().equals(" ")) {
				if (isPwd(mNewPwd.getText().toString())
						&& isPwd(mNewPwd2.getText().toString())) {
					if (mNewPwd.getText().toString()
							.equals(mNewPwd2.getText().toString())) {
//						new Thread(resetPswRun).start();
						resetPswHttp();
					} else {
						Toast.makeText(getApplicationContext(), "两次密码输入不一致！",
								Toast.LENGTH_SHORT).show();
					}
				} else {
					Toast.makeText(getApplicationContext(), "密码格式不正确！",
							Toast.LENGTH_SHORT).show();
				}
			} else {
				Toast.makeText(getApplicationContext(), "密码不能为空！",
						Toast.LENGTH_SHORT).show();
			}
			break;

		case R.id.retrieve_password_image:
			finish();
			break;
		case R.id.retrieve_pwd_get_code_text:
			if (!(mPhoneEdit.getText().toString().equals(""))) {
				if (isClick) {
//					if(isReg){
					
//					new Thread(codeRun).start();
						isRegPhone();
//					}else {
//						Toast.makeText(getApplicationContext(), "该手机号码尚未注册!",
//								Toast.LENGTH_SHORT).show();
//					}
				}
			} else {
				Toast.makeText(getApplicationContext(), "手机号不能为空!",
						Toast.LENGTH_SHORT).show();
			}
			Log.i("gallery", code + "=======================");
			break;
		}

	}
	//判断手机号码是否注册
	private void isRegPhone() {
		if (checkPhoneNum(mPhoneEdit.getText().toString())) {
		StringBuilder sb = new StringBuilder();
			sb.append("username=" + mPhoneEdit.getText().toString());
		XutilsUtils.get(Constants.isReg2, sb,
				new RequestCallBack<String>() {
					@Override
					public void onFailure(HttpException arg0, String arg1) {
//						isReg = false;
//						Toast.makeText(getApplicationContext(), arg1,
//								Toast.LENGTH_SHORT).show();
					}
					@Override
					public void onSuccess(ResponseInfo<String> res) {
//						mIsRegMap = Constants.getJsonObject(res.result);
//						if(mIsRegMap!=null){
//							if(mIsRegMap.get("Data")!=null){
//								
//							}
//						}
						if ((res.result).equals("true")) {
							isReg = true;
						} else {
							isReg = false;
						}
						if(isReg){
							codeHttp();
						}else{
							Toast.makeText(getApplicationContext(), "手机号码尚未注册", Toast.LENGTH_SHORT).show();
						}
					}
				});
		} else {
			Toast.makeText(getApplicationContext(), "手机号码格式不正确！",
					Toast.LENGTH_SHORT).show();
		}
	}
	
	OnItemClickListener mItemClickListener1 = new OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> parent, View v, int position,
				long id) {
			mProblemText1.setText(mArrays[position]);
			mPopupWindow.dismiss();
		}
	};

	OnItemClickListener mItemClickListener2 = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View v, int position,
				long id) {
			mProblemText2.setText(mArrays[position]);
			mPopupWindow.dismiss();
		}
	};

	OnItemClickListener mItemClickListener3 = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View v, int position,
				long id) {
			mProblemText3.setText(mArrays[position]);
			mPopupWindow.dismiss();

		}
	};
	private void codeHttp() {
		mGetCode.setTextColor(getResources().getColor(R.color.gray));
		isClick = false;
		Random random = new Random();
		String result = "";
		for (int i = 0; i < 6; i++) {
			result += random.nextInt(10);
		}
		code = result;
		Log.i("test", code + "==========code=========");
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
			sb.append("mobile=" + mPhoneEdit.getText().toString()+"&code="+code);
		XutilsUtils.get(Constants.sendCode3, sb,
				new RequestCallBack<String>() {
					@Override
					public void onFailure(HttpException arg0, String arg1) {
						handler.sendEmptyMessageDelayed(5, 0);
					}
					@Override
					public void onSuccess(ResponseInfo<String> res) {
						if((res.result).equals("true")){
							 handler.sendEmptyMessageDelayed(2, 0);
						 }else{
							 handler.sendEmptyMessageDelayed(5, 0);
						 }
					}
				});
	}
	private void resetPswHttp() {
		StringBuilder sb = new StringBuilder();
			sb.append("mobile=" + mPhoneEdit.getText().toString()+"&password="+p.matcher(mNewPwd.getText().toString()).replaceAll(""));
		XutilsUtils.get(Constants.resetLoginPsw2, sb,
				new RequestCallBack<String>() {
					@Override
					public void onFailure(HttpException arg0, String arg1) {
						Toast.makeText(getApplicationContext(), "修改失败！",
								Toast.LENGTH_SHORT).show();
					}
					@Override
					public void onSuccess(ResponseInfo<String> res) {
						if (!(res.result).equals("0")) {
							handler.sendEmptyMessageDelayed(1, 0);
						} else {
							Toast.makeText(getApplicationContext(), "修改失败！",
									Toast.LENGTH_SHORT).show();
						}
					}
				});
	}
	
	Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			Intent intent = new Intent();
			switch (msg.what) {
			case 1:
				String str="修改成功，请重新登录！";

				finish();
				break;

			case 2:
				mGetCode.setTextColor(getResources().getColor(R.color.gray));
				isClick = false;
				break;
			case 3:
				mCodeText.setText(n + "");
				mCodeText.setVisibility(View.VISIBLE);

				break;
			case 4:
				mGetCode.setTextColor(getResources().getColor(
						R.color.main_title_color));
				if(mTimer!=null){
				mTimer.cancel();
				mTimer.purge();
				}
				mTimer = null;
				isClick = true;
				n = 60;
				mCodeText.setVisibility(View.GONE);
				break;
			case 5:
				Toast.makeText(getApplicationContext(), "验证码发送失败！", Toast.LENGTH_SHORT).show();
				break;
			}
		};
	};

	// 验证密码
	public static boolean isPwd(String str) {
		String regex = "[0-9A-Za-z]*";
		return match(regex, str);
	}

	private static boolean match(String regex, String str) {
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(str);
		return matcher.matches();
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
