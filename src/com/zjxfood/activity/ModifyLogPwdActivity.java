package com.zjxfood.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.lidroid.xutils.BitmapUtils;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.umeng.analytics.MobclickAgent;
import com.zjxfood.application.ExitApplication;
import com.zjxfood.common.Constants;
import com.zjxfood.http.AsyUtils;
import com.zjxfood.view.HeadRoundImageView;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Administrator
 * 输入原密码的方式修改用户密码  不是忘记密码
 */
public class ModifyLogPwdActivity extends AppActivity implements OnClickListener {

	private EditText mPwd;
	private EditText mPwd2;
	private Button mCompleteBtn;
	private RelativeLayout mHeadLayout;
	private ImageView mBackImage;
	private HeadRoundImageView mHeadRoundImageView;
	private EditText mLogPwd;
	private Bitmap mHeadBitmap;
	private BitmapUtils mBitmapUtils;
	private TextView mTitleText;
	private Pattern p = Pattern.compile("\\s*|\t|\r|\n");
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		ExitApplication.getInstance().addActivity(this);
		ExitApplication.getInstance().addMyAccountActivity(this);
		setContentView(R.layout.modify_pwd_layout);
		setImmerseLayout(findViewById(R.id.head_layout));
		mBitmapUtils = new BitmapUtils(getApplicationContext());
		init();
		if (Constants.onLine == 1) {
//			loadHeadImage();
			//加载头像
			new Thread(loadHeadRun).start();
		}
		
	}

	private void init() {
		mPwd = (EditText) findViewById(R.id.modify_log_pwd_password_edit);
		mPwd2 = (EditText) findViewById(R.id.modify_log_pwd_confirm_password_edit);
		mCompleteBtn = (Button) findViewById(R.id.modify_log_pwd_log_submit);
		mHeadLayout = (RelativeLayout) findViewById(R.id.modify_log_pwd_title);
//		mBackImage = (ImageView) mHeadLayout.findViewById(R.id.title_back_image);
		mBackImage = (ImageView) findViewById(R.id.title_back_image);
		mTitleText = (TextView) findViewById(R.id.title_text);
		mTitleText.setText("修改密码");
		mHeadRoundImageView = (HeadRoundImageView) findViewById(R.id.modify_log_pwd_head_touxiang_image);
		mLogPwd = (EditText) findViewById(R.id.modify_log_pwd_input_logpwd_edit);

		mCompleteBtn.setOnClickListener(this);
		mBackImage.setOnClickListener(this);
	}

	private void loadHeadImage() {
		String path = Constants.fileUrl + Constants.mId
				+ "shishangnannvHead.jpg";
		Bitmap bitmap = BitmapFactory.decodeFile(path);
		if (bitmap != null) {
			mHeadRoundImageView.setImageBitmap(bitmap);
		}
	}

	private void isModify() {
		
		Log.i("code", Constants.mPassWord + "====================");
		if (mLogPwd.getText().toString().equals(Constants.mPassWord)
				&& !(mLogPwd.getText().toString().equals(""))) {
			if (isPwd(mPwd.getText().toString())) {
				if (!mPwd.getText().toString().equals(" ")) {
					if (!(mPwd2.getText().toString().equals(""))
							&& mPwd.getText().toString()
									.equals(mPwd2.getText().toString())) {
//						new Thread(modifyRun).start();
						modifyHttp();
					}else{
						Toast.makeText(getApplicationContext(), "两次密码输入不一致！", Toast.LENGTH_SHORT).show();
					}
				}else{
					Toast.makeText(getApplicationContext(), "密码不能为空！", Toast.LENGTH_SHORT).show();
				}
			}else{
				Toast.makeText(getApplicationContext(), "密码格式不正确！", Toast.LENGTH_SHORT).show();
			}
		}else{
			Toast.makeText(getApplicationContext(), "登录密码输入错误！", Toast.LENGTH_SHORT).show();
		}
	}

//	Runnable modifyRun = new Runnable() {
//		@Override
//		public void run() {
//			Looper.prepare();
//			String str = "usid=" + Constants.mId
//					+ "&password=" + p.matcher(mPwd.getText().toString()).replaceAll("");
//			try {
//				String res = ReadJson.getJson(Constants.modifyPwd, str);
//				if (res.equals("1")) {
//					handler.sendEmptyMessageDelayed(1, 0);
//				} else {
//					Toast.makeText(getApplicationContext(), "修改失败！",
//							Toast.LENGTH_SHORT).show();
//				}
//				Looper.loop();
//				Log.i("res", res + "======================");
//			} catch (IOException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//		}
//	};
	private void modifyHttp(){
		RequestParams params = new RequestParams();
		params.put("usid", Constants.mId);
		params.put("password",  p.matcher(mPwd.getText().toString()).replaceAll(""));
		AsyUtils.get(Constants.modifyPwd2, params, new AsyncHttpResponseHandler(){
			@Override
			@Deprecated
			public void onSuccess(int statusCode, String content) {
				if (content.equals("1")) {
					handler.sendEmptyMessageDelayed(1, 0);
				} else {
					Toast.makeText(getApplicationContext(), "修改失败！",
							Toast.LENGTH_SHORT).show();
				}
				super.onSuccess(statusCode, content);
			}
			@Override
			@Deprecated
			public void onFailure(Throwable error, String content) {
				Toast.makeText(getApplicationContext(), "修改失败！",
						Toast.LENGTH_SHORT).show();
				super.onFailure(error, content);
			}
		});
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.modify_log_pwd_log_submit:
			Log.i("code", "=========修改密码0=============");
			isModify();
			break;

		case R.id.title_back_image:
			finish();
			break;
		}
	}

	Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			Intent intent = new Intent();
			switch (msg.what) {
			case 0:

				break;
			case 1:
				Toast.makeText(getApplicationContext(), "修改成功！",
						Toast.LENGTH_SHORT).show();
				Constants.onLine = 0;
				Constants.mId = "";
				Constants.mPassWord = "";
				Constants.mUserName = "";
				intent.setClass(getApplicationContext(),
						MyUserLogActivity.class);
				startActivity(intent);
				ExitApplication.getInstance().finishMyAccount();
				break;
			case 2:
//				mHeadRoundImageView.setImageBitmap(mHeadBitmap);
				mBitmapUtils.display(mHeadRoundImageView, Constants.headPath );
				break;
			case 3:

				break;
			case 4:

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
	// 上传头像
		Runnable loadHeadRun = new Runnable() {
			@Override
			public void run() {
//				mHeadBitmap = ImageLoader.getInstance().loadImageSync(
//						Constants.loadHeadImage + Constants.mId + ".png");
				handler.sendEmptyMessageDelayed(2, 0);
			}
		};
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
