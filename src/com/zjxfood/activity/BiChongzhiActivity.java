package com.zjxfood.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.project.util.DensityUtils;
import com.umeng.analytics.MobclickAgent;
import com.zjxfood.common.Constants;
import com.zjxfood.http.XutilsUtils;

import java.util.HashMap;

/*
 * 现金购物卷充值
 */
public class BiChongzhiActivity extends AppActivity implements OnClickListener {

	private EditText mBiCodeEdit;// 充值码
	private Button mChongzhiBtn;
	private PopupWindow mPopupWindow;// 弹窗
	private LinearLayout mAlertLayout;
	private ImageView mXImage;
	private Button mBiBtn;
	private ImageView mBackImage;
	private int num = 0;
	private TextView mBiText;
	private EditText mCodePwd;// 充值密码
	private TextView mShowText;
	private HashMap<String,Object> mMaps;
	private TextView mTitleText;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.restaurant_bi_chongzhi_layout);
		setImmerseLayout(findViewById(R.id.head_layout));
		init();
		SharedPreferences sp = getApplicationContext()
				.getSharedPreferences("现金购物卷充值", MODE_PRIVATE);
		if(sp!=null && sp.getString("Content", "")!=null && !sp.getString("Content", "").toString().equals("")){
			mShowText.setText(sp.getString("Content", ""));
		}
	}

	private void init() {
		mBiCodeEdit = (EditText) findViewById(R.id.bi_code_edit);
		mChongzhiBtn = (Button) findViewById(R.id.bi_chongzhi_btn);
		mAlertLayout = (LinearLayout) findViewById(R.id.bi_chongzhi_alert_view);
//		mBackImage = (ImageView) findViewById(R.id.restaurant_bi_chongzhi_back_info_image);
		mCodePwd = (EditText) findViewById(R.id.bi_code_pwd_edit);
		mShowText = (TextView) findViewById(R.id.tips_text);
		mBackImage = (ImageView) findViewById(R.id.title_back_image);
		mTitleText = (TextView) findViewById(R.id.title_text);
		mTitleText.setText("食尚币充值");


		mChongzhiBtn.setOnClickListener(this);
		mBackImage.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.bi_chongzhi_btn:
			if (!(mBiCodeEdit.getText().toString().equals(""))) {
				if (!(mCodePwd.getText().toString().equals(""))) {
//					new Thread(runnable).start();
					getBalance();
				} else {
					Toast.makeText(getApplicationContext(), "充值密码不能为空！",
							Toast.LENGTH_SHORT).show();
				}
			} else {
				Toast.makeText(getApplicationContext(), "充值码不能为空！",
						Toast.LENGTH_SHORT).show();
			}
			break;

		case R.id.title_back_image:
			finish();
			break;
		}
	}

	OnClickListener mClickListener = new OnClickListener() {

		@Override
		public void onClick(View arg0) {
			mPopupWindow.dismiss();
			mAlertLayout.setVisibility(View.GONE);
		}
	};
	// 跳转到 我的现金购物卷 界面
	OnClickListener mBtnClick = new OnClickListener() {

		@Override
		public void onClick(View arg0) {
			mPopupWindow.dismiss();
			mAlertLayout.setVisibility(View.GONE);
			Intent intent = new Intent();
			intent.setClass(getApplicationContext(),
					MyRestaurantBiActivity.class);
			startActivity(intent);
			finish();
		}
	};

//	Runnable runnable = new Runnable() {
//		@Override
//		public void run() {
//			String str = "userId=" + Constants.mId + "&cardid="
//					+ mBiCodeEdit.getText().toString() + "&cardpass="
//					+ mCodePwd.getText().toString();
//			try {
//				String res = ReadJson.getJson(Constants.chongzhiBi2, str);
//				Log.i("res", res + "");
//				mMaps = Constants.getJsonObject(res.toString());
//				if(mMaps!=null && mMaps.size()>0){
//					if(mMaps.get("Message")!=null){
//						handler.sendEmptyMessageDelayed(5, 0);
//					}
//				}
//			} catch (IOException e) {
//				e.printStackTrace();
//			}
//		}
//	};
	//充值
	private void getBalance() {
		StringBuilder sb = new StringBuilder();
		sb.append("userId=" + Constants.mId + "&cardid=" + mBiCodeEdit.getText() + "&cardpass=" + mCodePwd.getText());
		XutilsUtils.get(Constants.chongzhiBi2, sb,
				new RequestCallBack<String>() {
					@Override
					public void onFailure(HttpException arg0, String arg1) {
					}
					@Override
					public void onSuccess(ResponseInfo<String> res) {
						Log.i("充值", res.result + "================");
						mMaps = Constants.getJsonObject(res.result);
						if(mMaps!=null && mMaps.size()>0){
							if(mMaps.get("Message")!=null){
								handler.sendEmptyMessageDelayed(5, 0);
							}
						}
					}
				});
	}
	Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 1:
				mAlertLayout.setVisibility(View.VISIBLE);
				LayoutInflater inflater = LayoutInflater
						.from(getApplicationContext());
				View view = inflater.inflate(
						R.layout.popup_bi_chongzhi_success, null);
				mXImage = (ImageView) view
						.findViewById(R.id.popup_bi_chongzhi_x_image);

				mBiBtn = (Button) view
						.findViewById(R.id.balance_bi_chongzhi_btn);
				mBiText = (TextView) view
						.findViewById(R.id.popup_balance_pay_money_text);
				mPopupWindow = new PopupWindow(view, DensityUtils.dp2px(
						getApplicationContext(), 250),
						LayoutParams.WRAP_CONTENT, false);
				mPopupWindow.showAtLocation(mChongzhiBtn, Gravity.CENTER, 0, 0);
				mPopupWindow.setOutsideTouchable(true);
				mPopupWindow.setFocusable(true);

				mPopupWindow.setBackgroundDrawable(new BitmapDrawable());

				mXImage.setOnClickListener(mClickListener);
				mBiBtn.setOnClickListener(mBtnClick);
				if(Constants.mShMoney!=null && !Constants.mShMoney.equals("")){
				Constants.mShMoney = Integer.parseInt(Constants.mShMoney) + num
						+ "";
				}
				mBiText.setText(num + "现金购物卷");
				break;

			case 2:
				Toast.makeText(getApplicationContext(), "最近一个月内已充值过！",
						Toast.LENGTH_SHORT).show();
				break;
			case 3:
				Toast.makeText(getApplicationContext(), "没有该充值卡！",
						Toast.LENGTH_SHORT).show();
				break;
			case 4:
				Toast.makeText(getApplicationContext(), "充值失败！",
						Toast.LENGTH_SHORT).show();
				break;
				case 5:
					Toast.makeText(getApplicationContext(), mMaps.get("Message").toString(),
							Toast.LENGTH_SHORT).show();
					break;
			}
		};
	};

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		System.gc();
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
