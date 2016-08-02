package com.zjxfood.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.lidroid.xutils.BitmapUtils;
import com.project.util.BitmapUtilSingle;
import com.umeng.analytics.MobclickAgent;
import com.zjxfood.application.ExitApplication;
import com.zjxfood.common.Constants;
import com.zjxfood.update.ParseXmlService;
import com.zjxfood.update.UpdateVersionService;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;

/**
 * 设置
 * 
 * @author zjx
 * 
 */
public class MySettingActivity extends AppActivity implements OnClickListener {

	private RelativeLayout mHeadLayout;
	private ImageView mBackImage;
	private TextView mTelText;
	private RelativeLayout mClearCache;
	private Button mUpdateBtn;
	private RelativeLayout mAgreement;// 协议说明
	private RelativeLayout mGuanyuwmmen;// 关于我们
	private TextView mVersionText;// 当前版本
	private String version;
	private HashMap<String, String> hashMap;// 存储更新版本的xml信息
	private UpdateVersionService updateVersionService;
	private boolean isUpdate = false;
	private RelativeLayout mXyLayout,mWoLayout,mClearLayout,mCallLayout;
	public static String isAlertFlag = "0";
	private BitmapUtils mBitmapUtils;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.my_setting_info_layout);
		setImmerseLayout(findViewById(R.id.head_layout));
		mBitmapUtils = BitmapUtilSingle.getBitmapInstance(getApplicationContext());
		ExitApplication.getInstance().addActivity(this);
		ExitApplication.getInstance().addMyAccountActivity(this);
		init();
		// 获取packagemanager的实例
		PackageManager packageManager = getPackageManager();
		// getPackageName()是你当前类的包名，0代表是获取版本信息
		PackageInfo packInfo;
		try {
			packInfo = packageManager.getPackageInfo(getPackageName(), 0);
			version = packInfo.versionName;
			
			// Log.i("version", version + "=======version==========");
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		mVersionText.setText(version + "");
	}

	private void init() {
		mHeadLayout = (RelativeLayout) findViewById(R.id.my_setting_title_id);
		mBackImage = (ImageView) mHeadLayout
				.findViewById(R.id.my_setting_back_image);
		mTelText = (TextView) findViewById(R.id.setting_phone_code_text);
		mClearCache = (RelativeLayout) findViewById(R.id.my_setting_clear_cache_layout);
		mUpdateBtn = (Button) findViewById(R.id.my_app_checkout_btn);
		mAgreement = (RelativeLayout) findViewById(R.id.my_setting_xieyi_layout);
		mGuanyuwmmen = (RelativeLayout) findViewById(R.id.my_setting_guanyuwomen_layout);
		mVersionText = (TextView) findViewById(R.id.my_app_now_banben_code);
		mXyLayout = (RelativeLayout) findViewById(R.id.my_setting_xieyi_layout);
		mWoLayout = (RelativeLayout) findViewById(R.id.my_setting_guanyuwomen_layout);
		mClearLayout = (RelativeLayout) findViewById(R.id.my_setting_clear_cache_layout);
		mCallLayout = (RelativeLayout) findViewById(R.id.my_setting_phone_layout);

		mXyLayout.setOnClickListener(this);
		mWoLayout.setOnClickListener(this);
		mClearLayout.setOnClickListener(this);
		mCallLayout.setOnClickListener(this);
		
		mBackImage.setOnClickListener(this);
		mUpdateBtn.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		Intent intent = new Intent();
		switch (v.getId()) {
		case R.id.my_setting_back_image:
			finish();
			break;

		case R.id.my_setting_phone_layout:
			intent.setAction("android.intent.action.CALL");
			intent.setData(Uri.parse("tel:" + mTelText.getText().toString()));

			startActivity(intent);
			break;
		case R.id.my_setting_clear_cache_layout:
			SharedPreferences sp = getApplicationContext()
					.getSharedPreferences("MyAccounts", MODE_PRIVATE);
			Editor editor = sp.edit();
			editor.clear();
			editor.commit();
			mBitmapUtils.clearCache();
			mBitmapUtils.clearDiskCache();
			mBitmapUtils.clearMemoryCache();
			Toast.makeText(getApplicationContext(), "清除成功！", Toast.LENGTH_SHORT)
					.show();
			break;
		case R.id.my_app_checkout_btn:
			if (Constants.onLine == 1) {
				isAlertFlag = "1";
				new Thread(startUpdateRun).start();
			} else {
				Toast.makeText(getApplicationContext(), "请登录后操作！",
						Toast.LENGTH_SHORT).show();
			}
			break;
		case R.id.my_setting_xieyi_layout:
			intent.setClass(getApplicationContext(), XieyiActivity.class);
			startActivity(intent);
			break;
		case R.id.my_setting_guanyuwomen_layout:
			intent.setClass(getApplicationContext(), AboutWeActivity.class);
			startActivity(intent);
			break;
		}
	}

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

	Runnable updateRun = new Runnable() {
		@Override
		public void run() {
			if (isUpdate()) {
				handler.sendEmptyMessageDelayed(1, 0);
			}
		}
	};
	Runnable startUpdateRun = new Runnable() {
		@Override
		public void run() {
			Looper.prepare();
			updateVersionService = new UpdateVersionService(
					Constants.UPDATEVERSIONXMLPATH, MySettingActivity.this);// 创建更新业务对象
			updateVersionService.checkUpdate();// 调用检查更新的方法,如果可以更新.就更新.不能更新就提示已经是最新的版本了
			Looper.loop();
		}
	};

	Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 1:
				new Thread(startUpdateRun).start();
				break;

			default:
				break;
			}
		};
	};

	private boolean isUpdate() {
		int versionCode = Integer.parseInt(version);
		Log.i("version", "=============1========" + versionCode);
		try {
			// 把version.xml放到网络上，然后获取文件信息
			URL url = new URL(Constants.UPDATEVERSIONXMLPATH);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setReadTimeout(5 * 1000);
			conn.setRequestMethod("GET");// 必须要大写
			InputStream inputStream = conn.getInputStream();
			// 解析XML文件。
			ParseXmlService service = new ParseXmlService();
			hashMap = service.parseXml(inputStream);
			Log.i("version",
					"=========2============"
							+ Integer.valueOf(hashMap.get("version")));
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (null != hashMap) {
			int serverCode = Integer.valueOf(hashMap.get("version"));
			Log.i("version", serverCode + "=====================" + versionCode);
			// 版本判断
			if (serverCode != versionCode) {
				// Toast.makeText(context, "新版本是: " + serverCode,
				// Toast.LENGTH_SHORT).show();
				return true;
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
