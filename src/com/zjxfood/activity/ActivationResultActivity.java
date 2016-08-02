package com.zjxfood.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.TextView;

import com.umeng.analytics.MobclickAgent;
import com.zjxfood.application.ExitApplication;
import com.zjxfood.common.Constants;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Random;
public class ActivationResultActivity extends AppActivity {

	private Bundle mBundle;
	private String mResult;
	private TextView mBcakBtn;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activation_result);
		setImmerseLayout(findViewById(R.id.head_layout));
		ExitApplication.getInstance().addJhActivity(this);
		mBundle = getIntent().getExtras();
		if (mBundle != null) {
			mResult = mBundle.getString("result");
//			zffs = mBundle.getString("zffs");
			if (mResult.equals("success")) {
				Constants.mIsjh = "1";
			}
		}
		
//		mResultText = (TextView) findViewById(R.id.activation_text);
		mBcakBtn = (TextView) findViewById(R.id.activation__back_text);
		mBcakBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent();
				intent.setClass(getApplicationContext(), MyNewActivity.class);
				startActivity(intent);
				ExitApplication.getInstance().finishJh();
			}
		});
	}

	Handler handler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 1:
//				String orderId = getOrderId();
//				String str = "userid=" + Constants.mId
//						+ "&mid=" + mId + "&money="+29.8
//						+ "&payid=" + orderId + "&issuccess=" + 1
//						+ "&zffs="+zffs;
//				try {
//					String res = ReadJson.getJson(Constants.getPay, str);
//					
//					Log.i("weixinpay", res + "=========res==============");
//				} catch (IOException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
				break;

			default:
				break;
			}
		};
	};
	//生成订单
		public String getOrderId() {
			SimpleDateFormat format = new SimpleDateFormat("MMddHHmmss",
					Locale.getDefault());
			Date date = new Date();
			String key = format.format(date);
			Random r = new Random();
			key = key + r.nextInt();
			key = key.substring(0, 15);
			return key+"_"+Constants.mId;
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
