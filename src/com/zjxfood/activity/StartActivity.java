package com.zjxfood.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

import com.umeng.analytics.AnalyticsConfig;
import com.umeng.analytics.MobclickAgent;
import com.umeng.message.PushAgent;
import com.zjxfood.application.ExitApplication;

import java.util.Timer;
import java.util.TimerTask;

//导航界面
public class StartActivity extends AppActivity {

	private Timer mTimer;
	private SharedPreferences mPreferences;
	private Editor editor;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
//		MobclickAgent.setDebugMode( true );
		MobclickAgent.openActivityDurationTrack(false);
		MobclickAgent.updateOnlineConfig(StartActivity.this);
		//消息推送
		PushAgent mPushAgent = PushAgent.getInstance(this);
		mPushAgent.enable();
		
		AnalyticsConfig.enableEncrypt(true);
		setContentView(R.layout.start_log_layout);
		ExitApplication.getInstance().addActivity(this);
		mPreferences = getSharedPreferences("app_log", Context.MODE_PRIVATE);
		mTimer = new Timer();

		if (!(isNetworkConnected(getApplicationContext()))
				&& !(isWifiConnected(getApplicationContext()))) {
			Toast.makeText(getApplicationContext(), "网络异常，请检查网络！",
					Toast.LENGTH_SHORT).show();
//			handler.sendEmptyMessageDelayed(2, 1500);
		} else {
			//
		}
		mTimer.schedule(task, 0);
		
	}

	TimerTask task = new TimerTask() {
		@Override
		public void run() {
			handler.sendEmptyMessageDelayed(1, 1000);
		}
	};

	Handler handler = new Handler() {
		Intent intent = new Intent();
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1:
				intent.setClass(getApplicationContext(),
						GuideActivity.class);
				startActivity(intent);
//				if (mPreferences != null) {
//					if (mPreferences.getString("flag", null)==null) {
//						Log.i("mPreferences", "============true=======");
//						editor = mPreferences.edit();
//						// 将登录标志位设置为false，下次登录时不在显示首次登录界面
//						editor.putString("flag", "false");
//						editor.commit();
//						intent.setClass(getApplicationContext(),
//								GuideActivity.class);
//						startActivity(intent);
//					} else {
//						Log.i("mPreferences", "============false=======");
//						intent.setClass(getApplicationContext(),
//								NewMainActivity.class);
//						startActivity(intent);
//					}
//				}
				finish();
				break;

			case 2:
				ExitApplication.getInstance().exit();
				break;
			}
		};
	};

	/**
	 * 判断网络是否打开
	 * 
	 * @param context
	 * @return
	 */
	public boolean isNetworkConnected(Context context) {
		if (context != null) {
			ConnectivityManager mConnectivityManager = (ConnectivityManager) context
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo mNetworkInfo = mConnectivityManager
					.getActiveNetworkInfo();
			if (mNetworkInfo != null) {
				return mNetworkInfo.isAvailable();
			}
		}
		return false;
	}

	/**
	 * 判断wifi是否可用
	 * 
	 * @param context
	 * @return
	 */
	public boolean isWifiConnected(Context context) {
		if (context != null) {
			ConnectivityManager mConnectivityManager = (ConnectivityManager) context
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo mWiFiNetworkInfo = mConnectivityManager
					.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
			if (mWiFiNetworkInfo != null) {
				return mWiFiNetworkInfo.isAvailable();
			}
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
