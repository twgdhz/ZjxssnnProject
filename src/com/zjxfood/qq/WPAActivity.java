package com.zjxfood.qq;

import com.zjxfood.activity.MallDetailActivity;
import com.zjxfood.activity.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

public class WPAActivity extends BaseActivity implements OnClickListener {
	EditText uinText;
	private Bundle mBundle;
	private String mQQ = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setBarTitle("临时会话");
		setLeftButtonEnable();
		setContentView(R.layout.wpa_activity_layout);
		mBundle = getIntent().getExtras();
		if (mBundle != null) {
			mQQ = mBundle.getString("qq");
		}
		LinearLayout linearLayout = (LinearLayout) findViewById(R.id.main_container);
		for (int i = 0; i < linearLayout.getChildCount(); i++) {
			View view = linearLayout.getChildAt(i);
			if (view instanceof Button) {
				view.setOnClickListener(this);
			}
		}
		uinText = new EditText(this);
		checkTencentInstance();
		onClickStartWPA(mQQ);
	}

	/**
	 * 异步显示结果
	 */
	private Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			if (msg.what == 0) {
				Context context = WPAActivity.this;
				String response = msg.getData().getString("response");
				if (response != null) {
					// 换行显示
					response = response.replace(",", "\r\n");
					AlertDialog dialog = new AlertDialog.Builder(context)
							.setMessage(response)
							.setNegativeButton("知道啦", null).create();
					dialog.show();
				}
			}
		};
	};

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		// case R.id.wpa_state_btn:
		// break;
		case R.id.start_wpa_btn:
			onClickStartWPA(mQQ);
			break;
		// case R.id.start_qq_group_btn:
		// break;
		}
	}

	/**
	 * 发起wpa会话
	 */
	private void onClickStartWPA(String qq) {
		String uin = qq;
		if (!"".equals(uin)) {
			int ret = MallDetailActivity.mTencent.startWPAConversation(
					WPAActivity.this, uin, "");
			if (ret != 0) {
				Toast.makeText(getApplicationContext(),
						"start WPA conversation failed. error:" + ret,
						Toast.LENGTH_LONG).show();
			}
		}
	}
//	private class WPAApiListener extends BaseUIListener {
//		private String mScope = "all";
//		private Boolean mNeedReAuth = false;
//		private Activity mActivity;
//
//		public WPAApiListener(String scope, boolean needReAuth,
//				Activity activity) {
//			super(activity);
//			this.mScope = scope;
//			this.mNeedReAuth = needReAuth;
//			this.mActivity = activity;
//		}
//
//		@Override
//		public void onComplete(Object response) {
//			Message msg = mHandler.obtainMessage(0);
//			Bundle data = new Bundle();
//			data.putString("response", response.toString());
//			msg.setData(data);
//			mHandler.sendMessage(msg);
//			Util.dismissDialog();
//		}
//	}
}
