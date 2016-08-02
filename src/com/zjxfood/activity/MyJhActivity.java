package com.zjxfood.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.zjxfood.common.Constants;
import com.zjxfood.http.XutilsUtils;

public class MyJhActivity extends AppActivity implements OnClickListener {

	private ImageView mBackImage;
	private Button mButton;
	private String mJhMoney = "";
	private TextView mShowText;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.my_jh_layout);
		setImmerseLayout(findViewById(R.id.head_layout));
		init();
		
		SharedPreferences sp = getApplicationContext()
				.getSharedPreferences("激活说明", MODE_PRIVATE);
		if(sp!=null && sp.getString("Content", "")!=null && !sp.getString("Content", "").toString().equals("")){
			mShowText.setText(sp.getString("Content", ""));
		}
	}

	private void init() {
		mBackImage = (ImageView) findViewById(R.id.jh_back_info_image);
		mButton = (Button) findViewById(R.id.my_jh_btn);
		mShowText = (TextView) findViewById(R.id.my_jh_value);

		mBackImage.setOnClickListener(this);
		mButton.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.jh_back_info_image:
			finish();
			break;

		case R.id.my_jh_btn:
			if (Constants.onLine == 1) {
				getJhDate();
			} else {
				Toast.makeText(getApplicationContext(), "请登录后在操作！",
						Toast.LENGTH_SHORT).show();
			}
			break;
		}
	}

	// 获取激活时间
//	private void getJhDate() {
//		RequestParams params = new RequestParams();
//		params.put("uid", Constants.mId);
//		AsyUtils.get(Constants.getJhDate2, params,
//				new AsyncHttpResponseHandler() {
//					@Override
//					@Deprecated
//					public void onSuccess(String content) {
//						if (content.equals("0")) {
//							Constants.mIsjh = 0 + "";
//						} else {
//							Constants.mIsjh = 1 + "";
//						}
//						handler.sendEmptyMessageDelayed(3, 0);
//						super.onSuccess(content);
//					}
//				});
//	}
	private void getJhDate() {
		StringBuilder sb = new StringBuilder();
			sb.append("uid=" + Constants.mId);
		XutilsUtils.get(Constants.getJhDate2, sb,
				new RequestCallBack<String>() {
					@Override
					public void onFailure(HttpException arg0, String arg1) {
					}
					@Override
					public void onSuccess(ResponseInfo<String> res) {
						Log.i("getJhDate2", res.result+"============="+res.reasonPhrase);
						if ((res.result).equals("0")) {
							Constants.mIsjh = 0 + "";
						} else {
							Constants.mIsjh = 1 + "";
						}
						handler.sendEmptyMessageDelayed(3, 0);
					}
				});
	}

	// 获取激活金额
//	private void getJhMoney() {
//		RequestParams params = new RequestParams();
//		params.put("uid", Constants.mId);
//		AsyUtils.get(Constants.getJhMoney2, params,
//				new AsyncHttpResponseHandler() {
//					@Override
//					@Deprecated
//					public void onSuccess(String content) {
//						if (content.length() > 10) {
//							handler.sendEmptyMessageDelayed(2, 0);
//						} else {
//							mJhMoney = content;
//							handler.sendEmptyMessageDelayed(1, 0);
//						}
//						super.onSuccess(content);
//					}
//				});
//	}
	private void getJhMoney() {
		StringBuilder sb = new StringBuilder();
			sb.append("uid=" + Constants.mId);
		XutilsUtils.get(Constants.getJhMoney2, sb,
				new RequestCallBack<String>() {
					@Override
					public void onFailure(HttpException arg0, String arg1) {
					}
					@Override
					public void onSuccess(ResponseInfo<String> res) {
						if ((res.result).length() > 10) {
							handler.sendEmptyMessageDelayed(2, 0);
						} else {
							mJhMoney = res.result;
							handler.sendEmptyMessageDelayed(1, 0);
						}
					}
				});
	}

	Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 1:
				// 跳转到激活页面
				Intent intent = new Intent();
				Bundle bundle = new Bundle();
				// 跳转到支付方式选择页面
				bundle.putString("price", mJhMoney);
//				 bundle.putString("price", "0.01");
				bundle.putString("merchantName", "食尚男女");
				bundle.putString("mId", Constants.mId);
				bundle.putString("LoginImage", "");
				bundle.putString("flum", "");
				bundle.putString("type", "");
				intent.putExtras(bundle);
				intent.setClass(getApplicationContext(), JHPayWayActivity.class);
				startActivity(intent);
				break;

			case 2:
				
				break;
			case 3:
				Log.i("激活状态", Constants.mIsjh);
				if (Constants.mIsjh.equals("1")) {
					Toast.makeText(getApplicationContext(), "你的账户已经激活",
							Toast.LENGTH_SHORT).show();
				} else {
					if (!(Constants.mId.equals(""))) {
						getJhMoney();
					} else {
						Toast.makeText(getApplicationContext(), "帐号异常，请重新登录！",
								Toast.LENGTH_SHORT).show();
					}
				}
				break;
			}
		};
	};
}
