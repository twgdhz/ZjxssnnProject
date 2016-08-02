package com.zjxfood.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.project.util.DensityUtils;
import com.umeng.analytics.MobclickAgent;

import java.util.Timer;
import java.util.TimerTask;

/**
 * 余额支付
 * @author zjx
 *
 */
public class BalancePayActivity extends AppActivity implements OnClickListener {

	private TextView mBalanceRebate;
	private String source = "<font color='#646464'>本次返利</font><font color='red'>52</font><font color='#646464'>元</font>";
	private Button mBalanceBtn;
	private PopupWindow mPopupWindow;
	private LinearLayout mAlertLayout;
	private ImageView mCancelX;
	private Button mGrabRedBtn;
	private RelativeLayout mHeadLayout;
	private ImageView mBackImage;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.balance_pay_layout);
		setImmerseLayout(findViewById(R.id.head_layout));
		init();
	}

	private void init() {
		mBalanceRebate = (TextView) findViewById(R.id.my_balance_money_rebate_text);
		mBalanceBtn = (Button) findViewById(R.id.balance_pay_btn);
		mAlertLayout = (LinearLayout) findViewById(R.id.balance_pay_alert_view);
		mHeadLayout = (RelativeLayout) findViewById(R.id.title_balance_pay_id);
		mBackImage = (ImageView) mHeadLayout
				.findViewById(R.id.balance_pay_info_image);

		mBalanceRebate.setText(Html.fromHtml(source));
		mBalanceBtn.setOnClickListener(this);
		mBackImage.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.balance_pay_btn:
			LayoutInflater inflater = LayoutInflater
					.from(getApplicationContext());
			// View view = inflater.inflate(R.layout.popup_balance_pay_success,
			// null);
			// mCancelX = (ImageView)
			// view.findViewById(R.id.popup_balance_pay_x_image);
			// mGrabRedBtn = (Button)
			// view.findViewById(R.id.balance_pay_grab_red_packets_btn);
			//
			// mPopupWindow = new PopupWindow(view, DensityUtils.dp2px(
			// getApplicationContext(), 250), LayoutParams.WRAP_CONTENT, false);
			//
			// mPopupWindow.setBackgroundDrawable(new BitmapDrawable());
			// mPopupWindow.showAtLocation(mBalanceBtn, Gravity.CENTER, 0,
			// DensityUtils.dp2px(getApplicationContext(), 68));
			// mAlertLayout.setVisibility(View.VISIBLE);
			//
			// mCancelX.setOnClickListener(mClickListener);
			// mGrabRedBtn.setOnClickListener(mGrabRedClick);
			openKeyboard();
			View view = inflater.inflate(R.layout.balance_pay_pwd_layout, null);

			mPopupWindow = new PopupWindow(view, DensityUtils.dp2px(
					getApplicationContext(), 250), LayoutParams.WRAP_CONTENT,
					false);
			mPopupWindow.setBackgroundDrawable(new BitmapDrawable());
			mPopupWindow.setFocusable(true);
			mPopupWindow.showAtLocation(mBalanceBtn, Gravity.CENTER, 0,
					DensityUtils.dp2px(getApplicationContext(), 20));
			mPopupWindow
					.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
			mAlertLayout.setVisibility(View.VISIBLE);
			break;

		case R.id.balance_pay_info_image:
			finish();
			break;
		}
	}

	// 抢红包
	android.view.View.OnClickListener mGrabRedClick = new OnClickListener() {

		@Override
		public void onClick(View arg0) {
			Intent intent = new Intent();
			intent.setClass(getApplicationContext(), GrabRedActivity.class);
			startActivity(intent);
			mPopupWindow.dismiss();
			handler.sendEmptyMessageDelayed(1, 0);
		}
	};

	OnClickListener mClickListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			mPopupWindow.dismiss();
			handler.sendEmptyMessageDelayed(1, 0);
		}
	};

	public boolean onKeyDown(int keyCode, KeyEvent event) {
		switch (keyCode) {
		case KeyEvent.KEYCODE_BACK:
			finish();
			break;

		default:
			break;
		}
		return false;

	};

	Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 1:
				mAlertLayout.setVisibility(View.GONE);
				break;

			default:
				break;
			}
		};
	};

	private void openKeyboard() {

		Timer timer = new Timer();
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
				imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);

			}
		}, 1000);
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
