package com.zjxfood.red;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.umeng.analytics.MobclickAgent;
import com.zjxfood.activity.AppActivity;
import com.zjxfood.activity.R;
import com.zjxfood.activity.RedListActivity;
import com.zjxfood.application.ExitApplication;
import com.zjxfood.common.Constants;
import com.zjxfood.http.XutilsUtils;

import java.util.HashMap;

/**
 * 发红包
 * 
 * @author zjx
 * 
 */
public class RedAwardActivity extends AppActivity implements OnClickListener {
	private ImageView mBackImage;
	private TextView mMyRedText;// 我的红包
	private EditText mRedNumbers, mRedPrice, mRedKm, mRedMsg;
	private Button mButton;
	private TextView mPriceText;
	private HashMap<String, Object> mRedMap;
	private RelativeLayout mAlertLayout;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		ExitApplication.getInstance().addActivity(this);
		setContentView(R.layout.red_award_layout);
		setImmerseLayout(findViewById(R.id.head_layout));
		init();
	}

	private void init() {
		mBackImage = (ImageView) findViewById(R.id.red_award_back_info_image);
		mMyRedText = (TextView) findViewById(R.id.my_red_award_text);
		mRedNumbers = (EditText) findViewById(R.id.red_award_numbers_edit);
		mRedPrice = (EditText) findViewById(R.id.red_award_price_edit);
		mRedKm = (EditText) findViewById(R.id.red_award_km_edit);
		mRedMsg = (EditText) findViewById(R.id.red_award_message_text2);
		mButton = (Button) findViewById(R.id.red_award_button);
		mPriceText = (TextView) findViewById(R.id.red_award_all_price_text);
		mAlertLayout = (RelativeLayout) findViewById(R.id.red_award_alert_layout);

		mBackImage.setOnClickListener(this);
		mButton.setOnClickListener(this);
		mMyRedText.setOnClickListener(this);
		mRedPrice.addTextChangedListener(mWatcher);
		mRedNumbers.addTextChangedListener(mWatcher);
	}

	TextWatcher mWatcher = new TextWatcher() {
		@Override
		public void onTextChanged(CharSequence arg0, int arg1, int arg2,
				int arg3) {
			if (!mRedPrice.getText().toString().equals("")) {
				mPriceText.setText("￥"+mRedPrice.getText().toString());
			}
		}

		@Override
		public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
				int arg3) {
		}

		@Override
		public void afterTextChanged(Editable arg0) {
		}
	};

	@Override
	public void onClick(View v) {
		Intent intent = new Intent();
		switch (v.getId()) {
		//我的红包
		case R.id.my_red_award_text:
			if (Constants.onLine == 1 && !(Constants.mId.equals(""))) {
			intent.setClass(getApplicationContext(), RedIssuedActivity.class);
			startActivity(intent);
			}else{
				Toast.makeText(getApplicationContext(), "请先登录帐号", Toast.LENGTH_SHORT).show();
			}
			break;
		case R.id.red_award_back_info_image:
			finish();
			break;

		case R.id.red_award_button:
			if (!mRedNumbers.getText().toString().equals("")) {
				if (!mRedPrice.getText().toString().equals("")) {
//					if (!mRedKm.getText().toString().equals("")) {
						mAlertLayout.setVisibility(View.VISIBLE);
//						if(Constants.longt>0 && Constants.lat>0){
							issueRed();
//						}else{
//							Toast.makeText(getApplicationContext(), "获取定位坐标失败", Toast.LENGTH_SHORT).show();
//						}
						
//					} else {
//						Toast.makeText(getApplicationContext(), "请输入红包领取范围",
//								Toast.LENGTH_SHORT).show();
//					}
				} else {
					Toast.makeText(getApplicationContext(), "请输入红包金额",
							Toast.LENGTH_SHORT).show();
				}
			} else {
				Toast.makeText(getApplicationContext(), "请输入红包个数",
						Toast.LENGTH_SHORT).show();
			}
			break;
		}
	}

	private void issueRed() {
		StringBuilder sb = new StringBuilder();
		if(mRedMsg.getText().toString().equals("")){
			mRedMsg.setText("恭喜发财，大吉大利！");
		}
		sb.append("userId=" + Constants.mId + "&x=" + 0
				+ "&y=" + 0 + "&money="
				+ mRedPrice.getText().toString() + "&totalNum="
				+ mRedNumbers.getText().toString() + "&distance=2001" + "&memo="
				+ mRedMsg.getText().toString());
		XutilsUtils.get(Constants.issueRed, sb, new RequestCallBack<String>() {
			@Override
			public void onFailure(HttpException arg0, String arg1) {
				mAlertLayout.setVisibility(View.GONE);
			}

			@Override
			public void onSuccess(ResponseInfo<String> res) {
				mAlertLayout.setVisibility(View.GONE);
				mRedMap = Constants.getJsonObject(res.result);
				if (mRedMap != null && mRedMap.get("Code") != null && mRedMap.get("Message")!=null) {
					Toast.makeText(getApplicationContext(),
							mRedMap.get("Message").toString(),
							Toast.LENGTH_SHORT).show();
					if(mRedMap.get("Code").toString().equals("200")){
						handler.sendEmptyMessageDelayed(1, 0);
					}
				}
				Log.i("消息", res.result + "=====================");
			}
		});
	}
	
	Handler handler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			Intent intent = new Intent();
			switch (msg.what) {
			case 1:
				intent.setClass(getApplicationContext(), RedListActivity.class);
				startActivity(intent);
				finish();
				break;

			default:
				break;
			}
		};
	};
	public void onResume() {
		super.onResume();
		MobclickAgent.onResume(this);
	}
	public void onPause() {
		super.onPause();
		MobclickAgent.onPause(this);
	}
}
